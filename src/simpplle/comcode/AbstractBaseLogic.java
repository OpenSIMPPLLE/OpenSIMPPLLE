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
 * AbstractBaseLogic is a template for a type of system knowledge. Each instance contains a
 * collection of logic tables. Each logic table controls one aspect of that knowledge.
 */

public abstract class AbstractBaseLogic {

  private static final int version = 3;

  /**
   * The type of system knowledge associated with these logic tables.
   */
  protected SystemKnowledge.Kinds sysKnowKind;

  /**
   * Maps a logic table name to a list of column names. The property name is misleading.
   */
  protected HashMap<String, ArrayList<String>> columns;

  /**
   * Maps a logic table name to a list of visible column names.
   */
  protected HashMap<String, ArrayList<String>> visibleColumnsHm = new HashMap<>();

  /**
   * Maps a logic table name to columns of logic data.
   */
  public HashMap<String, ArrayList<AbstractLogicData>> data = new HashMap<>();

  /**
   * The index of the first column.
   */
  public static final int ROW_COL = 0;

  /**
   * The index of the last column.
   */
  protected static final int LAST_COL = ROW_COL;

  /**
   *
   */
  private static boolean noChangeRead = false;

  /**
   * Creates a logic table for each system knowledge kind with a matching process and adds a
   * row index column.
   *
   * @param kinds An array of logic table names
   */
  protected AbstractBaseLogic(String[] kinds) {
    visibleColumnsHm.clear(); //
    columns = new HashMap<>();
    for (int i=0; i<kinds.length; i++) {
      Process process = Process.findInstance(kinds[i]);
      if (process != null && process.isUniqueUI()) continue;
      columns.put(kinds[i],new ArrayList<>());
      addColumn(kinds[i],"ROW_COL");
    }
  }

  /**
   * Adds a new column to a logic table.
   *
   * @param kind A logic table name
   * @param column The name of the new column
   */
  protected void addColumn(String kind, String column) {
    columns.get(kind).add(column);
  }

  /**
   * Returns the number of columns in a logic table.
   *
   * @param kind A logic table name
   * @return The number of columns
   */
  public int getColumnCount(String kind) {
    ArrayList<String> columns = visibleColumnsHm.get(kind);
    return columns != null ? columns.size() : 0;
  }

  /**
   * Returns the name of a column in a logic table at a specified index.
   *
   * @param col A column index
   * @return A column name
   */
  public abstract String getColumnName(String kind, int col);

  /**
   * Returns the name of the first column.
   *
   * @param col A column index
   * @return "Priority" if the index is zero, otherwise an empty string
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
   * @param kind A logic table name
   * @param colIdName A column name
   * @return The index of the column if it exists
   */
  public int getColumnPosition(String kind,String colIdName) {
    return columns.get(kind).indexOf(colIdName);
  }

  /**
   * Returns the name of a column for a kind of system knowledge given the column's index.
   *
   * @param kind A logic table name
   * @param col A column index
   * @return The name of the column if it exists
   */
  public String getColumnIdName(String kind,int col) {
    return columns.get(kind).get(col);
  }

  /**
   * Returns the index of the first column, 'Priority'.
   *
   * @param name A column name
   * @return A column index
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
   * @param kind A logic table name
   */
  public void addVisibleColumnAll(String kind) {
    for (int i=0; i<columns.get(kind).size(); i++) {
      addVisibleColumn(kind,i);
    }
  }

  /**
   *
   *
   * @param kind A logic table name
   * @param name
   */
  public void addVisibleColumn(String kind, String name) {
    int col = getColumnPosition(kind,name);
    addVisibleColumn(kind,col,name);
  }

  /**
   *
   *
   * @param kind A logic table name
   * @param col
   */
  public void addVisibleColumn(String kind, int col) {
    String name = getColumnIdName(kind,col);
    addVisibleColumn(kind,col,name);
  }

  /**
   *
   *
   * @param kind A logic table name
   * @param col
   * @param name A column name
   */
  public void addVisibleColumn(String kind, int col, String name) {
    ArrayList<String> values = visibleColumnsHm.get(kind);
    if (values == null) {
      values = new ArrayList<>();
      visibleColumnsHm.put(kind,values);
    }
    if (!values.contains(name)) {
      int insertPos = findVisibleColumnInsertPosition(kind,col,values);
      values.add(insertPos,name);
      SystemKnowledge.markChanged(sysKnowKind);
    }
  }

  /**
   * Hides a visible column and flags a change.
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
   * Returns a map of logic table names to names of visible columns.
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
   * @param kind A logic table name
   * @param col
   * @param values An array of column names
   * @return A new column index
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
   * Adds a row to a logic table.
   *
   * @param insertPos The index of the new row
   * @param kind A logic table name
   */
  public abstract void addRow(int insertPos, String kind);

  /**
   * Adds a row to a logic table.
   *
   * @param kind A logic table name
   * @param logicData A row of data
   */
  public void addRow(String kind, AbstractLogicData logicData) {
    addRow(-1,kind,logicData);
  }

