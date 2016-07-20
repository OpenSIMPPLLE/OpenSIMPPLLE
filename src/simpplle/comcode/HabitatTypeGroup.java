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
import java.awt.Point;
import org.apache.commons.collections.map.Flat3Map;


/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 *
 * <p> This class manages Habitat Type Group information,
 * as well as storing all of the Vegetative Types associated with
 * a particular HabitatTypeGroup.
 * 
 * @author Documentation by Brian Losi
 * <p> Original source code authorship: Kirk A. Moeller
 */

public final class HabitatTypeGroup {
  public static final String FILE_EXT = "pathway";

  private HabitatTypeGroupType groupType;
  private HashMap<String,VegetativeType> vegTypes;
  private Vector               habitatTypes;
  private Vector               climaxSpecies;
  private Vector               seralSpecies;
//  private boolean              systemGroup;
  private File                 inputFile;
  private boolean              changed = false;
  private boolean              isUserData = false;
  private Hashtable            seedSapStates;
  private Hashtable            regenStates;
  private Density              maxSeedSapDensity;
  private Hashtable            allLines = new Hashtable();
  private String               knowledgeSource;
  private Lifeform[]           yearlyPathwayLives;

  private static HashMap<HabitatTypeGroupType,HabitatTypeGroup> groups
      = new HashMap<HabitatTypeGroupType,HabitatTypeGroup>();

  private static String KEYWORD[] = {"CLASS","END", "NAME", "HABITAT-TYPES",
                                     "CLIMAX-SPECIES", "SERAL-SPECIES",  "SYSTEM",
                                     "HABITAT-TYPE-GROUP", "ALL-VEG-TYPES",
                                     "VEGETATIVE-TYPE", SystemKnowledge.KNOWLEDGE_SOURCE_KEYWORD,
                                     "YEARLY-PATHWAY-LIVES"};

  private static final int CLASS           = 0;
  private static final int END             = 1;
  private static final int NAME            = 2;
  private static final int HABITAT_TYPES   = 3;
  private static final int CLIMAX_SPECIES  = 4;
  private static final int SERAL_SPECIES   = 5;
  private static final int SYSTEM          = 6;

  private static final int HTGRP           = 7;
  private static final int ALL_VEG_TYPES   = 8;
  private static final int VEGETATIVE_TYPE = 9;
  private static final int KNOWLEDGE_SOURCE = 10;
  private static final int YEARLY_PATHWAY_LIVES = 11;

  private static final int EOF             = 12;

  /**
   * Constructor for Habitat Type Group.  Initializes the object with group type, vegetative types, habitat types, climax species, 
   * seral species, knowledge source, and yearly pathway lives. Also creates new hash tables for seed sap states, and regeneration states.  Initializes some fields.
   */
  public HabitatTypeGroup () {
    groupType       = null;
    vegTypes        = null;
    habitatTypes    = null;
    climaxSpecies   = null;
    seralSpecies    = null;
    seedSapStates   = new Hashtable();
    regenStates     = new Hashtable();
    knowledgeSource = "";
    yearlyPathwayLives = null;
//    systemGroup   = false;
  }
/**
 * Overloaded constructor.  Used when creating a new area.  
 * Used when creating a new area.  If the group does not exist
 * we need to create one to store in the evu so that it can be
 * corrected the user later on.
 */
  
  public HabitatTypeGroup (String newName) {
    groupType = HabitatTypeGroupType.get(newName);
    if (groupType == null) {
      groupType = new HabitatTypeGroupType(newName);
    }
    groups.put(groupType,this);

    vegTypes      = null;
    habitatTypes  = null;
    climaxSpecies = null;
    seralSpecies  = null;
    seedSapStates = null;
    knowledgeSource = "";
    yearlyPathwayLives = null;
  }

/**
 * Gets a particular HabitatTypeGroup object by its HabitatTypeGroupType from the <HabitatTypeGroupType, HabitatTypeGroup> hashmap, if one exists.
 * @param groupName HabitatTypeGroupType name
 * @return HabitatTypeGroup object
 */
  public static HabitatTypeGroup findInstance(String groupName) {
    HabitatTypeGroupType ht = HabitatTypeGroupType.get(groupName);
    if (ht == null) { return null; }
    return findInstance(ht);
  }
  /**
   * Uses a HabitatTypeGroupType to get a HabitatTypeGroup <HabitatTypeGroupType, HabitatTypeGroup> hashmap, if one exists
   * @param groupType
   * @return
   */
  public static HabitatTypeGroup findInstance(HabitatTypeGroupType groupType) {
    return (HabitatTypeGroup)groups.get(groupType);
  }
/**
 * Checks if habitat type group is valid by seeing if it has a vegetative type.  
 * @return true if has a vegetative type.  
 */
  public boolean isValid() { return vegTypes != null; }

  public boolean isSystemGroup() {
    return ( (groupType.isUserCreated() == false) || (!isUserData));
  }
  /**
   * Gets the  HabitatTypeGroupType of this HabitatTypeGroup
   * @return
   */
  public HabitatTypeGroupType getType() { return groupType; }
/**
 * Clears the hashmap which holds all the habitat type groups.  
 */
  public static void clearGroups() {
    groups.clear();
  }
  /**
   * Removes a habitat type group from the hashmap which holds all the habitat type groups.  This hashmap is keyed by 
   * the habitat type group types, with the value being a habitat type group.  
   * @param group the habitat type group to be removed from the hashmap which holds all the habitat type groups
   */
  public static void removeGroup(HabitatTypeGroup group) {
    if (group != null) {
      groups.remove(group.getType());
      group.markChanged();
    }
  }
  /**
   * Removes a habitat type group from the hashmap <HabitatTypeGroupType, HabitatTypeGroup> 
   * @param groupName
   */
  public static void removeGroup(String groupName) {
    HabitatTypeGroup group = findInstance(groupName);
    if (group != null) {
      groups.remove(group.getType());
    }
  }
/**
 * Creates a vector of HabitatTypeGroups from the hashmap <HabitatTypeGroupType, HabitatTypeGroup> 
 * @return
 */
  public static Vector getLoadedGroups() {
    if (groups.size() == 0) { return null; }
    return new Vector(groups.values());
  }
  /**
   * Creates a vector of HabitatTypeGroupTypes from the hashmap <HabitatTypeGroupType, HabitatTypeGroup> 
   * @return
   */
  //Edited to sort Eco Groups
  public static Vector getAllLoadedTypes() {
    if (groups.size() == 0) { return null; }
    
    Vector dummy = new Vector(groups.keySet());
    Collections.sort(dummy);
    return dummy;
  }
  /**
   * 
   * @return
   */
  public static ArrayList<HabitatTypeGroupType> getAllLoadedTypesNew() {
    return getAllLoadedTypesNew(false);
  }
  /**
   * Creates an ArrayList of HabitatTypeGroupTypes from the keys of the hashmap <HabitatTypeGroupType, HabitatTypeGroup>, sorts it and then includes 
   * ANY if required to.  
   * @param includeAny if true add HabitatTypeGroupType.ANY to index 0
   * @return ArrayList of HabitatTypeGroupTypes
   */
  public static ArrayList<HabitatTypeGroupType> getAllLoadedTypesNew(boolean includeAny) {
    ArrayList<HabitatTypeGroupType> values =
        new ArrayList<HabitatTypeGroupType>(groups.keySet());
    Collections.sort(values);
    if (includeAny) {
      values.add(0, HabitatTypeGroupType.ANY);
    }
    return values;
  }
  /**
   * Creates both an array of HabitatTypeGroup with size of hashmap <HabitatTypeGroupType, HabitatTypeGroup> and then 
   * creates a new arraylist of the HabitatTypeGroup values.
   * @return arraylist of the HabitatTypeGroup values
   */
  public static ArrayList getAllLoadedGroups() {
    if (groups == null || groups.size() == 0) { return null; }

    HabitatTypeGroup[] items = new HabitatTypeGroup[groups.size()];

    return new ArrayList(groups.values());
  }
  /**
   * Gets a string array of all the HabitatTypeGroup names. 
   * @return
   */
  public static String[] getLoadedGroupNames() {
    if (groups.size() == 0) { return null; }

    String[] names = new String[groups.size()];
    int      i=0;
    for (HabitatTypeGroup group : groups.values()) {
      names[i] = group.getName();
      i++;
    }
    Arrays.sort(names);
    return names;
  }
/**
 * Checks if a habitat type group is a forested type.  
 * @param group
 * @return
 */
  public static boolean isForested(HabitatTypeGroup group) {
    HabitatTypeGroupType groupType = group.getType();

    return (groupType.equals(HabitatTypeGroupType.A1) ||
            groupType.equals(HabitatTypeGroupType.A2) ||
            groupType.equals(HabitatTypeGroupType.B1) ||
            groupType.equals(HabitatTypeGroupType.B2) ||
            groupType.equals(HabitatTypeGroupType.B3) ||
            groupType.equals(HabitatTypeGroupType.C1) ||
            groupType.equals(HabitatTypeGroupType.C2) ||
            groupType.equals(HabitatTypeGroupType.D1) ||
            groupType.equals(HabitatTypeGroupType.D2) ||
            groupType.equals(HabitatTypeGroupType.D3) ||
            groupType.equals(HabitatTypeGroupType.E1) ||
            groupType.equals(HabitatTypeGroupType.E2) ||
            groupType.equals(HabitatTypeGroupType.F1) ||
            groupType.equals(HabitatTypeGroupType.F2) ||
            groupType.equals(HabitatTypeGroupType.G1) ||
            groupType.equals(HabitatTypeGroupType.G2));
  }
/**
 * Checks if a habitat type group is nonforested by negating the results of isForested method.  
 * @param group
 * @return
 */
  public static boolean isNonForested(HabitatTypeGroup group) {
    return (!isForested(group));
  }
/**
 * Closes the file containing habitat type group.
 */
  public void closeFile() {
    clearFilename();
    setChanged(false);
  }

