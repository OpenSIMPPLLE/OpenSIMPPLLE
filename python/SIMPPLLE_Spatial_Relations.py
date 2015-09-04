######################################################################
 
# The University of Montana owns copyright of the designated documentation contained 
# within this file as part of the software product designated by Uniform Resource Identifier 
# UM-OpenSIMPPLLE-0.9.  By copying this file the user accepts the University of Montana 
# Open Source License Contract pertaining to this documentation and agrees to abide by all 
# restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
# 
# @author Documentation by Brian Losi
# Original source code authorship: Kirk A. Moeller

## Purpose: To generate a text file that can be input in to SIMPPLLE
##          containing the spatial relationships of the input
##          feature classes.
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

#AddMsgAndPrint("Processing input parameters ",0)

# Set the necessary product code
#gp.SetProduct("ArcInfo")
workspace    = sys.argv[1]
gp.Workspace = workspace
inVegFC      = sys.argv[2]
outfilename  = sys.argv[3]
inLandFC     = sys.argv[4]
inStreamFC   = sys.argv[5]
inRoadFC     = sys.argv[6]
inTrailFC     = sys.argv[7]
wind         = sys.argv[8]
inDem        = sys.argv[9]
vegElevItem     = sys.argv[10]
landElevItem     = sys.argv[11]

if (inDem == "#"):
    inDem = ""
if (landElevItem == "#"):
    landElevItem = ""
if (vegElevItem == "#"):
    vegElevItem = ""
if (inStreamFC == "#"):
    inStreamFC = ""
if (inLandFC == "#"):
    inLandFC = ""
if (inRoadFC == "#"):
    inRoadFC = ""
if (inTrailFC == "#"):
    inTrailFC = ""
if (outfilename == "#" or outfilename == ""):
    outfilename = "spatial_relations"

if (inStreamFC != "" and inDem == ""):
    AddMsgAndPrint("A DEM is required for processing streams")
    sys.exit(2)
    
sr_outfile = makepath(workspace,outfilename) + ".spatialrelate"
att_outfile = makepath(workspace,outfilename) + ".attributesall"

if (ArcGIS_Version == "9.1"):
    gdb_outfile = makepath(workspace,outfilename) + ".mdb"
else:
    gdb_outfile = makepath(workspace,outfilename) + ".gdb"

i = 1
while (os.path.exists(sr_outfile) or os.path.exists(att_outfile) or os.path.exists(gdb_outfile)):
    i += 1
    outfilename = outfilename + str(i)
    sr_outfile = makepath(workspace,outfilename) + ".spatialrelate"
    att_outfile = makepath(workspace,outfilename) + ".attributesall"
    if (ArcGIS_Version == "9.1"):
        gdb_outfile = makepath(workspace,outfilename) + ".mdb"
    else:
        gdb_outfile = makepath(workspace,outfilename) + ".gdb"
#end while

gdb   = ""
dem   = ""
vegFC = ""
streamFC = ""
landFC   = ""
roadFC   = ""
trailFC  = ""

numNeighbors = 8
maxRow = 0
maxCol = 0
minRow = 9000000
minCol = 9000000

def writeData(fout,origRow, origCol, row, col,elev,rowColData):
    global wind
    if (isValidUnit(row,col,rowColData) and isValidUnit(origRow,origCol,rowColData)):
        downwind = isDownwind(origRow,origCol,row,col,wind)
        windDir = 'N'
        if (downwind):
            windDir = 'D'
        else:
            windDir = 'N'

        fout.write(str(rowColData[(origRow,origCol)]) + "," +
                   str(rowColData[(row,col)]) + "," + str(elev) + "," + windDir + "\n")
    return 1

def isValidUnit(row, col,rowColData):
    global minRow
    global minCol
    global maxRow
    global maxCol
    
    unitId = rowColData.get((row,col),-1)
    if ((row < minRow) or (col < minCol) or (row > maxRow) or (col > maxCol) or
        (unitId == -1) or (unitId == -9999)):
        return 0
    return 1
# End isValidUnit

