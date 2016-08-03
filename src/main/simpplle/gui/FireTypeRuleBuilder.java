/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import simpplle.comcode.Density;
import simpplle.comcode.FireTypeDataNewerLegacy;
import simpplle.comcode.HabitatTypeGroup;
import simpplle.comcode.Process;
import simpplle.comcode.ProcessType;
import simpplle.comcode.TreatmentType;

/**
 * This class sets up Fire Suppression Beyond Class A LogicPanel, a type of Vegetative Logic Panel, which inherits from Base Panel.
 * Class A fires are 0-.25 Acres.  
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 * @version 2.3
 * @deprecated No longer used
 */

public class FireTypeRuleBuilder extends JDialog {
  private static final String densityPrototype   = "1         ";
  private static final String treatmentPrototype = "ECOSYSTEM-MANAGEMENT-THIN-AND-UNDERBURN    ";
  private static final String processPrototype   = "COLD-INJURY-BARK-BEETLES    ";

  private boolean          inInit=false;

  private DefaultListModel densitySourceListModel;
  private DefaultListModel densityTargetListModel;
  private DefaultListModel treatmentSourceListModel;
  private DefaultListModel treatmentTargetListModel;
  private DefaultListModel processSourceListModel;
  private DefaultListModel processTargetListModel;

  private FireTypeDataNewerLegacy.FireTypeDataEntry.SizeClassRule sizeClassRule;

  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel jPanel1 = new JPanel();
  JPanel processPanel = new JPanel();
  JPanel treatmentPanel = new JPanel();
  JPanel densityPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  JPanel densityTargetPanel = new JPanel();
  JPanel densitySourcePanel = new JPanel();
  BorderLayout borderLayout2 = new BorderLayout();
  BorderLayout borderLayout3 = new BorderLayout();
  JScrollPane densitySourceScroll = new JScrollPane();
  JScrollPane densityTargetScroll = new JScrollPane();
  DragSourceList densitySourceList = new DragSourceList();
  DragDropList densityTargetList = new DragDropList();
  TitledBorder titledBorder1;
  TitledBorder titledBorder2;
  TitledBorder titledBorder3;
  FlowLayout flowLayout2 = new FlowLayout();
  JPanel treatmentTargetPanel = new JPanel();
  JPanel treatmentSourcePanel = new JPanel();
  JScrollPane treatmentSourceScroll = new JScrollPane();
  DragSourceList treatmentSourceList = new DragSourceList();
  JScrollPane treatmentTargetScroll = new JScrollPane();
  DragDropList treatmentTargetList = new DragDropList();
  JCheckBox treatmentAnyExceptCB = new JCheckBox();
  BorderLayout borderLayout4 = new BorderLayout();
  TitledBorder titledBorder4;
  TitledBorder titledBorder5;
  TitledBorder titledBorder6;
  FlowLayout flowLayout3 = new FlowLayout();
  JPanel processTargetPanel = new JPanel();
  JPanel processSourcePanel = new JPanel();
  BorderLayout borderLayout5 = new BorderLayout();
  BorderLayout borderLayout6 = new BorderLayout();
  JScrollPane processSourceScroll = new JScrollPane();
  JScrollPane processTargetScroll = new JScrollPane();
  DragSourceList processSourceList = new DragSourceList();
  DragDropList processTargetList = new DragDropList();
  TitledBorder titledBorder7;
  TitledBorder titledBorder8;

