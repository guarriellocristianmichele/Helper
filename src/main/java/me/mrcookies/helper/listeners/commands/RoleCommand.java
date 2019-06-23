package me.mrcookies.helper.listeners.commands;

import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class RoleCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        if (e.getAuthor().isBot()) return;

        if (Methods.returnChannels(e)) return;

        if (e.getMessage().getContentRaw().toLowerCase().startsWith(References.prefix + "role")) {

            if (Methods.isInvalidChannel(e.getChannel())) return;

            TextChannel channel = e.getChannel();

            if (!Methods.hasPermission(e.getMember(), channel)) return;

            String[] msgs = e.getMessage().getContentRaw().split(" ");

            if (msgs.length != 4) {
                Methods.sendErrorMessage(channel, "Use • `role [add/remove] [role] [@User]`");
                return;
            }

            if (e.getGuild().getRoles().stream().noneMatch(role -> role.getName().equals(msgs[2]))) {
                Methods.sendErrorMessage(channel, "Invalid role `" + msgs[2] + "`");
                return;
            }

            switch (msgs[1].toLowerCase()) {

                case "add": {

                    if (e.getMessage().getMentionedMembers().isEmpty()) {
                        Methods.sendErrorMessage(channel, "Use • `role add [role] [@User]`");
                        return;
                    }

                    User target = e.getMessage().getMentionedUsers().get(0);

                    if (target.isBot()) {
                        Methods.sendErrorMessage(channel, "Invalid user.");
                        return;
                    }

                    e.getGuild().addRoleToMember(e.getGuild().getMember(target), e.getGuild().getRolesByName(msgs[2], true).get(0)).queue();

                    Methods.sendSimpleEmbed(channel, "Roles", "Added `" + msgs[2] + "` to " + target.getAsMention());
                    break;
                }

                case "remove": {

                    if (e.getMessage().getMentionedMembers().isEmpty()) {
                        Methods.sendErrorMessage(channel, "Use • `role remove [role] [@User]`");
                        return;
                    }

                    User target = e.getMessage().getMentionedUsers().get(0);

                    if (target.isBot()) {
                        Methods.sendErrorMessage(channel, "Invalid user.");
                        return;
                    }

                    e.getGuild().removeRoleFromMember(e.getGuild().getMember(target),
                            e.getGuild().getRolesByName(msgs[2], true).get(0)).queue();

                    Methods.sendSimpleEmbed(channel, "Roles", "Removed `" + msgs[2] + "` to " + target.getAsMention());
                    break;
                }

                default: {
                    Methods.sendErrorMessage(channel, "Use • `role [add/remove] [role] [@User]`");
                    break;
                }

            }

        }

    }

}
