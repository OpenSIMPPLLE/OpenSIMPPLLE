/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import simpplle.JSimpplle;
import simpplle.comcode.VegetativeType;
import simpplle.comcode.VegetativeTypeNextState;
import simpplle.comcode.Process;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.*;

/** 
 * This class allows users to create a new vegetative state.  This is used in vegetative Pathways. Vegetative states include species, size class, age, density, and habitat type group.
 * The dialog is titled "Create a New State"
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */
public class PathwayPrevStates extends JDialog {
  private String protoCellValue = "RIPARIAN_GRASSES/CLOSED_TALL_SHRUB2/1 -- COLD-INJURY-BARK-BEETLES";
  private VegetativeType selectedState;
  private Process        selectedProcess;
  private MyCanvas       parentDlg;

  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel northPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  JPanel prevStatePanel = new JPanel();
  FlowLayout flowLayout2 = new FlowLayout();
  JPanel PBPanel = new JPanel();
  JScrollPane prevStateScroll = new JScrollPane();
  GridLayout gridLayout1 = new GridLayout();
  JButton editPB = new JButton();
  JButton displayDiagramPB = new JButton();
  JList prevStateList = new JList();
  JPopupMenu listPopupMenu = new JPopupMenu();
  JMenuItem contextMenuEdit = new JMenuItem();
  JMenuItem contextMenuDisplay = new JMenuItem();
  JPanel southPanel = new JPanel();
  FlowLayout flowLayout3 = new FlowLayout();
  JPanel currentPanel = new JPanel();
  JButton displayCurrentPB = new JButton();
  FlowLayout flowLayout4 = new FlowLayout();
  JButton editCurrentPB = new JButton();
  JLabel currentLabel = new JLabel();
  Border border1;
  TitledBorder titledBorder1;

