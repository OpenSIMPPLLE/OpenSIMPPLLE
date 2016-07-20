/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import simpplle.comcode.ConiferEncroachmentLogicData;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import simpplle.comcode.SystemKnowledge;
import java.io.*;

/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class defines the GUI for Conifer Encroachment Dialog, a type of JDialog. 
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class ConiferEncroachmentDialog extends JDialog {
  private ConiferEncroachmentPanel coniferEncroachmentPanel = new ConiferEncroachmentPanel();

  private JPanel mainPanel = new JPanel();
  private BorderLayout borderLayout1 = new BorderLayout();
  JMenuBar menuBar = new JMenuBar();
  JMenu jMenu1 = new JMenu();
  JMenuItem menuFileOpen = new JMenuItem();
  JMenuItem menuFileClose = new JMenuItem();
  JMenuItem menuFileSave = new JMenuItem();
  JMenuItem menuFileSaveAs = new JMenuItem();
  JMenuItem menuFileLoadDefaults = new JMenuItem();
  JMenuItem menuFileQuit = new JMenuItem();
/**
 * Primary constructor for Conifer Encroachment JDialog.  Inherits from JDialog superclass, then calls jbInit, and initialize
 * @param frame owner
 * @param title the name of conifer encroachment dialog
 * @param modal true if modality enabled
 */
  public ConiferEncroachmentDialog(Frame frame, String title, boolean modal) {
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
 * Overloaded constructor for Conifer Encroachment JDialog.  Calls to primary constructor and passes null for owner, empty string for name
 * and false for modal.  
 */
  public ConiferEncroachmentDialog() {
    this(null, "", false);
  }
  /**
   * sets the panel, menu text, action listeners, 
   * @throws Exception
   */
  private void jbInit() throws Exception {
    mainPanel.setLayout(borderLayout1);
    this.addWindowListener(new ConiferEncroachmentDialog_this_windowAdapter(this));
    this.setJMenuBar(menuBar);
    jMenu1.setText("File");
    menuFileOpen.setText("Open");
    menuFileOpen.addActionListener(new ConiferEncroachmentDialog_menuFileOpen_actionAdapter(this));
    menuFileClose.setEnabled(false);
    menuFileClose.setText("Close");
    menuFileClose.addActionListener(new ConiferEncroachmentDialog_menuFileClose_actionAdapter(this));
    menuFileSave.setEnabled(false);
    menuFileSave.setText("Save");
    menuFileSave.addActionListener(new ConiferEncroachmentDialog_menuFileSave_actionAdapter(this));
    menuFileSaveAs.setText("Save As");
    menuFileSaveAs.addActionListener(new ConiferEncroachmentDialog_menuFileSaveAs_actionAdapter(this));
    menuFileLoadDefaults.setText("Load Defaults");
    menuFileLoadDefaults.addActionListener(new ConiferEncroachmentDialog_menuFileLoadDefaults_actionAdapter(this));
    menuFileQuit.setText("Close Dialog");
    menuFileQuit.addActionListener(new ConiferEncroachmentDialog_menuFileQuit_actionAdapter(this));
    getContentPane().add(mainPanel);
    menuBar.add(jMenu1);
    jMenu1.add(menuFileOpen);
    jMenu1.add(menuFileClose);
    jMenu1.addSeparator();
    jMenu1.add(menuFileSave);
    jMenu1.add(menuFileSaveAs);
    jMenu1.addSeparator();
    jMenu1.add(menuFileLoadDefaults);
    jMenu1.addSeparator();
    jMenu1.add(menuFileQuit);
  }
/**
 * Adds the conifer encroachment panel and updates.
 */
  private void initialize() {
    mainPanel.add(coniferEncroachmentPanel, BorderLayout.CENTER);
    updateDialog();
  }
  /**
   * Sets the Conifer Encroachment panel with result of calling to conifer encroament logic data.  
   */
  private void updateDialog() {
    coniferEncroachmentPanel.initialize(ConiferEncroachmentLogicData.getTimeValues());
  }
  /**
   * If window closing event occurs quites the ConiferEncroachmentDialog.  
   * @param e window closing X choosen.
   */
  
  void this_windowClosing(WindowEvent e) {
    quit();
  }
  /**
   * If menu item quit is selected, this disposes the  ConiferEncroachmentDialog. 
   * @param e 'quit' selected
   */
  void menuFileQuit_actionPerformed(ActionEvent e) {
    quit();
    setVisible(false);
    dispose();
  }
  /**
   * Quits by setting Conifer Encroachment time values to Conifer encroachment values.  
   */
  private void quit() {
    ConiferEncroachmentLogicData.setTimeValues(coniferEncroachmentPanel.getValues());
  }
/**
 * Opens file after menu file open item selected, 
 * @param e
 */
  void menuFileOpen_actionPerformed(ActionEvent e) {
    SystemKnowledgeFiler.openFile(this, SystemKnowledge.CONIFER_ENCROACHMENT,
                                  menuFileSave,
                                  menuFileClose);
    update(getGraphics());
  }
/**
 * Saves this ConiferEncroachmentDialog to the existent conifer encroachment file if the 'save' menu file is selected. 
 * @param e
 */
  void menuFileSave_actionPerformed(ActionEvent e) {
    File outfile = SystemKnowledge.getFile(SystemKnowledge.CONIFER_ENCROACHMENT);
    SystemKnowledgeFiler.saveFile(this, outfile, SystemKnowledge.SPECIES,
                                  menuFileSave, menuFileClose);
    update(getGraphics());
  }
  /**
   * Sends to the system knowledge filer this dialog, and the type of system knowledge as conifer encroachment.  There it will make a new 
   * conifer encroachment file and save.  
   * @param e 'save as'
   */
  void menuFileSaveAs_actionPerformed(ActionEvent e) {
    SystemKnowledgeFiler.saveFile(this, SystemKnowledge.CONIFER_ENCROACHMENT,
                                  menuFileSave,
                                  menuFileClose);
    update(getGraphics());
  }
/**
 * Closes the current conifer encroachment dialog.  It loads the defaults and updates.  
 * @param e 'close'
 */
  void menuFileClose_actionPerformed(ActionEvent e) {
    loadDefaults();
    update(getGraphics());
  }
  /**
   * Loads the default data for conifer encroachment if load default menu item is choosen.  
   * @param e 'load defaults'
   */
  void menuFileLoadDefaults_actionPerformed(ActionEvent e) {
    loadDefaults();
    update(getGraphics());
  }
  /**
   * Method to load defaults for conifer encroachment data.  A JOptionPane pops up with warning message.  If the user selects YES
   * option the conifer encroachment zone default data will load.  
   */
  private void loadDefaults() {
    int choice;
    try {
      String msg = "This will load the default Conifer Encroachment Data.\n\n" +
                   "Do you wish to continue?";
      String title = "Load Default Conifer Encroachment Data";

      if (Utility.askYesNoQuestion(this,msg,title)) {
        SystemKnowledge.readZoneDefault(SystemKnowledge.CONIFER_ENCROACHMENT);
      }
    }
    catch (simpplle.comcode.SimpplleError err) {
      JOptionPane.showMessageDialog(this,err.getError(),"Unable to load file",
                                    JOptionPane.ERROR_MESSAGE);
    }
  }
}
/**
 * 
 *Creates a window adapter for conifer encroachment dialog specifically to close the window if window event closing is selected.  
 *
 */
class ConiferEncroachmentDialog_this_windowAdapter extends java.awt.event.WindowAdapter {
  ConiferEncroachmentDialog adaptee;

  ConiferEncroachmentDialog_this_windowAdapter(ConiferEncroachmentDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void windowClosing(WindowEvent e) {
    adaptee.this_windowClosing(e);
  }
}

class ConiferEncroachmentDialog_menuFileOpen_actionAdapter implements java.awt.event.ActionListener {
  ConiferEncroachmentDialog adaptee;

  ConiferEncroachmentDialog_menuFileOpen_actionAdapter(ConiferEncroachmentDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.menuFileOpen_actionPerformed(e);
  }
}

class ConiferEncroachmentDialog_menuFileClose_actionAdapter implements java.awt.event.ActionListener {
  ConiferEncroachmentDialog adaptee;

  ConiferEncroachmentDialog_menuFileClose_actionAdapter(ConiferEncroachmentDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.menuFileClose_actionPerformed(e);
  }
}

class ConiferEncroachmentDialog_menuFileSave_actionAdapter implements java.awt.event.ActionListener {
  ConiferEncroachmentDialog adaptee;

  ConiferEncroachmentDialog_menuFileSave_actionAdapter(ConiferEncroachmentDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.menuFileSave_actionPerformed(e);
  }
}

class ConiferEncroachmentDialog_menuFileSaveAs_actionAdapter implements java.awt.event.ActionListener {
  ConiferEncroachmentDialog adaptee;

  ConiferEncroachmentDialog_menuFileSaveAs_actionAdapter(ConiferEncroachmentDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.menuFileSaveAs_actionPerformed(e);
  }
}

class ConiferEncroachmentDialog_menuFileLoadDefaults_actionAdapter implements java.awt.event.ActionListener {
  ConiferEncroachmentDialog adaptee;

  ConiferEncroachmentDialog_menuFileLoadDefaults_actionAdapter(ConiferEncroachmentDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.menuFileLoadDefaults_actionPerformed(e);
  }
}

class ConiferEncroachmentDialog_menuFileQuit_actionAdapter implements java.awt.event.ActionListener {
  ConiferEncroachmentDialog adaptee;

  ConiferEncroachmentDialog_menuFileQuit_actionAdapter(ConiferEncroachmentDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.menuFileQuit_actionPerformed(e);
  }
}