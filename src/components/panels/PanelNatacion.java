package components.panels;
import database.dbConnection;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;

public class PanelNatacion extends JPanel {
    private JPanel mainContent;
    private String deporteFiltro = "Natacion";
    private JPanel paginationPanel;
    private int currentPage = 0; // Página actual
    private final int ITEMS_PER_PAGE = 2; // Items por página
    private JScrollPane scrollPane;
    private JLabel pageLabel;
    private JButton btnAnterior;
    private JButton btnSiguiente;
        
    private List<Map<String, String>> allItems = new ArrayList<>();
    
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm");
    
    public PanelNatacion(Runnable onBack) { 
        setLayout(new BorderLayout());
        setBackground(Color.decode("#F5F5F5"));
        
        // Panel principal 
        mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(Color.decode("#F5F5F5"));
        add(mainContent, BorderLayout.CENTER);

        // Pagina
        paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnAnterior = crearBoton("Anterior", e -> cambiarPagina(-1));
        btnSiguiente = crearBoton("Siguiente", e -> cambiarPagina(1));
        pageLabel = new JLabel();
        pageLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        paginationPanel.add(btnAnterior);
        paginationPanel.add(pageLabel);
        paginationPanel.add(btnSiguiente);
        paginationPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(paginationPanel, BorderLayout.SOUTH);

        // Panel superior (título + botones)
        JPanel topPanel = new JPanel(new BorderLayout());

        // Título
        JLabel titulo = new JLabel("NATACIÓN");
        titulo.setOpaque(true);
        titulo.setBackground(Color.decode("#00BCD4"));
        titulo.setForeground(Color.WHITE);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        topPanel.add(titulo, BorderLayout.NORTH);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.setBackground(Color.decode("#F5F5F5"));
        buttonPanel.add(crearBoton("Volver", e -> onBack.run()));
        buttonPanel.add(crearBoton("Crear Evento", e -> {}));
        buttonPanel.add(crearBoton("Crear Curso", e -> {}));
        buttonPanel.add(crearBoton("Crear Publicación", e -> {}));
        topPanel.add(buttonPanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        cargarContenido();
    }
    
    private void cambiarPagina(int direction) {
        currentPage += direction;
        if (currentPage < 0) currentPage = 0;
        actualizarVista();
    }
    
    // Método auxiliar para crear botones
    private JButton crearBoton(String texto, ActionListener accion) {
        JButton btn = new JButton(texto);
        btn.setPreferredSize(new Dimension(150, 40));
        btn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btn.setBackground(Color.decode("#00BCD4"));
        btn.setForeground(Color.WHITE);
        btn.addActionListener(accion);
        return btn;
    }

    private void cargarContenido() {
        new Thread(() -> {
            try (Connection conn = dbConnection.conectar()) {
            String query = 
                "SELECT * FROM ( " +
                "SELECT " +
                "a.ID_Act AS ID, " +
                "a.Tipo, " +
                "a.Nombre AS Titulo, " +
                "a.Descripcion, " +
                "a.Fecha_Inicio AS FechaOrden, " +
                "a.Fecha_Inicio, " +
                "a.Fecha_Fin, " +
                "d.Nombre AS Deporte, " +
                "u.Nombre_usuario AS Creador, " +
                "e.Ubicacion, " +
                "e.Aforo, " +
                "c.Horario, " +
                "ent.Nombre AS Entrenador " +
                "FROM Actividad a " +
                "LEFT JOIN Evento e ON a.ID_Act = e.ID_Evento " +
                "LEFT JOIN Curso c ON a.ID_Act = c.ID_Curso " +
                "LEFT JOIN Entrenador entr ON c.ID_Entrenador = entr.ID_Ent " +
                "LEFT JOIN Usuario ent ON entr.ID_Usu = ent.ID_Usu " +
                "JOIN Deporte d ON a.ID_Dep = d.ID_Dep " +
                "JOIN Usuario u ON a.ID_Creador = u.ID_Usu " +
                "WHERE d.Nombre = ? " +
                
                "UNION ALL " +

                "SELECT " +
                "p.ID_Pub AS ID, " +
                "'Publicacion' AS Tipo, " +
                "LEFT(p.Contenido, 50) AS Titulo, " +
                "p.Contenido AS Descripcion, " +
                "p.Fecha AS FechaOrden, " +
                "p.Fecha AS Fecha_Inicio, " +
                "NULL AS Fecha_Fin, " +
                "d.Nombre AS Deporte, " +
                "u.Nombre_usuario AS Creador, " +
                "NULL AS Ubicacion, " +
                "NULL AS Aforo, " +
                "NULL AS Horario, " +
                "NULL AS Entrenador " +
                "FROM Publicacion p " +
                "JOIN Deporte d ON p.ID_Dep = d.ID_Dep " +
                "JOIN Usuario u ON p.ID_Usu = u.ID_Usu " +
                "WHERE d.Nombre = ? " +

                ") AS combined ORDER BY FechaOrden DESC";
                
                allItems.clear(); 

                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setString(1, deporteFiltro);
                    pstmt.setString(2, deporteFiltro);
                    
                    ResultSet rs = pstmt.executeQuery();
                    
                    while (rs.next()) {
                        Map<String, String> item = new HashMap<>();
                        item.put("ID", rs.getString("ID"));
                        item.put("Tipo", rs.getString("Tipo"));
                        item.put("Titulo", rs.getString("Titulo"));
                        item.put("Descripcion", rs.getString("Descripcion"));
                        item.put("FechaInicio", dateFormat.format(rs.getTimestamp("Fecha_Inicio")));
                        item.put("FechaFin", rs.getTimestamp("Fecha_Fin") != null ? 
                            dateFormat.format(rs.getTimestamp("Fecha_Fin")) : "N/A");
                        item.put("Ubicacion", rs.getString("Ubicacion"));
                        item.put("Aforo", rs.getString("Aforo"));
                        item.put("Horario", rs.getString("Horario"));
                        item.put("Entrenador", rs.getString("Entrenador"));
                        item.put("Creador", rs.getString("Creador"));
                        item.put("Inscritos", obtenerInscritos(conn, rs.getString("ID")));
                        
                        System.out.println("Total items encontrados: " + allItems.size());
                        for (Map<String, String> it : allItems) {
                            System.out.println(it);
                        }
                        allItems.add(item);
                    }
                    SwingUtilities.invokeLater(() -> {
                        actualizarVista(); // Mostrar primera página
                        });
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al cargar contenido", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }).start();
    }

    private String obtenerInscritos(Connection conn, String idActividad) {
        if (idActividad == null) return "N/A";
        try (PreparedStatement pstmt = conn.prepareStatement(
                "SELECT COUNT(*) AS total FROM Participacion WHERE ID_Act = ?")) {
            pstmt.setInt(1, Integer.parseInt(idActividad));
            ResultSet rs = pstmt.executeQuery();
            return rs.next() ? rs.getString("total") : "0";
        } catch (Exception e) {
            return "Error";
        }
    }

private JPanel crearCardContenido(Map<String, String> item) {
    JPanel card = new JPanel();
    card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
    card.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(Color.LIGHT_GRAY),
        BorderFactory.createEmptyBorder(10, 10, 10, 10))
    );
    card.setBackground(Color.WHITE);
    card.setMaximumSize(new Dimension(800, Integer.MAX_VALUE));
    card.setAlignmentX(Component.CENTER_ALIGNMENT);

    // Encabezado
    JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
    header.setBackground(Color.WHITE);
    JLabel lblTipo = new JLabel("[" + item.get("Tipo") + "] " + item.get("Titulo"));
    lblTipo.setFont(new Font("SansSerif", Font.BOLD, 18));
    header.add(lblTipo);
    card.add(header);

    // Detalles
    JPanel detallesPanel = new JPanel(new GridLayout(0, 2, 10, 5));
    detallesPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    detallesPanel.setBackground(Color.WHITE);

    addDetail(detallesPanel, "Creado por:", item.get("Creador"));
    addDetail(detallesPanel, "Fecha inicio:", item.get("FechaInicio"));

    if (!item.get("Tipo").equals("Publicacion")) {
        addDetail(detallesPanel, "Fecha fin:", item.get("FechaFin"));
        addDetail(detallesPanel, "Inscritos:", item.get("Inscritos"));
    }

    if (item.get("Tipo").equals("Evento")) {
        addDetail(detallesPanel, "Ubicación:", item.get("Ubicacion"));
        addDetail(detallesPanel, "Aforo:", item.get("Aforo"));
    }

    if (item.get("Tipo").equals("Curso")) {
        addDetail(detallesPanel, "Horario:", item.get("Horario"));
        addDetail(detallesPanel, "Entrenador:", item.get("Entrenador"));
    }

    card.add(detallesPanel);

    JTextArea desc = new JTextArea(item.get("Descripcion"));
    desc.setLineWrap(true);
    desc.setWrapStyleWord(true);
    desc.setEditable(false);
    desc.setBackground(Color.WHITE);
    desc.setFont(new Font("SansSerif", Font.PLAIN, 14));
    desc.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

    JScrollPane descScroll = new JScrollPane(desc);
    descScroll.setMaximumSize(new Dimension(750, 100));
    descScroll.setPreferredSize(null); // deja que la altura se adapte
    descScroll.setBorder(null);
    card.add(descScroll);

    if (!item.get("Tipo").equals("Publicacion")) {
        cargarComentarios(item.get("ID"), "Actividad", card);
    } else {
        cargarComentarios(item.get("ID"), "Publicacion", card);
    }

    // Centrado
    return card;
}



private void addDetail(JPanel panel, String label, String value) {
    JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
    rowPanel.setBackground(Color.WHITE);
    
    JLabel lbl = new JLabel("<html><b>" + label + "</b></html>");
    lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
    
    JLabel val = new JLabel(value != null ? value : "N/A");
    val.setFont(new Font("SansSerif", Font.PLAIN, 12));

    rowPanel.add(lbl);
    rowPanel.add(val);
    panel.add(rowPanel);
}
    private void actualizarVista() {
        mainContent.removeAll();
        
        if (allItems.isEmpty()) {
            JLabel lblEmpty = new JLabel("No hay contenido disponible");
            lblEmpty.setFont(new Font("SansSerif", Font.ITALIC, 16));
            mainContent.add(lblEmpty);
        } else {
            int inicio = currentPage * ITEMS_PER_PAGE;
            int fin = Math.min(inicio + ITEMS_PER_PAGE, allItems.size());

            for (int i = inicio; i < fin; i++) {
                JPanel card = crearCardContenido(allItems.get(i));
                mainContent.add(card);
                mainContent.add(Box.createVerticalStrut(10));
            }

            mainContent.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));


            // Actualiza botones
            btnAnterior.setEnabled(currentPage > 0);
            btnSiguiente.setEnabled((currentPage + 1) * ITEMS_PER_PAGE < allItems.size());
            btnAnterior.setEnabled(currentPage > 0);
            btnSiguiente.setEnabled((currentPage + 1) * ITEMS_PER_PAGE < allItems.size());
            int totalPages = (int) Math.ceil((double) allItems.size() / ITEMS_PER_PAGE);
            pageLabel.setText("Página " + (currentPage + 1) + " de " + totalPages);
        }

        mainContent.revalidate();
        mainContent.repaint();
    }


private void cargarComentarios(String idObjeto, String tipoObjeto, JPanel parentCard) {
    new Thread(() -> {
        try (Connection conn = dbConnection.conectar()) {
            // Declarar la variable como final dentro del ámbito del hilo
            final int[] comentarioCount = {0}; 

            // 1. Consulta para contar comentarios
            try (PreparedStatement countStmt = conn.prepareStatement(
                "SELECT COUNT(*) AS total FROM Comentario WHERE ID_Obj = ? AND Tipo_Objeto = ?")) {
                
                countStmt.setInt(1, Integer.parseInt(idObjeto));
                countStmt.setString(2, tipoObjeto);
                ResultSet countRs = countStmt.executeQuery();
                
                if (countRs.next()) {
                    comentarioCount[0] = countRs.getInt("total"); // Usar arreglo para evitar problema de scope
                }
            }

            // 2. Consulta para obtener detalles de comentarios
            try (PreparedStatement pstmt = conn.prepareStatement(
                "SELECT c.Contenido, u.Nombre_usuario, c.Fecha " +
                "FROM Comentario c " +
                "JOIN Usuario u ON c.ID_Usu = u.ID_Usu " +
                "WHERE c.ID_Obj = ? AND c.Tipo_Objeto = ? " +
                "ORDER BY c.Fecha DESC")) {
                
                pstmt.setInt(1, Integer.parseInt(idObjeto));
                pstmt.setString(2, tipoObjeto);
                ResultSet rs = pstmt.executeQuery();

                // Procesar resultados dentro del mismo ámbito
                List<Map<String, String>> comentarios = new ArrayList<>();
                while (rs.next()) {
                    Map<String, String> comentario = new HashMap<>();
                    comentario.put("usuario", rs.getString("Nombre_usuario"));
                    comentario.put("texto", rs.getString("Contenido"));
                    comentario.put("fecha", dateFormat.format(rs.getTimestamp("Fecha")));
                    comentarios.add(comentario);
                }

                // Actualizar UI con datos recolectados
                SwingUtilities.invokeLater(() -> {
                    JPanel comentariosPanel = new JPanel();
                    comentariosPanel.setLayout(new BoxLayout(comentariosPanel, BoxLayout.Y_AXIS));
                    comentariosPanel.setBorder(BorderFactory.createTitledBorder("Comentarios (" + comentarioCount[0] + ")"));
                    comentariosPanel.setBackground(new Color(245, 245, 245));

                    for (Map<String, String> comentario : comentarios) {
                        JPanel comentarioPanel = new JPanel(new BorderLayout());
                        comentarioPanel.setBackground(Color.WHITE);
                        comentarioPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

                        JLabel usuario = new JLabel(comentario.get("usuario"));
                        usuario.setFont(new Font("SansSerif", Font.BOLD, 12));

                        JLabel fecha = new JLabel(comentario.get("fecha"));
                        fecha.setFont(new Font("SansSerif", Font.ITALIC, 10));

                        JPanel header = new JPanel(new BorderLayout());
                        header.add(usuario, BorderLayout.WEST);
                        header.add(fecha, BorderLayout.EAST);

                        JTextArea contenido = new JTextArea(comentario.get("texto"));
                        contenido.setLineWrap(true);
                        contenido.setWrapStyleWord(true);
                        contenido.setEditable(false);
                        contenido.setBackground(Color.WHITE);

                        comentarioPanel.add(header, BorderLayout.NORTH);
                        comentarioPanel.add(contenido, BorderLayout.CENTER);
                        comentariosPanel.add(comentarioPanel);
                        comentariosPanel.add(Box.createRigidArea(new Dimension(0, 5)));
                    }

                    parentCard.add(comentariosPanel);
                    parentCard.revalidate();
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }).start();
}

}