package eu.shindapp.hanaya.commands;

import eu.shindapp.hanaya.HanayaCore;
import eu.shindapp.hanaya.utils.ConfigUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
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
                Commands.slash("ban", "Cette commande permet de bannir un membre du discord définitivement.")
                                .addSubcommands(
                                        new SubcommandData("permanent", "Cette commande permet de bannir un membre du discord de manière définitive.")
                                                .addOptions(
                                                        new OptionData(OptionType.USER, "user", "La personne à bannir", true),
                                                        new OptionData(OptionType.STRING, "reason", "La raison de cette action (non obligatoire)", false)),
                                        new SubcommandData("temporaire", "Cette commande permet de bannir un membre du discord de manière temporaire.")
                                                .addOptions(
                                                        new OptionData(OptionType.USER, "user", "La personne à bannir", true),
                                                        new OptionData(OptionType.STRING, "reason", "La raison de cette action (non obligatoire)", false)))
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

        api.addEventListener(new PermanentBanCommand());
    }
}
