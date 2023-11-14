package net.voids.unethicalite.utils.tasksV2;

public class Time
{
    enum Type
    {
        MS, //pause for x milliseconds
        TICK, //pause for x ticks
    }

    Type type;
    long length;

    private Time(Type type, int length)
    {
        this.type = type;
        this.length = length;
    }

    public static Time ms(int length)
    {
        return new Time(Type.MS, length);
    }

    public static Time ticks(int length)
    {
        return new Time(Type.TICK, length);
    }

    public static Time tick()
    {
        return new Time(Type.TICK, 1);
    }

    public void sleep()
    {
        switch (type)
        {
            case MS:
                break;
        }
    }
}
