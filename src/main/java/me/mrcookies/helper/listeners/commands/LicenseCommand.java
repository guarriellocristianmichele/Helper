package me.mrcookies.helper.listeners.commands;

import me.mrcookies.helper.main.Core;
import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;
import java.util.ArrayList;

public class LicenseCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        if (e.getAuthor().isBot()) return;

        if (Methods.returnChannels(e)) return;

        if (e.getMessage().getContentRaw().toLowerCase().startsWith(References.prefix + "license")) {

            if (e.getChannel().getIdLong() != References.idStaffCommands) {
                Methods.sendErrorMessage(e.getChannel(), "This command can't be perfomed here.");
                return;
            }

            TextChannel channel = e.getChannel();
            User usr = e.getAuthor();

            if (!Methods.hasPermission(e, channel)) return;

            String[] msg = e.getMessage().getContentRaw().split(" ");

            if (msg.length < 2) {
                Methods.sendErrorMessage(channel, "Use • `license create | delete | list | info`");
                return;
            }

            TextChannel logsChannel = e.getGuild().getTextChannelById(References.idLogs);

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
                    sendLog(license, logsChannel, usr, null, value, true);
                    break;
                }

                case "delete": {

                    if (msg.length != 3) {
                        Methods.sendErrorMessage(channel, "Use • `license delete [license]`");
                        return;
                    }

                    if (msg[2].length() != 19) {
                        Methods.sendErrorMessage(channel, "Invalid license.");
                        return;
                    }

                    if (Core.getMySQL().getString("licenses", "license", "license", msg[2]) == null) {
                        Methods.sendErrorMessage(channel, "License not found.");
                        return;
                    }

                    int value = Core.getMySQL().getInt("licenses", "value", "license", msg[2]);
                    User redeem = null;

                    if (Core.getMySQL().isLicenseRedeemed(msg[2])) {
                        Long redeemed = Core.getMySQL().getLong("licenses", "redeemed", "license", msg[2]);
                        redeem = e.getGuild().getMemberById(redeemed).getUser();
                    }

                    sendLog(msg[2], logsChannel, usr, redeem, value, false);
                    Core.getMySQL().dropEntry("licenses", "license", msg[2]);
                    Methods.sendSENT(channel, "License", "License deleted successfully.");
                    break;
                }

                case "list": {

                    if (msg.length != 3) {
                        Methods.sendErrorMessage(channel, "Use • `license list [number]`");
                        return;
                    }

                    if (!Methods.isNumeric(msg[2])) {
                        Methods.sendErrorMessage(channel, "Invalid number `" + msg[2] + "`");
                        return;
                    }

                    int n = Integer.parseInt(msg[2]);

                    if (n > 20) {
                        Methods.sendErrorMessage(channel, "Maximum `20` licenses.");
                        return;
                    }

                    sendLicenses(channel, n);
                    break;
                }

                case "info": {

                    if (msg.length != 3) {
                        Methods.sendErrorMessage(channel, "Use • `license info [license]`");
                        return;
                    }

                    if (msg[2].length() != 19) {
                        Methods.sendErrorMessage(channel, "Invalid license.");
                        return;
                    }

                    if (Core.getMySQL().getString("licenses", "license", "license", msg[2]) == null) {
                        Methods.sendErrorMessage(channel, "License not found.");
                        return;
                    }

                    User redeem = null;
                    int value = Core.getMySQL().getInt("licenses", "value", "license", msg[2]);
                    Long created = Core.getMySQL().getLong("licenses", "created", "license", msg[2]);
                    User create = e.getGuild().getMemberById(created).getUser();

                    if (Core.getMySQL().isLicenseRedeemed(msg[2])) {
                        Long redeemed = Core.getMySQL().getLong("licenses", "redeemed", "license", msg[2]);
                        redeem = e.getGuild().getMemberById(redeemed).getUser();
                    }

                    sendInfo(msg[2], channel, create, redeem, value);
                    break;
                }

                default: {
                    Methods.sendErrorMessage(channel, "Use • `license create | delete | list | info`");
                    break;
                }

            }

        }

    }

    private void sendLog(String license, TextChannel channel, User usr, User redeem, int value, boolean create) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor("System", null, "https://i.imgur.com/IUFgzzq.png");
        builder.addField("Type:", "License", false);
        builder.addField("License:", license, false);

        if (create) {
            builder.addField("Created by:", usr.getName(), false);
        } else {
            builder.addField("Deleted by:", usr.getName(), false);
        }

        if (redeem != null) {
            builder.addField("Redeemed by:", redeem.getName(), false);
        }

        builder.addField("Value:", String.valueOf(value), false);
        builder.setThumbnail("https://i.imgur.com/QVw0qZa.png");
        builder.setColor(Color.decode("#fdcb6e"));
        builder.setFooter("Helper • License", "https://i.imgur.com/nepS3Lp.jpg");

        channel.sendMessage(builder.build()).queue();
    }

    private void sendInfo(String license, TextChannel channel, User usr, User redeem, int value) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor("License", null, "https://i.imgur.com/WLQIKDX.png");
        builder.addField("License:", license, false);
        builder.addField("Created by:", usr.getName(), false);

        if (redeem != null) {
            builder.addField("Redeemed by:", redeem.getName(), false);
        }

        builder.addField("Value:", String.valueOf(value), false);
        builder.setColor(Color.decode("#ce93d8"));
        builder.setFooter("Helper • Info", "https://i.imgur.com/nepS3Lp.jpg");

        channel.sendMessage(builder.build()).queue();
    }

    private void sendLicenses(TextChannel channel, int n) {
        ArrayList<String> licenses = Core.getMySQL().getLicenses(0, n);
        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor("Licenses", null, "https://i.imgur.com/WLQIKDX.png");

        if (licenses != null) {

            for (String license : licenses) {

                int value = Core.getMySQL().getInt("licenses", "value", "license", license);

                builder.addField("License", license, true);
                builder.addField("Value", String.valueOf(value), true);

                if (Core.getMySQL().isLicenseRedeemed(license)) {
                    builder.addField("Redeemed", "Yes", true);
                } else {
                    builder.addField("Redeemed", "No", true);
                }

            }

        } else {
            builder.setDescription("There are no licenses.");
        }

        builder.setColor(Color.decode("#ce93d8"));
        builder.setFooter("Helper • Info", "https://i.imgur.com/nepS3Lp.jpg");

        channel.sendMessage(builder.build()).queue();
    }

}
