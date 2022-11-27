package eu.shindapp.hanaya.listeners;

import eu.shindapp.hanaya.HanayaCore;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.time.Instant;

public class RaidModeListener extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        if (HanayaCore.isRaidModeActive()) {
            event.getMember().getUser().openPrivateChannel().queue(s -> {
                try {
                    s.sendMessageEmbeds(
                            new EmbedBuilder()
                                    .setColor(0x0000F)
                                    .setDescription("Le mode RAID est Actif sur le discord !\n" +
                                            "\n" +
                                            "<a:Orb:710574046995283979> Aucune personne ne peux rejoindre ce discord tant qu'il est actif !")
                                    .setFooter("HanayaShield", event.getGuild().getIconUrl())
                                    .setTimestamp(Instant.now())
                                    .build()
                    ).queue();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            event.getMember().kick().reason("Le mode RAID est actif.");
        }
    }
}
