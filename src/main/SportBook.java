package main;

import components.panels.*;
import database.dbConnection;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.*;

public class SportBook extends JFrame {
    private CardLayout contentLayout;
    private JPanel contentPanel;
    private LoginPanel loginPanel;
    private RegisterPanel registroPanel;
    private Sidebar sidebar;
    
    // Paneles de contenido
    private HomePanel homePanel;
    private EventsPanel eventsPanel;
    private CalendarPanel calendarioPanel;
    private CoursesPanel cursosPanel;
    private DeportesPanel deportesPanel;
    private GruposPanel gruposPanel;
    private PerfilPanel perfilPanel;
    private CrearGrupoPanel crearGrupoPanel;
    private PanelCrearEvento crearEventoPanel;
    private PanelCrearCurso crearCursoPanel;
    private PanelPublicaciones publicacionesPanel;
    
    // Paneles deportivos
    private PanelFutbol futbolPanel;
    private PanelBasquetbol basquetbolPanel;
    private PanelVoleibol voleibolPanel;
    private PanelTenis tenisPanel;
    private PanelNatacion natacionPanel;
    private PanelBox boxPanel;
    
    private int usuarioId;

    public SportBook() {
        setTitle("SportBook");
        setSize(1920, 1080);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        // Paneles de autenticación
        loginPanel = new LoginPanel(
            () -> {
                showContentPanel("Inicio");
                sidebar.setVisible(true);
            },
            () -> showRegisterPanel(),
            this
        );

        registroPanel = new RegisterPanel(
            () -> {
                JOptionPane.showMessageDialog(this, "¡Usuario registrado con éxito!");
                showLoginPanel();
            },
            () -> showLoginPanel()
        );

        // Sidebar
        sidebar = new Sidebar(this::switchPanel);
        sidebar.setVisible(false);

        initContentPanels();

        add(sidebar);
        add(contentPanel);
        add(loginPanel);
        add(registroPanel);

        registroPanel.setVisible(false);
        contentPanel.setVisible(false);
        setVisible(true);
    }

    private void initContentPanels() {
        contentPanel = new JPanel();
        contentLayout = new CardLayout();
        contentPanel.setLayout(contentLayout);
        contentPanel.setBounds(sidebar.getWidth(), 0, getWidth() - sidebar.getWidth(), getHeight());

        // Inicializar paneles
        homePanel = new HomePanel(() -> showContentPanel("Eventos"));
        eventsPanel = new EventsPanel(
            () -> showContentPanel("Inicio"),
            () -> showContentPanel("Crear Evento")
        );
        calendarioPanel = new CalendarPanel(() -> showContentPanel("Inicio"));
        cursosPanel = new CoursesPanel(
            () -> showContentPanel("Inicio"),
            () -> showContentPanel("Crear Curso")
        );
        gruposPanel = new GruposPanel(
            () -> showContentPanel("Inicio"),
            () -> showContentPanel("Crear Grupo")
        );
        perfilPanel = new PerfilPanel(() -> showContentPanel("Inicio"), usuarioId);
        crearGrupoPanel = new CrearGrupoPanel(() -> showContentPanel("Grupos"));
        deportesPanel = new DeportesPanel(deporte -> showContentPanel(deporte));
        
        // Paneles deportivos
        futbolPanel = new PanelFutbol(() -> showContentPanel("Deportes"));
        basquetbolPanel = new PanelBasquetbol(() -> showContentPanel("Deportes"));
        voleibolPanel = new PanelVoleibol(() -> showContentPanel("Deportes"));
        tenisPanel = new PanelTenis(() -> showContentPanel("Deportes"));
        natacionPanel = new PanelNatacion(() -> showContentPanel("Deportes"));
        boxPanel = new PanelBox(() -> showContentPanel("Deportes"));
        
        crearEventoPanel = new PanelCrearEvento(usuarioId, () -> showContentPanel("Inicio"));
        crearCursoPanel = new PanelCrearCurso(usuarioId, () -> showContentPanel("Cursos"));
        publicacionesPanel = new PanelPublicaciones(usuarioId,() -> showContentPanel("Inicio"));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Agregar paneles al contentPanel
        contentPanel.add(homePanel, "Inicio");
        contentPanel.add(eventsPanel, "Eventos");
        contentPanel.add(calendarioPanel, "Calendario");
        contentPanel.add(cursosPanel, "Cursos");
        contentPanel.add(gruposPanel, "Grupos");
        contentPanel.add(perfilPanel, "Perfil");
        contentPanel.add(crearGrupoPanel, "Crear Grupo");
        contentPanel.add(deportesPanel, "Deportes");
        contentPanel.add(futbolPanel, "Futbol");
        contentPanel.add(basquetbolPanel, "Basquetball");
        contentPanel.add(voleibolPanel, "Voleibol");
        contentPanel.add(tenisPanel, "Tenis");
        contentPanel.add(natacionPanel, "Natacion");
        contentPanel.add(boxPanel, "Box");
        contentPanel.add(crearEventoPanel, "Crear Evento");
        contentPanel.add(crearCursoPanel, "Crear Curso");
        contentPanel.add(publicacionesPanel, "Publicaciones");
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    private void switchPanel(String name) {
        if (name.equals("Perfil")) {
            contentPanel.remove(perfilPanel);
            perfilPanel = new PerfilPanel(() -> showContentPanel("Inicio"), usuarioId);
            contentPanel.add(perfilPanel, "Perfil");
        }
        contentLayout.show(contentPanel, name);
    }

    // Métodos de navegación
    private void showLoginPanel() {
        loginPanel.setVisible(true);
        registroPanel.setVisible(false);
        contentPanel.setVisible(false);
    }

    private void showRegisterPanel() {
        loginPanel.setVisible(false);
        registroPanel.setVisible(true);
        contentPanel.setVisible(false);
    }

    private void showContentPanel(String panelName) {
        loginPanel.setVisible(false);
        registroPanel.setVisible(false);
        contentPanel.setVisible(true);
        contentLayout.show(contentPanel, panelName);
    }

    public static void main(String[] args) {
        try (Connection conn = dbConnection.conectar()) {
            new SportBook();
            System.out.println("Conexión exitosa");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al conectar a la base de datos", "Error crítico", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
}