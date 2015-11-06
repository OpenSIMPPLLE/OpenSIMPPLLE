package simpplle.comcode;

import java.io.*;

/**
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class discusses treatment change and evaluation of treatment changes, it is an extension of TreatmentLogicData.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 * @since V2.2
 * 
 * @see simpplle.comcode.TreatmentLogicData 
 */


public class ChangeLogic extends TreatmentLogicData {
  private static final String FIRE_REGEN_FUNCTION = "fireRegen";
  private static final String FIRE_REGEN_DESC =
    "The new state for the treated unit will be the state returned by the\n" +
    "Fire Regeneration Function.";

  private static final String FIRE_TYPE_STATE_FUNCTION = "TypeOfFireNextState";
  private static final String FIRE_TYPE_STATE_DESC =
    "This function will determine the type of fire for a fire event in the treated Unit.\n" +
    "It will then determine the pathway next state for that fire.\n" +
    "The resulting state will be the new state for the treated unit.";

  private static final String NO_CHANGE_FUNCTION = "NoChange";
  private static final String NO_CHANGE_DESC =
    "This choice allows the treatment to be applied without changing\n" +
    "the state of the unit.\n" +
    "(e.g. If herbicide spraying is ineffective, it is applied with no state change.)";

  private static final String IS_EFFECTIVE_FUNCTION     = "IsEffective";
  private static final String IS_EFFECTIVE_FUNCTION_OLD = "IsEffectiveSpray";
  private static final String IS_EFFECTIVE_DESC =
    "Generates a random number to determine if Treatment Will be Effective.";

  private static final String NO_MATCHING_RULES_FUNCTION = "noMatchingRules";
  private static final String NO_MATCHING_RULES_DESC =
    "If all other rules fail to generate a new state then do this change.\n" +
    "  (default action if all rules fail is an INFEASIBLE treatment)";

  private static final String NO_SPREAD_SRF_REGEN = "noSpreadSrfRegen";
  private static final String NO_SPREAD_SRF_REGEN_DESC =
    "Create a Stand replacing Fire, call Fire Regeneration if appropriate\n" +
    "if no regen then state is next state from fire.";

  private static final String OPEN_ROADS = "openRoads";
  private static final String OPEN_ROADS_DESC = "Open Roads in treated Vegetation Unit";

  private static final String CLOSE_ROADS = "closeRoads";
  private static final String CLOSE_ROADS_DESC = "Close Roads in treated Vegetation Unit";

  private static final String OPEN_TRAILS = "openTrails";
  private static final String OPEN_TRAILS_DESC = "Open Trails in treated Vegetation Unit";

  private static final String CLOSE_TRAILS = "closeTrails";
  private static final String CLOSE_TRAILS_DESC = "Close Trails in treated Vegetation Unit";


  private static final String changeFunctions[] = {
    FIRE_REGEN_FUNCTION, FIRE_TYPE_STATE_FUNCTION, NO_CHANGE_FUNCTION,
    NO_SPREAD_SRF_REGEN, OPEN_ROADS, CLOSE_ROADS, OPEN_TRAILS, CLOSE_TRAILS };
  private static final String changeFunctionDesc[] = {
    FIRE_REGEN_DESC, FIRE_TYPE_STATE_DESC, NO_CHANGE_DESC,
    NO_SPREAD_SRF_REGEN_DESC, OPEN_ROADS_DESC, CLOSE_ROADS_DESC,
    OPEN_TRAILS_DESC, CLOSE_TRAILS_DESC };

  private static final String changeEvalFunctions[] = {
    IS_EFFECTIVE_FUNCTION, NO_MATCHING_RULES_FUNCTION};
  private static final String changeEvalFunctionDesc[] = {
    IS_EFFECTIVE_DESC, NO_MATCHING_RULES_DESC};

  public static final String TO_SPECIES       = "TO-SPECIES";
  public static final String TO_SIZE_CLASS    = "TO-SIZE-CLASS";
  public static final String TO_DENSITY       = "TO-DENSITY";
  public static final String TO_STATE         = "TO-STATE";
  public static final String TO_FUNCTION_CALL = "TO-FUNCTION-CALL";

  public String toChoice;
  public String toValue;

  private boolean isEffective;

