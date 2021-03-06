/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import java.awt.Frame;
import simpplle.comcode.InvasiveSpeciesLogicMSU;
import simpplle.comcode.SystemKnowledge;

/** 
 *
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class InvasiveSpeciesMSULogicDialog extends VegLogicDialog {

  public InvasiveSpeciesMSULogicDialog(Frame owner, String title, boolean modal) {
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

  public InvasiveSpeciesMSULogicDialog() {
    this(new Frame(), "InvasiveSpeciesMSULogicDialog", false);
  }
  // TODO: Look into removing this...
  private void jbInit() throws Exception {}

  private void initialize() {
    sysKnowKind = SystemKnowledge.INVASIVE_SPECIES_LOGIC_MSU;
    String[] kinds = new String[] {InvasiveSpeciesLogicMSU.PROBABILITY.toString(),
                                   InvasiveSpeciesLogicMSU.CHANGE_RATE.toString()};
    super.initialize(kinds);

    tabPanels = new InvasiveSpeciesMSULogicPanel[panelKinds.length];
    for (int i = 0; i < panelKinds.length; i++) {
      String kind = panelKinds[i];
      tabPanels[i] =
          new InvasiveSpeciesMSULogicPanel(this, kind,
                                        InvasiveSpeciesLogicMSU.getInstance(),
                                        sysKnowKind);
      tabbedPane.add(tabPanels[i], kind);
    }

    tabbedPane.setSelectedIndex(0);
    tabbedPane_stateChanged(null);
    updateDialog();
  }
}
