/*
 * Jerry Kim (18015036), 2019
 */
package game.database;

import game.debug.ConsoleLogger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * DatabaseConnection class test.
 * @author jerrykim
 */
public class DatabaseConnectionIT {
    
    private final String DatabaseName = "TesterDatabase";
    
    
    public DatabaseConnectionIT()
    {
    }
    
    /**
     * Testing connection to the database and its disposal.
     */
    @Test
    public void ConnectTest()
    {
        DatabaseConnection con = CreateConnection();
        DisposeConnection(con);
    }
    
    /**
     * Testing statement creation
     */
    @Test
    public void CreateStatementTest()
    {
        DatabaseConnection con = CreateConnection();
        CreateStatement(con);
        DisposeConnection(con);
    }
    
    /**
     * Testing table creation, check existence, and deletion.
     */
    @Test
    public void TableTest()
    {
        DatabaseConnection con = CreateConnection();
        CreateTestTable(con);
        DeleteTestTable(con);
        DisposeConnection(con);
    }
    
    /**
     * Testing table creation, inserting data, updating data, and deletion of table.
     */
    @Test
    public void InsertUpdateTest() throws SQLException
    {
        DatabaseConnection con = CreateConnection();
        CreateTestTable(con);
        
        // Insert
        Insert(con, 100, 0f);
        
        // Check existence
        ResultSet result = con.Query(CreateStatement(con), "select * from testtable where myInt=100 and myFloat=0");
        assertNotNull(result);
        assertTrue(result.next());
        result.close();
        
        // Update
        assertTrue(con.Execute(false, CreateStatement(con), "update testtable set myInt=101 where myInt=100 and myFloat=0"));
        
        DeleteTestTable(con);
        DisposeConnection(con);
    }
    
    /**
     * Testing table creation, inserting data in a batch and later deletion of table.
     */
    @Test
    public void BatchTest() throws SQLException
    {
        DatabaseConnection con = CreateConnection();
        CreateTestTable(con);
        
        // Execute batch
        ArrayList<String> commands = new ArrayList<>();
        commands.add("insert into testtable values (1, 2)");
        commands.add("insert into testtable values (2, 2)");
        commands.add("insert into testtable values (3, 2)");
        commands.add("insert into testtable values (5, 2)");
        commands.add("insert into testtable values (0, 2)");
        con.Execute(CreateStatement(con), commands);
        
        // Check validity
        int count = con.QueryCount(CreateStatement(con), "testtable", "myInt < 5");
        assertEquals(4, count);
        
        DeleteTestTable(con);
        DisposeConnection(con);
    }
    
    private DatabaseConnection CreateConnection()
    {
        DatabaseConnection con = new DatabaseConnection(DatabaseName, new ConsoleLogger());
        assertTrue(con.Connect());
        assertTrue(con.IsConnected());
        assertFalse(con.IsDisposed());
        return con;
    }
    
    private void DisposeConnection(DatabaseConnection con)
    {
        con.Dispose();
        assertTrue(con.IsDisposed());
        assertFalse(con.IsConnected());
    }
    
    private Statement CreateStatement(DatabaseConnection con)
    {
        Statement statement = con.CreateStatement();
        assertNotNull(statement);
        return statement;
    }
    
    private PreparedStatement CreatePreparedStatement(DatabaseConnection con, String template)
    {
        PreparedStatement statement = con.CreatePreparedStatement(template);
        assertNotNull(statement);
        return statement;
    }
    
    private void CreateTestTable(DatabaseConnection con)
    {
        DeleteTestTable(con);
        
        Statement statement = CreateStatement(con);
        if(!con.TableExists("testtable"))
        {
            assertTrue(con.Execute(false, statement, "create table testtable (myInt int, myFloat float)"));
            assertTrue(con.TableExists("testtable"));
        }
    }
    
    private void DeleteTestTable(DatabaseConnection con)
    {
        Statement statement = CreateStatement(con);
        if(con.TableExists("testtable"))
        {
            assertTrue(con.Execute(false, statement, "drop table testtable"));
            assertFalse(con.TableExists("testtable"));
        }
    }
    
    private void Insert(DatabaseConnection con, int i, float f)
    {
        Statement statement = CreateStatement(con);
        assertTrue(con.Execute(false, statement, String.format("insert into testtable values (%d, %f)", f, i)));
    }
}
