package me.mrcookies.helper.listeners.commands;

import me.mrcookies.helper.main.Core;
import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class ShutdownCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        if (e.getAuthor().isBot()) return;

        if (Methods.returnChannels(e)) return;

        if (e.getMessage().getContentRaw().equalsIgnoreCase(References.prefix + "sd")) {

            if (Methods.isInvalidChannel(e.getChannel())) return;

            TextChannel channel = e.getChannel();

            if (!Methods.hasPermission(e.getMember(), channel)) return;

            TextChannel c = e.getJDA().getTextChannelById(References.idStatus);
            List<Message> msgs = c.getHistory().retrievePast(1).complete();

            Methods.sendSENT(channel, "System", "The Bot will shutdown...");

            Core.getConfig().save();

            if (msgs.size() > 0) {
                c.getHistory().retrievePast(1).queue(msg -> msg.get(0).editMessage(msg(
                        e.getGuild().getEmoteById(References.offline).getAsMention() + "``Offline!``")).submit()
                        .thenRun(() -> {
                            try {
                                Runtime.getRuntime().exec("sudo pkill -f Helper");
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }));
            } else {
                c.sendMessage(msg(e.getGuild().getEmoteById(References.offline).getAsMention() + "``Offline!``")).submit()
                        .thenRun(() -> {
                            try {
                                Runtime.getRuntime().exec("sudo pkill -f Helper");
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        });
            }


        }

    }

    private MessageEmbed msg(String Description) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor("Status", null, "https://i.imgur.com/IUFgzzq.png");
        builder.setDescription(Description);
        builder.setColor(Color.decode("#fdcb6e"));
        builder.setFooter(References.h + " • Bye... Bye...", "https://i.imgur.com/nepS3Lp.jpg");
        return builder.build();
    }

}
