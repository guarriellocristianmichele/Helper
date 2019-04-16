package me.mrcookies.helper.listeners.commands;

import me.mrcookies.helper.main.Core;
import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.time.OffsetDateTime;

public class ProfileCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        if (e.getAuthor().isBot()) return;

        if (Methods.returnChannels(e)) return;

        if (e.getMessage().getContentRaw().toLowerCase().startsWith(References.prefix + "profile")) {

            if (Methods.isInvalidChannel(e.getChannel())) return;

            TextChannel channel = e.getChannel();
            User usr = e.getAuthor();
            Member mem = e.getMember();

            String[] msg = e.getMessage().getContentRaw().split(" ");

            if (msg.length > 2) {
                Methods.sendErrorMessage(channel, "Use • `profile | profile [@User]`");
                return;
            }

            if (msg.length == 1) {
                createProfileImage(channel, e.getMember());
                return;
            }

            if (msg.length == 2) {

                if (e.getMessage().getMentionedMembers().isEmpty()) {
                    Methods.sendErrorMessage(channel, "Use • `profile [@User]`");
                    return;
                }

                User target = e.getMessage().getMentionedUsers().get(0);

                if (target.isBot()) {
                    Methods.sendErrorMessage(channel, "Invalid user.");
                    return;
                }

                Member targ = e.getGuild().getMember(target);

                createProfileImage(channel, targ);
            }

        }

    }

    /*private void sendProfile(TextChannel channel, User usr, Member mem, Guild guild) {
        EmbedBuilder builder = new EmbedBuilder();
        String url;
        int warns = Core.getMySQL().getWarns(usr.getIdLong());
        builder.setAuthor("Profile of " + usr.getName(), null, "https://i.imgur.com/IUFgzzq.png");
        builder.appendDescription("```There will be all informations about " + usr.getName() + ".```");

        if (mem.getNickname() != null) {
            builder.addField("Nickname:", "`" + mem.getNickname() + "`", false);
        }

        builder.addField("Tag:", "`" + usr.getAsTag() + "`", false);
        builder.addField("ID:", "`" + usr.getIdLong() + "`", false);
        builder.addField("Role:", "`" + mem.getRoles().get(0).getName() + "`", true);
        builder.addField("Coins:", "`" + Core.getMySQL().getCoins(usr.getIdLong()) + "`", true);

        if (warns > 3) {
            builder.addField("Status:", "Muted", false);
        } else {
            builder.addField("Warns:", "`" + warns + "/3`", false);
        }

        builder.appendDescription("\n");
        builder.setColor(Color.decode("#fdcb6e"));
        builder.setFooter("Helper", "https://i.imgur.com/nepS3Lp.jpg");
        builder.setTimestamp(Instant.now());

        if (usr.getDefaultAvatarUrl() != null) {
            builder.setThumbnail(usr.getAvatarUrl());
        }

        if (!Core.getMySQL().hasSocial(usr.getIdLong())) {
            builder.addField("Social:", "No social linked.", false);
        }

        if (Core.getMySQL().hasSocial(usr.getIdLong(), "facebook")) {
            url = Core.getMySQL().getString("members", "facebook", "id_long", String.valueOf(usr.getIdLong()));
            builder.addField(guild.getEmoteById(References.facebook).getAsMention(), "[Click here.](" + url + " \"Profile\")", true);
        }

        if (Core.getMySQL().hasSocial(usr.getIdLong(), "instagram")) {
            url = Core.getMySQL().getString("members", "instagram", "id_long", String.valueOf(usr.getIdLong()));
            builder.addField(guild.getEmoteById(References.instagram).getAsMention(), "[Click here.](" + url + " \"Profile\")", true);
        }

        channel.sendMessage(builder.build()).queue();
    }*/

    private void createProfileImage(TextChannel channel, Member mem) {

        Font customFont = null;
        File pathToFile = new File("Helper/Images/Form.png");
        BufferedImage image = null;
        Image avatar = null;
        URL url;
        User usr = mem.getUser();
        OffsetDateTime date = mem.getJoinDate();
        String format = Methods.getCap(date.getDayOfWeek().toString().substring(0, 3).toLowerCase()) + ", " + Methods.getCap(date.getMonth().name().toLowerCase()) + " " + date.getDayOfMonth() + ", " + date.getYear();
        int coins = Core.getMySQL().getCoins(usr.getIdLong());

        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("Helper/custom.ttf"));
            image = ImageIO.read(pathToFile);

            if (usr.getAvatarUrl() != null) {
                url = new URL(usr.getAvatarUrl());
            } else {
                url = new URL("https://i.imgur.com/X1rQVvr.png");
            }

            URLConnection hc = url.openConnection();
            hc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
            avatar = ImageIO.read(hc.getInputStream()).getScaledInstance(90, 90, Image.SCALE_DEFAULT);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        Image av = Methods.makeRoundedCorner(avatar, 10);

        Graphics2D g2d = image.createGraphics();

        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        rh.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        rh.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        g2d.setRenderingHints(rh);

        g2d.drawImage(av, 180, 41, null);

        g2d.setColor(Color.decode("#23272A"));

        centerString(g2d, 374, 42, 39, 137, usr.getName() + "'s profile", customFont.deriveFont(20f));

        g2d.setColor(Color.decode("#2C2F33"));
        g2d.setFont(customFont.deriveFont(25f));

        g2d.drawString(usr.getAsTag(), 66, 260);
        g2d.drawString(format, 66, 346);
        g2d.drawString(String.valueOf(coins), 66, 437);

        g2d.dispose();

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            ImageIO.write(image, "png", os);
        } catch (IOException e) {
            e.printStackTrace();
        }

        sendFile(channel, os);
    }

    private static void sendFile(TextChannel channel, ByteArrayOutputStream by) {
        MessageBuilder message = new MessageBuilder();
        EmbedBuilder builder = new EmbedBuilder();
        InputStream file = new ByteArrayInputStream(by.toByteArray());
        builder.setImage("attachment://profile.png");
        builder.setColor(Color.decode("#2ecc71"));
        message.setEmbed(builder.build());
        channel.sendFile(file, "profile.png", message.build()).queue();
    }

    private void centerString(Graphics g, int width, int height, int x, int y, String s, Font font) {
        FontRenderContext frc = new FontRenderContext(null, true, true);

        Rectangle2D r2D = font.getStringBounds(s, frc);
        int rWidth = (int) Math.round(r2D.getWidth());
        int rHeight = (int) Math.round(r2D.getHeight());
        int rX = (int) Math.round(r2D.getX());
        int rY = (int) Math.round(r2D.getY());

        int a = (width / 2) - (rWidth / 2) - rX;
        int b = (height / 2) - (rHeight / 2) - rY;

        g.setFont(font);
        g.drawString(s, x + a, y + b);
    }

}
