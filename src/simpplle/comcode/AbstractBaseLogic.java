/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

/** 
 * This class defines the base logic for the System.  It is an abstract class and therefore cannot be instantiated.  It is subclassed by many other classes.
 * For an understanding of the base logic for the system see BaseLogic.java. 
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public abstract class AbstractBaseLogic {

  private static final int version = 3;

  protected SystemKnowledge.Kinds sysKnowKind;

  /**
   * Maps a logic table name to a list of column names.
   */
  protected HashMap<String, ArrayList<String>> columns;

  /**
   * Maps a logic table name to a list of visible column names.
   */
  protected HashMap<String,ArrayList<String>> visibleColumnsHm = new HashMap<String,ArrayList<String>>();

  /**
   * Maps a logic table name to columns of logic data.
   */
  public HashMap<String,ArrayList<AbstractLogicData>> data = new HashMap<String,ArrayList<AbstractLogicData>>();

  /**
   *
   */
  public static final int ROW_COL = 0;

  /**
   *
   */
  protected static final int LAST_COL = ROW_COL;

  /**
   *
   */
  private static boolean noChangeRead = false;

  /**
   * Deletes the columns from all kinds of system knowledge and adds a row index column to each. The
   * index column is not added if there is not a process with a name matching the kind of knowledge.
   *
   * @param kinds An array of names of system knowledge kinds
   */
  protected AbstractBaseLogic(String[] kinds) {
    visibleColumnsHm.clear();
    columns = new HashMap<String,ArrayList<String>>();
    for (int i=0; i<kinds.length; i++) {
      Process process = Process.findInstance(kinds[i]);
      if (process != null && process.isUniqueUI()) { continue; }

      columns.put(kinds[i],new ArrayList<String>());
      addColumn(kinds[i],"ROW_COL");
    }
  }

  /**
   * Adds a new column to a logic table.
   *
   * @param kind A kind of logic
   * @param column The name of the new column
   */
  protected void addColumn(String kind, String column) {
    columns.get(kind).add(column);
  }

  /**
   * This is used in the GUI Logic Data Model to get the visible column count for a particular logic kind.
   * @param kind process kind
   * @return returns the array size of visible columns for a particular kind, which is effictively a count of the columns.
   */
  public int getColumnCount(String kind) {
    ArrayList<String> columns = visibleColumnsHm.get(kind);
    return columns != null ? columns.size() : 0;
  }

  /**
   * Gets a particular column name based on data kind and column number.  The base logic columns are ECO_GROUP_COL,SPECIES_COL, SIZE_CLASS_COL, DENSITY_COL, PROCESS_COL, TREATMENT_COL, SEASON_COL,
   * MOISTURE_COL, TEMP_COL, TRACKING_SPECIES_COL, OWNERSHIP_COL, SPECIAL_AREA_COL, ROAD_STATUS_COL, TRAIL_STATUS_COL, LANDTYPE_COL.  These are set in Base Logic.java
   * @param col
   * @return
   */
  public abstract String getColumnName(String kind, int col);

  /**
   * Gets a particular column namebased on column number only.  The base logic columns are ECO_GROUP_COL,SPECIES_COL, SIZE_CLASS_COL, DENSITY_COL, PROCESS_COL, TREATMENT_COL, SEASON_COL,
   * MOISTURE_COL, TEMP_COL, TRACKING_SPECIES_COL, OWNERSHIP_COL, SPECIAL_AREA_COL, ROAD_STATUS_COL, TRAIL_STATUS_COL, LANDTYPE_COL.  These are set in Base Logic.java
   * @param col
   * @return
   */
  public static String getColumnName(int col) {
    switch (col) {
      case ROW_COL: return "Priority";
      default: return "";
    }
  }

  /**
   * Returns the index of a column for a kind of system knowledge given the column's name.
   *
   * @param kind A kind of system knowledge
   * @param colIdName A column name
   * @return The index of the column if it exists
   */
  public int getColumnPosition(String kind,String colIdName) {
    return columns.get(kind).indexOf(colIdName);
  }

  /**
   * Returns the name of a column for a kind of system knowledge given the column's index.
   *
   * @param kind A kind of system knowledge
   * @param col A column index
   * @return The name of the column if it exists
   */
  public String getColumnIdName(String kind,int col) {
    return columns.get(kind).get(col);
  }

  /**
   * This is used to get a turn a string column identifier into an integer representing the column number.  This is used by the GUI in table models.
   * @param name the string identifier of a column.
   * @return integer value of a column
   */
  public int getColumnNumFromName(String name) {
    if (name.equalsIgnoreCase("Priority")) {
      return ROW_COL;
    }
    return 0;
  }

  /**
   *
   *
   * @param kind
   */
  public void addVisibleColumnAll(String kind) {
    for (int i=0; i<columns.get(kind).size(); i++) {
      addVisibleColumn(kind,i);
    }
  }

  /**
   *
   *
   * @param kind logic kind
   * @param name
   */
  public void addVisibleColumn(String kind, String name) {
    int col = getColumnPosition(kind,name);
    addVisibleColumn(kind,col,name);
  }

  /**
   *
   *
   * @param kind
   * @param col
   */
  public void addVisibleColumn(String kind, int col) {
    String name = getColumnIdName(kind,col);
    addVisibleColumn(kind,col,name);
  }

  /**
   *
   *
   * Add visible column to the visible columns hashmap.  Uses the logic kind to get the string arraylist of columns.
   * If it is empty for that kind, will create a new arraylist of columns and add it to the visible column hashmap, otherwise
   * inserts the
   * Then marks the system knowledge changed for that
   * @param kind
   * @param col
   * @param name
   */
  public void addVisibleColumn(String kind, int col, String name) {
    ArrayList<String> values = visibleColumnsHm.get(kind);
    if (values == null) {
      values = new ArrayList<String>();
      visibleColumnsHm.put(kind,values);
    }
    if (values.contains(name) == false) {
      int insertPos = findVisibleColumnInsertPosition(kind,col,values);
      values.add(insertPos,name);
      SystemKnowledge.markChanged(sysKnowKind);
    }
  }

  /**
   * Hides a visible column. This is marked to be recorded with the system knowledge.
   *
   * @param kind The name of a logic table
   * @param col The index of a column to hide
   */
  public void removeVisibleColumn(String kind, int col) {
    String name = getColumnIdName(kind,col);
    ArrayList<String> values = visibleColumnsHm.get(kind);
    if (values == null) { return; }

    if (values.contains(name)) {
      values.remove(name);
      SystemKnowledge.markChanged(sysKnowKind);
    }
  }

  /**
   * Returns an array of visible column indices from a logic table.
   *
   * @param kind A kind of logic
   * @return An array of column indices
   */
  public int[] getVisibleColumns(String kind) {
     ArrayList<String> visibleColumns = visibleColumnsHm.get(kind);

     int[] cols = new int[visibleColumns.size()];
     for (int i=0; i<cols.length; i++) {
       cols[i] = getColumnPosition(kind,visibleColumns.get(i));
     }
     return cols;
  }

  /**
   * Gets the visible columns hashmap.
   * @return
   */
  public HashMap<String,ArrayList<String>> getVisibleColumnsHm() {
    return visibleColumnsHm;
  }

  /**
   * Determines if a column is visible in a logic table.
   *
   * @param kind A kind of logic
   * @param col The name of a column
   * @return True if the column is visible
   */
  public boolean isVisibleColumn(String kind, int col) {
    String name = getColumnIdName(kind,col);
    ArrayList<String> visibleColumns = visibleColumnsHm.get(kind);
    if (visibleColumns == null || visibleColumns.size() == 0) {
      return false;
    }
    int index = visibleColumns.indexOf(name);
    return (index != -1);
  }

  /**
   *
   *
   * @param kind
   * @param col
   * @param values
   * @return
   */
  protected int findVisibleColumnInsertPosition(String kind, int col, ArrayList<String> values) {
    int i=0;
    for (String name : values) {
      int listCol = getColumnPosition(kind,name);
      if (listCol > col) { return i; }
      i++;
    }
    return values.size();
  }

  /**
   * Abstract method to adds a row in the GUI table model.
   * @param insertPos the row number where one sill be inserted.
   * @param kind the logic data kind to be inserted.
   */
  public abstract void addRow(int insertPos, String kind);

  /**
   * Adds a row based on logic data kind, and
   * @param kind
   * @param logicData
   */
  public void addRow(String kind, AbstractLogicData logicData) {
    addRow(-1,kind,logicData);
  }

  /**
   * If insert position is -1, this is a flag to make a new row and add the Logic Data to it.  If not -1, just adds an Abstract Logic Data
   * object to the arraylist at a particular position.
   * @param insertPos
   * @param kind
   * @param logicData
   */
  public void addRow(int insertPos, String kind, AbstractLogicData logicData) {
    int size = getData(kind,true).size();
    if (insertPos > size || insertPos == -1) {
      getData(kind,true).add(logicData);
    }
    else {
      getData(kind,true).add(insertPos,logicData);
    }
  }

  /**
   * This is used in the GUI to remove a row from the table model.
   * @param row the row to be removed
   * @param kind the logic kind to be removed.
   */
  public void removeRow(int row, String kind) {
    getData(kind).remove(row);
  }

  /**
   * Deletes the data at a particular row.
   * @param row the row to be deleted
   * @param kind the logic data kind.
   */
  public void deleteDataRow(int row, String kind) {
    getData(kind).remove(row);
    markChanged();
  }

  /**
   * Abstract method to duplicates a row.
   * @param row
   * @param insertPos
   * @param kind
   */
  public abstract void duplicateRow(int row, int insertPos, String kind);

  /**
   * This moves a row up in the GUI table models by creating a new temporary row to hold the row information, then removing the row and setting the temporary row to
   * row number minus 1.  There is a check to make sure the row is not <0.  It then adds the row to the data kind arraylist.
   * @param row row to be moved upward
   * @param kind the logic data kind.
   * @return the int representing the new row
   */
  public int moveRowUp(int row, String kind) {
    AbstractLogicData logicData = getValueAt(row,kind);
    removeRow(row,kind);
    int newRow=row-1;
    if (newRow < 0) { newRow = getData(kind).size(); }
    getData(kind).add(newRow,logicData);
    return newRow;
  }

  /**
   * This moves a row down in the GUI table models by creating a new temporary row to hold the row information, then removing the row and setting the temporary row to
   * row number plus 1.  There is a check to make sure the row is not greater than the data arraylist for the particlar logic data kind.
   * It then adds the row to the data kind arraylist.
   * @param row row to be moved upward
   * @param kind the logic data kind.
   * @return the int representing the new row
   */
  public int moveRowDown(int row, String kind) {
    AbstractLogicData logicData = getValueAt(row,kind);
    removeRow(row,kind);
    int newRow=row+1;
    if (row+1 > getData(kind).size()) { newRow = 0; }
    getData(kind).add(newRow,logicData);
    return newRow;
  }

  /**
   * Uses the size of the data kind arraylist to obtain the last row index (-1 from size to account from first index of arraylist being 0)
   * @param kind
   * @return the integer value of last row.
   */
  public int getLastRowIndex(String kind) {
    return getData(kind).size() - 1;
  }


  /**
   * If there is a particular logic data kind present in instance, this will return the size of the data kind, which is effectively a row kind.
   * @param kind
   * @return
   */
  public int getRowCount(String kind) {
    return isDataPresent(kind) ? getData(kind).size() : 0;
  }

  /**
   * Sets the data value at a particular cell in the table model.  This is called from the GUI and used in logic data models.
   * @param value the object value to be edited at designated cell
   * @param row row of cell
   * @param col column of cell
   * @param kind logic data kind used to locate the row on which the cell to be edited is located.
   */
  public void setData(Object value, int row, int col, String kind) {
    getData(kind).get(row).setValueAt(col,value);
    markChanged();
  }

  /**
   * Gets an arraylist of a particular abstract logic data based on the logic data kind.
   * <logic kind, <ArrayList of Logic Data> >
   * -uses the kind of logic to get the kind of data.
   * @param kind
   * @return
   */
  protected ArrayList<AbstractLogicData> getData(String kind) {
    return data.get(kind.toString());
  }

  /**
   *
   * @param kind
   * @param addifNull
   * @return
   */
  protected ArrayList<AbstractLogicData> getData(String kind, boolean addifNull) {
    if (addifNull && getData(kind) == null) {
      data.put(kind.toString(),new ArrayList<AbstractLogicData>());
    }
    return getData(kind);
  }

  /**
   * Gets the object in a particular cell.  This is a common practice in the dialogs to get the value of an object.
   * @param row row used to locate the cell
   * @param col column Id used to
   * @param kind logic kind
   * @return
   */
  public Object getValueAt(int row, int col, String kind) {
    if (isDataPresent(kind)) {
      return getValueAt(row, kind).getValueAt(col);
    }
    return null;
  }

  /**
   * Gets the Abstract Logic Data found by row and column.
   * @param row
   * @param kind
   * @return
   */
  public AbstractLogicData getValueAt(int row, String kind) {
    if (isDataPresent(kind)) {
      return getData(kind).get(row);
    }
    return null;
  }

  /**
   * Clears data of a particular process kind if there is any present.
   * @param kind the process kind.
   */
  public void clearData(String kind) {
    if (isDataPresent(kind)) {
      getData(kind).clear();
    }
  }

  /**
   * This is used in the to check if there is data of an Abstract Logic kind.
   * @param kind logic kind
   * @return true if there is data of a particular kind, false if there is not.
   */
  public boolean isDataPresent(String kind) {
    return (getData(kind) != null && getData(kind).size() > 0);
  }

  /**
   * Marks the system knowledge changed for a particular kind of system knowledge.
   */
  public void markChanged() {
    SystemKnowledge.markChanged(sysKnowKind);
  }

  /**
   * Used to notify OpenSimpplle that system knowledge has changed.
   * @return true if system knowledge has changed or user data entered.
   */
  public boolean hasChanged() {
    return SystemKnowledge.hasChangedOrUserData(sysKnowKind);
  }

  /**
   * Creates an object output stream to save the data of a particular object kind.
   * @param kind
   * @param os
   * @throws IOException
   */
  protected void saveData(String kind, ObjectOutputStream os) throws IOException {
    saveData(kind,os,false);
  }

  /**
   * Saves the abstract logic data by saving the visible column infoGets the abstract logic data based on the logic data kind
   * @param kind
   * @param os
   * @param includeVisibleCol
   * @throws IOException
   */
  protected void saveData(String kind, ObjectOutputStream os, boolean includeVisibleCol) throws IOException {
    if (includeVisibleCol) {
      saveVisibleColumnInfo(os);
    }
    ArrayList<AbstractLogicData> logicData = getData(kind);
    os.writeObject(logicData);
  }

  /**
   *
   * @param kind
   * @param in
   * @param version
   * @throws IOException
   * @throws ClassNotFoundException
   */
  protected void readData(String kind, ObjectInputStream in, int version)
    throws IOException, ClassNotFoundException
  {
    readData(kind,in,version,false);
  }

  /**
   *
   * @param kind
   * @param in
   * @param version
   * @param includeVisibleCol
   * @throws IOException
   * @throws ClassNotFoundException
   */
  protected void readData(String kind, ObjectInputStream in, int version, boolean includeVisibleCol)
    throws IOException, ClassNotFoundException
  {
    if (version == 2) {
      if (includeVisibleCol) {
        readVisibleColumnInfo(in);
      }
      ArrayList<LogicData> list = (ArrayList<LogicData>)in.readObject();
      data.put(kind,new ArrayList<AbstractLogicData>(list));
    }
    else {
      if (includeVisibleCol) {
        readVisibleColumnInfo(in);
      }
      data.put(kind, (ArrayList<AbstractLogicData>) in.readObject());
    }
  }

  /**
   * Writes the visible column hashmap.  First writes the size of the visible column hashmap, 
   * then writes the kind, and the ArrayList of string columns.  
   * @param os
   * @throws IOException
   */
  private void saveVisibleColumnInfo(ObjectOutputStream os) throws IOException {
    os.writeInt(visibleColumnsHm.size());
    for (String key : visibleColumnsHm.keySet()) {
      os.writeObject(key);
      os.writeObject(visibleColumnsHm.get(key));
    }    
  }

  /**
   * Reads the visible column info and makes into visible column hashmap.   <logic kind, <string ArrayList of visible columns> > 
   * @param in
   * @throws IOException
   * @throws ClassNotFoundException
   */
  private void readVisibleColumnInfo(ObjectInputStream in) throws IOException, ClassNotFoundException
  {
    int size = in.readInt();
    for (int i = 0; i < size; i++) {
      String key = (String) in.readObject();
      ArrayList<String> list = visibleColumnsHm.get(key);
      visibleColumnsHm.put(key, (ArrayList<String>) in.readObject());
      if (list != null) {
        for (int l = 0; l < list.size(); l++) {
          int col = getColumnPosition(key,list.get(l));
          if (col > BaseLogic.LAST_COL) {
            addVisibleColumn(key,list.get(l));
          }
        }
      }
    }
  }

 /**
  *Saves the Logic object.  First saves the visible columns hashmap, then the data hashmap
  * @param os
  * @throws IOException
  */
  public void save(ObjectOutputStream os) throws IOException {
    os.writeInt(version);
    saveVisibleColumnInfo(os);
    os.writeInt(data.size());
    for (String kind : data.keySet()) {
      os.writeObject(kind);
      saveData(kind,os);
    }
    os.flush();
  }

  /**
   * Passes in an object input stream to be read.  Sends to
   * @param in
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public void read(ObjectInputStream in)
    throws IOException, ClassNotFoundException
  {
    read(in,-1);
  }

  /**
   * Will
   * @param in
   * @param version
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public void read(ObjectInputStream in, int version)
    throws IOException, ClassNotFoundException
  {

    noChangeRead=true;
    Species.setNoChangeRead(true);

    if (version == -1) {
      version = in.readInt();
    }
    data.clear();

    {
      readVisibleColumnInfo(in);
    }
    {
      int size = in.readInt();
      for (int i = 0; i < size; i++) {
        readData((String) in.readObject(), in, version);
      }
    }

    noChangeRead = false;
  }

  /**
   *
   * @return
   */
  public static boolean isNoChangeRead() { return noChangeRead; }

}
