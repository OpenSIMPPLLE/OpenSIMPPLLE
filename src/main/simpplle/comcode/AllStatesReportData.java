package simpplle.comcode;

import java.io.*;
import java.util.*;

import org.apache.commons.collections.*;
import org.apache.commons.collections.keyvalue.*;
import org.apache.commons.collections.map.*;
import simpplle.comcode.Climate.*;

/**
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class provides constructors and methods to read and output data constituting the all states reports.
 * These are designed to provide the capability to track all individual states for species/ size class-structure/density.
 * <p>This can be run through the OpenSimpplle Main under the Reports -> All States Report
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 */

public class AllStatesReportData implements Externalizable {
  static final long serialVersionUID = -7838211181054501820L;
  static final int  version          = 2;

  public String    description;

  public HashMap<HabitatTypeGroupType,String> groupHm;
  public HashMap<Species,String>              speciesHm;
  public HashMap<SizeClass,String>            sizeClassHm;
  public HashMap<Density,String>              densityHm;
  public HashMap<String,String>               specialAreaHm;
  public HashMap<String,String>               ownershipHm;

  // Key 1: SpecialArea, Key 2: Ownership, Key 3: Group, Key 4: Modified State
  // Value: int[time steps]
  private MultiKeyMap summaryHm;

  private ArrayList<String> allStates;
  private ArrayList<String> allGroups;
  private ArrayList<String> allSpecialArea;
  private ArrayList<String> allOwnership;

  private boolean summaryFinished=false;
/**
 * Reads an allstatesreportdata object from external source.  Containined in this will be description, then simpplle type hashmaps for habitat gropu, species, size class and density
 * special area, ownership, creates a multikey summary hashmap, and reads in the arraylists for allstates, all habitat groups, all special areas, and all ownerships. 
 */
  public void readExternal(ObjectInput in)
    throws IOException,ClassNotFoundException
  {
    int version = in.readInt();

    description = (String)in.readObject();

    readExternalSimpplleTypeMap(in,groupHm,SimpplleType.GROUP);
    readExternalSimpplleTypeMap(in,speciesHm,SimpplleType.SPECIES);
    readExternalSimpplleTypeMap(in,sizeClassHm,SimpplleType.SIZE_CLASS);
    readExternalSimpplleTypeMap(in,densityHm,SimpplleType.DENSITY);

    // No longer using age, but still need for older versions.
    if (version == 1) {
      HashMap<Integer, String> ageHm = new HashMap<Integer,String>();
      readExternalMap(in, ageHm);
      ageHm.clear();
      ageHm = null;
    }
    readExternalMap(in,specialAreaHm);
    readExternalMap(in,ownershipHm);

    {
      int size = in.readInt();
      for (int i=0; i<size; i++) {
        String key1 = (String)in.readObject();
        String key2 = (String)in.readObject();
        String key3 = (String)in.readObject();
        String key4 = (String)in.readObject();

        int[] value = (int[])in.readObject();

        summaryHm.put(key1,key2,key3,key4,value);
      }
    }

    {
      int size = in.readInt();
      for (int i=0; i<size; i++) {
        allStates.add((String)in.readObject());
      }
    }

    {
      int size = in.readInt();
      for (int i=0; i<size; i++) {
        allGroups.add((String)in.readObject());
      }
    }

    {
      int size = in.readInt();
      for (int i=0; i<size; i++) {
        allSpecialArea.add((String)in.readObject());
      }
    }

    {
      int size = in.readInt();
      for (int i=0; i<size; i++) {
        allOwnership.add((String)in.readObject());
      }
    }

    summaryFinished = in.readBoolean();
  }
/**
 * Writes the object containing all states report data.  These are description, habitat group hashmap, species hashmap, 
 * size class hashmap, density hashmap, specialarea hashmap, ownership hashmap, and the states, groups, 
 * special areas, and ownerships from their respective arraylists.
 */
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(version);

