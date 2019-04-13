package me.mrcookies.helper.listeners.events;

import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class CommandsChannelEvents extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        if (e.getAuthor().isBot()) return;

        User usr = e.getAuthor();

        if (e.getChannel().getIdLong() == References.idCommands && !e.getMessage().getContentRaw().startsWith(References.prefix)) {
            e.getMessage().delete().queue();
            usr.openPrivateChannel().queue((ch) -> Methods.sendSENT(ch, "System", "Please use only commands in commands's channel."));
        }

        if (e.getChannel().getIdLong() == References.idStaffCommands && !e.getMessage().getContentRaw().startsWith(References.prefix)) {
            e.getMessage().delete().queue();
            usr.openPrivateChannel().queue((ch) -> Methods.sendSENT(ch, "System", "Please use only commands in commands's channel."));
        }

    }

}
