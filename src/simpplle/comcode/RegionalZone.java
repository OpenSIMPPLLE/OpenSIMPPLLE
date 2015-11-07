package simpplle.comcode;

import java.io.*;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.zip.GZIPInputStream;

import simpplle.JSimpplle;
import simpplle.comcode.zone.EastsideRegionOne;
import simpplle.comcode.zone.GreatPlainsSteppe;
import simpplle.comcode.zone.MixedGrassPrairie;
import simpplle.comcode.zone.WesternGreatPlainsSteppe;


/**
  * 
  * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This is an abstract class containing fields and methods common to all zones.  This class is not instantiated but
 * rather serves as a common type for zones for use throughout this software.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 */ 

public abstract class RegionalZone {
  public static final int HTGRP      = 0;
  public static final int SPECIES    = 1;
  public static final int SIZE_CLASS = 2;
  public static final int DENSITY    = 3;
  public static final int PROCESS    = 4;

  protected String          name;
  protected boolean         available;
  protected String          zoneDir;
  protected Area[]          sampleAreas;
  protected Hashtable       allFmz;
  protected String          pathwayKnowFile;
  protected String          sysKnowFile;
  protected String          zoneDefnFile;
  protected String          gisExtraFile;
  protected boolean         hasAquatics;

  protected ProcessType[] probDataProcesses;

  protected static final String pathwayDir            = "pathways";
  protected static final String historicPathwayDir    = "historic-pathways";
  protected static final String sampleAreasDir        = "sample-areas";
  protected static final String dataDir               = "data";
  protected static final String appDataDir            = "data";
  protected static final String appGisExtraDir        = "gisdata";
  protected static final String imageDir              = "images";
  protected static final String gisDir                = "gis";
  protected static final String wildlifeDir           = "wildlife";
  protected static final String emissionsDataFile     = "emissions.txt";
  protected static final String insectDiseaseDataFile = "insectdisease.probability";
  protected static final String fireSpreadFile        = "firespread.firespread";
  protected static final String fireTypeFile          = "firetype.firetype";
  protected static final String fireSuppressionFile   = "firesuppression.firesuppression";
  protected static final String fmzFile               = "fmz.fmz";
  protected static final String treatmentLogicFile    = "treatmentlogic.treatmentlogic";
  protected static final String fireSuppWeatherClassAFile  =
      "firesuppweathera.firesuppweathera";
  protected static final String fireSuppWeatherFile  =
      "firesuppweather.firesuppweather";

  protected static final String DEFAULT_FMZ_NAME = "all";

  protected static boolean historicPathways;

  // Note these must be ordered so that the index
  // of the array matches the id's of the zone.
  private static final String availableZones[] = {"Westside Region One",
                                                  "Eastside Region One",
                                                  "Sierra Nevada",
                                                  "Southern California",
                                                  "Gila",
                                                  "South Central Alaska",
                                                  "Southwest Utah",
                                                  "Colorado Front Range",
                                                  "Western Great Plains Steppe",
                                                  "Great Plains Steppe",
                                                  "Mixed Grass Prairie",
                                                  "Colorado Plateau",
                                                  "Teton",
                                                  "Northern Central Rockies"};

  /**
   *  Constructor Sets several fields with values that are common to all zones.
   */
  protected RegionalZone () {
    allFmz           = new Hashtable();
    historicPathways = false;
    hasAquatics      = false;
    pathwayKnowFile  = null;
  }

  /**
    * @return a string array containing names of available zones
    */
  public static String[] availableZones () {
    return availableZones;
  }
  /**
   * Gets the vector of all the valid species for all the habitat type groups in this regional zone.  
   * @return
   */
  public Vector getAllSpecies() { return HabitatTypeGroup.getValidSpecies(); }
  /**
   * Gets the vector of all the valid size classes for all the habitat type groups in this regional zone.  
   * @return
   */
  public Vector getAllSizeClass() { return HabitatTypeGroup.getValidSizeClass(); }
  /**
   * Gets the vector of all the valid densities for all the habitat type groups in this regional zone.  
   * @return
   */
  public Vector getAllDensity() { return HabitatTypeGroup.getValidDensity(); }

  /**
   * 
   * @see simpplle.comcode.zone.WestsideRegionOne
   * @see simpplle.comcode.zone.EastsideRegionOne
   * @return true = the zone is available
   */
  public boolean isAvailable () {
    return available;
  }

