package me.mrcookies.helper.listeners.commands;

import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;

public class RandomCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        if (e.getAuthor().isBot()) return;

        if (Methods.returnChannels(e)) return;

        if (e.getMessage().getContentRaw().toLowerCase().startsWith(References.prefix + "random")) {

            if (Methods.isInvalidChannel(e.getChannel())) return;

            TextChannel channel = e.getChannel();

            if (!Methods.hasPermission(e.getMember(), channel)) return;

            String[] msg = e.getMessage().getContentRaw().split(" ");

            if (msg.length != 2) {
                Methods.sendErrorMessage(channel, "Use • `random [number] | user`");
                return;
            }

            if (msg[1].equalsIgnoreCase("user")) {

                int size = Methods.getUsersSize();
                ArrayList<Long> ids = Methods.getUsersIDs();
                Member extracted = e.getGuild().getMemberById(ids.get(Methods.getRandom(size, 0)));

                Methods.sendSENT(channel, "Random", "Your random user is " + extracted.getAsMention());
                return;
            }

            int n;

            try {
                n = Integer.parseInt(msg[1]);
            } catch (NumberFormatException ex) {
                Methods.sendErrorMessage(channel, "Invalid number");
                return;
            }

            if (n < 1) {
                Methods.sendErrorMessage(channel, "Invalid number");
                return;
            }

            Methods.sendSENT(channel, "Random", "Your random number is `" + Methods.getRandom(n, 1) + "` between `1` and `" + n + "`");
        }

    }

}
