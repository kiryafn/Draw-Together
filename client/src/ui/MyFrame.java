package ui;

import domain.Client;

import javax.swing.*;
import java.awt.*;

public class MyFrame extends JFrame {
    OptionPanel optionPanel;
    DrawingPanel drawingPanel;
    
    public MyFrame(Client client){
        optionPanel = new OptionPanel(client);
        drawingPanel = new DrawingPanel(optionPanel);
        setTitle("Draw Together");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1920, 1080);
        setLocationRelativeTo(null);
        setVisible(true);
        drawingPanel.setVisible(true);
        setLayout(new BorderLayout());
        add(optionPanel, BorderLayout.WEST);
        add(drawingPanel, BorderLayout.CENTER);
    }

    public OptionPanel getOptionPanel() {
        return optionPanel;
    }
}
