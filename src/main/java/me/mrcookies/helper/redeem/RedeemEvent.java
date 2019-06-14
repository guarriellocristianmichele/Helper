package me.mrcookies.helper.redeem;

import me.mrcookies.helper.main.Core;
import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;

public class RedeemEvent extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {

        if (e.getAuthor().isBot()) return;

        if (e.getChannel().getIdLong() != References.idRedeem) return;

        Message msg = e.getMessage();
        User usr = e.getAuthor();
        String mes = msg.getContentRaw();

        if (e.getMember().getRoles().stream().anyMatch(role -> role.getIdLong() == Core.getConfig().getYml().getLong("Roles.utility"))) {
            usr.openPrivateChannel().queue((ch) -> Methods.sendSENT(ch, "Redeem", "You can't redeem a license."));
            msg.delete().queue();
            return;
        }

        if (msg.getContentRaw().startsWith(References.prefix)) {
            usr.openPrivateChannel().queue((ch) -> Methods.sendSENT(ch, "Redeem", "You can't execute commands in redeem channel."));
            msg.delete().queue();
            return;
        }

        if (msg.getContentRaw().length() != 19) {
            usr.openPrivateChannel().queue((ch) -> Methods.sendSENT(ch, "Redeem", "You can only write licenses in redeem channel."));
            msg.delete().queue();
            return;
        }

        if (Core.getMySQL().getString("licenses", "license", "license", mes) == null) {
            usr.openPrivateChannel().queue((ch) -> Methods.sendSENT(ch, "Redeem", "License not found."));
            msg.delete().queue();
            return;
        }

        if (Core.getMySQL().isLicenseRedeemed(msg.getContentRaw())) {
            usr.openPrivateChannel().queue((ch) -> Methods.sendSENT(ch, "Redeem", "This license has already been redeemed."));
            msg.delete().queue();
            return;
        }

        int prize = Core.getMySQL().getInt("licenses", "value", "license", mes);
        int sum = Core.getMySQL().getCoins(usr.getIdLong()) + prize;
        Long created = Core.getMySQL().getLong("licenses", "created", "license", msg.getContentRaw());
        User create = e.getGuild().getMemberById(created).getUser();
        TextChannel logsChannel = e.getGuild().getTextChannelById(References.idLogs);

        Core.getMySQL().setInt("members", "coins", sum, "id_long", String.valueOf(usr.getIdLong()));
        Core.getMySQL().redeemLicense(mes, usr.getIdLong());
        usr.openPrivateChannel().queue((ch) -> Methods.sendSENT(ch, "Redeem", "License redeemed successfully.\n\n**You claimed:** `" + prize + "` coins"));
        sendLog(msg.getContentRaw(), logsChannel, create, usr, prize);
        msg.delete().queue();
    }

    private void sendLog(String license, TextChannel channel, User create, User redeem, int value) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor("System", null, "https://i.imgur.com/IUFgzzq.png");
        builder.addField("Type:", "License", false);
        builder.addField("License:", license, false);
        builder.addField("Created by:", create.getName(), false);
        builder.addField("Redeemed by:", redeem.getName(), false);
        builder.addField("Value:", String.valueOf(value), false);
        builder.setThumbnail("https://i.imgur.com/QVw0qZa.png");
        builder.setColor(Color.decode("#fdcb6e"));
        builder.setFooter(References.h + " • License", "https://i.imgur.com/nepS3Lp.jpg");

        channel.sendMessage(builder.build()).queue();
    }

}
