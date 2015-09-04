######################################################################
 
# The University of Montana owns copyright of the designated documentation contained 
# within this file as part of the software product designated by Uniform Resource Identifier 
# UM-OpenSIMPPLLE-0.9.  By copying this file the user accepts the University of Montana 
# Open Source License Contract pertaining to this documentation and agrees to abide by all 
# restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
# 
# @author Documentation by Brian Losi
# <p>Original source code authorship: Kirk A. Moeller

## Purpose:  To extract attributes to create attributes file for
##           input into SIMPPLLE.
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

workspace   = gp.GetParameterAsText(0)
outfilename = gp.GetParameterAsText(1)
vegFC       = gp.GetParameterAsText(2)
landFC      = gp.GetParameterAsText(3) #optional
streamFC    = gp.GetParameterAsText(4) #optional
roadFC      = gp.GetParameterAsText(5) #optional
trailFC     = gp.GetParameterAsText(6) #optional

##workspace   = r"C:\MyDocuments\MyProjects\New-Zone-Legals\ColoradoPlateau\basin_10"
##outfilename = r"output"
##vegFC       = r"C:\MyDocuments\MyProjects\New-Zone-Legals\ColoradoPlateau\basin_10\basin_10veg2\polygon"
##landFC      = r"C:\MyDocuments\MyProjects\New-Zone-Legals\ColoradoPlateau\basin_10\basin_soils\polygon"
##streamFC    = r"C:\MyDocuments\MyProjects\New-Zone-Legals\ColoradoPlateau\basin_10\tmpgdb.mdb\streams"
##roadFC      = r"#"

if (vegFC == "#"):
    vegFC = ""
if (landFC == "#"):
    landFC = ""
if (streamFC == "#"):
    streamFC = ""
if (roadFC == "#"):
    roadFC = ""
if (trailFC == "#"):
    trailFC = ""

if (string.find(outfilename,".attributesall") == -1):
    outfile = makepath(workspace,outfilename) + ".attributesall"

    i = 1
    while (os.path.exists(outfile)):
        i += 1
        outfilename = outfilename + str(i)
        outfile = makepath(workspace,outfilename) + ".attributesall"
    #end while
else:
    outfile = outfilename

## Turn value into a string and strip away white space
## if it is empty than make it the no data string "?"
def processTableValue(value):
    result = string.strip(str(value))
    if (len(result) == 0):
        result = "?"
    return result
#end processTableValue

