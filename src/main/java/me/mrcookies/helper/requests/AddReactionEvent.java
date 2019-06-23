package me.mrcookies.helper.requests;

import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class AddReactionEvent extends ListenerAdapter {

    @Override
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent e) {

        if (e.getUser().isBot()) return;

        if (e.getChannel().getIdLong() != References.idRequests) return;

        if (!e.getReactionEmote().isEmote()) {
            e.getReaction().removeReaction(e.getUser()).queue();
            return;
        }

        Emote emote = e.getReactionEmote().getEmote();
        User author = e.getUser();

        if (emote.getIdLong() != References.like && emote.getIdLong() != References.dislike) {
            e.getReaction().removeReaction(author).queue();
            return;
        }

        e.getChannel().retrieveMessageById(e.getMessageId()).queue(message -> {
            int cont = 0;

            for (MessageReaction re : message.getReactions()) {

                if (re.retrieveUsers().cache(false).stream().anyMatch(user -> user.getIdLong() == author.getIdLong())) {

                    cont++;

                    if (cont > 1) {
                        e.getReaction().removeReaction(author).queue();
                        author.openPrivateChannel().queue((ch) -> Methods.sendSENT(ch, "System", "You can't vote twice."));
                        break;
                    }

                }

            }
        });

        if (emote.getIdLong() == References.like) return;

        if (emote.getIdLong() == References.dislike) {

            e.getChannel().retrieveMessageById(e.getMessageId()).queue(message -> {

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

            });

        }

    }

}