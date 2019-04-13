package me.mrcookies.helper.redeem;

import me.mrcookies.helper.main.Core;
import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class RedeemEvent extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {

        if (e.getAuthor().isBot()) return;

        if (e.getChannel().getIdLong() != References.idRedeem) return;

        Message msg = e.getMessage();
        User usr = e.getAuthor();

        if (e.getMember().getRoles().stream().anyMatch(role -> role.getIdLong() == Core.getConfig().getYml().getLong("Roles.utility"))) {
            usr.openPrivateChannel().queue((ch) -> Methods.sendSENT(ch, "Redeem", "You can't redeem a prize."));
            msg.delete().queue();
            return;
        }

        if (msg.getContentRaw().startsWith(References.prefix)) {
            usr.openPrivateChannel().queue((ch) -> Methods.sendSENT(ch, "Redeem", "You can't execute commands in redeem channel."));
            msg.delete().queue();
            return;
        }

        if (msg.getContentRaw().length() != 16) {
            usr.openPrivateChannel().queue((ch) -> Methods.sendSENT(ch, "Redeem", "You can't write here, unless it's a license."));
            msg.delete().queue();
            return;
        }

    }

}