def doVegAttributes(fc, file):
    AddMsgAndPrint("Generating Vegetation Attributes",0)
    
    fout = open(file,"a")
    fout.write("BEGIN VEGETATION\n")

    slink     = "SLINK"
    species   = "SPECIES"
    size      = "SIZE_CLASS"
    density   = "DENSITY"
    group     = "HT_GRP"
    acres     = "ACRES"
    row       = "ROW"
    col       = "COL"
    stand_id  = "STAND_ID"
    ownership = "OWNERSHIP"
    roads     = "ROADS"
    fire      = "FIRE"
    fmz       = "FMZ"
    sp_area   = "SP_AREA"
    landtype  = "LANDTYPE"
    process   = "PROCESS"
    treatment = "TREATMENT"
    process_t   = "PROCESS_T"
    treatment_t = "TREAT_T"
    point_x     = "POINT_X"
    point_y     = "POINT_Y"


    speciesL = ["SPECIES_TR", "SPECIES_S", "SPECIES_H", "SPECIES_A", "SPECIES_N"]
    sizeL    = ["SIZE_TR", "SIZE_S", "SIZE_H", "SIZE_A", "SIZE_N"]
    densityL = ["DENSITY_TR", "DENSITY_S", "DENSITY_H", "DENSITY_A", "DENSITY_N"]
    procL    = ["PROC_TR", "PROC_S", "PROC_H", "PROC_A", "PROC_N"]
    treatL   = ["TREAT_TR", "TREAT_S", "TREAT_H", "TREAT_A", "TREAT_N"]
    proc_tL  = ["PROC_TR_T", "PROC_S_T", "PROC_H_T", "PROC_A_T", "PROC_N_T"]
    treat_tL = ["TREAT_TR_T", "TREAT_S_T", "TREAT_H_T", "TREAT_A_T", "TREAT_N_T"]

    sptk     = {}
    sptk_pct = {}

    max_lf_count = 5
    max_sp_count = 20
    for i in range(1,max_sp_count+1):
        sptk[(0,i)]     = "SPTK_TR" + str(i)
        sptk_pct[(0,i)] = "SPTK_TR" + str(i) + "P"

        sptk[(1,i)]     = "SPTK_S" + str(i)
        sptk_pct[(1,i)] = "SPTK_S" + str(i) + "P"

        sptk[(2,i)]     = "SPTK_H" + str(i)
        sptk_pct[(2,i)] = "SPTK_H" + str(i) + "P"

        sptk[(3,i)]     = "SPTK_A" + str(i)
        sptk_pct[(3,i)] = "SPTK_A" + str(i) + "P"

        sptk[(4,i)]     = "SPTK_N" + str(i)
        sptk_pct[(4,i)] = "SPTK_N" + str(i) + "P"
    #end for


    hasSptk     = {}
    hasSptk_pct = {}
    for lf in range(0,max_lf_count):
        for sp in range(1,max_sp_count+1):
            hasSptk[(lf,sp)]     = 0
            hasSptk_pct[(lf,sp)] = 0
        #end for
    #end for


    hasSlink       = 0
    hasSpecies     = 0
    hasSize        = 0
    hasDensity     = 0
    hasGroup       = 0
    hasAcres       = 0
    hasRow         = 0
    hasCol         = 0
    hasStand_id    = 0
    hasOwnership   = 0
    hasRoads       = 0
    hasFire        = 0
    hasFmz         = 0
    hasSp_area     = 0
    hasLandtype    = 0
    hasProcess     = 0
    hasTreatment   = 0
    hasProcess_t   = 0
    hasTreatment_t = 0
    hasPoint_x     = 0
    hasPoint_y     = 0
   
    hasSpeciesL = [0,0,0,0,0]
    hasSizeL    = [0,0,0,0,0]
    hasDensityL = [0,0,0,0,0]
    hasProcL    = [0,0,0,0,0]
    hasTreatL   = [0,0,0,0,0]
    hasProc_tL  = [0,0,0,0,0]
    hasTreat_tL = [0,0,0,0,0]
  
    fields = gp.ListFields(fc)
    field = fields.next()
    while field:
        if (field.Name == slink):
            hasSlink = 1
        if (field.Name == species):
            hasSpecies = 1
        if (field.Name == size):
            hasSize = 1
        if (field.Name == density):
            hasDensity = 1
        if (field.Name == group):
            hasGroup = 1
        if (field.Name == acres):
            hasAcres = 1
        if (field.Name == row):
            hasRow = 1
        if (field.Name == col):
            hasCol = 1
        if (field.Name == stand_id):
            hasStand_id = 1
        if (field.Name == ownership):
            hasOwnership = 1
        if (field.Name == roads):
            hasRoads = 1
        if (field.Name == fire):
            hasFire = 1
        if (field.Name == fmz):
            hasFmz = 1
        if (field.Name == sp_area):
            hasSp_area = 1
        if (field.Name == landtype):
            hasLandtype = 1
        if (field.Name == process):
            hasProcess = 1
        if (field.Name == treatment):
            hasTreatment = 1
        if (field.Name == process_t):
            hasProcess_t = 1
        if (field.Name == treatment_t):
            hasTreatment_t = 1
        if (field.Name == point_x):
            point_x = 1
        if (field.Name == point_y):
            point_y = 1
           
        for l in range(0,max_lf_count):
            if (field.Name == speciesL[l]):
                hasSpeciesL[l] = 1
            if (field.Name == sizeL[l]):
                hasSizeL[l] = 1
            if (field.Name == densityL[l]):
                hasDensityL[l] = 1
            if (field.Name == procL[l]):
                hasProcL[l] = 1
            if (field.Name == treatL[l]):
                hasTreatL[l] = 1
            if (field.Name == proc_tL[l]):
                hasProc_tL[l] = 1
            if (field.Name == treat_tL[l]):
                hasTreat_tL[l] = 1
        #end for

        for lf in range(0,max_lf_count):
            for sp in range(1,max_sp_count+1):
                if (field.Name == sptk[(lf,sp)]):
                    hasSptk[(lf,sp)]     = 1
                if (field.Name == sptk_pct[(lf,sp)]):
                    hasSptk_pct[(lf,sp)] = 1
            #end for
        #end for

            
        field = fields.next()
    #end while
    lf_count = 1
    # SIMPPLLE depends on count being > 1 if multiple lifeforms.
    if (hasSpeciesL[0] or hasSpeciesL[1] or hasSpeciesL[2]):
        lf_count = 5
        
    if (hasGroup == 0):
        AddMsgAndPrint("Missing HT_GRP in table",2)
        raise Exception, "Missing Attributes"

    if (hasAcres == 0):
        AddMsgAndPrint("Missing ACRES in table",2)
        raise Exception, "Missing Attributes"

    nodata = "?"
    spdelim = "#"

    rows = gp.SearchCursor(fc)
    tableRow = rows.Next()
    while tableRow:
        if (slink <= 1):
            tableRow = rows.Next()
            continue

        slinkStr = str(tableRow.GetValue(slink))
        rowStr       = nodata
        colStr       = nodata
        stand_idStr  = nodata
        acresStr     = nodata
        groupStr     = nodata
        ownershipStr = nodata
        roadsStr     = nodata
        fireStr      = nodata
        fmzStr       = nodata
        sp_areaStr   = nodata
        landtypeStr  = nodata
        point_xStr   = nodata
        point_yStr   = nodata

        if (hasRow):        
            rowStr = processTableValue(tableRow.GetValue(row))
        if (hasCol):        
            colStr = processTableValue(tableRow.GetValue(col))
        if (hasStand_id):        
            stand_idStr  = processTableValue(tableRow.GetValue(stand_id))
        if (hasAcres):        
            acresStr     = processTableValue(tableRow.GetValue(acres))
        if (hasGroup):        
            groupStr     = processTableValue(tableRow.GetValue(group))
        if (hasOwnership):        
            ownershipStr = processTableValue(tableRow.GetValue(ownership))
        if (hasRoads):        
            roadsStr     = processTableValue(tableRow.GetValue(roads))
        if (hasFire):        
            fireStr      = processTableValue(tableRow.GetValue(fire))
        if (hasFmz):        
            fmzStr       = processTableValue(tableRow.GetValue(fmz))
        if (hasSp_area):        
            sp_areaStr   = processTableValue(tableRow.GetValue(sp_area))
        if (hasLandtype):        
            landtypeStr  = processTableValue(tableRow.GetValue(landtype))
        if (hasPoint_x):        
            hasPoint_xStr  = processTableValue(tableRow.GetValue(point_x))
        if (hasPoint_y):        
            hasPoint_yStr  = processTableValue(tableRow.GetValue(point_y))

        fields = str(lf_count)
        if (lf_count == 1):
            speciesStr     = nodata
            sizeStr        = nodata
            densityStr     = nodata
            processStr     = nodata
            treatmentStr   = nodata
            process_tStr   = nodata
            treatment_tStr = nodata
            
            if (hasSpecies):
                speciesStr     = processTableValue(tableRow.GetValue(species))
            if (hasSize):
                sizeStr        = processTableValue(tableRow.GetValue(size))
            if (hasDensity):
                densityStr     = processTableValue(tableRow.GetValue(density))
            if (hasProcess):
                processStr     = processTableValue(tableRow.GetValue(process))
            if (hasTreatment):
                treatmentStr   = processTableValue(tableRow.GetValue(treatment))
            if (hasProcess_t):
                process_tStr   = processTableValue(tableRow.GetValue(process_t))
            if (hasTreatment_t):
                treatment_tStr = processTableValue(tableRow.GetValue(treatment_t))
            
            fields = fields + "," + speciesStr + "," + sizeStr + "," + densityStr + "," + processStr + "," + \
                     process_tStr + "," + treatmentStr + "," + treatment_tStr

            ## Tracking Species
            tmpCount = 0
            trackStr = ""
            for sp in range(1,max_sp_count+1):
                trackSpeciesStr = nodata
                trackSpeciesPctStr = nodata

                # We can't be sure which lifeform index holds the species so search for it.                    
                for lf in range(0,max_lf_count):
                    finish = 0
                    if (hasSptk[(lf,sp)]):
                        trackSpeciesStr = processTableValue(tableRow.GetValue(sptk[(lf,sp)]))
                        tmpCount += 1
                        finish = 1
                    if (hasSptk_pct[(lf,sp)]):
                        trackSpeciesPctStr = processTableValue(tableRow.GetValue(sptk_pct[(lf,sp)]))
                        finish = 1
                    if (finish):
                        break

                trackStr = trackStr + "," + trackSpeciesStr + "," + trackSpeciesPctStr            
            #end for
            if (tmpCount > 0):
                fields = fields + "," + str(max_sp_count) + trackStr
            else:
                fields = fields + "," + nodata
        #end if
        else:
            for lf in range(0,lf_count):
                speciesStr     = nodata
                sizeStr        = nodata
                densityStr     = nodata
                processStr     = nodata
                treatmentStr   = nodata
                process_tStr   = nodata
                treatment_tStr = nodata
                
                if (hasSpeciesL[lf]):
                    speciesStr = processTableValue(tableRow.GetValue(speciesL[lf]))
                if (hasSizeL[lf]):
                    sizeStr = processTableValue(tableRow.GetValue(sizeL[lf]))
                if (hasDensityL[lf]):
                    densityStr = processTableValue(tableRow.GetValue(densityL[lf]))
                if (hasProcL[lf]):
                    processStr = processTableValue(tableRow.GetValue(procL[lf]))
                if (hasProc_tL[lf]):
                    process_tStr = processTableValue(tableRow.GetValue(proc_tL[lf]))
                if (hasTreatL[lf]):
                    treatmentStr = processTableValue(tableRow.GetValue(treatL[lf]))
                if (hasTreat_tL[lf]):
                    treatment_tStr = processTableValue(tableRow.GetValue(treat_tL[lf]))
                    
                fields = fields + "," + speciesStr + "," + sizeStr + "," + densityStr + "," + processStr + "," + \
                         process_tStr + "," + treatmentStr + "," + treatment_tStr

                ## Tracking Species
                tmpCount = 0
                trackStr = ""                    
                for boo in range(1,max_sp_count+1):
                    trackSpeciesStr = nodata
                    trackSpeciesPctStr = nodata
                    
                    if (hasSptk[(lf,boo)]):
                        trackSpeciesStr = processTableValue(tableRow.GetValue(sptk[(lf,boo)]))
                        tmpCount += 1
                    if (hasSptk_pct[(lf,boo)]):
                        trackSpeciesPctStr = processTableValue(tableRow.GetValue(sptk_pct[(lf,boo)]))

                    trackStr = trackStr + "," + trackSpeciesStr + "," + trackSpeciesPctStr            
                #end for
                if (tmpCount > 0):
                    fields = fields + "," + str(max_sp_count) + trackStr
                else:
                    fields = fields + "," + nodata
            #end for
        #end else

        fout.write(slinkStr + "," +
                   rowStr + "," +
                   colStr + "," +
                   stand_idStr + "," +
                   acresStr + "," +
                   groupStr + "," +
                   spdelim + "," + fields + "," + spdelim + "," +
                   str(ownershipStr) + "," +
                   str(roadsStr) + "," +
                   str(fireStr) + "," +
                   str(fmzStr) + "," +
                   str(sp_areaStr) + "," +
                   str(landtypeStr) + "," +
                   str(point_xStr) + "," +
                   str(point_yStr) + "\n")

        tableRow = rows.Next()
    #end while
        
    fout.write("END\n\n")
    fout.close()  
