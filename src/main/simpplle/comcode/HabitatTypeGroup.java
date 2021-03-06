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
 * A habitat type group defines an ecological grouping, which stratifies vegetation pathways.
 */

public final class HabitatTypeGroup {

  /**
   * The extension used for files containing a habitat type group.
   */
  private static final String FILE_EXT = "pathway";

  /**
   * A collection of all created ecological groupings.
   */
  private static HashMap<HabitatTypeGroupType,HabitatTypeGroup> groups = new HashMap<>();

  /**
   * The type of ecological stratification that this represents.
   */
  private HabitatTypeGroupType groupType;

  /**
   * Maps vegetative type names to vegetative types.
   */
  private HashMap<String,VegetativeType> vegTypes;

  /**
   * An array of habitat type codes.
   */
  private Vector<Integer> habitatTypes;

  /**
   * An array of species that are in equilibrium.
   */
  private Vector<String> climaxSpecies;

  /**
   * An array of species advancing towards equilibrium.
   */
  private Vector<String> seralSpecies;

  /**
   * Maps species to their seed sapling states.
   */
  private Hashtable<Species,VegetativeType> seedSapStates;

  /**
   * A collection of unique regeneration states. (Abuses hash table to prevent duplication)
   */
  private Hashtable<VegetativeType,VegetativeType> regenStates;

  /**
   * An array of yearly pathway life forms.
   */
  private Lifeform[] yearlyPathwayLives;

  /**
   * The maximum seed sapling density of the existing states.
   */
  private Density maxSeedSapDensity;

  /**
   * A textual description of the source of knowledge for this vegetative pathway.
   */
  private String knowledgeSource;

  /**
   * The file that this group has been loaded from and/or will be saved to.
   */
  private File inputFile;

  /**
   * A flag indicating if any state has changed.
   */
  private boolean changed;

  /**
   * A flag indicating if this contains user-defined knowledge.
   */
  private boolean isUserData;

  /**
   * Holds pathway grid lines, which are drawn in the user interface.
   */
  private Hashtable allLines;

  /**
   * Keywords used in files containing a habitat type group.
   */
  private static String KEYWORD[] = {

      "CLASS",
      "END",
      "NAME",
      "HABITAT-TYPES",
      "CLIMAX-SPECIES",
      "SERAL-SPECIES",
      "SYSTEM",
      "HABITAT-TYPE-GROUP",
      "ALL-VEG-TYPES",
      "VEGETATIVE-TYPE",
      SystemKnowledge.KNOWLEDGE_SOURCE_KEYWORD,
      "YEARLY-PATHWAY-LIVES"

  };

  private static final int CLASS                = 0;
  private static final int END                  = 1;
  private static final int NAME                 = 2;
  private static final int HABITAT_TYPES        = 3;
  private static final int CLIMAX_SPECIES       = 4;
  private static final int SERAL_SPECIES        = 5;
  private static final int SYSTEM               = 6;
  private static final int HTGRP                = 7;
  private static final int ALL_VEG_TYPES        = 8;
  private static final int VEGETATIVE_TYPE      = 9;
  private static final int KNOWLEDGE_SOURCE     = 10;
  private static final int YEARLY_PATHWAY_LIVES = 11;
  private static final int EOF                  = 12;

  public HabitatTypeGroup () {

    groupType          = null;
    vegTypes           = new HashMap<>();
    habitatTypes       = null;
    climaxSpecies      = null;
    seralSpecies       = null;
    seedSapStates      = new Hashtable<>();
    regenStates        = new Hashtable<>();
    knowledgeSource    = "";
    yearlyPathwayLives = null;
    changed            = false;
    isUserData         = false;
    allLines           = new Hashtable();

  }

  public HabitatTypeGroup (String name) {

    this();

    groupType = HabitatTypeGroupType.get(name);
    if (groupType == null) {
      groupType = new HabitatTypeGroupType(name);
    }
    groups.put(groupType,this); // WARNING: Replaces instance if already exists.

  }

  //// Instance Methods ////

  public String getName () {
    return groupType.toString();
  }

  public int getStatesCount() {
    return vegTypes.size();
  }

