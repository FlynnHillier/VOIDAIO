package net.voids.unethicalite.voidaio.utils;

import net.runelite.api.GameState;
import net.unethicalite.api.game.Game;
import net.unethicalite.client.Static;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@PluginDescriptor(name = "Void utilities", description = "Utilities for void scripts.")
@Slf4j
public class Utils extends Plugin
{
    public static boolean isLoggedIn()
    {
        return Static.getClient() != null && Game.getState() == GameState.LOGGED_IN;
    }

}