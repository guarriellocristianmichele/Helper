package me.mrcookies.helper.listeners.events;

import me.mrcookies.helper.main.Core;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MemberLeftEvent extends ListenerAdapter {

    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent e) {

        if (e.getUser().isBot()) return;

        Core.getMySQL().removeMember(e.getUser().getIdLong());
    }

}
