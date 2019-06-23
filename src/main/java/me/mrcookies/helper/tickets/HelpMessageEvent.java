package me.mrcookies.helper.tickets;

import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

public class HelpMessageEvent extends ListenerAdapter {

    @Override
    public void onGuildReady(GuildReadyEvent e) {

        TextChannel tChannel = e.getJDA().getTextChannelById(References.idTickets);

        tChannel.getHistory().retrievePast(1).queue(msgs -> {

            if (msgs.size() > 0) return;

            sendTicketHelp(tChannel);
        });

    }

    private void sendTicketHelp(TextChannel channel) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor("Ticket", null, "https://i.imgur.com/nvQY65k.png");
        builder.setDescription("Do you want to receive direct support from us?\n\n**Type in your question or issue below and we will get to you as soon as possible!**");
        builder.setThumbnail("https://i.imgur.com/C1nbuwe.png");
        builder.setColor(Color.decode("#3498db"));
        builder.setFooter(References.h + " • Ticket", "https://i.imgur.com/nepS3Lp.jpg");

        channel.sendMessage(builder.build()).queue();
    }

}
