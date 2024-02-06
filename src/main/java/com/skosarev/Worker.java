package com.skosarev;

import com.skosarev.exception.NumberNotFoundException;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Worker {
    private static final int CPU_CORES = Runtime.getRuntime().availableProcessors();
    private static final ExecutorService executor = Executors.newFixedThreadPool(CPU_CORES);

    public static String execute(String algorithm, String checkSum, int length) throws NumberNotFoundException, InterruptedException {
        List<Callable<String>> tasks = new ArrayList<>();

        // Распределение промежутков чисел для проверки по разным задачам
        int upBorder = (int) Math.pow(10, length);
        int step = (int) Math.ceil(1.0 * upBorder / CPU_CORES);

        for (int i = 0; i < Math.pow(10, length); i += step) {
            final int from = i;
            final int to = Math.min(i + step, upBorder);

            tasks.add(() -> findAppropriateString(algorithm, checkSum, from, to, length));
        }

        // Ожидание результата
        String matchingString;
        try {
            matchingString = executor.invokeAny(tasks);
        } catch (ExecutionException e) {
            throw new NumberNotFoundException(0, upBorder);
        }
        executor.shutdown();

        return matchingString;
    }

    private static String findAppropriateString(String algorithm, String targetHash, int from, int to, int length) throws NumberNotFoundException {
        for (int i = from; i < to; i++) {
            String candidate = String.format("%0" + (length - 1) + "d", i);
            String candidateHash = getHash(algorithm, candidate);
            if (targetHash.equalsIgnoreCase(candidateHash)) {
                return candidate;
            }
        }
        throw new NumberNotFoundException(from, to);
    }


    private static String getHash(String algorithm, String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            byte[] encodedHash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return String.format("%032x", new BigInteger(1, encodedHash));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