#end def

def doLandAttributes(fc, file):
##    // attributes in the file in the following order:
##    // slink(id),acres,soilType,landform,aspect,slope,parent material
    AddMsgAndPrint("Generating Land Attributes",0)
    
    fout = open(file,"a")
    fout.write("BEGIN LANDFORM\n")

    nodata = "?"

    hasSlink = 0
    hasAcres = 0
    hasSoil  = 0
    hasLand  = 0
    hasSlope = 0
    hasAspect = 0
    hasParent = 0
    hasDepth  = 0
    hasPointX = 0
    hasPointY = 0
    
    fields = gp.ListFields(fc)
    field = fields.next()
    while field:
        if (field.Name == "SLINK"):
            hasSlink = 1
        if (field.Name == "ACRES"):
            hasAcres = 1
        if (field.Name == "SOIL_TYPE"):
            hasSoil = 1
        if (field.Name == "LANDFORM"):
            hasLand = 1
        if (field.Name == "SLOPE"):
            hasSlope = 1
        if (field.Name == "ASPECT"):
            hasAspect = 1
        if (field.Name == "PARENT_MAT"):
            hasParent = 1
        if (field.Name == "DEPTH"):
            hasDepth = 1
        if (field.Name == "POINT_X"):
            hasPointX = 1
        if (field.Name == "POINT_Y"):
            hasPointY = 1
        field = fields.next()
    #end while

    if ((hasSlink and hasAcres and hasSoil and hasLand and hasSlope and hasAspect and hasParent and hasDepth) == 0):
        AddMsgAndPrint("Missing Attributes in Land table",2)
        raise Exception, "Missing Attributes"

    rows = gp.SearchCursor(fc)
    tableRow = rows.Next()
    while tableRow:
        slink          = processTableValue(tableRow.GetValue("SLINK"))
        acres          = processTableValue(tableRow.GetValue("ACRES"))
        soilType       = processTableValue(tableRow.GetValue("SOIL_TYPE"))
        landform       = processTableValue(tableRow.GetValue("LANDFORM"))
        slope          = processTableValue(tableRow.GetValue("SLOPE"))
        aspect         = processTableValue(tableRow.GetValue("ASPECT"))
        parentMaterial = processTableValue(tableRow.GetValue("PARENT_MAT"))
        depth          = processTableValue(tableRow.GetValue("DEPTH"))

        pointX = nodata
        pointY = nodata
        if (hasPointX):
            pointX = processTableValue(tableRow.GetValue("POINT_X"))
        if (hasPointY):
            pointY = processTableValue(tableRow.GetValue("POINT_Y"))

        fout.write(str(slink) + "," +
                   str(acres) + "," +
                   str(soilType) + "," +
                   str(landform) + "," +
                   str(aspect) + "," +
                   str(slope) + "," +
                   str(parentMaterial) + "," +
                   str(depth) + "," +
                   str(pointX) + "," +
                   str(pointY) + "\n")

        tableRow = rows.Next()        
    #end while


    fout.write("END\n\n")
    fout.close()  
