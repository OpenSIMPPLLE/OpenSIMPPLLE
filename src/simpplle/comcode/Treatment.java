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
import java.util.zip.*;

/**
 * This class defines a Process treatment.  Results for treatment are Effective, Applied, Not Feasible, and not applied.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */
public class Treatment  implements Externalizable {
  static final long serialVersionUID = -3198831755032476583L;
  static final int  version          = 1;

  // Key   = TreatmentType instance
  // Value = TreatmentLogic instance
  private static Hashtable treatmentLogicHt;

  private static ArrayList legalTreatments;

  private static boolean logicChanged;

  protected int            id;
  protected int            status;
  protected VegetativeType newState;
  protected VegetativeType savedState;
  protected int            timeStep;
  private   TreatmentType  treatmentType;
  private   boolean        preventReTreatment;
  private   int            preventReTreatmentTimeSteps;

  public static final int EFFECTIVE   = 0;
  public static final int APPLIED     = 1;
  public static final int INFEASIBLE  = 2;
  public static final int NOT_APPLIED = 3;

  public static final String EFFECTIVE_STR   = "EFFECTIVE";
  public static final String APPLIED_STR     = "APPLIED";
  public static final String INFEASIBLE_STR  = "INFEASIBLE";
  public static final String NOT_APPLIED_STR = "NOT_APPLIED";

  protected static final int HTGRP      = 0;
  protected static final int SPECIES    = 1;
  protected static final int SIZE_CLASS = 2;
  protected static final int DENSITY    = 3;

  protected static final String SEPARATOR = ";";
  protected static final char   NODATA    = '?';

  public Treatment () {
    status                      = NOT_APPLIED;
    preventReTreatment          = false;
    preventReTreatmentTimeSteps = 5;
  }

  public Treatment (TreatmentType treatmentType) {
    this();
    this.treatmentType = treatmentType;
  }

  /**
   * Create a treatment which will represent an initial treatment.
   *
   * @param treatType The new treatment
   * @param savedState The state resulting from treatment
   */
  public static Treatment createInitialTreatment(TreatmentType treatType,
                                                 VegetativeType savedState)
  {
    Treatment treat  = new Treatment(treatType);
    treat.savedState = savedState;
    treat.timeStep   = 0;

    if (treatType == TreatmentType.HERBICIDE_SPRAYING) {
      treat.status = EFFECTIVE;
    }
    else {
      treat.status = APPLIED;
    }

    return treat;
  }

    /**
     * Gets the treatment type
     * @return
     */
  public TreatmentType getType() { return treatmentType; }

  public static boolean hasLogicChanged() { return logicChanged; }

    /**
     * Marks system knowledge changed with the new treatment logic
     */
  public static void markLogicChanged() {
    setLogicChanged(true);
    SystemKnowledge.markChanged(SystemKnowledge.TREATMENT_LOGIC);
  }
  private static void setLogicChanged(boolean value) { logicChanged = value; }

  private static void setLogicFile(File file) {
    SystemKnowledge.setFile(SystemKnowledge.TREATMENT_LOGIC,file);
    SystemKnowledge.markChanged(SystemKnowledge.TREATMENT_LOGIC);
  }

    /**
     * Closes the treatment logic file by clearing the system knowledge file for treatment.
     */
  public static void closeLogicFile() {
    SystemKnowledge.clearFile(SystemKnowledge.TREATMENT_LOGIC);
  }

//  private static TreatmentLogic getTreatmentLogic(String treatmentName) {
//    return getTreatmentLogic(TreatmentType.get(treatmentName));
//  }
  private static TreatmentLogic getTreatmentLogic(TreatmentType treatment) {
    return (TreatmentLogic)treatmentLogicHt.get(treatment);
  }

    /**
     * Sets the feasibility of a treatment by setting the logic (treatment logic
     * @param treatment
     * @param feasibilityLogic
     */
  public static void setFeasibilityLogic(TreatmentType treatment, FeasibilityLogic feasibilityLogic) {
    TreatmentLogic treatLogic = (TreatmentLogic)treatmentLogicHt.get(treatment);
    treatLogic.feasibility = feasibilityLogic;
  }
  public static FeasibilityLogic getFeasibilityLogic(TreatmentType treatment) {
    return ( ((TreatmentLogic)treatmentLogicHt.get(treatment)).feasibility );
  }

  public static String[] getFeasibilityFunctions() {
    return FeasibilityLogic.getFeasibilityFunctions();
  }
  public static String[] getFeasibilityFunctionDesc() {
    return FeasibilityLogic.getFeasibilityFunctionDesc();
  }
  public static String getFeasibilityFunctionDesc(String feasibilityFunction) {
    return FeasibilityLogic.getFeasibilityFunctionDesc(feasibilityFunction);
  }


  public static ChangeLogic findChangeRule(TreatmentType treatment, ChangeLogic rule) {
    TreatmentLogic treatLogic = (TreatmentLogic) treatmentLogicHt.get(treatment);
    if (treatLogic == null) { return null; }

    ChangeLogic tmpRule;
    for(int i=0; i<treatLogic.change.size(); i++) {
      tmpRule = (ChangeLogic)treatLogic.change.elementAt(i);
      if (tmpRule == rule) {
        return tmpRule;
      }
    }
    return null;
  }

    /**
     * Changes a treatment rule by removing the rule from the treatment logic hash table (unless already null)
     * @param treatment the treatment being changed
     * @param rule
     */
  public static void removeChangeRule(TreatmentType treatment, ChangeLogic rule) {
    TreatmentLogic treatLogic = (TreatmentLogic) treatmentLogicHt.get(treatment);
    if (treatLogic == null) { return; }

    treatLogic.change.removeElement(rule);
    markLogicChanged();
  }

