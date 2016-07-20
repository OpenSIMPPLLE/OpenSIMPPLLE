/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import java.io.*;
import java.util.Iterator;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Vector;

/**
 * Creates a drag drop list, a type of JList.
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 * @see javax.swing.JList
 */
public class DragDropList extends JList  implements DropTargetListener,DragSourceListener,
             DragGestureListener, MouseListener, KeyListener
  {

  private boolean multiValued;
  private boolean noDelete;

  Object selected=null;
  boolean localDrag = false;
  DropTarget dropTarget = null;
  DragSource dragSource = null;
  JPopupMenu popupMenu = new JPopupMenu();
  JMenuItem popupMenuDelete = new JMenuItem();

  // allows delete key items to be added back to the original source list
  // in the dialog where the item was dragged from.
  DragSourceList dialogDragSource = null;
/**
 * Primary constructor for drag drop list.  
 */
  public DragDropList() {
    this(false);
  }
/**
 * Overloaded constructor creates a new drop target, drag source, and listeners.  
 * @param noDelete
 */
  public DragDropList(boolean noDelete) {
    multiValued   = true;
    this.noDelete = noDelete;

    dropTarget = new DropTarget (this, this);
    dragSource = new DragSource();
    dragSource.createDefaultDragGestureRecognizer( this,DnDConstants.ACTION_MOVE, this);
    addMouseListener(this);

    if (noDelete == false) {
      addKeyListener(this);

      popupMenu.setInvoker(this);
      popupMenuDelete.setText("Delete Item");
      popupMenuDelete.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(ActionEvent e) {
          popupMenuDelete_actionPerformed(e);
        }
      });
      popupMenu.add(popupMenuDelete);
    }
  }
/**
 * Creates the list data model from the default lists.  
 * @param data
 */
  public void setListData(java.util.List data) {
    MyDefaultListModel model = new MyDefaultListModel();
    if (data != null) { model.addStringData(data); }
    setModel(model);
  }
  /**
   * Overloaded set list data method, which sets model list data to a passed vector of data.  
   */
  public void setListData(Vector data) {
    MyDefaultListModel model = new MyDefaultListModel();
    if (data != null) { model.addStringData(data); }
    setModel(model);
  }
  /**
   * Overloaded set list data method, which sets model list data to a passed array of object data.  
   */
  public void setListData(Object[] data) {
    MyDefaultListModel model = new MyDefaultListModel();
    if (data != null) { model.addStringData(data); }
    setModel(model);
  }
/**
 * Sets the dialog drag source list.  
 * @param dialogDragSource
 */
  public void setDialogDragSourceList(DragSourceList dialogDragSource) {
    this.dialogDragSource = dialogDragSource;
  }
  /**
   * Sets whether the drag drop list is multi valued.
   * @param value true if multi-valued
   */
  public void setMultiValued(boolean value) { multiValued = value; }
/**
 * Method to enter the drag event
 */
  public void dragEnter (DropTargetDragEvent event) {
    event.acceptDrag (DnDConstants.ACTION_COPY_OR_MOVE);
  }
  /**
   * Drag event which clears the selection. 
   */
  public void dragExit (DropTargetEvent event) {
    clearSelection();
  }
  /**
   * Drag event which sets the index.  
   */
  public void dragOver (DropTargetDragEvent event) {
    int index = locationToIndex(event.getLocation());

    if (index == -1) {
      clearSelection();
    }
    else {
      setSelectedIndex(index);
    }
  }
/**
 * Checks if the drop target event is a supported data flavor
 */
  public void drop (DropTargetDropEvent event) {
    try {
      Transferable transferable = event.getTransferable();

      // we accept only Strings
      if (transferable.isDataFlavorSupported (DataFlavor.stringFlavor) &&
          isDropOk(event)){

          event.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);   
          String s = (String)transferable.getTransferData ( DataFlavor.stringFlavor);
          if (localDrag) { removeElement(); }
          addElement(s, event.getLocation());
          event.getDropTargetContext().dropComplete(true);
      }
      else{
          event.rejectDrop();
      }
      localDrag = false;
    }
    catch (IOException exception) {
      exception.printStackTrace();
      System.err.println( "Exception" + exception.getMessage());
      event.rejectDrop();
    }
    catch (UnsupportedFlavorException ufException ) {
      ufException.printStackTrace();
      System.err.println( "Exception" + ufException.getMessage());
      event.rejectDrop();
    }
  }
