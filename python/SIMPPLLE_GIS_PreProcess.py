######################################################################
 
# The University of Montana owns copyright of the designated documentation contained 
# within this file as part of the software product designated by Uniform Resource Identifier 
# UM-OpenSIMPPLLE-0.9.  By copying this file the user accepts the University of Montana 
# Open Source License Contract pertaining to this documentation and agrees to abide by all 
# restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
# 
# @author Documentation by Brian Losi
# <p>Original source code authorship: Kirk A. Moeller
## Purpose:  To perpare SIMPPLLE GIS output files for use by the
##           ArcMap Extension.  While the extension will do this
##           processing as well, the python script will do it much
##           faster (< 5 minutes vs. hours with the extension)
######################################################################

# Import system modules
import sys, string, os, tempfile, traceback, _winreg

def getArcGISVersion():
    hkey = _winreg.OpenKey(_winreg.HKEY_LOCAL_MACHINE, "SOFTWARE\ESRI\ArcGIS", 0, _winreg.KEY_READ)
    val,typ = _winreg.QueryValueEx(hkey, "RealVersion")
    _winreg.CloseKey(hkey)

    ArcGIS_Version = "9.1"
    if (string.find(val,"9.1") != -1):
        return "9.1"
    elif (string.find(val,"9.2") != -1):
        return "9.2"
    elif (string.find(val,"9.3") != -1):
        return "9.3"
    else:
        return "9.1"
#end getArcGISVersion

#Get ArcGIS Version
ArcGIS_Version = getArcGISVersion()


# Create the Geoprocessor object

if (ArcGIS_Version == "9.1"):
    import win32com.client
    gp = win32com.client.Dispatch("esriGeoprocessing.GpDispatch.1")
else:
    import arcgisscripting
    gp = arcgisscripting.create()

def AddMsgAndPrint(msg, severity=0):
    msg = str(msg)
    # Adds a Message to the geoprocessor (in case this is run as a tool)
    # and also prints the message to the screen
    print msg

    #Split the message on \n first, so that if it's multiple lines, a GPMessage will be added for each line
    try:
        for string in msg.split('\n'):
            #Add a geoprocessing message (in case this is run as a tool)
            if severity == 0:
                gp.AddMessage(string)
            elif severity == 1:
                gp.AddWarning(string)
            elif severity == 2:
                gp.AddError(string)
        if (severity == 2 or severity == 1):
            trace = traceback.format_exception(sys.exc_type, sys.exc_value, sys.exc_traceback)
            for value in trace:
                print str(value)
                gp.AddError(str(value))
    except:
        pass       
#end AddMsgAndPrint
    
def makepath(dir,file):
    return dir + os.sep + file
#end makepath

def tempFileName(workspace):
    i = 1
    path = makepath(workspace,"tmpspread" + str(i))
    while (os.path.exists(path)):
        i += 1
        path = makepath(gp.workspace,"tmpspread" + str(i))
    return "tmpspread" + str(i)
#end tempFileName

def renameFile(filepath):
    i = 1
    path = filepath + ".orig"
    while (os.path.exists(path)):
        i += 1
        path = filepath + ".orig" + str(i)
    os.rename(filepath,path)
    return path
#end tempFileName

workspace = sys.argv[1]
fc        = sys.argv[2]
beginTS   = sys.argv[3]
endTS     = sys.argv[4]
minfoFile = ""

if (beginTS == "#" or beginTS == ""):
    beginTS = -1
if (endTS == "#" or endTS == ""):
    endTS = -1

beginTS = int(beginTS)
endTS   = int(endTS)

## Older versions of SIMPPLLE output the field names with dashes
## in the first line of spread files, which is not a legal character.
## this script fixes those files.
def fixDashesInFiles(workspace,file):
    fin=open(file,"r")

    line = fin.readline()
    if (line == "" or (line.find("-") == -1)):
        fin.close()
        return

    tmpfile = tempFileName(workspace)
    fout = open(tmpfile,"w")

    splitline = string.split(line,"-")
    line = ""    
    for str in splitline:
        if (line):
            line = line + "_" + str
        else:
            line = str
    
    while (line):
        fout.write(line)
        line = fin.readline()
    fin.close()
    fout.close()

    os.remove(file)
    os.rename(tmpfile,file)
    
#end fixDashesInSpreadFiles

