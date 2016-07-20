/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;

/**
 *
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller</p>
 */

public class MyJTextFieldEditor extends DefaultCellEditor {
  public MyJTextFieldEditor(String text) {
    super(new JTextField(text));
  }

}