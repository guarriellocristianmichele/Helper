package me.mrcookies.helper.tickets;

import me.mrcookies.helper.main.Core;
import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.io.File;
import java.io.IOException;

public class SolvedCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        if (e.getAuthor().isBot()) return;

        if (!isTicketChat(e.getChannel())) return;

        User usr = e.getAuthor();
        Message msg = e.getMessage();
        TextChannel channel = e.getChannel();
        TextChannel chatLogsChannel = e.getGuild().getTextChannelById(References.idChatLogs);

        if (msg.getContentRaw().equalsIgnoreCase(References.prefix + "solved")) {

            msg.delete().submit();

            if (e.getMember().getRoles().stream().anyMatch(role -> role.getIdLong() == Core.getConfig().getYml().getLong("Roles.utility"))) {

                usr.openPrivateChannel().queue((ch) -> {
                    Methods.sendSENT(ch, "Ticket", "Only the user is able to close the ticket.\nTo close it use `" + References.prefix + "close`.");
                    ch.close().queue();
                });

                return;
            }

            usr.openPrivateChannel().queue((ch) -> {
                Methods.sendSENT(ch, "Ticket", "You solved your ticket (`" + channel.getName() + "`).");
                ch.close().queue();
            });

            File chatlogs = null;

            try {
                chatlogs = Methods.createChatLog(channel);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            chatLogsChannel.sendFile(chatlogs).queue();

            channel.delete().complete();
            return;
        }

        if (msg.getContentRaw().equalsIgnoreCase(References.prefix + "close")) {

            msg.delete().submit();

            if (e.getMember().getRoles().stream().noneMatch(role -> role.getIdLong() == Core.getConfig().getYml().getLong("Roles.utility"))) {

                usr.openPrivateChannel().queue((ch) -> {
                    Methods.sendSENT(ch, "Ticket", "You can't execute this command. ");
                    ch.close().queue();
                });

                return;
            }

            User target = getTarget(channel);

            usr.openPrivateChannel().queue((ch) -> {
                Methods.sendSENT(ch, "Ticket", "You closed " + target.getAsMention() + "'s ticket (`" + channel.getName() + "`).");
                ch.close().queue();
            });

            target.openPrivateChannel().queue((ch) -> {
                Methods.sendSENT(ch, "Ticket", "Your ticket has been closed by " + usr.getAsMention() + " (`" + channel.getName() + "`).");
                ch.close().queue();
            });

            try {
                chatLogsChannel.sendFile(Methods.createChatLog(channel));
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            channel.delete().complete();
            return;
        }

        if (msg.getContentRaw().startsWith(References.prefix)) {

            usr.openPrivateChannel().queue((ch) -> {
                Methods.sendSENT(ch, "Ticket", "You can't execute commands in ticket's channel,\nonly `" + References.prefix + "solved` to solve the ticket.");
                ch.close().queue();
            });

            msg.delete().queue();
        }

    }

    private boolean isTicketChat(TextChannel channel) {
        return channel.getName().contains("ticket-");
    }

    private User getTarget(TextChannel channel) {

        for (Member member : channel.getMembers()) {

            if (channel.getTopic().contains(member.getAsMention())) {
                return member.getUser();
            }

        }

        return null;
    }

}