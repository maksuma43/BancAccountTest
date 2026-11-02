import java.util.Objects;

public final class Balance
{
    private final long count;
    private final Currency curr;

    public Balance()
    {
        this(0L, Currency.RUB);
    }

    public Balance(long count, Currency curr)
    {
        if (count < 0) {
            throw new IllegalArgumentException("Баланс не может быть отрицательным: " + count);
        }
        this.count = count;
        this.curr = Objects.requireNonNull(curr, "curr");
    }

    public long getCount() { return count; }
    public Currency getCurr() { return curr; }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Balance balance = (Balance) o;
        return count == balance.count && curr == balance.curr;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(count, curr);
    }

    @Override
    public String toString()
    {
        return String.format("%d.%02d %s", count / 100, count % 100, curr.getSymbol());
    }
}