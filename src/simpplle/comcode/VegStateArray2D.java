/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.util.*;
import java.io.PrintWriter;
import java.io.*;

/**
 *
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */
public class VegStateArray2D implements Externalizable {
  static final long serialVersionUID = 9009088630929100996L;
  static final int  version          = 1;

  private int nSteps;
  private int nRuns;
  private Object[] vegTypeData;
  private Object[] processData;
  private Object[] probData;
  private boolean disableMultipleLifeforms=true;

  public VegStateArray2D(int nRuns, int nSteps) {
    this.nSteps = nSteps;
    this.nRuns  = nRuns;
    vegTypeData = new Object[nRuns*nSteps];
    processData = new Object[nRuns*nSteps];
    probData    = new Object[nRuns*nSteps];
  }
  public VegStateArray2D(int nRuns, int nSteps, boolean noProbability) {
    this.nSteps = nSteps;
    this.nRuns  = nRuns;
    vegTypeData = new Object[nRuns*nSteps];
    processData = new Object[nRuns*nSteps];
    if (noProbability) {
      probData = new Object[0];
    }
    else {
      probData = new Object[nRuns*nSteps];
    }
  }

  public int getNumRuns() { return nRuns; }
  public int getNumTimeSteps() { return nSteps; }

  public VegetativeType findLatestVegType() {
    for (int i=vegTypeData.length-1; i>=0; i--) {
      Object vt = vegTypeData[i];
      if (vt == null) { continue; }

      if (vt instanceof VegetativeType) {
        return (VegetativeType)vt;
      }
      else if (vt instanceof VegetativeType[]) {
        return findDominantLife((VegetativeType[])vt);
      }
    }
    return null;
  }
  public ProcessType findLatestProcessType() {
    for (int i=processData.length-1; i>=0; i--) {
      Object value = processData[i];
      if (value == null) { continue; }

      if (value instanceof ProcessType) {
        return (ProcessType)value;
      }
      else if (value instanceof ProcessType[]) {
        Object vt = vegTypeData[i];
        int index = findDominantLifeIndex((VegetativeType[])vt);
        return ((ProcessType[])value)[index];
      }
    }
    return null;
  }
  public short findLatestProbability() {
    for (int i=probData.length-1; i>=0; i--) {
      Object value = probData[i];
      if (value == null) { continue; }

      if (value instanceof Short) {
        return ((Short)value).shortValue();
      }
      else if (value instanceof Short[]) {
        Object vt = vegTypeData[i];
        int index = findDominantLifeIndex((VegetativeType[])vt);
        Short[] tmpData = (Short[])value;
        return tmpData[index].shortValue();
      }
    }
    return (short)Evu.NOPROB;
  }
  /**
   *
   * @param run Should be >= 1
   * @param tStep value should be >= 0
   * @return
   */
  public VegetativeType getVegType(int tStep) {
    return getVegType(1,tStep);
  }
  public VegetativeType getVegType(int run, int tStep) {
    // Need to subtract 1 from run.
    Object value = vegTypeData[((run-1)*nSteps) + tStep];

    if (value instanceof VegetativeType) {
      return (VegetativeType)value;
    }
    else if (value instanceof VegetativeType[]) {
      return findDominantLife((VegetativeType[])value);
    }
    else {
      return null;
    }
  }
  public VegetativeType getVegType(int run, int tStep, Lifeform kind) {
    Object value = vegTypeData[((run-1)*nSteps) + tStep];

    if ((value instanceof VegetativeType) &&
        ((VegetativeType)value).getSpecies().getLifeform() == kind) {
      return (VegetativeType)value;
    }
    else if (value instanceof VegetativeType[]) {
      return findLifeform((VegetativeType[])value,kind);
    }
    else {
      return null;
    }
  }

