package components.panels;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.time.*;
import java.time.format.*;
import java.util.Locale;

public class CalendarPanel extends JPanel {
    private YearMonth currentYearMonth;
    private final JLabel monthYearLabel;
    private final JPanel daysPanel;
    private final JPanel weekDaysPanel;

    public CalendarPanel(Runnable onVolver) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        this.currentYearMonth = YearMonth.now();

        // Configuración de cabecera mejorada
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.decode("#4A306D"));
        headerPanel.setBorder(new EmptyBorder(15, 25, 15, 25));

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);

        JLabel titleLabel = new JLabel("SportBook");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);
        topBar.add(titleLabel, BorderLayout.WEST);

        this.monthYearLabel = new JLabel("", SwingConstants.CENTER);
        monthYearLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        monthYearLabel.setForeground(Color.WHITE);

        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        navPanel.setOpaque(false);

        JButton prevBtn = createNavButton("◀", "#6A4C93");
        prevBtn.addActionListener(e -> changeMonth(-1));

        JButton todayBtn = createNavButton("HOY", "#6A4C93");
        todayBtn.addActionListener(e -> {
            currentYearMonth = YearMonth.now();
            updateCalendar();
        });

        JButton nextBtn = createNavButton("▶", "#6A4C93");
        nextBtn.addActionListener(e -> changeMonth(1));

        navPanel.add(prevBtn);
        navPanel.add(todayBtn);
        navPanel.add(nextBtn);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(monthYearLabel, BorderLayout.NORTH);
        centerPanel.add(navPanel, BorderLayout.SOUTH);
        topBar.add(centerPanel, BorderLayout.CENTER);
        headerPanel.add(topBar, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Panel de días de la semana MEJORADO y ALINEADO
        this.weekDaysPanel = new JPanel(new GridLayout(1, 7));
        weekDaysPanel.setBackground(Color.decode("#4A306D"));
        weekDaysPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        weekDaysPanel.setPreferredSize(new Dimension(0, 40));

        // Corrección ortográfica: "MIÉRCOLES" y "SÁBADO" (antes "SARADO")
        String[] days = {"DOMINGO", "LUNES", "MARTES", "MIÉRCOLES", "JUEVES", "VIERNES", "SÁBADO"};
        for (String day : days) {
            JLabel label = new JLabel(day, SwingConstants.CENTER);
            label.setFont(new Font("SansSerif", Font.BOLD, 12));
            label.setForeground(Color.WHITE);
            weekDaysPanel.add(label);
        }

        // Panel de días del mes MEJORADO
        this.daysPanel = new JPanel(new GridLayout(0, 7, 0, 0)) {
            @Override
            public Dimension getPreferredSize() {
                int cellSize = 100; // Tamaño uniforme para celdas
                int rows = 6; // Siempre mostramos 6 filas para consistencia
                return new Dimension(7 * cellSize, rows * cellSize);
            }
        };
        daysPanel.setBackground(Color.WHITE);
        daysPanel.setBorder(new EmptyBorder(0, 0, 0, 0));

        // Contenedor principal con Scroll MEJORADO
        JPanel calendarContainer = new JPanel(new BorderLayout());
        calendarContainer.setBackground(Color.WHITE);
        calendarContainer.add(weekDaysPanel, BorderLayout.NORTH);
        calendarContainer.add(daysPanel, BorderLayout.CENTER);
        
        JScrollPane scrollPane = new JScrollPane(calendarContainer);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        // Panel contenedor para centrar el calendario
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(Color.WHITE);
        centerWrapper.add(scrollPane);
        
        add(centerWrapper, BorderLayout.CENTER);

        // Botón Volver MEJORADO
        JButton backBtn = createNavButton("VOLVER AL MENÚ", "#6A4C93");
        backBtn.addActionListener(e -> onVolver.run());
        backBtn.setPreferredSize(new Dimension(200, 45));
        
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(Color.WHITE);
        footerPanel.setBorder(new EmptyBorder(15, 0, 0, 0));
        footerPanel.add(backBtn);
        add(footerPanel, BorderLayout.SOUTH);

        updateCalendar();
    }

    private JButton createNavButton(String text, String color) {
        JButton button = new JButton(text);
        button.setBackground(Color.decode(color));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.decode("#3A2053"), 2),
            new EmptyBorder(10, 25, 10, 25)
        ));
        return button;
    }

    private void changeMonth(int direction) {
        currentYearMonth = direction > 0
                ? currentYearMonth.plusMonths(1)
                : currentYearMonth.minusMonths(1);
        updateCalendar();
    }

    private void updateCalendar() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy", new Locale("es", "ES"));
        monthYearLabel.setText(currentYearMonth.format(formatter).toUpperCase());

        daysPanel.removeAll();
        daysPanel.setLayout(new GridLayout(0, 7, 2, 2));

        LocalDate firstOfMonth = currentYearMonth.atDay(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue() % 7; // 0=DOM, 1=LUN, ..., 6=SÁB
        int daysInMonth = currentYearMonth.lengthOfMonth();
        LocalDate today = LocalDate.now();

        // Días vacíos al inicio
        for (int i = 0; i < dayOfWeek; i++) {
            daysPanel.add(createDayLabel(""));
        }

        // Días del mes
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = currentYearMonth.atDay(day);
            daysPanel.add(createDayButton(String.valueOf(day), date.equals(today)));
        }

        // Días vacíos al final (completar 6 semanas)
        int totalCells = dayOfWeek + daysInMonth;
        int remainingCells = 42 - totalCells;
        
        for (int i = 0; i < remainingCells; i++) {
            daysPanel.add(createDayLabel(""));
        }

        daysPanel.revalidate();
        daysPanel.repaint();
    }

    private JLabel createDayLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(100, 100);
            }
        };
        label.setFont(new Font("SansSerif", Font.PLAIN, 16));
        label.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Color.LIGHT_GRAY, 1),
            new EmptyBorder(8, 8, 8, 8)
        ));
        label.setOpaque(true);
        label.setBackground(Color.WHITE);
        label.setForeground(Color.BLACK);
        return label;
    }

    private JButton createDayButton(String day, boolean isToday) {
        JButton button = new JButton(day) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(100, 100);
            }
        };
        button.setFont(new Font("SansSerif", Font.PLAIN, 16));
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setVerticalAlignment(SwingConstants.CENTER);
        button.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(isToday ? Color.decode("#4A306D") : Color.LIGHT_GRAY, isToday ? 2 : 1),
            new EmptyBorder(8, 8, 8, 8)
        ));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);

        if (isToday) {
            button.setBackground(Color.decode("#6A4C93"));
            button.setForeground(Color.WHITE);
        } else {
            button.setBackground(Color.WHITE);
            button.setForeground(Color.BLACK);
        }

        button.addActionListener(e -> {
            int selectedDay = Integer.parseInt(day);
            LocalDate selectedDate = currentYearMonth.atDay(selectedDay);

            JOptionPane.showMessageDialog(
                this,
                "Has seleccionado el día: " + selectedDate.format(
                        DateTimeFormatter.ofPattern("EEEE d 'de' MMMM yyyy", new Locale("es", "ES"))),
                "Día seleccionado",
                JOptionPane.INFORMATION_MESSAGE
            );
        });

        return button;
    }
}