  private int isEffectiveProb;
  private TreatmentType treatType;

  /**
   * Constructor. Inherits from superclass TreatmentLogicData
   */
  public ChangeLogic() {
    super();

    useFunctionCall = false;
    toChoice = TO_SPECIES;
    toValue  = null;
    isEffective = false;
    isEffectiveProb = Simulation.getRationalProbability(100);
  }

  public static String[] getChangeFunctions() { return changeFunctions; }
  public static String[] getChangeFunctionDesc() { return changeFunctionDesc; }

  /**
   * Gets the message to be displayed of type of treatment change.  These are descriptive final strings in plain english.  
   * @param changeFunction the type of change which occurs.  
   * @return message describing type of change.  If parameters does not match a type of change, will return empty string.
   */
  public static String getChangeFunctionDesc(String changeFunction) {
    if (changeFunction.equals(FIRE_REGEN_FUNCTION)) {
      return FIRE_REGEN_DESC;
    }
    else if (changeFunction.equals(FIRE_TYPE_STATE_FUNCTION)) {
      return FIRE_TYPE_STATE_DESC;
    }
    else if (changeFunction.equals(NO_CHANGE_FUNCTION)) {
      return NO_CHANGE_DESC;
    }
    else if (changeFunction.equals(NO_SPREAD_SRF_REGEN)) {
      return NO_SPREAD_SRF_REGEN_DESC;
    }
    else if (changeFunction.equals(OPEN_ROADS)) {
      return OPEN_ROADS_DESC;
    }
    else if (changeFunction.equals(CLOSE_ROADS)) {
      return CLOSE_ROADS_DESC;
    }
    else if (changeFunction.equals(OPEN_TRAILS)) {
      return OPEN_TRAILS_DESC;
    }
    else if (changeFunction.equals(CLOSE_TRAILS)) {
      return CLOSE_TRAILS_DESC;
    }
    else {
      return "";
    }
  }

  public static String[] getChangeEvalFunctions() { return changeEvalFunctions; }
  
  public static String[] getChangeEvalFunctionDesc() { return changeEvalFunctionDesc; }
/**
 * Gives a description of the changed function evaluation
 * @param changeFunction the type of change made 
 * @return returns the evaluation in plain english.  
 */
  public static String getChangeEvalFunctionDesc(String changeFunction) {
    if (changeFunction.equals(IS_EFFECTIVE_FUNCTION)) {
      return IS_EFFECTIVE_DESC;
    }
    else if (changeFunction.equals(NO_MATCHING_RULES_FUNCTION)) {
      return NO_MATCHING_RULES_DESC;
    }
    else {
      return "";
    }
  }

  public boolean isNoMatchRule() {
    return (NO_MATCHING_RULES_FUNCTION.equalsIgnoreCase(functionCall));
  }

  public String getToChoice() { return toChoice; }
  
  /**
   * Sets the treatment choice.  if treatment parameter is same as current treatment, no change is made, 
   * otherwise sets the treatment choice variable and marks treatment logic changed 
   * @param choice treatment choice
   */
  public void setToChoice(String choice) {
    if (toChoice.equals(choice)) { return; }
    toChoice = choice;
    toValue  = null;
    Treatment.markLogicChanged();
  }

  public String getToValue() { return toValue; }

  public int getIsEffectiveProb() {
    return isEffectiveProb;
  }
/**
 * Sets the treatment value if same as current value returns, else sets the value and marks the treatment logic changed
 * @param value the treatment value to be changed
 */
  public void setToValue(String value) {
    if (toValue != null && toValue.equals(value)) { return; }
    toValue = value;
    Treatment.markLogicChanged();
  }
/**
 * Sets the treatment type. if treatment is herbicide spraying, gets the effectiveness probability
 * @param treatType
 * @see simpplle.comcode.TreatmentType for a list of treatment types
 */
  public void setTreatType(TreatmentType treatType) {
    this.treatType = treatType;

    if (treatType == TreatmentType.HERBICIDE_SPRAYING) {
      isEffectiveProb = Simulation.getRationalProbability(75);
    }
  }

  public void setIsEffectiveProb(int isEffectiveProb) {
    this.isEffectiveProb = isEffectiveProb;
  }

