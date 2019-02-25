import cli.CommandLineParser;
import poller.Poller;

import java.util.Scanner;

public class Main {
    // Work with Malformed XML only, no Atom fields
    public static void main(String[] args) {

        CommandLineParser cli = new CommandLineParser();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Use -h for help\n");

        Poller poller = new Poller();
        Thread pollingThread = new Thread(poller, "Poller");

        pollingThread.start();

        while (true) {
            int result = cli.parse(scanner.nextLine());
            if (result != 0) {
                poller.stop();
                break;
            }
        }

        try {
            pollingThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
