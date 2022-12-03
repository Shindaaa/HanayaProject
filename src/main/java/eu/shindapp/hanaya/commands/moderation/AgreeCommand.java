package eu.shindapp.hanaya.commands.moderation;

import eu.shindapp.hanaya.utils.ConfigUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;

public class AgreeCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        if (event.getName().equals("agree")) {
            event.deferReply(true).queue();

            Member author = event.getMember();
            assert author != null;
            Guild guild = event.getGuild();
            assert guild != null;

            if (!new ConfigUtils().getBoolean("command-agree-enabled")) {
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

            Role verifiedRole = guild.getRoleById(new ConfigUtils().getString("verification-verified-roleId"));

            if (event.getChannel().getId().equals(new ConfigUtils().getString("verification-channelId"))) {
                if (!author.getUser().isBot()) {
                    if (!author.getRoles().contains(guild.getRoleById(new ConfigUtils().getString("verification-verified-roleId")))) {
                        try {
                            guild.getManager().getGuild().addRoleToMember(author, verifiedRole).queue();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

    }

}