#end doLandAttributes

def doAquaticAttributes(fc, file):
##    // attributes in the file in the following order:
##    // slink(id),length,lta valley segment group,aquatic class,
##    // aquatic attribute,segment #,initial Process,status
    AddMsgAndPrint("Generating Aquatic Attributes",0)
    
    fout = open(file,"a")
    fout.write("BEGIN AQUATIC\n")

    hasSlink      = 0
    hasLength     = 0
    hasGroup      = 0
    hasClass      = 0
    hasAttribute  = 0
    hasSegmentNum = 0
    hasProcess    = 0
    hasStatus     = 0
    
    fields = gp.ListFields(fc)
    field = fields.next()
    while field:
        if (field.Name == "SLINK"):
            hasSlink = 1
        if (field.Name == "LENGTH"):
            hasLength = 1
        if (field.Name == "LTA_GRP"):
            hasGroup = 1
        if (field.Name == "AQ_CLASS"):
            hasClass = 1
        if (field.Name == "AQ_ATTRIB"):
            hasAttribute = 1
        if (field.Name == "SEG_NUM"):
            hasSegmentNum = 1
        if (field.Name == "INIT_PROC"):
            hasProcess = 1
        if (field.Name == "STATUS"):
            hasStatus = 1
        field = fields.next()
    #end while

    if ((hasSlink and hasLength and hasGroup and hasClass and hasAttribute and hasSegmentNum and hasProcess and hasStatus) == 0):
        AddMsgAndPrint("Missing Attributes in Aquatics table",2)
        raise Exception, "Missing Attributes"

    rows = gp.SearchCursor(fc)
    tableRow = rows.Next()
    while tableRow:
        slink       = processTableValue(tableRow.GetValue("SLINK"))
        length      = processTableValue(tableRow.GetValue("LENGTH"))
        group       = processTableValue(tableRow.GetValue("LTA_GRP"))
        aqClass     = processTableValue(tableRow.GetValue("AQ_CLASS"))
        aqAttrib    = processTableValue(tableRow.GetValue("AQ_ATTRIB"))
        segNum      = processTableValue(tableRow.GetValue("SEG_NUM"))
        initProcess = processTableValue(tableRow.GetValue("INIT_PROC"))
        status      = processTableValue(tableRow.GetValue("STATUS"))

        fout.write(str(slink) + "," +
                   str(length) + "," +
                   str(group) + "," +
                   str(aqClass) + "," +
                   str(aqAttrib) + "," +
                   str(segNum) + "," +
                   str(initProcess) + "," +
                   str(status) + "\n")

        tableRow = rows.Next()        
    #end while


    fout.write("END\n\n")
    fout.close()  
    
