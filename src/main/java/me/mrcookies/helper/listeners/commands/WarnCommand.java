package me.mrcookies.helper.listeners.commands;

import me.mrcookies.helper.main.Core;
import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class WarnCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        if (e.getAuthor().isBot()) return;

        if (Methods.returnChannels(e)) return;

        if (e.getMessage().getContentRaw().toLowerCase().startsWith(References.prefix + "warn")) {

            if (Methods.isInvalidChannel(e.getChannel())) return;

            TextChannel channel = e.getChannel();

            if (!Methods.hasPermission(e, channel)) return;

            String[] msg = e.getMessage().getContentRaw().split(" ");

            if (msg.length < 2 || msg.length > 3) {
                Methods.sendErrorMessage(channel, "Use • `warn [@User] | reset [@User]`");
                return;
            }

            if (msg.length == 3) {

                if (!msg[1].equalsIgnoreCase("reset")) {
                    Methods.sendErrorMessage(channel, "Use • `warn [@User] | reset [@User]`");
                    return;
                }

                if (e.getMessage().getMentionedMembers().isEmpty()) {
                    Methods.sendErrorMessage(channel, "Use • `warn reset [@User]`");
                    return;
                }

                User target = e.getMessage().getMentionedUsers().get(0);

                if (target.isBot()) {
                    Methods.sendErrorMessage(channel, "Invalid user.");
                    return;
                }

                Member mem = e.getGuild().getMember(target);

                if (Methods.isStaffer(mem)) {
                    Methods.sendErrorMessage(channel, "You can't use this command on this user.");
                    return;
                }

                int warns = Core.getMySQL().getWarns(target.getIdLong());

                if (warns == 0) {
                    Methods.sendErrorMessage(channel, "This user has no warns.");
                    return;
                }

                Core.getMySQL().setInt("members", "warns", 0, "id_long", String.valueOf(target.getIdLong()));
                Methods.sendSimpleEmbed(channel, "Security", "Reset warns to " + target.getAsMention());
                target.openPrivateChannel().queue((ch) -> Methods.sendSENT(ch, "Security", "Your warns have been reset."));
                return;
            }

            if (e.getMessage().getMentionedMembers().isEmpty()) {
                Methods.sendErrorMessage(channel, "Use • `warn [@User]`");
                return;
            }

            User target = e.getMessage().getMentionedUsers().get(0);

            if (target.isBot()) {
                Methods.sendErrorMessage(channel, "Invalid user.");
                return;
            }

            Member mem = e.getGuild().getMember(target);

            if (Methods.isStaffer(mem)) {
                Methods.sendErrorMessage(channel, "You can't warn this user.");
                return;
            }

            int warns = Core.getMySQL().getWarns(target.getIdLong());

            if (warns == 3) {
                e.getGuild().getController().addRolesToMember(mem, e.getGuild().getRoleById(Core.getConfig().getYml().getLong("Roles.muted"))).queue();
                Methods.sendSENT(channel, "Security", target.getAsMention() + " have been muted.");
                target.openPrivateChannel().queue((ch) -> Methods.sendSENT(ch, "Security", "You have been muted, (warns `3/3`)."));
                return;
            }

            Methods.sendSENT(channel, "Security", target.getAsMention() + " have been warned.");
            Core.getMySQL().setInt("members", "warns", warns + 1, "id_long", String.valueOf(target.getIdLong()));

            target.openPrivateChannel().queue((ch) -> {

                if (warns + 1 == 3) {
                    Methods.sendSENT(ch, "Security", "You have been warned (`" + (warns + 1) + "/3`), this is your last chance.");
                } else {
                    Methods.sendSENT(ch, "Security", "You have been warned (`" + (warns + 1) + "/3`)");
                }

            });

        }

    }

}
