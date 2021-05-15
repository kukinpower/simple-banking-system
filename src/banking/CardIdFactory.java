package banking;

public class CardIdFactory {
  private static int id;

  static int getId() {
    return id++;
  }
}
