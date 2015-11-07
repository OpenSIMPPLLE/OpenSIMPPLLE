package simpplle.comcode;

import java.io.*;
import java.util.*;

/**
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class defines the different kinds of species.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller 
 */
public class Species extends SimpplleType implements Externalizable, SpeciesStatic {
  static final long serialVersionUID = 3959573148579838330L;
  static final int  version          = 5;

  public static final int COLUMN_COUNT = 7;

  public static final int CODE_COL            = 0;
  public static final int DESCRIPTION_COL     = 1;
  public static final int LIFEFORM_COL        = 2;
  public static final int FIRE_RESISTANCE_COL = 3;
  public static final int RESISTANCE_COND_COL = 4;
  public static final int INVASIVE_COL        = 5;
  public static final int PATHWAY_PRESENT_COL = 6;

  private static HashMap transformHm;
  private String         species;
  private String         description;
  private Lifeform       lifeform;
  private FireResistance fireResistance = FireResistance.UNKNOWN;
  private HashMap        conditionalGroups;
  private boolean        invasive;

  private static boolean noChangeRead=false;

  private static ArrayList<Species> invasiveList = new ArrayList<Species>();

  public static HashMap<Short,Species> simIdHm = new HashMap<Short,Species>();
  private short simId=-1; // Random Access File ID
  public static short nextSimId=0;
  public short getSimId() {
    if (simId == -1) {
      simId = nextSimId;
      nextSimId++;
      simIdHm.put(simId,this);
    }
    return simId;
  }

  /**
   * Needs to be present for database, does nothing.
   * @param id short
   */
  public void setSimId(short id) {}
/**
 * 
 * @param simId 
 * @return the simId from the species hashmap.  
 */
  public static Species lookUpSpecies(short simId) { return simIdHm.get(simId); }
  
  public static void readExternalSimIdHm(ObjectInput in) throws IOException, ClassNotFoundException {
    int version = in.readInt();

    int size = in.readInt();
    for (int i=0; i<size; i++) {
      short id = in.readShort();
      Species species = (Species)readExternalSimple(in,SimpplleType.SPECIES);
      simIdHm.put(id,species);
      if ( (id+1) > nextSimId) {
        nextSimId = (short)(id+1);
      }
    }
  }
  public static void writeExternalSimIdHm(ObjectOutput out) throws IOException {
    out.writeInt(version);

    out.writeInt(simIdHm.size());
    for (Short id : simIdHm.keySet()) {
      out.writeShort(id);
      Species species = simIdHm.get(id);
      species.writeExternalSimple(out);
    }
  }

  /**
   * For use in serialization
   */
  public Species() {
    this.description       = null;
    this.lifeform          = Lifeform.NA;
    this.fireResistance    = FireResistance.UNKNOWN;
    this.conditionalGroups = null;
  }
  /**
   * Overloaded constructor. Takes in a series of values for species type, description of species, lifeform, and usercreated boolean
   * @param species the species
   * @param desc the description of the species
   * @param lifeform 
   * @param userCreated true if user created.  
   */
  public Species(String species, String desc, Lifeform lifeform, boolean userCreated) {
    this.species           = species.toUpperCase();
    this.description       = desc;
    this.lifeform          = lifeform;
    this.conditionalGroups = null;

    updateAllData(this,SimpplleType.SPECIES);
  }
/**
 * Constructor for Species 
 * @param species name of species with dashes.  
 * @param userCreated true if user entered this species for the first time
 */
  public Species(String species, boolean userCreated) {
    this(species,species,Lifeform.NA,userCreated);
  }
  /**
   * This constructor is used only when an area is loaded with an invalid
   * species.  In this case we don't place the species in the all species
   * hash so it will be marked as invalid thus alerting the user to the
   * problem.
   * @param species
   */
  public Species(String species) {
    this();
    this.species = species;
    this.description = species;
  }

