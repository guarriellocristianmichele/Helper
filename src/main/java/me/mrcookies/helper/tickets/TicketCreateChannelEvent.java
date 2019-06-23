package me.mrcookies.helper.tickets;

import me.mrcookies.helper.main.Core;
import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.*;

public class TicketCreateChannelEvent extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {

        if (e.getAuthor().isBot()) return;

        if (e.getChannel().getIdLong() != References.idTickets) return;

        Message msg = e.getMessage();
        User usr = e.getAuthor();
        String ms = e.getMessage().getContentRaw();

        if (e.getMember().getRoles().stream().anyMatch(role -> role.getIdLong() == Core.getConfig().getYml().getLong("Roles.utility"))) {
            usr.openPrivateChannel().queue((ch) -> Methods.sendSENT(ch, "Ticket", "You can't create a ticket."));
            msg.delete().queue();
            return;
        }

        if (msg.getContentRaw().startsWith(References.prefix)) {
            usr.openPrivateChannel().queue((ch) -> Methods.sendSENT(ch, "Ticket", "You can't execute commands in tickets creation channel."));
            msg.delete().queue();
            return;
        }

        if (hasOpenTicketChat(usr, e.getGuild())) {

            TextChannel tc = getOpenTicketChat(usr, e.getGuild());

            usr.openPrivateChannel().queue((ch) -> Methods.sendSENT(ch, "Ticket", "You have already a ticket opened " + Objects.requireNonNull(tc).getAsMention() + "\nIf you want to start another, please solve the opened one first."));
            msg.delete().queue();
            return;
        }

        if (msg.getContentRaw().length() < 4) {
            usr.openPrivateChannel().queue((ch) -> Methods.sendSENT(ch, "Ticket", "Description too short **(Min 4 letters)**."));
            msg.delete().queue();
            return;
        }

        createTicketChannel(usr, e.getGuild(), "**Description:** " + ms);
        msg.delete().queue();
    }

    private void createTicketChannel(User usr, Guild guild, String desc) {
        String name = "ticket-" + UUID.randomUUID().toString().split("-")[0];

        Category category = guild.getCategoryById(Core.getConfig().getYml().getLong("Category.tickets"));
        TextChannel logsChannel = guild.getTextChannelById(References.idLogs);
        Role supporter = guild.getRoleById(Core.getConfig().getYml().getLong("Roles.utility"));

        guild.createTextChannel(name)
                .setParent(category)
                .setTopic("Ticket from " + usr.getAsMention() + " | Problem Solved? Please type in **" + References.prefix + "solved**.")
                .queue(ticketChat -> {
                    ticketChat.getManager().clearOverridesRemoved();
                    ticketChat.getManager().clearOverridesAdded();

                    ticketChat.getManager()
                            .putPermissionOverride(supporter, getPermissions(), Collections.singletonList(Permission.MESSAGE_TTS))
                            .putPermissionOverride(guild.getMember(usr), getPermissions(), Collections.singletonList(Permission.MESSAGE_TTS))
                            .putPermissionOverride(guild.getPublicRole(), new ArrayList<>(), Arrays.asList(Permission.MESSAGE_READ, Permission.MESSAGE_WRITE))
                            .queue();

                    usr.openPrivateChannel().queue((ch) -> Methods.sendSENT(ch, "Ticket", "Your ticket has been created " + ticketChat.getAsMention() + "\nTo solve it, type in `" + References.prefix + "solved`."));
                    sendTicket(ticketChat, usr, desc);
                    sendLog(logsChannel, ticketChat, usr);

                });

    }

    private TextChannel getOpenTicketChat(User usr, Guild guild) {

        for (TextChannel channel : guild.getTextChannels()) {

            if (isTicketChat(channel)) {

                String topic = channel.getTopic();

                if (topic != null) {

                    if (topic.contains(usr.getAsMention())) {
                        return channel;
                    }

                }

            }

        }

        return null;
    }

    private boolean hasOpenTicketChat(User usr, Guild guild) {

        for (TextChannel channel : guild.getTextChannels()) {

            if (isTicketChat(channel)) {

                String topic = channel.getTopic();

                if (topic != null) {

                    if (topic.contains(usr.getAsMention())) {
                        return true;
                    }

                }

            }

        }

        return false;
    }

    private void sendTicket(TextChannel channel, User usr, String desc) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor("Ticket Info", null, "https://i.imgur.com/nvQY65k.png");
        builder.setDescription(desc);
        builder.setColor(Color.decode("#3498db"));
        builder.setFooter(References.h + " • Ticket created by " + usr.getName(), "https://i.imgur.com/nepS3Lp.jpg");

        channel.sendMessage(builder.build()).queue();
    }

    private boolean isTicketChat(TextChannel channel) {
        return channel.getName().contains("ticket-");
    }

    private void sendLog(TextChannel channel, TextChannel log, User open) {
        EmbedBuilder builder = new EmbedBuilder();

        builder.setAuthor("System", null, "https://i.imgur.com/IUFgzzq.png");
        builder.addField("Type:", "Ticket", false);
        builder.addField("Channel:", log.getName(), false);
        builder.addField("Opened by:", open.getName(), true);
        builder.setColor(Color.decode("#fdcb6e"));
        builder.setFooter(References.h + " • Ticket", "https://i.imgur.com/nepS3Lp.jpg");

        channel.sendMessage(builder.build()).queue();
    }

    private Collection<Permission> getPermissions() {

        Collection<Permission> permissionsAllow = new ArrayList<>();

        permissionsAllow.add(Permission.MESSAGE_ADD_REACTION);
        permissionsAllow.add(Permission.MESSAGE_ATTACH_FILES);
        permissionsAllow.add(Permission.MESSAGE_EMBED_LINKS);
        permissionsAllow.add(Permission.MESSAGE_READ);
        permissionsAllow.add(Permission.MESSAGE_WRITE);
        permissionsAllow.add(Permission.MESSAGE_HISTORY);

        return permissionsAllow;
    }

}
