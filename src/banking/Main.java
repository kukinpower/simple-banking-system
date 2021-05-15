package banking;

import java.util.InputMismatchException;
import java.util.Scanner;

class AppConsole {

  private final static String mainMenu = String.join(System.lineSeparator(),
      "1. Create an account",
      "2. Log into account",
      "0. Exit");
  private final static String accountMenu = String.join(System.lineSeparator(),
      "1. Balance",
      "2. Log out",
      "0. Exit");

  private final Scanner scanner;
  private AccountRepository accountRepository;
  private BankAccount activeAccount;

  public AppConsole() {
    this.scanner = new Scanner(System.in);
    this.accountRepository = new AccountRepository();
    this.activeAccount = null;
  }

  public void createNewAccount() {
    String cardNumber = CardService.generateCardNumber();
    String cardPin = CardService.generateCardPin();
    accountRepository.createAccount(cardNumber, cardPin);

    System.out.println(String.join(System.lineSeparator(),
        "Your card has been created",
        "Your card number:",
        cardNumber,
        "Your card PIN:",
        cardPin));
  }

  private boolean isValidInput(String cardNumber, String pin) {
    if (!cardNumber.matches("400000.*")) {
      return false;
    }
    return true;
  }

  private BankAccount getAccountByCard(Card card) {
    return accountRepository.findAccountByCard(card);
  }

  public boolean logIntoAccount() {
    System.out.println("Enter your card number:");
    String cardNumber = scanner.next();
    System.out.println("Enter your PIN:");
    String pin = scanner.next();
    BankAccount account = null;
    if (!isValidInput(cardNumber, pin)
        || (account = getAccountByCard(new Card(cardNumber, pin))) == null) {
      System.out.println("Wrong card number or PIN!");
      return false;
    }
    activeAccount = account;
    System.out.println("You have successfully logged in!");
    return true;
  }

  private void logOut() {
    activeAccount = null;
    System.out.println("You have successfully logged out!");
  }

  private void loginMenu() {
    if (!logIntoAccount()) {
      return;
    }
    System.out.println();
    while (true) {
      System.out.println(accountMenu);
      int choice = 3;
      try {
        choice = scanner.nextInt();
      } catch (InputMismatchException e) {
        scanner.next();
        System.out.println("Bad input");
        continue;
      }
      switch (choice) {
        case 1:
          System.out.println();
          System.out.println("Balance: " + activeAccount.getAccountBalance());
          break;
        case 2:
          System.out.println();
          logOut();
          return;
        case 0:
          return;
        default:
          break;
      }
      System.out.println();
    }
  }

  public void run() {
    try {
      while (true) {
        System.out.println(mainMenu);
        int choice = 3;
        try {
          choice = scanner.nextInt();
        } catch (InputMismatchException e) {
          System.out.println("Bad input");
          scanner.next();
          continue;
        }
        System.out.println();
        switch (choice) {
          case 1:
            createNewAccount();
            break;
          case 2: {
            loginMenu();
            break;
          }
          case 0:
            System.out.println("Bye!");
            return;
          default:
            continue;
        }
        System.out.println();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void stop() {
    scanner.close();
  }
}

public class Main {

  public static void main(String[] args) {
    AppConsole app = new AppConsole();
    app.run();
    app.stop();
  }
}