  /**
   * Gets the name of the zone.
   * @return the name of the zone.
   */
  public String getName () {
    return name;
  }
/**
 * Name of regional zone. Currently available zones are "Westside Region One", "Eastside Region One", "Sierra Nevada", "Southern California",
 * "Gila", "South Central Alaska", "Southwest Utah",  "Colorado Front Range", "Western Great Plains Steppe",
 *  "Great Plains Steppe", "Mixed Grass Prairie", "Colorado Plateau",  "Teton", "Northern Central Rockies 
 */
  public String toString() {
    return getName();
  }

  /**
   * Gets an array of the legal treatment associated with the zone.
   * @return an array of TreatmentType, the zone's legal treatments.
   */
  public TreatmentType[] getLegalTreatments () {
    return Treatment.getLegalTreatments();
  }

  /**
   * Gets the File object specifying the directory of the zone.
   * @return the zone's directory.
   */
  public String getZoneDir () {
    return zoneDir;
  }

  public abstract String getArcviewDir();

  /**
   * Gets the File object specifying the directory containing
   * the zone's pathway files.
   * @return a String, the zone's pathway files directory.
   */
  public String getPathwayDir () {
    if (historicPathways) {
      return (simpplle.comcode.utility.Utility.makePathname(zoneDir, historicPathwayDir));
    }
    else {
      return (simpplle.comcode.utility.Utility.makePathname(zoneDir, pathwayDir));
    }
  }

  /**
    * gets the current zones' data file directory
    * @return a String
    */
  public String getDataDir() {
    return (simpplle.comcode.utility.Utility.makePathname(zoneDir,dataDir));
  }
/**
 * Makes and gets the Image directory for this regional zone.  An example of this from the sierra nevada regional zone:
 * ("knowledge/zones/sierra-nevada" , "images").  This will make a pathname of "knowledge/zones/sierra-nevada/images"
 * @return the image directory.  from above example: "knowledge/zones/sierra-nevada/images"
 */
  public String getImageDir() {
    return (simpplle.comcode.utility.Utility.makePathname(zoneDir,imageDir));
  }
/**
 * Gets the GIS Directory for this Regional zone.  Must call to the utility class to make the pathname.
 *  Example: From the Regional zone sierra nevada.
 * ("knowledge/zones/sierra-nevada" , "gis").  This will make a pathname of "knowledge/zones/sierra-nevada/gis"
 * @return pathname with zoneDir/gisDir  from above example: "knowledge/zones/sierra-nevada/gis"
 */
  public String getGisDir() {
    return (simpplle.comcode.utility.Utility.makePathname(zoneDir,gisDir));
  }
/**
 * Gets the wildlife directory by passing to Utility class to make the pathname with zonedir and datadir, then passing that again to utility 
 * to make pathname.  
 * "knowledge/zones/sierra-nevada/data" , "wildlife".  This will make a pathname of "knowledge/zones/sierra-nevada/data/wildlife"
 * @return wildlife directory.  from above example: "knowledge/zones/sierra-nevada/data/wildlife"
 */
  public String getWildlifeDir() {
    String dir = simpplle.comcode.utility.Utility.makePathname(zoneDir,dataDir);
    return (simpplle.comcode.utility.Utility.makePathname(dir,wildlifeDir));
  }

  /**
   * Gets a File which is the directory where the zones
   * sample areas are stored.
   * "knowledge/zones/sierra-nevada" , "sample-areas".  This will make a pathname of "knowledge/zones/sierra-nevada/sample-areas"
   * @return the directory contains a zones sample area files. from above example: "knowledge/zones/sierra-nevada/sample-areas"
   */
  public String getSampleAreasDir () {
    return (simpplle.comcode.utility.Utility.makePathname(zoneDir, sampleAreasDir));
  }

  /**
   * Gets an array of a zones sample Areas.
   * This method is used to display a list of available
   * sample areas for a zone.
   * @return the sample area for the zone.
   */
  public Area[] getSampleAreas () {
    return sampleAreas;
  }

  /**
   * Gets the id of the zone, implemented in subclasses.
   * @see simpplle.comcode.zone.WestsideRegionOne
   * @see EastsideRegionOne
   * @return the zone id.
   */
  public abstract int getId ();

