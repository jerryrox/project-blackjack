/*
 * Jerry Kim (18015036), 2019
 */
package game.io.storage;

import game.database.DatabaseConnection;
import game.entities.OwnedItemEntity;
import game.rulesets.items.ItemDefinitions;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Item storage with database backend.
 * @author jerrykim
 */
public class ItemDatabaseStorage extends DatabaseStorage<OwnedItemEntity> {
    
    private ItemDefinitions definitions;
    

    public ItemDatabaseStorage(DatabaseConnection connection, ItemDefinitions definitions)
    {
        super(connection, "owneditem");
        this.definitions = definitions;
    }

    protected @Override String GetTableStructureString()
    {
        return "id varchar(50) unique not null, infoid int";
    }

    protected @Override String GetInsertValueString(OwnedItemEntity value)
    {
        return String.format("'%s', %d", value.GetId(), value.GetInfo().Id);
    }

    protected @Override String GetUpdateValueString(OwnedItemEntity value)
    {
        return String.format("infoid=%d", value.GetInfo().Id);
    }

    protected @Override OwnedItemEntity ParseEntity(ResultSet result) throws SQLException
    {
        OwnedItemEntity entity = new OwnedItemEntity(definitions);
        entity.SetId(result.getString("id"));
        entity.SetInfo(definitions.GetInfo(result.getInt("infoid")));
        return entity;
    }
}