  public File getFilename() { return inputFile; }
  public void setFilename(File file) { inputFile = file; }
  public void clearFilename() { inputFile = null; }

  public boolean hasChanged() { return changed; }
  /**
   * Marks the system knowledge change for vegetation pathways.  
   */
  public void markChanged() {
    setChanged(true);
    setIsUserData(true);
    SystemKnowledge.markChanged(SystemKnowledge.VEGETATION_PATHWAYS);
  }
  /**
   * Sets the changed boolean to input boolean.  
   * @param value
   */
  public void setChanged(boolean value) { changed = value; }
/**
 * checks if Habitat type group contains user data. 
 * @return
 */
  public boolean isUserData() { return isUserData; }
  public void setIsUserData(boolean value) { isUserData = value; }

  /**
   * Gets the print name of the Habitat Type Group.
   * @return a String.
   */
  public String getName () { return groupType.toString(); }
/**
 * Gets teh knowledge source for this habitat type group.
 * @return
 */
  public String getKnowledgeSource() { return knowledgeSource; }
  public void setKnowledgeSource(String source) { knowledgeSource = source; }
/**
 * Checks if 
 * @param life
 * @return
 */
  public boolean isYearlyPathwayLifeform(Lifeform life) {
    if (yearlyPathwayLives == null) { return false; }

    for (int i=0; i<yearlyPathwayLives.length; i++) {
      if (yearlyPathwayLives[i] == life) { return true; }
    }
    return false;
  }
  public Lifeform[] getYearlyPathwayLifeforms() {
    return yearlyPathwayLives;
  }
  public void setYearlyPathwayLifeforms(ArrayList<Lifeform> values) {
    if (values == null || values.size() == 0) {
      yearlyPathwayLives = null;
    }
    else {
      yearlyPathwayLives = (Lifeform[]) values.toArray(new Lifeform[values.size()]);
    }
  }
  /**
   * Finds a VegetativeType given its species, size class, and density.
   * Note: age defaults to 1.
   * @param species is a String.
   * @param sizeClass is a String.
   * @param density is an int.
   * @return a VegetativeType.
   */
  public VegetativeType getVegetativeType(Species species, SizeClass sizeClass,
                                          Density density) {
    return getVegetativeType(species,sizeClass,1,density);
  }

  /**
   * Finds a VegetativeType given its species, size class, age, and density.
   * @param species is a Species.
   * @param sizeClass is a SizeClass.
   * @param age is an int.
   * @param density is an Density.
   * @return a VegetativeType.
   */
  public VegetativeType getVegetativeType (Species species, SizeClass sizeClass,
                                           int age, Density density) {

    String speciesStr = species.toString();
    String sizeClassStr = sizeClass.toString();
    String ageStr       = (age == 1 ? "" : IntToString.get(age));
    String densityStr   = density.toString();

    StringBuffer buf =
      new StringBuffer(speciesStr.length()+sizeClassStr.length()+ageStr.length()+densityStr.length()+4);

    buf.append(speciesStr);
    buf.append("/");
    buf.append(sizeClassStr);
    buf.append(ageStr);
    buf.append("/");
    buf.append(densityStr);

    return getVegetativeType(buf.toString());
  }
/**
 * Finds the vegetative type with the lowest density, by creating a veg type with density.One.  Then checks if it is greater than Density.TWO and Density.Three.  
 * @param vt
 * @return
 */
  public VegetativeType findLowestDensityVegetativeType(VegetativeType vt) {
    Species species = vt.getSpecies();
    SizeClass sizeClass = vt.getSizeClass();
    int       age       = vt.getAge();
    Density   density   = vt.getDensity();

    VegetativeType veg = getVegetativeType(species,sizeClass,age,Density.ONE);
    if (veg == null && density.getValue() > Density.TWO.getValue()) {
      veg = getVegetativeType(species,sizeClass,age,Density.TWO);
    }
    if (veg == null && density.getValue() > Density.THREE.getValue()) {
      veg = getVegetativeType(species,sizeClass,age,Density.THREE);
    }
    if (veg == null) { veg = vt; }

    return veg;
  }

  public VegetativeType findLowerDensityVegetativeType(VegetativeType vt) {
    return findLowerDensityVegetativeType(vt.getSpecies(),vt.getSizeClass(),
                                         vt.getAge(),vt.getDensity());
  }
  private VegetativeType findLowerDensityVegetativeType(Species species, SizeClass sizeClass,
                                                        int age, Density density) {
    Density newDensity = Density.getLowerDensity(density);

    if (newDensity == null) { return null; }

    return getVegetativeType(species,sizeClass,age,newDensity);
  }

  public VegetativeType findHigherDensityVegetativeType(VegetativeType vt) {
    return findHigherDensityVegetativeType(vt.getSpecies(),vt.getSizeClass(),
                                           vt.getAge(),vt.getDensity());
  }
  private VegetativeType findHigherDensityVegetativeType(Species species, SizeClass sizeClass,
                                                         int age, Density density) {
    Density  newDensity = Density.getHigherDensity(density);

    if (newDensity == null) { return null; }

    return getVegetativeType(species,sizeClass,age,newDensity);
  }

  public VegetativeType findNextYoungerVegetativeType(VegetativeType vt) {
    return findNextYoungerVegetativeType(vt.getSpecies(),vt.getSizeClass(),
                                         vt.getAge(),vt.getDensity());
  }
  public VegetativeType findNextYoungerVegetativeType(Species species, SizeClass sizeClass,
                                                      int age, Density density) {
    String         str;
    VegetativeType vt;
    int            newAge = age-1;

    if (newAge == 0) { return null; }

    if (age == 1) {
      vt  = getVegetativeType(species,sizeClass,density);
    }
    else {
      vt  = getVegetativeType(species,sizeClass,newAge,density);
      if (vt == null) {
        return findNextYoungerVegetativeType(species,sizeClass,newAge,density);
      }
    }

    return vt;
  }

  public VegetativeType findOldestVegetativeType(Species species, SizeClass sizeClass,
                                                 Density density)
  {
    Iterator    keys = vegTypes.keySet().iterator();
    VegetativeType vt;
    String         key;
    int            maxAge = 1;
    Species        vtSpecies;
    SizeClass      vtSizeClass;
    Density        vtDensity;
    int            vtAge;

    while (keys.hasNext()) {
      key = (String) keys.next();
      vt  = (VegetativeType) vegTypes.get(key);

      vtSpecies   = vt.getSpecies();
      vtSizeClass = vt.getSizeClass();
      vtDensity   = vt.getDensity();

      if (vtSpecies == species && vtSizeClass == sizeClass && vtDensity == density) {
        vtAge = vt.getAge();
        if (vtAge > maxAge) { maxAge = vtAge; }
      }
    }
    return getVegetativeType(species,sizeClass,maxAge,density);
  }

  /**
   * Finds a VegetativeType given its descriptive state string.
   * @param vegTypeStr is a string, (e.g. "DF/LARGE/1")
   * @return a VegetativeType.
   */
  public VegetativeType getVegetativeType (String vegTypeStr) {
    if (vegTypeStr == null || vegTypes == null) { return null;}

    return ( (VegetativeType) vegTypes.get(vegTypeStr));
  }

  public VegetativeType getVegetativeType (Species species, Density density) {
    ArrayList values = new ArrayList(vegTypes.values());
    for (int i=0; i<values.size(); i++) {
      VegetativeType vt = (VegetativeType)values.get(i);
      if (vt.getSpecies() == species && vt.getDensity() == density) {
        return vt;
      }
    }
    return null;
  }

  /**
   * Go thru all of the ht groups and check to see if it
   * has the veg type.  Return the first we find.
   * @param vegTypeStr
   * @return
   */
  public static VegetativeType getVegType(String vegTypeStr) {
    if (vegTypeStr == null) { return null;}

    VegetativeType vt;
    for (HabitatTypeGroup group : groups.values()) {
      vt = group.getVegetativeType(vegTypeStr);
      if (vt != null) { return vt; }
    }
    return null;
  }

