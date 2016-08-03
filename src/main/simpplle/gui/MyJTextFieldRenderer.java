/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import javax.swing.table.TableCellRenderer;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.JTextField;
import java.awt.Color;

/**
 * This class creates a customized JTextField renderer, it is a type of table cell renderer.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class MyJTextFieldRenderer implements TableCellRenderer {
  JTextField textField;

  public Color rowColor = SimpplleMain.ROW_HIGHLIGHT_COLOR;
/**
 * Constructor for customized JTextField.  Instantiates a JTextField and sets its label text to empty string.  
 */
  public MyJTextFieldRenderer() {
    textField = new JTextField("");
  }
  /**
   * This method constructs a new java.awt.component.  It sets the customized textfield text to a particular parameter object.  The choices for 
   * this object are Integer, Float, Double and null.  It also horizontally aligns the textfield to the right.  
   * If the text field is selected (per parameter variable) it sets the foreground and background colors to the table colors.  
   * If not sets its foreground to the table foreground color and creates an alternating color for the background.  
   */
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    if (value == null) {
      textField.setText("");
    }
    else if (value instanceof Double) {
      textField.setText(Double.toString((Double)value));
      textField.setHorizontalAlignment(JTextField.RIGHT);
    }
    else if (value instanceof Float) {
      textField.setText(Float.toString((Float)value));
      textField.setHorizontalAlignment(JTextField.RIGHT);
    }
    else if (value instanceof Integer) {
      textField.setText(Integer.toString((Integer)value));
      textField.setHorizontalAlignment(JTextField.RIGHT);
    }
    textField.setText((value != null) ? value.toString() : "");

    if (isSelected) {
      textField.setForeground(table.getSelectionForeground());
      textField.setBackground(table.getSelectionBackground());
    }
    else {
      textField.setForeground(table.getForeground());
      Color color = ((row % 2) == 1) ? rowColor : rowColor.brighter();

      textField.setBackground(color);
    }


    return textField;
  }

}


