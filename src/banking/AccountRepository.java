package banking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountRepository {

  private List<BankAccount> accounts;
  private Map<Card, BankAccount> mapCardsToAccounts;

  public AccountRepository() {
    this.accounts = new ArrayList<>();
    this.mapCardsToAccounts = new HashMap<>();
  }

  public BankAccount createAccount(Card card) {
    BankAccount account = createAccount(card.getNumber(), card.getPin());
    mapCardsToAccounts.put(card, account);
    return account;
  }

  public void deleteCard(Card card) {
    mapCardsToAccounts.get(card).unlinkCard(card);
    mapCardsToAccounts.remove(card);
  }

  public BankAccount createAccount(String cardNumber, String pin) {
    BankAccount bankAccount = new BankAccount();
    Card card = new Card(cardNumber, pin);
    bankAccount.openNewCard(card);
    accounts.add(bankAccount);
    mapCardsToAccounts.put(card, bankAccount);
    return bankAccount;
  }

  public BankAccount findAccountByCard(Card card) {
    return mapCardsToAccounts.get(card);
  }
}
