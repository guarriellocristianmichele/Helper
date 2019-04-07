package me.mrcookies.helper.listeners.events;

import me.mrcookies.helper.main.Core;
import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MemberJoinEvent extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent e) {

        User usr = e.getUser();

        if (usr.isBot()) return;

        e.getGuild().getController().addRolesToMember(e.getMember(), e.getJDA().getRolesByName("Player", true)).complete();

        TextChannel rules = e.getGuild().getTextChannelById(References.idRules);

        Methods.sendPrivateMessage(usr, "**Welcome " + usr.getName() + "**, " +
                "on __TitanNetwork__!\nPlease read the rules in the " +
                "channel " + rules.getAsMention() +
                " and have a good permanence.\nFor bot's commands type `" + References.prefix + "help`.");

        Core.getMySQL().addMember(usr.getName(), usr.getIdLong());

    }

}