  public int getStatesCount() { return vegTypes.size(); }

  public Hashtable getAllSpeciesHt() {
    return getAllSpeciesHt(new Hashtable());
  }
  public Hashtable getAllSpeciesHt(Hashtable ht) {
    Iterator    keys = vegTypes.keySet().iterator();
    String         key;
    Species        species;
    VegetativeType vt;
    int            i;

    while (keys.hasNext()) {
      key = (String) keys.next();
      vt  = (VegetativeType) vegTypes.get(key);
      species = vt.getSpecies();
      ht.put(species,species);
      vt = null;
    }
    return ht;
  }
/**
 * Gets the array containing all the species objects in a habitat type group.  
 * @return
 */
  public Species[] getAllSpecies() {
    return HabitatTypeGroup.getAllSpecies(getAllSpeciesHt());
  }
  public static Species[] getAllSpecies(Hashtable ht) {
    Species[]   allSpecies = new Species[ht.size()];
    Enumeration e = ht.keys();
    int         i = 0;

    while (e.hasMoreElements()) {
      allSpecies[i] = (Species) e.nextElement();
      i++;
    }
    ht = null;

    Arrays.sort(allSpecies);
    return allSpecies;
  }
/**
 * Makes a vector of the valid species in habitat type group from the hash table of habitat type groups.  
 * @return
 */
  public static Vector getValidSpecies() {
    Hashtable    ht = new Hashtable();

    for (HabitatTypeGroup group : groups.values()) {
      group.getAllSpeciesHt(ht);
    }

    Species[] allSpecies = HabitatTypeGroup.getAllSpecies(ht);
    Vector    v = new Vector();
    for(int i=0; i<allSpecies.length; i++) {
      v.addElement(allSpecies[i]);
    }
    return v;
  }
/**
 * Checks if a habitat type group has a particular species.  
 * @param species the species being evaluated for existence in habitat type group.
 * @return
 */
  public static boolean hasSpecies(Species species) {
    for (HabitatTypeGroup group : groups.values()) {
      for (VegetativeType vt : group.vegTypes.values()) {
        if (vt.getSpecies().equals(species)) {
          return true;
        }
      }
    }

    return false;
  }

  /**
   * Gets an arraylist of all the ages for all the habitat type groups.  
   * @return
   */
  public static ArrayList<Integer> getAllAge() {
    ArrayList<Integer> values = new ArrayList<Integer>();

    for (HabitatTypeGroup group : groups.values()) {
      group.updateAllAgeList(values);
    }

    return values;
  }
  private void updateAllAgeList(ArrayList<Integer> allAge) {
    for (VegetativeType vt : vegTypes.values()) {
      Integer age = vt.getAge();
      if (allAge.contains(age) == false) { allAge.add(age); }
    }
  }

  // *************
/**
 * Gets the hashtable containing all the size classes in a particular vegetative type.  The new hash table is required, because it will 
 * be loaded up with size classes (as both key and value) from a the vegetative types hash map.  
 * @return hashtable containing all the size classes in a particular vegetative type.  
 */
  public Hashtable getAllSizeClassHt() {
    return getAllSizeClassHt(new Hashtable());
  }
  /**
   * Returns a hashtable of all the size classes in a particular parameter hashtable by iterating through all the keys
   * in the vegetative types hashmap (Key = string name, value = vegetative type) 
   * @param ht the paramether hash table that the size classes will be put into.   
   * @return hash table of all they size classes in the vegetative types hashmap.  both key and value will be the size class
   */
  public Hashtable getAllSizeClassHt(Hashtable ht) {
    Iterator    keys = vegTypes.keySet().iterator();
    String         key;
    SizeClass      sizeClass;
    VegetativeType vt;
    int            i;

    while (keys.hasNext()) {
      key = (String) keys.next();
      vt  = (VegetativeType) vegTypes.get(key);
      sizeClass = vt.getSizeClass();
      ht.put(sizeClass,sizeClass);
      vt = null;
    }
    return ht;
  }
/**
 * Creates an array of all the size classes from the all size classes hashtable, which in turn creates the hash table from 
 * the vegetative types hash map.
 * @return an array of all the size classes in a habitat type group.
 */
  public SizeClass[] getAllSizeClass() {
    return HabitatTypeGroup.getAllSizeClass(getAllSizeClassHt());
  }
  /**
   * Makes an array of all the size classes in this habitat type group from the habitat type hashtable.  
   * The size classes are the keys into the habitat type group hash table.  
   * @param ht
   * @return
   */
  public static SizeClass[] getAllSizeClass(Hashtable ht) {
    SizeClass[] allSizeClass = new SizeClass[ht.size()];
    Enumeration e = ht.keys();
    int         i = 0;

    while (e.hasMoreElements()) {
      allSizeClass[i] = (SizeClass) e.nextElement();
      i++;
    }
    ht = null;

    Arrays.sort(allSizeClass);
    return allSizeClass;
  }
/**
 * Makes a vector with all valid size classes, from the array containing all size classes.  
 * This array is constructed by first going through all the HabitatTypeGroups in the arraylist keyed by habitat type group type and getting all the size classes
 * 
 * from the keys of the hash table all size class array.  
 * @return
 */
  public static Vector getValidSizeClass() {
    Hashtable    ht = new Hashtable();

    for (HabitatTypeGroup group : groups.values()) {
      group.getAllSizeClassHt(ht);
    }

    SizeClass[] allSizeClass = HabitatTypeGroup.getAllSizeClass(ht);
    Vector      v = new Vector();
    for(int i=0; i<allSizeClass.length; i++) {
      v.addElement(allSizeClass[i]);
    }
    return v;
  }
/**
 * Gets all the density 
 * @return
 */
  public Hashtable getAllDensityHt() {
    return getAllDensityHt(new Hashtable());
  }
  public Hashtable getAllDensityHt(Hashtable ht) {
    Iterator    keys = vegTypes.keySet().iterator();
    String         key;
    Density        density;
    VegetativeType vt;
    int            i;

    while (keys.hasNext()) {
      key = (String) keys.next();
      vt  = (VegetativeType) vegTypes.get(key);
      density = vt.getDensity();
      ht.put(density,density);
      vt = null;
    }
    return ht;
  }

  public Density[] getAllDensity() {
    return HabitatTypeGroup.getAllDensity(getAllDensityHt());
  }
  public static Density[] getAllDensity(Hashtable ht) {
    Density[]    allDensity = new Density[ht.size()];
    Enumeration e = ht.keys();
    int         i = 0;

    while (e.hasMoreElements()) {
      allDensity[i] = (Density) e.nextElement();
      i++;
    }
    ht = null;

    Arrays.sort(allDensity);
    return allDensity;
  }

  public static Vector getValidDensity() {
    Hashtable    ht = new Hashtable();

    for (HabitatTypeGroup group : groups.values()) {
      group.getAllDensityHt(ht);
    }

    Density[] allDensity = HabitatTypeGroup.getAllDensity(ht);
    Vector    v = new Vector();
    for(int i=0; i<allDensity.length; i++) {
      v.addElement(allDensity[i]);
    }
    return v;
  }

  /**
    * Find Processes that have a next state in
    * those VegetativeTypes that match the given species.
    * This is used primarily in the pathways dialog.
    */
  public String[] getAllProcesses(Species species) {
    Hashtable      ht = new Hashtable();
    Iterator    keys = vegTypes.keySet().iterator();
    String[]       allProcesses;
    Process[]      processes;
    String         key;
    VegetativeType vt;
    int            i;

    while (keys.hasNext()) {
      key = (String) keys.next();
      vt  = (VegetativeType) vegTypes.get(key);
      if (species == vt.getSpecies()) {
        processes = vt.getProcesses();
        for(i=0;i<processes.length;i++) {
          ht.put(processes[i].toString(),processes[i].toString());
        }
      }
      vt = null;
    }

    allProcesses = new String[ht.size()];
    keys = ht.keySet().iterator();
    i            = 0;
    while (keys.hasNext()) {
      allProcesses[i] = (String)keys.next();
      i++;
    }
    ht = null;

    return allProcesses;
  }


  /**
    * Returns a vector of all vegetative types which match
    * the given species.  Among those it adds any types which
    * are the result of the provided process
    */
  public Hashtable findMatchingSpeciesTypes(Species species, Process process) {
    Iterator    keys = vegTypes.keySet().iterator();
    VegetativeType vt;
    String         key;
    Hashtable      matches = new Hashtable();

    while (keys.hasNext()) {
      key = (String) keys.next();
      vt  = (VegetativeType) vegTypes.get(key);
      if (species == vt.getSpecies()) {
        matches.put(key,vt);
        vt = vt.getProcessNextState(process);
        if (vt != null) { matches.put(vt.toString(),vt); }
      }
    }
    return matches;
  }

