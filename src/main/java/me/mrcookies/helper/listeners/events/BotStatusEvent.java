package me.mrcookies.helper.listeners.events;

import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

public class BotStatusEvent extends ListenerAdapter {

    @Override
    public void onGuildReady(GuildReadyEvent e) {

        TextChannel c = e.getGuild().getTextChannelById(References.idStatus);

        c.getHistory().retrievePast(1).queue(msgs -> {

            if (msgs.size() > 0) {
                msgs.get(0).editMessage(msg(e.getGuild().getEmoteById(References.online).getAsMention() + "``Online!``")).queue();
            }

        });

        c.sendMessage(msg(e.getGuild().getEmoteById(References.online).getAsMention() + "``Online!``")).queue();
    }

    private MessageEmbed msg(String Description) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor("Status", null, "https://i.imgur.com/IUFgzzq.png");
        builder.setDescription(Description);
        builder.setColor(Color.decode("#fdcb6e"));
        builder.setFooter(References.h + " • Hello world!", "https://i.imgur.com/nepS3Lp.jpg");
        return builder.build();
    }

}
