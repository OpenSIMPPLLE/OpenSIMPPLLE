package simpplle.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.GridLayout;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.Dimension;
import simpplle.comcode.*;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class creates the JFrame for Memory Display.  It informs users of memory usage.
 * @author Documentation by Brian Losi
 * <p>Original source authorship: Kirk A. Moeller
 * 
 *     
 */
public class MemoryDisplay extends JFrame implements Runnable  {
  private volatile Thread runner;

  JPanel jPanel1 = new JPanel();
  JPanel jPanel2 = new JPanel();
  GridLayout gridLayout1 = new GridLayout();
  JLabel memoryUsedLabel = new JLabel();
  JLabel jLabel2 = new JLabel();
  JLabel memoryAvailableLabel = new JLabel();
  JLabel jLabel4 = new JLabel();
  FlowLayout flowLayout1 = new FlowLayout();
  FlowLayout flowLayout2 = new FlowLayout();
/**
 * Constructor for memory display.  It creates a new thread containing this class, and starts it.  
 */
  public MemoryDisplay() {
    try {
      jbInit();
      runner = new Thread(this);
      runner.start();
    }
    catch (Exception exception) {
      exception.printStackTrace();
    }
  }
/**
 * Init method which sets the size, listeners, contentPane, borders, colors, and components for Memory Display
 * @throws Exception
 */
  private void jbInit() throws Exception {
    getContentPane().setLayout(gridLayout1);
    this.setSize(new Dimension(250, 100));
    this.addWindowListener(new MemoryDisplay_this_windowAdapter(this));
    this.getContentPane().add(jPanel1, null);
    jLabel2.setFont(new java.awt.Font("Monospaced", Font.BOLD, 14));
    jLabel2.setForeground(Color.blue);
    jLabel2.setText("Memory Used     ");
    memoryAvailableLabel.setText("jLabel3      ");
    jLabel4.setFont(new java.awt.Font("Monospaced", Font.BOLD, 14));
    jLabel4.setForeground(Color.blue);
    jLabel4.setText("Memory Available");
    jPanel1.setLayout(flowLayout1);
    jPanel2.setLayout(flowLayout2);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    flowLayout2.setAlignment(FlowLayout.LEFT);
    jPanel1.setBorder(BorderFactory.createEtchedBorder());
    jPanel2.setBorder(BorderFactory.createEtchedBorder());
    jPanel1.add(jLabel2);
    jPanel1.add(memoryUsedLabel);
    memoryUsedLabel.setText("jLabel1       ");
    gridLayout1.setRows(2);
    this.getContentPane().add(jPanel2, null);
    jPanel2.add(jLabel4);
    jPanel2.add(memoryAvailableLabel);
  }
/**
 * Main Memory Display method.  Creates a new instance of memory display.  
 * @param args
 */
  public static void main(String[] args) {
    MemoryDisplay memorydisplay = new MemoryDisplay();
  }
/**
 * Updates the display with memory used calculated by total memory - free memory, and max memory. 
 */
  private void updateDisplay() {
    long freeMem = Runtime.getRuntime().freeMemory();
    long totMem = Runtime.getRuntime().totalMemory();
    long maxMem  = Runtime.getRuntime().maxMemory();

    long used = ((totMem-freeMem) / 1024) / 1024;
    maxMem  = (maxMem / 1024) / 1024;
    
    
    memoryUsedLabel.setText(Long.toString((int)used));
    memoryAvailableLabel.setText(Long.toString((int)maxMem));
    update(getGraphics());
  }
  /**
   * The required run() method of the runnable interface.  Updates the display while the thread is going every 6 seconds.  
   */
  public void run() {
    while (runner != null) {
      updateDisplay();
      try {
        Thread.sleep(6000);
      }
      catch (InterruptedException e) {}
    }
  }
/**
 * Stops the runner thread, by setting to a temporary thread, nulling the current thread and then interrupting the temp thread.  
 */
  public void stop() {
    Thread tmp = runner;
    runner = null;
    tmp.interrupt();
  }
/**
 * Handles window closing event by stopping thread, setting the frame to not visible, and disposing of it.  
 * @param e
 */
  public void this_windowClosing(WindowEvent e) {
    stop();
    setVisible(false);
    dispose();
  }
}
/**
 * Creates a window adapter to handle the window closing event.  This saves work in implementing window listeners.  
 * It passes to method within Memory Display class.  
 *
 */
class MemoryDisplay_this_windowAdapter extends WindowAdapter {
  private MemoryDisplay adaptee;
  MemoryDisplay_this_windowAdapter(MemoryDisplay adaptee) {
    this.adaptee = adaptee;
  }

  public void windowClosing(WindowEvent e) {
    adaptee.this_windowClosing(e);
  }
}
