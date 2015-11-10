package simpplle.comcode;

import java.io.*;
import java.util.jar.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;



/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class provides for the ability to load a file which has information on files that need to be loaded
 * that modify system knowledge.  
 * <p>Files such as:
 * fmz data, fire spread data, fire type data,
 * insect-disease data, treatments, and lock-in processes.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 */
public class SystemKnowledge {
  public enum Kinds {
    FMZ,
    TREATMENT_SCHEDULE,
    TREATMENT_LOGIC,
    INSECT_DISEASE_PROB,
    PROCESS_PROB_LOGIC,
    FIRE_SUPP_BEYOND_CLASS_A,  /* need for load of old format file */
    EXTREME_FIRE_DATA,
    CLIMATE,
    FIRE_SUPP_WEATHER_BEYOND_CLASS_A,
    REGEN_LOGIC_FIRE,
    REGEN_LOGIC_SUCC,
    REGEN_DELAY_LOGIC,
    FIRE_SEASON,
    PROCESS_SCHEDULE,
    FIRESUPP_PRODUCTION_RATE,  /* need for load of old format file */
    FIRESUPP_SPREAD_RATE,  /* need for load of old format file */
    SPECIES,
    CONIFER_ENCROACHMENT,
    FIRE_TYPE_LOGIC,
    FIRE_SPREAD_LOGIC,
    VEGETATION_PATHWAYS,
    AQUATIC_PATHWAYS,
    INVASIVE_SPECIES_LOGIC,
    INVASIVE_SPECIES_LOGIC_MSU,
    DOCOMPETITION_LOGIC,
    GAP_PROCESS_LOGIC,
    EVU_SEARCH_LOGIC,
    PRODUCING_SEED_LOGIC,
    VEG_UNIT_FIRE_TYPE_LOGIC,
    FIRE_SUPP_CLASS_A_LOGIC,
    FIRE_SUPP_BEYOND_CLASS_A_LOGIC,
    FIRE_SUPP_PRODUCTION_RATE_LOGIC,
    FIRE_SUPP_SPREAD_RATE_LOGIC,
    FIRE_SUPP_WEATHER_CLASS_A_LOGIC,
    FIRE_SPOTTING_LOGIC,
    FIRE_SUPP_EVENT_LOGIC,

    TRACKING_SPECIES_REPORT,

    // ** Needed for Knowledge Source info.
    LP_MPB,
    PP_MPB,
    WP_MPB,
    WBP_MPB,
    WSBW,
    ROOT_DISEASE,
    SPRUCE_BEETLE,
    DF_BEETLE,
    WILDLIFE
  };

  public static final Kinds FMZ                              = Kinds.FMZ;
  public static final Kinds TREATMENT_SCHEDULE               = Kinds.TREATMENT_SCHEDULE;
  public static final Kinds TREATMENT_LOGIC                  = Kinds.TREATMENT_LOGIC;
  public static final Kinds INSECT_DISEASE_PROB              = Kinds.INSECT_DISEASE_PROB;
  public static final Kinds PROCESS_PROB_LOGIC               = Kinds.PROCESS_PROB_LOGIC;
  public static final Kinds FIRE_SUPP_BEYOND_CLASS_A         = Kinds.FIRE_SUPP_BEYOND_CLASS_A;
  public static final Kinds EXTREME_FIRE_DATA                = Kinds.EXTREME_FIRE_DATA;
  public static final Kinds CLIMATE                          = Kinds.CLIMATE;
  public static final Kinds FIRE_SUPP_WEATHER_BEYOND_CLASS_A = Kinds.FIRE_SUPP_WEATHER_BEYOND_CLASS_A;
  public static final Kinds REGEN_LOGIC_FIRE                 = Kinds.REGEN_LOGIC_FIRE;
  public static final Kinds REGEN_LOGIC_SUCC                 = Kinds.REGEN_LOGIC_SUCC;
  public static final Kinds REGEN_DELAY_LOGIC                = Kinds.REGEN_DELAY_LOGIC;
  public static final Kinds FIRE_SEASON                      = Kinds.FIRE_SEASON;
  public static final Kinds PROCESS_SCHEDULE                 = Kinds.PROCESS_SCHEDULE;
  public static final Kinds FIRESUPP_PRODUCTION_RATE         = Kinds.FIRESUPP_PRODUCTION_RATE;
  public static final Kinds FIRESUPP_SPREAD_RATE             = Kinds.FIRESUPP_SPREAD_RATE;
  public static final Kinds SPECIES                          = Kinds.SPECIES;
  public static final Kinds CONIFER_ENCROACHMENT             = Kinds.CONIFER_ENCROACHMENT;
  public static final Kinds FIRE_TYPE_LOGIC                  = Kinds.FIRE_TYPE_LOGIC;
  public static final Kinds FIRE_SPREAD_LOGIC                = Kinds.FIRE_SPREAD_LOGIC;
  public static final Kinds VEGETATION_PATHWAYS              = Kinds.VEGETATION_PATHWAYS;
  public static final Kinds AQUATIC_PATHWAYS                 = Kinds.AQUATIC_PATHWAYS;
  public static final Kinds INVASIVE_SPECIES_LOGIC           = Kinds.INVASIVE_SPECIES_LOGIC;
  public static final Kinds INVASIVE_SPECIES_LOGIC_MSU       = Kinds.INVASIVE_SPECIES_LOGIC_MSU;
  public static final Kinds DOCOMPETITION_LOGIC              = Kinds.DOCOMPETITION_LOGIC;
  public static final Kinds GAP_PROCESS_LOGIC                = Kinds.GAP_PROCESS_LOGIC;
  public static final Kinds EVU_SEARCH_LOGIC                 = Kinds.EVU_SEARCH_LOGIC;
  public static final Kinds PRODUCING_SEED_LOGIC             = Kinds.PRODUCING_SEED_LOGIC;
  public static final Kinds VEG_UNIT_FIRE_TYPE_LOGIC         = Kinds.VEG_UNIT_FIRE_TYPE_LOGIC;
  public static final Kinds FIRE_SUPP_CLASS_A_LOGIC          = Kinds.FIRE_SUPP_CLASS_A_LOGIC;
  public static final Kinds FIRE_SUPP_BEYOND_CLASS_A_LOGIC   = Kinds.FIRE_SUPP_BEYOND_CLASS_A_LOGIC;
  public static final Kinds TRACKING_SPECIES_REPORT          = Kinds.TRACKING_SPECIES_REPORT;
  public static final Kinds FIRE_SUPP_PRODUCTION_RATE_LOGIC  = Kinds.FIRE_SUPP_PRODUCTION_RATE_LOGIC;
  public static final Kinds FIRE_SUPP_SPREAD_RATE_LOGIC      = Kinds.FIRE_SUPP_SPREAD_RATE_LOGIC;
  public static final Kinds FIRE_SUPP_WEATHER_CLASS_A_LOGIC  = Kinds.FIRE_SUPP_WEATHER_CLASS_A_LOGIC;
  public static final Kinds FIRE_SPOTTING_LOGIC              = Kinds.FIRE_SPOTTING_LOGIC;
  public static final Kinds FIRE_SUPP_EVENT_LOGIC            = Kinds.FIRE_SUPP_EVENT_LOGIC;

  // ** Needed for Knowledge Source info.
  public static final Kinds LP_MPB              = Kinds.LP_MPB;
  public static final Kinds PP_MPB              = Kinds.PP_MPB;
  public static final Kinds WP_MPB              = Kinds.WP_MPB;
  public static final Kinds WBP_MPB             = Kinds.WBP_MPB;
  public static final Kinds WSBW                = Kinds.WSBW;
  public static final Kinds ROOT_DISEASE        = Kinds.ROOT_DISEASE;
  public static final Kinds SPRUCE_BEETLE       = Kinds.SPRUCE_BEETLE;
  public static final Kinds DF_BEETLE           = Kinds.DF_BEETLE;
  public static final Kinds WILDLIFE            = Kinds.WILDLIFE;

  public static final int NUMID = Kinds.values().length;

  private static final int VEG     = 0;
  private static final int AQUATIC = 1;


  private static boolean[] hasChanged = new boolean[NUMID];
  private static boolean[] loadSaveMe = new boolean[NUMID];
  private static boolean[] hasUserData   = new boolean[NUMID];

  private static File[] files = new File[NUMID];

  private static HabitatTypeGroup lastPathwayLoaded;
  private static LtaValleySegmentGroup lastAquaticPathwayLoaded;

  public static final String FMZ_ENTRY                      = "DATA/FMZ.TXT";
  public static final String TREATMENT_SCHEDULE_ENTRY       = "DATA/TREATMENT";
  public static final String TREATMENT_LOGIC_ENTRY          = "DATA/TREATMENT-LOGIC.TXT";
  public static final String PROCESS_SCHEDULE_ENTRY         = "DATA/PROCESS";
//  public static final String INSECT_DISEASE_PROB_ENTRY      = "DATA/INSECT-DISEASE-PROB.TXT";
  public static final String OLD_PROCESS_PROB_LOGIC_ENTRY   = "DATA/PROCESS-PROB-LOGIC.SER";
  public static final String PROCESS_PROB_LOGIC_ENTRY       = "DATA/PROCESS-PROB-LOGIC.XML";
  public static final String FIRE_SUPP_CLASS_A_ENTRY        = "DATA/FIRE-SUPPRESSION-CLASS-A.TXT";
  public static final String FIRE_SUPP_BEYOND_CLASS_A_ENTRY = "DATA/FIRE-SUPPRESSION-BEYOND-CLASS-A.TXT";
  public static final String EXTREME_FIRE_DATA_ENTRY        = "DATA/EXTREME-FIRE-DATA.TXT";
  public static final String CLIMATE_ENTRY                  = "DATA/CLIMATE";
  public static final String WILDLIFE_ENTRY                 = "DATA/WILDLIFE";
  public static final String EMISSIONS_ENTRY                = "DATA/EMISSIONS.TXT";
  public static final String PATHWAYS_ENTRY                 = "PATHWAYS";
  public static final String PATHWAYS_ENTRY_AQUATIC         = "AQUATIC-PATHWAYS";
  public static final String HISTORIC_PATHWAYS_ENTRY        = "HISTORIC-PATHWAYS";
  public static final String FIRE_SEASON_ENTRY              = "DATA/FIRE-SEASON.TXT";
  public static final String FIRESUPP_PRODUCTION_RATE_ENTRY = "DATA/FIRESUPP-PRODUCTION-RATE.SER";
  public static final String FIRESUPP_SPREAD_RATE_ENTRY     = "DATA/FIRESUPP-SPREAD-RATE.SER";
  public static final String SPECIES_ENTRY                  = "DATA/SPECIES.SER";
  public static final String CONIFER_ENCROACHMENT_ENTRY     = "DATA/CONIFER-ENCROACHMENT.SER";
  public static final String INVASIVE_SPECIES_LOGIC_ENTRY   = "DATA/INVASIVE-SPECIES-LOGIC.XML";
  public static final String INVASIVE_SPECIES_LOGIC_MSU_ENTRY = "DATA/INVASIVE-SPECIES-LOGIC-MSU.XML";

  public static final String GAP_PROCESS_LOGIC_ENTRY   = "DATA/GAP-PROCESS-LOGIC.XML";
  public static final String DOCOMPETITION_LOGIC_ENTRY = "DATA/DOCOMPETITION-LOGIC.XML";
  public static final String REGEN_DELAY_LOGIC_ENTRY   = "DATA/REGEN-DELAY-LOGIC.XML";

  public static final String EVU_SEARCH_LOGIC_ENTRY     = "DATA/EVU-SEARCH-LOGIC.XML";
  public static final String PRODUCING_SEED_LOGIC_ENTRY = "DATA/PRODUCING-SEED-LOGIC.XML";
  public static final String VEG_UNIT_FIRE_TYPE_LOGIC_ENTRY
      = "DATA/VEG-UNIT-FIRE-TYPE-LOGIC.XML";

  public static final String REGEN_LOGIC_FIRE_ENTRY
      = "DATA/REGENERATION-LOGIC-FIRE.XML";
  public static final String REGEN_LOGIC_SUCC_ENTRY
      = "DATA/REGENERATION-LOGIC-SUCCESSION.XML";
  public static final String OLD_REGEN_LOGIC_ENTRY
      = "DATA/REGENERATION-LOGIC.TXT";

  public static final String FIRE_SUPP_WEATHER_BEYOND_CLASS_A_ENTRY =
    "DATA/FIRE-SUPPRESSION-WEATHER-BEYOND-CLASS-A.XML";
  public static final String OLD_FIRE_SUPP_WEATHER_BEYOND_CLASS_A_ENTRY =
    "DATA/FIRE-SUPPRESSION-WEATHER-BEYOND-CLASS-A.TXT";

  public static final String FIRE_SUPP_WEATHER_CLASS_A_ENTRY  =
    "DATA/FIRE-SUPPRESSION-WEATHER-CLASS-A.TXT";

  public static final String OLD_FIRE_SPREAD_ENTRY      = "DATA/FIRE-SPREAD.TXT";
  public static final String FIRE_SPREAD_DATA_ENTRY = "DATA/FIRE-SPREAD-DATA.TXT";
  public static final String FIRE_SPREAD_LOGIC_ENTRY    = "DATA/FIRE-SPREAD-DATA.XML";


  public static final String FIRE_TYPE_LOGIC_ENTRY  = "DATA/FIRE-TYPE-DATA.XML";
  public static final String FIRE_TYPE_DATA_ENTRY   = "DATA/FIRE-TYPE-DATA.TXT";
  public static final String OLD_TYPE_OF_FIRE_ENTRY = "DATA/TYPE-OF-FIRE.TXT";

  public static final String FIRE_SUPP_CLASS_A_LOGIC_ENTRY =
      "DATA/FIRE-SUPP-CLASS-A-LOGIC.XML";

  public static final String FIRE_SUPP_BEYOND_CLASS_A_LOGIC_ENTRY =
      "DATA/FIRE-SUPP-BEYOND-CLASS-A-LOGIC.XML";

  public static final String FIRE_SUPP_PRODUCTION_RATE_LOGIC_ENTRY =
      "DATA/FIRE-SUPP-PRODUCTION-RATE-LOGIC.XML";

