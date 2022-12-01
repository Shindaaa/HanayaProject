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
            event.deferReply(true).queue();

            Member author = event.getMember();
            assert author != null;
            Guild guild = event.getGuild();
            assert guild != null;

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
