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

    private double extremeWindSpeedVariabilityFactor;
    private double normalWindSpeedVariabilityFactor;
    private double windDirectionVariability;

    private JPanel     mainPanel        = new JPanel();
    private JPanel     paramPanel       = new JPanel();
    private JPanel     buttonPanel      = new JPanel();
    private JButton    okButton         = new JButton("Ok");
    private JButton    cancelButton     = new JButton("Cancel");
    private JLabel     extremeWindLabel = new JLabel("Wind speed variability factor for extreme conditions");
    private JLabel     normalWindLabel  = new JLabel("Wind speed variability factor for normal conditions");
    private JLabel     windDirLabel     = new JLabel("Wind direction variability in degrees");
    private JTextField extremeWindField = new JTextField(6);
    private JTextField normalWindField  = new JTextField(6);
    private JTextField windDirField     = new JTextField(6);


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

        // TODO: Add a DoubleTextField class that enforces floating-point input

        // TODO: Add a LabeledDoubleTextField class that adds a label next to the field (OpenSIMPPLLE's expects integers..)

        extremeWindField.setHorizontalAlignment(SwingConstants.RIGHT);

        normalWindField.setHorizontalAlignment(SwingConstants.RIGHT);

        windDirField.setHorizontalAlignment(SwingConstants.RIGHT);

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row1.add(extremeWindField);
        row1.add(extremeWindLabel);

        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row2.add(normalWindField);
        row2.add(normalWindLabel);

        JPanel row3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row3.add(windDirField);
        row3.add(windDirLabel);

        paramPanel.setLayout(new BoxLayout(paramPanel,BoxLayout.Y_AXIS));
        paramPanel.add(row1);
        paramPanel.add(row2);
        paramPanel.add(row3);

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
     * @return The wind speed variability factor for extreme conditions
     */
    public double getExtremeWindSpeedVariabilityFactor() {
        return extremeWindSpeedVariabilityFactor;
    }

    /**
     * @return The wind speed variability factor for normal conditions
     */
    public double getNormalWindSpeedVariabilityFactor() {
        return normalWindSpeedVariabilityFactor;
    }

    /**
     * @return The wind direction variability in degrees
     */
    public double getWindDirectionVariability() {
        return windDirectionVariability;
    }

    /**
     * Sets initial values in the dialog.
     */
    private void initialize() {

        extremeWindField.setText("4.0");
        normalWindField.setText("0.5");
        windDirField.setText("45.0");

    }

    /**
     * Hides and disposes of the dialog after recording the parameters.
     */
    private void selectOk() {

        // TODO: Store the floating-point values in each field

        setVisible(false);
        dispose();

    }

    /**
     * Hides and disposes of the dialog.
     */
    private void selectCancel() {

        extremeWindSpeedVariabilityFactor = 0.0;
        normalWindSpeedVariabilityFactor = 0.0;
        windDirectionVariability   = 0.0;

        setVisible(false);
        dispose();

    }
}
