/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import simpplle.comcode.SimpplleType;
import java.util.Vector;

/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class has methods for getting OpenSimpplle types. 
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 *
 */


public class ListItem {
  public SimpplleType item;
  public boolean      selected;
/**
 * Constructor for List Item.  Brings in a simpplle type and assigns to this, selected to false
 * @param item
 */
  public ListItem(SimpplleType item) {
    this.item = item;
    selected  = false;
  }
/**
 * 
 * @return true if selected
 */
  public boolean isSelected() { return selected; }
  /**
   * Sets the boolean value to passed boolean parameter.  
   * @param selected
   */
  public void setSelected(boolean selected) { this.selected = selected; }
  /**
   * Changes the selected value by negating it.  Selected value is a boolean.  
   */
  public void changeSelected() {
    selected = (!selected);
  }
/**
 * The toString method.  If selected is true returns --> item.  Otherwise returns a to string of simpplle type.  
 */
  public String toString() {
    if (selected) {
      return "--> " + item;
    }
    else {
      return item.toString();
    }
  }
  /**
   * Brings in a vector of simpplle types, which was created in a situation where variable size storage is needed and puts them into an array which will be later used in GUI classes
   * @param strItems
   * @return
   */
  public static ListItem[] fillList(Vector strItems) {
    ListItem[] listItems = new ListItem[strItems.size()];
    for(int i=0; i<strItems.size(); i++) {
      SimpplleType item = (SimpplleType)strItems.elementAt(i);
      if (item == null) {
        System.out.println(listItems[i-1]);
      }
      listItems[i] = new ListItem((SimpplleType)strItems.elementAt(i));
    }
    return listItems;
  }
/**
 * Sets the list selection from the passed in items array and corresponding boolean.  
 * @param items the array of items to 
 * @param bool 
 */
  public static void setListSelection(ListItem[] items, boolean bool) {
    for(int i=0; i<items.length; i++) {
      items[i].setSelected(bool);
    }
  }
}


