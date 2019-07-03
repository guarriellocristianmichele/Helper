package me.mrcookies.helper.rules;

import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class RulesRemoveReactionEvent extends ListenerAdapter {

    @Override
    public void onGuildMessageReactionRemove(GuildMessageReactionRemoveEvent e) {

        if (e.getUser().isBot()) return;

        if (e.getChannel().getIdLong() != References.idRules) return;

        Member mem = e.getMember();
        User usr = mem.getUser();

        if (Methods.isStaffer(mem)) return;

        Emote emote = e.getReactionEmote().getEmote();

        if (emote.getIdLong() == References.check) {
            Role New = e.getGuild().getRoleById(References.newRole);

            e.getGuild().modifyMemberRoles(mem, null, mem.getRoles()).queue();
            e.getGuild().addRoleToMember(mem, New).queue();

            usr.openPrivateChannel().queue((ch) -> Methods.sendSENT(ch, "Rules", "**It seems like you have removed the reaction** " + getCheck(e.getGuild()).getAsMention()
                    + " **from the rules message.** " +
                    "\n\nYou must accept the rules in order to get support. " +
                    "\nThat's why I've took all your roles. \n\n**Don't worry:** " +
                    "\nYou will get them back once you add the reaction again"));
        }

    }

    private Emote getCheck(Guild guild) {
        return guild.getEmoteById(References.check);
    }

}