  public static final String FIRE_SUPP_SPREAD_RATE_LOGIC_ENTRY =
      "DATA/FIRE-SUPP-SPREAD-RATE-LOGIC.XML";

  public static final String FIRE_SUPP_WEATHER_CLASS_A_LOGIC_ENTRY =
      "DATA/FIRE-SUPP-WEATHER-CLASS-A-LOGIC.XML";

  public static final String TRACKING_SPECIES_REPORT_ENTRY =
      "DATA/TRACKING-SPECIES-REPORT.XML";

  public static final String FIRE_SPOTTING_LOGIC_ENTRY =
    "DATA/FIRE-SPOTTING-LOGIC.XML";

  public static final String FIRE_SUPP_EVENT_LOGIC_ENTRY =
    "DATA/FIRE-SUPP-EVENT-LOGIC.XML";

  private static final String SYSKNOW_FILEEXT = "sysknowledge";

  /**
   * Method to mark system knowledge changed for a particular kind.
   * This is used often throughout OpenSimpplle
   * @param kind System Knowledge kind of file.
   */
  public static void markChanged(Kinds kind) {
    hasChanged[kind.ordinal()] = true;
    setHasUserData(kind,true);
  }
  
  /**
   * Method to set whether system knowledge has changed.  
   * @param kind System Knowledge kind
   * @param value true if has changed
   */
  public static void setHasChanged(Kinds kind,boolean value) { hasChanged[kind.ordinal()] = value; }
  /**
   * Method notifies that system knowledge has changed through file input.  This is called from the main input reader readInputFile()
   * @param kind System Knowledge kind
   * @return true if has System Knowledge kind changed
   */
  private static boolean hasChanged(Kinds kind) { return hasChanged[kind.ordinal()]; }
/**
 * Sets that the system knowledge for a particular kind to true to indicate user data has been entered.  
 * @param kind SystemKnowledge kind checked against the boolean array with marking if system has user data.  
 * @param value true if file has user data
 */
  public static void setHasUserData(Kinds kind, boolean value) { hasUserData[kind.ordinal()] = value; }
  /**
   * Sets the boolan for has system has user data or a particular system knowledge kind.  
   * These are indexed by the ordinal into system knowledge enumeration.  
   * @param kind SystemKnowledge kind 
   */
  private static void setNotUserData(Kinds kind) { hasUserData[kind.ordinal()] = false; }

  /**
   * Method notifies that system knowledge has changed through file input.  This is called from the main input reader readInputFile()
   * @return true if the System Knowledge kind has changed 
   */
  public static boolean hasKnowledgeChanged() {
    for (int i=0; i<hasChanged.length; i++) {
      if (hasChanged[i]) { return true; }
    }
    return false;
  }

  /**
   * Puts the passed boolean into the loadSaveMe boolean array at a particular system knownledge kinds ordinal into kind enumeration.  
   * This is used to check if there is a particular type of system knowledge file
   * @param which the system knowledge kind.  Its ordinal is used as array index. 
   * @param bool true if has system knowledge of the kind at particular index.  
   */
  public static void setLoadSaveOption(Kinds which, boolean bool) {
    loadSaveMe[which.ordinal()] = bool;
  }
  /**
   * Checks both the hasChanged or hasUserData arrays of a particular system knowledge kind. 
   * These are indexed by the ordinal into system knowledge enumeration.
   * @param which system knowledge kind used as index into arrays.  
   * @return true if hasChanged or hasUserData system knowledge at kind ordinal
   */
  public static boolean hasChangedOrUserData(Kinds which) {
    return (hasChanged[which.ordinal()] || hasUserData[which.ordinal()]);
  }
  /**
   * Checks the boolean array loadSaveMe at the parameter system knowledge kind ordinal into kind enumeration.   
   * @param which
   * @return
   */
  public static boolean isPresentInFile(Kinds which) {
    return (loadSaveMe[which.ordinal()]);
  }
/**
 * Gets the file for a particular system knowledge kind.  
 * @param kind
 * @return
 */
  public static File getFile(Kinds kind) {
    return files[kind.ordinal()];
  }
  /**
   * Puts a system knowledge file into system knowledge file array at the index of its ordinal into kind enumeration. 
   * declared at beginning of SystemKnowledge class. 
   * @param kind system knowledge kind
   * @param file the file 
   */
  public static void setFile(Kinds kind, File file) {
    files[kind.ordinal()] = file;
  }
  /**
   * Clears a file in the system knowledge file array based on the ordinal into kind enumeration.
   * @param kind
   */
  public static void clearFile(Kinds kind) {
    files[kind.ordinal()] = null;
  }
/**
 * Gets the Habitat Type Group at the lastPathwayLoaded
 * @return
 */
  public static HabitatTypeGroup getLastPathwayLoaded() {
    return lastPathwayLoaded;
  }
/**
 * Gets the LtaValleySegmentGroup at the last aquatic pathway loaded. 
 * @return
 */
  public static LtaValleySegmentGroup getLastAquaticPathwayLoaded() {
    return lastAquaticPathwayLoaded;
  }

  /**
   * Method to copy a dummy database for debugging and training purposes. The file with this will be concatenated with the prefix "dummy"  
   * Checks to see if kind is "mdb" 
   * @param destDir File directory to copy database to. 
   * @param prefix 
   * @param kind System Knowledge kind 
   * @throws SimpplleError caught error if could not find file 
   */
  public static void copyDummyDatabaseFile(String destDir, String prefix, String kind) throws SimpplleError {
    BufferedInputStream  fin=null;
    BufferedOutputStream fout;
    String               name=null;
    String               outfile;
    File                 tmpDir;
    int                  data;

    File dir;
    if (kind.equalsIgnoreCase("mdb")) {
      dir = Simpplle.getCurrentZone().getSystemKnowledgeDummyMDBDir();
      outfile = Utility.makePathname(destDir, prefix,kind);
    }
    else {
      dir = Simpplle.getCurrentZone().getSystemKnowledgeDummyHsqldbDir();
      String dbPrefix=prefix+"db";
      File dbDir = new File(destDir,dbPrefix);
      dbDir.mkdir();
      outfile = Utility.makePathname(dbDir.toString(),dbPrefix,kind);
    }
    File filename = new File(dir,"dummy." + kind);

    try {
      fin = new BufferedInputStream(new FileInputStream(filename));
      fout = new BufferedOutputStream(new FileOutputStream(outfile));
      data = fin.read();
      while (data != -1) {
        fout.write(data);
        data = fin.read();
      }
      fout.flush();
      fout.close();
      fin.close();
    }
    catch (IOException err) {
      err.printStackTrace();
      throw new SimpplleError("Could not copy file.");
    }
  }


/**
 * Copies ArcviewGIS Files from the gis directory to a destination file. 
 * @param destDir File to be copied to.  
 * @throws SimpplleError not caught
 */
  public static void copyArcviewGisFiles(File destDir) throws SimpplleError {
    copyGisFiles(destDir,"gis");
  }
  /**
   * Copies ArcGISFiles  files from the arcgis directory to a destination file. 
   * @param destDir File where the ArcGisFiles willbe copied to 
   * @throws SimpplleError not caught
   */
  public static void copyArcGISFiles(File destDir) throws SimpplleError {
    copyGisFiles(destDir,"arcgis");
  }

  /**
   * Checks if there is a GIS Extras file for current zone.  
   * @return true if there is a GIS extraFile and it has data in it.  
   */
  public static boolean existsGISExtras() {
    File file = Simpplle.getCurrentZone().getSystemKnowledgeGisExtraFile();
    return (file.exists() && file.length() > 0);
  }

  
  /**
   * Method to copy GIS coverage data.  If the entry name ends in zip or mdb the jar.  If entry is a directory, a new directory path is created. 
   * @param destDir
   * @throws SimpplleError
   */
  public static void copyCoverage(File destDir) throws SimpplleError {
    JarInputStream       jarIn=null;
    JarEntry             jarEntry;
    BufferedInputStream  fin=null;
    BufferedOutputStream fout;
    String               name=null;
    File                 tmpDir;
    int                  data, i=1;

    File filename = Simpplle.getCurrentZone().getSystemKnowledgeGisExtraFile();
    try {
      tmpDir = new File(destDir.toString(),"SIMPPLLE-gisdata");
      tmpDir.mkdir();
      destDir = tmpDir;
      tmpDir = new File(destDir.toString(),"coverage");
      while (tmpDir.exists()) {
        tmpDir = new File(destDir.toString(),("coverage" + Integer.toString(i)));
        i++;
      }
      tmpDir.mkdir();
      destDir = tmpDir;

      jarIn = new JarInputStream(new FileInputStream(filename));
      fin   = new BufferedInputStream(jarIn);

      jarEntry = jarIn.getNextJarEntry();
      while (jarEntry != null) {
        name = jarEntry.getName().toLowerCase();
        if (name.endsWith("e00.zip") || name.endsWith("mdb")) {
          jarEntry = jarIn.getNextJarEntry();
          continue;
        }

        if (jarEntry.isDirectory()) {
          tmpDir = new File(destDir.toString(),name);
          tmpDir.mkdir();
          jarEntry = jarIn.getNextJarEntry();
          continue;
        }

        String[] pathSplit = name.split("/");
        tmpDir = new File(destDir,pathSplit[0]);
        if (tmpDir.exists() == false) {
          tmpDir.mkdir();
        }
        File outfile = new File(tmpDir,pathSplit[1]);

        fout    = new BufferedOutputStream(new FileOutputStream(outfile));
        data = fin.read();
        while(data != -1) {
          fout.write(data);
          data = fin.read();
        }
        fout.flush();
        fout.close();

        jarEntry = jarIn.getNextJarEntry();
      }
      fin.close();
    }
    catch (IOException err) {
      err.printStackTrace();
      throw new SimpplleError("Could not copy one or more GIS files.");
    }
  }
/**
 * Copies Interchange files by passing to copyGISExtrasFiles from the e00.zip directory to a destination file. 
 * @param destDir File where the data will be copied to
 * @throws SimpplleError
 */
  public static void copyInterchangeFile(File destDir) throws SimpplleError {
    copyGisExtraFiles(destDir, "e00.zip");
  }
  /**
   * Method to copy Geodatabase files, suffixed with "mdb".  
   * @param destDir File where the information is to be copied to.  
   * @throws SimpplleError not caught
   */
  public static void copyGeodatabase(File destDir) throws SimpplleError {
    copyGisExtraFiles(destDir, "mdb");
  }

  /**
   * Copies gis files from the gis directory to a destination directory. 
   * @param destDir
   * @param extension
   * @throws SimpplleError
   */
  private static void copyGisExtraFiles(File destDir, String extension) throws SimpplleError {
    JarInputStream       jarIn=null;
    JarEntry             jarEntry;
    BufferedInputStream  fin=null;
    BufferedOutputStream fout;
    String               name=null;
    File                 tmpDir;
    int                  data;

    File filename = Simpplle.getCurrentZone().getSystemKnowledgeGisExtraFile();
    try {
      tmpDir = new File(destDir.toString(),"SIMPPLLE-gisdata");
      tmpDir.mkdir();
      destDir = tmpDir;

      jarIn = new JarInputStream(new FileInputStream(filename));
      fin   = new BufferedInputStream(jarIn);

      jarEntry = jarIn.getNextJarEntry();
      while (jarEntry != null) {
        name = jarEntry.getName().toLowerCase();
        if (name.endsWith(extension)) {
          File outfile = new File(destDir,name);
          fout    = new BufferedOutputStream(new FileOutputStream(outfile));
          data = fin.read();
          while(data != -1) {
            fout.write(data);
            data = fin.read();
          }
          fout.flush();
          fout.close();
        }
        jarEntry = jarIn.getNextJarEntry();
      }
      fin.close();
    }
    catch (IOException err) {
      err.printStackTrace();
      throw new SimpplleError("Could not copy file.");
    }
  }
  /**
   * Copies gis files from the gis directory to a destination directory.  
   * @param destDir directory where file to be copied is kept
   * @param gisDir the suffix which will be appended at the end of the file to tell what type of GIS file it is.  
   * @throws SimpplleError caught if could not copy one or more files
   */

  private static void copyGisFiles(File destDir, String gisDir) throws SimpplleError {
    JarInputStream       jarIn=null;
    JarEntry             jarEntry;
    BufferedInputStream  fin=null;
    BufferedOutputStream fout;
    String               name=null;
    File                 tmpDir;
    int                  data;

    File filename = Simpplle.getCurrentZone().getSystemKnowledgeFile();
    try {
      jarIn = new JarInputStream(new FileInputStream(filename));
      fin   = new BufferedInputStream(jarIn);

      jarEntry = jarIn.getNextJarEntry();
      while (jarEntry != null) {
        name = jarEntry.getName().toLowerCase();
        if (jarEntry.isDirectory()) {
          jarEntry = jarIn.getNextJarEntry();
          continue;
        }
        if (name.startsWith(gisDir)) {
          String[] pathSplit = name.split("/");
          File outfile = new File(destDir,pathSplit[1]);

          fout    = new BufferedOutputStream(new FileOutputStream(outfile));
          data = fin.read();
          while(data != -1) {
            fout.write(data);
            data = fin.read(); 
          }
          fout.flush();
          fout.close();
        }
        jarEntry = jarIn.getNextJarEntry();
      }
      fin.close();
    }
    catch (IOException err) {
      err.printStackTrace();
      throw new SimpplleError("Could not copy one or more GIS files.");
    }
  }

/**
 * 
 * @param path
 * @return
 * @throws SimpplleError
 */
  public static JarInputStream getSampleAreaStream(String path) throws SimpplleError {
    JarInputStream  jarIn=null;
    JarEntry        jarEntry;
    BufferedReader  fin=null;
    String          name=null;

    File filename = Simpplle.getCurrentZone().getSystemKnowledgeFile();
    try {
      jarIn = new JarInputStream(new FileInputStream(filename));

      jarEntry = jarIn.getNextJarEntry();
      while (jarEntry != null) {
        if (jarEntry.isDirectory()) {
          jarEntry = jarIn.getNextJarEntry();
          continue;
        }
        name = jarEntry.getName().toUpperCase();
        if (name.equals(path.toUpperCase())) {
          return jarIn;
        }
        jarEntry = jarIn.getNextJarEntry();
      }
      return null;
    }
    catch (IOException err) {
      throw new SimpplleError("Could not read Sample Area", err);
    }
  }
  /**
   * Method to load sample area. Uses a JarInputStream to read the FileInputStream  
   * @param area The Area to be loaded - from Simpplle.comcode.Area
   * @throws SimpplleError caught if could not read sample area
   */

