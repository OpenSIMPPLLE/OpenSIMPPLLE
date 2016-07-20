/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.util.Vector;
import java.io.*;


/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class contains methods for FeasibilityLogic, a type of treatment logic data.  This will help decide whether a treatment
 * is feasible or not.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller

 * @see simpplle.comcode.TreatmentLogicData
 */

public class FeasibilityLogic extends TreatmentLogicData {
  private static final String CHECK_ADJ_SS_FUNCTION = "CheckAdjacentSS";
  private static final String CHECK_ADJ_SS_DESC =
    "Checks to see if the units adjacent to the treated unit are Seedling Sapling.\n" +
    "Return true if they are.";
  private static final String IS_WEED_CANDIDATE_FUNCTION = "IsWeedCandidate";
  private static final String IS_WEED_CANDIDATE_DESC =
    "Check to see if the unit is a candidate for weeds.";

  private static final String feasibilityFunctions[] = {
    CHECK_ADJ_SS_FUNCTION, IS_WEED_CANDIDATE_FUNCTION };
  private static final String feasibilityFunctionDesc[] = {
    CHECK_ADJ_SS_DESC, IS_WEED_CANDIDATE_DESC };

  public boolean feasible;
/**
 * Constructor.  initializes feasibility to true
 */
  public FeasibilityLogic() {
    super();
    feasible = true;
  }

  public static String[] getFeasibilityFunctions() { return feasibilityFunctions; }
  public static String[] getFeasibilityFunctionDesc() { return feasibilityFunctionDesc; }

  public static String getFeasibilityFunctionDesc(String feasibilityFunction) {
    if (feasibilityFunction.equals(CHECK_ADJ_SS_FUNCTION)) {
      return CHECK_ADJ_SS_DESC;
    }
    else if (feasibilityFunction.equals(IS_WEED_CANDIDATE_FUNCTION)) {
      return IS_WEED_CANDIDATE_DESC;
    }
    else {
      return "";
    }
  }
  /*
  public boolean useHtGrp()        { return useHtGrp;  }
  public void setUseHtGrp(boolean value) { useHtGrp = value; }

  public boolean useSpecies()      { return useSpecies;  }
  public void setUseSpecies(boolean value) { useSpecies = value; }

  public boolean useSizeClass()    { return useSizeClass;  }
  public void setUseSizeClass(boolean value) { useSizeClass = value; }

  public boolean useDensity()      { return useDensity;  }
  public void setUseDensity(boolean value) { useDensity = value; }

  public boolean useState()        { return useState; }
  public void setUseState(boolean value) { useState = value; }

  public boolean useCallFunction() { return useCallFunction; }
  public void setUseCallFunction(boolean value) { useCallFunction = value; }
  */

