/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import simpplle.comcode.Species;
import javax.swing.table.TableCellRenderer;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller</p>
 */

public class MyJComboBoxRenderer implements TableCellRenderer {
  JComboBox box;
  JTextArea textArea;

  public Color rowColor = SimpplleMain.ROW_HIGHLIGHT_COLOR;

  public MyJComboBoxRenderer(ArrayList items) {
    this(items.toArray());
  }
  public MyJComboBoxRenderer(ArrayList items, Color c) {
    this(items);
    rowColor = c;
  }
  public MyJComboBoxRenderer(Object[] items, Color c) {
    this(items);
    rowColor = c;
  }
  public MyJComboBoxRenderer(Object[] items) {
    box = new JComboBox(items);

    textArea = new JTextArea("");
    textArea.setLineWrap(true);
    textArea.setWrapStyleWord(true);
    box.setFont(new java.awt.Font("Monospaced", 0, 12));
    textArea.setFont(new java.awt.Font("Monospaced", 0, 12));
  }

  public Component getTableCellRendererComponent(JTable table, Object value,
                                                 boolean isSelected,
                                                 boolean hasFocus, int row,
                                                 int column) {
    Color foreground = isSelected ? table.getSelectionForeground() :
                       table.getForeground();
    Color background = isSelected ? table.getSelectionBackground() :
                       table.getBackground();

//    if (Species.getValueAt(row).isUserCreated() == false) {
//      textArea.setText(value.toString());
//      textArea.setForeground(foreground);
//      textArea.setBackground(background);
//
//      return textArea;
//    }

    if (isSelected) {
      box.setForeground(table.getSelectionForeground());
      box.setBackground(table.getSelectionBackground());
    }
    else {
      box.setForeground(foreground);
      Color color = ((row % 2) == 1) ? rowColor : rowColor.brighter();
      box.setBackground(color);
//    box.setBackground(background);
    }


    // Select the current value
    box.setSelectedItem(value);
    return box;
  }

}
