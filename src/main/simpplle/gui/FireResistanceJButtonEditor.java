package simpplle.gui;

import simpplle.comcode.Species;
import simpplle.comcode.FireResistance;

import javax.swing.*;
import java.awt.Component;
import java.util.EventObject;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;
import java.util.HashMap;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-0.9.  By copying this file the user accepts the University of Montana 
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
  * <p>This class defines the Fire Resistance JButton Editor.  This method modifies the Jbutton used in fire resistance.
  * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 *
 */

public class FireResistanceJButtonEditor extends AbstractCellEditor implements TableCellEditor {
  protected JButton button = null;
  protected HashMap data;
  protected JDialog dialog;
  protected JTable  theTable;
  protected String  title;
/**
 * Default constructor for fire resistance jbutton editor.  Sets dialog to null table to null, and title to empty string.  
 */
  public FireResistanceJButtonEditor() {
    this(null,null,"");
  }
  /**
   * Overloaded constructor for Fire resistance JButton editor.  Initializes to passed in dialog, table, and title.  
   * Creates a new JButton with empty text.  
   * @param dialog
   * @param theTable
   * @param title
   */

  public FireResistanceJButtonEditor(JDialog dialog, JTable theTable, String title) {
    this.dialog         = dialog;
    this.title          = title;
    this.theTable       = theTable;

    button = new JButton("");
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        button_actionPeformed(e);
      }
    });
  }
  /**
   * gets the cell editor value which is data from hash map.  
   */
  public Object getCellEditorValue() {
    return data;
  }
  /**
   * Sets the text of button to the value and enables it.  
   */
  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
    this.data = (HashMap) value;

    String text = (value == null) ? "" : value.toString();
    button.setText(text);
    button.setEnabled((value != null));

    return button;
  }
/**
 * When button pushed.  creates a new fire resistance conditional editor.  
 * @param e
 */
  private void button_actionPeformed(ActionEvent e) {
    FireResistanceConditionalEditor dlg =
        new FireResistanceConditionalEditor(dialog,title,true,data);
    dlg.setVisible(true);
    super.fireEditingStopped();
    dialog.update(dialog.getGraphics());
  }

}
