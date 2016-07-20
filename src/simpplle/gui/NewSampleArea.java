/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import simpplle.JSimpplle;
import simpplle.comcode.Area;
import simpplle.comcode.SimpplleError;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

/**
 * A NewSampleArea dialog prompts the user to select a sample area for the current regional zone.
 *
 * <p> Original source code authorship: Kirk A. Moeller
 */

public class NewSampleArea extends JDialog {

  private JList        areaList     = new JList();
  private JButton      okButton     = new JButton("Ok");
  private JButton      cancelButton = new JButton("Cancel");
  private JPanel       mainPanel    = new JPanel();
  private JPanel       buttonPanel  = new JPanel();
  private FlowLayout   flowLayout   = new FlowLayout();
  private BorderLayout borderLayout = new BorderLayout();

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

    areaList.setBorder(BorderFactory.createRaisedBevelBorder());
    areaList.setMinimumSize(new Dimension(161, 200));
    areaList.setForeground(Color.white);
    areaList.setBackground(Color.darkGray);
    areaList.setSelectionBackground(Color.white);
    areaList.setSelectionForeground(Color.orange);
    areaList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    areaList.addMouseListener(new java.awt.event.MouseAdapter() {
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

    buttonPanel.setLayout(flowLayout);
    buttonPanel.add(okButton, null);
    buttonPanel.add(cancelButton, null);

    mainPanel.setLayout(borderLayout);
    mainPanel.setMinimumSize(new Dimension(300, 237));
    mainPanel.setPreferredSize(new Dimension(300, 237));
    mainPanel.add(areaList, BorderLayout.CENTER);
    mainPanel.add(buttonPanel, BorderLayout.SOUTH);

    getContentPane().add(mainPanel);

  }

  /**
   * Initializes the sample area list with the sample areas in the current regional zone.
   */
  private void initialize() {

    areaList.setListData(JSimpplle.getComcode().getSampleAreas());

  }

  /**
   * Handles mouse input in the list view. If an item is double-clicked, then the selected sample area is loaded and
   * the dialog is disposed.
   * @param e Mouse event
   */
  void clickListItem(MouseEvent e) {
    if (e.getClickCount() == 2 && areaList.isSelectionEmpty() == false) {
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

    if (areaList.isSelectionEmpty()) {
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

    Area area = (Area) areaList.getSelectedValue();

    try {
      JSimpplle.getComcode().loadSampleArea(area);
    } catch (SimpplleError err) {
      JOptionPane.showMessageDialog(this,err.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
    }

    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

  }
}