  /**
   * Adds a row to a logic table.
   *
   * @param insertPos The index of the new row
   * @param kind A logic table name
   * @param logicData A row of data
   */
  public void addRow(int insertPos, String kind, AbstractLogicData logicData) {
    int size = getData(kind,true).size();
    if (insertPos > size || insertPos == -1) {
      getData(kind,true).add(logicData);
    } else {
      getData(kind,true).add(insertPos,logicData);
    }
  }

  /**
   * Removes a row from a logic table.
   *
   * @param row A row index
   * @param kind A logic table name
   */
  public void removeRow(int row, String kind) {
    getData(kind).remove(row);
  }

  /**
   * Removes a row from a logic table and flags a change.
   *
   * @param row A row index
   * @param kind A logic table name
   */
  public void deleteDataRow(int row, String kind) {
    getData(kind).remove(row);
    markChanged();
  }

  /**
   * Duplicates a row in a logic table.
   *
   * @param row A row index
   * @param insertPos The index of the new row
   * @param kind A logic table name
   */
  public abstract void duplicateRow(int row, int insertPos, String kind);

  /**
   * Moves a row up in a logic table.
   *
   * @param row The index of the row to move
   * @param kind A logic table name
   * @return The new row index
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
   * Moves a row down in a logic table.
   *
   * @param row The index of the row to move
   * @param kind A logic table name
   * @return The new row index
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
   * Returns the index of the last row in a logic table.
   *
   * @param kind A logic table name
   * @return A row index
   */
  public int getLastRowIndex(String kind) {
    return getData(kind).size() - 1;
  }


  /**
   * Returns the number of rows in a logic table.
   *
   * @param kind A logic table name
   * @return A row count
   */
  public int getRowCount(String kind) {
    return isDataPresent(kind) ? getData(kind).size() : 0;
  }

  /**
   * Assigns a value to a cell in a logic table and flags a change.
   *
   * @param value A cell value
   * @param row A row index
   * @param col A column index
   * @param kind A logic table name
   */
  public void setData(Object value, int row, int col, String kind) {
    getData(kind).get(row).setValueAt(col,value);
    markChanged();
  }

  /**
   * Returns the data for all columns in a logic table.
   *
   * @param kind A logic table name
   * @return An array containing all logic data
   */
  protected ArrayList<AbstractLogicData> getData(String kind) {
    return data.get(kind);
  }

  /**
   * Returns the data for all columns in a logic table. If no data exists, an empty list is returned.
   *
   * @param kind A logic table name
   * @param addIfNull A flag indicating if an empty list should be returned if there isn't data
   * @return An array containing all logic data
   */
  protected ArrayList<AbstractLogicData> getData(String kind, boolean addIfNull) {
    if (addIfNull && getData(kind) == null) {
      data.put(kind,new ArrayList<>());
    }
    return getData(kind);
  }

  /**
   * Returns the value of a cell in a logic table.
   *
   * @param row A row index
   * @param col A column index
   * @param kind A logic table name
   * @return The value in a cell, or null if no data exists
   */
  public Object getValueAt(int row, int col, String kind) {
    if (isDataPresent(kind)) {
      return getValueAt(row, kind).getValueAt(col);
    }
    return null;
  }

  /**
   * Returns a row from a logic table.
   *
   * @param row A row index
   * @param kind A logic table name
   * @return Logic data for a single row, or null if no data exists
   */
  public AbstractLogicData getValueAt(int row, String kind) {
    if (isDataPresent(kind)) {
      return getData(kind).get(row);
    }
    return null;
  }

  /**
   * Clears data from a logic table.
   *
   * @param kind A logic table name
   */
  public void clearData(String kind) {
    if (isDataPresent(kind)) {
      getData(kind).clear();
    }
  }

  /**
   * Determines if data exists for a logic table.
   *
   * @param kind A logic table name
   * @return True if data exists
   */
  public boolean isDataPresent(String kind) {
    return (getData(kind) != null && getData(kind).size() > 0);
  }

  /**
   * Flags a change to this collection of tables.
   */
  public void markChanged() {
    SystemKnowledge.markChanged(sysKnowKind);
  }

  /**
   * Returns true if a logic table has changed or contains user data.
   */
  public boolean hasChanged() {
    return SystemKnowledge.hasChangedOrUserData(sysKnowKind);
  }

  /**
   *
   *
   * @param kind
   * @param os
   * @throws IOException
   */
  protected void saveData(String kind, ObjectOutputStream os) throws IOException {
    saveData(kind,os,false);
  }

  /**
   *
   *
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
   * Saves the visible columns in this logic table to an object output stream.
   *
   * @param os An object output stream
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
   * Reads visible columns into this logic table from an object input stream.
   *
   * @param in An object input stream
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
  * Saves this logic table to an object output stream.
  *
  * @param os An object output stream
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
   *
   *
   * @param in An object input stream
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public void read(ObjectInputStream in)
    throws IOException, ClassNotFoundException
  {
    read(in,-1);
  }

  /**
   *
   *
   * @param in An object input stream
   * @param version A version number
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
   */
  public static boolean isNoChangeRead() { return noChangeRead; }

}
