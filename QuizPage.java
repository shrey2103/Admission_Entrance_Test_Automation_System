//Name:Shreyas
//RegNo:230970045
//Class:MCA 'A'
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuizPage extends JFrame {
    private JLabel imageLabel;
    private JLabel questionLabel;
    private JRadioButton[] options;
    private JButton nextButton;
    private JButton submitButton;
    private JButton previousButton;
    private ButtonGroup buttonGroup;
    private JPanel mainPanel;
    private List<Integer> questionIndexes;
    private int currentQuestionIndex = 0;
    private int totalQuestions = 5;
    private int score = 0;
    private String username;

    // Constructor
    public QuizPage(String username) {
        this.username = username;
        setTitle("Quiz Page");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create main panel with GridBagLayout
        mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Image label
        imageLabel = new JLabel(new ImageIcon(getClass().getResource("/icons/bannerImg.jpg")));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(imageLabel, gbc);

        // Question label
        questionLabel = new JLabel();
        questionLabel.setFont(new Font("Arial", Font.PLAIN, 25));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        mainPanel.add(questionLabel, gbc);

        // Options radio buttons
        options = new JRadioButton[4];
        buttonGroup = new ButtonGroup();
        for (int i = 0; i < 4; i++) {
            options[i] = new JRadioButton();
            options[i].setFont(new Font("Arial", Font.PLAIN, 25));
            buttonGroup.add(options[i]);
            gbc.gridx = 0;
            gbc.gridy = i + 2;
            gbc.gridwidth = 1;
            gbc.anchor = GridBagConstraints.LINE_START;
            mainPanel.add(options[i], gbc);
        }

        // Previous button
        Color prevColor = new Color(255, 165, 0);
        previousButton = new JButton("Previous");
        previousButton.setFont(new Font("Arial", Font.PLAIN, 30));
        previousButton.setBackground(prevColor);
        previousButton.setPreferredSize(new Dimension(200, 50));
        previousButton.setEnabled(false);
        previousButton.setFocusPainted(false);
        previousButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentQuestionIndex > 0) {
                    currentQuestionIndex--;
                    displayQuestion();
                    nextButton.setEnabled(true);
                    submitButton.setEnabled(false);
                    if (currentQuestionIndex == 0) {
                        previousButton.setEnabled(false);
                    }
                }
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.insets = new Insets(10, 10, 10, 10);
        mainPanel.add(previousButton, gbc);

        // Next button
        Color buttonColor = new Color(70, 130, 180);
        nextButton = new JButton("Next");
        nextButton.setBackground(buttonColor);
        nextButton.setFont(new Font("Arial", Font.PLAIN, 30));
        nextButton.setPreferredSize(new Dimension(200, 50));
        nextButton.setFocusPainted(false);
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentQuestionIndex < totalQuestions - 1) {
                    checkAnswer();
                    currentQuestionIndex++;
                    displayQuestion();
                    if (currentQuestionIndex == totalQuestions - 1) {
                        nextButton.setEnabled(false);
                        submitButton.setEnabled(true);
                    }
                    previousButton.setEnabled(true);
                }
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.insets = new Insets(10, 10, 10, 10);
        mainPanel.add(nextButton, gbc);

        // Submit button
        Color greenColor = new Color(0, 128, 0);
        submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Arial", Font.PLAIN, 30));
        submitButton.setPreferredSize(new Dimension(200, 50));
        submitButton.setEnabled(false);
        submitButton.setBackground(greenColor);
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkAnswer();
                dispose();
                submitFinalResultToTable();
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.insets = new Insets(10, 10, 10, 10);
        mainPanel.add(submitButton, gbc);

        // Add main panel to the frame
        add(mainPanel);
        retrieveRandomQuestions();
        displayQuestion();
    }

    // Retrieve random questions from the database
    private void retrieveRandomQuestions() {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/java_mini", "root", "");
             Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT qid FROM mcq ORDER BY RAND() LIMIT " + totalQuestions);

            questionIndexes = new ArrayList<>();
            while (rs.next()) {
                questionIndexes.add(rs.getInt("qid"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving questions. Please check the database connection.", "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
        }
    }

    // Display question and options
    // Display question and options
private void displayQuestion() {
    try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/java_mini", "root", "");
         Statement stmt = con.createStatement()) {
        ResultSet rs = stmt.executeQuery("SELECT * FROM mcq WHERE qid=" + questionIndexes.get(currentQuestionIndex));

        if (rs.next()) {
            questionLabel.setText(rs.getString("qtext"));
            for (int i = 0; i < 4; i++) {
                options[i].setText(rs.getString("op" + (char) ('A' + i)));
                options[i].setSelected(false); // Clear the selection of radio buttons
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


    // Check answer and update score
    private void checkAnswer() {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/java_mini", "root", "");
             Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT correct FROM mcq WHERE qid=" + questionIndexes.get(currentQuestionIndex));

            if (rs.next()) {
                String correctAnswer = rs.getString("correct");
                for (int i = 0; i < 4; i++) {
                    if (options[i].isSelected() && options[i].getText().equals(correctAnswer)) {
                        score++;
                        break;
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Submit final result to the database and open result page
    private void submitFinalResultToTable() {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/java_mini", "root", "")) {
            String insertQuery = "INSERT INTO result (user, score) VALUES (?, ?)";
            PreparedStatement pstmt = con.prepareStatement(insertQuery);
            pstmt.setString(1, username);
            pstmt.setInt(2, score);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        new ResultPage(username, score, totalQuestions); // Open the result page
    }

    // Main method to run the program
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Create and display the quiz page
                new QuizPage("username").setVisible(true);
            }
        });
    }
}