  /**
   * Gets an Fmz instance given its name.
   * This method is primarily used when reading an area data file.
   * @param fmz a string, the Fmz's name.
   * @return a fire management zone.
   */
  public Fmz getFmz(String fmz) {
    if (fmz == null) {
      return null;
    }
    else {
      return (Fmz) allFmz.get(fmz.toLowerCase());
    }
  }
/**
 * Gets all the fire management zones for this regional zone.  First makes a temporary fmz array then transfers into it all the elements
 * from the enumeration of the allFmz hashtable elements. 
 * @return array of all the fire management zones for this regional zone.  
 */
  public Fmz[] getAllFmz() {
    Fmz[]       tmpAllFmz;
    Enumeration e;
    int         i=0;

    tmpAllFmz = new Fmz[allFmz.size()];
    e = allFmz.elements();
    while (e.hasMoreElements()) {
      tmpAllFmz[i] = (Fmz) e.nextElement();
      i++;
    }
    return tmpAllFmz;
  }
/**
 * First makes a temporary array of all the fire management zones in this regional zone, then adds them to a vector.  
 * @return the vector with all fire management zones in a regional zone.  
 */
  public Vector getAllFmzNames() {
    Fmz[] tmpAllFmz = getAllFmz();
    Vector fmzNames = new Vector();

    for(int i=0;i<tmpAllFmz.length;i++) {
      fmzNames.addElement(tmpAllFmz[i].getName());
    }
    return fmzNames;
  }
/**
 * Adds a fire management zone to the hash table that contains all the fire management zones for this regional zone.  
 * @param fmz the fire management zone to be added to allFmz hashtable. 
 */
  public void addFmz(Fmz fmz) {
    String fmzName = fmz.getName();
    allFmz.put(fmzName.toLowerCase(),fmz);
  }
/**
 * Removes a fire management zone from this regional zone by removing it from the zone's fmz hashtable.  
 * Also updates the current area's fire management zones, which in turn updates all the EVU's who position is in this fmz, 
 * which are then reset to default fmz.  
 * @param fmz the fire management zone to be removed.  
 */
  public void removeFmz(Fmz fmz) {
    allFmz.remove(fmz.getName());
    fmz = null;

    // Make sure EVU's who point to this fmz are
    // reset to the default fmz.
    Area area = Simpplle.getCurrentArea();
    if (area != null) {
      area.updateFmzData();
    }
  }
/**
 * Removes all fire management zones for this regional zone by clearing the allFmz hashtable.  
 * Also updates the current area's fire management zones, which in turn updates all the EVU's who position is in this fmz, 
 * which are then reset to default fmz.
 * 
 */
  public void removeAllFmz() {
    Fmz all = getDefaultFmz();
    allFmz.clear();
    addFmz(all);

    // Make sure EVU's who point to this fmz are
    // reset to the default fmz.
    Area area = Simpplle.getCurrentArea();
    if (area != null) {
      area.updateFmzData();
    }
  }

  /**
    * Removes any Fmz's from storage that are whose keys
    * are not present in the array of keys provided.
    * &param keepKeys is an array of Strings
    */
  public void updateAllFmz(String keepKeys[]) {
    Enumeration e = allFmz.keys();
    String      name;
    int         i;
    boolean     keep;

    while (e.hasMoreElements()) {
      name = (String) e.nextElement();
      keep = false;
      for(i=0; i<keepKeys.length; i++) {
        if (name.equals(keepKeys[i])) {
          keep = true;
          break;
        }
      }
      if (!keep) {
        allFmz.remove(name);
      }
    }
  }

 /**
  * Gets the default fire management zone.  
  * @return the default fire management zone for this regional zone. 
  */
  public Fmz getDefaultFmz() {
    Fmz fmz = getFmz(DEFAULT_FMZ_NAME);
    if (fmz == null) {
      Fmz.makeDefault();
      fmz = getFmz(DEFAULT_FMZ_NAME);
    }
    return fmz;
  }
/**
 * Gets the default fire management zone for this regional zone.  This returns "all".
 * @return
 */
  public String getDefaultFmzName() { return DEFAULT_FMZ_NAME; }

  protected InputStream getKnowledgeFile(String path) throws IOException {
    InputStream is;

    is = getClass().getResourceAsStream(path);
    if (is == null) {
      String msg = "Could not load " + path + " from jar file";
      System.out.println(msg);
      throw new IOException(msg);
    }
    else {
      return is;
    }
  }

