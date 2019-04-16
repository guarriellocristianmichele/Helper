package me.mrcookies.helper.listeners.commands;

import me.mrcookies.helper.main.Core;
import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.List;

public class LicenseCreateCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        if (e.getAuthor().isBot()) return;

        if (Methods.returnChannels(e)) return;

        if (e.getMessage().getContentRaw().toLowerCase().startsWith(References.prefix + "license")) {

            if (Methods.isInvalidChannel(e.getChannel())) return;

            TextChannel channel = e.getChannel();
            User usr = e.getAuthor();
            List<String> types = new ArrayList<>();
            types.add("coins");

            if (!Methods.hasPermission(e, channel)) return;

            String[] msg = e.getMessage().getContentRaw().split(" ");

            if (msg.length < 2) {
                Methods.sendErrorMessage(channel, "Use • `license create | delete`");
                return;
            }

            switch (msg[1].toLowerCase()) {

                case "create": {

                    if (msg.length != 3) {
                        Methods.sendErrorMessage(channel, "Use • `license create [value]`");
                        return;
                    }

                    if (!Methods.isNumeric(msg[2])) {
                        Methods.sendErrorMessage(channel, "Invalid value `" + msg[2] + "`");
                        return;
                    }

                    String license = Methods.getRandomLicense();
                    int value = Integer.parseInt(msg[2]);

                    Core.getMySQL().createLicense(license, value, usr.getIdLong());
                    Methods.sendSENT(channel, "License", "License created successfully.\n\n**Your license:** `" + license + "`");
                    break;
                }

                case "delete": {

                    if (msg.length != 3) {
                        Methods.sendErrorMessage(channel, "Use • `license delete [license]`");
                        return;
                    }

                    if (msg[3].length() != 16) {
                        Methods.sendErrorMessage(channel, "Invalid license.");
                        return;
                    }

                    if (Core.getMySQL().getString("licenses", "license", "license", msg[2]) == null) {
                        Methods.sendErrorMessage(channel, "License not found.");
                        return;
                    }

                    Core.getMySQL().dropEntry("licenses", "license", msg[2]);
                    Methods.sendSENT(channel, "License", "License deleted successfully.");
                    break;
                }

                default: {
                    Methods.sendErrorMessage(channel, "Use • `license create | delete`");
                    break;
                }

            }

        }

    }

}
