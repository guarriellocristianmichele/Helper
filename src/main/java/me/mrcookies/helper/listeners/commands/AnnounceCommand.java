package me.mrcookies.helper.listeners.commands;

import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

public class AnnounceCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        if (e.getAuthor().isBot()) return;

        if (Methods.returnChannels(e)) return;

        if (e.getMessage().getContentRaw().toLowerCase().startsWith(References.prefix + "announce")) {

            if (Methods.isInvalidChannel(e.getChannel())) return;

            TextChannel channel = e.getChannel();
            User usr = e.getAuthor();

            if (!Methods.hasPermission(e.getMember(), channel)) return;

            String[] msgs = e.getMessage().getContentRaw().split(" / ");

            if (msgs.length != 4) {
                Methods.sendErrorMessage(channel, "Use • `announce / [title] / [description] / [#channel]`");
                return;
            }

            if (e.getMessage().getMentionedChannels().isEmpty()) {
                Methods.sendErrorMessage(channel, "Use • `announce / [title] / [description] / [#channel]`");
                return;
            }

            TextChannel target = e.getMessage().getMentionedChannels().get(0);

            sendAnnounce(target, msgs[1], msgs[2], e.getAuthor().getName());
            usr.openPrivateChannel().queue((ch) -> Methods.sendSENT(ch, "Announce", "Message sent successfully."));
        }

    }

    private void sendAnnounce(TextChannel channel, String title, String description, String name) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor(title, null, "https://i.imgur.com/vWc2wow.png");
        builder.setDescription(description);
        builder.setColor(Color.decode("#22b9ca"));
        builder.setFooter(References.h + " • Posted by " + name, "https://i.imgur.com/nepS3Lp.jpg");

        channel.sendMessage(builder.build()).queue();
    }

}
