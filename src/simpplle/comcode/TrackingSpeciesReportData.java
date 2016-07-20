/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.collections.map.MultiKeyMap;
import java.util.HashMap;
import org.apache.commons.collections.keyvalue.MultiKey;
import org.apache.commons.collections.MapIterator;
import org.apache.commons.collections.map.Flat3Map;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/** 
 * This class defines methods for Tracking Species Report Data.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */
public class TrackingSpeciesReportData {
  static final int  version          = 1;

  // SPECIES,SELECTED not used but its inclusion server to make sure the other
  // items are the correct column number.
  public enum Columns {
    SPECIES, CATEGORY, START_PCT, END_PCT;

    static Columns lookup(int ordinal) {
      for (Columns column : values()) {
        if (column.ordinal() == ordinal) {
          return column;
        } 
      }
      return null;
    }

  };

/**
 * 
 * Category class is used throughout the tracking species reports.  It acts similar to a graph node with attributes species, range, category and boolean default category.  
 *
 */
  public class Category {
    public Species species;
    public Range   range;
    public String  category;
    public boolean defaultCat;
/**
 * duplicates the category by creating another instance of category and copying the species, range, category name, and boolean default status.  
 * @return
 */
    public Category duplicate() {
      Category cat = new Category();
      cat.species  = this.species;
      cat.range    = new Range(range.lower, range.upper);
      cat.category = new String(category);
      cat.defaultCat = defaultCat;
      return cat;
    }
  }

  private ArrayList<Category> data;
  private static ArrayList<Species> allSpecies;

  // Key : Category
  // Value: int[time steps]  (acres is data)
  private HashMap<String,int[]> summaryHm = new HashMap<String,int[]>();

  private ArrayList<String> allCategories = new ArrayList<String>();

  static {
    TrackingSpeciesReportData.updateAllSpeciesList();
  }
/**
 * Checks if there is any data in the Tracking Species Category arraylist.  
 * @return true if there is any data in the Tracking Species Category arraylist
 */
  public boolean hasData() {
    return data != null && data.size() > 0;
  }
  /**
   * Clears the summary data hashmap and all categories string arraylist
   */
  public void clearSummaryData() {
    summaryHm.clear();
    allCategories.clear();
  }
  public HashMap<String,int[]> getSummaryHm() { return summaryHm; }
/**
 * Gets the arraylist containing the string names of all the categories Tracking species report Data which will basically be the name
 * of the tracking species.  
 * @return arraylist of names of all the tracking species.  
 */
  public ArrayList<String> getAllCategories() { return allCategories; }