    /**
     *
     * @param treatment
     */
  public static void addChangeRule(TreatmentType treatment) {
    ChangeLogic rule = new ChangeLogic();
    rule.setTreatType(treatment);

    TreatmentLogic treatLogic = (TreatmentLogic)treatmentLogicHt.get(treatment);
    if (findChangeRule(treatment, rule) != null) { return; }

    treatLogic.change.addElement(rule);
    markLogicChanged();
  }
  public static void addChangeRule(TreatmentType treatment, ChangeLogic rule) {
    TreatmentLogic treatLogic = (TreatmentLogic)treatmentLogicHt.get(treatment);
    if (findChangeRule(treatment, rule) != null) { return; }

    treatLogic.change.addElement(rule);
    markLogicChanged();
  }
  public static Vector getChangeLogic(TreatmentType treatment) {
    return ( ((TreatmentLogic)treatmentLogicHt.get(treatment)).change );
  }

  public static String[] getChangeFunctions() {
    return ChangeLogic.getChangeFunctions();
  }
  public static String[] getChangeFunctionDesc() {
    return ChangeLogic.getChangeFunctionDesc();
  }
  public static String getChangeFunctionDesc(String changeFunction) {
    return ChangeLogic.getChangeFunctionDesc(changeFunction);
  }

  public static String[] getChangeEvalFunctions() {
    return ChangeLogic.getChangeEvalFunctions();
  }
  public static String[] getChangeEvalFunctionDesc() {
    return ChangeLogic.getChangeEvalFunctionDesc();
  }
  public static String getChangeEvalFunctionDesc(String changeFunction) {
    return ChangeLogic.getChangeEvalFunctionDesc(changeFunction);
  }

  public static Treatment read(Evu evu, String data) throws ParseError {
    String              msg;
    StringTokenizerPlus strTok = new StringTokenizerPlus(data,SEPARATOR);

    if (strTok.countTokens() != 1 && strTok.countTokens() != 4) {
      throw new ParseError("Invalid Treatment data in Evu");
    }

    // Get the treatment name and create an instance.
    RegionalZone  zone      = Simpplle.getCurrentZone();
    String        name = strTok.nextToken().toUpperCase();
    TreatmentType treatType = TreatmentType.get(name);

    if (treatType == null) {
      throw new ParseError(name + " is not a valid treatment");
    }
    Treatment treatment = new Treatment(treatType);

    // This is to allow for old saved area to still load with
    // the incomplete treatment data.
    if (strTok.hasMoreTokens() == false) {
      treatment.status     = NOT_APPLIED;
      treatment.savedState = null;
      return treatment;
    }

    // Get the treatment status.
    String tmpStatus = strTok.nextToken();
    if (tmpStatus.equalsIgnoreCase("EFFECTIVE")) {
      treatment.status = EFFECTIVE;
    }
    else if (tmpStatus.equalsIgnoreCase("APPLIED")) {
      treatment.status = APPLIED;
    }
    else if (tmpStatus.equalsIgnoreCase("INFEASIBLE")) {
      treatment.status = INFEASIBLE;
    }
    else if (tmpStatus.equalsIgnoreCase("NOT_APPLIED")) {
      treatment.status = NOT_APPLIED;
    }
    else {
      throw new ParseError("Invalid status in saved treatment");
    }

    // Get the Treatment Saved State.
    HabitatTypeGroup htGrp = evu.getHabitatTypeGroup();

    String stateStr = strTok.getToken();
    if (stateStr == null) {
      treatment.savedState = null;
    }
    else {
      treatment.savedState = htGrp.getVegetativeType(stateStr);
      if (treatment.savedState == null) {
        msg = stateStr + "is not a valid Vegetative Type in saved treatment";
        throw new ParseError(msg);
      }
    }

    // Get the Treatment time step.
    treatment.timeStep = strTok.getIntToken();
    if (treatment.timeStep == -1) {
      throw new ParseError("Invalid time step in saved treatment");
    }

    return treatment;
  }

  public void save(PrintWriter fout) {
    fout.print(toString()); // treatment name

    fout.print(SEPARATOR);
    switch (status) {
      case EFFECTIVE:   fout.print(EFFECTIVE_STR);   break;
      case APPLIED:     fout.print(APPLIED_STR);     break;
      case INFEASIBLE:  fout.print(INFEASIBLE_STR);  break;
      case NOT_APPLIED: fout.print(NOT_APPLIED_STR); break;
      default:          fout.print(NOT_APPLIED_STR);
    }

    fout.print(SEPARATOR);
    if (savedState != null) {
      savedState.printCurrentState(fout);
    }
    else {
      fout.print(NODATA);
    }

    fout.print(SEPARATOR);
    fout.print(timeStep);
  }

  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    int version = in.readInt();

    treatmentType = (TreatmentType)in.readObject();

    // Get the treatment status.
    String tmpStatus = (String)in.readObject();
    if (tmpStatus.equals("EFFECTIVE")) {
      status = EFFECTIVE;
    }
    else if (tmpStatus.equals("APPLIED")) {
      status = APPLIED;
    }
    else if (tmpStatus.equals("INFEASIBLE")) {
      status = INFEASIBLE;
    }
    else if (tmpStatus.equals("NOT_APPLIED")) {
      status = NOT_APPLIED;
    }
    else {
      status = NOT_APPLIED;
    }

