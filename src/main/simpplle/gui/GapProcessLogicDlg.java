package simpplle.gui;

import java.awt.Frame;
import javax.swing.JDialog;
import simpplle.comcode.GapProcessLogic;
import simpplle.comcode.*;

/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class creates the GapProcess Logic Dialog, a type of VegLogicDialog.  
 * Gap Processes contains columns for Priority, Processes, Gap Processes, and Probability.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *   
 */
public class GapProcessLogicDlg extends VegLogicDialog {
  /**
   * Constructor for GapProcessLogicDlg.  This sets the frame owner, string title and modality.  
   * @param owner frame that owns the dialog
   * @param title name of dialog
   * @param modal specifies whether dialog blocks user input to other top-level windows when shown
   */
	public GapProcessLogicDlg(Frame owner, String title, boolean modal) {
    super(owner, title, modal);
    try {
      setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      jbInit();
      initialize();
      pack();
    }
    catch (Exception exception) {
      exception.printStackTrace();
    }
  }
/**
 * Overloaded GapProcessLogicDlg constructor.  
 */
  public GapProcessLogicDlg() {
    this(new Frame(), "GapProcessLogicDlg", false);
  }


  private void jbInit() throws Exception {
  }
/**
 * Initializes the Gap Process Logic Dialog with system knowledge. 
 */
  private void initialize() {
    sysKnowKind = SystemKnowledge.GAP_PROCESS_LOGIC;
    String[] kinds = new String[] {GapProcessLogic.GAP_PROCESSES.toString()};
    super.initialize(kinds);

    tabPanels = new GapProcessLogicPanel[panelKinds.length];
    for (int i = 0; i < panelKinds.length; i++) {
      String kind = panelKinds[i];
      tabPanels[i] =
          new GapProcessLogicPanel(this, kind,
                                      GapProcessLogic.getInstance(),
                                      sysKnowKind);
      tabbedPane.add(tabPanels[i], kind);
    }

    tabbedPane.setSelectedIndex(0);
    tabbedPane_stateChanged(null);
    updateDialog();
  }

}

