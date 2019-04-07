package me.mrcookies.helper.listeners.commands;

import me.mrcookies.helper.main.Core;
import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class AccountResetCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        if (e.getAuthor().isBot()) return;

        if (Methods.returnChannels(e)) return;

        if (e.getMessage().getContentRaw().toLowerCase().startsWith(References.prefix + "reset")) {

            if (Methods.isInvalidChannel(e.getChannel())) return;

            TextChannel channel = e.getChannel();

            if (!Methods.hasPermission(e, channel)) return;

            String[] msg = e.getMessage().getContentRaw().split(" ");

            if (msg.length != 2) {
                Methods.sendErrorMessage(channel, "Use • `reset [@User] | all`");
                return;
            }

            if (msg[1].equalsIgnoreCase("all")) {

                for (User usr : Core.getJDA().getUsers()) {

                    if (usr.isBot()) {
                        continue;
                    }

                    resetAccount(usr);
                }

                Methods.sendSimpleEmbed(channel, "System", "Reset all accounts.");
                return;
            }

            if (e.getMessage().getMentionedMembers().isEmpty()) {
                Methods.sendErrorMessage(channel, "Use • `reset [@User] | all`");
                return;
            }

            User target = e.getMessage().getMentionedUsers().get(0);

            if (target.isBot()) {
                Methods.sendErrorMessage(channel, "Invalid user.");
                return;
            }

            resetAccount(target);
            Methods.sendSimpleEmbed(channel, "System", "Reset of " + target.getAsMention() + "'s account.");
        }

    }

    private void resetAccount(User usr) {
        Core.getMySQL().setInt("members", "coins", 0, "id_long", String.valueOf(usr.getIdLong()));
        Core.getMySQL().setInt("members", "daily_start", 0, "id_long", String.valueOf(usr.getIdLong()));
        Core.getMySQL().setInt("members", "rob_start", 0, "id_long", String.valueOf(usr.getIdLong()));
        Core.getMySQL().setInt("members", "warns", 0, "id_long", String.valueOf(usr.getIdLong()));
        Core.getMySQL().setString("members", "facebook", null, "id_long", String.valueOf(usr.getIdLong()));
        Core.getMySQL().setString("members", "instagram", null, "id_long", String.valueOf(usr.getIdLong()));
    }

}
