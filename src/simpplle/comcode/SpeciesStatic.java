/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;


/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-0.9.  By copying this file the user accepts the University of Montana 
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This interface defines the static final variables representing different species used throughout OpenSimpplle. 
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 *
 *
 */


public interface SpeciesStatic {
  public static final Species UNKNOWN = new Species("UNKNOWN");

  public static final Species AGR           = new Species("AGR",false);
  public static final Species ALPINE_SHRUBS = new Species("ALPINE-SHRUBS",false);
  public static final Species CW            = new Species("CW",false);
  public static final Species DF            = new Species("DF",false);
  public static final Species LP            = new Species("LP",false);
  public static final Species MESIC_SHRUBS  = new Species("MESIC-SHRUBS",false);
  public static final Species MH            = new Species("MH",false);
  public static final Species ND            = new Species("ND",false);
  public static final Species NF            = new Species("NF",false);
  public static final Species NS            = new Species("NS",false);
  public static final Species PP            = new Species("PP",false);
  public static final Species QA            = new Species("QA",false);
  public static final Species RIP_SHRUBS    = new Species("RIP-SHRUBS",false);
  public static final Species WATER         = new Species("WATER",false);
  public static final Species WB            = new Species("WB",false);
  public static final Species WH            = new Species("WH",false);
  public static final Species WP            = new Species("WP",false);
  public static final Species XERIC_SHRUBS  = new Species("XERIC-SHRUBS",false);

  // ***********************************
  // *** Eastside and Westside Zones ***
  // ***********************************

  public static final Species AF              = new Species("AF",false);
  public static final Species ALPINE_GRASSES  = new Species("ALPINE-GRASSES",false);
  public static final Species ALTERED_GRASSES = new Species("ALTERED-GRASSES",false);
  public static final Species CW_MC           = new Species("CW-MC",false);
  public static final Species DF_AF           = new Species("DF-AF",false);
  public static final Species DF_ES           = new Species("DF-ES",false);
  public static final Species DF_LP           = new Species("DF-LP",false);
  public static final Species DF_LP_AF        = new Species("DF-LP-AF",false);
  public static final Species DF_LP_ES        = new Species("DF-LP-ES",false);
  public static final Species DF_PP_LP        = new Species("DF-PP-LP",false);
  public static final Species EARLY_SERAL     = new Species("EARLY-SERAL",false);
  public static final Species ES              = new Species("ES",false);
  public static final Species ES_AF           = new Species("ES-AF",false);
  public static final Species FESCUE          = new Species("FESCUE",false);
  public static final Species LATE_SERAL      = new Species("LATE-SERAL",false);
  public static final Species LP_AF           = new Species("LP-AF",false);
  public static final Species NATIVE_FORBS    = new Species("NATIVE-FORBS",false);
  public static final Species PF              = new Species("PF",false);
  public static final Species PP_DF           = new Species("PP-DF",false);
  public static final Species QA_MC           = new Species("QA-MC",false);
  public static final Species UPLAND_GRASSES  = new Species("UPLAND-GRASSES",false);
  public static final Species WB_ES_AF        = new Species("WB-ES-AF",false);

  // Added because of runtime errors
  public static final Species DF_PP           = new Species("DF-PP",false);

  // ***************************
  // *** Westside Region One ***
  // ***************************

  public static final Species AF_ES_MH         = new Species("AF-ES-MH",false);
  public static final Species AF_MH            = new Species("AF-MH",false);
  public static final Species AL               = new Species("AL",false);
  public static final Species AL_AF            = new Species("AL-AF",false);
  public static final Species AL_WB_AF         = new Species("AL-WB-AF",false);
  public static final Species C                = new Species("C",false);
  public static final Species DF_GF            = new Species("DF-GF",false);
  public static final Species DF_LP_GF         = new Species("DF-LP-GF",false);
  public static final Species DF_PP_GF         = new Species("DF-PP-GF",false);
  public static final Species DF_RRWP          = new Species("DF-RRWP",false);
  public static final Species DF_RRWP_GF       = new Species("DF-RRWP-GF",false);
  public static final Species DF_WP            = new Species("DF-WP",false);
  public static final Species DF_WP_GF         = new Species("DF-WP-GF",false);
  public static final Species GF               = new Species("GF",false);
  public static final Species L                = new Species("L",false);
  public static final Species L_DF             = new Species("L-DF",false);
  public static final Species L_DF_AF          = new Species("L-DF-AF",false);
  public static final Species L_DF_ES          = new Species("L-DF-ES",false);
  public static final Species L_DF_GF          = new Species("L-DF-GF",false);
  public static final Species L_DF_LP          = new Species("L-DF-LP",false);
  public static final Species L_DF_PP          = new Species("L-DF-PP",false);
  public static final Species L_DF_RRWP        = new Species("L-DF-RRWP",false);
  public static final Species L_DF_WP          = new Species("L-DF-WP",false);
  public static final Species L_ES             = new Species("L-ES",false);
  public static final Species L_ES_AF          = new Species("L-ES-AF",false);
  public static final Species L_GF             = new Species("L-GF",false);
  public static final Species L_LP             = new Species("L-LP",false);
  public static final Species L_LP_GF          = new Species("L-LP-GF",false);
  public static final Species LP_GF            = new Species("LP-GF",false);
  public static final Species L_PP             = new Species("L-PP",false);
  public static final Species L_PP_LP          = new Species("L-PP-LP",false);
  public static final Species L_RRWP           = new Species("L-RRWP",false);
  public static final Species L_RRWP_GF        = new Species("L-RRWP-GF",false);
  public static final Species L_WP             = new Species("L-WP",false);
  public static final Species L_WP_GF          = new Species("L-WP-GF",false);
  public static final Species MAHOGANY         = new Species("MAHOGANY",false);
  public static final Species RIPARIAN_GRASSES = new Species("RIPARIAN-GRASSES",false);
  public static final Species RIPARIAN_SHRUBS  = new Species("RIPARIAN-SHRUBS",false);
  public static final Species RRWP             = new Species("RRWP",false);
  public static final Species WH_C             = new Species("WH-C",false);
  public static final Species WH_C_GF          = new Species("WH-C-GF",false);
  public static final Species WOODLAND         = new Species("WOODLAND",false);

