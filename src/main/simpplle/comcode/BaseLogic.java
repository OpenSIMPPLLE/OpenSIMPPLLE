package simpplle.comcode;


/**
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This is an abstract class which contains the enumeration for columns and a string array containing they System Knowledge kinds. 
 * Columns are built by adding to the calling the AbstractBaseLogic variable LAST_COL.  
 * By default this is set to the first column if there is not already one in there.
 * The columns for Base Logic are ROW_COL,ECO_GROUP_COL,SPECIES_COL,SIZE_CLASS_COL,DENSITY_COL,PROCESS_COL,
 * TREATMENT_COL,SEASON_COL,MOISTURE_COL,TEMP_COL,TRACKING_SPECIES_COL,
 * OWNERSHIP_COL, SPECIAL_AREA_COL, ROAD_STATUS_COL, TRAIL_STATUS_COL, LANDTYPE_COL
 *
 * @author Documentation by Brian Losi
 * <p>Original source authorship: Kirk A. Moeller
 * 
 *
 * @see simpplle.comcode.AbstractBaseLogic
 * 
 */

public abstract class BaseLogic extends AbstractBaseLogic {
  public enum Columns {
    ROW_COL,ECO_GROUP_COL,SPECIES_COL,SIZE_CLASS_COL,DENSITY_COL,PROCESS_COL,
    TREATMENT_COL,SEASON_COL,MOISTURE_COL,TEMP_COL,TRACKING_SPECIES_COL,
    OWNERSHIP_COL, SPECIAL_AREA_COL, ROAD_STATUS_COL, TRAIL_STATUS_COL, LANDTYPE_COL
  }

  public static final int ECO_GROUP_COL        = AbstractBaseLogic.LAST_COL+1;
  public static final int SPECIES_COL          = AbstractBaseLogic.LAST_COL+2;
  public static final int SIZE_CLASS_COL       = AbstractBaseLogic.LAST_COL+3;
  public static final int DENSITY_COL          = AbstractBaseLogic.LAST_COL+4;
  public static final int PROCESS_COL          = AbstractBaseLogic.LAST_COL+5;
  public static final int TREATMENT_COL        = AbstractBaseLogic.LAST_COL+6;
  public static final int SEASON_COL           = AbstractBaseLogic.LAST_COL+7;
  public static final int MOISTURE_COL         = AbstractBaseLogic.LAST_COL+8;
  public static final int TEMP_COL             = AbstractBaseLogic.LAST_COL+9;
  public static final int TRACKING_SPECIES_COL = AbstractBaseLogic.LAST_COL+10;
  public static final int OWNERSHIP_COL        = AbstractBaseLogic.LAST_COL+11;
  public static final int SPECIAL_AREA_COL     = AbstractBaseLogic.LAST_COL+12;
  public static final int ROAD_STATUS_COL      = AbstractBaseLogic.LAST_COL+13;
  public static final int TRAIL_STATUS_COL     = AbstractBaseLogic.LAST_COL+14;
  public static final int LANDTYPE_COL         = AbstractBaseLogic.LAST_COL+15;
  
  protected static final int LAST_COL = LANDTYPE_COL;
/**
 * These form the basic system knowledge processes.  These are placed into a hash map which will be keyed by their string name.  
 * @param kinds
 */
  protected BaseLogic(String[] kinds) {
    super(kinds);
    for (int i=0; i<kinds.length; i++) {
      Process process = Process.findInstance(kinds[i]);
      if (process != null && process.isUniqueUI()) { continue; }

      addColumn(kinds[i],"ECO_GROUP_COL");
      addColumn(kinds[i],"SPECIES_COL");
      addColumn(kinds[i],"SIZE_CLASS_COL");
      addColumn(kinds[i],"DENSITY_COL");
      addColumn(kinds[i],"PROCESS_COL");
      addColumn(kinds[i],"TREATMENT_COL");
      addColumn(kinds[i],"SEASON_COL");
      addColumn(kinds[i],"MOISTURE_COL");
      addColumn(kinds[i],"TEMP_COL");
      addColumn(kinds[i],"TRACKING_SPECIES_COL");
      addColumn(kinds[i],"OWNERSHIP_COL");
      addColumn(kinds[i],"SPECIAL_AREA_COL");
      addColumn(kinds[i],"ROAD_STATUS_COL");
      addColumn(kinds[i],"TRAIL_STATUS_COL");
      addColumn(kinds[i],"LANDTYPE_COL");
    }

  }

