/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import simpplle.comcode.Simpplle;

/**
 * This class displays a dialog giving the user
 * a couple of options related to report output format
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */
public class ReportOption extends JDialog {
  public static final int NO_SELECTION = -1;

  private int selection;
  private boolean combineLifeforms;

  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel commaDelimitedPanel = new JPanel();
  JPanel buttonPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  JButton cancelPB = new JButton();
  JButton continuePB = new JButton();
  GridLayout gridLayout1 = new GridLayout();
  JPanel optionPanel1 = new JPanel();
  JPanel formattedPanel = new JPanel();
  GridLayout gridLayout2 = new GridLayout();
  TitledBorder titledBorder1;
  TitledBorder titledBorder2;
  GridLayout gridLayout3 = new GridLayout();
  JRadioButton formattedOwnerSpecialRB = new JRadioButton();
  JRadioButton formattedSpecialAreaRB = new JRadioButton();
  JRadioButton formattedOwnershipRB = new JRadioButton();
  JRadioButton formattedRB = new JRadioButton();
  JRadioButton spreadsheetOwnerSpecialRB = new JRadioButton();
  JRadioButton spreadsheetSpecialAreaRB = new JRadioButton();
  JRadioButton spreadsheetOwnershipRB = new JRadioButton();
  JRadioButton spreadsheetRB = new JRadioButton();
  private JCheckBox formattedCombineLifeforms = new JCheckBox();
  private JCheckBox spreadsheetCombineLifeforms = new JCheckBox();

  public ReportOption(Frame frame, String title, boolean modal) {
    this(frame,title,modal,true);
  }

