 package simpplle.comcode;

 

/**

* <p>Title: SIMulating Patterns and Processes at Landscape scaLEs</p>

* <p>Description: </p>

* <p>Copyright: Public Domain</p>

* <p>Company: USDA Forest Service, Rocky Mountain Research Station, Missoula Forestry Sciences Lab</p>

* @author Kirk A. Moeller

* @version 2.3

*/

 

public class LightWsbw2 extends Process {

  private static final String printName = "LIGHT-WSBW2";

  public LightWsbw2() {

      super();

 

      spreading   = false;

      description = "Light Western Spruce Budworm";

 

      defaultVisibleColumns.add(BaseLogic.Columns.ROW_COL.toString());

      defaultVisibleColumns.add(ProcessProbLogic.Columns.PROB_COL.toString());

  }

 

}