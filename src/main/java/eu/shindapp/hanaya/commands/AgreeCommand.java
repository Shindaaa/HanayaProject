package eu.shindapp.hanaya.commands;

import eu.shindapp.hanaya.utils.ConfigUtils;
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

            Member author = event.getMember();
            Guild guild = event.getGuild();
            Role verifiedRole = event.getGuild().getRoleById(new ConfigUtils().getString("verification-verified-roleId"));
            event.deferReply(true).queue();

            if (event.getChannel().getId().equals(new ConfigUtils().getString("verification-channelId"))) {
                if (!author.getUser().isBot()) {
                    if (!author.getRoles().contains(guild.getRoleById(new ConfigUtils().getString("verification-verified-roleId")))) {
                        try {
                            event.getGuild().getManager().getGuild().addRoleToMember(author, verifiedRole).queue();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

    }

}
