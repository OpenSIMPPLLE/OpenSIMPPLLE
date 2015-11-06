package simpplle.comcode.process;

import simpplle.comcode.Process;

/**
 * This class creates a Drought object which extends simpplle.Comcode.Process  
 * 
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-0.9.  By copying this file the user accepts the University of Montana 
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class is a dummy process for debugging.  It is a type of process.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 * 
 * @see simpplle.comcode.Process
 */


public class DummyProcess extends Process {
	/**
	 * Creates a dummy process.  This can be used for debugging or training purposes.  
	 */
  public DummyProcess() {
    super();
    description = "Dummy Process";
  }

}