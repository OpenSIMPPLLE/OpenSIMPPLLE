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
 * AbstractBaseLogic is a template for tabular logic data. It provides methods to query and
 * manipulate rows, columns, and cells. Multiple kinds of logic can be stored in an implementing
 * class, each in its own table. The logic collectively represents a kind of system knowledge.
 */

public abstract class AbstractBaseLogic {

  private static final int version = 3;

  /**
   * The kind of system knowledge associated with this logic.
   */
  protected SystemKnowledge.Kinds sysKnowKind;

  /**
   * Maps a kind of logic to a list of column names.
   */
  protected HashMap<String, ArrayList<String>> columns;

  /**
   * Maps a kind of logic to a list of visible column names.
   */
  protected HashMap<String, ArrayList<String>> visibleColumnsHm = new HashMap<>();

  /**
   * Maps a kind of logic to rows of logic data.
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
   * Creates a table for each kind of logic with a priority column.
   *
   * @param kinds An array of logic kinds
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
   * Adds a new column.
   *
   * @param kind A kind of logic
   * @param column The name of the new column
   */
  protected void addColumn(String kind, String column) {
    columns.get(kind).add(column);
  }

  /**
   * Returns a column count.
   *
   * @param kind A kind of logic
   * @return The number of columns
   */
  public int getColumnCount(String kind) {
    ArrayList<String> columns = visibleColumnsHm.get(kind);
    return columns != null ? columns.size() : 0;
  }

  /**
   * Returns the name of the column at columnIndex.
   *
   * @param kind A kind of logic
   * @param columnIndex A column index
   * @return The name of the column
   */
  public abstract String getColumnName(String kind, int columnIndex);

  /**
   * Returns the name of the first column.
   *
   * @param columnIndex A column index
   * @return "Priority" if the index is zero, otherwise an empty string
   */
  public static String getColumnName(int columnIndex) {
    switch (columnIndex) {
      case ROW_COL:
        return "Priority";
      default:
        return "";
    }
  }

  /**
   * Returns the index of the column named columnName.
   *
   * @param kind A kind of logic
   * @param columnName A column name
   * @return The index of the column, or -1 if it does not exist
   */
  public int getColumnPosition(String kind, String columnName) {
    ArrayList<String> columnNames = columns.get(kind);
    if (columnNames != null) {
      return columnNames.indexOf(columnName);
    } else {
      return -1;
    }
  }

  /**
   * Returns the name of the column at columnIndex.
   *
   * @param kind A kind of logic
   * @param columnIndex A column index
   * @return The name of the column, or an empty string if it does not exist
   */
  public String getColumnIdName(String kind, int columnIndex) {
    ArrayList<String> columnNames = columns.get(kind);
    if (columnNames != null) {
      return columnNames.get(columnIndex);
    } else {
      return "";
    }
  }

  /**
   * Returns the index of the column named columnName.
   *
   * @param columnName A column name
   * @return The index of the column
   */
  public int getColumnNumFromName(String columnName) {
    if (columnName.equalsIgnoreCase("Priority")) {
      return ROW_COL;
    }
    return 0;
  }

  /**
   * Marks all columns as visible.
   *
   * @param kind A kind of logic
   */
  public void addVisibleColumnAll(String kind) {
    for (int i = 0; i < columns.get(kind).size(); i++) {
      addVisibleColumn(kind,i);
    }
  }

  /**
   * Marks a column as visible.
   *
   * @param kind A kind of logic
   * @param columnName The name of the column to mark as visible
   */
  public void addVisibleColumn(String kind, String columnName) {
    addVisibleColumn(kind,getColumnPosition(kind,columnName));
  }

  /**
   * Marks a column as visible.
   *
   * @param kind A kind of logic
   * @param columnIndex The index of the column to mark as visible
   */
  public void addVisibleColumn(String kind, int columnIndex) {
    ArrayList<String> visibleColumns = visibleColumnsHm.get(kind);
    if (visibleColumns == null) {
      visibleColumns = new ArrayList<>();
      visibleColumnsHm.put(kind,visibleColumns);
    }
    String columnName = getColumnIdName(kind,columnIndex);
    if (!visibleColumns.contains(columnName)) {
      int insertPos = visibleColumns.size();
      for (int i = 0; i < visibleColumns.size(); i++) {
        int listCol = getColumnPosition(kind,visibleColumns.get(i));
        if (listCol > columnIndex) {
          insertPos = i;
          break;
        }
      }
      visibleColumns.add(insertPos,columnName);
      SystemKnowledge.markChanged(sysKnowKind);
    }
  }

