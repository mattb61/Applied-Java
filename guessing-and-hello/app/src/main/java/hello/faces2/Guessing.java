package hello.faces2;

import java.io.Serializable;
import java.util.Random;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class Guessing implements Serializable {
    private int number = Math.abs(new Random().nextInt() % 1024);
    private int maxGuesses = 10;
    private int guessesSoFar;
    private int guess;
    private String output;

    public void submit() {
        if (maxGuesses <= guessesSoFar) {
            output = "Failed to guess number " + number + ". Try again!";
            guessesSoFar = 0;
            number= Math.abs(new Random().nextInt() % 1024);
        } else if (guess < number) {
            output = "Too low... Guesses so far: " + guessesSoFar;
            guessesSoFar++;
        } else if (guess > number) {
            output = "Too high... Guesses so far: " + guessesSoFar;
            guessesSoFar++;
        } else {
            output = "Winner in " + guessesSoFar + " tries! Try again!";
            guessesSoFar = 0;
            number= Math.abs(new Random().nextInt() % 1024);
        }
    }

    public int getGuess() {
        return this.guess;
    }

    public void setGuess(int guess) {
        this.guess = guess;
    }

    public int getGuessesSoFar() {
        return guessesSoFar;
    }

    public void setGuessesSoFar(int guessesSoFar) {
        this.guessesSoFar = guessesSoFar;
    }

    public String getOutput() {
        return this.output;
    }

    public void setOutput(String output) {
        this.output = output;
    }
}
