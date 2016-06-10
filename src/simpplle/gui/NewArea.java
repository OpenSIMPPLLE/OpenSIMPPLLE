
package simpplle.gui;

import simpplle.JSimpplle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-0.9.  By copying this file the user accepts the University of Montana 
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p> A NewArea dialog prompts the user to select a type of area to load.
 *
 * <p> Original source code authorship: Kirk A. Moeller
 */
public class NewArea extends JDialog {

  private int choice;

  public static final int NONE         = -1;
  public static final int SAMPLE       =  0;
  public static final int USER_DEFINED =  1;
  public static final int PREVIOUS     =  2;
  public static final int PREVIOUS_OLD =  3;

  private JButton      okButton      = new JButton("Ok");
  private JButton      cancelButton  = new JButton("Cancel");
  private JRadioButton sampleAreaRB  = new JRadioButton("Select Sample Area");
  private JRadioButton prevAreaRB    = new JRadioButton("Load Previously Simulated Area (.simdata files)");
  private JRadioButton prevAreaOldRB = new JRadioButton("Load (old format) Simulated Area (.area files)");
  private JRadioButton userAreaRB    = new JRadioButton("Load User Defined Area");
  private JPanel       mainPanel     = new JPanel();
  private JPanel       buttonPanel   = new JPanel();
  private JPanel       radioPanel    = new JPanel();
  private GridLayout   gridLayout    = new GridLayout(4,1);
  private FlowLayout   flowLayout    = new FlowLayout();
  private BorderLayout borderLayout  = new BorderLayout();

  /**
   * Creates an area dialog with no owner and not modal.
   */
  public NewArea() {
    this(null, false);
  }

  /**
   * Creates an area dialog.
   * @param frame Owner of the dialog
   * @param modal If true, blocks input to other windows
   */
  public NewArea(Frame frame, boolean modal) {

    super(frame, "Please Choose an Option", modal);

    try  {
      jbInit();
      pack();
    } catch(Exception ex) {
      ex.printStackTrace();
    }

    initialize();

  }

  /**
   * Initializes the dialog with buttons, radio boxes, panels, and layouts.
   * @throws Exception
   */
  void jbInit() throws Exception {

    okButton.setMinimumSize(new Dimension(73, 27));
    okButton.setMaximumSize(new Dimension(73, 27));
    okButton.setPreferredSize(new Dimension(73, 27));
    okButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        selectOkButton();
      }
    });

    cancelButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        selectCancelButton();
      }
    });

    buttonPanel.setBorder(BorderFactory.createEtchedBorder());
    buttonPanel.setLayout(flowLayout);
    buttonPanel.add(okButton, null);
    buttonPanel.add(cancelButton, null);

    gridLayout.setColumns(1);
    gridLayout.setRows(4);

    radioPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
    radioPanel.setLayout(gridLayout);
    radioPanel.add(sampleAreaRB, null);
    radioPanel.add(userAreaRB, null);
    radioPanel.add(prevAreaRB, null);
    radioPanel.add(prevAreaOldRB, null);

    mainPanel.setLayout(borderLayout);
    mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    mainPanel.add(radioPanel, BorderLayout.NORTH);

    getContentPane().add(mainPanel);

  }

  /**
   * Initializes the new area dialog. The sample area radio button is enabled and selected if comcode has sample
   * areas. Otherwise the sample area button is disabled and the user area button is selected.
   */
  private void initialize() {

    choice = NONE;

    if (JSimpplle.getComcode().getSampleAreas() == null) {
      sampleAreaRB.setEnabled(false);
      userAreaRB.setSelected(true);
    } else {
      sampleAreaRB.setEnabled(true);
      sampleAreaRB.setSelected(true);
    }
  }

  /**
   * Returns the chosen area source as a numeric value. Possible values are -1 (NONE), 0 (SAMPLE), 1 (USER_DEFINED),
   * 2 (PREVIOUS), and 3 (PREVIOUS_OLD).
   * @return The chosen area source
   */
  public int getChoice() {
    return choice;
  }

  /**
   * Records the selected option and dispose of the dialog.
   */
  private void selectOkButton() {

    if (sampleAreaRB.isSelected()) {
      choice = SAMPLE;
    } else if (userAreaRB.isSelected()) {
      choice = USER_DEFINED;
    } else if (prevAreaRB.isSelected()) {
      choice = PREVIOUS;
    } else if (prevAreaOldRB.isSelected()) {
      choice = PREVIOUS_OLD;
    } else {
      choice = NONE;
    }

    setVisible(false);
    dispose();

  }

  /**
   * Records that no choice was made and dispose of the dialog.
   */
  private void selectCancelButton() {

    choice = NONE;

    setVisible(false);
    dispose();

  }
}