  public FireTypeRuleBuilder(Dialog dialog, String title, boolean modal,
                             FireTypeDataNewerLegacy.FireTypeDataEntry.SizeClassRule rule) {
    super(dialog, title, modal);
    try {
      jbInit();
      this.sizeClassRule = rule;
      initialize();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public FireTypeRuleBuilder() {
    this(null, "", false,null);
  }
  private void jbInit() throws Exception {
    titledBorder1 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Available");
    titledBorder2 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Chosen");
    titledBorder3 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Density");
    titledBorder4 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Available");
    titledBorder5 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Chosen");
    titledBorder6 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Treatments");
    titledBorder7 = new TitledBorder("");
    titledBorder8 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Processes");
    mainPanel.setLayout(borderLayout1);
    jPanel1.setLayout(new BoxLayout(jPanel1, BoxLayout.Y_AXIS));
    densityPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    densitySourcePanel.setLayout(borderLayout2);
    densityTargetPanel.setLayout(borderLayout3);
    densitySourcePanel.setBorder(titledBorder1);
    densityTargetPanel.setBorder(titledBorder2);
    densityPanel.setBorder(titledBorder3);
    treatmentPanel.setLayout(flowLayout2);
    treatmentSourcePanel.setLayout(borderLayout4);
    treatmentTargetPanel.setLayout(new BoxLayout(jPanel1, BoxLayout.Y_AXIS));
    flowLayout2.setAlignment(FlowLayout.LEFT);
    treatmentAnyExceptCB.setEnabled(true);
    treatmentAnyExceptCB.setFont(new java.awt.Font("Monospaced", 0, 12));
    treatmentAnyExceptCB.setText("Any Treatment Except:");
    treatmentSourcePanel.setBorder(titledBorder4);
    treatmentTargetPanel.setBorder(titledBorder5);
    treatmentPanel.setBorder(titledBorder6);
    processPanel.setLayout(flowLayout3);
    flowLayout3.setAlignment(FlowLayout.LEFT);
    processSourcePanel.setLayout(borderLayout5);
    processTargetPanel.setLayout(borderLayout6);
    densitySourceList.setFont(new java.awt.Font("Monospaced", 0, 12));
    densitySourceList.setPrototypeCellValue(densityPrototype);
    densitySourceList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    densitySourceList.setVisibleRowCount(4);
    densityTargetList.setFont(new java.awt.Font("Monospaced", 0, 12));
    densityTargetList.setPrototypeCellValue(densityPrototype);
    densityTargetList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    densityTargetList.setVisibleRowCount(4);
    treatmentSourceList.setFont(new java.awt.Font("Monospaced", 0, 12));
    treatmentSourceList.setPrototypeCellValue(treatmentPrototype);
    treatmentSourceList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    treatmentSourceList.setVisibleRowCount(6);
    treatmentTargetList.setFont(new java.awt.Font("Monospaced", 0, 12));
    treatmentTargetList.setPrototypeCellValue(treatmentPrototype);
    treatmentTargetList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    treatmentTargetList.setVisibleRowCount(4);
    processSourceList.setFont(new java.awt.Font("Monospaced", 0, 12));
    processSourceList.setPrototypeCellValue(processPrototype);
    processSourceList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    processSourceList.setVisibleRowCount(6);
    processTargetList.setFont(new java.awt.Font("Monospaced", 0, 12));
    processTargetList.setBorder(null);
    processTargetList.setPrototypeCellValue(processPrototype);
    processTargetList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    processTargetList.setVisibleRowCount(6);
    processSourcePanel.setBorder(titledBorder1);
    processTargetPanel.setBorder(titledBorder2);
    processPanel.setBorder(titledBorder8);
    densityPanel.add(densitySourcePanel, null);
    densitySourcePanel.add(densitySourceScroll, BorderLayout.CENTER);
    densitySourceScroll.getViewport().add(densitySourceList, null);
    densityPanel.add(densityTargetPanel, null);
    densityTargetPanel.add(densityTargetScroll, BorderLayout.CENTER);
    densityTargetScroll.getViewport().add(densityTargetList, null);
    getContentPane().add(mainPanel);
    mainPanel.add(jPanel1, BorderLayout.NORTH);
    jPanel1.add(densityPanel, null);
    jPanel1.add(treatmentPanel, null);
    jPanel1.add(processPanel, null);
    treatmentPanel.add(treatmentSourcePanel, null);
    treatmentSourcePanel.add(treatmentSourceScroll, BorderLayout.CENTER);
    treatmentSourceScroll.getViewport().add(treatmentSourceList, null);
    treatmentPanel.add(treatmentTargetPanel, null);
    treatmentTargetPanel.add(treatmentAnyExceptCB, null);
    treatmentTargetPanel.add(treatmentTargetScroll, null);
    treatmentTargetScroll.getViewport().add(treatmentTargetList, null);
    processPanel.add(processSourcePanel, null);
    processPanel.add(processTargetPanel, null);
    processSourcePanel.add(processSourceScroll, BorderLayout.CENTER);
    processSourceScroll.getViewport().add(processSourceList, null);
    processTargetPanel.add(processTargetScroll, BorderLayout.CENTER);
    processTargetScroll.getViewport().add(processTargetList, null);
  }

  public void initialize() {
    inInit = true;

    densitySourceListModel = new DefaultListModel();
    densityTargetListModel = new DefaultListModel();
    treatmentSourceListModel = new DefaultListModel();
    treatmentTargetListModel = new DefaultListModel();
    processSourceListModel = new DefaultListModel();
    processTargetListModel = new DefaultListModel();

    densitySourceList.setModel(densitySourceListModel);
    densityTargetList.setModel(densityTargetListModel);
    treatmentSourceList.setModel(treatmentSourceListModel);
    treatmentTargetList.setModel(treatmentTargetListModel);
    processSourceList.setModel(processSourceListModel);
    processTargetList.setModel(processTargetListModel);

    int i,j;
    Vector densities = HabitatTypeGroup.getValidDensity();
    for (i=0; i<densities.size(); i++) {
      densitySourceListModel.addElement(densities.elementAt(i).toString());
    }
    if (sizeClassRule.densities != null) {
      for (j = 0; j < sizeClassRule.densities.length; j++) {
        densityTargetListModel.addElement(sizeClassRule.densities[j].toString());
      }
    }

    TreatmentType[] treatments = simpplle.comcode.Simpplle.getCurrentZone().getLegalTreatments();
    for (i=0; i<treatments.length; i++) {
      treatmentSourceListModel.addElement(treatments[i].toString());
    }
    if (sizeClassRule.treatments != null) {
      for (j = 0; j < sizeClassRule.treatments.length; j++) {
        treatmentTargetListModel.addElement(sizeClassRule.treatments[j].toString());
      }
    }

    ProcessType[] processes = Process.getLegalProcesses();
    for (i=0; i<processes.length; i++) {
      processSourceListModel.addElement(processes[i].toString());
    }
    if (sizeClassRule.processes != null) {
      for (j = 0; j < sizeClassRule.processes.length; j++) {
        processTargetListModel.addElement(sizeClassRule.processes[j].toString());
      }
    }

    treatmentAnyExceptCB.setSelected(sizeClassRule.anyTreatmentExcept);

    update(getGraphics());
    inInit = false;
  }

  public void finishUp() {
    int i;
    int size;
    Density[]       densities = null;
    boolean         anyTreatmentExcept;
    TreatmentType[] treatments = null;
    ProcessType[]   processes = null;

    size = densityTargetListModel.size();
    if (size > 0) {
      densities = new Density[size];
      for (i = 0; i < size; i++) {
        densities[i] = Density.get( (String)densityTargetListModel.elementAt(i));
      }
    }

    size = treatmentTargetListModel.size();
    if (size > 0) {
      treatments = new TreatmentType[size];
      for (i=0; i<size; i++) {
        treatments[i] = TreatmentType.get((String)treatmentTargetListModel.elementAt(i));
      }
    }

    size = processTargetListModel.size();
    if (size > 0) {
      processes = new ProcessType[size];
      for (i=0; i<size; i++) {
        processes[i] = ProcessType.get((String)processTargetListModel.elementAt(i));
      }
    }

    anyTreatmentExcept = treatmentAnyExceptCB.isSelected();

    sizeClassRule.update(densities,anyTreatmentExcept,treatments,processes);
  }
}


