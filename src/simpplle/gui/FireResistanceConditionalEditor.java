/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import simpplle.comcode.FireResistance;
import simpplle.comcode.HabitatTypeGroupType;
import simpplle.comcode.HabitatTypeGroup;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Vector;
import java.awt.dnd.DropTarget;
import javax.swing.event.*;

/** 
 * This class defines the FireLogic Resistance Conditional Editor, a type of JDialog.
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */
public class FireResistanceConditionalEditor extends JDialog {
  private static final String protoTypeCellValue = "ABCDEF  ";
  private HashMap data;

  private JPanel panel1 = new JPanel();
  JPanel dropPanel = new JPanel();
  JPanel buttonPanel = new JPanel();
  JPanel highPanel = new JPanel();
  JPanel moderatePanel = new JPanel();
  JPanel lowPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  Border border1;
  TitledBorder lowBorder;
  Border border2;
  TitledBorder moderateBorder;
  Border border3;
  TitledBorder highBorder;
  FlowLayout flowLayout2 = new FlowLayout();
  JButton cancelPB = new JButton();
  JButton okPB = new JButton();
  FlowLayout flowLayout3 = new FlowLayout();
  FlowLayout flowLayout4 = new FlowLayout();
  JPanel groupDragSourcePanel = new JPanel();
  JPanel groupSourcePanel = new JPanel();
  JScrollPane groupSourceScroll = new JScrollPane();
  JList groupSourceList = new JList();
  Border border4;
  TitledBorder sourceBorder;
  JPanel lowListPanel = new JPanel();
  JPanel moderateListPanel = new JPanel();
  JPanel highListPanel = new JPanel();
  FlowLayout flowLayout6 = new FlowLayout();
  FlowLayout flowLayout7 = new FlowLayout();
  FlowLayout flowLayout8 = new FlowLayout();
  JScrollPane lowListScroll = new JScrollPane();
  JScrollPane moderateListScroll = new JScrollPane();
  JScrollPane highListScroll = new JScrollPane();
  JList lowList = new JList();
  JList moderateList = new JList();
  JList highList = new JList();
  FlowLayout flowLayout9 = new FlowLayout();
  Border border5;
  TitledBorder mainDropBorder;
  JPanel groupScrollPanel = new JPanel();
  FlowLayout flowLayout5 = new FlowLayout();
  FlowLayout flowLayout10 = new FlowLayout();
  FlowLayout flowLayout11 = new FlowLayout();
  
  /**
   * Constructor for fire resistance conditional editor.  
   * @param dialog
   * @param title
   * @param modal
   * @param data
   */

