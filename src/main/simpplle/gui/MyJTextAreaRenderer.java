package simpplle.gui;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;
import simpplle.comcode.Species;
import java.awt.*;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller</p>
 * 
 */

public class MyJTextAreaRenderer implements TableCellRenderer {
  JTextArea textArea = null;
  private boolean species;
  public Color rowColor = SimpplleMain.ROW_HIGHLIGHT_COLOR;

  public MyJTextAreaRenderer() {
    textArea = new JTextArea("");
    textArea.setLineWrap(true);
    textArea.setWrapStyleWord(true);
    textArea.setFont(new java.awt.Font("Monospaced", 0, 12));
    textArea.setToolTipText("");
  }
  public MyJTextAreaRenderer(Color c) {
    this();
    rowColor = c;
  }
  public MyJTextAreaRenderer(int numRows) {
    this();
    textArea.setRows(numRows);
  }
  public MyJTextAreaRenderer(int numRows, Color c) {
    this(numRows);
    rowColor = c;
  }
  public MyJTextAreaRenderer(boolean isSpecies) {
    this();
    this.species = isSpecies;
  }
  public MyJTextAreaRenderer(boolean isSpecies, Color c) {
    this(isSpecies);
    rowColor = c;
  }
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//    textArea.setText(((Vector)value).toString());
    textArea.setText(value.toString());
    String toolText = (species) ? ((Species)value).getDescription() : textArea.getText();
    textArea.setToolTipText(toolText);

    if (table.isEditing() == false) {
//      int height = table.getRowHeight(row);
//      int newHeight = textArea.getPreferredSize().height;
//      if (newHeight > height) { table.setRowHeight(row,newHeight); }
    }

    if (isSelected) {
      textArea.setForeground(table.getSelectionForeground());
      textArea.setBackground(table.getSelectionBackground());
    }
    else {
      textArea.setForeground(table.getForeground());
      Color color = ((row % 2) == 1) ? rowColor : rowColor.brighter();

      textArea.setBackground(color);
    }

    return textArea;
  }

  public void setSpecies(boolean species) {
    this.species = species;
  }

  public boolean isSpecies() {
    return species;
  }
}