def isDownwind(origRow, origCol, row, col, wind):

    if (wind == ""):
        return 1
    if (wind == "N"):
        if ((row == origRow+1 and col == origCol-1) or
            (row == origRow+1 and col == origCol) or
            (row == origRow+1 and col == origCol+1)):
            return 1
        return 0
    if (wind == "E"):
        if ((row == origRow-1 and col == origCol-1) or
            (row == origRow   and col == origCol-1) or
            (row == origRow+1 and col == origCol-1)):
            return 1
        return 0
    if (wind == "S"):
        if ((row == origRow-1 and col == origCol-1) or
            (row == origRow-1 and col == origCol) or
            (row == origRow+1 and col == origCol+1)):
            return 1
        return 0
    if (wind == "W"):
        if ((row == origRow-1 and col == origCol+1) or
            (row == origRow   and col == origCol+1) or
            (row == origRow+1 and col == origCol+1)):
            return 1
        return 0
    if (wind == "NE"):
        if ((row == origRow   and col == origCol-1) or
            (row == origRow+1 and col == origCol-1) or
            (row == origRow+1 and col == origCol)):
            return 1
        return 0
    if (wind == "SE"):
        if ((row == origRow-1 and col == origCol) or
            (row == origRow-1 and col == origCol-1) or
            (row == origRow   and col == origCol-1)):
            return 1
        return 0
    if (wind == "SW"):
        if ((row == origRow-1 and col == origCol) or
            (row == origRow-1 and col == origCol+1) or
            (row == origRow   and col == origCol+1)):
            return 1
        return 0
    if (wind == "NW"):
        if ((row == origRow   and col == origCol+1) or
            (row == origRow+1 and col == origCol+1) or
            (row == origRow+1 and col == origCol)):
            return 1
        return 0
    
    return 0
# End Function isDownwind

##def tempGDBName():
##    i = 1
##    path = makepath(gp.workspace,"tmp" + str(i) + ".mdb")
##    while (os.path.exists(path)):
##        i += 1
##        path = makepath(gp.workspace,"tmp" + str(i) + ".mdb")
##    return "tmp" + str(i)
def tempCoverName():
    i = 1
    path = makepath(gp.workspace,"tmpcov" + str(i))
    while (os.path.exists(path)):
        i += 1
        path = makepath(gp.workspace,"tmpcov" + str(i))
    return "tmpcov" + str(i)

def tempGridName():
    i = 1
    path = makepath(gp.workspace,"tmpgrid" + str(i))
    while (os.path.exists(path)):
        i += 1
        path = makepath(gp.workspace,"tmpgrid" + str(i))
    return "tmpgrid" + str(i)

def updateSlink(fc):   
    # Make sure the feature class has the field SLINK.
    # Also make sure slink matches OBJECTID
    fields = gp.ListFields(fc)
    field = fields.next()

    hasSlink = 0    
    while field:
        if (field.Name == "SLINK"):
            hasSlink = 1
            break;
        field = fields.next()
    #end while
    # This handles the case where someone creates an SLINK field that
    # is not large enough to hold the values.
    if (hasSlink):
        gp.deletefield_management(fc, "SLINK")
        
    gp.addfield_management(fc, "SLINK", "LONG")

    OIDFields = gp.ListFields(fc,"*","OID")
    OIDField = OIDFields.Next()
    OIDFieldExpr = "[" + OIDField.Name + "]"

    gp.calculatefield_management(fc, "SLINK", OIDFieldExpr)
    return 1
#end hasSlink

def buildTempGDB():
    global gdb
    global vegFC
    global landFC
    global streamFC
    global roadFC
    global trailFC
    global dem
    global gdb_outfile
    
    # Create a temporary geodatabase and copy the feature class and dem into it.
    # Later on we will add in the land feature and roads feature classes.
    tmplist = string.split(gdb_outfile,os.sep)
    gdbName = tmplist[len(tmplist)-1]

    if (ArcGIS_Version == "9.1"):
        gp.CreatePersonalGDB_management(workspace, gdbName)
    else:
        gp.CreateFileGDB_management(workspace, gdbName)
        
    gdb = gdb_outfile

    gp.FeatureClassToFeatureClass(inVegFC,gdb,"veg_polys")

    vegFC = makepath(gdb,"veg_polys")
    updateSlink(vegFC)

    dem   = ""
    if (inDem):
        dem = makepath(gdb,"dem")
        gp.CopyRaster(inDem,dem)

    if (inStreamFC):
        streamFC = processLineFC(inStreamFC,"streams")
    if (inLandFC):
        gp.FeatureClassToFeatureClass(inLandFC,gdb,"land")
        landFC = makepath(gdb,"land")
        updateSlink(landFC)
    if (inRoadFC):
        roadFC = processLineFC(inRoadFC,"roads")
    if (inTrailFC):
        trailFC = processLineFC(inTrailFC,"trails")

    return 0

