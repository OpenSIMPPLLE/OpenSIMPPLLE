package simpplle.gui;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import simpplle.JSimpplle;
/**
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class creates the Pathway Chooser dialog, a type of JDialog.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *  
 */

public class PathwayChooser extends JDialog {
  private simpplle.comcode.Simpplle comcode;
  private int choice;
  public static final int NONE     = -1;
  public static final int DEFAULT  = 0;
  public static final int HISTORIC = 1;
  public static final int USER     = 2;

  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel buttonPanel = new JPanel();
  JButton okButton = new JButton();
  JButton cancelButton = new JButton();
  FlowLayout flowLayout1 = new FlowLayout();
  JPanel radioPanel = new JPanel();
  JRadioButton defaultRB = new JRadioButton();
  JRadioButton userPathwayRB = new JRadioButton();
  JRadioButton historicRB = new JRadioButton();
  GridLayout gridLayout1 = new GridLayout();
  JPanel radioIndentPanel = new JPanel();
  FlowLayout flowLayout2 = new FlowLayout();
  /**
   * Constructor for Pathway Chooser.  Sets the JDialog owner, 
   * title, and modality as well as the logic data to be used. 
   * @param owner jdialog that owners this dialog
   * @param title title of this dialog
   * @param modal specifies whether dialog blocks user input to other top-level windows when shown
   */
  public PathwayChooser(Frame frame, String title, boolean modal) {
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
   * Overloaded constructor for Pathway Chooser dialog.  Sets the name to Pathway Chooser and owner to new dialog.    
   */
  public PathwayChooser() {
    this(null, "", false);
  }
  /**
   * Initializes the Pathway Chooser dialog with layouts, text, components, borders, and panels
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
    cancelButton.setNextFocusableComponent(defaultRB);
    cancelButton.setText("Cancel");
    cancelButton.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        cancelButton_actionPerformed(e);
      }
    });
    radioPanel.setLayout(gridLayout1);
    defaultRB.setSelected(true);
    defaultRB.setNextFocusableComponent(historicRB);
    defaultRB.setText("Load Default Pathway Files.");
    historicRB.setNextFocusableComponent(userPathwayRB);
    historicRB.setText("Load Historic Pathway Files.");
    userPathwayRB.setNextFocusableComponent(okButton);
    userPathwayRB.setText("Load a User Pathway File.");
    this.setTitle("Please Choose an Option");
    buttonPanel.setBorder(BorderFactory.createEtchedBorder());
    gridLayout1.setColumns(1);
    gridLayout1.setRows(3);
    radioIndentPanel.setLayout(flowLayout2);
    flowLayout2.setAlignment(FlowLayout.LEFT);
    flowLayout2.setHgap(10);
    radioPanel.setBorder(BorderFactory.createEtchedBorder());
    getContentPane().add(mainPanel);
    mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    buttonPanel.add(okButton, null);
    buttonPanel.add(cancelButton, null);
    mainPanel.add(radioIndentPanel, BorderLayout.NORTH);
    radioIndentPanel.add(radioPanel, null);
    radioPanel.add(defaultRB, null);
    radioPanel.add(historicRB, null);
    radioPanel.add(userPathwayRB, null);

    // Place radio buttons in a group
    ButtonGroup group = new ButtonGroup();
    group.add(defaultRB);
    group.add(historicRB);
    group.add(userPathwayRB);
  }
/**
 * Makes an instance of the comcode. Then initializes the pathways to  NONE = -1
 * Choices for the pathway are NONE = -1; DEFAULT  = 0; HISTORIC = 1; USER = 2;
 */
  private void initialize() {
    comcode = JSimpplle.getComcode();
    choice  = NONE;
  }
/**
 * Choices for the pathway are NONE = -1; DEFAULT  = 0; HISTORIC = 1; USER = 2;
 * @return NONE = -1; DEFAULT  = 0; HISTORIC = 1; USER = 2;
 */
  public int getChoice() { return choice; }
/**
 * When 'OK' button is pressed.  Sets the choice variable (NONE = -1; DEFAULT  = 0; HISTORIC = 1; USER = 2;) to the radiobutton selected.  
 * Then disposes the Pathways chooser.   
 * @param e 'OK'
 */
  void okButton_actionPerformed(ActionEvent e) {
    if (defaultRB.isSelected()) {
      choice = DEFAULT;
    }
    else if (historicRB.isSelected()) {
      choice = HISTORIC;
    }
    else if (userPathwayRB.isSelected()) {
      choice = USER;
    }
    else {
      choice = NONE;
    }
    setVisible(false);
    dispose();
  }
/**
 * When 'Cancel' button is pushed, sets the choice to default value of NONE.  
 * @param e 'Cancel'
 */
  void cancelButton_actionPerformed(ActionEvent e) {
    choice = NONE;
    setVisible(false);
    dispose();
  }
}
