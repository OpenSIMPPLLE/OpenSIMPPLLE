/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import javax.swing.*;
import java.awt.*;

/**
 *  This class defines the User Preferences Dialog
 */
public class UserPreferencesEditor extends JDialog{

  JPanel mainPanel;
  BorderLayout borderLayout;

  public UserPreferencesEditor(Frame owner, String title, boolean modal) {
    super(owner, title, modal);
    mainPanel = new JPanel();
    borderLayout = new BorderLayout();
    try {
      setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      jbInit();
      pack();
    }
    catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    mainPanel.setLayout(borderLayout);
  }

  public void initialize() {
  }

}