  /**
   * Updates the all species tracking list by getting all the invasive species.  Gets a trackings species if it exists, if not creates one.  The checks
   * the all species arraylist for the species.  If it does not exist in there already, puts it in.  The end result is a allSpecies arraylist
   * that contains all the tracking species.
   */
  public static void updateAllSpeciesList() {
    Collection allTrackSpecies = InclusionRuleSpecies.getAllSpecies();
    ArrayList<Species> invasiveSpeciesList = Species.getInvasiveSpeciesList();

    allSpecies = new ArrayList<Species>(invasiveSpeciesList);
    for (Object elem : allTrackSpecies) {
      InclusionRuleSpecies trkSpecies = (InclusionRuleSpecies)elem;
      Species species = Species.get(trkSpecies.toString(),true);
      if (allSpecies.contains(species) == false) {
        allSpecies.add(species);
      }
    }
  }
/**
 * Gets the row count by using the size() method for the Category arraylist containing the Tracking Species Categories objects.
 * @return
 */
  public int getRowCount() { return data.size(); }
  /**
   * Used in GUI table data model.  Returns 4 which tells how many columns are in the table data model.  
   * Choices for these are SPECIES, CATEGORY, START_PCT, END_PCT
   * @return 4
   */
  public static int getColumnCount() { return 4; }
/**
 * Constructor for tracking species report data.  Initializes a category arraylist to store data. 
 */
  public TrackingSpeciesReportData() {
    data = new ArrayList<Category>();
    updateAllSpeciesList();
  }
/**
 * Initializes the tracking species report.  Used to clear tracking species report, stored data, all species arraylist, 
 * updates the species lists and then adds them to the tracking species list 
 */
  public void initialize() {
    SystemKnowledge.clearFile(SystemKnowledge.TRACKING_SPECIES_REPORT);
    data.clear();
    allSpecies.clear();
    updateAllSpeciesList();

    for (Species species : allSpecies) {
      if (hasTrackingSpecies(species) == false) {
        addTrackingSpecies(species);
      }
    }
  }
  private static TrackingSpeciesReportData instance;
/**
 * Returns this instance of tracking species report data.
 * @return this tracking species report data
 */
  public static TrackingSpeciesReportData getInstance() { return instance; }
  /**
   * creates an instance of Tracking species report data
   * @return the instance of tracking species report data class
   */
  public static TrackingSpeciesReportData makeInstance() {
    instance = new TrackingSpeciesReportData();
    return getInstance();
  }
/**
 * Gets all the tracking species in the tracking species arraylist.  
 * @return the tracking species arraylist containing all the tracking species
 */
  public static ArrayList<Species> getAllSpeciesList() { return allSpecies; }
/**
 * gets the value at a specified cell.  
 * @param row
 * @param col
 * @return the data at specified cell, if no data returns default data for species, range, category and default category.  
 */
  public Object getValueAt(int row, int col) {
    Category item;
    if (data.size() == 0) {
      item = new Category();
      item.species = Species.UNKNOWN;
      item.range   = new Range(0,100);
      item.category = makeDefaultCategoryName(item);
      item.defaultCat = true;
    }
    else {
      item = data.get(row);
    }
    Columns column = Columns.lookup(col);


    switch (column) {
      case SPECIES:
        return item.species;
      case START_PCT:
        return item.range.getLower();
      case END_PCT:
        return item.range.getUpper();
      case CATEGORY:
        return item.category;
      default:
        return null;
    }
  }
/**
 * Gets the column clas according to column id.  
 * @param col Choices are SPECIES, CATEGORY, START_PCT, END_PCT
 * @return SPECIES = Species, CATEGORY = String, START_PCT = Integer, END_PCT = Integer or Object by default
 */
  public static Class getColumnClass(int col) {
    Columns column = Columns.lookup(col);

    switch (column) {
      case SPECIES:
        return Species.class;
      case START_PCT:
        return Integer.class;
      case END_PCT:
        return Integer.class;
      case CATEGORY:
        return String.class;
      default:
        return Object.class;
    }
  }
  /**
   * Sets an object value at a particular cell located by parameter row and column.
   * @param row
   * @param col
   * @param value
   */
  public void setValueAt(int row, int col, Object value) {
    Category item = data.get(row);
    Columns column = Columns.lookup(col);


    switch (column) {
      case SPECIES:
        break;
      case START_PCT:
        item.range.setLower((Integer)value);
        if (item.defaultCat) {
          item.category = makeDefaultCategoryName(item);
        }
        break;
      case END_PCT:
        item.range.setUpper((Integer)value);
        if (item.defaultCat) {
          item.category = makeDefaultCategoryName(item);
        }
        break;
      case CATEGORY:
        String str = (String)value;
        if (str.trim().length() == 0) {
          item.defaultCat = true;
          item.category = makeDefaultCategoryName(item);
        }
        else {
          item.category = str.trim();
          item.defaultCat = false;
        }
        break;
      default:
        break;
    }
  }
/**
 * Used in GUI table model.  Returns the column names as string.  
 * @param col column ID
 * @return the column name Choices are: "Track Species", "Start %", "End %", "Category Name".
 */
  public static String getColumnNames(int col) {
    Columns column = Columns.lookup(col);

    switch (column) {
      case SPECIES:      return "Track Species";
      case START_PCT:    return "Start %";
      case END_PCT:      return "End %";
      case CATEGORY:     return "Category Name";
      default:
        return "";
    }
  }
/**
 * Checks if there is a particular species in the tracking species report data.
 * @param species 
 * @return
 */
  public boolean hasTrackingSpecies(Species species) {
    return (getTrackingSpeciesRow(species) != -1);
  }
  /**
   * Gets teh particular row corresponding to a specific tracking species.  
   * @param species the tracking species 
   * @return
   */
  public int getTrackingSpeciesRow(Species species) {
    for (int row=0; row<data.size(); row++) {
      if (data.get(row).species == species) { return row; }
    }
    return -1;
  }
/**
 * Adds Tracking species category information at an insertion position.
 * @param insertPos where the tracking species will be added
 * @param newCategory category object of tracking species
 */
  private void addData(int insertPos, Category newCategory) {
    data.add(insertPos,newCategory);
  }
  /**
   * Overloaded addData method.  Adds Tracking species category information at an insertion positio
   * @param newCategory
   */
  private void addData(Category newCategory) {
    data.add(newCategory);
  }
  private String makeDefaultCategoryName(Category cat) {
    return new String(cat.species.toString() + " " +
                      Integer.toString(cat.range.lower) + "to" +
                      Integer.toString(cat.range.upper));
  }
  /**
   * Adds the species, range, category, and default category boolean for a Catagory object, which is then added to the arraylist containing 
   * all tracking species categories.  
   * @param species species to be added to the tracking species
   */
  public void addTrackingSpecies(Species species) {
    Category cat = new Category();
    cat.species  = species;
    cat.range    = new Range(0,100);
    cat.category = makeDefaultCategoryName(cat);
    cat.defaultCat = true;

    addData(cat);
  }
  /**
   * Removes a tracking species by removing the category object from the category arraylist. 
   * @param species
   */
  public void removeTrackingSpecies(Species species) {
    int index=-1;
    for (int row=0; row<data.size(); row++) {
      if (data.get(row).species == species) {
        index = row;
        break;
      }
    }
    if (index != -1) {
      data.remove(index);
    }
  }
/**
 * adds a row by creating a category object and adding species, range, default category name and inserting it into a specified position.   
 * @param insertPos
 * @param species
 */
  public void addRow(int insertPos, Species species) {
    Category cat = new Category();
    cat.species  = species;
    cat.range    = new Range(0,100);
    cat.category = makeDefaultCategoryName(cat);
    cat.defaultCat = true;

    addRow(insertPos,cat);
  }
  public void addRow(int insertPos, Category cat) {
    int size = data.size();
    if (insertPos > size || insertPos == -1) {
      addData(cat);
    }
    else {
      addData(insertPos,cat);
    }
  }
  /**
   * Duplicates a row
   * @param row
   * @param insertPos
   */
  public void duplicateRow(int row,int insertPos) {
    Category cat = data.get(row);
    addRow(insertPos,cat.duplicate());
  }
  public void removeRow(int row) {
    data.remove(row);
  }
  /**
   * 
   * @param row
   * @return
   */
  public int moveRowUp(int row) {
    Category cat = data.get(row);
    removeRow(row);
    int newRow=row-1;
    if (newRow < 0) { newRow = data.size(); }
    addRow(newRow,cat);
    return newRow;
  }
  public int moveRowDown(int row) {
    Category cat = data.get(row);
    removeRow(row);
    int newRow=row+1;
    if (row+1 > data.size()) { newRow = 0; }
    addRow(newRow,cat);
    return newRow;
  }
/**
 * reads in an object from external source, in order: version, species, lower and upper range, category, boolean for default category.  
 * Adds the category along with variables to category arraylist
 * @param in
 * @throws IOException
 * @throws ClassNotFoundException
 */
  public void read(ObjectInputStream in)
    throws IOException, ClassNotFoundException
  {
    int version = in.readInt();

    int size = in.readInt();
    data.clear();
    for (int i=0; i<size; i++) {
      Category cat = new Category();
      cat.species = (Species)SimpplleType.readExternalSimple(in,SimpplleType.SPECIES);
      cat.range = new Range(0,100);
      cat.range.lower = in.readInt();
      cat.range.upper = in.readInt();
      cat.category = (String)in.readObject();
      cat.defaultCat = in.readBoolean();
      data.add(cat);
    }
  }
  /**
   * 
   * @param os
   * @throws IOException caught in GUI
   */
  public void save(ObjectOutputStream os) throws IOException {
    os.writeInt(version);

    int size = data.size();
    os.writeInt(size);
    for (int i=0; i<size; i++) {
      Category cat = data.get(i);
      cat.species.writeExternalSimple(os);
      os.writeInt(cat.range.lower);
      os.writeInt(cat.range.upper);
      os.writeObject(cat.category);
      os.writeBoolean(cat.defaultCat);
    }
  }


