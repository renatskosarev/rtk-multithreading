package com.skosarev.command;

import com.skosarev.exception.IncorrectArgsException;

public class CommandParser {
    public static Request parse(String[] args) throws IncorrectArgsException {
        if (args.length != 6) {
            throw new IncorrectArgsException();
        }

        String alg = null;
        String hash = null;
        Integer len = null;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-a": {
                    alg = args[++i];
                    break;
                }
                case "-h": {
                    hash = args[++i];
                    break;
                } case "-l": {
                    len = Integer.parseInt(args[++i]);
                    break;
                } default:
                    throw new IncorrectArgsException();
            }
        }

        if (alg == null || hash == null || len == null) {
            throw new IncorrectArgsException();
        }

        return new Request(alg, hash, len);
    }
}
