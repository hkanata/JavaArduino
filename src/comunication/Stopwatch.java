package comunication;

public class Stopwatch
{
    private long start;

    public Stopwatch start()
    {
        start = System.currentTimeMillis();
        return this;
    }

    public long elapsed()
    {
        return System.currentTimeMillis() - start;
    }
}
