package hello.faces2;
import java.io.Serializable;
import java.util.Random;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class Guessing implements Serializable {
    private int number = Math.abs(new Random(System.currentTimeMillis()).nextInt() % 1024);
    private int guessesSoFar;
    private int maxGuesses = 10;
    private int guess;
    private String output;

    public void submit() {
        if (maxGuesses <= guessesSoFar) {
            output = "Failed to guess number " + number + ". Try again.";
            number = Math.abs(new Random(System.currentTimeMillis()).nextInt() % 1024);
            guessesSoFar = 0;
        } else if (guess < number) {
            output = "Too low...";
            guessesSoFar++;
        } else if (guess > number) {
            output = "Too high...";
            guessesSoFar++;
        } else {
            output = "Winner in " + guessesSoFar + " tries! Try again?";
            number = Math.abs(new Random(System.currentTimeMillis()).nextInt() % 1024);
            guessesSoFar = 0;
        }
    }

    public int getGuess() {
        return this.guess;
    }

    // public int getGuessesSoFar() {
    //     return guessesSoFar;
    // }

    // public void setGuessesSoFar(int guessesSoFar) {
    //     this.guessesSoFar = guessesSoFar;
    // }

    public void setGuess(int guess) {
        this.guess = guess;
    }

    public String getOutput() {
        return this.output;
    }
}
