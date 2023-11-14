package net.voids.unethicalite.utils.tasksV2;

public class Pause
{
    Type type;
    Time length;

    private long startMS;



    enum Type
    {
        Time,
        Condition,
    }



    /**
     *
     * @param type - assigned to type.
     * @param length
     */
    private Pause(Type type, Time length)
    {
        this.type = type;
        this.length = length;
    }

    private Pause(Type type, Runnable condition, Pause timeout)
    {

    }

    public static Pause ms(int milliseconds)
    {
        return new Pause(Type.Time, Time.ms(milliseconds));
    }
}
