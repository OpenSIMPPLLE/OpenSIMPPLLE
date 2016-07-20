/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import java.util.ArrayList;

/**
 *
 *
 * @author Documentation by Brian Losi
 * <p>Original source authorship: Kirk A. Moeller</p>
 */
public class MyJComboBoxEditor extends DefaultCellEditor {
  public MyJComboBoxEditor(Object[] items) {
    super(new JComboBox(items));
  }
  public MyJComboBoxEditor(ArrayList items) {
    this(items.toArray());
  }
}