  private String[] getMatchingSpeciesTypes(Species species) {
    Iterator    keys = vegTypes.keySet().iterator();
    VegetativeType vt;
    Vector         matches = new Vector();
    String[]       result;

    while (keys.hasNext()) {
      vt  = (VegetativeType) vegTypes.get((String) keys.next());
      if (species == vt.getSpecies()) {
        matches.addElement(vt.toString());
      }
    }
    result = new String[matches.size()];
    matches.copyInto(result);

    return result;
  }

  public String[] getSortedMatchingSpeciesTypes(Species species) {
    String[] result = getMatchingSpeciesTypes(species);
    Utility.sort(result);
    return result;
  }

  public static String[] getAllSortedMatchingSpeciesTypes(Species species) {
    Hashtable ht = new Hashtable();
    String[]  tmpTypes;
    String[]  result;
    int       j;

    for (HabitatTypeGroup group : groups.values()) {
      tmpTypes = group.getMatchingSpeciesTypes(species);
      for(j=0; j<tmpTypes.length; j++) {
        ht.put(tmpTypes[j],tmpTypes[j]);
      }
    }
    result = new String[ht.size()];
    ht.values().toArray(result);
    Utility.sort(result);
    return result;
  }

  public boolean isMemberSpecies(Species species) {
    VegetativeType vt;
    Iterator    keys = vegTypes.keySet().iterator();
    while (keys.hasNext()) {
      vt = (VegetativeType) vegTypes.get((String)keys.next());
      if (species == vt.getSpecies()) {
        return true;
      }
    }
    return false;
  }

  public Vector findPreviousStates(VegetativeType vegType) {
    Vector         result = new Vector();
    VegetativeType vt;
    Process        p;
    Iterator    keys = vegTypes.keySet().iterator();

    while (keys.hasNext()) {
      vt = (VegetativeType) vegTypes.get((String)keys.next());
      p = vt.getNextStateProcess(vegType);
      if (p != null) {
        result.addElement(new VegetativeTypeNextState(p,vt,0));
      }
    }
    return result;
  }

  /**
   * Find's the seedling sapling VegetativeType that matches
   * the given species, if any.
   * @param an Species, the species.
   * @return a VegetativeType or null.
   */
  public VegetativeType getSeedSapState(Species species) {
    return (VegetativeType) seedSapStates.get(species);
  }

  // ** Parsing Stuff **

  private boolean keyMatch (String key, int keyid) {
    return key.equalsIgnoreCase(KEYWORD[keyid]);
  }

  private int getKeyword (StringTokenizerPlus strTok) throws ParseError, IOException {
    String value;

    value = strTok.nextToken();

    if      (keyMatch(value,CLASS))          { return CLASS; }
    else if (keyMatch(value,NAME))           { return NAME;}
    else if (keyMatch(value,HABITAT_TYPES))  { return HABITAT_TYPES;}
    else if (keyMatch(value,CLIMAX_SPECIES)) { return CLIMAX_SPECIES;}
    else if (keyMatch(value,SERAL_SPECIES))  { return SERAL_SPECIES;}
    else if (keyMatch(value,SYSTEM))         { return SYSTEM; }
    else if (keyMatch(value,KNOWLEDGE_SOURCE)) { return KNOWLEDGE_SOURCE; }
    else if (keyMatch(value,YEARLY_PATHWAY_LIVES)) { return YEARLY_PATHWAY_LIVES; }
    else if (keyMatch(value,END))            { return END;}
    // imported text file only.
    else if (keyMatch(value,HTGRP))           { return HTGRP; }
    else if (keyMatch(value,VEGETATIVE_TYPE)) { return VEGETATIVE_TYPE; }
    else {
      throw new ParseError("Unknown Keyword: " + value);
    }
  }


  private void readHtGrp (BufferedReader fin) throws ParseError, IOException {
    int                 key = EOF;
    String              value, line;
    StringTokenizerPlus strTok;
    String              name;

    yearlyPathwayLives = null;
    // loop until we find the "END", "CLASS", or EOF.
    do {
      line   = fin.readLine();
      if (line == null) { key = EOF; continue;}

      strTok = new StringTokenizerPlus(line," ");
      if (strTok.hasMoreTokens() == false) {continue;}

      key = getKeyword(strTok);
      if (key != END && strTok.hasMoreTokens() == false) {
        throw new ParseError("Keyword: " + KEYWORD[key] + " has no value.");
      }

      switch (key) {
        case NAME:
          name = strTok.nextToken();
          if (name == null) { throw new ParseError("Invalid group Name"); }

          groupType = HabitatTypeGroupType.get(name);
          if (groupType == null) { groupType = new HabitatTypeGroupType(name); }

          groups.put(groupType,this);

          break;
        case HABITAT_TYPES:
          habitatTypes = strTok.getListValue(true);
          break;
        case CLIMAX_SPECIES:
          climaxSpecies = strTok.getListValue();
          break;
        case SERAL_SPECIES:
          seralSpecies = strTok.getListValue();
          break;
        case SYSTEM:
          String str = strTok.getToken();
//          systemGroup = (str.equalsIgnoreCase("true")) ? true : false;
          break;
        case KNOWLEDGE_SOURCE:
          knowledgeSource = SystemKnowledge.readKnowledgeSource(fin,SystemKnowledge.VEGETATION_PATHWAYS);
          break;
        case YEARLY_PATHWAY_LIVES:
          readYearlyPathwayLives(strTok);
          break;
        case END:
          break;
        default:
          throw new ParseError("Unknown keyword in input file.");
      }
    }
    while (key != EOF && key != CLASS && key != END);

    if (key == CLASS) {
      throw new ParseError("No END keyword found while reading HT-GRP data.");
    }
    else if (key == EOF) {
      throw new ParseError("Invalid Pathway file, no Pathway records found.");
    }
  }

  private void readYearlyPathwayLives(StringTokenizerPlus strTok) throws ParseError, IOException {
    Vector v = strTok.getListValue();
    yearlyPathwayLives = new Lifeform[v.size()];

    for (int i=0; i<v.size(); i++) {
      yearlyPathwayLives[i] = Lifeform.get((String)v.get(i));
    }
  }
  private void readVegTypes(BufferedReader fin) throws ParseError {
    VegetativeType vegData;
    String         line;
    int            id = 0, numVegTypes = 0;

    try {
      line = fin.readLine();
      numVegTypes = Integer.parseInt(line.trim());
    }
    catch (NumberFormatException NFE) {
      throw new ParseError("Invalid value for number of records");
    }
    catch (IOException IOX) {
      throw new ParseError("Problems reading Pathway records.");
    }
    vegTypes = new HashMap(numVegTypes);

    for(int i=0;i<numVegTypes;i++) {
      vegData = new VegetativeType(this);
      vegData.readData(fin);
      addVegetativeType(vegData);
    }
  }

  public void addVegetativeType(VegetativeType veg) {
    vegTypes.put(veg.getCurrentState(),veg);

    SimpplleType.updateAllData(veg.getSpecies(),SimpplleType.SPECIES);
    SimpplleType.updateAllData(veg.getSizeClass(),SimpplleType.SIZE_CLASS);
    SimpplleType.updateAllData(veg.getDensity(),SimpplleType.DENSITY);

    // Add to seedling Sapling States if Necessary.
    if (veg.getSizeClass() == SizeClass.SS &&
        veg.getDensity() == maxSeedSapDensity &&
        veg.getAge() == 1) {
      seedSapStates.put(veg.getSpecies(),veg);
    }
    markChanged();
  }

  public void deleteVegetativeType(VegetativeType veg) {
    RegionalZone zone = Simpplle.getCurrentZone();
    String       state = veg.getCurrentState();
    SizeClass    sizeClass = veg.getSizeClass();

    vegTypes.remove(state);
    if (sizeClass == SizeClass.SS) {
      // If state is SS then we need to run these again
      // to make sure we are accurate.
      findMaxSeedSapDensity();
      findSeedSapStates();
      findGroupRegenerationStates();
    }
    markChanged();
  }

  /**
   * This method is responsible for reading the information contained
   * in a pathway data files.
   * @param fin is a BufferedReader
   * @return the new HabitatTypeGroup
   */
  public static HabitatTypeGroup read(BufferedReader fin) {
    HabitatTypeGroup newGroup = new HabitatTypeGroup();
    newGroup.readData(fin);
    return newGroup;
  }

