package simpplle.gui;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import simpplle.comcode.logic.FireSpreadLogicData;
import simpplle.comcode.ProcessType;
import simpplle.comcode.Process;
import java.util.ArrayList;
/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class contains methods to handle Origin Fire Process Logic Chooser , a type of JDialog.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller</p>
 * 
 *
 */

public class LogicOriginFireProcessChooser extends JDialog {
  private FireSpreadLogicData logicData;

  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel cbPanel = new JPanel();
  JCheckBox lsfCB = new JCheckBox();
  JCheckBox msfCB = new JCheckBox();
  JCheckBox srfCB = new JCheckBox();
  GridLayout cbGridLayout = new GridLayout();
/**
 * Constructof for Logic Origin Fire Process Chooser.  Sets the dialog owner, title, and modality 
 * @param owner the dialog that owns this dialog
 * @param title title of this dialog
 * @param modal
 * @param logicData 
 */
  public LogicOriginFireProcessChooser(JDialog owner, String title, boolean modal,
                              FireSpreadLogicData logicData) {
    super(owner, title, modal);
    try {
      setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      this.logicData = logicData;
      jbInit();
      initialize();
      pack();
    }
    catch (Exception exception) {
      exception.printStackTrace();
    }
  }
/**
 * Overloaded constructor. References primary constructor and passes origin a new dialog as owner, the title as Origin Process, modality as false, and null logic data. 
 * 
 */
  public LogicOriginFireProcessChooser() {
    this(new JDialog(), "Origin Process", false,null);
  }
/**
 * Initializes Origin Fire Process Logic Chooser.  Sets the text, layout, listeners, and components. 
 * @throws Exception
 */
  private void jbInit() throws Exception {
    mainPanel.setLayout(borderLayout1);
    lsfCB.setText("Light Severity Fire");
    msfCB.setText("Mixed Severity Fire");
    srfCB.setText("Stand Replacing Fire");
    cbPanel.setLayout(cbGridLayout);
    cbGridLayout.setRows(3);
    this.addWindowListener(new LogicOriginFireProcessChooser_this_windowAdapter(this));
    getContentPane().add(mainPanel);
    mainPanel.add(cbPanel, java.awt.BorderLayout.NORTH);
    cbPanel.add(srfCB);
    cbPanel.add(msfCB);
    cbPanel.add(lsfCB);
    }
/**
 * Initializes the combo boxes and visible combo boxes for origin fire process types.  
 */
    private void initialize() {
      srfCB.setSelected(logicData.hasOriginProcess(ProcessType.SRF));
      msfCB.setSelected(logicData.hasOriginProcess(ProcessType.MSF));
      lsfCB.setSelected(logicData.hasOriginProcess(ProcessType.LSF));

      ArrayList<String> fireProcesses = Process.getFireSpreadUIProcesses(false);
      srfCB.setVisible(fireProcesses.contains("SRF"));
      msfCB.setVisible(fireProcesses.contains("MSF"));
      lsfCB.setVisible(fireProcesses.contains("LSF"));
    }
    /**
     * Handles the window closing event.  Will add a fire origin process type (SRF, MSF, or LSF) and eliminate all others from the fire origin process arraylist.   
     * @param e window closing event.  
     */
  public void this_windowClosing(WindowEvent e) {
    if (srfCB.isSelected()) { logicData.addOriginProcess(ProcessType.SRF); }
    else { logicData.removeOriginProcess(ProcessType.SRF); }

    if (msfCB.isSelected()) { logicData.addOriginProcess(ProcessType.MSF); }
    else { logicData.removeOriginProcess(ProcessType.MSF); }

    if (lsfCB.isSelected()) { logicData.addOriginProcess(ProcessType.LSF); }
    else { logicData.removeOriginProcess(ProcessType.LSF); }

    setVisible(false);
    dispose();

  }
}
/**
 * 
 * Window adapter to simplify handling of window closing event.  
 *
 */
class LogicOriginFireProcessChooser_this_windowAdapter extends WindowAdapter {
  private LogicOriginFireProcessChooser adaptee;
  LogicOriginFireProcessChooser_this_windowAdapter(LogicOriginFireProcessChooser adaptee) {
    this.adaptee = adaptee;
  }
/**
 * Window closing event will pass to internal this_windowClosing(e) which adds and subtracts processes from the origin fire process arraylist. 
 */
  public void windowClosing(WindowEvent e) {
    adaptee.this_windowClosing(e);
  }
}
