/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import javax.swing.JTextField;

/**
 * This class extends the JTextField to expand the abilities of these text fields.
 * It adds a datasource object and text variable to the normal JTextField.
 *
 * @author Documentation by Brian Losi
 * <p>Original source authorship: Kirk A. Moeller</p>
 */

public class JDataTextField extends JTextField {
  private Object dataSource;
  private int    data;
/**
 * Creates a new JDataTextField with the number of columns passed to JTextField superclass.  
 * Also sets the datasource for this JDataTextField, the reason for this special class.  
 * @param columns number of columns to make in text field.  
 * @param dataSource the type of data for this text field
 */
  public JDataTextField(int columns, Object dataSource) {
    super(columns);
    this.dataSource = dataSource;
  }
  /**
   * Overloaded constructor.  Creates a new JDataTextField with the number of columns passed to JTextField superclass.  
   * Also sets the datasource and text variable for this JDataTextField, the reason for this special class. 
   * The data variable is set to -1, a flag.  
   * @param text new variable added to JTextField 
   * @param columns number of columns for the JTextField
   * @param dataSource new variable added to JTextField
   */
  public JDataTextField(String text, int columns, Object dataSource) {
    this(text,columns,dataSource,-1);
  }
  /**
   * Overloaded constructor.  Creates a new JDataTextField with the number of columns and string text passed to JTextField superclass.  
   * Also sets the datasource and data variable for this JDataTextField, the reason for this special class. 
   * @param text text used to initialize JTextFieldnew variable added to JTextField 
   * @param columns number of columns for the JTextField
   * @param dataSource new variable added to JTextField
   * @param data
   */
  public JDataTextField(String text, int columns, Object dataSource, int data) {
    super(text,columns);
    this.dataSource = dataSource;
    this.data       = data;
  }
  
/**
 * Gets the data source for theJDataTextField.  
 * @return datasource object variable.
 */
  public Object getDataSource() { return dataSource; }
  /**
   * Gets the data in the JDataTextField.  
   * @return data 
   */
  public int    getData() { return data; }


}