    out.writeObject(description);

    writeExternalSimpplleTypeMap(out,groupHm);
    writeExternalSimpplleTypeMap(out,speciesHm);
    writeExternalSimpplleTypeMap(out,sizeClassHm);
    writeExternalSimpplleTypeMap(out,densityHm);

    writeExternalMap(out,specialAreaHm);
    writeExternalMap(out,ownershipHm);

    {
      out.writeInt(summaryHm.size());
      MapIterator it = summaryHm.mapIterator();
      while (it.hasNext()) {
        MultiKey keys = (MultiKey)it.next();
        out.writeObject(keys.getKey(0));
        out.writeObject(keys.getKey(1));
        out.writeObject(keys.getKey(2));
        out.writeObject(keys.getKey(3));

        out.writeObject(it.getValue());
      }
    }

    {
      int size = (allStates != null ? allStates.size() : 0);
      out.writeInt(size);
      for (int i=0; i<size; i++) {
        out.writeObject(allStates.get(i));
      }
    }

    {
      int size = (allGroups != null ? allGroups.size() : 0);
      out.writeInt(size);
      for (int i=0; i<size; i++) {
        out.writeObject(allGroups.get(i));
      }
    }

    {
      int size = (allSpecialArea != null ? allSpecialArea.size() : 0);
      out.writeInt(size);
      for (int i=0; i<size; i++) {
        out.writeObject(allSpecialArea.get(i));
      }
    }

    {
      int size = (allOwnership != null ? allOwnership.size() : 0);
      out.writeInt(size);
      for (int i=0; i<size; i++) {
        out.writeObject(allOwnership.get(i));
      }
    }

    out.writeBoolean(summaryFinished);
  }
/**
 * Method called to read in object information and put into a simpplle type hashmap.  These are the habitat group, species, 
 * size class, density.
 * @param in
 * @param hm
 * @param kind
 * @throws IOException
 * @throws ClassNotFoundException
 */
  public void readExternalSimpplleTypeMap(ObjectInput in, HashMap hm, SimpplleType.Types kind)
    throws IOException,ClassNotFoundException
  {
    int size = in.readInt();
    for (int i=0; i<size; i++) {
      SimpplleType key = SimpplleType.readExternalSimple(in,kind);
      String value = (String)in.readObject();
      hm.put(key,value);
    }
  }
/**
 * Writes to an external source the Simpplle Type objects in a Simpplle type hashmap.  These are the habitat group, species, 
 * size class, density.
 * @param out
 * @param hm the simpplle type hashmap.  These are the habitat group, species, size class, density hashmaps.
 * @throws IOException
 */
  private void writeExternalSimpplleTypeMap(ObjectOutput out, HashMap hm)
    throws IOException
  {
    int size = (hm != null ? hm.size() : 0);
    out.writeInt(size);

    if (size > 0) {
      for (Object key : hm.keySet()) {
        SimpplleType sType = (SimpplleType)key;
        sType.writeExternalSimple(out);
        out.writeObject(hm.get(sType));
      }
    }
  }
/**
 * reads in an external hashmap for objects which are not simpplle types.  Examples of these are special area and ownership.  
 * Objects are read in following order: size (used to end for loop for hashmap creation) , key, string value
 * places the object key and string value into hash map 
 * @param in
 * @param hm
 * @throws IOException
 * @throws ClassNotFoundException
 */
  public void readExternalMap(ObjectInput in, HashMap hm)
    throws IOException,ClassNotFoundException
  {
    int size = in.readInt();
    for (int i=0; i<size; i++) {
      Object key = in.readObject();
      String value = (String)in.readObject();
      hm.put(key,value);
    }
  }
  
  /**
   * 
   * writes the key and value within hashmap to external source
   * @param out 
   * @param hm the hashmap to be exported
   * @throws IOException caught in GUI
   */
  private void writeExternalMap(ObjectOutput out, HashMap hm)
    throws IOException
  {
    int size = (hm != null ? hm.size() : 0);
    out.writeInt(size);

    if (size > 0) {
      for (Object key : hm.keySet()) {
        out.writeObject(key);
        out.writeObject(hm.get(key));
      }
    }
  }
