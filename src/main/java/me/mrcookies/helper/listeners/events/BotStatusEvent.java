package me.mrcookies.helper.listeners.events;

import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.guild.GuildReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;
import java.time.Instant;
import java.util.List;

public class BotStatusEvent extends ListenerAdapter {

    @Override
    public void onGuildReady(GuildReadyEvent e) {

        TextChannel c = e.getGuild().getTextChannelById(References.idStatus);
        List<Message> msgs = c.getHistory().retrievePast(1).complete();

        if (msgs.size() > 0) {
            c.getHistory().retrievePast(1).queue(msg -> msg.get(0).editMessage(msg(e.getGuild().getEmoteById(References.online).getAsMention() + "``Online!``")).queue());
            return;
        }

        c.sendMessage(msg(e.getGuild().getEmoteById(References.online).getAsMention() + "``Online!``")).queue();
    }

    private MessageEmbed msg(String Description) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor("Status", null, "https://i.imgur.com/IUFgzzq.png");
        builder.setDescription(Description);
        builder.setColor(Color.decode("#fdcb6e"));
        builder.setFooter(References.h, "https://i.imgur.com/nepS3Lp.jpg");
        return builder.build();
    }

}
