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

public class PanelPublicaciones extends JPanel {

    private int idUsuario;
    private DefaultListModel<String> model;
    private JList<String> publicacionesList;

    public PanelPublicaciones(int idUsuario, Runnable onBack) {
        this.idUsuario = idUsuario;
        setLayout(new BorderLayout());
        setBackground(Color.decode("#F5F5F5"));

        JLabel titulo = new JLabel("Publicaciones");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setForeground(Color.decode("#2A5CAA"));

        model = new DefaultListModel<>();
        publicacionesList = new JList<>(model);
        publicacionesList.setFont(new Font("SansSerif", Font.PLAIN, 14));
        publicacionesList.setBorder(new RoundedBorder(10));
        publicacionesList.setBackground(Color.WHITE);

        JButton volver = new JButton("Volver");
        volver.setBackground(Color.GRAY);
        volver.setForeground(Color.WHITE);
        volver.setFont(new Font("SansSerif", Font.BOLD, 14));
        volver.setBorder(new RoundedBorder(10));
        volver.addActionListener(e -> onBack.run());

        JPanel centro = new JPanel(new BorderLayout());
        centro.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        centro.setBackground(Color.decode("#F5F5F5"));
        centro.add(new JScrollPane(publicacionesList), BorderLayout.CENTER);

        cargarPublicaciones();

        JButton btnPublicar = new JButton("Nueva Publicación");
        btnPublicar.addActionListener(e -> mostrarDialogoPublicacion(idUsuario));

        add(titulo, BorderLayout.NORTH);
        add(centro, BorderLayout.CENTER);
        add(volver, BorderLayout.SOUTH);
    }

      private void cargarPublicaciones() {
        model.clear();
        try (Connection conn = dbConnection.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                 "SELECT p.Contenido, d.Nombre AS Deporte, u.Nombre_usuario " +
                 "FROM Publicacion p " +
                 "JOIN Deporte d ON p.ID_dep = d.ID_Dep " +
                 "JOIN Usuario u ON p.ID_Usu = u.ID_Usu " +
                 "ORDER BY p.Fecha DESC")) {
            
            while (rs.next()) {
                model.addElement(
                    "[" + rs.getString("Deporte") + "] " +
                    rs.getString("Nombre_usuario") + ": " +
                    rs.getString("Contenido")
                );
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar publicaciones");
        }
    }

    private void mostrarDialogoPublicacion(int idUsuario) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Nueva Publicación");
        dialog.setModal(true);
        dialog.setSize(500, 400);
        dialog.setLayout(new BorderLayout());
        
        // Panel principal
        JPanel contentPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Componentes del formulario
        JLabel lblDeporte = new JLabel("Seleccionar Deporte:");
        JComboBox<String> comboDeportes = new JComboBox<>();
        JLabel lblContenido = new JLabel("Contenido:");
        JTextArea txtContenido = new JTextArea(5, 20);
        txtContenido.setLineWrap(true);
        txtContenido.setWrapStyleWord(true);
        
        // Cargar deportes desde la base de datos
        try (Connection conn = dbConnection.conectar();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT Nombre FROM Deporte")) {
            
            while(rs.next()) {
                comboDeportes.addItem(rs.getString("Nombre"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar deportes");
        }
        
        // Botones
        JButton btnPublicar = new JButton("Publicar");
        JButton btnCancelar = new JButton("Cancelar");
        
        btnPublicar.addActionListener(e -> {
            if(comboDeportes.getSelectedItem() == null || txtContenido.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Debes completar todos los campos");
                return;
            }
            
            try (Connection conn = dbConnection.conectar()) {
                // Obtener ID del deporte seleccionado
                String deporte = comboDeportes.getSelectedItem().toString();
                String sqlDeporte = "SELECT ID_Dep FROM Deporte WHERE Nombre = ?";
                
                PreparedStatement pstmtDeporte = conn.prepareStatement(sqlDeporte);
                pstmtDeporte.setString(1, deporte);
                ResultSet rsDeporte = pstmtDeporte.executeQuery();
                
                if(rsDeporte.next()) {
                    int idDep = rsDeporte.getInt("ID_Dep");
                    
                    // Insertar publicación
                    String sql = "INSERT INTO Publicacion (ID_dep, ID_Usu, Contenido) VALUES (?, ?, ?)";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setInt(1, idDep);
                    pstmt.setInt(2, idUsuario);
                    pstmt.setString(3, txtContenido.getText().trim());
                    
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(dialog, "Publicación creada exitosamente");
                    dialog.dispose();
                    cargarPublicaciones();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            }
        });
        
        btnCancelar.addActionListener(e -> dialog.dispose());
        
        // Agregar componentes
        contentPanel.add(lblDeporte);
        contentPanel.add(comboDeportes);
        contentPanel.add(lblContenido);
        contentPanel.add(new JScrollPane(txtContenido));
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(btnCancelar);
        buttonPanel.add(btnPublicar);
        
        dialog.add(contentPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}
