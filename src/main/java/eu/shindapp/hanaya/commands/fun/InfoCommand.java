package eu.shindapp.hanaya.commands.fun;

import eu.shindapp.hanaya.utils.BannerUtils;
import eu.shindapp.hanaya.utils.ConfigUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.time.Instant;

public class InfoCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        if (event.getName().equals("info")) {

            Member author = event.getMember();
            assert author != null;
            event.deferReply().queue();

            if (!new ConfigUtils().getBoolean("command-info-enabled")) {
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

            if (event.getOption("user") != null) {
                Member target = event.getOption("user").getAsMember();
                assert target != null;

                StringBuilder rolesList = new StringBuilder();
                for (Role roles : target.getRoles()) {
                    rolesList.append(roles.getAsMention())
                            .append("\n");
                }

                sendInfoEmbed(event, author, target, rolesList);
                return;
            }

            StringBuilder rolesList = new StringBuilder();
            for (Role roles : author.getRoles()) {
                rolesList.append(roles.getAsMention())
                        .append("\n");
            }

            sendInfoEmbed(event, author, author, rolesList);
        }
    }

    private void sendInfoEmbed(SlashCommandInteractionEvent event, Member author, Member target, StringBuilder rolesList) {
        event.getHook().sendMessageEmbeds(
                new EmbedBuilder()
                        .setColor(0x36393F)
                        .setAuthor(target.getUser().getAsTag(), null, target.getUser().getEffectiveAvatarUrl())
                        .setDescription(target.getAsMention())
                        .addField("ID", target.getUser().getId(), false)
                        .addField("Compte créé le", "<t:" + target.getUser().getTimeCreated().toEpochSecond() + ":F> (<t:" + target.getUser().getTimeCreated().toEpochSecond() + ":R>)", false)
                        .addField("A rejoint le", "<t:" + target.getTimeJoined().toEpochSecond() + ":F> (<t:" + target.getTimeJoined().toEpochSecond() + ":R>)", false)
                        .addField("Est un boosteur ?", target.isBoosting() ? "Oui" : "Non", false)
                        .addField("Roles (" + target.getRoles().size() + ")", rolesList.toString(), false)
                        .setImage(new BannerUtils().getBannerUrl(target.getId()))
                        .setThumbnail(target.getUser().getEffectiveAvatarUrl())
                        .setFooter("Envoyé pour " + author.getUser().getName(), author.getUser().getAvatarUrl())
                        .setTimestamp(Instant.now())
                        .build()
        ).queue();
    }
}
