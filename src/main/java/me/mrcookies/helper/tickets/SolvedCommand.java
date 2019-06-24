package me.mrcookies.helper.tickets;

import me.mrcookies.helper.main.Core;
import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class SolvedCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        if (e.getAuthor().isBot()) return;

        if (!isTicketChat(e.getChannel())) return;

        User usr = e.getAuthor();
        Message msg = e.getMessage();
        TextChannel channel = e.getChannel();
        TextChannel logsChannel = e.getGuild().getTextChannelById(References.idLogs);

        if (msg.getContentRaw().equalsIgnoreCase(References.prefix + "solved")) {

            msg.delete().submit();

            if (e.getMember().getRoles().stream().anyMatch(role -> role.getIdLong() == Core.getConfig().getYml().getLong("Roles.utility"))) {
                usr.openPrivateChannel().queue((ch) -> Methods.sendSENT(ch, "Ticket", "Only the user is able to close the ticket.\nTo close it use `" + References.prefix + "close`."));
                return;
            }

            usr.openPrivateChannel().queue((ch) -> Methods.sendSENT(ch, "Ticket", "You solved your ticket (`" + channel.getName() + "`)."));
            sendTicketClose(channel, usr);
            sendChatLogs(logsChannel, channel, usr, usr);
            channel.delete().queueAfter(15, TimeUnit.SECONDS);
            return;
        }

        if (msg.getContentRaw().equalsIgnoreCase(References.prefix + "close")) {

            msg.delete().submit();

            if (e.getMember().getRoles().stream().noneMatch(role -> role.getIdLong() == Core.getConfig().getYml().getLong("Roles.utility"))) {
                usr.openPrivateChannel().queue((ch) -> Methods.sendSENT(ch, "Ticket", "You can't execute this command. "));
                return;
            }

            User target = getTarget(channel);

            usr.openPrivateChannel().queue((ch) -> Methods.sendSENT(ch, "Ticket", "You closed " + Objects.requireNonNull(target).getAsMention() + "'s ticket (`" + channel.getName() + "`)."));
            Objects.requireNonNull(target).openPrivateChannel().queue((ch) -> Methods.sendSENT(ch, "Ticket", "Your ticket has been closed by " + usr.getAsMention() + " (`" + channel.getName() + "`)."));
            sendTicketClose(channel, usr);
            sendChatLogs(logsChannel, channel, target, usr);
            channel.delete().queueAfter(15, TimeUnit.SECONDS);
            return;
        }

        if (msg.getContentRaw().startsWith(References.prefix)) {
            usr.openPrivateChannel().queue((ch) -> Methods.sendSENT(ch, "Ticket", "You can't execute commands in ticket's channel,\nonly `" + References.prefix + "solved` to solve the ticket."));
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

    private void sendChatLogs(TextChannel channel, TextChannel log, User open, User close) {
        MessageBuilder message = new MessageBuilder();
        EmbedBuilder builder = new EmbedBuilder();
        File chatlogs = null;

        try {
            chatlogs = Methods.createChatLog(log);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        builder.setAuthor("System", null, "https://i.imgur.com/IUFgzzq.png");
        builder.addField("Type:", "Ticket", false);
        builder.addField("Channel:", log.getName(), false);
        builder.addField("Opened by:", open.getName(), true);
        builder.addField("Closed by:", close.getName(), true);
        builder.setColor(Color.decode("#fdcb6e"));
        builder.setFooter(References.h + " • Ticket", "https://i.imgur.com/nepS3Lp.jpg");

        message.setEmbed(builder.build());
        channel.sendMessage(message.build()).addFile(chatlogs).queue();
    }

    private void sendTicketClose(TextChannel channel, User usr) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor("Ticket", null, "https://i.imgur.com/nvQY65k.png");
        builder.setDescription("This channel is going to be deleted in ``15``s.");
        builder.setColor(Color.decode("#3498db"));
        builder.setFooter(References.h + " • Ticket closed by " + usr.getName(), "https://i.imgur.com/nepS3Lp.jpg");

        channel.sendMessage(builder.build()).queue();
    }


}