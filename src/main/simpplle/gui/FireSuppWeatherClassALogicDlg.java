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
 *
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class FireSuppWeatherClassALogicDlg extends VegLogicDialog {

  public FireSuppWeatherClassALogicDlg(Frame owner, String title, boolean modal) {
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

  public FireSuppWeatherClassALogicDlg() {
    this(new Frame(), "FireSuppSpreadRateLogicDlg", false);
  }

  private void jbInit() throws Exception {}

  private void initialize() {
    sysKnowKind = SystemKnowledge.FIRE_SUPP_WEATHER_CLASS_A_LOGIC;
    String[] kinds = new String[] {FireSuppWeatherClassALogic.FIRE_SUPP_WEATHER_CLASS_A_LOGIC.toString()};
    super.initialize(kinds);

    tabPanels = new FireSuppWeatherClassALogicPanel[panelKinds.length];
    for (int i = 0; i < panelKinds.length; i++) {
      String kind = panelKinds[i];
      tabPanels[i] =
          new FireSuppWeatherClassALogicPanel(this, kind,
                                       FireSuppWeatherClassALogic.getInstance(),
                                       sysKnowKind);
      tabbedPane.add(tabPanels[i], kind);
    }
    tabbedPane.setSelectedIndex(0);
    tabbedPane_stateChanged(null);
    updateDialog();
  }
}
