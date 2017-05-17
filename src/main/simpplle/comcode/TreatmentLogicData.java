/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.io.*;
import java.util.Vector;

/**
 * Originally intended this class to be internal to Treatment.
 * However, compiler would not allow me to create an instance from
 * a static context.  Don't really know why, must have something to
 * do with it be an internal class.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */
public abstract class TreatmentLogicData {
  private static final int NUM_EVAL = 6;

  public static final String AND = "AND";
  public static final String OR  = "OR";

  protected String[] boolChoices;

  protected boolean useHtGrp;
  protected boolean oneOfHtGrp;
  protected Vector  htGrpItems;
  protected int     htGrpEvalPos;

  protected boolean useSpecies;
  protected boolean oneOfSpecies;
  protected Vector  speciesItems;
  protected int     speciesEvalPos;

  protected boolean useSizeClass;
  protected boolean oneOfSizeClass;
  protected Vector  sizeClassItems;
  protected int     sizeClassEvalPos;

  protected boolean useDensity;
  protected boolean oneOfDensity;
  protected Vector  densityItems;
  protected int     densityEvalPos;

  protected boolean useState;
  protected String  state;
  protected int     stateEvalPos;

  protected boolean useFunctionCall;
  protected String  functionCall;
  protected int     functionCallEvalPos;

  protected static final boolean readOldFile = false;
  public static boolean readZoneFile;

  protected TreatmentLogicData() {
    useHtGrp        = false;
    useSpecies      = false;
    useSizeClass    = false;
    useDensity      = false;
    useState        = false;
    useFunctionCall = false;

    oneOfHtGrp     = true;
    oneOfSpecies   = true;
    oneOfSizeClass = true;
    oneOfDensity   = true;

    htGrpEvalPos        = 0;
    speciesEvalPos      = 1;
    sizeClassEvalPos    = 2;
    densityEvalPos      = 3;
    stateEvalPos        = 4;
    functionCallEvalPos = 5;

    boolChoices = new String[] {AND,AND,AND,AND,AND};
  }

  public abstract String[] makeEvalOrderList();

  public boolean isEmptyVector(Vector v) { return (v == null || v.size() == 0); }

  public void setHtGrpEvalPos(int change) {
    htGrpEvalPos += change;
    if (speciesEvalPos == htGrpEvalPos) { speciesEvalPos -= change; }
    else if (sizeClassEvalPos    == htGrpEvalPos) { sizeClassEvalPos    -= change; }
    else if (densityEvalPos      == htGrpEvalPos) { densityEvalPos      -= change; }
    else if (stateEvalPos        == htGrpEvalPos) { stateEvalPos        -= change; }
    else if (functionCallEvalPos == htGrpEvalPos) { functionCallEvalPos -= change; }
    Treatment.markLogicChanged();
  }
  public int getHtGrpEvalPos() { return htGrpEvalPos; }

  public void setSpeciesEvalPos(int change) {
    speciesEvalPos += change;
    if (htGrpEvalPos == speciesEvalPos) { htGrpEvalPos -= change; }
    else if (sizeClassEvalPos    == speciesEvalPos) { sizeClassEvalPos    -= change; }
    else if (densityEvalPos      == speciesEvalPos) { densityEvalPos      -= change; }
    else if (stateEvalPos        == speciesEvalPos) { stateEvalPos        -= change; }
    else if (functionCallEvalPos == speciesEvalPos) { functionCallEvalPos -= change; }
    Treatment.markLogicChanged();
  }

  public int getSpeciesEvalPos() { return speciesEvalPos; }

  public void setSizeClassEvalPos(int change) {
    sizeClassEvalPos += change;
    if (htGrpEvalPos == sizeClassEvalPos) { htGrpEvalPos -= change; }
    else if (speciesEvalPos      == sizeClassEvalPos) { speciesEvalPos      -= change; }
    else if (densityEvalPos      == sizeClassEvalPos) { densityEvalPos      -= change; }
    else if (stateEvalPos        == sizeClassEvalPos) { stateEvalPos        -= change; }
    else if (functionCallEvalPos == sizeClassEvalPos) { functionCallEvalPos -= change; }
    Treatment.markLogicChanged();
  }

  public int getSizeClassEvalPos() { return sizeClassEvalPos; }

