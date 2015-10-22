package simpplle.comcode;

import java.io.*;
import java.util.*;
import java.util.zip.GZIPInputStream;
import java.util.jar.JarOutputStream;
import java.util.jar.JarInputStream;

/**
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class contains methods for a Regeneration Logic. Regeneration is highly variable.  
 * It is dependent on spatial arrangement of plant communities, regeneration mechanisms of species, and spatial arrangement of 
 * adjacent plant communities.  
 * <p>Regeneration logic is applied under either succession or fire regeneration logic.  This class covers both.  
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 * 
 */
public abstract class RegenerationLogic {
  private static final int version = 2;

  private static HashMap<HabitatTypeGroupType,BaseLogic> fireData =
      new HashMap<HabitatTypeGroupType,BaseLogic>();
  private static HashMap<HabitatTypeGroupType,BaseLogic> succData =
      new HashMap<HabitatTypeGroupType,BaseLogic>();



  public enum DataKinds { FIRE, SUCCESSION };
  public static final DataKinds FIRE       = DataKinds.FIRE;
  public static final String FIRE_STR = FIRE.toString();

  public static final DataKinds SUCCESSION = DataKinds.SUCCESSION;
  public static final String SUCCESSION_STR = SUCCESSION.toString();

  private static ArrayList<Species> adjPrefSpecies; // Used in Fire Regen Only.

  private static HabitatTypeGroupType currentEcoGroup;
/**
 * Gets the hashmap for a particular type of regeneration logic.  CHoices are Succession or Fire.  
 * @param kind
 * @return
 */
  private static HashMap<HabitatTypeGroupType,BaseLogic> getDataHm(DataKinds kind) {
    switch (kind) {
      case FIRE:       return fireData;
      case SUCCESSION: return succData;
    }
    return null;
  }
/**
 * Sets the default habitat type group. If fire regeneration applies to any habitat type group type in the fire data, uses ANY as default habitata type group type (eco group)
 * Otherwise sets it to the key (habitat type group type) in the fire data hashmap.  
 * @param kind
 */
  public static void setDefaultEcoGroup(DataKinds kind) {
    switch (kind) {
      case FIRE:
        if (fireData.containsKey(HabitatTypeGroupType.ANY)) {
          currentEcoGroup = HabitatTypeGroupType.ANY;
        }
        else {
          for (HabitatTypeGroupType group : fireData.keySet()) {
            currentEcoGroup = group;
            break;
          }
        }
    }
  }
  /**
   * Gets the fire Logic Instance
   * @param kind will be either fire or succession.
   * @return
   */
  public static BaseLogic getLogicInstance(String kind) {
    return getData(DataKinds.valueOf(kind));
  }
  public static BaseLogic getData(DataKinds kind, HabitatTypeGroupType ecoGroup) {
    return getData(kind,ecoGroup,false);
  }
  /**
   * Gets the fire or succession regeneration data, if some exists.  If not will add the succession or fire regeneration info to the fireData or succData hashmaps.  
   * @param kind either fire or succession regeneration
   * @param ecoGroup 
   * @param addIfNull - if the data hashmap for either succession or fire is null will add the data to it.  
   * @return
   */
  public static BaseLogic getData(DataKinds kind, HabitatTypeGroupType ecoGroup, boolean addIfNull) {
    BaseLogic result;
    switch (kind) {
      case FIRE:
        result = fireData.get(ecoGroup);
        if (result == null && addIfNull) {
          fireData.put(ecoGroup,new RegenBaseLogic(kind));
        }
        return fireData.get(ecoGroup);
      case SUCCESSION:
        result = succData.get(ecoGroup);
        if (result == null && addIfNull) {
          succData.put(ecoGroup,new RegenBaseLogic(kind));
        }
        return succData.get(ecoGroup);
      default:
        return null;
    }
  }
  public static BaseLogic getData(DataKinds kind) {
    return getData(kind,false);
  }
  public static BaseLogic getData(DataKinds kind, boolean addIfNull) {
    return getData(kind,currentEcoGroup,addIfNull);
  }
  public static int getRowCount(DataKinds kind) {
    return getData(kind).getRowCount(kind.toString());
  }