  private void readData(BufferedReader fin) {
    int                 key = EOF, i;
    String              value, line;
    StringTokenizerPlus strTok;

    // Create a log file name
    String dir  = System.getProperty("user.dir");
    String name = "Pathways";

    File logFile = new File(dir,name + ".log");
    int n = 1;
    while (logFile.exists()) {
      logFile = new File(dir,name + "-" + n++ + ".log");
    }

    try {
      do {
        line   = fin.readLine();
        if (line == null) { key = EOF; continue;}

        strTok = new StringTokenizerPlus(line," ");
        if (strTok.hasMoreTokens() == false) {continue;}

        key = getKeyword(strTok);
        if (strTok.hasMoreTokens() == false) {
          throw new ParseError("Keyword: " + KEYWORD[key] + " has no value.");
        }

        if (key == CLASS) {
          value = strTok.nextToken();
        }
        else {
          throw new ParseError("Invalid record, first keyword must be CLASS");
        }

        if (keyMatch(value,HTGRP)) {
          readHtGrp(fin);
        }
        else if (keyMatch(value,ALL_VEG_TYPES)) {
          readVegTypes(fin);
        }
        else {
          throw new ParseError ("Invalid Class Specified:" + value);
        }
      }
      while (key != EOF);
      fixNextStates(logFile);  /* also builds the SS states hashtable */
      setChanged(false);
    }
    catch (ParseError PX) {
      System.out.println("Input file processing problem:");
      System.out.println(PX.msg);
    }
    catch (IOException IOE) {
      System.out.println("Problems while trying to read input file.");
    }
  }

  public static void importSpeciesChangeFile(BufferedReader fin) throws ParseError {
    String line="";
    try {
      line = fin.readLine();
      while (line != null) {
        line = line.trim();
        if (line.length() == 0) { line = fin.readLine(); continue; }

        StringTokenizerPlus strTok = new StringTokenizerPlus(line,", \t\n\r\f");

        String str = strTok.getToken();
        HabitatTypeGroup group = HabitatTypeGroup.findInstance(str);
        if (group == null) {
          throw new ParseError(str + " Pathway Grouping not found " + str);
        }

        line = fin.readLine();
        boolean groupEnd = false;
        while (line != null) {
          line = line.trim();
          if (line.length() > 0) {
            strTok = new StringTokenizerPlus(line,", \t\n\r\f");

            str = strTok.getToken();
            VegetativeType vt = group.getVegetativeType(str);
            if (vt == null) {
              throw new ParseError(str + " Vegetative Type not found in group " + group.toString());
            }
            groupEnd = vt.importSpeciesChange(fin, strTok);
            if (groupEnd) { break; }
          }
          line = fin.readLine();
        }
        if (line != null) {
          line = fin.readLine();
        }
        group.markChanged();
      }
    }
    catch (ParseError ex) {
      String msg =
          "An error occurred in this line: " + line + "\n" + ex.msg;
      throw new ParseError(msg);
    }
    catch (IOException ex) {
      throw new ParseError("Problems reading species change file");
    }
  }
/**
 * Imports the inclusion rules filefor a particular habitat type group.  This is used in creating vegetative pathways.  
 * @param infile
 * @throws SimpplleError
 */
  public static void importInclusionFile(File infile) throws SimpplleError {
    try {
      BufferedReader fin = new BufferedReader(new FileReader(infile));

      String line = fin.readLine();
      if (line == null) {
        throw new ParseError("File empty");
      }

      while (line != null) {
        line = line.trim();
        if (line.length() == 0) { line = fin.readLine(); continue; }

        // Group Species List
        StringTokenizerPlus strTok = new StringTokenizerPlus(line,",");

        String str = strTok.getToken();
        HabitatTypeGroup group = HabitatTypeGroup.findInstance(str);
        if (group == null) {
          throw new ParseError(str + " in not a valid pathway grouping");
        }

        int spCount = strTok.countTokens();
        ArrayList<InclusionRuleSpecies> spList = new ArrayList<InclusionRuleSpecies>(spCount);
        for (int i=0; i<spCount; i++) {
          str = strTok.getToken();
          spList.add(InclusionRuleSpecies.get(str,true));
        }


        line = fin.readLine();
        if (line == null) {
          throw new ParseError("No data for group" + group.toString());
        }
        while (line != null) {
          line = Utility.preProcessInputLine(line);
          if (line.length() == 0) { line = fin.readLine(); continue; }

          // Veg Type <tab> pct Values for species
          strTok = new StringTokenizerPlus(line,",");
          str = strTok.getToken();
          if (str.startsWith("END")) { line = null; continue; }

          VegetativeType vt = group.getVegetativeType(str);
          if (vt == null) {
            throw new ParseError("Invalid " + group.toString() + " VegetativeType in line: " + line);
          }
          if (strTok.countTokens() != spList.size()) {
            throw new ParseError("Not enough fields in line: " + line + " in group " + group.toString());
          }

          vt.clearSpeciesRange();

          StringTokenizerPlus strTok2;
          int lowerPct=0, upperPct=100;
          for (InclusionRuleSpecies species : spList) {
            if (strTok.hasMoreTokens() == false) { continue; }

            str = strTok.getToken();
            if (str == null) { continue; }
            if (str.contains(">")) {
              lowerPct = Integer.parseInt(str.substring(1)) + 1;
              upperPct = 100;
            }
            else if (str.contains("<")) {
              upperPct = Integer.parseInt(str.substring(1)) - 1;
              lowerPct = 0;
            }
            else if (str.contains(":")) {
              strTok2 = new StringTokenizerPlus(str,":");
              lowerPct = strTok2.getIntToken();
              upperPct = strTok2.getIntToken();
            }
            else { continue; }

            if (lowerPct < 0) { lowerPct = 0; }
            if (upperPct > 100) { upperPct = 100; }

            vt.addSpeciesRange(species,lowerPct,upperPct);
          }

          line = fin.readLine();
        }
        line = fin.readLine();
        group.markChanged();
      }

    }
    catch (Exception ex) {
      throw new SimpplleError("Problems while trying to read input file\n" + ex.getMessage(),ex);
    }
  }

  public VegetativeType findSpeciesChangeNextState(Flat3Map trkSpecies) {
    VegetativeType vt;
    // Need to make sure that water is the veg type chosen first if water is
    // a valid fit.  Otherwise some other veg type might be chosen because its
    // inclusion rule doesn't mention water anywhere.
    vt = getVegetativeType("WATER/NA/NA");
    if (vt != null && vt.validSpeciesFit(trkSpecies)) {
      return vt;
    }

    for (Object Obj : vegTypes.values()) {
      vt = (VegetativeType)Obj;
      if (vt.validSpeciesFit(trkSpecies)) {
        return vt;
      }
    }
    return null;
  }

  public void importTextFile(File infile) throws SimpplleError {
    BufferedReader      fin;
    int                 key = EOF, i;
    String              value, line;
    StringTokenizerPlus strTok;
    boolean             foundHtGrp = false, foundVegType = false;
    HabitatTypeGroup    newGroup, oldGroup=null;


    // Create a log file name
    String dir  = infile.getParent();
    String name = infile.getName();

    File logFile = new File(dir,name + ".log");
    int n = 1;
    while (logFile.exists()) {
      logFile = new File(dir,name + "-" + n++ + ".log");
    }

    try {
      fin      = new BufferedReader(new FileReader(infile));
      vegTypes = new HashMap();

      line = fin.readLine();
      do {
        if (line == null) { key = EOF; continue;}

        strTok = new StringTokenizerPlus(line.trim());
        if (strTok.hasMoreTokens() == false) {
          line = fin.readLine();
          continue;
        }

        key = getKeyword(strTok);
        if (key == HTGRP && strTok.hasMoreTokens() == false) {
          throw new ParseError("Keyword: " + KEYWORD[key] + " has no value.");
        }

        if (key == HTGRP) {
          line = importHtGrp(strTok.nextToken(), fin);
          oldGroup = (HabitatTypeGroup)groups.get(groupType);
          groups.put(groupType,this);
          foundHtGrp = true;
        }
        else if (key == VEGETATIVE_TYPE) {
          line = importVegType(strTok.nextToken(), fin);
          foundVegType = true;
        }
        else {
          line = fin.readLine();
          continue;
        }
      }
      while (key != EOF);

      fin.close();
      if (foundHtGrp == false || foundVegType == false) {
        String msg = "Could not find data in input file\n" +
                     "Check the file format and try again";
        throw new ParseError(msg);
      }
      fixNextStates(logFile);  /* also builds the SS states hashtable */
      markChanged();
    }
    catch (Exception err) {
      vegTypes = null;
      if (groupType != null) {
        if (oldGroup != null) {
          groups.put(groupType, oldGroup);
        }
        else {
          groups.remove(groupType);
        }
      }
      throw new SimpplleError("Problems while trying to read input file\n" + err.getMessage());
    }
  }

