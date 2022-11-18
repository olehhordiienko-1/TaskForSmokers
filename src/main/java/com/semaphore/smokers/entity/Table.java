package com.semaphore.smokers.entity;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Table {
    private final Semaphore tobaccoSem = new Semaphore(0);
    private final Semaphore paperSem = new Semaphore(0);
    private final Semaphore matchSem = new Semaphore(0);
    private final Semaphore smokerWithTobaccoSem;
    private final Semaphore smokerWithPaperSem;
    private final Semaphore smokerWithMatchesSem;
    private final Lock mutex = new ReentrantLock();
    private boolean isTobacco = false;
    private boolean isPaper = false;
    private boolean isMatch = false;

    public Table(Semaphore smokerWithTobaccoSem, Semaphore smokerWithPaperSem, Semaphore smokerWithMatchesSem) {
        this.smokerWithTobaccoSem = smokerWithTobaccoSem;
        this.smokerWithPaperSem = smokerWithPaperSem;
        this.smokerWithMatchesSem = smokerWithMatchesSem;

        initWorkers();
    }

    public void putTobacco() {
        tobaccoSem.release();
    }

    public void putPaper() {
        paperSem.release();
    }

    public void putMatches() {
        matchSem.release();
    }


    private void initWorkers() {
        Thread workerTobacco = new Thread(() -> {
            while (true) {
                try {
                    tobaccoSem.acquire();
                    mutex.lock();
                    try {
                        if (isPaper) {
                            isPaper = false;
                            smokerWithMatchesSem.release();
                        } else if (isMatch) {
                            isMatch = false;
                            smokerWithPaperSem.release();
                        } else {
                            isTobacco = true;
                        }
                    } finally {
                        mutex.unlock();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread workerPaper = new Thread(() -> {
            while (true) {
                try {
                    paperSem.acquire();
                    mutex.lock();
                    try {
                        if (isTobacco) {
                            isTobacco = false;
                            smokerWithMatchesSem.release();
                        } else if (isMatch) {
                            isMatch = false;
                            smokerWithTobaccoSem.release();
                        } else {
                            isPaper = true;
                        }
                    } finally {
                        mutex.unlock();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread workerMatches = new Thread(() -> {
            while (true) {
                try {
                    matchSem.acquire();
                    mutex.lock();
                    try {
                        if (isPaper) {
                            isPaper = false;
                            smokerWithTobaccoSem.release();
                        } else if (isTobacco) {
                            isTobacco = false;
                            smokerWithPaperSem.release();
                        } else {
                            isMatch = true;
                        }
                    } finally {
                        mutex.unlock();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        workerTobacco.start();
        workerMatches.start();
        workerPaper.start();
    }
}