package eu.shindapp.hanaya.commands.fun;

import eu.shindapp.hanaya.utils.ConfigUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.time.Instant;

public class AvatarCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        if (event.getName().equals("avatar")) {

            Member author = event.getMember();
            assert author != null;
            Member selfMember = event.getGuild().getSelfMember();
            event.deferReply().queue();

            if (!new ConfigUtils().getBoolean("command-avatar-enabled")) {
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
                sendAvatarEmbed(event, author, target, selfMember);
                return;
            }

            sendAvatarEmbed(event, author, author, selfMember);
        }
    }

    private void sendAvatarEmbed(SlashCommandInteractionEvent event, Member author, Member target, Member selfMember) {
        event.getHook().sendMessageEmbeds(
                new EmbedBuilder()
                        .setColor(0x36393F)
                        .setAuthor("Avatar de " + target.getUser().getName(), null, selfMember.getAvatarUrl())
                        .setImage(target.getUser().getAvatarUrl() + "?size=512")
                        .setFooter("Envoyé pour " + author.getUser().getName(), author.getUser().getAvatarUrl())
                        .setTimestamp(Instant.now())
                        .build()
        ).queue();
    }
}