  public File getSystemKnowledgeFile() {
    return new File(getAppDataDir(),sysKnowFile);
  }
  public File getSystemKnowledgePathwayFile() {
    if (pathwayKnowFile != null) {
      return new File(getAppDataDir(),pathwayKnowFile);
    }
    else {
      return new File(getAppDataDir(),sysKnowFile);
    }
  }
  public File getSystemKnowledgeGisExtraFile() {
    return new File(getAppGisExtraDir(),gisExtraFile);
  }
  public File getSystemKnowledgeDummyMDBDir() {
    return getAppDataDir();
  }
  public File getAppDataDir() {
    return new File (JSimpplle.getInstallDirectory(),appDataDir);
  }
  public File getAppGisExtraDir() {
    return new File (JSimpplle.getInstallDirectory(),appGisExtraDir);
  }
  public File getSystemKnowledgeDummyHsqldbDir() {
    return new File(getAppDataDir(),"db");
  }
  public static File getSystemFSLandscapeDiretory() {
    return new File(new File(JSimpplle.getInstallDirectory(),"gis"),"fslandscape");
  }

  protected abstract String[] getGisFiles();

  public void copyGisFiles(File destDir) throws SimpplleError {
    SystemKnowledge.copyArcviewGisFiles(destDir);
  }

  public void copyArcGISFiles(File destDir) throws SimpplleError {
    SystemKnowledge.copyArcGISFiles(destDir);
  }

  /**
   * Loads the pathway file in the parameter path.
   * @param path a File, the full pathname to the pathway data file.
   */
  public HabitatTypeGroup loadPathway (File path) throws SimpplleError {
    HabitatTypeGroup newGroup;
    try {
      newGroup = loadPathway(new FileInputStream(path));
      newGroup.setFilename(path);
      return newGroup;
    }
    catch (Exception e) {
      throw new SimpplleError(e.getMessage() + "\nCould not load Pathway File " + path);
    }
  }
  public void loadAquaticPathway (File path) throws SimpplleError {
    LtaValleySegmentGroup newGroup;
    try {
      newGroup = loadAquaticPathway(new FileInputStream(path));
      newGroup.setFilename(path);
    }
    catch (Exception e) {
      throw new SimpplleError(e.getMessage() + "\nCould not load Pathway File " + path);
    }
  }

  private LtaValleySegmentGroup loadAquaticPathway (InputStream is) throws SimpplleError {
    BufferedReader        fin;
    GZIPInputStream       gzip_in;
    LtaValleySegmentGroup newGroup;

    try {
      gzip_in = new GZIPInputStream(is);
      fin     = new BufferedReader(new InputStreamReader(gzip_in));

      newGroup = loadAquaticPathway(fin);
      fin.close();
    }
    catch (Exception e) {
      throw new SimpplleError("Problems accessing knowledge.jar file");
    }

    return newGroup;
  }
  private HabitatTypeGroup loadPathway (InputStream is) throws SimpplleError {
    BufferedReader   fin;
    GZIPInputStream  gzip_in;
    HabitatTypeGroup newGroup;

    try {
      gzip_in = new GZIPInputStream(is);
      fin     = new BufferedReader(new InputStreamReader(gzip_in));

      newGroup = loadPathway(fin);
      fin.close();
    }
    catch (Exception e) {
      throw new SimpplleError("Problems accessing knowledge.jar file");
    }

    return newGroup;
  }

  public LtaValleySegmentGroup loadAquaticPathway(BufferedReader fin) {
    return LtaValleySegmentGroup.read(fin);
  }
  public HabitatTypeGroup loadPathway(BufferedReader fin) {
    return HabitatTypeGroup.read(fin);
  }

  public void importAquaticPathway(File infile) throws SimpplleError {
    LtaValleySegmentGroup newGroup;

    try {
      newGroup = new LtaValleySegmentGroup();
      newGroup.importTextFile(infile);
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new SimpplleError(e.getMessage() + "\nCould not import Pathway File " + infile);
    }
  }
  public HabitatTypeGroup importPathway(File infile) throws SimpplleError {
    HabitatTypeGroup newGroup;

    try {
      newGroup = new HabitatTypeGroup();
      newGroup.importTextFile(infile);
      return newGroup;
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new SimpplleError(e.getMessage() + "\nCould not import Pathway File " + infile);
    }
  }

  /**
   * Loads all the pathways in a zones pathways data directory.
   */
  public void loadAllAquaticPathways () throws SimpplleError {
//    String msg = (isHistoric()) ? "Loading all Historic Pathways ..." :
//                                  "Loading all Pathways ...";

    Simpplle.setStatusMessage("Loading all Pathways ...");

    // Remove references to currently loaded pathways.
    LtaValleySegmentGroup.clearGroups();

    SystemKnowledge.loadAllAquaticPathways();

    // Garbage collect before returning.
    System.gc();
    Simpplle.clearStatusMessage();
  }
  public void loadAllPathways () throws SimpplleError {
    String msg = (isHistoric()) ? "Loading all Historic Pathways ..." :
                                  "Loading all Pathways ...";

    Simpplle.setStatusMessage(msg);

    // Remove references to currently loaded pathways.
    HabitatTypeGroup.clearGroups();

    SystemKnowledge.loadAllPathways();

    // Garbage collect before returning.
    System.gc();
    Simpplle.clearStatusMessage();
  }

