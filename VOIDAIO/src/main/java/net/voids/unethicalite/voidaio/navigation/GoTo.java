package net.voids.unethicalite.voidaio.navigation;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.coords.WorldArea;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.movement.Movement;
import net.unethicalite.api.movement.pathfinder.model.BankLocation;

@Slf4j
public class GoTo
{
    public static void worldArea(WorldArea destination)
    {
        if (Movement.isWalking())
        {
            //log.info("Moving to " + destination.getCenter() + ". I am at: " + Players.getLocal().getWorldLocation() + " Distance: " + Movement.calculateDistance(destination));
        }
        else
        {
            log.info("Moving to " + destination.getCenter() + ". I am at: " + Players.getLocal().getWorldLocation() + " Distance: " + Movement.calculateDistance(destination));
            Movement.walkTo(destination);
        }
    }

    public static void bankLocation(BankLocation destination)
    {

        if (Movement.isWalking())
        {
            log.info("Moving to " + destination.name() + ". I am at: " + Players.getLocal().getWorldLocation() + " Distance: " + Movement.calculateDistance(destination.getArea()));
        }
        else
        {
            log.info("Moving to " + destination.name() + ". I am at: " + Players.getLocal().getWorldLocation() + " Distance: " + Movement.calculateDistance(destination.getArea()));;
            Movement.walkTo(destination);
        }
    }

    public static void bankNearest()
    {

        if (Movement.isWalking())
        {
            log.info("Moving to " + BankLocation.getNearest().name() + ". I am at: " + Players.getLocal().getWorldLocation() + " Distance: " + Movement.calculateDistance(BankLocation.getNearest().getArea()));
        }
        else
        {
            log.info("Moving to " + BankLocation.getNearest().name() + ". I am at: " + Players.getLocal().getWorldLocation() + " Distance: " + Movement.calculateDistance(BankLocation.getNearest().getArea()));
            Movement.walkTo(BankLocation.getNearest());
        }
    }
}
