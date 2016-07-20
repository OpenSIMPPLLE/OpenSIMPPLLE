/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JTabbedPane;
import simpplle.comcode.InvasiveSpeciesLogic;
import simpplle.comcode.SystemKnowledge;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * This class defines the Invasive Species Logic Dialog.  There are only two zones with this logic, Eastside Region 1 and Colorado Plateau.
 * This class is specifically for the Colorado Plateau.  
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class InvasiveSpeciesLogicDialog extends VegLogicDialog {
  public JMenuItem menuActionSoilTypeEditor = new JMenuItem();

  public InvasiveSpeciesLogicDialog(Frame owner, String title, boolean modal) {
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

  public InvasiveSpeciesLogicDialog() {
    this(new Frame(), "InvasiveSpeciesLogic", false);
  }

  private void jbInit() throws Exception {
    menuActionSoilTypeEditor.setText("Soil Type Editor");
    menuActionSoilTypeEditor.addActionListener(new
        InvasiveSpeciesLogicDialog_menuActionSoilTypeEditor_actionAdapter(this));
    menuAction.addSeparator();
    menuAction.add(menuActionSoilTypeEditor);
  }

  private void initialize() {
    sysKnowKind = SystemKnowledge.INVASIVE_SPECIES_LOGIC;
    String[] kinds = new String[] {InvasiveSpeciesLogic.PROBABILITY.toString(),
                                   InvasiveSpeciesLogic.CHANGE_RATE.toString()};
    super.initialize(kinds);

    tabPanels = new InvasiveSpeciesLogicPanel[panelKinds.length];
    for (int i = 0; i < panelKinds.length; i++) {
      String kind = panelKinds[i];
      tabPanels[i] =
          new InvasiveSpeciesLogicPanel(this, kind,
                                        InvasiveSpeciesLogic.getInstance(),
                                        sysKnowKind);
      tabbedPane.add(tabPanels[i], kind);
    }

    tabbedPane.setSelectedIndex(0);
    tabbedPane_stateChanged(null);
    updateDialog();
  }

  public void menuActionSoilTypeEditor_actionPerformed(ActionEvent e) {
    SoilTypeChooser dlg = new SoilTypeChooser(this,"Soil Type Editor",true,null);
    dlg.setVisible(true);
  }


}

class InvasiveSpeciesLogicDialog_menuActionSoilTypeEditor_actionAdapter implements
    ActionListener {
  private InvasiveSpeciesLogicDialog adaptee;
  InvasiveSpeciesLogicDialog_menuActionSoilTypeEditor_actionAdapter(
      InvasiveSpeciesLogicDialog adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.menuActionSoilTypeEditor_actionPerformed(e);
  }
}


