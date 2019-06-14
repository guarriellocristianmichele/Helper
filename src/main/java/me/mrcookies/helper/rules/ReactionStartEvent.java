package me.mrcookies.helper.rules;

import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.guild.GuildReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class ReactionStartEvent extends ListenerAdapter {

    @Override
    public void onGuildReady(GuildReadyEvent e) {
        e.getGuild().getTextChannelById(References.idRules).getMessageById(References.rules).queue(msg -> {
            if (msg.getReactions().isEmpty()) {
                msg.addReaction(getCheck(e.getGuild())).queue();
            }
        });
    }

    private Emote getCheck(Guild guild) {
        return guild.getEmoteById(References.check);
    }

}
