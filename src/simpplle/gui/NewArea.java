

package simpplle.gui;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;


import simpplle.JSimpplle;
import javax.swing.*;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-0.9.  By copying this file the user accepts the University of Montana 
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 *<p> This class creates the New Area dialog.  This allows the user to define the type of area they want.  
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *      
 */
public class NewArea extends JDialog {
  private simpplle.comcode.Simpplle comcode;
  private int choice;
  public static final int NONE         = -1;
  public static final int SAMPLE       = 0;
  public static final int USER_DEFINED = 1;
  public static final int PREVIOUS     = 2;
  public static final int PREVIOUS_OLD = 3;

  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel buttonPanel = new JPanel();
  JButton okButton = new JButton();
  JButton cancelButton = new JButton();
  FlowLayout flowLayout1 = new FlowLayout();
  JPanel radioPanel = new JPanel();
  JRadioButton sampleAreaRB = new JRadioButton();
  JRadioButton prevAreaRB = new JRadioButton();
  JRadioButton userAreaRB = new JRadioButton();
  GridLayout gridLayout1 = new GridLayout();
  JPanel radioIndentPanel = new JPanel();
  FlowLayout flowLayout2 = new FlowLayout();
  JRadioButton prevAreaOldRB = new JRadioButton();
/**
 * Constructor for NewArea dialog.  Sets the frame that owns the dialog, dialog title, and modality. 
 * @param frame owner of the dialog
 * @param title title of dialog
 * @param modal specifies whether dialog blocks user input to other top-level windows when shown
 */
  public NewArea(Frame frame, String title, boolean modal) {
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
 * Overloaded constructor for NewArea dialog.  References the primary constructor and sets owner to null, title to empty string and modality to modeless.
 */
  public NewArea() {
    this(null, "", false);
  }
/**
 * Initializes the new area dialog with panels, components, titles, and layouts.  
 * @throws Exception
 */
  void jbInit() throws Exception {
    mainPanel.setLayout(borderLayout1);
    buttonPanel.setLayout(flowLayout1);
    okButton.setMaximumSize(new Dimension(73, 27));
    okButton.setMinimumSize(new Dimension(73, 27));
    okButton.setNextFocusableComponent(cancelButton);
    okButton.setPreferredSize(new Dimension(73, 27));
    okButton.setText("Ok");
    okButton.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        okButton_actionPerformed(e);
      }
    });
    cancelButton.setNextFocusableComponent(sampleAreaRB);
    cancelButton.setText("Cancel");
    cancelButton.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        cancelButton_actionPerformed(e);
      }
    });
    radioPanel.setLayout(gridLayout1);
    sampleAreaRB.setSelected(true);
    sampleAreaRB.setNextFocusableComponent(userAreaRB);
    sampleAreaRB.setText("Select Sample Area");
    prevAreaRB.setNextFocusableComponent(okButton);
    prevAreaRB.setText("Load Previously Simulated Area (.simdata files)");
    userAreaRB.setNextFocusableComponent(prevAreaRB);
    userAreaRB.setText("Load User Defined Area");
    this.setTitle("Please Choose an Option");
    buttonPanel.setBorder(BorderFactory.createEtchedBorder());
    gridLayout1.setColumns(1);
    gridLayout1.setRows(4);
    radioIndentPanel.setLayout(flowLayout2);
    flowLayout2.setAlignment(FlowLayout.LEFT);
    flowLayout2.setHgap(10);
    radioPanel.setBorder(BorderFactory.createEtchedBorder());
    prevAreaOldRB.setText("Load (old format) Simulated Area (.area files)");
    getContentPane().add(mainPanel);
    mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    buttonPanel.add(okButton, null);
    buttonPanel.add(cancelButton, null);
    mainPanel.add(radioIndentPanel, BorderLayout.NORTH);
    radioIndentPanel.add(radioPanel, null);
    radioPanel.add(sampleAreaRB, null);
    radioPanel.add(userAreaRB, null);
    radioPanel.add(prevAreaRB, null);
    radioPanel.add(prevAreaOldRB, null);

    // Place radio buttons in a group
    ButtonGroup group = new ButtonGroup();
    group.add(sampleAreaRB);
    group.add(userAreaRB);
    group.add(prevAreaRB);
    group.add(prevAreaOldRB);
  }
/**
 * Initializes the new area dialog.  If the comcode has sample areas, enables and selects the sample area radio button, otherwise dis-enables the 
 * sample area radio button and enables the user area one.  
 */
  private void initialize() {
    comcode = JSimpplle.getComcode();
    choice  = NONE;

    if (JSimpplle.getComcode().getSampleAreas() == null) {
      sampleAreaRB.setEnabled(false);
      userAreaRB.setSelected(true);
    }
    else {
      sampleAreaRB.setEnabled(true);
      sampleAreaRB.setSelected(true);
    }


  }
/**
 * Gets the value of choice variable. Possible int values for choice are  NONE = -1, SAMPLE = 0 USER_DEFINED = 1,PREVIOUS = 2, PREVIOUS_OLD = 3
 * @return current integer value of choice.  (initialized to NONE = -1)
 */
  public int getChoice() { return choice; }
/**
 * Handles user pressing 'OK' button. Sets the choice variable for this new area object depending on which radiobutton is selected.  
 * Int values for choice are  default = NONE = -1; sampleAreaRB = SAMPLE = 0; userAreaRB = USER_DEFINED = 1; prevAreaRB = PREVIOUS = 2; prevAreaOldRB = PREVIOUS_OLD = 3
 * @param e
 */
  void okButton_actionPerformed(ActionEvent e) {
    if (sampleAreaRB.isSelected()) {
      choice = SAMPLE;
    }
    else if (userAreaRB.isSelected()) {
      choice = USER_DEFINED;
    }
    else if (prevAreaRB.isSelected()) {
      choice = PREVIOUS;
    }
    else if (prevAreaOldRB.isSelected()) {
      choice = PREVIOUS_OLD;
    }
    else {
      choice = NONE;
    }
    setVisible(false);
    dispose();
  }
/**
 * Handles user pressing 'cancel' button.  Sets choice to NONE which = -1 and disposes the dialog. 
 * @param e
 */
  void cancelButton_actionPerformed(ActionEvent e) {
    choice = NONE;
    setVisible(false);
    dispose();
  }
}
