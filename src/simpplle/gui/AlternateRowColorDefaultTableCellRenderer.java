package simpplle.gui;

import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Component;
import javax.swing.JTable;

import simpplle.comcode.Evu;

import java.awt.Color;

 
/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-0.9.  By copying this file the user accepts the University of Montana 
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 * 
 * <p>This class defines an alternate row color default table cell renderer, a type of Javax default table cell renderer. 
 * It sets the default row color and the alternating colors seen in many of the OpenSimpplle GUI tables.  
 * 
 * @author Documentation by Brian Losi
 * Original source code authorship: Kirk A. Moeller
 *
 *
 */

public class AlternateRowColorDefaultTableCellRenderer extends
    DefaultTableCellRenderer {

  public Color rowColor = SimpplleMain.ROW_HIGHLIGHT_COLOR;
  /**
   * Primary constructor for Alternate Row Color Default Table Cell Renderer, inherits from Default Table Cell Renderer.  
   */
  public AlternateRowColorDefaultTableCellRenderer() {
    super();
  }
  /**
   * Overloaded Alternate Row Color Default Table Cell Renderer.  Inherits from super, but changes the row color to color parameter
   * @param c - color row is to be changed to 
   */
  public AlternateRowColorDefaultTableCellRenderer(Color c) {
    super();
    rowColor = c;
  }
/**
 * sets every other row brighter version of color if 'specific cell' boolean is not selected 
 */
  public Component getTableCellRendererComponent(JTable table, Object value,
                                                 boolean isSelected,
                                                 boolean hasFocus, int row,
                                                 int column) {
    Component c = super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
    // table.getBackground();

    if (isSelected) {
      c.setForeground(table.getSelectionForeground());
      c.setBackground(table.getSelectionBackground());
    }
    else {
      Color color = ((row % 2) == 1) ? rowColor : rowColor.brighter();
      c.setForeground(table.getForeground());
      c.setBackground(color);
    }
    return c;
  }
}
