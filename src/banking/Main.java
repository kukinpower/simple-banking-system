package banking;

import java.sql.SQLException;

public class Main {

  public static void main(String[] args) throws SQLException {
    AppConsole app;
    if (args.length == 2 && args[0].equals("-fileName") && args[1].endsWith(".s3db")) {
      app = new AppConsole(args[1]);
    } else {
      app = new AppConsole();
    }
    app.run();
    app.stop();
  }
}
