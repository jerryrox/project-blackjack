/*
 * Jerry Kim (18015036), 2019
 */
package game.database;

import game.debug.ILogger;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Manages the connection state to a database.
 * @author jerrykim
 */
public class DatabaseConnection {
    
    private String databaseName;
    private Connection connection;
    private ILogger logger;
    
    private boolean isConnected = false;
    private boolean isDisposed = false;
    
    
    public DatabaseConnection(String databaseName, ILogger logger)
    {
        // Enforce the suffix DB.
        if(databaseName == null)
            databaseName = "DefaultDB";
        else if(!databaseName.toLowerCase().endsWith("db"))
            databaseName += "DB";
        
        this.databaseName = databaseName;
        this.logger = logger;
    }
    
    /**
     * Connects to the database and returns whether it's a success.
     * If already connect, this method will return true.
     */
    public boolean Connect()
    {
        if(isDisposed)
        {
            LogDisposed("Connect");
            return false;
        }
        else if(isConnected)
            return true;
        try
        {
            connection = DriverManager.getConnection(GetConnectionString(), "jerryrox", "jerryrox");
            isConnected = connection != null && !connection.isClosed();
            return isConnected;
        }
        catch(SQLException e)
        {
            if(logger != null)
            {
                logger.LogErrorFormat(
                    "DatabaseConnection.Connect - Failed to connect to database using given connection string (%s).\n%s",
                    GetConnectionString(),
                    e.getMessage()
                );
            }
        }
        return false;
    }
    
    /**
     * Returns whether the connection to database is alive.
     */
    public boolean IsConnected() { return !isDisposed && isConnected; }
    
    /**
     * Disposes the connection and this object.
     */
    public void Dispose()
    {
        if(isDisposed)
        {
            LogDisposed("Dispose");
            return;
        }
        isDisposed = true;
        
        try
        {
            if(isConnected)
                connection.close();
        }
        catch(SQLException e)
        {
            if(logger != null)
            {
                logger.LogErrorFormat(
                    "DatabaseConnection.Dispose - Failed to close connection to database: %s",
                    e.getMessage()
                );
            }
        }
        
        databaseName = null;
        connection = null;
        logger = null;
    }
    
    /**
     * Returns the appropriate connection string for db connection.
     */
    public String GetConnectionString()
    {
        if(isDisposed)
        {
            LogDisposed("GetConnectionString");
            return null;
        }
        return String.format("jdbc:derby:%s;create=true", databaseName);
    }
    
    /**
     * Creates a new statement to execute commands on.
     */
    public Statement CreateStatement()
    {
        if(isDisposed)
        {
            LogDisposed("CreateStatement");
            return null;
        }
        else if(!isConnected)
        {
            LogNotConnected("CreateStatement");
            return null;
        }
        try
        {
            return connection.createStatement();
        }
        catch(SQLException e)
        {
            if(logger != null)
            {
                logger.LogErrorFormat(
                    "DatabaseConnection.CreateStatement - Failed to create a new statement: %s",
                    e.getMessage()
                );
            }
        }
        return null;
    }
    
    /**
     * Creates a new prepared statement to execute commands on.
     */
    public PreparedStatement CreatePreparedStatement(String template)
    {
        if(isDisposed)
        {
            LogDisposed("CreatePreparedStatement");
            return null;
        }
        else if(!isConnected)
        {
            LogNotConnected("CreatePreparedStatement");
            return null;
        }
        try
        {
            return connection.prepareStatement(template);
        }
        catch(SQLException e)
        {
            if(logger != null)
            {
                logger.LogErrorFormat(
                    "DatabaseConnection.CreatePreparedStatement - Failed to create a new prepared statement: %s",
                    e.getMessage()
                );
            }
        }
        return null;
    }
    
    /**
     * Closes the specified statement.
     */
    public void DisposeStatement(Statement statement)
    {
        try
        {
            if(statement != null)
                statement.close();
        }
        catch(SQLException e)
        {
            if(logger != null)
            {
                logger.LogWarning("DatabaseConnection.DisposeStatement - Failed to dispose statement: " + e.getMessage());
            }
        }
    }
    
    /**
     * Queries data from the database.
     * Returns the result set if successful.
     */
    public ResultSet Query(Statement statement, String command)
    {
        if(isDisposed)
        {
            LogDisposed("Query");
            return null;
        }
        else if(!isConnected)
        {
            LogNotConnected("Query");
            return null;
        }
        try
        {
            if(statement == null || statement.isClosed())
            {
                LogStatementInvalid("Query");
                return null;
            }
            return statement.executeQuery(command);
        }
        catch(SQLException e)
        {
            if(logger != null)
            {
                logger.LogErrorFormat(
                    "DatabaseConnection.Query - Failed to query command (%s): %s",
                    command, e.getMessage()
                );
            }
        }
        return null;
    }
    
