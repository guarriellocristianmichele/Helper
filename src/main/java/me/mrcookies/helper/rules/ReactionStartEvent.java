package me.mrcookies.helper.rules;

import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ReactionStartEvent extends ListenerAdapter {

    @Override
    public void onGuildReady(GuildReadyEvent e) {

        e.getGuild().getTextChannelById(References.idRules).retrieveMessageById(References.rules).queue(msg -> {

            if (msg.getReactions().isEmpty()) {
                msg.addReaction(getCheck(e.getGuild())).queue();
                return;
            }

            for (MessageReaction re : msg.getReactions()) {

                re.retrieveUsers().cache(false)
                        .forEachAsync(user -> user.getIdLong() != e.getGuild().getSelfMember().getUser().getIdLong())
                        .thenRun(() -> msg.addReaction(getCheck(e.getGuild())).queue());

            }

        });

    }

    private Emote getCheck(Guild guild) {
        return guild.getEmoteById(References.check);
    }

}
