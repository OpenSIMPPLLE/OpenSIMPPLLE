/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import java.awt.Frame;
import simpplle.comcode.*;

/** 
 * This class sets up Vegetative Unit Fire Type Logic dialog, a type of Abstract Logic Dialog, itself a type of JDialog.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class VegUnitFireTypeLogicDlg extends AbstractLogicDialog {
	/**
	 * Constructor for Veg Unit Fire Type Logic Dialog.  Passes to superclass the owner frame, title, and modality, then initializes.  
	 * @param owner frame owner
	 * @param title the title of dialog
	 * @param modal modality of dialog
	 */
  protected VegUnitFireTypeLogicDlg(Frame owner, String title, boolean modal) {
    super(owner, title, modal);
    try {
      setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      initialize();
      pack();
    }
    catch (Exception exception) {
      exception.printStackTrace();
    }
  }
  /**
   * Initializes the Vegetative Unit FIre Type Logic dialog.  Sets the system knowledge to VEG_UNIT_FIRE_TYPE_LOGIC and creates a new array of fire types.
   * Creates tabbed panels with the fire type and panels for the fire types by creating new tab panels with this as the dialog, fire type kind, the fire type logic instance and
   * current system knowledge kind = VEG_UNIT_FIRE_TYPE_LOGIC.  The first tab will be selected.
   */
  private void initialize() {
    sysKnowKind = SystemKnowledge.VEG_UNIT_FIRE_TYPE_LOGIC;
    String[] kinds = new String[] {VegUnitFireTypeLogic.UNIT_FIRE_TYPE.toString()};
    super.initialize(kinds);

    tabPanels = new VegUnitFireTypeLogicPanel[panelKinds.length];
    for (int i = 0; i < panelKinds.length; i++) {
      String kind = panelKinds[i];
      tabPanels[i] =
          new VegUnitFireTypeLogicPanel(this, kind,
                                      VegUnitFireTypeLogic.getInstance(),
                                      sysKnowKind);
      tabbedPane.add(tabPanels[i], kind);
    }

    tabbedPane.setSelectedIndex(0);
    tabbedPane_stateChanged(null);
    updateDialog();
  }
}
