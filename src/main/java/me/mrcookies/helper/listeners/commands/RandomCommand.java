package me.mrcookies.helper.listeners.commands;

import me.mrcookies.helper.main.Core;
import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.ArrayList;

public class RandomCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        if (e.getAuthor().isBot()) return;

        if (Methods.returnChannels(e)) return;

        if (e.getMessage().getContentRaw().toLowerCase().startsWith(References.prefix + "random")) {

            if (Methods.isInvalidChannel(e.getChannel())) return;

            TextChannel channel = e.getChannel();
            User usr = e.getAuthor();

            if (!Methods.hasPermission(e, channel)) return;

            String[] msg = e.getMessage().getContentRaw().split(" ");

            if (msg.length != 2) {
                Methods.sendErrorMessage(channel, "Use • `random [number] | user`");
                return;
            }

            if (msg[1].equalsIgnoreCase("user")) {

                int size = Methods.getUsersSize();
                ArrayList<Long> ids = Methods.getUsersIDs();

                Methods.sendSENT(channel, "Random", usr.getAsMention() + " was extracted " + Core.getJDA().getUserById(ids.get(Methods.getRandom(size, 1))).getAsMention());
                return;
            }

            if (!Methods.isNumeric(msg[1])) {
                Methods.sendErrorMessage(channel, "Use • `random [numero] | user`");
                return;
            }

            int n = Integer.parseInt(msg[1]);

            if (n < 1) {
                Methods.sendErrorMessage(channel, "Invalid number `" + n + "`");
                return;
            }

            Methods.sendSENT(channel, "Random", usr.getAsMention() + " was extracted `" + Methods.getRandom(n, 1) + "` between `1` and `" + n + "`");
        }

    }

}
