package components.panels;
import java.awt.*;
import javax.swing.*;

public class HomePanel extends JPanel {
    public HomePanel(Runnable onGoToEvents) {
        setLayout(null);
        setBackground(Color.decode("#F5F5F5"));
        setPreferredSize(new Dimension(1420, 1080)); // Tamaño estándar

        // ---------------------- Encabezado ----------------------
        JPanel header = new JPanel();
        header.setBounds(0, 0, 1420, 80); // Ancho completo
        header.setBackground(Color.decode("#2A5CAA"));
        header.setLayout(null);

        JLabel title = new JLabel("INICIO", SwingConstants.CENTER); // Título centrado
        title.setBounds(0, 20, 1420, 40); // Centrado horizontalmente
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        header.add(title);

        // ---------------------- Contenido principal ----------------------
        JPanel content = new JPanel();
        content.setLayout(null);
        content.setBackground(Color.WHITE);
        content.setBounds(0, 80, 1420, 1000); // Ancho completo

        JLabel subtitle = new JLabel("Entérate de las nuevas publicaciones en eventos y cursos");
        subtitle.setBounds(50, 20, 800, 30); // Posición mejorada
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 18));
        content.add(subtitle);

        // Botón de publicación (diseño mejorado)
        JButton publicarBtn = new JButton("Publicar nuevo evento / curso");
        publicarBtn.setBounds(1000, 20, 300, 40); // Tamaño y posición optimizados
        publicarBtn.setBackground(Color.decode("#4CAF50"));
        publicarBtn.setForeground(Color.WHITE);
        publicarBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        publicarBtn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        publicarBtn.setFocusPainted(false);
        content.add(publicarBtn);

        // Ejemplo de publicación (con scroll y formato mejorado)
        JTextArea post = new JTextArea("""
            📢 Evento: Expo Sport 2025
            🗓️ Fecha: 18 de mayo
            🕐 Hora: 10:00 AM
            📍 Lugar: Estadio principal
            
            ¡No te lo puedes perder! Participa en talleres gratuitos,
            competiciones y charlas con expertos.
            """);
        post.setBounds(50, 70, 1320, 150); // Ancho completo
        post.setFont(new Font("SansSerif", Font.PLAIN, 16)); // Fuente más legible
        post.setEditable(false);
        post.setBackground(Color.decode("#F5F5F5"));
        post.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPost = new JScrollPane(post);
        scrollPost.setBounds(50, 70, 1320, 150);
        scrollPost.setBorder(null);
        content.add(scrollPost);

        add(header);
        add(content);
    }
}