#end doAquaticAttributes
  
def doRoads(fc, file):
    AddMsgAndPrint("Generating Road Attributes",0)
    
    fout = open(file,"a")
    fout.write("BEGIN ROADS\n")

    hasSlink  = 0
    hasStatus = 0
    hasKind   = 0
    
    fields = gp.ListFields(fc)
    field = fields.next()
    while field:
        if (field.Name == "SLINK"):
            hasSlink = 1
        if (field.Name == "STATUS"):
            hasStatus = 1
        if (field.Name == "KIND"):
            hasKind = 1
        field = fields.next()
    #end while

    if ((hasSlink and hasStatus and hasKind) == 0):
        AddMsgAndPrint("Missing Attributes in Roads table",2)
        raise Exception, "Missing Attributes"

    rows = gp.SearchCursor(fc)
    tableRow = rows.Next()
    while tableRow:
        slink  = processTableValue(tableRow.GetValue("SLINK"))
        status = processTableValue(tableRow.GetValue("STATUS"))
        kind   = processTableValue(tableRow.GetValue("KIND"))

        fout.write(str(slink) + "," +
                   str(status) + "," +
                   str(kind) + "\n")

        tableRow = rows.Next()        
    #end while

    fout.write("END\n\n")
    fout.close()  
#end doRoads

