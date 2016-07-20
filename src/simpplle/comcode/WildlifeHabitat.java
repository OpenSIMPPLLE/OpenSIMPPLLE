/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import simpplle.gui.SimpplleMain;

/**
 * This class sets up Wildlife Habitat.  Many of the methods in this class are called from Wildlife Habitat dialogs in the GUi
 *
 * @author Documentation by Brian Losi
 * Original source code authorship: Kirk A. Moeller
 */
public abstract class WildlifeHabitat {
  public static final int MAMMALS    = 0;
  public static final int BIRDS      = 1;
  public static final int AMPHIBIANS = 2;
  public static final int REPTILES   = 3;

  public static final int MT_GAP = 0;
  public static final int ID_GAP = 1;
  public static final int R1_WHR = 2;

  private static final String[] groupFiles = new String[]
   {"MTGAP1GROUP.TXT", "IDGAP1GROUP.TXT", "R1WHR1GROUP.TXT"};

  private static final String[] landCoverFiles = new String[]
    {"MTGAP2LANDCOVER.TXT", "IDGAP2LANDCOVER.TXT", "R1WHR2LANDCOVER_STRUC.TXT"};

  private static final String[] densityFiles = new String[]
    {"MTGAP3DENSITY.TXT", null, "R1WHR3DENSITY.TXT"};

  private static final String r1WHRMinAreaFile = "R1WHR4MINAREA.TXT";

  private static final String[] otherFiles = new String[]
    {"MTGAP5OTHER.TXT", null, "R1WHR6OTHER.TXT"};

  private static final String[] elevFiles = new String[]
    {"MTGAP4ELEVATION.TXT", "IDGAP3ELEVATION.TXT", "R1WHR5ELEVATION.TXT"};

  private static Hashtable[] gapSpecies = new Hashtable[]
   { new Hashtable(), new Hashtable(), new Hashtable() };

  private static Hashtable   allSpecies   = new Hashtable();

  private static Hashtable groups = new Hashtable();

  private static boolean[] doEntireGroup = new boolean[] {true,true,true,true};

  private static boolean[] doGroup = new boolean[] {true,true,true,true};

  private static boolean[][] doModel = new boolean[][] {
    {true,true,true},{true,true,true},{true,true,true},{true,true,true}
  };

  private static Hashtable doSpeciesHt = new Hashtable();
/**
 * Checks if group ID is valid for a particular gap model.  
 * @param groupId
 * @return true if Mt Gap and Eastside or Westside Region 1, True if ID gap and Westside Region 1, True if Region 1 and EastSide or Westside Region 1. 
 */
  public static boolean isModelValid(int groupId) {
    int zoneId = Simpplle.getCurrentZone().getId();
    switch(groupId) {
      case MT_GAP:
        return (zoneId == ValidZones.EASTSIDE_REGION_ONE ||
                zoneId == ValidZones.WESTSIDE_REGION_ONE);
      case ID_GAP:
        return (zoneId == ValidZones.WESTSIDE_REGION_ONE);
      case R1_WHR:
        return (zoneId == ValidZones.EASTSIDE_REGION_ONE ||
                zoneId == ValidZones.WESTSIDE_REGION_ONE);
      default:
        return false;
    }
  }
/**
 * Verifies that a particular zone is either Eastside Region one, Westside Region 1 or the Gila region.  
 * @return
 */
  public static boolean isZoneValid() {
    int zoneId = Simpplle.getCurrentZone().getId();

    return (zoneId == ValidZones.EASTSIDE_REGION_ONE ||
            zoneId == ValidZones.WESTSIDE_REGION_ONE ||
            zoneId == ValidZones.GILA);
  }
/**
 * Called from Wildlife Habitat GUI.  
 * @param groupId
 * @return
 */
  public static boolean doEntireGroup(int groupId) { return  doEntireGroup[groupId]; }
  public static void setDoEntireGroup(int groupId, boolean value) {
    doEntireGroup[groupId] = value;
  }