  public static void loadSampleArea(Area area) throws SimpplleError {
    JarInputStream  jarIn=null;
    JarEntry        jarEntry;
    BufferedReader  fin=null;
    String          name=null;
    String          path = area.getPath();

    File filename = Simpplle.getCurrentZone().getSystemKnowledgeFile();
    try {
      jarIn = new JarInputStream(new FileInputStream(filename));
      fin   = new BufferedReader(new InputStreamReader(jarIn));

      jarEntry = jarIn.getNextJarEntry();
      while (jarEntry != null) {
        if (jarEntry.isDirectory()) {
          jarEntry = jarIn.getNextJarEntry();
          continue;
        }
        name = jarEntry.getName().toUpperCase();
        if (name.equals(path.toUpperCase())) {
          area.loadArea(fin);
          break;
        }
        jarEntry = jarIn.getNextJarEntry();
      }
      fin.close();
    }
    catch (IOException err) {
      err.printStackTrace();
      throw new SimpplleError("Could not read Sample Area");
    }
  }
  /**
   * Sets a HabitatTypeGroupType file pathway
   * @param groupName
   * @throws SimpplleError
   */
  public static void loadPathway(String groupName) throws SimpplleError {
    loadPathway(groupName,VEG);
  }
  /**
   * Sets aquatic HabitatTypeGroupType file pathway.
   * @param groupName
   * @throws SimpplleError
   */
  public static void loadAquaticPathway(String groupName) throws SimpplleError {
    loadPathway(groupName,AQUATIC);
  }
  
  /**
   * method to determine and load the directory path for files based on HabitatTypeGroupType name and system knowledge kind
   * @param htGrpName
   * @param kind
   * @throws SimpplleError
   */
  private static void loadPathway(String htGrpName, int kind) throws SimpplleError {
    RegionalZone    zone = Simpplle.getCurrentZone();
    JarInputStream  jarIn=null;
    JarEntry        jarEntry;
    BufferedReader  fin=null;
    String          name=null;
    String          groupFileOld = htGrpName;
    String          groupFileNew = htGrpName + ".txt";

    String pathwayStr;
    if (kind == VEG) {
      pathwayStr = (zone.isHistoric()) ? HISTORIC_PATHWAYS_ENTRY : PATHWAYS_ENTRY;
    }
    else {
      pathwayStr = PATHWAYS_ENTRY_AQUATIC;
    }

    File filename = Simpplle.getCurrentZone().getSystemKnowledgePathwayFile();
    try {
      jarIn = new JarInputStream(new FileInputStream(filename));
      fin   = new BufferedReader(new InputStreamReader(jarIn));

      jarEntry = jarIn.getNextJarEntry();
      while (jarEntry != null) {
        if (jarEntry.isDirectory()) {
          jarEntry = jarIn.getNextJarEntry();
          continue;
        }
        name = jarEntry.getName().toUpperCase();
        name = stripZoneDir(name);
        if (name.startsWith(pathwayStr) &&
            (name.endsWith(groupFileOld.toUpperCase()) ||
             name.endsWith(groupFileNew.toUpperCase()))) {
          if (kind == VEG) { zone.loadPathway(fin).setIsUserData(false); }
          else { zone.loadAquaticPathway(fin).setIsUserData(false); }
          break;
        }
        jarEntry = jarIn.getNextJarEntry();
      }
      fin.close();
    }
    catch (IOException err) {
      err.printStackTrace();
      throw new SimpplleError("Could not read Sample Area");
    }
  }
/**
 * Loads all EVU pathways.  
 * @throws SimpplleError
 */
  public static void loadAllPathways() throws SimpplleError {
    loadAllPathways(VEG);
  }
  /**
   * Loads all aquatic files. 
   * @throws SimpplleError
   */
  public static void loadAllAquaticPathways() throws SimpplleError {
    loadAllPathways(AQUATIC);
  }
  
  /**
   * Loads all the pathways of a particular system knowledge kind for the current regional zone.  
   * @param kind the kind of system knowledge 
   * @throws SimpplleError
   */
  private static void loadAllPathways(int kind) throws SimpplleError {
    RegionalZone     zone = Simpplle.getCurrentZone();
    JarInputStream   jarIn=null;
    JarEntry         jarEntry;
    BufferedReader   fin=null;
    String           name=null;

    String pathwayStr;
    if (kind == VEG) {
      pathwayStr = (zone.isHistoric()) ? HISTORIC_PATHWAYS_ENTRY : PATHWAYS_ENTRY;
    }
    else {
      pathwayStr = PATHWAYS_ENTRY_AQUATIC;
    }

    File filename = Simpplle.getCurrentZone().getSystemKnowledgePathwayFile();
    try {
      jarIn = new JarInputStream(new FileInputStream(filename));
      fin   = new BufferedReader(new InputStreamReader(jarIn));

      jarEntry = jarIn.getNextJarEntry();
      while (jarEntry != null) {
        if (jarEntry.isDirectory()) {
          jarEntry = jarIn.getNextJarEntry();
          continue;
        }
        name = jarEntry.getName().toUpperCase();
        name = stripZoneDir(name);
        if (name.startsWith(pathwayStr)) { 
          if (kind == VEG) { zone.loadPathway(fin).setIsUserData(false); }
          else {  zone.loadAquaticPathway(fin).setIsUserData(false); }
        }
        jarEntry = jarIn.getNextJarEntry();
      }
      fin.close();
    }
    catch (IOException err) {
      err.printStackTrace();
      throw new SimpplleError("Could not read Sample Area");
    }
  }

  /**
   * loop thru all of the jar entries until a entry is found that matches
   * the given stream.  If a match is found true is returned, otherwise false.
   * @param jarIn A JarInputStream
   * @param entryName a partial or full entry name to search for.
   * @return a boolean, true if entry found.
   */
  private static boolean findEntry(JarInputStream jarIn, String entryName)
    throws IOException
  {
    JarEntry        jarEntry;
    String          name=null;

    jarEntry = jarIn.getNextJarEntry();
    while (jarEntry != null) {
      if (jarEntry.isDirectory()) {
        jarEntry = jarIn.getNextJarEntry();
        continue;
      }
      name = jarEntry.getName().toUpperCase();
      if (name.startsWith(entryName)) {
        return true;
      }
      jarEntry = jarIn.getNextJarEntry();
    }
    return false;
  }
/**
 * Method to set the BufferedReader to desired input settings.  
 * @param filename File to be read
 * @param entryName 
 * @return BufferedReader
 * @throws SimpplleError caught exception if not able to find an entry or could not read the System Knowledge File.  
 */
  public static BufferedReader getEntryStream(File filename, String entryName)
    throws SimpplleError
  {
    JarInputStream jarIn;
    BufferedReader fin;

    try {
      jarIn = new JarInputStream(new FileInputStream(filename));
      fin   = new BufferedReader(new InputStreamReader(jarIn));

      if (findEntry(jarIn,entryName) == false) {
        throw new SimpplleError("Unable to find entry: " + entryName);
      }
      return fin;
    }
    catch (IOException err) {
      err.printStackTrace();
      throw new SimpplleError("Could not read System Knowledge File");
    }
  }
/**
 * Method to take off the directory path name.
 * @param name name of System Knowledge kind.  
 * @return name of System Knowledge kind in uppercase
 */
  private static String stripZoneDir(String name) {
    name = name.toUpperCase();
    String zoneDir = Simpplle.getCurrentZone().getZoneDir().toUpperCase();
    int    index = name.indexOf(zoneDir);

    if (index != -1) {
      // Add 1 for the slash at the end.
      return name.substring(index + zoneDir.length() + 1);
    }

    return name;
  }
/**
 * Method first takes off the directory path name, then in a series of conditionals to find ordinal of SystemKnowledgeKind which it calls EntryID.
 * @param name
 * @return int of ordinal location of SystemKnowledge kind in SystemKnowledge Kind enumeration.  Ordinal is now called EntryID.
 * Note: if name is null or name starts with WILDLIFE_ENTRY OR EMISSIONS_ENTRY returns null
 */
  private static Kinds getKnowledgeEntryId(String name) {
    name = stripZoneDir(name);

    if (name.equals(FMZ_ENTRY))                           { return FMZ; }
    else if (name.equals(FIRE_TYPE_LOGIC_ENTRY))           { return FIRE_TYPE_LOGIC; }
    else if (name.equals(FIRE_SPREAD_LOGIC_ENTRY))         { return FIRE_SPREAD_LOGIC; }
    else if (name.equals(TREATMENT_SCHEDULE_ENTRY))       { return TREATMENT_SCHEDULE; }
    else if (name.equals(TREATMENT_LOGIC_ENTRY))          { return TREATMENT_LOGIC; }
    else if (name.equals(PROCESS_SCHEDULE_ENTRY))         { return PROCESS_SCHEDULE; }
//    else if (name.equals(INSECT_DISEASE_PROB_ENTRY))      { return INSECT_DISEASE_PROB; }
    else if (name.equals(PROCESS_PROB_LOGIC_ENTRY))       { return PROCESS_PROB_LOGIC; }
    else if (name.equals(INVASIVE_SPECIES_LOGIC_ENTRY))   { return INVASIVE_SPECIES_LOGIC; }
    else if (name.equals(INVASIVE_SPECIES_LOGIC_MSU_ENTRY)) { return INVASIVE_SPECIES_LOGIC_MSU; }
    else if (name.startsWith(PATHWAYS_ENTRY))             { return VEGETATION_PATHWAYS; }
    else if (name.startsWith(HISTORIC_PATHWAYS_ENTRY))    { return VEGETATION_PATHWAYS; }
    else if (name.startsWith(PATHWAYS_ENTRY_AQUATIC))     { return AQUATIC_PATHWAYS; }
    else if (name.equals(EXTREME_FIRE_DATA_ENTRY))        { return EXTREME_FIRE_DATA; }
    else if (name.equals(CLIMATE_ENTRY))                  { return CLIMATE; }
    else if (name.equals(REGEN_LOGIC_FIRE_ENTRY))         { return REGEN_LOGIC_FIRE; }
    else if (name.equals(REGEN_LOGIC_SUCC_ENTRY))         { return REGEN_LOGIC_SUCC; }
    else if (name.equals(FIRE_SEASON_ENTRY))              { return FIRE_SEASON; }
    else if (name.equals(FIRESUPP_PRODUCTION_RATE_ENTRY)) { return FIRESUPP_PRODUCTION_RATE; }
    else if (name.equals(FIRESUPP_SPREAD_RATE_ENTRY))     { return FIRESUPP_SPREAD_RATE; }
    else if (name.equals(SPECIES_ENTRY))                  { return SPECIES; }
    else if (name.equals(CONIFER_ENCROACHMENT_ENTRY))     { return CONIFER_ENCROACHMENT; }

    else if (name.equals(REGEN_DELAY_LOGIC_ENTRY))    { return REGEN_DELAY_LOGIC; }
    else if (name.equals(DOCOMPETITION_LOGIC_ENTRY))  { return DOCOMPETITION_LOGIC; }
    else if (name.equals(GAP_PROCESS_LOGIC_ENTRY))    { return GAP_PROCESS_LOGIC; }
    else if (name.equals(EVU_SEARCH_LOGIC_ENTRY))     { return EVU_SEARCH_LOGIC; }
    else if (name.equals(PRODUCING_SEED_LOGIC_ENTRY)) { return PRODUCING_SEED_LOGIC; }
    else if (name.equals(VEG_UNIT_FIRE_TYPE_LOGIC_ENTRY))        { return VEG_UNIT_FIRE_TYPE_LOGIC; }
    else if (name.equals(FIRE_SUPP_CLASS_A_LOGIC_ENTRY))         { return FIRE_SUPP_CLASS_A_LOGIC; }
    else if (name.equals(FIRE_SUPP_BEYOND_CLASS_A_LOGIC_ENTRY))  { return FIRE_SUPP_BEYOND_CLASS_A_LOGIC; }
    else if (name.equals(FIRE_SUPP_PRODUCTION_RATE_LOGIC_ENTRY)) { return FIRE_SUPP_PRODUCTION_RATE_LOGIC; }
    else if (name.equals(FIRE_SUPP_SPREAD_RATE_LOGIC_ENTRY))     { return FIRE_SUPP_SPREAD_RATE_LOGIC; }
    else if (name.equals(FIRE_SUPP_WEATHER_CLASS_A_LOGIC_ENTRY)) { return FIRE_SUPP_WEATHER_CLASS_A_LOGIC; }
    else if (name.equals(TRACKING_SPECIES_REPORT_ENTRY))         { return TRACKING_SPECIES_REPORT; }
    else if (name.equals(FIRE_SPOTTING_LOGIC_ENTRY))             { return FIRE_SPOTTING_LOGIC; }
    else if (name.equals(FIRE_SUPP_EVENT_LOGIC_ENTRY))           { return FIRE_SUPP_EVENT_LOGIC; }

    else if (name.equals(FIRE_SUPP_BEYOND_CLASS_A_ENTRY)) {
      return FIRE_SUPP_BEYOND_CLASS_A;
    }
    else if (name.equals(FIRE_SUPP_WEATHER_BEYOND_CLASS_A_ENTRY)) {
      return FIRE_SUPP_WEATHER_BEYOND_CLASS_A;
    }

    else if (name.startsWith(WILDLIFE_ENTRY))             { return null;}
    else if (name.equals(EMISSIONS_ENTRY))                { return null;}

    return null;

  }
/**
 * If entryID is Vegetation pathway or Aquatic pathways uses SystemKnowledge PathwayFile() else it reads in the SystemKnowledgeFile.  
 * @param entryId ordinal of SystemKnowledge kind in SystemKnowledge kind enumeration
 * @throws SimpplleError - not caught
 */
  public static void readZoneDefault(Kinds entryId)
    throws SimpplleError
  {
    if (entryId == VEGETATION_PATHWAYS || entryId == AQUATIC_PATHWAYS) {
      readEntry(Simpplle.getCurrentZone().getSystemKnowledgePathwayFile(),entryId);
    }
    else {
      readEntry(Simpplle.getCurrentZone().getSystemKnowledgeFile(), entryId);
    }
  }
  