  public static final Species DF_C             = new Species("DF-C",false);
  public static final Species L_DF_C           = new Species("L-DF-C",false);
  public static final Species L_DF_WH          = new Species("L-DF-WH",false);
  public static final Species L_WH             = new Species("L-WH",false);
  public static final Species DF_WH            = new Species("DF-WH",false);
  public static final Species L_C              = new Species("L-C",false);
  public static final Species LP_ES            = new Species("LP-ES",false);
  public static final Species LP_ES_AF         = new Species("LP-ES-AF",false);

  // For Kipz Area
  public static final Species L_LP_AF = new Species("L-LP-AF",false);

  // ***************************
  // *** Eastside Region One ***
  // ***************************

  public static final Species AF_ES_LP        = new Species("AF-ES-LP",false);
  public static final Species AGSP            = new Species("AGSP",false);
  public static final Species ALPINE_HERBS    = new Species("ALPINE-HERBS",false);
  public static final Species ALTERED_NOXIOUS = new Species("ALTERED-NOXIOUS",false);
  public static final Species DF_AF_ES        = new Species("DF-AF-ES",false);
  public static final Species DF_PP_PF        = new Species("DF-PP-PF",false);
  public static final Species ES_LP           = new Species("ES-LP",false);
  public static final Species FS_S_G          = new Species("FS-S-G",false);
  public static final Species GA              = new Species("GA",false);
  public static final Species HERBS           = new Species("HERBS",false);
  public static final Species JUSC            = new Species("JUSC",false);
  public static final Species JUSC_AGSP       = new Species("JUSC-AGSP",false);
  public static final Species JUSC_ORMI       = new Species("JUSC-ORMI",false);
  public static final Species MID_SERAL       = new Species("MID-SERAL",false);
  public static final Species MTN_FS_SHRUBS   = new Species("MTN-FS-SHRUBS",false);
  public static final Species MTN_MAHOGANY    = new Species("MTN-MAHOGANY",false);
  public static final Species MTN_SHRUBS      = new Species("MTN-SHRUBS",false);
  public static final Species NOXIOUS         = new Species("NOXIOUS",false);
  public static final Species PF_LP           = new Species("PF-LP",false);
  public static final Species RIP_DECID       = new Species("RIP-DECID",false);
  public static final Species RIP_DECID_MC    = new Species("RIP-DECID-MC",false);
  public static final Species RIP_GRAMS       = new Species("RIP-GRAMS",false);
  public static final Species RIP_S_GRAMS     = new Species("RIP-S-GRAMS",false);
  public static final Species WB_AF           = new Species("WB-AF",false);
  public static final Species WB_DF           = new Species("WB-DF",false);
  public static final Species WB_ES           = new Species("WB-ES",false);
  public static final Species WB_ES_LP        = new Species("WB-ES-LP",false);
  public static final Species WB_LP_AF        = new Species("WB-LP-AF",false);
  public static final Species XERIC_FS_SHRUBS = new Species("XERIC-FS-SHRUBS",false);

  // ***************************************************
  // *** Sierra Nevada and Southern California Zones ***
  // ***************************************************

  /**
   * @todo replace trees with the proper lifeform class.
   */
  public static final Species BO             = new Species("BO",false);
  public static final Species BO_PP          = new Species("BO-PP",false);
  public static final Species BSB            = new Species("BSB",false);
  public static final Species CA_CHP         = new Species("CA-CHP",false);
  public static final Species C_CHP          = new Species("C-CHP",false);
  public static final Species CLO            = new Species("CLO",false);
  public static final Species CSS            = new Species("CSS",false);
  public static final Species CSS_EXOTICS    = new Species("CSS-EXOTICS",false);
  public static final Species EXOTIC_GRASSES = new Species("EXOTIC-GRASSES",false);
  public static final Species GRASS          = new Species("GRASS",false);
  public static final Species IC             = new Species("IC",false);
  public static final Species ILO            = new Species("ILO",false);
  public static final Species JP             = new Species("JP",false);
  public static final Species LP_RF          = new Species("LP-RF",false);
  public static final Species MC_DF          = new Species("MC-DF",false);
  public static final Species MC_IC          = new Species("MC-IC",false);
  public static final Species MC_PP          = new Species("MC-PP",false);
  public static final Species MC_RF          = new Species("MC-RF",false);
  public static final Species MC_SEQ         = new Species("MC-SEQ",false);
  public static final Species MC_WF          = new Species("MC-WF",false);
  public static final Species MDS            = new Species("MDS",false);
  public static final Species MTN_CHP        = new Species("MTN-CHP",false);
  public static final Species NM_CHP         = new Species("NM-CHP",false);
  public static final Species PC             = new Species("PC",false);
  public static final Species PJ             = new Species("PJ",false);
  public static final Species RF             = new Species("RF",false);
  public static final Species SB             = new Species("SB",false);
  public static final Species SD_CHP         = new Species("SD-CHP",false);
  public static final Species SEQ            = new Species("SEQ",false);
  public static final Species TBSB           = new Species("TBSB",false);
  public static final Species WJ             = new Species("WJ",false);

  // ***************************************************
  // *** Southern California ***
  // ***************************************************

  public static final Species BURNED_URBAN   = new Species("BURNED-URBAN",false);
  public static final Species URBAN          = new Species("URBAN",false);

  // ***************************************************
  // *** Gila ***
  // ***************************************************

