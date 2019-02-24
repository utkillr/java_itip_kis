import cli.CommandLineParser;

import java.util.Scanner;

public class Main {
    // Work with Malformed XML only, no Atom fields
    public static void main(String[] args) throws InterruptedException {

        CommandLineParser cli = new CommandLineParser();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            cli.parse(scanner.nextLine());
        }
    }
}
