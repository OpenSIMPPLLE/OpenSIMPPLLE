/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import simpplle.JSimpplle;

import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import java.util.zip.GZIPInputStream;

/**
 * RegionalZone contains methods for loading system knowledge for a zone.
 *
 * <p> Original source code authorship: Kirk A. Moeller
 */

public abstract class RegionalZone {

  public static final int HTGRP      = 0;
  public static final int SPECIES    = 1;
  public static final int SIZE_CLASS = 2;
  public static final int DENSITY    = 3;
  public static final int PROCESS    = 4;

  protected int     id;
  protected String  name;
  protected boolean available;
  protected boolean hasAquatics;
  protected Area[]  sampleAreas;

  protected String   zoneDir;
  protected String   arcviewDir;
  protected String   pathwayKnowFile;
  protected String   sysKnowFile;
  protected String   zoneDefnFile;
  protected String   gisExtraFile;
  protected String[] gisFiles;

  protected Hashtable<String,Fmz> allFmz;

  protected ProcessType[] userProbProcesses;

  protected static final String pathwayDir                = "pathways";
  protected static final String historicPathwayDir        = "historic-pathways";
  protected static final String sampleAreasDir            = "sample-areas";
  protected static final String dataDir                   = "data";
  protected static final String appDataDir                = "data";
  protected static final String appGisExtraDir            = "gisdata";
  protected static final String imageDir                  = "images";
  protected static final String gisDir                    = "gis";
  protected static final String wildlifeDir               = "wildlife";
  protected static final String emissionsDataFile         = "emissions.txt";
  protected static final String insectDiseaseDataFile     = "insectdisease.probability";
  protected static final String fireSpreadFile            = "firespread.firespread";
  protected static final String fireTypeFile              = "firetype.firetype";
  protected static final String fireSuppressionFile       = "firesuppression.firesuppression";
  protected static final String fmzFile                   = "fmz.fmz";
  protected static final String treatmentLogicFile        = "treatmentlogic.treatmentlogic";
  protected static final String fireSuppWeatherClassAFile = "firesuppweathera.firesuppweathera";
  protected static final String fireSuppWeatherFile       = "firesuppweather.firesuppweather";

  protected static final String DEFAULT_FMZ_NAME = "all";

  protected static boolean historicPathways;

  // IMPORTANT: The index of each name must match the index of the corresponding zone's ID.

  private static final String availableZones[] = { "Westside Region One",
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
                                                   "Northern Central Rockies" };

  /**
   * Creates a new regional zone with no historic pathways and no aquatics.
   */
  protected RegionalZone () {
    allFmz           = new Hashtable();
    historicPathways = false;
    hasAquatics      = false;
    pathwayKnowFile  = null;
  }

  /**
   * @return All available zone names
   */
  public static String[] availableZones () {
    return availableZones;
  }

  /**
   * @return All valid species for all habitat type groups
   */
  public Vector getAllSpecies() {
    return HabitatTypeGroup.getValidSpecies();
  }

  /**
   * @return All valid size classes for all habitat type groups
   */
  public Vector getAllSizeClass() {
    return HabitatTypeGroup.getValidSizeClass();
  }

  /**
   * @return All valid densities for all habitat type groups
   */
  public Vector getAllDensity() {
    return HabitatTypeGroup.getValidDensity();
  }

  /**
   * @return True if the zone is available
   */
  public boolean isAvailable() {
    return available;
  }

  /**
   * @return The zone's unique idenifier
   */
  public final int getId() {
    return id;
  }

  /**
   * @return The name of the zone
   */
  public String getName() {
    return name;
  }

  /**
   * @return The name of the zone
   */
  public String toString() {
    return getName();
  }

  /**
   * @return An array of legal treatment types
   */
  public TreatmentType[] getLegalTreatments () {
    return Treatment.getLegalTreatments();
  }

  /**
   * @return A path to the zone's directory
   */
  public String getZoneDir() {
    return zoneDir;
  }

  /**
   * @return A path to the zone's arcview directory
   */
  public String getArcviewDir() {
    return arcviewDir;
  }

  /**
   * @return A path to the zone's pathway directory
   */
  public String getPathwayDir () {
    if (historicPathways) {
      return (Utility.makePathname(zoneDir, historicPathwayDir));
    } else {
      return (Utility.makePathname(zoneDir, pathwayDir));
    }
  }