  public static final Species NRK      = new Species("NRK",false);
  public static final Species NFL      = new Species("NFL",false);
  public static final Species NPT      = new Species("NPT",false);
  public static final Species PVT      = new Species("PVT",false);
  public static final Species WET      = new Species("WET",false);
  public static final Species WAT      = new Species("WAT",false);
  public static final Species GRA      = new Species("GRA",false);
  public static final Species GRA_SMS  = new Species("GRA-SMS",false);
  public static final Species GRA_TAA  = new Species("GRA-TAA",false);
  public static final Species GRA_TADF = new Species("GRA-TADF",false);
  public static final Species GRA_TDF  = new Species("GRA-TDF",false);
//  public static final Species GRA_TDO  = new Species("GRA-TDO",false);
  public static final Species GRA_TMC  = new Species("GRA-TMC",false);
  public static final Species GRA_TJW  = new Species("GRA-TJW",false);
  public static final Species GRA_TPI  = new Species("GRA-TPI",false);
  public static final Species GRA_TPJ  = new Species("GRA-TPJ",false);
  public static final Species GRA_FW   = new Species("GRA-FW",false);
  public static final Species GRA_FD   = new Species("GRA-FD",false);
  public static final Species GRA_TOC  = new Species("GRA-TOC",false);
  public static final Species GRA_TODF = new Species("GRA-TODF",false);
  public static final Species GRA_TOPP = new Species("GRA-TOPP",false);
  public static final Species GRA_TMF  = new Species("GRA-TMF",false);
  public static final Species GRA_TOW  = new Species("GRA-TOW",false);
  public static final Species GMU      = new Species("GMU",false);
  public static final Species GWE      = new Species("GWE",false);
  public static final Species GWE_TCF  = new Species("GWE-TCF",false);
  public static final Species GWE_TCW  = new Species("GWE-TCW",false);
  public static final Species GWE_TRF  = new Species("GWE-TRF",false);
  public static final Species SHR      = new Species("SHR",false);
  public static final Species SHR_TCF  = new Species("SHR-TCF",false);
  public static final Species SHR_TRF  = new Species("SHR-TRF",false);
  public static final Species SHR_TODF = new Species("SHR-TODF",false);
  public static final Species SHR_TOPP = new Species("SHR-TOPP",false);
  public static final Species SHR_TOW  = new Species("SHR-TOW",false);
  public static final Species SMS      = new Species("SMS",false);
  public static final Species SGO      = new Species("SGO",false);
  public static final Species SMZ      = new Species("SMZ",false);
  public static final Species TAA      = new Species("TAA",false);
  public static final Species TADF     = new Species("TADF",false);
  public static final Species TDFA     = new Species("TDFA",false);
  public static final Species TCF      = new Species("TCF",false);
  public static final Species TCW      = new Species("TCW",false);
  public static final Species TDF_OAK  = new Species("TDF-OAK",false);
  public static final Species TPP_GRA  = new Species("TPP-GRA",false);
  public static final Species TDF      = new Species("TDF",false);
  public static final Species TPP_TDF  = new Species("TPP-TDF",false);
  public static final Species TDF_TPP  = new Species("TDF-TPP",false);
  public static final Species TES      = new Species("TES",false);
  public static final Species TJW      = new Species("TJW",false);
  public static final Species TMC      = new Species("TMC",false);
  public static final Species TMS      = new Species("TMS",false);
  public static final Species TGO      = new Species("TGO",false);
  public static final Species TOC      = new Species("TOC",false);
  public static final Species TODF     = new Species("TODF",false);
  public static final Species TDFO     = new Species("TDFO",false);
  public static final Species TOPP     = new Species("TOPP",false);
  public static final Species TPPO     = new Species("TPPO",false);
  public static final Species TOW      = new Species("TOW",false);
  public static final Species TPI      = new Species("TPI",false);
  public static final Species TPJ      = new Species("TPJ",false);
  public static final Species TPP      = new Species("TPP",false);
  public static final Species TRE      = new Species("TRE",false);
  public static final Species TRF      = new Species("TRF",false);
  public static final Species TRJ      = new Species("TRJ",false);
  public static final Species TSF      = new Species("TSF",false);
  public static final Species TWF      = new Species("TWF",false);
  public static final Species TWP      = new Species("TWP",false);

  public static final Species SHR_FD = new Species("SHR-FD",false);
  public static final Species SHR_FW = new Species("SHR-FW",false);
  public static final Species SHR_TOC = new Species("SHR-TOC",false);
  public static final Species GRA_TMS = new Species("GRA-TMS",false);

  // ***************************************************
  // *** South Central Alaska ***
  // ***************************************************

  public static final Species AC  = new Species("AC",false);
  public static final Species A   = new Species("A",false);
  public static final Species B   = new Species("B",false);
  public static final Species BS  = new Species("BS",false);
//  public static final Species CW  = new Species("CW",false);
  public static final Species DWS = new Species("DWS",false);
  public static final Species HD  = new Species("HD",false);
  public static final Species LS  = new Species("LS",false);
//  public static final Species MH  = new Species("MH",false);
  public static final Species SS  = new Species("SS",false);
//  public static final Species WH  = new Species("WH",false);
  public static final Species WS  = new Species("WS",false);

  // Tree Species Combinations (Chugach NF Book)
  public static final Species LS_A  = new Species("LS-A",false);
  public static final Species LS_B  = new Species("LS-B",false);
  public static final Species LS_CW = new Species("LS-CW",false);
  public static final Species MH_AC = new Species("MH-AC",false);
  public static final Species MH_B  = new Species("MH-B",false);
  public static final Species MH_LS = new Species("MH-LS",false);
  public static final Species MH_SS = new Species("MH-SS",false);
  public static final Species MH_WH = new Species("MH-WH",false);
  public static final Species WH_SS = new Species("WH-SS",false);