  /**
   * Sends to main input reader class: readInputFile with the file name, true for zoneFile, false for readAll and false for individual file.  
   * Also creates the loadSaveMe boolean array which is used to tell if there is a file of a particular System Knowledge kind to be loaded or saved.  
   * @param filename File to be read
   * @param entryId ordinal of SystemKnowledge kind in SystemKnowledge kind enumeration
   * @throws SimpplleError not caught
   */
  private static void readEntry(File filename, Kinds entryId)
    throws SimpplleError
  {
    for (int i=0; i<loadSaveMe.length; i++) {
      loadSaveMe[i] = false;
    }
    loadSaveMe[entryId.ordinal()] = true;
    readInputFile(filename,true,false,false);
  }

  /**
   * method to read individual files.  Different from most input files which contain many files of data.  
   * Sends to the overloaded readInputFile() the file name, false for zone file, false for readAll, and true for individual file
   * @param filename File to be read
   * @param entryId
   * @throws SimpplleError
   */
  public static void readIndividualInputFile(File filename, Kinds entryId)
    throws SimpplleError
  {
    for (int i=0; i<loadSaveMe.length; i++) {
      loadSaveMe[i] = false;
    }
    loadSaveMe[entryId.ordinal()] = true;
    readInputFile(filename,false,false,true);
    files[entryId.ordinal()] = filename;
  }

  /**
   * Overloaded readInputFile passes to readInputFile the file name and false for zonefile
   * @param filename File to be read
   * @throws SimpplleError not caught
   */
  public static void readInputFile(File filename) throws SimpplleError {
    readInputFile(filename,false);
  }

  /**
   * Overloaded readInputFile passes to readInputFile the file name, false for zoneFile, true for readAll, and false for isIndividualFile.
   * 
   * @param filename File to be read
   * @param zoneFile true if it is a zone file, false otherwise
   * @throws SimpplleError not caught
   */
  public static void readInputFile(File filename, boolean zoneFile)
    throws SimpplleError
  {
    readInputFile(filename,zoneFile,true,false);
  }

  /**
   * Overloaded readInputFile.  This is the main input file reader.  If zoneFile and readAll are true loops through the loadSaveMe boolean array 
   * and sets to indexes in its length. The method then uses conditionals to decide how to read input files, whether to clear the file previously present.   
   * Note: uses XStream to serialize to XML and back.  For more information on XStream 
   * @link http://xstream.codehaus.org/
   * @param filename File to be read
   * @param zoneFile true if file is a zone file, false otherwise
   * @param readAll true if readAll, false otherwise
   * @param isIndividualFile true if this is an individual file
   * @throws SimpplleError caught if could not read SystemKnowledge file
   */
  public static void readInputFile(File filename, boolean zoneFile,
                                   boolean readAll, boolean isIndividualFile)
    throws SimpplleError
  {
    RegionalZone    zone = Simpplle.getCurrentZone();
    JarInputStream  jarIn=null;
    JarEntry        jarEntry;
    BufferedReader  fin=null;
    String          name=null;
    int             begin, end;
    Kinds           entryId;
    String          msg;

    if (zoneFile && readAll) {
      for (int i=0; i<loadSaveMe.length; i++) {
        loadSaveMe[i] = true;
      }
      FireEvent.resetExtremeData();
    }

    try {
      jarIn = new JarInputStream(new FileInputStream(filename));
      fin   = new BufferedReader(new InputStreamReader(jarIn));

      jarEntry = jarIn.getNextJarEntry();
      while (jarEntry != null) {
        if (jarEntry.isDirectory()) {
          jarEntry = jarIn.getNextJarEntry();
          continue;
        }
        name = jarEntry.getName();
        name = stripZoneDir(name);
        begin = name.indexOf('/');
        end   = name.lastIndexOf('.');
        if (end < 0) { end = name.length(); }
        msg = "  ---> Loading " +
              name.substring(0,begin) + " " +
              name.substring(begin+1,end);
        name    = name.toUpperCase();
        entryId = getKnowledgeEntryId(name);

        if (entryId != null && entryId != VEGETATION_PATHWAYS) {
          Simpplle.setStatusMessage(msg);
        }

        if (entryId == FMZ && loadSaveMe[FMZ.ordinal()]) {
          Fmz.readData(fin);
          if (!isIndividualFile) { Fmz.clearFilename(); }
        }
        if ((name.equals(FIRE_TYPE_DATA_ENTRY) ||
             name.equals(OLD_TYPE_OF_FIRE_ENTRY)) && loadSaveMe[FIRE_TYPE_LOGIC.ordinal()]) {
          FireTypeDataNewerLegacy.readData(fin);
          FireTypeDataNewerLegacy.convertToFireTypeLogic();
          if (!isIndividualFile) { clearFile(FIRE_TYPE_LOGIC); }
        }

        if ((name.equals(FIRE_SPREAD_DATA_ENTRY) ||
             name.equals(OLD_FIRE_SPREAD_ENTRY)) && loadSaveMe[FIRE_SPREAD_LOGIC.ordinal()]) {
          FireSpreadDataNewerLegacy.readData(fin);
          FireSpreadDataNewerLegacy.convertToFireSpreadLogic();
          if (!isIndividualFile) { clearFile(FIRE_SPREAD_LOGIC); }
        }

        if (entryId == TREATMENT_SCHEDULE && loadSaveMe[TREATMENT_SCHEDULE.ordinal()]) {
          TreatmentSchedule ts = Area.createTreatmentSchedule();
          ts.read(fin);
        }
        if (entryId == TREATMENT_LOGIC && loadSaveMe[TREATMENT_LOGIC.ordinal()]) {
          Treatment.readLogic(fin);
          if (!isIndividualFile) { Treatment.closeLogicFile(); }
        }
        if (entryId == PROCESS_SCHEDULE && loadSaveMe[PROCESS_SCHEDULE.ordinal()]) {
          ProcessSchedule ps = Area.createProcessSchedule();
          ps.read(fin);
        }
//        if (entryId == INSECT_DISEASE_PROB && loadSaveMe[INSECT_DISEASE_PROB.ordinal()]) {
//          Process.readProbDataFile(fin);
//          if (!isIndividualFile) { Process.clearFilename(); }
//        }
        if (entryId == AQUATIC_PATHWAYS && loadSaveMe[AQUATIC_PATHWAYS.ordinal()]) {
          lastAquaticPathwayLoaded = zone.loadAquaticPathway(fin);
          lastAquaticPathwayLoaded.setIsUserData((!zoneFile));
        }


        if (entryId == EXTREME_FIRE_DATA && loadSaveMe[EXTREME_FIRE_DATA.ordinal()]) {
          FireEvent.readExtremeData(fin);
        }
        if (entryId == CLIMATE && loadSaveMe[CLIMATE.ordinal()]) {
          Climate climate = Simpplle.getClimate();
          climate.readData(fin);
          if (!isIndividualFile) { climate.clearFilename(); }
        }
        if (entryId == FIRE_SEASON && loadSaveMe[FIRE_SEASON.ordinal()]) {
          FireEvent.readFireSeasonData(fin);
          if (!isIndividualFile) { clearFile(FIRE_SEASON); }
        }
        if (entryId == SPECIES && loadSaveMe[SPECIES.ordinal()]) {
          SimpplleType.readData(jarIn,SimpplleType.SPECIES);
        }
        if (entryId == CONIFER_ENCROACHMENT && loadSaveMe[CONIFER_ENCROACHMENT.ordinal()]) {
          ConiferEncroachmentLogicData.read(jarIn);
        }
        if (name.startsWith(OLD_PROCESS_PROB_LOGIC_ENTRY)) {
          Process.readProbabilityLogic(jarIn);
        }
        // Since there are two sets of pathways we need to make sure we
        // load the correct ones.
        if (entryId == VEGETATION_PATHWAYS && loadSaveMe[VEGETATION_PATHWAYS.ordinal()]) {
          if ((zoneFile == false && name.startsWith(PATHWAYS_ENTRY)) ||
              ((name.startsWith(PATHWAYS_ENTRY) && zone.isHistoric() == false) ||
               (name.startsWith(HISTORIC_PATHWAYS_ENTRY) && zone.isHistoric()))) {
            Simpplle.setStatusMessage(msg);
            lastPathwayLoaded = zone.loadPathway(fin);
            lastPathwayLoaded.setIsUserData((!zoneFile));
          }
        }
        // These don't have loadSaveMe id's
        if (name.startsWith(WILDLIFE_ENTRY)) {
          Simpplle.setStatusMessage(msg);
          WildlifeHabitat.readDataFiles(name, fin);
        }
        if (name.startsWith(EMISSIONS_ENTRY)) {
          Simpplle.setStatusMessage(msg);
          Emissions.readData(fin);
        }
        if (name.startsWith(OLD_FIRE_SPREAD_ENTRY)) {
          FireSpreadDataLegacy.readData(fin);
          FireSpreadDataNewerLegacy.clearDataFilename();
          FireSpreadDataNewerLegacy.setFromLegacyData();
        }
        if (name.startsWith(OLD_TYPE_OF_FIRE_ENTRY)) {
          FireTypeDataLegacy.readData(fin);
          clearFile(FIRE_TYPE_LOGIC);
          FireTypeDataNewerLegacy.setFromLegacyData();
        }
        if (name.startsWith(OLD_FIRE_SUPP_WEATHER_BEYOND_CLASS_A_ENTRY)) {
          FireSuppWeatherData.readData(fin);
        }
        if (name.startsWith(OLD_REGEN_LOGIC_ENTRY)) {
          RegenerationLogic.readDataLegacy(fin);
          clearFile(REGEN_LOGIC_FIRE);
          clearFile(REGEN_LOGIC_SUCC);
        }

        // ** XStream Files ***
        // ********************
        if (entryId == FIRE_TYPE_LOGIC ||
            entryId == FIRE_SPREAD_LOGIC ||
            entryId == FIRE_SUPP_WEATHER_BEYOND_CLASS_A ||
            entryId == REGEN_LOGIC_FIRE ||
            entryId == REGEN_LOGIC_SUCC ||
            entryId == PROCESS_PROB_LOGIC ||
            entryId == INVASIVE_SPECIES_LOGIC ||
            entryId == INVASIVE_SPECIES_LOGIC_MSU ||
            entryId == REGEN_DELAY_LOGIC ||
            entryId == GAP_PROCESS_LOGIC ||
            entryId == DOCOMPETITION_LOGIC ||
            entryId == EVU_SEARCH_LOGIC ||
            entryId == PRODUCING_SEED_LOGIC ||
            entryId == VEG_UNIT_FIRE_TYPE_LOGIC ||
            entryId == FIRE_SUPP_CLASS_A_LOGIC ||
            entryId == FIRE_SUPP_BEYOND_CLASS_A_LOGIC ||
            entryId == FIRE_SUPP_PRODUCTION_RATE_LOGIC ||
            entryId == FIRE_SUPP_SPREAD_RATE_LOGIC ||
            entryId == FIRE_SUPP_WEATHER_CLASS_A_LOGIC ||
            entryId == FIRE_SPOTTING_LOGIC ||
            entryId == FIRE_SUPP_EVENT_LOGIC ||
            entryId == TRACKING_SPECIES_REPORT) {
          String line = fin.readLine();
          // For some reason some files start with blank lines.
          while (line.indexOf("object-stream") == -1) {
            line = fin.readLine();
          }
          StringBuffer strBuf = new StringBuffer(line);
          line="";
          while (line.indexOf("object-stream") == -1) {
            line = fin.readLine();
            strBuf.append(Simpplle.endl);
            strBuf.append(line);
          }
          strBuf.append(Simpplle.endl);
          StringReader strRead = new StringReader(strBuf.toString());
          XStream xs = new XStream(new DomDriver());//xml serializer
          SystemKnowledge.setupXStreamAliases(xs);
          ObjectInputStream in = xs.createObjectInputStream(strRead);

          if (entryId == FIRE_TYPE_LOGIC && loadSaveMe[FIRE_TYPE_LOGIC.ordinal()]) {
            FireEventLogic.read(FireEventLogic.TYPE_STR,in);
          }
          if (entryId == FIRE_SPREAD_LOGIC && loadSaveMe[FIRE_SPREAD_LOGIC.ordinal()]) {
            FireEventLogic.read(FireEventLogic.SPREAD_STR,in);
          }
          if (entryId == FIRE_SPOTTING_LOGIC && loadSaveMe[FIRE_SPOTTING_LOGIC.ordinal()]) {
            FireEventLogic.read(FireEventLogic.FIRE_SPOTTING_STR,in);
          }
          if (entryId == FIRE_SUPP_WEATHER_BEYOND_CLASS_A &&
              loadSaveMe[FIRE_SUPP_WEATHER_BEYOND_CLASS_A.ordinal()]) {
            FireSuppWeatherData.read(in);
          }
          if (entryId == REGEN_LOGIC_FIRE && loadSaveMe[REGEN_LOGIC_FIRE.ordinal()]) {
            RegenerationLogic.readFire(in);
          }
          if (entryId == REGEN_LOGIC_SUCC && loadSaveMe[REGEN_LOGIC_SUCC.ordinal()]) {
            RegenerationLogic.readSuccession(in);
          }
          if (entryId == PROCESS_PROB_LOGIC && loadSaveMe[PROCESS_PROB_LOGIC.ordinal()]) {
            ProcessProbLogic.getInstance().read(in);
          }
          if (entryId == INVASIVE_SPECIES_LOGIC && loadSaveMe[INVASIVE_SPECIES_LOGIC.ordinal()]) {
            InvasiveSpeciesLogic.getInstance().read(in);
          }
          if (entryId == INVASIVE_SPECIES_LOGIC_MSU && loadSaveMe[INVASIVE_SPECIES_LOGIC_MSU.ordinal()]) {
            InvasiveSpeciesLogicMSU.getInstance().read(in);
          }
          if (entryId == REGEN_DELAY_LOGIC && loadSaveMe[REGEN_DELAY_LOGIC.ordinal()]) {
            RegenerationDelayLogic.getInstance().read(in);
          }
          if (entryId == GAP_PROCESS_LOGIC && loadSaveMe[GAP_PROCESS_LOGIC.ordinal()]) {
            GapProcessLogic.getInstance().read(in);
          }
          if (entryId == DOCOMPETITION_LOGIC && loadSaveMe[DOCOMPETITION_LOGIC.ordinal()]) {
            DoCompetitionLogic.getInstance().read(in);
          }
          if (entryId == EVU_SEARCH_LOGIC && loadSaveMe[EVU_SEARCH_LOGIC.ordinal()]) {
            EvuSearchLogic.getInstance().read(in);
          }
          if (entryId == PRODUCING_SEED_LOGIC && loadSaveMe[PRODUCING_SEED_LOGIC.ordinal()]) {
            ProducingSeedLogic.getInstance().read(in);
          }
          if (entryId == VEG_UNIT_FIRE_TYPE_LOGIC && loadSaveMe[VEG_UNIT_FIRE_TYPE_LOGIC.ordinal()]) {
            VegUnitFireTypeLogic.getInstance().read(in);
          }
          if (entryId == FIRE_SUPP_CLASS_A_LOGIC && loadSaveMe[FIRE_SUPP_CLASS_A_LOGIC.ordinal()]) {
            FireSuppClassALogic.getInstance().read(in);
          }
          if (entryId == FIRE_SUPP_BEYOND_CLASS_A_LOGIC && loadSaveMe[FIRE_SUPP_BEYOND_CLASS_A_LOGIC.ordinal()]) {
            FireSuppBeyondClassALogic.getInstance().read(in);
          }
          if (entryId == FIRE_SUPP_PRODUCTION_RATE_LOGIC && loadSaveMe[FIRE_SUPP_PRODUCTION_RATE_LOGIC.ordinal()]) {
            FireSuppProductionRateLogic.getInstance().read(in);
          }
          if (entryId == FIRE_SUPP_SPREAD_RATE_LOGIC && loadSaveMe[FIRE_SUPP_SPREAD_RATE_LOGIC.ordinal()]) {
            FireSuppSpreadRateLogic.getInstance().read(in);
          }
          if (entryId == FIRE_SUPP_WEATHER_CLASS_A_LOGIC && loadSaveMe[FIRE_SUPP_WEATHER_CLASS_A_LOGIC.ordinal()]) {
            FireSuppWeatherClassALogic.getInstance().read(in);
          }
          if (entryId == TRACKING_SPECIES_REPORT && loadSaveMe[TRACKING_SPECIES_REPORT.ordinal()]) {
            TrackingSpeciesReportData.makeInstance().read(in);
          }
          if (entryId == FIRE_SUPP_EVENT_LOGIC && loadSaveMe[FIRE_SUPP_EVENT_LOGIC.ordinal()]) {
            FireSuppEventLogic.getInstance().read(in);
          }

          strRead.close();
          in.close();
          strBuf = null;
        }

        if (entryId != null) {
          setHasChanged(entryId, false);
          setHasUserData(entryId, (!zoneFile));

          if (!isIndividualFile) { clearFile(entryId); }
        }


        jarEntry = jarIn.getNextJarEntry();
      }
//      if (d != null) { hr.close(); }
      Simpplle.clearStatusMessage();
    }
    catch (IOException err) {
      throw new SimpplleError("Could not read System Knowledge File",err);
    }
    catch (ClassNotFoundException err) {
      err.printStackTrace();
      throw new SimpplleError("Could not read System Knowledge File");
    }
    catch (ParseError err) {
      throw new SimpplleError("While reading System Knowledge File: " + name
                              + "\n" + err.msg);
    }
    finally {
      Utility.close(fin);
      Utility.close(jarIn);
    }
  }
  /**
   * Method processes System Knowledge files and creates an boolean array called loadSaveMe to indicate whether there is a system knowledge file of a particular kind. 
   * pertaining to Fire Type, Fire Logic, and Fire Spread 
   * @param filename File to be processed.  
   * @throws SimpplleError caught if could not read file
   */