  public static void clearInvasive() {
    invasiveList.clear();
  }
/**
 * Method to get invasive species from the GUI table model.  
 * @param row from table model, this will be the row with the invasive species
 * @return an invasive species
 */
  public static Species getInvasiveSpecies(int row) {
    return invasiveList.get(row);
  }
  /**
   * Called from the invasive species table model.  It counts the rows by returning the size of invasive species list.  
   * This will give a count of unique number of invasive species  
   * @return int representing count of invasive species
   */
  public static int getInvasiveSpeciesRowCount() {
    return invasiveList.size();
  }
  public static ArrayList<Species> getInvasiveSpeciesList() {
    return invasiveList;
  }
/**
 * Method to include a new species in the invasiveList.  Checks if species is invasive and not already on the invasiveList (ArrayList)
 * @param newSpecies Species to be updated.  
 */
  public static void update(Species newSpecies) {
    if (newSpecies.isInvasive() && invasiveList.contains(newSpecies) == false) {
      invasiveList.add(newSpecies);
    }
  }
/**
 * Makes a toString by returning species string (in uppercase).  
 */
  public String toString() { return species; }
  
  public String getDescription() { return description; }

  public boolean lookupEquals(String name) {
    return equals(get(name));
  }
/**
 * Casts the object from Species ArrayList Object to Species object.  returns false if species ==null or object == null.  
 * returns true if current species equals object cast to species.   
 */
  public boolean equals(Object obj) {
    if (this == obj) { return true; }
    if (obj instanceof Species) {
      if (species == null || obj == null) { return false; }

      return species.equals(((Species)obj).species);
    }
    return false;
  }
/**
 * Makes a hashcode from the species name.
 */
  public int hashCode() {
    return species.hashCode();
  }
/**
 * Requisite compareTo method.  Compares a parameter species object with this one, by their toStrings. 
 */
  public int compareTo(Object o) {
    if (o == null) { return -1; }
    return species.compareTo(o.toString());
  }

  public boolean isValid() { return Species.get(species) != null; }

  /**
   * Overloaded get species object method. Used to get the species object by its string name.   
   * @param speciesStr this is not the object, but the string representing the species
   * @return result of passing species string and false to get(String, boolean)
   */
  public static Species get(String speciesStr) {
    return get(speciesStr,false);
  }
  /**
   * Overloaded get species object method.  Gets the Species object by its string name .  
   * @param speciesStr name of species used as key to get the species from hashmap then cast to a species object.  
   * @param ifNoExistCreate true if the species does not already exist in database false if already exists. 
   * Note: if does not exist a new Species object will be instantiated with the new species name.  
   * @return Species species object
   */
  public static Species get(String speciesStr, boolean ifNoExistCreate) {
    Species species = (Species)allSpeciesHm.get(speciesStr.toUpperCase());
    if (species == null && ifNoExistCreate) {
      species = new Species(speciesStr,true);
    }
    return species;
  }
/**
 * initializes the HashMap which holds species 
 * key = string with name of species, value is Species - the order of the two is not the same, hence why the transformMap
 */
  private static void initializeTransformHt() {
    transformHm = new HashMap();

    transformHm.put("DF-PP",Species.PP_DF);
    transformHm.put("AF-DF",Species.DF_AF);
    transformHm.put("LP-DF",Species.DF_LP);
    transformHm.put("ES-DF",Species.DF_ES);
    transformHm.put("LP-ES",Species.ES_LP);
    transformHm.put("AF-ES",Species.ES_AF);
    transformHm.put("AF-LP",Species.LP_AF);
    transformHm.put("DF-WB",Species.WB_DF);
    transformHm.put("ES-WB",Species.WB_ES);

    transformHm.put("LP-PP-DF",Species.DF_PP_LP);
    transformHm.put("PP-LP-DF",Species.DF_PP_LP);
    transformHm.put("PP-DF-LP",Species.DF_PP_LP);
    transformHm.put("LP-DF-PP",Species.DF_PP_LP);
    transformHm.put("DF-LP-PP",Species.DF_PP_LP);

    transformHm.put("PP-PF-DF",Species.DF_PP_PF);
    transformHm.put("PF-PP-DF",Species.DF_PP_PF);
    transformHm.put("DF-PF-PP",Species.DF_PP_PF);
    transformHm.put("PF-DF-PP",Species.DF_PP_PF);
    transformHm.put("PP-DF-PF",Species.DF_PP_PF);

    transformHm.put("DF-ES-LP",Species.DF_LP_ES);
    transformHm.put("ES-DF-LP",Species.DF_LP_ES);
    transformHm.put("LP-ES-DF",Species.DF_LP_ES);
    transformHm.put("ES-LP-DF",Species.DF_LP_ES);
    transformHm.put("LP-DF-ES",Species.DF_LP_ES);

    transformHm.put("DF-AF-LP",Species.DF_LP_AF);
    transformHm.put("AF-DF-LP",Species.DF_LP_AF);
    transformHm.put("LP-AF-DF",Species.DF_LP_AF);
    transformHm.put("AF-LP-DF",Species.DF_LP_AF);
    transformHm.put("LP-DF-AF",Species.DF_LP_AF);

    transformHm.put("WB-LP-ES",Species.WB_ES_LP);
    transformHm.put("LP-WB-ES",Species.WB_ES_LP);
    transformHm.put("ES-LP-WB",Species.WB_ES_LP);
    transformHm.put("LP-ES-WB",Species.WB_ES_LP);
    transformHm.put("ES-WB-LP",Species.WB_ES_LP);

    transformHm.put("WB-AF-ES",Species.WB_ES_AF);
    transformHm.put("AF-WB-ES",Species.WB_ES_AF);
    transformHm.put("ES-AF-WB",Species.WB_ES_AF);
    transformHm.put("AF-ES-WB",Species.WB_ES_AF);
    transformHm.put("ES-WB-AF",Species.WB_ES_AF);

    transformHm.put("AF-LP-ES",Species.AF_ES_LP);
    transformHm.put("LP-AF-ES",Species.AF_ES_LP);
    transformHm.put("ES-LP-AF",Species.AF_ES_LP);
    transformHm.put("LP-ES-AF",Species.AF_ES_LP);
    transformHm.put("ES-AF-LP",Species.AF_ES_LP);
  }
/**
 * Clears the transform hash map.
 */
  public static void clearFixData() {
    transformHm = null;
  }

