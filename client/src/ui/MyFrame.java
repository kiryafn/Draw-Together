package ui;

import javax.swing.*;
import java.awt.*;

public class MyFrame extends JFrame {
    OptionPanel optionPanel = new OptionPanel();
    public MyFrame(){
        setTitle("Draw Together");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1920, 1080);
        setLocationRelativeTo(null);
        setVisible(true);
        setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setVisible(true);
        panel.setBackground(Color.WHITE);
        panel.add(optionPanel, BorderLayout.WEST);
        add(panel);

    }
}