  public static void processInputFileEntries(File filename)
    throws SimpplleError
  {
    JarInputStream  jarIn;
    JarEntry        jarEntry;
    String          name=null;
    Kinds           entryId;

    for (int i=0; i<loadSaveMe.length; i++) {
      loadSaveMe[i] = false;
    }

    try {
      jarIn = new JarInputStream(new FileInputStream(filename));

      jarEntry = jarIn.getNextJarEntry();
      while (jarEntry != null) {
        if (jarEntry.isDirectory()) {
          jarEntry = jarIn.getNextJarEntry();
          continue;
        }
        name = jarEntry.getName();
        name = name.toUpperCase();

        entryId = getKnowledgeEntryId(name);
        name = stripZoneDir(name);
        if (entryId != null) {
          loadSaveMe[entryId.ordinal()] = true;
        }
        else if (name.equals(OLD_TYPE_OF_FIRE_ENTRY)) {
          loadSaveMe[FIRE_TYPE_LOGIC.ordinal()] = true;
        }
        else if (name.equals(OLD_FIRE_SPREAD_ENTRY)) {
          loadSaveMe[FIRE_SPREAD_LOGIC.ordinal()] = true;
        }
        else if (name.equals(FIRE_TYPE_DATA_ENTRY)) {
          loadSaveMe[FIRE_TYPE_LOGIC.ordinal()] = true;
        }
        else if (name.equals(FIRE_SPREAD_DATA_ENTRY)) {
          loadSaveMe[FIRE_SPREAD_LOGIC.ordinal()] = true;
        }
        else if (name.equals(OLD_REGEN_LOGIC_ENTRY)) {
          loadSaveMe[REGEN_LOGIC_FIRE.ordinal()] = true;
          loadSaveMe[REGEN_LOGIC_SUCC.ordinal()] = true;
        }

        jarEntry = jarIn.getNextJarEntry();
      }
      jarIn.close();
    }
    catch (IOException err) {
      err.printStackTrace();
      throw new SimpplleError("Could not read System Knowledge File");
    }

  }

