/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import simpplle.comcode.Simpplle;
import simpplle.comcode.Area;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;

import java.util.*;
import java.io.*;

/** 
 *
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class RemoveUnits extends JDialog {
  private static final String prototypeCellValue = "117000";

  private Vector units = new Vector();

  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel northPanel = new JPanel();
  JPanel centerPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  FlowLayout flowLayout2 = new FlowLayout();
  JTextField idText = new JTextField();
  JButton addPB = new JButton();
  JScrollPane unitListScroll = new JScrollPane();
  JButton removeSelectedPB = new JButton();
  JList unitList = new JList();
  JPanel southPanel = new JPanel();
  FlowLayout flowLayout3 = new FlowLayout();
  JButton cancelPB = new JButton();
  JButton deleteUnitsPB = new JButton();
  JButton loadFilePB = new JButton();

  public RemoveUnits(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try {
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public RemoveUnits() {
    this(null, "", false);
  }
  void jbInit() throws Exception {
    mainPanel.setLayout(borderLayout1);
    this.setModal(true);
    this.addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        this_windowClosing(e);
      }
    });
    northPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    flowLayout1.setVgap(0);
    centerPanel.setLayout(flowLayout2);
    flowLayout2.setAlignment(FlowLayout.LEFT);
    flowLayout2.setVgap(0);
    idText.setColumns(6);
    idText.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyTyped(KeyEvent e) {
        idText_keyTyped(e);
      }
    });
    idText.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        idText_actionPerformed(e);
      }
    });
    addPB.setEnabled(false);
    addPB.setText("Add");
    addPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        addPB_actionPerformed(e);
      }
    });
    removeSelectedPB.setEnabled(false);
    removeSelectedPB.setToolTipText("Removes the selected Units from the list.");
    removeSelectedPB.setText("Remove Selected");
    removeSelectedPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        removeSelectedPB_actionPerformed(e);
      }
    });
    unitList.setPrototypeCellValue(prototypeCellValue);
    unitList.setVisibleRowCount(15);
    unitList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        unitList_valueChanged(e);
      }
    });
    southPanel.setLayout(flowLayout3);
    cancelPB.setText("Cancel");
    cancelPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        cancelPB_actionPerformed(e);
      }
    });
    deleteUnitsPB.setEnabled(false);
    deleteUnitsPB.setToolTipText("Delete the units in the list.");
    deleteUnitsPB.setText("Delete Units");
    deleteUnitsPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        deleteUnitsPB_actionPerformed(e);
      }
    });
    loadFilePB.setText("Load From File");
    loadFilePB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        loadFilePB_actionPerformed(e);
      }
    });
    getContentPane().add(mainPanel);
    mainPanel.add(northPanel,  BorderLayout.NORTH);
    mainPanel.add(centerPanel, BorderLayout.CENTER);
    northPanel.add(idText, null);
    northPanel.add(addPB, null);
    northPanel.add(loadFilePB, null);
    centerPanel.add(unitListScroll, null);
    unitListScroll.getViewport().add(unitList, null);
    centerPanel.add(removeSelectedPB, null);
    mainPanel.add(southPanel, BorderLayout.SOUTH);
    southPanel.add(deleteUnitsPB, null);
    southPanel.add(cancelPB, null);
  }

  private void addUnit() {
    Integer unitId;
    Area area = Simpplle.getCurrentArea();

    try {
      unitId = Integer.valueOf(idText.getText());
      if (area.isValidUnitId(unitId) == false) {
        JOptionPane.showMessageDialog(this,"Invalid Unit Id","Invalid Unit Id",
                                      JOptionPane.ERROR_MESSAGE);
        return;
      }
      if (units.contains(unitId) == false) {
        units.addElement(unitId);
        unitList.setListData(units);
      }
      idText.setText("");
      addPB.setEnabled(false);
      deleteUnitsPB.setEnabled(true);
    }
    catch (NumberFormatException nfe) {
      String msg = "Please enter a valid unit Id.";
      JOptionPane.showMessageDialog(this,msg,"Invalid value",
                                    JOptionPane.ERROR_MESSAGE);
      return;
    }
  }

  void idText_actionPerformed(ActionEvent e) {
    addUnit();
  }

  void idText_keyTyped(KeyEvent e) {
    char key = e.getKeyChar();

    addPB.setEnabled(Character.isDigit(key));
    if (Character.isDigit(key) == false &&
        key != KeyEvent.VK_DELETE && key != KeyEvent.VK_BACK_SPACE &&
        key != KeyEvent.VK_ENTER) {
      e.consume();
      java.awt.Toolkit.getDefaultToolkit().beep();
    }
  }

  void addPB_actionPerformed(ActionEvent e) {
    addUnit();
  }

  void unitList_valueChanged(ListSelectionEvent e) {
    removeSelectedPB.setEnabled((unitList.isSelectionEmpty() == false));
  }

  void removeSelectedPB_actionPerformed(ActionEvent e) {
    Object[] values = unitList.getSelectedValues();
    for (int i=0; i<values.length; i++) {
      units.removeElement((Integer)values[i]);
    }
    unitList.clearSelection();
    deleteUnitsPB.setEnabled((units.size() != 0));
    removeSelectedPB.setEnabled(false);
    update(getGraphics());

  }
/**
 * If user pushes the Delete Units button is pressed a OptionPane pops up which warns the user of impending unit deletion.  If the user selects yes 
 * to delete units, these are removed from the current area.  
 * @param e
 */
  void deleteUnitsPB_actionPerformed(ActionEvent e) {
    String msg = "This will delete all the units in the list\n" +
                 "from the current area.\n\n" +
                 "Continue and delete the units?";
    int choice = JOptionPane.showConfirmDialog(this,msg,"Delete Units",
                                               JOptionPane.YES_NO_OPTION,
                                               JOptionPane.QUESTION_MESSAGE);

    if (choice == JOptionPane.NO_OPTION) { return; }

    Simpplle.getCurrentArea().removeUnits(units);

    msg = "Units deleted!\nPlease save the Area.";
    JOptionPane.showMessageDialog(this,msg,"Finsihed",JOptionPane.INFORMATION_MESSAGE);
    quit();
  }