/**
 * Constructor for AllStatesReportdata.  creates a series of hashmaps for group, species, size class, density, special area, and ownership.  
 * Then creates a new multikey map for the summary, and string arraylists for all: states, groups, special areas, and ownerships
 */
  public AllStatesReportData() {
    groupHm       = new HashMap<HabitatTypeGroupType,String>();
    speciesHm     = new HashMap<Species,String>();
    sizeClassHm   = new HashMap<SizeClass,String>();
    densityHm     = new HashMap<Density,String>();
    specialAreaHm = new HashMap<String,String>();
    ownershipHm   = new HashMap<String,String>();

    summaryHm = new MultiKeyMap();

    allStates = new ArrayList<String>();
    allGroups = new ArrayList<String>();
    allSpecialArea = new ArrayList<String>();
    allOwnership = new ArrayList<String>();
  }
/**
 * 
 * @param group
 * @return toString of habitat type groups type 
 */
  private String lookupGroup(HabitatTypeGroupType group) {
    String str = (String)groupHm.get(group);
    return (str != null ? str : group.toString());
  }
  /**
   * method to get the species from hashmap 
   * @param species
   * @return a string literal of species object
   */
  private String lookupSpecies(Species species) {
    String str = (String)speciesHm.get(species);
    return (str != null ? str : species.toString());
  }
  
  /**
   * method to get size class of a species from size class hashmap
   * @param size 
   * @return string literal of size class
   */
  private String lookupSizeClass(SizeClass size) {
    String str = (String)sizeClassHm.get(size);
    return (str != null ? str : size.toString());
  }
  /**
   * Accesses the density hashmap to get a parameter density object.  
   * @param density
   * @return toString of density object
   */
  private String lookupDensity(Density density) {
    String str = (String)densityHm.get(density);
    return (str != null ? str : density.toString());
  }
  /**
   * Accesses the special area hashmap to get a parameter special area object, if one exists. 
   * @param specialArea
   * @return toString of special area
   */
  private String lookupSpecialArea(String specialArea) {
    if (specialArea == null) { return "UNKNOWN"; }
    String str = (String)specialAreaHm.get(specialArea);
    return (str != null ? str : specialArea);
  }
  /**
   * Accesses the ownership hashmap to get a parameter ownership object, if one exists.
   * @param ownership
   * @return toString of ownership
   */
  private String lookupOwnership(String ownership) {
    if (ownership == null) { return "UNKNOWN"; }
    String str = (String)ownershipHm.get(ownership);
    return (str != null ? str : ownership);
  }

  public MultiKeyMap getSummaryData() {
    return summaryHm;
  }

  public void clearSummaryData() {
    summaryHm.clear();
    allGroups.clear();
    allSpecialArea.clear();
    allOwnership.clear();
    summaryFinished = false;
  }
  public void updateSummary(Evu unit) {
    updateSummary(unit,Simulation.getCurrentTimeStep());
  }
  public void updateSummary(Evu unit, int timeStep) {
    String groupStr       = lookupGroup(unit.getHabitatTypeGroup().getType());
    String specialAreaStr = lookupSpecialArea(unit.getSpecialArea());
    String ownershipStr   = lookupOwnership(unit.getOwnership());

    Simulation simulation = Simulation.getInstance();

    Lifeform[] lives  = Lifeform.getAllValues();

    for (int i=0; i<lives.length; i++) {
      VegSimStateData state = unit.getStateFinalSeason(timeStep,lives[i]);
      if (state == null) { continue; }

      String speciesStr   = lookupSpecies(state.getVeg().getSpecies());
      String sizeClassStr = lookupSizeClass(state.getVeg().getSizeClass());
      String densityStr   = lookupDensity(state.getVeg().getDensity());

      StringBuffer strBuf = new StringBuffer(speciesStr.length() +
                                             sizeClassStr.length() +
                                             densityStr.length() + 4);
      strBuf.append(speciesStr);
      strBuf.append("/");
      strBuf.append(sizeClassStr);
      strBuf.append("/");
      strBuf.append(densityStr);

      String stateStr = strBuf.toString();
      int[] acresData = (int[])summaryHm.get(specialAreaStr,ownershipStr,groupStr,stateStr);
      if (acresData == null) {
        acresData = new int[simulation.getNumTimeSteps()+1];
        for (int ts=0; ts<acresData.length; ts++) {
          acresData[ts] = 0;
        }
        summaryHm.put(specialAreaStr,ownershipStr,groupStr,stateStr,acresData);
      }
      acresData[timeStep] += unit.getAcres();

      if (allStates.contains(stateStr) == false) {
        allStates.add(stateStr);
      }
      if (allGroups.contains(groupStr) == false) {
        allGroups.add(groupStr);
      }
      if (allSpecialArea.contains(specialAreaStr) == false) {
        allSpecialArea.add(specialAreaStr);
      }
      if (allOwnership.contains(ownershipStr) == false) {
        allOwnership.add(ownershipStr);
      }

    }
  }

