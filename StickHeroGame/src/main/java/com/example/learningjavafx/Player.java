package com.example.learningjavafx;

import java.io.Serializable;

public class Player implements Serializable {
    //Singleton Design Pattern
    private static final long serialVersionUID = 1L;

    private static Player instance;

    private int lastScore;
    private int highestScore;
    private int numberOfCherries;

    private Player() {
        // Initialize attributes if needed
        lastScore = 0; // Initialize with a very high value
        highestScore = 0;
        numberOfCherries = 0;
    }

    public static Player getInstance() {
        if (instance == null) {
            synchronized (Player.class) {
                if (instance == null) {
                    instance = new Player();
                }
            }
        }
        return instance;
    }

    public int getLastScore() {
        return lastScore;
    }

    public void setLastScore(int lowestScore) {
        this.lastScore = lowestScore;
    }

    public int getHighestScore() {
        return highestScore;
    }

    public void setHighestScore(int highestScore) {
        this.highestScore = highestScore;
    }

    public int getNumberOfCherries() {
        return numberOfCherries;
    }

    public void setNumberOfCherries(int numberOfCherries) {
        this.numberOfCherries = numberOfCherries;
    }

    // Add any other methods or getters/setters as needed
}