  /**
   * Hides a visible column.
   *
   * @param kind A kind of logic
   * @param columnIndex The index of a column to hide
   */
  public void removeVisibleColumn(String kind, int columnIndex) {
    String columnName = getColumnIdName(kind,columnIndex);
    ArrayList<String> visibleColumns = visibleColumnsHm.get(kind);
    if (visibleColumns != null) {
      if (visibleColumns.contains(columnName)) {
        visibleColumns.remove(columnName);
        SystemKnowledge.markChanged(sysKnowKind);
      }
    }
  }

  /**
   * Returns an array of visible column indices.
   *
   * @param kind A kind of logic
   * @return Indices of visible columns
   */
  public int[] getVisibleColumns(String kind) {
     ArrayList<String> visibleColumns = visibleColumnsHm.get(kind);
     int[] indices = new int[visibleColumns.size()];
     for (int i = 0; i < indices.length; i++) {
       indices[i] = getColumnPosition(kind,visibleColumns.get(i));
     }
     return indices;
  }

  /**
   * Returns the name of a visible column.
   *
   * @param kind A kind of logic
   * @param columnIndex The index of the visible column
   * @return The name of the visible column
   */
  public String getVisibleColumnName(String kind, int columnIndex) {
    return visibleColumnsHm.get(kind).get(columnIndex);
  }

  /**
   * Determines if a column is visible.
   *
   * @param kind A kind of logic
   * @param col The index of the column to check
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
   * Creates a row of logic at insertPos.
   *
   * @param insertPos The index of the new row
   * @param kind A kind of logic
   */
  public abstract void addRow(int insertPos, String kind);

  /**
   * Appends a row of logic.
   *
   * @param kind A kind of logic
   * @param logicData The data to be appended
   */
  public void addRow(String kind, AbstractLogicData logicData) {
    addRow(-1,kind,logicData);
  }

  /**
   * Inserts a row of logic.
   *
   * @param insertPos The index of the new row
   * @param kind A kind of logic
   * @param logicData The data to be added
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
   * Removes a row of logic.
   *
   * @param row The index of the row to remove
   * @param kind A kind of logic
   */
  public void removeRow(int row, String kind) {
    getData(kind).remove(row);
  }

  /**
   * Removes a row of logic and flags a change.
   *
   * @param row The index of the row to remove
   * @param kind A kind of logic
   */
  public void deleteDataRow(int row, String kind) {
    getData(kind).remove(row);
    markChanged();
  }

  /**
   * Duplicates a row of logic.
   *
   * @param row The index of the row to duplicate
   * @param insertPos The index of the new row
   * @param kind A kind of logic
   */
  public abstract void duplicateRow(int row, int insertPos, String kind);

  /**
   * Decrements the index of a row.
   *
   * @param row The index of the row to move
   * @param kind A kind of logic
   * @return The new row index
   */
  public int moveRowUp(int row, String kind) {
    AbstractLogicData logicData = getValueAt(row,kind);
    removeRow(row,kind);
    int newRow = row - 1;
    if (newRow < 0) {
      newRow = getData(kind).size();
    }
    getData(kind).add(newRow,logicData);
    return newRow;
  }

  /**
   * Increments the index of a row.
   *
   * @param row The index of the row to move
   * @param kind A kind of logic
   * @return The new row index
   */
  public int moveRowDown(int row, String kind) {
    AbstractLogicData logicData = getValueAt(row,kind);
    removeRow(row,kind);
    int newRow = row + 1;
    if (newRow > getData(kind).size()) {
      newRow = 0;
    }
    getData(kind).add(newRow,logicData);
    return newRow;
  }

  /**
   * Returns the index of the last row.
   *
   * @param kind A kind of logic
   * @return The index of the last row
   */
  public int getLastRowIndex(String kind) {
    return getData(kind).size() - 1;
  }


  /**
   * Returns a row count.
   *
   * @param kind A kind of logic
   * @return The number of rows of logic
   */
  public int getRowCount(String kind) {
    return isDataPresent(kind) ? getData(kind).size() : 0;
  }

  /**
   * Assigns a value to a cell and flags a change.
   *
   * @param value A cell value
   * @param row A row index
   * @param col A column index
   * @param kind A kind of logic
   */
  public void setData(Object value, int row, int col, String kind) {
    getData(kind).get(row).setValueAt(col,value);
    markChanged();
  }

  /**
   * Returns all of the rows from a kind of logic.
   *
   * @param kind A kind of logic
   * @return A list of of row data
   */
  protected ArrayList<AbstractLogicData> getData(String kind) {
    return data.get(kind);
  }