    /**
     * Sets the evaluation order string.  As with change logic the order is Habitat Type Group, Species, Size Class, Density, and Vegetative Type.  But it adds a
     * 6th value for function call which is different from Change Logic.
     * @return
     */
  public String[] makeEvalOrderList() {
    String[] theList = new String[6];

    theList[getHtGrpEvalPos()]        = "Habitat Type Group";
    theList[getSpeciesEvalPos()]      = "Species";
    theList[getSizeClassEvalPos()]    = "Size Class";
    theList[getDensityEvalPos()]      = "Density";
    theList[getStateEvalPos()]        = "Vegetative Type";
    theList[getCallFunctionEvalPos()] = "Function Call";

    return theList;
  }
/**
 * sets the feasibility of a treatment, and markes the treatment logic changed
 * @param bool true if feasible
 */
  public void setFeasible(boolean bool) {
    if (feasible == bool) { return; }
    feasible = bool;
    Treatment.markLogicChanged();
  }
  /**
   * gets feasibility
   * @return true if feasible
   */
  public boolean isFeasible() { return feasible; }
/**
 * Gets the valid habitat type groups from the treatment logic data.
 * @return
 */
  public Vector getValidHabitatTypeGroups() {
    if (isEmptyVector(htGrpItems)) { return null; }

    if ((oneOfHtGrp && feasible) || (oneOfHtGrp == false && feasible == false)) {
      return htGrpItems;
    }
    else {
      String[] groups = HabitatTypeGroup.getLoadedGroupNames();
      Vector   v = new Vector();
      HabitatTypeGroupType group;
      for(int i=0; i<groups.length; i++) {
        group = HabitatTypeGroupType.get(groups[i]);
        if (htGrpItems.contains(group) == false) {
          v.addElement(group);
        }
      }
      return v;
    }
  }
/**
 * uses the Habitat type species vector, checks the species with treatment logic to see if valid then create a new vector of valid species  
 * @return vector with species
 */
  public Vector getValidSpecies() {
    if (isEmptyVector(speciesItems)) { return null; }

    if ((oneOfSpecies && feasible) || (oneOfSpecies == false && feasible == false)) {
      return speciesItems;
    }
    else {
      Vector allSpecies = HabitatTypeGroup.getValidSpecies();
      Vector   v = new Vector();
      Species  species;
      for(int i=0; i<allSpecies.size(); i++) {
        species = (Species)allSpecies.elementAt(i);
        if (speciesItems.contains(species) == false) {
          v.addElement(species);
        }
      }
      return v;
    }
  }
/**
 * uses the Habitat type sizeClass vector, checks against treatment size class to see if valid then creates a new vector of valid size class 
 * @return the valid size class vector
 */
  public Vector getValidSizeClass() {
    if (isEmptyVector(sizeClassItems)) { return null; }

    if ((oneOfSizeClass && feasible) || (oneOfSizeClass == false && feasible == false)) {
      return sizeClassItems;
    }
    else {
      Vector allSizeClass = HabitatTypeGroup.getValidSizeClass();
      Vector   v = new Vector();
      SizeClass  sizeClass;
      for(int i=0; i<allSizeClass.size(); i++) {
        sizeClass = (SizeClass)allSizeClass.elementAt(i);
        if (sizeClassItems.contains(sizeClass) == false) {
          v.addElement(sizeClass);
        }
      }
      return v;
    }
  }
/**
 * If there are no density items
 * @return
 */
  public Vector getValidDensity() {
    if (isEmptyVector(densityItems)) { return null; }

    if ((oneOfDensity && feasible) || (oneOfDensity == false && feasible == false)) {
      return densityItems;
    }
    else {
      Vector allDensity = HabitatTypeGroup.getValidDensity();
      Vector   v = new Vector();
      Density  density;
      for(int i=0; i<allDensity.size(); i++) {
        density = (Density)allDensity.elementAt(i);
        if (densityItems.contains(density) == false) {
          v.addElement(density);
        }
      }
      return v;
    }
  }
/**
 * reads in csv file and separates out the treatment function calls and feasibility value 
 * @param fin
 * @throws IOException caught in gui
 * @throws ParseError caught in gui
 * TODO differentiate error's by better wording
 */
  public void read(BufferedReader fin)
    throws IOException, ParseError
  {
    String line = fin.readLine();
    StringTokenizerPlus strTok = new StringTokenizerPlus(line,",");

    super.read(strTok);

    // *** Call Function Section ***
    if (readOldFile == false || readZoneFile) {
      this.useFunctionCall = Boolean.valueOf(strTok.getToken()).booleanValue();
    }
    String str = strTok.getToken();
    if (str != null && str.equals(CHECK_ADJ_SS_FUNCTION)) {
      functionCall = CHECK_ADJ_SS_FUNCTION;
    }
    else if (str != null && str.equals(IS_WEED_CANDIDATE_FUNCTION)) {
      functionCall = IS_WEED_CANDIDATE_FUNCTION;
    }
    else {
      functionCall = null;
    }

    functionCallEvalPos = strTok.getIntToken();
    functionCallEvalPos = 5;  // Until user changeable.

    // *** Feasible Section ***
    feasible = Boolean.valueOf(strTok.getToken()).booleanValue();
  }
/**
 * save feasibility boolean in comma separated value
 *  */
  public void save(PrintWriter fout) {
    super.save(fout);

    fout.print(",");
    print(fout,feasible);
    fout.println();
  }
/**
 * 
 */
  protected Boolean doCallFunction(Evu evu) {
    if (useCallFunction() == false || evu == null) { return null; }

    boolean result;
    if (CHECK_ADJ_SS_FUNCTION.equalsIgnoreCase(functionCall)) {
      result =  Treatment.checkAdjacentSS(evu);
    }
    else if (IS_WEED_CANDIDATE_FUNCTION.equalsIgnoreCase(functionCall)) {
      result = evu.isWeedCandidate();
    }
    else { result = false; }

    return ( (result) ? Boolean.TRUE : Boolean.FALSE );
  }
/**
 * feasibility check according to TreatmentLogicData
 * @param evu evaluated vegetative unit to be check
 * @return true if feasible not feasible
 */
  public boolean evaluateFeasibility(Evu evu) {
    boolean result = evaluateConditionals(evu).booleanValue();

    if (result == false && isFeasible() == false) { return true; }
    else { return (result && isFeasible()); }
  }
  /**
   * prints to gui a boilerplate message about treatment feasibility
   */

  protected void printCode(StringBuffer strBuf) {
    super.printCode(strBuf);

    strBuf.append("\n");
    strBuf.append("-------------------------------------------------\n");
    strBuf.append("If above conditions are true then this treatment\n");
    strBuf.append("is feasible, but will only be applied if a change\n");
    strBuf.append("logic rule is true\n");
    strBuf.append("=================================================\n");
  }

}




