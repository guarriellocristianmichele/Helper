package me.mrcookies.helper.giveaway;

import me.mrcookies.helper.main.Core;
import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class GiveawayEvent extends ListenerAdapter {

    @Override
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent e) {

        if (e.getUser().isBot()) return;

        if (e.getChannel().getIdLong() != References.idGiveaways) return;

        Long id = Core.getMySQL().getGiveawayID();
        Emote emote = e.getReactionEmote().getEmote();
        User author = e.getUser();

        if (Methods.isStaffer(e.getGuild().getMember(author))) {
            e.getReaction().removeReaction(author).queue();
            author.openPrivateChannel().queue(ch -> Methods.sendSENT(ch, "Giveaway", "You can't join the giveaway."));
            return;
        }

        if (e.getMessageIdLong() != id) {
            e.getReaction().removeReaction(author).queue();
            return;
        }

        if (!e.getReactionEmote().isEmote()) {
            e.getReaction().removeReaction(author).queue();
            return;
        }

        if (emote.getIdLong() != References.check) {
            e.getReaction().removeReaction(author).queue();
        }

    }

}
