package me.ccattell.plugins.completeeconomy.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import static me.ccattell.plugins.completeeconomy.CompleteEconomy.plugin;
import org.bukkit.ChatColor;

/**
 *
 * @author Charlie
 */
public class CEInitSQLite {

    CEDatabase service = CEDatabase.getInstance();
    public Connection connection = service.getConnection();
    public Statement statement = null;

    public void CEInitSQLite() {
    }

    public void initSQLite() {
        try {
            statement = connection.createStatement();
            String queryCE = "CREATE TABLE IF NOT EXISTS CEMain (player_name TEXT PRIMARY KEY COLLATE NOCASE, cash REAL DEFAULT 0.0, bank REAL DEFAULT 0.0, xp REAL DEFAULT 0.0, last_login INTEGER DEFAULT 0)";
            statement.executeUpdate(queryCE);
            String queryJobs = "CREATE TABLE IF NOT EXISTS CEJobs (player_name TEXT COLLATE NOCASE, job text NOT NULL COLLATE NOCASE, experience INTEGER DEFAULT 0, level INTEGER DEFAULT 0, status TEXT NOT NULL COLLATE NOCASE)";
            statement.executeUpdate(queryJobs);
            String querySkills = "CREATE TABLE IF NOT EXISTS CESkills (player_name TEXT COLLATE NOCASE, skill text NOT NULL COLLATE NOCASE, experience INTEGER DEFAULT 0, level INTEGER DEFAULT 0)";
            statement.executeUpdate(querySkills);
        } catch (SQLException e) {
            plugin.console.sendMessage(plugin.pluginName + ChatColor.GOLD + "Could not create SQLite tables: " + e.getMessage() + ChatColor.RESET);
        }
    }
}
