/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import simpplle.JSimpplle;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/** 
 * This class display the contents of System.out in a dialog box.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */
public class ConsoleMessages extends JDialog implements Runnable {
  private volatile Thread runner;

  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JScrollPane messageScroll = new JScrollPane();
  JTextArea messageArea = new JTextArea();
  JPanel labelPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  JLabel aLabel = new JLabel();
/**
 * Constructor for Console Messages.  Sets the owner frame as parameter passed, title as title passed and modality to modality passed.  Then passes that to JDialog
 * @param frame owner
 * @param title	title of JDialog
 * @param modal modality
 */
  public ConsoleMessages(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try  {
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
    initialize();
  }
/**
 * Overloaded constructor passes in null for owner, empty string for title, false for modality.  
 */
  public ConsoleMessages() {
    this(null, "", false);
  }
/**
 * Sets the rows, columns, colors, and layout for Console messages.
 * @throws Exception
 */
  void jbInit() throws Exception {
    mainPanel.setLayout(borderLayout1);
    messageArea.setColumns(80);
    messageArea.setRows(15);
    messageArea.setBackground(Color.white);
    messageArea.setSelectionColor(Color.blue);
    messageArea.setEditable(false);
    labelPanel.setLayout(flowLayout1);
    this.addWindowListener(new java.awt.event.WindowAdapter() {


      public void windowClosing(WindowEvent e) {
        this_windowClosing(e);
      }
    });
    aLabel.setFont(new java.awt.Font("Dialog", 1, 14));
    aLabel.setText("");
    getContentPane().add(mainPanel);
    mainPanel.add(messageScroll, BorderLayout.CENTER);
    mainPanel.add(labelPanel, BorderLayout.NORTH);
    labelPanel.add(aLabel, null);
    messageScroll.getViewport().add(messageArea, null);
  }
/**
 * Initializes the Console Messages dialog.  Redirects the system IO.  Creates a new thread passes in the dialog and starts.
 */
  public void initialize() {
    JSimpplle.redirectSystemIO();
    System.out.println("System.out and System.err messages follow:");
    updateDialog();
    runner = new Thread(this);
    runner.start();
  }

  private void updateDialog() {
    messageArea.setText(JSimpplle.getSystemOutBuffer());
    messageArea.setCaretPosition(messageArea.getText().length() - 1);
//    update(getGraphics());
  }
/**
 * Run method to run threads and update dialog.
 */
  public void run() {
    while (runner != null) {
      updateDialog();
      try {
//        runner.sleep(2000);
        Thread.sleep(2000);
      }
      catch (InterruptedException e) {}
    }
  }
/**
 * Stop method to halt threads by creating a temporary thread and transfering in the running thread.  Setting the running thread to stop 
 * and interrupting the temporary thread.  
 */
  public void stop() {
    Thread tmp = runner;
    runner = null;
    tmp.interrupt();
  }
  
  /**
   * Updates console messages dialog if refresh button called.  
   * @param e 'refresh'
   */

  void refreshPB_actionPerformed(ActionEvent e) {
    updateDialog();
  }
/**
 * Stops the console messages thread, restores the normal system IO sets the dialog to not visible and disposes when window closing event occurs.
 * 
 * @param e window closing event
 */
  void this_windowClosing(WindowEvent e) {
    stop();
    JSimpplle.restoreNormalSystemIO();
    setVisible(false);
    dispose();
  }
}