  public static boolean doGroup(int groupId) { return  doGroup[groupId]; }
  public static void setDoGroup(int groupId, boolean value) {
    doGroup[groupId] = value;
  }
/**
 * The model is set in in Gui for Wildlife Habitat.  Designates what type of model Mt Gap, ID gap, and Region 1 for a particular group 
 * Mammals, Bird, Amphibians, Reptiles. 
 * @param groupId choices are Mammals, bird, amphibians, Reptiles.
 * @param modelId  choices are Mt Gap, ID gap, and Region 1
 * @return
 */
  public static boolean doModel(int groupId, int modelId) {
    return  doModel[groupId][modelId];
  }
/**
 * The model is set in in Gui for Wildlife Habitat.
 * @param groupId choices are Mammals, bird, amphibians, Reptiles.
 * @param modelId choices are Mt Gap, ID gap, and Region 1
 * @param value
 */
  public static void setDoModel(int groupId, int modelId, boolean value) {
    doModel[groupId][modelId] = value;
  }

  public static boolean doSpecies(String speciesName) {
    Boolean result = (Boolean)doSpeciesHt.get(speciesName.toUpperCase());

    return ( (result != null) ? result.booleanValue() : false );
  }
/**
 * This is called in the Wildlife Select species GUI
 * @param speciesName
 * @param newValue
 */
  public static void setDoSpecies(String speciesName, boolean newValue) {
    Boolean value = (newValue) ? Boolean.TRUE : Boolean.FALSE;
    doSpeciesHt.put(speciesName.toUpperCase(),value);
  }
/**
 * Sets the wildlife grop name.  
 * @param groupId
 * @return
 */
  public static String getGroupName(int groupId) {
    switch (groupId) {
      case MAMMALS:    return "Mammals";
      case BIRDS:      return "Birds";
      case AMPHIBIANS: return "Amphibians";
      case REPTILES:   return "Reptiles";
      default: return "Unknown";
    }
  }
/**
 * Returns the group id of a particular wildlife group Mammals =0, Birds =1, Amphibians =2, Reptiles =3 .  
 * @param groupName choices are Mammals, Birds, Amphibians, Reptiles
 * @return
 */
  public static int getGroupId(String groupName) {
    if (groupName == null) { return -1; }

    if (groupName.equalsIgnoreCase("mammal"))         { return MAMMALS; }
    else if (groupName.equalsIgnoreCase("bird"))      { return BIRDS; }
    else if (groupName.equalsIgnoreCase("amphibian")) { return AMPHIBIANS; }
    else if (groupName.equalsIgnoreCase("reptile"))   { return REPTILES; }
    else {
      return -1;
    }
  }
/**
 * Returns a string version of Gap.  Types are Montana Gap, Idaho Gap, and Region 1.
 * @param groupId an int representing the type of gap.  
 * @return
 */
  public static String getGapName(int groupId) {
    switch (groupId) {
      case MT_GAP: return "Montana GAP";
      case ID_GAP: return "Idaho GAP";
      case R1_WHR: return "Region 1 WHR";
      default: return "Unknown";
    }
  }
/**
 * Collects all the wildlife species in a particular group into a vector.  
 * @param groupId
 * @return vector with all species of a particular group (e.g. all species of mammals, birds, etc...)
 */
  public static Vector getAllGroupSpecies(int groupId) {
    Vector groupSpecies = new Vector();
    String species, key;

    Enumeration keys = allSpecies.keys();
    while (keys.hasMoreElements()) {
      key     = (String) keys.nextElement();
      species = (String)allSpecies.get(key);
      if (getSpeciesGroup(species) == groupId) {
        groupSpecies.addElement(species);
      }
    }
    return groupSpecies;
  }

  public static Vector getChosenGroupSpecies(int groupId) {
    Vector groupSpecies = new Vector();
    String species, key;

    Enumeration keys = allSpecies.keys();
    while (keys.hasMoreElements()) {
      key     = (String) keys.nextElement();
      species = (String)allSpecies.get(key);
      if (getSpeciesGroup(species) == groupId &&
          (doEntireGroup(groupId) || doSpecies(species))) {
        groupSpecies.addElement(species);
      }
    }
    return groupSpecies;
  }
/**
 * Checks if a species is a gap species.  
 * @param gapId choces are 0=MT, 1=ID, 2 = Region 1
 * @param species
 * @return true if species is in Gap designated by ID.
 */
  public static boolean isGapSpecies(int gapId, String species) {
    return (gapSpecies[gapId].get(species.toUpperCase()) != null);
  }
/**
 * This method is used throughout much of OpenSimpplle.  It makes a vedtor of all the species within a the evaluated zone.  
 * @return
 */
  public static Vector getAllSpecies() {
    Vector tmpAllSpecies = new Vector();

    Enumeration keys = allSpecies.keys();
    while (keys.hasMoreElements()) {
      String key = (String) keys.nextElement();
      tmpAllSpecies.addElement((String)allSpecies.get(key));
    }
    return tmpAllSpecies;
  }
/**
 * Gets the integer index of a particular species group.  Choices for groups are Mammals =0, Birds =1, Amphibians =2, Reptiles =3 
 * @param species
 * @return
 */
  public static int getSpeciesGroup(String species) {
    String groupName = (String) groups.get(species.toUpperCase());
    return getGroupId(groupName);
  }