  public void loadAquaticPathway(String groupName) throws SimpplleError {
    if (LtaValleySegmentGroup.findInstance(groupName) == null) { return; }
    SystemKnowledge.loadAquaticPathway(groupName);
  }
  public void loadPathway(String groupName) throws SimpplleError {
    if (HabitatTypeGroup.findInstance(groupName) == null) { return; }
    SystemKnowledge.loadPathway(groupName);
  }

  public void removeAquaticPathway(String groupName) throws SimpplleError {
    LtaValleySegmentGroup group = LtaValleySegmentGroup.findInstance(groupName);
    if (group == null) { return; }

    if (group.isSystemGroup()) {
      SystemKnowledge.loadAquaticPathway(groupName);
    }
    else {
      LtaValleySegmentGroup.removeGroup(groupName);
    }
  }
  /**
   * Removes a pathway 
   * @param groupName
   * @throws SimpplleError
   */
  public void removePathway(String groupName) throws SimpplleError {
    HabitatTypeGroup group = HabitatTypeGroup.findInstance(groupName);
    if (group == null) { return; }

    if (group.isSystemGroup()) {
      SystemKnowledge.loadPathway(groupName);
    }
    else {
      HabitatTypeGroup.removeGroup(groupName);
    }
  }
/**
 * Checks if file pathway is historic pathway. 
 * @return
 */
  public boolean isHistoric() { return historicPathways; }
  /**
   * Tells OpenSimpplle to use historic pathways files.   
   * @param value
   */
  public void setUseHistoricPathways(boolean value) { historicPathways = value; }

  /**
   * This will load everything in the zone.jar file, which
   * includes pathways.
   */
  public void readZoneSystemKnowledgeFile() throws SimpplleError {
    // Remove references to currently loaded pathways.
    HabitatTypeGroup.clearGroups();
    LtaValleySegmentGroup.clearGroups();

    if (pathwayKnowFile != null) {
      SystemKnowledge.readInputFile(getSystemKnowledgePathwayFile(),true);
    }
    SystemKnowledge.readInputFile(getSystemKnowledgeFile(),true);
    // Garbage collect before returning.
    System.gc();
    Simpplle.clearStatusMessage();

    simpplle.comcode.process.FireEvent.setExtremeDataChanged(false);
  }

  public BufferedReader getTreatmentLogicFileStream() throws SimpplleError {
    return SystemKnowledge.getEntryStream(getSystemKnowledgeFile(),
                                          SystemKnowledge.TREATMENT_LOGIC_ENTRY);
  }

  public abstract ProcessType[] getUserProbProcesses();

/**
 * Reads the definition file at "ZONE/LEGAL-DESCRIPTION.TXT" and reads the process and treatemt legal files 
 * @throws SimpplleError
 */
  public void readZoneDefinitionFile() throws SimpplleError {
    File           defnFile = new File(getAppDataDir(),zoneDefnFile);
    BufferedReader fin;

      fin = SystemKnowledge.getEntryStream(defnFile,
                                           "ZONE/LEGAL-DESCRIPTION.TXT");
      Process.readLegalFile(fin);
      Treatment.readLegalFile(fin);
  }
/**
 * Writes the process and treatment legal files, which constitute the zone definition file.  
 * @param fout 
 */
  public void writeZoneDefinitionFile(PrintWriter fout) {
    Process.writeLegalFile(fout);
    Treatment.writeLegalFile(fout);
  }
/**
 * Checks whether this regional zone has aquatic units.
 * @return true if the zone has aquatic evu's
 */
  public boolean hasAquatics() { return hasAquatics; }
/**
 * Checks if regional zone is wyoming by checking if regional zone object is instance of WesternGreatPlainsSteppe, GreatPlainsSteppe, or MixedGrassPrairie.
 * @return true if regional zone is wyoming
 */
  public static boolean isWyoming() {
    RegionalZone zone = Simpplle.getCurrentZone();
    return ((zone instanceof WesternGreatPlainsSteppe) ||
            (zone instanceof GreatPlainsSteppe) ||
            (zone instanceof MixedGrassPrairie));

  }
}

