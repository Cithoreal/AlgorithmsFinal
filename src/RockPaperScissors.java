package src;

import com.github.cliftonlabs.json_simple.JsonObject;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;


public class RockPaperScissors implements Runnable {

    public enum moves {ROCK, PAPER, SCISSORS}

    private int playerScore;
    private int computerScore;

    private int[] playerMoves;
    private int[] computerMoves;

    private JFrame f;
    public RockPaperScissors() {
        f = new JFrame("Rock, Paper, Scissors");
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setLayout(new FlowLayout());
        f.setBounds(100,100,180,140);
        f.add(new JLabel("Your Move:"));
        f.add(new JButton("Rock"));
        f.add(new JButton("Paper"));
        f.add(new JButton("Scissors"));
    }

    public void run(){
        f.pack();
        f.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new RockPaperScissors());
    }

    public String determineWinner(moves playerChoice, moves computerChoice) {
        if (playerChoice == computerChoice) {
            return ("Draw");
        } else if (playerChoice == moves.PAPER && computerChoice == moves.ROCK ||
                playerChoice == moves.ROCK && computerChoice == moves.SCISSORS ||
                playerChoice == moves.SCISSORS && computerChoice == moves.PAPER) {
            return ("Win");
        } else
            return ("Lose");
    }

    public void updateScores(String result) throws IOException {
        if (Objects.equals(result, "Win"))
            playerScore++;
        else if (Objects.equals(result, "Lose"))
            computerScore++;
        //Update GUI with latest scores
        saveResults(result);
    }

    public void saveResults(String result) throws IOException {
        LocalDateTime timestamp = LocalDateTime.now();
        JsonObject jsonObject = new JsonObject();
        try (FileWriter file = new FileWriter("save.json")){
            //jsonObject.put("")
            //file.write(jsonObject.toJsonString());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play(moves playerChoice) throws IOException {
        //Use an algorithm to analyze past moves and determine the highest probability winning move
        moves computerChoice = moves.ROCK;
        String result = determineWinner(playerChoice, computerChoice);
        updateScores(result);
        //Update GUI
    }

    public void newGame(){
        playerScore = 0;
        computerScore = 0;

    }
}