//  public static void clear() { data = null; }
/**
 * reads in a file and creates an arraylist of all states report data
 * @param filename
 * @return arraylist of all states report data
 * @throws SimpplleError caught in GUI
 */
  public static ArrayList<AllStatesReportData> readFile(File filename) throws SimpplleError {
    if (filename == null) {
      ArrayList<AllStatesReportData> data = new ArrayList();

      AllStatesReportData newEntry = new AllStatesReportData();
      newEntry.description = "Default Report";
      data.add(newEntry);
      return data;
    }
    try {
      BufferedReader fin = new BufferedReader(new FileReader(filename));

      AllStatesReportData newEntry;

      ArrayList<AllStatesReportData> data = new ArrayList();

      String line = fin.readLine();
      while (line != null) {
        if (line.trim().length() == 0) {
          line = fin.readLine();
          continue;
        }
        if (line.toUpperCase().startsWith("BEGIN")) {
          newEntry = new AllStatesReportData();
          newEntry.description = line.substring(5).trim();
          newEntry.readEntryGroup(fin);
          data.add(newEntry);
        }
        line = fin.readLine();
      }
      return data;
    }
    catch (IOException ex) {
      throw new SimpplleError("Problems reading file:" + ex.getMessage());
    }
    catch (ParseError ex) {
      throw new SimpplleError("Problems reading file:" + ex.getMessage());
    }
  }
