package components.panels;
import components.borders.RoundedBorder;
import components.fields.*;
import components.utils.SHA256Utils;
import database.dbConnection;
import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class RegisterPanel extends JPanel {
    private final HintTextField txtNombre, txtApellido, txtUsername, txtEmail, txtEdad, txtEquipoFav;
    private final HintPasswordField txtPassword;
    private final JComboBox<String> comboDeporteFav;

    public RegisterPanel(Runnable onRegisterSuccess, Runnable onBackToLogin) {
        setLayout(null);
        setBounds(0, 0, 1920, 1080);
        setBackground(Color.WHITE);

        // ---------------------- Componentes UI ----------------------
        JLabel RegTitulo = new JLabel("¡REGÍSTRATE!");
        RegTitulo.setBounds(675, 20, 400, 30);
        RegTitulo.setFont(new Font("SansSerif", Font.BOLD, 30));
        RegTitulo.setForeground(Color.decode("#2A5CAA"));

        JLabel RegSubtitulo = new JLabel("Es completamente gratis");
        RegSubtitulo.setBounds(663, 70, 400, 30);
        RegSubtitulo.setFont(new Font("SansSerif", Font.PLAIN, 22));

        JLabel YaTienesCuenta = new JLabel("¿Ya tienes un usuario? Inicia sesión");
        YaTienesCuenta.setBounds(688, 535, 400, 30);
        YaTienesCuenta.setFont(new Font("SansSerif", Font.PLAIN, 12));

        int x = 570, x2 = 805, y = 125, step = 75;

        txtNombre = new HintTextField("Nombre");
        txtNombre.setBounds(x, y, 200, 45);

        txtApellido = new HintTextField("Apellido");
        txtApellido.setBounds(x2, y, 200, 45); y += step;

        txtUsername = new HintTextField("Nombre de usuario");
        txtUsername.setBounds(x, y, 200, 45);

        txtPassword = new HintPasswordField("Contraseña");
        txtPassword.setBounds(x2, y, 200, 45); y += step;

        txtEmail = new HintTextField("Correo electrónico");
        txtEmail.setBounds(x, y, 200, 45);

        txtEdad = new HintTextField("Edad");
        txtEdad.setBounds(x2, y, 200, 45); y += step;

        comboDeporteFav = new JComboBox<>(new String[]{"Futbol", "Tenis", "Basquetbol", "Box", "Natacion"});
        comboDeporteFav.setBounds(x, y, 200, 45);

        txtEquipoFav = new HintTextField("Equipo favorito (opcional)");
        txtEquipoFav.setBounds(x2, y, 200, 45); y += step;

        JButton btnRegistrar = new JButton("Registrar");
        btnRegistrar.setBounds(732, 485, 110, 35);
        btnRegistrar.setBackground(Color.decode("#2A5CAA"));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setBorder(new RoundedBorder(15));

        JButton btnVolverLogin = new JButton("Ingresar");
        btnVolverLogin.setBounds(732, 565, 110, 35);
        btnVolverLogin.setBackground(Color.decode("#4CAF50"));
        btnVolverLogin.setForeground(Color.WHITE);
        btnVolverLogin.setBorder(new RoundedBorder(15));
        btnVolverLogin.addActionListener(e -> onBackToLogin.run());

        // ---------------------- Lógica de Registro ----------------------
        btnRegistrar.addActionListener(e -> {
            if (validarCampos()) {
                try (Connection conn = dbConnection.conectar()) {
                    String sql = "INSERT INTO Usuario (Nombre, Apellidos, Edad, Nombre_usuario, "
                               + "Correo, Contrasena, Deporte_Fav, Equipo_Favorito) "
                               + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                    
                    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                        stmt.setString(1, txtNombre.getText());
                        stmt.setString(2, txtApellido.getText());
                        stmt.setInt(3, Integer.parseInt(txtEdad.getText()));
                        stmt.setString(4, txtUsername.getText());
                        stmt.setString(5, txtEmail.getText());
                        stmt.setString(6, SHA256Utils.hashPassword(new String(txtPassword.getPassword())));
                        stmt.setString(7, comboDeporteFav.getSelectedItem().toString());
                        stmt.setString(8, txtEquipoFav.getText().isBlank() ? null : txtEquipoFav.getText());

                        stmt.executeUpdate();
                        JOptionPane.showMessageDialog(this, "¡Registro exitoso!");
                        onRegisterSuccess.run();
                    }
                } catch (SQLException ex) {
                    manejarErroresSQL(ex);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(
                        this,
                        "La edad debe ser un número válido",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Complete todos los campos obligatorios (*)",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });

        // ---------------------- Añadir componentes ----------------------
        add(RegTitulo);
        add(RegSubtitulo);
        add(YaTienesCuenta);
        add(txtNombre);
        add(txtApellido);
        add(txtUsername);
        add(txtPassword);
        add(txtEmail);
        add(txtEdad);
        add(comboDeporteFav);
        add(txtEquipoFav);
        add(btnRegistrar);
        add(btnVolverLogin);
    }

    private boolean validarCampos() {
        return !txtNombre.getText().isBlank() &&
               !txtApellido.getText().isBlank() &&
               !txtUsername.getText().isBlank() &&
               !new String(txtPassword.getPassword()).isBlank() &&
               !txtEmail.getText().isBlank() &&
               !txtEdad.getText().isBlank();
    }

    private void manejarErroresSQL(SQLException ex) {
        if (ex.getErrorCode() == 1062) {
            String mensaje = ex.getMessage().contains("username") 
                ? "El nombre de usuario ya está en uso" 
                : "El correo electrónico ya está registrado";
            JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(
                this,
                "Error de base de datos: " + ex.getMessage(),
                "Error crítico",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}