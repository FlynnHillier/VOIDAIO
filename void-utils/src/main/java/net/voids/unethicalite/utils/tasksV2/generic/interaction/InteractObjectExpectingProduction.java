package net.voids.unethicalite.utils.tasksV2.generic.interaction;


import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.widgets.Production;

public class InteractObjectExpectingProduction extends InteractObjectAt
{
    public InteractObjectExpectingProduction(
            WorldPoint objectLocation,
            String objectName,
            String action
    )
    {
        super(objectLocation, objectName, action);
    }

    @Override
    public boolean completionCondition()
    {
        return hasInteracted
                && Production.isOpen();
    }
}
