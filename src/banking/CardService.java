package banking;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class CardService {

  private static String generateRandomIntSequenceStringOfLength(int length) {
    StringBuilder stringBuilder = new StringBuilder();

    ThreadLocalRandom random = ThreadLocalRandom.current();
    for (int i = 0; i < length; i++) {
      stringBuilder.append(random.nextInt(10));
    }
    return stringBuilder.toString();
  }

  public static String addChecksumByLuhnAlgorithm(String digits) {
    int[] arr = new int[digits.length()];
    for (int i = 0; i < digits.length(); i++) {
      int digit = digits.charAt(i) - '0';
      if (i % 2 == 0) {
        arr[i] = digit * 2 > 9 ? digit * 2 - 9 : digit * 2;
      } else {
        arr[i] = digit;
      }
    }

    int sum = Arrays.stream(arr).sum();
    int checksum = sum % 10 == 0 ? 0 : 10 - (sum % 10);

    return digits + checksum;
  }

  public static String generateCardNumber() {
    return addChecksumByLuhnAlgorithm(Card.BIN + generateRandomIntSequenceStringOfLength(9));
  }

  public static String generateCardPin() {
    return generateRandomIntSequenceStringOfLength(4);
  }
}
