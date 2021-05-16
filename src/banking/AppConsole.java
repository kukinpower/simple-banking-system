package banking;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.InputMismatchException;
import java.util.Scanner;

public class AppConsole {

  private final static String mainMenu = String.join(System.lineSeparator(),
      "1. Create an account",
      "2. Log into account",
      "0. Exit");
  private final static String accountMenu = String.join(System.lineSeparator(),
      "1. Balance",
      "2. Log out",
      "0. Exit");

  private final static String CREATE_TABLE = "create table IF NOT EXISTS card"
      + " (id INTEGER primary key,"
      + " number TEXT NOT NULL,"
      + " pin TEXT NOT NULL,"
      + " balance INTEGER DEFAULT 0)";
  private final static String INSERT_CARD = "insert into card(number, pin) values(?, ?)";
  private final static String FIND_CARD = "select * from card"
      + " where number = '%s' and pin = '%s'";
  private final static String GET_CARD_BALANCE = "select balance from card"
      + " where number = '%s' and pin = '%s'";

  private String currentNumber;
  private String currentPin;

  private Connection connection;

  private final Scanner scanner;
  private AccountRepository accountRepository;
  private BankAccount activeAccount;
  private String DB_PATH = "default.s3db";
  private String JDBC_DB = "jdbc:sqlite:";

  public AppConsole(String dbPath) {
    this();
    this.DB_PATH = dbPath;
  }

  public AppConsole() {
    this.scanner = new Scanner(System.in);
    this.accountRepository = new AccountRepository();
    this.activeAccount = null;
  }

  public void createNewAccount() throws SQLException {
    String cardNumber = CardService.generateCardNumber();
    String cardPin = CardService.generateCardPin();
//    accountRepository.createAccount(cardNumber, cardPin);
    insertCardToTable(cardNumber, cardPin);

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

  public boolean logIntoAccount() throws SQLException {
    System.out.println("Enter your card number:");
    String cardNumber = scanner.next();
    System.out.println("Enter your PIN:");
    String pin = scanner.next();
    BankAccount account = null;
    if (!isValidInput(cardNumber, pin)
        || !findCardInDatabase(cardNumber, pin)) {
      System.out.println("Wrong card number or PIN!");
      return false;
    }
    currentNumber = cardNumber;
    currentPin = pin;
    System.out.println("You have successfully logged in!");
    return true;
  }

  private void logOut() {
    activeAccount = null;
    System.out.println("You have successfully logged out!");
  }

  private boolean loginMenu() throws SQLException {
    if (!logIntoAccount()) {
      return true;
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
          System.out.println(queryCardBalance(currentNumber, currentPin));
          break;
        case 2:
          System.out.println();
          logOut();
          return true;
        case 0:
          return false;
        default:
          break;
      }
      System.out.println();
    }
  }

  private void openConnection() throws IOException, SQLException {
    connection = DriverManager.getConnection(JDBC_DB + DB_PATH);
  }

  private boolean findCardInDatabase(String number, String pin) throws SQLException {
    try (Statement statement = connection.createStatement()) {
      ResultSet resultSet = statement.executeQuery(String.format(FIND_CARD, number, pin));

      if (!resultSet.next()) {
        return false;
      }
      return true;
    }
  }

  private int queryCardBalance(String number, String pin) throws SQLException {
    try (Statement statement = connection.createStatement()) {
      ResultSet resultSet = statement.executeQuery(String.format(GET_CARD_BALANCE, number, pin));
      resultSet.next();
      return resultSet.getInt(1);
    }
  }

  private void createTable() throws SQLException {
    try (PreparedStatement preparedStatement = connection.prepareStatement(CREATE_TABLE)) {
      preparedStatement.executeUpdate();
    }
  }

  private void insertCardToTable(String number, String pin) throws SQLException {
    PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CARD);
    preparedStatement.setString(1, number);
    preparedStatement.setString(2, pin);
    preparedStatement.executeUpdate();
    preparedStatement.close();
  }

  public void run() {
    try {
      openConnection();
      createTable();

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
            if (!loginMenu()) {
              return;
            }
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
    } catch (IOException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void stop() throws SQLException {
    scanner.close();
    connection.close();
  }
}
