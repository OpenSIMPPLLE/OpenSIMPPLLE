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
import javax.swing.border.*;
import java.awt.event.*;
import java.io.*;
import simpplle.JSimpplle;
import java.util.ArrayList;
import simpplle.comcode.Simulation;

/** 
 * This class defines the dialog for Choose Simulation, a type of JDialog.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class ChooseSimulation extends JDialog {
  private File directory = null;
  private File areaFile = null;
  private File[] simdataFiles = null;
  private File simdataFile=null;
  private boolean recreateMrSummary=false;

  private JPanel mainPanel = new JPanel();
  private BorderLayout borderLayout1 = new BorderLayout();
  JPanel northPanel = new JPanel();
  JPanel directoryPanel = new JPanel();
  BorderLayout borderLayout2 = new BorderLayout();
  JButton directoryPB = new JButton();
  JPanel centerPanel = new JPanel();
  JPanel simListPanel = new JPanel();
  BorderLayout borderLayout3 = new BorderLayout();
  JScrollPane listScrollPane = new JScrollPane();
  JList simList = new JList();
  TitledBorder titledBorder1;
  JPanel jPanel1 = new JPanel();
  JTextField directoryText = new JTextField();
  BorderLayout borderLayout4 = new BorderLayout();
  JCheckBox mrSummaryCB = new JCheckBox();
/**
 * Primary construction for Choose Simulation GUI, sets frame owner, title, and modality
 * @param frame owner frame
 * @param title name of frame
 * @param modal 
 */
  public ChooseSimulation(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try {
      jbInit();
      initialize();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
/**
 * Overloaded constructor.  Calls primary and initializes owner to null, title to empty and modality to false. 
 */
  public ChooseSimulation() {
    this(null, "", false);
  }
  private void jbInit() throws Exception {
    titledBorder1 = new TitledBorder(new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(148, 145, 140)),"Choose a Simulation to Load.");
    mainPanel.setLayout(borderLayout1);
    northPanel.setLayout(borderLayout2);
    directoryPB.setText("Simulation Directory..");
    directoryPB.addActionListener(new ChooseSimulation_directoryPB_actionAdapter(this));
    directoryPanel.setLayout(new BoxLayout(directoryPanel, BoxLayout.Y_AXIS));
    centerPanel.setLayout(borderLayout3);
    simListPanel.setLayout(borderLayout4);
    simListPanel.setBorder(titledBorder1);
    titledBorder1.setTitle("Double-click a Simulation to Load.");
    simList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    simList.addMouseListener(new ChooseSimulation_simList_mouseAdapter(this));
    this.addWindowListener(new ChooseSimulation_this_windowAdapter(this));
    directoryText.setBackground(Color.white);
    directoryText.setEnabled(true);
    directoryText.setFont(new java.awt.Font("Dialog", 1, 12));
    directoryText.setForeground(Color.blue);
    directoryText.setDisabledTextColor(Color.blue);
    directoryText.setEditable(true);
    directoryText.setText("Please choose a directory with Simulation Data");
    directoryText.addKeyListener(new ChooseSimulation_directoryText_keyAdapter(this));
    mrSummaryCB.setText("Re-Create Multiple Run Summary Data (last run only!)");
    getContentPane().add(mainPanel);
    mainPanel.add(northPanel,  BorderLayout.NORTH);
    northPanel.add(directoryPanel, BorderLayout.CENTER);
    directoryPanel.add(jPanel1, null);
    jPanel1.add(directoryPB, null);
    directoryPanel.add(directoryText, null);
    directoryPanel.add(mrSummaryCB);
    mainPanel.add(centerPanel, BorderLayout.CENTER);
    centerPanel.add(simListPanel, BorderLayout.CENTER);
    simListPanel.add(listScrollPane, BorderLayout.CENTER);
    listScrollPane.getViewport().add(simList, null);
  }
/**
 * Method to get the area files needed, if any exist, in the choose simulation GUI.  The file for area is gotten using 
 * the sim data file.
 * @return array of Files containing area file and simulation data file
 */
  public File[] getFiles() {
    if (simdataFile == null) { return null; }

    areaFile = Simulation.findSimulationAreaFile(simdataFile);
    if (areaFile != null && areaFile.exists() == false) {
      JOptionPane.showMessageDialog(this, "Unable to find file: " + areaFile,
                                    "", JOptionPane.ERROR_MESSAGE);
      return null;
    }
    return new File[] { areaFile, simdataFile};
  }
/**
 * checks if the recreate muliple run summary should be done
 * @return true to recreated multiple run summary.
 */
  public boolean recreateMrSummary() { return recreateMrSummary; }
/**
 * Initializes the choose simulation by calling updateDialog. 
 */
  public void initialize() {
    updateDialog();
  }
/**
 * Gets the working directory (this is set in regional zone), makes an arraylist of files from simulation data files.  If files are null, user prompted to pick a new directory.
 * else area file is gotten at index 0.  Simulation data files are gotten and put into an array of sim data files, and their description
 * is put into a data array.  Graphics are then called. 
 */
  public void updateDialog() {
    if (directory == null) {
      directory = JSimpplle.getWorkingDir();
    }

    ArrayList files = Simulation.findSimulationDataFiles(directory);

    if (files == null || files.size() == 0) {
      simList.removeAll();
      simList.setEnabled(false);
      areaFile = null;
      simdataFiles = null;
//      directoryText.setColumns(50);
      directoryText.setText("Please choose a directory with Simulation Data");
      setSize(getPreferredSize());
      update(getGraphics());
      return;
    }
    directoryText.setText(directory.toString());

    areaFile = (File)files.get(0);
    simdataFiles = new File[files.size()-1];
    String[] data = new String[simdataFiles.length];
    for (int i=1; i<files.size(); i++) {
      simdataFiles[i-1] = (File)files.get(i);
      data[i-1] = Simulation.getDataFileDescription(simdataFiles[i-1]);
    }
    simList.setListData(data);
    simList.setEnabled(true);

//    this.invalidate();
//    this.validate();
//    directoryText.setSize(directoryText.getPreferredSize());
//    setSize(getPreferredSize());
    update(getGraphics());
  }
/**
 * Handles when the "Simulation Directory.." button is pushed
 * @param e
 */
  void directoryPB_actionPerformed(ActionEvent e) {
    JFileChooser chooser = new JFileChooser(JSimpplle.getWorkingDir());
    String       msg = "Select the new diretory with simulation files and press Ok.";

    chooser.setDialogTitle(msg);
    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    chooser.setApproveButtonToolTipText(msg);
    int returnVal = chooser.showDialog(this,"Ok");
    if(returnVal == JFileChooser.APPROVE_OPTION) {
      directory = chooser.getSelectedFile();
    }
    updateDialog();
  }
/**
 * handles the mouse event when a click takes place on the simulation option and multiple run summary is selected list
 * @param e
 */
  void simList_mouseClicked(MouseEvent e) {
    if (areaFile == null || simdataFiles == null) { return; }

    if (e.getClickCount() == 2 && simList.isSelectionEmpty() == false) {
      simdataFile = simdataFiles[simList.getSelectedIndex()];
      recreateMrSummary = mrSummaryCB.isSelected();
      setVisible(false);
      dispose();
    }
  }
/**
 * If window closing event occurs, closes the JDialog for ChooseSimulation and sets area rules, simdata Files and the simdata file to null, then 
 * disposes the ChooseSimulation dialog.
 * @param e window closing X pushed.
 */
  void this_windowClosing(WindowEvent e) {
    areaFile = null;
    simdataFiles = null;
    simdataFile = null;
    setVisible(false);
    dispose();
  }
  /**
   * Keys allowed are left, right, home, and end.  Will beep otherwise.  
   * @param e
   */

  void directoryText_keyTyped(KeyEvent e) {
    char key = e.getKeyChar();
    if (key != KeyEvent.VK_LEFT && key != KeyEvent.VK_RIGHT &&
        key != KeyEvent.VK_HOME && key != KeyEvent.VK_END) {
      e.consume();
      java.awt.Toolkit.getDefaultToolkit().beep();
    }
  }


}

