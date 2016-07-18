
package simpplle.gui;

import simpplle.JSimpplle;

import javax.swing.*;
import java.awt.*;

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

  private JButton      cancelButton  = new JButton("Cancel");
  private JButton sampleArea = new JButton("Select Sample Area");
  private JButton userArea = new JButton("Load User Defined Area");
  private JButton prevArea = new JButton("Load .simdata files");
  private JButton prevAreaOld = new JButton("Load .area files");
  private JPanel       mainPanel     = new JPanel();
  private JPanel       buttonPanel   = new JPanel();
  private JPanel       selectionPanel    = new JPanel();
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

    sampleArea.setToolTipText("Click to select a sample area.");
    sampleArea.addActionListener(e1 -> makeSelection(SAMPLE));
    userArea.setToolTipText("Click to select a user defined area.");
    userArea.addActionListener(e1 -> makeSelection(USER_DEFINED));
    prevArea.setToolTipText("Load previously simulated Area files.");
    prevArea.addActionListener(e1 -> makeSelection(PREVIOUS));
    prevAreaOld.setToolTipText("Load old format simulated Area files.");
    prevAreaOld.addActionListener(e1 -> makeSelection(PREVIOUS_OLD));

    cancelButton.addActionListener(e -> selectCancelButton());
    buttonPanel.setBorder(BorderFactory.createEtchedBorder());
    buttonPanel.setLayout(flowLayout);
    buttonPanel.add(cancelButton, null);

    gridLayout.setColumns(1);
    gridLayout.setRows(4);

    selectionPanel.setBorder(BorderFactory.createEmptyBorder(10,40,10,40));
    selectionPanel.setLayout(gridLayout);
    selectionPanel.add(sampleArea, null);
    selectionPanel.add(userArea, null);
    selectionPanel.add(prevArea, null);
    selectionPanel.add(prevAreaOld, null);

    mainPanel.setLayout(borderLayout);
    mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    mainPanel.add(selectionPanel, BorderLayout.NORTH);

    getContentPane().add(mainPanel);

  }

  /**
   * Initializes the new area dialog. The sample area radio button is enabled and selected if comcode has sample
   * areas. Otherwise the sample area button is disabled and the user area button is selected.
   */
  private void initialize() {

    choice = NONE;

    if (JSimpplle.getComcode().getSampleAreas() == null) {
      sampleArea.setEnabled(false);
      userArea.setSelected(true);
    } else {
      sampleArea.setEnabled(true);
      sampleArea.setSelected(true);
    }
  }

  /**
   * Returns the chosen area source as a numeric value. Possible values are
   * -1 (NONE),
   *  0 (SAMPLE),
   *  1 (USER_DEFINED),
   *  2 (PREVIOUS),
   *  3 (PREVIOUS_OLD).
   * @return The chosen area source
   */
  public int getChoice() {
    return choice;
  }

  /**
   * Records the selected option and dispose of the dialog.
   */
  private void makeSelection(int selection){
    choice = selection;
    setVisible(false);
    dispose();
  }

  /**
   * Records that no choice was made and dispose of the dialog.
   */
  private void selectCancelButton() {
    setVisible(false);
    dispose();
  }
}
