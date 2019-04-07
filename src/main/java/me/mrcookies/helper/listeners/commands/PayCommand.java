package me.mrcookies.helper.listeners.commands;

import me.mrcookies.helper.main.Core;
import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class PayCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        if (e.getAuthor().isBot()) return;

        if (Methods.returnChannels(e)) return;

        if (e.getMessage().getContentRaw().toLowerCase().startsWith(References.prefix + "pay")) {

            if (Methods.isInvalidChannel(e.getChannel())) return;

            TextChannel channel = e.getChannel();
            User usr = e.getAuthor();

            String[] msg = e.getMessage().getContentRaw().split(" ");

            if (msg.length != 3) {
                Methods.sendErrorMessage(channel, "Use • `pay [@User] [amount] | all`");
                return;
            }

            if (e.getMessage().getMentionedMembers().isEmpty()) {
                Methods.sendErrorMessage(channel, "Use • `pay [@User] [amount] | all`");
                return;
            }

            User target = e.getMessage().getMentionedUsers().get(0);

            if (target.isBot()) {
                Methods.sendErrorMessage(channel, "Invalid number.");
                return;
            }

            if (msg[2].equalsIgnoreCase("all")) {

                int coins = Core.getMySQL().getCoins(usr.getIdLong());

                if (coins == 0) {
                    Methods.sendErrorMessage(channel, "**You don't have any coins.**");
                    return;
                }

                int sum = coins + Core.getMySQL().getCoins(target.getIdLong());

                Core.getMySQL().setInt("members", "coins", sum, "id_long", String.valueOf(target.getIdLong()));
                Core.getMySQL().setInt("members", "coins", 0, "id_long", String.valueOf(usr.getIdLong()));
                Methods.sendSimpleEmbed(channel, "Coins", "You sent all your coins (`" + coins + "`) to " + target.getAsMention());
                return;
            }

            if (!Methods.isNumeric(msg[2])) {
                Methods.sendErrorMessage(channel, "Invalid number `" + msg[2] + "`");
                return;
            }

            int n = Integer.parseInt(msg[2]);

            if (Core.getMySQL().getCoins(usr.getIdLong()) < n) {
                Methods.sendErrorMessage(channel, "You don't have `" + n + "` coins.");
                return;
            }

            int sum = Core.getMySQL().getCoins(target.getIdLong()) + n;
            int sub = Core.getMySQL().getCoins(usr.getIdLong()) - n;

            Core.getMySQL().setInt("members", "coins", sum, "id_long", String.valueOf(target.getIdLong()));
            Core.getMySQL().setInt("members", "coins", sub, "id_long", String.valueOf(usr.getIdLong()));
            Methods.sendSimpleEmbed(channel, "Coins", "You sent `" + n + "` coins to " + target.getAsMention());
        }

    }

}



