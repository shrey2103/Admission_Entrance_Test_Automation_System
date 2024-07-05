//Name:Shreyas
//RegNo:230970045
//Class:MCA 'A'
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginFrame extends JFrame implements ActionListener {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginFrame() {
        setTitle("Login Page");
        setSize(600, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Define colors
        Color backgroundColor = new Color(240, 240, 240); // Light gray
        Color labelColor = new Color(30, 30, 30); // Dark gray
        Color buttonColor = new Color(70, 130, 180); // Steel blue

        // Load the image
        ImageIcon imageIcon = new ImageIcon(getClass().getResource("/icons/png.png"));
        Image image = imageIcon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        ImageIcon scaledImageIcon = new ImageIcon(image);

        // Create a panel for the image and form
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(backgroundColor);

        // Create a panel for the form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(backgroundColor);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);

        // Create and configure components
        JLabel titleLabel = new JLabel("Login Page");
        titleLabel.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 24)); // Bold and italic
        titleLabel.setForeground(Color.RED); // Red color for the title

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        usernameLabel.setForeground(labelColor);
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        passwordLabel.setForeground(labelColor);

        usernameField = new JTextField(15);
        usernameField.setBackground(Color.WHITE);
        passwordField = new JPasswordField(15);
        passwordField.setBackground(Color.WHITE);

        loginButton = new JButton("Login");
        loginButton.addActionListener(this);
        loginButton.setBackground(buttonColor);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setOpaque(true);

        // Add components to the form panel with layout constraints
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        formPanel.add(titleLabel, constraints);

        constraints.gridy = 1;
        constraints.gridwidth = 1;
        formPanel.add(usernameLabel, constraints);

        constraints.gridy = 2;
        formPanel.add(passwordLabel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        formPanel.add(usernameField, constraints);

        constraints.gridy = 2;
        formPanel.add(passwordField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        formPanel.add(loginButton, constraints);

        // Add the image panel to the left and the form panel to the center of the main panel
        panel.add(new JLabel(scaledImageIcon), BorderLayout.WEST);
        panel.add(formPanel, BorderLayout.CENTER);

        // Add the main panel to the frame's content pane
        getContentPane().add(panel);
        setVisible(true); // Make the frame visible
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String username = usernameField.getText();
            char[] passwordChars = passwordField.getPassword();
            String password = new String(passwordChars);

            try {
                // Establish database connection
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/java_mini", "root", "");
                String query = "SELECT * FROM logintable WHERE user = ? AND pass = ?";
                PreparedStatement pstmt = con.prepareStatement(query);
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    dispose(); // Close the login frame
                    new QuizPage(username).setVisible(true); // Open the QuizPage
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid username or password", "Login Failed",
                            JOptionPane.ERROR_MESSAGE);
                }

                // Close resources
                rs.close();
                pstmt.close();
                con.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new LoginFrame(); // Create an instance of LoginFrame
    }
}
