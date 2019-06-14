package me.mrcookies.helper.minigames.quickmath;

import me.mrcookies.helper.main.Core;
import me.mrcookies.helper.utils.Methods;
import me.mrcookies.helper.utils.References;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Objects;

public class QuickMathEvent extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        if (e.getAuthor().isBot()) return;

        if (e.getChannel().getIdLong() != References.idQuickMath) return;

        TextChannel channel = e.getChannel();
        Message msg = e.getMessage();
        User usr = e.getAuthor();
        int number;

        channel.getHistory().retrievePast(1).queue((msgs) -> {

            if (msgs.size() > 0) return;

            usr.openPrivateChannel().queue((ch) -> Methods.sendSENT(ch, "Quick Math", "Minigame not started."));
        });

        int reference = Core.getConfig().getYml().getInt("Games.QuickMath.reference");

        try {
            number = Integer.parseInt(msg.getContentRaw());
        } catch (NumberFormatException ex) {
            usr.openPrivateChannel().queue((ch) -> Methods.sendSENT(ch, "Quick Math", "Invalid number."));
            msg.delete().queue();
            return;
        }

        if (number != reference) {
            msg.delete().queue();
            usr.openPrivateChannel().queue((ch) -> Methods.sendSENT(ch, "Quick Math", "Sorry, this is not the correct answer `" + number + "`."));
            return;
        }

        sendWin(usr, reference, channel);
        quickMathCore(channel);
        msg.delete().queue();
    }

    public static void quickMathCore(TextChannel channel) {
        String[] op = {"+", "-", "*"};

        int x = Methods.getRandom(1000, 1);
        int y = Methods.getRandom(1000, 1);
        int i = Methods.getRandom(op.length, 0);
        String operation;

        switch (op[i]) {

            case "+": {
                operation = x + " + " + y;
                Core.getConfig().getYml().set("Games.QuickMath.reference", x + y);
                createQuickMathImage(operation, channel);
                break;
            }

            case "-": {
                operation = x + " - " + y;
                Core.getConfig().getYml().set("Games.QuickMath.reference", x - y);
                createQuickMathImage(operation, channel);
                break;
            }

            case "*": {
                int z = Methods.getRandom(100, 1);
                int k = Methods.getRandom(100, 1);
                operation = z + " x " + k;
                Core.getConfig().getYml().set("Games.QuickMath.reference", z * k);
                createQuickMathImage(operation, channel);
                break;
            }

        }

        Core.getConfig().save();
    }

    private static void createQuickMathImage(String math, TextChannel channel) {
        int width = 182;
        int height = 37;

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Font customFont = null;

        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("Helper/custom.ttf")).deriveFont(20f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        Graphics2D g2d = bufferedImage.createGraphics();

        g2d.setColor(Color.decode("#2C2F33"));

        g2d.fillRect(0, 0, width, height);

        g2d.setColor(Color.decode("#ecf0f1"));

        Rectangle r = new Rectangle(0, 0, width, height);

        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        rh.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        rh.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        g2d.setRenderingHints(rh);

        Methods.centerString(g2d, r, math, Objects.requireNonNull(customFont));

        g2d.dispose();

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            ImageIO.write(bufferedImage, "png", os);
        } catch (IOException e) {
            e.printStackTrace();
        }

        sendFile(channel, os);
    }

    private static void sendFile(TextChannel channel, ByteArrayOutputStream by) {
        MessageBuilder message = new MessageBuilder();
        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor("Quick Math", null, "https://i.imgur.com/tXTuUuZ.png");
        InputStream file = new ByteArrayInputStream(by.toByteArray());
        builder.setImage("attachment://math.png");
        builder.setColor(Color.decode("#FF5252"));
        builder.setFooter(References.h + " • Try to solve this equation to win!", "https://i.imgur.com/nepS3Lp.jpg");
        message.setEmbed(builder.build());
        channel.sendFile(file, "math.png", message.build()).queue();
    }

    private void sendWin(User usr, int n, TextChannel channel) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor("Quick Math", null, "https://i.imgur.com/p3owVKR.png");
        builder.setDescription(usr.getAsMention() + " you guessed!\n**The correct answer was** `" + n + "`.");
        builder.setColor(Color.decode("#2ecc71"));
        builder.setFooter(References.h + " • New equation incoming!", "https://i.imgur.com/nepS3Lp.jpg");
        channel.sendMessage(builder.build()).queue();
    }

}