  /**
   * method to get formatted string version of to be displayed in user interactions and reports 
   * @param col ordinal into the column enumeration
   * @return name of column "Eco Group", "Species","Size Classes", "Densities", "Processes", "Treatments", "Season",  "Moisture", "Temp",
   *  "Tracking Species", "Ownership", "Special Area", "Roads", "Trails", "Land Type" or "Priority"(for ROW_COL)
   */
  public static String getColumnName(int col) {
    switch (col) {
      case ECO_GROUP_COL:        return "Eco Group";
      case SPECIES_COL:          return "Species";
      case SIZE_CLASS_COL:       return "Size Classes";
      case DENSITY_COL:          return "Densities";
      case PROCESS_COL:          return "Processes";
      case TREATMENT_COL:        return "Treatments";
      case SEASON_COL:           return "Season";
      case MOISTURE_COL:         return "Moisture";
      case TEMP_COL:             return "Temp";
      case TRACKING_SPECIES_COL: return "Tracking Species";
      case OWNERSHIP_COL:        return "Ownership";
      case SPECIAL_AREA_COL:     return "Special Area";
      case ROAD_STATUS_COL:      return "Roads";
      case TRAIL_STATUS_COL:     return "Trails";
      case LANDTYPE_COL:         return "Land Type";
      default:
        return AbstractBaseLogic.getColumnName(col);
    }
  }
  /**
   * Returns the static final int of column num from the column name 
   * ROW_COL =0,ECO_GROUP_COL=1,SPECIES_COL=2,SIZE_CLASS_COL=3,DENSITY_COL=4,PROCESS_COL=5,
   * TREATMENT_COL=6,SEASON_COL=7,MOISTURE_COL=8,TEMP_COL=9,TRACKING_SPECIES_COL=10,
   * OWNERSHIP_COL=11, SPECIAL_AREA_COL=12, ROAD_STATUS_COL=13, TRAIL_STATUS_COL=14, LANDTYPE_COL=15
   */

  public int getColumnNumFromName(String name) {
    if (name.equalsIgnoreCase("Eco Group")) {
      return ECO_GROUP_COL;
    }
    else if (name.equalsIgnoreCase("Species")) {
      return SPECIES_COL;
    }
    else if (name.equalsIgnoreCase("Size Classes")) {
      return SIZE_CLASS_COL;
    }
    else if (name.equalsIgnoreCase("Densities")) {
      return DENSITY_COL;
    }
    else if (name.equalsIgnoreCase("Processes")) {
      return PROCESS_COL;
    }
    else if (name.equalsIgnoreCase("Treatments")) {
      return TREATMENT_COL;
    }
    else if (name.equalsIgnoreCase("Season")) {
      return SEASON_COL;
    }
    else if (name.equalsIgnoreCase("Moisture")) {
      return MOISTURE_COL;
    }
    else if (name.equalsIgnoreCase("Temp")) {
      return TEMP_COL;
    }
    else if (name.equalsIgnoreCase("Tracking Species")) {
      return TRACKING_SPECIES_COL;
    }
    else if (name.equalsIgnoreCase("Ownership")) {
      return OWNERSHIP_COL;
    }
    else if (name.equalsIgnoreCase("Special Area")) {
      return SPECIAL_AREA_COL;
    }
    else if (name.equalsIgnoreCase("Roads")) {
      return ROAD_STATUS_COL;
    }
    else if (name.equalsIgnoreCase("Trails")) {
      return TRAIL_STATUS_COL;
    }
    else if (name.equalsIgnoreCase("Land Type")) {
      return LANDTYPE_COL;
    }
    else {
      return super.getColumnNumFromName(name);
    }
  }

}
