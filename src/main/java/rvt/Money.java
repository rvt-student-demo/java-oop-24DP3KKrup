package rvt;
public class Money {

    private final int eiro;
    private final int centi;

    public Money(int eiro, int centi) {
        this.eiro = eiro;
        this.centi = centi;
    }

    public int eiro() {
        return eiro;
    }

    public int centi() {
        return centi;
    }

    public String toString() {
        String zero = "";
        if (centi < 10) {
            zero = "0";
        }
        return eiro + "." + zero + centi + "eur";
    }

    public Money plus(Money add) {
        int eiro = this.eiro + add.eiro;
        int centi = this.centi + add.centi;

        if (centi >= 100) {
            eiro = eiro + 1;
            centi = centi - 100;
        }

        return new Money(eiro, centi);
    }

    public boolean lessThan(Money compared) {
        if (this.eiro < compared.eiro) {
            return true;
        }

        if (this.eiro == compared.eiro && this.centi < compared.centi) {
            return true;
        }

        return false;
    }

    public Money minus(Money decr) {
        int eiro = this.eiro - decr.eiro;
        int centi = this.centi - decr.centi;

        if (centi < 0) {
            centi = centi + 100;
            eiro = eiro - 1;
        }

        if (eiro < 0) {
            return new Money(0, 0);
        }

        return new Money(eiro, centi);
    }

    public static void main(String[] args) {

        Money a = new Money(10, 0);
        Money b = new Money(5, 0);

        Money c = a.plus(b);

        System.out.println(a);
        System.out.println(b);
        System.out.println(c);

        a = a.plus(c);

        System.out.println(a);
        System.out.println(b);
        System.out.println(c);


        Money x = new Money(10, 0);
        Money y = new Money(3, 0);
        Money z = new Money(5, 0);

        System.out.println(x.lessThan(y));
        System.out.println(y.lessThan(z));


        Money m = new Money(10, 0);
        Money n = new Money(3, 50);

        Money o = m.minus(n);

        System.out.println(m);
        System.out.println(n);
        System.out.println(o);

        o = o.minus(m);

        System.out.println(m);
        System.out.println(n);
        System.out.println(o);
    }
}
