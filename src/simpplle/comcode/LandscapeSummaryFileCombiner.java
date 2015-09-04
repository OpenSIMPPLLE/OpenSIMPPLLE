package simpplle.comcode;

import java.util.TreeMap;
import java.io.File;
import java.util.ArrayList;
import java.io.*;
import java.util.*;

/**
 * /**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * @author Documentation by Brian Losi
 * <p>Original authorship: Kirk A. Moeller
 *
 *
 * <p>Description:
 *      The purpose of this class is to combine Landscape summary files.
 *      These files are generated when doing a multiple run simulation.
 *      The problem that has come up is the data carried by the class
 *      simpplle.comcode.MultipleRunSummary very quickly allocates large
 *      amounts of memory.  These leads to users not being able to do as
 *      many runs as they would like.  This utility allows the user to
 *      combine "-ls" files run simulations into one "-ls" file.  So if
 *      the user did two 5 run simulations the result would be a "-ls"
 *      files with runs numbering 0 - 10.

 */
public class LandscapeSummaryFileCombiner {

  // PROCESS,SPECIES,SIZECLASS,DENSITY,TREATMENT,SUPPRESSIONCOST,EMISSIONS
  private static TreeMap<String,TreeMap<Integer,ArrayList<Integer>>> processMap;
  private static TreeMap<String,TreeMap<Integer,ArrayList<Integer>>> speciesMap;
  private static TreeMap<String,TreeMap<Integer,ArrayList<Integer>>> sizeClassMap;
  private static TreeMap<String,TreeMap<Integer,ArrayList<Integer>>> densityMap;
  private static TreeMap<String,TreeMap<Integer,ArrayList<Integer>>> treatmentMap;
  private static TreeMap<String,TreeMap<Integer,ArrayList<Integer>>> suppCostMap;
  private static TreeMap<String,TreeMap<Integer,ArrayList<Integer>>> emissionsMap;

  private static TreeMap<String,TreeMap<Integer,ArrayList<Integer>>> currentMap;

  private static ArrayList<String> specialAreaOwnershipList;

  private static int nRuns=0;
  private static boolean nRunsSet=false;
  public static String combineLandscapeSummaryFiles(File[] files)
     throws SimpplleError
   {
     processMap   = new TreeMap<String,TreeMap<Integer,ArrayList<Integer>>>();
     speciesMap   = new TreeMap<String,TreeMap<Integer,ArrayList<Integer>>>();
     sizeClassMap = new TreeMap<String,TreeMap<Integer,ArrayList<Integer>>>();
     densityMap   = new TreeMap<String,TreeMap<Integer,ArrayList<Integer>>>();
     treatmentMap = new TreeMap<String,TreeMap<Integer,ArrayList<Integer>>>();
     suppCostMap  = new TreeMap<String,TreeMap<Integer,ArrayList<Integer>>>();
     emissionsMap = new TreeMap<String,TreeMap<Integer,ArrayList<Integer>>>();

     specialAreaOwnershipList = new ArrayList<String>();

     for (int i=0; i<files.length; i++) {
       nRunsSet = false;
       processLSFile(files[i],i);
     }

     String str = files[0].getName();
     int index = str.indexOf('-');
     String prefix = str.substring(0,index);
     File outfile = new File(files[0].getParent(),prefix + "-combined-ls.txt");

     writeNewFile(outfile);

     return outfile.getPath();
   }

   private static void writeNewFile(File outfile) throws SimpplleError {
    try {
      PrintWriter fout = new PrintWriter(outfile);
      StringBuilder sb = new StringBuilder();
      Formatter formatter = new Formatter(sb, Locale.US);

      if (specialAreaOwnershipList == null || specialAreaOwnershipList.size() == 0) {
        formatter.format("%s%n", "PROCESS");
        writeMap(formatter, processMap,"");

        formatter.format("%s%n", "SPECIES");
        writeMap(formatter, speciesMap, "");

        formatter.format("%s%n", "SIZECLASS");
        writeMap(formatter, sizeClassMap, "");
        
        formatter.format("%s%n", "DENSITY");
        writeMap(formatter, densityMap, "");
      }
      else {
        formatter.format("%s%n", "PROCESS");
        for (String saOwn : specialAreaOwnershipList) {
          writeMap(formatter, processMap,saOwn);
        }

        formatter.format("%s%n", "SPECIES");
        for (String saOwn : specialAreaOwnershipList) {
          writeMap(formatter, speciesMap, saOwn);
        }

        formatter.format("%s%n", "SIZECLASS");
        for (String saOwn : specialAreaOwnershipList) {
          writeMap(formatter, sizeClassMap, saOwn);
        }

        formatter.format("%s%n", "DENSITY");
        for (String saOwn : specialAreaOwnershipList) {
          writeMap(formatter, densityMap, saOwn);
        }
      }

      if (treatmentMap.size() > 0) {
        formatter.format("%s%n", "TREATMENT");
        writeMap(formatter, treatmentMap,"");
      }
      if (suppCostMap.size() > 0) {
        formatter.format("%s%n", "SUPPRESSIONCOST");
        writeMap(formatter, suppCostMap,"");
      }
      if (emissionsMap.size() > 0) {
        formatter.format("%s%n", "EMISSIONS");
        writeMap(formatter, emissionsMap,"");
      }

      fout.println(sb.toString());
      fout.flush();
      fout.close();
    }
    catch (FileNotFoundException ex) {
      throw new SimpplleError(ex.getMessage(),ex);
    }
   }

