package eu.shindapp.hanaya.commands;

import eu.shindapp.hanaya.HanayaCore;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class RaidModeCommand extends ListenerAdapter {

    private UUID buttonId;
    private Member author;

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("raid-mode")) {

            buttonId = UUID.randomUUID();
            author = event.getMember();
            Member selfUser = event.getGuild().getSelfMember();
            boolean desiredStatus = event.getOption("statut").getAsBoolean();
            event.deferReply().queue();

            if (author.getRoles().contains(event.getGuild().getRoleById("1024348109721043005")) || //mod role
            author.getRoles().contains(event.getGuild().getRoleById("1024348110891257976")) || //resp staff role
            author.getRoles().contains(event.getGuild().getRoleById("1024348106105552988")) || //owner role
            author.getRoles().contains(event.getGuild().getRoleById("1024348108152389712"))) { //admin role
                if (!desiredStatus) {
                    HanayaCore.setRaidModeStatus(false);
                    event.getChannel().sendMessageEmbeds(
                            new EmbedBuilder()
                                    .setColor(0x0000F)
                                    .setDescription("Le mode RAID est maintenant désactivé!\n" +
                                            "\n" +
                                            "<a:Orb:710574046995283979> Tout le monde peut à présent rejoindre le discord !")
                                    .setFooter("HanayaShield", event.getGuild().getIconUrl())
                                    .setTimestamp(Instant.now())
                                    .build()
                    ).queue(msg -> msg.delete().queueAfter(1, TimeUnit.MINUTES));
                }

                if (desiredStatus) {
                    event.getHook().sendMessageEmbeds(
                            new EmbedBuilder()
                                    .setColor(0x0000F)
                                    .setAuthor("Raid-Mode", null, event.getGuild().getIconUrl())
                                    .setDescription("Souhaitez vous vraiment activer le mode RAID sur le serveur ?\nActiver ce mode empèchera quiconque de rejoindre le discord.\n\n*Si vous souhaitez annuler cette action supprimez juste ce message.*")
                                    .build()
                    ).addActionRow(
                            Button.danger(String.valueOf(buttonId), "Activer").withEmoji(Emoji.fromUnicode("⚠️"))
                    ).setEphemeral(true).queue();
                }
            }
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (Objects.equals(event.getButton().getId(), String.valueOf(buttonId))) {
            if (event.getMember().equals(author)) {
                HanayaCore.setRaidModeStatus(true);
                event.getChannel().sendMessageEmbeds(
                        new EmbedBuilder()
                                .setColor(0x0000F)
                                .setAuthor("RAID-MODE", null, event.getGuild().getIconUrl())
                                .setDescription("Le mode `RAID` est maintenant `Actif` sur le discord !\n" +
                                        "\n" +
                                        "<a:Orb:710574046995283979> `Aucune` personne ne peux `rejoindre` ce discord tant qu'il est `actif` !\n" +
                                        "<a:Orb:710574046995283979> **N'oubliez pas de le désactiver une fois l'attaque terminé !**")
                                .build()
                ).queue();
            }
        }
    }
}
