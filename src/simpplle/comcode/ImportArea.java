/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.io.*;
import java.util.*;

import simpplle.gui.ElevationRelativePosition;

/**
 * The purpose of this class is to read an input file
 * that provides information needed to create a new area.
 * <p>This class handles all aspects of creating the new area
 * including verifying and correcting states if necessary.
 */

public class ImportArea {

  private static final int EVU = 0;
  private static final int ELU = 1;
  private static final int ERU = 2;
  private boolean hasAttributes;

  public boolean attributesAdded() {
    return hasAttributes;
  }
/**
 * Check to see if this has both a row & a column.  
 * Note: if Field #5 is a number than we have row & col, however if it is a string it is not a row & col
 * @param line
 * @return true if have both a row and column (field #5 is a number), false if do not have a row & col (field #5 is a string)
 * @throws ParseError is caught in GUI, if trouble parsing line, however if it throws a parse error on field #5 while expecting a number it means we have a string which is caught here
 */
  private boolean hasRowCol(String line) throws ParseError {
    StringTokenizerPlus strTok = new StringTokenizerPlus(line,",");

    // If field #5 is a number than we have row & col,
    // however if it is a string we do not have row & col.
    String str;
    try {
      str = strTok.getToken();
      str = strTok.getToken();
      str = strTok.getToken();
      str = strTok.getToken();
    }
    catch (ParseError e) {
      throw new ParseError(e.getMessage() + "\nError parsing line: " + line);
    }

    try {
      // Field #5: Either ACRES or SPECIES
      str = strTok.getToken();
      if (str == null) {
        throw new ParseError("Not enough fields in line: " + line);
      }
      float value = Float.valueOf(str);
      // if no exception, than field #5 was a number.
      return true;
    }
    catch (NumberFormatException err) {
      return false;
    }
  }
/**
 * Checks to see if the string passed contains digits throughout.  If so it substrings from 0 to index-1
 * @param str string to be checked if has digits
 * @return a digit in string form, designates size class
 */
  private String parseSizeClass(String str) {
    if (str == null) { return str; }

    boolean foundNumber = false;

    int i = 0;
    while (i < str.length() && !foundNumber) {
      foundNumber = Character.isDigit(str.charAt(i));
      i++;
    }

    if (foundNumber) {
      return str.substring(0,(i-1));
    }
    else { return str; }
  }
/**
 * Parses a passed string which is converted to an int and designates age
 * @param size
 * @return the age parsed from passed string
 * @throws NumberFormatException - caught here, used to designate if age is 1
 */
  private int parseAge(String size) {
    if (size == null) { return 1; }

    boolean foundNumber = false;
    int     age = 1;

    int i = 0;
    while (i < size.length() && !foundNumber) {
      foundNumber = Character.isDigit(size.charAt(i));
      i++;
    }

    if (foundNumber) {
      i--;
      try {
        age = Integer.parseInt(size.substring(i));
      }
      catch (NumberFormatException NFE) {
        age = 1;
      }
    }
    return age;
  }
/**
 *  
 *  Attributes of an area, they are stored in the file in the following order:
 * +slink, ++row#, ++col#, +unit#, +acres, +htgrp, +species, +size class,
 * +density, ownership, road status, ignition prob,
 * fmz, special area, landtype, initial process, initial Treatment
 * Longitude, Latitude.
 * +  = Required Field
  * ++ = Required only in GRASSLAND Zone.
 *
 * @param area whose attributes are being read in.
 * @param fin
 * @param logFile
 * @throws ParseError line where parse error occurred is added in message caught in GUI
 * @throws IOException caught in GUI
 */
  private void readAttributes(Area area, BufferedReader fin, PrintWriter logFile) throws ParseError, IOException {
   

    String              line, str;
    StringTokenizerPlus strTok;
    int                 value, count;
    float               fValue;
    Evu                 evu;
    RegionalZone        zone = Simpplle.getCurrentZone();
    HabitatTypeGroup    htGrp;
    VegetativeType      state = null;
    String              speciesStr, sizeClassStr, densityStr;
    int                 age, id;
    ProcessType         process;
    Treatment           treatment;
    TreatmentType       treatType;
    Fmz                 fmz;
    StringBuffer        strBuf;
    int                 begin, index;
    int                 numReqFields = -1;
    boolean             hasRowCol=false;

    boolean             processedAsMultipleLife=false;

    line = fin.readLine();
    if (line == null) {
      logFile.println("No Attribute data found in input file.");
      logFile.println();
      hasAttributes = false;
      return;
    }

    while(line != null && !line.trim().equals("END")) {
      try {
        line = line.trim();
        if (line.length() == 0) { line = fin.readLine(); continue; }

        line = Utility.preProcessInputLine(line);

        if (numReqFields == -1) {
          // Find out if we have row and col.
          hasRowCol = hasRowCol(line);
          numReqFields = (hasRowCol) ? 9 : 7;
        }

        strTok = new StringTokenizerPlus(line,",");
        count  = strTok.countTokens();
        if (count < numReqFields) {
          logFile.println(line);
          logFile.println("   Not enough fields in attribute data.");
          logFile.println("   Required fields are:");
          if (zone.getId() == ValidZones.GRASSLAND) {
            logFile.println("     slink, row #, col #, unit #, acres,");
            logFile.println("     habitat type group, species, size class, density");
          }
          else {
            logFile.println("     slink, unit #, acres,");
            logFile.println("     habitat type group, species, size class, density");
            logFile.println("   note: (row, col) can optionally be placed after slink:");
            logFile.println("    (e.g. slink, row, col, unit #, acres, ...");
          }
          logFile.println();
          line = fin.readLine();
          continue;
        }

        // Get the evu id
        id = strTok.getIntToken();
        if (id == -1) {
          logFile.println(line);
          logFile.println("  Invalid Slink");
          logFile.println();
          continue;
        }
        evu = area.getEvu(id);
        if (evu == null) {
          logFile.println(line);
          logFile.println("  An Evu with the id: " + id + " does not exist.");
          logFile.println();
          line = fin.readLine();
          continue;
        }

        if (hasRowCol) {
          // get the Location
          // Note: not all areas will have x,y values
          //       so we don't really need to check anything.
          //       Also, location is not really used right now.
          int row, col;
          row = Math.round(strTok.getFloatToken());
          col = Math.round(strTok.getFloatToken());

          evu.setLocationX(col);

          evu.setLocationY(row);
        }

        // Get the Unit Number (which is actually a string)
        str = strTok.getToken();
        if (str == null) {
          logFile.println(line);
          logFile.println("  In Evu-" + id + ":");
          logFile.println("  The Unit number is missing (a required field)");
          logFile.println("  The value \"UNKNOWN\" was assigned");

          str = "UNKNOWN";
        }
        str = str.trim();
        evu.setUnitNumber(str);

        // Get the Acres
        fValue  = strTok.getFloatToken();
        evu.setAcres(fValue);
        // Acres check is done later, because we need to know species for check.

        // Get the Habitat Type Group
        str = strTok.getToken();
        str = (str != null) ? str.trim().toUpperCase() : "UNKNOWN";
        htGrp = HabitatTypeGroup.findInstance(str);
        if (htGrp == null) {
          logFile.println(line);
          logFile.println("  In Evu-" + id + ":");
          logFile.println("  " + htGrp + " is not a valid Ecological Grouping.");
          logFile.println("  The value \"" + str + "\" was assigned");
          logFile.println();

          // Need to set this anyway so that user can correct it later.
          evu.setHabitatTypeGroup(new HabitatTypeGroup(str));
        }
        else {
          evu.setHabitatTypeGroup(htGrp);
        }

        // Get the species, size class, and density;
        boolean newFileFormat=false;
        speciesStr = strTok.getToken();
        if (speciesStr != null && speciesStr.equals("#")) {
          processedAsMultipleLife = true;
          area.setDisableMultipleLifeforms(false);
          readMultiLifeformState(logFile,strTok,line,evu,htGrp);
          newFileFormat = true;
        }
        else {

          if (speciesStr == null) {
            speciesStr = "UNKNOWN";
            logFile.println(line);
            logFile.println("  In Evu-" + id + " Species value is missing.");
            logFile.println("  The value \"UNKNOWN\" was assigned");
            logFile.println();
          }
          else {
            speciesStr = speciesStr.trim().toUpperCase();
          }

          str = strTok.getToken();
          if (str != null) {
            str = str.trim().toUpperCase();
          }
          sizeClassStr = parseSizeClass(str);
          if (sizeClassStr == null) {
            sizeClassStr = "UNKNOWN";
            logFile.println(line);
            logFile.println("  In Evu-" + id + " Size Class value is missing.");
            logFile.println("  The value \"UNKNOWN\" was assigned");
            logFile.println();
          }
          age = parseAge(str);

          densityStr = strTok.getToken();
          if (densityStr == null) {
            densityStr = "UNKNOWN";
            logFile.println(line);
            logFile.println("  In Evu-" + id + " Density value is missing.");
            logFile.println("  The value \"UNKNOWN\" was assigned");
            logFile.println();
          }
          Species species = Species.get(speciesStr);
          if (species == null) {
            species = new Species(speciesStr);
            logFile.println(line);
            logFile.println("  In Evu-" + id + " Species \"" + speciesStr +
                            "\" is unknown");
          }

          SizeClass sizeClass = SizeClass.get(sizeClassStr);
          if (sizeClass == null) {
            sizeClass = new SizeClass(sizeClassStr);
            logFile.println(line);
            logFile.println("  In Evu-" + id + " Size Class \"" + sizeClassStr +
                            "\" is unknown");
          }

          Density density = Density.get(densityStr);
          if (density == null) {
            density = new Density(densityStr);
            logFile.println(line);
            logFile.println("  In Evu-" + id + " Density \"" + densityStr +
                            "\" is unknown");
          }

          state = null;
          if (species != null && sizeClass != null && density != null && htGrp != null) {
            state = htGrp.getVegetativeType(species, sizeClass, age, density);
          }
          if (state == null) {
            logFile.println(line);
            logFile.println("  In Evu-" + id + "Could not build a valid state.");
            logFile.println("  One or more of the following must be invalid:");
            logFile.println(
                "  Species, Size Class, Density, or Ecological Grouping");
            logFile.println();

            // Even invalid states need to be set in the evu, in order to
            // allow for later correction.
            state = new VegetativeType(species, sizeClass, age, density);
            evu.setState(state,Climate.Season.YEAR);
          }
          else {
            evu.setState(state,Climate.Season.YEAR);
          }

          // In case there is not Initial Process.
          evu.setInitialProcess(Evu.getDefaultInitialProcess());
        }

        // Acres check depends on species being available.
        if (evu.isAcresValid() == false) {
          logFile.println(line);
          logFile.println("  In Evu-" + id + " Acres is Invalid");
          logFile.println("  Acres must be a value greater than 0.0");
          logFile.println("    - acres can be equal to 0 if species=ND");
          logFile.println();
        }

        // End of required fields
        if (count == numReqFields) { line = fin.readLine(); continue; }

        // Get the Ownership information (if any)
        str = strTok.getToken();
        evu.setOwnership(str);
        if (count == (numReqFields+1)) { line = fin.readLine(); continue; }

        // Get the Road Status information (if any)
        str = strTok.getToken();
        if (str != null) { str = str.trim().toUpperCase(); }

        if (str == null) {
          evu.setRoadStatus(Roads.Status.UNKNOWN);
        }
        else {
          evu.setRoadStatus(Roads.Status.lookup(str));
        }
        if (count == (numReqFields+2)) { line = fin.readLine(); continue; }

        // Get the Ignition Prob
        // value not used anymore.
        str = strTok.getToken();
        evu.setIgnitionProb(0);

        if (count == (numReqFields+3)) { line = fin.readLine(); continue; }

        // Get the Fmz
        str = strTok.getToken();
        if (str == null) {
          fmz = zone.getDefaultFmz();
        }
        else {
          fmz = zone.getFmz(str);
        }
        if (fmz == null) {
          logFile.println(line);
          logFile.println("  In Evu-" + id + ":");
          logFile.println("  " + str + " is not a valid Fire Management Zone.");
          logFile.println();

          evu.setFmz(new Fmz(str));
        }
        else {
          evu.setFmz(fmz);
        }
        if (count == (numReqFields+4)) { line = fin.readLine(); continue; }

        // Get the Special Area
        str = strTok.getToken();
        evu.setSpecialArea(str);
        if (count == (numReqFields+5)) { line = fin.readLine(); continue; }

        // Read Landtype
        str = strTok.getToken();
        evu.setLandtype(str);
        if (count == (numReqFields+6)) { line = fin.readLine(); continue; }

        if (!newFileFormat) {
          // Read Initial Process
          str = strTok.getToken();
          if (str == null) {
            str = Evu.getDefaultInitialProcess().toString();
          }
          process = ProcessType.get(str.toUpperCase());
          if (process == null) {
            logFile.println(line);
            logFile.println("  In Evu-" + id + ":");
            logFile.println("  " + str + " is not a Process.");
            logFile.println();

            evu.setInitialProcess(str);
          }
          else {
            evu.setInitialProcess(process);
          }
          if (count == (numReqFields + 7)) { line = fin.readLine(); continue; }

          // Read Initial Treatment
          str = strTok.getToken();
          if (str != null) {
            treatType = TreatmentType.get(str.toUpperCase());
            if (treatType == null) {
              logFile.println(line);
              logFile.println("  In Evu-" + id + ":");
              logFile.println("  " + str + " is not a Treatment.");
              logFile.println();
            }
            else {
              /** TODO need to update state later if it is an invalid one. */
              /** TODO carry process & treatment as initial when make sim ready */
              treatment = Treatment.createInitialTreatment(treatType, state);
              evu.addTreatment(treatment);
            }
          }
        }
        else {
          double longitude = strTok.getDoubleToken(Double.NaN); // POINT_X Field
          evu.setLongitude(longitude);
          if (count == (numReqFields + 7)) { line = fin.readLine(); continue; }

          double latitude = strTok.getDoubleToken(Double.NaN); // POINT_Y Field
          evu.setLatitude(latitude);
        }

        // Get the Next Line.
        line = fin.readLine();
      }
      catch (ParseError err) {
        String msg = "The following error occurred in this line: \n" +
                     line + "\n" + err.getMessage();
        throw new ParseError(msg);
      }
    }
    area.updateArea();

    area.setMultipleLifeformStatus();
    if ((area.multipleLifeformsEnabled() == false) &&
        processedAsMultipleLife) {
      // Need to change Evu's to be single lifeform;
      Evu[] evus = area.getAllEvu();
      for (int i=0; i<evus.length; i++) {
        if (evus[i] != null) { evus[i].makeSingleLife(); }
      }
    }
  }
/**
 * Reads in the Multi Life Form State.  Sets species, size Class, and density 
 * @param logFile
 * @param strTok
 * @param line
 * @param evu
 * @param htGrp
 * @throws ParseError caught in GUI
 */
  private void readMultiLifeformState(PrintWriter logFile,
                                      StringTokenizerPlus strTok,
                                      String line, Evu evu,
                                      HabitatTypeGroup htGrp) throws ParseError {
    String              str;
    VegetativeType      vegState = null;
    String              speciesStr, sizeClassStr=null, densityStr;
    int                 age=1;
    ProcessType         process;
    Treatment           treatment;
    TreatmentType       treatType;
    boolean             emptyState=false;

    int numLifeforms = Math.round(strTok.getFloatToken());
    if (numLifeforms == -1) {
      logFile.println(line);
      logFile.println("  In Evu-" + evu.getId() + " Number of Lifeforms is missing.");
      logFile.println();
      throw new ParseError("Data for Evu-" + evu.getId() + " Missing, see logfile");
    }

    for (int lf=0; lf<numLifeforms; lf++) {
      speciesStr   = null;
      sizeClassStr = null;
      densityStr   = null;

      VegSimStateData state = new VegSimStateData(evu.getId());

      speciesStr = strTok.getToken();
      if (speciesStr != null) {
        speciesStr = speciesStr.trim().toUpperCase();
      }

      str = strTok.getToken();
      if (str != null) {
        str = str.trim().toUpperCase();
        sizeClassStr = parseSizeClass(str);
        age = parseAge(str);
      }

      densityStr = strTok.getToken();

      vegState = null;
      emptyState = (speciesStr == null && sizeClassStr == null && densityStr == null);


      if (!emptyState)
      {
        if (speciesStr == null) { speciesStr = "UNKNOWN"; }
        if (sizeClassStr == null) { sizeClassStr = "UNKNOWN"; }
        if (densityStr == null) { densityStr = "UNKNOWN"; }

        Species species = Species.get(speciesStr, true);
        SizeClass sizeClass = SizeClass.get(sizeClassStr, true);
        Density density = Density.get(densityStr, true);

        if (species != null && sizeClass != null && density != null && htGrp != null) {
          vegState = htGrp.getVegetativeType(species, sizeClass, age, density);
        }

        if (vegState == null) {
          logFile.println(line);
          logFile.println("  In Evu-" + evu.getId() +
                          "Could not build a valid state.");
          logFile.println("  One or more of the following must be invalid:");
          logFile.println("  Species, Size Class, Density, or Ecological Grouping");
          logFile.println();

          // Even invalid states need to be set in the evu, in order to
          // allow for later correction.
          vegState = new VegetativeType(species, sizeClass, age, density);
        }
        state.setVegType(vegState);
      }
      // Skip the rest of the tokens.
      if (emptyState) {
        strTok.nextToken();
        strTok.nextToken();
        strTok.nextToken();
        strTok.nextToken();
      }

      if (!emptyState)
      {
        // Read Initial Process
        str = strTok.getToken();
        if (str == null) {
          str = Evu.getDefaultInitialProcess().toString();
        }

        process = ProcessType.get(str.toUpperCase());
        if (process == null) {
          logFile.println(line);
          logFile.println("  In Evu-" + evu.getId() + ":");
          logFile.println("  " + str + " is not a Process.");
          logFile.println();

          process = ProcessType.makeInvalidInstance(str);
        }
        state.setProcess(process);

        // Read process time Steps in past.
        int ts = Math.round(strTok.getFloatToken());

        // Read Initial Treatment
        str = strTok.getToken();
        if (str != null) {
          treatType = TreatmentType.get(str.toUpperCase());
          if (treatType == null) {
            logFile.println(line);
            logFile.println("  In Evu-" + evu.getId() + ":");
            logFile.println("  " + str + " is not a Treatment.");
            logFile.println();
          }
          else {
            /** TODO need to update state later if it is an invalid one. */
            /** TODO carry process & treatment as initial when make sim ready */
            treatment = Treatment.createInitialTreatment(treatType, vegState);
            evu.addTreatment(treatment);
          }
        }
        // Read treatment time steps in past
        ts = Math.round(strTok.getFloatToken());
      }

      if (!emptyState) {
        evu.setState(state, Climate.Season.YEAR);
      }

      int numTrackSpecies = Math.round(strTok.getFloatToken());

      // Skip the tracking species info if no valid state information.
      if (emptyState) {
        for (int i=0; i<numTrackSpecies; i++) {
          strTok.nextToken();  // Species
          strTok.nextToken();  // Percent
        }
        continue;
      }

      int percent;
      if (numTrackSpecies != -1) {
        state.initializeTrackingSpecies(numTrackSpecies);
        for (int i = 0; i < numTrackSpecies; i++) {
          str = strTok.getToken();
          if (str != null) {
            str = str.trim().toUpperCase();
          }

          percent = Math.round(strTok.getFloatToken());
          if (str == null || percent == -1) {
            continue;
          }

          InclusionRuleSpecies trk_species = InclusionRuleSpecies.get(str,true);

          Range range = vegState.getSpeciesRange(trk_species);
          if (range != null) {
            if (percent < range.getLower()) {
              percent = range.getLower();
            }
            else if (percent > range.getUpper()) {
              percent = range.getUpper();
            }
          }
          state.addTrackSpecies(trk_species, percent);
        }
        state.addMissingTrackSpecies();
        state.removeInvalidTrackSpecies();
      }

    }
    // This should be the ending #
    str = strTok.getToken();

    // If we have multiple lifeforms they use different columns and the
    // count will always be five.
    if (numLifeforms == 1) {
      evu.makeSingleLife();
    }
  }
  /**
   * reads in Land Attributes.  They are stored in file in the following order: slink (ID), acres, soilType, landform, aspect, slope, parent material, depth
   * @param area
   * @param fin
   * @param logFile
   * @throws ParseError
   * @throws IOException
   */
  private void readLandAttributes(Area area, BufferedReader fin, PrintWriter logFile) throws ParseError, IOException {

    String              line, str;
    StringTokenizerPlus strTok;
    int                 count;
    int                 id;
    ExistingLandUnit    elu;
    float               fValue;
    int                 numReqFields = 8;

    line = fin.readLine();
    if (line == null) {
      logFile.println("No Land Attribute data found in input file.");
      logFile.println();
      hasAttributes = false;
      return;
    }

    while(line != null && line.trim().equals("END") == false) {
      try {
        line = line.trim();
        if (line.length() == 0) { line = fin.readLine(); continue; }

        line = Utility.preProcessInputLine(line);

        strTok = new StringTokenizerPlus(line,",");
        count  = strTok.countTokens();
        if (count < numReqFields) {
          logFile.println(line);
          logFile.println("   Not enough fields in attribute data.");
          logFile.println("   Required fields are:");
          logFile.print("     slink, acres, name, landform,");
          logFile.println("   aspect, slope, parent material");
          logFile.println();
          line = fin.readLine();
          continue;
        }

        // Get the unit id
        id = strTok.getIntToken();
        if (id == -1) {
          logFile.println(line);
          logFile.println("  Invalid Slink");
          logFile.println();
          continue;
        }
        elu = area.getElu(id);
        if (elu == null) {
          logFile.println(line);
          logFile.println("  An Existing Land Unit with the id: " + id + " does not exist.");
          logFile.println();
          line = fin.readLine();
          continue;
        }

        // Get the Acres
        fValue  = strTok.getFloatToken();
        elu.setAcres(fValue);
        // Acres check is done later, because we need to know species for check.

        // Get the Soil Type
        str = strTok.getToken();
        if (str != null) {
          elu.setSoilType(SoilType.get(str,true));
        }

        // Get the Landform
        str = strTok.getToken();
        if (str != null) {
          elu.setLandform(str);
        }

        // Get the aspect (can be either a double or String)
        str = strTok.getToken();
        if (str != null) {
          try {
            double value = Double.parseDouble(str);
            elu.setAspect(value);
          }
          catch (NumberFormatException ex) {
            elu.setAspectName(str);
          }
        }

        // Get the slope
        fValue = strTok.getFloatToken(Float.NaN);
        if (Float.isNaN(fValue)) {
          logFile.println(line);
          logFile.println("  In Elu-" + id + " Slope is Invalid");
          logFile.println("  Value of 0.0 assigned");
          logFile.println();
          fValue = 0.0f;
        }
        elu.setSlope(fValue);

        // Get the parent material
        str = strTok.getToken();
        if (str != null) {
          elu.setParentMaterial(str);
        }

        // Get the Depth
        str = strTok.getToken();
        if (str != null) {
          elu.setDepth(str);
        }

        if (strTok.hasMoreTokens()) {
          double longitude = strTok.getDoubleToken(Double.NaN); // POINT_X Field
          elu.setLongitude(longitude);
        }

        if (strTok.hasMoreTokens()) {
          double latitude = strTok.getDoubleToken(Double.NaN); // POINT_Y Field
          elu.setLatitude(latitude);
        }

       // Acres check depends on species being available.
        if (elu.isAcresValid() == false) {
          logFile.println(line);
          logFile.println("  In Elu-" + id + " Acres is Invalid");
          logFile.println("  Acres must be a value greater than 0.0");
          logFile.println();
        }

        // Get the Next Line.
        line = fin.readLine();
      }
      catch (ParseError err) {
        String msg = "The following error occurred in this line: \n" +
                     line + "\n" + err.getMessage();
        throw new ParseError(msg);
      }
    }
  }
/**
 * Reads in the road attributes.  They are stored in the file in the following order: slink (id), Status, Kind
 * @param area
 * @param fin
 * @param logFile
 * @throws ParseError
 * @throws IOException
 */
  private void readRoadsAttributes(Area area, BufferedReader fin, PrintWriter logFile) throws ParseError, IOException {
/*
    public enum Status { OPEN, CLOSED, PROPOSED, ELIMINATED, UNKNOWN };
    public enum Kind { SINGLE_LANE, DOUBLE_LANE, UNIMPROVED, SYSTEM, NONSYSTEM, UNKNOWN};
*/
    // attributes in the file in the following order:
    // slink(id),Status,Kind

    String              line, str;
    StringTokenizerPlus strTok;
    int                 count;
    int                 id;
    Roads               roadUnit;
    float               fValue;
    int                 numReqFields = 3;

    line = fin.readLine();
    if (line == null) {
      logFile.println("No Roads Attribute data found in input file.");
      logFile.println();
      hasAttributes = false;
      return;
    }

    while(line != null && line.trim().equals("END") == false) {
      try {
        line = line.trim();
        if (line.length() == 0) { line = fin.readLine(); continue; }

        line = Utility.preProcessInputLine(line);

        strTok = new StringTokenizerPlus(line,",");
        count  = strTok.countTokens();
        if (count < numReqFields) {
          logFile.println(line);
          logFile.println("   Not enough fields in attribute data.");
          logFile.println("   Required fields are:");
          logFile.println("     slink, Status, Kind");
          logFile.println();
          line = fin.readLine();
          continue;
        }

        // Get the unit id
        id = strTok.getIntToken();
        if (id == -1) {
          logFile.println(line);
          logFile.println("  Invalid Slink");
          logFile.println();
          continue;
        }
        roadUnit = area.getRoadUnit(id);
        if (roadUnit == null) {
          logFile.println(line);
          logFile.println("  An Road Unit with the id: " + id + " does not exist.");
          logFile.println();
          line = fin.readLine();
          continue;
        }

        // Get the Status
        Roads.Status status = Roads.Status.UNKNOWN;
        str = strTok.getToken();
        if (str != null) {
          status = Roads.Status.lookup(str);
          if (status == null) {
            logFile.println(line);
            logFile.println("  Invalid Road Status: " + str);
            logFile.println();
          }
        }
        roadUnit.setStatus(status);

        // Get the Kind
        Roads.Kind kind = Roads.Kind.UNKNOWN;
        str = strTok.getToken();
        if (str != null) {
          kind = Roads.Kind.valueOf(str);
          if (kind == null) {
            logFile.println(line);
            logFile.println("  Invalid Road Kind: " + str);
            logFile.println();
          }
        }
        roadUnit.setKind(kind);

        // Get the Next Line.
        line = fin.readLine();
      }
      catch (ParseError err) {
        String msg = "The following error occurred in this line: \n" +
                     line + "\n" + err.getMessage();
        throw new ParseError(msg);
      }
    }
  }
  /**
   * Reads in the trail attributes.  They are stored in the area file in the following order: slink (ID) status, kind 
   * Default status is UNKNOWN, default kind is UNKNOWN
   * @param area
   * @param fin
   * @param logFile
   * @throws ParseError
   * @throws IOException
   */
  private void readTrailsAttributes(Area area, BufferedReader fin, PrintWriter logFile) throws ParseError, IOException {
/*
    public enum Status { OPEN, CLOSED, PROPOSED, ELIMINATED, UNKNOWN };
    public enum Kind { HIKE};
*/
    // attributes in the file in the following order:
    // slink(id),Status,Kind

    String              line, str;
    StringTokenizerPlus strTok;
    int                 count;
    int                 id;
    Trails              trailUnit;
    float               fValue;
    int                 numReqFields = 3;

    line = fin.readLine();
    if (line == null) {
      logFile.println("No Trails Attribute data found in input file.");
      logFile.println();
      hasAttributes = false;
      return;
    }

    while(line != null && line.trim().equals("END") == false) {
      try {
        line = line.trim();
        if (line.length() == 0) { line = fin.readLine(); continue; }

        line = Utility.preProcessInputLine(line);

        strTok = new StringTokenizerPlus(line,",");
        count  = strTok.countTokens();
        if (count < numReqFields) {
          logFile.println(line);
          logFile.println("   Not enough fields in attribute data.");
          logFile.println("   Required fields are:");
          logFile.println("     slink, Status, Kind");
          logFile.println();
          line = fin.readLine();
          continue;
        }

        // Get the unit id
        id = strTok.getIntToken();
        if (id == -1) {
          logFile.println(line);
          logFile.println("  Invalid Slink");
          logFile.println();
          continue;
        }
        trailUnit = area.getTrailUnit(id);
        if (trailUnit == null) {
          logFile.println(line);
          logFile.println("  An Road Unit with the id: " + id + " does not exist.");
          logFile.println();
          line = fin.readLine();
          continue;
        }

        // Get the Status
        Trails.Status status = Trails.Status.UNKNOWN;
        str = strTok.getToken();
        if (str != null) {
          status = Trails.Status.valueOf(str);
          if (status == null) {
            logFile.println(line);
            logFile.println("  Invalid Road Status: " + str);
            logFile.println();
          }
        }
        trailUnit.setStatus(status);

        // Get the Kind
        Trails.Kind kind = Trails.Kind.UNKNOWN;
        str = strTok.getToken();
        if (str != null) {
          kind = Trails.Kind.valueOf(str);
          if (kind == null) {
            logFile.println(line);
            logFile.println("  Invalid Road Kind: " + str);
            logFile.println();
          }
        }
        trailUnit.setKind(kind);

        // Get the Next Line.
        line = fin.readLine();
      }
      catch (ParseError err) {
        String msg = "The following error occurred in this line: \n" +
                     line + "\n" + err.getMessage();
        throw new ParseError(msg);
      }
    }
  }
/**
 * Reads in aquatic attributes of a specified area.  The attributes are stored in a file in the following order:
 * slink (ID), length, lta valley segment group, aquatic class, aquatic attribute, segment #, initial process, status.
 * @param area
 * @param fin
 * @param logFile
 * @throws ParseError
 * @throws IOException
 */
  private void readAquaticAttributes(Area area, BufferedReader fin, PrintWriter logFile) throws ParseError, IOException {
    

    String              line, str;
    StringTokenizerPlus strTok;
    int                 count;
    int                 id;
    ExistingAquaticUnit eau;
    float               fValue;
    int                 numReqFields = 7;

    line = fin.readLine();
    if (line == null) {
      logFile.println("No Aquatic Attribute data found in input file.");
      logFile.println();
      hasAttributes = false;
      return;
    }

    while(line != null && line.trim().equals("END") == false) {
      try {
        line = line.trim();
        if (line.length() == 0) { line = fin.readLine(); continue; }

        line = Utility.preProcessInputLine(line);

        strTok = new StringTokenizerPlus(line,",");
        count  = strTok.countTokens();
        if (count < numReqFields) {
          logFile.println(line);
          logFile.println("   Not enough fields in attribute data.");
          logFile.println("   Required fields are:");
          logFile.print("     slink, length, Lta Valley Segment Group,");
          logFile.println("   aquatic class, aquatic attribute,");
          logFile.println("   segment number, initial Process");
          logFile.println();
          line = fin.readLine();
          continue;
        }

        // Get the unit id
        id = strTok.getIntToken();
        if (id == -1) {
          logFile.println(line);
          logFile.println("  Invalid Slink");
          logFile.println();
          continue;
        }
        eau = area.getEau(id);
        if (eau == null) {
          eau = new ExistingAquaticUnit(id);
          area.setEau(eau);
          logFile.println(line);
          logFile.println("  Aquatic unit #" + id + " not found in spatialrelate file" +
                          " creating a default(island) unit");
          logFile.println();
        }

        // Get the Length
        fValue  = strTok.getFloatToken();
        eau.setLength(fValue);

        // Get the Lta Valley Segment Group
        str = strTok.getToken();
        str = (str != null) ? str.trim().toUpperCase() : "UNKNOWN";
        LtaValleySegmentGroup group = LtaValleySegmentGroup.findInstance(str);
        if (group == null) {
          logFile.println(line);
          logFile.println("  In Eau-" + id + ":");
          logFile.println("  " + group + " is not a valid Lta Valley Segment Group.");
          logFile.println("  The value \"UNKNOWN\" was assigned");
          logFile.println();

          // Need to set this anyway so that user can correct it later.
          eau.setLtaValleySegmentGroup(new LtaValleySegmentGroup(str));
        }
        else {
          eau.setLtaValleySegmentGroup(group);
        }

        // Get the aquatic class and attribute.
        String aquaticClassStr = strTok.getToken();
        if (aquaticClassStr == null) {
          aquaticClassStr = "UNKNOWN";
          logFile.println(line);
          logFile.println("  In Eau-" + id + " Aquatic Class value is missing.");
          logFile.println("  The value \"UNKNOWN\" was assigned");
          logFile.println();
        }
        else { aquaticClassStr = aquaticClassStr.trim().toUpperCase(); }

        String aquaticAttributeStr = strTok.getToken();
        if (aquaticAttributeStr == null) {
          aquaticAttributeStr = "UNKNOWN";
          logFile.println(line);
          logFile.println("  In Eau-" + id + " Aquatic Attribute value is missing.");
          logFile.println("  The value \"UNKNOWN\" was assigned");
          logFile.println();
        }
        else { aquaticAttributeStr = aquaticAttributeStr.trim().toUpperCase(); }


        AquaticClass aquaticClass = AquaticClass.get(aquaticClassStr);
        if (aquaticClass == null) {
          aquaticClass = new AquaticClass(aquaticClassStr);
          logFile.println(line);
          logFile.println("  In Evu-" + id + " Aquatic Class \"" + aquaticClassStr + "\" is unknown");
        }

        AquaticAttribute aquaticAttribute = AquaticAttribute.get(aquaticAttributeStr);
        if (aquaticAttribute == null) {
          aquaticAttribute = new AquaticAttribute(aquaticAttributeStr);
          logFile.println(line);
          logFile.println("  In Evu-" + id + " Aquatic Attribute \"" + aquaticAttributeStr + "\" is unknown");
        }

        PotentialAquaticState state = null;
        if (aquaticClass != null && aquaticAttribute != null && group != null) {
          state = group.getPotentialAquaticState(aquaticClass,aquaticAttribute);
        }
        if (state == null) {
          logFile.println(line);
          logFile.println("  In Eau-" + id + "Could not build a valid state.");
          logFile.println("  One or more of the following must be invalid:");
          logFile.println("  Aquatic Class, Aquatic Attribute, Lta Valley Segment Group");
          logFile.println();

          // Even invalid states need to be set in the evu, in order to
          // allow for later correction.
          state = new PotentialAquaticState(aquaticClass,aquaticAttribute);
        }
        eau.setCurrentState(state);
        eau.setPotentialAquaticState(state);

        // Get the Segment Number
        int value = strTok.getIntToken();
        if (value == -1) { value = id; }
        eau.setSegmentNumber(value);

        if (eau.isLengthValid() == false) {
          logFile.println(line);
          logFile.println("  In Eau-" + id + " Length is Invalid");
          logFile.println("  Length must be a value greater than 0.0");
          logFile.println();
        }

        // Read Initial Process
        str = strTok.getToken();
        if (str == null) {
          str = ExistingAquaticUnit.getDefaultInitialProcess().toString();
        }

        Process process = Process.findInstance(ProcessType.get(str.toUpperCase()));
        if (process == null) {
          logFile.println(line);
          logFile.println("  In Eau-" + id + ":");
          logFile.println("  " + str + " is not a Process.");
          logFile.println();

          eau.setInitialProcess(str);
        }
        else {
          eau.setInitialProcess(process);
        }

        // Read the Status
        str = strTok.getToken();
        if (str == null) {
          eau.setStatus(ExistingAquaticUnit.PERENNIAL);
        }
        else if (eau.setStatus(str) == false) {
          eau.setStatus(ExistingAquaticUnit.PERENNIAL);
        }


        // Get the Next Line.
        line = fin.readLine();
      }
      catch (ParseError err) {
        String msg = "The following error occurred in this line: \n" +
                     line + "\n" + err.getMessage();
        throw new ParseError(msg);
      }
    }
  }
  public void readAtributesFile(File prefix) throws SimpplleError {
    File file, log, logFile;
    Area area = Simpplle.getCurrentArea();

    hasAttributes = false;

    // Veg Attributes
    file = Utility.makeSuffixedPathname(prefix, "", "attributes");
    logFile = Utility.makeUniqueLogFile(prefix, "vegatt");
    read(area, file, logFile, EVU, true);

    hasAttributes = true;
  }

