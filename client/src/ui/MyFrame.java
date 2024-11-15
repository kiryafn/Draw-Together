package ui;

import javax.swing.*;
import java.awt.*;

public class MyFrame extends JFrame {
    OptionPanel optionPanel = new OptionPanel();
    DrawingPanel drawingPanel = new DrawingPanel(optionPanel);
    
    public MyFrame(){
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
}
