package me.mrcookies.helper.listeners.events;

import me.mrcookies.helper.main.Core;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BotStopEvent extends ListenerAdapter {

    @Override
    public void onShutdown(ShutdownEvent e) {
        Core.getConfig().save();
    }

}