  public static boolean isValidModel(String species, int gapId) {
    WildlifeHabitatData data;
    data = (WildlifeHabitatData)gapSpecies[gapId].get(species.toUpperCase());

    return (data != null);
  }

  public static WildlifeHabitatData getSpeciesModelData(String species, String groupName) {
    return getSpeciesModelData(species, getSpeciesGroup(groupName));
  }
  public static WildlifeHabitatData getSpeciesModelData(String species, int groupId) {
    return (WildlifeHabitatData)gapSpecies[groupId].get(species.toUpperCase());
  }
/**
 * Gets information on the Wildlife files from particular GAP's group, land cover, density, elevation, and 'other' files 
 * @param filename the end of this will designate which GAP index to take the file in the files array.  
 * @param fin buffered reader
 * @throws SimpplleError
 */
  public static void readDataFiles(String filename, BufferedReader fin)
    throws SimpplleError
  {

    try {
      if (filename.endsWith(groupFiles[ID_GAP])) {
        readGroupFile(fin,ID_GAP);
      }
      else if (filename.endsWith(groupFiles[MT_GAP])) {
        readGroupFile(fin,MT_GAP);
      }
      else if (filename.endsWith(groupFiles[R1_WHR])) {
        readGroupFile(fin,R1_WHR);
      }
      else if (filename.endsWith(landCoverFiles[ID_GAP])) {
        readLandCoverFile(fin,ID_GAP);
      }
      else if (filename.endsWith(landCoverFiles[MT_GAP])) {
        readLandCoverFile(fin,MT_GAP);
      }
      else if (filename.endsWith(landCoverFiles[R1_WHR])) {
        readLandCoverFile(fin,R1_WHR);
      }
      else if (filename.endsWith(densityFiles[R1_WHR])) {
        readDensityFile(fin,R1_WHR);
      }
      else if (filename.endsWith(densityFiles[MT_GAP])) {
        readDensityFile(fin,MT_GAP);
      }
      else if (filename.endsWith(r1WHRMinAreaFile)) {
        readR1WHRMinAreaFile(fin);
      }
      else if (filename.endsWith(elevFiles[ID_GAP])) {
        readElevationFile(fin,ID_GAP);
      }
      else if (filename.endsWith(elevFiles[MT_GAP])) {
        readElevationFile(fin,MT_GAP);
      }
      else if (filename.endsWith(elevFiles[R1_WHR])) {
        readElevationFile(fin,R1_WHR);
      }
      else if (filename.endsWith(otherFiles[MT_GAP])) {
        readOtherFile(fin,MT_GAP);
      }
      else if (filename.endsWith(otherFiles[R1_WHR])) {
        readOtherFile(fin,R1_WHR);
      }
    }
    catch (ParseError err) {
      throw new SimpplleError(err.msg);
    }

  }

/*
  private static BufferedReader openDataFile(String file) throws SimpplleError {
    RegionalZone    zone = Simpplle.getCurrentZone();
    String          dataDir = zone.getWildlifeDir();
    String          dataFile = "";
    GZIPInputStream gzip_in;
    BufferedReader  fin;
    InputStream     is;

    try {
      dataFile = Utility.makePathname(dataDir,file);
      is = zone.getKnowledgeFile(dataFile);

      gzip_in = new GZIPInputStream(is);
      fin = new BufferedReader(new InputStreamReader(gzip_in));

      return fin;

    }
    catch (IOException e) {
      throw new SimpplleError("Could not open " + dataFile + " data file.");
    }
  }
*/