    /**
     * Creates a string of evaluation order for treatement change logic.  These are Habitat Type Group, Species, Size Class, Density, and Vegeatative Type.
     * @return the string array
     */
  public String[] makeEvalOrderList() {
    String[] theList = new String[5];

    theList[getHtGrpEvalPos()]     = "Habitat Type Group";
    theList[getSpeciesEvalPos()]   = "Species";
    theList[getSizeClassEvalPos()] = "Size Class";
    theList[getDensityEvalPos()]   = "Density";
    theList[getStateEvalPos()]     = "Vegetative Type";
    theList[getCallFunctionEvalPos()] = "Call Function";

    return theList;
  }

  public void read(String line)
    throws IOException, ParseError
  {
    StringTokenizerPlus strTok = new StringTokenizerPlus(line,",");

    super.read(strTok);
    if (readOldFile == false || readZoneFile) {
      useFunctionCall = Boolean.valueOf(strTok.getToken()).booleanValue();
      String str = strTok.getToken();
      if (str != null && str.equals(IS_EFFECTIVE_FUNCTION_OLD)) {
        functionCall = IS_EFFECTIVE_FUNCTION;
      }
      if (str != null && str.equals(IS_EFFECTIVE_FUNCTION)) {
        functionCall = IS_EFFECTIVE_FUNCTION;
      }
      else if (str != null && str.equals(NO_MATCHING_RULES_FUNCTION)) {
        functionCall = NO_MATCHING_RULES_FUNCTION;
      }
      else {
        functionCall = null;
      }

      functionCallEvalPos = strTok.getIntToken();
    }
    functionCallEvalPos = 5;  // Until user changeable.

    String str = strTok.getToken();
    if (str.equals(TO_SPECIES))            { toChoice = TO_SPECIES; }
    else if (str.equals(TO_SIZE_CLASS))    { toChoice = TO_SIZE_CLASS; }
    else if (str.equals(TO_DENSITY))       { toChoice = TO_DENSITY; }
    else if (str.equals(TO_STATE))         { toChoice = TO_STATE; }
    else if (str.equals(TO_FUNCTION_CALL)) { toChoice = TO_FUNCTION_CALL; }
    else {
      throw new ParseError("Invalid To Choice in input file.");
    }

    toValue = strTok.getToken();

    if (strTok.hasMoreTokens()) {
      isEffectiveProb = strTok.getIntToken(10000);
    }
  }

  public void save(PrintWriter fout) {
    super.save(fout);

    fout.print(",");
    print(fout,toChoice);
    fout.print(",");
    print(fout,toValue);
    fout.print(",");
    print(fout,isEffectiveProb);
    fout.println();
  }

  public boolean isEffectiveCallFunction() {
    return (IS_EFFECTIVE_FUNCTION.equalsIgnoreCase(functionCall));
  }
  protected Boolean doCallFunction(Evu evu) {
    if (useCallFunction() == false) { return null; }

    isEffective = false;
    boolean result;
    if (IS_EFFECTIVE_FUNCTION.equalsIgnoreCase(functionCall)) {
      result = Treatment.isEffective(evu,isEffectiveProb);
      isEffective = result;
    }
    else if (NO_MATCHING_RULES_FUNCTION.equalsIgnoreCase(functionCall)) {
      /* When rules are processed these rules are seperated from the others.
         therefore we want it to always be true when it is processed.
         This functions' primary purpose is to serve as a way of identifying
         these rules.
      */
      result = true;
    }
    else { result = false; }

    return ( (result) ? Boolean.TRUE : Boolean.FALSE );
  }

