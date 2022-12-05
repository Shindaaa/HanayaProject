package eu.shindapp.hanaya;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;
import eu.shindapp.hanaya.commands.CommandManager;
import eu.shindapp.hanaya.models.HanayaLogChannels;
import eu.shindapp.hanaya.models.HanayaMembers;
import eu.shindapp.hanaya.models.HanayaSanctions;
import eu.shindapp.hanaya.utils.ConfigUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.sql.SQLException;
import java.util.logging.Logger;

public class HanayaCore {

    private static JDA api;

    private static JdbcConnectionSource connectionSource;

    private static Dao<HanayaMembers, String> hanayaMembersDao;
    private static Dao<HanayaSanctions, String> hanayaSanctionsDao;
    private static Dao<HanayaLogChannels, String> hanayaLogChannels;

    private static Logger logger = Logger.getGlobal();

    public static void main(String[] args) {
        try {
            logger.info("Checking configuration file...");
            new ConfigUtils().init();
            logger.info("Configuration file checked and ready to go.");
        } catch (Exception e) {
            logger.severe("An error occurred while checking the configuration files.\nError message " + e.getMessage());
            System.exit(0);
        }

        if (new ConfigUtils().getBoolean("mysql-enabled")) {
            try {
                logger.info("Connecting to MySQL Database...");
                connectionSource = new JdbcConnectionSource("jdbc:mariadb://" + new ConfigUtils().getString("mysql-host") + ":3306/" + new ConfigUtils().getString("mysql-database") + "?autoReconnect=true&wait_timeout=172800", new ConfigUtils().getString("mysql-username"), new ConfigUtils().getString("mysql-password"));
                logger.info("Successfully connected to MySQL Database ! Starting loading all tables...");

                hanayaMembersDao = DaoManager.createDao(connectionSource, HanayaMembers.class);
                TableUtils.createTableIfNotExists(connectionSource, HanayaMembers.class);
                hanayaSanctionsDao = DaoManager.createDao(connectionSource, HanayaSanctions.class);
                TableUtils.createTableIfNotExists(connectionSource, HanayaSanctions.class);
                hanayaLogChannels = DaoManager.createDao(connectionSource, HanayaLogChannels.class);
                TableUtils.createTableIfNotExists(connectionSource, HanayaLogChannels.class);
                logger.info("All MySQL tables loaded.");

            } catch (SQLException e) {
                logger.severe("An error occurred while connecting to the MySQL Database.\nError message " + e.getMessage());
                System.exit(0);
            }
        }

        try {
            logger.info("Starting building JDA client...");
            api = JDABuilder
                    .createDefault(new ConfigUtils().getString("bot-token"))
                    .enableIntents(
                            GatewayIntent.GUILD_MEMBERS,
                            GatewayIntent.GUILD_PRESENCES)
                    .enableCache(CacheFlag.ACTIVITY)
                    .build().awaitReady();
            logger.info("JDA client built.");
            logger.info("Starting loading all commands...");
            new CommandManager().registerCommands();
            logger.info("All commands files loaded successfully.");
        } catch (Exception e) {
            logger.severe("An error occurred while starting the bot\nError message: " + e.getMessage());
            System.exit(0);
        }
    }

    public static JDA getApi() {
        return api;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static JdbcConnectionSource getConnectionSource() {
        return connectionSource;
    }

    public static Dao<HanayaMembers, String> getHanayaMembersDao() {
        return hanayaMembersDao;
    }

    public static Dao<HanayaSanctions, String> getHanayaSanctionsDao() {
        return hanayaSanctionsDao;
    }

    public static Dao<HanayaLogChannels, String> getHanayaLogChannels() {
        return hanayaLogChannels;
    }
}
