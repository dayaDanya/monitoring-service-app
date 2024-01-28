package org.ylab;

import org.ylab.cli.ConsoleCLI;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        ConsoleCLI cli  = new ConsoleCLI();
        try {
            cli.run();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}