  private static WildlifeHabitatData makeNewWildlifeHabitatData(String commonName, int gapId) {
    WildlifeHabitatData data = new WildlifeHabitatData(commonName,gapId);
    allSpecies.put(commonName.toUpperCase(),commonName);
    doSpeciesHt.put(commonName.toUpperCase(),Boolean.FALSE);
    gapSpecies[gapId].put(commonName.toUpperCase(),data);

    return data;
  }
  private static void readGroupFile(BufferedReader fin, int gapId)
    throws SimpplleError, ParseError {
    StringTokenizerPlus strTok;
    String line;

    try {
      do {
        line = fin.readLine();
        if (line == null) { continue; }

        line = WildlifeHabitatData.preProcessLine(line);
        strTok = new StringTokenizerPlus(line,",");
        String commonName = strTok.getToken();
        String groupName  = strTok.getToken();

        int groupId = getGroupId(groupName);
        groups.put(commonName.toUpperCase(),groupName);

        WildlifeHabitatData data = (WildlifeHabitatData) gapSpecies[gapId].get(commonName.toUpperCase());
        if (data == null) {
          data = makeNewWildlifeHabitatData(commonName,gapId);
          data.setGroupId(groupId);
        }
      }
      while (line != null);
    }
    catch (IOException err) {
      throw new SimpplleError("Could not read from Group file");
    }
  }

  public static void readLandCoverFile(BufferedReader fin, int gapId)
    throws SimpplleError, ParseError
  {
    WildlifeHabitatData data;
    String              commonName, line;
    StringTokenizerPlus strTok;

    try {
      do {
        line = fin.readLine();
        if (line == null) { continue; }
        line = WildlifeHabitatData.preProcessLine(line);
        strTok = new StringTokenizerPlus(line,",");

        commonName = strTok.getToken();
        data = (WildlifeHabitatData) gapSpecies[gapId].get(commonName.toUpperCase());
        if (data == null) {
          data = makeNewWildlifeHabitatData(commonName,gapId);
        }

        if (gapId == R1_WHR) {
          data.readR1WHRLandCoverStruct(strTok);
        }
        else {
          data.readLandCover(strTok);
        }
      }
      while (line != null);
    }
    catch (IOException err) {
      throw new SimpplleError("Could not read from Land Cover file");
    }
  }

  public static void readDensityFile(BufferedReader fin, int gapId)
    throws SimpplleError, ParseError
  {
    WildlifeHabitatData data;
    String              commonName, line;
    StringTokenizerPlus strTok;

    try {
      do {
        line = fin.readLine();
        if (line == null) { continue; }
        line = WildlifeHabitatData.preProcessLine(line);
        strTok = new StringTokenizerPlus(line,",");

        commonName = strTok.getToken();
        data = (WildlifeHabitatData) gapSpecies[gapId].get(commonName.toUpperCase());
        if (data == null) {
          data = makeNewWildlifeHabitatData(commonName,gapId);
        }

        data.readDensity(strTok);
      }
      while (line != null);
    }
    catch (IOException err) {
      throw new SimpplleError("Could not read from Density file");
    }
  }

  public static void readElevationFile(BufferedReader fin, int gapId)
    throws SimpplleError, ParseError
  {
    WildlifeHabitatData data;
    String              commonName, line;
    StringTokenizerPlus strTok;

    try {
      do {
        line = fin.readLine();
        if (line == null) { continue; }
        line = WildlifeHabitatData.preProcessLine(line);
        strTok = new StringTokenizerPlus(line,",");

        commonName = strTok.getToken();
        data = (WildlifeHabitatData) gapSpecies[gapId].get(commonName.toUpperCase());
        if (data == null) {
          data = makeNewWildlifeHabitatData(commonName,gapId);
        }

        if (gapId == R1_WHR) {
          data.readR1WHRElevation(strTok);
        }
        else {
          data.readElevation(strTok);
        }
      }
      while (line != null);
    }
    catch (IOException err) {
      throw new SimpplleError("Could not read from Density file");
    }
  }

  public static void readOtherFile(BufferedReader fin, int gapId)
    throws SimpplleError, ParseError
  {
    WildlifeHabitatData data;
    String              commonName, line;
    StringTokenizerPlus strTok;

    try {
      do {
        line = fin.readLine();
        if (line == null) { continue; }
        line = WildlifeHabitatData.preProcessLine(line);
        strTok = new StringTokenizerPlus(line,",");

        commonName = strTok.getToken();
        data = (WildlifeHabitatData) gapSpecies[gapId].get(commonName.toUpperCase());
        if (data == null) {
          data = makeNewWildlifeHabitatData(commonName,gapId);
        }

        data.readOther(strTok);
      }
      while (line != null);
    }
    catch (IOException err) {
      throw new SimpplleError("Could not read from Density file");
    }
  }

