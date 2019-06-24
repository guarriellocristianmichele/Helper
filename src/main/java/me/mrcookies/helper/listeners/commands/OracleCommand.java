package me.mrcookies.helper.listeners.commands;

import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

public class OracleCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        if (e.getAuthor().isBot()) return;

        if (Methods.returnChannels(e)) return;

        if (e.getMessage().getContentRaw().toLowerCase().startsWith(References.prefix + "oracle")) {

            if (Methods.isInvalidChannel(e.getChannel())) return;

            TextChannel channel = e.getChannel();

            String[] msgs = e.getMessage().getContentRaw().split(References.prefix + "oracle ");

            if (msgs.length != 2) {
                Methods.sendErrorMessage(channel, "Use • `oracle [question]`");
                return;
            }

            String[] arr = {
                    "As I see it, yes",
                    "It is certain",
                    "It is decidedly so",
                    "Most likely",
                    "Outlook good",
                    "Signs point to yes",
                    "Without a doubt",
                    "Yes",
                    "Yes – definitely",
                    "You may rely on it",
                    "Reply hazy, try again",
                    "Ask again later",
                    "Better not tell you now",
                    "Cannot predict now",
                    "Concentrate and ask again",
                    "Don't count on it",
                    "My reply is no",
                    "My sources say no",
                    "Outlook not so good",
                    "Very doubtful"
            };

            int ran = Methods.getRandom(arr.length, 0);

            if (ran <= 9) {
                sendOracleResponse(channel, msgs[1], arr[ran], "#2ecc71", "https://i.imgur.com/p3owVKR.png");
                return;
            }

            if (ran < 14) {
                sendOracleResponse(channel, msgs[1], arr[ran], "#f1c40f", "https://i.imgur.com/t0XlqC6.png");
                return;
            }

            if (ran > 14) {
                sendOracleResponse(channel, msgs[1], arr[ran], "#e74c3c", "https://i.imgur.com/FtoMNlh.png");
                return;
            }

            Methods.sendErrorMessage(channel, "The oracle had a problem.");
        }

    }

    private void sendOracleResponse(TextChannel channel, String message, String response, String color, String url) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor("Oracle", null, url);

        String correct = message.substring(0, 1).toUpperCase() + message.substring(1);

        if (!message.contains("?")) {
            builder.setDescription("*" + correct + "?*\n\n`" + response + ".`");
        } else {
            builder.setDescription("*" + correct + "*\n\n`" + response + ".`");
        }

        builder.setColor(Color.decode(color));
        builder.setFooter(References.h, "https://i.imgur.com/nepS3Lp.jpg");

        channel.sendMessage(builder.build()).queue();
    }

}
