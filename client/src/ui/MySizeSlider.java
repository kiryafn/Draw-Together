package ui;

import javax.swing.*;

public class MySizeSlider extends JSlider {
    public MySizeSlider(int orientation, int min, int max, int value) {
        super(orientation, min, max, value);
        setMajorTickSpacing(10);
        setMinorTickSpacing(1);
        setPaintTicks(true);
        setPaintLabels(true);
    }
}
