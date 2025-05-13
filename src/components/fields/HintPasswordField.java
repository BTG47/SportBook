package components.fields;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import components.borders.RoundedBorder;

public class HintPasswordField extends JPasswordField {
    private final String hint;
    private boolean showingHint;

    public HintPasswordField(String hint) {
        super(hint);
        this.hint = hint;
        this.showingHint = true;
        setForeground(Color.GRAY);
        setEchoChar((char) 0);
        setBorder(new components.borders.RoundedBorder(15));

        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (showingHint) {
                    setText("");
                    setEchoChar('•');
                    setForeground(Color.BLACK);
                    showingHint = false;
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getPassword().length == 0) {
                    setText(hint);
                    setEchoChar((char) 0);
                    setForeground(Color.GRAY);
                    showingHint = true;
                }
            }
        });
    }

    @Override
    public char[] getPassword() {
        return showingHint ? new char[0] : super.getPassword();
    }
}