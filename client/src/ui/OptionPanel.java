package ui;

import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import java.awt.*;

public class OptionPanel extends JPanel {
    private Color currentColor = Color.BLACK;
    private int brushSize = 10;

    private final JColorChooser colorChooser;
    private final JSlider sizeSlider;
    private final JButton eraserButton;

    public OptionPanel() {
        //setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(600, 300));

        //Color Chooser Panel
        colorChooser = new JColorChooser(currentColor);
        leaveHSLonly(colorChooser);
        colorChooser.setPreviewPanel(new JPanel());
        colorChooser.getSelectionModel().addChangeListener(e -> currentColor = colorChooser.getColor());


        //Eraser Button
        eraserButton = new JButton();
        eraserButton.setIcon(new ImageIcon("client/icons/eraser.png"));
        eraserButton.setHorizontalAlignment(SwingConstants.CENTER);
        eraserButton.setVerticalAlignment(SwingConstants.CENTER);
        eraserButton.setPreferredSize(new Dimension(50, 50));  // Размер кнопки
        eraserButton.setBackground(Color.WHITE);  // Устанавливаем цвет для "ластика"
        eraserButton.addActionListener(e -> currentColor = Color.WHITE);  // Устанавливаем белый цвет для "ластика"

        //Brush Size Slider
        sizeSlider = new JSlider(JSlider.HORIZONTAL, 0, 50, brushSize);
        sizeSlider.setMajorTickSpacing(10);
        sizeSlider.setMinorTickSpacing(1);
        sizeSlider.setPaintTicks(true);
        sizeSlider.setPaintLabels(true);
        sizeSlider.addChangeListener(e -> brushSize = sizeSlider.getValue());

        JPanel colorPanel = new JPanel();
        colorPanel.add(colorChooser);
        add(colorPanel);

        JPanel otherPanel = new JPanel();
        otherPanel.add(eraserButton);
        otherPanel.add(sizeSlider);

        add(otherPanel);


    }

    public Color getCurrentColor() {
        return currentColor;
    }

    public int getBrushSize() {
        return brushSize;
    }

    private void leaveHSLonly(JColorChooser colorChooser) {
        AbstractColorChooserPanel[] panels = colorChooser.getChooserPanels();


        for (AbstractColorChooserPanel panel : panels) {
            String displayName = panel.getDisplayName();
            if (!displayName.equals("HSL") && !displayName.equals("Swatches")) {
                colorChooser.removeChooserPanel(panel);
            }
        }

    }
}