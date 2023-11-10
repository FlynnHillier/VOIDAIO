package net.voids.unethicalite.cowkill.data;

import lombok.Data;
import lombok.Getter;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.coords.PolygonalArea;

import javax.inject.Singleton;

@Getter
@Data
@Singleton
public class Areas
{
    public static PolygonalArea FALADOR_COW_FIELD = new PolygonalArea(0,
            new WorldPoint(3021, 3311, 0),
            new WorldPoint(3022, 3313, 0),
            new WorldPoint(3023, 3314, 0),
            new WorldPoint(3042, 3314, 0),
            new WorldPoint(3044, 3312, 0),
            new WorldPoint(3044, 3300, 0),
            new WorldPoint(3040, 3297, 0),
            new WorldPoint(3038, 3297, 0),
            new WorldPoint(3038, 3298, 0),
            new WorldPoint(3032, 3298, 0),
            new WorldPoint(3032, 3297, 0),
            new WorldPoint(3029, 3297, 0),
            new WorldPoint(3021, 3297, 0)
    );

    public static WorldPoint FALADOR_COOKER_TILE = new WorldPoint(3039, 3345, 0);
}
