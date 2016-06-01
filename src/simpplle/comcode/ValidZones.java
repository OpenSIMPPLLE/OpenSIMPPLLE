package simpplle.comcode;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 * <p>
 * This interface contains constants to make and identifying the type of current zone used in several classes.
 * It contains all those zones currently supported in OpenSimpplle.
 * !!! Warning:  A change in these values will affect treatments. !!!
 *
 * Note** these have not been changed but a skip of Michigan and Zone Builder was built into the Main in GUI to jump over the two now deleted zones.
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 * 
 */

public interface ValidZones {

  public static final int WESTSIDE_REGION_ONE         = 0;
  public static final int EASTSIDE_REGION_ONE         = 1;
  public static final int SIERRA_NEVADA               = 2;
  public static final int SOUTHERN_CALIFORNIA         = 3;
  public static final int GILA                        = 4;
  public static final int SOUTH_CENTRAL_ALASKA        = 5;
  public static final int SOUTHWEST_UTAH              = 6;
  public static final int COLORADO_FRONT_RANGE        = 7;
  public static final int MICHIGAN                    = 8;
  public static final int ZONE_BUILDER                = 9;
  public static final int WESTERN_GREAT_PLAINS_STEPPE = 10;
  public static final int GREAT_PLAINS_STEPPE         = 11;
  public static final int MIXED_GRASS_PRAIRIE         = 12;
  public static final int COLORADO_PLATEAU            = 13;
  public static final int TETON                       = 14;
  public static final int NORTHERN_CENTRAL_ROCKIES    = 15;
  public static final int GRASSLAND                   = 16;

}