  public ReportOption(Frame frame, String title, boolean modal, boolean basic) {
    super(frame, title, modal);
    try  {
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
    selection = NO_SELECTION;

    if (basic) {
      formattedOwnershipRB.setVisible(false);
      formattedSpecialAreaRB.setVisible(false);
      formattedOwnerSpecialRB.setVisible(false);
      spreadsheetOwnershipRB.setVisible(false);
      spreadsheetSpecialAreaRB.setVisible(false);
      spreadsheetOwnerSpecialRB.setVisible(false);
      formattedCombineLifeforms.setVisible(false);
      spreadsheetCombineLifeforms.setVisible(false);
    }
    else {
      formattedOwnershipRB.setVisible(true);
      formattedSpecialAreaRB.setVisible(true);
      formattedOwnerSpecialRB.setVisible(true);
      spreadsheetOwnershipRB.setVisible(true);
      spreadsheetSpecialAreaRB.setVisible(true);
      spreadsheetOwnerSpecialRB.setVisible(true);
      formattedCombineLifeforms.setVisible(true);
      spreadsheetCombineLifeforms.setVisible(true);
    }
    combineLifeforms = false;
    formattedCombineLifeforms.setSelected(false);
    spreadsheetCombineLifeforms.setSelected(false);
  }

  public ReportOption() {
    this(null, "", false);
  }

  void jbInit() throws Exception {
    titledBorder1 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Formatted Output");
    titledBorder2 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Comma Delimited (speadsheet) Output");
    mainPanel.setLayout(borderLayout1);
    buttonPanel.setLayout(flowLayout1);
    cancelPB.setText("Cancel");
    cancelPB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        cancelPB_actionPerformed(e);
      }
    });
    continuePB.setNextFocusableComponent(cancelPB);
    continuePB.setSelected(true);
    continuePB.setText("Continue");
    continuePB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        continuePB_actionPerformed(e);
      }
    });
    commaDelimitedPanel.setLayout(gridLayout1);
    gridLayout1.setHgap(1);
    gridLayout1.setRows(5);
    optionPanel1.setLayout(gridLayout2);
    optionPanel1.setBorder(BorderFactory.createEtchedBorder());
    this.setTitle("Choose an Option");
    gridLayout2.setRows(2);
    formattedPanel.setBorder(titledBorder1);
    formattedPanel.setLayout(gridLayout3);
    commaDelimitedPanel.setBorder(titledBorder2);
    gridLayout3.setRows(5);
    formattedOwnerSpecialRB.setText("Ownership and Special Area");
    formattedSpecialAreaRB.setText("Special Area");
    formattedOwnershipRB.setText("Ownership");
    formattedRB.setSelected(true);
    formattedRB.setText("Standard");
    formattedRB.setToolTipText("");
    spreadsheetOwnerSpecialRB.setText("Ownership and Special Area               ");
    spreadsheetSpecialAreaRB.setText("Special Area");
    spreadsheetOwnershipRB.setText("Ownership");
    spreadsheetRB.setNextFocusableComponent(continuePB);
    spreadsheetRB.setText("Standard");
    formattedCombineLifeforms.setText("Combine Lifeforms");
    spreadsheetCombineLifeforms.setText("CombineLifeforms");
    getContentPane().add(mainPanel);
    mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    buttonPanel.add(continuePB, null);
    buttonPanel.add(cancelPB, null);
    mainPanel.add(optionPanel1, BorderLayout.NORTH);
    optionPanel1.add(formattedPanel, null);
    formattedPanel.add(formattedRB, null);
    formattedPanel.add(formattedOwnershipRB, null);
    formattedPanel.add(formattedSpecialAreaRB, null);
    formattedPanel.add(formattedOwnerSpecialRB, null);
    formattedPanel.add(formattedCombineLifeforms);
    optionPanel1.add(commaDelimitedPanel, null);
    commaDelimitedPanel.add(spreadsheetRB, null);
    commaDelimitedPanel.add(spreadsheetOwnershipRB, null);
    commaDelimitedPanel.add(spreadsheetSpecialAreaRB, null);
    commaDelimitedPanel.add(spreadsheetOwnerSpecialRB, null);
    commaDelimitedPanel.add(spreadsheetCombineLifeforms);

    // Place radio buttons in a group
    ButtonGroup group = new ButtonGroup();
    group.add(formattedRB);
    group.add(formattedOwnershipRB);
    group.add(formattedSpecialAreaRB);
    group.add(formattedOwnerSpecialRB);
    group.add(spreadsheetRB);
    group.add(spreadsheetOwnershipRB);
    group.add(spreadsheetSpecialAreaRB);
    group.add(spreadsheetOwnerSpecialRB);
  }
  /*
    // Saving this code here because above function is generated
    // and thus this custom code could get wiped out.
    ButtonGroup group = new ButtonGroup();
    group.add(formattedRB);
    group.add(formattedOwnershipRB);
    group.add(formattedSpecialAreaRB);
    group.add(formattedOwnerSpecialRB);
    group.add(spreadsheetRB);
    group.add(spreadsheetOwnershipRB);
    group.add(spreadsheetSpecialAreaRB);
    group.add(spreadsheetOwnerSpecialRB);
  */

  public int getSelection() { return selection; }

  public boolean isCombineLifeforms() {
    return combineLifeforms;
  }

  void continuePB_actionPerformed(ActionEvent e) {
    if (formattedRB.isSelected()) {
      selection = Simpplle.FORMATTED;
      combineLifeforms = formattedCombineLifeforms.isSelected();
    }
    else if (formattedOwnershipRB.isSelected()) {
      selection = Simpplle.FORMATTED_OWNERSHIP;
      combineLifeforms = formattedCombineLifeforms.isSelected();
    }
    else if (formattedSpecialAreaRB.isSelected()) {
      selection = Simpplle.FORMATTED_SPECIAL_AREA;
      combineLifeforms = formattedCombineLifeforms.isSelected();
    }
    else if (formattedOwnerSpecialRB.isSelected()) {
      selection = Simpplle.FORMATTED_OWNER_SPECIAL;
      combineLifeforms = formattedCombineLifeforms.isSelected();
    }
    else if (spreadsheetRB.isSelected()) {
      selection = Simpplle.CDF;
      combineLifeforms = spreadsheetCombineLifeforms.isSelected();
    }
    else if (spreadsheetOwnershipRB.isSelected()) {
      selection = Simpplle.CDF_OWNERSHIP;
      combineLifeforms = spreadsheetCombineLifeforms.isSelected();
    }
    else if (spreadsheetSpecialAreaRB.isSelected()) {
      selection = Simpplle.CDF_SPECIAL_AREA;
      combineLifeforms = spreadsheetCombineLifeforms.isSelected();
    }
    else if (spreadsheetOwnerSpecialRB.isSelected()) {
      selection = Simpplle.CDF_OWNER_SPECIAL;
      combineLifeforms = spreadsheetCombineLifeforms.isSelected();
    }
    else {
      selection = NO_SELECTION;
      combineLifeforms = false;
    }
    setVisible(false);
    dispose();
  }

  void cancelPB_actionPerformed(ActionEvent e) {
    selection = NO_SELECTION;
    setVisible(false);
    dispose();
  }
}