    savedState = (VegetativeType)in.readObject();
    timeStep   = in.readInt();

  }
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(version);

    out.writeObject(treatmentType);
    switch (status) {
      case EFFECTIVE:   out.writeObject(EFFECTIVE_STR);   break;
      case APPLIED:     out.writeObject(APPLIED_STR);     break;
      case INFEASIBLE:  out.writeObject(INFEASIBLE_STR);  break;
      case NOT_APPLIED: out.writeObject(NOT_APPLIED_STR); break;
      default:          out.writeObject(NOT_APPLIED_STR);
    }

    out.writeObject(savedState);
    out.writeInt(timeStep);
  }

  // Later this will be done when reading the zone
  // data files.
  public static void initializeLogic() {
    treatmentLogicHt = new Hashtable();
    treatmentLogicHt.put(TreatmentType.AGRICULTURE,new TreatmentLogic());
    treatmentLogicHt.put(TreatmentType.CLEARCUT_WITH_RESERVES,new TreatmentLogic());
    treatmentLogicHt.put(TreatmentType.CLEARCUT_WITH_RESERVES_PLANT,new TreatmentLogic());
    treatmentLogicHt.put(TreatmentType.COMMERCIAL_THINNING,new TreatmentLogic());
    treatmentLogicHt.put(TreatmentType.ECOSYSTEM_MANAGEMENT_BROADCAST_BURN,new TreatmentLogic());
    treatmentLogicHt.put(TreatmentType.ECOSYSTEM_MANAGEMENT_THIN_AND_UNDERBURN,new TreatmentLogic());
    treatmentLogicHt.put(TreatmentType.ECOSYSTEM_MANAGEMENT_UNDERBURN,new TreatmentLogic());
    treatmentLogicHt.put(TreatmentType.FIREWOOD_REMOVAL,new TreatmentLogic());
    treatmentLogicHt.put(TreatmentType.GROUP_SELECTION_CUT,new TreatmentLogic());
    treatmentLogicHt.put(TreatmentType.IMPROVEMENT_CUT,new TreatmentLogic());
    treatmentLogicHt.put(TreatmentType.INDIVIDUAL_SELECTION_CUT,new TreatmentLogic());
    treatmentLogicHt.put(TreatmentType.LIBERATION_CUT,new TreatmentLogic());
    treatmentLogicHt.put(TreatmentType.PRECOMMERCIAL_THINNING,new TreatmentLogic());
    treatmentLogicHt.put(TreatmentType.PRECOMMERCIAL_THINNING_DIVERSITY,new TreatmentLogic());
    treatmentLogicHt.put(TreatmentType.SANITATION_SALVAGE,new TreatmentLogic());
    treatmentLogicHt.put(TreatmentType.SEEDTREE_FINAL_CUT,new TreatmentLogic());
    treatmentLogicHt.put(TreatmentType.SEEDTREE_FINAL_PLANT,new TreatmentLogic());
    treatmentLogicHt.put(TreatmentType.SEEDTREE_FINAL_WITH_RESERVES,new TreatmentLogic());
    treatmentLogicHt.put(TreatmentType.SEEDTREE_FINAL_WR_PLANT,new TreatmentLogic());
    treatmentLogicHt.put(TreatmentType.SEEDTREE_SEEDCUT,new TreatmentLogic());
    treatmentLogicHt.put(TreatmentType.SHELTERWOOD_FINAL_CUT,new TreatmentLogic());
    treatmentLogicHt.put(TreatmentType.SHELTERWOOD_FINAL_PLANT,new TreatmentLogic());
    treatmentLogicHt.put(TreatmentType.SHELTERWOOD_FINAL_WITH_RESERVES,new TreatmentLogic());
    treatmentLogicHt.put(TreatmentType.SHELTERWOOD_FINAL_WR_PLANT,new TreatmentLogic());
    treatmentLogicHt.put(TreatmentType.SHELTERWOOD_SEEDCUT,new TreatmentLogic());

    // Not in Westside
    treatmentLogicHt.put(TreatmentType.LOW_INTENSITY_GRAZING,new TreatmentLogic());
    treatmentLogicHt.put(TreatmentType.MODERATE_INTENSITY_GRAZING,new TreatmentLogic());
    treatmentLogicHt.put(TreatmentType.HIGH_INTENSITY_GRAZING,new TreatmentLogic());
    treatmentLogicHt.put(TreatmentType.HERBICIDE_SPRAYING,new TreatmentLogic());

    // ***********************************
    // *** Eastside and Westside Zones ***
    // ***********************************
    treatmentLogicHt.put(TreatmentType.CLEARCUT_WR_PRRWP,new TreatmentLogic());
    treatmentLogicHt.put(TreatmentType.SEEDTREE_FINAL_PRRWP,new TreatmentLogic());
    treatmentLogicHt.put(TreatmentType.SEEDTREE_FINAL_WR_PRRWP,new TreatmentLogic());
    treatmentLogicHt.put(TreatmentType.SHELTERWOOD_FINAL_PRRWP,new TreatmentLogic());
    treatmentLogicHt.put(TreatmentType.SHELTERWOOD_FINAL_WR_PRRWP,new TreatmentLogic());

    // ***************************************************
    // *** Sierra Nevada and Southern California Zones ***
    // ***************************************************
    treatmentLogicHt.put(TreatmentType.CUTTING,new TreatmentLogic());
    treatmentLogicHt.put(TreatmentType.CUTTING_BURNING,new TreatmentLogic());
    treatmentLogicHt.put(TreatmentType.CRUSHING,new TreatmentLogic());
    treatmentLogicHt.put(TreatmentType.CRUSHING_BURNING,new TreatmentLogic());
    treatmentLogicHt.put(TreatmentType.CUT_STACK_BURN,new TreatmentLogic());
    treatmentLogicHt.put(TreatmentType.LINE_BURNING,new TreatmentLogic());

    setLogicChanged(false);
  }

  public static void readLogic(File infile) throws SimpplleError {
    GZIPInputStream gzip_in;
    BufferedReader fin;

    TreatmentLogicData.readZoneFile = false;
    try {
      gzip_in = new GZIPInputStream(new FileInputStream(infile));
      fin = new BufferedReader(new InputStreamReader(gzip_in));
      readLogic(fin);
      fin.close();
      gzip_in.close();

      setLogicFile(infile);
    }
    catch (IOException err) {
      throw new SimpplleError("Error reading file");
    }
    catch (ParseError err) {
      throw new SimpplleError(err.msg);
    }
    catch (Exception err) {
      err.printStackTrace();
      throw new SimpplleError("An Exception occurred while reading the input file.");
    }
  }

  public static void readLogic(InputStream stream) throws SimpplleError {
    GZIPInputStream gzip_in;
    BufferedReader fin;

    TreatmentLogicData.readZoneFile = true;
    try {
      gzip_in = new GZIPInputStream(stream);
      fin = new BufferedReader(new InputStreamReader(gzip_in));
      readLogic(fin);
      // *** Important ***
      // DO NOT CLOSE THESE STREAMS.
      // IT WILL CAUSE READING FROM JAR FILES TO FAIL.
    }
    catch (ParseError err) {
      throw new SimpplleError(err.msg);
    }
    catch (IOException e) {
      String msg = "An Exception occurred while reading the input file.";
      throw new SimpplleError(msg);
    }
  }

  public static void readLogic(BufferedReader fin)
    throws IOException, ParseError
  {
    String         zoneName, treatmentName;
    TreatmentLogic treatLogic;
    TreatmentType  treatType;
    RegionalZone   zone = Simpplle.getCurrentZone();

    zoneName = fin.readLine();  // Ignored for now.

    // This bit of code is because of adding new treatments, which creates
    // treatment files out there without the full compilment of treatments.
    // This means that we cannot simply clear the hashtable, we must instead
    // clear only treatments that are not legal for the current zone.
    if (treatmentLogicHt != null) {
      TreatmentType[] treatments = zone.getLegalTreatments();
      Enumeration     keys       = treatmentLogicHt.keys();
      Vector          v          = new Vector(treatments.length);

      for (int i=0; i<treatments.length; v.addElement(treatments[i++])) {}

      while(keys.hasMoreElements()) {
        treatType = (TreatmentType)keys.nextElement();
        if (v.contains(treatType) == false) {
          treatmentLogicHt.remove(treatType);
        }
      }
    }
    else {
      treatmentLogicHt = new Hashtable();
    }

    treatmentName = fin.readLine();
    while (treatmentName != null) {
      treatType  = TreatmentType.get(treatmentName,true);
      if (isLegalTreatment(treatType) == false) {
        treatType = addLegalTreatment(treatmentName.toUpperCase());
      }
      treatLogic = (TreatmentLogic)treatmentLogicHt.get(treatType);
      if (treatLogic == null) {
        treatLogic = new TreatmentLogic();
      }
      else {
        treatLogic.clear();
      }
      treatmentLogicHt.put(treatType,treatLogic);
      treatLogic.read(fin,treatType);
      treatmentName = fin.readLine();
    }
    setLogicChanged(false);
  }

  public static void readLogic(TreatmentType treat) throws SimpplleError {
    BufferedReader fin;
    String         zoneName, treatmentName, str;
    TreatmentLogic treatLogic;
    TreatmentType  treatType;

    TreatmentLogicData.readZoneFile = true;
    try {
      fin      = Simpplle.getCurrentZone().getTreatmentLogicFileStream();
      zoneName = fin.readLine();  // Ignored for now.

      treatmentName = fin.readLine();
      while (treatmentName != null) {
        treatType  = TreatmentType.get(treatmentName);
        if (treatType != treat) {
          do {
            str = fin.readLine();
          }
          while (str != null && str.equals("END") == false);
        }
        else {
          treatLogic = getTreatmentLogic(treatType);
          if (treatLogic == null) { continue; }
          treatLogic.read(fin,treatType);
          return;
        }
        treatmentName = fin.readLine();
      }
    }
    catch (ParseError err) {
      throw new SimpplleError(err.msg);
    }
    catch (IOException e) {
      String msg = "An Exception occurred while reading the input file.";
      throw new SimpplleError(msg);
    }
  }


  public static void saveLogic(File outfile) throws SimpplleError {
    GZIPOutputStream out;
    PrintWriter      fout;

    try {
      out = new GZIPOutputStream(new FileOutputStream(outfile));
      fout = new PrintWriter(out);
      saveLogic(fout);
      setLogicFile(outfile);
      setLogicChanged(false);
      fout.flush();
      fout.close();
    }
    catch (Exception err) {
      throw new SimpplleError("Unable to write to output file.");
    }
  }

  public static void saveLogic(PrintWriter fout) {
    String[]    treatments;
    Enumeration keys = treatmentLogicHt.keys();

    fout.println(Simpplle.getCurrentZone().toString());
    int i = 0;
    treatments = new String[treatmentLogicHt.size()];
    while (keys.hasMoreElements()) {
      treatments[i] = ((TreatmentType)keys.nextElement()).toString();
      i++;
    }
    Utility.sort(treatments);

    FeasibilityLogic feasLogic;
    TreatmentType    treat;
    TreatmentLogic   treatLogic;
    Vector           changeRules;
    ChangeLogic      changeRule;

    int j;
    for(i=0; i<treatments.length; i++) {
      fout.println(treatments[i]);
      treat = TreatmentType.get(treatments[i]);
      treatLogic = (TreatmentLogic)treatmentLogicHt.get(treat);
      treatLogic.save(fout);
    }
  }

  public static boolean isLegalTreatment(TreatmentType treat) {
    return (legalTreatments != null && legalTreatments.contains(treat));
  }
  public static TreatmentType addLegalTreatment(String name) {
    TreatmentType treatType = TreatmentType.get(name);
    if (treatType == null) {
      treatType = new TreatmentType(name);
    }
    if (legalTreatments != null && legalTreatments.contains(treatType) == false) {
      legalTreatments.add(treatType);
      Collections.sort(legalTreatments);
      SimpplleType.initializeTreatmentList();
    }
    TreatmentLogic treatLogic = new TreatmentLogic();
    if (treatmentLogicHt == null) { treatmentLogicHt = new Hashtable(); }
    treatmentLogicHt.put(treatType,treatLogic);
    return treatType;
  }
  public static TreatmentType[] getLegalTreatments() {
    return (TreatmentType[])legalTreatments.toArray(new TreatmentType[legalTreatments.size()]);
  }
  public static ArrayList getLegalTreatmentList() {
    return legalTreatments;
  }

  public static void writeLegalFile(PrintWriter fout) {
    fout.println("// ** Legal Treatments");

    for (int i=0; i<legalTreatments.size(); i++) {
      if (i>0) { fout.print(","); }
      fout.print(legalTreatments.get(i).toString());
    }
    fout.println();
  }

  public static void readLegalFile(BufferedReader fin) throws SimpplleError {
    StringTokenizerPlus strTok;
    String              line, str;
    TreatmentType       treatmentType;

    try {

      line = fin.readLine(); // Comment
      if (line == null) { throw new SimpplleError("Invalid zone Definition File."); }

      line = fin.readLine();
      if (line == null) { throw new SimpplleError("Invalid zone Definition File."); }

      strTok = new StringTokenizerPlus(line,",");
      if (strTok.countTokens() < 1) { throw new SimpplleError("No Treatments Found."); }

      legalTreatments = new ArrayList(strTok.countTokens()+5);
      while (strTok.hasMoreElements()) {
        str = strTok.getToken();
        if (str == null) { throw new SimpplleError("Null Treatment found"); }

        addLegalTreatment(str.toUpperCase());
      }
    }
    catch (IOException ex) {
      throw new SimpplleError("Problems reading Legal Treatments from zone definition file.");
    }
    catch (ParseError ex) {
      throw new SimpplleError(ex.msg);
    }

  }

  /**
   * Creates treatments from a text file, each line should contain the treatment
   * name.
   * @param filename File
   * @throws SimpplleError
   */
  public static void importLegalFile(File filename) throws SimpplleError {
    String         line;
    BufferedReader fin;

    try {
      fin = new BufferedReader(new FileReader(filename));

      line = fin.readLine();
      if (line == null) { throw new SimpplleError("Nothing in file"); }

      while (line != null) {
        while (line != null && line.trim().length() == 0) {
          line = fin.readLine();
        }
        if (line == null) { continue; }

        line = line.trim().toUpperCase();
        addLegalTreatment(line);

        line = fin.readLine();
      }

      fin.close();
    }
    catch (IOException ex) {
      throw new SimpplleError("Problems importing Legal Treatments from file.");
    }
  }

  /**
    * Returns the follow-up treatment associated with the given treatment.
    * If the given treatment has no follup then null is returned.
    * @param zone is a RegionalZone
    * @param treatment is a Treatment
    * @return a Treatment, the follup treatment if any.
    */
  public static TreatmentType getFollowUpTreatment(RegionalZone zone, TreatmentType treatment) {
    int zoneId = zone.getId();

    switch (zoneId) {
      case ValidZones.WESTSIDE_REGION_ONE:
        return getFollowUpTreatment((WestsideRegionOne)zone, treatment);
      case ValidZones.EASTSIDE_REGION_ONE:
        return getFollowUpTreatment((EastsideRegionOne)zone, treatment);
      case ValidZones.TETON:
        return getFollowUpTreatment((Teton)zone, treatment);
      case ValidZones.NORTHERN_CENTRAL_ROCKIES:
        return getFollowUpTreatment((NorthernCentralRockies)zone, treatment);
      case ValidZones.SIERRA_NEVADA:
        return getFollowUpTreatment((SierraNevada)zone, treatment);
      case ValidZones.SOUTHERN_CALIFORNIA:
        return getFollowUpTreatment((SouthernCalifornia)zone, treatment);
      case ValidZones.GILA:
        return getFollowUpTreatment((Gila)zone, treatment);
      case ValidZones.SOUTH_CENTRAL_ALASKA:
        return getFollowUpTreatment((SouthCentralAlaska)zone, treatment);
      case ValidZones.COLORADO_FRONT_RANGE:
        return getFollowUpTreatment((ColoradoFrontRange)zone, treatment);
      default: return null;
    }
  }

  private static TreatmentType getFollowUpTreatment(TreatmentType treatment) {
    if (treatment == TreatmentType.SHELTERWOOD_SEEDCUT) {
        return TreatmentType.SHELTERWOOD_FINAL_WITH_RESERVES;
    }
    else if (treatment == TreatmentType.CLEARCUT_WITH_RESERVES) {
      return TreatmentType.PRECOMMERCIAL_THINNING;
    }
    else if (treatment == TreatmentType.CLEARCUT_WITH_RESERVES_PLANT) {
      return TreatmentType.PRECOMMERCIAL_THINNING;
    }
    else if (treatment == TreatmentType.SEEDTREE_SEEDCUT) {
      return TreatmentType.SEEDTREE_FINAL_WITH_RESERVES;
    }
    else if (treatment == TreatmentType.INDIVIDUAL_SELECTION_CUT) {
      return TreatmentType.INDIVIDUAL_SELECTION_CUT;
    }
    else if (treatment == TreatmentType.GROUP_SELECTION_CUT) {
      return TreatmentType.GROUP_SELECTION_CUT;
    }
    else {
      return null;
    }
  }

  private static TreatmentType getFollowUpTreatmentCommon(TreatmentType treatment) {
    TreatmentType treat = getFollowUpTreatment(treatment);
    if (treat == null) {
      if (treatment ==  TreatmentType.ECOSYSTEM_MANAGEMENT_UNDERBURN) {
        return TreatmentType.ECOSYSTEM_MANAGEMENT_UNDERBURN;
      }
      else {
        return null;
      }
    }
    else {
      return treat;
    }
  }
  private static TreatmentType getFollowUpTreatment(ColoradoFrontRange zone, TreatmentType treatment) {
    if (treatment == TreatmentType.THIN_CHIP_UNDERBURN) {
        return TreatmentType.UNDERBURN;
    }
    else {
      return null;
    }
  }
  private static TreatmentType getFollowUpTreatment(EastsideRegionOne zone, TreatmentType treatment) {
    if (treatment ==  TreatmentType.ECOSYSTEM_MANAGEMENT_THIN_AND_UNDERBURN) {
      return TreatmentType.ECOSYSTEM_MANAGEMENT_UNDERBURN;
    }
    else if (treatment ==  TreatmentType.ENCROACHMENT_CUT_AND_BURN) {
      return TreatmentType.ENCROACHMENT_BURN;
    }
    else {
      return getFollowUpTreatmentCommon(treatment);
    }
  }
  private static TreatmentType getFollowUpTreatment(Teton zone, TreatmentType treatment) {
    if (treatment ==  TreatmentType.ECOSYSTEM_MANAGEMENT_THIN_AND_UNDERBURN) {
      return TreatmentType.ECOSYSTEM_MANAGEMENT_UNDERBURN;
    }
    else if (treatment ==  TreatmentType.ENCROACHMENT_CUT_AND_BURN) {
      return TreatmentType.ENCROACHMENT_BURN;
    }
    else {
      return getFollowUpTreatmentCommon(treatment);
    }
  }
  private static TreatmentType getFollowUpTreatment(NorthernCentralRockies zone, TreatmentType treatment) {
    if (treatment ==  TreatmentType.ECOSYSTEM_MANAGEMENT_THIN_AND_UNDERBURN) {
      return TreatmentType.ECOSYSTEM_MANAGEMENT_UNDERBURN;
    }
    else if (treatment ==  TreatmentType.ENCROACHMENT_CUT_AND_BURN) {
      return TreatmentType.ENCROACHMENT_BURN;
    }
    else {
      return getFollowUpTreatmentCommon(treatment);
    }
  }

  private static TreatmentType getFollowUpTreatment(WestsideRegionOne zone, TreatmentType treatment) {
    return getFollowUpTreatmentCommon(treatment);
  }

  private static TreatmentType getFollowUpTreatment(SierraNevada zone, TreatmentType treatment) {
    return getFollowUpTreatment(treatment);
  }

  private static TreatmentType getFollowUpTreatment(SouthernCalifornia zone, TreatmentType treatment) {
    return getFollowUpTreatment(treatment);
  }

  private static TreatmentType getFollowUpTreatment(Gila zone, TreatmentType treatment) {
    return getFollowUpTreatment(treatment);
  }

  private static TreatmentType getFollowUpTreatment(SouthCentralAlaska zone, TreatmentType treatment) {
    return getFollowUpTreatment(treatment);
  }

