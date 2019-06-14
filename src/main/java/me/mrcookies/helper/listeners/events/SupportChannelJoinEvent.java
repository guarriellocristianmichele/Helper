package me.mrcookies.helper.listeners.events;

import me.mrcookies.helper.main.Core;
import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class SupportChannelJoinEvent extends ListenerAdapter {

    @Override
    public void onGuildVoiceJoin(GuildVoiceJoinEvent e) {

        if (e.getChannelJoined().getIdLong() != References.idSupportChannel) return;

        if (!e.getMember().getRoles().contains(e.getGuild().getRoleById(Core.getConfig().getYml().getLong("Roles.support"))))
            return;

        int cont = 0;

        for (User usr : e.getJDA().getUsers()) {

            Member mem = e.getGuild().getMember(usr);

            if (mem.getOnlineStatus().equals(OnlineStatus.OFFLINE) || mem.getOnlineStatus().equals(OnlineStatus.INVISIBLE))
                continue;

            if (mem.getRoles().contains(e.getJDA().getRoleById(Core.getConfig().getYml().getLong("Roles.helper")))) {
                usr.openPrivateChannel().queue((channel) -> Methods.sendSENT(channel, "Support", e.getMember().getAsMention() + " needs help."));
                e.getMember().getUser().openPrivateChannel().queue((channel) -> Methods.sendSENT(channel, "Support", "Be patient, soon someone will come to help you."));
                cont++;
            }

        }

        if (cont == 0) {
            e.getMember().getUser().openPrivateChannel().queue((channel) -> {
                Methods.sendSENT(channel, "Support", "There isn't any helper online.");
                channel.close().queue();
            });
        }

    }

}