  /**
   * Attempt to to re-order a species made of multiple species
   * in order to find a valid species.
   * Will return null if no match found.
   */
  public static Species fixOrder(Species species) {
    if (transformHm == null) { initializeTransformHt(); }

    return (Species) transformHm.get(species.toString());
  }
/**
 * Gets the lifeform object.  
 * @return lifeform
 */
  public Lifeform getLifeform() { return lifeform; }
/**
 * Sets the species description via input string.  
 * @param value the description of species
 */
  private void setDescription(String value) { description = value; }
  /**
   * Sets lifeform for the species from parameter lifeform
   * @param value the lifeform to be set
   */
  private void setLifeform(Lifeform value) { lifeform = value; }
  /**
   * Sets the fire resistance for this species.  
   * @param value the fire resistance value 
   */
  private void setFireResistance(FireResistance value) { fireResistance = value; }
/**
 * 
 * @param species
 */
  public void setSpecies(String species) {
    this.species = species;
  }
/**
 * 
 * @param invasive whether a species is invasive.  true if invasive
 */
  public void setInvasive(boolean invasive) {

    this.invasive = invasive;
  }

  public static void setNoChangeRead(boolean noChangeRead) {
    noChangeRead = noChangeRead;
  }

  public FireResistance getFireResistance() {
    return fireResistance;
  }

  public String getSpecies() {
    return species;
  }
/**
 * 
 * @return true if invasive species, false otherwise
 */
  public boolean isInvasive() {

    return invasive;
  }

  public static boolean isNoChangeRead() {
    return noChangeRead;
  }

  public HashMap getResistanceConditional() { return conditionalGroups; }

  public ArrayList getResistanceGroups(FireResistance resistance) {
    if (conditionalGroups != null) {
      return (ArrayList)conditionalGroups.get(resistance);
    }
    return null;
  }

  public Species getDominantSpecies() {
    int    index = species.indexOf('-');
    if (index == -1) { return this; }

    Species tmpSpecies = Species.get(species.substring(0,index));
    return ((tmpSpecies != null) ? tmpSpecies : this);
  }
  /**
   * Finds the secondary species.  Uses the indexOf and character '-' then substring from 0 to index.  
   * @return if secondary species is different from current species, will return the new dominant species, else will return the current species 
   * Note -the '-' is not always a species separator 
   */
  public Species getSecondSpecies() {
    int begin = species.indexOf('-');
    if (begin == -1) { return this; }

    int index = species.indexOf('-',begin+1);
    if (index == -1) { return this; }

    Species tmpSpecies = Species.get(species.substring(begin+1,index));
    return ((tmpSpecies != null) ? tmpSpecies : this);
  }

