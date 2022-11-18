package com.semaphore.smokers;

import com.semaphore.smokers.entity.Barmen;
import com.semaphore.smokers.entity.Smoker;
import com.semaphore.smokers.entity.Table;

import java.util.concurrent.Semaphore;

public class Runner {
    static Semaphore barmenSem = new Semaphore(1);
    static Semaphore smokerWithTobaccoSem = new Semaphore(0);
    static Semaphore smokerWithPaperSem = new Semaphore(0);
    static Semaphore smokerWithMatchesSem = new Semaphore(0);

    public static void main(String[] args) {
        Table table = new Table(smokerWithTobaccoSem, smokerWithPaperSem, smokerWithMatchesSem);
        new Barmen(table, barmenSem).start();
        new Smoker(smokerWithTobaccoSem, barmenSem, Constants.smokerWithTobacco).start();
        new Smoker(smokerWithPaperSem, barmenSem, Constants.smokerWithPaper).start();
        new Smoker(smokerWithMatchesSem, barmenSem, Constants.smokerWithMatches).start();
    }

}