  public static int getColumnCount(DataKinds kind) {
    return getData(kind).getColumnCount(kind.toString());
  }
/**
 * Checks if there is either fire or succession regeneration data.
 * @return True if either fire or succession data hashmaps are not null.  
 */
  public static boolean isDataPresent() {
    return isDataPresent(FIRE) && isDataPresent(SUCCESSION);
  }
  /**
   * Checks if a particular kind of regeneration data is present in the fire or succession regen data hashmaps.
   * @param kind either fire or succession regeneration data
   * @return true if there is data present
   */
  public static boolean isDataPresent(DataKinds kind) {
    return (getData(kind) != null && getData(kind).getRowCount(kind.toString()) > 0);
  }
/**
 * Clears the regeneration hashmap for either fire or succession regeneration.
 * @param kind either fire or succession.  
 */
  public static void clearData(DataKinds kind) {
    switch (kind) {
      case FIRE: fireData.clear(); break;
      case SUCCESSION: succData.clear(); break;
    }
  }

  public static Object getValueAt(int row, int col, DataKinds kind) {
    return getData(kind).getValueAt(row,col,kind.toString());
  }

  public static void setSpecies(int row, Species species, DataKinds kind) {
    RegenerationData regenData =
      (RegenerationData)getData(kind).getValueAt(row,kind.toString());
    regenData.setSpecies(species);
  }
  public static void addDataRows(int insertPos, String kind, Vector speciesList) {
    for (int i=0; i<speciesList.size(); i++) {
      addDataRow((Species)speciesList.get(i),insertPos,DataKinds.valueOf(kind));
    }
  }
  public static void addDataRow(int insertPos, DataKinds kind) {
    addDataRow(null,insertPos,kind);
  }
  private static void addDataRow(Species species, int insertPos, DataKinds kind) {
    RegenerationData regenData;
    switch(kind) {
      case FIRE:       regenData = new FireRegenerationData(currentEcoGroup);     break;
      case SUCCESSION: regenData = new SuccessionRegenerationData(false,currentEcoGroup); break;
      default: return;
    }
    if (species != null) {
      regenData.species = species;
    }
    getData(kind,true).addRow(insertPos,kind.toString(),regenData);
    markChanged(kind);
  }
  public static void deleteDataRow(int row, DataKinds kind) {
    getData(kind).removeRow(row,kind.toString());
    markChanged(kind);
  }
  public static boolean isSpeciesPresent(Species species, DataKinds kind) {
    BaseLogic logic = getData(kind);
    ArrayList<AbstractLogicData> dataList = logic.getData(kind.toString());
    if (dataList != null) {
      for (AbstractLogicData data : dataList) {
        RegenerationData regenData = (RegenerationData)data;
        if (regenData.getSpecies() == species) { return true; }
      }
    }
    return false;
  }