  public void setDensityEvalPos(int change) {
    densityEvalPos += change;
    if (htGrpEvalPos == densityEvalPos) { htGrpEvalPos -= change; }
    else if (speciesEvalPos      == densityEvalPos) { speciesEvalPos      -= change; }
    else if (sizeClassEvalPos    == densityEvalPos) { sizeClassEvalPos    -= change; }
    else if (stateEvalPos        == densityEvalPos) { stateEvalPos        -= change; }
    else if (functionCallEvalPos == densityEvalPos) { functionCallEvalPos -= change; }
    Treatment.markLogicChanged();
  }

  public int getDensityEvalPos() { return densityEvalPos; }

  public void setStateEvalPos(int change) {
    stateEvalPos += change;
    if (htGrpEvalPos == stateEvalPos) { htGrpEvalPos -= change; }
    else if (speciesEvalPos      == stateEvalPos) { speciesEvalPos      -= change; }
    else if (sizeClassEvalPos    == stateEvalPos) { sizeClassEvalPos    -= change; }
    else if (densityEvalPos      == stateEvalPos) { densityEvalPos      -= change; }
    else if (functionCallEvalPos == stateEvalPos) { functionCallEvalPos -= change; }
    Treatment.markLogicChanged();
  }

  public int getStateEvalPos() { return stateEvalPos; }

  public void setCallFunctionEvalPos(int change) {
    functionCallEvalPos += change;
    if (htGrpEvalPos == functionCallEvalPos) { htGrpEvalPos -= change; }
    else if (speciesEvalPos   == functionCallEvalPos) { speciesEvalPos   -= change; }
    else if (sizeClassEvalPos == functionCallEvalPos) { sizeClassEvalPos -= change; }
    else if (densityEvalPos   == functionCallEvalPos) { densityEvalPos   -= change; }
    else if (stateEvalPos     == functionCallEvalPos) { stateEvalPos     -= change; }
    Treatment.markLogicChanged();
  }

  public int getCallFunctionEvalPos() { return functionCallEvalPos; }

  public String getBoolChoice(int evalPos) { return boolChoices[evalPos]; }
  public void setBoolChoice(int evalPos, String value) {
    if (boolChoices[evalPos].equals(value)) { return; }
    boolChoices[evalPos] = value;
    Treatment.markLogicChanged();
  }
  public void resetBoolChoice(int evalPos) {
    if (boolChoices[evalPos].equals(AND)) { return; }
    boolChoices[evalPos] = AND;
    Treatment.markLogicChanged();
  }

  public String getState() { return state; }
  public void setState(String newState) {
    if (state != null && state.equals(newState)) { return; }
    state = newState;
    Treatment.markLogicChanged();
  }
  public void clearState() {
    if (state == null) { return; }
    state = null;
    Treatment.markLogicChanged();
  }

  public String getCallFunction() { return functionCall; }
  public void setCallFunction(String value) {
    if (functionCall != null && functionCall.equals(value)) { return; }
    functionCall = value;
    Treatment.markLogicChanged();
  }
  public void clearCallFunction() {
    if (functionCall == null) { return; }
    functionCall = null;
    Treatment.markLogicChanged();
  }

  public boolean useHtGrp() {
    useHtGrp = !isEmptyVector(htGrpItems);
    return useHtGrp;
  }
  public void setUseHtGrp(boolean value) { useHtGrp = value; }

  public boolean useSpecies() {
    useSpecies = !isEmptyVector(speciesItems);
    return useSpecies;
  }
  public void setUseSpecies(boolean value) { useSpecies = value; }

  public boolean useSizeClass() {
    useSizeClass = !isEmptyVector(sizeClassItems);
    return useSizeClass;
  }
  public void setUseSizeClass(boolean value) { useSizeClass = value; }

  public boolean useDensity() {
    useDensity = !isEmptyVector(densityItems);
    return useDensity;
  }
  public void setUseDensity(boolean value) { useDensity = value; }

  public boolean useState() {
    useDensity = (state != null);
    return useState;
  }
  public void setUseState(boolean value) { useState = value; }

  public boolean useCallFunction() {
    useFunctionCall = (functionCall != null);
    return useFunctionCall;
  }
  public void setUseCallFunction(boolean value) { useFunctionCall = value; }


  public boolean isOneOfHtGrp() { return oneOfHtGrp; }
  public void setOneOfHtGrp(boolean value) {
    if (oneOfHtGrp == value) { return; }
    oneOfHtGrp = value;
    Treatment.markLogicChanged();
  }