  public Area importNewFiles(File filename) throws SimpplleError {
    Area newArea = importSpatial(filename);

    File prefix     = Utility.stripExtension(filename);
    File logFile    = Utility.makeUniqueLogFile(prefix, "attrib");
    File attribFile = Utility.makeSuffixedPathname(prefix, "", "attributesall");

    if (!attribFile.exists()) {
      hasAttributes = false;
    }
    readAttributesNew(newArea,attribFile,logFile);

    // Set the Elevation Relative Position
    // For use in later calculating Above, Below, Next-to
    newArea.setElevationRelativePositionDefault();
    ElevationRelativePosition dlg = new ElevationRelativePosition(simpplle.JSimpplle.getSimpplleMain(),"Elevation Relative Position",true,newArea);
    
    dlg.setVisible(true);
    
    int elevRelativePos = dlg.getValue();
    
    newArea.initPolygonWidth();
    newArea.calcRelativeSlopes(); // requires spatialrelation and attributesall to be loaded.
    newArea.setElevationRelativePosition(elevRelativePos);
    return newArea;
  }

//  private Area importSpatialRelations(File filename) throws SimpplleError {
//    PrintWriter    log=null;
//    BufferedReader fin;
//    Area           newArea = new Area(Area.USER);
//    File           prefix = Utility.stripExtension(filename);
//    File           logFile = Utility.makeUniqueLogFile(prefix,"");
//    boolean        success = false;
//
//    try {
//      log = new PrintWriter(new FileWriter(logFile));
//      fin = new BufferedReader(new FileReader(filename));
//
//      String line = fin.readLine();
//
//      while (line != null) {
//        // skip blank lines
//        while (line != null && line.trim().length() == 0) {
//          line = fin.readLine();
//        }
//
//        StringTokenizer strTok = new StringTokenizer(line.trim());
//        String str = strTok.nextToken();
//        if (str == null || !str.equalsIgnoreCase("BEGIN")) {
//          throw new SimpplleError(
//              "Invalid Spatial Relationships file: missing BEGIN in line:" +
//              line);
//        }
//
//        str = strTok.nextToken();
//        if (str == null) {
//          throw new SimpplleError(
//              "Invalid Spatial Relationships file: missing KEYWORD in line:" +
//              line);
//        }
//
//        if (str.equalsIgnoreCase("VEGETATION-VEGETATION")) {
//          success = readNeighborsNew(newArea, fin, log);
//        }
//        else if (str.equalsIgnoreCase("LANDFORM-LANDFORM")) {
//          success = readLandNeighbors(newArea, fin, log);
//        }
//        else if (str.equalsIgnoreCase("AQUATIC-AQUATIC")) {
//          success = readAquaticNeighbors(newArea, fin, log);
//        }
//        else if (str.equalsIgnoreCase("VEGETATION-LANDFORM")) {
//          success = readVegLandRelations(newArea,fin,log);
//        }
//        else if (str.equalsIgnoreCase("VEGETATION-AQUATIC")) {
//          success = readAquaticVegRelations(newArea,fin,log);
//        }
//        else if (str.equalsIgnoreCase("ROADS-ROADS")) {
//          success = readRoadNeighbors(newArea, fin, log);
//        }
//        else if (str.equalsIgnoreCase("TRAILS-TRAILS")) {
//          success = readTrailNeighbors(newArea, fin, log);
//        }
//        else if (str.equalsIgnoreCase("VEGETATION-ROADS")) {
//          success = readVegRoadRelations(newArea,fin,log);
//        }
//        else if (str.equalsIgnoreCase("VEGETATION-TRAILS")) {
//          success = readVegTrailRelations(newArea,fin,log);
//        }
//        else {
//          line = fin.readLine();
//        }
//
//        if (!success) {
//          fin.close();
//          log.flush();
//          log.close();
//          throw new SimpplleError("Could not load files. Please check log file for details.");
//        }
//
//
//        line = fin.readLine();
//        while (line != null && !line.trim().toUpperCase().startsWith("BEGIN")) {
//          line = fin.readLine();
//        }
//      }
//      fin.close();
//      return newArea;
//    }
//    catch (FileNotFoundException ex) {
//      String msg = "Could not open input file: " + filename;
//      log.println(msg);
//      log.flush();
//      log.close();
//      throw new SimpplleError(msg);
//    }
//    catch (ParseError e) {
//      String msg = "The following error occurred while trying to create the area:";
//      log.println(msg);
//      log.println(e.msg);
//      log.flush();
//      log.close();
//      throw new SimpplleError(msg + "\n" + e.msg,e);
//    }
//    catch (IOException ex) {
//      throw new SimpplleError("Could write to log file: " + logFile);
//    }
//  }

