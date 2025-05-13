package components.panels;
import components.borders.RoundedBorder;
import components.fields.HintTextField;
import java.awt.*;
import javax.swing.*;

public class CrearGrupoPanel extends JPanel {
    public CrearGrupoPanel(Runnable onBack) {
        setBackground(Color.WHITE);
        setLayout(null); // Posicionamiento absoluto para mayor control

        // ---------------------- Header ----------------------
        JPanel header = new JPanel();
        header.setBounds(0, 0, 1420, 80);
        header.setBackground(Color.decode("#2A5CAA"));
        header.setLayout(new BorderLayout());

        JLabel title = new JLabel("CREA TU PROPIO GRUPO", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.CENTER);
        add(header);

        // ---------------------- Subtítulo ----------------------
        JLabel subtitle = new JLabel("Ingresa los datos", SwingConstants.CENTER);
        subtitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        subtitle.setBounds(0, 100, 1420, 30);
        add(subtitle);

        // ---------------------- Campos del formulario ----------------------
        // Nombre del grupo
        HintTextField nombreGrupoField = new HintTextField("Nombre del grupo");
        nombreGrupoField.setBounds(590, 160, 240, 40);
        add(nombreGrupoField);

        // Tipo de deporte (mejorado a JComboBox)
        String[] deportes = {"Fútbol", "Básquetbol", "Voleibol", "Tenis", "Natación", "Box"};
        JComboBox<String> deporteCombo = new JComboBox<>(deportes);
        deporteCombo.setBounds(590, 210, 240, 40);
        deporteCombo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        deporteCombo.setBorder(new RoundedBorder(15));
        deporteCombo.setFocusable(false);
        add(deporteCombo);

        // Área de descripción con hint
        JTextArea descripcionArea = new JTextArea();
        descripcionArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        descripcionArea.setForeground(Color.GRAY);
        descripcionArea.setText("Descripción");
        descripcionArea.setLineWrap(true);
        descripcionArea.setWrapStyleWord(true);
        descripcionArea.setBorder(new RoundedBorder(15));
        descripcionArea.setBounds(540, 270, 340, 100);

        // Manejo de hint para JTextArea
        descripcionArea.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (descripcionArea.getText().equals("Descripción")) {
                    descripcionArea.setText("");
                    descripcionArea.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (descripcionArea.getText().isEmpty()) {
                    descripcionArea.setForeground(Color.GRAY);
                    descripcionArea.setText("Descripción");
                }
            }
        });
        add(descripcionArea);

        // ---------------------- Botones ----------------------
        // Botón Registrar
        JButton registrarButton = new JButton("Registrar");
        registrarButton.setBounds(600, 390, 120, 40);
        registrarButton.setBackground(Color.decode("#4CAF50"));
        registrarButton.setForeground(Color.WHITE);
        registrarButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        registrarButton.setFocusPainted(false);
        registrarButton.setBorder(new RoundedBorder(15));
        // registrarButton.addActionListener(e -> guardarGrupo()); // Lógica de guardado
        add(registrarButton);

        // Botón Volver (desde CrearGrupoPanel.java)
        JButton backButton = new JButton("Volver");
        backButton.setBounds(750, 390, 120, 40);
        backButton.setBackground(Color.decode("#FF5722")); // Color distintivo
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        backButton.setFocusPainted(false);
        backButton.setBorder(new RoundedBorder(15));
        backButton.addActionListener(e -> onBack.run());
        add(backButton);
    }
}