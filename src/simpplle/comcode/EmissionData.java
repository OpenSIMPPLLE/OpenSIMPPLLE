/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;


/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 * 
 * <p>This is a class with methods to handle Emission Data
 *  This class is used to create a hash table key for
 * looking up particulate matter-10 (pm10) emissions values.
 *  
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 *
 */


public class EmissionData {
  private int    crbId;
  private int    strStg;
  private String moisture;

  /**
   * Primary constructor for Emission data.  It does not initialize any variables.
   */
  public EmissionData() {}
  
  /**
   * Overloaded constructor which calls to the setData method to set the Emmission data - does not reference default EmissionData constructor
   * @param crbId
   * @param strStg
   * @param moisture
   */
  public EmissionData(int crbId, int strStg, String moisture) {
    setData(crbId,strStg,moisture);
  }
/**
 * Sets the crbID, strStg and moisture for this emissions Data object.
 * @param crbId
 * @param strStg
 * @param moisture
 */
  public void setData(int crbId, int strStg, String moisture) {
    this.crbId    = crbId;
    this.strStg   = strStg;
    this.moisture = moisture;
  }

 
  public void setCrbId(int crbId) { this.crbId = crbId; }
  
  public void setStrStg(int strStg) { this.strStg = strStg; }
  
  public void setMoisture(String moisture) {this.moisture = moisture; }

  /**
   * Method to configure hashcode key for hash table to store particulate matter - 10 (pm10)data
   * 
   */
  public int hashCode() {
    return (13 * crbId + 17 * strStg + moisture.hashCode());
  }
/**
 * Checks if an EmissionData object equals another.  checks crbId, strStg, moisture.  
 * @return true if equal, false if not equal or not an EmmissionData object  
 */
  public boolean equals(Object o) {
    if (o instanceof EmissionData) {
      EmissionData emit = (EmissionData)o;
      return (crbId  == emit.crbId &&
              strStg == emit.strStg &&
              moisture.equals(emit.moisture));
    }
    else {
      return false;
    }
  }
}
