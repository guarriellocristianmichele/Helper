package me.mrcookies.helper.listeners.events;

import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CancelCommandsEvent extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        Message msg = e.getMessage();

        if (e.getAuthor().isBot()) return;

        if (e.getChannel().getIdLong() == References.idCountGame) return;

        if (e.getChannel().getIdLong() == References.idQuickMath) return;

        if (e.getChannel().getIdLong() == References.idHigherLower) return;

        if (e.getChannel().getIdLong() == References.idTickets) return;

        if (e.getChannel().getIdLong() == References.idRequests) return;

        if (e.getChannel().getIdLong() == References.idRedeem) return;

        if (e.getChannel().getIdLong() == References.idCommands) return;

        if (e.getChannel().getIdLong() == References.idStaffCommands) return;

        if (e.getChannel().getName().contains("ticket-")) return;

        if (msg.getContentRaw().startsWith(References.prefix)) msg.delete().queue();

    }

}
