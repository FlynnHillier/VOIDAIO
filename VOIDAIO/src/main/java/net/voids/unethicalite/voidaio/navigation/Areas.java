package net.voids.unethicalite.voidaio.navigation;

import net.runelite.api.coords.WorldArea;
import net.unethicalite.api.movement.Movement;

public class Areas
{

    //Cities, Towns, & Villages
    public static WorldArea GOBLIN_VILLAGE = new WorldArea(2956, 3510, 4, 3, 0);

    //Shops
    public static WorldArea GENERAL_STORE_LUMBRIDGE = new WorldArea(3208, 3244, 7, 6, 0);

    //Foraging & Gathering
    public static WorldArea FORAGE_ONIONS_LUMBRIDGE = new WorldArea(3186, 3266, 6, 4, 0);

    public static WorldArea FORAGE_ONIONS_RIMMINGTON = new WorldArea(2945, 3249, 10, 5, 0);

    public static WorldArea VARROCK_WHEAT_FIELD = new WorldArea(3139, 3458, 5, 6, 0);

    public static WorldArea getClosestOnion()
    {
        if (Movement.calculateDistance(FORAGE_ONIONS_LUMBRIDGE) < Movement.calculateDistance(FORAGE_ONIONS_RIMMINGTON))
        {
            return FORAGE_ONIONS_LUMBRIDGE;
        }
        else
        {
            return FORAGE_ONIONS_RIMMINGTON;
        }
    }
}
