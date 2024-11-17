package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class DrawingPanel extends JPanel {
    private final OptionPanel optionPanel;
    private BufferedImage canvas;

    public DrawingPanel(OptionPanel optionPanel) {
        this.optionPanel = optionPanel;
        setBackground(Color.WHITE);
        setFocusable(true);

        // Инициализация холста с размерами панели
        canvas = new BufferedImage(1700, 1080, BufferedImage.TYPE_INT_ARGB); // Размеры можно изменить при необходимости

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                drawOnCanvas(e.getX(), e.getY()); // Рисуем на холсте
                repaint(); // Обновляем отображение
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                drawOnCanvas(e.getX(), e.getY()); // Рисуем на холсте
                repaint(); // Обновляем отображение
            }
        });
    }

    private void drawOnCanvas(int x, int y) {
        Graphics2D g2d = canvas.createGraphics();
        g2d.setColor(optionPanel.getCurrentColor());
        g2d.fillOval(x-optionPanel.getBrushSize()/2, y-optionPanel.getBrushSize()/2, optionPanel.getBrushSize(), optionPanel.getBrushSize());
        g2d.dispose(); // Освобождаем ресурсы графики
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Очищаем панель

        // Отображаем содержимое холста
        g.drawImage(canvas, 0, 0, null);
    }
}
