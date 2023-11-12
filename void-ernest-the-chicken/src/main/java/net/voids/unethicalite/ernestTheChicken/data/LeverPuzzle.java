package net.voids.unethicalite.ernestTheChicken.data;

import lombok.Data;
import net.runelite.api.coords.WorldPoint;

import java.util.HashMap;

@Data
public class LeverPuzzle
{
    public static final HashMap<Integer, WorldPoint> DOOR_LOCATIONS = new HashMap<>()
    {
        {
            put(1, new WorldPoint(3108, 9758, 0));
            put(2, new WorldPoint(3105, 9760, 0));
            put(3, new WorldPoint(3105, 9765, 0));
            put(4, new WorldPoint(3100, 9765, 0));
            put(5, new WorldPoint(3100, 9760, 0));
            put(6, new WorldPoint(3102, 9763, 0));
            put(7, new WorldPoint(3097, 9763, 0));
            put(8, new WorldPoint(3102, 9758, 0));
            put(9, new WorldPoint(3099, 9755, 0));
        }
    };

    public static final HashMap<Character, WorldPoint> LEVER_LOCATIONS = new HashMap<>()
    {
        {
            put('A', new WorldPoint(3108, 9745, 0));
            put('B', new WorldPoint(3118, 9752, 0));
            put('C', new WorldPoint(3112, 9760, 0));
            put('D', new WorldPoint(3108, 9767, 0));
            put('E', new WorldPoint(3097, 9767, 0));
            put('F', new WorldPoint(3096, 9765, 0));
        }
    };


}