  public boolean contains (String speciesStr) {
    Species tmp = Species.get(speciesStr);
    return (tmp != null) ? contains(tmp) : false;
  }
  public boolean contains(Species species) {
    if (equals(species)) { return true; }

    String str = "-" + species.toString();
    int index = this.species.indexOf(str);
    if (index != -1) { return true; }

    str = species.toString() + "-";
    index = this.species.indexOf(str);
    if (index != -1) { return true; }

    return false;
  }
/**
 * Instructions on how to handle reading fireResistance, userCreated, conditionalGrous, and invasive input in different versions. 
 * @throws IOException, ClassNotFoundException  
 */
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    int version = in.readInt();

    species           = (String)in.readObject();
    description       = (String)in.readObject();
    lifeform          = (Lifeform)in.readObject();
    fireResistance    = FireResistance.UNKNOWN;
    conditionalGroups = null;
    invasive        = false;

    if (version > 1) {
      fireResistance = (FireResistance)in.readObject();
    }
    if (version > 2) {
      boolean userCreated = in.readBoolean();
    }
    if (version > 3) {
      conditionalGroups = (HashMap)in.readObject();
    }
    if (version > 4) {
      invasive = in.readBoolean();
    }
  }
  
  /**
   * Method with output instructions for Species class
   * @throws IOException
   */
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(version);

    out.writeObject(species);
    out.writeObject(description);
    out.writeObject(lifeform);
    out.writeObject(fireResistance);
    out.writeBoolean(true /*userCreated*/);
    out.writeObject(conditionalGroups);
    out.writeBoolean(invasive);
  }
  /**
   * Method to create Species objects.  
   * @return
   * @throws java.io.ObjectStreamException
   */
  private Object readResolve () throws java.io.ObjectStreamException
  {
    Species speciesObj = Species.get(species,true);

    if (!simpplle.comcode.logic.AbstractBaseLogic.isNoChangeRead()) {
      speciesObj.description = this.description;
      speciesObj.lifeform = this.lifeform;
      speciesObj.fireResistance = this.fireResistance;
      speciesObj.conditionalGroups = this.conditionalGroups;
      speciesObj.invasive = this.invasive;
    }

    updateAllData(speciesObj,SPECIES);
    return speciesObj;
  }
/**
 * Sort function.  
 */
  public static void sort() {
    HashMap lifeformHm = new HashMap();

    Species          species;
    ArrayList        list;
    Lifeform         lifeform;
    for (int i=0; i<allSpeciesList.size(); i++) {
      species = (Species)allSpeciesList.get(i);
      lifeform  = species.getLifeform();
      list = (ArrayList)lifeformHm.get(lifeform.toString());
      if (list == null) {
        list = new ArrayList();
        lifeformHm.put(lifeform.toString(),list);
      }
      list.add(species);
    }

    allSpeciesList.clear();

    Lifeform[] allLives = Lifeform.getAllValues();

    for (int i=0; i<allLives.length; i++) {
      list = (ArrayList)lifeformHm.get(allLives[i].toString());
      if (list == null) { continue; }

      Collections.sort(list);
      for (int j=0; j<list.size(); j++) {
        allSpeciesList.add((Species)list.get(j));
      }
      list.clear();
      list = null;
    }
  }
