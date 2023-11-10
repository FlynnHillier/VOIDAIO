package net.voids.unethicalite.cowkill.data;
import lombok.Value;
import net.voids.unethicalite.utils.api.Activity;

@Value
public class PluginActivity
{
    public static final Activity COOKING = new Activity("cooking");
    public static final Activity LOOTING = new Activity("looting");
}
