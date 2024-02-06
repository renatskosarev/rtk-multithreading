package com.skosarev.command;


public record Request(String algorithm, String hash, int length) {
}