  // Tree Species Combinations (Kenai14.apr)
  public static final Species AB         = new Species("AB",false);
  public static final Species A_BS       = new Species("A-BS",false);
  public static final Species A_WS       = new Species("A-WS",false);
  public static final Species AB_DWS     = new Species("AB-DWS",false);
  public static final Species AB_WS      = new Species("AB-WS",false);
  public static final Species AB_BS      = new Species("AB-BS",false);
  public static final Species AB_B       = new Species("AB-B",false);
  public static final Species AB_BS_DWS  = new Species("AB-BS-DWS",false);
  public static final Species ALP        = new Species("ALP",false);
  public static final Species B_A        = new Species("B-A",false);
  public static final Species B_BS       = new Species("B-BS",false);
  public static final Species B_WS       = new Species("B-WS",false);
  public static final Species B_DWS      = new Species("B-DWS",false);
  public static final Species B_CW       = new Species("B-CW",false);
  public static final Species BS_A       = new Species("BS-A",false);
  public static final Species BS_AB     = new Species("BS-AB",false);
  public static final Species BS_B       = new Species("BS-B",false);
  public static final Species BS_HD      = new Species("BS-HD",false);
  public static final Species CW_WS      = new Species("CW-WS",false);
  public static final Species CW_B       = new Species("CW-B",false);
  public static final Species CW_DWS     = new Species("CW-DWS",false);
  public static final Species DWS_A      = new Species("DWS-A",false);
  public static final Species DWS_CW     = new Species("DWS-CW",false);
  public static final Species DWS_AB    = new Species("DWS-AB",false);
  public static final Species DWS_B      = new Species("DWS-B",false);
  public static final Species DWS_HD     = new Species("DWS-HD",false);
  public static final Species DWS_WS     = new Species("DWS-WS",false);
  public static final Species HD_BS      = new Species("HD-BS",false);
  public static final Species HD_WS      = new Species("HD-WS",false);
  public static final Species HD_WS_DWS  = new Species("HD-WS-DWS",false);
  public static final Species NON_FOREST = new Species("NON-FOREST",false);
  public static final Species WS_A       = new Species("WS-A",false);
  public static final Species WS_B       = new Species("WS-B",false);
  public static final Species WS_HD      = new Species("WS-HD",false);
  public static final Species WS_A_B     = new Species("WS-A-B",false);
  public static final Species WS_BS      = new Species("WS-BS",false);
  public static final Species WS_DWS     = new Species("WS-DWS",false);
  public static final Species WS_DWS_HD  = new Species("WS-DWS-HD",false);

  // Shrub Species
  public static final Species WIL               = new Species("WIL",false);
  public static final Species ALD               = new Species("ALD",false);
  public static final Species MIXED_TALL_SHRUB  = new Species("MIXED-TALL-SHRUB",false);
  public static final Species MIXED_LOW_SHRUB   = new Species("MIXED-LOW-SHRUB",false);
  public static final Species MIXED_DWARF_SHRUB = new Species("MIXED-DWARF-SHRUB",false);

  // Herbaceous Species
  public static final Species HERB = new Species("HERB",false);
  public static final Species GH   = new Species("GH",false);
  public static final Species AQU  = new Species("AQU",false);

  // Misc
  public static final Species DSS = new Species("DSS",false);
  public static final Species DLS   = new Species("DLS",false);

  // Additional ones added 10/1
  public static final Species SS_DSS = new Species("SS-DSS",false);
  public static final Species LS_DLS = new Species("LS-DLS",false);
  public static final Species DSS_SS = new Species("DSS-SS",false);
  public static final Species DLS_LS = new Species("DLS-LS",false);
  public static final Species A_DWS  = new Species("A-DWS",false);
  public static final Species BA_DWS = new Species("BA-DWS",false);
  public static final Species HD_DWS = new Species("HD-DWS",false);
  public static final Species DWS_BA = new Species("DWS-BA",false);
  public static final Species BS_WS  = new Species("BS-WS",false);
  public static final Species BS_LS  = new Species("BS-LS",false);
  public static final Species BS_SS  = new Species("BS-SS",false);
  public static final Species MH_WS  = new Species("MH-WS",false);
  public static final Species WH_WS  = new Species("WH-WS",false);
  public static final Species MH_BS  = new Species("MH-BS",false);
  public static final Species WH_BS  = new Species("WH-BS",false);
  public static final Species WH_LS  = new Species("WH-LS",false);
  public static final Species BS_MH  = new Species("BS-MH",false);
  public static final Species BS_WH  = new Species("BS-WH",false);
  public static final Species LS_BS  = new Species("LS-BS",false);
  public static final Species SS_BS  = new Species("SS-BS",false);
  public static final Species WS_SS  = new Species("WS-SS",false);
  public static final Species SS_WS  = new Species("SS-WS",false);
  public static final Species BA_CW  = new Species("BA-CW",false);
  public static final Species BA_HD  = new Species("BA-HD",false);
  public static final Species A_CW   = new Species("A-CW",false);
  public static final Species A_HD   = new Species("A-HD",false);
  public static final Species B_HD   = new Species("B-HD",false);
  public static final Species AB_CW  = new Species("AB-CW",false);
  public static final Species AB_HD  = new Species("AB-HD",false);
  public static final Species CW_HD  = new Species("CW-HD",false);
  public static final Species CW_A   = new Species("CW-A",false);
  public static final Species HD_A   = new Species("HD-A",false);
  public static final Species HD_B   = new Species("HD-B",false);
  public static final Species CW_AB  = new Species("CW-AB",false);
  public static final Species HD_AB  = new Species("HD-AB",false);
  public static final Species HD_CW  = new Species("HD-CW",false);
  public static final Species CW_BA  = new Species("CW-BA",false);
  public static final Species HD_BA  = new Species("HD-BA",false);
  public static final Species WS_BA  = new Species("WS-BA",false);
  public static final Species WS_CW  = new Species("WS-CW",false);
  public static final Species LS_AB  = new Species("LS-AB",false);
  public static final Species LS_HD  = new Species("LS-HD",false);
  public static final Species SS_A   = new Species("SS-A",false);
  public static final Species SS_B   = new Species("SS-B",false);
  public static final Species SS_AB  = new Species("SS-AB",false);
  public static final Species SS_BA  = new Species("SS-BA",false);
  public static final Species SS_CW  = new Species("SS-CW",false);
  public static final Species SS_HD  = new Species("SS-HD",false);
  public static final Species BS_BA  = new Species("BS-BA",false);
  public static final Species BS_CW  = new Species("BS-CW",false);
  public static final Species MH_A   = new Species("MH-A",false);
  public static final Species MH_AB  = new Species("MH-AB",false);
  public static final Species MH_BA  = new Species("MH-BA",false);
  public static final Species MH_CW  = new Species("MH-CW",false);
  public static final Species MH_HD  = new Species("MH-HD",false);
  public static final Species WH_A   = new Species("WH-A",false);
  public static final Species WH_B   = new Species("WH-B",false);
  public static final Species WH_AB  = new Species("WH-AB",false);
  public static final Species WH_BA  = new Species("WH-BA",false);
  public static final Species WH_CW  = new Species("WH-CW",false);
  public static final Species WH_HD  = new Species("WH-HD",false);
  public static final Species BA_WS  = new Species("BA-WS",false);
  public static final Species A_LS   = new Species("A-LS",false);
  public static final Species B_LS   = new Species("B-LS",false);
  public static final Species AB_LS  = new Species("AB-LS",false);
  public static final Species BA_LS  = new Species("BA-LS",false);
  public static final Species CW_LS  = new Species("CW-LS",false);
  public static final Species HD_LS  = new Species("HD-LS",false);
  public static final Species A_SS   = new Species("A-SS",false);
  public static final Species B_SS   = new Species("B-SS",false);
  public static final Species AB_SS  = new Species("AB-SS",false);
  public static final Species BA_SS  = new Species("BA-SS",false);
  public static final Species CW_SS  = new Species("CW-SS",false);
  public static final Species HD_SS  = new Species("HD-SS",false);
  public static final Species BA_BS  = new Species("BA-BS",false);
  public static final Species CW_BS  = new Species("CW-BS",false);
  public static final Species A_MH   = new Species("A-MH",false);
  public static final Species B_MH   = new Species("B-MH",false);
  public static final Species AB_MH  = new Species("AB-MH",false);
  public static final Species BA_MH  = new Species("BA-MH",false);
  public static final Species CW_MH  = new Species("CW-MH",false);
  public static final Species HD_MH  = new Species("HD-MH",false);
  public static final Species A_WH   = new Species("A-WH",false);
  public static final Species B_WH   = new Species("B-WH",false);
  public static final Species AB_WH  = new Species("AB-WH",false);
  public static final Species BA_WH  = new Species("BA-WH",false);
  public static final Species CW_WH  = new Species("CW-WH",false);
  public static final Species HD_WH  = new Species("HD-WH",false);
  public static final Species OCEAN  = new Species("OCEAN",false);