  public boolean isOneOfSpecies() { return oneOfSpecies; }
  public void setOneOfSpecies(boolean value) {
    if (oneOfSpecies == value) { return; }
    oneOfSpecies = value;
    Treatment.markLogicChanged();
  }

  public boolean isOneOfSizeClass() { return oneOfSizeClass; }
  public void setOneOfSizeClass(boolean value) {
    if (oneOfSizeClass == value) { return; }
    oneOfSizeClass = value;
    Treatment.markLogicChanged();
  }

  public boolean isOneOfDensity() { return oneOfDensity; }
  public void setOneOfDensity(boolean value) {
    if (oneOfDensity == value) { return; }
    oneOfDensity = value;
    Treatment.markLogicChanged();
  }

  public void addHtGrp(SimpplleType value) {
    if (htGrpItems == null) { htGrpItems = new Vector(); }
    htGrpItems.addElement((HabitatTypeGroupType)value);
    Treatment.markLogicChanged();
  }
  public void removeHtGrp(SimpplleType value) {
    if (htGrpItems == null) { return; }
    htGrpItems.removeElement((HabitatTypeGroupType)value);
    Treatment.markLogicChanged();
  }
  public boolean isSelectedHtGrp(SimpplleType value) {
    if (htGrpItems == null) { return false; }
    return htGrpItems.contains((HabitatTypeGroupType)value);
  }
  public void clearHtGrpItems() {
    if (htGrpItems == null) { return; }
    htGrpItems.clear();
    Treatment.markLogicChanged();
  }

  public void addSpecies(SimpplleType value) {
    if (speciesItems == null) { speciesItems = new Vector(); }
    speciesItems.addElement((Species)value);
    Treatment.markLogicChanged();
  }
  public void removeSpecies(SimpplleType value) {
    if (speciesItems == null) { return; }
    speciesItems.removeElement((Species)value);
    Treatment.markLogicChanged();
  }
  public boolean isSelectedSpecies(SimpplleType value) {
    if (speciesItems == null) { return false; }
    return speciesItems.contains((Species)value);
  }
  public void clearSpeciesItems() {
    if (speciesItems == null) { return; }
    speciesItems.clear();
    Treatment.markLogicChanged();
  }

  public void addSizeClass(SimpplleType value) {
    if (sizeClassItems == null) { sizeClassItems = new Vector(); }
    sizeClassItems.addElement((SizeClass)value);
    Treatment.markLogicChanged();
  }
  public void removeSizeClass(SimpplleType value) {
    if (sizeClassItems == null) { return; }
    sizeClassItems.removeElement((SizeClass)value);
    Treatment.markLogicChanged();
  }
  public boolean isSelectedSizeClass(SimpplleType value) {
    if (sizeClassItems == null) { return false; }
    return sizeClassItems.contains((SizeClass)value);
  }
  public void clearSizeClassItems() {
    if (sizeClassItems == null) { return; }
    sizeClassItems.clear();
    Treatment.markLogicChanged();
  }

  public void addDensity(SimpplleType value) {
    if (densityItems == null) { densityItems = new Vector(); }
    densityItems.addElement((Density)value);
    Treatment.markLogicChanged();
  }
  public void removeDensity(SimpplleType value) {
    if (densityItems == null) { return; }
    densityItems.removeElement((Density)value);
    Treatment.markLogicChanged();
  }
  public boolean isSelectedDensity(SimpplleType value) {
    if (densityItems == null) { return false; }
    return densityItems.contains((Density)value);
  }
  public void clearDensityItems() {
    if (densityItems == null) { return; }
    densityItems.clear();
    Treatment.markLogicChanged();
  }

  protected void print(PrintWriter fout, boolean bool) {
    fout.print(bool);
  }

