/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import java.awt.Frame;
import simpplle.comcode.DoCompetitionLogic;
import simpplle.comcode.*;

/**
* Class that creates a dialog for Do Competition Dialog, a type of Veg Logic Dialog.
* 
* @author Documentation by Brian Losi
* <p>Original source code authorship: Kirk A. Moeller
*/
public class DoCompetitionDlg extends VegLogicDialog {
	/**
	 * Constructor for Do Competition Dialog.  Sets the frame owner, dialog title, modality.  Passes to JDialog superclass.  
	 * @param owner the frame which owns dialog 
	 * @param title dialog title
	 * @param modal sets modality
	 */
  public DoCompetitionDlg(Frame owner, String title, boolean modal) {
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
   * Overloaded constructor.  Builds the Do Competition dialog.
   */
  public DoCompetitionDlg() {
    this(new Frame(), "DoCompetitionDlg", false);
  }

  /**
   * Init method does not set any components.
   * @throws Exception
   */
  private void jbInit() throws Exception {
  }
  /**
   * Initializes the system knowledge kind to Do Competition logic.  Creates a set of tab panels based on number of kinds and
   * adds them to a tabbed pane.
   */
  private void initialize() {
    sysKnowKind = SystemKnowledge.DOCOMPETITION_LOGIC;
    String[] kinds = new String[] {DoCompetitionLogic.COMPETITION.toString()};
    super.initialize(kinds);

    tabPanels = new DoCompetitionLogicPanel[panelKinds.length];
    for (int i = 0; i < panelKinds.length; i++) {
      String kind = panelKinds[i];
      tabPanels[i] =
          new DoCompetitionLogicPanel(this, kind,
                                      DoCompetitionLogic.getInstance(),
                                      sysKnowKind);
      tabbedPane.add(tabPanels[i], kind);
    }

    tabbedPane.setSelectedIndex(0);
    tabbedPane_stateChanged(null);
    updateDialog();
  }

}

