import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ProgrammableCalculator extends Application {

    private TextArea displayTextArea;

    @Override
    public void start(Stage primaryStage) {
        displayTextArea = new TextArea();
        displayTextArea.setEditable(false);
        displayTextArea.setPrefRowCount(3);

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setVgap(5);
        gridPane.setHgap(5);

        Button button1 = new Button("1");
        Button button2 = new Button("2");
        Button button3 = new Button("3");
        Button button4 = new Button("4");
        Button button5 = new Button("5");
        Button button6 = new Button("6");
        Button button7 = new Button("7");
        Button button8 = new Button("8");
        Button button9 = new Button("9");
        Button button0 = new Button("0");
        Button buttonPlus = new Button("+");
        Button buttonMinus = new Button("-");
        Button buttonMultiply = new Button("*");
        Button buttonDivide = new Button("/");
        Button buttonEquals = new Button("=");

        // Set button actions
        button1.setOnAction(e -> addToDisplay("1"));
        button2.setOnAction(e -> addToDisplay("2"));
        button3.setOnAction(e -> addToDisplay("3"));
        button4.setOnAction(e -> addToDisplay("4"));
        button5.setOnAction(e -> addToDisplay("5"));
        button6.setOnAction(e -> addToDisplay("6"));
        button7.setOnAction(e -> addToDisplay("7"));
        button8.setOnAction(e -> addToDisplay("8"));
        button9.setOnAction(e -> addToDisplay("9"));
        button0.setOnAction(e -> addToDisplay("0"));
        buttonPlus.setOnAction(e -> addToDisplay("+"));
        buttonMinus.setOnAction(e -> addToDisplay("-"));
        buttonMultiply.setOnAction(e -> addToDisplay("*"));
        buttonDivide.setOnAction(e -> addToDisplay("/"));
        buttonEquals.setOnAction(e -> evaluateExpression());

        gridPane.add(displayTextArea, 0, 0, 4, 1);
        gridPane.add(button1, 0, 1);
        gridPane.add(button2, 1, 1);
        gridPane.add(button3, 2, 1);
        gridPane.add(buttonPlus, 3, 1);
        gridPane.add(button4, 0, 2);
        gridPane.add(button5, 1, 2);
        gridPane.add(button6, 2, 2);
        gridPane.add(buttonMinus, 3, 2);
        gridPane.add(button7, 0, 3);
        gridPane.add(button8, 1, 3);
        gridPane.add(button9, 2, 3);
        gridPane.add(buttonMultiply, 3, 3);
        gridPane.add(button0, 0, 4);
        gridPane.add(buttonEquals, 1, 4, 2, 1);
        gridPane.add(buttonDivide, 3, 4);

        Scene scene = new Scene(gridPane, 300, 200);
        primaryStage.setTitle("Programmable Calculator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void addToDisplay(String value) {
        displayTextArea.appendText(value);
    }

    private void evaluateExpression() {
        String expression = displayTextArea.getText();
        try {
            double result = eval(expression);
            displayTextArea.clear();
            displayTextArea.setText(Double.toString(result));
        } catch (Exception e) {
            displayTextArea.clear();
            displayTextArea.setText("Error: Invalid expression");
        }
    }

    private double eval(String expression) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < expression.length()) ? expression.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ')
                    nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < expression.length())
                    throw new RuntimeException("Unexpected: " + (char) ch);
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                while (true) {
                    if (eat('+'))
                        x += parseTerm();
                    else if (eat('-'))
                        x -= parseTerm();
                    else
                        return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                while (true) {
                    if (eat('*'))
                        x *= parseFactor();
                    else if (eat('/'))
                        x /= parseFactor();
                    else
                        return x;
                }
            }

            double parseFactor() {
                if (eat('+'))
                    return parseFactor();
                if (eat('-'))
                    return -parseFactor();

                double x;
                int startPos = this.pos;
                if (eat('(')) {
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') {
                    while ((ch >= '0' && ch <= '9') || ch == '.')
                        nextChar();
                    x = Double.parseDouble(expression.substring(startPos, this.pos));
                } else {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }

                return x;
            }
        }.parse();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
