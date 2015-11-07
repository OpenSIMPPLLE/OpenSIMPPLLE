package simpplle.gui;

import java.awt.Frame;
import simpplle.comcode.*;

/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-0.9.  By copying this file the user accepts the University of Montana 
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class sets up Fire Suppression Beyond Class A Logic Dialog a type of vegetative logic Dialog.  Class A fire logic is fires within 
 * 0-.25 Acres.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller</p>
 *
 */
public class FireSuppBeyondClassALogicDlg extends VegLogicDialog {
	/**
	 * Constructor for Fire Suppression Beyond Class A Logic Dialog
	 * @param owner dialog owner
	 * @param title name of dialog
	 * @param modal modality
	 */
  public FireSuppBeyondClassALogicDlg(Frame owner, String title, boolean modal) {
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
   * Overloaded Constructor for Fire Suppression Beyond Class A Logic.  Makes a new frame and sets the title to Fire Suppression Beyond Class A Logic Dialog, and modality to false.
   */
  public FireSuppBeyondClassALogicDlg() {
    this(new Frame(), "FireSuppBeyondClassALogicDlg", false);
  }
  private void jbInit() throws Exception {

  }
/**
 * Initializes the Fire Suppression Beyond Class A Logic with the Fire Suppression Beyond Class A System Logic.  
 * Sets up a tabbed panels according to type number of fire suppression class Beyond Class A logic 
 */
  private void initialize() {
    sysKnowKind = SystemKnowledge.FIRE_SUPP_BEYOND_CLASS_A_LOGIC;
    String[] kinds = new String[] {simpplle.comcode.logic.FireSuppBeyondClassALogic.FIRE_SUPP_BEYOND_CLASS_A.toString()};
    super.initialize(kinds);

    tabPanels = new FireSuppBeyondClassALogicPanel[panelKinds.length];
    for (int i = 0; i < panelKinds.length; i++) {
      String kind = panelKinds[i];
      tabPanels[i] =
          new FireSuppBeyondClassALogicPanel(this, kind,
                                       simpplle.comcode.logic.FireSuppBeyondClassALogic.getInstance(),
                                       sysKnowKind);
      tabbedPane.add(tabPanels[i], kind);
    }

    tabbedPane.setSelectedIndex(0);
    tabbedPane_stateChanged(null);
    updateDialog();
  }

}
