package me.mrcookies.helper.requests;

import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageReaction;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class AddReactionEvent extends ListenerAdapter {

    @Override
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent e) {

        if (e.getUser().isBot()) return;

        if (e.getChannel().getIdLong() != References.idRequests) return;

        if (!e.getReactionEmote().isEmote()) {
            e.getReaction().removeReaction(e.getUser()).queue();
            return;
        }

        Message message = e.getChannel().getMessageById(e.getMessageId()).complete();
        Emote emote = e.getReactionEmote().getEmote();

        if (emote.getIdLong() != References.like && emote.getIdLong() != References.dislike) {
            e.getReaction().removeReaction(e.getUser()).queue();
            return;
        }

        if (emote.getIdLong() == References.like) return;

        if (emote.getIdLong() == References.dislike) {

            int likes = 0;
            int dislikes = 0;

            for (MessageReaction reaction : message.getReactions()) {

                if (reaction.getReactionEmote().getIdLong() == References.like) {
                    likes = reaction.getCount();
                }

                if (reaction.getReactionEmote().getIdLong() == References.dislike) {
                    dislikes = reaction.getCount();
                }

            }

            int rating = likes - dislikes;

            if (rating < -5) {
                message.delete().queue();
            }

        }

    }

}