def processLineFC(fc,newFCName):
    global vegFC
    
    tmpLineFC = makepath(gdb,"linetmp")
    lineFC    = makepath(gdb,newFCName)

    ## put cover in GeoDatabase
    gp.FeatureClassToFeatureClass(fc,gdb,"linetmp")

    if (hasFNodeTNode(tmpLineFC) == 0):
        ## This code seems to generate a M-Aware error on occassion
        ## which is why the check to see if FNODE/TNODE exist, because
        ## doing these steps manually via the tools doesn't generate
        ## the error.
        ##
        ## turn back into coverage and build topology
        cover = makepath(workspace,tempCoverName())
        gp.featureclasstocoverage_conversion(tmpLineFC + " ARC",cover)
        gp.delete(tmpLineFC)

        gp.build_arc(cover,"NODE")
        gp.build_arc(cover,"LINE")

        ## take arcs with fnode/tnode and put into database.
        gp.FeatureClassToFeatureClass(makepath(cover,"arc"),gdb,"linetmp")
        gp.Delete(cover)

    gp.Clip_analysis(tmpLineFC, vegFC, lineFC)
    gp.Delete(tmpLineFC)

    # Clip renumbers so we need to update slink        
    updateSlink(lineFC)

    return lineFC    
#end processLineFC

def hasFNodeTNode(fc):   
    hasFNode = 0
    hasTNode = 0
    fields = gp.ListFields(fc)
    field = fields.next()
    while field:
        if (field.Name == "FNODE_"):
            hasFNode = 1
        if (field.Name == "TNODE_"):
            hasTNode = 1
        if (hasFNode and hasTNode):
            return 1
        field = fields.next()
    #end while
    return 0
#end hasFNodeTNode
        
def hasRowCol(fc):   
    hasRow = 0
    hasCol = 0
    fields = gp.ListFields(fc)
    field = fields.next()
    while field:
        if (field.Name == "ROW"):
            hasRow = 1
        if (field.Name == "COL"):
            hasCol = 1
        if (hasRow and hasCol):
            return 1
        field = fields.next()
    #end while
    return 0
#end hasRowCol

def findMinMaxRowCol(fc):
    global minRow
    global maxRow
    global minCol
    global maxCol

    rowColData = {}
    
    rows = gp.SearchCursor(fc)
    tableRow = rows.Next()

    while tableRow:
        row = tableRow.GetValue("ROW")
        col = tableRow.GetValue("COL")
        rowColData[(row,col)] = tableRow.GetValue("SLINK")        
            
        if (row > maxRow):
            maxRow = row
        if (col > maxCol):
            maxCol = col
        if (row < minRow):
            minRow = row
        if (col < minCol):
            minCol = col
        tableRow = rows.Next()
    # End While
    del tableRow, rows
    return rowColData

def doElevationNew(fc, elevItem):
    global gdb
    global dem
    
    AddMsgAndPrint("Computing Elevation Using ExtractValuesToPoints for " + str(fc),0)

    if (elevItem):
        elev = {}
        rows = gp.SearchCursor(fc)
        tableRow = rows.Next()
        while tableRow:
            slink = tableRow.SLINK
            if (slink == -9999):
                tableRow = rows.Next()
                continue
            mean = long(round(tableRow.GetValue(elevItem)))
            elev[(slink)] = mean
            tableRow = rows.Next()
        #end while
        return elev
    #end if
                  
    AddMsgAndPrint("   Running Feature to Point ",0)
    pointFC = makepath(gdb,"tmp_point")
    
    gp.FeatureToPoint_management (fc, pointFC)
                  
    AddMsgAndPrint("   Extract Values to Points ",0)
    # Check out Spatial Analyst extension license
    gp.CheckOutExtension("Spatial")

    pointFCExt = makepath(gdb,"tmp_point_extract")
    gp.ExtractValuesToPoints_sa(pointFC, dem, pointFCExt, "INTERPOLATE", "VALUE_ONLY") 

    gp.CheckInExtension("Spatial")

    AddMsgAndPrint("   Processing elevation values. ",0)
    elev = {}
    rows = gp.SearchCursor(pointFCExt)
    tableRow = rows.Next()
    while tableRow:
        slink = tableRow.SLINK
        if (slink == -9999):
            tableRow = rows.Next()
            continue
        mean = long(round(tableRow.GetValue("RASTERVALU")))
        elev[(slink)] = mean
        tableRow = rows.Next()
    #end while

    #Cleanup
    del tableRow, rows
    gp.delete(pointFC)
    gp.delete(pointFCExt)

    return elev

