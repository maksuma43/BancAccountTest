import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class BankAccount
{
    private static final AtomicLong ID_GENERATOR = new AtomicLong();

    private final long id;
    private String accName;
    private Balance accBalance;

    public BankAccount()
    {
        this.id = ID_GENERATOR.incrementAndGet();
        this.accBalance = new Balance(0L, Currency.RUB);
    }

    public BankAccount(String accName)
    {
        this(accName, 0L, Currency.RUB);
    }

    public BankAccount(String accName, long count, Currency curr)
    {
        this.id = ID_GENERATOR.incrementAndGet();
        this.accName = Objects.requireNonNull(accName, "accName");
        this.accBalance = new Balance(count, curr);
    }

    public String getName() { return accName; }
    public Balance getBalance() { return accBalance; }

    public void add(long count, Currency curr)
    {
        requirePositive(count);
        Objects.requireNonNull(curr, "curr");

        Currency currentCurr = accBalance.getCurr();
        long currentCount = accBalance.getCount();

        long delta = convertAmount(count, curr, currentCurr);
        long newCount = Math.addExact(currentCount, delta);

        accBalance = new Balance(newCount, currentCurr);
    }

    public void subtract(long count)
    {
        requirePositive(count);

        long currentCount = accBalance.getCount();
        if (count > currentCount)
        {
            throw new IllegalStateException("На счёте" + accName + "недостаточно средств");
        }
        accBalance = new Balance(currentCount - count, accBalance.getCurr());
    }

    public void convert(Currency curr)
    {
        Objects.requireNonNull(curr, "curr");

        Currency from = accBalance.getCurr();
        if (from == curr) return;

        long newCount = convertAmount(accBalance.getCount(), from, curr);
        accBalance = new Balance(newCount, curr);
    }

    public void transfer(BankAccount target, long count)
    {
        Objects.requireNonNull(target, "target");
        if (target == this)
        {
            throw new IllegalArgumentException("Нельзя перевести средства на тот же счёт");
        }
        requirePositive(count);

        long currentCount = accBalance.getCount();
        if (count > currentCount)
        {
            throw new IllegalStateException("Недостаточно средств для перевода со счёта" + accName);
        }
        target.add(count, accBalance.getCurr());
        subtract(count);
    }

    public void showBalance()
    {
        Balance b = accBalance;
        System.out.printf("Средств на счёте %s: %d.%02d %s%n", accName, b.getCount() / 100, b.getCount() % 100, b.getCurr().getSymbol());
    }

    private static void requirePositive(long count)
    {
        if (count <= 0)
        {
            throw new IllegalArgumentException("Сумма должна быть положительной: " + count);
        }
    }

    private static long convertAmount(long count, Currency from, Currency to)
    {
        if (from == to) return count;
        long numerator = Math.multiplyExact(count, from.getPriceInRublesKopecks());
        long toPrice = to.getPriceInRublesKopecks();
        return Math.addExact(numerator, toPrice / 2) / toPrice;
    }

    public void rename(String name)
    {
        this.accName=validateName(name);
    }

    private static String validateName(String name)
    {
        Objects.requireNonNull(name, "accName");
        String trimmed = name.strip();

        if (trimmed.isEmpty())
        {
            throw new IllegalArgumentException("Имя счёта не может быть пустым");
        }
        if (trimmed.length() > 100)
        {
            throw new IllegalArgumentException("Имя счёта слишком длинное: " + trimmed.length());
        }
        if (!trimmed.matches("[\\p{L}\\p{N} .,\\-()]+"))
        {
            throw new IllegalArgumentException("Недопустимые символы в имени счёта: " + trimmed);
        }
        return trimmed;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankAccount that = (BankAccount) o;
        return id == that.id;
    }

    @Override
    public int hashCode()
    {
        return Long.hashCode(id);
    }

    @Override
    public String toString()
    {
        return "BankAccount{id=" + id + ", name='" + accName + "', balance=" + accBalance + "}";
    }
}