  private boolean isPercentInRange(Category cat, float pct) {
    return cat.range.inRange(pct);
  }
  /**
   * methods to update summary report.
   * @param unit
   * @param timeStep
   */
  public void updateSummary(Evu unit, int timeStep) {
    Simulation simulation = Simulation.getInstance();

    Lifeform[] lives  = Lifeform.getAllValues();

    for (int i=0; i<lives.length; i++) {
      VegSimStateData state = unit.getStateFinalSeason(timeStep,lives[i]);
      if (state == null) { continue; }

      Flat3Map map = state.getTrackingSpeciesMap();
      if (map == null) { continue; }

      MapIterator it = map.mapIterator();

      while (it.hasNext()) {
        InclusionRuleSpecies sp = (InclusionRuleSpecies)it.next();
        Species species = Species.get(sp.toString());
        Float pctObj = (Float)it.getValue();
        if (pctObj == null) { continue; }

        for (int row=0; row<data.size(); row++) {
          if (data.get(row).species == species) {
            Category cat = data.get(row);
            if (isPercentInRange(cat,pctObj) == false) { continue; }

            int[] acresData = summaryHm.get(cat.category);
            if (acresData == null) {
              acresData = new int[simulation.getNumTimeSteps() + 1];
              for (int ts = 0; ts < acresData.length; ts++) {
                acresData[ts] = 0;
              }
              summaryHm.put(cat.category, acresData);
            }
            acresData[timeStep] += unit.getAcres();

            if (allCategories.contains(cat.category) == false) {
              allCategories.add(cat.category);
            }

          }
        }
      }

    }
  }


}




