package net.centralfloridaattorney.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;

public class JPValueEditor extends JPanel {

    //
    private int height = 5;
    private JScrollPane jScrollPanel;
    private JTextArea jTextArea;
    //private String observedKey = ContextManager.VALUE_DEFAULT;
    private int width = 25;

    public String getText() {
        return jTextArea.getText();
    }

    public void setText(String text) {
        jTextArea.setText(text);
    }

    public void setColumns(int columns) {
        jTextArea.setColumns(columns);
    }

    public void update(Observable o, Object arg) {
        jTextArea.setText(arg.toString());
    }

    private void init(JPanel jpContent) {
        jTextArea = new JTextArea(height, width);
        jTextArea.setLineWrap(true);
        jTextArea.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                jTextArea.selectAll();
                jTextArea.setSelectionColor(Color.YELLOW);
            }
        });
        jScrollPanel = new JScrollPane(jTextArea);
        add(jScrollPanel, BorderLayout.CENTER);
    }

    public JPValueEditor() {
        super();
        //init();
    }


    public JPValueEditor(JPanel jpContent) {
        super();
        init(jpContent);

    }
}
