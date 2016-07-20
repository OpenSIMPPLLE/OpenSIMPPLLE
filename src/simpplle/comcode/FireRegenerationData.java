/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.io.PrintWriter;
import java.util.Vector;
import java.io.Externalizable;
import java.io.ObjectOutput;
import java.io.IOException;
import java.io.ObjectInput;
import java.util.ArrayList;

/**
 * This class provides methods to get and set Fire Regeneration data, this is a type of Regeneration Data.  It includes resprouting, adjacent resprouting, in place seed
 * in landscape seed and adjacent evu info.
 *
 * Fire Process logic
 * determine all process probabilities for each evu ->use probabilities to select process
 * if selected process is fire event->if fire suppresssion ->determine probability of staying class size A due to weather or fire suppression → if yes change process for evu to succession and record a class A fire with suppression costs
 * if not suppressed at Class A level → determine type of fire and fire spread → at end of simulation calculate fire suppression costs and emissions
 * if selected process is fire and fire suppression is no, determine probability of staying class A size due to weather → if it spreads beyond class A size determine type of firefighter and fire → at end of simulation calculate emissions
  *if stays at class A size due to weather->change process for evu to succession and record class A fire
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 * @see simpplle.comcode.RegenerationData
 */

public class FireRegenerationData
  extends RegenerationData implements Externalizable
{
  static final long serialVersionUID = 7114703750696960417L;
  static final int  version          = 2;

  public static final int RESPROUTING_COL        = LAST_COL+1;
  public static final int ADJ_RESPROUTING_COL    = LAST_COL+2;
  public static final int IN_PLACE_SEED_COL      = LAST_COL+3;
  public static final int IN_LANDSCAPE_COL       = LAST_COL+4;
  public static final int ADJACENT_COL           = LAST_COL+5;

  private static final int NUM_COLUMNS = ADJACENT_COL+1;


  private static final String LISTDELIM = ":";

  public ArrayList<VegetativeType>  resprouting       = new ArrayList<VegetativeType>();
  public ArrayList<VegetativeType>  adjResprouting    = new ArrayList<VegetativeType>();
  public ArrayList<VegetativeType>  inPlaceSeed       = new ArrayList<VegetativeType>();
  public ArrayList<VegetativeType>  inLandscape       = new ArrayList<VegetativeType>();
  public ArrayList<VegetativeType>  adjacent          = new ArrayList<VegetativeType>();
/**
 * Primary constructor.  Initializes the system knowledge to regeneration logic, and inherits from the RegenerationData superclass  
 */
  public FireRegenerationData() {
    super();
    sysKnowKind = SystemKnowledge.REGEN_LOGIC_FIRE;
  }
  /**
   * Overloaded constructor.  Initializes the system knowledge to regeneration logic, takes in the HabitatTypeGroup, and inherits from the RegenerationData superclass  
   * @param group habitat type group type passed to the superclass constructor
   */
  public FireRegenerationData(HabitatTypeGroupType group) {
    super(group);
    sysKnowKind = SystemKnowledge.REGEN_LOGIC_FIRE;
  }
  /**
   * Overloaded constructor.  Initializes the system knowledge to regeneration logic, takes in the species parameter, and inherits from the RegenerationData superclass
   * @param species passed to the superclass constructor
   */
  public FireRegenerationData(Species species) {
    super(species);
    sysKnowKind = SystemKnowledge.REGEN_LOGIC_FIRE;
  }
  /**
   * initializes new vegetative type arraylists with duplicate data
   * returns a duplicate of resprouting, adjacent resprouting, in place seed, in landscape, and adjacent vegetative type.  
   */
  public AbstractLogicData duplicate() {
    FireRegenerationData logicData = new FireRegenerationData();
    super.duplicate(logicData);

    logicData.resprouting    = new ArrayList<VegetativeType>(resprouting);
    logicData.adjResprouting = new ArrayList<VegetativeType>(adjResprouting);
    logicData.inPlaceSeed    = new ArrayList<VegetativeType>(inPlaceSeed);
    logicData.inLandscape    = new ArrayList<VegetativeType>(inLandscape);
    logicData.adjacent       = new ArrayList<VegetativeType>(adjacent);

    return logicData;
  }

  /**
   *
   *These setResprouting, setAdjResprouting, setInPlaceSeed, setInLandscape, setAdjacent 
   *setter functions are only used when loading data from a file
   *in the form of strings.  We will search for the first VegetativeType
   *we can find.  When this information is actually used we will use the
   *individual components (e.g. species, size class, density) to build
   *a new state in the appropriate habitat type group.  This avoids wasting
   *memory with lots of unnecessary strings.
   * */
   
  public void setResprouting(Vector v) {
    buildVegetativeTypeList(v,resprouting);
  }
  /**
   * @see note in void simpplle.comcode.FireRegenerationData.setResprouting(Vector v)
   */
  public void setAdjResprouting(Vector v) {
    buildVegetativeTypeList(v,adjResprouting);
  }
  /**
   * @see note in void simpplle.comcode.FireRegenerationData.setResprouting(Vector v)
   *
   */
  public void setInPlaceSeed(Vector v) {
    buildVegetativeTypeList(v,inPlaceSeed);
  }
  /**
   * @see note in void simpplle.comcode.FireRegenerationData.setResprouting(Vector v)
   */
  public void setInLandscape(Vector v) {
    buildVegetativeTypeList(v,inLandscape);
  }
  /**
   * @see note in void simpplle.comcode.FireRegenerationData.setResprouting(Vector v)
   */
  public void setAdjacent(Vector v) {
    buildVegetativeTypeList(v,adjacent);
  }
  private void buildVegetativeTypeList(Vector v, ArrayList<VegetativeType> vtList) {
    if (v == null) { return; }

    VegetativeType vt;
    for (int i=0; i<v.size(); i++) {
      vt = HabitatTypeGroup.getVegType((String)v.elementAt(i));
      if (vt != null) {
        vtList.add(vt);
      }
      vt = null;
    }
  }

/**
 * gets object at a specified column for RESPROUTING_COL, ADJ_RESPROUTING_COL, IN_PLACE_SEED_COL, IN_LANDSCAPE_COL, and ADJACENT_COL
 * default calls the superclass getValueAt method
 */
  public Object getValueAt(int col) {
    switch (col) {
      case RESPROUTING_COL:        return resprouting;
      case ADJ_RESPROUTING_COL:    return adjResprouting;
      case IN_PLACE_SEED_COL:      return inPlaceSeed;
      case IN_LANDSCAPE_COL:       return inLandscape;
      case ADJACENT_COL:           return adjacent;
      default:                     return super.getValueAt(col);
    }
  }
/**
 * sets the Fire regeneration data in a column based on an integer key choices are 
 * RESPROUTING_COL, ADJ_RESPROUTING_COL, IN_PLACE_SEED_COL, IN_LANDSCAPE_COL, and ADJACENT_COL
 * <p>Also marks systemknowledge changed
 */
  public void setValueAt(int col, Object value) {
    switch (col) {
      case FireRegenerationData.RESPROUTING_COL:
      case FireRegenerationData.ADJ_RESPROUTING_COL:
      case FireRegenerationData.IN_LANDSCAPE_COL:
      case FireRegenerationData.IN_PLACE_SEED_COL:
      case FireRegenerationData.ADJACENT_COL:
        SystemKnowledge.markChanged(sysKnowKind);
        break;
      default:
        super.setValueAt(col,value);
    }
  }

  public static int getNumColumns() { return NUM_COLUMNS; }

  /**
   * makes a integer array of the columns indexed starting at 0 for Species_Code_Col
   * @return integer array of the columns indexed starting at 0 for Species_Code_Col
   */
  public static int[] getColumns() {
    return new int[] {
        SPECIES_CODE_COL,
        RESPROUTING_COL,
        ADJ_RESPROUTING_COL,
        IN_PLACE_SEED_COL,
        IN_LANDSCAPE_COL,
        ADJACENT_COL
    };
  }
  /**
   * 
   * @return a string literal array of the column names indexed starting at 0
   */
  public static String[] getColumnNames() {
    return new String[] {
        "Species",
        "Resprouting State",
        "Adjacent Resprouting",
        "In-place Seed",
        "In Landscape",
        "Adjacent"
    };
  }

  public static String getColumnName(int column) {
    switch (column) {
      case RESPROUTING_COL:        return "Resprouting State";
      case ADJ_RESPROUTING_COL:    return "Adjacent Resprouting";
      case IN_PLACE_SEED_COL:      return "In-place Seed";
      case IN_LANDSCAPE_COL:       return "In Landscape";
      case ADJACENT_COL:           return "Adjacent";
      default:                     return RegenerationData.getColumnName(column);
    }
  }

  public static int getColumnNumFromName(BaseLogic logic,String name) {
    if (name.equalsIgnoreCase("Resprouting State")) {
      return RESPROUTING_COL;
    }
    else if (name.equalsIgnoreCase("Adjacent Resprouting")) {
      return ADJ_RESPROUTING_COL;
    }
    else if (name.equalsIgnoreCase("In-place Seed")) {
      return IN_PLACE_SEED_COL;
    }
    else if (name.equalsIgnoreCase("In Landscape")) {
      return IN_LANDSCAPE_COL;
    }
    else if (name.equalsIgnoreCase("Adjacent")) {
      return ADJACENT_COL;
    }
    else {
      return RegenerationData.getColumnNumFromName(logic,name);
    }
  }

  public void writeExternal(ObjectOutput out) throws IOException {
    super.writeExternal(out);

    out.writeInt(version);
    VegetativeType.setLimitedSerialization();
    writeVegTypeList(out,resprouting);
    writeVegTypeList(out,adjResprouting);
    writeVegTypeList(out,inPlaceSeed);
    writeVegTypeList(out,inLandscape);
    writeVegTypeList(out,adjacent);
    VegetativeType.clearLimitedSerialization();
  }
  private void writeVegTypeList(ObjectOutput out, ArrayList<VegetativeType> v)
    throws IOException
  {
    int size = (v != null) ? v.size() : 0;
    out.writeInt(size);
    for (int i=0; i<size; i++) {
      out.writeObject(v.get(i));
    }
  }
  private void readVegTypeList(ObjectInput in, ArrayList<VegetativeType> vtList)
    throws IOException, ClassNotFoundException
  {
    vtList.clear();
    int size = in.readInt();
    for (int i=0; i<size; i++) {
      vtList.add((VegetativeType)in.readObject());
    }
  }

    /**
     * read in Fire regeneration data
     * @param in
     * @throws IOException
     * @throws ClassNotFoundException
     */
  public void readExternal(ObjectInput in)
      throws IOException, ClassNotFoundException
  {
    super.readExternal(in);

    int version = in.readInt();
    readVegTypeList(in,resprouting);
    readVegTypeList(in,adjResprouting);
    readVegTypeList(in,inPlaceSeed);
    readVegTypeList(in,inLandscape);
    readVegTypeList(in,adjacent);
  }
}
