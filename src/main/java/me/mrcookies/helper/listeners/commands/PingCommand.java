package me.mrcookies.helper.listeners.commands;

import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class PingCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        if (e.getAuthor().isBot()) return;

        if (Methods.returnChannels(e)) return;

        if (e.getMessage().getContentRaw().equalsIgnoreCase(References.prefix + "ping")) {

            if (Methods.isInvalidChannel(e.getChannel())) return;

            TextChannel channel = e.getChannel();
            long time = System.currentTimeMillis();

            channel.sendMessage(msg(e.getGuild().getEmoteById(References.loading).getAsMention() + " Testing ping")).queue(message -> {
                long ping = System.currentTimeMillis() - time;
                message.editMessage(msg("**Ping:** `" + ping + "`ms\n**WebSocket:** `" + e.getJDA().getGatewayPing() + "`ms")).queueAfter(2, TimeUnit.SECONDS);
                message.delete().submitAfter(5, TimeUnit.SECONDS);
            });

        }

    }

    private MessageEmbed msg(String Description) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor("System", null, "https://i.imgur.com/IUFgzzq.png");
        builder.setDescription(Description);
        builder.setColor(Color.decode("#fdcb6e"));
        builder.setFooter(References.h, "https://i.imgur.com/nepS3Lp.jpg");
        return builder.build();
    }

}
