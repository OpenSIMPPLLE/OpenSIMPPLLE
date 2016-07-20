/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.io.*;
import java.util.Vector;

/**
 * Originally intended this class to be internal to Treatment.
 * However, compiler would not allow me to create an instance from
 * a static context.  Don't really know why, must have something to
 * do with it be an internal class.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class TreatmentLogic {
  public FeasibilityLogic feasibility;
  public Vector           change;

  public TreatmentLogic() {
    feasibility = new FeasibilityLogic();
    change      = new Vector();
  }

  public void clear() {
    feasibility = new FeasibilityLogic();
    change.clear();
  }

  public void read(BufferedReader fin, TreatmentType treatType)
    throws IOException, ParseError
  {
    String         line;
    ChangeLogic    changeLogic;

    change.clear();
    feasibility.read(fin);
    line = fin.readLine();
    while (line != null && line.equals("END") == false) {
      changeLogic = new ChangeLogic();
      changeLogic.setTreatType(treatType);
      changeLogic.read(line);
      change.addElement(changeLogic);
      line = fin.readLine();
    }
  }

  public void save(PrintWriter fout) {
    feasibility.save(fout);

    if (change != null) {
      for(int j=0; j<change.size(); j++) {
        ((ChangeLogic)change.elementAt(j)).save(fout);
      }
      fout.println("END");
    }
  }

  public void printCode(StringBuffer strBuf) {
    feasibility.printCode(strBuf);
    if (change == null || change.size() == 0) { return; }

    ChangeLogic cl;
    for (int i=0; i<change.size(); i++) {
      cl = (ChangeLogic)change.elementAt(i);
      cl.printCode(strBuf);
    }
  }
}



