/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import java.awt.Frame;
import simpplle.comcode.RegenerationDelayLogic;
import simpplle.comcode.*;

/**
 * This class creates the Regeneration Delay Logic Dialog, a type of VegLogicDialog.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class RegenDelayLogicDlg extends VegLogicDialog {
	  /**
	   * Constructor for Regeneration Delay LogicDlg.  This sets the frame owner, string title and modality.  
	   * @param owner frame that owns the dialog
	   * @param title name of dialog
	   * @param modal specifies whether dialog blocks user input to other top-level windows when shown
	   */
	
	public RegenDelayLogicDlg(Frame owner, String title, boolean modal) {
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
	 * Overloaded RegenDelayLogicDlg constructor. Creates a new frame as owner, sets the title to ProducingSeedLogicDlg and sets modality to modeless. 
	 */
  public RegenDelayLogicDlg() {
    this(new Frame(), "RegenDelayLogicDlg", false);
  }

/**
 * Inits the dialog.  
 * @throws Exception
 */
  private void jbInit() throws Exception {
  }
  /**
   * Initializes the Regeneration Delay Logic Dialog with producing seed logic system knowledge. then initializes the dialog with Jmenu items for 
   *  regeneration delay logic to the VegLogicDialog base columns and sets this.panel producing seeds tostring which is then used to create tabbed panels.  
   */
  private void initialize() {
    sysKnowKind = SystemKnowledge.REGEN_DELAY_LOGIC;
    String[] kinds = new String[] {RegenerationDelayLogic.REGEN_DELAY.toString()};
    super.initialize(kinds);

    tabPanels = new RegenDelayLogicPanel[panelKinds.length];
    for (int i = 0; i < panelKinds.length; i++) {
      String kind = panelKinds[i];
      tabPanels[i] =
          new RegenDelayLogicPanel(this, kind,
                                      RegenerationDelayLogic.getInstance(),
                                      sysKnowKind);
      tabbedPane.add(tabPanels[i], kind);
    }

    tabbedPane.setSelectedIndex(0);
    tabbedPane_stateChanged(null);
    updateDialog();
  }

}

