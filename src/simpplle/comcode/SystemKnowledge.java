/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.io.*;
import java.util.jar.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * System knowledge describes how vegetation change by processes, the probability of processes and
 * their spread, and the impact of treatments. The knowledge applies to a geographical region,
 * referred to as a 'zone' in OpenSIMPPLLE. Multiple zones are built into OpenSIMPPLLE. Individual
 * parts of a zone's knowledge may be modified by users and saved into a user knowledge file.
 * User knowledge files should only be loaded into the zone that they were derived from.
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
    FIRESUPP_SPREAD_RATE,      /* need for load of old format file */
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
    LP_MPB,
    PP_MPB,
    WP_MPB,
    WBP_MPB,
    WSBW,
    ROOT_DISEASE,
    SPRUCE_BEETLE,
    DF_BEETLE,
    WILDLIFE,
    KEANE_PARAMETERS

  }

  public static final int NUM_KINDS = Kinds.values().length;

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
  public static final Kinds LP_MPB                           = Kinds.LP_MPB;
  public static final Kinds PP_MPB                           = Kinds.PP_MPB;
  public static final Kinds WP_MPB                           = Kinds.WP_MPB;
  public static final Kinds WBP_MPB                          = Kinds.WBP_MPB;
  public static final Kinds WSBW                             = Kinds.WSBW;
  public static final Kinds ROOT_DISEASE                     = Kinds.ROOT_DISEASE;
  public static final Kinds SPRUCE_BEETLE                    = Kinds.SPRUCE_BEETLE;
  public static final Kinds DF_BEETLE                        = Kinds.DF_BEETLE;
  public static final Kinds WILDLIFE                         = Kinds.WILDLIFE;
  public static final Kinds KEANE_PARAMETERS                 = Kinds.KEANE_PARAMETERS;

  public static final String FMZ_ENTRY                                  = "DATA/FMZ.TXT";
  public static final String TREATMENT_SCHEDULE_ENTRY                   = "DATA/TREATMENT";
  public static final String TREATMENT_LOGIC_ENTRY                      = "DATA/TREATMENT-LOGIC.TXT";
  public static final String PROCESS_SCHEDULE_ENTRY                     = "DATA/PROCESS";
  public static final String INSECT_DISEASE_PROB_ENTRY                  = "DATA/INSECT-DISEASE-PROB.TXT";
  public static final String OLD_PROCESS_PROB_LOGIC_ENTRY               = "DATA/PROCESS-PROB-LOGIC.SER";
  public static final String PROCESS_PROB_LOGIC_ENTRY                   = "DATA/PROCESS-PROB-LOGIC.XML";
  public static final String FIRE_SUPP_CLASS_A_ENTRY                    = "DATA/FIRE-SUPPRESSION-CLASS-A.TXT";
  public static final String FIRE_SUPP_BEYOND_CLASS_A_ENTRY             = "DATA/FIRE-SUPPRESSION-BEYOND-CLASS-A.TXT";
  public static final String EXTREME_FIRE_DATA_ENTRY                    = "DATA/EXTREME-FIRE-DATA.TXT";
  public static final String CLIMATE_ENTRY                              = "DATA/CLIMATE";
  public static final String WILDLIFE_ENTRY                             = "DATA/WILDLIFE";
  public static final String EMISSIONS_ENTRY                            = "DATA/EMISSIONS.TXT";
  public static final String PATHWAYS_ENTRY                             = "PATHWAYS";
  public static final String PATHWAYS_ENTRY_AQUATIC                     = "AQUATIC_PATHWAY-PATHWAYS";
  public static final String HISTORIC_PATHWAYS_ENTRY                    = "HISTORIC-PATHWAYS";
  public static final String FIRE_SEASON_ENTRY                          = "DATA/FIRE-SEASON.TXT";
  public static final String FIRESUPP_PRODUCTION_RATE_ENTRY             = "DATA/FIRESUPP-PRODUCTION-RATE.SER";
  public static final String FIRESUPP_SPREAD_RATE_ENTRY                 = "DATA/FIRESUPP-SPREAD-RATE.SER";
  public static final String SPECIES_ENTRY                              = "DATA/SPECIES.SER";
  public static final String CONIFER_ENCROACHMENT_ENTRY                 = "DATA/CONIFER-ENCROACHMENT.SER";
  public static final String INVASIVE_SPECIES_LOGIC_ENTRY               = "DATA/INVASIVE-SPECIES-LOGIC.XML";
  public static final String INVASIVE_SPECIES_LOGIC_MSU_ENTRY           = "DATA/INVASIVE-SPECIES-LOGIC-MSU.XML";
  public static final String GAP_PROCESS_LOGIC_ENTRY                    = "DATA/GAP-PROCESS-LOGIC.XML";
  public static final String DOCOMPETITION_LOGIC_ENTRY                  = "DATA/DOCOMPETITION-LOGIC.XML";
  public static final String REGEN_DELAY_LOGIC_ENTRY                    = "DATA/REGEN-DELAY-LOGIC.XML";
  public static final String EVU_SEARCH_LOGIC_ENTRY                     = "DATA/EVU-SEARCH-LOGIC.XML";
  public static final String PRODUCING_SEED_LOGIC_ENTRY                 = "DATA/PRODUCING-SEED-LOGIC.XML";
  public static final String VEG_UNIT_FIRE_TYPE_LOGIC_ENTRY             = "DATA/VEG_PATHWAY-UNIT-FIRE-TYPE-LOGIC.XML";
  public static final String REGEN_LOGIC_FIRE_ENTRY                     = "DATA/REGENERATION-LOGIC-FIRE.XML";
  public static final String REGEN_LOGIC_SUCC_ENTRY                     = "DATA/REGENERATION-LOGIC-SUCCESSION.XML";
  public static final String OLD_REGEN_LOGIC_ENTRY                      = "DATA/REGENERATION-LOGIC.TXT";
  public static final String FIRE_SUPP_WEATHER_BEYOND_CLASS_A_ENTRY     = "DATA/FIRE-SUPPRESSION-WEATHER-BEYOND-CLASS-A.XML";
  public static final String OLD_FIRE_SUPP_WEATHER_BEYOND_CLASS_A_ENTRY = "DATA/FIRE-SUPPRESSION-WEATHER-BEYOND-CLASS-A.TXT";
  public static final String FIRE_SUPP_WEATHER_CLASS_A_ENTRY            = "DATA/FIRE-SUPPRESSION-WEATHER-CLASS-A.TXT";
  public static final String OLD_FIRE_SPREAD_ENTRY                      = "DATA/FIRE-SPREAD.TXT";
  public static final String FIRE_SPREAD_DATA_ENTRY                     = "DATA/FIRE-SPREAD-DATA.TXT";
  public static final String FIRE_SPREAD_LOGIC_ENTRY                    = "DATA/FIRE-SPREAD-DATA.XML";
  public static final String FIRE_TYPE_LOGIC_ENTRY                      = "DATA/FIRE-TYPE-DATA.XML";
  public static final String FIRE_TYPE_DATA_ENTRY                       = "DATA/FIRE-TYPE-DATA.TXT";
  public static final String OLD_TYPE_OF_FIRE_ENTRY                     = "DATA/TYPE-OF-FIRE.TXT";
  public static final String FIRE_SUPP_CLASS_A_LOGIC_ENTRY              = "DATA/FIRE-SUPP-CLASS-A-LOGIC.XML";
  public static final String FIRE_SUPP_BEYOND_CLASS_A_LOGIC_ENTRY       = "DATA/FIRE-SUPP-BEYOND-CLASS-A-LOGIC.XML";
  public static final String FIRE_SUPP_PRODUCTION_RATE_LOGIC_ENTRY      = "DATA/FIRE-SUPP-PRODUCTION-RATE-LOGIC.XML";
  public static final String FIRE_SUPP_SPREAD_RATE_LOGIC_ENTRY          = "DATA/FIRE-SUPP-SPREAD-RATE-LOGIC.XML";
  public static final String FIRE_SUPP_WEATHER_CLASS_A_LOGIC_ENTRY      = "DATA/FIRE-SUPP-WEATHER-CLASS-A-LOGIC.XML";
  public static final String TRACKING_SPECIES_REPORT_ENTRY              = "DATA/TRACKING-SPECIES-REPORT.XML";
  public static final String FIRE_SPOTTING_LOGIC_ENTRY                  = "DATA/FIRE-SPOTTING-LOGIC.XML";
  public static final String FIRE_SUPP_EVENT_LOGIC_ENTRY                = "DATA/FIRE-SUPP-EVENT-LOGIC.XML";
  public static final String KEANE_PARAMETERS_ENTRY                     = "DATA/KEANE-PARAMETERS-ENTRY";

  /**
   * Flags indicating if a kind of knowledge has changed.
   */
  private static boolean[] hasChanged = new boolean[NUM_KINDS];

  /**
   * Flags indicating if a kind of knowledge should be saved and loaded.
   */
  private static boolean[] loadSaveMe = new boolean[NUM_KINDS];

  /**
   * Flags indicating if a kind of knowledge is a substitute the data in a zone.
   */
  private static boolean[] hasUserData = new boolean[NUM_KINDS];

  /**
   * An array of files saved individually, outside of a collection of knowledge.
   */
  private static File[] files = new File[NUM_KINDS];

  /**
   * Source information for each kind of knowledge.
   */
  private static String[] knowledgeSource = new String[NUM_KINDS];

  /**
   * Keyword marking the beginning of source information in a file.
   */
  public static String KNOWLEDGE_SOURCE_KEYWORD = "KNOWLEDGE-SOURCE";

  /**
   * The last vegetative pathway loaded from a knowledge file.
   */
  private static HabitatTypeGroup lastPathwayLoaded;

  /**
   * The last aquatic pathway loaded from a knowledge file.
   */
  private static LtaValleySegmentGroup lastAquaticPathwayLoaded;

  /**
   * The file extension appended to a file containing a collection of knowledge.
   */
  private static final String SYSKNOW_FILEEXT = "sysknowledge";

  /**
   * Returns the kind of knowledge represented by a knowledge file.
   *
   * @param name The name of a knowledge file
   * @return A knowledge kind, or null
   */
  private static Kinds getKnowledgeEntryId(String name) {

    name = stripZoneDir(name);

    if (name.equals(FMZ_ENTRY)) {
      return FMZ;
    } else if (name.equals(FIRE_TYPE_LOGIC_ENTRY)) {
      return FIRE_TYPE_LOGIC;
    } else if (name.equals(FIRE_SPREAD_LOGIC_ENTRY)) {
      return FIRE_SPREAD_LOGIC;
    } else if (name.equals(TREATMENT_SCHEDULE_ENTRY)) {
      return TREATMENT_SCHEDULE;
    } else if (name.equals(TREATMENT_LOGIC_ENTRY)) {
      return TREATMENT_LOGIC;
    } else if (name.equals(PROCESS_SCHEDULE_ENTRY)) {
      return PROCESS_SCHEDULE;
//    } else if (name.equals(INSECT_DISEASE_PROB_ENTRY)) {
//      return INSECT_DISEASE_PROB;
    } else if (name.equals(PROCESS_PROB_LOGIC_ENTRY)) {
      return PROCESS_PROB_LOGIC;
    } else if (name.equals(INVASIVE_SPECIES_LOGIC_ENTRY)) {
      return INVASIVE_SPECIES_LOGIC;
    } else if (name.equals(INVASIVE_SPECIES_LOGIC_MSU_ENTRY)) {
      return INVASIVE_SPECIES_LOGIC_MSU;
    } else if (name.startsWith(PATHWAYS_ENTRY)) {
      return VEGETATION_PATHWAYS;
    } else if (name.startsWith(HISTORIC_PATHWAYS_ENTRY)) {
      return VEGETATION_PATHWAYS;
    } else if (name.startsWith(PATHWAYS_ENTRY_AQUATIC)) {
      return AQUATIC_PATHWAYS;
    } else if (name.equals(EXTREME_FIRE_DATA_ENTRY)) {
      return EXTREME_FIRE_DATA;
    } else if (name.equals(CLIMATE_ENTRY)) {
      return CLIMATE;
    } else if (name.equals(REGEN_LOGIC_FIRE_ENTRY)) {
      return REGEN_LOGIC_FIRE;
    } else if (name.equals(REGEN_LOGIC_SUCC_ENTRY)) {
      return REGEN_LOGIC_SUCC;
    } else if (name.equals(FIRE_SEASON_ENTRY)) {
      return FIRE_SEASON;
    } else if (name.equals(FIRESUPP_PRODUCTION_RATE_ENTRY)) {
      return FIRESUPP_PRODUCTION_RATE;
    } else if (name.equals(FIRESUPP_SPREAD_RATE_ENTRY)) {
      return FIRESUPP_SPREAD_RATE;
    } else if (name.equals(SPECIES_ENTRY)) {
      return SPECIES;
    } else if (name.equals(CONIFER_ENCROACHMENT_ENTRY)) {
      return CONIFER_ENCROACHMENT;
    } else if (name.equals(REGEN_DELAY_LOGIC_ENTRY)) {
      return REGEN_DELAY_LOGIC;
    } else if (name.equals(DOCOMPETITION_LOGIC_ENTRY)) {
      return DOCOMPETITION_LOGIC;
    } else if (name.equals(GAP_PROCESS_LOGIC_ENTRY)) {
      return GAP_PROCESS_LOGIC;
    } else if (name.equals(EVU_SEARCH_LOGIC_ENTRY)) {
      return EVU_SEARCH_LOGIC;
    } else if (name.equals(PRODUCING_SEED_LOGIC_ENTRY)) {
      return PRODUCING_SEED_LOGIC;
    } else if (name.equals(VEG_UNIT_FIRE_TYPE_LOGIC_ENTRY)) {
      return VEG_UNIT_FIRE_TYPE_LOGIC;
    } else if (name.equals(FIRE_SUPP_CLASS_A_LOGIC_ENTRY)) {
      return FIRE_SUPP_CLASS_A_LOGIC;
    } else if (name.equals(FIRE_SUPP_BEYOND_CLASS_A_LOGIC_ENTRY)) {
      return FIRE_SUPP_BEYOND_CLASS_A_LOGIC;
    } else if (name.equals(FIRE_SUPP_PRODUCTION_RATE_LOGIC_ENTRY)) {
      return FIRE_SUPP_PRODUCTION_RATE_LOGIC;
    } else if (name.equals(FIRE_SUPP_SPREAD_RATE_LOGIC_ENTRY)) {
      return FIRE_SUPP_SPREAD_RATE_LOGIC;
    } else if (name.equals(FIRE_SUPP_WEATHER_CLASS_A_LOGIC_ENTRY)) {
      return FIRE_SUPP_WEATHER_CLASS_A_LOGIC;
    } else if (name.equals(TRACKING_SPECIES_REPORT_ENTRY)) {
      return TRACKING_SPECIES_REPORT;
    } else if (name.equals(FIRE_SPOTTING_LOGIC_ENTRY)) {
      return FIRE_SPOTTING_LOGIC;
    } else if (name.equals(FIRE_SUPP_EVENT_LOGIC_ENTRY)) {
      return FIRE_SUPP_EVENT_LOGIC;
    } else if (name.equals(FIRE_SUPP_BEYOND_CLASS_A_ENTRY)) {
      return FIRE_SUPP_BEYOND_CLASS_A;
    } else if (name.equals(FIRE_SUPP_WEATHER_BEYOND_CLASS_A_ENTRY)) {
      return FIRE_SUPP_WEATHER_BEYOND_CLASS_A;
    } else if (name.equals(KEANE_PARAMETERS_ENTRY)) {
      return KEANE_PARAMETERS;
    } else if (name.startsWith(WILDLIFE_ENTRY)) {
      return null;
    } else if (name.equals(EMISSIONS_ENTRY)) {
      return null;
    } else {
      return null;
    }
  }

  /**
   * Returns the file extension associated with a kind of knowledge.
   *
   * @param kind A kind of knowledge
   * @return A file extension, or an empty string
   */
  public static String getKnowledgeFileExtension(Kinds kind) {

    switch (kind) {

      case FMZ:
        return "sk_fmz";
      case TREATMENT_SCHEDULE:
        return "sk_treatsched";
      case TREATMENT_LOGIC:
        return "sk_treatlogic";
      case PROCESS_SCHEDULE:
        return "sk_processsched";
//      case INSECT_DISEASE_PROB:
//        return "sk_probability";
      case PROCESS_PROB_LOGIC:
        return "sk_processproblogic";
      case FIRE_SUPP_BEYOND_CLASS_A:
        return "sk_fire_suppbeyonda";
      case FIRE_SUPP_WEATHER_BEYOND_CLASS_A:
        return "sk_fire_suppweather";
      case CLIMATE:
        return "sk_climate";
      case REGEN_LOGIC_FIRE:
        return "sk_regenlogicfire";
      case REGEN_LOGIC_SUCC:
        return "sk_regenlogicsucc";
      case FIRE_SEASON:
        return "sk_fireseason";
      case FIRE_TYPE_LOGIC:
        return "sk_firetype";
      case FIRE_SPREAD_LOGIC:
        return "sk_firespread";
      case FIRE_SPOTTING_LOGIC:
        return "sk_firespotting";
      case FIRE_SUPP_EVENT_LOGIC:
        return "sk_firesuppevent";
      case CONIFER_ENCROACHMENT:
        return "sk_conifer";
      case SPECIES:
        return "sk_species";
      case FIRESUPP_PRODUCTION_RATE:
        return "sk_firesuppprodrate";
      case FIRESUPP_SPREAD_RATE:
        return "sk_firesuppspreadrate";
      case VEGETATION_PATHWAYS:
        return "sk_pathway";
      case AQUATIC_PATHWAYS:
        return "sk_aquapathway";
      case INVASIVE_SPECIES_LOGIC:
        return "sk_invasivespecieslogic";
      case INVASIVE_SPECIES_LOGIC_MSU:
        return "sk_invasivespecieslogicmsu";
      case REGEN_DELAY_LOGIC:
        return "sk_regendelaylogic";
      case DOCOMPETITION_LOGIC:
        return "sk_competitionlogic";
      case GAP_PROCESS_LOGIC:
        return "sk_gapprocesslogic";
      case EVU_SEARCH_LOGIC:
        return "sk_evusearchlogic";
      case PRODUCING_SEED_LOGIC:
        return "sk_producingseedlogic";
      case VEG_UNIT_FIRE_TYPE_LOGIC:
        return "sk_vegunitfiretypelogic";
      case FIRE_SUPP_CLASS_A_LOGIC:
        return "sk_firesuppclassalogic";
      case FIRE_SUPP_BEYOND_CLASS_A_LOGIC:
        return "sk_firesuppbeyondclassalogic";
      case FIRE_SUPP_PRODUCTION_RATE_LOGIC:
        return "sk_firesuppproductionratelogic";
      case FIRE_SUPP_SPREAD_RATE_LOGIC:
        return "sk_firesuppspreadratelogic";
      case FIRE_SUPP_WEATHER_CLASS_A_LOGIC:
        return "sk_firesuppweatherclassalogic";
      case TRACKING_SPECIES_REPORT:
        return "sk_trackingspeciesreport";
      default:
        return "";

    }
  }

  /**
   * Returns the name of a kind of knowledge.
   *
   * @param kind A kind of knowledge
   * @return A knowledge name, or an empty string
   */
  public static String getKnowledgeFileDescription(Kinds kind) {

    switch (kind) {

      case FMZ:
        return "FMZ Data";
      case TREATMENT_SCHEDULE:
        return "Treatment Schedule";
      case TREATMENT_LOGIC:
        return "Treatment Logic";
      case PROCESS_SCHEDULE:
        return "Process Schedule";
//      case INSECT_DISEASE_PROB:
//        return "insect/Disease Probability";
      case PROCESS_PROB_LOGIC:
        return "Process Probability Logic";
      case FIRE_SUPP_BEYOND_CLASS_A:
        return "Fire Suppression";
      case FIRE_SUPP_WEATHER_BEYOND_CLASS_A:
        return "Fire Suppression";
      case CLIMATE:
        return "Climate";
      case REGEN_LOGIC_FIRE:
        return "Regeneration Logic Fire";
      case REGEN_LOGIC_SUCC:
        return "Regeneration Logic Succession";
      case FIRE_SEASON:
        return "Fire Season";
      case FIRE_TYPE_LOGIC:
        return "Type of Fire Logic";
      case FIRE_SPREAD_LOGIC:
        return "Fire Spread Logic";
      case FIRE_SPOTTING_LOGIC:
        return "Fire Spotting Logic";
      case FIRE_SUPP_EVENT_LOGIC:
        return "Fire Suppression Event Logic";
      case CONIFER_ENCROACHMENT:
        return "Conifer Encroachment";
      case SPECIES:
        return "Species";
      case FIRESUPP_PRODUCTION_RATE:
        return "Fire Supp Prod Rate";
      case FIRESUPP_SPREAD_RATE:
        return "Fire Supp Spread Rate";
      case VEGETATION_PATHWAYS:
        return "Veg Pathway";
      case AQUATIC_PATHWAYS:
        return "Aquatic Pathway";
      case INVASIVE_SPECIES_LOGIC:
        return "Invasive Species Logic";
      case INVASIVE_SPECIES_LOGIC_MSU:
        return "Invasive Species Logic MSU";
      case REGEN_DELAY_LOGIC:
        return "Regeneration Delay Logic";
      case DOCOMPETITION_LOGIC:
        return "Lifeform Competition Logic";
      case GAP_PROCESS_LOGIC:
        return "Gap Process Logic";
      case EVU_SEARCH_LOGIC:
        return "Evu Search Logic";
      case PRODUCING_SEED_LOGIC:
        return "Producing Seed Logic";
      case VEG_UNIT_FIRE_TYPE_LOGIC:
        return "Veg Unit Fire Type Logic";
      case FIRE_SUPP_CLASS_A_LOGIC:
        return "Fire Supp Class A Logic";
      case FIRE_SUPP_BEYOND_CLASS_A_LOGIC:
        return "Fire Supp Beyond Class A Logic";
      case FIRE_SUPP_PRODUCTION_RATE_LOGIC:
        return "Fire Supp Production Rate Logic";
      case FIRE_SUPP_SPREAD_RATE_LOGIC:
        return "Fire Supp Spread Rate Logic";
      case FIRE_SUPP_WEATHER_CLASS_A_LOGIC:
        return "Fire Supp Weather Class A Logic";
      case TRACKING_SPECIES_REPORT:
        return "Tracking Species Report";
      case KEANE_PARAMETERS:
        return "Keane Fire Spread Parameters";
      default:
        return "";

    }
  }

  /**
   * Returns the name of a kind of knowledge.
   *
   * @param kind A kind of knowledge
   * @return A knowledge name, or an empty string
   */
  public static String getKnowledgeFileTitle(Kinds kind) {

    switch (kind) {

      case FMZ:
        return "Fire Management Zone Data";
      case TREATMENT_SCHEDULE:
        return "Treatment Schedule";
      case TREATMENT_LOGIC:
        return "Treatment Logic";
      case PROCESS_SCHEDULE:
        return "Process Schedule";
//      case INSECT_DISEASE_PROB:
//        return "insect/Disease Probability";
      case FIRE_SUPP_BEYOND_CLASS_A:
        return "Fire Suppression (Beyond Class A)";
      case FIRE_SUPP_WEATHER_BEYOND_CLASS_A:
        return "Fire Suppression (Beyond Class A Weather Event)";
      case CLIMATE:
        return "Climate";
      case REGEN_LOGIC_FIRE:
        return "Regeneration Logic Fire";
      case REGEN_LOGIC_SUCC:
        return "Regeneration Logic Succession";
      case FIRE_SEASON:
        return "Fire Season";
      case FIRE_TYPE_LOGIC:
        return "Type of Fire Logic";
      case FIRE_SPREAD_LOGIC:
        return "Fire Spread Logic";
      case FIRE_SPOTTING_LOGIC:
        return "Fire Spotting Logic";
      case FIRE_SUPP_EVENT_LOGIC:
        return "Fire Suppression Event Logic";
      case CONIFER_ENCROACHMENT:
        return "Conifer Encroachment";
      case SPECIES:
        return "Species Knowledge";
      case FIRESUPP_PRODUCTION_RATE:
        return "Fire Suppression Production Rate";
      case FIRESUPP_SPREAD_RATE:
        return "Fire Suppression Spread Rate";
      case VEGETATION_PATHWAYS:
        return "Vegetation Pathway";
      case AQUATIC_PATHWAYS:
        return "Aquatic Pathway";
      case PROCESS_PROB_LOGIC:
        return "Process Probability Logic";
      case INVASIVE_SPECIES_LOGIC:
        return "Invasive Species Logic";
      case INVASIVE_SPECIES_LOGIC_MSU:
        return "Invasive Species Logic MSU";
      case REGEN_DELAY_LOGIC:
        return "Regeneration Delay Logic";
      case DOCOMPETITION_LOGIC:
        return "Lifeform Competition Logic";
      case GAP_PROCESS_LOGIC:
        return "Gap Process Logic";
      case EVU_SEARCH_LOGIC:
        return "Evu Search Logic";
      case PRODUCING_SEED_LOGIC:
        return "Producing Seed Logic";
      case VEG_UNIT_FIRE_TYPE_LOGIC:
        return "Veg Unit Fire Type Logic";
      case FIRE_SUPP_CLASS_A_LOGIC:
        return "Fire Supp Class A Logic";
      case FIRE_SUPP_BEYOND_CLASS_A_LOGIC:
        return "Fire Supp Beyond Class A Logic";
      case FIRE_SUPP_PRODUCTION_RATE_LOGIC:
        return "Fire Supp Production Rate Logic";
      case FIRE_SUPP_SPREAD_RATE_LOGIC:
        return "Fire Supp Spread Rate Logic";
      case FIRE_SUPP_WEATHER_CLASS_A_LOGIC:
        return "Fire Supp Weather Class A Logic";
      case TRACKING_SPECIES_REPORT:
        return "Tracking Species Report";
      case KEANE_PARAMETERS:
        return "Keane Cell Percolation Parameters";
      default:
        return "";

    }
  }

  /**
   * Flags a change to a kind of knowledge, and flags the replacement of zone knowledge.
   *
   * @param kind A kind of knowledge
   */
  public static void markChanged(Kinds kind) {
    hasChanged[kind.ordinal()] = true;
    setHasUserData(kind,true);
  }
  
  /**
   * Sets a flag indicating if a kind of knowledge has changed.
   *
   * @param kind A kind of knowledge
   * @param value Flag indicating if the knowledge changed
   */
  public static void setHasChanged(Kinds kind, boolean value) {
    hasChanged[kind.ordinal()] = value;
  }

  /**
   * Returns true if the kind of knowledge has changed.
   *
   * @param kind A kind of system knowledge
   * @return True if the knowledge has changed
   */
  private static boolean hasChanged(Kinds kind) {
    return hasChanged[kind.ordinal()];
  }

  /**
   * Sets a flag indicating if a kind of knowledge is replacing zone data.
   *
   * @param kind A kind of knowledge
   * @param value Flag indicating if the knowledge replaces zone data
   */
  public static void setHasUserData(Kinds kind, boolean value) {
    hasUserData[kind.ordinal()] = value;
  }

  /**
   * Returns true if any kind of knowledge has been changed.
   *
   * @return True if any knowledge changed
   */
  public static boolean hasKnowledgeChanged() {
    for (int i=0; i<hasChanged.length; i++) {
      if (hasChanged[i]) { return true; }
    }
    return false;
  }

  /**
   * Returns true if a kind of knowledge has changed and replaces zone data.
   *
   * @param kind A kind of knowledge
   * @return True fi the knowledge has changed or replaces zone data
   */
  public static boolean hasChangedOrUserData(Kinds kind) {
    return (hasChanged[kind.ordinal()] || hasUserData[kind.ordinal()]);
  }

  /**
   * Flags a kind of knowledge to be saved or loaded during the next save or load operation.
   *
   * @param kind A kind of knowledge
   * @param loadSave Flag indicating that the knowledge should be saved or loaded
   */
  public static void setLoadSave(Kinds kind, boolean loadSave) {
    loadSaveMe[kind.ordinal()] = loadSave;
  }

  /**
   * Returns true if a kind of knowledge will be saved or loaded.
   *
   * @param kind A kind of knowledge
   * @return True if the knowledge will be saved or loaded
   */
  public static boolean isLoadSave(Kinds kind) {
    return loadSaveMe[kind.ordinal()];
  }

  /**
   * Returns the file associated with a kind of knowledge.
   *
   * @param kind A kind of knowledge
   * @return A file, or null if no file exists
   */
  public static File getFile(Kinds kind) {
    return files[kind.ordinal()];
  }

  /**
   * Assigns a file to a kind of knowledge.
   *
   * @param kind A kind of knowledge
   * @param file A file to associate
   */
  public static void setFile(Kinds kind, File file) {
    files[kind.ordinal()] = file;
  }

  /**
   * Unassigns a file from a kind of knowledge.
   *
   * @param kind A kind of knowledge
   */
  public static void clearFile(Kinds kind) {
    files[kind.ordinal()] = null;
  }

  /**
   * Returns the aquatic pathway that was loaded most recently.
   *
   * @return A LTA valley segment group
   */
  public static LtaValleySegmentGroup getLastAquaticPathwayLoaded() {
    return lastAquaticPathwayLoaded;
  }

  /**
   * Returns the vegetative pathway that was loaded most recently.
   *
   * @return A habitat type group
   */
  public static HabitatTypeGroup getLastVegetativePathwayLoaded() {
    return lastPathwayLoaded;
  }

  /**
   * Checks if there is a GIS Extras file for current zone.
   *
   * @return True if the file exists and is not empty
   */
  public static boolean existsGISExtras() {
    File file = Simpplle.getCurrentZone().getSystemKnowledgeGisExtraFile();
    return (file.exists() && file.length() > 0);
  }

  /**
   * Removes the current zone directory from a path.
   *
   * @param path A path to an entry in a zone directory
   * @return The path with the zone directory stripped
   */
  private static String stripZoneDir(String path) {
    path = path.toUpperCase();
    String zoneDir = Simpplle.getCurrentZone().getZoneDir().toUpperCase();
    int index = path.indexOf(zoneDir);
    if (index != -1) {
      // Add 1 for the slash at the end.
      return path.substring(index + zoneDir.length() + 1);
    }
    return path;
  }

  /**
   * Copies a dummy database for debugging and training purposes. The output file is prefixed with
   * "dummy".
   *
   * @param destDir The output directory
   * @param prefix A prefix for the output file
   * @param kind A kind of knowledge
   */
  public static void copyDummyDatabaseFile(String destDir, String prefix, String kind) throws SimpplleError {

    File dir;
    String outfile;
    if (kind.equalsIgnoreCase("mdb")) {
      dir = Simpplle.getCurrentZone().getSystemKnowledgeDummyMDBDir();
      outfile = Utility.makePathname(destDir, prefix, kind);
    } else {
      dir = Simpplle.getCurrentZone().getSystemKnowledgeDummyHsqldbDir();
      String dbPrefix = prefix + "db";
      File dbDir = new File(destDir, dbPrefix);
      dbDir.mkdir();
      outfile = Utility.makePathname(dbDir.toString(),dbPrefix,kind);
    }

    try {

      BufferedInputStream fin = new BufferedInputStream(new FileInputStream(new File(dir, "dummy." + kind)));
      BufferedOutputStream fout = new BufferedOutputStream(new FileOutputStream(outfile));

      int data = fin.read();
      while (data != -1) {
        fout.write(data);
        data = fin.read();
      }

      fout.flush();
      fout.close();
      fin.close();

    } catch (IOException err) {
      err.printStackTrace();
      throw new SimpplleError("Could not copy file.");
    }
  }

  /**
   * Copies ArcView GIS files from the "gis" directory to a destination directory.
   *
   * @param destDir The directory to copy files to
   * @throws SimpplleError
   */
  public static void copyArcviewGisFiles(File destDir) throws SimpplleError {
    copyGisFiles(destDir,"gis");
  }

  /**
   * Copies ArcGIS files from the "arcgis" directory to a destination directory.
   *
   * @param destDir The directory to copy files to
   * @throws SimpplleError
   */
  public static void copyArcGISFiles(File destDir) throws SimpplleError {
    copyGisFiles(destDir,"arcgis");
  }

  /**
   * Copies GIS coverage files to a destination directory.
   *
   * @param destDir The directory to copy files to
   * @throws SimpplleError
   */
  public static void copyCoverage(File destDir) throws SimpplleError {

    try {

      File tmpDir = new File(destDir.toString(),"SIMPPLLE-gisdata");
      tmpDir.mkdir();
      destDir = tmpDir;
      tmpDir = new File(destDir.toString(),"coverage");
      int index=1;
      while (tmpDir.exists()) {
        tmpDir = new File(destDir.toString(),("coverage" + Integer.toString(index)));
        index++;
      }
      tmpDir.mkdir();
      destDir = tmpDir;

      File filename = Simpplle.getCurrentZone().getSystemKnowledgeGisExtraFile();
      JarInputStream jarIn = new JarInputStream(new FileInputStream(filename));
      BufferedInputStream fin = new BufferedInputStream(jarIn);

      JarEntry jarEntry = jarIn.getNextJarEntry();
      while (jarEntry != null) {
        String name = jarEntry.getName().toLowerCase();
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

        BufferedOutputStream fout = new BufferedOutputStream(new FileOutputStream(outfile));
        int data = fin.read();
        while(data != -1) {
          fout.write(data);
          data = fin.read();
        }
        fout.flush();
        fout.close();

        jarEntry = jarIn.getNextJarEntry();

      }

      fin.close();

    } catch (IOException err) {
      err.printStackTrace();
      throw new SimpplleError("Could not copy one or more GIS files.");
    }
  }

  /**
   * Copies GIS interchange files.
   *
   * @param destDir The directory to copy files to
   * @throws SimpplleError
   */
  public static void copyInterchangeFile(File destDir) throws SimpplleError {
    copyGisExtraFiles(destDir, "e00.zip");
  }

  /**
   * Copies GIS geo database files'
   *
   * @param destDir The directory to copy files to
   * @throws SimpplleError
   */
  public static void copyGeodatabase(File destDir) throws SimpplleError {
    copyGisExtraFiles(destDir, "mdb");
  }

  /**
   * Copies extra GIS files.
   *
   * @param destDir The directory to copy files to
   * @param extension The extension of the files to copy
   * @throws SimpplleError
   */
  private static void copyGisExtraFiles(File destDir, String extension) throws SimpplleError {

    try {

      File tmpDir = new File(destDir.toString(),"SIMPPLLE-gisdata");
      tmpDir.mkdir();
      destDir = tmpDir;

      File filename = Simpplle.getCurrentZone().getSystemKnowledgeGisExtraFile();
      JarInputStream jarIn = new JarInputStream(new FileInputStream(filename));
      BufferedInputStream fin = new BufferedInputStream(jarIn);

      JarEntry jarEntry = jarIn.getNextJarEntry();
      while (jarEntry != null) {
        String name = jarEntry.getName().toLowerCase();
        if (name.endsWith(extension)) {
          File outfile = new File(destDir,name);
          BufferedOutputStream fout = new BufferedOutputStream(new FileOutputStream(outfile));
          int data = fin.read();
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

    } catch (IOException err) {
      err.printStackTrace();
      throw new SimpplleError("Could not copy file.");
    }
  }

  /**
   * Copies gis files to a destination directory.
   *
   * @param destDir The directory to copy files to
   * @param gisDir The prefix of the files to copy
   * @throws SimpplleError
   */
  private static void copyGisFiles(File destDir, String gisDir) throws SimpplleError {

    try {

      File filename = Simpplle.getCurrentZone().getSystemKnowledgeFile();
      JarInputStream jarIn = new JarInputStream(new FileInputStream(filename));
      BufferedInputStream fin = new BufferedInputStream(jarIn);

      JarEntry jarEntry = jarIn.getNextJarEntry();
      while (jarEntry != null) {
        String name = jarEntry.getName().toLowerCase();
        if (jarEntry.isDirectory()) {
          jarEntry = jarIn.getNextJarEntry();
          continue;
        }
        if (name.startsWith(gisDir)) {
          String[] pathSplit = name.split("/");
          File outfile = new File(destDir,pathSplit[1]);

          BufferedOutputStream fout = new BufferedOutputStream(new FileOutputStream(outfile));
          int data = fin.read();
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

    } catch (IOException err) {
      err.printStackTrace();
      throw new SimpplleError("Could not copy one or more GIS files.");
    }
  }

  /**
   * Returns true if an entry exists in a JAR input stream.
   *
   * @param stream A JAR input stream
   * @param entryName An entry name prefix
   * @return True if an entry with a matching prefix is found
   */
  private static boolean findEntry(JarInputStream stream, String entryName) throws IOException {
    JarEntry jarEntry = stream.getNextJarEntry();
    while (jarEntry != null) {
      if (jarEntry.isDirectory()) {
        jarEntry = stream.getNextJarEntry();
        continue;
      }
      String name = jarEntry.getName().toUpperCase();
      if (name.startsWith(entryName)) {
        return true;
      }
      jarEntry = stream.getNextJarEntry();
    }
    return false;
  }

  /**
   * Returns a reader for an entry in a JAR file.
   *
   * @param file A JAR file
   * @param entryName An entry name prefix
   * @return A reader for the first entry whose name contains the prefix
   * @throws SimpplleError
   */
  public static BufferedReader getEntryStream(File file, String entryName) throws SimpplleError {
    try {
      JarInputStream jarIn = new JarInputStream(new FileInputStream(file));
      BufferedReader fin = new BufferedReader(new InputStreamReader(jarIn));
      if (!findEntry(jarIn, entryName)) {
        throw new SimpplleError("Unable to find entry: " + entryName);
      }
      return fin;
    } catch (IOException err) {
      err.printStackTrace();
      throw new SimpplleError("Could not read System Knowledge File");
    }
  }

  /**
   * Returns an input stream for a sample area in a JAR file.
   *
   * @param path The path to a sample area
   * @return An input stream if the area exists, or null otherwise
   * @throws SimpplleError
   */
  public static JarInputStream getSampleAreaStream(String path) throws SimpplleError {
    try {
      File filename = Simpplle.getCurrentZone().getSystemKnowledgeFile();
      JarInputStream jarIn = new JarInputStream(new FileInputStream(filename));
      JarEntry jarEntry = jarIn.getNextJarEntry();
      while (jarEntry != null) {
        if (jarEntry.isDirectory()) {
          jarEntry = jarIn.getNextJarEntry();
          continue;
        }
        String name = jarEntry.getName();
        if (name.equalsIgnoreCase(path.toUpperCase())) {
          return jarIn;
        }
        jarEntry = jarIn.getNextJarEntry();
      }
      return null;
    } catch (IOException err) {
      throw new SimpplleError("Could not read Sample Area", err);
    }
  }

  /**
   * Updates save and load flags to match the kinds of knowledge present in a JAR file.
   *
   * @param file A knowledge file
   * @throws SimpplleError
   */
  public static void recordExistingKnowledge(File file) throws SimpplleError {

    for (int i = 0; i < loadSaveMe.length; i++) {
      loadSaveMe[i] = false;
    }

    try {
      JarInputStream jarIn = new JarInputStream(new FileInputStream(file));
      JarEntry jarEntry = jarIn.getNextJarEntry();
      while (jarEntry != null) {
        if (jarEntry.isDirectory()) {
          jarEntry = jarIn.getNextJarEntry();
          continue;
        }
        String name = jarEntry.getName().toUpperCase();
        Kinds entryId = getKnowledgeEntryId(name);
        name = stripZoneDir(name);
        if (entryId != null) {
          loadSaveMe[entryId.ordinal()] = true;
        } else if (name.equals(OLD_TYPE_OF_FIRE_ENTRY)) {
          loadSaveMe[FIRE_TYPE_LOGIC.ordinal()] = true;
        } else if (name.equals(OLD_FIRE_SPREAD_ENTRY)) {
          loadSaveMe[FIRE_SPREAD_LOGIC.ordinal()] = true;
        } else if (name.equals(FIRE_TYPE_DATA_ENTRY)) {
          loadSaveMe[FIRE_TYPE_LOGIC.ordinal()] = true;
        } else if (name.equals(FIRE_SPREAD_DATA_ENTRY)) {
          loadSaveMe[FIRE_SPREAD_LOGIC.ordinal()] = true;
        } else if (name.equals(OLD_REGEN_LOGIC_ENTRY)) {
          loadSaveMe[REGEN_LOGIC_FIRE.ordinal()] = true;
          loadSaveMe[REGEN_LOGIC_SUCC.ordinal()] = true;
        }
        jarEntry = jarIn.getNextJarEntry();
      }
      jarIn.close();
    } catch (IOException err) {
      err.printStackTrace();
      throw new SimpplleError("Could not read System Knowledge File");
    }
  }

  /**
   * Loads a sample area from the current zone's system knowledge file.
   *
   * @param area The area to be loaded
   * @throws SimpplleError
   */
  public static void loadSampleArea(Area area) throws SimpplleError {
    String path = area.getPath();
    try {
      File filename = Simpplle.getCurrentZone().getSystemKnowledgeFile();
      JarInputStream jarIn = new JarInputStream(new FileInputStream(filename));
      BufferedReader fin = new BufferedReader(new InputStreamReader(jarIn));
      JarEntry jarEntry = jarIn.getNextJarEntry();
      while (jarEntry != null) {
        if (jarEntry.isDirectory()) {
          jarEntry = jarIn.getNextJarEntry();
          continue;
        }
        String name = jarEntry.getName();
        if (name.equalsIgnoreCase(path)) {
          area.loadArea(fin);
          break;
        }
        jarEntry = jarIn.getNextJarEntry();
      }
      fin.close();
    } catch (IOException err) {
      err.printStackTrace();
      throw new SimpplleError("Could not read Sample Area");
    }
  }

  /**
   * Sets aquatic HabitatTypeGroupType file pathway.
   *
   * @param groupName Name of the pathway group
   * @throws SimpplleError
   */
  public static void loadAquaticPathway(String groupName) throws SimpplleError {
    loadPathway(groupName, false);
  }

  /**
   * Loads a vegetative pathway from the current zone.
   *
   * @param groupName Name of the pathway group
   * @throws SimpplleError
   */
  public static void loadVegetativePathway(String groupName) throws SimpplleError {
    loadPathway(groupName, true);
  }

  /**
   * Loads a vegetative or aquatic pathway for a habitat type group in the current zone.
   *
   * @param htGrpName The name of a habitat type group
   * @param isVegetative True if the pathway is vegetative, otherwise aquatic
   * @throws SimpplleError
   */
  private static void loadPathway(String htGrpName, boolean isVegetative) throws SimpplleError {
    RegionalZone zone = Simpplle.getCurrentZone();

    String groupFileOld = htGrpName;
    String groupFileNew = htGrpName + ".txt";

    String pathwayStr;
    if (isVegetative) {
      pathwayStr = (zone.isHistoric()) ? HISTORIC_PATHWAYS_ENTRY : PATHWAYS_ENTRY;
    } else {
      pathwayStr = PATHWAYS_ENTRY_AQUATIC;
    }

    try {
      File filename = Simpplle.getCurrentZone().getSystemKnowledgePathwayFile();
      JarInputStream jarIn = new JarInputStream(new FileInputStream(filename));
      BufferedReader fin   = new BufferedReader(new InputStreamReader(jarIn));

      JarEntry jarEntry = jarIn.getNextJarEntry();
      while (jarEntry != null) {
        if (jarEntry.isDirectory()) {
          jarEntry = jarIn.getNextJarEntry();
          continue;
        }
        String name = jarEntry.getName().toUpperCase();
        name = stripZoneDir(name);
        if (name.startsWith(pathwayStr) &&
            (name.endsWith(groupFileOld.toUpperCase()) ||
             name.endsWith(groupFileNew.toUpperCase()))) {
          if (isVegetative) {
            zone.loadPathway(fin).setIsUserData(false);
          } else {
            zone.loadAquaticPathway(fin).setIsUserData(false);
          }
          break;
        }
        jarEntry = jarIn.getNextJarEntry();
      }
      fin.close();
    } catch (IOException err) {
      err.printStackTrace();
      throw new SimpplleError("Could not read Sample Area");
    }
  }

  /**
   * Loads all vegetative pathways for the current zone.
   *
   * @throws SimpplleError
   */
  public static void loadAllVegetativePathways() throws SimpplleError {
    loadAllPathways(true);
  }

  /**
   * Loads all aquatic pathways for the current zone.
   *
   * @throws SimpplleError
   */
  public static void loadAllAquaticPathways() throws SimpplleError {
    loadAllPathways(false);
  }
  
  /**
   * Loads all vegetative or aquatic pathways for the current zone.
   *
   * @param isVegetative True if the pathway is vegetative, otherwise aquatic
   * @throws SimpplleError
   */
  private static void loadAllPathways(boolean isVegetative) throws SimpplleError {

    RegionalZone zone = Simpplle.getCurrentZone();

    String pathwayStr;
    if (isVegetative) {
      pathwayStr = (zone.isHistoric()) ? HISTORIC_PATHWAYS_ENTRY : PATHWAYS_ENTRY;
    } else {
      pathwayStr = PATHWAYS_ENTRY_AQUATIC;
    }

    File filename = Simpplle.getCurrentZone().getSystemKnowledgePathwayFile();
    try {
      JarInputStream jarIn = new JarInputStream(new FileInputStream(filename));
      BufferedReader fin   = new BufferedReader(new InputStreamReader(jarIn));

      JarEntry jarEntry = jarIn.getNextJarEntry();
      while (jarEntry != null) {
        if (jarEntry.isDirectory()) {
          jarEntry = jarIn.getNextJarEntry();
          continue;
        }
        String name = jarEntry.getName().toUpperCase();
        name = stripZoneDir(name);
        if (name.startsWith(pathwayStr)) { 
          if (isVegetative) {
            zone.loadPathway(fin).setIsUserData(false);
          } else {
            zone.loadAquaticPathway(fin).setIsUserData(false);
          }
        }
        jarEntry = jarIn.getNextJarEntry();
      }
      fin.close();
    } catch (IOException err) {
      err.printStackTrace();
      throw new SimpplleError("Could not read Sample Area");
    }
  }

  /**
   * Loads a single kind of knowledge from a user knowledge file.
   *
   * @param file A user knowledge file
   * @param kind A kind of knowledge
   * @throws SimpplleError
   */
  public static void loadUserKnowledge(File file, Kinds kind) throws SimpplleError {
    for (int i = 0; i < loadSaveMe.length; i++) {
      loadSaveMe[i] = false;
    }
    loadSaveMe[kind.ordinal()] = true;
    loadKnowledge(file, false, false, true);
    files[kind.ordinal()] = file;
  }

  /**
   * Loads a user knowledge file.
   *
   * @param file A user knowledge file
   * @throws SimpplleError
   */
  public static void loadUserKnowledge(File file) throws SimpplleError {
    loadKnowledge(file, false, true, false);
  }

  /**
   * Reads a single kind of knowledge from the current zone's knowledge file.
   *
   * @param kind A kind of knowledge
   * @throws SimpplleError
   */
  public static void loadZoneKnowledge(Kinds kind) throws SimpplleError {

    for (int i = 0; i < loadSaveMe.length; i++) {
      loadSaveMe[i] = false;
    }
    loadSaveMe[kind.ordinal()] = true;

    File file;
    if (kind == VEGETATION_PATHWAYS || kind == AQUATIC_PATHWAYS) {
      file = Simpplle.getCurrentZone().getSystemKnowledgePathwayFile();
    } else {
      file = Simpplle.getCurrentZone().getSystemKnowledgeFile();
    }

    loadKnowledge(file, true, false, false);

  }

  /**
   * Loads a zone knowledge file.
   * 
   * @param file A zone knowledge file
   * @throws SimpplleError
   */
  public static void loadZoneKnowledge(File file) throws SimpplleError {
    loadKnowledge(file, true, true, false);
  }

  /**
   * Loads knowledge from a JAR file.
   * <p>
   * This method uses the XStream library to deserialize data from XML files.
   * @link http://xstream.codehaus.org/
   *
   * @param file A knowledge file
   * @param isZone A flag indicating if this is zone knowledge
   * @param readAll A flag indicating if all kinds of knowledge should be read
   * @param isIndividualFile A flag indicating if files should be recorded
   * @throws SimpplleError
   */
  public static void loadKnowledge(File file,
                                   boolean isZone,
                                   boolean readAll,
                                   boolean isIndividualFile) throws SimpplleError {

    RegionalZone zone = Simpplle.getCurrentZone();
    JarInputStream jarIn = null;
    BufferedReader fin = null;
    String name = null;

    if (isZone && readAll) {
      for (int i=0; i<loadSaveMe.length; i++) {
        loadSaveMe[i] = true;
      }
      FireEvent.resetExtremeData();
    }

    FireSpottingLogicData.clearMaxDistance();

    try {
      jarIn = new JarInputStream(new FileInputStream(file));
      fin   = new BufferedReader(new InputStreamReader(jarIn));

      JarEntry jarEntry = jarIn.getNextJarEntry();
      while (jarEntry != null) {
        if (jarEntry.isDirectory()) {
          jarEntry = jarIn.getNextJarEntry();
          continue;
        }
        name = jarEntry.getName();
        name = stripZoneDir(name);
        int begin = name.indexOf('/');
        int end   = name.lastIndexOf('.');
        if (end < 0) { end = name.length(); }
        String msg = " ---> Loading " + name.substring(0,begin) + " " + name.substring(begin+1,end);
        name = name.toUpperCase();
        Kinds entryId = getKnowledgeEntryId(name);

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
          lastAquaticPathwayLoaded.setIsUserData((!isZone));
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

        if (entryId == KEANE_PARAMETERS && loadSaveMe[KEANE_PARAMETERS.ordinal()]){
          ProcessOccurrenceSpreadingFire.loadKeaneParameters(fin);
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

        // Since there are two sets of pathways we need to make sure we load the correct ones.
        if (entryId == VEGETATION_PATHWAYS && loadSaveMe[VEGETATION_PATHWAYS.ordinal()]) {
          if ((!isZone && name.startsWith(PATHWAYS_ENTRY)) ||
              ((name.startsWith(PATHWAYS_ENTRY) && !zone.isHistoric()) ||
               (name.startsWith(HISTORIC_PATHWAYS_ENTRY) && zone.isHistoric()))) {
            Simpplle.setStatusMessage(msg);
            lastPathwayLoaded = zone.loadPathway(fin);
            lastPathwayLoaded.setIsUserData((!isZone));
          }
        }

        // These don't have loadSaveMe flags
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
          while (!line.contains("object-stream")) {
            line = fin.readLine();
          }
          StringBuffer strBuf = new StringBuffer(line);
          line="";
          while (!line.contains("object-stream")) {
            line = fin.readLine();
            strBuf.append(Simpplle.endl);
            strBuf.append(line);
          }
          strBuf.append(Simpplle.endl);
          StringReader strRead = new StringReader(strBuf.toString());
          XStream xs = new XStream(new DomDriver());
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
          setHasUserData(entryId, (!isZone));

          if (!isIndividualFile) { clearFile(entryId); }
        }


        jarEntry = jarIn.getNextJarEntry();
      }
//      if (d != null) { hr.close(); }
      Simpplle.clearStatusMessage();
    } catch (IOException err) {
      throw new SimpplleError("Could not read System Knowledge File",err);
    } catch (ClassNotFoundException err) {
      err.printStackTrace();
      throw new SimpplleError("Could not read System Knowledge File");
    } catch (ParseError err) {
      throw new SimpplleError("While reading System Knowledge File: " + name + "\n" + err.msg);
    } finally {
      Utility.close(fin);
      Utility.close(jarIn);
    }
  }

  /**
   * Saves an aquatic pathway to a file.
   *
   * @param file An output file
   * @param group An LTA valley segment group
   * @throws SimpplleError
   */
  public static void saveAquaticPathway(File file, LtaValleySegmentGroup group) throws SimpplleError {

    String fileExt = getKnowledgeFileExtension(AQUATIC_PATHWAYS);

    try {
      File outfile = Utility.makeSuffixedPathname(file, "", fileExt);
      JarOutputStream jarOut = new JarOutputStream(new FileOutputStream(outfile), new Manifest());
      PrintWriter pout = new PrintWriter(jarOut);

      JarEntry jarEntry = new JarEntry(PATHWAYS_ENTRY_AQUATIC + "/" + group.getName());
      jarOut.putNextEntry(jarEntry);
      group.save(pout);
      group.setFilename(outfile);
      group.setIsUserData(true);

      pout.flush();
      pout.close();
      jarOut.close();
    } catch (IOException ex) {
      throw new SimpplleError("Problems writing system knowledge file");
    }
  }

  /**
   * Saves a vegetative pathway to a file.
   *
   * @param file An output file
   * @param group A habitat type group
   * @throws SimpplleError
   */
  public static void saveVegetativePathway(File file, HabitatTypeGroup group) throws SimpplleError {

    String fileExt = getKnowledgeFileExtension(VEGETATION_PATHWAYS);

    try {
      File outfile = Utility.makeSuffixedPathname(file, "", fileExt);
      JarOutputStream jarOut = new JarOutputStream(new FileOutputStream(outfile), new Manifest());
      PrintWriter pout = new PrintWriter(jarOut);

      JarEntry jarEntry = new JarEntry(PATHWAYS_ENTRY + "/" + group.getName());
      jarOut.putNextEntry(jarEntry);
      group.save(pout);
      group.setFilename(outfile);
      group.setIsUserData(true);

      pout.flush();
      pout.close();
      jarOut.close();
    } catch (IOException ex) {
      throw new SimpplleError("Problems writing system knowledge file");
    }
  }

  /**
   * Saves zone knowledge to a file.
   *
   * @param file An output file
   * @throws SimpplleError
   */
  public static void saveZoneKnowledge(File file) throws SimpplleError {
    saveKnowledge(file,"simpplle_zone",true);
  }

  /**
   * Saves user knowledge to a file.
   *
   * @param file An output file
   * @throws SimpplleError
   */
  public static void saveUserKnowledge(File file) throws SimpplleError {
    saveKnowledge(file,SYSKNOW_FILEEXT,false);
  }

  /**
   * Saves a single kind of user knowledge to a file.
   *
   * @param file An output file
   * @param kind A kind of knowledge
   * @throws SimpplleError
   */
  public static void saveUserKnowledge(File file, Kinds kind) throws SimpplleError {
    for (int i = 0; i < loadSaveMe.length; i++) {
      loadSaveMe[i] = false;
    }
    loadSaveMe[kind.ordinal()] = true;
    saveKnowledge(file,getKnowledgeFileExtension(kind),false);
    files[kind.ordinal()] = file;
  }

  /**
   * Saves a particular system knowledge file.
   *
   * @param file An output file
   * @param fileExt The file extension for the output file
   * @param doZoneDef A flag indicating if a legal description should be saved
   * @throws SimpplleError
   */
  private static void saveKnowledge(File file,
                                    String fileExt,
                                    boolean doZoneDef) throws SimpplleError {

    RegionalZone zone = Simpplle.getCurrentZone();

    try {
      File outfile = Utility.makeSuffixedPathname(file, "", fileExt);
      JarOutputStream jarOut = new JarOutputStream(new FileOutputStream(outfile), new Manifest());
      PrintWriter pout = new PrintWriter(new OutputStreamWriter(jarOut));

      if (doZoneDef) {
        JarEntry jarEntry = new JarEntry("ZONE/LEGAL-DESCRIPTION.TXT");
        jarOut.putNextEntry(jarEntry);
        zone.writeZoneDefinitionFile(pout);
        for (int i = 0; i < loadSaveMe.length; i++) {
          loadSaveMe[i] = true;
        }
        pout.flush();
      }

      if (loadSaveMe[FMZ.ordinal()]) {
        JarEntry jarEntry = new JarEntry(FMZ_ENTRY);
        jarOut.putNextEntry(jarEntry);
        Fmz.save(pout);
        pout.flush();
      }

      TreatmentSchedule ts = Area.getTreatmentSchedule();
      if (ts != null && ts.hasApplications()) {
        JarEntry jarEntry = new JarEntry(TREATMENT_SCHEDULE_ENTRY);
        jarOut.putNextEntry(jarEntry);
        ts.save(pout);
        pout.flush();
      }

      if (loadSaveMe[TREATMENT_LOGIC.ordinal()]) {
        JarEntry jarEntry = new JarEntry(TREATMENT_LOGIC_ENTRY);
        jarOut.putNextEntry(jarEntry);
        Treatment.saveLogic(pout);
        pout.flush();
      }

      ProcessSchedule ps = Area.getProcessSchedule();
      if (ps != null && ps.getCurrentApplication() != null) {
        JarEntry jarEntry = new JarEntry(PROCESS_SCHEDULE_ENTRY);
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
          JarEntry jarEntry = new JarEntry(name);
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
          JarEntry jarEntry = new JarEntry(name);
          jarOut.putNextEntry(jarEntry);
          group.save(pout);
          group.setIsUserData(true);
          pout.flush();
        }
      }

      if (loadSaveMe[EXTREME_FIRE_DATA.ordinal()]) {
        JarEntry jarEntry = new JarEntry(EXTREME_FIRE_DATA_ENTRY);
        jarOut.putNextEntry(jarEntry);
        FireEvent.saveExtremeData(pout);
        pout.flush();
      }

      Climate climate = Simpplle.getClimate();
      if (climate != null && loadSaveMe[CLIMATE.ordinal()]) {
        JarEntry jarEntry = new JarEntry(CLIMATE_ENTRY);
        jarOut.putNextEntry(jarEntry);
        climate.save(pout);
        pout.flush();
      }

      if (loadSaveMe[FIRE_SEASON.ordinal()]) {
        JarEntry jarEntry = new JarEntry(FIRE_SEASON_ENTRY);
        jarOut.putNextEntry(jarEntry);
        FireEvent.saveFireSeasonData(pout);
        pout.flush();
      }

      if (loadSaveMe[KEANE_PARAMETERS.ordinal()]) {
        JarEntry jarEntry = new JarEntry(KEANE_PARAMETERS_ENTRY);
        jarOut.putNextEntry(jarEntry);
        ProcessOccurrenceSpreadingFire.saveKeaneParameters(pout);
        pout.flush();
      }

      if (loadSaveMe[SPECIES.ordinal()]) {
        JarEntry jarEntry = new JarEntry(SPECIES_ENTRY);
        jarOut.putNextEntry(jarEntry);
        SimpplleType.saveData(jarOut,SimpplleType.SPECIES);
      }

      if (loadSaveMe[CONIFER_ENCROACHMENT.ordinal()]) {
        JarEntry jarEntry = new JarEntry(CONIFER_ENCROACHMENT_ENTRY);
        jarOut.putNextEntry(jarEntry);
        ConiferEncroachmentLogicData.save(jarOut);
      }

      // XStream needs to be setup last as it writes stuff to the stream immediately upon creation.
      XStream xs;
      ObjectOutputStream os;

      // ** XStream Files ***
      // ********************
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
          JarEntry jarEntry = new JarEntry(FIRE_SUPP_WEATHER_BEYOND_CLASS_A_ENTRY);
          jarOut.putNextEntry(jarEntry);
          FireSuppWeatherData.save(os);
          os.flush();
          hw.endNode();
          headerWritten = false;
        }
        if (loadSaveMe[FIRE_SPREAD_LOGIC.ordinal()]) {
          if (!headerWritten) { hw.startNode(rootNodeName); }
          JarEntry jarEntry = new JarEntry(FIRE_SPREAD_LOGIC_ENTRY);
          jarOut.putNextEntry(jarEntry);
          FireEventLogic.save(FireEventLogic.SPREAD_STR,os);
          os.flush();
          hw.endNode();
          headerWritten = false;
        }
        if (loadSaveMe[FIRE_TYPE_LOGIC.ordinal()]) {
          if (!headerWritten) { hw.startNode(rootNodeName); }
          JarEntry jarEntry = new JarEntry(FIRE_TYPE_LOGIC_ENTRY);
          jarOut.putNextEntry(jarEntry);
          FireEventLogic.save(FireEventLogic.TYPE_STR,os);
          os.flush();
          hw.endNode();
          headerWritten = false;
        }
        if (loadSaveMe[FIRE_SPOTTING_LOGIC.ordinal()]) {
          if (!headerWritten) { hw.startNode(rootNodeName); }
          JarEntry jarEntry = new JarEntry(FIRE_SPOTTING_LOGIC_ENTRY);
          jarOut.putNextEntry(jarEntry);
          FireEventLogic.save(FireEventLogic.FIRE_SPOTTING_STR,os);
          os.flush();
          hw.endNode();
          headerWritten = false;
        }
        if (loadSaveMe[REGEN_LOGIC_FIRE.ordinal()] &&
            RegenerationLogic.isDataPresent(RegenerationLogic.FIRE)) {
          if (!headerWritten) { hw.startNode(rootNodeName); }
          JarEntry jarEntry = new JarEntry(REGEN_LOGIC_FIRE_ENTRY);
          jarOut.putNextEntry(jarEntry);
          RegenerationLogic.saveFire(os);
          os.flush();
          hw.endNode();
          headerWritten = false;
        }
        if (loadSaveMe[REGEN_LOGIC_SUCC.ordinal()] &&
            RegenerationLogic.isDataPresent(RegenerationLogic.SUCCESSION)) {
          if (!headerWritten) { hw.startNode(rootNodeName); }
          JarEntry jarEntry = new JarEntry(REGEN_LOGIC_SUCC_ENTRY);
          jarOut.putNextEntry(jarEntry);
          RegenerationLogic.saveSuccession(os);
          os.flush();
          hw.endNode();
          headerWritten = false;
        }
        if (loadSaveMe[PROCESS_PROB_LOGIC.ordinal()]) {
          if (!headerWritten) { hw.startNode(rootNodeName); }
          JarEntry jarEntry = new JarEntry(PROCESS_PROB_LOGIC_ENTRY);
          jarOut.putNextEntry(jarEntry);
          ProcessProbLogic.getInstance().save(os);
          os.flush();
          hw.endNode();
          headerWritten = false;
        }
        if (loadSaveMe[INVASIVE_SPECIES_LOGIC.ordinal()]) {
          if (!headerWritten) { hw.startNode(rootNodeName); }
          JarEntry jarEntry = new JarEntry(INVASIVE_SPECIES_LOGIC_ENTRY);
          jarOut.putNextEntry(jarEntry);
          InvasiveSpeciesLogic.getInstance().save(os);
          os.flush();
          hw.endNode();
          headerWritten = false;
        }
        if (loadSaveMe[INVASIVE_SPECIES_LOGIC_MSU.ordinal()]) {
          if (!headerWritten) { hw.startNode(rootNodeName); }
          JarEntry jarEntry = new JarEntry(INVASIVE_SPECIES_LOGIC_MSU_ENTRY);
          jarOut.putNextEntry(jarEntry);
          InvasiveSpeciesLogicMSU.getInstance().save(os);
          os.flush();
          hw.endNode();
          headerWritten = false;
        }
        if (loadSaveMe[REGEN_DELAY_LOGIC.ordinal()]) {
          if (!headerWritten) { hw.startNode(rootNodeName); }
          JarEntry jarEntry = new JarEntry(REGEN_DELAY_LOGIC_ENTRY);
          jarOut.putNextEntry(jarEntry);
          RegenerationDelayLogic.getInstance().save(os);
          os.flush();
          hw.endNode();
          headerWritten = false;
        }
        if (loadSaveMe[GAP_PROCESS_LOGIC.ordinal()]) {
          if (!headerWritten) { hw.startNode(rootNodeName); }
          JarEntry jarEntry = new JarEntry(GAP_PROCESS_LOGIC_ENTRY);
          jarOut.putNextEntry(jarEntry);
          GapProcessLogic.getInstance().save(os);
          os.flush();
          hw.endNode();
          headerWritten = false;
        }
        if (loadSaveMe[DOCOMPETITION_LOGIC.ordinal()]) {
          if (!headerWritten) { hw.startNode(rootNodeName); }
          JarEntry jarEntry = new JarEntry(DOCOMPETITION_LOGIC_ENTRY);
          jarOut.putNextEntry(jarEntry);
          DoCompetitionLogic.getInstance().save(os);
          os.flush();
          hw.endNode();
          headerWritten = false;
        }
        if (loadSaveMe[EVU_SEARCH_LOGIC.ordinal()]) {
          if (!headerWritten) { hw.startNode(rootNodeName); }
          JarEntry jarEntry = new JarEntry(EVU_SEARCH_LOGIC_ENTRY);
          jarOut.putNextEntry(jarEntry);
          EvuSearchLogic.getInstance().save(os);
          os.flush();
          hw.endNode();
          headerWritten = false;
        }
        if (loadSaveMe[PRODUCING_SEED_LOGIC.ordinal()]) {
          if (!headerWritten) { hw.startNode(rootNodeName); }
          JarEntry jarEntry = new JarEntry(PRODUCING_SEED_LOGIC_ENTRY);
          jarOut.putNextEntry(jarEntry);
          ProducingSeedLogic.getInstance().save(os);
          os.flush();
          hw.endNode();
          headerWritten = false;
        }
        if (loadSaveMe[VEG_UNIT_FIRE_TYPE_LOGIC.ordinal()]) {
          if (!headerWritten) { hw.startNode(rootNodeName); }
          JarEntry jarEntry = new JarEntry(VEG_UNIT_FIRE_TYPE_LOGIC_ENTRY);
          jarOut.putNextEntry(jarEntry);
          VegUnitFireTypeLogic.getInstance().save(os);
          os.flush();
          hw.endNode();
          headerWritten = false;
        }
        if (loadSaveMe[FIRE_SUPP_CLASS_A_LOGIC.ordinal()]) {
          if (!headerWritten) { hw.startNode(rootNodeName); }
          JarEntry jarEntry = new JarEntry(FIRE_SUPP_CLASS_A_LOGIC_ENTRY);
          jarOut.putNextEntry(jarEntry);
          FireSuppClassALogic.getInstance().save(os);
          os.flush();
          hw.endNode();
          headerWritten = false;
        }
        if (loadSaveMe[FIRE_SUPP_BEYOND_CLASS_A_LOGIC.ordinal()]) {
          if (!headerWritten) { hw.startNode(rootNodeName); }
          JarEntry jarEntry = new JarEntry(FIRE_SUPP_BEYOND_CLASS_A_LOGIC_ENTRY);
          jarOut.putNextEntry(jarEntry);
          FireSuppBeyondClassALogic.getInstance().save(os);
          os.flush();
          hw.endNode();
          headerWritten = false;
        }
        if (loadSaveMe[FIRE_SUPP_PRODUCTION_RATE_LOGIC.ordinal()]) {
          if (!headerWritten) { hw.startNode(rootNodeName); }
          JarEntry jarEntry = new JarEntry(FIRE_SUPP_PRODUCTION_RATE_LOGIC_ENTRY);
          jarOut.putNextEntry(jarEntry);
          FireSuppProductionRateLogic.getInstance().save(os);
          os.flush();
          hw.endNode();
          headerWritten = false;
        }
        if (loadSaveMe[FIRE_SUPP_SPREAD_RATE_LOGIC.ordinal()]) {
          if (!headerWritten) { hw.startNode(rootNodeName); }
          JarEntry jarEntry = new JarEntry(FIRE_SUPP_SPREAD_RATE_LOGIC_ENTRY);
          jarOut.putNextEntry(jarEntry);
          FireSuppSpreadRateLogic.getInstance().save(os);
          os.flush();
          hw.endNode();
          headerWritten = false;
        }
        if (loadSaveMe[FIRE_SUPP_WEATHER_CLASS_A_LOGIC.ordinal()]) {
          if (!headerWritten) { hw.startNode(rootNodeName); }
          JarEntry jarEntry = new JarEntry(FIRE_SUPP_WEATHER_CLASS_A_LOGIC_ENTRY);
          jarOut.putNextEntry(jarEntry);
          FireSuppWeatherClassALogic.getInstance().save(os);
          os.flush();
          hw.endNode();
          headerWritten = false;
        }
        if (loadSaveMe[TRACKING_SPECIES_REPORT.ordinal()]) {
          if (!headerWritten) { hw.startNode(rootNodeName); }
          JarEntry jarEntry = new JarEntry(TRACKING_SPECIES_REPORT_ENTRY);
          jarOut.putNextEntry(jarEntry);
          TrackingSpeciesReportData.getInstance().save(os);
          os.flush();
          hw.endNode();
          headerWritten = false;
        }
        if (loadSaveMe[FIRE_SUPP_EVENT_LOGIC.ordinal()]) {
          if (!headerWritten) { hw.startNode(rootNodeName); }
          JarEntry jarEntry = new JarEntry(FIRE_SUPP_EVENT_LOGIC_ENTRY);
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

    } catch (IOException err) {
      throw new SimpplleError("Problems writing system knowledge file");
    }
  }

  // ********************************
  // *** Knowledge Source Methods ***
  // ********************************

  /**
   * Returns the source information for a kind of knowledge.
   *
   * @param kind A kind of knowledge
   * @return Source information
   */
  public static String getSource(Kinds kind) {

    if (kind == WILDLIFE) {
      return "Documented in draft GTR, Carattia, Chew and Samson";
    }

    int zoneId = Simpplle.getCurrentZone().getId();

    switch(zoneId) {
      case ValidZones.WESTSIDE_REGION_ONE:
        return getWestsideRegionOneSource(kind);
      case ValidZones.EASTSIDE_REGION_ONE:
        return getEastsideRegionOneSource(kind);
      case ValidZones.TETON:
        return getTetonSource(kind);
      case ValidZones.NORTHERN_CENTRAL_ROCKIES:
        return getNorthernCentralRockiesSource(kind);
      case ValidZones.COLORADO_PLATEAU:
        return getColoradoPlateauSource(kind);
      default:
        return "No Knowledge Source Data Available at this time.";

    }
  }

  /**
   * Returns the source information for knowledge in Westside Region 1.
   *
   * @param kind A kind of knowledge
   * @return Source information
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
   * Returns the source information for knowledge in Eastside Region 1.
   *
   * @param kind A kind of knowledge
   * @return Source information
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
   * Returns the source information for knowledge in the Colorado Plateau.
   *
   * @param kind A kind of knowledge
   * @return Source information
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
   * Returns the source information for knowledge in Teton.
   *
   * @param kind A kind of knowledge
   * @return Source information
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
   * Returns the source information for knowledge in the Northern Central Rockies.
   *
   * @param kind A kind of knowledge
   * @return Source information
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

  /**
   * Returns source information for a kind of knowledge
   *
   * @param kind A kind of knowledge
   * @return Source information
   */
  public static String getKnowledgeSource(Kinds kind) {
    return knowledgeSource[kind.ordinal()];
  }

  /**
   * Sets source information for a kind of knowledge
   *
   * @param kind A kind of knowledge
   * @param source Source information
   */
  public static void setKnowledgeSource(Kinds kind, String source)  {
    knowledgeSource[kind.ordinal()] = source;
  }

  /**
   * Writes knowledge source information.
   *
   * @param writer A print writer
   * @param source Source information
   */
  public static void writeKnowledgeSource(PrintWriter writer, String source) {
    if (source != null && source.length() > 0) {
      writer.print(KNOWLEDGE_SOURCE_KEYWORD);
      writer.println(" dummy");
      writer.println(source);
      writer.println("END-KNOWLEDGE-SOURCE");
    }
  }

  /**
   * Writes knowledge source information for a kind of knowledge.
   *
   * @param writer A print writer
   * @param kind A kind of knowledge
   */
  public static void writeKnowledgeSource(PrintWriter writer, Kinds kind) {
    writeKnowledgeSource(writer,knowledgeSource[kind.ordinal()]);
  }

  /**
   * Reads knowledge source information for a kind of knowledge.
   *
   * @param fin A buffered reader containing source information
   * @param kind A kind of knowledge
   * @return Source information
   * @throws IOException
   */
  public static String readKnowledgeSource(BufferedReader fin, Kinds kind) throws IOException {
    StringBuffer strBuf = new StringBuffer("");
    String line = fin.readLine();
    String nl = System.getProperty("line.separator");
    while (line != null && !line.equals("END-KNOWLEDGE-SOURCE")) {
      strBuf.append(line);
      strBuf.append(nl);
      line = fin.readLine();
    }
    if (kind != VEGETATION_PATHWAYS && kind != AQUATIC_PATHWAYS) {
      setKnowledgeSource(kind,strBuf.toString());
    } else {
      return strBuf.toString();
    }
    return getKnowledgeSource(kind);
  }

  /**
   * Sets aliases for classes serialized with the XStream library.
   *
   * @param xs An instance of the XStream library
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
