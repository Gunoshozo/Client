package com.client;

import groovy.json.JsonOutput;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class UIForm extends JFrame {


    private JPanel panel1;
    private JRadioButton radioButton1;
    private JRadioButton radioButton2;
    private JRadioButton radioButton3;
    private JRadioButton radioButton4;
    private JRadioButton radioButton5;
    private JButton makeAMoveButton;
    private JButton button2;
    private JTextField textField1 ;
    private JLabel MagicLabel;
    private JLabel TurnLabel;

    Client client;

    void setClient(Client client) {
        this.client = client;
    }

    UIForm getInstance(){
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
                if (matcher.matches()) {
                    client = new Client(textField1.getText());
                    client.setUiForm(getInstance())
                    client.start();
                    button2.setVisible(false);
                    textField1.setVisible(false);
                }
                else {
                    JOptionPane.showMessageDialog(getThis(),"Wrong format of IP address");
                }

            }
        });
        makeAMoveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                client.getServerSocket().withStreams {input,output->
                    def data = [type:"Move",
                                oper:getOpNumber()]
                    def json = JsonOutput.toJson(data)
                    output.newObjectOutputStream().writeObject(json)
                    EnableButtons(false)
                }
            }
        });
    }

    Integer getOpNumber(){
        def radioGroup = [radioButton1,radioButton2,radioButton3,radioButton4,radioButton5]
        for (int i = 0; i < radioGroup.size(); i++) {
            if(radioGroup[i])
                return i;
        }
    }

    void InitVals(List<String> ops, Integer magic) {
        radioButton1.setText(ops[0]);
        radioButton2.setText(ops[1]);
        radioButton3.setText(ops[2]);
        radioButton4.setText(ops[3]);
        radioButton5.setText(ops[4]);
        MagicLabel.setText("Magic Number:"+ magic.toString())
    }

    void UpdateNumber(Integer Magic){
        MagicLabel.setText("Magic Number:"+ Magic.toString());
    }

    void UpdateTurn(boolean flag){
        TurnLabel.setText(flag?"Your turn":"Opponent's turn");
    }

    void EnableButtons( boolean val) {
        radioButton1.setEnabled(true)
        radioButton2.setEnabled(true)
        radioButton3.setEnabled(true)
        radioButton4.setEnabled(true)
        radioButton5.setEnabled(true)
        makeAMoveButton.setEnabled(true)
    }

}