package comunication;

@FunctionalInterface
public interface Callback<T>
{
    public abstract void run(final T result);
}
