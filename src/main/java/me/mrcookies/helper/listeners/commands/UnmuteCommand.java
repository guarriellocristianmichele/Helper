package me.mrcookies.helper.listeners.commands;

import me.mrcookies.helper.main.Core;
import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class UnmuteCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        if (e.getAuthor().isBot()) return;

        if (Methods.returnChannels(e)) return;

        if (e.getMessage().getContentRaw().toLowerCase().startsWith(References.prefix + "unmute")) {

            if (Methods.isInvalidChannel(e.getChannel())) return;

            TextChannel channel = e.getChannel();

            if (!Methods.hasPermission(e.getMember(), channel)) return;

            String[] msg = e.getMessage().getContentRaw().split(" ");

            if (msg.length != 2) {
                Methods.sendErrorMessage(channel, "Use • `unmute [@User]`");
                return;
            }

            if (e.getMessage().getMentionedMembers().isEmpty()) {
                Methods.sendErrorMessage(channel, "Use • `unmute [@User]`");
                return;
            }

            User target = e.getMessage().getMentionedUsers().get(0);

            if (target.isBot()) {
                Methods.sendErrorMessage(channel, "Invalid user.");
                return;
            }

            Member mem = e.getGuild().getMember(target);

            if (!mem.getRoles().contains(e.getGuild().getRoleById(Core.getConfig().getYml().getLong("Roles.muted")))) {
                Methods.sendErrorMessage(channel, "This user isn't muted.");
                return;
            }

            e.getGuild().getController().removeRolesFromMember(mem, e.getGuild().getRoleById(Core.getConfig().getYml().getLong("Roles.muted"))).queue();
            Core.getMySQL().setInt("members", "warns", 0, "id_long", String.valueOf(target.getIdLong()));
            Methods.sendSimpleEmbed(channel, "Security", target.getAsMention() + " have been unmuted.");
            target.openPrivateChannel().queue((ch) -> Methods.sendSENT(ch, "Security", "You have been unmuted."));
        }

    }

}
