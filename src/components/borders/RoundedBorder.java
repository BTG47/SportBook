package components.borders;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.border.AbstractBorder;

public class RoundedBorder extends AbstractBorder {
    private final int radius;
    private final Color color;
    private final BasicStroke stroke;

    public RoundedBorder(int radius) {
        this(radius, Color.GRAY, 1.0f);
    }

    public RoundedBorder(int radius, Color color) {
        this(radius, color, 1.0f);
    }

    public RoundedBorder(int radius, Color color, float strokeWidth) {
        this.radius = radius;
        this.color = color;
        this.stroke = new BasicStroke(strokeWidth);
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(this.color);
        g2d.setStroke(this.stroke);
        // Ajustar coordenadas para evitar recorte
        g2d.drawRoundRect(x + 1, y + 1, width - 2, height - 2, this.radius, this.radius);
        g2d.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(this.radius, this.radius, this.radius, this.radius);
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.left = insets.right = this.radius + 1;
        insets.top = insets.bottom = this.radius + 1;
        return insets;
    }
}