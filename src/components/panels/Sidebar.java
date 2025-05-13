package components.panels;
import components.borders.RoundedBorder;
import java.awt.*;
import java.util.function.Consumer;
import javax.swing.*;

public class Sidebar extends JPanel {
    private JButton selectedButton; // Almacena el botón seleccionado

    public Sidebar(Consumer<String> onSectionChange) {
        setLayout(null);
        setBackground(Color.decode("#333333"));
        setBounds(0, 0, 240, 1080);
    
        JLabel lblMenu = new JLabel("MENÚ");
        lblMenu.setBounds(78, 30, 200, 40);
        lblMenu.setForeground(Color.WHITE);
        lblMenu.setFont(new Font("SansSerif", Font.BOLD, 28));
        add(lblMenu);
    
        String[] sections = {"Inicio", "Calendario", "Eventos", "Cursos", "Deportes", "Grupos", "Perfil"};
        for (int i = 0; i < sections.length; i++) {
            JButton btn = new JButton(sections[i]);
            btn.setBounds(32, 100 + i * 60, 175, 40);
            btn.setBackground(Color.decode("#333333"));
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("SansSerif", Font.BOLD, 18));
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setMargin(new Insets(0, 10, 0, 0));
            btn.setBorder(new RoundedBorder(15));
            
            // Lógica para resaltar el botón seleccionado
            String section = sections[i]; // Variable local final
            btn.addActionListener(e -> {
                resetButtonColors();
                btn.setBackground(Color.decode("#4CAF50"));
                selectedButton = btn;
                onSectionChange.accept(section); // Usamos la copia
            });
            
            add(btn);
        }
    }

    // Método para resetear los colores de los botones
    private void resetButtonColors() {
        for (Component comp : getComponents()) {
            if (comp instanceof JButton && comp != selectedButton) {
                comp.setBackground(Color.decode("#333333"));
            }
        }
    }
}