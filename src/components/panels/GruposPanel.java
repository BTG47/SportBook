package components.panels;
import components.borders.RoundedBorder;
import java.awt.*;
import javax.swing.*;

public class GruposPanel extends JPanel {
    public GruposPanel(Runnable onBack, Runnable onCrearGrupo) {
        setBackground(Color.WHITE);
        setLayout(null); // Posicionamiento absoluto para control detallado

        // ---------------------- Encabezado ----------------------
        JPanel header = new JPanel();
        header.setBounds(0, 0, 1420, 80); // Ancho completo
        header.setBackground(Color.decode("#8E6C88")); // Color de GruposPanelN
        header.setLayout(new BorderLayout());

        JLabel title = new JLabel("GRUPOS", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 28)); // Tamaño de fuente aumentado
        header.add(title, BorderLayout.CENTER);
        add(header);

        // ---------------------- Subtítulo ----------------------
        JLabel subtitle = new JLabel("Explora o crea grupos deportivos", SwingConstants.CENTER);
        subtitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        subtitle.setBounds(0, 100, 1420, 30); // Centrado y ancho completo
        add(subtitle);

        // ---------------------- Botón "Crear nuevo grupo" ----------------------
        JButton crearGrupoButton = new JButton("Crear nuevo grupo");
        crearGrupoButton.setBounds(600, 160, 220, 45); // Posición centrada
        crearGrupoButton.setBackground(Color.decode("#4CAF50"));
        crearGrupoButton.setForeground(Color.WHITE);
        crearGrupoButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        crearGrupoButton.setBorder(new RoundedBorder(15)); // Borde redondeado
        crearGrupoButton.setFocusPainted(false);
        crearGrupoButton.addActionListener(e -> onCrearGrupo.run());
        add(crearGrupoButton);

        // ---------------------- Botón "Volver" ----------------------
        JButton backButton = new JButton("Volver");
        backButton.setBounds(600, 220, 220, 45); // Alineado con el botón anterior
        backButton.setBackground(Color.decode("#FF5722")); // Color distintivo
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        backButton.setBorder(new RoundedBorder(15));
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> onBack.run());
        add(backButton);
    }
}