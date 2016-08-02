/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import java.io.*;
import simpplle.comcode.SystemKnowledge;
import simpplle.comcode.SimpplleError;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JMenuItem;
import simpplle.comcode.*;

/**
 * This class creates the System Knowledge Filer which allows user choices in other dialogs to be used to find a file to open and/or save to a particular file.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class SystemKnowledgeFiler {
/**
 * Opens a particular file.  The kind is used to get the file extension, description and title of the file to be opened.
 * It will attempt to read the individual file 
 * @param dlg
 * @param kind
 * @param saveMenuItem
 * @param closeMenuItem
 * @return
 */
  public static boolean openFile(JDialog dlg, SystemKnowledge.Kinds kind, JMenuItem saveMenuItem,
                                 JMenuItem closeMenuItem) {
    String       ext = SystemKnowledge.getKnowledgeFileExtension(kind);
    String       description = SystemKnowledge.getKnowledgeFileDescription(kind);
    String       title = SystemKnowledge.getKnowledgeFileTitle(kind);

    MyFileFilter extFilter = new MyFileFilter(ext,description);

    File file = Utility.getOpenFile(dlg,"Open " + title,extFilter);
    if (file == null) { return false; }

    try {
      dlg.setCursor(Utility.getWaitCursor());

      SystemKnowledge.loadUserKnowledge(file,kind);
      saveMenuItem.setEnabled(true);
      if (closeMenuItem != null) { closeMenuItem.setEnabled(true); }

      dlg.setCursor(Utility.getNormalCursor());
      return true;
    }
    catch (SimpplleError ex) {
      JOptionPane.showMessageDialog(dlg, ex.getMessage(), "",
                                    JOptionPane.ERROR_MESSAGE);
      dlg.setCursor(Utility.getNormalCursor());
      return false;
    }
  }

  public static boolean saveFile(JDialog dlg, File outfile, SystemKnowledge.Kinds kind,
                                  JMenuItem saveMenuItem,
                                  JMenuItem closeMenuItem) {
    return saveFile(dlg, outfile, kind, saveMenuItem, closeMenuItem, null);
  }
  public static boolean saveFile(JDialog dlg, File outfile, SystemKnowledge.Kinds kind,
                                 JMenuItem saveMenuItem,
                                 JMenuItem closeMenuItem, Object obj)
  {
    try {
      if (outfile == null) {
        if (saveFile(dlg,kind,saveMenuItem,closeMenuItem,obj) == false) {
          return false;
        }
      }

      if (kind == SystemKnowledge.VEGETATION_PATHWAYS) {
        SystemKnowledge.saveVegetativePathway(outfile,(HabitatTypeGroup)obj);
      }
      else if (kind == SystemKnowledge.AQUATIC_PATHWAYS) {
        SystemKnowledge.saveIndividualAquaticPathwayFile(outfile,(LtaValleySegmentGroup)obj);
      }
      else {
        SystemKnowledge.saveIndividualInputFile(outfile, kind);
      }
      saveMenuItem.setEnabled(true);
      if (closeMenuItem != null) { closeMenuItem.setEnabled(true); }
      return true;
    }
    catch (SimpplleError ex) {
      JOptionPane.showMessageDialog(dlg, ex.getMessage(), "",
                                    JOptionPane.ERROR_MESSAGE);
      return false;
    }
  }

  public static boolean saveFile(JDialog dlg, SystemKnowledge.Kinds kind,
                                 JMenuItem saveMenuItem,
                                 JMenuItem closeMenuItem) {
    return saveFile(dlg,kind,saveMenuItem,closeMenuItem,null);
  }
  public static boolean saveFile(JDialog dlg, SystemKnowledge.Kinds kind,
                                 JMenuItem saveMenuItem,
                                 JMenuItem closeMenuItem, Object obj)
  {
    String ext = SystemKnowledge.getKnowledgeFileExtension(kind);
    String description = SystemKnowledge.getKnowledgeFileDescription(kind);
    String title = SystemKnowledge.getKnowledgeFileTitle(kind);

    MyFileFilter extFilter = new MyFileFilter(ext,description);

    File outfile = Utility.getSaveFile(dlg, "Save " + title, extFilter);
    if (outfile == null) { return false; }

    return saveFile(dlg,outfile,kind,saveMenuItem,closeMenuItem,obj);
  }
}