  private String importHtGrp(String htGrpName, BufferedReader fin)
    throws IOException, ParseError
  {
    int                 key = EOF;
    String              value, line;
    StringTokenizerPlus strTok;

    String name = htGrpName.trim().toUpperCase();
    if (name == null) { throw new ParseError("Invalid Group Name Found."); }

    groupType = HabitatTypeGroupType.get(name);
    if (groupType == null) { groupType = new HabitatTypeGroupType(name); }

    line   = fin.readLine();
    while (line != null && key != VEGETATIVE_TYPE) {
      line = line.trim();
      strTok = new StringTokenizerPlus(line);
      if (line.length() == 0 || strTok.hasMoreTokens() == false) {
        line = fin.readLine();
        continue;
      }

      key = getKeyword(strTok);
      if (strTok.hasMoreTokens() == false) {
        throw new ParseError("Keyword: " + KEYWORD[key] + " has no value.");
      }

      switch (key) {
        case HABITAT_TYPES:
          habitatTypes = parseList(strTok,true);
          break;
        case CLIMAX_SPECIES:
          climaxSpecies = parseList(strTok);
          break;
        case SERAL_SPECIES:
          seralSpecies = parseList(strTok);
          break;
        case VEGETATIVE_TYPE:
          break;
        default:
          throw new ParseError("Unknown keyword in import text file.");
      }

      if (key != VEGETATIVE_TYPE) {
        line = fin.readLine();
      }
    }

    if (key == EOF || line == null) {
      throw new ParseError("Invalid Pathway file, no Pathway records found.");
    }

    return line;
  }

  private Vector parseList(StringTokenizerPlus strTok)
    throws ParseError
  {
    return parseList(strTok,false);
  }

  private Vector parseList(StringTokenizerPlus strTok, boolean isInteger)
    throws ParseError
  {
    Vector  result = null;
    Integer intVal;
    String  value = null;
    boolean moreTokens;

    if (strTok.hasMoreTokens()) {
      value = strTok.nextToken();
    }
    if (value == null) {return null;}

    result = new Vector(strTok.countTokens() + 1);

    do {
      if (!isInteger) {
        result.addElement(value);
      }
      else {
        try {
          intVal = Integer.valueOf(value);
          result.addElement(intVal);
        }
        catch (NumberFormatException NFE) {
          throw new ParseError("Invalid number found when reading a list.");
        }
      }
      moreTokens = strTok.hasMoreTokens();
      if (moreTokens) { value = strTok.nextToken();}
    }
    while (moreTokens);

    return result;
  }

  private String importVegType(String vegTypeStr, BufferedReader fin)
    throws ParseError
  {
    VegetativeType vegData;
    String         line;
    int            id = 0;

    vegData = new VegetativeType(this, vegTypeStr);
    line = vegData.importVegetativeType(fin);
    vegTypes.put(vegData.getCurrentState(),vegData);

    SimpplleType.updateAllData(vegData.getSpecies(),SimpplleType.SPECIES);
    SimpplleType.updateAllData(vegData.getSizeClass(),SimpplleType.SIZE_CLASS);
    SimpplleType.updateAllData(vegData.getDensity(),SimpplleType.DENSITY);

    return line;
  }

  public void saveAs(File outfile) throws SimpplleError {
    setFilename(Utility.makeSuffixedPathname(outfile,"",FILE_EXT));
    save();
  }

  public void save() throws SimpplleError {
    PrintWriter fout;
    try {
      fout = Utility.openPrintWriter(getFilename());
    }
    catch (SimpplleError err) {
      clearFilename();
      throw err;
    }

    save(fout);
    setChanged(false);
    fout.flush();
    fout.close();
  }

  public void save(PrintWriter fout) {
    saveHabitatTypeInfo(fout);
    saveVegetativeTypeInfo(fout);
  }

  private void saveHabitatTypeInfo(PrintWriter fout) {
    fout.println("CLASS HABITAT-TYPE-GROUP");
    fout.println("NAME " + getName());

    int i;
    Integer num;
    if (habitatTypes != null) {
      fout.print("HABITAT-TYPES ");
      for(i=0; i<habitatTypes.size(); i++) {
        if (i > 0) { fout.print(":"); }
        num = (Integer) habitatTypes.elementAt(i);
        fout.print(num.toString());
      }
      fout.println();
    }

    if (climaxSpecies != null) {
      fout.print("CLIMAX-SPECIES ");
      for(i=0; i<climaxSpecies.size(); i++) {
        if (i > 0) { fout.print(":"); }
        fout.print((String)climaxSpecies.elementAt(i));
      }
      fout.println();
    }

    if (seralSpecies != null) {
      fout.print("SERAL-SPECIES ");
      for(i=0; i<seralSpecies.size(); i++) {
        if (i > 0) { fout.print(":"); }
        fout.print((String)seralSpecies.elementAt(i));
      }
      fout.println();
    }

    if (knowledgeSource != null && knowledgeSource.length() > 0) {
      SystemKnowledge.writeKnowledgeSource(fout,knowledgeSource);
    }

    if (yearlyPathwayLives != null && yearlyPathwayLives.length > 0) {
      fout.print("YEARLY-PATHWAY-LIVES ");
      for (int j=0; j<yearlyPathwayLives.length; j++) {
        if (j > 0) { fout.print(":"); }
        if (yearlyPathwayLives[j] == Lifeform.NA) {
          fout.print("NA");
        }
        else {
          fout.print(yearlyPathwayLives[j].toString());
        }
      }
      fout.println();
    }
    fout.println("END");
    fout.println();
  }

  private void saveVegetativeTypeInfo(PrintWriter fout) {
    fout.println("CLASS ALL-VEG-TYPES");
    fout.println(vegTypes.size());

    VegetativeType vt;
    Iterator    keys = vegTypes.keySet().iterator();
    String      key;
    while (keys.hasNext()) {
      key = (String) keys.next();
      vt  = (VegetativeType) vegTypes.get(key);
      vt.save(fout);
    }
  }

  /**
   * Change the next stateis in the VegetativeType's
   * from String's to VegetativeType references.
   * Also find seedling sapling states.
   */
  private void fixNextStates (File logFile) throws ParseError {
    Iterator    keys = vegTypes.keySet().iterator();
    String         state;
    VegetativeType veg;
    RegionalZone   zone = Simpplle.currentZone;
    Species        species;
    Density        density;
    StringBuffer   strBuf = new StringBuffer();
    boolean        foundErrors = false;

    maxSeedSapDensity = null;
    while (keys.hasNext()) {
      state = (String) keys.next();
      veg = (VegetativeType) vegTypes.get(state);
      try {
        veg.fixNextState(strBuf);
      }
      catch (ParseError err) {
        foundErrors = true;
      }

      if (veg.getSizeClass() == SizeClass.SS) {
        density = veg.getDensity();
        if (maxSeedSapDensity == null ||
            density.getValue() > maxSeedSapDensity.getValue()) {
          maxSeedSapDensity = density;
        }
      }
    }

    if (foundErrors) {
      PrintWriter fout;
      try {
        FileWriter fwrite;
        fout = new PrintWriter(new FileWriter(logFile));
        fout.println(strBuf.toString());
        fout.flush();
        fout.close();
      }
      catch (IOException err) {
        System.err.println("Error trying to write log file, log follows: " +
                           strBuf.toString());
      }

      String msg = "Invalid States were found.\n" +
                   "Please refer to the log file: " + logFile + "\n" +
                   "For specific information.";
      throw new ParseError(msg);
    }

    findSeedSapStates();
    findGroupRegenerationStates();
  }

  private void findMaxSeedSapDensity() {
    Iterator    keys = vegTypes.keySet().iterator();
    String         state;
    VegetativeType veg;
    RegionalZone   zone = Simpplle.currentZone;
    Density        density;

    maxSeedSapDensity = null;
    while (keys.hasNext()) {
      state = (String) keys.next();
      veg = (VegetativeType) vegTypes.get(state);

      if (veg.getSizeClass() == SizeClass.SS) {
        density = veg.getDensity();
        if (density.getValue() > maxSeedSapDensity.getValue()) {
          maxSeedSapDensity = density;
        }
      }
    }
  }

  /**
   * This method is called by fixNextStates with the maximum
   * seedling sapling state density found.  This method puts
   * VegetativeType's in the seedSapStates Hashtable if their
   * density match's the maximum density.
   */
  private void findSeedSapStates() {
    Iterator    keys = vegTypes.keySet().iterator();
    String         state;
    VegetativeType veg;
    RegionalZone   zone = Simpplle.currentZone;

    seedSapStates.clear();
    while (keys.hasNext()) {
      state = (String) keys.next();
      veg = (VegetativeType) vegTypes.get(state);

      if (veg.getSizeClass() == SizeClass.SS &&
          veg.getDensity() == maxSeedSapDensity &&
          veg.getAge() == 1) {
        seedSapStates.put(veg.getSpecies(),veg);
      }
    }
  }