    /**
     * Returns the new state resulting from the change rule.
     * @param treatment treatment being done
     * @param evu Existing Vegetative Unit treatment is being done on.
     * @return string of next state.
     */
  public String doChange(Treatment treatment, Evu evu) {
    RegionalZone zone = Simpplle.getCurrentZone();
    Boolean      result = evaluateConditionals(evu);
    TreatmentType treatType = treatment.getType();

    if (isEffective) { treatment.setStatus(Treatment.EFFECTIVE); }

    if (result != null && result.booleanValue() == false) { return null; }

    VegSimStateData state = evu.getState();
    if (state == null) { return null; }

    VegetativeType newState = null;
    if (toChoice.equals(TO_SPECIES) || toChoice.equals(TO_SIZE_CLASS) ||
        toChoice.equals(TO_DENSITY) || toChoice.equals(TO_STATE)) {
      return toValue;
    }
    else if (toChoice.equals(TO_FUNCTION_CALL)) {
      if (FIRE_REGEN_FUNCTION.equalsIgnoreCase(toValue)) {
        newState = simpplle.comcode.process.FireEvent.regen(evu.getDominantLifeform(),evu);
        return (newState != null) ? newState.toString() : null;
      }
      else if (FIRE_TYPE_STATE_FUNCTION.equalsIgnoreCase(toValue)) {
        ProcessType fireProcess = simpplle.comcode.process.FireEvent.getTypeOfFire(zone,evu,evu.getDominantLifeform());
        if (fireProcess == ProcessType.NONE) {
          return null;
        }

        newState = state.getVeg().getProcessNextState(fireProcess);
        return (newState != null) ? newState.toString() : null;
      }
      else if (NO_CHANGE_FUNCTION.equalsIgnoreCase(toValue)) {
        newState = state.getVeg();
        return (newState != null) ? newState.toString() : null;
      }
      else if (NO_SPREAD_SRF_REGEN.equalsIgnoreCase(toValue)) {
        if ((treatType == TreatmentType.ENCROACHMENT_BURN ||
             treatType == TreatmentType.ENCROACHMENT_CUT_AND_BURN) &&
             simpplle.comcode.process.FireEvent.isRegenState(zone,evu)) {
          newState = simpplle.comcode.process.FireEvent.regen(evu.getDominantLifeform(),evu);
          if (newState != null) { return newState.toString(); }
        }

        newState = state.getVeg().getProcessNextState(Process.findInstance(ProcessType.STAND_REPLACING_FIRE));
        return (newState != null) ? newState.toString() : null;
      }
      else if (OPEN_ROADS.equalsIgnoreCase(toValue)) {
        newState = null;
        if (evu.hasRoadUnits()) {
          evu.openRoads();
          newState = state.getVeg();
        }
        return (newState != null) ? newState.toString() : null;
      }
      else if (CLOSE_ROADS.equalsIgnoreCase(toValue)) {
        newState = null;
        if (evu.hasRoadUnits()) {
          evu.closeRoads();
          newState = state.getVeg();
        }
        return (newState != null) ? newState.toString() : null;
      }
      else if (OPEN_TRAILS.equalsIgnoreCase(toValue)) {
        newState = null;
        if (evu.hasTrailUnits()) {
          evu.openTrails();
          newState = state.getVeg();
        }
        return (newState != null) ? newState.toString() : null;
      }
      else if (CLOSE_TRAILS.equalsIgnoreCase(toValue)) {
        newState = null;
        if (evu.hasTrailUnits()) {
          evu.closeTrails();
          newState = state.getVeg();
        }
        return (newState != null) ? newState.toString() : null;
      }
      else { return null; }
    }
    else {
      return null;
    }
  }

    /**
     * prints out the result of rule if conditions entered are true
     * @param strBuf
     */
  protected void printCode(StringBuffer strBuf) {
    strBuf.append("\n");
    strBuf.append("***********************\n");
    strBuf.append("**** Begin of Rule ****\n");
    super.printCode(strBuf);

    strBuf.append("-----------------------------------------\n");
    strBuf.append("If above conditions are true then change:\n");
    if (toChoice.equals(TO_SPECIES)) {
      strBuf.append("  SPECIES --> ");
    }
    else if (toChoice.equals(TO_SIZE_CLASS)) {
      strBuf.append("  SIZE CLASS --> ");
    }
    else if (toChoice.equals(TO_DENSITY)) {
      strBuf.append("  DENSITY --> ");
    }
    else if (toChoice.equals(TO_STATE)) {
      strBuf.append("  CURRENT STATE --> ");
    }
    else if (toChoice.equals(TO_FUNCTION_CALL)) {
      strBuf.append("  STATE TO RESULT OF CALLING --> ");
    }
    else {
      strBuf.append("  Nothing specified???");
    }
    strBuf.append(toValue + "\n");
  }

}



