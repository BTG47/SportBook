package components.panels;
import java.awt.*;
import java.util.function.Consumer;
import javax.swing.*;

public class DeportesPanel extends JPanel {

    public DeportesPanel(Consumer<String> onDeporteSeleccionado) {
        setLayout(null);
        setPreferredSize(new Dimension(1420, 1080));
        setBackground(Color.decode("#F5F5F5"));

        // ---------------------- Título ----------------------
        JLabel lblTitulo = new JLabel("DEPORTES");
        lblTitulo.setBounds(0, 0, 1420, 80); // Ancho completo
        lblTitulo.setOpaque(true);
        lblTitulo.setBackground(Color.decode("#FFA630"));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 32)); // Tamaño de fuente más grande
        add(lblTitulo);

        // ---------------------- Texto informativo ----------------------
        JLabel lblTexto = new JLabel("Ingresa a un deporte para conocer sus últimas publicaciones");
        lblTexto.setBounds(0, 100, 1420, 30); // Ancho completo
        lblTexto.setHorizontalAlignment(SwingConstants.CENTER);
        lblTexto.setFont(new Font("SansSerif", Font.PLAIN, 18));
        lblTexto.setForeground(Color.decode("#333333"));
        add(lblTexto);

        // ---------------------- Botones de deportes ----------------------
        String[] deportes = {"Futbol", "Basquetbol", "Voleibol", "Tenis", "Natacion", "Box"};
        String[] colores = {"#00A878", "#FF5722", "#E91E63", "#8BC34A", "#00BCD4", "#D32F2F"};

        // Configuración de posiciones
        int x = 220;
        int y = 180;
        int ancho = 200;
        int alto = 60;
        int espacioX = 240;
        int espacioY = 100;

        for (int i = 0; i < deportes.length; i++) {
            final int index = i;
            JButton btn = new JButton(deportes[index].toUpperCase());
            btn.setBounds(x, y, ancho, alto);
            btn.setBackground(Color.decode(colores[index]));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setFont(new Font("SansSerif", Font.BOLD, 16));
            btn.setBorder(BorderFactory.createEmptyBorder());
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.addActionListener(e -> onDeporteSeleccionado.accept(deportes[index]));

            add(btn);

            // Actualizar coordenadas para la cuadrícula de 3 columnas
            x += espacioX;
            if ((i + 1) % 3 == 0) {
                x = 220;
                y += espacioY;
            }
        }
    }
}