  /**
   * Process .spatialrelate files based on key
   * @param file input File
   * @return newly created area with evus and associated data
   * @throws SimpplleError
   */
  private Area importSpatial(File file) throws SimpplleError{
    PrintWriter    log = null;
    BufferedReader fin;
    RelationParser parser;
    File           prefix = Utility.stripExtension(file);
    File           logFile = Utility.makeUniqueLogFile(prefix,"");
    Area newArea = new Area(Area.USER);
    Boolean success = false;

    try {
      log = new PrintWriter(new FileWriter(logFile));
      fin = new BufferedReader(new FileReader(file));
      String line = fin.readLine();
      while (line != null){
        StringTokenizer strTok = new StringTokenizer(line.trim());
        String str = strTok.nextToken();
        if (str == null || !str.equalsIgnoreCase("BEGIN")) {
          throw new SimpplleError(
              "Invalid Spatial Relationships file: missing BEGIN in line:" +
              line);
        }
        String key = strTok.nextToken();
        if (key == null) {
          throw new SimpplleError(
              "Invalid Spatial Relationships file: missing KEYWORD in line:" +
              line);
        }
        // Determine parser strategy based on key
        switch (key.toUpperCase()){
          case "VEGETATION-VEGETATION":
            parser = new ParseNewNeighbors();
            break;
          case "VEGETATION-VEGETATION-KEANE":
            parser = new ParseNewNeighborsKeane();
            newArea.setHasKeaneAttributes(true);
            break;
          case "LANDFORM-LANDFORM":
            parser = new ParseLandNeighbors();
            break;
          case "AQUATIC-AQUATIC":
            parser = new ParseAquaticNeighbors();
            break;
          case "VEGETATION-LANDFORM":
            parser = new ParseVegLandRelations();
            break;
          case "VEGETATION-AQUATIC":
            parser = new ParseAquaticVegRelations();
            break;
          case "ROADS-ROADS":
            parser = new ParseRoadNeighbors();
            break;
          case "TRAILS-TRAILS":
            parser = new ParseTrailNeighbors();
            break;
          case "VEGETATION-ROADS":
            parser = new ParseVegRoadRelations();
            break;
          case "VEGETATION-TRAILS":
            parser = new ParseVegTrailRelations();
            break;
          default:
            parser = null;
        }

        if (parser == null) line = fin.readLine();   // no keyword, skip line
        else  success = parser.readSection(newArea, fin, log);

        if (!success) {
          fin.close();
          log.flush();
          log.close();
          throw new SimpplleError("Could not load files. Please check log file for details.");
        }
        line = fin.readLine();
        while (line != null && !line.trim().toUpperCase().startsWith("BEGIN")) {
          line = fin.readLine();
        }
      }
      fin.close();
      return newArea;
    } catch (IOException e) {
      throw new SimpplleError("Error in reading " + file);
    }
    catch (ParseError e) {
      String msg = "The following error occurred while trying to create the area:";
      log.println(msg);
      log.println(e.msg);
      log.flush();
      log.close();
      throw new SimpplleError(msg + "\n" + e.msg,e);
    }
  }

