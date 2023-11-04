package net.voids.unethicalite.aio.managers;

import net.voids.unethicalite.utils.jobs.Job;
import net.voids.unethicalite.utils.managers.Manager;
import net.voids.unethicalite.woodcutting.WoodCutting;

public class SkillManager extends Manager {

    @Override
    public String getTitle() {
        return "skilling";
    }

    @Override
    public Job getSuitableJob() {
        return new WoodCutting();
    }
}
