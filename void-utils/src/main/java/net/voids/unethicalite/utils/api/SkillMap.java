package net.voids.unethicalite.utils.api;


import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import net.runelite.api.Skill;
import net.unethicalite.api.game.Skills;

public abstract class SkillMap
{

    @Getter
    private HashMap<Skill,Integer> skills = new HashMap<>();

    public boolean playerHasRequirements()
    {
        for(HashMap.Entry<Skill,Integer> skillEntry : skills.entrySet())
        {
            if(skillEntry.getValue() > Skills.getLevel(skillEntry.getKey()))
            {
                return false;
            }
        }
        return true;
    }
}
