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
import java.text.NumberFormat;

/**
 *
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller</p>
 */

public class MyJTextFieldDoubleRenderer implements TableCellRenderer {
  JTextField textField;

  public Color rowColor = SimpplleMain.ROW_HIGHLIGHT_COLOR;

  public MyJTextFieldDoubleRenderer() {
    textField = new JTextField("");
  }
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//    NumberFormat nf = NumberFormat.getInstance();
//    nf.setMaximumFractionDigits(6);  // Don't show fractional part.
    String str = (value != null ? Double.toString((Double)value) : "");
    textField.setText(str);
    textField.setHorizontalAlignment(JTextField.RIGHT);

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