  public FireResistanceConditionalEditor(Dialog dialog, String title, boolean modal, HashMap data) {
    super(dialog, title, modal);
    try {
      jbInit();
      this.data = data;
      initialize();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
  /**
   * Overloaded Fire Resistance Conditional Editor.  References primary constructor and sets dialog as null, title to empty string, modality to false, and data to null.
   * 
   */

  public FireResistanceConditionalEditor() {
    this(null, "", false,null);
  }
  /**
   * Sets the border, panels, components, layouts, and lists used by the Fire Resistance Conditional Editor. 
   * @throws Exception
   */
  private void jbInit() throws Exception {
    border1 = new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(148, 145, 140));
    lowBorder = new TitledBorder(border1,"Low Resistance Ecological Grouping");
    border2 = new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(148, 145, 140));
    moderateBorder = new TitledBorder(border2,"Moderate Resistance Ecological Grouping");
    border3 = new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(148, 145, 140));
    highBorder = new TitledBorder(border3,"High Resistance Ecological Grouping");
    border4 = new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(148, 145, 140));
    sourceBorder = new TitledBorder(border4,"Ecological Grouping Not Yet Assigned");
    border5 = BorderFactory.createEmptyBorder();
    mainDropBorder = new TitledBorder(new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(148, 145, 140)),"Fire Resistance by Ecological Grouping");
    panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));
    dropPanel.setLayout(flowLayout9);
    lowPanel.setLayout(flowLayout1);
    moderatePanel.setLayout(flowLayout3);
    highPanel.setLayout(flowLayout4);
    buttonPanel.setLayout(flowLayout2);
    cancelPB.setText("Cancel");
    cancelPB.addActionListener(new FireResistanceConditionalEditor_cancelPB_actionAdapter(this));
    okPB.setText("Ok");
    okPB.addActionListener(new FireResistanceConditionalEditor_okPB_actionAdapter(this));
    flowLayout1.setAlignment(FlowLayout.LEFT);
    flowLayout1.setHgap(3);
    flowLayout1.setVgap(0);
    flowLayout3.setAlignment(FlowLayout.LEFT);
    flowLayout3.setHgap(3);
    flowLayout3.setVgap(0);
    flowLayout4.setAlignment(FlowLayout.LEFT);
    flowLayout4.setHgap(3);
    flowLayout4.setVgap(0);
    this.setModal(true);
    this.setTitle("Fire Resistance Conditionals Editor");
    groupDragSourcePanel.setLayout(flowLayout5);
    groupSourcePanel.setLayout(flowLayout10);
    groupSourceList.setToolTipText("Use Drag-n-Drop to move items");
    groupSourceList.setPrototypeCellValue(protoTypeCellValue);
    groupSourceList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    groupSourceList.setVisibleRowCount(8);
    lowListPanel.setLayout(flowLayout6);
    moderateListPanel.setLayout(flowLayout7);
    highListPanel.setLayout(flowLayout8);
    lowPanel.setBorder(null);
    moderatePanel.setBorder(null);
    highPanel.setBorder(null);
    lowList.setEnabled(true);
    lowList.setPrototypeCellValue(protoTypeCellValue);
    lowList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    lowList.addListSelectionListener(new FireResistanceConditionalEditor_lowList_listSelectionAdapter(this));
    moderateList.setEnabled(true);
    moderateList.setPrototypeCellValue(protoTypeCellValue);
    moderateList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    moderateList.addListSelectionListener(new FireResistanceConditionalEditor_moderateList_listSelectionAdapter(this));
    highList.setEnabled(true);
    highList.setPrototypeCellValue(protoTypeCellValue);
    highList.addListSelectionListener(new FireResistanceConditionalEditor_highList_listSelectionAdapter(this));
    flowLayout9.setAlignment(FlowLayout.LEFT);
    flowLayout6.setAlignment(FlowLayout.LEFT);
    flowLayout7.setAlignment(FlowLayout.LEFT);
    flowLayout8.setAlignment(FlowLayout.LEFT);
    lowBorder.setTitle("Low");
    lowBorder.setBorder(border1);
    dropPanel.setBorder(mainDropBorder);
    lowListPanel.setBorder(lowBorder);
    moderateListPanel.setBorder(moderateBorder);
    highListPanel.setBorder(highBorder);
    sourceBorder.setTitle("Ecological Grouping Not Yet Assigned");
    moderateBorder.setTitle("Moderate");
    highBorder.setTitle("High");
//    verticalFlowLayout1.setHorizontalFill(true);
    groupScrollPanel.setLayout(flowLayout11);
    groupSourcePanel.setBorder(null);
    groupDragSourcePanel.setBorder(sourceBorder);
    flowLayout11.setAlignment(FlowLayout.LEFT);
    flowLayout11.setHgap(0);
    flowLayout11.setVgap(0);
    flowLayout10.setAlignment(FlowLayout.LEFT);
    flowLayout10.setHgap(0);
    flowLayout10.setVgap(0);
    flowLayout5.setAlignment(FlowLayout.LEFT);
    flowLayout5.setHgap(0);
    flowLayout5.setVgap(0);
    groupSourcePanel.add(groupScrollPanel, null);
    groupScrollPanel.add(groupSourceScroll, null);
    groupSourceScroll.getViewport().add(groupSourceList, null);
    getContentPane().add(panel1);
    panel1.add(groupDragSourcePanel, null);
    groupDragSourcePanel.add(groupSourcePanel, null);
    panel1.add(dropPanel, null);
    dropPanel.add(lowPanel, null);
    dropPanel.add(moderatePanel, null);
    dropPanel.add(highPanel, null);
    panel1.add(buttonPanel, null);
    buttonPanel.add(okPB, null);
    buttonPanel.add(cancelPB, null);
    lowPanel.add(lowListPanel, null);
    lowListPanel.add(lowListScroll, null);
    lowListScroll.getViewport().add(lowList, null);
    moderatePanel.add(moderateListPanel, null);
    moderateListPanel.add(moderateListScroll, null);
    moderateListScroll.getViewport().add(moderateList, null);
    highPanel.add(highListPanel, null);
    highListPanel.add(highListScroll, null);
    highListScroll.getViewport().add(highList, null);
  }
