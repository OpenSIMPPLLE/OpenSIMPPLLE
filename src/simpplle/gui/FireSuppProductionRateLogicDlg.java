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
import javax.swing.JMenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JOptionPane;

/**
 *
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class FireSuppProductionRateLogicDlg extends VegLogicDialog {
  JMenuItem menuImportOldFile = new JMenuItem();

  public FireSuppProductionRateLogicDlg(Frame owner, String title, boolean modal) {
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
  public FireSuppProductionRateLogicDlg() {
    this(new Frame(), "FireSuppProductionRateLogicDlg", false);
  }
  private void jbInit() throws Exception {
    menuImportOldFile.setText("Import old format File");
    menuImportOldFile.addActionListener(new
      FireSuppProductionRateLogicDlg_menuImportOldFile_actionAdapter(this));
    menuFile.add(menuImportOldFile,3);
  }

  private void initialize() {
    sysKnowKind = SystemKnowledge.FIRE_SUPP_PRODUCTION_RATE_LOGIC;
    String[] kinds = new String[] {FireSuppProductionRateLogic.FIRE_SUPP_PRODUCTION_RATE_LOGIC.toString()};
    super.initialize(kinds);

    tabPanels = new FireSuppProductionRateLogicPanel[panelKinds.length];
    for (int i = 0; i < panelKinds.length; i++) {
      String kind = panelKinds[i];
      tabPanels[i] =
          new FireSuppProductionRateLogicPanel(this, kind,
                                       FireSuppProductionRateLogic.getInstance(),
                                       sysKnowKind);
      tabbedPane.add(tabPanels[i], kind);
    }

    tabbedPane.setSelectedIndex(0);
    tabbedPane_stateChanged(null);
    updateDialog();
  }

  public void menuImportOldFile_actionPerformed(ActionEvent e) {
    File         inputFile;
    MyFileFilter extFilter;
    String       title = "Select a Fire Supp Production Rate File";
    String       ext;

    extFilter = new MyFileFilter(new String[] {"sk_firesuppprodrate", "sk_firesuppprodrate"},
                                 "Fire Supp Production Rate");

    inputFile = Utility.getOpenFile(this,title,extFilter);

    if (inputFile != null) {
      try {
        SystemKnowledge.loadUserKnowledge(inputFile,SystemKnowledge.FIRESUPP_PRODUCTION_RATE);
        updateDialog();
      }
      catch (SimpplleError err) {
        JOptionPane.showMessageDialog(this,err.getError(),"Error loading file",
                                      JOptionPane.ERROR_MESSAGE);
      }
    }
  }

}

class FireSuppProductionRateLogicDlg_menuImportOldFile_actionAdapter implements ActionListener {
  private FireSuppProductionRateLogicDlg adaptee;
  FireSuppProductionRateLogicDlg_menuImportOldFile_actionAdapter(FireSuppProductionRateLogicDlg adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.menuImportOldFile_actionPerformed(e);
  }
}
