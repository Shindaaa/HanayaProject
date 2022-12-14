package eu.shindapp.hanaya.commands.moderation;

import eu.shindapp.hanaya.HanayaCore;
import eu.shindapp.hanaya.models.HanayaSanctions;
import eu.shindapp.hanaya.utils.ConfigUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.concurrent.TimeUnit;

public class BanCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        String reason = null;
        Member author = event.getMember();
        assert author != null;
        Guild guild = event.getGuild();
        assert guild != null;

        if (event.getName().equals("ban") && event.getSubcommandName().equals("permanent")) {
            event.deferReply(true).queue();

            if (!new ConfigUtils().getBoolean("command-permanent-ban-enabled")) {
                event.getHook().sendMessageEmbeds(
                        new EmbedBuilder()
                                .setColor(0x36393F)
                                .setAuthor("Commande désactivé", null, "https://cdn.discordapp.com/attachments/856076221687660574/1048324320629374976/error-message.png")
                                .setDescription("Cette commande est actuellement désactivée dans le fichier de configuration.")
                                .setImage("https://cdn.discordapp.com/attachments/856076221687660574/856086449404903434/barre.rouge2.png")
                                .build()
                ).queue();
                return;
            }

            if (userHasModRole(author)) {

                if (event.getOption("user") == null || event.getOption("reason").equals("")) {
                    event.getHook().sendMessageEmbeds(
                            new EmbedBuilder()
                                    .setColor(0x36393F)
                                    .setAuthor("Utilisateur introuvable", null, "https://cdn.discordapp.com/attachments/856076221687660574/1048324320629374976/error-message.png")
                                    .setDescription("La personne mentionnée est introuvable sur le discord.")
                                    .setImage("https://cdn.discordapp.com/attachments/856076221687660574/856086449404903434/barre.rouge2.png")
                                    .build()
                    ).queue();
                    return;
                }

                Member target = event.getOption("user").getAsMember();
                assert target != null;

                if (event.getOption("reason") == null) {
                    reason = "Aucune raison donnée (Banni par " + author.getUser().getAsTag() + ")";
                }

                target.ban(7, TimeUnit.DAYS).reason(reason).queue();
                event.getHook().sendMessageEmbeds(
                        new EmbedBuilder()
                                .setColor(0x36393F)
                                .setAuthor("Sanction effectuée", null, "https://cdn.discordapp.com/attachments/856076221687660574/1048324312064593970/ban-user.png")
                                .setDescription(target.getUser().getAsTag() + " a bien été banni du discord.")
                                .setImage("https://cdn.discordapp.com/attachments/856076221687660574/857224409532989450/barre.verte2.png")
                                .build()
                ).queue();

                try {
                    HanayaSanctions hanayaSanctions = new HanayaSanctions();
                    hanayaSanctions.setType("ban");
                    hanayaSanctions.setReason(reason);
                    hanayaSanctions.setModId(event.getMember().getId());
                    hanayaSanctions.setUserId(target.getId());
                    hanayaSanctions.save();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                HanayaCore.getLogger().info("[PERMANENT BAN] user:(" + target.getId() + ") got banned from guild:(" + guild.getId() + ") with reason:(" + reason + ")");
                return;
            }

            event.getHook().sendMessageEmbeds(
                    new EmbedBuilder()
                            .setColor(0x36393F)
                            .setAuthor("Accès refusé", null, "https://cdn.discordapp.com/attachments/856076221687660574/1048324320629374976/error-message.png")
                            .setDescription("Votre rang n'authorise pas l'utilisation de cette commande.")
                            .setImage("https://cdn.discordapp.com/attachments/856076221687660574/856086449404903434/barre.rouge2.png")
                            .build()
            ).queue();
        }
    }

    private boolean userHasModRole(Member user) {
        for (Role role : user.getRoles()) {
            if (role.getId().equals("1024348106105552988") || role.getId().equals("1024348110891257976") || role.getId().equals("1024348108152389712") || role.getId().equals("1024348107342872597") || role.getId().equals("1024348109721043005")) {
                return true;
            }
        }
        return false;
    }
}
