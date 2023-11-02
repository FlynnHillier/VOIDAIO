package net.voids.unethicalite.utils;

import net.runelite.api.GameState;
import net.unethicalite.api.game.Game;
import net.unethicalite.client.Static;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import org.pf4j.Extension;

import javax.inject.Singleton;

@Extension
@PluginDescriptor(
        name = "Void utilities",
        description = "Utilities for void scripts."
)
@SuppressWarnings("unused")
@Slf4j
@Singleton
public class Utils extends Plugin
{
    public static boolean isLoggedIn()
    {
        return Static.getClient() != null && Game.getState() == GameState.LOGGED_IN;
    }


    @Override
    public void startUp()
    {
        log.info(this.getName() + " started");
    }

    @Override
    protected void shutDown()
    {
        log.info(this.getName() + " stopped");
    }
}