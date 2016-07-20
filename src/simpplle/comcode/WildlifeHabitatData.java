/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-0.9.  By copying this file the user accepts the University of Montana 
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class defines the Wildlife Habitat data.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 * @see simpplle.comcode.Process
 */

public class WildlifeHabitatData {
  private static class WildlifeElevation {
    String description;
    int    min;
    int    max;
  };

  private String    commonName;
  private int       groupId;
  private int       gapId;
  private String    habitat;
  private Hashtable speciesHt;
  private Hashtable sizeClassHt;
  private Vector    density;
  private Vector    elevation;
  private String    aspect;
  private String    slope;
  private String    buffers;
  private Species   neighborSpecies;
  private String    neighborMinNum;
  private String    geoArea;
  private int       minAcres;
  private boolean   selected;


  public WildlifeHabitatData() {
    commonName      = null;
    habitat         = null;
    speciesHt       = new Hashtable();
    sizeClassHt     = new Hashtable();
    density         = null;
    elevation       = new Vector();
    aspect          = null;
    slope           = null;
    buffers         = null;
    neighborSpecies = null;
    neighborMinNum  = null;
    geoArea         = null;
    minAcres        = 0;
    selected        = false;
  }

  public WildlifeHabitatData(String commonName, int gapId) {
    this();
    this.commonName = commonName;
    this.gapId      = gapId;
  }

  public boolean isSelected() { return selected; }
  public void setSelected(boolean state) { selected = state; }

  public String getCommonName() { return commonName; }
  public void setCommonName(String newName) { commonName = newName; }

  public int getGroupId() { return groupId; }
  public void setGroupId(int newGroupId) { groupId = newGroupId; }

  public int getGapId() { return gapId; }
  public void setGapId(int newGapId) { gapId = newGapId; }

  public String[] getHtGrpNames() {
    Enumeration keys = speciesHt.keys();
    String[]    names = new String[speciesHt.size()];
    int         i=0;

    while (keys.hasMoreElements()) {
      names[i] = (String)keys.nextElement();
      i++;
    }
    return names;
  }

  public int getNextElevationIndex(int index) {
    index++;
    if (index >= elevation.size()) { index = 0;}

    return index;
  }

  public boolean isElevationData() {
    return (elevation.size() != 0);
  }
  public boolean isSingleElevation() {
    return (elevation.size() == 1);
  }

  public String getElevationDescription(int index) {
    if (elevation.size() == 0) { return null; }
    if (index < 0 || index >= elevation.size()) { index = 0; }

    WildlifeElevation elev = (WildlifeElevation)elevation.elementAt(index);
    return elev.description;
  }

  public int getElevationMin(int index) {
    if (elevation.size() == 0) { return -1; }
    if (index < 0 || index >= elevation.size()) { index = 0; }

    WildlifeElevation elev = (WildlifeElevation)elevation.elementAt(index);
    return elev.min;
  }

  public int getElevationMax(int index) {
    if (elevation.size() == 0) { return -1; }
    if (index < 0 || index >= elevation.size()) { index = 0; }

    WildlifeElevation elev = (WildlifeElevation)elevation.elementAt(index);
    return elev.max;
  }

  public String getAspect() { return aspect; }

  public String getSlope() { return slope; }

  public String getBuffers() { return buffers; }

  public void readLandCover(StringTokenizerPlus strTok) throws ParseError {
    if (strTok.countTokens() != 2) {
      throw new ParseError("Wrong number of fields in Land Cover file");
    }

    String str = strTok.getToken();
    if (str == null) { return; }
    String htGrp = str.toUpperCase();

    Vector species = (Vector)speciesHt.get(htGrp);
    if (species == null) {
      species = new Vector();
      speciesHt.put(htGrp,species);
    }

    str = strTok.getToken();
    if (str != null) {
      Species tmpSpecies = Species.get(str.toUpperCase());
      if (tmpSpecies == null) { return; }
      species.addElement(tmpSpecies);
    }
  }

  public String getLandCoverText(String htGrpName) {
    Species      species;
    SizeClass    sizeClass;
    Vector       speciesData, sizeClassData;
    StringBuffer strBuf = new StringBuffer();
    Hashtable    ht;
    int          i, j;

    speciesData = (Vector)speciesHt.get(htGrpName);
    for (i=0; i<speciesData.size(); i++) {
      species = (Species)speciesData.elementAt(i);
      strBuf.append("Species == ");
      strBuf.append(species);

      if (gapId == WildlifeHabitat.R1_WHR) {
        ht = (Hashtable)sizeClassHt.get(htGrpName);
        sizeClassData = (Vector)ht.get(species);
        if (sizeClassData.size() > 0) {
          strBuf.append(" -- Size/Structure == ");
        }
        for (j=0; j<sizeClassData.size(); j++) {
          sizeClass = (SizeClass)sizeClassData.elementAt(j);
          if (j != 0) {
            strBuf.append(", ");
          }
          strBuf.append(sizeClass);
        }
      }
      strBuf.append("\n");
    }
    return strBuf.toString();
  }