  private void readAttributesNew(Area area, File filename, File logFile) throws SimpplleError {
    PrintWriter    log = null;
    BufferedReader fin;

    hasAttributes = false;

    try {
      log = new PrintWriter(new FileWriter(logFile));
      fin = new BufferedReader(new FileReader(filename));

      String line = fin.readLine();
      while (line != null) {
        while (line != null && line.trim().length() == 0) {
          line = fin.readLine();
        }
        if (line == null) {
          log.flush();
          log.close();
          fin.close();
          throw new SimpplleError("File is empty");
        }

        StringTokenizer strTok = new StringTokenizer(line.trim());
        String str = strTok.nextToken();
        if (str == null || str.equalsIgnoreCase("BEGIN") == false) {
          log.flush();
          log.close();
          fin.close();
          throw new SimpplleError(
              "Invalid Spatial Relationships file: missing BEGIN in line:" +
              line);
        }

        str = strTok.nextToken();
        if (str == null) {
          log.flush();
          log.close();
          fin.close();
          throw new SimpplleError(
              "Invalid Spatial Relationships file: missing KEYWORD in line:" +
              line);
        }

        if (str.equalsIgnoreCase("VEGETATION")) {
          readAttributes(area,fin,log);
        }
        else if (str.equalsIgnoreCase("LANDFORM")) {
          readLandAttributes(area,fin,log);
        }
        else if (str.equalsIgnoreCase("ROADS") ||
                 str.equalsIgnoreCase("ROAD")) {
          readRoadsAttributes(area,fin,log);
        }
        else if (str.equalsIgnoreCase("TRAILS")) {
          readTrailsAttributes(area,fin,log);
        }
        else if (str.equalsIgnoreCase("AQUATIC")) {
          readAquaticAttributes(area,fin,log);
        }

        line = fin.readLine();
        while (line != null && !line.trim().toUpperCase().startsWith("BEGIN")) {
          line = fin.readLine();
        }
      }
      log.flush();
      log.close();
      fin.close();
      hasAttributes = true;
    }
    catch (FileNotFoundException ex) {
      String msg = "Could not open input file: " + filename;
      log.println(msg);
      log.flush();
      log.close();
      throw new SimpplleError(msg);
    }
    catch (ParseError e) {
      String msg = "The following error occurred while trying to create the area:";
      log.println(msg);
      log.println(e.msg);
      log.flush();
      log.close();
      throw new SimpplleError(msg + "\n" + e.msg);
    }
    catch (IOException ex) {
      throw new SimpplleError("Could write to log file: " + logFile);
    }
  }

