public class Main
{
    public static void main()
    {
        try
        {
            BankAccount A = new BankAccount("Sasha");
            BankAccount B = new BankAccount("Dima", 1000000000L, Currency.RUB);
            A.showBalance();
            B.showBalance();
            A.add(100000L, Currency.RUB);
            B.convert(Currency.USD);
            A.showBalance();
            B.showBalance();
            B.transfer(A, 6000L);
            A.rename("Koklush");
            A.showBalance();
            B.showBalance();
            B.add(70000, Currency.EUR);
            A.subtract(800);
            A.showBalance();
            B.showBalance();
        }
        catch(IllegalArgumentException | IllegalStateException | NullPointerException | ArithmeticException e)
        {
            System.out.println(e.getClass().getName());
            System.out.println(e.getMessage());
        }
    }
}