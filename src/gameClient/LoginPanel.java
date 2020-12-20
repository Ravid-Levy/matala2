package gameClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPanel {
    JFrame frame1;
    private JPanel JPanel1;
    private JButton StartButton;
    private JTextField textFieldID;
    private JTextField textFieldScenario;
    private JLabel id;
    private JLabel scenario;


    public static void main(String[] args) {
        LoginPanel login = new LoginPanel();
        login.MakeLogin();
    }
    private void MakeLogin(){
        this.frame1 = new JFrame();
        this.JPanel1 = new JPanel();
        this.StartButton = new JButton("Start game!");
        this.textFieldID = new JTextField();
        this.textFieldScenario = new JTextField();
        this.id = new JLabel("Please enter ur id down below");
        this.scenario = new JLabel("please enter the scenario number down below");
        JPanel1.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
        JPanel1.setLayout(new GridLayout(0,1));
        JPanel1.add(id);
        JPanel1.add(textFieldID);
        JPanel1.add(scenario);
        JPanel1.add(textFieldScenario);
        JPanel1.add(StartButton);
        JPanel1.add(StartButton);
        frame1.add(JPanel1,BorderLayout.CENTER);
        frame1.setSize(1000 ,700);
        frame1.pack();
        frame1.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        textFieldID.setVisible(true);
        textFieldScenario.setVisible(true);
        id.setVisible(true);
        scenario.setVisible(true);
        StartButton.setVisible(true);
        JPanel1.setVisible(true);
        frame1.setVisible(true);
        StartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                long id=Long.parseLong(textFieldID.getText());
                int level=Integer.parseInt(textFieldScenario.getText());
                frame1.dispose();
                Ex2_Client.test(id,level);
            }
        });

    }
    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
