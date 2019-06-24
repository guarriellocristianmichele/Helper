package me.mrcookies.helper.utils;

import me.mrcookies.helper.main.Core;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Methods {

    public static void sendErrorMessage(TextChannel channel, String Description) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor("Error", null, "https://i.imgur.com/FtoMNlh.png");
        builder.setDescription(Description);
        builder.setColor(Color.decode("#e74c3c"));
        builder.setFooter(References.h + " • Seems you're wrong...", "https://i.imgur.com/nepS3Lp.jpg");

        channel.sendMessage(builder.build()).queue(msg -> msg.delete().queueAfter(5, TimeUnit.SECONDS));
    }

    public static void sendSimpleEmbed(TextChannel channel, String title, String Description) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor(title, null, "https://i.imgur.com/IUFgzzq.png");
        builder.setDescription(Description);
        builder.setColor(Color.decode("#fdcb6e"));
        builder.setFooter(References.h, "https://i.imgur.com/nepS3Lp.jpg");

        channel.sendMessage(builder.build()).queue(msg -> msg.delete().queueAfter(5, TimeUnit.SECONDS));
    }

    public static void sendSENT(TextChannel channel, String title, String Description) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor(title, null, "https://i.imgur.com/IUFgzzq.png");
        builder.setDescription(Description);
        builder.setColor(Color.decode("#fdcb6e"));
        builder.setFooter(References.h, "https://i.imgur.com/nepS3Lp.jpg");

        channel.sendMessage(builder.build()).queue();
    }

    public static void sendSENT(PrivateChannel channel, String title, String Description) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor(title, null, "https://i.imgur.com/IUFgzzq.png");
        builder.setDescription(Description);
        builder.setColor(Color.decode("#fdcb6e"));
        builder.setFooter(References.h, "https://i.imgur.com/nepS3Lp.jpg");

        channel.sendMessage(builder.build()).queue();
    }

    public static int getRandom(int n, int start) {
        Random ran = new Random();
        return ran.nextInt(n) + start;
    }

    public static void sendSENTImage(TextChannel channel, String title, String Description, String img) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor(title, null, "https://i.imgur.com/IUFgzzq.png");
        builder.setDescription(Description);
        builder.setThumbnail(img);
        builder.setColor(Color.decode("#fdcb6e"));
        builder.setFooter(References.h, "https://i.imgur.com/nepS3Lp.jpg");

        channel.sendMessage(builder.build()).queue();
    }

    public static int getUsersSize() {

        int cont = 0;

        for (User usr : Core.getJDA().getUsers()) {

            if (usr.isBot()) continue;

            cont++;
        }

        return cont;
    }

    public static ArrayList<Long> getUsersIDs() {

        ArrayList<Long> ids = new ArrayList<>();

        for (User usr : Core.getJDA().getUsers()) {

            if (usr.isBot()) continue;

            ids.add(usr.getIdLong());
        }

        return ids;
    }

    public static String getTimeFormat(Long milliseconds) {

        String message;

        if (milliseconds >= 1000) {

            long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds) % 60;
            long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds) % 60;
            long hours = TimeUnit.MILLISECONDS.toHours(milliseconds) % 24;
            long days = TimeUnit.MILLISECONDS.toDays(milliseconds);

            if ((days == 0) && (hours != 0)) {
                message = String.format("%dh %dm %ds", hours, minutes, seconds);
            } else if ((hours == 0) && (minutes != 0)) {
                message = String.format("%dm %ds", minutes, seconds);
            } else if (days == 0) {
                message = String.format("%ds", seconds);
            } else {
                message = String.format("%dd %dh %dm %ds", days, hours, minutes, seconds);
            }

        } else {
            message = "0s";
        }

        return message;
    }

    public static boolean hasPermission(Member mem, TextChannel channel) {
        if (mem.getRoles().stream().noneMatch(role -> role.getIdLong() == Core.getConfig().getYml().getLong("Roles.permission"))) {
            sendErrorMessage(channel, "No permission.");
            return false;
        }
        return true;
    }

    public static boolean returnChannels(GuildMessageReceivedEvent e) {
        if (e.getChannel().getIdLong() == References.idCountGame) return true;
        if (e.getChannel().getIdLong() == References.idQuickMath) return true;
        if (e.getChannel().getIdLong() == References.idHigherLower) return true;
        if (e.getChannel().getIdLong() == References.idTickets) return true;
        if (e.getChannel().getIdLong() == References.idRequests) return true;
        if (e.getChannel().getIdLong() == References.idRedeem) return true;
        return e.getChannel().getName().contains("ticket-");
    }

    public static String getRandomLicense() {
        return new SecureRandom().ints(0, 36)
                .mapToObj(i -> Integer.toString(i, 36))
                .map(String::toUpperCase).distinct().limit(16).collect(Collectors.joining())
                .replaceAll("([A-Z0-9]{4})", "$1-").substring(0, 19);
    }

    public static boolean isInvalidChannel(TextChannel channel) {
        if (channel.getIdLong() != References.idCommands) {
            if (channel.getIdLong() == References.idStaffCommands) return false;
            sendErrorMessage(channel, "You can't execute commands here.");
            return true;
        }
        return false;
    }

    public static String getOrdinalNumbers(int i) {
        return i % 100 == 11 || i % 100 == 12 || i % 100 == 13 ? i + "th" : i + new String[]{"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th"}[i % 10];
    }

    public static boolean isStaffer(Member mem) {
        return mem.getRoles().stream().anyMatch(role -> role.getIdLong() == Core.getConfig().getYml().getLong("Roles.utility"));
    }

    public static File createChatLog(TextChannel channel) throws IOException {

        final String filename = channel.getName() + ".txt";
        final File saveFile = new File("Helper/Chatlogs/" + filename);

        if (!saveFile.getParentFile().exists()) {
            saveFile.getParentFile().mkdirs();
        }

        try {
            saveFile.createNewFile();
        } catch (final Exception e) {
            e.printStackTrace();
        }

        final List<Message> messages = channel.getIterableHistory().complete();
        Collections.reverse(messages);

        final BufferedWriter br = new BufferedWriter(new FileWriter("Helper/Chatlogs/" + filename));

        for (final Message m : messages) {

            StringBuilder attachment = new StringBuilder();

            if (m.getAttachments().size() != 0) {

                for (final Message.Attachment a : m.getAttachments()) {
                    attachment = new StringBuilder("[Attachment: " + a.getUrl() + "]");
                }

            }

            final String messagecontent = m.getContentDisplay();

            for (final MessageEmbed me : m.getEmbeds()) {
                attachment.append(" ").append(me.getDescription());
            }

            br.write("Date: " + m.getTimeCreated().getDayOfMonth() + "/" + m.getTimeCreated().getMonthValue() + "/" + m.getTimeCreated().getYear() + " ID: " + m.getAuthor().getId() + " Name: " + m.getAuthor().getName() + " » " + messagecontent + " " + attachment);
            br.newLine();
        }

        br.close();
        return saveFile;
    }

    public static BufferedImage makeRoundedCorner(Image image, int cornerRadius) {
        int w = image.getWidth(null);
        int h = image.getHeight(null);
        BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = output.createGraphics();

        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Float(0, 0, w, h, cornerRadius, cornerRadius));

        g2.setComposite(AlphaComposite.SrcAtop);
        g2.drawImage(image, 0, 0, null);

        g2.dispose();

        return output;
    }

    public static void centerString(Graphics g, Rectangle r, String s, Font font) {
        FontRenderContext frc = new FontRenderContext(null, true, true);

        Rectangle2D r2D = font.getStringBounds(s, frc);
        int rWidth = (int) Math.round(r2D.getWidth());
        int rHeight = (int) Math.round(r2D.getHeight());
        int rX = (int) Math.round(r2D.getX());
        int rY = (int) Math.round(r2D.getY());

        int a = (r.width / 2) - (rWidth / 2) - rX;
        int b = (r.height / 2) - (rHeight / 2) - rY;

        g.setFont(font);
        g.drawString(s, r.x + a, r.y + b);
    }

    public static String getCap(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

}
