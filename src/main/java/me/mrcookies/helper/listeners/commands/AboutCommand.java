package me.mrcookies.helper.listeners.commands;

import me.mrcookies.helper.main.Core;
import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class AboutCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        if (e.getAuthor().isBot()) return;

        if (Methods.returnChannels(e)) return;

        if (e.getMessage().getContentRaw().equalsIgnoreCase(References.prefix + "about")) {

            if (Methods.isInvalidChannel(e.getChannel())) return;

            sendAbout(e.getChannel(), e.getGuild().getMembers().size());
        }

    }

    private void sendAbout(TextChannel channel, int size) {
        String statusUpdate = "Stable";
        EmbedBuilder b = new EmbedBuilder();
        b.setAuthor("Information", null, "https://i.imgur.com/IUFgzzq.png");
        b.setDescription("**Author** » Mr_Cookies\n**Version** » " + Core.getVersion() + " *(" + statusUpdate + ")*\n" + "**Prefix** » `" + References.prefix + "`" +
                "\n**Members** » " + size + "\n**Players Online** » " +
                getOnlinePlayerCount(Core.getConfig().getYml().getString("Server.ip"), Core.getConfig().getYml().getInt("Server.port")) + "/2000");
        b.addField("Minecraft", "mc.titannetwork.eu", true);
        b.addField("WebSite", "[Click here.](https://www.titannetwork.eu \"Titan Network\")", true);
        b.setThumbnail("https://i.imgur.com/tyKvUls.png");
        b.setColor(Color.decode("#fdcb6e"));
        b.setFooter(References.h, "https://i.imgur.com/nepS3Lp.jpg");

        channel.sendMessage(b.build()).queue();
    }

    private int getOnlinePlayerCount(String ip, int port) {
        try {

            Socket sock = new Socket();
            sock.setSoTimeout(100);
            sock.connect(new InetSocketAddress(ip, port), 100);

            DataOutputStream out = new DataOutputStream(sock.getOutputStream());
            DataInputStream in = new DataInputStream(sock.getInputStream());

            out.write(0xFE);

            int b;
            StringBuilder str = new StringBuilder();

            while ((b = in.read()) != -1) {

                if (b > 16 && b != 255 && b != 23 && b != 24) {
                    str.append((char) b);
                }

            }

            String[] data = str.toString().split("§");
            int onlinePlayers = Integer.parseInt(data[1]);

            sock.close();

            return onlinePlayers;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

}
