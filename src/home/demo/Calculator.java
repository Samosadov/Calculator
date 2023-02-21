package home.demo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

public class Calculator {
    private static final int WINDOW_WIDTH = 350;
    private static final int WINDOW_HEIGHT = 540;
    private static final int BUTTON_WIDTH = 60;
    private static final int BUTTON_HEIGHT = 50;
    private static final int TEXTFIELD_WIDTH = 300;
    private static final int TEXTFIELD_HEIGHT = 50;
    private static final int MARGIN_X = 20;
    private static final int MARGIN_Y = 20;
    private static final int DELTA_X = BUTTON_WIDTH + MARGIN_X;
    private static final int DELTA_Y = BUTTON_HEIGHT + MARGIN_Y;
    private static final int MAIN_FONT_SIZE = 24;
    private static final int OPTIONAL_FONT_SIZE = 16;

    private final JFrame window;

    private final JTextField input;

    private JButton btnPercent, btnCE, btnC, btnBackspace,
            btn1Div, btnSqr, btnRoot, btnDiv, btnMul, btnAdd, btnSub,
            btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9,
            btnChange, btnPoint, btnResult;

    private char operator = ' ';
    private double result = 0;
    private boolean addable = true;
    private boolean flagError = false;

    public Calculator() {
        window = new JFrame("Калькулятор");
        window.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        window.setLocationRelativeTo(null);

        int[] x = {MARGIN_X, MARGIN_X + DELTA_X, MARGIN_X + DELTA_X * 2, MARGIN_X + DELTA_X * 3};
        int[] y = {MARGIN_Y, MARGIN_Y + DELTA_Y, MARGIN_Y + DELTA_Y * 2, MARGIN_Y + DELTA_Y * 3,
                MARGIN_Y + DELTA_Y * 4, MARGIN_Y + DELTA_Y * 5, MARGIN_Y + DELTA_Y * 6};

        input = new JTextField("0");
        input.setHorizontalAlignment(SwingConstants.RIGHT);
        input.setBounds(x[0], y[0], TEXTFIELD_WIDTH, TEXTFIELD_HEIGHT);
        input.setEditable(false);
        input.setBackground(Color.WHITE);
        input.setFont(new Font("Arial", Font.PLAIN, MAIN_FONT_SIZE));
        window.add(input);

        btnPercent = initButton("%", MAIN_FONT_SIZE, x[0], y[1], event -> {
            result = result / 100;
            if (Pattern.matches("[-]?[\\d]+[.][0]*", String.valueOf(result))) {
                input.setText(String.valueOf((int) result));
            }
            else { input.setText(String.valueOf(result)); }
        });

        btnCE = initButton("CE", OPTIONAL_FONT_SIZE, x[1], y[1], event -> {
            input.setText("0");
            addable = true;
        });

        btnC = initButton("C",MAIN_FONT_SIZE,  x[2], y[1], event -> reset());

        btnBackspace = initButton("<-", MAIN_FONT_SIZE, x[3], y[1], event -> {
            if (addable) {
                String str = input.getText();
                if (str.length() > 1) {
                    input.setText(str.substring(0, str.length() - 1));
                }
                else {
                    input.setText("0");
                }
            }
            else {
                addable = true;
                input.setText("0");
            }
        });

        btn1Div = initButton("1/x", OPTIONAL_FONT_SIZE, x[0], y[2], event -> {
            result = Double.parseDouble(input.getText());
            if (result == 0) {
                input.setText("На ноль делить нельзя.");
            }
            else {
                result = 1 / result;
                record();
            }
            addable = false;
        });

        btnSqr = initButton("x^2", OPTIONAL_FONT_SIZE, x[1], y[2], event -> {
            result = Double.parseDouble(input.getText());
            result = result * result;
            record();
        });

        btnRoot = initButton("√", MAIN_FONT_SIZE, x[2], y[2], event -> {
            result = Double.parseDouble(input.getText());
            if (result < 0) {
                input.setText("Ошибка.");
                flagError = true;
            }
            else {
                result = Math.sqrt(result);
                record();
            }
            addable = false;
        });

        btnDiv = initButton(":", MAIN_FONT_SIZE, x[3], y[2], event -> eventOperation('/'));

        btn7 = initButton("7", MAIN_FONT_SIZE, x[0], y[3], event -> { eventDigital("7"); });

        btn8 = initButton("8", MAIN_FONT_SIZE, x[1], y[3], event -> { eventDigital("8"); });

        btn9 = initButton("9", MAIN_FONT_SIZE, x[2], y[3], event -> { eventDigital("9"); });

        btnMul = initButton("x", MAIN_FONT_SIZE, x[3], y[3], event -> eventOperation('*'));

        btn4 = initButton("4", MAIN_FONT_SIZE, x[0], y[4], event -> eventDigital("4"));

        btn5 = initButton("5", MAIN_FONT_SIZE, x[1], y[4], event -> eventDigital("5"));

        btn6 = initButton("6", MAIN_FONT_SIZE, x[2], y[4], event -> eventDigital("6"));

        btnSub = initButton("-", MAIN_FONT_SIZE, x[3], y[4], event -> eventOperation('-'));

        btn1 = initButton("1", MAIN_FONT_SIZE, x[0], y[5], event -> eventDigital("1"));

        btn2 = initButton("2", MAIN_FONT_SIZE, x[1], y[5], event -> eventDigital("2"));

        btn3 = initButton("3", MAIN_FONT_SIZE, x[2], y[5], event -> eventDigital("3"));

        btnAdd = initButton("+", MAIN_FONT_SIZE, x[3], y[5], event -> eventOperation('+'));

        btnChange = initButton("+/-", OPTIONAL_FONT_SIZE, x[0], y[6], event -> {
            if (!flagError) {
                if (input.getText().charAt(0) == '-') {
                    input.setText(input.getText().substring(1));
                }
                else {
                    input.setText("-" + input.getText());
                }
            }
        });

        btn0 = initButton("0", MAIN_FONT_SIZE, x[1], y[6], event -> eventDigital("0"));

        btnPoint = initButton(".", MAIN_FONT_SIZE, x[2], y[6], event -> {
            if (addable) {
                if (!input.getText().contains(".")) {
                    input.setText(input.getText() + ".");
                }
            }
            else {
                if (flagError) { reset(); }
                else {
                    addable = true;
                    input.setText("0.");
                }
            }
        });

        btnResult = initButton("=", MAIN_FONT_SIZE, x[3], y[6], event -> eventOperation('='));

//        btnLog = initButton("ln", x[4], y[3], event -> {
//            repaintFont();
//            if (Pattern.matches("([-]?\\d+[.]\\d*)|(\\d+)", input.getText())) {
//                if (flag) {
//                    result = Math.log(Double.parseDouble(input.getText()));
//                    if (Pattern.matches("[-]?[\\d]+[.][0]*", String.valueOf(result))) {
//                        input.setText(String.valueOf((int) result));
//                    }
//                    else {
//                        input.setText(String.valueOf(result));
//                    }
//                    operator = 'l';
//                    addable = false;
//                }
//            }
//        });
////        btnLog.setVisible(false);

        window.setLayout(null);
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
    }