  /**
    * @return A path to the zone's data directory
    */
  public String getDataDir() {
    return (Utility.makePathname(zoneDir,dataDir));
  }

  /**
   * @return A path to the zone's image directory
   */
  public String getImageDir() {
    return (Utility.makePathname(zoneDir,imageDir));
  }

  /**
   * @return A path to the zone's GIS directory
   */
  public String getGisDir() {
    return (Utility.makePathname(zoneDir,gisDir));
  }

  /**
   * @return A path to the zone's wildlife directory
   */
  public String getWildlifeDir() {
    String dir = Utility.makePathname(zoneDir,dataDir);
    return (Utility.makePathname(dir,wildlifeDir));
  }

  /**
   * @return A path to the zone's sample areas directory
   */
  public String getSampleAreasDir() {
    return (Utility.makePathname(zoneDir, sampleAreasDir));
  }

  /**
   * @return An array of sample areas
   */
  public Area[] getSampleAreas () {
    return sampleAreas;
  }

  /**
   * @return A fire management zone with a matching name
   */
  public Fmz getFmz(String fmz) {
    if (fmz == null) {
      return null;
    } else {
      return (Fmz) allFmz.get(fmz.toLowerCase());
    }
  }

  /**
   * @return An array of all fire management zones in this regional zone
   */
  public Fmz[] getAllFmz() {
    int i = 0;
    Fmz[] output = new Fmz[allFmz.size()];
    for (Fmz fmz : allFmz.values()) {
      output[i] = fmz;
      i++;
    }
    return output;
  }

  /**
   * @return A vector of all fire management zone names in this regional zone
   */
  public Vector getAllFmzNames() {
    Fmz[] tmpAllFmz = getAllFmz();
    Vector fmzNames = new Vector();
    for(int i = 0; i < tmpAllFmz.length; i++) {
      fmzNames.addElement(tmpAllFmz[i].getName());
    }
    return fmzNames;
  }

  /**
   * Adds a fire management zone to this regional zone.
   * @param fmz A fire management zone
   */
  public void addFmz(Fmz fmz) {
    allFmz.put(fmz.getName().toLowerCase(),fmz);
  }

  /**
   * Removes a fire management zone from this regional zone and the EVUs in the current area.
   * @param fmz A fire management zone
   */
  public void removeFmz(Fmz fmz) {

    allFmz.remove(fmz.getName());

    Area area = Simpplle.getCurrentArea();
    if (area != null) {
      area.updateFmzData();
    }
  }

  /**
   * Removes all fire management zones from this regional zone and the EVUs in the current area.
   */
  public void removeAllFmz() {

    Fmz all = getDefaultFmz();
    allFmz.clear();
    addFmz(all);

    Area area = Simpplle.getCurrentArea();
    if (area != null) {
      area.updateFmzData();
    }
  }

  /**
   * Removes any fire management zones whose name is not listed in the provided array.
   * @param keepNames An array of FMZ names to keep
   */
  public void updateAllFmz(String keepNames[]) {

    List<String> keysToRemove = new ArrayList<>();

    for (String key : allFmz.keySet()) {

      boolean keep = false;

      for (String keepName : keepNames) {
        if (key.equals(keepName)) {
          keep = true;
          break;
        }
      }

      if (!keep) keysToRemove.add(key);

    }

    for (String key : keysToRemove) {

      allFmz.remove(key);

    }
  }

  /**
   * @return The default fire management zone for this regional zone.
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
   * @return The name of the default fire management zone
   */
  public String getDefaultFmzName() {
    return DEFAULT_FMZ_NAME;
  }

  protected InputStream getKnowledgeFile(String path) throws IOException {
    InputStream is = getClass().getResourceAsStream(path);
    if (is == null) {
      String msg = "Could not load " + path + " from jar file";
      System.out.println(msg);
      throw new IOException(msg);
    } else {
      return is;
    }
  }

  /**
   * @return A system knowledge file for this regional zone
   */
  public File getSystemKnowledgeFile() {
    return new File(getAppDataDir(),sysKnowFile);
  }

  /**
   * @return A pathway file for this regional zone
   */
  public File getSystemKnowledgePathwayFile() {
    if (pathwayKnowFile != null) {
      return new File(getAppDataDir(),pathwayKnowFile);
    } else {
      return new File(getAppDataDir(),sysKnowFile);
    }
  }