  private static void writeMap(Formatter formatter,
                               TreeMap<String,TreeMap<Integer,ArrayList<Integer>>> map,
                               String saOwn) {

    for (String field : map.keySet()) {
      String fieldKey = field;
      String tmpSaOwn="";
      String[] strs = field.split("#");
      if (strs.length > 1) {
        field = strs[0];
        tmpSaOwn = strs[1];
        if (saOwn.length() > 0 && saOwn.equals(tmpSaOwn) == false) {
          continue;
        }
      }
      if (tmpSaOwn.length() > 0) {
        formatter.format("TIME-%s", tmpSaOwn);
      }
      else {
        formatter.format("TIME");
      }
      for (int i=0; i<nRuns; i++) {
        formatter.format(" %s-%d", field , i+1);
      }
      formatter.format("%n");

      TreeMap<Integer,ArrayList<Integer>> list = map.get(fieldKey);
      for (Integer ts : list.keySet()) {
        ArrayList<Integer> acresList = list.get(ts);
        formatter.format("%d",ts);
        for (int r=0; r<nRuns; r++) {
          if (r < acresList.size()) {
            formatter.format(" %d", acresList.get(r));
          }
          else {
            formatter.format(" 0");
          }
        }
        formatter.format("%n");
      }
    }
  }

   private static String processHeaderLine(String line) {
     StringTokenizer strTok = new StringTokenizer(line);
     String saOwn="";

     while (strTok.hasMoreTokens()) {
       String token = strTok.nextToken();
       if (token != null && token.trim().indexOf("TIME") != -1) {
         if (!nRunsSet) {
           nRuns += strTok.countTokens();
           nRunsSet = true;
         }
         if (token.trim().indexOf("TIME-") != -1) {
           String[] strs = token.trim().split("-");
           saOwn = strs[1];
         }
         continue;
       }

       int index = token.lastIndexOf('-');

       String field = token.substring(0,index);
       if (saOwn.length() > 0) {
         field = field + "#" + saOwn;
         if (specialAreaOwnershipList.contains(saOwn) == false) {
           specialAreaOwnershipList.add(saOwn);
         }
       }

       if (currentMap.containsKey(field) == false) {
         currentMap.put(field, new TreeMap<Integer,ArrayList<Integer>>());
       }
       // Don't need to read the rest as they are all the same except run #.
       return field;
     }
     return null;
   }

   private static String checkForNewSectionTag(String line) {
     String sectionName = line.trim();
     if (sectionName.equalsIgnoreCase("PROCESS")) {
       currentMap = processMap;
     }
     else if (sectionName.equalsIgnoreCase("SPECIES")) {
       currentMap = speciesMap;
     }
     else if (sectionName.equalsIgnoreCase("SIZECLASS")) {
       currentMap = sizeClassMap;
     }
     else if (sectionName.equalsIgnoreCase("DENSITY")) {
       currentMap = densityMap;
     }
     else if (sectionName.equalsIgnoreCase("TREATMENT")) {
       currentMap = treatmentMap;
     }
     else if (sectionName.equalsIgnoreCase("SUPPRESSIONCOST")) {
       currentMap = suppCostMap;
     }
     else if (sectionName.equalsIgnoreCase("EMISSIONS")) {
       currentMap = emissionsMap;
     }
     else if (sectionName.startsWith("TIME")) {
       return processHeaderLine(line);
     }
     else {
       return null;
     }

     return sectionName;
   }

   private static int nextRun=0;
   private static void processLSFile(File file, int fileCount)
     throws SimpplleError
   {
     BufferedReader fin=null;
     String         field="";
     int run=0;

     try {
       fin = new BufferedReader(new FileReader(file));

       String line = fin.readLine();
       while (line != null) {
         run = 0;
         line = line.trim();
         if (line.length() == 0) {
           line = fin.readLine();
           continue;
         }

         String tmpField = checkForNewSectionTag(line);

         if (tmpField != null) {
           field = tmpField;
           line = fin.readLine();
           continue;
         }

         StringTokenizerPlus strTok = new StringTokenizerPlus(line);

         int ts = strTok.getIntToken();

         TreeMap<Integer,ArrayList<Integer>> list = currentMap.get(field);
         ArrayList<Integer> acresList = list.get(ts);
         if (acresList == null) {
           acresList = new ArrayList<Integer>();
           list.put(ts,acresList);
         }

         if (fileCount != 0) {
           run = nextRun;
         }

         if (acresList.size() < run && fileCount > 0) {
           for (int r=0; r<run; r++) {
             acresList.add(new Integer(0));
           }
         }

         while (strTok.hasMoreTokens()) {
           int acres = strTok.getIntToken();
           acresList.add(new Integer(acres));

           run++;
         }
         line = fin.readLine();
       }
       nextRun = run;
     }
     catch (IOException ex) {
       String msg = ex.getMessage() + "\n" + "Problems reading input file";
       throw new SimpplleError(msg,ex);
     }
     catch (ParseError ex) {
       throw new SimpplleError(ex.msg,ex);
     }
     finally {
       Utility.close(fin);
     }
   }

}
