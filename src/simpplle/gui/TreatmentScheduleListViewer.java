/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import java.awt.*;
import javax.swing.*;
import java.util.*;

import simpplle.comcode.TreatmentApplication;
import simpplle.comcode.MyInteger;
import java.awt.event.*;

/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 */

public class TreatmentScheduleListViewer extends JDialog {
  private simpplle.comcode.TreatmentSchedule schedule;

  private MyInteger            currentTimeStep;
  private MyInteger[]          allTimeSteps;
  private Vector               apps;
  private DefaultListModel     listModel;
  private boolean              listChanged;
  private TreatmentApplication selectedApp;

  JPanel panel1 = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel jPanel1 = new JPanel();
  JPanel jPanel2 = new JPanel();
  JPanel jPanel3 = new JPanel();
  FlowLayout flowLayout2 = new FlowLayout();
  JPanel prevNextPBPanel = new JPanel();
  FlowLayout flowLayout4 = new FlowLayout();
  JButton nextPB = new JButton();
  JButton prevPB = new JButton();
  BorderLayout borderLayout2 = new BorderLayout();
  JScrollPane jScrollPane1 = new JScrollPane();
  TreatmentAppDragDropList theList = new TreatmentAppDragDropList();
  BorderLayout borderLayout3 = new BorderLayout();
  JPanel buttonPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  JButton editSelectedPB = new JButton();
  JButton closePB = new JButton();

  public TreatmentScheduleListViewer(Frame frame, String title, boolean modal,
                                     simpplle.comcode.TreatmentSchedule schedule,
                                     MyInteger timeStep) {
    super(frame, title, modal);
    try {
      jbInit();
      currentTimeStep = timeStep;
      this.schedule   = schedule;
      initialize();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public TreatmentScheduleListViewer() {
    this(null, "", false,null,null);
  }
  void jbInit() throws Exception {
    panel1.setLayout(borderLayout1);
    jPanel1.setLayout(borderLayout2);
    jPanel3.setLayout(flowLayout2);
    jPanel2.setLayout(borderLayout3);
    flowLayout2.setAlignment(FlowLayout.LEFT);
    flowLayout2.setHgap(0);
    flowLayout2.setVgap(0);
    prevNextPBPanel.setLayout(flowLayout4);
    nextPB.setIcon(new ImageIcon(simpplle.gui.TreatmentScheduleListViewer.class.getResource("images/next.gif")));
    nextPB.setMargin(new Insets(0, 0, 0, 0));
    nextPB.setPressedIcon(new ImageIcon(simpplle.gui.TreatmentScheduleListViewer.class.getResource("images/nextg.gif")));
    nextPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        nextPB_actionPerformed(e);
      }
    });
    prevPB.setIcon(new ImageIcon(simpplle.gui.TreatmentScheduleListViewer.class.getResource("images/prev.gif")));
    prevPB.setMargin(new Insets(0, 0, 0, 0));
    prevPB.setPressedIcon(new ImageIcon(simpplle.gui.TreatmentScheduleListViewer.class.getResource("images/prevg.gif")));
    prevPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        prevPB_actionPerformed(e);
      }
    });
    this.setModal(true);
    this.addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        this_windowClosing(e);
      }
    });
    buttonPanel.setLayout(flowLayout1);
    editSelectedPB.setEnabled(false);
    editSelectedPB.setText("Close and Edit Selected");
    editSelectedPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        editSelectedPB_actionPerformed(e);
      }
    });
    closePB.setText("Close");
    closePB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        closePB_actionPerformed(e);
      }
    });
    buttonPanel.setBorder(BorderFactory.createEtchedBorder());
    theList.setFont(new java.awt.Font("Monospaced", 0, 12));
    theList.setVisibleRowCount(20);
    getContentPane().add(panel1);
    panel1.add(jPanel1,  BorderLayout.CENTER);
    jPanel1.add(jPanel3,  BorderLayout.NORTH);
    jPanel3.add(prevNextPBPanel, null);
    prevNextPBPanel.add(prevPB, null);
    prevNextPBPanel.add(nextPB, null);
    jPanel3.add(buttonPanel, null);
    jPanel1.add(jPanel2, BorderLayout.CENTER);
    jPanel2.add(jScrollPane1, BorderLayout.CENTER);
    jScrollPane1.getViewport().add(theList, null);
    buttonPanel.add(closePB, null);
    buttonPanel.add(editSelectedPB, null);
  }

  private void initialize() {
    allTimeSteps = schedule.getAllTimeSteps();
    listModel    = new DefaultListModel();

    theList.initList(this,listModel);
    updateDialog();
  }

  private void updateDialog() {
    apps = schedule.getApplications(currentTimeStep);

    theList.updateListData(apps);
    clearListChanged();
    setSelectedItem(null);
    selectedApp = null;
    update(getGraphics());
  }

  public void markListChanged() { listChanged = true; }
  private void clearListChanged() { listChanged = false; }
  public void setSelectedItem(TreatmentApplication app) {
    selectedApp = app;
    editSelectedPB.setEnabled((selectedApp != null));
  }

  public TreatmentApplication getSelectedApp() { return selectedApp; }


  private void close() {
    selectedApp = null;
    setVisible(false);
    dispose();
  }
  void closePB_actionPerformed(ActionEvent e) {
    close();
  }
  void this_windowClosing(WindowEvent e) {
    close();
  }

  void editSelectedPB_actionPerformed(ActionEvent e) {
    setVisible(false);
    dispose();
  }

  void prevPB_actionPerformed(ActionEvent e) {
    int index = -1;
    for (int i = 0; i < allTimeSteps.length; i++) {
      if (currentTimeStep.equals(allTimeSteps[i])) {
        index = i - 1;
        if (index < 0) {
          index = allTimeSteps.length - 1;
        }
        currentTimeStep = allTimeSteps[index];
        break;
      }
    }
    if (index == -1) {
      currentTimeStep = allTimeSteps[0];
    } // impossible, but just in case.
    updateDialog();
  }

  void nextPB_actionPerformed(ActionEvent e) {
    int index = -1;
    for (int i=0; i<allTimeSteps.length; i++) {
      if (currentTimeStep.equals(allTimeSteps[i])) {
        index = i + 1;
        if (index == allTimeSteps.length) { index = 0; }
        currentTimeStep = allTimeSteps[index];
        break;
      }
    }
    if (index == -1) { currentTimeStep = allTimeSteps[0]; } // impossible, but just in case.
    updateDialog();
  }


}