  /**
   * @return A GIS extra file for this regional zone
   */
  public File getSystemKnowledgeGisExtraFile() {
    return new File(getAppGisExtraDir(),gisExtraFile);
  }

  /**
   * @return A dummy Microsoft database directory for this regional zone
   */
  public File getSystemKnowledgeDummyMDBDir() {
    return getAppDataDir();
  }

  /**
   * @return An application data directory for this regional zone
   */
  public File getAppDataDir() {
    return new File (JSimpplle.getInstallDirectory(),appDataDir);
  }

  /**
   * @return An application GIS extra directory for this regional zone
   */
  public File getAppGisExtraDir() {
    return new File (JSimpplle.getInstallDirectory(),appGisExtraDir);
  }

  /**
   * @return A dummy HyperSQL database directory for this regional zone
   */
  public File getSystemKnowledgeDummyHsqldbDir() {
    return new File(getAppDataDir(),"db");
  }

  /**
   * @return A system FS landscape directory for this regional zone
   */
  public static File getSystemFSLandscapeDiretory() {
    return new File(new File(JSimpplle.getInstallDirectory(),"gis"),"fslandscape");
  }

  /**
   * @return All GIS file paths for this regional zone
   */
  protected final String[] getGisFiles() {
    return gisFiles;
  }

  /**
   * Copies all GIS files in this regional zone to another directory
   * @param destDir Directory to copy files to
   * @throws SimpplleError
   */
  public void copyGisFiles(File destDir) throws SimpplleError {
    SystemKnowledge.copyArcviewGisFiles(destDir);
  }

  /**
   * Copies all ArcGIS files in this regional zone to another directory
   * @param destDir Directory to copy files to
   * @throws SimpplleError
   */
  public void copyArcGISFiles(File destDir) throws SimpplleError {
    SystemKnowledge.copyArcGISFiles(destDir);
  }

  public HabitatTypeGroup loadPathway (File path) throws SimpplleError {
    try {
      HabitatTypeGroup newGroup = loadPathway(new FileInputStream(path));
      newGroup.setFilename(path);
      return newGroup;
    } catch (Exception e) {
      throw new SimpplleError(e.getMessage() + "\nCould not load Pathway File " + path);
    }
  }

  private HabitatTypeGroup loadPathway (InputStream is) throws SimpplleError {
    try {
      BufferedReader fin = new BufferedReader(new InputStreamReader(new GZIPInputStream(is)));
      HabitatTypeGroup newGroup = loadPathway(fin);
      fin.close();
      return newGroup;
    } catch (Exception e) {
      throw new SimpplleError("Problems accessing knowledge.jar file");
    }
  }

  public HabitatTypeGroup loadPathway(BufferedReader fin) {
    return HabitatTypeGroup.read(fin);
  }

  public void loadAquaticPathway (File path) throws SimpplleError {
    try {
      LtaValleySegmentGroup newGroup = loadAquaticPathway(new FileInputStream(path));
      newGroup.setFilename(path);
    } catch (Exception e) {
      throw new SimpplleError(e.getMessage() + "\nCould not load Pathway File " + path);
    }
  }

  private LtaValleySegmentGroup loadAquaticPathway (InputStream is) throws SimpplleError {
    try {
      BufferedReader fin = new BufferedReader(new InputStreamReader(new GZIPInputStream(is)));
      LtaValleySegmentGroup newGroup = loadAquaticPathway(fin);
      fin.close();
      return newGroup;
    } catch (Exception e) {
      throw new SimpplleError("Problems accessing knowledge.jar file");
    }
  }

  public LtaValleySegmentGroup loadAquaticPathway(BufferedReader fin) {
    return LtaValleySegmentGroup.read(fin);
  }

  public HabitatTypeGroup importPathway(File infile) throws SimpplleError {
    try {
      HabitatTypeGroup newGroup = new HabitatTypeGroup();
      newGroup.importTextFile(infile);
      return newGroup;
    } catch (Exception e) {
      e.printStackTrace();
      throw new SimpplleError(e.getMessage() + "\nCould not import Pathway File " + infile);
    }
  }