  public PathwayPrevStates(Frame frame, String title, boolean modal,
                           MyCanvas parentDlg, VegetativeType selectedState,
                           Process selectedProcess)
  {
    super(frame, title, modal);
    try {
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
    this.parentDlg       = parentDlg;
    this.selectedState   = selectedState;
    this.selectedProcess = selectedProcess;
    initialize();
  }

  public PathwayPrevStates() {
    this(null, "", false,null,null,null);
  }
  void jbInit() throws Exception {
    border1 = new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(148, 145, 140));
    titledBorder1 = new TitledBorder(border1,"Current Vegetative Type");
    mainPanel.setLayout(borderLayout1);
    northPanel.setLayout(flowLayout1);
    prevStatePanel.setLayout(flowLayout2);
    PBPanel.setLayout(gridLayout1);
    gridLayout1.setRows(2);
    gridLayout1.setVgap(5);
    editPB.setEnabled(false);
    editPB.setText("Edit State");
    editPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        editPB_actionPerformed(e);
      }
    });
    displayDiagramPB.setEnabled(false);
    displayDiagramPB.setText("Display Diagram");
    displayDiagramPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        displayDiagramPB_actionPerformed(e);
      }
    });
    prevStateList.setPrototypeCellValue(protoCellValue);
    prevStateList.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        prevStateList_mouseClicked(e);
      }
      public void mousePressed(MouseEvent e) {
        prevStateList_mousePressed(e);
      }
      public void mouseReleased(MouseEvent e) {
        prevStateList_mouseReleased(e);
      }
    });
    flowLayout2.setAlignment(FlowLayout.LEFT);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    contextMenuEdit.setText("Edit");
    contextMenuEdit.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        contextMenueditCurrentPB_actionPerformed(e);
      }
    });
    contextMenuDisplay.setText("Display Diagram");
    contextMenuDisplay.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        contextMenuDisplay_actionPerformed(e);
      }
    });
    this.addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        this_windowClosing(e);
      }
    });
    southPanel.setLayout(flowLayout3);
    displayCurrentPB.setFont(new java.awt.Font("Monospaced", 0, 12));
    displayCurrentPB.setText("Display Diagram");
    displayCurrentPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        displayCurrentPB_actionPerformed(e);
      }
    });
    currentPanel.setLayout(flowLayout4);
    editCurrentPB.setFont(new java.awt.Font("Monospaced", 0, 12));
    editCurrentPB.setText("Edit");
    editCurrentPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        editCurrentPB_actionPerformed(e);
      }
    });
    currentPanel.setBorder(titledBorder1);
    flowLayout4.setAlignment(FlowLayout.LEFT);
    getContentPane().add(mainPanel);
    mainPanel.add(northPanel, BorderLayout.NORTH);
    northPanel.add(prevStatePanel, null);
    prevStatePanel.add(prevStateScroll, null);
    prevStateScroll.getViewport().add(prevStateList, null);
    prevStatePanel.add(PBPanel, null);
    PBPanel.add(editPB, null);
    PBPanel.add(displayDiagramPB, null);
    mainPanel.add(southPanel, BorderLayout.SOUTH);
    southPanel.add(currentPanel, null);
    currentPanel.add(currentLabel, null);
    currentPanel.add(editCurrentPB, null);
    currentPanel.add(displayCurrentPB, null);
    listPopupMenu.add(contextMenuEdit);
    listPopupMenu.add(contextMenuDisplay);
  }

  private void initialize() {
    prevStateList.setListData(selectedState.findPreviousStates());
    currentLabel.setText(selectedState.toString());
    update(getGraphics());
  }

  void prevStateList_mouseClicked(MouseEvent e) {
   editPB.setEnabled((prevStateList.isSelectionEmpty() == false));
   displayDiagramPB.setEnabled((prevStateList.isSelectionEmpty() == false));
  }

  public void maybePopupMenu(MouseEvent e) {
    if (e.isPopupTrigger() && prevStateList.isSelectionEmpty() == false) {
      listPopupMenu.show(e.getComponent(),e.getX(),e.getY());
    }
  }

  void prevStateList_mousePressed(MouseEvent e) {
   editPB.setEnabled((prevStateList.isSelectionEmpty() == false));
   displayDiagramPB.setEnabled((prevStateList.isSelectionEmpty() == false));
   maybePopupMenu(e);
  }

  void prevStateList_mouseReleased(MouseEvent e) {
   editPB.setEnabled((prevStateList.isSelectionEmpty() == false));
   displayDiagramPB.setEnabled((prevStateList.isSelectionEmpty() == false));
   maybePopupMenu(e);
  }

  private VegetativeType getSelectedNextState() {
    VegetativeTypeNextState
        selection = (VegetativeTypeNextState)prevStateList.getSelectedValue();
    return selection.getNextState();
  }

  private void editState() {
    editState(getSelectedNextState());
  }
  private void editState(VegetativeType selection) {
    PathwayEditor dlg = new PathwayEditor(JSimpplle.getSimpplleMain(),
                                          "Pathway Editor", true,
                                          selection);
    dlg.setVisible(true);
    parentDlg.getPathwayDlg().updateDialog();
    parentDlg.refreshDiagram();
  }
  void contextMenueditCurrentPB_actionPerformed(ActionEvent e) {
    editState();
  }
  void editPB_actionPerformed(ActionEvent e) {
    editState();
  }
  void editCurrentPB_actionPerformed(ActionEvent e) {
    editState(selectedState);
  }

  private void displayDiagram() {
    VegetativeTypeNextState
        selection = (VegetativeTypeNextState)prevStateList.getSelectedValue();

    Pathway pathwayDlg = parentDlg.getPathwayDlg();
    pathwayDlg.setSpeciesAndProcess(selection.getNextState(),selection.getProcess());
  }
  void displayDiagramPB_actionPerformed(ActionEvent e) {
    displayDiagram();
  }
  void contextMenuDisplay_actionPerformed(ActionEvent e) {
    displayDiagram();
  }
  void displayCurrentPB_actionPerformed(ActionEvent e) {
    Pathway pathwayDlg = parentDlg.getPathwayDlg();
    pathwayDlg.setSpeciesAndProcess(selectedState,selectedProcess);
  }

  void this_windowClosing(WindowEvent e) {
    parentDlg.setPrevDialogClosed();
    setVisible(false);
    dispose();
  }


}