  private static RegenerationData findRegenData(HabitatTypeGroupType ecoGroup,
                                                Evu evu, Lifeform lifeform,
                                                DataKinds kind) {
    int cStep = Simulation.getCurrentTimeStep();
    return findRegenData(ecoGroup,evu,cStep,lifeform,kind);
  }
  /**
   * This is one of the more important methods in regeneration logic.  It checks to see if there is regeneration data is present for a particular ecogropu
   * evu, time step, lifeform and kind
   * @param ecoGroup
   * @param evu
   * @param tStep time step.
   * @param lifeform
   * @param kind fire or succession
   * @return
   */
  private static RegenerationData findRegenData(HabitatTypeGroupType ecoGroup,
                                                Evu evu, int tStep, Lifeform lifeform,
                                                DataKinds kind) {
    BaseLogic logic = getData(kind,ecoGroup);
    if (logic != null) {
      ArrayList<AbstractLogicData> dataList = logic.getData(kind.toString());
      if (dataList != null) {
        for (AbstractLogicData data : dataList) {
          RegenerationData regenData = (RegenerationData) data;
          if (regenData.isMatch(evu, tStep, lifeform)) {
            return regenData;
          }
        }
      }
    }
    if (ecoGroup != HabitatTypeGroupType.ANY) {
      return findRegenData(HabitatTypeGroupType.ANY,evu,tStep,lifeform,kind);
    }
    return null;
  }
  private static RegenerationData findRegenDataSuccInLandscapeSeed(
      HabitatTypeGroupType ecoGroup, Evu evu,
      VegSimStateData state, int tStep, Lifeform lifeform, DataKinds kind)
  {
    BaseLogic logic = getData(kind,ecoGroup);
    if (logic != null) {
      ArrayList<AbstractLogicData> dataList = logic.getData(kind.toString());
      if (dataList != null) {
        for (AbstractLogicData data : dataList) {
          RegenerationData regenData = (RegenerationData) data;
          if (regenData.isMatch(evu, state, tStep, lifeform)) {
            return regenData;
          }
        }
      }
    }
    if (ecoGroup != HabitatTypeGroupType.ANY) {
      return findRegenDataSuccInLandscapeSeed(HabitatTypeGroupType.ANY,evu,state,tStep,lifeform,kind);
    }
    return null;
  }
  /**
   * Checks if a species is a regeneration succession species.  
   * @param ecoGroup
   * @param evu
   * @param lifeform
   * @return
   */
  public static boolean isSuccessionSpecies(HabitatTypeGroupType ecoGroup,
                                            Evu evu,Lifeform lifeform) {
    SuccessionRegenerationData regenData =
        (SuccessionRegenerationData)findRegenData(ecoGroup,evu,lifeform,SUCCESSION);
    if (regenData == null) { return false; }

    return (
        regenData.succession.booleanValue() ||
        (regenData.successionSpecies != null && regenData.successionSpecies.size() > 0));
  }

  public static boolean useSuccessionDominantSeed(HabitatTypeGroupType ecoGroup,
                                                  Evu evu,Lifeform lifeform) {
    SuccessionRegenerationData regenData =
      (SuccessionRegenerationData)findRegenData(ecoGroup,evu,lifeform,SUCCESSION);
    if (regenData == null) { return false; }

    return ((regenData != null) ? regenData.succession.booleanValue() : false);
  }

  public static VegetativeType getResproutingState(HabitatTypeGroupType ecoGroup,
                                                   Evu evu,Lifeform lifeform) {
    FireRegenerationData regenData =
      (FireRegenerationData)findRegenData(ecoGroup,evu,lifeform,FIRE);
    if (regenData == null) { return null; }

    // resprouting is only allowed one state, but is in a vector
    // to simplify the table logic.
    if (regenData.resprouting == null || regenData.resprouting.size() == 0) {
      return null;
    }
    return (VegetativeType)regenData.resprouting.get(0);
  }
  public static VegetativeType getAdjResproutingState(HabitatTypeGroupType ecoGroup,
                                                      Evu evu,
                                                      int tStep, Lifeform lifeform) {

    FireRegenerationData regenData =
       (FireRegenerationData)findRegenData(ecoGroup,evu,tStep,lifeform,FIRE);
    if (regenData == null) { return null; }

    // Adjacent resprouting is only allowed one state, but is in a vector
    // to simplify the table logic.
    if (regenData.adjResprouting != null && (regenData.adjResprouting.size() != 0)) {
      return (VegetativeType)regenData.adjResprouting.get(0);
    }
    else { return null; }
  }
  public static VegetativeType getInPlaceSeedState(HabitatTypeGroupType ecoGroup,
                                                   Evu evu,Lifeform lifeform) {
    FireRegenerationData regenData =
        (FireRegenerationData)findRegenData(ecoGroup,evu,lifeform,FIRE);
    if (regenData == null) { return null; }

    // In Place Seed is only allowed one state, but is in a vector
    // to simplify the table logic.
    if (regenData.inPlaceSeed == null || regenData.inPlaceSeed.size() == 0) {
      return null;
    }
    return (VegetativeType)regenData.inPlaceSeed.get(0);
  }

