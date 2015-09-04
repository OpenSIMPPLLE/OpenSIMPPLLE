package simpplle.gui;

import simpplle.comcode.SystemKnowledge;
import simpplle.comcode.RegionalZone;
import simpplle.JSimpplle;
import simpplle.comcode.SimpplleError;
import simpplle.comcode.Simpplle;

import java.io.*;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;

/**
 *
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>Class that creates a dialog for choosing the directory and copying GIS, ArcView, ArcGis, coverage, geodatabase files.  
 * The three choices for sample area are no longer available.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller 
 *   
 */

public class CopyGis extends JDialog {
  private File destDir;

  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel choicesMainPanel = new JPanel();
  JPanel checkBoxPanel = new JPanel();
  GridLayout gridLayout1 = new GridLayout();
  JCheckBox copyArcviewCB = new JCheckBox();
  JCheckBox copyE00CB = new JCheckBox();
  JCheckBox copyGeodatabaseCB = new JCheckBox();
  JCheckBox copyCoverageCB = new JCheckBox();
  JCheckBox copyArcGISCB = new JCheckBox();
  JPanel destDirMainPanel = new JPanel();
  BorderLayout borderLayout2 = new BorderLayout();
  FlowLayout flowLayout1 = new FlowLayout();
  JPanel destDirPanel = new JPanel();
  FlowLayout flowLayout2 = new FlowLayout();
  JButton destDirSelectPB = new JButton();
  JTextField destDirText = new JTextField();
  TitledBorder titledBorder1;
  JPanel southPanel = new JPanel();
  JButton cancelPB = new JButton();
  JButton okPB = new JButton();
/**
 * Constructor for CopyGIS.  This dialog provides methods to get and use GIS information from GIS file.  
 * @param frame owner frame
 * @param title title of JDialog
 * @param modal	modality 
 */
  public CopyGis(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try {
      jbInit();
      pack();
      destDir = JSimpplle.getWorkingDir();
      destDirText.setText(destDir.toString());
      okPB.setEnabled(true);

      copyCoverageCB.setEnabled(SystemKnowledge.existsGISExtras());
      copyE00CB.setEnabled(SystemKnowledge.existsGISExtras());
      copyGeodatabaseCB.setEnabled(SystemKnowledge.existsGISExtras());
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
/**
 * Overloaded constructor.  Sets the owner as null, title to empty, and modality to false.  
 *   
 */
  public CopyGis() {
    this(null, "", false);
  }
  /**
   * Borders, panels, layouts, text, and components are set.
   * @throws Exception
   */
  private void jbInit() throws Exception {
    titledBorder1 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Destination Directory");
    mainPanel.setLayout(borderLayout1);
    checkBoxPanel.setLayout(gridLayout1);
    gridLayout1.setRows(5);
    copyArcviewCB.setSelected(true);
    copyArcviewCB.setText("Copy Arcview Files");
    copyE00CB.setText("Copy ArcInfo Interchange File for Sample Area");
    copyGeodatabaseCB.setText("Copy ArcGIS Geodatabase for Sample Area");
    copyCoverageCB.setText("Copy Coverage for Sample Area");
    copyArcGISCB.setSelected(true);
    copyArcGISCB.setText("Copy ArcGIS Files");
    choicesMainPanel.setLayout(borderLayout2);
    destDirMainPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    destDirPanel.setLayout(flowLayout2);
    flowLayout2.setAlignment(FlowLayout.LEFT);
    destDirSelectPB.setText("Select");
    destDirSelectPB.addActionListener(new CopyGis_destDirSelectPB_actionAdapter(this));
    destDirText.setFont(new java.awt.Font("MS Sans Serif", 0, 11));
    destDirText.setText("");
    destDirText.setColumns(40);
    destDirPanel.setBorder(titledBorder1);
    cancelPB.setText("Cancel");
    cancelPB.addActionListener(new CopyGis_cancelPB_actionAdapter(this));
    okPB.setText("Ok");
    okPB.addActionListener(new CopyGis_okPB_actionAdapter(this));
    choicesMainPanel.add(destDirMainPanel,  BorderLayout.SOUTH);
    destDirMainPanel.add(destDirPanel, null);
    destDirPanel.add(destDirText, null);
    destDirPanel.add(destDirSelectPB, null);
    getContentPane().add(mainPanel);
    mainPanel.add(choicesMainPanel,  BorderLayout.NORTH);
    choicesMainPanel.add(checkBoxPanel,  BorderLayout.NORTH);
    checkBoxPanel.add(copyArcviewCB, null);
    checkBoxPanel.add(copyArcGISCB, null);
    checkBoxPanel.add(copyCoverageCB, null);
    checkBoxPanel.add(copyGeodatabaseCB, null);
    checkBoxPanel.add(copyE00CB, null);
    mainPanel.add(southPanel,  BorderLayout.SOUTH);
    southPanel.add(okPB, null);
    southPanel.add(cancelPB, null);
  }
/**
 * Creates a file chooser to allow user to select destination directory.  If the choosen directory is ok it will set the destination directory to that.  
 * @param e destination directory select button pushed
 */
  void destDirSelectPB_actionPerformed(ActionEvent e) {
    JFileChooser chooser = new JFileChooser(JSimpplle.getWorkingDir().getParentFile());
    RegionalZone zone = Simpplle.getCurrentZone();

    chooser.setDialogTitle("Select the the Destination directory and press Ok.");
    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    chooser.setSelectedFile(JSimpplle.getWorkingDir());
    int returnVal = chooser.showDialog(this,"Ok");
    if(returnVal == JFileChooser.APPROVE_OPTION) {
      destDir = chooser.getSelectedFile();
      destDirText.setText(destDir.toString());
    }
    update(getGraphics());
    okPB.setEnabled((destDir != null));
  }
/**
 *If OK button is pushed copies the GIS Files at the specifiec directory either copy ArcView, copy ArcGIS, copy Coverage, copy Geodatabase, copy Interchange File
 * @param e
 */
  void okPB_actionPerformed(ActionEvent e) {
    JSimpplle.getSimpplleMain().setWaitState("Copying GIS Files to : " + destDir.toString());

    try {
      if (copyArcviewCB.isSelected()) {
        Simpplle.getCurrentZone().copyGisFiles(destDir);
      }
      if (copyArcGISCB.isSelected()) {
        Simpplle.getCurrentZone().copyArcGISFiles(destDir);
      }
      if (copyCoverageCB.isSelected()) {
        SystemKnowledge.copyCoverage(destDir);
      }
      if (copyGeodatabaseCB.isSelected()) {
        SystemKnowledge.copyGeodatabase(destDir);
      }
      if (copyE00CB.isSelected()) {
        SystemKnowledge.copyInterchangeFile(destDir);
      }
    }
    catch (SimpplleError ex) {
      JOptionPane.showMessageDialog(this,ex.getMessage(),"Copy Failed",JOptionPane.ERROR_MESSAGE);
      JSimpplle.getSimpplleMain().setNormalState();
      return;
    }

    JSimpplle.getSimpplleMain().setNormalState();
    setVisible(false);
    dispose();
  }
/***
 * If cancel button is pushed sets the dialog to not visible than disposes.  
 */
  void cancelPB_actionPerformed(ActionEvent e) {
    setVisible(false);
    dispose();
  }


}
/**
 * Action adapter used when destination director select button is pushed.  
 */
class CopyGis_destDirSelectPB_actionAdapter implements java.awt.event.ActionListener {
  CopyGis adaptee;

  CopyGis_destDirSelectPB_actionAdapter(CopyGis adaptee) {
    this.adaptee = adaptee;
  }
  /**
   * Invoked if destination directory button pushed.  
   */
  public void actionPerformed(ActionEvent e) {
    adaptee.destDirSelectPB_actionPerformed(e);
  }
}
/**
 * Action adapter used when ok button is pushed.  
 */
class CopyGis_okPB_actionAdapter implements java.awt.event.ActionListener {
  CopyGis adaptee;

  CopyGis_okPB_actionAdapter(CopyGis adaptee) {
    this.adaptee = adaptee;
  }
  /**
   * Ok button pushed. 
   */
  public void actionPerformed(ActionEvent e) {
    adaptee.okPB_actionPerformed(e);
  }
}
/**
 * Action adapter used when cancel button is pushed.  
 */

class CopyGis_cancelPB_actionAdapter implements java.awt.event.ActionListener {
  CopyGis adaptee;

  CopyGis_cancelPB_actionAdapter(CopyGis adaptee) {
    this.adaptee = adaptee;
  }
  /**
   * Cancel button is pushed.
   */
  public void actionPerformed(ActionEvent e) {
    adaptee.cancelPB_actionPerformed(e);
  }
}