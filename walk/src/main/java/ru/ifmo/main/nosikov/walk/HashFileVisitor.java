package ru.ifmo.main.nosikov.walk;

import java.io.*;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

public class HashFileVisitor extends SimpleFileVisitor<Path> {

    private static int FNV_32_PRIME = 0x01000193;
    private static int bufferSize = 1024 * 1024 * 16;

    private Map<Path, Integer> pathToHashMap;

    public Map<Path, Integer> getPathToHashMap() {
        return pathToHashMap;
    }

    protected HashFileVisitor() {
        super();
        pathToHashMap = new HashMap<>();
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        System.out.println("Entering directory " + dir.toString());
        return super.preVisitDirectory(dir, attrs);
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        System.out.println("Exiting directory " + dir.toString());
        return super.postVisitDirectory(dir, exc);
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        System.out.println("Visiting file " + file.toString());
        BufferedReader reader = new BufferedReader(new FileReader(file.toFile()));
        char[] buffer = new char[bufferSize];
        int offset = 0;
        int fnvHash = 0;
        while (reader.ready()) {
            int length = reader.read(buffer, offset, bufferSize);
            fnvHash =  fnv(fnvHash, buffer, length);
        }
        pathToHashMap.put(file, fnvHash);
        reader.close();
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        System.out.println("Visit file " + file.toString() + " failed");
        System.out.println(exc.getMessage());
        return super.visitFileFailed(file, exc);
    }

    private int fnv(int fnvHash, char[] symbols, int length) {
        for (int i = 0; i < length; i++) {
            fnvHash *= FNV_32_PRIME;
            fnvHash ^= symbols[i];
        }
        return fnvHash;
    }
}