/**
 * Initializes the Fire Resistance Conditional Editor.  Sets the model based on the fire resistance either low, moderate, or high. 
 */
  private void initialize() {
    ArrayList            groups;
    ArrayList            allGroups;
    HabitatTypeGroupType group;
    boolean              hasLow, hasModerate, hasHigh;

    allGroups = HabitatTypeGroupType.getAllLoadedGroups();

    hasLow      = data.containsKey(FireResistance.LOW);
    hasModerate = data.containsKey(FireResistance.MODERATE);
    hasHigh     = data.containsKey(FireResistance.HIGH);

    int i;
    DefaultListModel model;

    lowList.setModel(new DefaultListModel());
    moderateList.setModel(new DefaultListModel());
    highList.setModel(new DefaultListModel());
    groupSourceList.setModel(new DefaultListModel());

    if (hasLow) {
      model = (DefaultListModel)lowList.getModel();
      groups = (ArrayList)data.get(FireResistance.LOW);
      for (i=0; i<groups.size(); i++) {
        group = (HabitatTypeGroupType)groups.get(i);
        model.addElement(group.toString());
        if (allGroups.contains(group)) {
          allGroups.remove(group);
        }
      }
    }
    if (hasModerate) {
      model = (DefaultListModel)moderateList.getModel();
      groups = (ArrayList)data.get(FireResistance.MODERATE);
      for (i=0; i<groups.size(); i++) {
        group = (HabitatTypeGroupType)groups.get(i);
        model.addElement(group.toString());
        if (allGroups.contains(group)) {
          allGroups.remove(group);
        }
      }
    }
    if (hasHigh) {
      model = (DefaultListModel)highList.getModel();
      groups = (ArrayList)data.get(FireResistance.HIGH);
      for (i=0; i<groups.size(); i++) {
        group = (HabitatTypeGroupType)groups.get(i);
        model.addElement(group.toString());
        if (allGroups.contains(group)) {
          allGroups.remove(group);
        }
      }
    }

    model = (DefaultListModel)groupSourceList.getModel();
    for (i=0; i<allGroups.size(); i++) {
      model.addElement(((HabitatTypeGroupType)allGroups.get(i)).toString());
    }

    ArrayListTransferHandler handler = new ArrayListTransferHandler();

    lowList.setDragEnabled(true);
    lowList.setTransferHandler(handler);

    moderateList.setDragEnabled(true);
    moderateList.setTransferHandler(handler);

    highList.setDragEnabled(true);
    highList.setTransferHandler(handler);

    groupSourceList.setDragEnabled(true);
    groupSourceList.setTransferHandler(handler);

//    lowList.setPreferredSize(groupSourceList.getPreferredSize());
//    moderateList.setPreferredSize(groupSourceList.getPreferredSize());
//    highList.setPreferredSize(groupSourceList.getPreferredSize());
    lowList.setPreferredSize(lowList.getPreferredSize());
    moderateList.setPreferredSize(moderateList.getPreferredSize());
    highList.setPreferredSize(highList.getPreferredSize());
  }

  void lowList_valueChanged(ListSelectionEvent e) {
    lowList.setPreferredSize(lowList.getPreferredSize());
  }

  void moderateList_valueChanged(ListSelectionEvent e) {
    moderateList.setPreferredSize(moderateList.getPreferredSize());
  }

  void highList_valueChanged(ListSelectionEvent e) {
    highList.setPreferredSize(highList.getPreferredSize());
  }

  void okPB_actionPerformed(ActionEvent e) {
    finishUp();
    setVisible(false);
    dispose();
  }

  void cancelPB_actionPerformed(ActionEvent e) {
    setVisible(false);
    dispose();
  }

  private void finishUp() {
    DefaultListModel model;
    ArrayList        groups;

    model = (DefaultListModel)lowList.getModel();
    if (model.size() > 0) {
      groups = (ArrayList)data.get(FireResistance.LOW);

      if (groups == null) {
        groups = new ArrayList(model.size());
        data.put(FireResistance.LOW,groups);
      }
      copyModelData(model,groups);
    }
    else {
      groups = (ArrayList)data.remove(FireResistance.LOW);
      if (groups != null) {
        groups.clear();
      }
    }

    model = (DefaultListModel)moderateList.getModel();
    if (model.size() > 0) {
      groups = (ArrayList)data.get(FireResistance.MODERATE);

      if (groups == null) {
        groups = new ArrayList(model.size());
        data.put(FireResistance.MODERATE,groups);
      }
      copyModelData(model,groups);
    }
    else {
      groups = (ArrayList)data.remove(FireResistance.MODERATE);
      if (groups != null) {
        groups.clear();
      }
    }

    model = (DefaultListModel)highList.getModel();
    if (model.size() > 0) {
      groups = (ArrayList)data.get(FireResistance.HIGH);

      if (groups == null) {
        groups = new ArrayList(model.size());
        data.put(FireResistance.HIGH,groups);
      }
      copyModelData(model,groups);
    }
    else {
      groups = (ArrayList)data.remove(FireResistance.HIGH);
      if (groups != null) {
        groups.clear();
      }
    }

  }

  private void copyModelData(DefaultListModel model, ArrayList groups) {
    groups.clear();
    for (int i=0; i<model.size(); i++) {
      groups.add(HabitatTypeGroupType.get((String)model.elementAt(i)));
    }
  }

}

