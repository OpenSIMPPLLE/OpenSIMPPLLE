package simpplle.gui;

import java.awt.Frame;

import simpplle.comcode.FireSuppEventLogic;
import simpplle.comcode.SystemKnowledge;
/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class sets up Fire Suppression Beyond Class A LogicPanel, a type of Vegetative Logic Panel, which inherits from Base Panel.
 * Class A fires are 0-.25 Acres.  
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 */
public class FireSuppEventLogicDlg extends VegLogicDialog {
  public FireSuppEventLogicDlg(Frame owner, String title, boolean modal) {
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
  public FireSuppEventLogicDlg() {
    this(new Frame(), "FireSuppEventLogicDlg", false);
  }
  private void jbInit() throws Exception {

  }

  private void initialize() {
    sysKnowKind = SystemKnowledge.FIRE_SUPP_EVENT_LOGIC;
    String[] kinds = new String[] {FireSuppEventLogic.FIRE_SUPP_EVENT_LOGIC.toString()};
    super.initialize(kinds);

    tabPanels = new FireSuppEventLogicPanel[panelKinds.length];
    for (int i = 0; i < panelKinds.length; i++) {
      String kind = panelKinds[i];
      tabPanels[i] =
          new FireSuppEventLogicPanel(this, kind,FireSuppEventLogic.getInstance(),sysKnowKind);
      tabbedPane.add(tabPanels[i], kind);
    }

    tabbedPane.setSelectedIndex(0);
    tabbedPane_stateChanged(null);
    updateDialog();
  }

}
