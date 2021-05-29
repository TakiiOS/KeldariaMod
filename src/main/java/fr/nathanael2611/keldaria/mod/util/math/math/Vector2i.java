package fr.nathanael2611.keldaria.mod.util.math.math;

/**
 * A simple vector that contains two integers
 */
public class Vector2i
{

    /* Variables */
    public int x;
    public int y;

    /**
     * Constructor
     */
    public Vector2i(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * Convert a Vector2i to String
     */
    @Override
    public String toString()
    {
        return x + "," + y;
    }

    /**
     * Create a Vector2i from a given String
     */
    public static Vector2i fromString(String str)
    {
        return new Vector2i(
                Integer.parseInt(str.split(",")[0]),
                Integer.parseInt(str.split(",")[1])
        );
    }
}
