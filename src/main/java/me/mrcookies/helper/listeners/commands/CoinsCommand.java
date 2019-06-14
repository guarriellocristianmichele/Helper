package me.mrcookies.helper.listeners.commands;

import me.mrcookies.helper.main.Core;
import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class CoinsCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        if (e.getAuthor().isBot()) return;

        if (Methods.returnChannels(e)) return;

        if (e.getMessage().getContentRaw().toLowerCase().startsWith(References.prefix + "coins")) {

            if (Methods.isInvalidChannel(e.getChannel())) return;

            TextChannel channel = e.getChannel();
            User usr = e.getAuthor();
            int coins;

            String[] msg = e.getMessage().getContentRaw().split(" ");

            if (msg.length > 2) {
                Methods.sendErrorMessage(channel, "Use • `coins | [@User]`");
                return;
            }

            if (msg.length == 1) {
                coins = Core.getMySQL().getCoins(usr.getIdLong());
                Methods.sendSENT(channel, "Coins", "You have `" + coins + "`");
                return;
            }

            if (msg.length == 2) {

                if (e.getMessage().getMentionedMembers().isEmpty()) {
                    Methods.sendErrorMessage(channel, "Use • `coins [@User]`");
                    return;
                }

                User target = e.getMessage().getMentionedUsers().get(0);

                if (target.isBot()) {
                    Methods.sendErrorMessage(channel, "Invalid user.");
                    return;
                }

                coins = Core.getMySQL().getCoins(target.getIdLong());

                Methods.sendSENT(channel, "Coins", target.getAsMention() + " has `" + coins + "`");
            }

        }

    }

}
