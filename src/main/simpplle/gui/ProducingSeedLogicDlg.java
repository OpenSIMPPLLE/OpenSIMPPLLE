package simpplle.gui;

import java.awt.Frame;
import simpplle.comcode.*;

/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class creates the Producing Seed Logic Dialog, a type of VegLogicDialog.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *   
 */
public class ProducingSeedLogicDlg extends VegLogicDialog {
	  /**
	   * Constructor for Producing Seed LogicDlg.  This sets the frame owner, string title and modality.  
	   * @param owner frame that owns the dialog
	   * @param title name of dialog
	   * @param modal specifies whether dialog blocks user input to other top-level windows when shown
	   */
  public ProducingSeedLogicDlg(Frame owner, String title, boolean modal) {
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
   * Overloaded GapProcessLogicDlg constructor. Creates a new frame as owner, sets the title to ProducingSeedLogicDlg and sets modality to modeless. 
   */
  public ProducingSeedLogicDlg() {
    this(new Frame(), "ProducingSeedLogicDlg", false);
  }
  /**
   * Inits the dialog, (sets nothing)
   * @throws Exception
   */
  private void jbInit() throws Exception {

  }
/**
 * Initializes the Producing Seed Logic Dialog with producing seed logic system knowledge. then initializes the dialog with Jmenu items for 
 *  producing seed logic to the VegLogicDialog base columns and sets this.panel producing seeds tostring which is then used to create tabbed panels.  
 */
  private void initialize() {
    sysKnowKind = SystemKnowledge.PRODUCING_SEED_LOGIC;
    String[] kinds = new String[] {ProducingSeedLogic.PRODUCING_SEED.toString()};
    super.initialize(kinds);

    tabPanels = new ProducingSeedLogicPanel[panelKinds.length];
    for (int i = 0; i < panelKinds.length; i++) {
      String kind = panelKinds[i];
      tabPanels[i] =
          new ProducingSeedLogicPanel(this, kind,
                                      ProducingSeedLogic.getInstance(),
                                      sysKnowKind);
      tabbedPane.add(tabPanels[i], kind);
    }

    tabbedPane.setSelectedIndex(0);
    tabbedPane_stateChanged(null);
    updateDialog();
  }

}
