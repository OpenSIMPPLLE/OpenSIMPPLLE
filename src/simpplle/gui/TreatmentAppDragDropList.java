package simpplle.gui;

import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import simpplle.comcode.Evu;
import simpplle.comcode.TreatmentApplication;
import simpplle.comcode.*;


/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 */

public class TreatmentAppDragDropList extends JList
  implements DropTargetListener,DragSourceListener,
             DragGestureListener, MouseListener
  {

  private Vector                      apps;
  private TreatmentApplication        removedApp;
  private TreatmentScheduleListViewer viewer;

  Object selected=null;
  boolean localDrag = false;
  DropTarget dropTarget = null;
  DragSource dragSource = null;
  JPopupMenu popupMenu = new JPopupMenu();
  JMenuItem popupMenuDelete = new JMenuItem();

  public TreatmentAppDragDropList() {
    dropTarget = new DropTarget (this, this);
    dragSource = new DragSource();
    dragSource.createDefaultDragGestureRecognizer( this,DnDConstants.ACTION_MOVE, this);
    addMouseListener(this);
  }

  public void initList(TreatmentScheduleListViewer viewer,
                       DefaultListModel model) {

    this.viewer = viewer;
    this.setModel(model);
  }

  public void updateListData(Vector v) {
    DefaultListModel     model = (DefaultListModel)getModel();
    TreatmentApplication app;
    String               str;
//    int                  specialAreaLength = 3, roadStatusLength = 3, i;
//    int                  ownershipLength = 3;
    int                  roadStatusLength = 3, i;

    model.clear();
    apps = v;
    for (i=0; i<apps.size(); i++) {
      app = (TreatmentApplication)apps.elementAt(i);

//      str = app.getSpecialArea();
//      if (str == null) { str = "n/a"; }
//      if (str.length() > specialAreaLength) { specialAreaLength = str.length(); }
//
//      str = app.getOwnership();
//      if (str == null) { str = "n/a"; }
//      if (str.length() > ownershipLength) { ownershipLength = str.length(); }

      str = Roads.Status.lookup(app.getRoadStatus()).toString();
      if (str == null) { str = "n/a"; }
      if (str.length() > roadStatusLength) { roadStatusLength = str.length(); }
    }

    for (i=0; i<apps.size(); i++) {
      app = (TreatmentApplication)apps.elementAt(i);
      model.addElement(app.getDescription(roadStatusLength));
    }
    clearSelection();
    selected = null;
  }

  public void dragEnter (DropTargetDragEvent event) {
    event.acceptDrag (DnDConstants.ACTION_COPY_OR_MOVE);
  }
  public void dragExit (DropTargetEvent event) {
    clearSelection();
    viewer.setSelectedItem(null);
  }
  public void dragOver (DropTargetDragEvent event) {
    int index = locationToIndex(event.getLocation());

    if (index == -1) {
      clearSelection();
      viewer.setSelectedItem(null);
    }
    else {
      setSelectedIndex(index);
      viewer.setSelectedItem((TreatmentApplication)apps.elementAt(index));
    }
  }

  public void drop (DropTargetDropEvent event) {
    try {
      Transferable transferable = event.getTransferable();

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

  private boolean isDropOk(DropTargetDropEvent event) {
    return localDrag;
  }

  /**
   * is invoked if the use modifies the current drop gesture
   *
   */
  public void dropActionChanged ( DropTargetDragEvent event ) {
  }

  /**
   * a drag gesture has been initiated
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
//      System.out.println( "nothing was selected");
    }
  }

  public void dragDropEnd (DragSourceDropEvent event) {
  }
  public void dragEnter (DragSourceDragEvent event) {
    DragSourceContext context = event.getDragSourceContext();
    context.setCursor(DragSource.DefaultMoveDrop);
  }
  public void dragExit (DragSourceEvent event) {
    DragSourceContext context = event.getDragSourceContext();
    context.setCursor(DragSource.DefaultMoveNoDrop);
  }
  public void dragOver (DragSourceDragEvent event) {
  }

  public void dropActionChanged ( DragSourceDragEvent event) {
  }

   public void addElement(Object s, Point loc) {
     int index = locationToIndex(loc);
     DefaultListModel model = (DefaultListModel)getModel();

     if (model.contains(s.toString())) { return; }

     if (index == -1) {
       model.addElement(s.toString());
       apps.addElement(removedApp);
     }
     else {
       model.insertElementAt(s.toString(),index);
       apps.insertElementAt(removedApp,index);
     }
     removedApp = null;
     viewer.markListChanged();
  }

  public void removeElement() {
    DefaultListModel model = (DefaultListModel)getModel();
    int              index = model.indexOf(selected);

    removedApp = (TreatmentApplication)apps.elementAt(index);
    apps.removeElementAt(index);
    model.removeElementAt(index);
    selected=null;
    viewer.markListChanged();
    viewer.setSelectedItem(null);
  }

  public void mouseClicked(MouseEvent e){}
  public void mouseEntered(MouseEvent e) {}
  public void mouseExited(MouseEvent e) {}
  public void mousePressed(MouseEvent e)
  {
    int index = locationToIndex(e.getPoint());
    if (index == -1) {
      selected = null;
      viewer.setSelectedItem(null);
    }
    else {
      selected = getModel().getElementAt(index).toString();
      viewer.setSelectedItem((TreatmentApplication)apps.elementAt(index));
    }
  }
  public void mouseReleased(MouseEvent e) {}

  public void removeElementAt(int index) {
    ((DefaultListModel)getModel()).removeElementAt(index);
  }
}

