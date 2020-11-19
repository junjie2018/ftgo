package fun.junjie.ftgo.common;

import lombok.*;

import java.math.BigDecimal;

@ToString
@EqualsAndHashCode
public class Money {
    public static Money ZERO = new Money(0);

    private BigDecimal amount;


    // region constructors

    public Money() {

    }

    public Money(BigDecimal amount) {
        this.amount = amount;
    }

    public Money(String amount) {
        this.amount = new BigDecimal(amount);
    }

    public Money(int amount) {
        this.amount = new BigDecimal(amount);
    }

    // endregion

    public Money add(Money delta) {
        return new Money(amount.add(delta.amount));
    }

    public boolean isGreaterThanOrEqual(Money other) {
        return amount.compareTo(other.amount) >= 0;
    }

    public String asString() {
        return amount.toPlainString();
    }

    public Money multiply(int x) {
        return new Money(amount.multiply(new BigDecimal(x)));
    }

    public Long asLong() {
        return multiply(100).amount.longValue();
    }
}
