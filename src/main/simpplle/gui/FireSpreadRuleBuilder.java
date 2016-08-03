/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import java.util.*;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

import simpplle.comcode.*;
import simpplle.comcode.FireSpreadDataNewerLegacy.*;
import simpplle.comcode.Process;

/** 
 * This class implements a Fire Spread Rule Builder dialog which
 * allows the user to change the Fire Spread Probability. 
 * 
 * @author Documentation by Brian Losi
 * Original source code authorship: Kirk A. Moeller
 */

public class FireSpreadRuleBuilder extends JDialog {
  private static final String sizeClassPrototype   = "CLOSED-TALL-SHRUB    ";
  private static final String processPrototype   = "COLD-INJURY-BARK-BEETLES    ";

  private boolean          inInit=false;

  private DefaultListModel sizeClassSourceListModel;
  private DefaultListModel sizeClassTargetListModel;
  private DefaultListModel processSourceListModel;
  private DefaultListModel processTargetListModel;

  private FireSpreadDataNewerLegacy.FireSpreadDataEntry.DensityRule densityRule;

  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel jPanel1 = new JPanel();
  JPanel processPanel = new JPanel();
  JPanel sizeClassPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  JPanel sizeClassTargetPanel = new JPanel();
  JPanel sizeClassSourcePanel = new JPanel();
  BorderLayout borderLayout2 = new BorderLayout();
  BorderLayout borderLayout3 = new BorderLayout();
  JScrollPane sizeClassSourceScroll = new JScrollPane();
  JScrollPane sizeClassTargetScroll = new JScrollPane();
  DragSourceList sizeClassSourceList = new DragSourceList();
  DragDropList sizeClassTargetList = new DragDropList();
  TitledBorder titledBorder1;
  TitledBorder titledBorder2;
  TitledBorder titledBorder3;
  FlowLayout flowLayout3 = new FlowLayout();
  JPanel processTargetPanel = new JPanel();
  JPanel processSourcePanel = new JPanel();
  BorderLayout borderLayout5 = new BorderLayout();
  BorderLayout borderLayout6 = new BorderLayout();
  JScrollPane processSourceScroll = new JScrollPane();
  JScrollPane processTargetScroll = new JScrollPane();
  DragSourceList processSourceList = new DragSourceList();
  DragDropList processTargetList = new DragDropList();
  TitledBorder titledBorder8;