#end doElevationNew

def doElevation(fc, elevItem):
    global gdb
    global dem
    
    AddMsgAndPrint("doElevation() for " + str(fc),0)

    if (elevItem):
        elev = {}
        rows = gp.SearchCursor(fc)
        tableRow = rows.Next()
        while tableRow:
            slink = tableRow.SLINK
            if (slink == -9999):
                tableRow = rows.Next()
                continue
            mean = long(round(tableRow.GetValue(elevItem)))
            elev[(slink)] = mean
            tableRow = rows.Next()
        #end while
        return elev
    #end if
            
    elevTbl = gdb + os.sep + "elevTbl"

    tmpgrid = makepath(workspace,tempGridName())
    gp.FeatureToRaster_conversion(fc, "SLINK", tmpgrid, 30)


    if (ArcGIS_Version == "9.1"):
        gp.BuildVat_simppllev2pt5(tmpgrid)
    else:
        gp.BuildRasterAttributeTable_management(tmpgrid,"#")

    # Check out Spatial Analyst extension license
    gp.CheckOutExtension("Spatial")

    gp.ZonalStatisticsAsTable_sa(tmpgrid, "VALUE", dem, elevTbl, "DATA")

    gp.CheckInExtension("Spatial")

    # It appears that ZonalStats creates a field of VALUE_ if the table
    # is in a database but used "VALUE" if outside a database (i.e. DBF)
    slinkField = "VALUE"
    fields = gp.ListFields(elevTbl)
    field = fields.next()
    while field:
        if (field.Name == "VALUE_"):
            slinkField = "VALUE_"
            break
        if (field.Name == "VALUE"):
            slinkField = "VALUE"
            break
        field = fields.next()
    #end while
    del fields
    del field
    
    elev = {}    
    rows = gp.SearchCursor(elevTbl)
    tableRow = rows.Next()
    while tableRow:
        slink = tableRow.GetValue(slinkField)
        if (slink == -9999):
            tableRow = rows.Next()
            continue
        mean = long(round(tableRow.MEAN))
        elev[(slink)] = mean
        tableRow = rows.Next()
    #end while

    #Cleanup
    del tableRow, rows
    gp.delete(elevTbl)
    gp.delete(tmpgrid)

    return elev
#end doElevation

## Find the polygons in poly2FC that are inside of polygons in poly1FC
##
def doPolyInPolys(outfile, poly1FC, poly2FC, sectionDescription):
    global gdb

    AddMsgAndPrint("Running PolyInPolys",0)

    polyPoly = gdb + os.sep + "poly_poly"

    gp.identity_analysis(poly1FC, poly2FC, polyPoly, "ONLY_FID")
  
    # Identity seems to differ from 9.0 to 9.1 with the naming of the FID field
    # so find the field with name containing with FID_
    vegFID  = ""
    vegSet  = 0
    landFID = ""
    landSet = 0


    fields = gp.ListFields(polyPoly)
    field = fields.next()
    while field:
        if (string.find(field.Name,"FID_veg_polys") != -1):
            vegFID = field.Name
            vegSet = 1
        if (string.find(field.Name,"FID_land") != -1):
            landFID = field.Name
            landSet = 1
        if (vegSet and landSet):
            break;
        field = fields.next()
    #end while
    del fields
    del field

    if (vegFID == "" and landFID == ""):
        AddMsgAndPrint("Could not find FID field after running Identity",2)
        sys.exit(2)

    fout = open(outfile,"a")
    fout.write("BEGIN " + sectionDescription + "\n")

    rows = gp.SearchCursor(polyPoly)
    tableRow = rows.Next()

    while tableRow:
        unit = tableRow.GetValue(vegFID)
        adj  = tableRow.GetValue(landFID)
        if (unit != -1 and adj != -1):
            fout.write(str(unit) + "," + str(adj) + "\n")
        tableRow = rows.Next()
    #end while
    del tableRow, rows
    gp.delete(polyPoly)

    fout.write("END\n\n")
    fout.close()
    return 1
#end doPolyInPolys

