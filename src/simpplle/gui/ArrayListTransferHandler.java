/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import javax.swing.*;

import simpplle.comcode.Evu;

/** 
 * This class has methods to handle the two types of accepted data flavors (local and serial)
 * and their corresponding array lists.  This is a type of of java swing transfer handler.
 * 
 * @see javax.swing.TransferHandler
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class ArrayListTransferHandler extends TransferHandler {
    DataFlavor localArrayListFlavor, serialArrayListFlavor;
    String localArrayListType = DataFlavor.javaJVMLocalObjectMimeType +
                                ";class=java.util.ArrayList";
    JList source = null;
    int[] indices = null;
    int addIndex = -1; //Location where items were added
    int addCount = 0;  //Number of items added
/**
 * Constructor for ArrayList transfer handler.    First tries to create an instance of local data flavor.  It then goes on to 
 * create an instance of serial array list flavor
 */
    public ArrayListTransferHandler() {
        try {
            localArrayListFlavor = new DataFlavor(localArrayListType);
        } catch (ClassNotFoundException e) {
            System.out.println(
             "ArrayListTransferHandler: unable to create data flavor");
        }
        serialArrayListFlavor = new DataFlavor(ArrayList.class,
                                              "ArrayList");
    }
/**
 * Method to import data.  If parameter t (transferable data instance) is an instance of local or serial ArrayList flavor 
 * creates a new arraylist, casts the data, and stores in the newly created arrayList.  
 * Then imports the data from these arraylists into a list model
 * @param t - instance of data. If this is either local or serial flavor, will be stored in new arraylist, else an exception is thrown. 
 * @see  javax.swing.DefaultListModel
 */
    public boolean importData(JComponent c, Transferable t) {
        JList target = null;
        ArrayList alist = null;
        if (!canImport(c, t.getTransferDataFlavors())) {
            return false;
        }
        try {
            target = (JList)c;
            if (hasLocalArrayListFlavor(t.getTransferDataFlavors())) {
                alist = (ArrayList)t.getTransferData(localArrayListFlavor);
            } else if (hasSerialArrayListFlavor(t.getTransferDataFlavors())) {
                alist = (ArrayList)t.getTransferData(serialArrayListFlavor);
            } else {
                return false;
            }
        } catch (UnsupportedFlavorException ufe) {
            System.out.println("importData: unsupported data flavor");
            return false;
        } catch (IOException ioe) {
            System.out.println("importData: I/O exception");
            return false;
        }

        //At this point we use the same code to retrieve the data
        //locally or serially.

        //We'll drop at the current selected index.
        int index = target.getSelectedIndex();

        //Prevent the user from dropping data back on itself.
        //For example, if the user is moving items #4,#5,#6 and #7 and
        //attempts to insert the items after item #5, this would
        //be problematic when removing the original items.
        //This is interpreted as dropping the same data on itself
        //and has no effect.
        if (source.equals(target)) {
            if (indices != null && index >= indices[0] - 1 &&
                  index <= indices[indices.length - 1]) {
                indices = null;
                return true;
            }
        }

        DefaultListModel listModel = (DefaultListModel)target.getModel();
        int max = listModel.getSize();
        if (index < 0) {
            index = max;
        } else {
            index++;
            if (index > max) {
                index = max;
            }
        }
        addIndex = index;
        addCount = alist.size();
        for (int i=0; i < alist.size(); i++) {
            listModel.add(index++, alist.get(i));
        }

        if (source.equals(target) == false) {
          addIndex = -1;
          addCount = 0;
        }
        return true;
    }
    /**
     * Method to move/remove and adjust indices within models.  
     */

    protected void exportDone(JComponent c, Transferable data, int action) {
        if ((action == MOVE) && (indices != null)) {
            DefaultListModel model = (DefaultListModel)source.getModel();

            //If we are moving items around in the same list, we
            //need to adjust the indices accordingly since those
            //after the insertion point have moved.
            if (addCount > 0) {
                for (int i = 0; i < indices.length; i++) {
                    if (indices[i] > addIndex) {
                        indices[i] += addCount;
                    }
                }
            }
            for (int i = indices.length -1; i >= 0; i--)
                model.remove(indices[i]);
        }
        indices = null;
        addIndex = -1;
        addCount = 0;
    }