  protected void read(StringTokenizerPlus strTok)
    throws IOException, ParseError
  {
    String str;
    Vector v;
    int    i;

    // *** Evaluation Order Bools Section ***
    v = strTok.getListValue();
    if (v != null) {
      for(i=0; i<v.size(); i++) {
        str = (String)v.elementAt(i);
        boolChoices[i] = (str.equals(AND)) ? AND : OR;
      }
    }

    // *** Habitat Type Group Section ***
    if (readOldFile == false || readZoneFile) {
      useHtGrp = Boolean.valueOf(strTok.getToken()).booleanValue();
    }
    oneOfHtGrp = Boolean.valueOf(strTok.getToken()).booleanValue();

    clearHtGrpItems();
    v = strTok.getListValue();
    if (v != null) {
      for(i=0; i<v.size(); i++) {
        str = (String)v.elementAt(i);
        HabitatTypeGroupType htGrpType = HabitatTypeGroupType.get(str);
        if (htGrpType == null) {
          HabitatTypeGroup htGrp = new HabitatTypeGroup(str);
          htGrpType = htGrp.getType();
        }
        addHtGrp(htGrpType);
      }
    }

    htGrpEvalPos = strTok.getIntToken();

    // *** Species Section ***
    if (readOldFile == false || readZoneFile) {
      useSpecies = Boolean.valueOf(strTok.getToken()).booleanValue();
    }
    oneOfSpecies = Boolean.valueOf(strTok.getToken()).booleanValue();

    clearSpeciesItems();
    v = strTok.getListValue();
    if (v != null) {
      for(i=0; i<v.size(); i++) {
        str = (String)v.elementAt(i);
        addSpecies(Species.get(str,true));
      }
    }

    speciesEvalPos = strTok.getIntToken();

    // *** Size Class Section ***
    if (readOldFile == false || readZoneFile) {
      useSizeClass = Boolean.valueOf(strTok.getToken()).booleanValue();
    }
    oneOfSizeClass  = Boolean.valueOf(strTok.getToken()).booleanValue();

    clearSizeClassItems();
    v = strTok.getListValue();
    if (v != null) {
      for(i=0; i<v.size(); i++) {
        str = (String)v.elementAt(i);
        addSizeClass(SizeClass.get(str,true));
      }
    }

    sizeClassEvalPos = strTok.getIntToken();

    // *** Density Section ***
    if (readOldFile == false || readZoneFile) {
      useDensity = Boolean.valueOf(strTok.getToken()).booleanValue();
    }
    oneOfDensity  = Boolean.valueOf(strTok.getToken()).booleanValue();

    clearDensityItems();
    v = strTok.getListValue();
    if (v != null) {
      for(i=0; i<v.size(); i++) {
        str = (String)v.elementAt(i);
        addDensity(Density.getOrCreate(str));
      }
    }

    densityEvalPos = strTok.getIntToken();

    // *** Vegetative Type Section ***
    if (readOldFile == false || readZoneFile) {
      useState = Boolean.valueOf(strTok.getToken()).booleanValue();
    }
    setState(strTok.getToken());
    stateEvalPos = strTok.getIntToken();

    /**
     * This code insures that things stay in the proper order until
     * user is able to change the order via the GUI.
     */
    htGrpEvalPos        = 0;
    speciesEvalPos      = 1;
    sizeClassEvalPos    = 2;
    densityEvalPos      = 3;
    stateEvalPos        = 4;

  }

  protected void print(PrintWriter fout, Vector v) {
    if (v == null || v.size() == 0) {
      fout.print("?");
      return;
    }
    int i;
    for(i=0; i<v.size(); i++) {
      if (i != 0) { fout.print(":"); }
      fout.print(((SimpplleType)v.elementAt(i)).toString());
    }
  }

  protected void print(PrintWriter fout, String str) {
    if (str == null) {
      fout.print("?");
    }
    else {
      fout.print(str);
    }
  }

  protected void print(PrintWriter fout, int num) {
    fout.print(num);
  }

  protected void print(PrintWriter fout, String[] values) {
    for(int i=0; i<values.length; i++) {
      if (i != 0) { fout.print(":"); }
      fout.print(values[i]);
    }
  }

  protected void printVectorCode(StringBuffer strBuf, Vector v) {
    int    numPrinted = 5;
    String str;

    strBuf.append(" (");
    for (int i=0; i<v.size(); i++) {
//      if ((numPrinted + 10) >= 70) {
//        strBuf.append("     \n");
//        numPrinted = 5;
//      }
      if (i != 0) { strBuf.append(", "); }
      str = ((SimpplleType)v.elementAt(i)).toString();
      strBuf.append(str);
//      numPrinted += str.length();
    }
    strBuf.append(")\n");
  }

  public String getPrintCode() {
    StringBuffer result = new StringBuffer();
    printCode(result);
    return result.toString();
  }

  public void doPrintCode(StringBuffer result) {
    printCode(result);
  }

