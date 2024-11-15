package ui;

import javax.swing.*;
import java.awt.*;

public class EraserButton extends JButton {
    public EraserButton() {
        setIcon(new ImageIcon("client/icons/eraser.png"));
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
        setPreferredSize(new Dimension(50, 50));  // Размер кнопки
        setBackground(Color.WHITE);  // Устанавливаем цвет для "ластика"
    }
}