/**
 * Quits by disposing the Remove Units Dialog.
 */
  private void quit() {
    setVisible(false);
    dispose();
  }
/**
 * If user presses "Cancel" button, disposes the Remove Units dialog. 
 * @param e
 */
  void cancelPB_actionPerformed(ActionEvent e) {
    quit();
  }
/**
 * If a window closing event occurs, disposes the Remove Units Dialog.  
 * @param e
 */
  void this_windowClosing(WindowEvent e) {
    quit();
  }
/**
 * Loads the Evu Id file.  
 */
  private void loadFile() {
    BufferedReader fin;

    File outfile = Utility.getOpenFile(this,"Unit Id file");
    if (outfile == null) { return; }

    try {
      fin = new BufferedReader(new FileReader(outfile));
      loadFile(fin);
      fin.close();
    }
    catch (Exception err) {
      JOptionPane.showMessageDialog(this,err.getMessage(),"Error reading file",JOptionPane.ERROR_MESSAGE);
      return;
    }
  }

  private void loadFile(BufferedReader fin) {
    String  line;
    Area    area = Simpplle.getCurrentArea();
    Integer unitId;

    try {
      line = fin.readLine();
      if (line == null) {
        throw new Exception("No data in file");
      }
      while (line != null) {
        unitId = Integer.valueOf(line.trim());
        // Skip invalid unit id's
        if (area.isValidUnitId(unitId) == false) {
          line = fin.readLine();
          continue;
        }

        if (units.contains(unitId) == false) {
          units.addElement(unitId);
        }
        line = fin.readLine();
      }
      if (units.size() > 0) {
        unitList.setListData(units);
        deleteUnitsPB.setEnabled(true);
      }
      update(getGraphics());
    }
    catch (Exception err) {
      JOptionPane.showMessageDialog(this,err.getMessage(),"Error reading file",JOptionPane.ERROR_MESSAGE);
    }
  }

  void loadFilePB_actionPerformed(ActionEvent e) {
    loadFile();
  }
}


