/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import java.awt.*;
import javax.swing.JPanel;

/**
 * This class creates a new type of JPanel called the JDataPanel.
 * It is designed to add a new variable into the normal JPanel.  
 * The constructor takes a layout manager that will be passed to 
 * jpanel superclass, and of course, the new datasource variable is set.  
 * 
 * @author Documentation by Brian Losi
 * <p>Original source authorship: Kirk A. Moeller</p>
 */

public class JDataPanel extends JPanel {
  Object sourceData;
/**
 * Constructor for JDataPanel.  Creates a new type of panel which sends the layout to JPanel superclass and adds an object sourceData variable.  
 * @param layout
 * @param data
 */
  public JDataPanel(LayoutManager layout, Object data) {
    super(layout);
    this.sourceData = data;
  }
  /**
   * Gets the DataSource object variable.  This is the main addition in the JDataPanel class. 
   * @return datasource object
   */
  public Object getSource() { return sourceData; }
}