def doTrails(fc, file):
    AddMsgAndPrint("Generating Road Attributes",0)
    
    fout = open(file,"a")
    fout.write("BEGIN TRAILS\n")

    hasSlink  = 0
    hasStatus = 0
    hasKind   = 0
    
    fields = gp.ListFields(fc)
    field = fields.next()
    while field:
        if (field.Name == "SLINK"):
            hasSlink = 1
        if (field.Name == "STATUS"):
            hasStatus = 1
        if (field.Name == "KIND"):
            hasKind = 1
        field = fields.next()
    #end while

    if ((hasSlink and hasStatus and hasKind) == 0):
        AddMsgAndPrint("Missing Attributes in Trails table",2)
        raise Exception, "Missing Attributes"

    rows = gp.SearchCursor(fc)
    tableRow = rows.Next()
    while tableRow:
        slink  = processTableValue(tableRow.GetValue("SLINK"))
        status = processTableValue(tableRow.GetValue("STATUS"))
        kind   = processTableValue(tableRow.GetValue("KIND"))

        fout.write(str(slink) + "," +
                   str(status) + "," +
                   str(kind) + "\n")

        tableRow = rows.Next()        
    #end while

    fout.write("END\n\n")
    fout.close()  
#end doRoads

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

def main():
    global vegFC
    global landFC
    global streamFC
    global roadFC
    global trailFC
    global outfile

    doVegAttributes(vegFC,outfile)
    if (landFC):
        doLandAttributes(landFC,outfile)
    if (streamFC):
        doAquaticAttributes(streamFC,outfile)
    if (roadFC):
        doRoads(roadFC,outfile)
    if (trailFC):
        doTrails(trailFC,outfile)
#end main

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

    