  // Additional ones added 10/11
  public static final Species BA_DSS = new Species("BA-DSS",false);
  public static final Species DSS_BA = new Species("DSS-BA",false);
  public static final Species AB_DSS = new Species("AB-DSS",false);
  public static final Species DSS_AB = new Species("DSS-AB",false);
  public static final Species CW_DSS = new Species("CW-DSS",false);
  public static final Species DSS_CW = new Species("DSS-CW",false);
  public static final Species A_DSS  = new Species("A-DSS",false);
  public static final Species DSS_A  = new Species("DSS-A",false);
  public static final Species B_DSS  = new Species("B-DSS",false);
  public static final Species DSS_B  = new Species("DSS-B",false);
  public static final Species HD_DSS = new Species("HD-DSS",false);
  public static final Species DSS_HD = new Species("DSS-HD",false);
  public static final Species BS_DSS = new Species("BS-DSS",false);
  public static final Species DSS_BS = new Species("DSS-BS",false);
  public static final Species MH_DSS = new Species("MH-DSS",false);
  public static final Species DSS_MH = new Species("DSS-MH",false);
  public static final Species WH_DSS = new Species("WH-DSS",false);
  public static final Species DSS_WH = new Species("DSS-WH",false);
  public static final Species DWS_BS = new Species("DWS-BS",false);
  public static final Species DWS_SS = new Species("DWS-SS",false);
  public static final Species DWS_MH = new Species("DWS-MH",false);
  public static final Species DWS_WH = new Species("DWS-WH",false);
  public static final Species SS_DWS = new Species("SS-DWS",false);
  public static final Species SS_WH  = new Species("SS-WH",false);
  public static final Species SS_MH  = new Species("SS-MH",false);
  public static final Species WH_DWS = new Species("WH-DWS",false);
  public static final Species WS_WH  = new Species("WS-WH",false);
  public static final Species WS_MH  = new Species("WS-MH",false);
  // Lutz Combinations
  public static final Species BA_DLS = new Species("BA-DLS",false);
  public static final Species DLS_BA = new Species("DLS-BA",false);
  public static final Species AB_DLS = new Species("AB-DLS",false);
  public static final Species DLS_AB = new Species("DLS-AB",false);
  public static final Species CW_DLS = new Species("CW-DLS",false);
  public static final Species DLS_CW = new Species("DLS-CW",false);
  public static final Species A_DLS  = new Species("A-DLS",false);
  public static final Species DLS_A  = new Species("DLS-A",false);
  public static final Species B_DLS  = new Species("B-DLS",false);
  public static final Species DLS_B  = new Species("DLS-B",false);
  public static final Species HD_DLS = new Species("HD-DLS",false);
  public static final Species DLS_HD = new Species("DLS-HD",false);
  public static final Species DLS_WS = new Species("DLS-WS",false);
  public static final Species DLS_SS = new Species("DLS-SS",false);
  public static final Species SS_DLS = new Species("SS-DLS",false);
  public static final Species BS_DLS = new Species("BS-DLS",false);
  public static final Species DLS_BS = new Species("DLS-BS",false);
  public static final Species MH_DLS = new Species("MH-DLS",false);
  public static final Species DLS_MH = new Species("DLS-MH",false);
  public static final Species WH_DLS = new Species("WH-DLS",false);
  public static final Species DLS_WH = new Species("DLS-WH",false);
  public static final Species LS_BA  = new Species("LS-BA",false);
  public static final Species LS_SS  = new Species("LS-SS",false);
  public static final Species LS_MH  = new Species("LS-MH",false);
  public static final Species LS_WH  = new Species("LS-WH",false);

