package components.panels;
import components.borders.RoundedBorder;
import database.dbConnection;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.*;

public class PanelCrearCurso extends JPanel {

    private int idCreador;

    public PanelCrearCurso(int idCreador, Runnable onBack) {
        this.idCreador = idCreador;
        setLayout(new GridBagLayout());
        setBackground(Color.decode("#F5F5F5"));

        JPanel formPanel = new JPanel(new GridLayout(0, 1, 15, 15));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel titulo = new JLabel("Crear Curso");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        titulo.setForeground(Color.decode("#2A5CAA"));

        // Crear JComboBox para seleccionar el deporte
        JComboBox<String> deporte = new JComboBox<>();
        try (Connection conn = dbConnection.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT Nombre FROM Deporte")) {
            while (rs.next()) {
                deporte.addItem(rs.getString("Nombre"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error cargando deportes: " + ex.getMessage());
        }

        JTextField nombre = crearHintField("Nombre del curso");
        JTextField descripcion = crearHintField("Descripción");
        JTextField entrenador = crearHintField("Entrenador");
        JTextField horario = crearHintField("Horario (e.g., Martes y Viernes 09:00-11:00)");
        JTextField fechaInicio = crearHintField("Fecha inicio (YYYY-MM-DD HH:MM:SS)");
        JTextField fechaFin = crearHintField("Fecha fin (YYYY-MM-DD HH:MM:SS)");

        JButton crear = crearBoton("Crear Curso");
        JButton volver = crearBoton("Volver");
        crear.addActionListener(e -> {
            try (Connection conn = dbConnection.conectar()) {
                // Insertar en Actividad
                String sqlActividad = "INSERT INTO Actividad (Tipo, Nombre, Descripcion, Fecha_Inicio, Fecha_Fin, ID_Dep, ID_Creador) " +
                                      "VALUES ('Curso', ?, ?, ?, ?, (SELECT ID_Dep FROM Deporte WHERE Nombre = ?), ?)";
                
                PreparedStatement pstmt = conn.prepareStatement(sqlActividad, Statement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, nombre.getText());
                pstmt.setString(2, descripcion.getText());
                pstmt.setString(3, fechaInicio.getText());
                pstmt.setString(4, fechaFin.getText());
                pstmt.setString(5, deporte.getSelectedItem().toString());
                pstmt.setInt(6, idCreador);
                
                pstmt.executeUpdate();
                
                // Obtener ID generado
                ResultSet rs = pstmt.getGeneratedKeys();
                if(rs.next()) {
                    int idActividad = rs.getInt(1);
                    
                    // Insertar en Curso
                    String sqlCurso = "INSERT INTO Curso (ID_Curso, ID_Entrenador, Horario) VALUES (?, ?, ?)";
                    PreparedStatement pstmtCurso = conn.prepareStatement(sqlCurso);
                    pstmtCurso.setInt(1, idActividad);
                    pstmtCurso.setInt(2, obtenerIdEntrenador(conn, entrenador.getText()));
                    pstmtCurso.setString(3, horario.getText());
                    pstmtCurso.executeUpdate();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al crear el curso: " + ex.getMessage());
            }
        });

        formPanel.add(titulo);
        formPanel.add(deporte);
        formPanel.add(nombre);
        formPanel.add(descripcion);
        formPanel.add(entrenador);
        formPanel.add(horario);
        formPanel.add(fechaInicio);
        formPanel.add(fechaFin);
        formPanel.add(crear);
        formPanel.add(volver);
        volver.setBackground(Color.GRAY);
        volver.addActionListener(e -> onBack.run());
        add(formPanel);
    }

    private int obtenerIdEntrenador(Connection conn, String nombreEntrenador) throws SQLException {
        String sql = "SELECT e.ID_Ent FROM Entrenador e " +
                     "JOIN Usuario u ON e.ID_Usu = u.ID_Usu " +
                     "WHERE CONCAT(u.Nombre, ' ', u.Apellidos) = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, nombreEntrenador);
        ResultSet rs = pstmt.executeQuery();
        return rs.next() ? rs.getInt(1) : -1;
    }

    private JTextField crearHintField(String hint) {
        JTextField field = new JTextField(hint);
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBorder(new RoundedBorder(10));
        return field;
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setFocusPainted(false);
        boton.setBackground(Color.decode("#00A896"));
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("SansSerif", Font.BOLD, 14));
        boton.setBorder(new RoundedBorder(10));
        return boton;
    }
}
