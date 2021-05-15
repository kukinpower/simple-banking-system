package banking;

import java.util.concurrent.ThreadLocalRandom;

public class CardService {

  private static String generateRandomIntSequenceStringOfLength(int length) {
    StringBuilder stringBuilder = new StringBuilder();

    ThreadLocalRandom random = ThreadLocalRandom.current();
    for (int i = 0; i < length; i++) {
      stringBuilder.append(random.nextInt(10));
    }
    return stringBuilder.toString();
  }

  public static String generateCardNumber() {
    return Card.BIN + generateRandomIntSequenceStringOfLength(10);
  }

  public static String generateCardPin() {
    return generateRandomIntSequenceStringOfLength(4);
  }
}
