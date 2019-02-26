import cli.CommandLineParser;
import lombok.extern.slf4j.Slf4j;
import poller.Poller;

import java.util.Scanner;

@Slf4j
public class Main {
    public static void main(String[] args) {

        // Initialize CLI
        CommandLineParser cli = new CommandLineParser();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Use -h for help\n");

        // Initialize thread and start it
        Poller poller = new Poller();
        Thread pollingThread = new Thread(poller, "Poller");
        pollingThread.start();

        while (true) {
            int result = cli.parse(scanner.nextLine());
            // Graceful thread stop
            if (result != 0) {
                poller.stop();
                break;
            }
        }

        try {
            // Graceful shutdown
            pollingThread.join();
        } catch (InterruptedException e) {
            log.error("[ERROR] Waits for thread dieing is interrupted: " + e.getMessage());
        }
    }
}
