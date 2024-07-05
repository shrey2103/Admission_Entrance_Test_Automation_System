//Name:Shreyas
//RegNo:230970045
//Class:MCA 'A'
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ResultPage extends JFrame {
    private JLabel titleLabel;
    private JLabel scoreLabel;
    private JLabel messageLabel;
    private JButton closeButton;
    private String username;

    // Constructor
    public ResultPage(String username, int score, int totalQuestions) {
        this.username = username;
        setTitle("Quiz Result");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create main panel with GridBagLayout
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Image label
        ImageIcon imageIcon = new ImageIcon(getClass().getResource("/icons/done.png"));
        Image image = imageIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon scaledImageIcon = new ImageIcon(image);
        JLabel imageLabel = new JLabel(scaledImageIcon);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(imageLabel, gbc);

        // Title label
        titleLabel = new JLabel("Quiz Result");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(titleLabel, gbc);

        // Score label
        scoreLabel = new JLabel("Your Score: " + score + " out of " + totalQuestions);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(scoreLabel, gbc);

        // Message label
        messageLabel = new JLabel();
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        if (score == totalQuestions) {
            messageLabel.setText("Congratulations! " + username + " You got a perfect score!");
        } else if (score >= totalQuestions / 2) {
            messageLabel.setText("Well done! " + username + " You passed the quiz!");
        } else {
            messageLabel.setText("Better luck next time!");
        }
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(messageLabel, gbc);

        // Close button
        closeButton = new JButton("Close");
        closeButton.setFont(new Font("Arial", Font.PLAIN, 16));
        closeButton.setForeground(Color.WHITE);
        closeButton.setBackground(Color.RED);
        closeButton.setBorder(null);
        closeButton.setFocusPainted(false);
        closeButton.setPreferredSize(new Dimension(100, 50));

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(closeButton, gbc);

        // Add main panel to the frame
        add(mainPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Create and display the result page
                new ResultPage("JohnDoe", 3, 5).setVisible(true);
            }
        });
    }
}