  private VegetativeType findLowestAgeState(VegetativeType veg) {
    int            densityValue = 9999, age = 9999;
    Density        density = null;
    VegetativeType state;

    // First find lowest age.
    Iterator e = vegTypes.values().iterator();
    while (e.hasNext()) {
      state = (VegetativeType)e.next();
      if (state.getSpecies() == veg.getSpecies() &&
          state.getSizeClass() == veg.getSizeClass() &&
          state.getDensity()   == veg.getDensity() &&
          state.getAge() < age) {
        age = state.getAge();
      }
    }

//    // Now find lowest density.
//    e = vegTypes.elements();
//    while (e.hasMoreElements()) {
//      state = (VegetativeType)e.nextElement();
//      if (state.getSpecies() == veg.getSpecies() &&
//          state.getSizeClass() == veg.getSizeClass() &&
//          state.getAge() == age &&
//          state.getDensity().getValue() < densityValue) {
//        density      = state.getDensity();
//        densityValue = density.getValue();
//      }
//    }
    return getVegetativeType(veg.getSpecies(),veg.getSizeClass(),age,veg.getDensity());
  }

  public static void findAllRegenerationStates() {
    ArrayList allGroups = getAllLoadedGroups();
    if (allGroups == null) { return; }
    for (int i=0; i<allGroups.size(); i++) {
      ((HabitatTypeGroup)allGroups.get(i)).findGroupRegenerationStates();
    }
  }
  private void findGroupRegenerationStates() {
    final RegionalZone   zone = Simpplle.getCurrentZone();
    final SizeClass E = SizeClass.get("E");

    regenStates.clear();

    for (Iterator i = vegTypes.values().iterator(); i.hasNext(); ) {
      VegetativeType veg = (VegetativeType) i.next();

      Species   species = veg.getSpecies();
      SizeClass sizeClass = veg.getSizeClass();
      Density   density = veg.getDensity();

      if ((species.getLifeform() != Lifeform.TREES) ||
          ((species.getLifeform() == Lifeform.TREES) &&
             (sizeClass == SizeClass.SS || sizeClass == SizeClass.SS_SS ||
              sizeClass == SizeClass.SS_POLE || sizeClass == SizeClass.SS_LARGE ||
              sizeClass == SizeClass.POLE_SS || sizeClass == SizeClass.LARGE_SS ||
              sizeClass == SizeClass.GRA || sizeClass == SizeClass.SHR ||
              sizeClass == E) ||
             /* Utah */
             (zone instanceof SouthwestUtah &&
               (sizeClass == SizeClass.GF || sizeClass == SizeClass.MEDIUM ||
                sizeClass == SizeClass.LS || sizeClass == SizeClass.MS ||
                sizeClass == SizeClass.TS)))) {
        VegetativeType tmpVeg = findLowestAgeState(veg);
        regenStates.put(tmpVeg, tmpVeg);
      }
    }
  }

  public static VegetativeType[] getAllRegenerationStates() {

    Hashtable        ht = new Hashtable();
    VegetativeType   vt;
    Enumeration      e;

    for (HabitatTypeGroup group : groups.values()) {
      e     = group.regenStates.elements();
      while (e.hasMoreElements()) {
        vt = (VegetativeType)e.nextElement();
        ht.put(vt,vt); // using ht to eliminate duplicates
      }
    }
    VegetativeType[] states = new VegetativeType[ht.size()];
    e = ht.elements();
    int j=0;
    while (e.hasMoreElements()) {
      states[j] = (VegetativeType)e.nextElement();
      j++;
    }
    Arrays.sort(states);
    return states;
  }

  public static Vector getAllRegenerationStatesWithSpecies(Species species) {
    VegetativeType[] states = getAllRegenerationStates();
    Vector           result = new Vector(states.length);
    Species          tmpSpecies;

    for (int i=0; i<states.length; i++) {
      tmpSpecies = states[i].getSpecies();
      if (tmpSpecies.contains(species)) {
        result.addElement(new RegenerationSuccessionInfo(species,states[i]));
      }
    }

    return result;
  }

  /**
   * Gets the print name of the Habitat Type Group.
   * @return a String.
   */
  public String toString() {
    return getName();
  }
/*
  private void printVector (Vector v, String vname) {
    System.out.print(vname);
    if (v == null) {
      System.out.print("null");
    }
    else {
      for (int i=0;i<v.size();i++) {
        System.out.print(" " + (Object) v.elementAt(i));
      }
    }
    System.out.println();
  }
*/
  public static void exportGISTable(File outfile) throws SimpplleError {
    PrintWriter fout;
    try {
      fout = new PrintWriter(new FileWriter(outfile));
    }
    catch (IOException err) {
      throw new SimpplleError("Problems writing file.");
    }
    
    fout.println("LIFEFORM,HT_GRP,SPECIES,SIZE_CLASS,AGE,DENSITY");
    for (HabitatTypeGroup group : groups.values()) {
      group.exportGISTableVegTypes(fout);
    }
    fout.flush();
    fout.close();
  }
  
  private void exportGISTableVegTypes(PrintWriter fout) {
    for (String stateName : vegTypes.keySet()) {
      VegetativeType vegType = vegTypes.get(stateName);
      
      String groupName = groupType.toString();
      Species species = vegType.getSpecies();
      String life = species.getLifeform().toString();
      String speciesStr = species.toString();
      String sizeClass  = vegType.getSizeClass().toString();
      String density    = vegType.getDensity().toString();
      int    age        = vegType.getAge();
      
      fout.printf("%s,%s,%s,%s,%d,%s%n", life,groupName,speciesStr,sizeClass,age,density);
    }
    
  }
  public void export(File outfile) throws SimpplleError {
    PrintWriter fout;
    try {
      fout = new PrintWriter(new FileWriter(outfile));
    }
    catch (IOException err) {
      throw new SimpplleError("Problems writing file.");
    }

    fout.println(KEYWORD[HTGRP] + " " + groupType.toString());

    int i;
    if (habitatTypes != null) {
      fout.print("  " + KEYWORD[HABITAT_TYPES] + " ");
      for(i=0; i<habitatTypes.size(); i++) {
        fout.print(" " + (Integer)habitatTypes.elementAt(i));
      }
      fout.println();
    }

    if (climaxSpecies != null) {
      fout.print("  " + KEYWORD[CLIMAX_SPECIES]);
      for(i=0; i<climaxSpecies.size(); i++) {
        fout.print(" " + (String)climaxSpecies.elementAt(i));
      }
      fout.println();
    }

    if (seralSpecies != null) {
      fout.print("  " + KEYWORD[SERAL_SPECIES] + " ");
      for(i=0; i<seralSpecies.size(); i++) {
        fout.print(" " + (String)seralSpecies.elementAt(i));
      }
      fout.println();
      fout.println();
    }

    VegetativeType veg;
    Iterator    keys = vegTypes.keySet().iterator();
    Vector         v = new Vector();
    String[]       states;

    while (keys.hasNext()) {
      v.addElement((String)keys.next());
    }
    states = (String[]) v.toArray(new String[v.size()]);
    Utility.sort(states);

    for(i=0; i<states.length; i++) {
      veg = (VegetativeType) vegTypes.get(states[i]);
      veg.export(fout);
    }
    fout.flush();
    fout.close();
  }

  private void printMagisAllVegTypes(PrintWriter fout) {
    Iterator    keys = vegTypes.keySet().iterator();
    VegetativeType vt;
    while (keys.hasNext()) {
      vt = (VegetativeType)vegTypes.get((String)keys.next());
      vt.printMagisAll(fout);
    }
  }

  public static void magisAllVegTypes(File outfile) throws SimpplleError {
    PrintWriter fout;

    try {
      fout = new PrintWriter(new FileWriter(outfile));
      for (HabitatTypeGroup group : groups.values()) {
        group.printMagisAllVegTypes(fout);
      }
      fout.flush();
      fout.close();
    }
    catch (IOException err) {
      throw new SimpplleError(err.getMessage());
    }
  }

  public void autoPositionSpecies(Species species) {
    Iterator    keys = vegTypes.keySet().iterator();
    VegetativeType vt;
    Point          p;

    while (keys.hasNext()) {
      vt = (VegetativeType) vegTypes.get((String)keys.next());
      if (species == vt.getSpecies()) {
        p = getDefaultVegTypePosition(vt);
        vt.setSpeciesPosition(species,p);
      }
    }
  }
  public void autoPositionAllSpecies() {
    Iterator    keys = vegTypes.keySet().iterator();
    VegetativeType vt;
    Point          p;

    while (keys.hasNext()) {
      vt = (VegetativeType) vegTypes.get((String)keys.next());
      p = getDefaultVegTypePosition(vt);
      vt.setSpeciesPosition(vt.getSpecies(),p);
    }
  }

  private Point getDefaultVegTypePosition(VegetativeType vt) {
    RegionalZone zone = Simpplle.getCurrentZone();

    switch (zone.getId()) {
      case ValidZones.COLORADO_PLATEAU:
        return getDefaultVegTypePositionColoradoPlateau(vt);
      default:
        return getDefaultVegTypePositionCommon(vt);
    }
  }

