package me.mrcookies.helper.listeners.commands;

import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;
import java.time.Instant;

public class FileCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        if (e.getAuthor().isBot()) return;

        if (Methods.returnChannels(e)) return;

        if (e.getMessage().getContentRaw().toLowerCase().startsWith(References.prefix + "file")) {

            if (Methods.isInvalidChannel(e.getChannel())) return;

            TextChannel channel = e.getChannel();
            User usr = e.getAuthor();

            if (!Methods.hasPermission(e.getMember(), channel)) return;

            String[] msg = e.getMessage().getContentRaw().split(" / ");

            if (msg.length != 4) {
                Methods.sendErrorMessage(channel, "Use • `file / [title] / [link] / [#channel]`");
                return;
            }

            if (e.getMessage().getMentionedChannels().isEmpty()) {
                Methods.sendErrorMessage(channel, "Use • `file / [title] / [link] / [#channel]`");
                return;
            }

            TextChannel target = e.getMessage().getMentionedChannels().get(0);

            sendFile(target, msg[1], msg[2]);
            usr.openPrivateChannel().queue((ch) -> Methods.sendSENT(ch, "System", "File sent successfully."));
        }

    }

    private void sendFile(TextChannel channel, String title, String link) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor(title, null, "https://i.imgur.com/IUFgzzq.png");
        builder.setImage(link);
        builder.setColor(Color.decode("#fdcb6e"));
        builder.setFooter("Helper", "https://i.imgur.com/nepS3Lp.jpg");
        builder.setTimestamp(Instant.now());

        channel.sendMessage(builder.build()).queue();
    }

}
