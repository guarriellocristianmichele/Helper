package me.mrcookies.helper.listeners.commands;

import me.mrcookies.helper.main.Core;
import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class LinkCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        if (e.getAuthor().isBot()) return;

        if (Methods.returnChannels(e)) return;

        if (e.getMessage().getContentRaw().toLowerCase().startsWith(References.prefix + "link")) {

            if (Methods.isInvalidChannel(e.getChannel())) return;

            TextChannel channel = e.getChannel();
            User usr = e.getAuthor();

            String[] msg = e.getMessage().getContentRaw().split(" ");

            if (msg.length < 2) {
                Methods.sendErrorMessage(channel, "Use • `link (facebook | instagram) [link]`");
                return;
            }

            switch (msg[1].toLowerCase()) {

                case "facebook": {

                    if (msg.length != 3) {
                        Methods.sendErrorMessage(channel, "Use • `link facebook [link]`");
                        return;
                    }

                    String url = msg[2].toLowerCase();

                    if (!url.startsWith("https://www.facebook.com") && !url.startsWith("http://www.facebook.com")) {
                        Methods.sendErrorMessage(channel, "Invalid URL.");
                        return;
                    }

                    if (Core.getMySQL().hasSocial(usr.getIdLong(), "facebook")) {
                        Methods.sendSimpleEmbed(channel, "Social", "You have updated your facebook link.");
                    } else {
                        Methods.sendSimpleEmbed(channel, "Social", "You have associated facebook to your account.");
                    }

                    Core.getMySQL().setString("members", "facebook", url, "id_long", String.valueOf(usr.getIdLong()));
                    break;
                }

                case "instagram": {

                    if (msg.length != 3) {
                        Methods.sendErrorMessage(channel, "Use • `link instagram [link]`");
                        return;
                    }

                    String url = msg[2].toLowerCase();

                    if (!url.startsWith("https://www.instagram.com") && !url.startsWith("http://www.instagram.com")) {
                        Methods.sendErrorMessage(channel, "Invalid URL.");
                        return;
                    }

                    if (Core.getMySQL().hasSocial(usr.getIdLong(), "instagram")) {
                        Methods.sendSimpleEmbed(channel, "Social", "You have updated your instagram link.");
                    } else {
                        Methods.sendSimpleEmbed(channel, "Social", "You have associated instagram to your account.");
                    }

                    Core.getMySQL().setString("members", "instagram", url, "id_long", String.valueOf(usr.getIdLong()));
                    break;
                }

                default: {
                    Methods.sendErrorMessage(channel, "Use • `link (facebook | instagram) [link]`");
                    break;
                }

            }

        }

    }

}
