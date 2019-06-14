package me.mrcookies.helper.listeners.events;

import me.mrcookies.helper.main.Core;
import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MemberJoinEvent extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent e) {

        if (e.getUser().isBot()) return;

        Member mem = e.getMember();
        Role New = e.getGuild().getRoleById(References.newRole);

        e.getGuild().getController().addRolesToMember(mem, New).queue();

        TextChannel rules = e.getGuild().getTextChannelById(References.idRules);
        TextChannel commands = e.getGuild().getTextChannelById(References.idCommands);

        mem.getUser().openPrivateChannel().queue((ch) -> Methods.sendSENT(ch, "Welcome " + mem.getUser().getName(),
                "Please read and accept the rules by adding the reaction " + getCheck(e.getGuild()).getAsMention() + " in the channel " + rules.getAsMention() +
                        ", have a good permanence.\nFor bot's commands type `" + References.prefix + "help` in the channel " + commands.getAsMention()));
        Core.getMySQL().addMember(mem.getUser().getName(), mem.getUser().getIdLong());
    }

    private Emote getCheck(Guild guild) {
        return guild.getEmoteById(References.check);
    }

}
