package com.semaphore.smokers.entity;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Barmen extends Thread {
    private final Random rnd = new Random();
    private final Table table;
    private final Semaphore barmen;

    public Barmen(Table table, Semaphore barmen) {
        this.table = table;
        this.barmen = barmen;
    }

    @Override
    public void run() {
        while (true) {
            try {
                barmen.acquire();
                provideIngredients();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void provideIngredients() {
        int temp = rnd.nextInt(3);
        if (temp == 0) {
            System.out.println(".. Barmen .. puts paper and matches on the table");
            table.putPaper();
            table.putMatches();
        }
        if (temp == 1) {
            System.out.println(".. Barmen .. puts pushing tobacco and matches on the table");
            table.putTobacco();
            table.putMatches();
        }
        if (temp == 2) {
            System.out.println(".. Barmen .. puts pushing tobacco and paper on the table");
            table.putTobacco();
            table.putPaper();
        }
    }

}
