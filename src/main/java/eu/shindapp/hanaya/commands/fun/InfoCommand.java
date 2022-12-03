package eu.shindapp.hanaya.commands.fun;

import eu.shindapp.hanaya.utils.BannerUtils;
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
                        .setColor(0x8e00ff)
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
