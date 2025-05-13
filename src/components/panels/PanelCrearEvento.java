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

public class PanelCrearEvento extends JPanel {

    private int idCreador;

    public PanelCrearEvento(int idCreador,Runnable onBack) {
        this.idCreador = idCreador;
        setLayout(new GridBagLayout());
        setBackground(Color.decode("#F5F5F5"));

        JPanel formPanel = new JPanel(new GridLayout(0, 1, 15, 15));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel titulo = new JLabel("Crear Evento");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        titulo.setForeground(Color.decode("#2A5CAA"));

        JTextField nombre = crearHintField("Nombre del evento");
        JTextField descripcion = crearHintField("Descripción");
        JTextField ubicacion = crearHintField("Ubicación");
        JTextField aforo = crearHintField("Aforo");
        JTextField fechaInicio = crearHintField("Fecha inicio (YYYY-MM-DD HH:MM:SS)");
        JTextField fechaFin = crearHintField("Fecha fin (YYYY-MM-DD HH:MM:SS)");

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

        JButton crear = crearBoton("Crear Evento");
crear.addActionListener(e -> {
            try (Connection conn = dbConnection.conectar()) {
                // Insertar en Actividad
                String sqlActividad = "INSERT INTO Actividad (Tipo, Nombre, Descripcion, Fecha_Inicio, Fecha_Fin, ID_Dep, ID_Creador) " +
                                      "VALUES ('Evento', ?, ?, ?, ?, (SELECT ID_Dep FROM Deporte WHERE Nombre = ?), ?)";
                
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
                    
                    // Insertar en Evento
                    String sqlEvento = "INSERT INTO Evento (ID_Evento, Ubicacion, Aforo) VALUES (?, ?, ?)";
                    PreparedStatement pstmtEvento = conn.prepareStatement(sqlEvento);
                    pstmtEvento.setInt(1, idActividad);
                    pstmtEvento.setString(2, ubicacion.getText());
                    pstmtEvento.setInt(3, Integer.parseInt(aforo.getText()));
                    pstmtEvento.executeUpdate();
                }
                
                JOptionPane.showMessageDialog(this, "Evento creado exitosamente");
                onBack.run();
            } catch (SQLException | NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        JButton volver = crearBoton("Volver");
        volver.setBackground(Color.GRAY);
        volver.addActionListener(e -> onBack.run());

        formPanel.add(titulo);
        formPanel.add(nombre);
        formPanel.add(descripcion);
        formPanel.add(ubicacion);
        formPanel.add(aforo);
        formPanel.add(fechaInicio);
        formPanel.add(fechaFin);
        formPanel.add(crear);
        formPanel.add(volver);

        add(formPanel);
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
        boton.setBackground(Color.decode("#4CAF50"));
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("SansSerif", Font.BOLD, 14));
        boton.setBorder(new RoundedBorder(10));
        return boton;
    }
}
