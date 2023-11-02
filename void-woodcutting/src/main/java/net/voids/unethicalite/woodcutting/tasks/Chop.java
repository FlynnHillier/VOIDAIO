package net.voids.unethicalite.woodcutting.tasks;

//import net.runelite.api.Tile;
import net.runelite.api.Animation;
import net.runelite.api.AnimationID;
import net.runelite.api.Tile;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.movement.Movement;
import net.unethicalite.api.movement.Reachable;
import net.unethicalite.api.scene.Tiles;
import net.voids.unethicalite.utils.tasks.Task;
import net.voids.unethicalite.woodcutting.WoodCutting;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Optional;

public class Chop extends Task
{
    @Inject
    WoodCutting plugin;


    private WorldPoint currentTreePosition;


    private WorldArea TREE_PATCH_LUMBRIDGE = new WorldArea(3180, 3208, 19, 21, 0);

    @Override
    public String getStatus()
    {
        return "chopping trees.";
    }

    @Override
    public boolean validate()
    {
        return currentTreePosition == null
                || Arrays.stream(Tiles.getAt(currentTreePosition).getGameObjects()).findAny().isEmpty()
                || !Arrays.stream(Tiles.getAt(currentTreePosition).getGameObjects()).findFirst().get().hasAction("Chop down");
    }

    @Override
    public void execute()
    {
        Optional<TileObject> tree = Optional.ofNullable(
                TileObjects.getNearest(tile ->
                        TREE_PATCH_LUMBRIDGE.contains(tile.getWorldLocation())
                                && tile.getName().equals("Tree")
                                && tile.hasAction("Chop down")
                )
        );

        if (tree.isEmpty())
        {
            return;
        }

        currentTreePosition = tree.get().getWorldLocation();

        if (!Reachable.isInteractable(tree.get()))
        {
            //get into interactable distance of the tree.
            Movement.walkTo(tree.get().getWorldLocation());

            Time.sleepTicksUntil(() -> Reachable.isInteractable(tree.get()), 10);
        }

        if (!tree.get().hasAction("Chop down"))
        {
            //test here if we need to refetch tileObject at tree location, if not update actions when tree is chopped down during
            return;
        }

        tree.get().interact("Chop down");

        Time.sleepTick();

        //wait until starts swinging
        Time.sleepTicksUntil(()->
                Players.getLocal().isAnimating()
                        && Players.getLocal().getAnimation() == AnimationID.WOODCUTTING_BRONZE,
                10);

        //wait until stops swinging axe
        Time.sleepTicksUntil(()->
                !Players.getLocal().isAnimating()
                        || Players.getLocal().getAnimation() != AnimationID.WOODCUTTING_BRONZE,
                30);
    }
}