/**
 * Loops through the list of data flavors (types) and sees if there is a flavor within there that is an instance of local arrayList
 * @param flavors the array of arraylist flavors (types)
 * @return true if has a local arrayList flavor, false if null or no local arraylist in the flavors
 */
    private boolean hasLocalArrayListFlavor(DataFlavor[] flavors) {
        if (localArrayListFlavor == null) {
            return false;
        }

        for (int i = 0; i < flavors.length; i++) {
            if (flavors[i].equals(localArrayListFlavor)) {
                return true;
            }
        }
        return false;
    }
/**
 * 
 * Loops through the list of data flavors (types) and sees if there is a flavor within there that is an instance of serial  arrayList
 * @param flavors the array of arraylist flavors (types)
 * @return true if has a serial arrayList flavor, false if null or no serial arraylist in the flavors
 */
    private boolean hasSerialArrayListFlavor(DataFlavor[] flavors) {
        if (serialArrayListFlavor == null) {
            return false;
        }

        for (int i = 0; i < flavors.length; i++) {
            if (flavors[i].equals(serialArrayListFlavor)) {
                return true;
            }
        }
        return false;
    }
/**
 * Check to see if data flavor array contains either local or serial arraylist flavors (types).  
 * @returns true if either local or serial data flavor is located in data flavor arraylist
 */
    public boolean canImport(JComponent c, DataFlavor[] flavors) {
        if (hasLocalArrayListFlavor(flavors))  { return true; }
        if (hasSerialArrayListFlavor(flavors)) { return true; }
        return false;
    }
/**
 * Creates a new arraylist of same length as the passed JList (c) and transfers them to an arrayist, then sends to 
 * ArrayListTransferable(ArrayList) method to create transferable arraylist,
 */
    protected Transferable createTransferable(JComponent c) {
        if (c instanceof JList) {
            source = (JList)c;
            indices = source.getSelectedIndices();
            Object[] values = source.getSelectedValues();
            if (values == null || values.length == 0) {
                return null;
            }
            ArrayList alist = new ArrayList(values.length);
            for (int i = 0; i < values.length; i++) {
                Object o = values[i];
                String str = o.toString();
                if (str == null) str = "";
                alist.add(str);
            }
            return new ArrayListTransferable(alist);
        }
        return null;
    }
/**
 * Method to get the action of passed Jcomponent.  Choice is copy_or_move.
 */
    public int getSourceActions(JComponent c) {
        return COPY_OR_MOVE;
    }
/**
 * Using the java.awt.datatransfer.Transferable interface this class implements methods that takes in a arraylist of data
 * checks to see that it is either local or serial data and returns the data, or throws an unsupportedFlavorException. 
 * @see java.awt.datatransfer.Transferable
 *
 */
    public class ArrayListTransferable implements Transferable {
        ArrayList data;

        public ArrayListTransferable(ArrayList alist) {
            data = alist;
        }

        public Object getTransferData(DataFlavor flavor)
                                 throws UnsupportedFlavorException {
            if (!isDataFlavorSupported(flavor)) {
                throw new UnsupportedFlavorException(flavor);
            }
            return data;
        }

        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[] { localArrayListFlavor,
                                      serialArrayListFlavor };
        }

        public boolean isDataFlavorSupported(DataFlavor flavor) {
            if (localArrayListFlavor.equals(flavor)) {
                return true;
            }
            if (serialArrayListFlavor.equals(flavor)) {
                return true;
            }
            return false;
        }
    }
}