  public HabitatTypeGroupType getType() {
    return groupType;
  }

  Vector<Integer> getHabitatTypes() {
    return habitatTypes;
  }

  void setHabitatTypes(Vector<Integer> habitatTypes) {
    this.habitatTypes = habitatTypes;
  }

  Vector<String> getClimaxSpecies() {
    return climaxSpecies;
  }

  void setClimaxSpecies(Vector<String> climaxSpecies) {
    this.climaxSpecies = climaxSpecies;
  }

  Vector<String> getSeralSpecies() {
    return seralSpecies;
  }

  void setSeralSpecies(Vector<String> seralSpecies) {
    this.seralSpecies = seralSpecies;
  }

  public Collection<VegetativeType> getVegTypes() {
    return vegTypes.values();
  }

  public boolean isValid() {
    return !vegTypes.isEmpty();
  }

  boolean isSystemGroup() {
    return !groupType.isUserCreated() || !isUserData;
  }

  public boolean isForested() {
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

  boolean isNonForested() {
    return !isForested();
  }

  public void setChanged(boolean value) {
    changed = value;
  }

  public boolean hasChanged() {
    return changed;
  }

  public void markChanged() {
    setChanged(true);
    setIsUserData(true);
    SystemKnowledge.markChanged(SystemKnowledge.VEGETATION_PATHWAYS);
  }

  public void closeFile() {
    clearFilename();
    setChanged(false);
  }

  public void clearFilename() {
    inputFile = null;
  }

  public File getFilename() {
    return inputFile;
  }

  public void setFilename(File file) {
    inputFile = file;
  }

  public String getKnowledgeSource() {
    return knowledgeSource;
  }

  public void setKnowledgeSource(String source) {
    knowledgeSource = source;
  }

  public boolean isUserData() {
    return isUserData;
  }

  void setIsUserData(boolean value) {
    isUserData = value;
  }

  public Lifeform[] getYearlyPathwayLifeforms() {
    return yearlyPathwayLives;
  }

  public void setYearlyPathwayLifeforms(ArrayList<Lifeform> values) {
    if (values == null || values.size() == 0) {
      yearlyPathwayLives = null;
    } else {
      yearlyPathwayLives = (Lifeform[]) values.toArray(new Lifeform[values.size()]);
    }
  }

  boolean isYearlyPathwayLifeform(Lifeform life) {
    if (yearlyPathwayLives == null) { return false; }

    for (int i=0; i<yearlyPathwayLives.length; i++) {
      if (yearlyPathwayLives[i] == life) { return true; }
    }
    return false;
  }

  public VegetativeType getVegetativeType (String name) {
    return vegTypes.get(name);
  }

  public VegetativeType getVegetativeType (Species species, Density density) {

    for (VegetativeType vegType : vegTypes.values()) {
      if (vegType.getSpecies() == species &&
          vegType.getDensity() == density) {
        return vegType;
      }
    }

    return null;

  }

  public VegetativeType getVegetativeType(Species species,
                                          SizeClass sizeClass,
                                          Density density) {

    if (species == null || sizeClass == null || density == null) {
      return null;
    } else {
      return getVegetativeType(species + "/" + sizeClass + "/" + density);
    }
  }

  public VegetativeType getVegetativeType (Species species,
                                           SizeClass sizeClass,
                                           int age,
                                           Density density) {

    if (species == null || sizeClass == null || density == null) {
      return null;
    } else {
      String ageString = age == 1 ? "" : Integer.toString(age);
      return getVegetativeType(species + "/" + sizeClass + ageString + "/" + density);
    }
  }

  VegetativeType findLowestDensityVegetativeType(VegetativeType vt) {
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

  VegetativeType findLowerDensityVegetativeType(VegetativeType vt) {
    return findLowerDensityVegetativeType(vt.getSpecies(),vt.getSizeClass(),
        vt.getAge(),vt.getDensity());
  }

  private VegetativeType findLowerDensityVegetativeType(Species species, SizeClass sizeClass,
                                                        int age, Density density) {
    Density newDensity = Density.getLowerDensity(density);

    if (newDensity == null) { return null; }

    return getVegetativeType(species,sizeClass,age,newDensity);
  }

  VegetativeType findHigherDensityVegetativeType(VegetativeType vt) {
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

  VegetativeType findNextYoungerVegetativeType(Species species, SizeClass sizeClass,
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

  VegetativeType findOldestVegetativeType(Species species, SizeClass sizeClass, Density density)
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

  private Hashtable getAllSpeciesHt() {
    return getAllSpeciesHt(new Hashtable());
  }

  private Hashtable getAllSpeciesHt(Hashtable ht) {
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

  public Species[] getAllSpecies() {
    return HabitatTypeGroup.getAllSpecies(getAllSpeciesHt());
  }

  private void updateAllAgeList(ArrayList<Integer> allAge) {
    for (VegetativeType vt : vegTypes.values()) {
      Integer age = vt.getAge();
      if (allAge.contains(age) == false) { allAge.add(age); }
    }
  }

  private Hashtable getAllSizeClassHt() {
    return getAllSizeClassHt(new Hashtable());
  }

  private Hashtable getAllSizeClassHt(Hashtable ht) {
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

  public SizeClass[] getAllSizeClass() {
    return HabitatTypeGroup.getAllSizeClass(getAllSizeClassHt());
  }

  private Hashtable getAllDensityHt() {
    return getAllDensityHt(new Hashtable());
  }
  private Hashtable getAllDensityHt(Hashtable ht) {
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

  boolean isMemberSpecies(Species species) {
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

  Vector findPreviousStates(VegetativeType vegType) {
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

  VegetativeType getSeedSapState(Species species) {
    return seedSapStates.get(species);
  }

  //// Class Methods ////

  static ArrayList<Integer> getAllAge() {
    ArrayList<Integer> values = new ArrayList<>();
    for (HabitatTypeGroup group : groups.values()) {
      group.updateAllAgeList(values);
    }
    return values;
  }

  public static Density[] getAllDensity(Hashtable ht) {
    Density[] allDensity = new Density[ht.size()];
    Enumeration e = ht.keys();
    int i = 0;
    while (e.hasMoreElements()) {
      allDensity[i] = (Density) e.nextElement();
      i++;
    }
    Arrays.sort(allDensity);
    return allDensity;
  }

  public static SizeClass[] getAllSizeClass(Hashtable ht) {
    SizeClass[] allSizeClass = new SizeClass[ht.size()];
    Enumeration e = ht.keys();
    int i = 0;
    while (e.hasMoreElements()) {
      allSizeClass[i] = (SizeClass) e.nextElement();
      i++;
    }
    Arrays.sort(allSizeClass);
    return allSizeClass;
  }

  public static Species[] getAllSpecies(Hashtable ht) {
    Species[] allSpecies = new Species[ht.size()];
    Enumeration e = ht.keys();
    int i = 0;
    while (e.hasMoreElements()) {
      allSpecies[i] = (Species) e.nextElement();
      i++;
    }
    Arrays.sort(allSpecies);
    return allSpecies;
  }

  public static VegetativeType[] getAllRegenerationStates() {
    Hashtable<VegetativeType,VegetativeType> ht = new Hashtable<>();
    VegetativeType vt;
    Enumeration e;
    for (HabitatTypeGroup group : groups.values()) {
      e = group.regenStates.elements();
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
    Vector result = new Vector(states.length);
    Species tmpSpecies;
    for (int i = 0; i < states.length; i++) {
      tmpSpecies = states[i].getSpecies();
      if (tmpSpecies.contains(species)) {
        result.addElement(new RegenerationSuccessionInfo(species,states[i]));
      }
    }
    return result;
  }

  public static String[] getAllSortedMatchingSpeciesTypes(Species species) {
    Hashtable ht = new Hashtable();
    String[] tmpTypes;
    String[] result;
    int j;
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

  public static Vector getValidDensity() {
    Hashtable ht = new Hashtable();
    for (HabitatTypeGroup group : groups.values()) {
      group.getAllDensityHt(ht);
    }
    Density[] allDensity = HabitatTypeGroup.getAllDensity(ht);
    Vector v = new Vector();
    for(int i = 0; i < allDensity.length; i++) {
      v.addElement(allDensity[i]);
    }
    return v;
  }

  public static Vector getValidSizeClass() {
    Hashtable ht = new Hashtable();
    for (HabitatTypeGroup group : groups.values()) {
      group.getAllSizeClassHt(ht);
    }
    SizeClass[] allSizeClass = HabitatTypeGroup.getAllSizeClass(ht);
    Vector v = new Vector();
    for(int i = 0; i < allSizeClass.length; i++) {
      v.addElement(allSizeClass[i]);
    }
    return v;
  }

  public static Vector getValidSpecies() {
    Hashtable ht = new Hashtable();
    for (HabitatTypeGroup group : groups.values()) {
      group.getAllSpeciesHt(ht);
    }
    Species[] allSpecies = HabitatTypeGroup.getAllSpecies(ht);
    Vector v = new Vector();
    for(int i = 0; i < allSpecies.length; i++) {
      v.addElement(allSpecies[i]);
    }
    return v;
  }

  public static VegetativeType getVegType(String vegTypeStr) {
    if (vegTypeStr == null) { return null;}
    VegetativeType vt;
    for (HabitatTypeGroup group : groups.values()) {
      vt = group.getVegetativeType(vegTypeStr);
      if (vt != null) { return vt; }
    }
    return null;
  }

  static boolean hasSpecies(Species species) {
    for (HabitatTypeGroup group : groups.values()) {
      for (VegetativeType vt : group.vegTypes.values()) {
        if (vt.getSpecies().equals(species)) {
          return true;
        }
      }
    }
    return false;
  }

  public static HabitatTypeGroup findInstance(String groupName) {
    HabitatTypeGroupType ht = HabitatTypeGroupType.get(groupName);
    if (ht == null) { return null; }
    return findInstance(ht);
  }

  public static HabitatTypeGroup findInstance(HabitatTypeGroupType groupType) {
    return groups.get(groupType);
  }

  static void findAllRegenerationStates() {
    ArrayList allGroups = getAllLoadedGroups();
    if (allGroups == null) { return; }
    for (int i = 0; i < allGroups.size(); i++) {
      ((HabitatTypeGroup)allGroups.get(i)).findGroupRegenerationStates();
    }
  }

  public static void removeGroup(HabitatTypeGroup group) {
    if (group != null) {
      groups.remove(group.getType());
      group.markChanged();
    }
  }

  static void removeGroup(String groupName) {
    HabitatTypeGroup group = findInstance(groupName);
    if (group != null) {
      groups.remove(group.getType());
    }
  }

  static void clearGroups() {
    groups.clear();
  }

  public static Vector getLoadedGroups() {
    if (groups.size() == 0) { return null; }
    return new Vector(groups.values());
  }

  static ArrayList getAllLoadedGroups() {
    if (groups == null || groups.size() == 0) { return null; }
    return new ArrayList(groups.values());
  }

  public static String[] getLoadedGroupNames() {
    if (groups.size() == 0) { return null; }
    String[] names = new String[groups.size()];
    int i=0;
    for (HabitatTypeGroup group : groups.values()) {
      names[i] = group.getName();
      i++;
    }
    Arrays.sort(names);
    return names;
  }

  public static Vector getAllLoadedTypes() {
    if (groups.size() == 0) { return null; }

    Vector dummy = new Vector(groups.keySet());
    Collections.sort(dummy);
    return dummy;
  }

  public static ArrayList<HabitatTypeGroupType> getAllLoadedTypesNew() {
    return getAllLoadedTypesNew(false);
  }

  public static ArrayList<HabitatTypeGroupType> getAllLoadedTypesNew(boolean includeAny) {
    ArrayList<HabitatTypeGroupType> values =
        new ArrayList<HabitatTypeGroupType>(groups.keySet());
    Collections.sort(values);
    if (includeAny) {
      values.add(0, HabitatTypeGroupType.ANY);
    }
    return values;
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
      yearlyPathwayLives[i] = Lifeform.findByName((String)v.get(i));
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

    vegTypes.clear();

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

  /**
   * Removes a vegetative type from this habitat type group. If the size class equals SS, then the
   * max seed sap density, seed sap states, and group regeneration states are updated.
   *
   * @param veg a vegetative type to remove
   */
  public void removeVegetativeType(VegetativeType veg) {

    // Remove the vegetative type from this group
    vegTypes.remove(veg.getPrintName());

    // Update derived attributes if the size class is SS
    SizeClass sizeClass = veg.getSizeClass();
    if (sizeClass == SizeClass.SS) {
      findMaxSeedSapDensity();
      findSeedSapStates();
      findGroupRegenerationStates();
    }

    // Flag the user modification
    markChanged();

  }

  /**
   * Removes all vegetative types from this habitat type group.
   */
  private void removeAllVegetativeTypes() {

    // Remove all vegetative types
    vegTypes.clear();

    // Update derived attributes
    findMaxSeedSapDensity();
    findSeedSapStates();
    findGroupRegenerationStates();

    // Flag the user modification
    markChanged();

  }

  /**
   * This method is responsible for reading the information contained
   * in a pathway data files.
   * @param fin is a BufferedReader
   * @return the new HabitatTypeGroup
   */
  public static HabitatTypeGroup read(BufferedReader fin) throws ParseError, IOException {
    HabitatTypeGroup newGroup = new HabitatTypeGroup();
    newGroup.readData(fin);
    return newGroup;
  }

  private void readData(BufferedReader fin) throws ParseError, IOException {

    String dir  = System.getProperty("user.dir");
    String name = "Pathways";
    File logFile = new File(dir,name + ".log");
    int n = 1;
    while (logFile.exists()) {
      logFile = new File(dir,name + "-" + n++ + ".log");
    }

    int key = EOF;
    String value;

    do {
      String line = fin.readLine();
      if (line == null) {
        key = EOF;
        continue;
      }

      StringTokenizerPlus strTok = new StringTokenizerPlus(line," ");
      if (!strTok.hasMoreTokens()) continue;

      key = getKeyword(strTok);
      if (!strTok.hasMoreTokens()) {
        throw new ParseError("Keyword: " + KEYWORD[key] + " has no value.");
      }

      if (key == CLASS) {
        value = strTok.nextToken();
      } else {
        throw new ParseError("Invalid record, first keyword must be CLASS");
      }

      if (keyMatch(value,HTGRP)) {
        readHtGrp(fin);
      } else if (keyMatch(value,ALL_VEG_TYPES)) {
        readVegTypes(fin);
      } else {
        throw new ParseError ("Invalid Class Specified:" + value);
      }
    } while (key != EOF);

    fixNextStates(logFile);  /* also builds the SS states hashtable */
    setChanged(false);

  }

  public static void importSpeciesChangeFile(File file) throws ParseError {
    String line="";
    try {
      BufferedReader fin = new BufferedReader(new FileReader(file));
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

  VegetativeType findSpeciesChangeNextState(Flat3Map trkSpecies) {
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

  void importTextFile(File infile) throws SimpplleError {
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

      vegTypes.clear();

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
      vegTypes.clear();
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

  private String importHtGrp(String htGrpName, BufferedReader fin) throws IOException, ParseError
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

  private Vector parseList(StringTokenizerPlus strTok) throws ParseError
  {
    return parseList(strTok,false);
  }

  private Vector parseList(StringTokenizerPlus strTok, boolean isInteger) throws ParseError
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

  private String importVegType(String vegTypeStr, BufferedReader fin) throws ParseError
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
        fout.print(climaxSpecies.elementAt(i));
      }
      fout.println();
    }

    if (seralSpecies != null) {
      fout.print("SERAL-SPECIES ");
      for(i=0; i<seralSpecies.size(); i++) {
        if (i > 0) { fout.print(":"); }
        fout.print(seralSpecies.elementAt(i));
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
        fout.print(" " + habitatTypes.elementAt(i));
      }
      fout.println();
    }

    if (climaxSpecies != null) {
      fout.print("  " + KEYWORD[CLIMAX_SPECIES]);
      for(i=0; i<climaxSpecies.size(); i++) {
        fout.print(" " + climaxSpecies.elementAt(i));
      }
      fout.println();
    }

    if (seralSpecies != null) {
      fout.print("  " + KEYWORD[SERAL_SPECIES] + " ");
      for(i=0; i<seralSpecies.size(); i++) {
        fout.print(" " + (seralSpecies.elementAt(i)));
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

  public static void exportCoordinateTable(File file) throws SimpplleError {

    try (PrintWriter writer = new PrintWriter(file)) {

      writer.write("HabitatTypeGroup,"
                 + "Species,"
                 + "Size,"
                 + "Age,"
                 + "Density,"
                 + "View,"
                 + "X,"
                 + "Y\n");

      for (HabitatTypeGroup group : groups.values()) {
        for (VegetativeType type : group.getVegTypes()) {
          for (Species species : type.getSpeciesWithPositions()) {
            Point point = type.getSpeciesPosition(species);
            writer.format("%s,", group.getType().getName());
            writer.format("%s,", type.getSpecies());
            writer.format("%s,", type.getSizeClass());
            writer.format("%d,", type.getAge());
            writer.format("%s,", type.getDensity());
            writer.format("%s,", species);
            writer.format("%d,", point.x);
            writer.format("%d\n", point.y);
          }
        }
      }
    } catch (IOException e) {
      throw new SimpplleError(e.getMessage());
    }
  }

  /**
   * Imports state coordinates from a CSV file. Missing habitat type groups, vegetative types, or
   * species result in an exception. Missing coordinates are replaced with zero.
   *
   * @param file a reference to a CSV file
   * @throws SimpplleError if there is a parsing error
   */
  public static void importCoordinateTable(File file) throws SimpplleError {

    try (CsvReader reader = new CsvReader(file,",")) {

      if (!reader.hasField("HabitatTypeGroup")) {
        throw new SimpplleError("Missing column 'HabitatTypeGroup'");
      }
      if (!reader.hasField("Species")) {
        throw new SimpplleError("Missing column 'Species'");
      }
      if (!reader.hasField("Size")) {
        throw new SimpplleError("Missing column 'Size'");
      }
      if (!reader.hasField("Age")) {
        throw new SimpplleError("Missing column 'Age'");
      }
      if (!reader.hasField("Density")) {
        throw new SimpplleError("Missing column 'Density'");
      }
      if (!reader.hasField("View")) {
        throw new SimpplleError("Missing column 'View'");
      }
      if (!reader.hasField("X")) {
        throw new SimpplleError("Missing column 'X'");
      }
      if (!reader.hasField("Y")) {
        throw new SimpplleError("Missing column 'Y'");
      }

      while (reader.nextRecord()) {

        String groupName = reader.getString("HabitatTypeGroup");
        HabitatTypeGroupType groupType = HabitatTypeGroupType.get(groupName);
        if (groupType == null)  {
          throw new SimpplleError("Missing habitat type group type " + groupName);
        }

        HabitatTypeGroup group = groups.get(groupType);
        if (group == null) {
          throw new SimpplleError("Missing habitat type group " + groupName);
        }

        String species = reader.getString("Species");
        String size    = reader.getString("Size");
        String age     = reader.getString("Age");
        String density = reader.getString("Density");
        String vegName = species + "/" + size + (age.equals("1") ? "" : age) + "/" + density;
        VegetativeType type = group.getVegetativeType(vegName);
        if (type == null) {
          throw new SimpplleError("Missing vegetative type " + vegName);
        }

        String viewName = reader.getString("View");
        Species view = Species.get(viewName);
        if (view == null) {
          throw new SimpplleError("Missing species " + viewName);
        }

        Integer x = reader.getInteger("X");
        if (x == null) x = 0;

        Integer y = reader.getInteger("Y");
        if (y == null) y = 0;

        type.setSpeciesPosition(view,new Point(x,y));

      }
    } catch (IOException e) {
      throw new SimpplleError(e.getMessage());
    } catch (NumberFormatException e) {
      throw new SimpplleError("Exception parsing field " + e.getMessage());
    }
  }

  public static void exportHabitatTypeGroupTable(File file) throws SimpplleError {

    try (PrintWriter writer = new PrintWriter(file)) {

      writer.write("HabitatTypeGroup,"
                 + "HabitatTypes,"
                 + "ClimaxSpecies,"
                 + "SeralSpecies\n");

      for (HabitatTypeGroup group : groups.values()) {

        writer.format("%s,",group.getType().getName());

        Vector<Integer> habitatTypes = group.getHabitatTypes();
        for (int i = 0; i < habitatTypes.size(); i++) {
          writer.write(Integer.toString(habitatTypes.get(i)));
          if (i < habitatTypes.size() - 1) {
            writer.write(":");
          }
        }
        writer.write(",");
        Vector<String> climaxSpecies = group.getClimaxSpecies();
        for (int i = 0; i < climaxSpecies.size(); i++) {
          writer.write(climaxSpecies.get(i));
          if (i < climaxSpecies.size() - 1) {
            writer.write(":");
          }
        }
        writer.write(",");
        Vector<String> seralSpecies = group.getSeralSpecies();
        for (int i = 0; i < seralSpecies.size(); i++) {
          writer.write(seralSpecies.get(i));
          if (i < seralSpecies.size() - 1) {
            writer.write(":");
          }
        }
        writer.write("\n");
      }
    } catch (IOException e) {
      throw new SimpplleError(e.getMessage());
    }
  }

  /**
   * Imports habitat type groups from a CSV file containing the columns HabitatTypeGroup,
   * HabitatTypes, ClimaxSpecies, and SeralSpecies delimited by a comma character. Habitat type
   * groups overwrite existing or adds new groups.
   *
   * @param file a reference to a CSV file
   * @throws SimpplleError if there is a parsing error
   */
  public static void importHabitatTypeGroupTable(File file) throws SimpplleError {

    try (CsvReader reader = new CsvReader(file,",")) {

      if (!reader.hasField("HabitatTypeGroup")) {
        throw new SimpplleError("Missing column 'HabitatTypeGroup'");
      }
      if (!reader.hasField("HabitatTypes")) {
        throw new SimpplleError("Missing column 'HabitatTypes'");
      }
      if (!reader.hasField("ClimaxSpecies")) {
        throw new SimpplleError("Missing column 'ClimaxSpecies'");
      }
      if (!reader.hasField("SeralSpecies")) {
        throw new SimpplleError("Missing column 'SeralSpecies'");
      }

      while (reader.nextRecord()) {

        String name = reader.getString("HabitatTypeGroup");
        HabitatTypeGroup group = findInstance(name);
        if (group == null) {
          group = new HabitatTypeGroup(name);
        }

        Integer[] habitatTypes = reader.getIntegerArray("HabitatTypes",":");
        group.setHabitatTypes(new Vector<>(Arrays.asList(habitatTypes)));

        String[] climaxSpecies = reader.getStringArray("ClimaxSpecies",":");
        group.setClimaxSpecies(new Vector<>(Arrays.asList(climaxSpecies)));

        String[] seralSpecies = reader.getStringArray("SeralSpecies",":");
        group.setSeralSpecies(new Vector<>(Arrays.asList(seralSpecies)));

      }
    } catch (IOException e) {
      throw new SimpplleError(e.getMessage());
    } catch (NumberFormatException e) {
      throw new SimpplleError("Exception parsing field " + e.getMessage());
    }
  }

  public static void exportPathwayTable(File file) throws SimpplleError {

    try (PrintWriter writer = new PrintWriter(file)) {

      writer.write("HabitatTypeGroup,"
                 + "FromSpecies,"
                 + "FromSize,"
                 + "FromAge,"
                 + "FromDensity,"
                 + "Process,"
                 + "ToSpecies,"
                 + "ToSize,"
                 + "ToAge,"
                 + "ToDensity\n");

      for (HabitatTypeGroup group : groups.values()) {
        for (VegetativeType type : group.getVegTypes()) {
          for (Process process : type.getProcesses()) {
            VegetativeType next = type.getProcessNextState(process);
            if (next != null) {
              writer.format("%s,", group.getType().getName());
              writer.format("%s,", type.getSpecies());
              writer.format("%s,", type.getSizeClass());
              writer.format("%d,", type.getAge());
              writer.format("%s,", type.getDensity());
              writer.format("%s,", process);
              writer.format("%s,", next.getSpecies());
              writer.format("%s,", next.getSizeClass());
              writer.format("%d,", next.getAge());
              writer.format("%s\n", next.getDensity());
            }
          }
        }
      }
    } catch (IOException e) {
      throw new SimpplleError(e.getMessage());
    }
  }

  /**
   * Imports pathway states from a CSV file. New pathway states are added to an existing habitat
   * type group. Next states are assigned to each from vegetative type for the corresponding
   * process.
   *
   * @param file a reference to a CSV file
   * @throws SimpplleError if there is a parsing error
   */
  public static void importPathwayTable(File file) throws SimpplleError {

    try (CsvReader reader = new CsvReader(file,",")) {

      if (!reader.hasField("HabitatTypeGroup")) {
        throw new SimpplleError("Missing column 'HabitatTypeGroup'");
      }
      if (!reader.hasField("FromSpecies")) {
        throw new SimpplleError("Missing column 'FromSpecies'");
      }
      if (!reader.hasField("FromSize")) {
        throw new SimpplleError("Missing column 'FromSize'");
      }
      if (!reader.hasField("FromAge")) {
        throw new SimpplleError("Missing column 'FromAge'");
      }
      if (!reader.hasField("FromDensity")) {
        throw new SimpplleError("Missing column 'FromDensity'");
      }
      if (!reader.hasField("Process")) {
        throw new SimpplleError("Missing column 'Process'");
      }
      if (!reader.hasField("ToSpecies")) {
        throw new SimpplleError("Missing column 'ToSpecies'");
      }
      if (!reader.hasField("ToSize")) {
        throw new SimpplleError("Missing column 'ToSize'");
      }
      if (!reader.hasField("ToAge")) {
        throw new SimpplleError("Missing column 'ToAge'");
      }
      if (!reader.hasField("ToDensity")) {
        throw new SimpplleError("Missing column 'ToDensity'");
      }

      for (HabitatTypeGroup group : groups.values()) {
        group.removeAllVegetativeTypes();
      }

      while (reader.nextRecord()) {

        String name = reader.getString("HabitatTypeGroup");
        HabitatTypeGroup group = groups.get(name);
        if (group == null) {
          group = new HabitatTypeGroup(name);
        }

        String fromSpecies = reader.getString("FromSpecies");
        String fromSize    = reader.getString("FromSize");
        String fromAge     = reader.getString("FromAge");
        String fromDensity = reader.getString("FromDensity");
        String fromName    = fromSpecies + "/" + fromSize + (fromAge.equals("1") ? "" : fromAge) + "/" + fromDensity;

        VegetativeType fromType;
        try {
          fromType = group.getVegetativeType(fromName);
          if (fromType == null) {
            fromType = new VegetativeType(group, fromName);
          }
        } catch (ParseError e) {
          throw new SimpplleError("Invalid source vegetative type " + e.getMessage());
        }

        String toSpecies = reader.getString("ToSpecies");
        String toSize    = reader.getString("ToSize");
        String toAge     = reader.getString("ToAge");
        String toDensity = reader.getString("ToDensity");
        String toName    = toSpecies + "/" + toSize + (toAge.equals("1") ? "" : toAge) + "/" + toDensity;

        VegetativeType toType;
        try {
          toType = group.getVegetativeType(toName);
          if (toType == null) {
            toType = new VegetativeType(group, toName);
          }
        } catch (ParseError e) {
          throw new SimpplleError("Invalid destination vegetative type " + e.getMessage());
        }

        String processName = reader.getString("Process");
        Process process = Process.findInstance(processName);

        if (process != null) {
          fromType.addProcessNextState(process,toType);
          group.addVegetativeType(fromType);
          group.addVegetativeType(toType);
        } else {
          throw new SimpplleError("Unable to find process " + processName);
        }
      }
    } catch (IOException e) {
      throw new SimpplleError(e.getMessage());
    }
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

  private static void makeSimpplleTypesSourceFile(File filename, String kind) throws SimpplleError
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
