package components.panels;
import components.fields.HintTextField;
import database.dbConnection;
import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class PerfilPanel extends JPanel {
    public PerfilPanel(Runnable onBack, int usuarioId) {
        setBackground(Color.WHITE);
        setLayout(null);

        // ---------------------- Encabezado ----------------------
        JPanel header = new JPanel();
        header.setBounds(0, 0, 1420, 80);
        header.setBackground(Color.decode("#666666"));
        header.setLayout(new BorderLayout());

        JLabel title = new JLabel("PERFIL", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.CENTER);
        add(header);

        // ---------------------- Campos de información ----------------------
        
        JLabel usernameLabel = new JLabel("Usuario");
        JLabel nombreLabel = new JLabel("Nombre");
        JLabel apellidoLabel = new JLabel("Apellido");
        JLabel deporteFavoritoLabel = new JLabel("Deporte favorito");
        
        usernameLabel.setBounds(220, 100, 250, 50);
        nombreLabel.setBounds(500, 100, 250, 50);
        apellidoLabel.setBounds(780, 100, 250, 50);
        deporteFavoritoLabel.setBounds(220, 190, 250, 50);
        
        usernameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        nombreLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        apellidoLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        deporteFavoritoLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        add(usernameLabel);
        add(nombreLabel);
        add(apellidoLabel);
        add(deporteFavoritoLabel);

        HintTextField usernameField = crearCampoNoEditable(220, 150, "Nombre de usuario");
        HintTextField nombreField = crearCampoNoEditable(500, 150, "Nombre");
        HintTextField apellidosField = crearCampoNoEditable(780, 150, "Apellidos");
        HintTextField deporteFavField = crearCampoNoEditable(220, 230, "Deporte favorito");

        // ---------------------- Secciones dinámicas ----------------------
        JTextArea gruposText = crearAreaTexto(220, 320, "Grupos actuales");
        JTextArea cursosText = crearAreaTexto(500, 320, "Cursos actuales");
        JTextArea eventosText = crearAreaTexto(780, 320, "Eventos actuales");

        // ---------------------- Botón "Volver" ----------------------
        JButton backButton = new JButton("Volver");
        backButton.setBounds(50, 100, 100, 40);
        backButton.setBackground(Color.decode("#4CAF50"));
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> onBack.run());
        add(backButton);

        // ---------------------- Cargar datos desde BD ----------------------
        cargarDatosUsuario(usuarioId, usernameField, nombreField, apellidosField, deporteFavField, gruposText, cursosText, eventosText);
    }

    // Método para crear campos no editables
    private HintTextField crearCampoNoEditable(int x, int y, String hint) {
        HintTextField campo = new HintTextField(hint);
        campo.setBounds(x, y, 250, 50);
        campo.setEditable(false);
        campo.setFocusable(false);
        campo.setBackground(Color.WHITE);
        campo.setForeground(Color.BLACK);
        add(campo);
        return campo;
    }

    // Método para cargar datos del usuario
    private void cargarDatosUsuario(int usuarioId, HintTextField usernameField, HintTextField nombreField, 
                                    HintTextField apellidosField, HintTextField deporteFavField, 
                                    JTextArea gruposText, JTextArea cursosText, JTextArea eventosText) {
        try (Connection conn = dbConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT " +
                 "u.Nombre_usuario, u.Nombre, u.Apellidos, u.Deporte_Fav, " +
                 "GROUP_CONCAT(DISTINCT CASE WHEN a.Tipo = 'Grupo' THEN a.Nombre END SEPARATOR '\\n') AS Grupos, " +
                 "GROUP_CONCAT(DISTINCT CASE WHEN a.Tipo = 'Curso' THEN a.Nombre END SEPARATOR '\\n') AS Cursos, " +
                 "GROUP_CONCAT(DISTINCT CASE WHEN a.Tipo = 'Evento' THEN a.Nombre END SEPARATOR '\\n') AS Eventos " +
                 "FROM Usuario u " +
                 "LEFT JOIN Participacion p ON u.ID_Usu = p.ID_Usu " +
                 "LEFT JOIN Actividad a ON p.ID_Act = a.ID_Act " +
                 "WHERE u.ID_Usu = ? " +
                 "GROUP BY u.ID_Usu"
             )) {
            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                usernameField.setText(rs.getString("Nombre_usuario"));
                nombreField.setText(rs.getString("Nombre"));
                apellidosField.setText(rs.getString("Apellidos"));
                deporteFavField.setText(rs.getString("Deporte_Fav") != null ? rs.getString("Deporte_Fav") : "No especificado");

                gruposText.setText(rs.getString("Grupos") != null ? rs.getString("Grupos") : "No participa en grupos");
                cursosText.setText(rs.getString("Cursos") != null ? rs.getString("Cursos") : "No está inscrito en cursos");
                eventosText.setText(rs.getString("Eventos") != null ? rs.getString("Eventos") : "No asiste a eventos");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos", "Error", JOptionPane.ERROR_MESSAGE);
            gruposText.setText("Error al cargar");
            cursosText.setText("Error al cargar");
            eventosText.setText("Error al cargar");
        }
    }

    // Método para crear áreas de texto con scroll
    private JTextArea crearAreaTexto(int x, int y, String titulo) {
        JLabel label = new JLabel(titulo);
        label.setBounds(x, y - 30, 200, 25);
        label.setFont(new Font("SansSerif", Font.BOLD, 16));
        add(label);

        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBackground(Color.WHITE);
        area.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        JScrollPane scroll = new JScrollPane(area);
        scroll.setBounds(x, y, 250, 100);
        add(scroll);

        return area;
    }
}