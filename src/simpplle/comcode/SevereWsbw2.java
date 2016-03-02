 package simpplle.comcode;



 /**
  *
  * The University of Montana owns copyright of the designated documentation contained
  * within this file as part of the software product designated by Uniform Resource Identifier
  * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
  * Open Source License Contract pertaining to this documentation and agrees to abide by all
  * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
  *
  * <p>This class contains methods for Severe Western Spruce Budworm, a type of Process.
  *
  */

 

public class SevereWsbw2 extends Process {

  private static final String printName = "SEVERE-WSBW2";

  public SevereWsbw2() {

      super();

 

      spreading   = false;

      description = "Severe Western Spruce Budworm";

 

      defaultVisibleColumns.add(BaseLogic.Columns.ROW_COL.toString());

      defaultVisibleColumns.add(ProcessProbLogic.Columns.PROB_COL.toString());

  }

 

}