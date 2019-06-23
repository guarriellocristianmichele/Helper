package me.mrcookies.helper.listeners.commands;

import me.mrcookies.helper.main.Core;
import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class PresenceCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        if (e.getAuthor().isBot()) return;

        if (Methods.returnChannels(e)) return;

        if (e.getMessage().getContentRaw().toLowerCase().startsWith(References.prefix + "presence")) {

            if (Methods.isInvalidChannel(e.getChannel())) return;

            TextChannel channel = e.getChannel();

            if (!Methods.hasPermission(e.getMember(), channel)) return;

            String[] msg = e.getMessage().getContentRaw().split(" ");
            String[] text = e.getMessage().getContentRaw().split(" - ");

            if (text.length != 2) {
                Methods.sendErrorMessage(channel, "Use • `presence watch | play | listen - [value]`");
                return;
            }

            switch (msg[1].toLowerCase()) {

                case "watch": {
                    Core.getJDA().getPresence().setActivity(Activity.watching(text[1]));
                    Methods.sendSENT(channel, "System", "The presence has been changed.");
                    break;
                }

                case "play": {
                    Core.getJDA().getPresence().setActivity(Activity.playing(text[1]));
                    Methods.sendSENT(channel, "System", "The presence has been changed.");
                    break;
                }

                case "listen": {
                    Core.getJDA().getPresence().setActivity(Activity.listening(text[1]));
                    Methods.sendSENT(channel, "System", "The presence has been changed.");
                    break;
                }

                default: {
                    Methods.sendErrorMessage(channel, "Use • `presence watch | play | listen - [value]`");
                    break;
                }

            }

        }

    }

}