  /**
   * Special version to allow the saved last Lifeform state to be passed
   * along into the chain of functions here.  This makes certain that we don't
   * end up trying to access a previous state in the unit that is no longer
   * in memory.
   * Should also note that it is only succession regen that that does this
   * looking way back in time to find out if there was seed in the past, say
   * if trees were wiped out by a stand-replacing fire.
   */
  public static VegetativeType getInLandscapeSeedState(HabitatTypeGroupType ecoGroup,
                                                       Evu evu,
                                                       VegSimStateData state,
                                                       int tStep,
                                                       Lifeform lifeform) {

    FireRegenerationData regenData =
        (FireRegenerationData)findRegenDataSuccInLandscapeSeed(ecoGroup,evu,state,tStep,lifeform,FIRE);
    if (regenData == null) { return null; }

    // In Landscape Seed is only allowed one state, but is in a vector
    // to simplify the table logic.
    if (regenData.inLandscape == null || regenData.inLandscape.size() == 0) {
      return null;
    }
    return (VegetativeType)regenData.inLandscape.get(0);
  }


  public static VegetativeType getInLandscapeSeedState(HabitatTypeGroupType ecoGroup,
                                                       Evu evu, int tStep, Lifeform lifeform) {

    FireRegenerationData regenData =
        (FireRegenerationData)findRegenData(ecoGroup,evu,tStep,lifeform,FIRE);
    if (regenData == null) { return null; }

    // In Landscape Seed is only allowed one state, but is in a vector
    // to simplify the table logic.
    if (regenData.inLandscape == null || regenData.inLandscape.size() == 0) {
      return null;
    }
    return (VegetativeType)regenData.inLandscape.get(0);
  }
  public static ArrayList<VegetativeType> getAdjacentStates(HabitatTypeGroupType ecoGroup,Evu evu,
                                                            int tStep, Lifeform lifeform) {
    FireRegenerationData regenData =
       (FireRegenerationData)findRegenData(ecoGroup,evu,tStep,lifeform,FIRE);

    if (regenData == null) { return null; }
    return regenData.adjacent;
  }

  public static ArrayList<Species> getAdjacentPreferredSpecies() { return adjPrefSpecies; }

  public static ArrayList<RegenerationSuccessionInfo>
                getSuccessionSpecies(HabitatTypeGroupType ecoGroup,
                                     Evu evu,Lifeform lifeform)
  {
    SuccessionRegenerationData regenData =
      (SuccessionRegenerationData)findRegenData(ecoGroup,evu,lifeform,SUCCESSION);
    if (regenData == null) { return null; }
    return regenData.successionSpecies;
  }

//  public static void setData(Object value, int row, int col, DataKinds kind) {
//    switch (kind) {
//      case FIRE:       setFireData(value,row,col); break;
//      case SUCCESSION: setSuccData(value,row,col); break;
//      default:
//    }
//  }
//  private static void setFireData(Object value, int row, int col) {
//    switch (col) {
//      case FireRegenerationData.RESPROUTING_COL:
//      case FireRegenerationData.ADJ_RESPROUTING_COL:
//      case FireRegenerationData.IN_LANDSCAPE_COL:
//      case FireRegenerationData.IN_PLACE_SEED_COL:
//      case FireRegenerationData.ADJACENT_COL:
//        // Set in the table editor
//        break;
//    }
//    markChanged(FIRE);
//  }
//  private static void setSuccData(Object value, int row, int col) {
//    switch (col) {
//      case SuccessionRegenerationData.SUCCESSION_SPECIES_COL:
//        // Set in the table editor
//        break;
//      case SuccessionRegenerationData.SUCCESSION_COL:
//        BaseLogic logic = getData(SUCCESSION);
//        logic.getValueAt(row,SUCCESSION.toString());
//        SuccessionRegenerationData regenData =
//          (SuccessionRegenerationData)logic.getValueAt(row,SUCCESSION.toString());
//        regenData.succession = (Boolean)value;
//        break;
//    }
//    markChanged(SUCCESSION);
//  }

