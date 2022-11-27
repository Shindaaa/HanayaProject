package eu.shindapp.hanaya.commands;

import eu.shindapp.hanaya.HanayaCore;
import eu.shindapp.hanaya.utils.ConfigUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
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
                Commands.slash("raid-mode", "Permet d'activer ou de désactiver le mode \"raid\"")
                        .addOptions(
                                new OptionData(OptionType.BOOLEAN, "statut", "Que voulez vous faire ?")
                                        .addChoices(
                                                new Command.Choice("Activer", 1),
                                                new Command.Choice("Désactiver", "0")
                                        ).setRequired(true)
                        )
        ).queue();

        api.addEventListener(
                new RaidModeCommand()
        );
    }
}
