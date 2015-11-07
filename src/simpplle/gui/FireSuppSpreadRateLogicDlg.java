package simpplle.gui;

import java.awt.Frame;
import simpplle.comcode.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.JOptionPane;
import javax.swing.JMenuItem;
/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-0.9.  By copying this file the user accepts the University of Montana 
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 */
public class FireSuppSpreadRateLogicDlg extends VegLogicDialog {
  JMenuItem menuImportOldFile = new JMenuItem();

  public FireSuppSpreadRateLogicDlg(Frame owner, String title, boolean modal) {
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
  public FireSuppSpreadRateLogicDlg() {
    this(new Frame(), "FireSuppSpreadRateLogicDlg", false);
  }
  private void jbInit() throws Exception {
    menuImportOldFile.setText("Import old format File");
    menuImportOldFile.addActionListener(new
      FireSuppSpreadRateLogicDlg_menuImportOldFile_actionAdapter(this));
    menuFile.add(menuImportOldFile,3);
  }

  private void initialize() {
    sysKnowKind = SystemKnowledge.FIRE_SUPP_SPREAD_RATE_LOGIC;
    String[] kinds = new String[] {FireSuppSpreadRateLogic.FIRE_SUPP_SPREAD_RATE_LOGIC.toString()};
    super.initialize(kinds);

    tabPanels = new FireSuppSpreadRateLogicPanel[panelKinds.length];
    for (int i = 0; i < panelKinds.length; i++) {
      String kind = panelKinds[i];
      tabPanels[i] =
          new FireSuppSpreadRateLogicPanel(this, kind,
                                       FireSuppSpreadRateLogic.getInstance(),
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
    String       title = "Select a Fire Supp Spread Rate File";
    String       ext;

    extFilter = new MyFileFilter(new String[] {"sk_firesuppspreadrate", "sk_firesuppspreadrate"},
                                 "Fire Supp Spread Rate");

    inputFile = Utility.getOpenFile(this,title,extFilter);

    if (inputFile != null) {
      try {
        SystemKnowledge.readIndividualInputFile(inputFile,SystemKnowledge.FIRESUPP_SPREAD_RATE);
        updateDialog();
      }
      catch (SimpplleError err) {
        JOptionPane.showMessageDialog(this,err.getError(),"Error loading file",
                                      JOptionPane.ERROR_MESSAGE);
      }
    }
  }

}


class FireSuppSpreadRateLogicDlg_menuImportOldFile_actionAdapter implements ActionListener {
  private FireSuppSpreadRateLogicDlg adaptee;
  FireSuppSpreadRateLogicDlg_menuImportOldFile_actionAdapter(FireSuppSpreadRateLogicDlg adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.menuImportOldFile_actionPerformed(e);
  }
}

