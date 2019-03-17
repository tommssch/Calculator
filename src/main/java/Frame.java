import javax.swing.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Frame extends JDialog {
    private JPanel contentPane;
    private JTextField textIn;
    private JButton calculateButton;
    private JTextField textOut;
    private JButton clearButton;
    private JButton upButton;

    public final static String EXP1 = "\\(((((-?)|(\\+?))(0|([1-9][0-9]*))(\\.\\d+)?(([+\\-*/])-?(0|([1-9][0-9]*))(\\.\\d+)?)+)|" +
            "(((-?)|(\\+?))(0|([1-9][0-9]*))(\\.\\d+)?))\\)";
    public final static String EXP2 = "((-?)|(\\+?))(0|([1-9][0-9]*))(\\.\\d+)?(([+\\-*/])-?(0|([1-9][0-9]*))(\\.\\d+)?)+|" +
            "((-?)|(\\+?))(0|([1-9][0-9]*))(\\.\\d+)?";


    public Frame() {
        setContentPane(contentPane);
        setModal(true);
        addButtonListeners();
    }

    private void addButtonListeners() {

        calculateButton.addActionListener(e -> parse(textIn.getText()));
        clearButton.addActionListener(e -> {
            textIn.setText("");
            textOut.setText("");
        });
        upButton.addActionListener(e-> textIn.setText(textOut.getText()));
    }

    private void parse(String str) {
         textOut.setText(calculate(str));
    }

    private String calculate(String exp) {
        Pattern pattern = Pattern.compile(EXP1);
        Matcher matcher = pattern.matcher(exp);

        while (matcher.find()) {
                exp = exp.replace(matcher.group(), calculateExpression(matcher.group()));
                exp = replaceDuplicates(exp);
                matcher = pattern.matcher(exp);
        }

        pattern = Pattern.compile(EXP2);
        matcher = pattern.matcher(exp);

        if(!matcher.matches()) {
            JOptionPane.showMessageDialog(null,"Check your expression and correct it");
            return "";
        }

        exp = calculateExpression(exp);
        exp = exp.replace("+","");

        return exp;
    }

    private String calculateExpression(String exp){

        exp = exp.replaceAll("[()]","");

        exp = allDelete(exp);
        exp = allMulty(exp);
        exp = allPlus(exp);
        exp = allMinus(exp);

        return exp;
    }
    private String allDelete(String exp){
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?/-?\\d+(\\.\\d+)?");
        Matcher matcher = pattern.matcher(exp);
        double arg1;
        double arg2;
        double result;

        while (matcher.find())
        {
            String [] operands = matcher.group().split("/");
            arg1 = Double.parseDouble(operands[0]);
            arg2 = Double.parseDouble(operands[1]);
            result = arg1 / arg2;
            exp = exp.replaceFirst(operands[0] + "/" +operands[1],
                    (result >= 0.0 ? "+" : "")  + String.valueOf(result));
            matcher = pattern.matcher(exp);
        }
        exp = replaceDuplicates(exp);
        return exp;
    }
    private String allMinus(String exp){
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?--?\\d+(\\.\\d+)?");
        Matcher matcher = pattern.matcher(exp);
        double arg1;
        double arg2;
        double result;

        while (matcher.find()) {
            if(matcher.group().charAt(0) == '-'){

                String [] operands = matcher.group().substring(1).split("-");
                arg1 = Double.parseDouble(operands[0]);
                arg2 = Double.parseDouble(operands[1]);
                exp = exp.replaceFirst(operands[0] + "-" + operands[1],
                           String.valueOf(arg1 + arg2));
            }
            else {

                String[] operands = matcher.group().split("-");
                arg1 = Double.parseDouble(operands[0]);
                arg2 = Double.parseDouble(operands[1]);

                result = arg1 - arg2;

                exp = exp.replaceFirst(operands[0] + "-" +operands[1],
                        (result >= 0.0 ? "+" : "")  + String.valueOf(result));
            }
            matcher = pattern.matcher(exp);
        }
        exp = replaceDuplicates(exp);
        return exp;
    }
    private String allPlus(String exp){
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?\\+-?\\d+(\\.\\d+)?");
        Matcher matcher = pattern.matcher(exp);
        double arg1;
        double arg2;
        double result;

        while (matcher.find())
        {
            String [] operands = matcher.group().split("\\+");
            arg1 = Double.parseDouble(operands[0]);
            arg2 = Double.parseDouble(operands[1]);
            result = arg1 + arg2;
            exp = exp.replaceFirst(operands[0] + "\\+" +operands[1],
                    (result >= 0.0 ? "+" : "")  +  String.valueOf(result));
            matcher = pattern.matcher(exp);
        }
        exp = replaceDuplicates(exp);
        return exp;
    }
    private String allMulty(String exp){
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?\\*-?\\d+(\\.\\d+)?");
        Matcher matcher = pattern.matcher(exp);
        double arg1;
        double arg2;
        double result;

        while (matcher.find())
        {
            String [] operands = matcher.group().split("\\*");
            arg1 = Double.parseDouble(operands[0]);
            arg2 = Double.parseDouble(operands[1]);
            result = arg1 * arg2;
            exp = exp.replaceFirst(operands[0] + "\\*" + operands[1],
                    (result >= 0.0 ? "+" : "")  +   String.valueOf(result));
            matcher = pattern.matcher(exp);
        }
        exp = replaceDuplicates(exp);
        return exp;
    }
    private String replaceDuplicates(String exp){
        exp = exp.replaceAll("--+","+");
        exp = exp.replaceAll("\\+-+","-");
        exp = exp.replaceAll("-\\++","-");
        exp = exp.replaceAll("\\+\\++","+");
        exp = exp.replaceAll("\\*\\++","*");
        exp = exp.replaceAll("/\\++","/");

        return  exp;
    }
}
