package me.mrcookies.helper.listeners.events;

import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class CommandsChannelEvents extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        if (e.getAuthor().isBot()) return;

        if (e.getChannel().getIdLong() == References.idCommands && !e.getMessage().getContentRaw().startsWith(References.prefix)) {
            e.getMessage().delete().queue();
        }

        if (e.getChannel().getIdLong() == References.idStaffCommands && !e.getMessage().getContentRaw().startsWith(References.prefix)) {
            e.getMessage().delete().queue();
        }

    }

}
