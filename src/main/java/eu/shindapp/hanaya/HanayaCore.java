package eu.shindapp.hanaya;

import eu.shindapp.hanaya.commands.CommandManager;
import eu.shindapp.hanaya.listeners.ListenerManager;
import eu.shindapp.hanaya.utils.ConfigUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.util.logging.Logger;

public class HanayaCore {

    private static JDA api;

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
            new ListenerManager().registerEvents();
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
}
