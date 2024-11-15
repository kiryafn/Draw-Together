package ui;

import javax.swing.*;
import java.awt.*;

public class OptionPanel extends JPanel {
    private Color currentColor = Color.BLACK;
    private int brushSize = 10;

    private final MySizeSlider sizeSlider = new MySizeSlider(JSlider.HORIZONTAL, 0, 50, brushSize);
    private final MyColorChooser colorChooser = new MyColorChooser();
    private final EraserButton eraserButton = new EraserButton();

    public OptionPanel() {
        setPreferredSize(new Dimension(635, 300));

        //Add listeners
        colorChooser.getSelectionModel().addChangeListener(e -> currentColor = colorChooser.getColor());
        eraserButton.addActionListener(e -> currentColor = Color.WHITE);
        sizeSlider.addChangeListener(e -> brushSize = sizeSlider.getValue());

        JPanel colorPanel = new JPanel();
        colorPanel.add(colorChooser);

        JPanel otherPanel = new JPanel();
        otherPanel.add(eraserButton);
        otherPanel.add(sizeSlider);

        add(colorPanel);
        add(otherPanel);


    }

    public Color getCurrentColor() {
        return currentColor;
    }

    public int getBrushSize() {
        return brushSize;
    }
}