package me.mrcookies.helper.listeners.commands;

import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class ChatLogCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        if (e.getAuthor().isBot()) return;

        if (Methods.returnChannels(e)) return;

        if (e.getMessage().getContentRaw().toLowerCase().startsWith(References.prefix + "chatlog")) {

            if (Methods.isInvalidChannel(e.getChannel())) return;

            TextChannel channel = e.getChannel();

            if (!Methods.hasPermission(e.getMember(), channel)) return;

            String[] msg = e.getMessage().getContentRaw().split(" ");

            if (msg.length != 2) {
                Methods.sendErrorMessage(channel, "Use • `chatlog [#channel]`");
                return;
            }

            if (e.getMessage().getMentionedChannels().isEmpty()) {
                Methods.sendErrorMessage(channel, "Use • `chatlog [#channel]`");
                return;
            }

            long now = System.currentTimeMillis();

            TextChannel target = e.getMessage().getMentionedChannels().get(0);
            TextChannel logsChannel = e.getGuild().getTextChannelById(References.idLogs);

            sendChatLogs(logsChannel, target, e.getAuthor());

            long time = System.currentTimeMillis() - now;

            Methods.sendSENT(channel, "Chatlog", "**Successfully logged** " + target.getAsMention() + "\n\nIt tooks `" + time + "`ms.");
        }

    }

    private void sendChatLogs(TextChannel channel, TextChannel log, User open) {
        MessageBuilder message = new MessageBuilder();
        EmbedBuilder builder = new EmbedBuilder();
        File chatlogs = null;

        try {
            chatlogs = Methods.createChatLog(log);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        builder.setAuthor("System", null, "https://i.imgur.com/IUFgzzq.png");
        builder.addField("Type:", "Chatlog", false);
        builder.addField("Channel:", log.getName(), false);
        builder.addField("Logged by:", open.getName(), false);
        builder.setColor(Color.decode("#fdcb6e"));
        builder.setFooter(References.h + " • Chatlog", "https://i.imgur.com/nepS3Lp.jpg");

        message.setEmbed(builder.build());
        channel.sendFile(Objects.requireNonNull(chatlogs), message.build()).queue();
    }

}
