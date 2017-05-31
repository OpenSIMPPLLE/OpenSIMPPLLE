/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.io.*;
import java.util.zip.GZIPInputStream;

/**
 * This class is the Legacy fire spread data.  Will probably be deprecated in OpenSimpplle v1.0
 *  
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public abstract class FireSpreadDataLegacy {
  private static boolean extreme;

  // Fire Type (for Spread)
  public static final int LMSF_CLASS = FireEvent.LMSF_CLASS;
  public static final int SRF_CLASS  = FireEvent.SRF_CLASS;

  // Relative Position
  public static final int A  = FireEvent.A;
  public static final int BN = FireEvent.BN;

  // Density Grouping
  public static final int LOW_DENSITY  = FireEvent.LOW_DENSITY;
  public static final int HIGH_DENSITY = FireEvent.HIGH_DENSITY;

  // Resistance.
  public static final int LOW      = FireEvent.LOW;
  public static final int MODERATE = FireEvent.MODERATE;
  public static final int HIGH     = FireEvent.HIGH;

  // Ownership
  public static final int NF_WILDERNESS = FireEvent.NF_WILDERNESS;
  public static final int NF_OTHER      = FireEvent.NF_OTHER;
  public static final int OTHER         = FireEvent.OTHER;

  public static final Structure NON_FOREST     = Structure.NON_FOREST;
  public static final Structure SINGLE_STORY   = Structure.SINGLE_STORY;
  public static final Structure MULTIPLE_STORY = Structure.MULTIPLE_STORY;

  private static final int NUM_SPREAD_TYPE     = 2;
  private static final int NUM_SPREAD_POSITION = 2;
  private static final int NUM_SPREAD_DENSITY  = 2;

  private static final int NUM_STRUCTURE      = 3;
  private static final int NUM_RESISTANCE     = 3;
  private static final int NUM_FIRETYPE_PAGES = 3;

  // fields are:
  //  FireType,Position,Density,resistance,structure,page,row,col
  private static ProcessType[][][][][][][][] spreadData;

  private static ProcessType getSpreadData(int fireType, int position, int density,
                                  int resistance, Structure structure,
                                  int page, int row, int col) {
    return spreadData[fireType][position][density][resistance][structure.ordinal()][page][row][col];
  }
  public static ProcessType getData(int fireClass, int position,
                                               int resistance,
                                               SizeClass sizeClass,
                                               Density density,
                                               ProcessType process,
                                               boolean extremeEvent) {
    extreme = extremeEvent;
    return getFireSpread(fireClass,position,resistance,sizeClass,density,process);
  }


//  private static void setSpreadData(String fireTypeStr, String positionStr,
//                                         String densityStr, String sizeKind,
//                                         String resistanceStr, int page,
//                                         int row, int col, ProcessType processType) {
//    int          fireType = LMSF_CLASS;
//    int          position = A;
//    int          density  = LOW_DENSITY;
//    int          resistance=LOW;
//    int          structure=NON_FOREST;
//    RegionalZone zone = Simpplle.getCurrentZone();
//
//    if (fireTypeStr.equals("SRF")) { fireType = SRF_CLASS; }
//    else { fireType = LMSF_CLASS; }
//
//    if (positionStr.equals("A")) { position = A; }
//    else { position = BN; }
//
//    if (densityStr.equals("LOW")) { density = LOW_DENSITY; }
//    else { density = HIGH_DENSITY; }
//
//    if (resistanceStr.equals("LOW")) { resistance = LOW; }
//    else if (resistanceStr.equals("MODERATE")) { resistance = MODERATE; }
//    else if (resistanceStr.equals("HIGH"))     { resistance = HIGH; }
//
//    if (sizeKind.equals("NF"))      { structure = NON_FOREST; }
//    else if (sizeKind.equals("SS")) { structure = SINGLE_STORY; }
//    else if (sizeKind.equals("MS"))  { structure = MULTIPLE_STORY; }
//
//
//    setSpreadData(fireType,position,density,resistance,structure,page,row,col,
//                  processType);
//  }

  private static void setSpreadData(int fireType, int position, int density,
                                    int resistance, int structure,
                                    int page, int row, int col, ProcessType value) {
    if (spreadData[fireType][position][density][resistance][structure][page][row][col] == value) {
      return;
    }
    spreadData[fireType][position][density][resistance][structure][page][row][col] = value;
  }

  /**
    * Load a user provided Fire Spread data file.
    * @param path is a File, the pathname of the data file.
    * @return a boolean, true if file was successfully loaded.
    */
  public static void loadData(File path) throws SimpplleError {
    GZIPInputStream gzip_in;
    BufferedReader  fin;

    try {
      gzip_in = new GZIPInputStream(new FileInputStream(path));
      fin = new BufferedReader(new InputStreamReader(gzip_in));

      readData(fin);
      fin.close();
      gzip_in.close();
    }
    catch (IOException e) {
      String msg = "Problems reading from Fire Spread data file:" + path;
      System.out.println(msg);
      throw new SimpplleError(msg);
    }
  }

  public static void loadData(InputStream is) throws SimpplleError {
    GZIPInputStream gzip_in;
    BufferedReader fin;

    try {
      gzip_in = new GZIPInputStream(is);
      fin = new BufferedReader(new InputStreamReader(gzip_in));
      readData(fin);
      // *** Important ***
      // DO NOT CLOSE THESE STREAMS.
      // IT WILL CAUSE READING FROM JAR FILES TO FAIL.
    }
    catch (IOException e) {
      String msg = "Problems reading from Fire Spread data file\n" +
                   "knowledge.jar file may be missing or damaged.";
      System.out.println(msg);
      throw new SimpplleError(msg);
    }
  }

  // Fire Spread data is organized into:
  //      resistance, structure, pages, rows, and columns;
  // The first number in the file is the number of resistance groups.
  // The second number is the number structure groups
  // The third number in the file (on line 3) is the number of pages
  //   in the following group (i.e. the number of lines)
  // Each group is proceeded by a line specifying the number of lines
  //   that follow.
  // Lines in the input file are the pages.
  // Each line has the rows comma delimited.
  // Each row is colon delimited and usually has two values.
  // The two values are average and extreme fire types.
  public static void readData(BufferedReader fin) throws SimpplleError {
    String              line, str;
    StringTokenizerPlus strTok, pageTok, rowTok, valueTok;
    int                 numPages, numRows, numCols;
    int                 resist, structure, page, row, col;
    int                 fireType, position, density;
    ProcessType         processType;
    RegionalZone        zone = Simpplle.getCurrentZone();

    try {
      spreadData
        = new ProcessType[NUM_SPREAD_TYPE][NUM_SPREAD_POSITION][NUM_SPREAD_DENSITY][NUM_RESISTANCE][NUM_STRUCTURE][][][];

      for(resist=0;resist<NUM_RESISTANCE;resist++) {
        for(structure=0;structure<NUM_STRUCTURE;structure++) {
          line = fin.readLine(); // Eat Comment
          if (line == null) {
            throw new ParseError("Fire Spread Data file is empty.");
          }

          for(fireType=0; fireType<NUM_SPREAD_TYPE; fireType++) {
            for(position=0; position<NUM_SPREAD_POSITION; position++) {
              for(density=0; density<NUM_SPREAD_DENSITY; density++) {
                line     = fin.readLine();
                pageTok   = new StringTokenizerPlus(line,";");
                numPages = pageTok.countTokens();
                spreadData[fireType][position][density][resist][structure] =
                  new ProcessType[numPages][][];

                for(page=0;page<numPages;page++) {
                  rowTok  = new StringTokenizerPlus(pageTok.nextToken(),",");
                  numRows = rowTok.countTokens();
                  spreadData[fireType][position][density][resist][structure][page] = new ProcessType[numRows][];

                  for(row=0;row<numRows;row++) {
                    valueTok = new StringTokenizerPlus(rowTok.getToken(),":");
                    numCols = valueTok.countTokens();
                    spreadData[fireType][position][density][resist][structure][page][row] = new ProcessType[numCols];
                    for(col=0;col<numCols;col++) {
                      processType = ProcessType.get(valueTok.getToken());
                      if (processType.equals(ProcessType.NIL)) {
                        processType = ProcessType.NONE;
                      }
                      setSpreadData(fireType,position,density,resist,structure,page,row,col,processType);
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    catch (NumberFormatException nfe) {
      String msg = "Invalid value found in fire spread data file.";
      System.out.println(msg);
      throw new SimpplleError(msg);
    }
    catch (ParseError pe) {
      System.out.println(pe.msg);
      throw new SimpplleError(pe.msg);
    }
    catch (IOException e) {
      String msg = "Problems reading from fire spread data file.";
      System.out.println(msg);
      throw new SimpplleError(msg);
    }
  }

  private static ProcessType getFireSpread(int fireClass, int position,
                                          int resistance,
                                          SizeClass sizeClass,
                                          Density density,
                                          ProcessType process)
  {
    int zoneId = Simpplle.getCurrentZone().getId();

    if (zoneId == ValidZones.SOUTH_CENTRAL_ALASKA) {
      return getFireSpreadAlaska(fireClass,position,resistance,sizeClass,density,process);
    }
    else if (zoneId == ValidZones.SIERRA_NEVADA ||
             zoneId == ValidZones.SOUTHERN_CALIFORNIA) {
      return getFireSpreadCalifornia(fireClass, position, resistance, sizeClass,
                                     density, process);
    }
    else {
      return getFireSpreadCommon(fireClass,position,resistance,sizeClass,density,process);
    }
  }

  private static ProcessType getFireSpreadCommon(int fireClass, int position,
                                               int resistance,
                                               SizeClass sizeClass,
                                               Density density,
                                               ProcessType pastProcessType)
  {
    Structure structure;
    int densityClass;
    int page, row, col = 0;

    if (density == Density.ONE || density == Density.TWO) { densityClass = LOW_DENSITY; }
    else if (density == Density.THREE || density == Density.FOUR) { densityClass = HIGH_DENSITY; }
    else { return ProcessType.NONE; }

    if (// Non Forest
        sizeClass == SizeClass.GRASS           || sizeClass == SizeClass.UNIFORM ||
        sizeClass == SizeClass.SCATTERED       || sizeClass == SizeClass.CLUMPED ||
        sizeClass == SizeClass.OPEN_HERB       || sizeClass == SizeClass.CLOSED_HERB  ||
        sizeClass == SizeClass.OPEN_LOW_SHRUB  || sizeClass == SizeClass.CLOSED_LOW_SHRUB ||
        sizeClass == SizeClass.OPEN_MID_SHRUB  || sizeClass == SizeClass.CLOSED_MID_SHRUB ||
        sizeClass == SizeClass.OPEN_TALL_SHRUB || sizeClass == SizeClass.CLOSED_TALL_SHRUB) {
      structure = NON_FOREST;
      page = 0;
    }
    // Single Story
    else if (sizeClass == SizeClass.SS) {
      structure = SINGLE_STORY;
      page = 0;
    }
    else if (sizeClass == SizeClass.POLE) {
      structure = SINGLE_STORY;
      page = 1;
    }
    else if (sizeClass == SizeClass.MEDIUM || sizeClass == SizeClass.LARGE ||
             sizeClass == SizeClass.VERY_LARGE) {
      structure = SINGLE_STORY;
      page = 2;
    }
    // Multiple Story
    else if (sizeClass == SizeClass.PTS || sizeClass == SizeClass.PMU  || sizeClass == SizeClass.MTS ||
             sizeClass == SizeClass.MMU || sizeClass == SizeClass.LMU  || sizeClass == SizeClass.MU  ||
             sizeClass == SizeClass.LTS || sizeClass == SizeClass.VLTS || sizeClass == SizeClass.VLMU) {
      structure = MULTIPLE_STORY;
      page = 0;
    }
    else {
      return ProcessType.NONE;
    }

    if (pastProcessType.equals(ProcessType.SUCCESSION)     ||
        pastProcessType.equals(ProcessType.LIGHT_LP_MPB)   ||
        pastProcessType.equals(ProcessType.LIGHT_WSBW)     ||
        pastProcessType.equals(ProcessType.PP_MPB)         ||
        pastProcessType.equals(ProcessType.WBP_MPB)        ||
        pastProcessType.equals(ProcessType.WINTER_DROUGHT) ||
        pastProcessType.equals(ProcessType.NONE)) {
      row = 0;
    }
    else if (pastProcessType.equals(ProcessType.SEVERE_LP_MPB) ||
             pastProcessType.equals(ProcessType.SEVERE_WSBW)   ||
             pastProcessType.equals(ProcessType.ROOT_DISEASE)  ||
             pastProcessType.equals(ProcessType.WINDTHROW)     ||
             pastProcessType.equals(ProcessType.BARK_BEETLES)  ||
             pastProcessType.equals(ProcessType.DF_BEETLE)     ||
             pastProcessType.equals(ProcessType.SPRUCE_BEETLE) ||
             pastProcessType.equals(ProcessType.WP_MPB)        ||
             pastProcessType.equals(ProcessType.COLD_INJURY_BARK_BEETLES)) {
      row = 1;
    }
    else if (pastProcessType.equals(ProcessType.LIGHT_SEVERITY_FIRE) ||
             pastProcessType.equals(ProcessType.MIXED_SEVERITY_FIRE)) {
      row = 2;
    }
    else {
      return ProcessType.NONE;
    }

    if (extreme) { col++; }

    return getSpreadData(fireClass,position,densityClass,
                                    resistance,structure,page,row,col);
  }

  private static ProcessType getFireSpreadCalifornia(int fireClass,
      int position,
      int resistance,
      SizeClass sizeClass,
      Density density,
      ProcessType pastProcessType)
  {
    Structure structure;
    int densityClass;
    int page, row=-1, col = 0;

    if (density == Density.ONE || density == Density.TWO) { densityClass = LOW_DENSITY; }
    else if (density == Density.THREE || density == Density.FOUR) { densityClass = HIGH_DENSITY; }
    else { return ProcessType.NONE; }

    if (sizeClass == SizeClass.GRASS           || sizeClass == SizeClass.OPEN_HERB        ||
        sizeClass == SizeClass.CLOSED_HERB     || sizeClass == SizeClass.UNIFORM          ||
        sizeClass == SizeClass.SCATTERED       || sizeClass == SizeClass.CLUMPED) {
      structure = NON_FOREST;
      page = 0;
    }
    else if (sizeClass == SizeClass.OPEN_LOW_SHRUB  || sizeClass == SizeClass.CLOSED_LOW_SHRUB ||
             sizeClass == SizeClass.OPEN_MID_SHRUB  || sizeClass == SizeClass.CLOSED_MID_SHRUB) {
      structure = NON_FOREST;
      page = 1;
    }
    else if (sizeClass == SizeClass.OPEN_TALL_SHRUB || sizeClass == SizeClass.CLOSED_TALL_SHRUB) {
      structure = NON_FOREST;
      page = 2;
    }
    else if (sizeClass == SizeClass.SS) {
      structure = SINGLE_STORY;
      page = 0;
    }
    else if (sizeClass == SizeClass.POLE) {
      structure = SINGLE_STORY;
      page = 1;
    }
    else if (sizeClass == SizeClass.MEDIUM ||
             sizeClass == SizeClass.LARGE  || sizeClass == SizeClass.VERY_LARGE) {
      structure = SINGLE_STORY;
      page = 2;
    }
    else if (sizeClass == SizeClass.PTS || sizeClass == SizeClass.PMU  ||
             sizeClass == SizeClass.MTS || sizeClass == SizeClass.MMU  ||
             sizeClass == SizeClass.LTS || sizeClass == SizeClass.LMU  ||
             sizeClass == SizeClass.MU  || sizeClass == SizeClass.VLMU ||
             sizeClass == SizeClass.VLTS) {
      structure = MULTIPLE_STORY;
      page = 0;
    }
    else {
      return ProcessType.NONE;
    }

    if (pastProcessType.equals(ProcessType.SUCCESSION)         ||
        pastProcessType.equals(ProcessType.LIGHT_BARK_BEETLES) ||
        pastProcessType.equals(ProcessType.BLISTER_RUST)  ||
        pastProcessType.equals(ProcessType.NONE)) {
      row = 0;
    }
    else if (pastProcessType.equals(ProcessType.SEVERE_BARK_BEETLES) ||
             pastProcessType.equals(ProcessType.ROOT_DISEASE) ||
             pastProcessType.equals(ProcessType.BB_RD_DM_COMPLEX) ||
             pastProcessType.equals(ProcessType.DROUGHT_MORTALITY)) {
      row = 1;
    }
    else if (pastProcessType.equals(ProcessType.LIGHT_SEVERITY_FIRE) ||
             pastProcessType.equals(ProcessType.MIXED_SEVERITY_FIRE)) {
      row = 2;
    }
    else {
      return ProcessType.NONE;
    }

    if (extreme) { col++; }

    return getSpreadData(fireClass, position, densityClass,
                         resistance, structure, page, row, col);
  }

  private static ProcessType getFireSpreadAlaska(int fireClass, int position,
                                               int resistance,
                                               SizeClass sizeClass,
                                               Density density,
                                               ProcessType pastProcessType)
  {
    Structure structure;
    int densityClass;
    int page, row=-1, col = 0;

    if (density == Density.W) { densityClass = LOW_DENSITY; }
    else if (density == Density.O || density == Density.C) { densityClass = HIGH_DENSITY; }
    else { return ProcessType.NONE; }

    if (// Non Forest
        sizeClass == SizeClass.HERB        || sizeClass == SizeClass.GH        ||
        sizeClass == SizeClass.TALL_SHRUB  || sizeClass == SizeClass.LOW_SHRUB ||
        sizeClass == SizeClass.DWARF_SHRUB || sizeClass == SizeClass.AQU) {
      structure = NON_FOREST;
      page = 0;
    }
    // Single Story
    else if (sizeClass == SizeClass.SS || sizeClass == SizeClass.SS_SS) {
      structure = SINGLE_STORY;
      page = 0;
    }
    else if (sizeClass == SizeClass.POLE || sizeClass == SizeClass.POLE_POLE) {
      structure = SINGLE_STORY;
      page = 1;
    }
    else if (sizeClass == SizeClass.LARGE || sizeClass == SizeClass.LARGE_LARGE) {
      structure = SINGLE_STORY;
      page = 2;
    }
    // Multiple Story
    else if (sizeClass == SizeClass.SS_POLE  || sizeClass == SizeClass.SS_LARGE   ||
             sizeClass == SizeClass.POLE_SS  || sizeClass == SizeClass.POLE_LARGE ||
             sizeClass == SizeClass.LARGE_SS || sizeClass == SizeClass.LARGE_POLE) {
      structure = MULTIPLE_STORY;
      page = 0;
    }
    else {
      return ProcessType.NONE;
    }

    switch (structure) {
      case NON_FOREST:
        if (pastProcessType.equals(ProcessType.SUCCESSION)) { row = 0; }
        break;
      case SINGLE_STORY:
        switch (page) {
          case 0:
            switch (resistance) {
              case LOW:
                if (pastProcessType.equals(ProcessType.SUCCESSION)) { row = 0; }
                break;
              case MODERATE:
              case HIGH:
                if (pastProcessType.equals(ProcessType.SUCCESSION)) {
                  row = 0;
                }
                else if (pastProcessType.equals(ProcessType.WILDLIFE_BROWSING)) {
                  row = 1;
                }
                else if (pastProcessType.equals(ProcessType.LIGHT_SEVERITY_FIRE) ||
                         pastProcessType.equals(ProcessType.MIXED_SEVERITY_FIRE)) {
                  row = 2;
                }
                break;
              default: return ProcessType.NONE;
            }
            break;
          case 1:
          case 2:
            switch (resistance) {
              case LOW:
                if (pastProcessType.equals(ProcessType.SUCCESSION)    ||
                    pastProcessType.equals(ProcessType.LOW_WINDTHROW) ||
                    pastProcessType.equals(ProcessType.MEDIUM_WINDTHROW)) {
                  row = 0;
                }
                else if (pastProcessType.equals(ProcessType.HIGH_WINDTHROW)) { row = 1; }
                break;
              case MODERATE:
                if (pastProcessType.equals(ProcessType.SUCCESSION)    ||
                    pastProcessType.equals(ProcessType.LOW_WINDTHROW) ||
                    pastProcessType.equals(ProcessType.MEDIUM_WINDTHROW)) {
                  row = 0;
                }
                else if (pastProcessType.equals(ProcessType.ROOT_DISEASE) ||
                         pastProcessType.equals(ProcessType.LIGHT_SB)     ||
                         pastProcessType.equals(ProcessType.HIGH_WINDTHROW)) {
                  row = 1;
                }
                else if (pastProcessType.equals(ProcessType.LIGHT_SEVERITY_FIRE) ||
                         pastProcessType.equals(ProcessType.MIXED_SEVERITY_FIRE)) {
                  row = 2;
                }
                break;
              case HIGH:
                if (pastProcessType.equals(ProcessType.SUCCESSION)) {
                  row = 0;
                }
                else if (pastProcessType.equals(ProcessType.LIGHT_SEVERITY_FIRE) ||
                         pastProcessType.equals(ProcessType.MIXED_SEVERITY_FIRE)) {
                  row = 1;
                }
                break;
              default: return ProcessType.NONE;
            }
            break;
          default: return ProcessType.NONE;
        }
        break;
      case MULTIPLE_STORY:
        switch (densityClass) {
          case LOW_DENSITY:
            switch (resistance) {
              case LOW:
                if (pastProcessType.equals(ProcessType.SUCCESSION)    ||
                    pastProcessType.equals(ProcessType.LOW_WINDTHROW) ||
                    pastProcessType.equals(ProcessType.MEDIUM_WINDTHROW)) {
                  row = 0;
                }
                else if (pastProcessType.equals(ProcessType.HIGH_WINDTHROW)) { row = 1; }
                break;
              case MODERATE:
                if (pastProcessType.equals(ProcessType.SUCCESSION)    ||
                    pastProcessType.equals(ProcessType.LOW_WINDTHROW) ||
                    pastProcessType.equals(ProcessType.MEDIUM_WINDTHROW)) {
                  row = 0;
                }
                else if (pastProcessType.equals(ProcessType.ROOT_DISEASE)   ||
                         pastProcessType.equals(ProcessType.LIGHT_SB)       ||
                         pastProcessType.equals(ProcessType.HIGH_WINDTHROW) ||
                         pastProcessType.equals(ProcessType.WILDLIFE_BROWSING)) {
                  row = 1;
                }
                else if (pastProcessType.equals(ProcessType.LIGHT_SEVERITY_FIRE) ||
                         pastProcessType.equals(ProcessType.MIXED_SEVERITY_FIRE)) {
                  row = 2;
                }
                break;
              case HIGH:
                if (pastProcessType.equals(ProcessType.SUCCESSION)) {
                  row = 0;
                }
                else if (pastProcessType.equals(ProcessType.WILDLIFE_BROWSING)) {
                  row = 1;
                }
                else if (pastProcessType.equals(ProcessType.LIGHT_SEVERITY_FIRE) ||
                         pastProcessType.equals(ProcessType.MIXED_SEVERITY_FIRE)) {
                  row = 2;
                }
                break;
              default: return ProcessType.NONE;
            }
            break;
          case HIGH_DENSITY:
            switch (resistance) {
              case LOW:
                if (pastProcessType.equals(ProcessType.SUCCESSION)    ||
                    pastProcessType.equals(ProcessType.LOW_WINDTHROW) ||
                    pastProcessType.equals(ProcessType.MEDIUM_WINDTHROW)) {
                  row = 0;
                }
                else if (pastProcessType.equals(ProcessType.HIGH_WINDTHROW)) { row = 1; }
                break;
              case MODERATE:
                if (pastProcessType.equals(ProcessType.SUCCESSION)    ||
                    pastProcessType.equals(ProcessType.LOW_WINDTHROW) ||
                    pastProcessType.equals(ProcessType.MEDIUM_WINDTHROW)) {
                  row = 0;
                }
                else if (pastProcessType.equals(ProcessType.ROOT_DISEASE)   ||
                         pastProcessType.equals(ProcessType.LIGHT_SB)       ||
                         pastProcessType.equals(ProcessType.HIGH_WINDTHROW) ||
                         pastProcessType.equals(ProcessType.WILDLIFE_BROWSING)) {
                  row = 1;
                }
                else if (pastProcessType.equals(ProcessType.LIGHT_SEVERITY_FIRE) ||
                         pastProcessType.equals(ProcessType.MIXED_SEVERITY_FIRE)) {
                  row = 2;
                }
                break;
              case HIGH:
                if (pastProcessType.equals(ProcessType.SUCCESSION)) {
                  row = 0;
                }
                else if (pastProcessType.equals(ProcessType.WILDLIFE_BROWSING)) {
                  row = 1;
                }
                else if (pastProcessType.equals(ProcessType.LIGHT_SEVERITY_FIRE) ||
                         pastProcessType.equals(ProcessType.MIXED_SEVERITY_FIRE)) {
                  row = 2;
                }
                break;
              default: return ProcessType.NONE;
            }
            break;
          default: return ProcessType.NONE;
        }
        break;
      default:
        return ProcessType.NONE;
    }

    if (row == -1) { return ProcessType.NONE; }


    if (extreme) { col++; }

    return getSpreadData(fireClass,position,densityClass,
                                    resistance,structure,page,row,col);
  }

}
