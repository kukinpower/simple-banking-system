package banking;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BankAccount {

  private BigDecimal balance;
  private boolean isActualBalance = false;
  private final List<Card> cards;
  private int id;

  public BankAccount() {
    this.balance = new BigDecimal(0);
    this.cards = new ArrayList<>();
    this.id = CardIdFactory.getId();
  }

  public void openNewCard(String number, String pin) {
    cards.add(new Card(number, pin));
  }

  public void openNewCard(Card card) {
    cards.add(card);
  }

  public void increaseBalance(Card card, BigDecimal amount) {
    card.increaseBalance(amount);
    setChangedBalance();
  }

  public void decreaseBalance(Card card, BigDecimal amount) {
    card.decreaseBalance(amount);
    setChangedBalance();
  }

  public void setChangedBalance() {
    isActualBalance = false;
  }

  public void addExistingCard(Card card) {
    cards.add(card);
    if (!card.getBalance().equals(BigDecimal.ZERO)) {
      balance = balance.add(card.getBalance());
    }
  }

  public void unlinkCard(Card card) {
    cards.remove(card);
  }

  public BigDecimal getAccountBalance() {
    if (!isActualBalance) {
      return balance;
    }
    balance = cards.stream()
        .map(Card::getBalance)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    isActualBalance = true;

    return balance;
  }
}