  public void importAquaticPathway(File infile) throws SimpplleError {
    try {
      LtaValleySegmentGroup newGroup = new LtaValleySegmentGroup();
      newGroup.importTextFile(infile);
    } catch (Exception e) {
      e.printStackTrace();
      throw new SimpplleError(e.getMessage() + "\nCould not import Pathway File " + infile);
    }
  }

  public void loadAllAquaticPathways () throws SimpplleError {

    Simpplle.setStatusMessage("Loading all Aquatic Pathways ...");

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

    SystemKnowledge.loadAllVegetativePathways();

    // Garbage collect before returning.
    System.gc();

    Simpplle.clearStatusMessage();

  }

  public void loadAquaticPathway(String groupName) throws SimpplleError {
    if (LtaValleySegmentGroup.findInstance(groupName) == null) return;
    SystemKnowledge.loadAquaticPathway(groupName);
  }

  public void loadPathway(String groupName) throws SimpplleError {
    if (HabitatTypeGroup.findInstance(groupName) == null) return;
    SystemKnowledge.loadPathway(groupName);
  }

  public void removeAquaticPathway(String groupName) throws SimpplleError {
    LtaValleySegmentGroup group = LtaValleySegmentGroup.findInstance(groupName);
    if (group == null) return;
    if (group.isSystemGroup()) {
      SystemKnowledge.loadAquaticPathway(groupName);
    } else {
      LtaValleySegmentGroup.removeGroup(groupName);
    }
  }

  public void removePathway(String groupName) throws SimpplleError {
    HabitatTypeGroup group = HabitatTypeGroup.findInstance(groupName);
    if (group == null) { return; }

    if (group.isSystemGroup()) {
      SystemKnowledge.loadPathway(groupName);
    } else {
      HabitatTypeGroup.removeGroup(groupName);
    }
  }

  /**
   * @return True if the zone is using historic pathways
   */
  public boolean isHistoric() { return historicPathways; }

  /**
   * @param useHistoricPathways Boolean indicating if the historic pathways should be used by all regional zones
   */
  public void setUseHistoricPathways(boolean useHistoricPathways) { historicPathways = useHistoricPathways; }

  /**
   * Loads all knowledge for this regional zone.
   */
  public void loadKnowledge() throws SimpplleError {

    // Remove references to currently loaded pathways.
    HabitatTypeGroup.clearGroups();
    LtaValleySegmentGroup.clearGroups();

    if (pathwayKnowFile != null) {
      SystemKnowledge.loadZoneKnowledge(getSystemKnowledgePathwayFile());
    }

    SystemKnowledge.loadZoneKnowledge(getSystemKnowledgeFile());

    System.gc();

    Simpplle.clearStatusMessage();

    FireEvent.setExtremeDataChanged(false);

  }

  /**
   * @return A buffered reader for treatment logic
   */
  public BufferedReader getTreatmentLogicFileStream() throws SimpplleError {
    return SystemKnowledge.getEntryStream(getSystemKnowledgeFile(), SystemKnowledge.TREATMENT_LOGIC_ENTRY);
  }

  /**
   * @return An array of process types used by this regional zone
   */
  public final ProcessType[] getUserProbProcesses() {
    return userProbProcesses;
  }

  /**
   * Reads legal processes and treatments from the zone definition file.
   */
  public void readZoneDefinitionFile() throws SimpplleError {
    File defnFile = new File(getAppDataDir(),zoneDefnFile);
    BufferedReader fin = SystemKnowledge.getEntryStream(defnFile,"ZONE/LEGAL-DESCRIPTION.TXT");
    Process.readLegalFile(fin);
    Treatment.readLegalFile(fin);
  }

  /**
   * Writes legal processes and treatments to the zone definition file.
   * @param fout A print writer for the zone definition file
   */
  public void writeZoneDefinitionFile(PrintWriter fout) {
    Process.writeLegalFile(fout);
    Treatment.writeLegalFile(fout);
  }

  /**
   * @return True if this regional zone has aquatic units
   */
  public boolean hasAquatics() {
    return hasAquatics;
  }

  /**
   * @return True if this instance is WesternGreatPlainsSteppe, GreatPlainsSteppe, or MixedGrassPrairie
   */
  public static boolean isWyoming() {
    RegionalZone zone = Simpplle.getCurrentZone();
    return ((zone instanceof WesternGreatPlainsSteppe) ||
            (zone instanceof GreatPlainsSteppe) ||
            (zone instanceof MixedGrassPrairie));
  }
}