  /**
   * Returns all of the rows from a kind of logic or returns an empty array if none exist.
   *
   * @param kind A kind of logic
   * @param addIfNull A flag indicating if an empty list should be returned if there isn't data
   * @return A list of row data
   */
  protected ArrayList<AbstractLogicData> getData(String kind, boolean addIfNull) {
    if (addIfNull && getData(kind) == null) {
      data.put(kind,new ArrayList<>());
    }
    return getData(kind);
  }

  /**
   * Returns the value of a cell.
   *
   * @param row The row index of the cell
   * @param col The column index of the cell
   * @param kind A kind of logic
   * @return The value in a cell, or null if no rows exist
   */
  public Object getValueAt(int row, int col, String kind) {
    if (isDataPresent(kind)) {
      return getValueAt(row, kind).getValueAt(col);
    }
    return null;
  }

  /**
   * Returns a row of data.
   *
   * @param row The index of the row
   * @param kind A kind of logic
   * @return A row of data, or null if there are not rows
   */
  public AbstractLogicData getValueAt(int row, String kind) {
    if (isDataPresent(kind)) {
      return getData(kind).get(row);
    }
    return null;
  }

  /**
   * Removes all of the rows from a kind of logic.
   *
   * @param kind A kind of logic
   */
  public void clearData(String kind) {
    if (isDataPresent(kind)) {
      getData(kind).clear();
    }
  }

  /**
   * Returns true if there is at least one row of data for a kind of logic.
   *
   * @param kind A kind of logic
   * @return True if there is at least one row
   */
  public boolean isDataPresent(String kind) {
    return (getData(kind) != null && getData(kind).size() > 0);
  }

  /**
   * Flags a change to this kind of system knowledge.
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
   * @param kind A kind of logic
   * @param os An object output stream
   * @throws IOException
   */
  protected void saveData(String kind, ObjectOutputStream os) throws IOException {
    saveData(kind,os,false);
  }

  /**
   *
   *
   * @param kind A kind of logic
   * @param os An object output stream
   * @param includeVisibleCol Flag indicating if visible columns should be saved
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
   * @param kind A kind of logic
   * @param in An object input stream
   * @param version The version number of the object being read
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
   * @param kind A kind of logic
   * @param in An object input stream
   * @param version The version number of the object being read
   * @param includeVisibleCol Flag indicating if visible columns should be read
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
      data.put(kind, new ArrayList<>(list));
    } else {
      if (includeVisibleCol) {
        readVisibleColumnInfo(in);
      }
      data.put(kind, (ArrayList<AbstractLogicData>) in.readObject());
    }
  }

  /**
   * Saves the visible columns for each kind of logic to an object output stream.
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
   * Reads visible columns from an object input stream.
   *
   * @param in An object input stream
   * @throws IOException
   * @throws ClassNotFoundException
   */
  private void readVisibleColumnInfo(ObjectInputStream in) throws IOException, ClassNotFoundException {
    int numKinds = in.readInt();
    for (int i = 0; i < numKinds; i++) {
      String kind = (String) in.readObject();
      if (!columns.containsKey(kind)) {
        SystemKnowledge.addUnhandledErrorMessage(kind + " is not a legal process and has been ignored");
      }
      visibleColumnsHm.put(kind, (ArrayList<String>) in.readObject());
      ArrayList<String> columnNames = visibleColumnsHm.get(kind);
      if (columnNames != null) {
        for (String name : columnNames) {
          int index = getColumnPosition(kind,name);
          if (index > BaseLogic.LAST_COL) {
            addVisibleColumn(kind, name);
          }
        }
      }
    }
  }

 /**
  * Saves visible column names and column data for each kind of logic.
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
   * Reads visible column names and column data for each kind of logic. The version is read from
   * the object input stream.
   *
   * @param in An object input stream
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public void read(ObjectInputStream in) throws IOException, ClassNotFoundException {
    read(in,-1);
  }

  /**
   * Reads visible column names and column data for each kind of logic. If version equals -1,
   * then the version is read from the object input stream.
   *
   * @param in An object input stream
   * @param version A version number
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public void read(ObjectInputStream in, int version) throws IOException, ClassNotFoundException {

    noChangeRead = true;
    Species.setNoChangeRead(true);

    if (version == -1) {
      version = in.readInt();
    }

    data.clear();

    readVisibleColumnInfo(in);

    int size = in.readInt();
    for (int i = 0; i < size; i++) {
      readData((String) in.readObject(), in, version);
    }

    noChangeRead = false;

  }

  /**
   *
   */
  public static boolean isNoChangeRead() { return noChangeRead; }

}
