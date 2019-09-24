/*
 * Jerry Kim (18015036), 2019
 */
package game.io.storage;

import game.database.DatabaseConnection;
import game.debug.Debug;
import game.entities.UserEntity;
import game.entities.UserStats;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User storage with database backend.
 * @author jerrykim
 */
public class UserDatabaseStorage extends DatabaseStorage<UserEntity> {

    public UserDatabaseStorage(DatabaseConnection connection)
    {
        super(connection, "player");
    }

    protected @Override String GetTableStructureString()
    {
        return "id varchar(50) unique not null, " +
                "username varchar(20), " +
                "gold int, " +
                "survivalround int, " +
                "power int, " +
                "armor int, " +
                "endurance int, " +
                "luck int, " +
                "fortune int";
    }

    protected @Override String GetInsertValueString(UserEntity value)
    {
        UserStats stats = value.GetStats();
        return String.format(
            "'%s', '%s', %d, %d, %d, %d, %d, %d, %d",
            value.GetId(),
            value.Username.GetValue(),
            value.Gold.GetValue(),
            value.SurvivalRound.GetValue(),
            stats.Power.Level.GetValue(),
            stats.Armor.Level.GetValue(),
            stats.Endurance.Level.GetValue(),
            stats.Luck.Level.GetValue(),
            stats.Fortune.Level.GetValue()
        );
    }

    protected @Override String GetUpdateValueString(UserEntity value)
    {
        UserStats stats = value.GetStats();
        return String.format(
            "username='%s', gold=%d, survivalround=%d, power=%d, armor=%d, endurance=%d, luck=%d, fortune=%d",
            value.Username.GetValue(),
            value.Gold.GetValue(),
            value.SurvivalRound.GetValue(),
            stats.Power.Level.GetValue(),
            stats.Armor.Level.GetValue(),
            stats.Endurance.Level.GetValue(),
            stats.Luck.Level.GetValue(),
            stats.Fortune.Level.GetValue()
        );
    }

    protected @Override UserEntity ParseEntity(ResultSet result) throws SQLException
    {
        UserEntity entity = new UserEntity();
        UserStats stats = entity.GetStats();
        
        entity.SetId(result.getString("id"));
        entity.Username.SetValue(result.getString("username"));
        entity.Gold.SetValue(result.getInt("gold"));
        entity.SurvivalRound.SetValue(result.getInt("survivalround"));
        stats.Power.Level.SetValue(result.getInt("power"));
        stats.Armor.Level.SetValue(result.getInt("armor"));
        stats.Endurance.Level.SetValue(result.getInt("endurance"));
        stats.Luck.Level.SetValue(result.getInt("luck"));
        stats.Fortune.Level.SetValue(result.getInt("fortune"));
        return entity;
    }
}
