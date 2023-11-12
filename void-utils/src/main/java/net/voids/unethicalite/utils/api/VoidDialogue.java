package net.voids.unethicalite.utils.api;

import net.unethicalite.api.commons.Time;
import net.unethicalite.api.widgets.Dialog;

public class VoidDialogue
{
    public static boolean completeDialogue(String ...dialogOptions)
    {
        if (!Dialog.isOpen())
        {
            return false;
        }

        int dialogOptionIndex = 0;

        while (Dialog.isOpen())
        {
            if (Dialog.isViewingOptions())
            {
                //select appropriate option
                if (dialogOptionIndex >= dialogOptions.length)
                {
                    //not enough options were provided
                    return false;
                }

                boolean didSelectOption = selectDialogueOption(dialogOptions[dialogOptionIndex]);

                if (!didSelectOption)
                {
                    Dialog.close();
                    return false;
                }

                ++dialogOptionIndex;
            }
            else if (Dialog.canContinue())
            {
                Dialog.continueSpace();
            }

            Time.sleepTick();
        }

        return true;
    }

    public static boolean selectDialogueOption(String option)
    {
        if (!Dialog.hasOption(option))
        {
            return false;
        }

        Dialog.chooseOption(option);
        return true;
    }
}