  public ProcessType getProcessType(int tStep) {
    return getProcessType(1,tStep);
  }
  public ProcessType getProcessType(int run, int tStep) {
    // Need to subtract 1 from run.
    Object value = processData[((run-1)*nSteps) + tStep];

    if (value instanceof ProcessType) {
      return (ProcessType)value;
    }
    else if (value instanceof ProcessType[]) {
      Object vt = vegTypeData[((run-1)*nSteps) + tStep];
      int index = findDominantLifeIndex((VegetativeType[])vt);
      return ((ProcessType[])value)[index];
    }
    else {
      return null;
    }
  }
  public ProcessType getProcessType(int run, int tStep, Lifeform kind) {
    Object value = processData[((run-1)*nSteps) + tStep];

    if (value instanceof ProcessType) {
      return (ProcessType)value;
    }
    else if (value instanceof ProcessType[]) {
      Object vt = vegTypeData[((run-1)*nSteps) + tStep];
      int index = findLifeformIndex((VegetativeType[])vt,kind);
      return ((ProcessType[])value)[index];
    }
    else {
      return null;
    }
  }

  public short getProbability(int tStep) {
    return getProbability(1,tStep);
  }
  public short getProbability(int run, int tStep) {
    // Need to subtract 1 from run.
    Object value = probData[((run-1)*nSteps) + tStep];

    if (value instanceof Short) {
      return ((Short)value).shortValue();
    }
    else if (value instanceof Short[]) {
      Object vt = vegTypeData[((run-1)*nSteps) + tStep];
      int index = findDominantLifeIndex((VegetativeType[])vt);
      Short[] tmpData = (Short[])value;
      return tmpData[index].shortValue();
    }
    else {
      return (short)Evu.NOPROB;
    }
  }
  public short getProbability(int run, int tStep, Lifeform kind) {
    Object value = probData[((run-1)*nSteps) + tStep];

    if (value instanceof Short) {
      return ((Short)value).shortValue();
    }
    else if (value instanceof Short[]) {
      Object vt = vegTypeData[((run-1)*nSteps) + tStep];
      int index = findLifeformIndex((VegetativeType[])vt,kind);
      Short[] tmpData = (Short[])value;
      return tmpData[index].shortValue();
    }
    else {
      return (short)Evu.NOPROB;
    }
  }

  public void setVegetativeType(int tStep, VegetativeType value) {
    setVegetativeType(1,tStep,value);
  }
  public void setVegetativeType(int run, int tStep, VegetativeType value) {
    vegTypeData[((run-1)*nSteps) + tStep] = value;
  }
  public void addVegetativeType(int tStep, VegetativeType value) {
    addVegetativeType(1,tStep,value);
  }
  public void addVegetativeType(int run, int tStep, VegetativeType value) {
    if (disableMultipleLifeforms) {
      setVegetativeType(run,tStep,value);
      return;
    }
    int index = ((run-1)*nSteps) + tStep;
    Object currentValue = vegTypeData[index];
    if (currentValue == null) {
      vegTypeData[index] = value;
    }
    else if (currentValue instanceof VegetativeType) {
      VegetativeType[] newData = new VegetativeType[2];
      newData[0] = (VegetativeType)currentValue;
      newData[1] = (VegetativeType)value;
      vegTypeData[index] = newData;
    }
    else if (currentValue instanceof VegetativeType[]) {
      VegetativeType[] oldData = (VegetativeType[])currentValue;
      VegetativeType[] newData = new VegetativeType[oldData.length+1];
      for (int i=0; i<oldData.length; i++) { newData[i] = oldData[i]; }
      newData[newData.length-1] = (VegetativeType)value;
      vegTypeData[index] = newData;
    }
  }
  public void updateVegetativeType(int tStep, VegetativeType value) {
    updateVegetativeType(1,tStep,value);
  }
  public void updateVegetativeType(int run, int tStep, VegetativeType value) {
    if (disableMultipleLifeforms) {
      setVegetativeType(run,tStep,value);
      return;
    }
    int index = ((run-1)*nSteps) + tStep;
    Object currentValue = vegTypeData[index];
    if (currentValue == null || (currentValue instanceof VegetativeType)) {
      vegTypeData[index] = value;
    }
    else if (currentValue instanceof VegetativeType[]) {
      VegetativeType[] values = (VegetativeType[])currentValue;
      for (int i=0; i<values.length; i++) {
        if (values[i].getSpecies().getLifeform() == value.getSpecies().getLifeform()) {
          values[i] = value;
          return;
        }
      }
      addVegetativeType(run,tStep,value);
    }
  }