/**
 *This is an action adaptor to handle the "Simulation Directory.." button is pushed. It is a convenience method which makes it easier to 
 *write action listeners.   
 *
 */
class ChooseSimulation_directoryPB_actionAdapter implements java.awt.event.ActionListener {
  ChooseSimulation adaptee;

  ChooseSimulation_directoryPB_actionAdapter(ChooseSimulation adaptee) {
    this.adaptee = adaptee;
  }
  /**
   * Calls directoryPB_actionPerformed(e) which then handles the "Simulation Directory.." button is pushed event.  
   */
  public void actionPerformed(ActionEvent e) {
    adaptee.directoryPB_actionPerformed(e);
  }
}
/**
 *This is a mouse adaptor to handle mouse double click in simulation list. It is a convenience method which makes it easier to 
 *write mouse  listeners.   
 *
 */
class ChooseSimulation_simList_mouseAdapter extends java.awt.event.MouseAdapter {
  ChooseSimulation adaptee;

  ChooseSimulation_simList_mouseAdapter(ChooseSimulation adaptee) {
    this.adaptee = adaptee;
  }
  /**
   * Calls simList_mouseClicked(e) within ChooseSimulation class which handles double click in choose simulation list.  
   */
  public void mouseClicked(MouseEvent e) {
    adaptee.simList_mouseClicked(e);
  }
}
/**
 *This is an window adaptor to handle the "Simulation Directory.." button is pushed. It is a convenience method which makes it easier to 
 *write window listeners.   
 *
 */
class ChooseSimulation_this_windowAdapter extends java.awt.event.WindowAdapter {
  ChooseSimulation adaptee;

  ChooseSimulation_this_windowAdapter(ChooseSimulation adaptee) {
    this.adaptee = adaptee;
  }
  /**
   * Calls this_windowClosing(e) method in ChooseSimulation class when window closing event occurs.  
   */
  public void windowClosing(WindowEvent e) {
    adaptee.this_windowClosing(e);
  }
}
/**
 *This is an key adaptor to handle typing in the directory text field. It is a convenience method which makes it easier to 
 *write key listeners.   
 *
 */
class ChooseSimulation_directoryText_keyAdapter extends java.awt.event.KeyAdapter {
  ChooseSimulation adaptee;

  ChooseSimulation_directoryText_keyAdapter(ChooseSimulation adaptee) {
    this.adaptee = adaptee;
  }
  /**
   * Calls directoryPB_actionPerformed(e) in the ChooseSimulation class which handles the typing in directory text field.   
   */
  public void keyTyped(KeyEvent e) {
    adaptee.directoryText_keyTyped(e);
  }
}
