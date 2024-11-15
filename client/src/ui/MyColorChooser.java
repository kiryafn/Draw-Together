package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.colorchooser.AbstractColorChooserPanel;

public class MyColorChooser extends JColorChooser {

    MyColorChooser() {
        leaveHSLonly();
        setPreviewPanel(new JPanel());
        setBorder(new EmptyBorder(0, 23, 0, 0)); // Отступы
    }
    private void leaveHSLonly() {
        AbstractColorChooserPanel[] panels = getChooserPanels();

        for (AbstractColorChooserPanel panel : panels) {
            String displayName = panel.getDisplayName();
            if (!displayName.equals("HSL") && !displayName.equals("Swatches")) {
                removeChooserPanel(panel);
            }
        }

    }
}
