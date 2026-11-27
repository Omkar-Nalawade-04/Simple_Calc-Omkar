import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Calculator extends JFrame implements ActionListener {

    private final JTextField display;
    private double num1;
    private String operator = "";
    private boolean newNumber = true;
    private boolean errorState = false;

    public Calculator() {
        setTitle("Student Calculator");
        setSize(380, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Student details
        JPanel header = new JPanel(new GridLayout(3, 1, 2, 2));
        header.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        header.setBackground(new Color(220, 235, 255));

        JLabel name = new JLabel("Name : Samruddhi Khade");
        JLabel roll = new JLabel("Roll No : IT2263");
        JLabel div = new JLabel("Div : B");

        for (JLabel label : new JLabel[]{name, roll, div}) {
            label.setFont(new Font("Arial", Font.BOLD, 15));
            header.add(label);
        }
        add(header, BorderLayout.NORTH);

        // Display
        display = new JTextField("0");
        display.setFont(new Font("Arial", Font.BOLD, 30));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        display.setBackground(Color.WHITE);

        JPanel displayPanel = new JPanel(new BorderLayout());
        displayPanel.setPreferredSize(new Dimension(350, 65));
        displayPanel.add(display, BorderLayout.CENTER);

        // Buttons
        JPanel buttonsPanel = new JPanel(new GridLayout(5, 4, 8, 8));
        String[] buttons = {
            "C", "DEL", "%", "/",
            "7", "8", "9", "*",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "+/-", "0", ".", "="
        };

        for (String value : buttons) {
            JButton button = new JButton(value);
            button.setFont(new Font("Arial", Font.BOLD, 21));
            button.addActionListener(this);

            if ("+-*/=".contains(value) && value.length() == 1) {
                button.setBackground(new Color(65, 125, 210));
                button.setForeground(Color.WHITE);
            } else if (value.equals("C")) {
                button.setBackground(new Color(220, 70, 70));
                button.setForeground(Color.WHITE);
            } else {
                button.setBackground(new Color(230, 235, 245));
            }
            buttonsPanel.add(button);
        }

        JPanel center = new JPanel(new BorderLayout(8, 8));
        center.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        center.add(displayPanel, BorderLayout.NORTH);
        center.add(buttonsPanel, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (command.matches("[0-9]")) {
            if (newNumber || errorState || display.getText().equals("0")) {
                display.setText(command);
                newNumber = false;
                errorState = false;
            } else {
                display.setText(display.getText() + command);
            }
            return;
        }

        if (command.equals(".")) {
            if (newNumber || errorState) {
                display.setText("0.");
                newNumber = false;
                errorState = false;
            } else if (!display.getText().contains(".")) {
                display.setText(display.getText() + ".");
            }
            return;
        }

        if (command.equals("C")) {
            clear();
            return;
        }

        if (command.equals("DEL")) {
            if (!newNumber && !errorState) {
                String value = display.getText();
                if (value.length() > 1) {
                    display.setText(value.substring(0, value.length() - 1));
                } else {
                    display.setText("0");
                    newNumber = true;
                }
            }
            return;
        }

        if (errorState) {
            clear();
            return;
        }

        if (command.equals("+/-")) {
            double value = getDisplayValue();
            display.setText(format(-value));
            return;
        }

        if (command.equals("%")) {
            double value = getDisplayValue();
            display.setText(format(value / 100.0));
            newNumber = true;
            return;
        }

        if (command.equals("=")) {
            if (!operator.isEmpty() && !newNumber) {
                performCalculation();
            }
            return;
        }

        // Arithmetic operator
        if ("+-*/".contains(command) && command.length() == 1) {
            if (!operator.isEmpty() && !newNumber) {
                performCalculation();
                if (errorState) return;
            }
            num1 = getDisplayValue();
            operator = command;
            newNumber = true;
        }
    }

    private void performCalculation() {
        double num2 = getDisplayValue();
        double result;

        switch (operator) {
            case "+":
                result = num1 + num2;
                break;
            case "-":
                result = num1 - num2;
                break;
            case "*":
                result = num1 * num2;
                break;
            case "/":
                if (num2 == 0.0) {
                    display.setText("Cannot divide by zero");
                    operator = "";
                    newNumber = true;
                    errorState = true;
                    return;
                }
                result = num1 / num2;
                break;
            default:
                return;
        }

        display.setText(format(result));
        num1 = result;
        operator = "";
        newNumber = true;
    }

    private double getDisplayValue() {
        return Double.parseDouble(display.getText());
    }

    private void clear() {
        display.setText("0");
        num1 = 0;
        operator = "";
        newNumber = true;
        errorState = false;
    }

    private String format(double value) {
        if (Double.isFinite(value) && value == Math.rint(value)
                && value >= Long.MIN_VALUE && value <= Long.MAX_VALUE) {
            return Long.toString((long) value);
        }
        return Double.toString(value);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Calculator::new);
    }
}
