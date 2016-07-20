/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.io.*;
import java.util.ArrayList;

/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This is the import data location for the AbstractBaseLogic class.  
 * It is abstract and cannot be instantiated. 
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 * 
 * 
 * 
 */
public abstract class AbstractLogicData implements Externalizable {
  static final long serialVersionUID = -170886324118818854L;
  static final int  version          = 1;

  protected SystemKnowledge.Kinds sysKnowKind;
  public abstract AbstractLogicData duplicate();
  
  public void duplicate(AbstractLogicData logicData) {
    sysKnowKind = logicData.sysKnowKind;
  }
/**
 * Get the object by column Id.
 * @param col column Id
 * @return
 */
  public abstract Object getValueAt(int col);
/**
 * Set a parameter object in a column based on column Id.
 * @param col column Id
 * @param value object being set
 */
  public abstract void setValueAt(int col, Object value);
/**
 * Abstract method to get the object in list at a particular list index and column Id
 * @param listIndex the index into the list where the object is located
 * @param col column Id
 * @return the object to be returned
 */
  public abstract Object getListValueAt(int listIndex, int col);
  /**
   * Abstract method to adds an Object to a list at a particular column Id.
   * @param col column Id.
   * @param value the object to be added to the list
   */
  public abstract void addListValueAt(int col, Object value);
  /**
   *Abstract method to remove an object from a list based on column Id.
   * @param col the column Id.
   * @param value the object to be removed
   */
  public abstract void removeListValueAt(int col, Object value);
  /**
   * Abstract method to get the list row count of a particular column based on column Id.
   * @param col the column Id
   * @return the row count.
   */
  public abstract int getListRowCount(int col);
  /**
   * Abstract method to check whether list has a particular object in it.  
   * @param col column Id
   * @param value the object being checked
   * @return
   */
  public abstract boolean hasListValue(int col, Object value);
/**
 * Is match 
 * @return true
 */
  public boolean isMatch() { return true;}
/**
 * Abstract method for getting all the possible values at a column Id
 * @param col the column Id
 * @return arraylist of all possible values
 */
  public abstract ArrayList getPossibleValues(int col);
/**
 * Marks the system knowledge changed for a particular type of system knowledge.  
 */
  public void markChanged() {
    SystemKnowledge.markChanged(sysKnowKind);
  }
/**
 * Reads an external object from a source and the system knowledge kind is set with it.
 * Currently version is read in, but nothing is done with it.  Might be deprecated in V1.1
 * @throws IOException, ClassNotFoundException
 */
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    int version = in.readInt();

    SystemKnowledge.setKnowledgeSource(sysKnowKind,(String)in.readObject());

  }
  
  /**
   * Writes Logic Data to external source.  Writes version #, which is final in this class at 1 
   * @throws IOException
   */
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(version);

    String knowledge = SystemKnowledge.getKnowledgeSource(sysKnowKind);
    out.writeObject(knowledge);
  }
}