def doPolyOnArcs(outfile, polyFC, arcFC, sectionDescription, polyName, arcName):
    global gdb

    AddMsgAndPrint("Running PolyOnArcs",0)
    arcPoly = gdb + os.sep + "arc_poly"

    gp.identity_analysis(arcFC, polyFC, arcPoly, "ONLY_FID")

    polyFID = ""
    polySet = 0
    arcFID  = ""
    arcSet  = 0

    polyFIDFieldName = "FID_" + polyName
    arcFIDFieldName  = "FID_" + arcName
    
    fields = gp.ListFields(arcPoly)
    field = fields.next()
    while field:
        if (string.find(field.Name,polyFIDFieldName) != -1):
            polyFID = field.Name
            polySet = 1
        if (string.find(field.Name,arcFIDFieldName) != -1):
            arcFID = field.Name
            arcSet = 1
        if (polySet and arcSet):
            break;
        field = fields.next()
    #end while
    del fields
    del field

    if (polyFID == "" or arcFID == ""):
        AddMsgAndPrint("Could not find FID field after running Identity",2)
        sys.exit(2)

    fout = open(outfile,"a")
    fout.write("BEGIN " + sectionDescription + "\n")

    rows = gp.SearchCursor(arcPoly)
    tableRow = rows.Next()

    while tableRow:
        unit = tableRow.GetValue(arcFID)
        adj  = tableRow.GetValue(polyFID)
        if (unit != -1 and adj != -1):
            fout.write(str(unit) + "," + str(adj) + "\n")
        tableRow = rows.Next()
    #end while
    del tableRow, rows
    gp.delete(arcPoly)

    fout.write("END\n\n")
    fout.close()
    return 1
#end doPolyOnArcs

def doAdjacentArcs(outfile,arcFC,sectionDescription,doFlow):
    AddMsgAndPrint("Finding Adjacent Arcs ",0)

    nodeUnits = {}

    minNode = 9000000
    maxNode = 0

    OIDFields = gp.ListFields(arcFC,"*","OID")
    OIDField = OIDFields.Next()

    rows = gp.SearchCursor(arcFC)      
    tableRow = rows.Next()

    while tableRow:
        fnode = tableRow.FNODE_
        tnode = tableRow.TNODE_

        if (not nodeUnits.has_key(fnode)):
            nodeUnits[(fnode)] = []
        if (not nodeUnits.has_key(tnode)):           
            nodeUnits[(tnode)] = []
           
        nodeUnits[(fnode)].append(tableRow.GetValue(OIDField.Name))
        nodeUnits[(tnode)].append(tableRow.GetValue(OIDField.Name))

        minNode = min(fnode,tnode,minNode)
        maxNode = max(fnode,tnode,maxNode)
        
        tableRow = rows.Next()        
    #end while
    del tableRow, rows

    if (doFlow):
        elevData = doElevation(arcFC,"")

    fout = open(outfile,"a")
    fout.write("BEGIN " + sectionDescription + "\n")

    for node in range(minNode,maxNode+1):
        units = nodeUnits.get((node),[])
        size = len(units)
        # Use -1 for adjacent if no adjacent unit to make sure that
        # Island units will get included in the output.
        if (size == 0):
            continue
        if (size == 1):
            if (doFlow):
                flow = 'N'
                fout.write(str(units[0]) + ",-9999" + "," + flow + "\n")
            else:
                fout.write(str(units[0]) + ",-9999\n")
            continue

        for i in range(0,size):
            unit = units[i]
            for j in range(0,size):
                adjacent = units[j]
                if (unit == adjacent):
                    continue

                if (doFlow):
                    unitElev = elevData.get((unit),0)
                    adjElev = elevData.get((adjacent),0)
                    if (adjElev > unitElev):
                        flow = 'P'
                    elif (adjElev < unitElev):
                        flow = 'S'
                    else:
                        flow = 'N'

                    fout.write(str(unit) + "," + str(adjacent) + "," + flow + "\n")
                else:
                    fout.write(str(unit) + "," + str(adjacent) + "\n")
                    
            #end for j
        #end for i
    #end for node
    fout.write("END\n\n")
    fout.close()

    if (doFlow):    
        del elevData
    
    return 1
#end doAdjacentArcs

