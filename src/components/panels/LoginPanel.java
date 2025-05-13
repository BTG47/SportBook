package components.panels;
import main.SportBook;
import components.borders.RoundedBorder;
import components.fields.HintPasswordField;
import components.fields.HintTextField;
import components.utils.SHA256Utils;
import database.dbConnection;
import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class LoginPanel extends JPanel {
    private final SportBook sportBook;
    private JLabel LoginTitulo, LoginTitulo2, LoginSubtitulo, NoTienesCuenta;
    private HintTextField UsuarioLoginTextfield;
    private HintPasswordField IPaswordLogin;
    private JButton Login, Registrate;

    public LoginPanel(Runnable onLoginSuccess, Runnable onRegisterClick, SportBook sportBook) {
        this.sportBook = sportBook;
        setLayout(null);
        setBackground(Color.WHITE);
        setBounds(0, 0, 1920, 1080);

        // ---------------------- Componentes UI ----------------------
        LoginTitulo = new JLabel("¡BIENVENIDO A ");
        LoginTitulo.setBounds(412, 20, 400, 30);
        LoginTitulo.setFont(new Font("SansSerif", Font.BOLD, 30));

        LoginTitulo2 = new JLabel("SPORTBOOK!");
        LoginTitulo2.setBounds(650, 20, 400, 30);
        LoginTitulo2.setFont(new Font("SansSerif", Font.BOLD, 30));
        LoginTitulo2.setForeground(Color.decode("#2A5CAA"));

        LoginSubtitulo = new JLabel("Ingresa tu usuario y contraseña");
        LoginSubtitulo.setBounds(470, 265, 400, 30);
        LoginSubtitulo.setFont(new Font("SansSerif", Font.PLAIN, 22));

        NoTienesCuenta = new JLabel("¿No tienes una cuenta? Regístrate");
        NoTienesCuenta.setBounds(540, 505, 400, 30);
        NoTienesCuenta.setFont(new Font("SansSerif", Font.PLAIN, 12));

        UsuarioLoginTextfield = new HintTextField("Usuario");
        UsuarioLoginTextfield.setBounds(512, 315, 250, 50);

        IPaswordLogin = new HintPasswordField("Contraseña");
        IPaswordLogin.setBounds(512, 375, 250, 50);

        Login = new JButton("Ingresar");
        Login.setBounds(580, 445, 110, 35);
        Login.setBackground(Color.decode("#4CAF50"));
        Login.setForeground(Color.WHITE);
        Login.setFocusPainted(false);
        Login.setBorder(new RoundedBorder(15));

        Registrate = new JButton("Registrarte");
        Registrate.setBounds(580, 540, 110, 35);
        Registrate.setBackground(Color.decode("#2A5CAA"));
        Registrate.setForeground(Color.WHITE);
        Registrate.setFocusPainted(false);
        Registrate.setBorder(new RoundedBorder(15));

        // ---------------------- Lógica de Login ----------------------
        Login.addActionListener(e -> {
            onLoginSuccess.run(); //NOTA: BORRAR, SOLO ES PARA PROBAR LOS CAMBIOS
            String usuario = UsuarioLoginTextfield.getText().trim();
            String contraseña = new String(IPaswordLogin.getPassword()).trim();

            // Validar campos vacíos
            if (usuario.isEmpty() || contraseña.isEmpty()) {
                JOptionPane.showMessageDialog(
                    this,
                    "Usuario y contraseña son obligatorios",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            try (Connection conn = dbConnection.conectar()) {
                String sql = "SELECT ID_Usu, Contrasena FROM Usuario WHERE Nombre_usuario = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, usuario);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    String storedHash = rs.getString("Contrasena");
                    if (SHA256Utils.checkPassword(contraseña, storedHash)) {
                        sportBook.setUsuarioId(rs.getInt("ID_Usu")); // Solo esta línea es necesaria
                        onLoginSuccess.run();
                    } else {
                        JOptionPane.showMessageDialog(this, "Contraseña incorrecta", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Usuario no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error de conexión a la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        Registrate.addActionListener(e -> onRegisterClick.run());

        // ---------------------- Añadir componentes ----------------------
        add(LoginTitulo);
        add(LoginTitulo2);
        add(LoginSubtitulo);
        add(NoTienesCuenta);
        add(UsuarioLoginTextfield);
        add(IPaswordLogin);
        add(Login);
        add(Registrate);
    }
}