  public static void saveIndividualPathwayFile(File filename,
                                               HabitatTypeGroup group)
    throws SimpplleError
  {
    String fileExt = getKnowledgeFileExtension(VEGETATION_PATHWAYS);

    try {
      File outfile = Utility.makeSuffixedPathname(filename, "", fileExt);
      JarOutputStream jarOut = new JarOutputStream(new FileOutputStream(outfile),
          new Manifest());
      PrintWriter pout = new PrintWriter(jarOut);

      JarEntry jarEntry = new JarEntry(PATHWAYS_ENTRY + "/" + group.getName());
      jarOut.putNextEntry(jarEntry);
      group.save(pout);
      group.setFilename(outfile);
      group.setIsUserData(true);

      pout.flush();
      pout.close();
      jarOut.close();
    }
    catch (IOException ex) {
      throw new SimpplleError("Problems writing system knowledge file");
    }
  }
/**
 * Saves an individual aquatic pathway file for a particular LtaValleySegmentGroup group
 * @param filename
 * @param group
 * @throws SimpplleError
 */
  public static void saveIndividualAquaticPathwayFile(File filename,
                                                      LtaValleySegmentGroup group)
    throws SimpplleError
  {
    String fileExt = getKnowledgeFileExtension(AQUATIC_PATHWAYS);

    try {
      File outfile = Utility.makeSuffixedPathname(filename, "", fileExt);
      JarOutputStream jarOut = new JarOutputStream(new FileOutputStream(outfile),
          new Manifest());
      PrintWriter pout = new PrintWriter(jarOut);

      JarEntry jarEntry = new JarEntry(PATHWAYS_ENTRY_AQUATIC + "/" + group.getName());
      jarOut.putNextEntry(jarEntry);
      group.save(pout);
      group.setFilename(outfile);
      group.setIsUserData(true);

      pout.flush();
      pout.close();
      jarOut.close();
    }
    catch (IOException ex) {
      throw new SimpplleError("Problems writing system knowledge file");
    }
  }
  /**
   * Saves one particular input file.  
   * First makes all the loadSaveMe booleans false.  Then changes the boolean at the system knowledge kind ordinal to true and 
   * passes to save input file.  
   * @param filename
   * @param entryId
   * @throws SimpplleError
   */
  public static void saveIndividualInputFile(File filename, Kinds entryId)
    throws SimpplleError
  {
    for (int i=0; i<loadSaveMe.length; i++) {
      loadSaveMe[i] = false;
    }
    loadSaveMe[entryId.ordinal()] = true;
    saveInputFile(filename,getKnowledgeFileExtension(entryId),false);
    files[entryId.ordinal()] = filename;
  }
  /**
   * Saves 
   * @param zoneName
   * @throws SimpplleError
   */
  public static void saveZone(File zoneName) throws SimpplleError {
    saveInputFile(zoneName,"simpplle_zone",true);
  }
  public static void saveInputFile(File fileprefix)
    throws SimpplleError
  {
    saveInputFile(fileprefix,SYSKNOW_FILEEXT,false);
  }
/**
 * Saves a particular system knowledge file.  This long method goes through all the types of system knowledge kinds and 
 * @param filename
 * @param fileExt
 * @param doZoneDef
 * @throws SimpplleError
 */
  private static void saveInputFile(File filename, String fileExt, boolean doZoneDef)
    throws SimpplleError
  {
    RegionalZone    zone = Simpplle.getCurrentZone();
    File            outfile;
    JarOutputStream jarOut;
    JarEntry        jarEntry;
    PrintWriter     pout;

    try {
      outfile = Utility.makeSuffixedPathname(filename,"",fileExt);
      jarOut  = new JarOutputStream(new FileOutputStream(outfile),new Manifest());
//      bufWriter = new BufferedWriter(new OutputStreamWriter(jarOut));
      pout    = new PrintWriter(new OutputStreamWriter(jarOut));

      if (doZoneDef) {
        jarEntry = new JarEntry("ZONE/LEGAL-DESCRIPTION.TXT");
        jarOut.putNextEntry(jarEntry);
        zone.writeZoneDefinitionFile(pout);
        for (int i = 0; i < loadSaveMe.length; i++) {
          loadSaveMe[i] = true;
        }
        pout.flush();
      }

      // Do the FMZ file.
      if (loadSaveMe[FMZ.ordinal()]) {
        jarEntry = new JarEntry(FMZ_ENTRY);
        jarOut.putNextEntry(jarEntry);
        Fmz.save(pout);
        pout.flush();
      }

      TreatmentSchedule ts = Area.getTreatmentSchedule();
      if (ts != null && ts.hasApplications()) {
        jarEntry = new JarEntry(TREATMENT_SCHEDULE_ENTRY);
        jarOut.putNextEntry(jarEntry);
        ts.save(pout);
        pout.flush();
      }

      if (loadSaveMe[TREATMENT_LOGIC.ordinal()]) {
        jarEntry = new JarEntry(TREATMENT_LOGIC_ENTRY);
        jarOut.putNextEntry(jarEntry);
        Treatment.saveLogic(pout);
        pout.flush();
      }

      ProcessSchedule ps = Area.getProcessSchedule();
      if (ps != null && ps.getCurrentApplication() != null) {
        jarEntry = new JarEntry(PROCESS_SCHEDULE_ENTRY);
        jarOut.putNextEntry(jarEntry);
        ps.save(pout);
        pout.flush();
      }

      if (loadSaveMe[VEGETATION_PATHWAYS.ordinal()]) {
        String[] groups = HabitatTypeGroup.getLoadedGroupNames();
        HabitatTypeGroup group;
        String name;
        for (int i = 0; (groups != null && i < groups.length); i++) {
          group = HabitatTypeGroup.findInstance(groups[i]);
          outfile = group.getFilename();

          name = PATHWAYS_ENTRY + "/" + group.getName();
          jarEntry = new JarEntry(name);
          jarOut.putNextEntry(jarEntry);
          group.save(pout);
          group.setIsUserData(true);
          pout.flush();
        }
      }

      if (loadSaveMe[AQUATIC_PATHWAYS.ordinal()]) {
        String[] groups = LtaValleySegmentGroup.getLoadedGroupNames();
        LtaValleySegmentGroup group;
        String name;
        for (int i = 0; ((groups != null) && i < groups.length); i++) {
          group = LtaValleySegmentGroup.findInstance(groups[i]);
          outfile = group.getFilename();

          name = PATHWAYS_ENTRY_AQUATIC + "/" + group.getName();
          jarEntry = new JarEntry(name);
          jarOut.putNextEntry(jarEntry);
          group.save(pout);
          group.setIsUserData(true);
          pout.flush();
        }
      }

      if (loadSaveMe[EXTREME_FIRE_DATA.ordinal()]) {
        jarEntry = new JarEntry(EXTREME_FIRE_DATA_ENTRY);
        jarOut.putNextEntry(jarEntry);
        FireEvent.saveExtremeData(pout);
        pout.flush();
      }

      Climate climate = Simpplle.getClimate();
      if (climate != null && loadSaveMe[CLIMATE.ordinal()])
      {
        jarEntry = new JarEntry(CLIMATE_ENTRY);
        jarOut.putNextEntry(jarEntry);
        climate.save(pout);
        pout.flush();
      }

      // Do the Type of Fire Season Data
      if (loadSaveMe[FIRE_SEASON.ordinal()]) {
        jarEntry = new JarEntry(FIRE_SEASON_ENTRY);
        jarOut.putNextEntry(jarEntry);
        FireEvent.saveFireSeasonData(pout);
        pout.flush();
      }

      if (loadSaveMe[SPECIES.ordinal()]) {
        jarEntry = new JarEntry(SPECIES_ENTRY);
        jarOut.putNextEntry(jarEntry);
        SimpplleType.saveData(jarOut,SimpplleType.SPECIES);
      }
      if (loadSaveMe[CONIFER_ENCROACHMENT.ordinal()]) {
        jarEntry = new JarEntry(CONIFER_ENCROACHMENT_ENTRY);
        jarOut.putNextEntry(jarEntry);
        ConiferEncroachmentLogicData.save(jarOut);
      }

      // XStream needs to be setup last as it writes stuff to the stream
      // immediately upon creation.
      XStream xs;
      ObjectOutputStream os=null;

      if (loadSaveMe[FIRE_SUPP_WEATHER_BEYOND_CLASS_A.ordinal()] ||
          loadSaveMe[FIRE_SPREAD_LOGIC.ordinal()] ||
          loadSaveMe[FIRE_TYPE_LOGIC.ordinal()] ||
          loadSaveMe[FIRE_SPOTTING_LOGIC.ordinal()] ||
          loadSaveMe[FIRE_SUPP_EVENT_LOGIC.ordinal()] ||
          loadSaveMe[REGEN_LOGIC_FIRE.ordinal()] ||
          loadSaveMe[REGEN_LOGIC_SUCC.ordinal()] ||
          loadSaveMe[PROCESS_PROB_LOGIC.ordinal()] ||
          loadSaveMe[INVASIVE_SPECIES_LOGIC.ordinal()] ||
          loadSaveMe[INVASIVE_SPECIES_LOGIC_MSU.ordinal()] ||
          loadSaveMe[REGEN_DELAY_LOGIC.ordinal()] ||
          loadSaveMe[GAP_PROCESS_LOGIC.ordinal()] ||
          loadSaveMe[DOCOMPETITION_LOGIC.ordinal()] ||
          loadSaveMe[EVU_SEARCH_LOGIC.ordinal()] ||
          loadSaveMe[PRODUCING_SEED_LOGIC.ordinal()] ||
          loadSaveMe[VEG_UNIT_FIRE_TYPE_LOGIC.ordinal()] ||
          loadSaveMe[FIRE_SUPP_CLASS_A_LOGIC.ordinal()] ||
          loadSaveMe[FIRE_SUPP_BEYOND_CLASS_A_LOGIC.ordinal()] ||
          loadSaveMe[FIRE_SUPP_PRODUCTION_RATE_LOGIC.ordinal()] ||
          loadSaveMe[FIRE_SUPP_SPREAD_RATE_LOGIC.ordinal()] ||
          loadSaveMe[FIRE_SUPP_WEATHER_CLASS_A_LOGIC.ordinal()] ||
          loadSaveMe[TRACKING_SPECIES_REPORT.ordinal()]) {
        DomDriver d = new DomDriver();
        HierarchicalStreamWriter hw = d.createWriter(pout);
        xs = new XStream(d);
        SystemKnowledge.setupXStreamAliases(xs);
        String rootNodeName = "object-stream";
        os = xs.createObjectOutputStream(hw,rootNodeName);
        boolean headerWritten = true;

        if (loadSaveMe[FIRE_SUPP_WEATHER_BEYOND_CLASS_A.ordinal()]) {
          if (!headerWritten) { hw.startNode(rootNodeName); }
          jarEntry = new JarEntry(FIRE_SUPP_WEATHER_BEYOND_CLASS_A_ENTRY);
          jarOut.putNextEntry(jarEntry);
          FireSuppWeatherData.save(os);
          os.flush();
          hw.endNode();
          headerWritten = false;
        }
        if (loadSaveMe[FIRE_SPREAD_LOGIC.ordinal()]) {
          if (!headerWritten) { hw.startNode(rootNodeName); }
          jarEntry = new JarEntry(FIRE_SPREAD_LOGIC_ENTRY);
          jarOut.putNextEntry(jarEntry);
          FireEventLogic.save(FireEventLogic.SPREAD_STR,os);
          os.flush();
          hw.endNode();
          headerWritten = false;
        }
        if (loadSaveMe[FIRE_TYPE_LOGIC.ordinal()]) {
          if (!headerWritten) { hw.startNode(rootNodeName); }
          jarEntry = new JarEntry(FIRE_TYPE_LOGIC_ENTRY);
          jarOut.putNextEntry(jarEntry);
          FireEventLogic.save(FireEventLogic.TYPE_STR,os);
          os.flush();
          hw.endNode();
          headerWritten = false;
        }
        if (loadSaveMe[FIRE_SPOTTING_LOGIC.ordinal()]) {
          if (!headerWritten) { hw.startNode(rootNodeName); }
          jarEntry = new JarEntry(FIRE_SPOTTING_LOGIC_ENTRY);
          jarOut.putNextEntry(jarEntry);
          FireEventLogic.save(FireEventLogic.FIRE_SPOTTING_STR,os);
          os.flush();
          hw.endNode();
          headerWritten = false;
        }
        if (loadSaveMe[REGEN_LOGIC_FIRE.ordinal()] &&
            RegenerationLogic.isDataPresent(RegenerationLogic.FIRE)) {
          if (!headerWritten) { hw.startNode(rootNodeName); }
          jarEntry = new JarEntry(REGEN_LOGIC_FIRE_ENTRY);
          jarOut.putNextEntry(jarEntry);
          RegenerationLogic.saveFire(os);
          os.flush();
          hw.endNode();
          headerWritten = false;
        }
        if (loadSaveMe[REGEN_LOGIC_SUCC.ordinal()] &&
            RegenerationLogic.isDataPresent(RegenerationLogic.SUCCESSION)) {
          if (!headerWritten) { hw.startNode(rootNodeName); }
          jarEntry = new JarEntry(REGEN_LOGIC_SUCC_ENTRY);
          jarOut.putNextEntry(jarEntry);
          RegenerationLogic.saveSuccession(os);
          os.flush();
          hw.endNode();
          headerWritten = false;
        }
        if (loadSaveMe[PROCESS_PROB_LOGIC.ordinal()]) {
          if (!headerWritten) { hw.startNode(rootNodeName); }
          jarEntry = new JarEntry(PROCESS_PROB_LOGIC_ENTRY);
          jarOut.putNextEntry(jarEntry);
          ProcessProbLogic.getInstance().save(os);
          os.flush();
          hw.endNode();
          headerWritten = false;
        }
        if (loadSaveMe[INVASIVE_SPECIES_LOGIC.ordinal()]) {
          if (!headerWritten) { hw.startNode(rootNodeName); }
          jarEntry = new JarEntry(INVASIVE_SPECIES_LOGIC_ENTRY);
          jarOut.putNextEntry(jarEntry);
          InvasiveSpeciesLogic.getInstance().save(os);
          os.flush();
          hw.endNode();
          headerWritten = false;
        }
        if (loadSaveMe[INVASIVE_SPECIES_LOGIC_MSU.ordinal()]) {
          if (!headerWritten) { hw.startNode(rootNodeName); }
          jarEntry = new JarEntry(INVASIVE_SPECIES_LOGIC_MSU_ENTRY);
          jarOut.putNextEntry(jarEntry);
          InvasiveSpeciesLogicMSU.getInstance().save(os);
          os.flush();
          hw.endNode();
          headerWritten = false;
        }
        if (loadSaveMe[REGEN_DELAY_LOGIC.ordinal()]) {
          if (!headerWritten) { hw.startNode(rootNodeName); }
          jarEntry = new JarEntry(REGEN_DELAY_LOGIC_ENTRY);
          jarOut.putNextEntry(jarEntry);
          RegenerationDelayLogic.getInstance().save(os);
          os.flush();
          hw.endNode();
          headerWritten = false;
        }
        if (loadSaveMe[GAP_PROCESS_LOGIC.ordinal()]) {
          if (!headerWritten) { hw.startNode(rootNodeName); }
          jarEntry = new JarEntry(GAP_PROCESS_LOGIC_ENTRY);
          jarOut.putNextEntry(jarEntry);
          GapProcessLogic.getInstance().save(os);
          os.flush();
          hw.endNode();
          headerWritten = false;
        }
        if (loadSaveMe[DOCOMPETITION_LOGIC.ordinal()]) {
          if (!headerWritten) { hw.startNode(rootNodeName); }
          jarEntry = new JarEntry(DOCOMPETITION_LOGIC_ENTRY);
          jarOut.putNextEntry(jarEntry);
          DoCompetitionLogic.getInstance().save(os);
          os.flush();
          hw.endNode();
          headerWritten = false;
        }
        if (loadSaveMe[EVU_SEARCH_LOGIC.ordinal()]) {
          if (!headerWritten) { hw.startNode(rootNodeName); }
          jarEntry = new JarEntry(EVU_SEARCH_LOGIC_ENTRY);
          jarOut.putNextEntry(jarEntry);
          EvuSearchLogic.getInstance().save(os);
          os.flush();
          hw.endNode();
          headerWritten = false;
        }
        if (loadSaveMe[PRODUCING_SEED_LOGIC.ordinal()]) {
          if (!headerWritten) { hw.startNode(rootNodeName); }
          jarEntry = new JarEntry(PRODUCING_SEED_LOGIC_ENTRY);
          jarOut.putNextEntry(jarEntry);
          ProducingSeedLogic.getInstance().save(os);
          os.flush();
          hw.endNode();
          headerWritten = false;
        }
        if (loadSaveMe[VEG_UNIT_FIRE_TYPE_LOGIC.ordinal()]) {
          if (!headerWritten) { hw.startNode(rootNodeName); }
          jarEntry = new JarEntry(VEG_UNIT_FIRE_TYPE_LOGIC_ENTRY);
          jarOut.putNextEntry(jarEntry);
          VegUnitFireTypeLogic.getInstance().save(os);
          os.flush();
          hw.endNode();
          headerWritten = false;
        }
        if (loadSaveMe[FIRE_SUPP_CLASS_A_LOGIC.ordinal()]) {
          if (!headerWritten) { hw.startNode(rootNodeName); }
          jarEntry = new JarEntry(FIRE_SUPP_CLASS_A_LOGIC_ENTRY);
          jarOut.putNextEntry(jarEntry);
          FireSuppClassALogic.getInstance().save(os);
          os.flush();
          hw.endNode();
          headerWritten = false;
        }
        if (loadSaveMe[FIRE_SUPP_BEYOND_CLASS_A_LOGIC.ordinal()]) {
          if (!headerWritten) { hw.startNode(rootNodeName); }
          jarEntry = new JarEntry(FIRE_SUPP_BEYOND_CLASS_A_LOGIC_ENTRY);
          jarOut.putNextEntry(jarEntry);
          FireSuppBeyondClassALogic.getInstance().save(os);
          os.flush();
          hw.endNode();
          headerWritten = false;
        }
        if (loadSaveMe[FIRE_SUPP_PRODUCTION_RATE_LOGIC.ordinal()]) {
          if (!headerWritten) { hw.startNode(rootNodeName); }
          jarEntry = new JarEntry(FIRE_SUPP_PRODUCTION_RATE_LOGIC_ENTRY);
          jarOut.putNextEntry(jarEntry);
          FireSuppProductionRateLogic.getInstance().save(os);
          os.flush();
          hw.endNode();
          headerWritten = false;
        }
        if (loadSaveMe[FIRE_SUPP_SPREAD_RATE_LOGIC.ordinal()]) {
          if (!headerWritten) { hw.startNode(rootNodeName); }
          jarEntry = new JarEntry(FIRE_SUPP_SPREAD_RATE_LOGIC_ENTRY);
          jarOut.putNextEntry(jarEntry);
          FireSuppSpreadRateLogic.getInstance().save(os);
          os.flush();
          hw.endNode();
          headerWritten = false;
        }
        if (loadSaveMe[FIRE_SUPP_WEATHER_CLASS_A_LOGIC.ordinal()]) {
          if (!headerWritten) { hw.startNode(rootNodeName); }
          jarEntry = new JarEntry(FIRE_SUPP_WEATHER_CLASS_A_LOGIC_ENTRY);
          jarOut.putNextEntry(jarEntry);
          FireSuppWeatherClassALogic.getInstance().save(os);
          os.flush();
          hw.endNode();
          headerWritten = false;
        }
        if (loadSaveMe[TRACKING_SPECIES_REPORT.ordinal()]) {
          if (!headerWritten) { hw.startNode(rootNodeName); }
          jarEntry = new JarEntry(TRACKING_SPECIES_REPORT_ENTRY);
          jarOut.putNextEntry(jarEntry);
          TrackingSpeciesReportData.getInstance().save(os);
          os.flush();
          hw.endNode();
          headerWritten = false;
        }
        if (loadSaveMe[FIRE_SUPP_EVENT_LOGIC.ordinal()]) {
          if (!headerWritten) { hw.startNode(rootNodeName); }
          jarEntry = new JarEntry(FIRE_SUPP_EVENT_LOGIC_ENTRY);
          jarOut.putNextEntry(jarEntry);
          FireSuppEventLogic.getInstance().save(os);
          os.flush();
          hw.endNode();
          headerWritten = false;
        }
        hw.close();
      }

      pout.flush();
      pout.close();
      jarOut.close();

      Kinds[] kinds = Kinds.values();
      for (int i=0; i<loadSaveMe.length; i++) {
        setHasChanged(kinds[i],false);
      }

    }
    catch (IOException err) {
      throw new SimpplleError("Problems writing system knowledge file");
    }

  }

