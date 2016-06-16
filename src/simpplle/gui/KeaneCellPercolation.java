package simpplle.gui;

import javax.swing.*;
import java.awt.*;

/**
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p> This dialog allows a user to adjust parameters for the cell percolation spread algorithm developed by Robert
 * Keane, 2006. These parameters are used during simulation when the fire spread model is set to 'Keane'. Details about
 * the algorithm are published in RMRS-GTR-171CD.
 */

public class KeaneCellPercolation extends JDialog {

    private JPanel mainPanel   = new JPanel();
    private JPanel paramPanel  = new JPanel();
    private JPanel buttonPanel = new JPanel();

    private JButton okButton     = new JButton("Ok");
    private JButton cancelButton = new JButton("Cancel");

    private LabeledDoubleField extremeWindField = new LabeledDoubleField("Wind speed variability factor for extreme conditions");
    private LabeledDoubleField normalWindField  = new LabeledDoubleField("Wind speed variability factor for normal conditions");
    private LabeledDoubleField windDirField     = new LabeledDoubleField("Wind direction variability in degrees");

    public KeaneCellPercolation(Frame frame) {

        super(frame,"Keane Cell Percolation",true);

        try  {
            jbInit();
            pack();
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        initialize();

    }

    /**
     * Configures components in the dialog.
     * @throws Exception
     */
    private void jbInit() throws Exception {

        paramPanel.setLayout(new BoxLayout(paramPanel,BoxLayout.Y_AXIS));
        paramPanel.add(extremeWindField);
        paramPanel.add(normalWindField);
        paramPanel.add(windDirField);

        okButton.setMinimumSize(new Dimension(73, 27));
        okButton.setMaximumSize(new Dimension(73, 27));
        okButton.setPreferredSize(new Dimension(73, 27));
        okButton.addActionListener(e -> selectOk());

        cancelButton.setMinimumSize(new Dimension(73, 27));
        cancelButton.setMaximumSize(new Dimension(73, 27));
        cancelButton.setPreferredSize(new Dimension(73, 27));
        cancelButton.addActionListener(e -> selectCancel());

        buttonPanel.setBorder(BorderFactory.createEtchedBorder());
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));
        mainPanel.add(paramPanel);
        mainPanel.add(buttonPanel);

        getContentPane().add(mainPanel);

    }

    /**
     * Sets initial values in the dialog.
     */
    private void initialize() {

        extremeWindField.setValue(5.0);
        normalWindField.setValue(0.5);
        windDirField.setValue(45.0);

    }

    /**
     * Hides and disposes of the dialog.
     */
    private void selectOk() {

        setVisible(false);
        dispose();

    }

    /**
     * Hides and disposes of the dialog.
     */
    private void selectCancel() {

        setVisible(false);
        dispose();

    }

    /**
     * @return The wind speed variability factor for extreme conditions
     */
    public double getExtremeWindSpeedVariabilityFactor() {
        return extremeWindField.getValue();
    }

    /**
     * @return The wind speed variability factor for normal conditions
     */
    public double getNormalWindSpeedVariabilityFactor() {
        return normalWindField.getValue();
    }

    /**
     * @return The wind direction variability in degrees
     */
    public double getWindDirectionVariability() {
        return windDirField.getValue();
    }

}
