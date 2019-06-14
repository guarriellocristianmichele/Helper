package me.mrcookies.helper.rules;

import me.mrcookies.helper.main.Core;
import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.Collection;

public class RulesAddReactionEvent extends ListenerAdapter {

    @Override
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent e) {

        if (e.getUser().isBot()) return;

        if (e.getChannel().getIdLong() != References.idRules) return;

        if (!e.getReactionEmote().isEmote()) {
            e.getReaction().removeReaction(e.getUser()).queue();
            return;
        }

        Emote emote = e.getReactionEmote().getEmote();
        User usr = e.getUser();
        Member mem = e.getMember();

        if (emote.getIdLong() != References.check) {
            e.getReaction().removeReaction(usr).queue();
            return;
        }

        if (emote.getIdLong() == References.check) {
            Collection<Role> roles = new ArrayList<>();
            roles.add(e.getGuild().getRoleById(Core.getConfig().getYml().getLong("Roles.member")));
            roles.add(e.getGuild().getRoleById(Core.getConfig().getYml().getLong("Roles.support")));
            Role New = e.getGuild().getRoleById(References.newRole);

            e.getGuild().getController().addRolesToMember(mem, roles).queue();
            e.getGuild().getController().removeRolesFromMember(mem, New).queue();
            usr.openPrivateChannel().queue((ch) -> Methods.sendSENT(ch, "Rules", "Dear " + usr.getAsMention() + ",\nthanks accepting our rules.\n**Now you are our Member.**" +
                    "\n\n`Remember if you need help open a ticket or join the voice channel.`"));
        }

    }

}
