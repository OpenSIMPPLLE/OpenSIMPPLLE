package simpplle.gui;

import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import simpplle.comcode.FireSpreadLogicData;
import simpplle.comcode.FireEvent;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class defines Position Logic Chooser, a type of JDialog.  This deals with the position of one EVU to another EVU.  
 * The choices are next to , below, or above.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source authorship: Kirk A. Moeller</p>
 * 
 */
public class LogicPositionChooser extends JDialog {
  private FireSpreadLogicData logicData;

  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel cbPanel = new JPanel();
  JCheckBox nextToCB = new JCheckBox();
  JCheckBox belowCB = new JCheckBox();
  JCheckBox aboveCB = new JCheckBox();
  GridLayout cbGridLayout = new GridLayout();
/**
 * Constructor for Position Logic Chooser.  Sets the dialog owner, title, modality, and logic data to passed in parameters.
 * @param owner
 * @param title
 * @param modal
 * @param logicData
 */
  public LogicPositionChooser(JDialog owner, String title, boolean modal,
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
 * Overloaded Constructor for Position Logic Chooser.  Makes a new dialog as owner for this dialog, sets title and modality to false, and logic data to null.
 */
  public LogicPositionChooser() {
    this(new JDialog(), "LogicPositionChooser", false,null);
  }

  private void jbInit() throws Exception {
    mainPanel.setLayout(borderLayout1);
    nextToCB.setText("Next To");
    belowCB.setText("Below");
    aboveCB.setText("Above");
    cbPanel.setLayout(cbGridLayout);
    cbGridLayout.setRows(3);
    this.addWindowListener(new LogicPositionChooser_this_windowAdapter(this));
    getContentPane().add(mainPanel);
    mainPanel.add(cbPanel, java.awt.BorderLayout.NORTH);
    cbPanel.add(aboveCB);
    cbPanel.add(belowCB);
    cbPanel.add(nextToCB);
    }
/**
 * Initializes the Position Logic Chooser dialog combo boxes for above, below, and next to. 
 */
    private void initialize() {
      aboveCB.setSelected(logicData.hasPosition(FireEvent.ABOVE));
      belowCB.setSelected(logicData.hasPosition(FireEvent.BELOW));
      nextToCB.setSelected(logicData.hasPosition(FireEvent.NEXT_TO));
    }
    /**
     * Method to handle window closing event.  This will be called from the window adapter created in this class.  
     * If any of the three combo boxes are selected add the corresponding position to the logic data, and remove all others.  
     * @param e
     */
  public void this_windowClosing(WindowEvent e) {
    if (aboveCB.isSelected()) { logicData.addPosition(FireEvent.ABOVE); }
    else { logicData.removePosition(FireEvent.ABOVE); }

    if (belowCB.isSelected()) { logicData.addPosition(FireEvent.BELOW); }
    else { logicData.removePosition(FireEvent.BELOW); }

    if (nextToCB.isSelected()) { logicData.addPosition(FireEvent.NEXT_TO); }
    else { logicData.removePosition(FireEvent.NEXT_TO); }

    setVisible(false);
    dispose();

  }
}
/**
 * Window adapter to handle window closing events.  
 *
 */
class LogicPositionChooser_this_windowAdapter extends WindowAdapter {
  private LogicPositionChooser adaptee;
  LogicPositionChooser_this_windowAdapter(LogicPositionChooser adaptee) {
    this.adaptee = adaptee;
  }
/**
 * Handles the window closing event .  
 * Calls this_WindowClosing().  If any of the three combo boxes are selected add the corresponding position to the logic data, and remove all others. 
 */
  public void windowClosing(WindowEvent e) {
    adaptee.this_windowClosing(e);
  }
}
