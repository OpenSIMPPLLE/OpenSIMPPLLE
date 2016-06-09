package simpplle.gui;

import simpplle.JSimpplle;
import simpplle.comcode.Area;
import simpplle.comcode.SimpplleError;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

/**
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p> A NewSampleArea dialog prompts the user to select a sample area for the current regional zone.
 *
 * <p> Original source code authorship: Kirk A. Moeller
 */

public class NewSampleArea extends JDialog {

  private JList        theList       = new JList();
  private JPanel       panel1        = new JPanel();
  private JPanel       buttonPanel   = new JPanel();
  private JButton      okButton      = new JButton("Ok");
  private JButton      cancelButton  = new JButton("Cancel");
  private FlowLayout   flowLayout2   = new FlowLayout();
  private BorderLayout borderLayout1 = new BorderLayout();

  /**
   * Creates a new area dialog with no owner and not modal.
   */
  public NewSampleArea() {

    this(null, false);

  }

  /**
   * Creates a new area dialog.
   * @param frame Owner of the dialog
   * @param modal If true, blocks input to other windows
   */
  public NewSampleArea(SimpplleMain frame, boolean modal) {

    super(frame, "Select an Area", modal);

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

    theList.setBorder(BorderFactory.createRaisedBevelBorder());
    theList.setMinimumSize(new Dimension(161, 200));
    theList.setPreferredSize(new Dimension(161, 200));
    theList.setForeground(Color.white);
    theList.setBackground(Color.darkGray);
    theList.setSelectionBackground(Color.white);
    theList.setSelectionForeground(Color.orange);
    theList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    theList.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        clickListItem(e);
      }
    });

    okButton.setMinimumSize(new Dimension(73, 27));
    okButton.setPreferredSize(new Dimension(73, 27));
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

    buttonPanel.setLayout(flowLayout2);
    buttonPanel.add(okButton, null);
    buttonPanel.add(cancelButton, null);

    panel1.setLayout(borderLayout1);
    panel1.setMinimumSize(new Dimension(300, 237));
    panel1.setPreferredSize(new Dimension(300, 237));
    panel1.add(buttonPanel, BorderLayout.SOUTH);
    panel1.add(theList, BorderLayout.NORTH);

    getContentPane().add(panel1);

    setResizable(false);

  }

  /**
   * Initializes the sample area list with the sample areas in the current regional zone.
   */
  private void initialize() {

    theList.setListData(JSimpplle.getComcode().getSampleAreas());

  }

  /**
   * Handles mouse input in the list view. If an item is double-clicked, then the selected sample area is loaded and
   * the dialog is disposed.
   * @param e Mouse event
   */
  void clickListItem(MouseEvent e) {
    if (e.getClickCount() == 2 && theList.isSelectionEmpty() == false) {
      loadSampleArea();
      setVisible(false);
      dispose();
    }
  }

  /**
   * Handles selection of the ok button by loading the selected sample area and disposing of the dialog. An error
   * message is displayed if there is no selection.
   */
  private void selectOk() {

    if (theList.isSelectionEmpty()) {
      JOptionPane.showMessageDialog(this,"Please Select an Area","No Area Selected",JOptionPane.WARNING_MESSAGE);
      return;
    }

    loadSampleArea();
    setVisible(false);
    dispose();

  }

  /**
   * Handles selection of the cancel button by disposing of the dialog.
   */
  private void selectCancel() {

    setVisible(false);
    dispose();

  }

  /**
   * Loads the selected sample area. An error message is displayed if there is an exception.
   */
  private void loadSampleArea() {

    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

    Area area = (Area)theList.getSelectedValue();

    try {
      JSimpplle.getComcode().loadSampleArea(area);
    } catch (SimpplleError err) {
      JOptionPane.showMessageDialog(this,err.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
    }

    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

  }
}