  public void setProcessType(int tStep, ProcessType value) {
    setProcessType(1,tStep,value);
  }
  public void setProcessType(int run, int tStep, ProcessType value) {
    processData[((run-1)*nSteps) + tStep] = value;
  }
  public void addProcessType(int run, int tStep, ProcessType value) {
    if (disableMultipleLifeforms) {
      setProcessType(run,tStep,value);
      return;
    }
    int index = ((run-1)*nSteps) + tStep;
    Object currentValue = processData[index];
    if (currentValue == null) {
      processData[index] = value;
    }
    else if (currentValue instanceof ProcessType) {
      ProcessType[] newData = new ProcessType[2];
      newData[0] = (ProcessType)currentValue;
      newData[1] = (ProcessType)value;
      processData[index] = newData;
    }
    else if (currentValue instanceof ProcessType[]) {
      ProcessType[] oldData = (ProcessType[])currentValue;
      ProcessType[] newData = new ProcessType[oldData.length+1];
      for (int i=0; i<oldData.length; i++) { newData[i] = oldData[i]; }
      newData[newData.length-1] = (ProcessType)value;
      processData[index] = newData;
    }
  }

  public void setProbability(int run, int tStep, Short value) {
    if (value == null) {
      probData[((run-1)*nSteps) + tStep] = null;
    }
    else {
      setProbability(run,tStep,value.shortValue());
    }
  }
  public void setProbability(int tStep, Short value) {
    setProbability(1,tStep,value);
  }
  public void setProbability(int tStep, short value) {
    setProbability(1,tStep,value);
  }
  public void setProbability(int run, int tStep, short value) {
    probData[((run-1)*nSteps) + tStep] = ProbCache.get(value);
  }
  public void addProbability(int run, int tStep, short value) {
    if (disableMultipleLifeforms) {
      setProbability(run,tStep,value);
      return;
    }
    int index = ((run-1)*nSteps) + tStep;
    Object currentValue = probData[index];
    if (currentValue == null) {
      probData[index] = ProbCache.get(value);
    }
    else if (currentValue instanceof Short) {
      Short[] newData = new Short[2];
      newData[0] = (Short)currentValue;
      newData[1] = (Short)ProbCache.get(value);
      probData[index] = newData;
    }
    else if (currentValue instanceof Short[]) {
      Short[] oldData = (Short[])currentValue;
      Short[] newData = new Short[oldData.length+1];
      for (int i=0; i<oldData.length; i++) { newData[i] = oldData[i]; }
      newData[newData.length-1] = (Short)ProbCache.get(value);
      probData[index] = newData;
    }
  }

