package components.panels;
import java.awt.*;
import javax.swing.*;
import java.sql.*;
import database.dbConnection;

public class PanelFutbol extends JPanel {
    public PanelFutbol(Runnable onBack) {
        setLayout(null);
        setPreferredSize(new Dimension(1420, 1080));
        setBackground(Color.decode("#F5F5F5"));

        // ---------------------- Título ----------------------
        JLabel titulo = new JLabel("FÚTBOL");
        titulo.setBounds(0, 0, 1420, 80);
        titulo.setOpaque(true);
        titulo.setBackground(Color.decode("#4CAF50"));
        titulo.setForeground(Color.WHITE);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        add(titulo);

        // ---------------------- Botón "Volver" ----------------------
        JButton btnVolver = new JButton("Volver");
        btnVolver.setBounds(50, 100, 100, 40);
        btnVolver.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btnVolver.setBackground(Color.decode("#4CAF50"));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.addActionListener(e -> onBack.run());
        add(btnVolver);

        // ---------------------- Área de contenido dinámico ----------------------
        JTextArea contenido = new JTextArea();
        contenido.setBounds(50, 180, 1320, 800); // Mayor espacio y scroll
        contenido.setFont(new Font("SansSerif", Font.PLAIN, 16));
        contenido.setLineWrap(true);
        contenido.setWrapStyleWord(true);
        contenido.setEditable(false);
        contenido.setBackground(Color.WHITE);
        contenido.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(contenido);
        scrollPane.setBounds(50, 180, 1320, 800);
        scrollPane.setBorder(null);
        add(scrollPane);

        // ---------------------- Cargar datos desde BD ----------------------
        try (Connection conn = dbConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT Titulo, Contenido FROM Curso_Evento WHERE Deporte_ID = ?"
             )) {
            stmt.setInt(1, 1); // ID del fútbol
            ResultSet rs = stmt.executeQuery();
            StringBuilder contenidoBuilder = new StringBuilder();
            while (rs.next()) {
                contenidoBuilder.append("📌 ")
                               .append(rs.getString("Titulo"))
                               .append("\n")
                               .append(rs.getString("Contenido"))
                               .append("\n\n");
            }
            contenido.setText(contenidoBuilder.toString());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                this,
                "Error al cargar contenido de fútbol",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            contenido.setText("Bienvenido a la sección de fútbol.\nAquí podrás ver noticias, partidos y publicaciones relacionadas.");
        }
    }
}