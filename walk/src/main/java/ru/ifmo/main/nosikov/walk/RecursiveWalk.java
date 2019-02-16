package ru.ifmo.main.nosikov.walk;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class RecursiveWalk {

    public static String inputFileName = "C:\\university\\java\\walk\\src\\main\\resources\\input.txt";
    public static String outputFileName = "C:\\university\\java\\walk\\src\\main\\resources\\output.txt";

    public static void main(String[] args) {
        RecursiveWalk recursiveWalk = new RecursiveWalk();
        // recursiveWalk.run(args[0], args[1]);
        recursiveWalk.run(inputFileName, outputFileName);
    }

    public void run(String input, String output) {
        List<String> files;
        List<Map> result = new ArrayList<>();
        try {
            files = readFileNames(input);
        } catch (FileNotFoundException e) {
            System.out.println("Can't find file: " + input);
            System.out.println(e.getMessage());
            return;
        }

        for (String file : files) {
            try {
                HashFileVisitor visitor = new HashFileVisitor();
                Files.walkFileTree(new File(file).toPath(), visitor);
                result.add(visitor.getPathToHashMap());
            } catch (IOException e) {
                System.out.println("Error reading file " + file + " occurred");
                System.out.println(e.getMessage());
            }
        }

        try {
            printResult(output, result);
        } catch (FileNotFoundException e) {
            System.out.println("Error printing to file: " + output);
            System.out.println(e.getMessage());
        }
    }

    public List<String> readFileNames(String path) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(path));
        ArrayList<String> list = new ArrayList<>();
        while (scanner.hasNext()){
            list.add(scanner.next());
        }
        scanner.close();
        return list;
    }

    public void printResult(String path, List<Map> result) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(path);
        result.forEach(map -> map.forEach(
                (file, fnvHash) -> {
                    String hex = String.format("%08x", (Integer)fnvHash);
                    writer.println(hex + " " + file);
                }
        ));
        writer.close();
    }
}