  // New ones found while loading pathway file.
  public static final Species BS_DWS = new Species("BS-DWS",false);
  public static final Species WS_AB  = new Species("WS-AB",false);
  public static final Species BA     = new Species("BA",false);
  public static final Species MSH    = new Species("MSH",false);

  public static final Species DWS_C  = new Species("DWS_C",false);

  // **********************
  // *** Southwest Utah ***
  // **********************
  public static final Species GW_PG     = new Species("GW-PG",false);
  public static final Species MS_PG     = new Species("MS-PG",false);
  public static final Species MS_PJU_PG = new Species("MS-PJU-PG",false);
  public static final Species MS_PJU    = new Species("MS-PJU",false);
  public static final Species PJU       = new Species("PJU",false);
  public static final Species PJU_MS    = new Species("PJU-MS",false);
  public static final Species SD_PG     = new Species("SD-PG",false);
  public static final Species WS_PG     = new Species("WS-PG",false);
  public static final Species WS_PJU_PG = new Species("WS-PJU-PG",false);
  public static final Species MM_OK     = new Species("MM-OK",false);
  public static final Species MM_OK_PJU = new Species("MM-OK-PJU",false);
  public static final Species OK        = new Species("OK",false);
  public static final Species OK_PJU    = new Species("OK-PJU",false);
  public static final Species PJU_MM_OK = new Species("PJU-MM-OK",false);
  public static final Species PJU_OK    = new Species("PJU-OK",false);
//  public static final Species PP        = new Species("PP",false);
  public static final Species MF        = new Species("MF",false);
  public static final Species PP_MF     = new Species("PP-MF",false);
  public static final Species MF_A      = new Species("MF-A",false);
  public static final Species SF        = new Species("SF",false);
  public static final Species SF_A      = new Species("SF-A",false);
//  public static final Species A         = new Species("A",false);
  public static final Species A_MS      = new Species("A-MS",false);
  public static final Species A_PG      = new Species("A-PG",false);
  public static final Species A_MF      = new Species("A-MF",false);
  public static final Species AG        = new Species("AG",false);
  public static final Species PG        = new Species("PG",false);
  public static final Species RIPARIAN  = new Species("RIPARIAN",false);
  public static final Species ROCK_BARE = new Species("ROCK-BARE",false);
//  public static final Species WATER     = new Species("Water",false);
  public static final Species AGR_URB   = new Species("AGR-URB",false);
  public static final Species A_SF      = new Species("A-SF",false);
  public static final Species PJU_WS    = new Species("PJU-WS",false);
  public static final Species ALPINE    = new Species("ALPINE",false);

