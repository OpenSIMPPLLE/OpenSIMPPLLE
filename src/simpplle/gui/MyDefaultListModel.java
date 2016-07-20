/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import javax.swing.DefaultListModel;
import java.util.Vector;
import java.util.List;
import java.util.Iterator;

/**
 *
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller</p>
 */

public class MyDefaultListModel extends DefaultListModel {
  public MyDefaultListModel() {
    super();
  }

  public void addStringData(Object[] data) {
    if (data == null || data.length == 0) { return; }

    for (int i=0; i<data.length; i++) {
      addElement(data[i].toString());
    }
  }

  public void addStringData(Vector data) {
    if (data == null || data.size() == 0) { return; }

    for (int i=0; i<data.size(); i++) {
      addElement(data.elementAt(i).toString());
    }
  }
  public void addStringData(List list) {
    for(Iterator it=list.iterator(); it.hasNext();) {
      addElement(it.next().toString());
    }
  }
}