  public FireSpreadRuleBuilder(Dialog dialog, String title, boolean modal,
                             FireSpreadDataNewerLegacy.FireSpreadDataEntry.DensityRule rule) {
    super(dialog, title, modal);
    try {
      jbInit();
      this.densityRule = rule;
      initialize();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public FireSpreadRuleBuilder() {
    this(null, "", false,null);
  }
  private void jbInit() throws Exception {
    titledBorder1 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Available");
    titledBorder2 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Chosen");
    titledBorder3 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Density");
    titledBorder8 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Processes");
    mainPanel.setLayout(borderLayout1);
    jPanel1.setLayout(new BoxLayout(jPanel1, BoxLayout.Y_AXIS));
    sizeClassPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    sizeClassSourcePanel.setLayout(borderLayout2);
    sizeClassTargetPanel.setLayout(borderLayout3);
    sizeClassSourcePanel.setBorder(titledBorder1);
    sizeClassTargetPanel.setBorder(titledBorder2);
    sizeClassPanel.setBorder(titledBorder3);
    processPanel.setLayout(flowLayout3);
    flowLayout3.setAlignment(FlowLayout.LEFT);
    processSourcePanel.setLayout(borderLayout5);
    processTargetPanel.setLayout(borderLayout6);
    sizeClassSourceList.setFont(new java.awt.Font("Monospaced", 0, 12));
    sizeClassSourceList.setPrototypeCellValue(sizeClassPrototype);
    sizeClassSourceList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    sizeClassSourceList.setVisibleRowCount(8);
    sizeClassTargetList.setFont(new java.awt.Font("Monospaced", 0, 12));
    sizeClassTargetList.setPrototypeCellValue(sizeClassPrototype);
    sizeClassTargetList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    sizeClassTargetList.setVisibleRowCount(6);
    processSourceList.setFont(new java.awt.Font("Monospaced", 0, 12));
    processSourceList.setPrototypeCellValue(processPrototype);
    processSourceList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    processSourceList.setVisibleRowCount(8);
    processTargetList.setFont(new java.awt.Font("Monospaced", 0, 12));
    processTargetList.setBorder(null);
    processTargetList.setPrototypeCellValue(processPrototype);
    processTargetList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    processTargetList.setVisibleRowCount(6);
    processSourcePanel.setBorder(titledBorder1);
    processTargetPanel.setBorder(titledBorder2);
    processPanel.setBorder(titledBorder8);
    titledBorder3.setTitle("Size Class");
    sizeClassPanel.add(sizeClassSourcePanel, null);
    sizeClassSourcePanel.add(sizeClassSourceScroll, BorderLayout.CENTER);
    sizeClassSourceScroll.getViewport().add(sizeClassSourceList, null);
    sizeClassPanel.add(sizeClassTargetPanel, null);
    sizeClassTargetPanel.add(sizeClassTargetScroll, BorderLayout.CENTER);
    sizeClassTargetScroll.getViewport().add(sizeClassTargetList, null);
    getContentPane().add(mainPanel);
    mainPanel.add(jPanel1, BorderLayout.NORTH);
    jPanel1.add(sizeClassPanel, null);
    jPanel1.add(processPanel, null);
    processPanel.add(processSourcePanel, null);
    processPanel.add(processTargetPanel, null);
    processSourcePanel.add(processSourceScroll, BorderLayout.CENTER);
    processSourceScroll.getViewport().add(processSourceList, null);
    processTargetPanel.add(processTargetScroll, BorderLayout.CENTER);
    processTargetScroll.getViewport().add(processTargetList, null);
  }

  public void initialize() {
    inInit = true;

    sizeClassSourceListModel = new DefaultListModel();
    sizeClassTargetListModel = new DefaultListModel();
    processSourceListModel = new DefaultListModel();
    processTargetListModel = new DefaultListModel();

    sizeClassSourceList.setModel(sizeClassSourceListModel);
    sizeClassTargetList.setModel(sizeClassTargetListModel);
    processSourceList.setModel(processSourceListModel);
    processTargetList.setModel(processTargetListModel);

    int i,j;
    Vector sizeClasses = HabitatTypeGroup.getValidSizeClass();
    for (i=0; i<sizeClasses.size(); i++) {
      sizeClassSourceListModel.addElement(sizeClasses.elementAt(i).toString());
    }
    if (densityRule.sizeClasses != null) {
      for (j = 0; j < densityRule.sizeClasses.length; j++) {
        sizeClassTargetListModel.addElement(densityRule.sizeClasses[j].toString());
      }
    }

    ProcessType[] processes = Process.getLegalProcesses();
    for (i=0; i<processes.length; i++) {
      processSourceListModel.addElement(processes[i].toString());
    }
    if (densityRule.processes != null) {
      for (j = 0; j < densityRule.processes.length; j++) {
        processTargetListModel.addElement(densityRule.processes[j].toString());
      }
    }

    update(getGraphics());
    inInit = false;
  }

  public boolean finishUp() {
    int i;
    int size;
    SizeClass[]     sizeClasses = null;
    ProcessType[]   processes = null;

    if (sizeClassTargetListModel.size() == 0 &&
        processTargetListModel.size() == 0) {
      return false;
    }

    size = sizeClassTargetListModel.size();
    if (size > 0) {
      sizeClasses = new SizeClass[size];
      for (i = 0; i < size; i++) {
        sizeClasses[i] = SizeClass.get( (String)sizeClassTargetListModel.elementAt(i));
      }
    }

    size = processTargetListModel.size();
    if (size > 0) {
      processes = new ProcessType[size];
      for (i=0; i<size; i++) {
        processes[i] = ProcessType.get((String)processTargetListModel.elementAt(i));
      }
    }

    densityRule.update(sizeClasses,processes);
    return true;
  }
}


