package net.voids.unethicalite.ernestTheChicken.tasks;

import net.runelite.api.TileObject;
import net.runelite.api.Varbits;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.Vars;
import net.voids.unethicalite.ernestTheChicken.data.LeverPuzzle;
import net.voids.unethicalite.utils.api.VoidMovement;
import net.voids.unethicalite.utils.jobs.Job;
import net.voids.unethicalite.utils.tasks.Task;

import java.util.Arrays;

//TODO: write some method into VoidMovement which detects if a given worldpoint is reachable, if doors are opened.
// - return true if path exists considering we open some (openable) doors
// - return false if no such path exists.

public class LeversPuzzleSolve extends Task
{
    public LeversPuzzleSolve(Job job)
    {
        super(job);
    }

    @Override
    public String getStatus()
    {
        return "solving levers puzzle";
    }

    @Override
    public boolean validate()
    {
        return Players.getLocal().getWorldLocation().getRegionID() == 12440
                && (
                        Vars.getBit(Varbits.DRAYNOR_MANOR_BASEMENT_DOOR_9_STATE) == 0
                        || (
                                Vars.getBit(Varbits.DRAYNOR_MANOR_BASEMENT_DOOR_9_STATE) == 1
                                        && Vars.getBit(Varbits.DRAYNOR_MANOR_BASEMENT_LEVER_C) == 1
                                        && Vars.getBit(Varbits.DRAYNOR_MANOR_BASEMENT_LEVER_E) == 1
                                )
        );
    }

    @Override
    public void execute()
    {
        boolean[] doorStates = new boolean[] {
                Vars.getBit(Varbits.DRAYNOR_MANOR_BASEMENT_DOOR_1_STATE) == 1,
                Vars.getBit(Varbits.DRAYNOR_MANOR_BASEMENT_DOOR_2_STATE) == 1,
                Vars.getBit(Varbits.DRAYNOR_MANOR_BASEMENT_DOOR_3_STATE) == 1,
                Vars.getBit(Varbits.DRAYNOR_MANOR_BASEMENT_DOOR_4_STATE) == 1,
                Vars.getBit(Varbits.DRAYNOR_MANOR_BASEMENT_DOOR_5_STATE) == 1,
                Vars.getBit(Varbits.DRAYNOR_MANOR_BASEMENT_DOOR_6_STATE) == 1,
                Vars.getBit(Varbits.DRAYNOR_MANOR_BASEMENT_DOOR_7_STATE) == 1,
                Vars.getBit(Varbits.DRAYNOR_MANOR_BASEMENT_DOOR_8_STATE) == 1,
                Vars.getBit(Varbits.DRAYNOR_MANOR_BASEMENT_DOOR_9_STATE) == 1
        };

        if (Arrays.equals(
                doorStates,
                new boolean[] {
                        false, //1
                        false, //2
                        false, //3
                        false, //4
                        false, //5
                        false, //6
                        false, //7
                        false, //8
                        false, //9
                })
        )
        {
            if (Vars.getBit(Varbits.DRAYNOR_MANOR_BASEMENT_LEVER_B) == 0)
            {
                if (!pullLeverB())
                {
                    job.getLogger().info("failed to pull lever B");
                }
            }
            else if (Vars.getBit(Varbits.DRAYNOR_MANOR_BASEMENT_LEVER_A) == 0)
            {
                if (!pullLeverA())
                {
                    job.getLogger().info("failed to pull lever A");
                }
            }
        }
        else if (Arrays.equals(
                doorStates,
                new boolean[] {
                        true, //1
                        true, //2
                        false, //3
                        false, //4
                        false, //5
                        false, //6
                        false, //7
                        false, //8
                        false, //9
                })
        )
        {
            pullLeverD();
        }
        else if (Arrays.equals(
                doorStates,
                new boolean[] {
                        true, //1
                        true, //2
                        true, //3
                        true, //4
                        false, //5
                        false, //6
                        false, //7
                        false, //8
                        false, //9
                })
        )
        {
            pullLeverB();
        }
        else if (Arrays.equals(
                doorStates,
                new boolean[] {
                        false, //1
                        true, //2
                        true, //3
                        true, //4
                        true, //5
                        false, //6
                        false, //7
                        false, //8
                        false, //9
                })
        )
        {
            pullLeverA();
        }
        else if (Arrays.equals(
                doorStates,
                new boolean[] {
                        false, //1
                        false, //2
                        true, //3
                        true, //4
                        true, //5
                        false, //6
                        false, //7
                        false, //8
                        false, //9
                })
        )
        {
            pullLeverF(); //actually lever F?
        }
        else if (Arrays.equals(
                doorStates,
                new boolean[] {
                        false, //1
                        false, //2
                        true, //3
                        false, //4
                        false, //5
                        true, //6
                        false, //7
                        true, //8
                        false, //9
                })
        )
        {
            pullLeverE(); //actually lever F?
        }
        else if (Arrays.equals(
                doorStates,
                new boolean[] {
                        false, //1
                        false, //2
                        false, //3
                        false, //4
                        false, //5
                        true, //6
                        true, //7
                        false, //8
                        false, //9
                })
        )
        {
            pullLeverC();
        }
        else if (Arrays.equals(
                doorStates,
                new boolean[] {
                        false, //1
                        false, //2
                        false, //3
                        false, //4
                        false, //5
                        true, //6
                        true, //7
                        false, //8
                        true, //9
                })
        )
        {
            pullLeverE();
        }

        return;
    }

    private static TileObject getLever (Character lever_id)
    {
        if (TileObjects.getAt(LeverPuzzle.LEVER_LOCATIONS.get(lever_id), item -> item.getName().startsWith("Lever")).isEmpty())
        {
            return null;
        }

        return TileObjects.getAt(LeverPuzzle.LEVER_LOCATIONS.get(lever_id), item -> item.getName().startsWith("Lever")).get(0);
    };



    private static boolean _pullLever(Character leverChar, int varBitID)
    {
        TileObject lever = getLever(leverChar);
        if (lever == null)
        {
            return false;
        }


        int initialLeverState = Vars.getBit(varBitID);

        if (lever.getWorldLocation().distanceTo(Players.getLocal().getWorldLocation()) > 2)
        {
            VoidMovement.walkTo(lever.getWorldLocation(), 0, 40, 0);
        }

        lever.interact("Pull");

        return Time.sleepTicksUntil(() -> Vars.getBit(varBitID) != initialLeverState, 10);
    };



    private static boolean pullLeverA ()
    {
        return _pullLever('A', Varbits.DRAYNOR_MANOR_BASEMENT_LEVER_A);
    }

    private static boolean pullLeverB ()
    {
        return _pullLever('B', Varbits.DRAYNOR_MANOR_BASEMENT_LEVER_B);
    }

    private static boolean pullLeverC ()
    {
        return _pullLever('C', Varbits.DRAYNOR_MANOR_BASEMENT_LEVER_C);
    }

    private static boolean pullLeverD ()
    {
        return _pullLever('D', Varbits.DRAYNOR_MANOR_BASEMENT_LEVER_D);
    }

    private static boolean pullLeverE ()
    {
        return _pullLever('E', Varbits.DRAYNOR_MANOR_BASEMENT_LEVER_E);
    }

    private static boolean pullLeverF ()
    {
        return _pullLever('F', Varbits.DRAYNOR_MANOR_BASEMENT_LEVER_F);
    }

}
