package me.mrcookies.helper.listeners.commands;

import me.mrcookies.helper.main.Core;
import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class EconomyCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        if (e.getAuthor().isBot()) return;

        if (Methods.returnChannels(e)) return;

        if (e.getMessage().getContentRaw().toLowerCase().startsWith(References.prefix + "eco")) {

            if (Methods.isInvalidChannel(e.getChannel())) return;

            TextChannel channel = e.getChannel();

            if (!Methods.hasPermission(e, channel)) return;

            String[] msg = e.getMessage().getContentRaw().split(" ");

            if (msg.length < 2) {
                Methods.sendErrorMessage(channel, "Use • `eco reset | add | remove`");
                return;
            }

            switch (msg[1].toLowerCase()) {

                case "reset": {

                    if (msg.length != 3) {
                        Methods.sendErrorMessage(channel, "Use • `eco reset [@User]`");
                        return;
                    }

                    if (e.getMessage().getMentionedMembers().isEmpty()) {
                        Methods.sendErrorMessage(channel, "Use • `eco reset [@User]`");
                        return;
                    }

                    User target = e.getMessage().getMentionedUsers().get(0);

                    if (target.isBot()) {
                        Methods.sendErrorMessage(channel, "Invalid user.");
                        return;
                    }

                    Core.getMySQL().setInt("members", "coins", 0, "id_long", String.valueOf(target.getIdLong()));
                    Methods.sendSimpleEmbed(channel, "Economy", "Reset coins to " + target.getAsMention());
                    break;
                }

                case "add": {

                    if (msg.length != 4) {
                        Methods.sendErrorMessage(channel, "Use • `eco add [@User] [amount]`");
                        return;
                    }

                    if (e.getMessage().getMentionedMembers().isEmpty()) {
                        Methods.sendErrorMessage(channel, "Use • `eco add [@User] [amount]`");
                        return;
                    }

                    if (!Methods.isNumeric(msg[3])) {
                        Methods.sendErrorMessage(channel, "Invalid number `" + msg[3] + "`");
                        return;
                    }

                    User target = e.getMessage().getMentionedUsers().get(0);

                    if (target.isBot()) {
                        Methods.sendErrorMessage(channel, "Invalid user.");
                        return;
                    }

                    int n = Integer.parseInt(msg[3]);

                    int sum = Core.getMySQL().getCoins(target.getIdLong()) + n;

                    Core.getMySQL().setInt("members", "coins", sum, "id_long", String.valueOf(target.getIdLong()));
                    Methods.sendSimpleEmbed(channel, "Economy", "Added `" + sum + "` coins to " + target.getAsMention());
                    break;
                }

                case "remove": {

                    if (msg.length != 4) {
                        Methods.sendErrorMessage(channel, "Use • `eco remove [@User] [amount]`");
                        return;
                    }

                    if (e.getMessage().getMentionedMembers().isEmpty()) {
                        Methods.sendErrorMessage(channel, "Use • `eco remove [@User] [amount]`");
                        return;
                    }

                    if (!Methods.isNumeric(msg[3])) {
                        Methods.sendErrorMessage(channel, "Invalid number `" + msg[3] + "`");
                        return;
                    }

                    User target = e.getMessage().getMentionedUsers().get(0);

                    if (target.isBot()) {
                        Methods.sendErrorMessage(channel, "Invalid user.");
                        return;
                    }

                    int n = Integer.parseInt(msg[3]);
                    int coins = Core.getMySQL().getCoins(target.getIdLong());

                    if (coins < n) {
                        Methods.sendErrorMessage(channel, target.getAsMention() + " hasn't `" + n + "` coins.");
                    }

                    int sub = coins - n;

                    Core.getMySQL().setInt("members", "coins", sub, "id_long", String.valueOf(target.getIdLong()));
                    Methods.sendSimpleEmbed(channel, "Economy", "Removed `" + sub + "` coins to " + target.getAsMention());
                    break;
                }

                default: {
                    Methods.sendErrorMessage(channel, "Use • `eco reset | add | remove`");
                    break;
                }

            }

        }

    }

}
