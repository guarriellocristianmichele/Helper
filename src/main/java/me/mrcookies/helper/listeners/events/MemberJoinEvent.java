package me.mrcookies.helper.listeners.events;

import me.mrcookies.helper.main.Core;
import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.Collection;

public class MemberJoinEvent extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent e) {

        if (e.getUser().isBot()) return;

        Member mem = e.getMember();

        Collection<Role> roles = new ArrayList<>();
        roles.add(e.getGuild().getRoleById(Core.getConfig().getYml().getLong("Roles.player")));
        roles.add(e.getGuild().getRoleById(Core.getConfig().getYml().getLong("Roles.support")));

        e.getGuild().getController().addRolesToMember(mem, roles).queue();

        TextChannel rules = e.getGuild().getTextChannelById(References.idRules);

        mem.getUser().openPrivateChannel().queue((ch) -> Methods.sendSENT(ch, "Welcome " + mem.getUser().getName(),
                "Please read the rules in the " + "channel " + rules.getAsMention() +
                        " and have a good permanence.\nFor bot's commands type `" + References.prefix + "help`."));
        Core.getMySQL().addMember(mem.getUser().getName(), mem.getUser().getIdLong());
    }

}
