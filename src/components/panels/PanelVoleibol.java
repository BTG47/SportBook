package components.panels;
import java.awt.*;
import javax.swing.*;

public class PanelVoleibol extends JPanel {
    public PanelVoleibol(Runnable onBack) {
        setLayout(null);
        setPreferredSize(new Dimension(1420, 1080));
        setBackground(Color.decode("#F5F5F5"));

        // Título
        JLabel titulo = new JLabel("VOLEIBOL");
        titulo.setBounds(0, 0, 1420, 80);
        titulo.setOpaque(true);
        titulo.setBackground(Color.decode("#E91E63")); // Rosa fuerte
        titulo.setForeground(Color.WHITE);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        add(titulo);

        // Botón "Volver"
        JButton btnVolver = new JButton("Volver");
        btnVolver.setBounds(50, 100, 100, 40);
        btnVolver.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btnVolver.setBackground(Color.decode("#E91E63"));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.addActionListener(e -> onBack.run());
        add(btnVolver);

        // Área de contenido
        JTextArea contenido = new JTextArea(
            "Bienvenido a la sección de voleibol.\n" +
            "Aquí podrás ver noticias, partidos y publicaciones relacionadas."
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