package simpplle.comcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 * <p>
 * <p> {{ Class Description }}
 *
 * @author Michael Kinsey
 */
public class ParseAquaticNeighbors implements RelationParser{

  // TODO: resolve with importarea
  private boolean hasAttributes;

  /**
   * Reads in the aquatic neighbors of a specified area.  Area id, adjacent id, flow (P or S or N = no flow)
   * Sets existing aquatic unit.  Sets the predecessor, and successor units.
   * @param area the current area whose neighbors are defined in this method.
   * @return true if area file found (sets aquatic neighbor information as well)
   * @throws ParseError caught in ImportArea
   * @throws IOException caught in ImportArea
   */
  @Override
  public boolean readSection(Area area, BufferedReader in, PrintWriter log) throws ParseError, IOException {

    String              line, str;
    StringTokenizerPlus strTok;
    ExistingAquaticUnit eau = null, adjEau = null;
    HashMap units = new HashMap();
    ExistingAquaticUnit[] allEau;
    int                 maxId = -1, id, adjId, index;
    Integer             idObj, adjIdObj;
    String              flowStr;
    char                flow;

    line = in.readLine();
    if (line == null) {
      log.println("Invalid New Area File (file is empty))");
      log.println();
      return false;
    }

    while (line != null && !line.trim().equals("END")) {
      if (line.trim().length() == 0) { line = in.readLine(); continue; }
      strTok = new StringTokenizerPlus(line,",");

      id = strTok.getIntToken();
      adjId = strTok.getIntToken();
      if (id == -1 || adjId == -1) {
        log.println(line);
        log.println("  One of the id's in above line is invalid");
        log.println();
        return false;
      }
      if (id > maxId) { maxId = id; }// sets the current id as new max if greater than current max
      if (adjId > maxId) { maxId = adjId; }

      flowStr = strTok.getToken();
      if (flowStr.length() > 1) {
        index = flowStr.lastIndexOf('\'');
        if (index == -1) {
          index = flowStr.length() - 1;
        }
        flowStr = flowStr.substring(1, index);
      }
      if (flowStr.length() > 1) {
        log.println(line);
        log.println("   " + flowStr + " is not a valid Flow Direction");
        log.println("   Valid values are:  P, S, N");
        return false;
      }
      flow = flowStr.charAt(0);

      idObj = id;
      eau = (ExistingAquaticUnit)units.get(idObj);
      if (eau == null) {
        eau = new ExistingAquaticUnit(id);
        units.put(idObj,eau);
      }

      if (adjId != -9999) {
        adjIdObj = adjId;
        adjEau = (ExistingAquaticUnit) units.get(adjIdObj);
        if (adjEau == null) {
          adjEau = new ExistingAquaticUnit(adjId);
          units.put(adjIdObj, adjEau);
        }

        if (flow == 'P') {
          eau.addPredecessor(adjEau);
        }
        else if (flow == 'S') {
          eau.addSuccessor(adjEau);
        }
      }
      // Get the next line.
      line = in.readLine();
    }
    if (line == null) {
      hasAttributes = false;
    }
    else {
      hasAttributes = true;
    }

    allEau = new ExistingAquaticUnit[maxId+1];

    Iterator it = units.keySet().iterator();
    while (it.hasNext()) {
      idObj = (Integer)it.next();
      eau   = (ExistingAquaticUnit)units.get(idObj);
      allEau[eau.getId()] = eau;
    }

    area.setAllEau(allEau);

    // In the situation where two stream come together at the same
    // Arc Node to form one stream we need to make sure the upper streams
    // do not reference each other as Predecessors or Successors.
    for (int i=0; i<allEau.length; i++) {
      if (allEau[i] == null) { continue; }

      ArrayList preds = allEau[i].getPredecessors();
      if (preds != null && preds.size() > 0) {
        for (int a=0; a<preds.size(); a++) {
          ExistingAquaticUnit unit = (ExistingAquaticUnit)preds.get(a);
          for (int b=a+1; b<preds.size(); b++) {
            ExistingAquaticUnit unit2 = (ExistingAquaticUnit)preds.get(b);

            ArrayList unitPreds = unit.getPredecessors();
            if (unitPreds != null) { unitPreds.remove(unit2); }

            ArrayList unit2Preds = unit2.getPredecessors();
            if (unit2Preds != null) { unit2Preds.remove(unit); }

            ArrayList unitSuccs = unit.getSuccessors();
            if (unitSuccs != null) { unitSuccs.remove(unit2); }

            ArrayList unit2Succs = unit2.getSuccessors();
            if (unit2Succs != null) { unit2Succs.remove(unit); }
          }
        }
      }
    }

    return true;
  }

}
