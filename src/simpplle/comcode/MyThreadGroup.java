package simpplle.comcode;


/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class contains methods for MyThreadGroup.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 * @see java.lang.ThreadGroup
 */
public class MyThreadGroup extends ThreadGroup {
  int threadCount = 0;
/**
 * Constructor for My thread group.  Inherits from Java Thread Group and initializes name and number of threads
 * @param name
 * @param numThreads
 */
  public MyThreadGroup(String name, int numThreads) {
    super(name);
    this.threadCount = numThreads;
  }

  public synchronized void decThreadCount() {
    threadCount--;
    if (threadCount == 0) {
      notifyAll();
    }
  }

}