def doOldNeighbors(outfile,fc,sectionDescription):
    global vegFC
    global landFC
    global vegElevItem
    
    AddMsgAndPrint("Finding Neighbors for " + str(fc),0)

    # first copy fc into new coverage
    newcovName = tempCoverName()
    newcov = makepath(gp.workspace,newcovName)
    gp.featureclasstocoverage_conversion(fc + " POLYGON",  newcov)

    # update the labels
    gp.createlabels_arc(newcov)
    
    # Build topology    
    gp.build_arc(newcov,"NODE")
    gp.build_arc(newcov,"LINE")
    gp.build_arc(newcov,"POLY")

    #delete old fc
    gp.delete(fc)
    
    # determine new fc Names
    if (sectionDescription == "VEGETATION-VEGETATION"):
        polyName = "veg_polys"
        arcName  = "veg_arcs"
        vegFC = makepath(gdb,polyName)
        fc    = vegFC
        elevItem = vegElevItem
    elif (sectionDescription == "LANDFORM-LANDFORM"):
        polyName = "land_polys"
        arcName  = "land_arcs"
        landFC = makepath(gdb,polyName)
        fc = landFC
        elevItem = landElevItem

    # copy coverage into gdb    
    gp.FeatureClassToFeatureClass(makepath(newcov,"arc"),gdb,arcName)
    gp.FeatureClassToFeatureClass(makepath(newcov,"polygon"),gdb,polyName)

    updateSlink(fc)
    
    windData = doOldDownwind(gp.workspace,newcovName)
    elevData = doElevation(fc,elevItem)

    # delete coverage
    gp.delete(newcov)

    fout = open(outfile,"a")
    fout.write("BEGIN " + sectionDescription + "\n")
    
    rows = gp.SearchCursor(makepath(gdb,arcName))
    tableRow = rows.Next()

    while tableRow:
        lpoly = tableRow.LPOLY_
        rpoly = tableRow.RPOLY_

        if (lpoly <= 1 or rpoly <= 1):
            tableRow = rows.Next()
            continue

        wind = windData.get((lpoly,rpoly),"N")       
        elev = elevData.get((lpoly),0)
        fout.write(str(lpoly) + "," + str(rpoly) + "," + str(elev) + "," + str(wind) + "\n")

        wind = windData.get((rpoly,lpoly),"N")       
        elev = elevData.get((rpoly),0)
        fout.write(str(rpoly) + "," + str(lpoly) + "," + str(elev) + "," + str(wind) + "\n")
        
        tableRow = rows.Next()
    #end while
    del tableRow, rows

    fout.write("END\n\n")
    fout.close()

    del windData
    del elevData
    return 1
#end doOldNeighbors

def doOldNeighborsNew(outfile,fc,sectionDescription):
    global vegElevItem
    global landElevItem
    
    fcView = ""
    elevItem = ""
    if (sectionDescription == "VEGETATION-VEGETATION"):
        fcView   = "vegFC_view"
        elevItem = vegElevItem
        fcLines  = makepath(gdb,"vegFC_lines")
    elif (sectionDescription == "LANDFORM-LANDFORM"):
        fcView   = "landFC_view"
        elevItem = landElevItem
        fcLines  = makepath(gdb,"landFC_lines")


    updateSlink(fc)
    
    windData = doOldDownwind(fc)    
    elevData = doElevation(fc,elevItem)

    AddMsgAndPrint("Converting Features to Lines to get data for determining neighbors")
    gp.PolygonToLine_management(fc, fcLines)

    fout = open(outfile,"a")
    fout.write("BEGIN " + sectionDescription + "\n")

    rows = gp.SearchCursor(fcLines)
    tableRow = rows.Next()

    while tableRow:
        lpoly = tableRow.LEFT_FID
        rpoly = tableRow.RIGHT_FID

        if (lpoly <= 1 or rpoly <= 1):
            tableRow = rows.Next()
            continue

        wind = windData.get((lpoly,rpoly),"N")       
        elev = elevData.get((lpoly),0)
        fout.write(str(lpoly) + "," + str(rpoly) + "," + str(elev) + "," + str(wind) + "\n")

        wind = windData.get((rpoly,lpoly),"N")       
        elev = elevData.get((rpoly),0)
        fout.write(str(rpoly) + "," + str(lpoly) + "," + str(elev) + "," + str(wind) + "\n")
        
        tableRow = rows.Next()
    #end while
    del tableRow, rows
    gp.delete(fcLines)
    
    fout.write("END\n\n")
    fout.close()

    del windData
    del elevData
    
    return 1
