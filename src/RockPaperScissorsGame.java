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
    private String[] allChoices = new String[4];
    private int computerScore = 0;
    private int[] metaChoices = new int[4];


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
        resultLabel.setFont(new Font("Arial", Font.PLAIN, 24)); // Fill some space
        resultLabel.setVerticalAlignment(0);
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
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(screenSize.width/2 - this.getWidth() /2, screenSize.height/2 -  this.getHeight() /2);
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

            int numOfR = 0;         //  These are a local form of our global "r", "s", "p" variables.
            int numOfS = 0;         //  We use these to compare with the global count variables at the end
            int numOfP = 0;         //         of each pattern checking phase.

            String find = this.playerMoveHistory.substring(index);        //  find stores the pattern we are searching for.

            int historyIndex = 0;     // We use "historyIndex" as the starting index while we iterate through the global "this.playerMoveHistory"

            if (!find.isEmpty())
            {
                for (int pos = find.length(); pos < this.playerMoveHistory.length(); pos++)   //    We initialize pos as the end index while
                {                                                          //       iterating through the global "this.playerMoveHistory"

                    if (this.playerMoveHistory.substring(historyIndex, pos).equals(find))    //   Comparing our gathered value to our pattern in "find".
                    {
                        switch (this.playerMoveHistory.substring(pos, pos + 1))    //   Whatever letter comes after our pattern should be
                        {                                                          //       recorded into our local "numOf" variables.
                            case "r" -> numOfR++;
                            case "s" -> numOfS++;
                            case "p" -> numOfP++;
                        }
                    }
                    historyIndex++;    //  Moves the base index of "this.playerMoveHistory" along :P
                }
            }
            if (numOfR > r)
            {
                r = numOfR;                 //
            }                               //
            if (numOfS > s)                 //  Simply moves the local "numOfR" variables into the
            {                               //      global "r","s","p" variables if they are longer.
                s = numOfS;                 //
            }                               //
            if (numOfP > p)
            {
                p = numOfP;
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

        //   We improve on this by adding meta strategies
        //   which use this algorithm with different min lengths
        //   and play the one that is correct most often.

    }

    private String pickBestPatternStartLength()     //  Decides which base length to use.
    {                                               //  A larger base length is more effective at picking longer patterns.
                                                    //  Very nice to have since player may not use the same short patterns again and again.
        allChoices[0] = findPattern(0);
        allChoices[1] = findPattern(3);
        allChoices[2] = findPattern(5);
        allChoices[3] = findPattern(8);
        int highestScore =0 ;
        int bestIndex = 0;
        int index = 0;
        for (int score : metaChoices)
        {

            if (score >= highestScore)
            {
                highestScore = score;
                bestIndex = index;
            }
            index++;
        }

    return allChoices[bestIndex];

    }

    private void recordMetaRecords(String computerChoice, String result )   //  Just sees if the computer is winning or losing.
    {                                                                       //  Then adjusts how accurate the pattern is projected to be.
        if (result.equalsIgnoreCase("You win!"))
        {
            for(int index = 0; index < metaChoices.length; index++)
            {
                if(allChoices[index].equals(computerChoice))
                {
                    metaChoices[index]--;
                }else
                {
                    metaChoices[index]++;
                }
            }
        } else if (result.equalsIgnoreCase("Computer wins!"))
        {
            for (int index = 0; index < metaChoices.length; index++) {
                if (allChoices[index].equals(computerChoice)) {
                    metaChoices[index]++;
                } else {
                    metaChoices[index]--;
                }
            }

        }
    }
    private void removeOldHistory(int purgeLength)  //  "purgeLength"
    {                                               //      length of old data which will be removed.
        this.playerMoveHistory = this.playerMoveHistory.substring(purgeLength);
    }
    private void play(String playerChoice) {
        String computerChoice = pickBestPatternStartLength();

        String result = determineWinner(playerChoice, computerChoice);
        playerChoice = playerChoice.substring(0,1);
        playerChoice = playerChoice.toLowerCase();
        recordMetaRecords(computerChoice, result);
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