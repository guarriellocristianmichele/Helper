package me.mrcookies.helper.listeners.events;

import me.mrcookies.helper.main.Core;
import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MemberJoinEvent extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent e) {

        if (e.getUser().isBot()) return;

        Member mem = e.getMember();

        e.getGuild().getController().addRolesToMember(mem, e.getGuild().getRoleById(Core.getConfig().getYml().getLong("Roles.player"))).queue();
        e.getGuild().getController().addRolesToMember(mem, e.getGuild().getRoleById(Core.getConfig().getYml().getLong("Roles.support"))).queue();

        TextChannel rules = e.getGuild().getTextChannelById(References.idRules);

        mem.getUser().openPrivateChannel().queue((ch) -> Methods.sendSENT(ch, "Welcome " + mem.getUser().getName(),
                "Please read the rules in the " + "channel " + rules.getAsMention() +
                        " and have a good permanence.\nFor bot's commands type `" + References.prefix + "help`."));
        Core.getMySQL().addMember(mem.getUser().getName(), mem.getUser().getIdLong());
    }

}