  // *** Colorado Front Range ***
  // ****************************
  public static final Species CEMO2_RIBES   = new Species("CEMO2-RIBES",false);
  public static final Species ALINT         = new Species("ALINT",false);
  public static final Species ARTRW8_CHVI8  = new Species("ARTRW8-CHVI8",false);
  public static final Species POAN3         = new Species("POAN3",false);
  public static final Species ERPAA4        = new Species("ERPAA4",false);
  public static final Species PIPU_POAN3    = new Species("PIPU-POAN3",false);
  public static final Species ACNE2_PSME    = new Species("ACNE2-PSME",false);
  public static final Species ABLA_PIAR     = new Species("ABLA-PIAR",false);
  public static final Species PIFL2         = new Species("PIFL2",false);
  public static final Species PICO_PIEN     = new Species("PICO-PIEN",false);
  public static final Species PIEN_ABCO     = new Species("PIEN-ABCO",false);
  public static final Species MESIC_SHRUB   = new Species("MESIC-SHRUB",false);
  public static final Species JUCO6_ARUV    = new Species("JUCO6-ARUV",false);
  public static final Species VASC          = new Species("VASC",false);
  public static final Species POTR5_PIFL2   = new Species("POTR5-PIFL2",false);
  public static final Species QUGA_AMAL2    = new Species("QUGA-AMAL2",false);
  public static final Species ARTR2_CEMO2   = new Species("ARTR2-CEMO2",false);
  public static final Species PIPU_PSME     = new Species("PIPU-PSME",false);
  public static final Species POTR5_ABLA    = new Species("POTR5-ABLA",false);
  public static final Species PIED_POTR5    = new Species("PIED-POTR5",false);
  public static final Species ACGL          = new Species("ACGL",false);
  public static final Species CAEL3_CARUD   = new Species("CAEL3-CARUD",false);
  public static final Species ABCO          = new Species("ABCO",false);
  public static final Species PIAR_PIEN     = new Species("PIAR-PIEN",false);
  public static final Species FEAR2_DAPA2   = new Species("FEAR2-DAPA2",false);
  public static final Species ABCO_POTR5    = new Species("ABCO-POTR5",false);
  public static final Species PSME          = new Species("PSME",false);
  public static final Species JUCO6_SALIXU  = new Species("JUCO6-SALIXU",false);
  public static final Species ACHY          = new Species("ACHY",false);
  public static final Species POTR5_POAN3   = new Species("POTR5-POAN3",false);
  public static final Species PASM          = new Species("PASM",false);
  public static final Species PSSP6         = new Species("PSSP6",false);
  public static final Species PIEN_PSME     = new Species("PIEN-PSME",false);
  public static final Species SALIXU        = new Species("SALIXU",false);
  public static final Species SALIX_ALINT   = new Species("SALIX-ALINT",false);
  public static final Species PIPO_PIED     = new Species("PIPO-PIED",false);
  public static final Species SAGL          = new Species("SAGL",false);
  public static final Species CEMO2_SALIXU  = new Species("CEMO2-SALIXU",false);
  public static final Species SHCA          = new Species("SHCA",false);
  public static final Species ABCO_PIFL2    = new Species("ABCO-PIFL2",false);
  public static final Species PUTR2         = new Species("PUTR2",false);
  public static final Species RIBES         = new Species("RIBES",false);
  public static final Species POFE          = new Species("POFE",false);
  public static final Species SALIX         = new Species("SALIX",false);
  public static final Species MUMO          = new Species("MUMO",false);
  public static final Species ARUV          = new Species("ARUV",false);
  public static final Species PG_FORBS      = new Species("PG-FORBS",false);
  public static final Species AMAL2         = new Species("AMAL2",false);
//  public static final Species BA            = new Species("BA",false);
  public static final Species HECO26        = new Species("HECO26",false);
  public static final Species POTR5_PICO    = new Species("POTR5-PICO",false);
  public static final Species PIPO_PIAR     = new Species("PIPO-PIAR",false);
  public static final Species QUGA_ARUV     = new Species("QUGA-ARUV",false);
  public static final Species JUMO          = new Species("JUMO",false);
  public static final Species CACA4         = new Species("CACA4",false);
  public static final Species PSME_JUSC2    = new Species("PSME-JUSC2",false);
  public static final Species PUTR2_RIBES   = new Species("PUTR2-RIBES",false);
  public static final Species CHVI8         = new Species("CHVI8",false);
  public static final Species JUSC2_PSME    = new Species("JUSC2-PSME",false);
  public static final Species SALIXU_RIBES  = new Species("SALIXU-RIBES",false);
  public static final Species JUSC2_PIPO    = new Species("JUSC2-PIPO",false);
  public static final Species PIPU_PIPO     = new Species("PIPU-PIPO",false);
  public static final Species PIEN_PIAR     = new Species("PIEN-PIAR",false);
  public static final Species POTR5_PIAR    = new Species("POTR5-PIAR",false);
  public static final Species XERIC_SHRUB   = new Species("XERIC-SHRUB",false);
  public static final Species JUSC2_PIED    = new Species("JUSC2-PIED",false);
  public static final Species VAMY2         = new Species("VAMY2",false);
  public static final Species PIED_PIPO     = new Species("PIED-PIPO",false);
  public static final Species PSME_PIEN     = new Species("PSME-PIEN",false);
//  public static final Species WATER         = new Species("WATER",false);
  public static final Species POTR5_ABCO    = new Species("POTR5-ABCO",false);
  public static final Species CEMO2         = new Species("CEMO2",false);
  public static final Species PIED_JUMO     = new Species("PIED-JUMO",false);
  public static final Species ABCO_PIPO     = new Species("ABCO-PIPO",false);
  public static final Species ARTRW8        = new Species("ARTRW8",false);
  public static final Species PIED          = new Species("PIED",false);
  public static final Species CEMO2_JUCO6   = new Species("CEMO2-JUCO6",false);
  public static final Species PIAR_POTR5    = new Species("PIAR-POTR5",false);
  public static final Species PIEN_PIPU     = new Species("PIEN-PIPU",false);
  public static final Species CEMO2_DAFL3   = new Species("CEMO2-DAFL3",false);
  public static final Species BRTE          = new Species("BRTE",false);
  public static final Species PIPU_POTR5    = new Species("PIPU-POTR5",false);
  public static final Species PSME_POTR5    = new Species("PSME-POTR5",false);
//  public static final Species NF            = new Species("NF",false);
  public static final Species ABCO_PSME     = new Species("ABCO-PSME",false);
  public static final Species CEMO2_PUTR2   = new Species("CEMO2-PUTR2",false);
  public static final Species PSME_PIAR     = new Species("PSME-PIAR",false);
  public static final Species JUSC2_POTR5   = new Species("JUSC2-POTR5",false);
  public static final Species PSME_ABCO     = new Species("PSME-ABCO",false);
  public static final Species PICO_ABLA     = new Species("PICO-ABLA",false);
  public static final Species ABLA_PIFL2    = new Species("ABLA-PIFL2",false);
  public static final Species CEMO2_ARTR2   = new Species("CEMO2-ARTR2",false);
  public static final Species LEKI2         = new Species("LEKI2",false);
  public static final Species POAN3_PSME    = new Species("POAN3-PSME",false);
//  public static final Species ND            = new Species("ND",false);
  public static final Species PIFL2_PIAR    = new Species("PIFL2-PIAR",false);
  public static final Species QUGA_VASC     = new Species("QUGA-VASC",false);
  public static final Species PIPO_JUSC2    = new Species("PIPO-JUSC2",false);
  public static final Species CAPU          = new Species("CAPU",false);
  public static final Species PIPO_PIPU     = new Species("PIPO-PIPU",false);
  public static final Species ABLA          = new Species("ABLA",false);
  public static final Species CAGE2         = new Species("CAGE2",false);
  public static final Species CEMO2_ARUV    = new Species("CEMO2-ARUV",false);
  public static final Species CAREXU        = new Species("CAREXU",false);
  public static final Species JUBAL_CAGE2   = new Species("JUBAL-CAGE2",false);
  public static final Species PIED_ABCO     = new Species("PIED-ABCO",false);
  public static final Species PICO_PIFL2    = new Species("PICO-PIFL2",false);
  public static final Species CEMO2_PHMO4   = new Species("CEMO2-PHMO4",false);
  public static final Species PIEN          = new Species("PIEN",false);
  public static final Species QUGA          = new Species("QUGA",false);
  public static final Species FEAR2         = new Species("FEAR2",false);
  public static final Species PIED_PSME     = new Species("PIED-PSME",false);
  public static final Species SALIX_BEOC2   = new Species("SALIX-BEOC2",false);
  public static final Species ARTR2_JUCO6   = new Species("ARTR2-JUCO6",false);
  public static final Species PIPO_PIFL2    = new Species("PIPO-PIFL2",false);
  public static final Species FEAR2_MUMO    = new Species("FEAR2-MUMO",false);
  public static final Species POTR5_PIPU    = new Species("POTR5-PIPU",false);
  public static final Species PSME_PIPO     = new Species("PSME-PIPO",false);
  public static final Species PIEN_PIFL2    = new Species("PIEN-PIFL2",false);
  public static final Species POAN3_PIPO    = new Species("POAN3-PIPO",false);
  public static final Species ALINT_BEOC2   = new Species("ALINT-BEOC2",false);
  public static final Species FEID          = new Species("FEID",false);
  public static final Species PIFL2_PIPO    = new Species("PIFL2-PIPO",false);
  public static final Species QUGA_SALIXU   = new Species("QUGA-SALIXU",false);
  public static final Species CAFO3         = new Species("CAFO3",false);
  public static final Species CEMO2_QUGA    = new Species("CEMO2-QUGA",false);
  public static final Species PHCO9_POAL2   = new Species("PHCO9-POAL2",false);
  public static final Species RICE          = new Species("RICE",false);
  public static final Species DAFL3         = new Species("DAFL3",false);
  public static final Species CEMO2_SYOR    = new Species("CEMO2-SYOR",false);
//  public static final Species AGR           = new Species("AGR",false);
  public static final Species POPR          = new Species("POPR",false);
  public static final Species PIPO          = new Species("PIPO",false);
  public static final Species PIPO_ABCO     = new Species("PIPO-ABCO",false);
  public static final Species ABLA_PICO     = new Species("ABLA-PICO",false);
  public static final Species PUTR2_ARTRV   = new Species("PUTR2-ARTRV",false);
  public static final Species CEMO2_RICE    = new Species("CEMO2-RICE",false);
  public static final Species ARTRV         = new Species("ARTRV",false);
  public static final Species ACNE2         = new Species("ACNE2",false);
  public static final Species RIBES_PUTR2   = new Species("RIBES-PUTR2",false);
  public static final Species PIAR_PSME     = new Species("PIAR-PSME",false);
  public static final Species BOGR2         = new Species("BOGR2",false);
  public static final Species POAL2_KOMY    = new Species("POAL2-KOMY",false);
  public static final Species POTR5         = new Species("POTR5",false);
  public static final Species PICO_PSME     = new Species("PICO-PSME",false);
  public static final Species PIAR          = new Species("PIAR",false);
  public static final Species PIAR_PIPO     = new Species("PIAR-PIPO",false);
  public static final Species POTR5_PIPO    = new Species("POTR5-PIPO",false);
  public static final Species PIFL2_PSME    = new Species("PIFL2-PSME",false);
  public static final Species CARUD_FEBRC   = new Species("CARUD-FEBRC",false);
  public static final Species JUCO6         = new Species("JUCO6",false);
  public static final Species PIEN_POTR5    = new Species("PIEN-POTR5",false);
  public static final Species POTR5_PIEN    = new Species("POTR5-PIEN",false);
  public static final Species PSME_PIED     = new Species("PSME-PIED",false);
  public static final Species FETH          = new Species("FETH",false);
  public static final Species PUTR2_CEMO2   = new Species("PUTR2-CEMO2",false);
  public static final Species POTR5_PSME    = new Species("POTR5-PSME",false);
  public static final Species CAREX_JUNCU   = new Species("CAREX-JUNCU",false);
  public static final Species PIEN_ABLA     = new Species("PIEN-ABLA",false);
  public static final Species ABCO_PIEN     = new Species("ABCO-PIEN",false);
  public static final Species PIPU          = new Species("PIPU",false);
  public static final Species PIED_JUSC2    = new Species("PIED-JUSC2",false);
  public static final Species CARO5         = new Species("CARO5",false);
  public static final Species QUGA_CEMO2    = new Species("QUGA-CEMO2",false);
  public static final Species PIPO_PSME     = new Species("PIPO-PSME",false);
  public static final Species PSME_PIFL2    = new Species("PSME-PIFL2",false);
  public static final Species POAL2_CAEL3   = new Species("POAL2-CAEL3",false);
  public static final Species CAREXU_CARU   = new Species("CAREXU-CARU",false);
  public static final Species PIAR_PIFL2    = new Species("PIAR-PIFL2",false);
  public static final Species CAREX         = new Species("CAREX",false);
  public static final Species PICO_POTR5    = new Species("PICO-POTR5",false);
  public static final Species POAN3_PIPU    = new Species("POAN3-PIPU",false);
  public static final Species PHMO4         = new Species("PHMO4",false);
  public static final Species PSME_PIPU     = new Species("PSME-PIPU",false);
  public static final Species PIEN_PICO     = new Species("PIEN-PICO",false);
  public static final Species JUSC2         = new Species("JUSC2",false);
  public static final Species AG_FORBS      = new Species("AG-FORBS",false);
  public static final Species PIED_PIAR     = new Species("PIED-PIAR",false);
  public static final Species QUGA_JUCO6    = new Species("QUGA-JUCO6",false);
  public static final Species PIPO_POTR5    = new Species("PIPO-POTR5",false);
  public static final Species PIFL2_ABCO    = new Species("PIFL2-ABCO",false);
  public static final Species PIFL2_POTR5   = new Species("PIFL2-POTR5",false);
  public static final Species PIPO_POAN3    = new Species("PIPO-POAN3",false);
  public static final Species JAAM          = new Species("JAAM",false);
  public static final Species ABLA_PIEN     = new Species("ABLA-PIEN",false);
  public static final Species PICO_PIPO     = new Species("PICO-PIPO",false);
//  public static final Species NS            = new Species("NS",false);
  public static final Species PSME_PICO     = new Species("PSME-PICO",false);
//  public static final Species RIPARIAN      = new Species("RIPARIAN",false);
  public static final Species PICO          = new Species("PICO",false);
  public static final Species PIAR_PICO     = new Species("PIAR-PICO",false);
  public static final Species POAN3_POTR5   = new Species("POAN3-POTR5",false);
  public static final Species ARTR2         = new Species("ARTR2",false);
  public static final Species ARTRV_PUTR2   = new Species("ARTRV-PUTR2",false);
  public static final Species FEAR2_BOGR2   = new Species("FEAR2-BOGR2",false);
  public static final Species PIPO_PICO     = new Species("PIPO-PICO",false);
  public static final Species JUMO_PIED     = new Species("JUMO-PIED",false);
  public static final Species PIFL2_PICO    = new Species("PIFL2-PICO",false);
  public static final Species PIFL2_PIEN    = new Species("PIFL2-PIEN",false);




}