  public static void readR1WHRMinAreaFile(BufferedReader fin)
    throws SimpplleError, ParseError
  {
    WildlifeHabitatData data;
    String              commonName, line;
    StringTokenizerPlus strTok;

    try {
      do {
        line = fin.readLine();
        if (line == null) { continue; }
        line = WildlifeHabitatData.preProcessLine(line);
        strTok = new StringTokenizerPlus(line,",");

        commonName = strTok.getToken();
        data = (WildlifeHabitatData) gapSpecies[R1_WHR].get(commonName.toUpperCase());
        if (data == null) {
          data = makeNewWildlifeHabitatData(commonName,R1_WHR);
        }

        data.readR1WHRMinArea(strTok);
      }
      while (line != null);
    }
    catch (IOException err) {
      throw new SimpplleError("Could not read from Min Area file");
    }
  }

  public static void report(File file) throws SimpplleError {
    try {
      PrintWriter fout = new PrintWriter(new FileWriter(file));

      doGapReport(fout,MT_GAP);
      doGapReport(fout,ID_GAP);
      doGapReport(fout,R1_WHR);

      fout.flush();
      fout.close();
    }
    catch (IOException err) {
      throw new SimpplleError("Unable to write file");
    }

  }

  private static void doGapReport(PrintWriter fout, int gapId) {
    boolean firstCalled = true;;

    if (doModel(MAMMALS,gapId) && doGroup(MAMMALS)) {
      firstCalled = doReport(fout,getChosenGroupSpecies(MAMMALS),gapId,MAMMALS,firstCalled);
    }
    if (doModel(BIRDS,gapId) && doGroup(BIRDS)) {
      firstCalled = doReport(fout,getChosenGroupSpecies(BIRDS),gapId,BIRDS,firstCalled);
    }
    if (doModel(AMPHIBIANS,gapId) && doGroup(AMPHIBIANS)) {
      firstCalled = doReport(fout,getChosenGroupSpecies(AMPHIBIANS),gapId,AMPHIBIANS,firstCalled);
    }
    if (doModel(REPTILES,gapId) && doGroup(REPTILES)) {
      firstCalled = doReport(fout,getChosenGroupSpecies(REPTILES),gapId,REPTILES,firstCalled);
    }
  }

  private static boolean doReport(PrintWriter fout, Vector allSpecies, int gapId,
                               int groupId, boolean firstCalled) {
    if (allSpecies == null || allSpecies.size() == 0) {
      return true;
    }

    Simulation          simulation = Simpplle.getCurrentSimulation();
    int                 nSteps = 0;
    Area                area = Simpplle.getCurrentArea();
    Evu[]               allEvu = area.getAllEvu();
    int                 i, j, ts;
    int[]               acres;
    String              species;
    WildlifeHabitatData data;
    boolean             onFirst = true;
    boolean             noSpecies = true;

    if (simulation != null) {
      nSteps = Simpplle.getCurrentSimulation().getNumTimeSteps();
    }
    acres = new int[nSteps+1];

    for(i=0; i<allSpecies.size(); i++) {
      for(ts=0; ts<=nSteps; ts++) {acres[ts] = 0;}

      species = (String)allSpecies.elementAt(i);
      data    = (WildlifeHabitatData)gapSpecies[gapId].get(species.toUpperCase());
      if (data == null ||
          (doEntireGroup(groupId) == false && data.isSelected() == false)) {
        continue;
      }

      for(j=0; j<allEvu.length; j++) {
        if (allEvu[j] == null) { continue; }
        for(ts=0; ts<=nSteps; ts++) {
          if (data.isValidHabitat(allEvu[j],ts)) {
            acres[ts] += allEvu[j].getAcres();
          }
        }
      }

      if (firstCalled && onFirst) {
        fout.println(getGapName(gapId));
        noSpecies = false;
      }

      if (onFirst) {
        fout.println("  " + getGroupName(groupId));

        fout.print(Formatting.padLeft("",40));
        for(ts=0;ts<=nSteps;ts++) {
          fout.print(Formatting.fixedField(ts,9));
        }
        fout.println();
        onFirst = false;
      }

      fout.print(Formatting.fixedField(species,35));
      for(ts=0;ts<=nSteps;ts++) {
        fout.print(Formatting.fixedField(Area.getFloatAcres(acres[ts]),9));
      }
      fout.println();
    }
    return noSpecies;
  }
}





