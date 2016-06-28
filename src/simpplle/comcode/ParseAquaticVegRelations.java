package simpplle.comcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 * <p>
 * Reads in the Aquatic vegetation relations of a specified area.
 */
public class ParseAquaticVegRelations implements RelationParser{

  /**
   * Reads in the Aquatic vegetation relations of a specified area.  Sets EVU id, upland adjacent value (valid are A = adjacent or U Upland),
   * gets the existing aquatic unit for this area and adds the evu to it.
   * @param area the current area whose Aquatic vegetation relations are defined in this method.
   * @return true if area file found (sets Aquatic Vegetation relations information as well)
   * @throws ParseError caught in ImportArea
   * @throws IOException caught in ImportArea
   */
  @Override
  public boolean readSection(Area area, BufferedReader in, PrintWriter log) throws ParseError, IOException {
    String              line;
    StringTokenizerPlus strTok;
    ExistingAquaticUnit eau;
    Evu                 evu;
    int                 id, evuId, index;
    String              upAdjStr;
    char                upAdj;

    line = in.readLine();
    if (line == null) {
      log.println("Invalid New Area File (file is empty)");
      log.println();
      return false;
    }

    while (line != null && !line.trim().equals("END")) {
      if (line.trim().length() == 0) { line = in.readLine(); continue; }
      strTok = new StringTokenizerPlus(line,",");

      id = strTok.getIntToken();
      evuId = strTok.getIntToken();
      if (id == -1 || evuId == -1) {
        log.println(line);
        log.println("  One of the id's in above line is invalid");
        log.println();
        return false;
      }

      upAdjStr = null;
      if (strTok.hasMoreTokens()) {
        upAdjStr = strTok.getToken();
      }
      // Currently the amls don't include this info, so use A as default.
      if (upAdjStr == null) { upAdjStr = "A"; }

      if (upAdjStr.length() > 1) {
        index = upAdjStr.lastIndexOf('\'');
        if (index == -1) {
          index = upAdjStr.length() - 1;
        }
        upAdjStr = upAdjStr.substring(1, index);
      }
      if (upAdjStr.length() > 1) {
        log.println(line);
        log.println("   " + upAdjStr + " is not a Upland/Adjacent Value");
        log.println("   Valid values are:  A, U");
        return false;
      }
      upAdj = upAdjStr.charAt(0);

      eau = area.getEau(id);
      if (eau == null) {
        eau = new ExistingAquaticUnit(id);
        area.setEau(eau);
      }

      evu = area.getEvu(evuId);
      if (evu == null) {
        log.println(line);
        log.println("Vegetative Unit " + evuId + " is not valid");
        log.println();
        return false;
      }

      evu.addAssociatedAquaticUnit(eau);
      if (upAdj == 'A') {
        eau.addAdjacentEvu(evu);
      }
      else if (upAdj == 'U') {
        eau.addUplandEvu(evu);
      }

      // Get the next line.
      line = in.readLine();
    }
    return true;
  }


}
