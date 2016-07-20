/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import simpplle.JSimpplle;
import simpplle.comcode.TreatmentType;
import simpplle.comcode.Simpplle;
import simpplle.comcode.RegionalZone;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/**
 * This class creates the New Treatment dialog, a type of JDialog.  This allows the user to select a new treatment from the comcode.
 *
 * @author Documentation by Brian Losi
 * <p>Original source authorship: Kirk A. Moeller
 */
public class NewTreatment extends JDialog {
  String treatmentName;
  private String protoCellValue = "ECOSYSTEM-MANAGEMENT-THIN-AND-UNDERBURN      ";

  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JLabel treatmentLabel = new JLabel();
  JList treatmentList = new JList();
  JPanel buttonPanel = new JPanel();
  JButton cancelPB = new JButton();
  JButton okPB = new JButton();
  FlowLayout flowLayout1 = new FlowLayout();
  JScrollPane treatmentListScroll = new JScrollPane();

  /**
   * Primary constructor for NewTreatment dialog.  Sets the frame that owns the dialog, the dialog title, and modality. 
   * @param frame owner of the dialog
   * @param title title of dialog
   * @param modal specifies whether dialog blocks user input to other top-level windows when shown
   */
  public NewTreatment(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try  {
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
    initialize();
  }
  /**
   * Overloaded constructor for NewSampleArea dialog.  References the primary constructor and sets owner to null, title to empty string and modality to modeless.
   */
  public NewTreatment() {
    this(null, "", false);
    initialize();
  }

  void jbInit() throws Exception {
    mainPanel.setLayout(borderLayout1);
    treatmentLabel.setFont(new java.awt.Font("Dialog", 1, 16));
    treatmentLabel.setHorizontalAlignment(SwingConstants.CENTER);
    treatmentLabel.setText("Pick a Treatment");
    buttonPanel.setLayout(flowLayout1);
    cancelPB.setText("Cancel");
    cancelPB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        cancelPB_actionPerformed(e);
      }
    });
    okPB.setMaximumSize(new Dimension(73, 27));
    okPB.setMinimumSize(new Dimension(73, 27));
    okPB.setPreferredSize(new Dimension(73, 27));
    okPB.setText("Ok");
    okPB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        okPB_actionPerformed(e);
      }
    });
    treatmentList.setBackground(Color.darkGray);
    treatmentList.setForeground(Color.white);
    treatmentList.setPrototypeCellValue(protoCellValue);
    treatmentList.setSelectionBackground(Color.white);
    treatmentList.setSelectionForeground(Color.orange);
    treatmentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    treatmentList.setVisibleRowCount(10);
    this.setResizable(false);
    getContentPane().add(mainPanel);
    mainPanel.add(treatmentLabel, BorderLayout.NORTH);
    mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    buttonPanel.add(okPB, null);
    buttonPanel.add(cancelPB, null);
    mainPanel.add(treatmentListScroll, BorderLayout.CENTER);
    treatmentListScroll.getViewport().add(treatmentList, null);
  }
/**
 * Initializes the NewTreatment dialog.  Sets the zone to the current zone and sets the treatment list choices to the zones legal treatments.  
 */
  private void initialize() {
    RegionalZone zone = Simpplle.getCurrentZone();

    treatmentName = null;
    treatmentList.setListData(zone.getLegalTreatments());
  }
/**
 * Gets the treatment name that the user selected from the treatment list.   
 * @return treatment name 
 */
  public String getSelection() {
    return treatmentName;
  }
/**
 * Handles when the user presses the 'OK' button.  If no treatment is selected sends a warning message to user.  Otherwise sets the treatment name
 * to the selected treatment.  Then disposes of the dialog.  
 * @param e
 */
  void okPB_actionPerformed(ActionEvent e) {
    if (treatmentList.isSelectionEmpty()) {
      JOptionPane.showMessageDialog(this,"Please select a Treatment",
                                    "No Treatment Selected.",
                                    JOptionPane.WARNING_MESSAGE);
      return;
    }

    treatmentName = ((TreatmentType)treatmentList.getSelectedValue()).toString();
    setVisible(false);
    dispose();
  }
/**
 * If user 'Cancel' the new treatment dialog, the new treatment is disposed and set to not visible.  
 * @param e
 */
  void cancelPB_actionPerformed(ActionEvent e) {
    setVisible(false);
    dispose();
  }

}
