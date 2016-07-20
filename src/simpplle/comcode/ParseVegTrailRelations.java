/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Reads in and sets the vegetative trail relations of a specified Area.
 */

public class ParseVegTrailRelations implements RelationParser{

  /**
   * Reads in and sets the vegetative trail relations of a specified Area.  Read in are the trial ID's, evu ID's
   * then the evu is assigned the associated trail units (trail ID) and the trails are assigned the associated evu
   * @param area the current area whose Vegetative Trail Relations are defined in this method.
   * @return true if area file found (sets Vegetative Trail Relations information as well)
   * @throws ParseError caught in GUI
   * @throws IOException caught in GUI
   */
  @Override
  public boolean readSection(Area area, BufferedReader in, PrintWriter log) throws ParseError, IOException {
    String line = in.readLine();
    if (line == null) {
      log.println("Invalid New Area File (file is empty))");
      log.println();
      return false;
    }

    while (line != null && !line.trim().equals("END")) {
      if (line.trim().length() == 0) { line = in.readLine(); continue; }
      StringTokenizerPlus strTok = new StringTokenizerPlus(line,",");

      int trailId = strTok.getIntToken();
      int evuId = strTok.getIntToken();
      if (evuId == -1 || trailId == -1) {
        log.println(line + "\n  One of the id's in above line is invalid.\n");
        return false;
      }

      Evu evu = area.getEvu(evuId);
      if (evu == null) {
        log.println(line);
        log.println("In Vegetation-Trails Evu-" + evuId + " is not valid");
        log.println();
      }

      Trails trailUnit = area.getTrailUnit(trailId);
      if (trailUnit == null) {
        log.println(line);
        log.println("In Vegetation-Trails Trail Id: " + trailId + " is not valid");
        log.println();
      }

      if (evu != null && trailUnit != null) {
        evu.addAssociatedTrailUnit(trailUnit);
        trailUnit.addAssociatedVegUnit(evu);
      }

      // Get the next line.
      line = in.readLine();
    }
    return true;
  }
}
