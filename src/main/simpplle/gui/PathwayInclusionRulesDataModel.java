/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import javax.swing.table.AbstractTableModel;
import simpplle.comcode.VegetativeType;
import java.util.ArrayList;
import simpplle.comcode.InclusionRuleSpecies;

/** 
 * This class creates a  PathwayInclusionRules Data Model, an extension of the Abstract table model.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class PathwayInclusionRulesDataModel extends AbstractTableModel {
	/**
	 * Constructor for Pathway Inclusion Rules Data Model.  Doesn't initialize anything.  
	 */
  public PathwayInclusionRulesDataModel() {
  }
  VegetativeType vt;
  ArrayList data;
/**
 * Sets the vegetative type for this data model, and creates an inclusion rules array. 
 * @param vt
 */
  public void setData(VegetativeType vt) {
    this.vt = vt;
    this.data = vt.makeInclusionRulesArray();
  }
/**
 * Gets the column count of inclusion rules - this is a static variable = 2. 
 */
  public int getColumnCount() {
    return VegetativeType.getInclusionRulesColumnCount();
  }
/**
 * Gets row count by using the inclusion rules arraylist size method.  
 */
  public int getRowCount() {
    return (data != null) ? data.size() : 1;
  }
/**
 * Gets the object at a particular cell found using parameter row and column indexes.  
 */
  public Object getValueAt(int rowIndex, int columnIndex) {
    if (data == null) { return ""; }

    Object[] colData = (Object[])data.get(rowIndex);
    return colData[columnIndex];
  }
  /**
   * Sets the value at a particular cell.  Inclusion rule species include species name, lower range percent at which the plant community identified as this state, 
   * and upper percent range at which plant community will be identified in this state (this is often 100 - meaning entire plant community is taken over 
   * by this species).
   */
  public void setValueAt(Object value, int row, int col) {
    if (col == 0 || value == null) { return; }

    Object[] colData = (Object[])data.get(row);
    InclusionRuleSpecies species = (InclusionRuleSpecies)colData[0];
    colData[col] = (Integer)value;

    int lower = (Integer)colData[1];
    int upper = (Integer)colData[2];

    if (col == 1 && (lower > upper)) { lower = upper; colData[1] = lower;}
    if (col == 2 && (upper < lower)) { upper = lower; colData[2] = upper;}

    vt.setInclusionRule(species,lower,upper);
    fireTableCellUpdated(row,col);
 }
/**
 * Gets the class of an object by column Id.  IF row is greater than 0 will return the column id's will get an object 
 * at 0 = inclusion rule species (keyed by species name), 1 will be an Integer representing the 
 * lower range, 2 will be an Integer representing upper range.  
 * if Row is 0 that means there are no inclusion rule species and will just be the object type returned - either InclusionRuleSpecies, Integer, or Integer.
 */
  public Class getColumnClass(int c) {
    Object o = getValueAt(0,c);
    if (o == null) {
      switch (c) {
        case 0: return InclusionRuleSpecies.class;
        case 1: return Integer.class;
        case 2: return Integer.class;
        default: return String.class;
      }
    }
    else {
      return getValueAt(0, c).getClass();
    }
  }
/**
 * IF column Id is greater than 0, cell is editable.  
 */
  public boolean isCellEditable(int row, int col) {
    return (col > 0);
  }
/**
 * Gets the name of the inclusion rule by column Id.  Choices for this are "Inclusion Rule Species",  "Lower",  "Upper" or null.  
 */
  public String getColumnName(int column) {
    String result = VegetativeType.getInclusionRulesColumnName(column);
    return ((result == null) ? super.getColumnName(column) : result);
  }
/**
 * Add a row representing an inclusion rule for a particular species.  Then notify all the tables listeners that the table has changed.  
 * @param species
 */
  public void addRow(InclusionRuleSpecies species) {
    vt.addInclusionRule(species);
    data = vt.makeInclusionRulesArray();
    fireTableDataChanged();
  }
  /**
   * Delete a row.  Then notify all the tables listeners that the table has changed.    
   * @param row
   */
  public void deleteRow(int row) {
    Object[] colData = (Object[])data.get(row);
    vt.removeInclusionRule((InclusionRuleSpecies)colData[0]);
    data = vt.makeInclusionRulesArray();
    fireTableDataChanged();
  }
  /**
   * Deletes rows from the inclusion rules data model.  colData [0] is the index with species name.  This will delete an inclusion rule from the 
   * speciesRange hashmap which is keyed by species.  Then notifies all the table listeners that the table has changed.  
   * @param rows
   */
  public void deleteRows(int[] rows) {
    for (int row : rows) {
      Object[] colData = (Object[])data.get(row);
      vt.removeInclusionRule((InclusionRuleSpecies)colData[0]);
    }
    data = vt.makeInclusionRulesArray();
    fireTableDataChanged();
  }
}

