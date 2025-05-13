package components.panels;
import database.dbConnection;
import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class CoursesPanel extends JPanel {
    private DefaultListModel<String> model;
    private JList<String> listaCursos;

    public CoursesPanel(Runnable onVolver, Runnable onCrearCurso) {
        setLayout(null);
        setPreferredSize(new Dimension(1420, 1080));
        setBackground(Color.decode("#F5F5F5"));

        // Título (ancho completo)
        JLabel lblTitulo = new JLabel("CURSOS");
        lblTitulo.setBounds(0, 0, 1420, 80);
        lblTitulo.setOpaque(true);
        lblTitulo.setBackground(Color.decode("#00A896"));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        add(lblTitulo);

        // Subtítulo
        JLabel lblSubtitulo = new JLabel("¡Checa los últimos cursos de SportBook!");
        lblSubtitulo.setBounds(50, 90, 500, 30);
        lblSubtitulo.setFont(new Font("SansSerif", Font.PLAIN, 18));
        add(lblSubtitulo);

        // Botón publicar (posición mejorada)
        JButton btnPublicar = new JButton("Publicar nuevo curso");
        btnPublicar.setBounds(1050, 90, 200, 30);
        btnPublicar.setBackground(Color.decode("#4CAF50"));
        btnPublicar.setForeground(Color.WHITE);
        btnPublicar.setFocusPainted(false);
        btnPublicar.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btnPublicar.addActionListener(e -> {
            onCrearCurso.run();
            refreshCursos(); // Actualizar después de crear
        });
        add(btnPublicar);

        // ScrollPane con lista dinámica
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(50, 140, 1320, 800);
        
        model = new DefaultListModel<>();
        listaCursos = new JList<>(model);
        listaCursos.setFont(new Font("SansSerif", Font.PLAIN, 16));
        scrollPane.setViewportView(listaCursos);
        add(scrollPane);

        // Cargar datos iniciales
        refreshCursos();
    }

    private void refreshCursos() {
        model.clear(); // Limpiar datos anteriores
        
        try (Connection conn = dbConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT Descripcion, Fecha, COUNT(ice.ID_usu) AS Inscritos " +
                 "FROM Curso_Evento ce " +
                 "LEFT JOIN Integrantes_CE ice ON ce.ID_CE = ice.ID_CE " +
                 "WHERE ce.Tipo = 'Curso' " +
                 "GROUP BY ce.ID_CE")) {
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                model.addElement(
                    "Curso: " + rs.getString("Descripcion") + 
                    " | Fecha: " + rs.getDate("Fecha") + 
                    " | Inscritos: " + rs.getInt("Inscritos")
                );
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar cursos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}