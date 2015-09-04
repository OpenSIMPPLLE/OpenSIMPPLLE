
package simpplle.gui;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Insets;

import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.BoxLayout;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import simpplle.JSimpplle;
import simpplle.comcode.LogicRule;
import simpplle.comcode.Process;
import simpplle.comcode.ProcessType;
import simpplle.comcode.SimpplleType;


/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class defines the Logic Rule Basic Panel, a type of Jpanel
 * This handles many of the methods from the Logic Rule class in comcode.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source authorship: Kirk A. Moeller</p>
 * 
 */
public class LogicRuleBasicPanel extends JPanel {
  private List rules;
  private LogicRule selectedRule;
  private JDialog dialog;
  private String[] probLabels;
  private ProcessType process;

  private boolean inInit=false;

  private TitledBorder htGroupBorder = new TitledBorder("HT Groups");
  private TitledBorder speciesBorder = new TitledBorder("Species");
  private TitledBorder sizeClassBorder = new TitledBorder("Size Class");
  private TitledBorder densityBorder = new TitledBorder("Density");
  private TitledBorder processBorder = new TitledBorder("Process");
  private TitledBorder treatmentBorder = new TitledBorder("Treatment");

  private TitledBorder[] probLabelBorder;
  private JPanel mainPanel = null;
  private JPanel centerPanel = null;
  private JScrollPane rulesScrollPane = null;
  private JPanel rulesPanel = null;

  /**
   *Constructor for Logic Rule Basic Panel.  Creates a new Jpanel, and then initializes it
   */
  public LogicRuleBasicPanel() {
    super();

    initialize();
  }
/**
 * Overloaded constructor for the Logic Rule Basic Panel. Adds a dialog, and process.   
 * @param dialog
 * @param process
 */
  public LogicRuleBasicPanel(JDialog dialog, ProcessType process) {
    this();
    this.dialog  = dialog;
    this.process = process;

    myInit();
  }

  /**
   * This method initializes the logic rule base panel with layout and size.
   *
   * @return void
   */
  private void initialize() {
    this.setLayout(new BorderLayout());
    this.setSize(661, 544);
    this.add(getMainPanel(), java.awt.BorderLayout.CENTER);
  }
/**
 * This is an init method, similar to jbInit.  It gets the process logic rules and creates an array of labels with those rules. 
 * It goes through the probability labels and creates label borders for each with the string version of probability.  
 *  
 */
  private void myInit() {
    inInit=true;
    Process inst = Process.findInstance(process);

    rules = inst.getLogicRules();
    probLabels = inst.getProbabilityLabels();

    probLabelBorder = new TitledBorder[probLabels.length];
    for (int i = 0; i < probLabels.length; i++) {
      probLabelBorder[i] = new TitledBorder(probLabels[i]);
    }
    updateRules();
    inInit=false;
  }
/**
 * Calls myInit() to update all probability labels and logic rules.  
 */
  public void updateAll() {
    myInit();
  }
/**
 * Makes a title border.  This is a convenience method to make sure the titled borders stay similar.  
 * @param title
 * @return
 */
  private TitledBorder makeTitledBorder(String title) {
    TitledBorder border = new TitledBorder(BorderFactory.createEtchedBorder(
        Color.white, new Color(148, 145, 140)), title);
    border.setTitleFont(new java.awt.Font("Monospaced", 0, 12));
    return border;
  }
/**
 * Makes a line border.  This 
 * @param panel
 * @param visible
 * @return
 */
  private LineBorder makeLineBorder(JDataPanel panel, boolean visible) {
    Color color = (visible) ? Color.black : panel.getBackground();
    return new LineBorder(color);
  }
/**
 * Adds a now logic rule to Logic rules basic panel
 * @param rule
 */
  public void AddNewRule(LogicRule rule) {
    rules.add(rule);
    updateRules();
  }
  private void updateRules() {
    rulesPanel.removeAll();

    JDataPanel jPanel;
    JPanel listPanel, probPanel, probValuePanel;
    JRadioButton radio;
    ButtonGroup buttonGroup = new ButtonGroup();
    JButton editPB;
    JList jList;
    LogicRule rule;
    for (int i = 0; i < rules.size(); i++) {

      rule = (LogicRule) rules.get(i);

      jPanel = new JDataPanel(new FlowLayout(FlowLayout.LEFT), rule);
      //      jPanel.addMouseListener(new java.awt.event.MouseAdapter() {
      //
      //        public void mouseClicked(MouseEvent e) {
      //          sizeClassRuleSelected(e);
      //        }
      //      });

      radio = new JRadioButton("", (i == 0));
      radio.setToolTipText("Click to select rule");
      radio.addActionListener(new java.awt.event.ActionListener() {

        public void actionPerformed(ActionEvent e) {
          rulesRB_actionPerformed(e);
        }
      });
      buttonGroup.add(radio);
      jPanel.add(radio);

      editPB = new JButton();
      editPB.setText("Edit");
      editPB.setMargin(new Insets(1, 1, 1, 1));
      editPB.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(ActionEvent e) {
          editPB_actionPerformed(e);
        }
      });
      jPanel.add(editPB);

      if (i == 0) {
        jPanel.setBorder(makeLineBorder(jPanel, true));
        selectedRule = rule;
        //        menuSelectedEditRule.setEnabled(true);
        //        menuSelectedMoveRuleUp.setEnabled(true);
        //        menuSelectedMoveRuleDown.setEnabled(true);
        //        menuSelectedDeleteRule.setEnabled(true);
        //        menuSelectedCopyRule.setEnabled((currentEntry.sizeClasses != null));
      }

