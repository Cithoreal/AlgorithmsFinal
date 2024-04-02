package project;
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

    private void play(String playerChoice) {
        String[] choices = {"Rock", "Paper", "Scissors"};
        String computerChoice = choices[new Random().nextInt(choices.length)];

        String result = determineWinner(playerChoice, computerChoice);
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