  protected void printCode(StringBuffer result) {
    if ((useHtGrp() == false)     && (useSpecies() == false) &&
        (useSizeClass() == false) && (useDensity() == false) &&
        (useState() == false)     && (useCallFunction() == false))
    {
      result.append("No conditions on applying this rule\n");
      result.append("This rule will be evaluated only after conditional rules.\n");
      return;
    }

    StringBuffer[] strBuf = new StringBuffer[NUM_EVAL];
    boolean[]      selected = new boolean[NUM_EVAL];
    int evalPos;

    evalPos = getHtGrpEvalPos();
    strBuf[evalPos] = new StringBuffer();
    selected[evalPos] = (useHtGrp() && htGrpItems != null);

    if (useHtGrp() && htGrpItems != null) {
      strBuf[evalPos].append("\n");
      strBuf[evalPos].append("Ecological Grouping ");
      if (this.isOneOfHtGrp()) { strBuf[evalPos].append("<IS ONE OF>"); }
      else { strBuf[evalPos].append("<NOT ONE OF>"); }
      printVectorCode(strBuf[evalPos],htGrpItems);
    }

    evalPos = getSpeciesEvalPos();
    strBuf[evalPos]   = new StringBuffer();
    selected[evalPos] = (useSpecies() && speciesItems != null);

    if (useSpecies() && speciesItems != null) {
      strBuf[evalPos].append("\n");
      strBuf[evalPos].append("Species ");
      if (this.isOneOfSpecies()) { strBuf[evalPos].append("<IS ONE OF>"); }
      else { strBuf[evalPos].append("<NOT ONE OF>"); }
      printVectorCode(strBuf[evalPos],speciesItems);
    }

    evalPos = getSizeClassEvalPos();
    strBuf[evalPos] = new StringBuffer();
    selected[evalPos] = (useSizeClass() && sizeClassItems != null);

    if (useSizeClass() && sizeClassItems != null) {
      strBuf[evalPos].append("\n");
      strBuf[evalPos].append("Size Class ");
      if (this.isOneOfSizeClass()) { strBuf[evalPos].append("<IS ONE OF>"); }
      else { strBuf[evalPos].append("<NOT ONE OF>"); }
      printVectorCode(strBuf[evalPos],sizeClassItems);
    }

    evalPos = getDensityEvalPos();
    strBuf[evalPos] = new StringBuffer();
    selected[evalPos] = (useDensity() && densityItems != null);

    if (useDensity() && densityItems != null) {
      strBuf[evalPos].append("\n");
      strBuf[evalPos].append("Density ");
      if (this.isOneOfDensity()) { strBuf[evalPos].append("<IS ONE OF>"); }
      else { strBuf[evalPos].append("<NOT ONE OF>"); }
      printVectorCode(strBuf[evalPos],densityItems);
    }

    evalPos = getStateEvalPos();
    strBuf[evalPos] = new StringBuffer();
    selected[evalPos] = (useState() && state != null);

    if (useState() && state != null) {
      strBuf[evalPos].append("\n");
      strBuf[evalPos].append("Current State IS " + state + "\n");
    }

    evalPos = getCallFunctionEvalPos();
    strBuf[evalPos] = new StringBuffer();
    selected[evalPos] = (useCallFunction() && functionCall != null);

    if (useCallFunction() && functionCall != null) {
      strBuf[evalPos].append("\n");
      strBuf[evalPos].append("Call Function IS " + functionCall + "\n");
    }

    for (int i=0; i<strBuf.length; i++) {
      if (i != 0 && strBuf[i].length() != 0) {
        if (isPreviousEvalBoolSelected(selected,i)) {
          result.append("\n<" + boolChoices[i-1] + ">\n");
        }
      }
      result.append(strBuf[i].toString());
    }

  }

  private boolean isPreviousEvalBoolSelected(boolean[] selected, int evalPos) {
    for (int i=evalPos-1; i>=0 ; i--) {
      if (selected[i]) { return true; }
    }
    return false;
  }

  public void save(PrintWriter fout) {
    print(fout,boolChoices);

    fout.print(",");
    print(fout,useHtGrp);
    fout.print(",");
    print(fout,oneOfHtGrp);
    fout.print(",");
    print(fout,htGrpItems);
    fout.print(",");
    print(fout,htGrpEvalPos);

    fout.print(",");
    print(fout,useSpecies);
    fout.print(",");
    print(fout,oneOfSpecies);
    fout.print(",");
    print(fout,speciesItems);
    fout.print(",");
    print(fout,speciesEvalPos);

    fout.print(",");
    print(fout,useSizeClass);
    fout.print(",");
    print(fout,oneOfSizeClass);
    fout.print(",");
    print(fout,sizeClassItems);
    fout.print(",");
    print(fout,sizeClassEvalPos);

    fout.print(",");
    print(fout,useDensity);
    fout.print(",");
    print(fout,oneOfDensity);
    fout.print(",");
    print(fout,densityItems);
    fout.print(",");
    print(fout,densityEvalPos);

    fout.print(",");
    print(fout,useState);
    fout.print(",");
    print(fout,state);
    fout.print(",");
    print(fout,stateEvalPos);

    fout.print(",");
    print(fout,useFunctionCall);
    fout.print(",");
    print(fout,functionCall);
    fout.print(",");
    print(fout,functionCallEvalPos);
  }

