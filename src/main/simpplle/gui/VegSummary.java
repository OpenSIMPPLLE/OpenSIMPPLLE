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
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.event.*;
import simpplle.comcode.FireReports;
import simpplle.comcode.SimpplleType;
import static simpplle.comcode.SimpplleType.*;
import simpplle.comcode.*;

/**
 * This class creates a dialog to display the vegetative summary report.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class VegSummary extends JDialog {
  private SimpplleType.Types state;  // will have one of the above values.
  private boolean combineLifeforms;

  private simpplle.comcode.Simpplle comcode;

  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JTextArea textArea = new JTextArea();
  JButton closeButton = new JButton();
  JPanel buttonPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  JPanel textPanel = new JPanel();
  JPanel radioIndentPanel = new JPanel();
  JPanel radioPanel = new JPanel();
  GridLayout gridLayout1 = new GridLayout();
  JRadioButton processRB = new JRadioButton();
  JRadioButton densityRB = new JRadioButton();
  JRadioButton sizeClassRB = new JRadioButton();
  JRadioButton speciesRB = new JRadioButton();
  TitledBorder titledBorder1;
  FlowLayout flowLayout3 = new FlowLayout();
  JScrollPane textAreaScroll = new JScrollPane();
  GridLayout gridLayout2 = new GridLayout();
  JRadioButton treatmentRB = new JRadioButton();
  JRadioButton fireEventRB = new JRadioButton();
  private JMenuBar menuBar = new JMenuBar();
  private JMenu menuOptions = new JMenu();
  private JCheckBoxMenuItem menuOptionCombineLifeforms = new JCheckBoxMenuItem();
/**
 * Constructor for Vegetative Summary dialog, setting the frame owner, title, and modality.
 * @param frame
 * @param title
 * @param modal
 */
  public VegSummary(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try  {
      jbInit();
      initialize();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
/**
 * Overloaded Vegetative Summary dialog.  Sets the owner to null, title to empty string, and modality to false.  
 */
  public VegSummary() {
    this(null, "", false);
  }
/**
 * Initializes the getting the area summary, setting the state to null, combine life forms to false, and process radio buttons to selected.  
 */
  private void initialize() {
    if (Simulation.getInstance() == null) {
      AreaSummary.newAreaSummaryTemp();
    }
    state = null;
    combineLifeforms = false;
    menuOptionCombineLifeforms.setSelected(combineLifeforms);
    processRB.setSelected(true);
    processRB_actionPerformed(null);
  }
/**
 * Initializes the JDialog with borders, components, panels, colors, and layouts.  This also sets up the radio buttons and their action listeners at the top of the panel which 
 * are used to tailor the summary to a particular type.  The button choices are processes, specis, size class, density, and treatmens.  
 * @throws Exception
 */
  void jbInit() throws Exception {
    titledBorder1 = new TitledBorder("");
    mainPanel.setLayout(borderLayout1);
    textArea.setColumns(100);
    textArea.setRows(30);
    textArea.setBackground(Color.white);
    textArea.setSelectionColor(Color.blue);
    textArea.setEditable(false);
    textArea.setFont(new java.awt.Font("Monospaced", 0, 12));
    closeButton.setNextFocusableComponent(processRB);
    closeButton.setText("Close");
    closeButton.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        closeButton_actionPerformed(e);
      }
    });
    buttonPanel.setLayout(flowLayout1);
    textPanel.setLayout(gridLayout2);
    radioIndentPanel.setLayout(flowLayout3);
    radioPanel.setLayout(gridLayout1);
    gridLayout1.setColumns(6);
    gridLayout1.setHgap(2);
    processRB.setNextFocusableComponent(speciesRB);
    processRB.setText("Processes");
    processRB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        processRB_actionPerformed(e);
      }
    });
    densityRB.setNextFocusableComponent(treatmentRB);
    densityRB.setText("Density");
    densityRB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        densityRB_actionPerformed(e);
      }
    });
    sizeClassRB.setNextFocusableComponent(densityRB);
    sizeClassRB.setText("Size Class");
    sizeClassRB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        sizeClassRB_actionPerformed(e);
      }
    });
    speciesRB.setNextFocusableComponent(sizeClassRB);
    speciesRB.setText("Species");
    speciesRB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        speciesRB_actionPerformed(e);
      }
    });
    radioPanel.setBorder(BorderFactory.createEtchedBorder());
    flowLayout3.setAlignment(FlowLayout.LEFT);
    flowLayout3.setHgap(10);
    textAreaScroll.setAutoscrolls(true);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    gridLayout2.setColumns(1);
    treatmentRB.setNextFocusableComponent(closeButton);
    treatmentRB.setText("Treatments");
    treatmentRB.setActionCommand("Treatment");
    treatmentRB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        treatmentRB_actionPerformed(e);
      }
    });
    fireEventRB.setText("Fire Events");
    fireEventRB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        fireEventRB_actionPerformed(e);
      }
    });
    menuOptions.setText("Options");
    menuOptionCombineLifeforms.setEnabled(false);
    menuOptionCombineLifeforms.setText("Combine Lifeforms");
    menuOptionCombineLifeforms.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuOptionCombineLifeforms_actionPerformed(e);
      }
    });
    getContentPane().add(mainPanel);
    mainPanel.add(buttonPanel, BorderLayout.NORTH);
    buttonPanel.add(radioIndentPanel, null);
    radioIndentPanel.add(radioPanel, null);
    radioPanel.add(processRB, null);
    radioPanel.add(speciesRB, null);
    radioPanel.add(sizeClassRB, null);
    radioPanel.add(densityRB, null);
    radioPanel.add(treatmentRB, null);
    radioPanel.add(fireEventRB, null);
    buttonPanel.add(closeButton, null);
    mainPanel.add(textPanel, BorderLayout.CENTER);
    textPanel.add(textAreaScroll, null);
    textAreaScroll.getViewport().add(textArea, null);

    // Place radio buttons in a group
    ButtonGroup group = new ButtonGroup();
    group.add(processRB);
    group.add(speciesRB);
    group.add(sizeClassRB);
    group.add(densityRB);
    group.add(treatmentRB);
    group.add(fireEventRB);
    menuBar.add(menuOptions);
    menuOptions.add(menuOptionCombineLifeforms);
    this.setJMenuBar(menuBar);
  }
  /**
   * If process radio button selected, calls to doStateReport to produce the vegetative shapes size class report.
   * This calls the comcodes processReports class and produces a string version of the report.
   * his calls the comcode's state reports method to produce this report 
   * @param e 'Size Class'
   */
  void processRB_actionPerformed(ActionEvent e) {
    simpplle.comcode.ProcessReports report;
    String                          processStr;

    if (state != PROCESS) {
      state      = PROCESS;
      report     = new simpplle.comcode.ProcessReports();
      processStr = report.getSummaryReport();
      textArea.setText(processStr);
      textArea.setCaretPosition(0);
      menuOptionCombineLifeforms.setEnabled(false);
    }
  }
  /**
   * If species radio button selected, calls to doStateReport to produce the vegetative shapes species report.
   * This calls the comcode's state reports method to produce this report 
   * @param e 'Species'
   */
  void speciesRB_actionPerformed(ActionEvent e) {
    if (state != SPECIES) {
      state = SPECIES;
      doStateReport(state);
      menuOptionCombineLifeforms.setEnabled(true);
    }
  }
  /**
   * If size class radio button selected, calls to doStateReport to produce the vegetative shapes size class report.
   * This calls the comcode's state reports method to produce this report 
   * @param e 'Size Class'
   */
  void sizeClassRB_actionPerformed(ActionEvent e) {
    if (state != SIZE_CLASS) {
      state = SIZE_CLASS;
      doStateReport(state);
      menuOptionCombineLifeforms.setEnabled(true);
    }
  }
  /**
   * If density radio button selected, calls to doStateReport to produce the vegetative shapes density report.
   * This calls the comcode's state reports method to produce this report.  
   *  
   * @see  simpplle.comcode.StateReports  
   * @param e 'Density'
   */
  void densityRB_actionPerformed(ActionEvent e) {
    if (state != DENSITY) {
      state = DENSITY;
      doStateReport(state);
      menuOptionCombineLifeforms.setEnabled(true);
    }
  }