/**
 * Reads in a particular group for Habitat type group, species, size class, density, special area, or ownership. 
 * @param fin
 * @throws IOException
 * @throws SimpplleError
 * @throws ParseError
 */
  private void readEntryGroup(BufferedReader fin) throws IOException, SimpplleError, ParseError {
    String line = fin.readLine();
    String str;

    if (line == null) {
      throw new SimpplleError("No input entry: " + description);
    }

    int index;
    while (line != null) {
      line = line.trim();
      if (line.startsWith("HABITAT-TYPE-GROUP")) {
        readEntry("HABITAT-TYPE-GROUP", fin, line);
      }
      else if (line.startsWith("SPECIES")) {
        readEntry("SPECIES", fin, line);
      }
      else if (line.startsWith("SIZE-CLASS")) {
        readEntry("SIZE-CLASS", fin, line);
      }
      else if (line.startsWith("DENSITY")) {
        readEntry("DENSITY", fin, line);
      }
      else if (line.startsWith("SPECIAL-AREA")) {
        readEntry("SPECIAL-AREA", fin, line);
      }
      else if (line.startsWith("OWNERSHIP")) {
        readEntry("OWNERSHIP", fin, line);
      }
      else if (line.startsWith("END")) {
        break;
      }
      else if (line.startsWith("BEGIN")) {
        throw new SimpplleError("No END keyword found while reading: " + description);
      }
      else {
        throw new SimpplleError("Unknown keyword found in line: " + line);
      }

      line = fin.readLine();
      if (line == null) {
        throw new SimpplleError("No END keyword found while reading: " + description);
      }
    }
  }
  private void readEntry(String keyword, BufferedReader fin, String line)
    throws IOException, SimpplleError, ParseError
  {
    String desc="";

    if (line.length() > keyword.length()) {
      desc = line.substring(keyword.length()).trim();
    }
    line = fin.readLine();
    if (line == null) {
      throw new SimpplleError("No " + keyword + " in: " + desc);
    }
    line = line.trim();
    readEntryList(line,keyword,desc);
  }
  /**
   * Reads om and places into hashmaps list info for habitat type group, species, size class, density, special area, or ownership.  
   * @param line
   * @param keyword
   * @param desc
   * @throws SimpplleError
   * @throws ParseError
   */
  private void readEntryList(String line, String keyword, String desc)  throws SimpplleError, ParseError {
    StringTokenizerPlus strTok = new StringTokenizerPlus(line.trim(),",");

    HabitatTypeGroupType group;
    Species              species;
    SizeClass            sizeClass;
    Density              density;
    String               str;
    int                  age;

    while(strTok.hasMoreTokens()) {
      str = strTok.getToken();

      if (keyword.equals("HABITAT-TYPE-GROUP")) {
        group = HabitatTypeGroupType.get(str);
        if (group != null) {
          groupHm.put(group, desc);
        }
      }
      else if (keyword.equals("SPECIES")) {
        species = Species.get(str);
        if (species != null) {
          speciesHm.put(species,desc);
        }
      }
      else if (keyword.equals("SIZE-CLASS")) {
        sizeClass = SizeClass.get(str);
        if (sizeClass != null) {
          sizeClassHm.put(sizeClass,desc);
        }
      }
      else if (keyword.equals("DENSITY")) {
        density = Density.get(str);
        if (density != null) {
          densityHm.put(density,desc);
        }
      }
      else if (keyword.equals("SPECIAL-AREA")) {
        if (str == null || str.length() == 0) {
          throw new SimpplleError("Invalid Data in line: " + line);
        }
        specialAreaHm.put(str,desc);
      }
      else if (keyword.equals("OWNERSHIP")) {
        if (str == null || str.length() == 0) {
          throw new SimpplleError("Invalid Data in line: " + line);
        }
        ownershipHm.put(str,desc);
      }
      else {
        throw new SimpplleError("Unknown Keyword: " + keyword);
      }
    }
  }
/**
 * Gets the arraylist with all the habitat groups
 * @return the arraylist with all the habitat groups
 */
  public ArrayList<String> getAllGroups() {
    return allGroups;
  }
  /**
   * Gets the arraylist with all the habitat groups
   * @return the arraylist with all the habitat groups
   */
  public ArrayList<String> getAllOwnership() {
    return allOwnership;
  }
  /**
   * Gets the arraylist with all the special areas
   * @return the arraylist with all the special areas
   */
  public ArrayList<String> getAllSpecialArea() {
    return allSpecialArea;
  }
  /**
   * Gets the arraylist with all the vegetative states
   * @return the arraylist with all the vegetative states
   */
  public ArrayList<String> getAllStates() {
    return allStates;
  }
/**
 * Checks if summary is finished.    
 * @return True if summary is finished.
 */
  public boolean isSummaryFinished() {
    return summaryFinished;
  }
/**
 * Sets the summary finished boolean which will be used to notify the object writers that is all the information to be written
 * @param summaryFinished true if summary is finished.  
 */
  public void setSummaryFinished(boolean summaryFinished) {
    this.summaryFinished = summaryFinished;
  }
}