  protected Boolean isMemberHtGrp(Evu evu) {
    HabitatTypeGroupType htGrp;
    htGrp = HabitatTypeGroupType.get(evu.getHabitatTypeGroup().toString());

    if (useHtGrp() == false) { return null; }

    boolean result;
    result = ((isOneOfHtGrp() && htGrpItems.contains(htGrp)) ||
              (isOneOfHtGrp() == false && htGrpItems.contains(htGrp) == false));
    return ( (result) ? Boolean.TRUE : Boolean.FALSE );
  }

  protected Boolean isMemberSpecies(Evu evu, VegSimStateData state) {
    if (state == null) { return null; }
    Species species = state.getVeg().getSpecies();

    if (useSpecies() == false) { return null; }

    boolean result;
    result = ((isOneOfSpecies() && speciesItems.contains(species)) ||
              (isOneOfSpecies() == false && speciesItems.contains(species) == false));
    return ( (result) ? Boolean.TRUE : Boolean.FALSE );
  }

  protected Boolean isMemberSizeClass(Evu evu, VegSimStateData state) {
    if (state == null) { return null; }
    SizeClass sizeClass = state.getVeg().getSizeClass();

    if (useSizeClass() == false) { return null; }

    boolean result;
    result = ((isOneOfSizeClass() && sizeClassItems.contains(sizeClass)) ||
              (isOneOfSizeClass() == false && sizeClassItems.contains(sizeClass) == false));
    return ( (result) ? Boolean.TRUE : Boolean.FALSE );
  }

  protected Boolean isMemberDensity(Evu evu, VegSimStateData state) {
    if (state == null) { return null; }
    Density density = state.getVeg().getDensity();

    if (useDensity() == false) { return null; }

    boolean result;
    result = ((isOneOfDensity() && densityItems.contains(density)) ||
              (isOneOfDensity() == false && densityItems.contains(density) == false));
    return ( (result) ? Boolean.TRUE : Boolean.FALSE );
  }

  protected Boolean isStateMatch(Evu evu, VegSimStateData state) {
    if (state == null) { return null; }
    VegetativeType vegType = state.getVeg();

    if (useState() == false) { return null; }

    boolean result = (state.equals(vegType.toString()));
    return ( (result) ? Boolean.TRUE : Boolean.FALSE );
  }

  protected abstract Boolean doCallFunction(Evu evu);

  public Boolean evaluateConditionals(Evu evu) {
    Boolean[] bools = new Boolean[] {null,null,null,null,null,null};
    Boolean   result;

    bools[htGrpEvalPos]        = isMemberHtGrp(evu);
    VegSimStateData state = evu.getState();
    bools[speciesEvalPos]      = isMemberSpecies(evu,state);
    bools[sizeClassEvalPos]    = isMemberSizeClass(evu,state);
    bools[densityEvalPos]      = isMemberDensity(evu,state);
    bools[stateEvalPos]        = isStateMatch(evu,state);
    bools[functionCallEvalPos] = doCallFunction(evu);

    result = evaluate(bools[0],bools[1],boolChoices[0]);
    result = evaluate(result,bools[2],boolChoices[1]);
    result = evaluate(result,bools[3],boolChoices[2]);
    result = evaluate(result,bools[4],boolChoices[3]);
    result = evaluate(result,bools[5],boolChoices[4]);
    if (result == null) { result = Boolean.TRUE; }

    return result;
  }

  protected Boolean evaluate(Boolean bools1, Boolean bools2, String oper) {
    if (bools1 == null) { return bools2; }
    if (bools2 == null) { return bools1; }

    boolean result;
    if (oper.equals(AND)) { result = (bools1.booleanValue() && bools2.booleanValue()); }
    else { result =  (bools1.booleanValue() || bools2.booleanValue()); }

    return ( (result) ? Boolean.TRUE : Boolean.FALSE );
  }

}