  public static void loadAllDefaults() throws SimpplleError {
    RegionalZone zone = Simpplle.getCurrentZone();

    zone.readZoneSystemKnowledgeFile();
    FireEvent.resetExtremeData();
  }
/**
 * Gets the system knowledge file extension based on system knowledge kind.  
 * @param kind system knowledge kind 
 * @return file extension.  example for System Knowledge TREATMENT_SCHEDULE will return "sk_treatsched";
 */
  public static String getKnowledgeFileExtension(Kinds kind) {
    // old Fire Spread files .firespread
    // old Type of Fire files .firetype
    // old Regeneration Logic .sk_regenlogic

    switch (kind) {
      case FMZ:                              return ("sk_fmz");
      case TREATMENT_SCHEDULE:               return ("sk_treatsched");
      case TREATMENT_LOGIC:                  return ("sk_treatlogic");
      case PROCESS_SCHEDULE:                 return ("sk_processsched");
//      case INSECT_DISEASE_PROB:              return ("sk_probability");
      case PROCESS_PROB_LOGIC:               return ("sk_processproblogic");
      case FIRE_SUPP_BEYOND_CLASS_A:         return ("sk_fire_suppbeyonda");
      case FIRE_SUPP_WEATHER_BEYOND_CLASS_A: return ("sk_fire_suppweather");
      case CLIMATE:                          return ("sk_climate");
      case REGEN_LOGIC_FIRE:                 return ("sk_regenlogicfire");
      case REGEN_LOGIC_SUCC:                 return ("sk_regenlogicsucc");
      case FIRE_SEASON:                      return ("sk_fireseason");
      case FIRE_TYPE_LOGIC:                  return ("sk_firetype");
      case FIRE_SPREAD_LOGIC:                return ("sk_firespread");
      case FIRE_SPOTTING_LOGIC:              return ("sk_firespotting");
      case FIRE_SUPP_EVENT_LOGIC:            return ("sk_firesuppevent");
      case CONIFER_ENCROACHMENT:             return ("sk_conifer");
      case SPECIES:                          return ("sk_species");
      case FIRESUPP_PRODUCTION_RATE:         return ("sk_firesuppprodrate");
      case FIRESUPP_SPREAD_RATE:             return ("sk_firesuppspreadrate");
      case VEGETATION_PATHWAYS:              return ("sk_pathway");
      case AQUATIC_PATHWAYS:                 return ("sk_aquapathway");
      case INVASIVE_SPECIES_LOGIC:           return ("sk_invasivespecieslogic");
      case INVASIVE_SPECIES_LOGIC_MSU:       return ("sk_invasivespecieslogicmsu");
      case REGEN_DELAY_LOGIC:                return ("sk_regendelaylogic");
      case DOCOMPETITION_LOGIC:              return ("sk_competitionlogic");
      case GAP_PROCESS_LOGIC:                return ("sk_gapprocesslogic");
      case EVU_SEARCH_LOGIC:                 return ("sk_evusearchlogic");
      case PRODUCING_SEED_LOGIC:             return ("sk_producingseedlogic");
      case VEG_UNIT_FIRE_TYPE_LOGIC:         return ("sk_vegunitfiretypelogic");
      case FIRE_SUPP_CLASS_A_LOGIC:          return ("sk_firesuppclassalogic");
      case FIRE_SUPP_BEYOND_CLASS_A_LOGIC:   return ("sk_firesuppbeyondclassalogic");
      case FIRE_SUPP_PRODUCTION_RATE_LOGIC:  return ("sk_firesuppproductionratelogic");
      case FIRE_SUPP_SPREAD_RATE_LOGIC:      return ("sk_firesuppspreadratelogic");
      case FIRE_SUPP_WEATHER_CLASS_A_LOGIC:  return ("sk_firesuppweatherclassalogic");
      case TRACKING_SPECIES_REPORT:          return ("sk_trackingspeciesreport");
      default:                               return ("");
      // Extreme fire doesn't have its own file.
      // Wildlife doesn't have its own file.
    }
  }
  /**
   * Gets the string descrpition of the file for a particular system knowledge kind.  
   * Example for system knowledge TREATMENT_SCHEDULE will return "Treatment Schedule"
   * @param kind System Knowledge kind 
   * @return a string with the System Knowledge file formatted for readability by user.  
   */
  public static String getKnowledgeFileDescription(Kinds kind) {
    // old Fire Spread files .firespread
    // old Type of Fire files .firetype

    switch (kind) {
      case FMZ:                              return ("FMZ Data");
      case TREATMENT_SCHEDULE:               return ("Treatment Schedule");
      case TREATMENT_LOGIC:                  return ("Treatment Logic");
      case PROCESS_SCHEDULE:                 return ("Process Schedule");
//      case INSECT_DISEASE_PROB:              return ("insect/Disease Probability");
      case PROCESS_PROB_LOGIC:               return ("Process Probability Logic");
      case FIRE_SUPP_BEYOND_CLASS_A:         return ("Fire Suppression");
      case FIRE_SUPP_WEATHER_BEYOND_CLASS_A: return ("Fire Suppression");
      case CLIMATE:                          return ("Climate");
      case REGEN_LOGIC_FIRE:                 return ("Regeneration Logic Fire");
      case REGEN_LOGIC_SUCC:                 return ("Regeneration Logic Succession");
      case FIRE_SEASON:                      return ("Fire Season");
      case FIRE_TYPE_LOGIC:                  return ("Type of Fire Logic");
      case FIRE_SPREAD_LOGIC:                return ("Fire Spread Logic");
      case FIRE_SPOTTING_LOGIC:              return ("Fire Spotting Logic");
      case FIRE_SUPP_EVENT_LOGIC:            return ("Fire Suppression Event Logic");
      case CONIFER_ENCROACHMENT:             return ("Conifer Encroachment");
      case SPECIES:                          return ("Species");
      case FIRESUPP_PRODUCTION_RATE:         return ("Fire Supp Prod Rate");
      case FIRESUPP_SPREAD_RATE:             return ("Fire Supp Spread Rate");
      case VEGETATION_PATHWAYS:              return ("Veg Pathway");
      case AQUATIC_PATHWAYS:                 return ("Aquatic Pathway");
      case INVASIVE_SPECIES_LOGIC:           return ("Invasive Species Logic");
      case INVASIVE_SPECIES_LOGIC_MSU:       return ("Invasive Species Logic MSU");
      case REGEN_DELAY_LOGIC:                return ("Regeneration Delay Logic");
      case DOCOMPETITION_LOGIC:              return ("Lifeform Competition Logic");
      case GAP_PROCESS_LOGIC:                return ("Gap Process Logic");
      case EVU_SEARCH_LOGIC:                 return ("Evu Search Logic");
      case PRODUCING_SEED_LOGIC:             return ("Producing Seed Logic");
      case VEG_UNIT_FIRE_TYPE_LOGIC:         return ("Veg Unit Fire Type Logic");
      case FIRE_SUPP_CLASS_A_LOGIC:          return ("Fire Supp Class A Logic");
      case FIRE_SUPP_BEYOND_CLASS_A_LOGIC:   return ("Fire Supp Beyond Class A Logic");
      case FIRE_SUPP_PRODUCTION_RATE_LOGIC:  return ("Fire Supp Production Rate Logic");
      case FIRE_SUPP_SPREAD_RATE_LOGIC:      return ("Fire Supp Spread Rate Logic");
      case FIRE_SUPP_WEATHER_CLASS_A_LOGIC:  return ("Fire Supp Weather Class A Logic");
      case TRACKING_SPECIES_REPORT:          return ("Tracking Species Report");
      default:                               return ("");
      // Extreme fire doesn't have its own file.
      // Wildlife doesn't have its own file.
    }
  }
/**
 * Gets the system knowledge 
 * @param kind
 * @return the System Knowledge file kind as a 
 */
  public static String getKnowledgeFileTitle(Kinds kind) {
    // old Fire Spread files .firespread
    // old Type of Fire files .firetype

    switch (kind) {
      case FMZ:                              return ("Fire Management Zone Data");
      case TREATMENT_SCHEDULE:               return ("Treatment Schedule");
      case TREATMENT_LOGIC:                  return ("Treatment Logic");
      case PROCESS_SCHEDULE:                 return ("Process Schedule");
//      case INSECT_DISEASE_PROB:              return ("insect/Disease Probability");
      case FIRE_SUPP_BEYOND_CLASS_A:         return ("Fire Suppression (Beyond Class A)");
      case FIRE_SUPP_WEATHER_BEYOND_CLASS_A: return ("Fire Suppression (Beyond Class A Weather Event)");
      case CLIMATE:                          return ("Climate");
      case REGEN_LOGIC_FIRE:                 return ("Regeneration Logic Fire");
      case REGEN_LOGIC_SUCC:                 return ("Regeneration Logic Succession");
      case FIRE_SEASON:                      return ("Fire Season");
      case FIRE_TYPE_LOGIC:                  return ("Type of Fire Logic");
      case FIRE_SPREAD_LOGIC:                return ("Fire Spread Logic");
      case FIRE_SPOTTING_LOGIC:              return ("Fire Spotting Logic");
      case FIRE_SUPP_EVENT_LOGIC:            return ("Fire Suppression Event Logic");
      case CONIFER_ENCROACHMENT:             return ("Conifer Encroachment");
      case SPECIES:                          return ("Species Knowledge");
      case FIRESUPP_PRODUCTION_RATE:         return ("Fire Suppression Production Rate");
      case FIRESUPP_SPREAD_RATE:             return ("Fire Suppression Spread Rate");
      case VEGETATION_PATHWAYS:              return ("Vegetation Pathway");
      case AQUATIC_PATHWAYS:                 return ("Aquatic Pathway");
      case PROCESS_PROB_LOGIC:               return ("Process Probability Logic");
      case INVASIVE_SPECIES_LOGIC:           return ("Invasive Species Logic");
      case INVASIVE_SPECIES_LOGIC_MSU:       return ("Invasive Species Logic MSU");
      case REGEN_DELAY_LOGIC:                return ("Regeneration Delay Logic");
      case DOCOMPETITION_LOGIC:              return ("Lifeform Competition Logic");
      case GAP_PROCESS_LOGIC:                return ("Gap Process Logic");
      case EVU_SEARCH_LOGIC:                 return ("Evu Search Logic");
      case PRODUCING_SEED_LOGIC:             return ("Producing Seed Logic");
      case VEG_UNIT_FIRE_TYPE_LOGIC:         return ("Veg Unit Fire Type Logic");
      case FIRE_SUPP_CLASS_A_LOGIC:          return ("Fire Supp Class A Logic");
      case FIRE_SUPP_BEYOND_CLASS_A_LOGIC:   return ("Fire Supp Beyond Class A Logic");
      case FIRE_SUPP_PRODUCTION_RATE_LOGIC:  return ("Fire Supp Production Rate Logic");
      case FIRE_SUPP_SPREAD_RATE_LOGIC:      return ("Fire Supp Spread Rate Logic");
      case FIRE_SUPP_WEATHER_CLASS_A_LOGIC:  return ("Fire Supp Weather Class A Logic");
      case TRACKING_SPECIES_REPORT:          return ("Tracking Species Report");
      default:                               return ("");
      // Extreme fire doesn't have its own file.
      // Wildlife doesn't have its own file.
    }
  }
  // ********************************
  // *** Knowledge Source Methods ***
  // ********************************
  public static String getSource(Kinds kind) {
    if (kind == WILDLIFE) {
      return "Documented in draft GTR, Carattia, Chew and Samson";
    }

    int zoneId = Simpplle.getCurrentZone().getId();

    switch(zoneId) {
      case ValidZones.WESTSIDE_REGION_ONE:      return getWestsideRegionOneSource(kind);
      case ValidZones.EASTSIDE_REGION_ONE:      return getEastsideRegionOneSource(kind);
      case ValidZones.TETON:                    return getTetonSource(kind);
      case ValidZones.NORTHERN_CENTRAL_ROCKIES: return getNorthernCentralRockiesSource(kind);
      case ValidZones.COLORADO_PLATEAU:         return getColoradoPlateauSource(kind);
      default:  return "No Knowledge Source Data Available at this time.";
    }
  }
/**
 * Gets the historical source information for a particular system knowledge kind in Westside Region 1.  
 * These are string descriptions which will be output in GUI.  
 * Kinds with historical source are: FMZ, LP_MPB, PP_MPB, WSBW, TREATMENT_LOGIC, REGEN_LOGIC_FIRE, REGEN_LOGIC_SUCC  
 * @param kind system knowledge kind.  
 * @return string representing the system knowledge kinds historical source
 */
  private static String getWestsideRegionOneSource(Kinds kind) {
    StringBuffer strBuf = new StringBuffer();
    strBuf.append(
      "Default logic and probability values developed through a series"  +
      " of Regional Workshops with silviculturists and ecologists" +
      " May through Dec 1998.\n\n");

    switch (kind) {
      case FMZ:
        strBuf.append("Additional Sources:\n\n");
        strBuf.append(
          "default file is based on PCHA reports for all WestSide Forests" +
          " generated by the Regional Office for the period 1985 -1994\n");
        break;
      case LP_MPB:
        strBuf.append("Additional Sources:\n\n");
        strBuf.append(
          "Amman, G.D., M.D. McGregor, D.B. Cahill, and W.H. Klein. 1977." +
          "  Guidelines for reducing losses of lodgepole pine to the" +
          " mountain pine beetle in unmanaged stands in the Rocky Mountains." +
          "  USDA Forest Service, Intermountain Forest and Range" +
          " Experiment Station.  Ogden, UT. Gen. Tech. Rep. INT-36. 19 p\n");
        break;
      case PP_MPB:
        strBuf.append("Additional Sources:\n\n");
        strBuf.append(
          "Stevens, R.E., W.F. McCambridge, and C.B. Edminster. 1980." +
          " Risk rating guide for mountain pine beetle in Black Hills" +
          " ponderosa pine.  USDA Forest Service, Rocky Mountain  Forest" +
          " and Range Experiment Station. Res. Note RM-385.\n");
        break;
      case WSBW:
        strBuf.append("Additional Sources:\n\n");
        strBuf.append(
          "Carlson, C.E. and N.W. Wulf. 1989.  Silvicultural strategies" +
          " to reduce stand and forest susceptibility to the western spruce" +
          " budworm. USDA Forest Service, Ag. Handb. No. 676.\n");
        break;
      case TREATMENT_LOGIC:
        strBuf.append("Additional Sources:\n\n");
        strBuf.append(
          "Testing by Don Helmbrecht, UM grad student, on Bitterroot Face," +
          " April through June 2002, identified problems in logic for" +
          " repeated treatments.  Significant changes made in identifying" +
          " the next state after thinning and ecosystem management burning" +
          " treatments.\n");
        break;
      case REGEN_LOGIC_FIRE:
      case REGEN_LOGIC_SUCC:
        strBuf.append("Additional Sources:\n\n");
        strBuf.append(
          "Testing on Lolo Forest, Ninemile District, Nov 2002. Change made" +
          " to give priority to presence of larch and ponderosa pine seed" +
          " sources over other species.\n");
        break;
    }
    return strBuf.toString();
  }
  /**
   * Gets the historical source information for a particular system knowledge kind in Eastside Region 1.  
   * These are string descriptions which will be output in GUI.  
   * Kinds with historical source are: FMZ, LP_MPB, PP_MPB, WSBW  
   * @param kind system knowledge kind.  
   * @return string representing the system knowledge kinds historical source
   */
  private static String getEastsideRegionOneSource(Kinds kind) {
    StringBuffer strBuf = new StringBuffer();

    strBuf.append(
      "Default logic and probability values developed through a series of" +
      " workshops with silviculturists, ecologists, planners, and resource" +
      " specialists for the \"Analysis of the Management Situation --" +
      " Eastside Planning Zone\"  Oct 1999 through March 2000.  Logic and" +
      " values were tested on a sample landscape for each Forest in the" +
      " Planning Zone.\n\n");

    switch (kind) {
      // " +"
      case FMZ:
        strBuf.append("Additional Sources:\n\n");
        strBuf.append(
          "Default file is based on PCHA reports for all Eastside Forests\n" +
          "Generated by the Regional Office for the period 1985-1994\n");
        break;
      case LP_MPB:
        strBuf.append("Additional Sources:\n\n");
        strBuf.append(
          "Amman, G.D., M.D. McGregor, D.B. Cahill, and W.H. Klein. 1977." +
          "  Guidelines for reducing losses of lodgepole pine to the" +
          " mountain pine beetle in unmanaged stands in the Rocky Mountains." +
          "  USDA Forest Service, Intermountain Forest and Range" +
          "  Experiment Station.  Ogden, UT. Gen. Tech. Rep. INT-36. 19 p\n");
        break;
      case PP_MPB:
        strBuf.append("Additional Sources:\n\n");
        strBuf.append(
          "Stevens, R.E., W.F. McCambridge, and C.B. Edminster. 1980. Risk" +
          " rating guide for mountain pine beetle in Black Hills ponderosa" +
          " pine.  USDA Forest Service, Rocky Mountain  Forest and Range" +
          " Experiment Station. Res. Note RM-385.\n");
        break;
      case WSBW:
        strBuf.append("Additional Sources:\n\n");
        strBuf.append(
          "Carlson, C.E. and N.W. Wulf. 1989.  Silvicultural strategies to" +
          " reduce stand and forest susceptibility to the western spruce" +
          " budworm. USDA Forest Service, Ag. Handb. No. 676.\n");
        break;
    }
    return strBuf.toString();
  }
  /**
   * Gets the historical source information for a particular system knowledge kind in Colorado Plateau  
   * These are string descriptions which will be output in GUI.  
   * @param kind system knowledge kind.  
   * @return string representing the system knowledge kinds historical source
   */
  private static String getColoradoPlateauSource(Kinds kind) {
    StringBuffer strBuf = new StringBuffer();

    strBuf.append(
        "System knowledge for this geographic area was developed in " +
        "connection with USGS's FRAME project for Mesa Verde National Park.  " +
        "This effort involves scientists and managers from USGS, BLM, " +
        "Mesa Verde National Park, Colorado State University, " +
        "Northern Arizona University, and Prescott College in workshops " +
        "from June 2004 through November 2006.\n\n" +
        "Four zones were created by using elevation breaks with a " +
        "map of past fire occurrences.");
    return strBuf.toString();
  }
/**
 * Gets the history of system knowledge information for the Teton regional zone.  
 * @param kind This will be either FMZ, LP_MPB, PP_MPB, or WSBW.  Used to print out info on the history and sources for information pertaining to these
 * system processes. 
 * @return a string.  this will be printed in GUI.
 */
  private static String getTetonSource(Kinds kind) {
    StringBuffer strBuf = new StringBuffer();

    strBuf.append(
        "This system knowledge is intended to be modified for the " +
        "Teton Geographic Area in Wyoming. " +
        "This is only a starting point for development of this geographic area.\n" +
        "This initial default knowledge is from modification of the " +
        "Eastside of Region One Geographic Area by Ecosystem Research Group " +
        "of Missoula for work on the Shoshone National Forest\n\n");

    strBuf.append(
      "Default logic and probability values developed through a series of" +
      " workshops with silviculturists, ecologists, planners, and resource" +
      " specialists for the \"Analysis of the Management Situation --" +
      " Eastside Planning Zone\"  Oct 1999 through March 2000.  Logic and" +
      " values were tested on a sample landscape for each Forest in the" +
      " Planning Zone.\n\n");

    switch (kind) {
      // " +"
      case FMZ:
        strBuf.append("Additional Sources:\n\n");
        strBuf.append(
          "Default file is based on PCHA reports for all Eastside Forests\n" +
          "Generated by the Regional Office for the period 1985-1994\n");
        break;
      case LP_MPB:
        strBuf.append("Additional Sources:\n\n");
        strBuf.append(
          "Amman, G.D., M.D. McGregor, D.B. Cahill, and W.H. Klein. 1977." +
          "  Guidelines for reducing losses of lodgepole pine to the" +
          " mountain pine beetle in unmanaged stands in the Rocky Mountains." +
          "  USDA Forest Service, Intermountain Forest and Range" +
          "  Experiment Station.  Ogden, UT. Gen. Tech. Rep. INT-36. 19 p\n");
        break;
      case PP_MPB:
        strBuf.append("Additional Sources:\n\n");
        strBuf.append(
          "Stevens, R.E., W.F. McCambridge, and C.B. Edminster. 1980. Risk" +
          " rating guide for mountain pine beetle in Black Hills ponderosa" +
          " pine.  USDA Forest Service, Rocky Mountain  Forest and Range" +
          " Experiment Station. Res. Note RM-385.\n");
        break;
      case WSBW:
        strBuf.append("Additional Sources:\n\n");
        strBuf.append(
          "Carlson, C.E. and N.W. Wulf. 1989.  Silvicultural strategies to" +
          " reduce stand and forest susceptibility to the western spruce" +
          " budworm. USDA Forest Service, Ag. Handb. No. 676.\n");
        break;
    }
    return strBuf.toString();
  }
  /**
   * Gets the historical source information for a particular system knowledge kind in Westside Region 1.  
   * These are string descriptions which will be output in GUI.  
   * Kinds with historical source are: FMZ, LP_MPB, PP_MPB, WSBW 
   * @param kind system knowledge kind.  
   * @return string representing the system knowledge kinds historical source
   */
  private static String getNorthernCentralRockiesSource(Kinds kind) {
    StringBuffer strBuf = new StringBuffer();

    strBuf.append(
        "This system knowledge is intended to be modified for the " +
        "Northern Central Rockies Geographic Area in Wyoming. " +
        "This is only a starting point for development of this geographic area.\n" +
        "This initial default knowledge is from modification of the " +
        "Eastside of Region One Geographic Area by Ecosystem Research Group " +
        "of Missoula for work on the Shoshone National Forest\n\n");

    strBuf.append(
      "Default logic and probability values developed through a series of" +
      " workshops with silviculturists, ecologists, planners, and resource" +
      " specialists for the \"Analysis of the Management Situation --" +
      " Eastside Planning Zone\"  Oct 1999 through March 2000.  Logic and" +
      " values were tested on a sample landscape for each Forest in the" +
      " Planning Zone.\n\n");

    switch (kind) {
      // " +"
      case FMZ:
        strBuf.append("Additional Sources:\n\n");
        strBuf.append(
          "Default file is based on PCHA reports for all Eastside Forests\n" +
          "Generated by the Regional Office for the period 1985-1994\n");
        break;
      case LP_MPB:
        strBuf.append("Additional Sources:\n\n");
        strBuf.append(
          "Amman, G.D., M.D. McGregor, D.B. Cahill, and W.H. Klein. 1977." +
          "  Guidelines for reducing losses of lodgepole pine to the" +
          " mountain pine beetle in unmanaged stands in the Rocky Mountains." +
          "  USDA Forest Service, Intermountain Forest and Range" +
          "  Experiment Station.  Ogden, UT. Gen. Tech. Rep. INT-36. 19 p\n");
        break;
      case PP_MPB:
        strBuf.append("Additional Sources:\n\n");
        strBuf.append(
          "Stevens, R.E., W.F. McCambridge, and C.B. Edminster. 1980. Risk" +
          " rating guide for mountain pine beetle in Black Hills ponderosa" +
          " pine.  USDA Forest Service, Rocky Mountain  Forest and Range" +
          " Experiment Station. Res. Note RM-385.\n");
        break;
      case WSBW:
        strBuf.append("Additional Sources:\n\n");
        strBuf.append(
          "Carlson, C.E. and N.W. Wulf. 1989.  Silvicultural strategies to" +
          " reduce stand and forest susceptibility to the western spruce" +
          " budworm. USDA Forest Service, Ag. Handb. No. 676.\n");
        break;
    }
    return strBuf.toString();
  }