  public static void readDataLegacy(File infile) throws SimpplleError {
    GZIPInputStream gzip_in;
    BufferedReader fin;

    try {
      gzip_in = new GZIPInputStream(new FileInputStream(infile));
      fin = new BufferedReader(new InputStreamReader(gzip_in));
      readDataLegacy(fin);
      fin.close();
      gzip_in.close();
//
//      setFile(infile);
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

  public static void readDataLegacy(BufferedReader fin)
    throws ParseError, IOException
  {
    String              line, str;
    StringTokenizerPlus strTok;
    FireRegenerationData           regenData;
    SuccessionRegenerationData succRegenData;

    line = fin.readLine();
    if (line == null) {
      throw new ParseError("No data in Regeneration Logic File");
    }

    int rowCount = 0;
    HabitatTypeGroupType ecoGroup;
    ArrayList<RegenerationData> dataList;
    ArrayList<RegenerationData> succDataList;

    try {
      rowCount       = Integer.parseInt(line);
      fireData.clear();
      succData.clear();
      adjPrefSpecies = new ArrayList<Species>();
    }
    catch (NumberFormatException err) {
      throw new ParseError("Invalid row count in Regeneration Logic File.");
    }

    line = fin.readLine();
    strTok = new StringTokenizerPlus(line,",");
    int count = strTok.countTokens();
    for (int i=0; i<count; i++) {
      str = strTok.getToken();
      if (str == null) { break; }
      adjPrefSpecies.add(Species.get(str,true));
    }

    line = fin.readLine();
    for (int row=0; row < rowCount; row++) {
      strTok = new StringTokenizerPlus(line,",");
      regenData = new FireRegenerationData();
      Species species = Species.get(strTok.getToken(),true);
      regenData.setSpecies(species);
      regenData.setResprouting(strTok.getListValue());
      regenData.setAdjResprouting(strTok.getListValue());
//      regenData.setAdjResprouting(null);
      regenData.setInPlaceSeed(strTok.getListValue());
      regenData.setInLandscape(strTok.getListValue());
      regenData.setAdjacent(strTok.getListValue());

      succRegenData = new SuccessionRegenerationData();
      succRegenData.setSpecies(species);
      succRegenData.setSuccession(Boolean.valueOf(strTok.getToken()));
      succRegenData.setSuccessionSpecies(strTok.getListValue());

      if (strTok.hasMoreTokens()) {
        ecoGroup = HabitatTypeGroupType.get(strTok.getToken());
        regenData.setEcoGroup(ecoGroup);
        succRegenData.setEcoGroup(ecoGroup);
      }
      else { ecoGroup = HabitatTypeGroupType.ANY; }

      {
        BaseLogic logic = getData(FIRE, ecoGroup,true);
        logic.addRow(FIRE.toString(), regenData);
      }
      {
        BaseLogic logic = getData(SUCCESSION, ecoGroup,true);
        logic.addRow(FIRE.toString(), succRegenData);
      }

      line = fin.readLine();
      if (line == null || line.trim().length() == 0) { break; }
    }
    setDefaultEcoGroup(SUCCESSION);
    setDefaultEcoGroup(FIRE);
    sortData();
  }

  private static void sortData() {
    for (HabitatTypeGroupType ecoGroup : fireData.keySet()) {
      sortData(getData(FIRE,ecoGroup).getData(FIRE.toString()));
    }
    for (HabitatTypeGroupType ecoGroup : succData.keySet()) {
      sortData(getData(SUCCESSION,ecoGroup).getData(SUCCESSION.toString()));
    }
  }
  private static void sortData(ArrayList<AbstractLogicData> dataList) {
    if (dataList == null) { return; }

    HashMap lifeformHm = new HashMap();

    RegenerationData regenData;
    ArrayList        list;
    Lifeform         lifeform;
    for (int i=0; i<dataList.size(); i++) {
      regenData = (RegenerationData)dataList.get(i);
      lifeform = regenData.species.getLifeform();
      list = (ArrayList)lifeformHm.get(lifeform.toString());
      if (list == null) {
        list = new ArrayList();
        lifeformHm.put(lifeform.toString(),list);
      }
      list.add(regenData);
    }

    dataList.clear();

    Lifeform[] allLives = Lifeform.getAllValues();

    for (int i=0; i<allLives.length; i++) {
      list = (ArrayList)lifeformHm.get(allLives[i].toString());
      if (list == null) { continue; }

      Collections.sort(list);
      for (int j=0; j<list.size(); j++) {
        dataList.add((RegenerationData)list.get(j));
      }
      list.clear();
      list = null;
    }
  }
  public static void makeBlankLogic(DataKinds kind) {
    ArrayList<SimpplleType> list = Species.getList(SimpplleType.SPECIES);
    if (list == null || list.size() == 0) { return; }

    BaseLogic logic = getData(kind);
    if (logic != null) {
      logic.clearData(kind.toString());
    }
    switch(kind) {
      case FIRE:
        fireData.clear();
        adjPrefSpecies = new ArrayList<Species>();
        break;
      case SUCCESSION: succData.clear(); break;
    }

    logic = getData(kind,HabitatTypeGroupType.ANY,true);

    for (int i=0; i<list.size(); i++) {
      if (kind == FIRE) {
        logic.addRow(kind.toString(),new FireRegenerationData((Species) list.get(i)));
      }
      else if (kind == SUCCESSION) {
        logic.addRow(kind.toString(),new SuccessionRegenerationData((Species) list.get(i), false));
      }
    }
    setDefaultEcoGroup(kind);
  }

  public static boolean hasChanged(DataKinds kind) {
    if (kind == FIRE) {
      return SystemKnowledge.hasChangedOrUserData(SystemKnowledge.REGEN_LOGIC_FIRE);
    }
    else {
      return SystemKnowledge.hasChangedOrUserData(SystemKnowledge.REGEN_LOGIC_SUCC);
    }
  }
  public static void markChanged(DataKinds kind) {
    if (kind == FIRE) {
      SystemKnowledge.markChanged(SystemKnowledge.REGEN_LOGIC_FIRE);
    }
    else {
      SystemKnowledge.markChanged(SystemKnowledge.REGEN_LOGIC_SUCC);
    }
  }
/**
 * Sets the current eco group for regeneration logic.  
 * @param kind will be either REGEN_LOGIC_FIRE or REGEN_LOGIC_SUCC (fire or succession regeneration) 
 * @param ecoGroup
 */
  public static void setCurrentEcoGroup(DataKinds kind,HabitatTypeGroupType ecoGroup) {
    currentEcoGroup = ecoGroup;
    if (kind != null) {
      getData(kind, currentEcoGroup, true);
    }
  }

  private static void setFile(File newFile, DataKinds kind) {
    if (kind == FIRE) {
      SystemKnowledge.setFile(SystemKnowledge.REGEN_LOGIC_FIRE, newFile);
      SystemKnowledge.markChanged(SystemKnowledge.REGEN_LOGIC_FIRE);
    }
    else {
      SystemKnowledge.setFile(SystemKnowledge.REGEN_LOGIC_SUCC, newFile);
      SystemKnowledge.markChanged(SystemKnowledge.REGEN_LOGIC_SUCC);
    }
  }

  public static void clearFile(DataKinds kind) {
    if (kind == FIRE) {
      SystemKnowledge.clearFile(SystemKnowledge.REGEN_LOGIC_FIRE);
    }
    else {
      SystemKnowledge.clearFile(SystemKnowledge.REGEN_LOGIC_SUCC);
    }
  }
  public static void closeFile(DataKinds kind) {
    if (kind == FIRE) {
      clearFile(kind);
      SystemKnowledge.setHasChanged(SystemKnowledge.REGEN_LOGIC_FIRE, false);
    }
    else {
      clearFile(kind);
      SystemKnowledge.setHasChanged(SystemKnowledge.REGEN_LOGIC_SUCC, false);
    }
  }

  public static void saveFire(ObjectOutputStream stream) throws SimpplleError {
    save(stream,FIRE);
  }
  public static void saveSuccession(ObjectOutputStream stream) throws SimpplleError {
    save(stream,SUCCESSION);
  }

  public static void save(ObjectOutputStream s, DataKinds kind) throws SimpplleError {
    try {
      s.writeInt(version);
      if (kind == FIRE) {
        s.writeInt((adjPrefSpecies != null) ? adjPrefSpecies.size() : 0);
        for (Species sp : adjPrefSpecies) {
          sp.writeExternalSimple(s);
        }
      }
      s.writeInt( (getData(kind) != null) ? getDataHm(kind).size() : 0);
      for (HabitatTypeGroupType ecoGroup : getDataHm(kind).keySet()) {
        ecoGroup.writeExternalSimple(s);
        BaseLogic logic = getData(kind,ecoGroup);
        logic.save(s);
      }

    }
    catch (IOException ex) {
    }
  }

  public static void readFire(ObjectInputStream stream)
    throws IOException, ClassNotFoundException
 {
    read(stream,FIRE);
    setDefaultEcoGroup(FIRE);
  }
  public static void readSuccession(ObjectInputStream stream)
    throws IOException, ClassNotFoundException
  {
    read(stream,SUCCESSION);
    setDefaultEcoGroup(SUCCESSION);
  }

  public static void read(ObjectInputStream in, DataKinds kind)
    throws IOException, ClassNotFoundException
  {
    int version = in.readInt();

    if (kind == FIRE) {
      int nSpecies = in.readInt();
      adjPrefSpecies = new ArrayList<Species>(nSpecies);
      for (int n=0; n<nSpecies; n++) {
        adjPrefSpecies.add( (Species)SimpplleType.readExternalSimple(in,SimpplleType.SPECIES) );
      }
    }
    if (version == 1) {
      readVersion1(in,kind);
      return;
    }

    int size = in.readInt();
    getDataHm(kind).clear();
    for (int i=0; i<size; i++) {
      HabitatTypeGroupType ecoGroup =
          (HabitatTypeGroupType)SimpplleType.readExternalSimple(in,SimpplleType.HTGRP);
      BaseLogic logic = getData(kind,ecoGroup,true);
      logic.read(in);
    }
  }
  public static void readVersion1(ObjectInputStream in, DataKinds kind)
    throws IOException, ClassNotFoundException
  {
    int size = in.readInt();
    getDataHm(kind).clear();
    for (int i=0; i<size; i++) {
      HabitatTypeGroupType ecoGroup =
          (HabitatTypeGroupType)SimpplleType.readExternalSimple(in,SimpplleType.HTGRP);
      BaseLogic logic = getData(kind,ecoGroup,true);
      int listSize = in.readInt();
      for (int l=0; l<listSize; l++) {
        if (kind == FIRE) {
          logic.addRow(kind.toString(),(FireRegenerationData)in.readObject());
        }
        else if (kind == SUCCESSION) {
          logic.addRow(kind.toString(),(SuccessionRegenerationData)in.readObject());
        }
      }
    }
  }

  public static HabitatTypeGroupType getCurrentEcoGroup() {
    return currentEcoGroup;
  }

  public static int getColumnNumFromName(DataKinds kind,String name) {
    BaseLogic logic = getData(kind);
    switch (kind) {
      case FIRE:       return FireRegenerationData.getColumnNumFromName(logic,name);
      case SUCCESSION: return SuccessionRegenerationData.getColumnNumFromName(logic,name);
      default: return -1;
    }

  }

  public static String getColumnName(DataKinds kind,int visibleCol) {
    BaseLogic logic = getData(kind);

    String colName = logic.getVisibleColumnsHm().get(kind.toString()).get(visibleCol);
    int col = logic.getColumnPosition(kind.toString(),colName);

    String name;
    switch (kind) {
      case FIRE:       name = FireRegenerationData.getColumnName(col);       break;
      case SUCCESSION: name = SuccessionRegenerationData.getColumnName(col); break;
      default:         name = logic.getColumnName(col);
    }
    return name;
  }

  public static void duplicateRow(int row,int insertPos, DataKinds kind) {
    AbstractLogicData logicData, newLogicData;

    switch (kind) {
      case FIRE:
        logicData = new FireRegenerationData();
        break;
      case SUCCESSION:
        logicData = new SuccessionRegenerationData(false);
        break;
      default:
        return;
    }
    newLogicData = logicData.duplicate();

    getData(kind).addRow(insertPos,kind.toString(),newLogicData);
  }

}






