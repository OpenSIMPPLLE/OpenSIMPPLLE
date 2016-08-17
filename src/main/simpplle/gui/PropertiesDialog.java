/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import simpplle.JSimpplle;

import java.io.*;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * Dialog used to manage settings stored in the opensimpplle.properties file.
 */

public class PropertiesDialog extends JDialog {

  private File workingDir;

  private JPanel mainPanel = new JPanel();
  private JPanel choicesMainPanel = new JPanel();
  private JPanel checkBoxPanel = new JPanel();
  private JPanel southPanel = new JPanel();
  private JPanel destDirMainPanel = new JPanel();
  private JPanel destDirPanel = new JPanel();

  private BorderLayout borderLayout1 = new BorderLayout();
  private BorderLayout borderLayout2 = new BorderLayout();
  private FlowLayout flowLayout1 = new FlowLayout();
  private FlowLayout flowLayout2 = new FlowLayout();
  private GridLayout gridLayout1 = new GridLayout();

  private JCheckBox simulationLoggingCB = new JCheckBox();
  private JCheckBox invasiveMSUCB = new JCheckBox();
  private JButton destDirSelectPB = new JButton();
  private JTextField destDirText = new JTextField();
  private JButton closeBtn = new JButton();
  private JButton saveBtn = new JButton();
  private TitledBorder titledBorder1;

/**
 * Constructor for CopyGIS.  This dialog provides methods to get and use GIS information from GIS file.  
 * @param frame owner frame
 * @param title title of JDialog
 * @param modal	modality 
 */
  public PropertiesDialog(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try {
      jbInit();
      pack();
      workingDir = JSimpplle.getWorkingDir();
      destDirText.setText(workingDir.toString());
      saveBtn.setEnabled(true);

    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
/**
 * Overloaded constructor.  Sets the owner as null, title to empty, and modality to false.  
 */
  public PropertiesDialog() {
    this(null, "", false);
  }
  /**
   * Borders, panels, layouts, text, and components are set.
   */
  private void jbInit() throws Exception {
    titledBorder1 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Set Working Directory");
    mainPanel.setLayout(borderLayout1);
    checkBoxPanel.setLayout(gridLayout1);
    gridLayout1.setRows(3);

    simulationLoggingCB.setSelected(JSimpplle.getProperties().isSimulationLogging());
    simulationLoggingCB.setText("Do Detailed Simulation Logging");
    simulationLoggingCB.addActionListener(e -> simulationLoggingAction());

    invasiveMSUCB.setSelected(JSimpplle.getProperties().isInvasiveMSU());
    invasiveMSUCB.setText("Use MSU Invasive Species Logic");
    invasiveMSUCB.addActionListener(e -> invasiveMSUAction());

    choicesMainPanel.setLayout(borderLayout2);
    destDirMainPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    destDirPanel.setLayout(flowLayout2);
    flowLayout2.setAlignment(FlowLayout.LEFT);
    destDirSelectPB.setText("Change");
    destDirSelectPB.addActionListener(e -> setWorkingDirectoryAction());
    destDirText.setFont(new java.awt.Font("MS Sans Serif", 0, 11));
    destDirText.setText("");
    destDirText.setColumns(40);
    destDirPanel.setBorder(titledBorder1);
    closeBtn.setText("Close");
    closeBtn.addActionListener(e -> closeAction());
    choicesMainPanel.add(destDirMainPanel,  BorderLayout.SOUTH);
    destDirMainPanel.add(destDirPanel, null);
    destDirPanel.add(destDirText, null);
    destDirPanel.add(destDirSelectPB, null);
    getContentPane().add(mainPanel);
    mainPanel.add(choicesMainPanel,  BorderLayout.NORTH);
    choicesMainPanel.add(checkBoxPanel,  BorderLayout.NORTH);
    checkBoxPanel.add(simulationLoggingCB, null);
    checkBoxPanel.add(invasiveMSUCB, null);
    mainPanel.add(southPanel,  BorderLayout.SOUTH);
    southPanel.add(closeBtn, null);
  }

  /**
   * Creates a file chooser to allow user to change the working directory.
   */
  private void setWorkingDirectoryAction() {
    JFileChooser chooser = new JFileChooser(JSimpplle.getWorkingDir());
    chooser.setDialogTitle("Change The Working Directory and Press OK");
    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    chooser.setSelectedFile(JSimpplle.getWorkingDir());
//    chooser.setApproveButtonToolTipText(msg);
    int returnVal = chooser.showDialog(this,"Ok");

    if(returnVal == JFileChooser.APPROVE_OPTION) {
      // Valid directory, update dialog and change properties
      workingDir = chooser.getSelectedFile();
      destDirText.setText(this.workingDir.toString());
      JSimpplle.setWorkingDir(workingDir);
    }
    update(getGraphics());
    saveBtn.setEnabled((this.workingDir != null));
  }

  /**
   * Called when the simulationLoggingCB changes value. Sets the System property to the current
   * value of the checkbox.
   */
  private void simulationLoggingAction(){
    JSimpplle.getProperties().setSimulationLogging(simulationLoggingCB.isSelected());
  }

  /**
   * Called when the invasiveMSUCB changes value. Sets the System property to the current
   * value of the checkbox.
   */
  private void invasiveMSUAction(){
    JSimpplle.getProperties().setInvasiveMSU(invasiveMSUCB.isSelected());
  }

  /**
   * If cancel button is pushed sets the dialog to not visible than disposes.
   */
  private void closeAction() {
    setVisible(false);
    dispose();
  }
}