/*
  public static int getFollupTreatmentWaitSteps(Treatment treatment) {
    switch (treatment.getId()) {
      case SHELTERWOOD_SEEDCUT:            return 1;
      case CLEARCUT_WITH_RESERVES:         return 2;
      case CLEARCUT_WITH_RESERVES_PLANT:   return 2;
      case SEEDTREE_SEEDCUT:               return 1;
      case INDIVIDUAL_SELECTION_CUT:       return 1;
      case GROUP_SELECTION_CUT:            return 1;
      case ECOSYSTEM_MANAGEMENT_UNDERBURN: return 2;
      default: return 0;
    }
  }
*/

//  public abstract String[] getValidStateData(int zoneId, int kind);

  public static Vector getValidHabitatTypeGroups(TreatmentType treatment) {
    TreatmentLogic treatLogic = (TreatmentLogic) treatmentLogicHt.get(treatment);

    Vector groups = treatLogic.feasibility.getValidHabitatTypeGroups();
    if (groups == null) {
      String[] strings = HabitatTypeGroup.getLoadedGroupNames();
      groups = new Vector();
      for(int i=0; i<strings.length; i++) {
        groups.addElement(HabitatTypeGroupType.get(strings[i]));
      }
    }
    return groups;
  }

  public static Vector getValidSpecies(TreatmentType treatment) {
    TreatmentLogic treatLogic = (TreatmentLogic) treatmentLogicHt.get(treatment);

    Vector allSpecies = treatLogic.feasibility.getValidSpecies();
    if (allSpecies == null) {
      allSpecies = HabitatTypeGroup.getValidSpecies();
    }
    return allSpecies;
  }

  public static Vector getValidSizeClass(TreatmentType treatment) {
    TreatmentLogic treatLogic = (TreatmentLogic) treatmentLogicHt.get(treatment);

    Vector allSizeClass = treatLogic.feasibility.getValidSizeClass();
    if (allSizeClass == null) {
      allSizeClass = HabitatTypeGroup.getValidSizeClass();
    }
    return allSizeClass;
  }

  public static Vector getValidDensity(TreatmentType treatment) {
    TreatmentLogic treatLogic = (TreatmentLogic) treatmentLogicHt.get(treatment);

    Vector allDensity = treatLogic.feasibility.getValidDensity();
    if (allDensity == null) {
      allDensity = HabitatTypeGroup.getValidDensity();
    }
    return allDensity;
  }

  /**
   * Gets the status of the Treatment.
   * Possible values: EFFECTIVE, APPLIED, INFEASIBLE, NOT_APPLIED;
   * @return an int.
   */
  public int getStatus() { return status; }
  public void setStatus(int newStatus) { status = newStatus; }
  /**
   * Gets the state that the existed before the treatment was applied.
   * Will return null if the treatment was not applied or infeasible.
   * @return an VegetativeType, the prior state.
   */
  public VegetativeType getSavedState() {
    if (status == NOT_APPLIED || status == INFEASIBLE) {
      return null;
    }
    else {
      return savedState;
    }
  }

  public void setSavedState(VegetativeType state) { savedState = state; }

  /**
   * Gets the id of the subclass which inherits this method.
   * Each subclass sets its own unique value to the id field.
   * This method returns that value.
   * @return an int, the Process id.
   */
  public int getId() { return id;}

  /**
   * Gets the time step in which the treatment was applied.
   * @return an int, the time step.
   */
  public int getTimeStep() { return timeStep; }

  public boolean preventReTreatment() { return preventReTreatment; }
  public void setPreventReTreatment(boolean value) { preventReTreatment = value; }

  public int getPreventReTreatmentTimeSteps() { return preventReTreatmentTimeSteps; }
  public void setPreventReTreatmentTimeSteps(int value) { preventReTreatmentTimeSteps = value; }

  /*
  protected void doChange (RegionalZone zone, Evu evu) {
    int zoneId = zone.getId();

    timeStep = Simpplle.getCurrentSimulation().getCurrentTimeStep();

    switch (zoneId) {
      case ValidZones.WESTSIDE_REGION_ONE:
        doChange((WestsideRegionOne) zone, evu);
        break;
      case ValidZones.EASTSIDE_REGION_ONE:
        doChange((EastsideRegionOne) zone, evu);
        break;
      case ValidZones.SIERRA_NEVADA:
        doChange((SierraNevada) zone, evu);
        break;
      case ValidZones.SOUTHERN_CALIFORNIA:
        doChange((SouthernCalifornia) zone, evu);
        break;
      case ValidZones.GILA:
        status = NOT_APPLIED;
//        doChange((Gila) zone, evu);
        break;
      default:
        break;
    }

    // save the past state.
    savedState = evu.getCurrentState();

    evu.addTreatment(this);
    evu.updateCurrentState(newState);
    newState = null;
  }
*/

  /**
   * @deprecated
   */
  public static boolean isHerbicideSprayingEffective(Evu evu) {
    Species species = (Species)evu.getState(SimpplleType.SPECIES);
    if ((Simulation.getInstance().random() < 7500) &&
        species != null &&
        species.toString().equals("NOXIOUS/UNIFORM/2")) {
      return true;
    }
    else {
      return false;
    }
  }
  public static boolean isEffective(Evu evu, int prob) {
    if ((Simulation.getInstance().random() < prob)) {
      return true;
    }
    else {
      return false;
    }
  }

  public static boolean isFeasible(Evu evu, TreatmentType treatmentType) {
    FeasibilityLogic feasibilityLogic = getFeasibilityLogic(treatmentType);

    return feasibilityLogic.evaluateFeasibility(evu);
  }

  private void doChange(Evu evu, Vector changeLogic) {
    Vector      noMatchRules = new Vector();
    Vector      rules = new Vector();
    ChangeLogic changeLogicRule;

    for (int i=0; i<changeLogic.size(); i++) {
      changeLogicRule = (ChangeLogic)changeLogic.elementAt(i);
      if (changeLogicRule.isNoMatchRule()) {
        noMatchRules.addElement(changeLogicRule);
      }
      else {
        rules.addElement(changeLogicRule);
      }
    }

    processChangeRules(evu, rules);
    if (newState == null && noMatchRules.size() > 0) {
      processChangeRules(evu, noMatchRules);
    }
  }

  private void processChangeRules(Evu evu, Vector rules) {
    ChangeLogic      changeLogicRule;
    String           result;
    boolean          speciesChanged=false,sizeClassChanged=false,densityChanged=false;
    HabitatTypeGroup htGrp = evu.getHabitatTypeGroup();

    VegSimStateData state = evu.getState();
    if (state == null) { return; }

    Species   newSpecies   = state.getVeg().getSpecies();
    SizeClass newSizeClass = state.getVeg().getSizeClass();
    Density   newDensity   = state.getVeg().getDensity();
    int       age          = state.getVeg().getAge();

    for(int i=0; i<rules.size(); i++) {
      changeLogicRule = (ChangeLogic)rules.elementAt(i);
      if (changeLogicRule.getToChoice().equals(ChangeLogic.TO_SPECIES)) {
        result = changeLogicRule.doChange(this,evu);
        if (result != null && (Species.get(result) != newSpecies)) {
          newSpecies     = Species.get(result);
          speciesChanged = true;
        }
      }
      else if (changeLogicRule.getToChoice().equals(ChangeLogic.TO_SIZE_CLASS)) {
        result = changeLogicRule.doChange(this,evu);
        if (result != null && (SizeClass.get(result) != newSizeClass)) {
          newSizeClass    = SizeClass.get(result);
         sizeClassChanged = true;
        }
      }
      else if (changeLogicRule.getToChoice().equals(ChangeLogic.TO_DENSITY)) {
        result = changeLogicRule.doChange(this,evu);
        if (result != null && (Density.get(result) != newDensity)) {
          newDensity     = Density.get(result);
          densityChanged = true;
        }
      }
      else {
        result = changeLogicRule.doChange(this,evu);
        if (result != null) {
          newState = htGrp.getVegetativeType(result);
        }
      }

      if (newState == null &&
          speciesChanged &&
          (sizeClassChanged && newSizeClass != SizeClass.SS) &&
          densityChanged)
      {
        newState = htGrp.findOldestVegetativeType(newSpecies,newSizeClass,
                                                  newDensity);
        if (newState == null) {
          Species updatedSpecies = (Species)evu.getState(SimpplleType.SPECIES);
          if (updatedSpecies == null) { continue; }
          newState = htGrp.findOldestVegetativeType(updatedSpecies,
                                                    newSizeClass,
                                                    newDensity);
        }
      }
      else if (newState == null &&
               speciesChanged &&
               (sizeClassChanged && newSizeClass == SizeClass.SS) &&
               densityChanged) {
        newState = htGrp.getVegetativeType(newSpecies, newSizeClass, 1,
                                           newDensity);
      }

      if (newState != null) { break; }
    }

    if (newState == null &&
        ((speciesChanged || densityChanged) && (sizeClassChanged == false))) {
      newState = htGrp.getVegetativeType(newSpecies,newSizeClass,age,newDensity);
      if (newState == null) {
        newState = htGrp.findNextYoungerVegetativeType(newSpecies,newSizeClass,
                                                       age,newDensity);
      }
    }
    else if (newState == null && (sizeClassChanged && newSizeClass != SizeClass.SS)) {
      newState = htGrp.findOldestVegetativeType(newSpecies,newSizeClass,
                                                newDensity);
      if (newState == null) {
        Species updatedSpecies = (Species)evu.getState(SimpplleType.SPECIES);
        if (updatedSpecies == null) { return; }
        newState = htGrp.findOldestVegetativeType(updatedSpecies,
                                                  newSizeClass,newDensity);
      }
    }
    else if (newState == null && (sizeClassChanged && newSizeClass == SizeClass.SS)) {
      newState = htGrp.getVegetativeType(newSpecies,newSizeClass,1,newDensity);
    }
    if (newState == null &&
        ((TreatmentType.get("CLOSE-ROADS") == treatmentType) ||
         (TreatmentType.get("OPEN-ROADS") == treatmentType) ||
         (TreatmentType.get("CLOSE-TRAILS") == treatmentType) ||
         (TreatmentType.get("OPEN-TRAILS") == treatmentType))) {
      return;
    }
    else if (newState == null &&
             (speciesChanged == false && sizeClassChanged == false &&
              densityChanged == false)) {
      VegSimStateData updatedState = evu.getState();
      if (updatedState != null) {
        newState = updatedState.getVeg();
      }
    }
  }

  /*
               **** Logic for applying treatment logic rules ****
               **************************************************
    If all three attributes change find the veg. Type that is the oldest.  If
    this fails then try keeping the species the same, and try again.
    Failing this continue through and exhaust all the rules.  After this, if
    species or density have changed but size class has not, then try to find a
    new state with the current age. If there is no such state find the next
    youngest valid state.  If there are no valid states then we fail and the
    treatment is applied with no state change.  However if only the size class
    has changed then find the oldest state with the new size class.  If this
    fails try the same thing but leave the species unchanged.  If this fails
    then the treatment is applied with no state change.
  */

  public void doChange (Evu evu) {
    FeasibilityLogic feasibilityLogic = getFeasibilityLogic(treatmentType);
    Vector           changeLogic      = getChangeLogic(treatmentType);

    newState = null;
    timeStep = Simpplle.getCurrentSimulation().getCurrentTimeStep();
    status   = NOT_APPLIED;

    boolean hasChangeLogic = (changeLogic != null && changeLogic.size() > 0);
    boolean feasible = feasibilityLogic.evaluateFeasibility(evu);
    if (feasible) {
      if (hasChangeLogic) {
        doChange(evu,changeLogic);
      }

      if (newState != null &&
          (treatmentType == TreatmentType.HERBICIDE_SPRAYING && status == EFFECTIVE)) {
        status = EFFECTIVE;
      }
      else if (newState != null || (hasChangeLogic == false)) {
        status = APPLIED;
      }
      else { status = NOT_APPLIED; }
    }
    else {
      status = INFEASIBLE;
    }

    // save the past state.
    if (newState == null) {
      savedState = evu.getState(timeStep, evu.getDominantLifeform()).getVeg();
    }
    else {
      savedState = newState;
    }

    evu.applyTreatment(this,newState);
    newState = null;

  }

  // Check to see if the adjacent units have a
  // size class of Seedling Sapling.  If they
  // do return true, otherwise false;
  // Treatment is infeasable if any adjacent unit has
  // Seedling sapling.
  public static boolean checkAdjacentSS(Evu evu) {
    AdjacentData[] adjacentData = evu.getAdjacentData();
    Evu            adj;

    for(int i=0;i<adjacentData.length;i++) {
      adj = adjacentData[i].getEvu();

      SizeClass sizeClass = (SizeClass)adj.getState(SimpplleType.SIZE_CLASS);
      if (sizeClass == SizeClass.SS) { return true; }
    }
    return false;
  }

  public String toString() {
    return treatmentType.toString();
  }

  public static String getPrintedTreatmentLogic(TreatmentType treatmentType) {
    TreatmentLogic treat = getTreatmentLogic(treatmentType);
    StringBuffer   strBuf = new StringBuffer();

    strBuf.append("Treatment Logic for: ");
    strBuf.append(treatmentType.toString());
    strBuf.append("\n\n");
    treat.printCode(strBuf);

    return strBuf.toString();
  }

}


