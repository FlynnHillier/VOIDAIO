package net.voids.unethicalite.ernestTheChicken.data;

import lombok.Data;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.coords.PolygonalArea;

import javax.inject.Singleton;

@Data
@Singleton
public class Areas
{
    public static WorldArea DRAYNOR_MANOR_GATE = new WorldArea(3107, 3328, 7, 3, 0);;
    public static WorldArea DRAYNOR_MANOR_TOP_FLOOR = new WorldArea(3104, 3357, 9, 14, 2);
    public static WorldArea DRAYNOR_MANOR_MIDDLE_FLOOR = new WorldArea(3091, 3353, 35, 21, 1);
    public static PolygonalArea DRAYNOR_MANOR_GROUND_FLOOR = new PolygonalArea(0, new WorldPoint[] {
            new WorldPoint(3091, 3363, 0),
            new WorldPoint(3091, 3354, 0),
            new WorldPoint(3126, 3354, 0),
            new WorldPoint(3126, 3360, 0),
            new WorldPoint(3119, 3360, 0),
            new WorldPoint(3119, 3373, 0),
            new WorldPoint(3097, 3373, 0),
            new WorldPoint(3097, 3364, 0)
    });
    public static PolygonalArea DRAYNOR_MANOR_COURT_YARD_AND_GROUND_FLOOR = new PolygonalArea(0, new WorldPoint[] {
            new WorldPoint(3086, 3351, 0),
            new WorldPoint(3085, 3354, 0),
            new WorldPoint(3085, 3358, 0),
            new WorldPoint(3084, 3359, 0),
            new WorldPoint(3085, 3358, 0),
            new WorldPoint(3085, 3358, 0),
            new WorldPoint(3084, 3359, 0),
            new WorldPoint(3084, 3363, 0),
            new WorldPoint(3085, 3365, 0),
            new WorldPoint(3086, 3366, 0),
            new WorldPoint(3085, 3370, 0),
            new WorldPoint(3083, 3372, 0),
            new WorldPoint(3083, 3376, 0),
            new WorldPoint(3085, 3379, 0),
            new WorldPoint(3085, 3383, 0),
            new WorldPoint(3089, 3387, 0),
            new WorldPoint(3091, 3387, 0),
            new WorldPoint(3104, 3386, 0),
            new WorldPoint(3107, 3385, 0),
            new WorldPoint(3117, 3385, 0),
            new WorldPoint(3127, 3376, 0),
            new WorldPoint(3126, 3375, 0),
            new WorldPoint(3128, 3374, 0),
            new WorldPoint(3127, 3371, 0),
            new WorldPoint(3127, 3370, 0),
            new WorldPoint(3127, 3366, 0),
            new WorldPoint(3129, 3364, 0),
            new WorldPoint(3130, 3355, 0),
            new WorldPoint(3128, 3354, 0),
            new WorldPoint(3129, 3347, 0),
            new WorldPoint(3127, 3345, 0),
            new WorldPoint(3127, 3340, 0),
            new WorldPoint(3126, 3339, 0),
            new WorldPoint(3126, 3334, 0),
            new WorldPoint(3123, 3331, 0),
            new WorldPoint(3091, 3332, 0),
            new WorldPoint(3091, 3337, 0),
            new WorldPoint(3088, 3338, 0),
            new WorldPoint(3085, 3338, 0),
            new WorldPoint(3085, 3341, 0),
            new WorldPoint(3084, 3341, 0),
            new WorldPoint(3084, 3346, 0)
    });



    public static final WorldPoint DRAYNOR_MANOR_SPADE_SPAWN_TILE = new WorldPoint(3120, 3359, 0);

    public static final WorldPoint DRAYNOR_MANOR_COMPOST_HEAP_DIG_TILE = new WorldPoint(3086, 3361, 0);

    public static final WorldArea DRAYNOR_MANOR_FOUNTAIN = new WorldArea(3085, 3332, 6, 6, 0);
}