/**
 * Method to retrieve data from database column.  
 */
  public Object getColumnData(int col) {
    switch (col) {
//      case USER_CREATED_COL:
//        return (isUserCreated() ? Boolean.TRUE : Boolean.FALSE);
      case CODE_COL:
        return this;
      case DESCRIPTION_COL:
        return getDescription();
      case LIFEFORM_COL:
        return getLifeform();
      case FIRE_RESISTANCE_COL:
        return getFireResistance();
      case RESISTANCE_COND_COL:
        return getResistanceConditional();
      case INVASIVE_COL:
        return this.isInvasive();
      case PATHWAY_PRESENT_COL:
        return (HabitatTypeGroup.hasSpecies(this) ? "Yes" : "No");
      default: return null;
    }
  }

  public void setColumnData(Object value, int col) {
    switch (col) {
      case DESCRIPTION_COL:
        description = (String)value;
        break;
      case LIFEFORM_COL:
        lifeform = (Lifeform)value;
        break;
      case FIRE_RESISTANCE_COL:
        fireResistance = (FireResistance)value;
        if (fireResistance != FireResistance.CONDITIONAL) {
          conditionalGroups = null;
        }
        else { conditionalGroups = new HashMap(3); }
        break;
      case RESISTANCE_COND_COL:
        conditionalGroups = (HashMap)value;
        break;
      case INVASIVE_COL:
        if (invasive != (Boolean)value) {
          invasive = (Boolean) value;
          if (isInvasive()) {
            if (invasiveList.contains(this) == false) { invasiveList.add(this); }
          }
          else {
            if (invasiveList.contains(this)) { invasiveList.remove(this); }
          }
        }
        break;
      default: return;
    }
    SystemKnowledge.markChanged(SystemKnowledge.SPECIES);
  }
/**
 * Method to take the name of column and format it into string for presentation in GUI
 * @param col int used in switch - indexed starting at 0
 * @return name of column formatted as string.   
 */
  public static String getColumnName(int col) {
    switch (col) {
//      case USER_CREATED_COL:    return "User";
      case CODE_COL:            return "Species";
      case DESCRIPTION_COL:     return "Description";
      case LIFEFORM_COL:        return "Lifeform Type";
      case FIRE_RESISTANCE_COL: return "Fire Resistance";
      case RESISTANCE_COND_COL: return "Conditional Fire Resistance";
      case INVASIVE_COL:        return "Invasive";
      case PATHWAY_PRESENT_COL: return "Pathway Present";
      default: return "";
    }
  }

  /**
   * Import species definitions from a text file.
   * Each line should be comma delimited.
   * The fields are species name, description, lifeform, fire-resistance.
   *
   * @param filename File
   * @throws SimpplleError
   */
  public static void Import(File filename) throws SimpplleError {
    simpplle.comcode.utility.StringTokenizerPlus strTok;
    String              line, str;
    Species             species;
    Lifeform            lf;
    FireResistance      fr;
    BufferedReader      fin;

    try {
      fin = new BufferedReader(new FileReader(filename));

      line = fin.readLine();
      if (line == null) { throw new SimpplleError("Invalid Species Text File."); }

      while (line != null) {

        strTok = new simpplle.comcode.utility.StringTokenizerPlus(line, ",");
        if (strTok.countTokens() < 3) {
          throw new SimpplleError(
              "Invalid line in file (should be species,despcription,lifeform,fire resistance(optional)): \n" +
              line);
        }

        // Species name
        str = strTok.getToken();
        if (str == null || str.trim().length() == 0) {
          throw new SimpplleError("No species in line: " + line);
        }
        species = Species.get(str.toUpperCase());
        if (species == null) {
          species = new Species(str.toUpperCase(), true);
        }

        // Species Description
        str = strTok.getToken();
        if (str == null || str.trim().length() == 0) {
          str = "";
        }
        species.setDescription(str.trim());

        // Species Lifeform type
        str = strTok.getToken();
        if (str == null || str.trim().length() == 0) {
          throw new SimpplleError("No Lifeform type in line: " + line);
        }
        lf = Lifeform.get(str.trim());
        if (lf == null) { lf = Lifeform.NA; }
        species.setLifeform(lf);

        // Species Fire Resistance
        str = (strTok.hasMoreTokens()) ? strTok.getToken() : "LOW";
        if (str == null || str.trim().length() == 0) {
          str = "LOW";
        }
        fr = FireResistance.get(str.trim());
        if (fr == null) { fr = FireResistance.LOW; }
        species.setFireResistance(fr);

        line = fin.readLine();
        if (line != null && line.length() == 0) { line = null; }
      }
      fin.close();
    }
    catch (IOException ex) {
      throw new SimpplleError("Problems reading Species file.");
    }
    catch (ParseError ex) {
      throw new SimpplleError(ex.msg);
    }

  }

}