  public void readR1WHRLandCoverStruct(StringTokenizerPlus strTok) throws ParseError {
    if (strTok.countTokens() != 4) {
      throw new ParseError("Wrong number of fields in Land Cover file");
    }

    habitat    = strTok.getToken();

    String str = strTok.getToken();
    if (str == null) { return; }
    String htGrp = str.toUpperCase();

    Vector speciesData = (Vector)speciesHt.get(htGrp);
    if (speciesData == null) {
      speciesData = new Vector();
      speciesHt.put(htGrp,speciesData);
    }

    str = strTok.getToken();
    if (str == null) { return; }
    Species species = Species.get(str.toUpperCase());
    if (species == null) { return; }
    speciesData.addElement(species);

    Hashtable ht = (Hashtable)sizeClassHt.get(htGrp);
    if (ht == null) {
      ht = new Hashtable();
      sizeClassHt.put(htGrp,ht);
    }

    Vector sizeClass = (Vector)ht.get(species);
    if (sizeClass == null) {
      sizeClass = new Vector();
      ht.put(species,sizeClass);
    }

    str = strTok.getToken();
    if (str == null) { return; }

    StringTokenizerPlus strTok1 = new StringTokenizerPlus(str,";");
    int       count = strTok1.countTokens();
    SizeClass sizeClassValue;
    for(int i=0; i<count; i++) {
      sizeClassValue = SizeClass.get(strTok1.getToken());
      if (sizeClassValue == null) { continue; }
      sizeClass.addElement(sizeClassValue);
    }
  }

  public void readDensity(StringTokenizerPlus strTok) throws ParseError {
    if (strTok.countTokens() != 1) {
      throw new ParseError("Wrong number of fields in Density file");
    }

    StringTokenizerPlus strTok1 = new StringTokenizerPlus(strTok.getToken(),"-");
    if (density == null) { density = new Vector(); }
    int     count = strTok1.countTokens();
    Density densityValue;

    for(int i=0; i<count; i++) {
      densityValue = Density.get(strTok1.getToken());
      if (densityValue == null) { continue; }
      density.addElement(densityValue);
    }
  }

  public void readOther(StringTokenizerPlus strTok) throws ParseError {
    if (strTok.countTokens() != 3) {
      throw new ParseError("Wrong number of fields in Buffers file");
    }

    aspect  = strTok.getToken();
    slope   = strTok.getToken();
    buffers = strTok.getToken();
 }

  public void readElevation(StringTokenizerPlus strTok) throws ParseError {
    if (strTok.countTokens() != 2) {
      throw new ParseError("Wrong number of fields in Elevation file");
    }

    WildlifeElevation elev = new WildlifeElevation();

    elev.description = null;
    elev.min = strTok.getIntToken();
    elev.max = strTok.getIntToken();

    elevation.addElement(elev);
 }

  public void readR1WHRElevation(StringTokenizerPlus strTok) throws ParseError {
    if (strTok.countTokens() != 3) {
      throw new ParseError("Wrong number of fields in Elevation file");
    }

    WildlifeElevation elev = new WildlifeElevation();

    elev.description = strTok.getToken();
    elev.min = strTok.getIntToken();
    elev.max = strTok.getIntToken();

    elevation.addElement(elev);
  }

  public void readR1WHRMinArea(StringTokenizerPlus strTok) throws ParseError {
    if (strTok.countTokens() != 1) {
      throw new ParseError("Wrong number of fields in Min Area file");
    }

    minAcres = strTok.getIntToken();
  }

  public static String preProcessLine(String line) {
    StringBuffer strBuf;
    int begin, index;

    line = line.trim();
    // Replace instances of ",,", with ",?,"
    // As well as filling in the empty space at end of line
    strBuf = new StringBuffer();
    begin = 0;
    index = line.indexOf(",,");
    while (index != -1) {
      strBuf.append(line.substring(begin,index+1));
      strBuf.append("?");
      begin = index + 1;
      index = line.indexOf(",,",(index+1));
    }
    strBuf.append(line.substring(begin));
    if (line.charAt(line.length()-1) == ',') {
      strBuf.append("?");
    }
    line = strBuf.toString();
    strBuf = null;

    strBuf = new StringBuffer();
    begin = 0;
    index = line.indexOf("\"");
    while (index != -1) {
      strBuf.append(line.substring(begin,index));
      begin = index + 1;
      index = line.indexOf("\"",(index+1));
    }
    strBuf.append(line.substring(begin));

    line = strBuf.toString();
    strBuf = null;
    return line;
  }

  public boolean isValidHabitat(Evu evu, int tStep) {
    VegSimStateData state = evu.getState(tStep);
    if (state == null) { return false; }

    Species        unitSpecies   = state.getVeg().getSpecies();
    String         unitHtGrp     = evu.getHabitatTypeGroup().toString();
    SizeClass      unitSizeClass = state.getVeg().getSizeClass();
    Density        unitDensity   = state.getVeg().getDensity();
    Hashtable      ht;

    int i;
    boolean result = false;
    Vector  species = (Vector)speciesHt.get(unitHtGrp);
    if (species != null) {
      for(i=0; i<species.size(); i++) {
        if (unitSpecies == ((Species)species.elementAt(i))) {
          result = true;
          break;
        }
      }
    }
    if (species != null && result == false) { return false; }

    Vector sizeClass = null;
    result = false;
    ht     = (Hashtable)sizeClassHt.get(unitHtGrp);
    if (ht != null) {
      sizeClass = (Vector)ht.get(unitSpecies);
    }
    if (sizeClass != null) {
      for(i=0; i<sizeClass.size(); i++) {
        if (unitSizeClass == ((SizeClass)sizeClass.elementAt(i))) {
          result = true;
          break;
        }
      }
    }
    if (sizeClass != null && result == false) { return false; }

    result = false;
    if (density != null) {
      for(i=0; i<density.size(); i++) {
        if (unitDensity == ((Density)density.elementAt(i))) {
          result = true;
          break;
        }
      }
    }
    if (density != null && result == false) { return false; }

    if (species == null && sizeClass == null && density == null) { return false; }

    if (Area.getRationalAcres((float)minAcres) > evu.getAcres()) {
      return false;
    }

    return true;
  }
}











