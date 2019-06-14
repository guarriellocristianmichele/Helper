package me.mrcookies.helper.rules;

import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class RulesRemoveReactionEvent extends ListenerAdapter {

    @Override
    public void onGuildMessageReactionRemove(GuildMessageReactionRemoveEvent e) {

        if (e.getUser().isBot()) return;

        if (e.getChannel().getIdLong() != References.idRules) return;

        Emote emote = e.getReactionEmote().getEmote();
        User usr = e.getUser();
        Member mem = e.getMember();

        if (emote.getIdLong() == References.check) {
            Role New = e.getGuild().getRoleById(References.newRole);

            e.getGuild().getController().removeRolesFromMember(mem, mem.getRoles()).queue();
            e.getGuild().getController().addRolesToMember(mem, New).queue();
            usr.openPrivateChannel().queue((ch) -> Methods.sendSENT(ch, "Rules", "It seems like you have removed the reaction " + getCheck(e.getGuild()).getAsMention()
                    + " from the rules message. " +
                    "\nYou must accept the rules in order to get support. " +
                    "\nThat's why I've took all your roles. But don't worry. " +
                    "\nou will get them back once you add the reaction again"));
        }

    }

    private Emote getCheck(Guild guild) {
        return guild.getEmoteById(References.check);
    }

}