      jPanel.add(makeListPanel(rule.getGroups(), 50, htGroupBorder));
      jPanel.add(makeListPanel(rule.getSpecies(), 150, speciesBorder));
      jPanel.add(makeListPanel(rule.getSizeClasses(), 150, sizeClassBorder));
      jPanel.add(makeListPanel(rule.getDensities(), 50, densityBorder));
      jPanel.add(makeListPanel(rule.getProcesses(), 200, processBorder));
      jPanel.add(makeListPanel(rule.getTreatments(), 325, treatmentBorder));

      probPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      JProbabilityTextField field;
      for (int j = 0; j < probLabels.length; j++) {
        probValuePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        probValuePanel.setBorder(probLabelBorder[j]);

        field = new JProbabilityTextField(rule.getProbability(j), 5, rule,j);
        field.addActionListener(new java.awt.event.ActionListener() {

          public void actionPerformed(ActionEvent e) {
            prob_actionPerformed(e);
          }
        });
        field.addFocusListener(new java.awt.event.FocusAdapter() {

          public void focusLost(FocusEvent e) {
            prob_focusLost(e);
          }
        });

        probValuePanel.add(field);
        probPanel.add(probValuePanel);
      }
      jPanel.add(probPanel);

      rulesPanel.add(jPanel);
    }
    rulesPanel.invalidate();
    rulesPanel.validate();
    rulesScrollPane.revalidate();
  }
  public void prob_actionPerformed(ActionEvent e) {
    if (inInit) { return; }
    JProbabilityTextField field = (JProbabilityTextField)e.getSource();
    probabilityChanged(field);
  }
  /**
   * Handles the loss of focus from probability text field.  Passes to probability changed method.  
   * @param e loss of focus in probability text field.  
   */
  public void prob_focusLost(FocusEvent e) {
    if (inInit) { return; }
    JProbabilityTextField field = (JProbabilityTextField)e.getSource();
    probabilityChanged(field);
  }
  /**
   * Gets the data from the probability text field, casts it to a logic rule and then sets the probability, by getting the probability
   * @param field
   */
  private void probabilityChanged(JProbabilityTextField field) {
    LogicRule rule = (LogicRule)field.getDataSource();
    rule.setProbability(field.getData(),field.getProbability());

    // We don't need to know anything other than that a rule has changed.
    // the newValue, oldValue aren't used, except that they need to be
    // different to insure the event is generated.
    Runnable doPropertyChange = new Runnable() {
      public void run() {
        firePropertyChange("rule.changed",true,false);
      }
    };
    SwingUtilities.invokeLater(doPropertyChange);
//  menuFileSave.setEnabled(isSaveNeeded());
  }
  /**
   * Makes a JList panel for the parameter simpplle types.   
   * @param values
   * @param cellWidth
   * @param border
   * @return
   */
  private JPanel makeListPanel(SimpplleType[] values, int cellWidth,
      TitledBorder border) {
    JList jList = new JList();
    if (values != null) {
      jList.setListData(values);
    }
    jList.setFixedCellWidth(cellWidth);
    jList.setFont(new java.awt.Font("Monospaced", 0, 12));
    JPanel listPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    listPanel.add(jList);
    if (values != null) {
      listPanel.setBorder(border);
    }

    return listPanel;
  }
/**
 * This handles the selection of logic rules from radio buttons on panel.  
 * @param e
 */
  public void rulesRB_actionPerformed(ActionEvent e) {
    JDataPanel panel;
    Component[] panels = rulesPanel.getComponents();
    for (int i = 0; i < panels.length; i++) {
      panel = (JDataPanel) panels[i];
      panel.setBorder(makeLineBorder(panel, false));
    }

    panel = (JDataPanel) ((JComponent) e.getSource()).getParent();
    panel.setBorder(makeLineBorder(panel, true));
    selectedRule = (LogicRule) panel.getSource();

  }

  public void editPB_actionPerformed(ActionEvent e) {
    rulesRB_actionPerformed(e);
    LogicRuleBuilder dlg = new LogicRuleBuilder(JSimpplle.getSimpplleMain(),selectedRule);
    dlg.setVisible(true);
  }

  /**
   * This method initializes mainPanel, sets the layout to BorderLayout and puts the centerPanel at CENTER.  
   *
   * @return javax.swing.JPanel
   */
  private JPanel getMainPanel() {
    if (mainPanel == null) {
      mainPanel = new JPanel();
      mainPanel.setLayout(new BorderLayout());
      mainPanel.add(getCenterPanel(), java.awt.BorderLayout.CENTER);
    }
    return mainPanel;
  }

  /**
   * This method initializes centerPanel
   *
   * @return javax.swing.JPanel
   */
  private JPanel getCenterPanel() {
    if (centerPanel == null) {
      centerPanel = new JPanel();
      centerPanel.setLayout(new BorderLayout());
      centerPanel.add(getRulesScrollPane(), java.awt.BorderLayout.CENTER);
    }
    return centerPanel;
  }

  /**
   * This method initializes rulesScrollPane
   *
   * @return javax.swing.JScrollPane
   */
  private JScrollPane getRulesScrollPane() {
    if (rulesScrollPane == null) {
      rulesScrollPane = new JScrollPane();
      rulesScrollPane.setViewportView(getRulesPanel());
    }
    return rulesScrollPane;
  }

  /**
   * This method initializes rulesPanel
   *
   * @return javax.swing.JPanel
   */
  private JPanel getRulesPanel() {
    if (rulesPanel == null) {
      rulesPanel = new JPanel();
      rulesPanel.setLayout(new BoxLayout(rulesPanel, BoxLayout.Y_AXIS));
    }
    return rulesPanel;
  }
} //  @jve:decl-index=0:visual-constraint="10,10"
