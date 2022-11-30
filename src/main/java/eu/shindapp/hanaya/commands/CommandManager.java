package eu.shindapp.hanaya.commands;

import eu.shindapp.hanaya.HanayaCore;
import eu.shindapp.hanaya.utils.ConfigUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

public class CommandManager {

    public void registerCommands() {

        JDA api = HanayaCore.getApi();

        if (new ConfigUtils().getBoolean("bot-onMainGuildOnly")) {
            if (api.getGuildById(new ConfigUtils().getString("bot-mainGuildId")) != null) {
                Guild mainGuild = api.getGuildById(new ConfigUtils().getString("bot-mainGuildId"));
                assert mainGuild != null;
                HanayaCore.getLogger().info("Loading command for main guild Only...");
                commandLoader(api, mainGuild.updateCommands());
            }
            return;
        }

        HanayaCore.getLogger().info("Loading command for all guild...");
        commandLoader(api, api.updateCommands());
    }

    private void commandLoader(JDA api, CommandListUpdateAction commandListUpdateAction) {
        commandListUpdateAction.addCommands(
                //...
        ).queue();

        if (new ConfigUtils().getBoolean("verification-enabled")) {
            commandListUpdateAction.addCommands(
                    Commands.slash("agree", "Accepte le règlement du serveur et deviens un membre de notre communauté !")
            ).queue();
        }

        if (new ConfigUtils().getBoolean("verification-enabled")) {
            HanayaCore.getLogger().info("verification module is enabled !");
            api.addEventListener(new AgreeCommand());
        }
    }
}
