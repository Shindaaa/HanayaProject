package eu.shindapp.hanaya.commands;

import eu.shindapp.hanaya.HanayaCore;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.concurrent.TimeUnit;

public class PermanentBanCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        String reason = null;
        Member author = event.getMember();
        assert author != null;
        Guild guild = event.getGuild();
        assert guild != null;

        if (event.getName().equals("ban") && event.getSubcommandName().equals("permanent")) {
            event.deferReply(true).queue();

            if (userHasModRole(author)) {

                if (event.getOption("user") == null) {
                    event.getHook().sendMessageEmbeds(
                            new EmbedBuilder()
                                    .setColor(0xff0000)
                                    .setDescription("La personne mentionnée est introuvable sur le discord.")
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
                                .setColor(0xff0000)
                                .setDescription("Rappel:\n\n・Vous avez banni " + target.getUser().getAsTag() + "(" + target.getId() + ")\n・Pour raison: " + reason)
                                .build()
                ).queue();
                HanayaCore.getLogger().info("[PERMANENT BAN] user:(" + target.getId() + ") got banned from guild:(" + guild.getId() + ") with reason:(" + reason + ")");
                return;
            }

            event.getHook().sendMessageEmbeds(
                    new EmbedBuilder()
                            .setColor(0xff0000)
                            .setDescription("Votre rang n'authorise pas l'utilisation de cette commande.")
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
