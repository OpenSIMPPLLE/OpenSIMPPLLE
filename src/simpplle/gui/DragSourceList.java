/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Vector;

/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-0.9.  By copying this file the user accepts the University of Montana 
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class defines model for DragSource List, a type of JList.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 * @see javax.swing.JList
 *
 */

public class DragSourceList extends JList
  implements DragSourceListener, DragGestureListener, MouseListener
  {

  Object     selected=null;
  DragSource dragSource = null;
  Cursor     dragCursorNoDrop;
  Cursor     dragCursorDrop;
  boolean    moveItems;
/**
 * Constructor for DragSourceList.
 */
  public DragSourceList() {
    this(false);
  }
  /**
   * Overloaded Drag SourceList constructor.  This method decides whether the action is move or copy.    
   * @param moveItems true if action is move, false if action is copy.  
   * 
   */
  public DragSourceList(boolean moveItems) {
    int action;

    this.moveItems = moveItems;
    if (moveItems) {
      action = DnDConstants.ACTION_MOVE;
      dragCursorNoDrop = DragSource.DefaultMoveNoDrop;
      dragCursorDrop   = DragSource.DefaultMoveDrop;
    }
    else {
      action = DnDConstants.ACTION_COPY;
      dragCursorNoDrop = DragSource.DefaultCopyNoDrop;
      dragCursorDrop   = DragSource.DefaultCopyDrop;
    }

    dragSource = new DragSource();
    dragSource.createDefaultDragGestureRecognizer( this,action, this);
    addMouseListener(this);
  }
  /**
   * Sets the list data model to the default list model and adds input list data.  Then sets this list data as the new model.   
   * @param data
   */

  public void setListData(java.util.List data) {
    MyDefaultListModel model = new MyDefaultListModel();
    model.addStringData(data);
    setModel(model);
  }
  /**
   * Sets the list data from passed data vector.  
   */
  public void setListData(Vector data) {
    MyDefaultListModel model = new MyDefaultListModel();
    model.addStringData(data);
    setModel(model);
  }
  /**
   * Sets the list data from passed object data array.  
   */
  public void setListData(Object[] data) {
    MyDefaultListModel model = new MyDefaultListModel();
    model.addStringData(data);
    setModel(model);
  }
/**
 * Handles what happens when drag is initiated (gesture recognized)
 */
  public void dragGestureRecognized( DragGestureEvent event) {
    if ( selected != null ){
      StringSelection text = new StringSelection( selected.toString());

      // as the name suggests, starts the dragging
      dragSource.startDrag (event, dragCursorNoDrop, text, this);
    }
  }

  /**
   * This message goes to DragSourceListener, informing it that the dragging
   * has ended
   *
   */
  public void dragDropEnd (DragSourceDropEvent event) {
    if (event.getDropSuccess() && moveItems) {
      removeElement();
    }
  }

  /**
   * This message goes to DragSourceListener, informing it that the dragging
   * has entered the DropSite
   *
   */
  public void dragEnter (DragSourceDragEvent event) {
    DragSourceContext context = event.getDragSourceContext();
    context.setCursor(dragCursorDrop);
  }

  /**
   * This message goes to DragSourceListener, informing it that the dragging
   * has exited the DropSite
   *
   */
  public void dragExit (DragSourceEvent event) {
    DragSourceContext context = event.getDragSourceContext();
    context.setCursor(dragCursorNoDrop);
  }

  /**
   * This message goes to DragSourceListener, informing it that the dragging is currently
   * ocurring over the DropSite
   *
   */
  public void dragOver (DragSourceDragEvent event) {
  }

  /**
   * is invoked when the user changes the dropAction
   *
   */
  public void dropActionChanged ( DragSourceDragEvent event) {
  }

  /**
   * Gets current list model and adds element to itself.
   *
   */
   public void addElement( Object s ) {
     (( DefaultListModel )getModel()).addElement (s.toString());
  }

  /**
   * Gets current list model and removes element from it.   
   */
  public void removeElement() {
    (( DefaultListModel)getModel()).removeElement( selected);
    selected=null;
  }
  /**
   * No implementation.   * 
   */
  public void mouseClicked(MouseEvent e){}
  /**
   * No implementation.
   */
  public void mouseEntered(MouseEvent e) {}
  /**
   * No implementation.
   */
  public void mouseExited(MouseEvent e) {}
  /**
   * Gets the model at the index where the mouse was pressed
   */
  public void mousePressed(MouseEvent e)
  {
    int index = locationToIndex(e.getPoint());
    selected = (index != -1) ? getModel().getElementAt(index).toString() : null;
  }
  /**
   * No implementation.
   */
  public void mouseReleased(MouseEvent e) {}
}


