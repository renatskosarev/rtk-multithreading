package com.skosarev;


import com.skosarev.command.CommandParser;
import com.skosarev.command.Request;
import com.skosarev.exception.IncorrectArgsException;
import com.skosarev.exception.NumberNotFoundException;

public class App {
    public static void main(String[] args) {
        try {
            Request req = CommandParser.parse(args);

            long msBefore = System.currentTimeMillis();
            String result = Worker.execute(req.algorithm(), req.hash(), req.length());
            System.out.println(result + " (" + (System.currentTimeMillis() - msBefore) + "ms)");

        } catch (IncorrectArgsException e) {
            System.out.println("Incorrect parameters.");
        } catch (NumberNotFoundException e) {
            System.out.println("The number for the specified hash was not found.");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.exit(0);
    }
}
