package banking;

import java.math.BigDecimal;

public class Card {
  public static String BIN = "400000";
  private String pin;
  private String number;
  private BigDecimal balance;

  public Card(String number, String pin) {
    this.pin = pin;
    this.number = number;
    this.balance = new BigDecimal(0);
  }

  public BigDecimal getBalance() {
    return balance;
  }

  public void increaseBalance(BigDecimal amount) {
    balance = balance.add(amount);
  }

  public void decreaseBalance(BigDecimal amount) {
    balance = balance.subtract(amount);
  }

  public String getPin() {
    return pin;
  }

  public String getNumber() {
    return number;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Card card = (Card) o;

    if (!pin.equals(card.pin)) {
      return false;
    }
    if (!number.equals(card.number)) {
      return false;
    }
    return balance.equals(card.balance);
  }

  @Override
  public int hashCode() {
    int result = pin.hashCode();
    result = 31 * result + number.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "Card{" +
        "pin='" + pin + '\'' +
        ", number='" + number + '\'' +
        ", balance=" + balance +
        '}';
  }
}
