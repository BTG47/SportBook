package components.panels;
import java.awt.*;
import javax.swing.*;

public class PanelBox extends JPanel {
    public PanelBox(Runnable onBack) {
        setLayout(null);
        setPreferredSize(new Dimension(1420, 1080));
        setBackground(Color.decode("#F5F5F5"));

        // Título (ancho completo para consistencia)
        JLabel titulo = new JLabel("BOX");
        titulo.setBounds(0, 0, 1420, 80); // Usa 1420px como en PanelBox.java
        titulo.setOpaque(true);
        titulo.setBackground(Color.decode("#D32F2F"));
        titulo.setForeground(Color.WHITE);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        add(titulo);

        // Botón "Volver"
        JButton btnVolver = new JButton("Volver");
        btnVolver.setBounds(50, 100, 100, 40);
        btnVolver.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btnVolver.setBackground(Color.decode("#D32F2F"));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.addActionListener(e -> onBack.run());
        add(btnVolver);

        // Contenido informativo
        JTextArea contenido = new JTextArea(
            "Bienvenido a la sección de box.\n" +
            "Aquí podrás ver noticias, entrenamientos y publicaciones relacionadas."
        );
        contenido.setBounds(50, 180, 1000, 150);
        contenido.setFont(new Font("SansSerif", Font.PLAIN, 16));
        contenido.setLineWrap(true);
        contenido.setWrapStyleWord(true);
        contenido.setEditable(false);
        contenido.setBackground(Color.WHITE);
        add(contenido);
    }
}