  private static String[]  knowledgeSource = new String[NUMID];
  public static String KNOWLEDGE_SOURCE_KEYWORD="KNOWLEDGE-SOURCE";

  
  /**
   * Gets the system knowledge source by looking up the system knowledge kind by its ordinal into enumeration. 
   * @param kind
   * @return
   */
  public static String getKnowledgeSource(Kinds kind) {
    return knowledgeSource[kind.ordinal()];
  }
  /**
   * Sets the system knowledge source to parameter at index of a system knowledge kind. 
   * @param kind
   * @param source
   */
  public static void setKnowledgeSource(Kinds kind, String source)  {
    knowledgeSource[kind.ordinal()] = source;
  }
/**
 * Writes the knowledge source, will Knowledge Source, dummy, value, end knowledge source.  
 * @param fout
 * @param value
 */
  public static void writeKnowledgeSource(PrintWriter fout, String value) {
    if (value != null && value.length() > 0) {
      fout.print(KNOWLEDGE_SOURCE_KEYWORD);
      fout.println(" dummy");
      fout.println(value);
      fout.println("END-KNOWLEDGE-SOURCE");
    }
  }
  public static void writeKnowledgeSource(PrintWriter fout, Kinds kind) {
    writeKnowledgeSource(fout,knowledgeSource[kind.ordinal()]);
  }

  /**
   * Reads the KnowledgeSource string from a system knowledge file and returns
   * the string read.  The returned string is only used in the case of
   * HabitatTypeGroup's, for the rest of the Knowledge Source for the various
   * data files is stored in this class.
   *
   * @param fin BufferedReader
   * @param kind int The id of the data file we are reading, FIRE_SPREAD_DATA, etc.
   * @return String
   * @throws IOException
   */
  public static String readKnowledgeSource(BufferedReader fin, Kinds kind) throws IOException {
    StringBuffer strBuf = new StringBuffer("");
    String line = fin.readLine();
    String nl = System.getProperty("line.separator");
    while (line != null && line.equals("END-KNOWLEDGE-SOURCE") == false) {
      strBuf.append(line);
      strBuf.append(nl);
      line = fin.readLine();
    }
    if (kind != VEGETATION_PATHWAYS && kind != AQUATIC_PATHWAYS) {
      setKnowledgeSource(kind,strBuf.toString());
    }
    else {
      return strBuf.toString();
    }
    return getKnowledgeSource(kind);
  }
/**
 * Sets the aliases used in xstream xml encoding. 
 * @param xs
 */
  public static void setupXStreamAliases(XStream xs) {
    xs.alias("FireResistance",FireResistance.class);
    xs.alias("FireTypeLogicData",FireTypeLogicData.class);
    xs.alias("HabitatTypeGroupType",HabitatTypeGroupType.class);
    xs.alias("Species",Species.class);
    xs.alias("SizeClass",SizeClass.class);
    xs.alias("SizeClassStructure",SizeClass.Structure.class);
    xs.alias("ProcessType",ProcessType.class);
    xs.alias("TreatmentType",TreatmentType.class);
    xs.alias("Density",Density.class);
    xs.alias("FireSpreadLogicData",FireSpreadLogicData.class);

    xs.alias("SuccessionRegenerationData",SuccessionRegenerationData.class);
    xs.alias("RegenerationSuccessionInfo",RegenerationSuccessionInfo.class);
    xs.alias("FireRegenerationData",FireRegenerationData.class);
    xs.alias("VegetativeType",VegetativeType.class);
    xs.alias("ProcessProbLogicData",ProcessProbLogicData.class);
    xs.alias("MtnPineBeetleHazard",MtnPineBeetleHazard.Hazard.class);

    xs.alias("InvasiveSpeciesLogicData",InvasiveSpeciesLogicData.class);
    xs.alias("InvasiveSpeciesChangeLogicData",InvasiveSpeciesChangeLogicData.class);
    xs.alias("InvasiveSpeciesLogicDataMSU",InvasiveSpeciesLogicDataMSU.class);
    xs.alias("ProcessProbLogicData",ProcessProbLogicData.class);

    xs.alias("Lifeform",Lifeform.class);
    xs.alias("Moisture",Climate.Moisture.class);
    xs.alias("Temperature",Climate.Temperature.class);

    xs.alias("SoilType",simpplle.comcode.SoilType.class);
    xs.alias("VegFunctionalGroup",simpplle.comcode.InvasiveSpeciesLogicData.VegFunctionalGroup.class);

    xs.alias("RegenerationDelayLogicData",simpplle.comcode.RegenerationDelayLogicData.class);
    xs.alias("GapProcessLogicData",simpplle.comcode.GapProcessLogicData.class);
    xs.alias("DoCompetitionData",simpplle.comcode.DoCompetitionData.class);

    xs.alias("DoCompetitionDataDensityChange",simpplle.comcode.DoCompetitionData.DensityChange.class);
    xs.alias("DoCompetitionDataActions",simpplle.comcode.DoCompetitionData.Actions.class);

    xs.alias("EvuSearchData",simpplle.comcode.EvuSearchData.class);
    xs.alias("ProducingSeedLogicData",simpplle.comcode.ProducingSeedLogicData.class);
    xs.alias("EvuRegenTypes",simpplle.comcode.Evu.RegenTypes.class);

    xs.alias("AbstractLogicData",simpplle.comcode.AbstractLogicData.class);
    xs.alias("VegUnitFireTypeLogicData",simpplle.comcode.VegUnitFireTypeLogicData.class);
    xs.alias("FireSuppClassALogicData",simpplle.comcode.FireSuppClassALogicData.class);
    xs.alias("FireSuppBeyondClassALogicData",simpplle.comcode.FireSuppBeyondClassALogicData.class);
    xs.alias("FireSuppProductionRateLogicData",simpplle.comcode.FireSuppProductionRateLogicData.class);
    xs.alias("FireSuppSpreadRateLogicData",simpplle.comcode.FireSuppSpreadRateLogicData.class);
    xs.alias("FireSuppWeatherClassALogicData",simpplle.comcode.FireSuppWeatherClassALogicData.class);
    xs.alias("Ownership",simpplle.comcode.Ownership.class);
    xs.alias("RoadsStatus",simpplle.comcode.Roads.Status.class);
    xs.alias("FireType",simpplle.comcode.FireSuppBeyondClassALogicData.FireType.class);
    xs.alias("SpreadKind",simpplle.comcode.FireSuppBeyondClassALogicData.SpreadKind.class);
    xs.alias("FireSpottingLogicData",FireSpottingLogicData.class);
    xs.alias("FireSuppEventLogicData",FireSuppEventLogicData.class);

//    xs.alias("InvasiveSpeciesSimpplleTypeCoeffData",simpplle.comcode.InvasiveSpeciesSimpplleTypeCoeffData.class);
  }
}