  public void printAll(PrintWriter pout) {
    pout.println("  Vegetative Types:");
    pout.print("    (");
    int count = 0;
    for (int i=0; i<vegTypeData.length; i++) {
      if (i != 0 && (count % nSteps) == 0) {
        pout.print(") (");
        count = 0;
      }
      else if (i != 0) { pout.print(", "); }
      pout.print(vegTypeData[i]);
      count++;
    }
    pout.println(")");

    if (probData.length > 0) {
      pout.println("  Probabilities   :");
      pout.println("    (");
    }
    count = 0;
    for(int i=0;i<probData.length; i++) {
      if (i != 0 && (count % nSteps) == 0) {
        pout.print(") (");
        count = 0;
      }
      else if (i != 0) {
        pout.print(", ");
      }

      String str;
      if (probData[i] instanceof Short) {
        str = Evu.getProbabilityPrintString(((Short) probData[i]).shortValue());
        pout.print(str);
      }
      else if (probData[i] instanceof Short[]) {
        Short[] values = (Short[]) probData[i];
        for (int j = 0; j < values.length; j++) {
          str = Evu.getProbabilityPrintString(values[j].shortValue());
          pout.print(str);
        }
      }
    }
    if (probData.length > 0) {
      pout.println(")");
    }

    pout.println("  Processes       :");
    pout.print("    (");
    count = 0;
    for (int i=0; i<processData.length; i++) {
      if (i != 0 && (count % nSteps) == 0) {
        pout.print(") (");
        count = 0;
      }
      else if (i != 0) { pout.print(", "); }
      pout.print(processData[i]);
      count++;
    }
    pout.println(")");
  }

  private VegetativeType findLifeform(VegetativeType[] states,  Lifeform kind) {
    int index = findLifeformIndex(states,kind);
    return (index != -1) ? states[index] : null;
  }
  private int findLifeformIndex(VegetativeType[] states,  Lifeform kind) {
    for (int i=0; i<states.length; i++) {
      if (states[i].getSpecies().getLifeform() == kind) {
        return i;
      }
    }
    return -1;
  }
  private VegetativeType findDominantLife(VegetativeType[] states) {
    int index = findDominantLifeIndex(states);
    return (index != -1) ? states[index] : null;
  }
  private int findDominantLifeIndex(VegetativeType[] states) {
    VegetativeType state;
    Lifeform      kind, dominantKind=null;
    int           dominantIndex=-1;

    for (int i=0; i<states.length; i++) {
      kind = states[i].getSpecies().getLifeform();
      if (kind == Lifeform.TREES) {
        return i;
      }

      if (dominantIndex == -1) {
        dominantIndex = i;
        dominantKind = states[dominantIndex].getSpecies().getLifeform();
      }
      else if (kind == Lifeform.SHRUBS &&
               dominantKind != Lifeform.TREES) {
        dominantIndex = i;
        dominantKind = states[dominantIndex].getSpecies().getLifeform();
      }
      else if (kind == Lifeform.HERBACIOUS &&
               dominantKind != Lifeform.TREES &&
               dominantKind != Lifeform.SHRUBS) {
        dominantIndex = i;
        dominantKind = states[dominantIndex].getSpecies().getLifeform();
      }
      else if (kind == Lifeform.AGRICULTURE &&
               dominantKind != Lifeform.TREES &&
               dominantKind != Lifeform.SHRUBS &&
               dominantKind != Lifeform.HERBACIOUS) {
        dominantIndex = i;
        dominantKind = states[dominantIndex].getSpecies().getLifeform();
      }
      else if (kind == Lifeform.NA &&
               dominantKind != Lifeform.TREES &&
               dominantKind != Lifeform.SHRUBS &&
               dominantKind != Lifeform.HERBACIOUS &&
               dominantKind != Lifeform.AGRICULTURE) {
        dominantIndex = i;
        dominantKind = states[dominantIndex].getSpecies().getLifeform();
      }
    }
    return dominantIndex;
  }

  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    int version = in.readInt();

    // ** VegetativeType **
    {
      VegetativeType[] values;
      vegTypeData = new Object[in.readInt()];
      for (int i = 0; i < vegTypeData.length; i++) {
        int count = in.readInt();
        if (count == 1) {
          vegTypeData[i] = VegetativeType.readExternalData(in);
        }
        else if (count > 1) {
          values = new VegetativeType[count];
          for (int j = 0; j < count; j++) {
            values[j] = VegetativeType.readExternalData(in);
          }
          vegTypeData[i] = values;
        }
        else {
          vegTypeData[i] = null;
        }
      }
    }

