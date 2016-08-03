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
 * AbstractLogicData is a template for a logic rule.
 */

public abstract class AbstractLogicData implements Externalizable {
  static final long serialVersionUID = -170886324118818854L;
  static final int  version = 1;

  /**
   * The type of system knowledge associated with this rule.
   */
  protected SystemKnowledge.Kinds sysKnowKind;

  /**
   * Returns a copy of this logic data.
   */
  public abstract AbstractLogicData duplicate();

  /**
   * Copies the system knowledge kind from this into the passed logic data.
   */
  public void duplicate(AbstractLogicData logicData) {
    sysKnowKind = logicData.sysKnowKind;
  }

  /**
   * Returns an object from the specified column index.
   *
   * @param col A column index
   */
  public abstract Object getValueAt(int col);

  /**
   * Assigns a value to a specified column index.
   *
   * @param col A column index
   * @param value An object
   */
  public abstract void setValueAt(int col, Object value);

  /**
   * Returns an element from a list from a specified column index.
   *
   * @param listIndex An element index
   * @param col A column index
   * @return An object
   */
  public abstract Object getListValueAt(int listIndex, int col);

  /**
   * Adds an element to a list at a specified column index.
   *
   * @param col A column index
   * @param value An object to add
   */
  public abstract void addListValueAt(int col, Object value);

  /**
   * Removes a value from a list at a specified column index.
   *
   * @param col A column index
   * @param value An object to remove
   */
  public abstract void removeListValueAt(int col, Object value);

  /**
   * Returns the number of elements in a list at a specified column index.
   *
   * @param col A column index
   * @return The total number of elements
   */
  public abstract int getListRowCount(int col);

  /**
   * Determines if a list at a specified column index contains a value.
   *
   * @param col A column index
   * @param value An object to search for
   * @return True if the object is in the list
   */
  public abstract boolean hasListValue(int col, Object value);

  /**
   * TODO: Look into removing this method
   *
   * @return True
   */
  public boolean isMatch() {
    return true;
  }

  /**
   * Returns an array of possible values for a specified column.
   *
   * @param col A column index
   * @return An array of legal values
   */
  public abstract ArrayList getPossibleValues(int col);

  /**
   * Flags a change to this rule's system knowledge type.
   */
  public void markChanged() {
    SystemKnowledge.markChanged(sysKnowKind);
  }

  /**
   * Reads an object and stores it as a system knowledge source.
   *
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    int version = in.readInt();
    SystemKnowledge.setKnowledgeSource(sysKnowKind,(String)in.readObject());
  }
  
  /**
   * Writes the system knowledge source for this rule's kind of system knowledge.
   *
   * @throws IOException
   */
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(version);
    String knowledge = SystemKnowledge.getKnowledgeSource(sysKnowKind);
    out.writeObject(knowledge);
  }
}
