/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import simpplle.JSimpplle;
import simpplle.comcode.ValidZones;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;

/**
 * A NewZoneDialog dialog prompts the user for a regional zone.
 *
 * <p>Original source code authorship: Kirk A. Moeller  
 */

public class NewZoneDialog extends JDialog {

  private boolean historic = false;
  private boolean newZone  = false;

  private JList        zoneList      = new JList();
  private JButton      okButton      = new JButton("Ok");
  private JButton      cancelButton  = new JButton("Cancel");
  private JCheckBox    historicCB    = new JCheckBox("Use Historic Pathways");
  private JScrollPane  scrollPane    = new JScrollPane();
  private JPanel       mainPanel     = new JPanel();
  private JPanel       northPanel    = new JPanel();
  private JPanel       southPanel    = new JPanel();
  private JPanel       historicPanel = new JPanel();
  private FlowLayout   flowLayout1   = new FlowLayout();
  private FlowLayout   flowLayout2   = new FlowLayout();
  private BorderLayout borderLayout1 = new BorderLayout();
  private BorderLayout borderLayout2 = new BorderLayout();

  /**
   * Creates a new zone dialog with no owner and not modal.
   */
  public NewZoneDialog() {

    this(null, false);

  }

  /**
   * Creates a new zone dialog.
   * @param frame Owner of the dialog
   * @param modal If true, blocks input to other windows
   */
  public NewZoneDialog(SimpplleMain frame, boolean modal) {

    super(frame, "Select a Zone", modal);

    try  {
      jbInit();
      pack();
    } catch(Exception ex) {
      ex.printStackTrace();
    }

    initialize();

  }

  /**
   * Initializes the dialog with buttons, a list, panels, and layouts.
   * @throws Exception
   */
  void jbInit() throws Exception {

    zoneList.setBorder(BorderFactory.createRaisedBevelBorder());
    zoneList.setVisibleRowCount(20);
    zoneList.setForeground(Color.white);
    zoneList.setBackground(Color.darkGray);
    zoneList.setSelectionBackground(Color.white);
    zoneList.setSelectionForeground(Color.orange);
    zoneList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    zoneList.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        clickListItem(e);
      }
    });

    scrollPane.setViewportView(zoneList);

    historicCB.setToolTipText("load historic pathways instead of the default pathways");
    historicCB.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        toggleHistoricCB(e);
      }
    });

    flowLayout1.setAlignment(FlowLayout.LEFT);
    flowLayout1.setHgap(10);

    historicPanel.setLayout(flowLayout1);
    historicPanel.add(historicCB, null);

    northPanel.setLayout(borderLayout2);
    northPanel.add(scrollPane, BorderLayout.CENTER);
    northPanel.add(historicPanel, BorderLayout.SOUTH);

    okButton.setMaximumSize(new Dimension(73, 27));
    okButton.setMinimumSize(new Dimension(73, 27));
    okButton.setPreferredSize(new Dimension(73, 27));
    okButton.setMargin(new Insets(2, 14, 2, 14));
    okButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        selectOk();
      }
    });

    cancelButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        selectCancel();
      }
    });

    southPanel.setLayout(flowLayout2);
    southPanel.setBorder(BorderFactory.createEtchedBorder());
    southPanel.add(okButton, null);
    southPanel.add(cancelButton, null);

    mainPanel.setLayout(borderLayout1);
    mainPanel.add(southPanel, BorderLayout.SOUTH);
    mainPanel.add(northPanel);

    getContentPane().add(mainPanel);

  }

  /**
   * Initializes the zone list with the hard-coded available zones.
   */
  private void initialize() {

    zoneList.setListData(JSimpplle.getComcode().availableZones());

  }

  /**
   * Checks whether the zone loaded is a new zone.
   * @return True if the zone is new
   */
  public boolean isNewZone() {
    return newZone;
  }

  /**
   * Handles mouse input in the list view. If a zone is double-clicked, then the selected zone is loaded and the
   * dialog is disposed.
   * @param e Mouse event
   */
  void clickListItem(MouseEvent e) {
    if (e.getClickCount() == 2 && zoneList.isSelectionEmpty() == false) {

      loadZone();

      setVisible(false);
      dispose();

    }
  }

  /**
   * Synchronizes the historic pathways boolean with the state of the radio button.
   * @param e Item event
   */
  void toggleHistoricCB(ItemEvent e) {
    if (e.getStateChange() == ItemEvent.DESELECTED) {
      historic = false;
    } else if (e.getStateChange() == ItemEvent.SELECTED) {
      historic = true;
    }
  }

  /**
   * Loads the selected zone and disposes of the dialog. An error message is displayed if there is no selection.
   */
  private void selectOk() {

    if (zoneList.isSelectionEmpty()) {
      JOptionPane.showMessageDialog(this,"Please select a Zone","No Zone Selected",JOptionPane.WARNING_MESSAGE);
      newZone = false;
      return;
    }

    loadZone();

    setVisible(false);
    dispose();

  }

  /**
   * Disposes of the dialog without loading a zone.
   */
  private void selectCancel() {

    setVisible(false);
    dispose();

  }

  /**
   * Loads the selected zone. An error message is displayed if there is an exception.
   */
  private void loadZone() {

    //=====================================================================
    // This is a hack from early OpenSIMPPLLE. It skips the index of the
    // Michigan and Zone Builder zones, which have been removed. This
    // should be fixed as it will break if a zone is added before index 8
    // or their order is changed.
    //=====================================================================

    int zoneId;
    if (zoneList.getSelectedIndex() >= 8) {
      zoneId = zoneList.getSelectedIndex() + 2;
    } else {
      zoneId = zoneList.getSelectedIndex();
    }

    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

    //=====================================================================
    // This is another hack from early OpenSIMPPLLE. It warns the user that
    // Eastside Region One doesn't have historic pathways.
    //=====================================================================

    if ((zoneId != ValidZones.EASTSIDE_REGION_ONE) && historic) {

      historicCB.setSelected(false);
      historic = false;

      int choice = JOptionPane.showConfirmDialog(this,
                                                 "Historic pathways are not available for this zone.\n\n" +
                                                 "Continue using default pathways?",
                                                 "Historic Pathways not available",
                                                 JOptionPane.YES_NO_OPTION,
                                                 JOptionPane.INFORMATION_MESSAGE);

      if (choice == JOptionPane.NO_OPTION) {
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        return;
      }

    }

    newZone = true;

    try {
      JSimpplle.getComcode().loadZone(zoneId,historic);
    } catch (simpplle.comcode.SimpplleError e) {
      JOptionPane.showMessageDialog(this,e.getError(), "Error loading zone", JOptionPane.ERROR_MESSAGE);
    }

    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

  }
}

