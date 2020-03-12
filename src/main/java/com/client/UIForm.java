package com.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class UIForm extends JFrame {


    private JPanel panel1;
    private JRadioButton radioButton1;
    private JRadioButton radioButton2;
    private JRadioButton radioButton3;
    private JRadioButton radioButton4;
    private JRadioButton radioButton5;
    private JButton button1;
    private JButton button2;
    private JTextField textField1 ;

    Client client;

    void setClient(Client client) {
        this.client = client;
    }

    UIForm getThis(){
        return this;
    }

    UIForm() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        add(panel1);
        setTitle("LR3-Client");
        setSize(500, 500);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String ip = textField1.getText();

                String patternString ="([0-9]){1,3}\\.([0-9]){1,3}\\.([0-9]){1,3}\\.([0-9]){1,3}";
                Pattern pattern = Pattern.compile(patternString);
                Matcher matcher = pattern.matcher(ip);
                if (matcher.matches())
                    client = new Client(getThis(),textField1.getText());
                else {
                    JOptionPane.showMessageDialog(getThis(),"Wrong format of IP address");
                }

            }
        });
    }

    void InitVals(List<String> ops, Integer magic) {

    }

    void EnableButtons() {

    }






}