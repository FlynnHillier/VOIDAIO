package net.voids.unethicalite.ernestTheChicken;

import net.runelite.client.plugins.PluginDescriptor;
import net.voids.unethicalite.ernestTheChicken.tasks.*;
import net.voids.unethicalite.utils.jobs.Job;

@PluginDescriptor(
        name = "void-ernest-the-chicken",
        description = "complete the 'ernest the chicken' quest."
)
public class ErnestTheChicken extends Job
{
    @Override
    public String getTitle()
    {
        return "Ernest-the-chicken";
    }

    public ErnestTheChicken()
    {
        {
            addTask(new TalkToVeronica(this));
            addTask(new CollectPoison(this));
            addTask(new CollectFishFood(this));
            addTask(new CombineFishFoodPoison(this));
            addTask(new TalkToProfessor(this));
            addTask(new CollectOilCan(this));
            addTask(new LeversPuzzleSolve(this));
            addTask(new CollectSpade(this));
            addTask(new DigCompost(this));
            addTask(new UsePoisonedFishFoodOnFountain(this));
            addTask(new CollectRubberTube(this));
            addTask(new TalkToProfessorFinish(this));
        }
    }
}
