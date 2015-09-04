package simpplle.gui;

import simpplle.JSimpplle;
import simpplle.comcode.SimpplleError;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import simpplle.comcode.InclusionRuleSpecies;
/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 *<p> This class creates the New Sample Area dialog, a type of JDialog.  This allows the user to select a sample area
 *from the comcode.    
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *  *     
 */

public class NewSampleArea extends JDialog {
  JPanel panel1 = new JPanel();
  JList theList = new JList();
  JButton OkButton = new JButton();
  JPanel buttonPanel = new JPanel();
  FlowLayout flowLayout2 = new FlowLayout();
  BorderLayout borderLayout1 = new BorderLayout();
  JButton CancelButton = new JButton();

  private SimpplleMain              simpplleMain;
  private simpplle.comcode.Simpplle comcode;

  /**
   * Primary constructor for NewSampleArea dialog.  Sets the frame that owns the dialog to the main frame for Simpplle, dialog title, and modality. 
   * @param frame owner of the dialog
   * @param title title of dialog
   * @param modal specifies whether dialog blocks user input to other top-level windows when shown
   */
  public NewSampleArea(SimpplleMain frame, String title, boolean modal) {
    super(frame, title, modal);
    simpplleMain = frame;
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
  public NewSampleArea() {
    this(null, "", false);
  }
  /**
   * Initializes the new sample area dialog with panels, components, titles, and layouts.  
   * @throws Exception
   */
  void jbInit() throws Exception {
    panel1.setLayout(borderLayout1);
    theList.setBackground(Color.darkGray);
    theList.setForeground(Color.white);
    theList.setBorder(BorderFactory.createRaisedBevelBorder());
    theList.setMinimumSize(new Dimension(161, 200));
    theList.setPreferredSize(new Dimension(161, 200));
    theList.setToolTipText("Please select an Area.");
    theList.setSelectionBackground(Color.white);
    theList.setSelectionForeground(Color.orange);
    theList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    theList.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        theList_mouseClicked(e);
      }
    });
    OkButton.setMinimumSize(new Dimension(73, 27));
    OkButton.setPreferredSize(new Dimension(73, 27));
    OkButton.setText("Ok");
    OkButton.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        OkButton_actionPerformed(e);
      }
    });
    buttonPanel.setLayout(flowLayout2);
    CancelButton.setText("Cancel");
    CancelButton.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        CancelButton_actionPerformed(e);
      }
    });
    panel1.setMinimumSize(new Dimension(300, 237));
    panel1.setPreferredSize(new Dimension(300, 237));
    getContentPane().add(panel1);
    panel1.add(buttonPanel, BorderLayout.SOUTH);
    buttonPanel.add(OkButton, null);
    buttonPanel.add(CancelButton, null);
    panel1.add(theList, BorderLayout.NORTH);
    this.setResizable(false);
  }
/**
 * Initializes the logic for the jdialog by getting the comcodes sample areas for the currently loaded zone.  
 */
  private void initialize() {
    comcode = JSimpplle.getComcode();

    theList.setListData(comcode.getSampleAreas());
  }
/**
 * If none of the list of sample areas are selected sends an error message to the user.  
 * If there is one selected loads the sample area, then disposes of the new sample area dialog.  
 */
  private void ok() {
    if (theList.isSelectionEmpty()) {
      JOptionPane.showMessageDialog(this,"Please select an Area",
                                    "No Area Selected.",
                                    JOptionPane.WARNING_MESSAGE);
      return;
    }
    loadSampleArea();
    setVisible(false);
    dispose();
  }
/**
 * Cancels the dialog in the normal way by setting the visibility to false and disposing it.  
 */
  private void cancel() {
    setVisible(false);
    dispose();
  }
/**
 * Loads sample area for the current zone from the comcode that the user selected.  Sends a message to the user to reassure them that 
 * the selected sample area is loading.  Then tries to load it.  If there is a problem sends an error message to the user and sets the cursor to the
 * default cursor.   
 */
  private void loadSampleArea() {
    simpplle.comcode.Area area =
      (simpplle.comcode.Area) theList.getSelectedValue();

    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

    simpplleMain.setStatusMessage("Loading Area: " + area.getName() + " ...");
    simpplleMain.refresh();
    try {
      comcode.loadSampleArea(area);
    }
    catch (SimpplleError err) {
      JOptionPane.showMessageDialog(this,err.getMessage(),"Error",
                                    JOptionPane.ERROR_MESSAGE);
    }

    simpplleMain.clearStatusMessage();

    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
  }


  // **** Events ****
  // ****************
/**
 * If ok button pushed, loads the sample area if one is selected (sends error if none selected), then disposes of the new sample area dialog.
 * @param e 'OK'
 */
  void OkButton_actionPerformed(ActionEvent e) {
    ok();
  }
/**
 * If user presses the 'Cancel' button, the new sample area dialog is disposed.   
 * @param e 'Cancel'
 */
  void CancelButton_actionPerformed(ActionEvent e) {
    cancel();
  }
/**
 * Handles a mouse click within the list of sample areas.  Loads the user selected area from the comcode and disposes the new sample area dialog.  
 * @param e
 */
  void theList_mouseClicked(MouseEvent e) {
    if (e.getClickCount() == 2 && theList.isSelectionEmpty() == false) {
      loadSampleArea();
      setVisible(false);
      dispose();
    }
  }
}