  public Area readFiles(File prefix) throws SimpplleError {
    File file, log, logFile;
    Area newArea = new Area(Area.USER);

    boolean attributesAdded = false;

    file = Utility.makeSuffixedPathname(prefix,"","nbr");
    logFile = Utility.makeUniqueLogFile(prefix,"veg");
    if (!read(newArea, file, logFile, EVU, false)) {
      return null;
    }

    if (!hasAttributes) {
      file = Utility.makeSuffixedPathname(prefix,"","attributes");
      if (!file.exists()) {
        hasAttributes = false;
        return newArea;
      }
      logFile = Utility.makeUniqueLogFile(prefix,"vegatt");
      attributesAdded = read(newArea,file,logFile,EVU,true);
      if (!attributesAdded) { return null; }
    }

    hasAttributes = attributesAdded;
    return newArea;
  }

  private boolean read(Area newArea, File inputFile, File logFile, int kind, boolean attribOnly) {
    BufferedReader fin;
    PrintWriter    log;
    boolean        success;
    RelationParser parser;

    try {
      log = new PrintWriter(new FileWriter(logFile));
    }
    catch (IOException e) {
      e.printStackTrace();
      System.out.println("Could not Open log file for writing: logFile");
      return false;
    }

    try {
      fin = new BufferedReader(new FileReader(inputFile));

      if (!attribOnly) {
        switch (kind) {
          case EVU:
            parser = new ParseNeighbors();
            success = parser.readSection(newArea, fin, log);
            break;
          case ELU:
            parser = new ParseLandNeighbors();
            success = parser.readSection(newArea, fin, log);
            break;
          default:
            success = false;
        }
        if (!success) {
          fin.close();
          log.flush();
          log.close();
          return false;
        }
      }
      if (hasAttributes || attribOnly) {
        switch (kind) {
          case EVU:
            readAttributes(newArea,fin,log);
            break;
          case ELU:
            readLandAttributes(newArea,fin,log);
            break;
        }
      }
      fin.close();
    }
    catch (ParseError e) {
      log.println("The following error occurred while trying to create the area:");
      log.println(e.msg);
    }
    catch (FileNotFoundException e) {
      log.println("Could not open file: " + inputFile);
    }
    catch (IOException e) {
      log.println("The following error occurred while trying to create the area:");
      e.printStackTrace(log);
    }
    log.flush();
    log.close();

    return true;
  }

  /**
   *
   * @param filename Name of the file that should have a corresponding log
   * @param suffix optional
   * @return PrintWriter open to log file
   */
//  private PrintWriter openLog(File filename, String suffix) {
//    PrintWriter log;
//    File prefix = Utility.stripExtension(filename);
//    File logFile = Utility.makeUniqueLogFile(prefix, suffix);
//    try {
//      log = new PrintWriter(new FileWriter(logFile));
//      return log;
//    } catch (IOException e) {
//      e.printStackTrace();
//      System.out.println("Could not Open log file for writing: logFile");
//    }
//  }
}
