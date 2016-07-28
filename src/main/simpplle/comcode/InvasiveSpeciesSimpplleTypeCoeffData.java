/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.io.Externalizable;
import java.io.ObjectOutput;
import java.io.IOException;
import java.io.ObjectInput;

/**
 * This class contains methods to handle Invasive Species Simpplle Type Coeffiecient Data.
 *  
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class InvasiveSpeciesSimpplleTypeCoeffData implements Externalizable,
  Comparable {
  static final long serialVersionUID = 4351470290880207756L;
  static final int  version          = 1;

  private SimpplleType simpplleType;
  private int          timeSteps;
  private double       coeff;
/**
 * Constructor.  Initializes the simpple type to process type None, time steps to 1 (initial) and coeffiecint to 0.0
 */
  public InvasiveSpeciesSimpplleTypeCoeffData() {
    simpplleType = ProcessType.NONE;
    timeSteps    = 1;
    coeff        = 0.0;
  }
/**
 * duplicates the invasive species simpplle type coefficient data 
 * @return a copy of invasive species simpplle type coefficient data
 */
  public InvasiveSpeciesSimpplleTypeCoeffData duplicate() {
    InvasiveSpeciesSimpplleTypeCoeffData newData = new InvasiveSpeciesSimpplleTypeCoeffData();

    newData.simpplleType = this.simpplleType;
    newData.timeSteps    = this.timeSteps;
    newData.coeff        = this.coeff;

    return newData;
  }

  public double getCoeff() {
    return coeff;
  }

  public SimpplleType getSimpplleType() {
    return simpplleType;
  }

  public int getTimeSteps() {
    return timeSteps;
  }

  public void setCoeff(double coeff) {
    this.coeff = coeff;
  }

  public void setSimpplleType(SimpplleType simpplleType) {
    this.simpplleType = simpplleType;
  }

  public void setTimeSteps(int timeSteps) {
    this.timeSteps = timeSteps;
  }

  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(version);

    out.writeObject((simpplleType instanceof ProcessType ? "ProcessType" : "TreatmentType"));
    simpplleType.writeExternalSimple(out);
    out.writeInt(timeSteps);
    out.writeDouble(coeff);
  }

  public void readExternal(ObjectInput in)
    throws IOException, ClassNotFoundException
  {
    int version = in.readInt();
    String className = (String)in.readObject();
    if (className.equals("ProcessType")) {
      simpplleType = SimpplleType.readExternalSimple(in,SimpplleType.PROCESS);
    }
    else {
      simpplleType = SimpplleType.readExternalSimple(in,SimpplleType.TREATMENT);
    }

    timeSteps = in.readInt();
    coeff     = in.readDouble();
  }

  public int compareTo(Object o) {
    if (o == null) { return -1; }
    return simpplleType.compareTo(o.toString());
  }

  public String toString() {
    return "{" + simpplleType.toString() + " " +
           Double.toString(coeff) + " " +
           Integer.toString(timeSteps) + "}";
  }
}