/**
 * If treatments radio button selected, calls to comcodes treatment reports class to produce the treatment report.  
 * @param e 'Treatments'
 */
  void treatmentRB_actionPerformed(ActionEvent e) {
    simpplle.comcode.TreatmentReports report;
    String                            reportStr;

    if (state != TREATMENT) {
      state      = TREATMENT;
      report     = new simpplle.comcode.TreatmentReports();
      reportStr  = report.getSummaryReport();
      textArea.setText(reportStr);
      textArea.setCaretPosition(0);
      menuOptionCombineLifeforms.setEnabled(false);
    }
  }
  /**
   * If fire event radio button is selected.  Calls to FireReports class in comcode to produce the Fire Reports. 
   * @param e 'Fire Events'
   */
  void fireEventRB_actionPerformed(ActionEvent e) {
    FireReports report;
    String                            reportStr;

    if (state != null) {
      state = null;
      report     = new FireReports();
      reportStr  = report.getFireEventReport();
      textArea.setText(reportStr);
      textArea.setCaretPosition(0);
      menuOptionCombineLifeforms.setEnabled(false);
    }
  }
/**
 * This is called when combine life forms is choosen.  It calls to the comcode StateReports class.  
 * @see  simpplle.comcode.StateReports
 * @param kind the simpplle types kind.  choices are SPECIES, SIZE_CLASS, DENSITY, PROCESS, TREATMENT, GROUP
 */
  private void doStateReport(SimpplleType.Types kind) {
    simpplle.comcode.StateReports report;
    String                        data = "";

    report = new simpplle.comcode.StateReports();
    data = report.getStateReport(kind,combineLifeforms);
    textArea.setText(data);
    textArea.setCaretPosition(0);
  }
/**
 * Disposes the Vegetative Summary analysis dialog if close button is pushed.
 * @param e 'Close'
 */
  void closeButton_actionPerformed(ActionEvent e) {
    setVisible(false);
    dispose();
  }
/**
 * Calls to make a report for multiple life forms summary report. 
 * @param e
 */
  public void menuOptionCombineLifeforms_actionPerformed(ActionEvent e) {
    combineLifeforms = menuOptionCombineLifeforms.isSelected();
    doStateReport(state);
  }


}