#end doOldNeighborsNew
def doOldDownwind(fc):
    global gdb
    global workspace
    
    AddMsgAndPrint("Running Downwind for " + str(fc),0)

    # Idea here is to shift a copy of the feature class into the direction
    # that the wind is coming from, so that when we do a union we will find
    # the neighbors that are downwind of a given polygon.
    if (wind == "N"):
        dx = 0
        dy = 10
    elif (wind == "NE"):
        dx = 10
        dy = 10
    elif (wind == "E"):
        dx = 10
        dy = 0
    elif (wind == "SE"):
        dx = 10
        dy = -10
    elif (wind == "S"):
        dx = 0
        dy = -10
    elif (wind == "SW"):
        dx = -10
        dy = -10
    elif (wind == "W"):
        dx = -10
        dy = 0
    elif (wind == "NW"):
        dx = -10
        dy = 10
        
    shiftfc = makepath(gdb,"shiftveg")
        
    AddMsgAndPrint("Creating Shifted feature class to calculate downwind polygons")

    gp.ShiftFeatures_simppllev2pt5(fc, shiftfc, dx, dy)

    # Looks like shiftfeatures is changing workspace, restore to correct value.
    gp.Workspace = workspace
    
    input = str(fc) + "; " + str(shiftfc)
    unionfc = makepath(gdb,"unionveg")
    gp.Union_analysis(input, unionfc, "ONLY_FID")    

    gp.Delete(shiftfc)

    windData = {}
    
    rows = gp.SearchCursor(unionfc)
    tableRow = rows.Next()

    unitField = str("FID_veg_polys")
    adjField  = str("FID_shiftveg")
    
    while tableRow:
        unit = tableRow.GetValue(unitField)
        adj  = tableRow.GetValue(adjField)
        if (unit == adj or unit <= 1 or adj <= 1):
            tableRow = rows.Next()
            continue
        
        windData[(unit,adj)] = "D"
        tableRow = rows.Next()
    #end while
    del rows
    del tableRow

    gp.Delete(unionfc)
    return windData   
#end doOldDownwind
    
def doNeighbors(outfile,fc,sectionDescription):
    global gdb
    global minRow
    global maxRow
    global minCol
    global maxCol
    global vegElevItem
    global landElevItem
    
    rowColPresent = hasRowCol(fc)
    if (rowColPresent):
        rowColData = findMinMaxRowCol(fc)
    else:
        doOldNeighborsNew(outfile,fc,sectionDescription)
        return 1

    if (sectionDescription == "VEGETATION-VEGETATION"):
        elevItem = vegElevItem
    elif (sectionDescription == "LANDFORM-LANDFORM"):
        elevItem = landElevItem

    AddMsgAndPrint("Running Uniform Polygon Neighbors " + str(fc),0)
    elevData = doElevation(fc,elevItem)

    fout = open(outfile,"a")
    fout.write("BEGIN " + sectionDescription + "\n")
   
    for row in range(minRow,maxRow+1):
        for col in range(minCol,maxCol+1):
            unitId = rowColData.get((row,col),-1)
            if (unitId == -1 or unitId == -9999):
                continue

            if ((unitId in elevData) == 0):
                AddMsgAndPrint("In Feature Class: " + str(fc),1)
                msg = "  No Elevation Data for SLINK: " + str(unitId)
                AddMsgAndPrint(msg,1)
                elev = 0
            else:
                elev   = elevData[(unitId)]

            writeData(fout,row,col,row-1,col,elev,rowColData) ## N
            writeData(fout,row,col,row,col-1,elev,rowColData) ## W
            writeData(fout,row,col,row,col+1,elev,rowColData) ## E
            writeData(fout,row,col,row+1,col,elev,rowColData) ## S

            if (numNeighbors == 8):
                writeData(fout,row, col, row - 1, col - 1,elev,rowColData) ## NW
                writeData(fout,row, col, row - 1, col + 1,elev,rowColData) ## NE
                writeData(fout,row, col, row + 1, col - 1,elev,rowColData) ## SW
                writeData(fout,row, col, row + 1, col + 1,elev,rowColData) ## SE
        # end for c
    # end for r
    fout.write("END\n\n")
    fout.close()

    del rowColData
    del elevData
    return 1
#end doNeighbors
    
# ********************
# *** Main Routine ***
# ********************
def main():
    global gp
    global minRow
    global maxRow
    global minCol
    global maxCol
    global vegFC
    global landFC
    global streamFC
    global roadFC
    global trailFC
    global sr_outfile
    global att_outfile
    global workspace

    AddMsgAndPrint("Starting Main Routine ",0)

