package net.voids.unethicalite.utils.tasksV2.generic.interaction;


import net.runelite.api.GameState;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.Game;
import net.unethicalite.api.movement.Movement;
import net.voids.unethicalite.utils.api.VoidMovement;
import net.voids.unethicalite.utils.tasksV2.Failure;
import net.voids.unethicalite.utils.tasksV2.Task;

import java.util.ArrayList;
import java.util.Random;

/**
 * interact with an object.
 */
public abstract class InteractObjectAt extends Task
{
    private final WorldPoint objectLocation;
    private final String objectName;
    private final String action;
    protected boolean hasInteracted;
    private TileObject object;

    private WorldPoint interactableFromTileAt; //a location next to the object in which the player can interact with the object from.


    public InteractObjectAt(
            WorldPoint objectLocation,
            String objectName,
            String action
    )
    {
        super("interacting with object");
        this.failable = true;

        this.objectLocation = objectLocation;
        this.objectName = objectName;
        this.action = action;
    }

    @Override
    public boolean validationCondition()
    {
        return true;
    }

    @Override
    protected void onInitialise()
    {
        this.object = null;
        this.hasInteracted = false;
    }

    @Override
    protected void execute()
    {

        //TODO:
        // - check if object is in render distance
        // - if not, path to objects worldpoint until it should be.
        // - if not present when it should be, fail.
        // - if it is, start pathing to correct adjacent tile


        if (object == null)
        {
            object = TileObjects.getFirstAt(objectLocation, objectName);
            if (object == null)
            {
                //object is still null after attempting to be assigned
                failed(Failure.Type.NOT_FOUND, "could not locate '" + objectName + "'object at specified location", null);
                return;
            }
        }

        if (hasInteracted)
        {
            //we should now just be waiting for completion condition to be true.
            return;
        }

        if (interactableFromTileAt == null)
        {
            //identify where we should walk to

            ArrayList<WorldPoint> interactableFromTilesAt = VoidMovement.getNonBlockedAdjacentTiles(objectLocation);

            if (interactableFromTilesAt.isEmpty())
            {
                //no valid adjacent tiles are present
                failed(Failure.Type.PATHING, "no available adjacent tiles.", null);
                return;
            }

            //assign a target tile we should aim to walk to.
            interactableFromTileAt = interactableFromTilesAt.get(
                    new Random().nextInt(interactableFromTilesAt.size())
            );
        }

        if (Players.getLocal() == null || !Players.getLocal().getWorldLocation().equals(interactableFromTileAt))
        {
            //walk toward target tile
            if (!Movement.isWalking())
            {
                Movement.walkTo(interactableFromTileAt);
            }
            return;
        }
        else if (!Game.getState().equals(GameState.LOADING)
                && !Game.getState().equals(GameState.CONNECTION_LOST)
        )
        {
            object.interact(action);
            hasInteracted = true;
        }
    }
}