    // ** ProcessType **
    {
      ProcessType[] values;
      processData = new Object[in.readInt()];
      for (int i = 0; i < processData.length; i++) {
        int count = in.readInt();
        if (count == 1) {
          processData[i] = ProcessType.readExternalSimple(in);
        }
        else if (count > 1) {
          values = new ProcessType[count];
          for (int j = 0; j < count; j++) {
            values[j] = ProcessType.readExternalSimple(in);
          }
          processData[i] = values;
        }
        else {
          processData[i] = null;
        }
      }
    }

    // ** Probabilities **
    {
      Short[] values;
      String  str;
      probData = new Object[in.readInt()];
      for (int i = 0; i < probData.length; i++) {
        int count = in.readInt();
        if (count == 1) {
          probData[i] = new Short((short)Evu.parseProbabilityString((String)in.readObject()));
        }
        else if (count > 1) {
          values = new Short[count];
          for (int j = 0; j < count; j++) {
            values[j] = new Short((short)Evu.parseProbabilityString((String)in.readObject()));
          }
          probData[i] = values;
        }
        else {
          probData[i] = null;
        }
      }
    }

  }

  private int countValidVegType() {
    int count=0;
    for (int i=0; i<vegTypeData.length; i++) {
      if (vegTypeData[i] != null) { count++; }
    }
    return count;
  }
  private int countValidProcess() {
    int count=0;
    for (int i=0; i<processData.length; i++) {
      if (processData[i] != null) { count++; }
    }
    return count;
  }
  private int countValidProbability() {
    int count=0;
    for (int i=0; i<probData.length; i++) {
      if (probData[i] != null) { count++; }
    }
    return count;
  }

  public void writeExternal(ObjectOutput out) throws IOException {
    VegetativeType.setLimitedSerialization();

    out.writeInt(version);

    // ** VegetativeType **
    {
      VegetativeType[] values;
      int count = countValidVegType();
      out.writeInt(count);
      for (int i = 0; i < count; i++) {
        if (vegTypeData[i] instanceof VegetativeType) {
          out.writeInt(1);
          ((VegetativeType) vegTypeData[i]).writeExternal(out);
        }
        else if (vegTypeData[i] instanceof VegetativeType[]) {
          values = (VegetativeType[]) vegTypeData[i];
          out.writeInt(values.length);
          for (int j = 0; j < values.length; j++) {
            values[j].writeExternal(out);
          }
        }
        else {
          out.writeInt(0);
        }
      }
    }

    // ** Processes **
    {
      ProcessType[] values;
      int count = countValidProcess();
      out.writeInt(count);
      for (int i = 0; i < count; i++) {
        if (processData[i] instanceof ProcessType) {
          out.writeInt(1);
          ((ProcessType) processData[i]).writeExternalSimple(out);
        }
        else if (processData[i] instanceof ProcessType[]) {
          values = (ProcessType[]) processData[i];
          out.writeInt(values.length);
          for (int j = 0; j < values.length; j++) {
            values[j].writeExternalSimple(out);
          }
        }
        else {
          out.writeInt(0);
        }
      }
    }

    // ** Probabilities **
    {
      Short[] values;
      int count = countValidProbability();
      out.writeInt(count);
      for (int i = 0; i < count; i++) {
        if (probData[i] instanceof Short) {
          out.writeInt(1);
          out.writeObject(Evu.getProbabilitySaveString(((Short)probData[i]).shortValue()));
        }
        else if (probData[i] instanceof Short[]) {
          values = (Short[]) probData[i];
          out.writeInt(values.length);
          for (int j = 0; j < values.length; j++) {
            out.writeObject(Evu.getProbabilitySaveString(values[j].shortValue()));
          }
        }
        else {
          out.writeInt(0);
        }
      }
    }

    VegetativeType.clearLimitedSerialization();
  }


}
