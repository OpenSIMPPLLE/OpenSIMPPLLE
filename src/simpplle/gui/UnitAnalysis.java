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
import java.awt.event.*;

/** 
 * This class sets up Unit Analysis, a type of JDialog.
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class UnitAnalysis extends JDialog {
  private static UnitAnalysis instance;
  public static boolean isOpen = false;

  private EluAnalysis landInstance;

  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel tabbedPanel = new JPanel();
  BorderLayout borderLayout2 = new BorderLayout();
  JTabbedPane tabbedPane = new JTabbedPane();
/**
 * Constructor for Unit Analysis.  
 * @param frame owner frame
 * @param title name of unit Analysis dialog
 * @param modal modality
 */
  public UnitAnalysis(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    this.instance = this;
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
 * Overloaded constructor.  Creates a new frame owner, sets the title to empty string and modality to true, giving access to other window containers.  
 */
  public UnitAnalysis() {
    super(new Frame(), "", true);
    try {
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    mainPanel.setLayout(borderLayout1);
    tabbedPanel.setLayout(borderLayout2);
    tabbedPane.setTabPlacement(JTabbedPane.LEFT);
    this.addWindowListener(new UnitAnalysis_this_windowAdapter(this));
    getContentPane().add(mainPanel);
    mainPanel.add(tabbedPanel, BorderLayout.NORTH);
    tabbedPanel.add(tabbedPane, BorderLayout.WEST);
  }
/**
 * Gets the current instance of Unit Analysis.
 * @return current instance
 */
  public static UnitAnalysis getInstance() { return instance; }
/**
 * Initializes the Unit Analysis dialog.  Creates a new instance of Existing Land Unit Panel and passes this analysis dialog to it.  Adds the the panel to the tabbed panel with title Land Units
 * 
 */
  private void initialize() {
    isOpen = true;
//    tabbedPane.add(new JPanel(),"Vegetation Units");
    landInstance = new EluAnalysis(this);

    tabbedPane.add(landInstance,"Land Units");
    update(getGraphics());
  }
/**
 * Checks if the current Unit Analysis dialog is open.
 * @return true if Unit Analysis dialog is open.
 */
  public static boolean isOpen() { return isOpen; }
/**
 * Gets the panel associated with this unit analysis.
 * @return Elu Analysis panel associated with this unit analysis.
 */
  public EluAnalysis getLandInstance() { return landInstance; }
/**
 * Refreshes the graphing by calling update and get graphics.  
 */
  public void refresh() {
    update(getGraphics());
  }
/**
 * Closes the Unit Analysis dialog if window closing event occurs.  
 * @param e 'X' button on window pressed
 */
  void this_windowClosing(WindowEvent e) {
    isOpen = false;
    setVisible(false);
    dispose();
  }
}
/**
 * Creates a window adapter which allows this Unit Analysis to only call a window closing event method of window listener. 
 * @author lab
 *
 */
class UnitAnalysis_this_windowAdapter extends java.awt.event.WindowAdapter {
  UnitAnalysis adaptee;

  UnitAnalysis_this_windowAdapter(UnitAnalysis adaptee) {
    this.adaptee = adaptee;
  }
  /**
   * Closes the window by calling to window closing event, which sets the isOpen variable to false, and disposes the dialog.
   */
  public void windowClosing(WindowEvent e) {
    adaptee.this_windowClosing(e);
  }
}


