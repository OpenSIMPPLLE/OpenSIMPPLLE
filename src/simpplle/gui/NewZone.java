
package simpplle.gui;

import  simpplle.JSimpplle;
import  simpplle.comcode.ValidZones;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 *<p> This class creates the New Area dialog.  This allows the user to define the type of area they want.  
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller  
 */

public class NewZone extends JDialog {
  private boolean historic;
  private boolean newZone;

  JPanel mainPanel = new JPanel();
  JList theList = new JList();
  JButton OkButton = new JButton();
  JPanel southPanel = new JPanel();
  FlowLayout flowLayout2 = new FlowLayout();
  BorderLayout borderLayout1 = new BorderLayout();
  JButton CancelButton = new JButton();

  private SimpplleMain              simpplleMain;
  private simpplle.comcode.Simpplle comcode;
  JPanel northPanel = new JPanel();
  JCheckBox historicCB = new JCheckBox();
  BorderLayout borderLayout2 = new BorderLayout();
  JPanel historicPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();

  private final JScrollPane scrollPane = new JScrollPane();
  /**
   * Primary constructor for NewZone dialog.  Sets the frame that owns the dialog to the main frame for Simpplle, dialog title, and modality. 
   * @param frame owner of the dialog
   * @param title title of dialog
   * @param modal specifies whether dialog blocks user input to other top-level windows when shown
   */
  public NewZone(SimpplleMain frame, String title, boolean modal) {
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
   * Overloaded constructor for NewZone dialog.  References the primary constructor and sets owner to null, title to empty string and modality to modeless.
   * Two important booleans are set - historic and newzone, both inizialized to false.  
   */
  public NewZone() {
    this(null, "", false);
    historic = false;
    newZone  = false;
  }
  /**
   * Initializes the new zone dialog with panels, components, titles, and layouts.  
   * @throws Exception
   */
  void jbInit() throws Exception {
    mainPanel.setLayout(borderLayout1);
    OkButton.setMaximumSize(new Dimension(73, 27));
    OkButton.setMinimumSize(new Dimension(73, 27));
    OkButton.setPreferredSize(new Dimension(73, 27));
    OkButton.setMargin(new Insets(2, 14, 2, 14));
    OkButton.setText("Ok");
    OkButton.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        OkButton_actionPerformed(e);
      }
    });
    southPanel.setLayout(flowLayout2);
    CancelButton.setText("Cancel");
    CancelButton.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        CancelButton_actionPerformed(e);
      }
    });
    northPanel.setLayout(borderLayout2);
    historicCB.setToolTipText("load historic pathways instead of the default pathways");
    historicCB.setText("Use Historic Pathways");
    historicCB.addItemListener(new java.awt.event.ItemListener() {

      public void itemStateChanged(ItemEvent e) {
        historicCB_itemStateChanged(e);
      }
    });
    historicPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    flowLayout1.setHgap(10);
    southPanel.setBorder(BorderFactory.createEtchedBorder());
    getContentPane().add(mainPanel);
    mainPanel.add(southPanel, BorderLayout.SOUTH);
    southPanel.add(OkButton, null);
    southPanel.add(CancelButton, null);
    mainPanel.add(northPanel);
    northPanel.add(historicPanel, BorderLayout.SOUTH);
    historicPanel.add(historicCB, null);
    
    northPanel.add(scrollPane);
    theList.setBackground(Color.darkGray);
    scrollPane.setViewportView(theList);
    theList.setVisibleRowCount(20);
    theList.setForeground(Color.white);
    theList.setBorder(BorderFactory.createRaisedBevelBorder());
    theList.setToolTipText("Please select a zone.");
    theList.setSelectionBackground(Color.white);
    theList.setSelectionForeground(Color.orange);
    theList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    theList.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        theList_mouseClicked(e);
      }
    });
  }
  /**
   * If ok button pushed, loads the sample area if one is selected (sends error if none selected), then disposes of the new sample area dialog.
   * @param e 'OK'
   */
  void OkButton_actionPerformed(ActionEvent e) {
    ok();
  }
/**
 * If user presses 'Cancel' button disposes of the new zone dialog.  
 * @param e
 */
  void CancelButton_actionPerformed(ActionEvent e) {
    cancel();
  }
/**
 * If ok button is pushed and no selection is made a warning message is shown.  
 * If a zone is selected, the zone will be loaded by its zone Id, if it exists.  
 */
  private void ok() {
    if (theList.isSelectionEmpty()) {
      JOptionPane.showMessageDialog(this,"Please select a Zone.",
                                    "No Zone Selected.",
                                    JOptionPane.WARNING_MESSAGE);
      newZone = false;
      return;
    }
    loadZone();
  }
/**
 * Checks whether the zone loaded is a new zone.  
 * @return
 */
  public boolean isNewZone() { return newZone; }
/**
 * Cancels the NewZone dialog by setting the visibility to false and disposing the dialog.  
 */
  private void cancel() {
    setVisible(false);
    dispose();
  }
/**
 * Initializes the new zone dialog by making a JList of all the available zones.  
 */
  private void initialize() {
    String[] zones;

    comcode = JSimpplle.getComcode();
    zones   = comcode.availableZones();

    theList.setListData(zones);
  }
/**
 * If selected zone is not a valid Eastside Region 1 zone and historic is true, will inform user that no historic pathwasys are available.
 * Otherwise will set newzone variable to true, tells the user the zone is loading, and attempts to load the zone and all pathways
 * by its zone Id and the historic boolean.  The new zone dialog is then disposed.  
 */
  private void loadZone() {
    int    zoneId;
      if (theList.getSelectedIndex()>=8){
          zoneId=theList.getSelectedIndex()+2;}
      else
      {zoneId= theList.getSelectedIndex();}
    String zoneName = (String) theList.getSelectedValue();

    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

    if ((zoneId != ValidZones.EASTSIDE_REGION_ONE) && historic) {
      historicCB.setSelected(false);
      historic = false;
      String msg = "Historic pathways are not available for this zone.\n\n" +
                   "Continue using default pathways?";
      int choice = JOptionPane.showConfirmDialog(
                       this,msg,"Historic Pathways not available",
                                           JOptionPane.YES_NO_OPTION,
                                           JOptionPane.INFORMATION_MESSAGE);

      if (choice == JOptionPane.NO_OPTION) {
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        return;
      }
    }

    newZone = true;
    simpplleMain.setStatusMessage("Loading Zone: " + zoneName + " ...");
    simpplleMain.refresh();
    try {
      comcode.loadZone(zoneId,historic);
    }
    catch (simpplle.comcode.SimpplleError e) {
      JOptionPane.showMessageDialog(this,e.getError(),"Error loading zone",
                                    JOptionPane.ERROR_MESSAGE);
    }
    simpplleMain.clearStatusMessage();

    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    setVisible(false);
    dispose();
  }
/**
 * If the historic pathways checkbox has its state changed to deselected, sets historic boolean to false, if the state changed is selection of the 
 * checkbox sets the historic pathways boolean to true.   
 * @param e
 */
  void historicCB_itemStateChanged(ItemEvent e) {
    if (e.getStateChange() == ItemEvent.DESELECTED) {
      historic = false;
    }
    else if (e.getStateChange() == ItemEvent.SELECTED) {
      historic = true;
    }

  }
/**
 * Handles the clicking of the mouse button in the list.  If a zone is selected election, loads the zone.  
 * @param e mouse click in list 
 */
  void theList_mouseClicked(MouseEvent e) {
    if (e.getClickCount() == 2 && theList.isSelectionEmpty() == false) {
      loadZone();
    }
  }
}

