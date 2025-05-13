package components.panels;
import java.awt.*;
import javax.swing.*;

public class EventsPanel extends JPanel {
    public EventsPanel(Runnable onBackToHome, Runnable onCrearEvento) {
        setLayout(null);
        setBounds(0, 0, 1920, 1080);
        setBackground(Color.decode("#FFFFFF"));

        // ---------------------- Barra de título mejorada ----------------------
        JLabel lblTitulo = new JLabel("EVENTOS");
        lblTitulo.setBounds(0, 0, 1420, 80); // Ancho completo
        lblTitulo.setOpaque(true);
        lblTitulo.setBackground(Color.decode("#FFA630"));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        add(lblTitulo);

        // ---------------------- Botón crear evento ----------------------
        JButton btnCrearEvento = new JButton("Crear nuevo evento");
        btnCrearEvento.setBounds(1050, 90, 200, 30); // Posición alineada con otros paneles
        btnCrearEvento.setBackground(Color.decode("#4CAF50"));
        btnCrearEvento.setForeground(Color.WHITE);
        btnCrearEvento.setFocusPainted(false);
        btnCrearEvento.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btnCrearEvento.addActionListener(e -> onCrearEvento.run());
        add(btnCrearEvento);

        // ---------------------- Contenido con scroll ----------------------
        JTextArea txtEventosContenido = new JTextArea(
            "- Carrera 5K en el Parque\n" +
            "- Torneo de fútbol el domingo\n" +
            "- Clases de yoga este sábado\n" +
            "- Taller de nutrición deportiva\n" +
            "- Maratón de básquetbol universitario\n" +
            "- Campeonato de natación regional\n" +
            "- Exhibición de artes marciales\n" +
            "- Clínica de tenis para niños"
        );
        
        txtEventosContenido.setFont(new Font("SansSerif", Font.PLAIN, 18));
        txtEventosContenido.setLineWrap(true);
        txtEventosContenido.setWrapStyleWord(true);
        txtEventosContenido.setEditable(false);
        txtEventosContenido.setBackground(Color.decode("#F5F5F5"));
        txtEventosContenido.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(txtEventosContenido);
        scrollPane.setBounds(50, 120, 1320, 800); // Mayor altura para aprovechar espacio
        scrollPane.setBorder(null);
        add(scrollPane);
    }
}