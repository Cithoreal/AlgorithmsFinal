import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class RockPaperScissorsGame extends JFrame {
    private int playerScore = 0;
    private String playerMoveHistory = "";
    private int computerScore = 0;

    private JLabel playerScoreLabel;
    private JLabel computerScoreLabel;
    private JLabel resultLabel;

    public RockPaperScissorsGame() {
        setTitle("Rock-Paper-Scissors Game");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(Color.decode("#ECECEC"));

        playerScoreLabel = new JLabel("Player: " + playerScore);
        playerScoreLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        playerScoreLabel.setForeground(Color.BLUE);

        computerScoreLabel = new JLabel("Computer: " + computerScore);
        computerScoreLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        computerScoreLabel.setForeground(Color.RED);

        resultLabel = new JLabel("");
        resultLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        resultLabel.setHorizontalAlignment(JLabel.CENTER);

        String[] choices = {"Rock", "Paper", "Scissors"};
        JComboBox<String> choiceMenu = new JComboBox<>(choices);
        choiceMenu.setSelectedItem("Rock");

        JButton playButton = new JButton("Play!");
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                play((String) choiceMenu.getSelectedItem());
            }
        });

        setLayout(new FlowLayout());

        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");
        JMenuItem newGameMenuItem = new JMenuItem("New Game");
        JMenuItem changeNameMenuItem = new JMenuItem("Change Game Name");

        newGameMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newGame();
            }
        });

        changeNameMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeGameName();
            }
        });

        gameMenu.add(newGameMenuItem);
        gameMenu.add(changeNameMenuItem);
        menuBar.add(gameMenu);
        setJMenuBar(menuBar);

        add(choiceMenu);
        add(playButton);
        add(playerScoreLabel);
        add(computerScoreLabel);
        add(resultLabel);

        setVisible(true);
    }
    private String findPattern(int minLength)       //  this.playerMoveHistory is the data from our game
    {                                               //  minLength is the min size of our pattern
        int r = 0;         //
        int s = 0;         //   These are the highest amount of each move found during the pattern checking phase.
        int p = 0;         //

        for(int index = this.playerMoveHistory.length() - minLength ; index  >= 0 ; index--)       // Reformat this and look it over
        {                                                                                          // just a quick bandaid for now (I am very sleepy) :P

            String numOfR = "";         //  These are a local form of our global "r", "s", "p" variables.
            String numOfS = "";         //  We use these to compare with the global count variables at the end
            String numOfP = "";         //         of each pattern checking phase.

            String find = this.playerMoveHistory.substring(index);        //  find stores the pattern we are searching for.

            int historyIndex = 0;     // We use "historyIndex" as the starting index while we iterate through the global "this.playerMoveHistory"

            if (!find.isEmpty())
            {
                for (int pos = find.length(); pos < this.playerMoveHistory.length(); pos++)   //    We initialize pos as the end index while
                {                                                                             //       iterating through the global "this.playerMoveHistory"

                    if (this.playerMoveHistory.substring(historyIndex, pos).equals(find))     //   Comparing our gathered value to our pattern in "find".
                    {
                        switch (this.playerMoveHistory.substring(pos, pos + 1))    //   Whatever letter comes after our pattern should be
                        {                                                          //       recorded into our local "numOf" variables.
                            case "r" -> numOfR = numOfR.concat("r");
                            case "s" -> numOfS = numOfS.concat("s");
                            case "p" -> numOfP = numOfP.concat("p");
                        }
                    }
                    historyIndex++;    //  Moves the base index of "this.playerMoveHistory" along :P
                }
            }
            if (numOfR.length() > r)
            {
                r = numOfR.length();        //
            }                               //
            if (numOfS.length() > s)        //  Simply moves the local "numOfR" variables into the
            {                               //      global "r","s","p" variables if they are longer.
                s = numOfS.length();        //
            }                               //
            if (numOfP.length() > p)
            {
                p = numOfP.length();
            }

        }

        if (p > r && p > s)
        {
            return "Scissors";
        }                                             //
        else if (r > p && r > s)                      //
        {                                             //   Tries to find which global count appears more
            return "Paper";                           //
        }                                             //
        else if (s > p && s > r)
        {
            return "Rock";
        }
        else
        {
            Random rand = new Random();
            return switch (rand.nextInt(3))
            {                                              //   Otherwise we simply leave it up to random chance.
                case 0 -> "Rock";
                case 1 -> "Paper";
                default -> "Scissors";
            };

        }

        //   We can improve on this by adding meta strategies
        //   which use this algorithm with different min lengths
        //   and play the one that is correct most often.

    }


    private void removeOldHistory(int purgeLength)  //  "purgeLength"
    {                                               //      length of old data which will be removed.
        this.playerMoveHistory = this.playerMoveHistory.substring(purgeLength);
    }
    private void play(String playerChoice) {
        String computerChoice = findPattern(3);

        String result = determineWinner(playerChoice, computerChoice);
        playerChoice = playerChoice.substring(0,1);
        playerChoice = playerChoice.toLowerCase();

        this.playerMoveHistory = this.playerMoveHistory.concat(playerChoice);

        if(this.playerMoveHistory.length() > 20)    //  Too much old data can result in inaccurate results
        {
            removeOldHistory(5);
        }

        updateScores(result);

        resultLabel.setText("<html><div style='text-align: center;'>Computer chose: " + computerChoice + "<br>" + result + "</div></html>");
    }

    private String determineWinner(String playerChoice, String computerChoice) {
        if (playerChoice.equals(computerChoice)) {
            return "It's a tie!";
        } else if ((playerChoice.equals("Rock") && computerChoice.equals("Scissors")) ||
                (playerChoice.equals("Scissors") && computerChoice.equals("Paper")) ||
                (playerChoice.equals("Paper") && computerChoice.equals("Rock"))) {
            return "You win!";
        } else {
            return "Computer wins!";
        }
    }

    private void updateScores(String result) {
        if (result.equals("You win!")) {
            playerScore++;
        } else if (result.equals("Computer wins!")) {
            computerScore++;
        }

        playerScoreLabel.setText("Player: " + playerScore);
        computerScoreLabel.setText("Computer: " + computerScore);
        saveResults(result);
    }

    private void saveResults(String result) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        try (FileWriter fileWriter = new FileWriter("game_results.txt", true)) {
            fileWriter.write(timestamp + " - Result: " + result + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void newGame() {
        playerScore = 0;
        computerScore = 0;
        playerScoreLabel.setText("Player: " + playerScore);
        computerScoreLabel.setText("Computer: " + computerScore);
        resultLabel.setText("");
        JOptionPane.showMessageDialog(this, "New game started!");
    }

    private void changeGameName() {
        String newGameName = JOptionPane.showInputDialog(this, "Enter new game name:");
        if (newGameName != null && !newGameName.isEmpty()) {
            setTitle(newGameName);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RockPaperScissorsGame();
            }
        });
    }
}