/**
 * Checks if the drop event was acceptable
 * @param event
 * @return true if drag drop event is ok.
 */
  private boolean isDropOk(DropTargetDropEvent event) {
    return true;
  }

  /**
   * is invoked if the use modifies the current drop gesture
   *
   */
  public void dropActionChanged ( DropTargetDragEvent event ) {
  }

  /**
   * Method for event that a drag gesture has been initiated.  
   *
   */
  public void dragGestureRecognized( DragGestureEvent event) {
    if ( selected != null ){
      StringSelection text = new StringSelection( selected.toString());

      // as the name suggests, starts the dragging
      dragSource.startDrag (event, DragSource.DefaultMoveDrop, text, this);
      localDrag = true;
    }
    else {
    }
  }
  /**
   * Drag drop event ended
   */

  public void dragDropEnd (DragSourceDropEvent event) {
  }
  /**
   * 
   */
  public void dragEnter (DragSourceDragEvent event) {
    DragSourceContext context = event.getDragSourceContext();
    context.setCursor(DragSource.DefaultMoveDrop);
  }
  /**
   * Handles event that cursor has moved out of range of thing being dragged.  
   */
  
  public void dragExit (DragSourceEvent event) {
    DragSourceContext context = event.getDragSourceContext();
    context.setCursor(DragSource.DefaultMoveNoDrop);
  }
  /**
   * No implementation for method.
   */
  public void dragOver (DragSourceDragEvent event) {
  }
/**
 * No implementation for method.    
 */
  public void dropActionChanged ( DragSourceDragEvent event) {
  }
/**
 * Adds an element to the model
 * If model already contains dropped item, nothing happens, if not set as multi valued clears the model, else if nothing is in 
 * model adds it, else it inserts element at a particular location 
 * @param s the object selected
 * @param loc index point
 */
  public void addElement(Object s, Point loc) {
     int index = locationToIndex(loc);
     DefaultListModel model = (DefaultListModel)getModel();

     if (model.contains(s.toString())) { return; }

     if (!multiValued) { model.clear(); }
     if (index == -1) {
       model.addElement(s.toString());
     }
     else {
       model.insertElementAt(s.toString(),index);
     }
  }
/**
 * remove an element from the default list model and sets it null.  
 */
  public void removeElement() {
    (( DefaultListModel)getModel()).removeElement( selected);
    selected=null;
  }
/**
 * Checks if mouse event that occurred is a right click.  
 * @param e a mouse event occurred
 * @return true if input event is right click on mouse button.
 */
  private boolean isRightClick(MouseEvent e) {
    return (e.getModifiers () & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK;
  }
  /**
   * No implementation for method
   */
  public void mouseClicked(MouseEvent e){}
  /**
  * No implementation for method
   */
  public void mouseEntered(MouseEvent e) {}
  /**
  * No implementation for method
   */
  public void mouseExited(MouseEvent e) {}
  /**
   * Uses the mouse to press select object from drag drop list.
   */
  public void mousePressed(MouseEvent e)
  {
    int index = locationToIndex(e.getPoint());
    if (index == -1) {
      selected = null;
      return;
    }
    else if (isRightClick(e) && (noDelete == false)) {
      this.setSelectedIndex(index);
      popupMenu.show(e.getComponent(),e.getX(),e.getY());
    }
    else {
      selected = getModel().getElementAt(index).toString();
    }
  }
  /**
   * No implementation to this method.  
   */
  public void mouseReleased(MouseEvent e) {}
/**
 * Removes an element if delete key is pressed.
 */
  public void keyPressed(KeyEvent e) {
    int index = getSelectedIndex();
    if (e.getKeyCode() == KeyEvent.VK_DELETE && index != -1) {
      if (dialogDragSource != null) {
        dialogDragSource.addElement(((DefaultListModel)getModel()).elementAt(index));
      }
      removeElementAt(index);
    }
  }
  /**
   * No implementation to this method.
   */
  public void keyTyped(KeyEvent e) {}
  /**
  * No implementation to this method.
   */
  public void keyReleased(KeyEvent e) {}
/**
 * Removes element from the model at a specific location. 
 * @param index
 */
  public void removeElementAt(int index) {
    ((DefaultListModel)getModel()).removeElementAt(index);
  }
  /**
   * Removes element at particular index.
   * @param e 'delete'
   */
  void popupMenuDelete_actionPerformed(ActionEvent e) {
    if (getSelectedIndex() == -1) { return; }
    int index = getSelectedIndex();
    if (dialogDragSource != null) {
      dialogDragSource.addElement(((DefaultListModel)getModel()).elementAt(index));
    }
    removeElementAt(index);
  }
}