def modifyMinfoFile(beginTS,endTS):
    global minfoFile

    originalFile = renameFile(minfoFile)    
    
    fin=open(originalFile,"r")
    fout=open(minfoFile,"w")

    line = fin.readline()
    while (line):
        if (line.find("TIMESTEPS") != -1):
            fout.write("  TIMESTEPS," + str(beginTS) + "," + str(endTS) + "\n")
        else:
            fout.write(line)
        line = fin.readline()

    fin.close()
    fout.close()
    
#end modifyMinfoFile
    
def main():
    global workspace
    global fc
    global minfoFile
    global beginTS
    global endTS

    
    # Find Prefix name and update and spread files
    files = os.listdir(workspace)
    prefix = ""
    for i in range(0,len(files)):
        if (files[i].endswith('.sinfo')):
            split = string.split(files[i],".sinfo")
            prefix = split[0]
            break
        if (files[i].endswith('.minfo')):
            split = string.split(files[i],".minfo")
            minfoFile = makepath(workspace,files[i])
            prefix = split[0]
            break
    if (prefix == ""):
        AddMsgAndPrint("Unable to find .sinfo file in workspace",2)
        raise Exception, "Missing .sinfo file"

    if (minfoFile and beginTS != -1 and endTS != -1):
        modifyMinfoFile(beginTS,endTS)

    tablesList = []
    tables = ""
    for i in range(0,len(files)):
        if ((files[i].find(prefix)     != -1) and
            ((files[i].find("update")  != -1) or
             (files[i].find("spread")  != -1) or
             (files[i].find("process") != -1) or
             (files[i].find("size")    != -1) or
             (files[i].find("species") != -1) or
             (files[i].find("canopy")  != -1)) and
            (files[i].find("firespread") == -1) and
            (files[i].find(".txt") != -1)):

            if (minfoFile and beginTS != -1 and endTS != -1):
                split = string.split(files[i],"-")
                tsStr = split[len(split)-2]
                if (tsStr != "n"):
                    ts = int(tsStr)
                    if (ts < beginTS or ts > endTS):
                        continue
            
            file = makepath(workspace,files[i])
            if ((files[i].find("spread") != -1) or
                (files[i].find("process") != -1) or
                (files[i].find("size") != -1) or
                (files[i].find("species") != -1) or
                (files[i].find("canopy") != -1)):
                fixDashesInFiles(workspace,file)

            if ((len(tables) + len(file)) > 1000):
                tablesList.append(tables)
                tables = ""
            if (tables):
                tables = tables + ";" + file
            else:
                tables = file

    if (len(tables) > 0):
        tablesList.append(tables)

    del tables
    if (len(tablesList) == 0):
        AddMsgAndPrint("Unable to find gis files",2)
        raise Exception, "Missing SIMPPLLE gis files"


    gp.RefreshCatalog(workspace)
    gdbName = prefix + ".mdb"
    # Create GeoDatabase.
    AddMsgAndPrint("Creating Geodatabase",0)
    gp.CreatePersonalGDB_management(workspace, gdbName)
    AddMsgAndPrint(gp.GetMessages(0))
    AddMsgAndPrint("")

    AddMsgAndPrint("Copy Feature Class into geodatabase",0)
    gdb = makepath(workspace,gdbName)
    gp.FeatureclassToFeatureclass_conversion(fc, gdb, prefix)
    AddMsgAndPrint(gp.GetMessages(0))
    AddMsgAndPrint("")

    AddMsgAndPrint("Copy tables into geodatabase",0)

    for tables in tablesList:
        gp.TableToGeodatabase_conversion(tables, gdb)
        AddMsgAndPrint(gp.GetMessages(0))     
        AddMsgAndPrint("")

#end main

def doGPErrorMessages():
    regMsg  = gp.GetMessages(0)
    warnMsg = gp.GetMessages(1)
    errMsg  = gp.GetMessages(2)
    if regMsg != "":
        AddMsgAndPrint(regMsg,0)
    if warnMsg != "":
        AddMsgAndPrint(warnMsg,1)
    if errMsg != "":
        AddMsgAndPrint(errMsg,2)

    if (regMsg == "" and warnMsg == "" and errMsg == ""):
        return 0
    return 1
#end doGPErrorMessages
    
try:
    main()

    print "Finished."
    del gp

except AttributeError, ErrDesc:
    AddMsgAndPrint(ErrDesc,1)
    doGPErrorMessages()
except StandardError, ErrDesc:
    AddMsgAndPrint(ErrDesc,1)
    doGPErrorMessages()
except:
    if (doGPErrorMessages() == 0):
        AddMsgAndPrint("An Error Occurred",2)
    del gp
