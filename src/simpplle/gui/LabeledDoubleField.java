/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.NumberFormat;

/**
 * LabeledDoubleField is a labeled text field that enforces entry of floating-point values.
 */

public class LabeledDoubleField extends JPanel {

    private JFormattedTextField field;
    private Label label;

    /**
     * Creates a new instance with the provided string and an initial value of zero.
     * @param text Label text
     */
    public LabeledDoubleField(String text) {

        this(text,0.0);

    }

    /**
     * Creates a new instance with the provided string and initial value.
     * @param text Label text
     * @param initialValue Initial field value
     */
    public LabeledDoubleField(String text, double initialValue) {

        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMinimumFractionDigits(1);

        field = new JFormattedTextField(format);
        field.setColumns(6);
        field.setValue(initialValue);
        field.setHorizontalAlignment(SwingConstants.RIGHT);
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        field.selectAll();
                    }
                });
            }
        });

        label = new Label(text);

        setLayout(new FlowLayout(FlowLayout.LEFT));

        add(field);
        add(label);

    }

    public void setLabel(String text) {

        label.setText(text);

    }

    public String getLabel() {

        return label.getText();

    }

    public void setValue(double value) {

        field.setValue(value);

    }

    public double getValue() {

        return Double.parseDouble(field.getText());

    }
}
