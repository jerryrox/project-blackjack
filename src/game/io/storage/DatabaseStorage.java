/*
 * Jerry Kim (18015036), 2019
 */
package game.io.storage;

import game.database.DatabaseConnection;
import game.debug.Debug;
import game.entities.IEntity;
import game.io.IStorage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Data provider using a database.
 * @author jerrykim
 */
public abstract class DatabaseStorage<T extends IEntity> implements IStorage<T> {

    private DatabaseConnection connection;
    private String tableName;
    
    
    public DatabaseStorage(DatabaseConnection connection, String tableName)
    {
        this.connection = connection;
        this.tableName = tableName;
    }
    
    public @Override void Initialize()
    {
        if(connection.Connect())
        {
            // Create the table if not already exists.
            if(!connection.TableExists(tableName))
            {
                connection.Execute(
                    false,
                    connection.CreateStatement(),
                    String.format("create table %s (%s)", tableName, GetTableStructureString())
                );
            }
        }
    }

    public @Override void Dispose()
    {
        if(connection.IsConnected())
            connection.Dispose();
    }
    
    public @Override void Save() {}

    public @Override void Add(T value)
    {
        String existingId = value.GetId();
        if(existingId == null || existingId.length() == 0)
            value.SetId(existingId = UUID.randomUUID().toString());
        
        Set(existingId, value);
    }

    public @Override void Set(String id, T value)
    {
        if(id == null || id.length() == 0)
            return;
        if(value == null)
            Remove(id);
        else
        {
            // Make sure the value's id matches the specified id.
            value.SetId(id);
        
            Statement statement = connection.CreateStatement();
            if(statement == null)
                return;
            // If id already exists, update
            if(ContainsId(id))
            {
                connection.Execute(true, statement, String.format(
                    "update %s set %s where id='%s'",
                    tableName, GetUpdateValueString(value), id
                ));
            }
            // Otherwise, insert
            else
            {
                connection.Execute(false, statement, String.format(
                    "insert into %s values (%s)",
                    tableName, GetInsertValueString(value)
                ));
            }
            connection.DisposeStatement(statement);
        }
    }

    public @Override void Remove(String id)
    {
        if(id == null || id.length() == 0)
            return;
        Statement statement = connection.CreateStatement();
        if(statement == null)
            return;
        // Delete record of id.
        connection.Execute(false, statement, String.format(
            "delete from %s where id='%s'",
            tableName, id
        ));
        connection.DisposeStatement(statement);
    }

    public @Override void Clear()
    {
        Statement statement = connection.CreateStatement();
        if(statement == null)
            return;
        // Clear table.
        connection.Execute(false, statement, String.format(
            "truncate table %s",
            tableName
        ));
        connection.DisposeStatement(statement);
    }

    public @Override T Get(String id)
    {
        if(id == null || id.length() == 0)
            return null;
        Statement statement = connection.CreateStatement();
        if(statement == null)
            return null;
        // Query value of id
        ResultSet result = connection.Query(statement, String.format(
            "select * from %s where id='%s'",
            tableName, id
        ));
        try
        {
            if(result != null && result.next())
                return ParseEntity(result);
        }
        catch(SQLException e)
        {
            Debug.LogErrorFormat(
                "DatabaseStorage.Get - Failed while parsing entity with id (%s): %s",
                id, e.getMessage()
            );
        }
        finally
        {
            connection.DisposeStatement(statement);
        }
        return null;
    }

    public @Override Iterable<String> GetAllId()
    {
        Statement statement = connection.CreateStatement();
        if(statement == null)
            return null;
        // Query all ids
        ResultSet result = connection.Query(statement, String.format(
            "select id from %s",
            tableName
        ));
        try
        {
            if(result != null)
            {
                // Add all ids to list and return it.
                ArrayList<String> ids = new ArrayList<>();
                while(result.next())
                    ids.add(result.getString(1));
                return ids;
            }
        }
        catch(SQLException e)
        {
            Debug.LogErrorFormat(
                "DatabaseStorage.GetAllId - Failed while retrieving ids: %s",
                e.getMessage()
            );
        }
        finally
        {
            connection.DisposeStatement(statement);
        }
        return null;
    }

    public @Override Iterable<T> GetAll()
    {
        Statement statement = connection.CreateStatement();
        if(statement == null)
            return null;
        // Query all values
        ResultSet result = connection.Query(statement, String.format(
            "select * from %s",
            tableName
        ));
        try
        {
            if(result != null)
            {
                // Add values to list and return it.
                ArrayList<T> values = new ArrayList<>();
                while(result.next())
                    values.add(ParseEntity(result));
                return values;
            }
        }
        catch(SQLException e)
        {
            Debug.LogErrorFormat(
                "DatabaseStorage.GetAll - Failed while retrieving values: %s",
                e.getMessage()
            );
        }
        finally
        {
            connection.DisposeStatement(statement);
        }
        return null;
    }

    public @Override boolean ContainsId(String id)
    {
        if(id == null || id.length() == 0)
            return false;
        Statement statement = connection.CreateStatement();
        if(statement == null)
            return false;
        // Check if any exists that matches the id.
        int count = connection.QueryCount(statement, tableName, String.format("id='%s'", id));
        connection.DisposeStatement(statement);
        return count > 0;
    }
    
    /**
     * Returns the string value which defines the column names and data types for
     * current entity T.
     */
    protected abstract String GetTableStructureString();
    
    /**
     * Returns the string value which defines the inserted column values for specified entity T.
     */
    protected abstract String GetInsertValueString(T value);
    
    /**
     * Returns the string value which defines the updated column values for specified entity T.
     */
    protected abstract String GetUpdateValueString(T value);
    
    /**
     * Parses the entity instance from specified result and returns it, it successful.
     */
    protected abstract T ParseEntity(ResultSet result) throws SQLException;
}