class FireResistanceConditionalEditor_okPB_actionAdapter implements java.awt.event.ActionListener {
  FireResistanceConditionalEditor adaptee;

  FireResistanceConditionalEditor_okPB_actionAdapter(FireResistanceConditionalEditor adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.okPB_actionPerformed(e);
  }
}

class FireResistanceConditionalEditor_cancelPB_actionAdapter implements java.awt.event.ActionListener {
  FireResistanceConditionalEditor adaptee;

  FireResistanceConditionalEditor_cancelPB_actionAdapter(FireResistanceConditionalEditor adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.cancelPB_actionPerformed(e);
  }
}

class FireResistanceConditionalEditor_lowList_listSelectionAdapter implements javax.swing.event.ListSelectionListener {
  FireResistanceConditionalEditor adaptee;

  FireResistanceConditionalEditor_lowList_listSelectionAdapter(FireResistanceConditionalEditor adaptee) {
    this.adaptee = adaptee;
  }
  public void valueChanged(ListSelectionEvent e) {
    adaptee.lowList_valueChanged(e);
  }
}

class FireResistanceConditionalEditor_moderateList_listSelectionAdapter implements javax.swing.event.ListSelectionListener {
  FireResistanceConditionalEditor adaptee;

  FireResistanceConditionalEditor_moderateList_listSelectionAdapter(FireResistanceConditionalEditor adaptee) {
    this.adaptee = adaptee;
  }
  public void valueChanged(ListSelectionEvent e) {
    adaptee.moderateList_valueChanged(e);
  }
}

class FireResistanceConditionalEditor_highList_listSelectionAdapter implements javax.swing.event.ListSelectionListener {
  FireResistanceConditionalEditor adaptee;

  FireResistanceConditionalEditor_highList_listSelectionAdapter(FireResistanceConditionalEditor adaptee) {
    this.adaptee = adaptee;
  }
  public void valueChanged(ListSelectionEvent e) {
    adaptee.highList_valueChanged(e);
  }
}
