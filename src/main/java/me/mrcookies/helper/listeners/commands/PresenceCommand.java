package me.mrcookies.helper.listeners.commands;

import me.mrcookies.helper.main.Core;
import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class PresenceCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        if (e.getAuthor().isBot()) return;

        if (Methods.returnChannels(e)) return;

        if (e.getMessage().getContentRaw().toLowerCase().startsWith(References.prefix + "presence")) {

            if (Methods.isInvalidChannel(e.getChannel())) return;

            TextChannel channel = e.getChannel();
            User usr = e.getAuthor();

            if (!Methods.hasPermission(e, channel)) return;

            String[] msg = e.getMessage().getContentRaw().split(" ");

            if (msg.length != 3) {
                Methods.sendErrorMessage(channel, "Use • `presence watch | play | listen [value]`");
                return;
            }

            switch (msg[1].toLowerCase()) {

                case "watch": {
                    Core.getJDA().getPresence().setGame(Game.watching(msg[2]));
                    break;
                }

                case "play": {
                    Core.getJDA().getPresence().setGame(Game.playing(msg[2]));
                    break;
                }

                case "listen": {
                    Core.getJDA().getPresence().setGame(Game.listening(msg[2]));
                    break;
                }

                default: {
                    Methods.sendErrorMessage(channel, "Use • `presence watch | play | listen [value]`");
                    break;
                }

            }

            Methods.sendSENT(channel, "System", "The presence has been changed.");
        }

    }

}
