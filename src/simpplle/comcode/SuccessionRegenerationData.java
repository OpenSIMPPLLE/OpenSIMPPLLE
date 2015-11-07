package simpplle.comcode;

import java.util.Vector;
import java.io.Externalizable;
import java.io.ObjectOutput;
import java.io.IOException;
import java.io.ObjectInput;
import java.util.ArrayList;

/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class deals Succession Regeneration Data.  This is one of the two types of regeneration data.  Fire is the other one. 
 * Succession occurrs if nonforest state exist on a forested habitat type.  It uses only the adjacent communities logic, basically looking for adjacent seed sources.   
 *
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *   
 */

public class SuccessionRegenerationData
  extends simpplle.comcode.logic.RegenerationData implements  Externalizable
{
  static final long serialVersionUID = 2575438321718108700L;
  static final int  version          = 2;

  public static final int SUCCESSION_COL         = LAST_COL+1;
  public static final int SUCCESSION_SPECIES_COL = LAST_COL+2;

  private static final int NUM_COLUMNS = SUCCESSION_SPECIES_COL+1;

  private static final String LISTDELIM = ":";

  public Boolean succession        = false;
  public ArrayList<RegenerationSuccessionInfo> successionSpecies =
      new ArrayList<RegenerationSuccessionInfo>();

  public SuccessionRegenerationData() {
    super();
    sysKnowKind = SystemKnowledge.REGEN_LOGIC_SUCC;
  }
  private SuccessionRegenerationData(HabitatTypeGroupType group) {
    super(group);
    sysKnowKind = SystemKnowledge.REGEN_LOGIC_SUCC;
  }
  public SuccessionRegenerationData(boolean succession) {
    this.succession        = Boolean.valueOf(succession);
    sysKnowKind = SystemKnowledge.REGEN_LOGIC_SUCC;
  }
  public SuccessionRegenerationData(boolean succession,HabitatTypeGroupType group) {
    this(group);
    this.succession        = Boolean.valueOf(succession);
    sysKnowKind = SystemKnowledge.REGEN_LOGIC_SUCC;
  }
  public SuccessionRegenerationData(Species species, boolean succession) {
    super(species);
    this.succession        = Boolean.valueOf(succession);
    sysKnowKind = SystemKnowledge.REGEN_LOGIC_SUCC;
  }
/**
 * Duplicates this succession regeneration object.  
 */
  public simpplle.comcode.logic.AbstractLogicData duplicate() {
    SuccessionRegenerationData logicData = new SuccessionRegenerationData();
    super.duplicate(logicData);

    logicData.succession = succession;
    logicData.successionSpecies = new ArrayList<RegenerationSuccessionInfo>(successionSpecies);

    return logicData;
  }

  public void setSuccession(Boolean bool) {
    succession = (bool != null) ? bool : Boolean.FALSE;
  }
  // Vector is assumed to contain species strings.
  public void setSuccessionSpecies(Vector v) throws ParseError {
    successionSpecies.clear();
    if (v == null) { return; }

    for (int i=0; i<v.size(); i++) {
      successionSpecies.add(
          new RegenerationSuccessionInfo((String)v.elementAt(i)));
    }
  }

  public Object getValueAt(int col) {
    switch (col) {
      case SUCCESSION_COL:         return succession;
      case SUCCESSION_SPECIES_COL: return successionSpecies;
      default:                     return super.getValueAt(col);
    }
  }

  public void setValueAt(int col, Object value) {
    switch (col) {
      case SUCCESSION_SPECIES_COL:
        // Set in the table editor
        SystemKnowledge.markChanged(sysKnowKind);
        break;
      case SuccessionRegenerationData.SUCCESSION_COL:
        if ((Boolean)value != succession) {
          SystemKnowledge.markChanged(sysKnowKind);
        }
        succession = (Boolean)value;
        break;
      default:
        super.setValueAt(col,value);
    }
  }

  public static int getNumColumns() { return NUM_COLUMNS; }
/**
 * Returns the Id of the succession regeneration column.  
 * @return
 */
  public static int[] getColumns() {
    return new int[] {
        SPECIES_CODE_COL,
        SUCCESSION_COL,
        SUCCESSION_SPECIES_COL,
    };
  }
/**
 * Columns to be desplayed in GUI Regeneration Logic table.  
 * @return column name for succession regeneration.  
 */
  public static String[] getColumnNames() {
    return new String[] {
        "Species",
        "Succession Regen Possible",
        "Succession Seed species/To State",
    };
  }
/**
 * Gets the succession regeneration column name from its column Id.
 * @param column the column Id.
 * @return the column name.  
 */
  public static String getColumnName(int column) {
    switch (column) {
      case SUCCESSION_COL:         return "Succession Regen Possible";
      case SUCCESSION_SPECIES_COL: return "Succession Seed species/To State";
      default:                     return simpplle.comcode.logic.RegenerationData.getColumnName(column);
    }
  }
/**
 * Gets the succession column Id from its name.  These will be either the succession_col (17) or succession_species_col (18) or the species, which is a base logic column 
 * @param logic
 * @param name
 * @return
 */
  public static int getColumnNumFromName(simpplle.comcode.logic.BaseLogic logic, String name) {
    if (name.equalsIgnoreCase("Succession Regen Possible")) {
      return SUCCESSION_COL;
    }
    else if (name.equalsIgnoreCase("Succession Seed species/To State")) {
      return SUCCESSION_SPECIES_COL;
    }
    else {
      return simpplle.comcode.logic.RegenerationData.getColumnNumFromName(logic,name);
    }
  }
/**
 * Writes out the succession regeneration info.  These are boolean for succession, succession species arraylist size, and all the succession species.  
 */
  public void writeExternal(ObjectOutput out) throws IOException {
    super.writeExternal(out);

    out.writeInt(version);
    out.writeBoolean(succession);
    int size = (successionSpecies != null) ? successionSpecies.size() : 0;
    out.writeInt(size);
    for (int i=0; i<size; i++) {
      out.writeObject(successionSpecies.get(i));
    }
  }

  public void readExternal(ObjectInput in)
      throws IOException, ClassNotFoundException
  {
    super.readExternal(in);

    int version = in.readInt();
    succession = in.readBoolean();
    successionSpecies.clear();
    int size = in.readInt();
    if (size > 0) {
      for (int i=0; i<size; i++) {
        successionSpecies.add((RegenerationSuccessionInfo)in.readObject());
      }
    }

  }
}
