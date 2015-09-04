package simpplle.comcode;

import java.io.*;
import java.util.Hashtable;


/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class TreatmentType extends SimpplleType implements Externalizable {
  static final long serialVersionUID = -2478691869570190094L;
  static final int  version          = 1;

  public static final int COLUMN_COUNT = 1;
  public static final int CODE_COL     = 0;

  private String treatmentName;

  // ************************************
  // *** Common to more than one zone ***
  // ************************************
  public static final TreatmentType NONE                                    = new TreatmentType("NONE");
  public static final TreatmentType AGRICULTURE                             = new TreatmentType("AGRICULTURE");
  public static final TreatmentType CLEARCUT_WITH_RESERVES                  = new TreatmentType("CLEARCUT-WITH-RESERVES");
  public static final TreatmentType CLEARCUT_WITH_RESERVES_PLANT            = new TreatmentType("CLEARCUT-WITH-RESERVES-PLANT");
  public static final TreatmentType COMMERCIAL_THINNING                     = new TreatmentType("COMMERCIAL-THINNING");
  public static final TreatmentType ECOSYSTEM_MANAGEMENT_BROADCAST_BURN     = new TreatmentType("ECOSYSTEM-MANAGEMENT-BROADCAST-BURN");
  public static final TreatmentType ECOSYSTEM_MANAGEMENT_THIN_AND_UNDERBURN = new TreatmentType("ECOSYSTEM-MANAGEMENT-THIN-AND-UNDERBURN");
  public static final TreatmentType ECOSYSTEM_MANAGEMENT_UNDERBURN          = new TreatmentType("ECOSYSTEM-MANAGEMENT-UNDERBURN");
  public static final TreatmentType FIREWOOD_REMOVAL                        = new TreatmentType("FIREWOOD-REMOVAL");
  public static final TreatmentType GROUP_SELECTION_CUT                     = new TreatmentType("GROUP-SELECTION-CUT");
  public static final TreatmentType IMPROVEMENT_CUT                         = new TreatmentType("IMPROVEMENT-CUT");
  public static final TreatmentType INDIVIDUAL_SELECTION_CUT                = new TreatmentType("INDIVIDUAL-SELECTION-CUT");
  public static final TreatmentType LIBERATION_CUT                          = new TreatmentType("LIBERATION-CUT");
  public static final TreatmentType PRECOMMERCIAL_THINNING                  = new TreatmentType("PRECOMMERCIAL-THINNING");
  public static final TreatmentType PRECOMMERCIAL_THINNING_DIVERSITY        = new TreatmentType("PRECOMMERCIAL-THINNING-DIVERSITY");
  public static final TreatmentType SANITATION_SALVAGE                      = new TreatmentType("SANITATION-SALVAGE");
  public static final TreatmentType SEEDTREE_FINAL_CUT                      = new TreatmentType("SEEDTREE-FINAL-CUT");
  public static final TreatmentType SEEDTREE_FINAL_PLANT                    = new TreatmentType("SEEDTREE-FINAL-PLANT");
  public static final TreatmentType SEEDTREE_FINAL_WITH_RESERVES            = new TreatmentType("SEEDTREE-FINAL-WITH-RESERVES");
  public static final TreatmentType SEEDTREE_FINAL_WR_PLANT                 = new TreatmentType("SEEDTREE-FINAL-WR-PLANT");
  public static final TreatmentType SEEDTREE_SEEDCUT                        = new TreatmentType("SEEDTREE-SEEDCUT");
  public static final TreatmentType SHELTERWOOD_FINAL_CUT                   = new TreatmentType("SHELTERWOOD-FINAL-CUT");
  public static final TreatmentType SHELTERWOOD_FINAL_PLANT                 = new TreatmentType("SHELTERWOOD-FINAL-PLANT");
  public static final TreatmentType SHELTERWOOD_FINAL_WITH_RESERVES         = new TreatmentType("SHELTERWOOD-FINAL-WITH-RESERVES");
  public static final TreatmentType SHELTERWOOD_FINAL_WR_PLANT              = new TreatmentType("SHELTERWOOD-FINAL-WR-PLANT");
  public static final TreatmentType SHELTERWOOD_SEEDCUT                     = new TreatmentType("SHELTERWOOD-SEEDCUT");

  // Not in Westside
  public static final TreatmentType LOW_INTENSITY_GRAZING                   = new TreatmentType("LOW-INTENSITY-GRAZING");
  public static final TreatmentType MODERATE_INTENSITY_GRAZING              = new TreatmentType("MODERATE-INTENSITY-GRAZING");
  public static final TreatmentType HIGH_INTENSITY_GRAZING                  = new TreatmentType("HIGH-INTENSITY-GRAZING");
  public static final TreatmentType HERBICIDE_SPRAYING                      = new TreatmentType("HERBICIDE-SPRAYING");

  // ***********************************
  // *** Eastside and Westside Zones ***
  // ***********************************
  public static final TreatmentType CLEARCUT_WR_PRRWP              = new TreatmentType("CLEARCUT-WR-PRRWP");
  public static final TreatmentType SEEDTREE_FINAL_PRRWP           = new TreatmentType("SEEDTREE-FINAL-PRRWP");
  public static final TreatmentType SEEDTREE_FINAL_WR_PRRWP        = new TreatmentType("SEEDTREE-FINAL-WR-PRRWP");
  public static final TreatmentType SHELTERWOOD_FINAL_PRRWP        = new TreatmentType("SHELTERWOOD-FINAL-PRRWP");
  public static final TreatmentType SHELTERWOOD_FINAL_WR_PRRWP     = new TreatmentType("SHELTERWOOD-FINAL-WR-PRRWP");
  public static final TreatmentType ASPEN_RESTORATION_BURN         = new TreatmentType("ASPEN-RESTORATION-BURN");
  public static final TreatmentType ASPEN_RESTORATION_CUT_AND_BURN = new TreatmentType("ASPEN-RESTORATION-CUT-AND-BURN");
  public static final TreatmentType ENCROACHMENT_BURN              = new TreatmentType("ENCROACHMENT-BURN");
  public static final TreatmentType ENCROACHMENT_CUT_AND_BURN      = new TreatmentType("ENCROACHMENT-CUT-AND-BURN");

  // ***************************************************
  // *** Sierra Nevada and Southern California Zones ***
  // ***************************************************
  public static final TreatmentType CUTTING                                 = new TreatmentType("CUTTING");
  public static final TreatmentType CUTTING_BURNING                         = new TreatmentType("CUTTING-BURNING");
  public static final TreatmentType CRUSHING                                = new TreatmentType("CRUSHING");
  public static final TreatmentType CRUSHING_BURNING                        = new TreatmentType("CRUSHING-BURNING");
  public static final TreatmentType CUT_STACK_BURN                          = new TreatmentType("CUT-STACK-BURN");
  public static final TreatmentType LINE_BURNING                            = new TreatmentType("LINE-BURNING");

  // ****************************
  // *** South Central Alaska ***
  // ****************************

  // **********************
  // *** Southwest Utah ***
  // **********************

  public static final TreatmentType MECHANICAL_SEED        = new TreatmentType("MECHANICAL-SEED");
  public static final TreatmentType MECHANICAL_BURN_SEED   = new TreatmentType("MECHANICAL-BURN-SEED");
  public static final TreatmentType PRESCRIBE_BURN_SEED    = new TreatmentType("PRESCRIBE-BURN-SEED");
  public static final TreatmentType PRESCRIBE_BURN         = new TreatmentType("PRESCRIBE-BURN");
  public static final TreatmentType PRESCRIBE_UNDERBURN    = new TreatmentType("PRESCRIBE-UNDERBURN");
//  public static final TreatmentType SANITATION_SALVAGE     = new TreatmentType("SANITATION-SALVAGE");
//  public static final TreatmentType CLEARCUT_WITH_RESERVES = new TreatmentType("CLEARCUT-WITH-RESERVES");

  // ****************************
  // *** Colorado Front Range ***
  // ****************************
  public static final TreatmentType THIN_CHIP_UNDERBURN = new TreatmentType("THIN-CHIP-UNDERBURN");
  public static final TreatmentType UNDERBURN           = new TreatmentType("UNDERBURN");

  public TreatmentType() {
    treatmentName = null;
  }

  public boolean equals(Object obj) {
    if (this == obj) { return true; }
    if (obj instanceof TreatmentType) {
      if (treatmentName == null || obj == null) { return false; }

      return treatmentName.equals(((TreatmentType)obj).treatmentName);
    }
    return false;
  }

  public int hashCode() {
    return treatmentName.hashCode();
  }

  public int compareTo(Object o) {
    if (o == null) { return -1; }
    return treatmentName.compareTo(o.toString());
  }

  public TreatmentType(String treatmentName) {
    this.treatmentName = treatmentName.toUpperCase();

    updateAllData(this,TREATMENT);
  }

  public String toString() { return treatmentName; }

  public static TreatmentType get(String treatmentName) {
    return ( (TreatmentType)allTreatmentHm.get(treatmentName.toUpperCase()) );
  }
  public static TreatmentType get(String treatmentName, boolean noExistCreate) {
    TreatmentType treat = get(treatmentName);
    if (treat == null && noExistCreate) { return new TreatmentType(treatmentName); }

    return treat;
  }

  public int getId() { return 0; }

  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    int version = in.readInt();

    treatmentName = (String)in.readObject();
  }
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(version);

    out.writeObject(treatmentName);
  }
  private Object readResolve () throws java.io.ObjectStreamException
  {
    TreatmentType treatObj = TreatmentType.get(treatmentName,true);
    updateAllData(treatObj,TREATMENT);
    return treatObj;
  }

  // *** JTable section ***
  // **********************

  public Object getColumnData(int col) {
    switch (col) {
      case CODE_COL:
        return this;
      default: return null;
    }
  }
  public void setColumnData(Object value, int col) {
    switch (col) {
      default: return;
    }
//    SystemKnowledge.markChanged(SystemKnowledge.TREATMENT_TYPE);
  }

  public static String getColumnName(int col) {
    switch (col) {
      case CODE_COL: return "Treatment";
      default: return "";
    }
  }


}
