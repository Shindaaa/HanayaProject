package eu.shindapp.hanaya.commands;

import eu.shindapp.hanaya.HanayaCore;
import eu.shindapp.hanaya.commands.fun.AvatarCommand;
import eu.shindapp.hanaya.commands.fun.BannerCommand;
import eu.shindapp.hanaya.commands.fun.InfoCommand;
import eu.shindapp.hanaya.commands.moderation.AgreeCommand;
import eu.shindapp.hanaya.commands.moderation.BanCommand;
import eu.shindapp.hanaya.commands.moderation.KickCommand;
import eu.shindapp.hanaya.commands.moderation.TimeoutCommand;
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
                                                        new OptionData(OptionType.STRING, "reason", "La raison de cette action (non obligatoire)", false))),

                Commands.slash("kick", "Cette commande permet de kick un membre du discord.")
                        .addOptions(
                                new OptionData(OptionType.USER, "user", "La personne à bannir", true),
                                new OptionData(OptionType.STRING, "reason", "La raison de cette action (non obligatoire)", false)),

                Commands.slash("timeout", "Cette commande permet d'empêcher une personne d'écrire et de parler pendant un temps donné.")
                        .addOptions(
                                new OptionData(OptionType.USER, "user", "La personne à timeout", true),
                                new OptionData(OptionType.INTEGER, "duration", "pendant combien de temps ? (en chiffre)", true),
                                new OptionData(OptionType.STRING, "time_unit", "pendant combien de temps ? (unité de temps)", true)
                                        .addChoice("Jours", "days")
                                        .addChoice("Heures", "hours")
                                        .addChoice("Minutes", "minutes")
                                        .addChoice("Secondes", "seconds"),
                                new OptionData(OptionType.STRING, "reason", "La raison de cette action (non obligatoire)", false)),

                Commands.slash("avatar", "Affiche l'avatar d'une personne mentionnée.")
                        .addOptions(
                                new OptionData(OptionType.USER, "user", "la personne dont vous voulez voir l'avatar.")
                                        .setRequired(false)),

                Commands.slash("banner", "Affiche la bannière d'une personne mentionnée.")
                        .addOptions(
                                new OptionData(OptionType.USER, "user", "la personne dont vous voulez voir la bannière.")
                                        .setRequired(false)),

                Commands.slash("info", "Permet d'avoir plus d'information à propos d'une personne mentionnée.")
                        .addOptions(
                                new OptionData(OptionType.USER, "user", "la personne dont vous voulez avoir plus d'informations")
                                        .setRequired(false))
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

        api.addEventListener(new BanCommand());
        api.addEventListener(new KickCommand());
        api.addEventListener(new TimeoutCommand());
        api.addEventListener(new AvatarCommand());
        api.addEventListener(new BannerCommand());
        api.addEventListener(new InfoCommand());
    }
}
