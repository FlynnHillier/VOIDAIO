package net.voids.unethicalite.utils.api;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Item;
import net.runelite.api.TileObject;
import net.runelite.api.widgets.Widget;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.movement.Reachable;
import net.unethicalite.api.widgets.Widgets;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
public class VoidBank
{
    private static int DEFAULT_BANK_OPEN_TIMEOUT = 20;


    public static void depositAll(String... names)
    {
        depositAll(true, names);
    }

    public static void depositAll(boolean delay, String... names)
    {
        depositAll(delay, x -> Arrays.stream(names).anyMatch(name -> x.getName().equals(name)));
    }

    public static void depositAll(int... ids)
    {
        depositAll(true, ids);
    }

    public static void depositAll(boolean delay, int... ids)
    {
        depositAll(delay, x -> Arrays.stream(ids).anyMatch(id -> x.getId() == id));
    }

    public static void depositAll(Predicate<Item> filter)
    {
        depositAll(true, filter);
    }

    public static void depositAll(boolean delay, Predicate<Item> filter)
    {
        Set<Item> items =
                Bank.Inventory.getAll(filter).stream()
                        .filter(VoidPredicates.distinctByProperty(Item::getId))
                        .collect(Collectors.toSet());

        items.forEach(
                (item) -> {
                    Bank.depositAll(item.getId());

                    if (delay)
                    {
                        Time.sleepTick();
                    }
                });
    }

    public static void depositAllExcept(String... names)
    {
        depositAllExcept(true, names);
    }

    public static void depositAllExcept(boolean delay, String... names)
    {
        depositAllExcept(delay, x -> Arrays.stream(names).anyMatch(name -> x.getName().equals(name)));
    }

    public static void depositAllExcept(int... ids)
    {
        depositAllExcept(true, ids);
    }

    public static void depositAllExcept(boolean delay, int... ids)
    {
        depositAllExcept(delay, x -> Arrays.stream(ids).anyMatch(id -> x.getId() == id));
    }

    public static void depositAllExcept(Predicate<Item> filter)
    {
        depositAllExcept(true, filter);
    }

    public static void depositAllExcept(boolean delay, Predicate<Item> filter)
    {
        depositAll(delay, filter.negate());
    }

    /**
     * handle bank-tutorial popups if present.
     */
    private static void handleBankPopups ()
    {
        Widget bankTutorialWidget = Widgets.get(664, 29, 0);

        if (Widgets.isVisible(bankTutorialWidget))
        {
            Time.sleepTick();
            bankTutorialWidget.interact("Close");
            Time.sleepTick();
        }
    }


    /**
     *
     * @return false if returned due to timeout.
     */
    public static boolean sleepUntilBankIsOpen()
    {
        return sleepUntilBankIsOpen(DEFAULT_BANK_OPEN_TIMEOUT);
    }

    /**
     *
     * @param maxTicks maximum ticks to wait before cancelling.
     * @return false if returned due to timeout.
     */
    public static boolean sleepUntilBankIsOpen(int maxTicks)
    {
        return Time.sleepTicksUntil(Bank::isOpen, maxTicks);
    }

    /**
     *
     * @return true if nearest bank is interactable.
     */
    public static boolean nearestBankIsInteractable()
    {
        Optional<TileObject> bankTile = getNearestBankTile();

        if (bankTile.isEmpty())
        {
            return false;
        }

        return Reachable.isInteractable(bankTile.get());
    }

    /**
     *
     * @return an Optional containing the nearest bank tile.
     */
    public static Optional<TileObject> getNearestBankTile()
    {
        return Optional.ofNullable(
                TileObjects.getNearest(VoidBank::tileObjectIsBank)
        );
    }


    /**
     * Open bank UI at a given bank tile.
     * If passed tile is not bank tile, will return false.
     * If passed tile is not interactable, will return false.
     * @param tileObject bank tile.
     * @return true if bank was successfully opened.
     */
    public static boolean openBankAtTile(TileObject tileObject)
    {
        return openBankAtTile(tileObject, DEFAULT_BANK_OPEN_TIMEOUT);
    }

    public static boolean openBankAtTile(TileObject tileObject, int timeout)
    {
        if (!tileObjectIsBank(tileObject))
        {
            return false;
        }

        if (!Reachable.isInteractable(tileObject))
        {
            return false;
        }

        tileObject.interact("Bank");

        if (!sleepUntilBankIsOpen(timeout))
        {
            return false;
        }

        handleBankPopups();

        Time.sleepTick();
        return true;
    }


    /**
     *
     * @param tileObject tileObject to test if is valid bank tile.
     * @return if argument tileObject was valid bank tile.
     */
    public static boolean tileObjectIsBank(TileObject tileObject)
    {
        return tileObject != null && tileObject.hasAction("Bank", "Collect");
    }

    public static void close()
    {
        Bank.close();
    }


}