  private Point getDefaultVegTypePositionColoradoPlateau(VegetativeType vt) {
    SizeClass sizeClass = vt.getSizeClass();
    int       age       = vt.getAge();
    Density   density   = vt.getDensity();
    int       x,y;

    int xtr = 40; int xs = 40; int xg = 40; int xo = 40;
    int ytr = 20; int ys = 80; int yg = 80; int yo = 80;
    int xsep = 65;
    int ysep = 130;

    if (sizeClass == SizeClass.E) {
      x = xtr; y = ytr;
    }
    if (sizeClass == SizeClass.SS) {
      x = xtr+xsep; y = ytr;
    }
    else if (sizeClass == SizeClass.MEDIUM) {
      x = xtr+(xsep*2); y = ytr;
    }
    else if (sizeClass == SizeClass.MTS) {
      x = xtr+(xsep*3); y = ytr;
    }
    else if (sizeClass == SizeClass.MMU) {
      x = xtr+(xsep*4); y = ytr;
    }
    else if (sizeClass == SizeClass.LARGE) {
      x = xtr+(xsep*5); y = ytr;
    }
    else if (sizeClass == SizeClass.LTS) {
      x = xtr+(xsep*6); y = ytr;
    }
    else if (sizeClass == SizeClass.LMU) {
      x = xtr+(xsep*7); y = ytr;
    }
    else if (sizeClass == SizeClass.VERY_LARGE) {
      x = xtr+(xsep*8); y = ytr;
    }
    else if (sizeClass == SizeClass.VLMU) {
      x = xtr+(xsep*9); y = ytr;
    }

    else if (sizeClass == SizeClass.NS) {
      x = xo; y = yo;
    }
    else if (sizeClass == SizeClass.NF) {
      x = xo; y = yo;
    }

    else if (sizeClass == SizeClass.SMALL_SH) {
      x = xs; y = ys;
    }
    else if (sizeClass == SizeClass.MEDIUM_SH) {
      x = xs; y = ys;
    }
    else if (sizeClass == SizeClass.LARGE_SH) {
      x = xs; y = ys;
    }

    else if (sizeClass == SizeClass.CLUMPED) {
      x = xg; y = yg;
    }
    else if (sizeClass == SizeClass.UNIFORM) {
      x = xg; y = yg;
    }
    else {
      x = 20; y = 20;
    }

    x += ((age-1) * 10);
    y += ((age-1) * 10);

    if (density == Density.TWO) { y += ysep; }
    else if (density == Density.THREE) { y += (ysep*2); }
    else if (density == Density.FOUR) { y += (ysep*3); }

    return new Point(x,y);
  }
  private Point getDefaultVegTypePositionCommon(VegetativeType vt) {
    SizeClass sizeClass = vt.getSizeClass();
    int       age       = vt.getAge();
    Density   density   = vt.getDensity();
    int       x,y;

    if (sizeClass == SizeClass.SS) {
      x = 40; y = 20;
    }
    else if (sizeClass == SizeClass.POLE || sizeClass == SizeClass.SS_SS) {
      x = 135; y = 20;
    }
    else if (sizeClass == SizeClass.PTS || sizeClass == SizeClass.SS_POLE) {
      x = 180; y = 20;
    }
    else if (sizeClass == SizeClass.PMU || sizeClass == SizeClass.SS_LARGE) {
      x = 260; y = 20;
    }
    else if (sizeClass == SizeClass.MEDIUM || sizeClass == SizeClass.POLE) {
      x = 315; y = 20;
    }
    else if (sizeClass == SizeClass.MTS || sizeClass == SizeClass.POLE_SS) {
      x = 380; y = 20;
    }
    else if (sizeClass == SizeClass.MMU || sizeClass == SizeClass.POLE_POLE) {
      x = 455; y = 20;
    }
    else if (sizeClass == SizeClass.LARGE || sizeClass == SizeClass.POLE_LARGE) {
      x = 530; y = 20;
    }
    else if (sizeClass == SizeClass.LTS || sizeClass == SizeClass.LARGE) {
      x = 610; y = 20;
    }
    else if (sizeClass == SizeClass.LMU || sizeClass == SizeClass.LARGE_SS) {
      x = 690; y = 20;
    }
    else if (sizeClass == SizeClass.VERY_LARGE || sizeClass == SizeClass.LARGE_POLE) {
      x = 780; y = 20;
    }
    else if (sizeClass == SizeClass.VLTS || sizeClass == SizeClass.LARGE_LARGE) {
      x = 850; y = 20;
    }
    else if (sizeClass == SizeClass.VLMU) {
      x = 955; y = 20;
    }
    else if (sizeClass == SizeClass.NS) {
      x = 40; y = 80;
    }
    else {
      x = 0; y = 0;
    }

    x += ((age-1) * 10);
    y += ((age-1) * 10);

    if (density == Density.THREE || density == Density.O) { y += 130; }
    else if (density == Density.FOUR || density == Density.C) { y += 260; }

    return new Point(x,y);
  }

  public static void makeAllSimpplleTypeFiles(File prefix) throws SimpplleError {
    File newfile = Utility.makeSuffixedPathname(prefix,"-species","txt");
    printAllSimpplleType(newfile,getValidSpecies());

    newfile = Utility.makeSuffixedPathname(prefix,"-size","txt");
    printAllSimpplleType(newfile,getValidSizeClass());

    newfile = Utility.makeSuffixedPathname(prefix,"-density","txt");
    printAllSimpplleType(newfile,getValidDensity());

    newfile = Utility.makeSuffixedPathname(prefix,"-group","txt");
    printAllSimpplleType(newfile,getAllLoadedGroups());
  }
  private static void printAllSimpplleType(File filename, List values) throws SimpplleError {
    try {
      PrintWriter fout = new PrintWriter(new FileWriter(filename, true));
      for (int i = 0; i < values.size(); i++) {
        fout.println(values.get(i));
      }
      fout.flush();
      fout.close();
    }
    catch (IOException ex) {
      throw new SimpplleError("Problems writing file: " + filename,ex);
    }
  }

  // This code is essentially part II to the above code so it seemed to make
  // sense to place it here.
  public static void makeAllSimpplleTypesSourceFiles(File filename) throws SimpplleError {
    if (filename.getName().indexOf("species") != -1) {
      makeSimpplleTypesSourceFile(filename, "Species");
    }
    else if (filename.getName().indexOf("size") != -1) {
      makeSimpplleTypesSourceFile(filename, "SizeClass");
    }
    else if (filename.getName().indexOf("density") != -1) {
      makeSimpplleTypesSourceFile(filename, "Density");
    }
    else if (filename.getName().indexOf("group") != -1) {
      makeSimpplleTypesSourceFile(filename, "HabitatTypeGroupType");
    }
  }
  public static void makeSimpplleTypesSourceFile(File filename, String kind)
    throws SimpplleError
  {
    HashMap hm = new HashMap();
    BufferedReader fin;
    int            maxLength=0;

    try {
      fin = new BufferedReader(new FileReader(filename));
      String line = fin.readLine();
      while (line != null) {
        line = line.trim();
        if (line.length() > maxLength) { maxLength = line.length(); }
        hm.put(line.trim(), line.trim());
        line = fin.readLine();
      }
      fin.close();

      File outfile = new File(filename.getParent(),kind  + "Types.txt");
      PrintWriter fout = new PrintWriter(new FileWriter(outfile));
      for (Iterator i = hm.keySet().iterator(); i.hasNext(); ) {
        String name = (String) i.next();
        String varName = Utility.dashesToUnderscores(name);
        Formatting.fixedField(name,maxLength+1,true);
        fout.println("  public static final " + kind + " " +
                     Formatting.fixedField(varName, maxLength + 1, true) +
                     " = new " + kind + "(\"" + name + "\",false);");
      }
      fout.flush();
      fout.close();
    }
    catch (IOException ex) {
      throw new SimpplleError("Problems writing file: " + filename,ex);
    }
  }

  public Hashtable getLines(Species aSpecies, ProcessType aProcess){
          return (Hashtable)allLines.get(new HtSpeciesProcess(aSpecies, aProcess));
  }

  public void setLines(Hashtable newLines, Species aSpecies, ProcessType aProcess){
          allLines.put(new HtSpeciesProcess(aSpecies, aProcess), newLines);
  }

  class HtSpeciesProcess{
          Species     sp;
          ProcessType pc;

          public HtSpeciesProcess(Species aSpecies, ProcessType aProcess){
                  sp = aSpecies;
                  pc = aProcess;
          }

          public int hashCode(){
                  // M. Dousset - This needs to return the same value for equal objects (see equals method below)
                  // Documentation recommends different number for unequal object but lack time to do this at the moment
                  return sp.hashCode() ^ pc.hashCode();
          }

          public boolean equals(Object obj){
                  boolean retval = false;
                  HtSpeciesProcess hsp;

                  if(obj instanceof HtSpeciesProcess){
                          hsp = (HtSpeciesProcess)obj;
                          retval = (hsp.sp.equals(sp)) && (hsp.pc.equals(pc));
                  }
                  return retval;
          }
  }

}






