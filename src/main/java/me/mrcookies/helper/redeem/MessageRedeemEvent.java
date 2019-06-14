package me.mrcookies.helper.redeem;

import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.guild.GuildReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;

public class MessageRedeemEvent extends ListenerAdapter {

    @Override
    public void onGuildReady(GuildReadyEvent e) {

        TextChannel rChannel = e.getJDA().getTextChannelById(References.idRedeem);

        rChannel.getHistory().retrievePast(1).queue((msgs) -> {

            if (msgs.size() > 0) return;

            sendRedeemHelp(rChannel);
        });

    }

    private void sendRedeemHelp(TextChannel channel) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor("Redeem", null, "https://i.imgur.com/WLQIKDX.png");
        builder.setDescription("Do you want to redeem your prize?\n\n**Type in your license!**");
        builder.setThumbnail("https://i.imgur.com/QVw0qZa.png");
        builder.setColor(Color.decode("#ce93d8"));
        builder.setFooter(References.h + " • Redeem", "https://i.imgur.com/nepS3Lp.jpg");

        channel.sendMessage(builder.build()).queue();
    }

}