#    SIMPPLLE_toolbox = makepath(os.path.dirname(sys.argv[0]),"SIMPPLLE.tbx")
#    gp.AddToolbox(SIMPPLLE_toolbox)
        
    buildTempGDB()
  
    doNeighbors(sr_outfile,vegFC,"VEGETATION-VEGETATION")
    if (landFC):
        doNeighbors(sr_outfile,landFC,"LANDFORM-LANDFORM")
        doPolyInPolys(sr_outfile,vegFC,landFC,"VEGETATION-LANDFORM")

    if (streamFC):
        doAdjacentArcs(sr_outfile,streamFC,"AQUATIC-AQUATIC",1)
        doPolyOnArcs(sr_outfile,vegFC,streamFC,"VEGETATION-AQUATIC","veg_polys","streams")

    if (roadFC):
        doAdjacentArcs(sr_outfile,roadFC,"ROADS-ROADS",0)
        doPolyOnArcs(sr_outfile,vegFC,roadFC,"VEGETATION-ROADS","veg_polys","roads")

    if (trailFC):
        doAdjacentArcs(sr_outfile,trailFC,"TRAILS-TRAILS",0)
        doPolyOnArcs(sr_outfile,vegFC,trailFC,"VEGETATION-TRAILS","veg_polys","trails")

    if (landFC and roadFC):
        doPolyOnArcs(sr_outfile,landFC,roadFC,"LANDFORM-ROADS","land","roads")

    if (landFC and trailFC):
        doPolyOnArcs(sr_outfile,landFC,trailFC,"LANDFORM-TRAILS","land","trails")

    if (landFC and streamFC):
        doPolyOnArcs(sr_outfile,landFC,streamFC,"LANDFORM-AQUATIC","land","streams")
       
    gp.RefreshCatalog(workspace)

    ## Do the Attributes
##    SIMPPLLE_toolbox = os.environ['SIMPPLLE_HOME'] + os.sep + "gis" + os.sep + "python" + os.sep + "SIMPPLLE.tbx"
      
    AddMsgAndPrint("Running Attributes Script ",0)
    gp.Attributes_simppllev2pt5(workspace,att_outfile,vegFC,landFC,streamFC,roadFC,trailFC)
#    gp.RemoveToolbox(SIMPPLLE_toolbox)
    
    return 1    
# end Main routine

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



############################################################
## How to add row ##
##    gp.CreateTable_management(gdb,"nbr_arc")
##    nbrTable = makepath(gdb,"nbr")
##    gp.AddField(nbrTable,"UNIT","LONG")
##    gp.AddField(nbrTable,"ADJACENT","LONG")
##    gp.AddField(nbrTable,"ELEV","LONG")
##    gp.AddField(nbrTable,"FLOW","TEXT","","",1)
##
##    rows = gp.InsertCursor(nbrTable)
##    tableRow = rows.NewRow()
##    tableRow.UNIT     = rowColData[(origRow,origCol)]
##    tableRow.ADJACENT = rowColData[(row,col)]
##    rows.InsertRow(tableRow)
##    del tableRow
############################################################

##  downwind direct none aml code (doesn't work)
##    xtics = {1:xmin, 2:xmin, 3:xmax, 4:xmax}
##    ytics = {1:ymin, 2:ymax, 3:ymax, 4:ymin}
##
##    rows = gp.InsertCursor(makepath(cover,"tic"))
##    for id in range(1,5):
##        tableRow = rows.NewRow()
##        tableRow.IDTIC = id
##        tableRow.XTIC = xtics[id]
##        tableRow.YTIC = ytics[id]
##        rows.InsertRow(tableRow)
##    #end while
##    del rows
##    del tableRow
##    del xtics
##    del ytics
##
##    d = 10
##    if (wind == "N" or wind == "NE" or wind == "NW"):
##        ytics = {1:ymin+d, 2:ymax+d, 3:ymax+d, 4:ymin+d}
##    elif (wind == "S" or wind == "SE" or wind == "SW"):
##        ytics = {1:ymin-d, 2:ymax-d, 3:ymax-d, 4:ymin-d}
##    else:
##        ytics = {1:ymin, 2:ymax, 3:ymax, 4:ymin}
##
##    if (wind == "E" or wind == "NE" or wind == "SE"):
##        xtics = {1:xmin+d, 2:xmin+d, 3:xmax+d, 4:xmax+d}
##    elif (wind == "W" or wind == "SW" or wind == "NW"):
##        xtics = {1:xmin-d, 2:xmin-d, 3:xmax-d, 4:xmax-d}
##    else:
##        xtics = {1:xmin, 2:xmin, 3:xmax, 4:xmax}
##    rows = gp.InsertCursor(makepath(shiftCover,"tic"))
##    for id in range(1,5):
##        tableRow = rows.NewRow()
##        tableRow.IDTIC = id
##        tableRow.XTIC = xtics[id]
##        tableRow.YTIC = ytics[id]
##        rows.InsertRow(tableRow)
##    #end while
##    del rows
##    del tableRow
##    del xtics
##    del ytics