    /*    private JComboBox<String> initComboBox(String[] items, int x, int y, String toolTip, Consumer consumerEvent) {
            JComboBox<String> combo = new JComboBox<>(items);
            combo.setBounds(x, y, 140, 25);
            combo.setToolTipText(toolTip);
            combo.setCursor(new Cursor(Cursor.HAND_CURSOR));
            combo.addItemListener(consumerEvent::accept);
            window.add(combo);

            return combo;
        }*/
    private void reset() {
        input.setText("0");
        operator = ' ';
        result = 0;
        addable = true;
        flagError = false;
    }

    private void record() {
        if (Pattern.matches("[-]?[\\d]+[.][0]*", String.valueOf(result))) {
            input.setText(String.valueOf((int) result));
        }
        else {
            input.setText(String.valueOf(result));
        }
    }

    private void eventDigital(String digit) {
        if (flagError) {
            reset();
        }
        if(input.getText().equals("0") || !addable) {
            input.setText(digit);
            addable = true;
        }
        else {
            input.setText(input.getText() + digit);
        }
    }

    private void eventOperation(char op) {
        if (!flagError) {
            if (operator != ' ') {
                try {
                    result = calc(result, input.getText(), operator);
                    record();
                } catch (Exception e) {
                    input.setText("Ошибка.");
                    flagError = true;
                }
            }
            else {
                result = Double.parseDouble(input.getText());
            }
            if (op != '=') { operator = op; }
            else { operator = ' '; }
            addable = false;
        }
    }

    private JButton initButton(String label, int fontSize, int x, int y, ActionListener event) {
        JButton btn = new JButton(label);
        btn.setBounds(x, y, BUTTON_WIDTH, BUTTON_HEIGHT);
        btn.setFont(new Font("Arial", Font.PLAIN, fontSize));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));;
        btn.addActionListener(event);
        btn.setFocusable(false);
        window.add(btn);

        return btn;
    }

    private double calc(double a, String input, char operator) {
        this.input.setFont(this.input.getFont().deriveFont(Font.PLAIN));
        double b = Double.parseDouble(input);
        switch(operator) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                return a / b;
            default:
                return b;
        }
    }

    public static void main(String[] args) {
        new Calculator();
    }
}

