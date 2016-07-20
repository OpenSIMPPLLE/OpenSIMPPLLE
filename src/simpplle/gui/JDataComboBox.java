/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import javax.swing.JComboBox;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
  * @author Documentation by Brian Losi
 * <p>Original source authorship: Kirk A. Moeller</p>
 * 
 * This class creates a new type of combo box called the JDataComboBox.  
 * It is designed to add a new variable into the normal JComboBox.  
 * The constructor takes an object array that will be passed to 
 * combo box superclass, and of course, the new datasource variable is set.  
 * 
 */

public class JDataComboBox extends JComboBox {
  private Object dataSource;
/**
 * Constructor for JDataComboBox.  It 
 * @param items
 * @param dataSource
 */
  public JDataComboBox(Object[] items, Object dataSource) {
    super(items);
    this.dataSource = dataSource;
  }
/**
 * Gets the DataSource object variable.  This is the main addition in the JDataComboBox class. 
 * @return datasource object
 */
  public Object getDataSource() { return dataSource; }

}