    /**
     * Returns the number of results for specified tables and predicate.
     * Returns a negative value if an error occurred.
     */
    public int QueryCount(Statement statement, String tables, String predicate)
    {
        ResultSet result = Query(statement, String.format("select count(*) from %s where %s", tables, predicate));
        if(result == null)
            return -1;
        try
        {
            if(result.next())
                return result.getInt(1);
        }
        catch(SQLException e)
        {
            if(logger != null)
            {
                logger.LogErrorFormat(
                    "DatabaseConnection.QueryCount - Failed to query count for (table: %s, predicate: %s): %s",
                    tables, predicate, e.getMessage()
                );
            }
        }
        finally
        {
            try
            {
                result.close();
            }
            catch(SQLException e) {}
        }
        return -1;
    }
    
    /**
     * Executes the specified commands in a batch.
     * Returns whether it was successful.
     */
    public boolean Execute(Statement statement, Iterable<String> commands)
    {
        if(isDisposed)
        {
            LogDisposed("Execute");
            return false;
        }
        else if(!isConnected)
        {
            LogNotConnected("Execute");
            return false;
        }
        try
        {
            if(statement == null || statement.isClosed())
            {
                LogStatementInvalid("Execute");
                return false;
            }
            else if(commands == null)
            {
                if(logger != null)
                    logger.LogError("DatabaseConnection.Execute - commands is null!");
                return false;
            }
            for(String cmd : commands)
                statement.addBatch(cmd);
            statement.executeBatch();
            return true;
        }
        catch(SQLException e)
        {
            if(logger != null)
            {
                logger.LogError("DatabaseConnection.Execute - Failed to execute commands: " + e.getMessage());
                SQLException nextException = null;
                while((nextException = e.getNextException()) != null)
                    logger.LogError(nextException.getMessage());
            }
        }
        return false;
    }
    
    /**
     * Executes the specified command.
     * Returns whether it was successful.
     */
    public boolean Execute(boolean update, Statement statement, String command)
    {
        if(isDisposed)
        {
            LogDisposed("Execute");
            return false;
        }
        else if(!isConnected)
        {
            LogNotConnected("Execute");
            return false;
        }
        try
        {
            if(statement == null || statement.isClosed())
            {
                LogStatementInvalid("Execute");
                return false;
            }
            if(update)
                return statement.executeUpdate(command) >= 0;
            statement.execute(command);
            return true;
        }
        catch(SQLException e)
        {
            if(logger != null)
            {
                logger.LogError("DatabaseConnection.Execute - Failed to execute commands: " + e.getMessage());
                SQLException nextException = null;
                while((nextException = e.getNextException()) != null)
                    logger.LogError(nextException.getMessage());
            }
        }
        return false;
    }
    
    /**
     * Returns whether the specified table name exists.
     */
    public boolean TableExists(String tableName)
    {
        if(isDisposed)
        {
            LogDisposed("TableExists");
            return false;
        }
        else if(!isConnected)
        {
            LogNotConnected("TableExists");
            return false;
        }
        try
        {
            // Table name must be uppercase, or it'll not work.
            tableName = tableName.toUpperCase();
            
            DatabaseMetaData dbm = connection.getMetaData();
            ResultSet tables = dbm.getTables(null, null, tableName, null);
            boolean exists = tables.next();
            tables.close();
            return exists;
        }
        catch(SQLException e)
        {
            if(logger != null)
            {
                logger.LogErrorFormat(
                    "DatabaseConnection.TableExists - Failed to check whether table (%s) exists: %s",
                    tableName, e.getMessage()
                );
            }
        }
        return false;
    }
    
    /**
     * Returns whether this object has been disposed.
     */
    public boolean IsDisposed() { return isDisposed; }
    
    /**
     * Outputs statement invalid message to logger.
     */
    private void LogStatementInvalid(String methodName)
    {
        if(logger != null)
        {
            logger.LogErrorFormat(
                "DatabaseConnection.%s - statement is null or closed!",
                methodName
            );
        }
    }
    
    /**
     * Outputs object disposed message to logger.
     */
    private void LogDisposed(String methodName)
    {
        if(logger != null)
        {
            logger.LogErrorFormat(
                "DatabaseConnection.%s - This object has been disposed!",
                methodName
            );
        }
    }
    
    /**
     * Outputs connection not established message to logger.
     */
    private void LogNotConnected(String methodName)
    {
        if(logger != null)
        {
            logger.LogErrorFormat(
                "DatabaseConnection.%s - Connection to database is currently not valid!!",
                methodName
            );
        }
    }
}
