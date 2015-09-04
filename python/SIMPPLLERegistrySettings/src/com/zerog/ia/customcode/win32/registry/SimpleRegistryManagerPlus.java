/* -*-tab-width: 4; -*-
 * 
 * SimpleRegistryManagerPlus.java 
 * Version 1.0 - 11.30.1999
 * Version 2.0 - 02.05.2000
 *
 * MMA - MoMac
 * 
 * Copyright 1999 Zero G Software, Inc., All rights reserved.
 * 514 Bryant St., San Francisco, California, 94107, U.S.A.
 * 
 * CONFIDENTIAL AND PROPRIETARY INFORMATION - DO NOT DISCLOSE.
 *
 * The following program code (the "Software") consists of
 * unpublished, confidential and proprietary information of
 * Zero G Software, Inc.  The use of the Software is governed
 * by a license agreement and protected by trade secret and
 * copyright laws.  Disclosure of the Software to third
 * parties, in any form, in whole or in part, is expressly
 * prohibited except as authorized by the license agreement.
 * 
 * The DevNet license agreement gives you specific rights
 * with regard to using this code in your own projects and
 * in derivative works.  See the DevNet license agreement
 * for more information.
 * 
 */

package com.zerog.ia.customcode.win32.registry;

import com.zerog.ia.api.pub.*;
import java.util.*;

/**
 * SimpleRegistryMangerPlus.java
 * 
 * This class provides a slightly improved alternative to the default SimpleRegistryManger
 * API in InstallAnywhere.  New additions to this class include methods to make registry
 * entries based on delimited String values, automated creation of deletion Strings for
 * removal of the installed registry entry during uninstall, new default intelligent
 * registry entry rollback functionality (which can be switched off, but is on by default),
 * and improved error handling.
 * 
 */
public class SimpleRegistryManagerPlus
{
	private SimpleRegistryManager srm;
	private InstallerProxy ip;
	private UninstallerProxy up;
	
	// the delimiter used when parsing a Registry Entry supplied as a String.
	private String delimiter; 
	
	// the value of this flag identifies the state of the rollback functionaity.
	private boolean rollbackFlag;
	
	// the name of the class -- primarily used by Msg methods.
	private static final String CLASS_NAME = "SimpleRegistryManagerPlus";
	// the status message to be displayed during installation.
	private static final String INSTALL_MESSAGE = "Win32 Registry Entry";
	// the status message to be displayed during uninstall.
	private static final String UNINSTALL_MESSAGE = "Win 32 Registry Entry";

	// determines if debugMsg will produce output during execution.
	public  static boolean debug = false; 
	
	// ***************************************************************************
	// Class Constants
	// ***************************************************************************
	
	private static final int MAX_REG_COMPONENTS = 4,
	                         KEYPATH = 0,
	                         VALUENAME = 1,
	                         VALUETYPE = 2,
	                         VALUE = 3;
	                         
	private static final String INSTALLED_CONST = "$INSTALLED$",
	                            ROLLBACK_CONST = "$ROLLBACK$",
	                            DELETE_CONST = "$DELETE$";
	                            
	private static final String KEY_ONLY_CONST = "$KEY_ONLY$",
	                            DEFAULT_CONST = "$DEFAULT$",
	                            INTEGER_TYPE_CONST = "INTEGER",
	                            BYTE_TYPE_CONST = "BYTE",
	                            STRING_TYPE_CONST = "STRING";
	                            
	private static final String DEFAULT_DELIMITER = ",";
	
	// ***************************************************************************


	// ***************************************************************************
	// Debugging Methods
	// ***************************************************************************

	/**
	 *	This method outputs the additional debugging messages from this 
	 *  custom code action.   The messages are directed to 'stderr' and will
	 *  only be output if the class's 'debug' flag has been turned on and
	 *  the installer is run in 'debug' mode.
	 */
	private static void debugMsg(String msg)
	{
		if (debug)	// if output should be shown.
		{
			System.err.println(CLASS_NAME + "(D): " + msg);	// show message.
		}
	}


	/**
	 *	This method outputs the standard messages from this custom code action
	 *  which are always seen in 'stdout' when an installer is
	 *  run in 'debug' mode.
	 */
	private static void stdMsg(String msg)
	{
		// stdMsgs are always output.
		System.out.println(CLASS_NAME + ": " + msg);	// show message.
	}
	
	// ***************************************************************************


	// ***************************************************************************
	// Constructors
	// ***************************************************************************

	/**
	 *
	 * Constructor for use at install time.
	 * UninstallerProxy 'up' will be null when this constructor is used.
	 *
	 */
	public SimpleRegistryManagerPlus(InstallerProxy ip)
	{
		this.delimiter = DEFAULT_DELIMITER;
		this.up = null;
		this.ip = ip;
		this.rollbackFlag = true;
		srm = (SimpleRegistryManager)ip.getService(SimpleRegistryManager.class);
	}

	
	/**
	 *
	 * Constructor for use at uninstall time.
	 * InstallerProxy 'ip' will be null when this constructor is used.
	 *
	 */
	public SimpleRegistryManagerPlus(UninstallerProxy up)
	{
		this.delimiter = DEFAULT_DELIMITER;
		this.ip = null;
		this.up = up;
		this.rollbackFlag = true;
		srm = (SimpleRegistryManager)up.getService(SimpleRegistryManager.class);
	}
	

	/**
	 *
	 * Constructor for use at install time (delimiter specified).
	 * UninstallerProxy 'up' will be null when this constructor is used.
	 *
	 */
	public SimpleRegistryManagerPlus(InstallerProxy ip, String delimiter)
	{
		this.delimiter = delimiter;
		this.up = null;
		this.ip = ip;
		this.rollbackFlag = true;
		srm = (SimpleRegistryManager)ip.getService(SimpleRegistryManager.class);
	}
	

	/**
	 *
	 * Constructor for use at uninstall time (delimiter specified).
	 * InstallerProxy 'ip' will be null when this constructor is used.
	 *
	 */
	public SimpleRegistryManagerPlus(UninstallerProxy up, String delimiter)
	{
		this.delimiter = delimiter;
		this.ip = null;
		this.up = up;
		this.rollbackFlag = true;
		srm = (SimpleRegistryManager)up.getService(SimpleRegistryManager.class);
	}

	// ***************************************************************************


	// ***************************************************************************
	// Public Methods
	// ***************************************************************************

	/**
	 *
	 * This method enables the advanced rollback functionality.
	 * Rollback functionality is on by default.
	 */
	public void enableRollback()
	{
		rollbackFlag = true;
	}
	
	/**
	 *
	 * This method disables the advanced rollback functionality.
	 *
	 */
	public void disableRollback()
	{
		rollbackFlag = false;
	}


	/**
	 *
	 * This method determines whether a registry key already exists at
	 * the specified keyPath.
	 *
	 */
	public boolean exists(String keyPath)
	{
		boolean registryExists = false;
		String tempValueName = "ZGKeyTestEntry199x";

		try
		{
			//
			// attempt to make a random registry entry at the key designated
			// by the keyPath.  Success signifies that that key existed.
			//
			if (srm.setRegistryKeyValue(keyPath, tempValueName, ""))
			{
				registryExists = true;
				srm.deleteRegistryKeyValue(keyPath, tempValueName);
			}
			else
			{
				registryExists = false;
			}
		}
		catch (Exception e)
		{
			registryExists = false;
		}

		return registryExists;
	}


	/**
	 *
	 * String setRegistryEntryDelimitedString(String registryEntry)
	 *
	 * This method will make a registry entry based on a single line delimited
	 * string.  The delimiter is specified during construction (default is a 
	 * comma delimited string).  The number of tokens in the delimited string directly
	 * effects the kind of registry entry that is made.
	 *
	 * This method returns a String value that represents a deletion entry that
	 * can be used by deleteRegistryEntryDelimitedString(String entryData) at uninstall
	 * time to remove this entry or to rollback to the original value.  A return value
	 * of null signifies failure.
	 *
	 * A fully delimted string (where the delimiter is a comma) would take the
	 * form of:
	 *
	 *    "keyPath, valueName, valueType, value"
	 *
	 * where valueName could have the special String values of:
	 *    "$KEY_ONLY" - signifies a request to create a key, valueType and
	 *        value will be ignored.
	 *    "$DEFAULT$ -  signifies a request to make an entry in the 'default'
	 *        valueName sub-key.
	 *    "@" - same as $DEFAULT$
	 *
	 * where valuetype could have the special String values of:
	 *    INTEGER - signifies a request to create an Integer value.
	 *    BYTE    - signifies a request to create an byte array value.
	 *    STRING  - signifies a request to create an String value.
	 *
	 * Strings sent to this method that do not have all four tokens are treated in the
	 * following manner:
	 *
	 * A string with only a single token is assumed to be a request to create 
	 * a Registry Key.  In this circumstance, this is what will be attempted.
	 *
	 * A string with only two tokens is assumed to be a request to create 
	 * a Registry Key, if and only if the second token is $KEY_ONLY$ or whitespace.
	 * In this circumstance, this is what will be attempted.
	 *
	 * A string with only three tokens is assumed to not include a valueType token
	 * and is treated as a request to make a entry into a valueName of type 
	 * String (the default type). In this circumstance, this is what will be attempted.
	 *
	 */
	public String setRegistryEntryDelimitedString(String registryEntry)
	{
		String[] registryArray = null;

		String returnValue = null;

		//
		// Get an array of registry components that fits the SRMP internal format,
		// which is <keyPath, valueName, valueType, value>
		//
		registryArray = getSRMP_InternalFormatFromDelimitedString(registryEntry);
		
		//
		// convertDelimitedStringTo_SRMP_InternalFormat() returns null to signify a malformed
		// registryEntry String.  We do not want to attempt an entry if this is the case.
		//
		if (registryArray != null)
		{
			returnValue = setRegistryEntry(registryArray);
		}
		
		return returnValue;
	}

				
	/**
	 *
	 * This method sets the data in a value, specified by the keyPath and valueName.
	 * If the value does not exist under a certain key, it will be created and its
	 * data set.
	 *
	 * Parameters:
	 *    keyPath - the full path name that refers to the key where the value will
	 *       be set.
	 *    valueName - the identifier for this specific value / data pair.
	 *    value - what you want to set the value to. You can send a String,
	 *       Integer, or byte[].
	 * 
	 * Returns: A String that can be passed to the deleteRegistryEntryDelimitedString method
	 *    if the value was successfully set, and created if necessary,
	 *    null if the value could not be set. Usually this means that the key path
	 *    is wrong, or you don't have the required permissions to set the value.
	 *
	 */
	public String setRegistryKeyValue(String keyPath, String valueName, Object value)
	{
		String returnValue = null;
		String [] registryArray = new String[MAX_REG_COMPONENTS];
		
		registryArray[KEYPATH] = keyPath;
		registryArray[VALUENAME] = valueName;
		
		if (value instanceof String)
		{
			registryArray[VALUETYPE] = STRING_TYPE_CONST;
			registryArray[VALUE] = value.toString();
		}	
		else if (value instanceof Integer)
		{
			registryArray[VALUETYPE] = INTEGER_TYPE_CONST;
			registryArray[VALUE] = value.toString();
		}	
		else if (value instanceof byte[])
		{			
			//
			// store the contents of replaceValue byte array in an actual byte[] and
			// re-initialize the replace value to empty string.  We will refill it
			// with the String representation of the byte array below.
			//	
			String byteArrayValue = "";
			for (int i = 0; i < ((byte[])value).length; i++)
			{
				byteArrayValue += (String)byteArrayValue + Byte.toString(((byte[])value)[i]) + " ";
			}

			registryArray[VALUETYPE] = BYTE_TYPE_CONST;
			registryArray[VALUE] = byteArrayValue;
		}	

		returnValue = setRegistryEntry(registryArray);

		return returnValue;
	}

	
	/**
	 *
	 * This method creates a new registry key. It will create all of the keys leading
	 * up to the key to create if they don't already exist.
	 *
	 * Parameters:
	 *    keyPath - the full path name that refers to the key where the value will
	 *       be set.
	 * 
	 * Returns: A String that can be passed to the deleteRegistryEntryDelimitedString method
	 *    if the key was created, null if the key could not be created. Usually this means 
	 *    that keyPath is wrong (root-level keys [like HKEY_LOCAL_MACHINE] cannot be created),
	 *    or that a key exists already for that path.
	 *
	 */
	public String createRegistryKey(String keyPath)
	{
		return setRegistryEntryDelimitedString(keyPath);
	}


	/**
	 *
	 * This method Returns the data associated with a value.
	 *
	 * Parameters:
	 *    keyPath - the full path name that refers to the key where the value is located.
	 *    valueName - the identifier for this specific value / data pair (or value).
	 *
	 * Returns: the value's data as an Object or null if the data could not be
	 *    found or accessed (likely due to an incorrect path or value name). You can
	 *    cast this to a String, Integer, or byte[], or you can compare it's class to
	 *    find out what kind of data it is.
	 *
	 */
	public Object getRegistryKeyValue(String keyPath, String valueName)
	{
		Object returnValue =  null;
		
		if (exists(keyPath))
		{
			try
			{
				if(valueName.equalsIgnoreCase(DEFAULT_CONST) || valueName.equals("@"))
				{
					valueName = "";
				}
				
				returnValue = srm.getRegistryKeyValue(keyPath, valueName);
			}
			catch (Exception e)
			{
				debugMsg("Exception: likely non-existent valueName or 'No Value Set.'");
			}
		}
		else
		{
			debugMsg("Key does not exist.");
		}
		
		return returnValue;
	}	
	
	
	/**
	 *
	 * boolean equalsValue(String registryEntry)
	 *
	 * This method checks whether or not a registry value at the valueName and keyPath
	 * supplied in the parameters equals the value that is also supplied in this
	 * methods parameters.
	 *
	 * Parameters:
	 *    keyPath - the full path name that refers to the key where the value will
	 *       be set.
	 *    valueName - the identifier for this specific value / data pair.
	 *    value - what you want to set the value to. You can send a String,
	 *       Integer, or byte[].
	 *
	 * where valueName could have the special String values of:
	 *    "$DEFAULT$ -  signifies a request to make an entry in the 'default'
	 *        valueName sub-key.
	 *    "@" - same as $DEFAULT$
	 * 
	 * Returns: a boolean value where 'true' signifies that the values are equal (i.e.,
	 *    the values are of the same type and have the same value), or 'false' which
	 *    signifies that the values are not equal.
	 *
	 */
	public boolean equalsValue(String keyPath, String valueName, Object value)
	{
		boolean equality = false;
		
		Object currentRegistryValue;
		
		// If comparing to the key's default value...
		//
		if (valueName.equalsIgnoreCase("$DEFAULT$") || valueName.equals("@"))
		{
			valueName = "";
		}
		
		currentRegistryValue = srm.getRegistryKeyValue(keyPath, valueName);

		//
		// AKM 2001/01/22
		// Need to replace double backslashes with a single backslash in value,
		// if both objects are Strings. If the value object is a String that contains
		// double backslashes, the registry entry will not be removed or rollbacked correctly.
		//

		if (value instanceof String)
		{
			if (currentRegistryValue instanceof String)
			{
				value = replaceDoubleBackSlashes(value.toString());
			}
		}
		
		//
		// need to check each element of the byte array for equality to determine if
		// the registry entries are equal.  Byte [] is a special case for comparison.
		//
		if (value instanceof byte[])
		{
			//
			// if: your value is a byte[] but the current value is not, definitely not equal.
			// if: the lengths of the arrays are not equal, then...
			//
			if (currentRegistryValue instanceof byte[] && 
			    ((byte[])value).length == ((byte[])currentRegistryValue).length)
			{				
				equality = true;
				
				//
				// compare the individual elements of the array.
				//
				for(int i = 0; i < ((byte[])value).length; i++)
				{
					if (((byte[])value)[i] != ((byte[])currentRegistryValue)[i])
					{
						equality = false;
						break; // inequality found,Êbreak the for loop.
					}
				}
			}
		}
		//
		// else: you can just compare the values of the objects.
		//
		else if (value.equals(currentRegistryValue))
		{
			equality = true;
		}
		
		return equality;
	}

	
	/**
	 *
	 * boolean equalsDelimitedStringValue(String registryEntry)
	 *
	 * This method checks whether or not a registry value at the valueName and keyPath
	 * represented in the delimited String equals the value that is also supplied
	 * in the delimited string.
	 *
	 * Parameters:
	 *    registryEntry - the delimited String that includes the entries path, valuetype,
	 *       and value.
	 *
	 * A fully delimted string (where the delimiter is a comma) would take the
	 * form of:
	 *
	 *    "keyPath, valueName, valueType, value"
	 *
	 * where valueName could have the special String values of:
	 *    "$DEFAULT$ -  signifies a request to make an entry in the 'default'
	 *        valueName sub-key.
	 *    "@" - same as $DEFAULT$
	 *
	 * where valuetype could have the special String values of:
	 *    INTEGER - signifies a request to create an Integer value.
	 *    BYTE    - signifies a request to create an byte array value.
	 *    STRING  - signifies a request to create an String value.
	 * 
	 * Returns: a boolean value where 'true' signifies that the values are equal (i.e.,
	 *    the values are of the same type and have the same value), or 'false' which
	 *    signifies that the values are not equal.
	 *
	 */
	public boolean equalsDelimitedStringValue(String registryEntry)
	{
		boolean equality = false;
		String[] registryArray = null;
		
		//
		// Get an array of registry components that fits the SRMP internal format,
		// which is <keyPath, valueName, valueType, value>
		//
		registryArray = getSRMP_InternalFormatFromDelimitedString(registryEntry);
		
		//
		// convertDelimitedStringTo_SRMP_InternalFormat() returns null to signify a malformed
		// registryEntry String.  We do not want to attempt an entry if this is the case.
		//
		if (registryArray != null)
		{
			try
			{			
				if (equalsValue(registryArray[KEYPATH], 
				           registryArray[VALUENAME], 
				           getValue(registryArray[VALUETYPE], registryArray[VALUE])))
				{
					equality = true;
				}
			}
			catch (Exception e)
			{
				// equality will remain false.
			}
		}
		
		return equality;
	}
		
	
	/**
	 *
	 * This method will remove an installed registry entry or replace (rollback) to a
	 * prior registry entry based on a single line delimited string.  The delimter is
	 * specified during construction (default is a 'pipe' (|) delimited string).  The number
	 * of tokens in the delimited string directly effects the kind of registry entry that is made.
	 *
	 * When advanced rollback functionality was enabled at install time, the original
	 * entry that was made at install is verified to confirm that it has not been
	 * changed from an external source before either rolling back or deleting.
	 *
	 * This method returns a boolean value representing the success of the operation.
	 * A return value of true signifies success. A return value of false signifies
	 * failure.
	 *
	 * A fully delimted string (where the delimiter is a comma) would take the
	 * form of:
	 *
	 *    "keyPath, valueName",
	 *
	 *    "$INSTALLED$, <a Delimited String representing the original entry made at install>,
	 *     $ROLLBACK$,  <a Delimited String representing the entry to rollback to>", or
	 *
	 *    "$INSTALLED$, <a Delimited String representing the original entry made at install>,
	 *     $DELETE$,  <a Delimited String representing the entry to delete>"
	 *
	 */
	public boolean deleteRegistryEntryDelimitedString(String entryData)
	{
		boolean success = false;

		if (entryData != null && !entryData.equals(""))
		{
			//
			// Check to see if the deletion data includes a verification
			// of the original registry entry.
			//
			if (entryData.startsWith(INSTALLED_CONST))
			{
				success = verifiedRegistryEntryRemoval(entryData);
			}
			else
			{
				success = straightRegistryEntryRemoval(entryData);
			}
					
		}
		else
		{
			debugMsg("No deletion registry entry data provided.");
		}
		
		//
		// if debugging identify what key failed to make an entry
		//
		if (!success)
		{
			debugMsg("Deletion Failure: " + entryData);
		}
		
		return success;
	}		
	
	
	/**
	 *
	 * This method Deletes a key from the Registry. All subkeys will be automatically removed,
	 * and all values will be automatically removed.
	 *
	 * Parameters:
	 *    keyPath - the full path name that refers to the key where the value will
	 *       be set.
	 * 
	 * Returns: true if the key was successfully found and removed. false if the value
	 *    could not be found or could not be removed. Usually this means that keyPath is
	 *    wrong, or that the key specified does not exist.
	 *
	 */
	public boolean deleteRegistryKey(String keyPath)
	{
		boolean success = false;
		
		try
		{
			success = srm.deleteRegistryKey(keyPath);
		}
		catch (Exception e) { }
		
		return success;
	}		

	
	/**
	 *
	 * This method deletes a value from the registry.
	 *
	 * Parameters:
	 *    keyPath - the full path name that refers to the key where the value is located.
	 *	  valueName - is the identifier for this specific value / data pair (or value).
	 *
	 * Returns: 'true' if the value was successfully found and removed. false if the
	 * value could not be found or could not be removed. Usually this means that
	 * keyPath is wrong, or valueName is wrong.
	 *
	 */
	public boolean deleteRegistryKeyValue(String keyPath, String valueName)
	{
		boolean success = false;

		try
		{
			success = srm.deleteRegistryKeyValue(keyPath, valueName);
		}
		catch (Exception e) { }
		
		return success;
	}		

	// ***************************************************************************


	// ***************************************************************************
	// Private Methods
	// ***************************************************************************

	/**
	 *
	 * This private method actually handles the making of entries (by way of the 
	 * SimpleRegistryManger) requested by calls to the setRegistry... public methods.
	 *
	 */
	private boolean makeEntry(String keyPath, String valueName, String valueType, String value)
	{
		boolean success = false;
				
		try
		{
			//
			// always need to attempt to create the keyPath.  Attempting to
			// make a setRegistryKeyValue() along a keyPath that does not exist
			// always fails.
			//
			if(success = srm.createRegistryKey(keyPath))
			{
				//
				// if we are doing more than just creating the key...
				//
				if (!valueName.equalsIgnoreCase(KEY_ONLY_CONST))
				{
					//
					// If setting the key's default value...
					//
					if (valueName.equalsIgnoreCase(DEFAULT_CONST) || valueName.equals("@"))
					{
						valueName = "";
					}
					
					//
					// send the request to the SimpleRegistryManger
					//
					success = srm.setRegistryKeyValue(keyPath, valueName, 
														getValue(valueType, value));
				}
			}
		}
		catch (Exception e)
		{
			stdMsg("Exception trying to make registry entry. Entry Failed.");
			success = false;
		}
		
		return success;
	}
	
	
	/**
	 *
	 * This private method transforms a String supplied for a 'value' into the appropriate
	 * data type: String, Integer, or byte[], and returns that transformed value.
	 *
	 * Excpetions are thrown back up to the calling method.
	 *
	 */
	private Object getValue(String valueType, String value) throws Exception
	{
		Object returnValue;
		
		if (valueType.equalsIgnoreCase(STRING_TYPE_CONST))
		{
			returnValue = value;
		}
		else if (valueType.equalsIgnoreCase(INTEGER_TYPE_CONST))
		{
			returnValue =  (Integer.valueOf(value));
		}
		else if (valueType.equalsIgnoreCase(BYTE_TYPE_CONST))
		{		
			byte[] byteArrayValues;  // will store the value to be returned.
			
			//
			// Tokenize the string representing the byte array entry
			// that is to made.
			// 
			StringTokenizer st = new StringTokenizer(value);
			
			//
			// create the byte array of a size equal to the number of tokens
			// in the array.
			//
			byteArrayValues = new byte[st.countTokens()];
			
			// 
			// convert the tokens into byte values and store each individual byte
			// as an element in the byte array.
			//
			for (int i = 0; st.hasMoreTokens(); i++)
			{
				// Get the value of the String as a Byte and convert it into a byte.
				byteArrayValues[i] = Byte.valueOf(st.nextToken()).byteValue();
			}
			
			returnValue = byteArrayValues;
		}
		else
		{
			debugMsg("Error: not a recognized value type.");
			throw new Exception();
		}
		
		return returnValue;
	}
		
		
	/**
	 *
	 * This private method assists in the staight deletion of an installed registry entry.
	 *
	 */
	private boolean straightRegistryEntryRemoval(String entryData)
	{
		boolean success = false;
		
		StringTokenizer st = new StringTokenizer(entryData, delimiter);
		int numberOfTokens = st.countTokens();
		
		String keyPath = st.nextToken().trim();
		
		try
		{
			if (numberOfTokens == 1)	// only one token (keyPath?)
			{	
				//
				// i.e., propably wanted to delete $DEFAULT$
				//			
				if (entryData.trim().endsWith(delimiter))	
				{
					success = srm.deleteRegistryKeyValue(keyPath, "");
				}
				else
				{
					success = srm.deleteRegistryKey(keyPath);
				}
			}
			//
			// else: Must be two or more tokens (this is private and empty or tokenless Strings are screened).
			//       However, only the very next token, what should be the 'valueName', of the 
			//       String for the registry entry to delete will matter.
			//
			else	
			{
				String valueName = st.nextToken().trim();
				
				//
				// but second token could also be $KEY_ONLY$.
				//
				if (valueName.equalsIgnoreCase(KEY_ONLY_CONST)) 
				{
					success = srm.deleteRegistryKey(keyPath);
				}
				else
				{
					if (valueName.equals("@") || valueName.equalsIgnoreCase(DEFAULT_CONST))
					{
						valueName = "";
					}
					
					success = srm.deleteRegistryKeyValue(keyPath, valueName);
				}
			}
		}
		catch (Exception e)
		{
			debugMsg("deleteRegistryEntry threw Exception. Operation Failed.");
			// delete request threw Exception. success already false.
		}
		
		return success;	
	}
	
		
	/**
	 *
	 * This private method assists in the removal of an installed registry entry or the 
	 * replacement (rollback) of a registry entry that existed prior to the install
	 * of the application that used this custom code.
	 *
	 */
	private boolean verifiedRegistryEntryRemoval(String entryData)
	{
		boolean success = false;
		boolean malformed = false;
				
		int firstDelimiter    = entryData.indexOf(delimiter);
		
		//
		// -1 values indicate that the desired substrings were not present, the entry must 
		// have been malformed.
		//
		if (firstDelimiter != -1)
		{
			int rollbackIndex     = entryData.indexOf(ROLLBACK_CONST);
			int deleteIndex       = entryData.indexOf(DELETE_CONST);
			
			//
			// Determine which type of registry entry removal is being requested.
			//
			int tempIndex = Math.max(deleteIndex, rollbackIndex);
			
			//
			// if: tempIndex == -1, neither of the expected Strings were encountered.
			//
			if (tempIndex != -1)
			{
				int nextDelimiter = entryData.indexOf(delimiter, tempIndex + 1);
				
				if (nextDelimiter != -1)
				{
					// original - the string that wass made at install.
					String original     = entryData.substring(firstDelimiter + 1, tempIndex);
					// actionString - the action to perform if orginal has not changed.
					String actionString = entryData.substring(nextDelimiter + 1);
					
					//
					// check to see if the value origially placed in the registry entry is
					// still equal to the current value.  If it is, we will rollback the entry.
					//
					if (equalsDelimitedStringValue(original))
					{
						if (!actionString.equals(""))
						{
							//
							// if: this is a $ROLLBACK$ operation.
							//
							if (tempIndex == rollbackIndex)
							{
								//
								// AKM 2001/01/22
								// Need to replace double backslashes with a single
								// backslash in actionString.  If actionString
								// contains double backslashes, the registry
								// entry will not be removed or rollbacked correctly.
								//

								actionString = replaceDoubleBackSlashes(actionString);
							
								stdMsg("RollBack: " + actionString); 
								success = (setRegistryEntryDelimitedString(actionString) !=  null);
							}
							//
							// else: this must be a $DELETE$ operation.
							//
							else
							{
                                stdMsg("Delete: " + actionString);
								success = straightRegistryEntryRemoval(actionString);
							}
						}
					}
					else
					{
						stdMsg("Registry Entries have changed since installation.  Not rolling back or removing.");
						
						//
						// entries are not supposed to change, so our job is complete!!
						//
						success = true; 
					}
				}
				//
				// else: a next delimiter was not found after the $ROLLBACK$ 
				//       or $DELETE$ action strings.
				//
				else
				{
					debugMsg("a next delimiter was not found after the $ROLLBACK$ or $DELETE$" + 
					         " action strings.");
					malformed = true;
				}
			}
			//
			// else: neither the $ROLLBACK$ or $DELETE$ action strings were encountered.
			//
			else
			{
				debugMsg("neither the $ROLLBACK$ or $DELETE$ action strings were encountered.");			
				malformed = true;
			}
		}
		//
		// else: there were no further delimiters following the $INSTALLED$ identifier string. 
		//
		else
		{
			debugMsg("there were no further delimiters following the $INSTALLED$" + 
			         " identifier string.");
			malformed = true;
		}
		

		if (malformed)
		{
			stdMsg("Malformed delete registry entry data.");
		}
		
		return success;
	}
	
	
	/**
	 *
	 * This method is the intermediate private method that handles the processing of requests
	 * made on the various public methods to set a registry entry.  This method handles
	 * the house keeping tasks of determining deletion values, etc.
	 *
	 */
	private String setRegistryEntry(String[] registryArray)
	{
		String returnValue = null;
		String topKey,
			   rollbackInfo = null;

		// if true parent key, you will only delete the registry value, not the key.	   
		boolean trueParentKey = true;
		boolean success = false;
		
		// 
		// find the currently existing topKey for the Registry entry that
		// wants to be made.  This will determine where in the Registry we
		// should delete when it is time to uninstall this key.
		// 
		topKey = getTopKey(registryArray[KEYPATH]);
		
		//
		// a trueParentKey is a key whose topKey is equal to its keyPath.
		//
		if (!topKey.equals(registryArray[KEYPATH]) || !exists(topKey))
		// ***** !exists(topKey) is needed to accurately determine truePK, but this is an expensive
		// operation that we will want to replace in some fashion in the future.  getTopKey()
		// is already dtermining this, so we want to find a method to use its work to our
		// advantage. *****
		{
			trueParentKey = false;
		}
		
		if (rollbackFlag)
		{
			//
			// if key is a trueParentKey and we will be setting a value to a valueName,
			// we will want to determine the rollbackInfo (existing valueType and value,
			// for this valueName, if any) so that this information can be used to rollback
			// the original value at a later time if so desired.
			//		
			if (trueParentKey && !registryArray[VALUENAME].equals(KEY_ONLY_CONST))
			{
				rollbackInfo = getRollbackInfo(registryArray);
			}
		}
		//
		// else: rollback info remains 'null'
		//
		
		//
		// once the rollback information has been aquired make the registry entry
		//
		success = makeEntry(registryArray[KEYPATH], 
		                    registryArray[VALUENAME], 
		                    registryArray[VALUETYPE], 
		                    registryArray[VALUE]);

		//
		// If the registry entry was successfully made, determine the return value
		//
		if (success) 
		{
			returnValue = getReturnValueForRollback(registryArray, 
			                                        topKey, 
			                                        rollbackInfo, 
			                                        trueParentKey);
		}
		//
		// else: returnValue will remain null.
		//

		debugMsg("keyPath = " + registryArray[KEYPATH]);
		debugMsg("topKey = " + topKey);
		debugMsg("trueParentKey = " + (trueParentKey ? "true" : "false"));
		debugMsg("returnValue = " + returnValue);
		
		return returnValue;
	}


	/**
	 *
	 * This private method determines the topKey along a registry path.  The topKey is
	 * the key along the supplied keyPath that is the first key that does not currently
	 * exist along that path. 
	 *
	 */
	private String getTopKey(String keyPath)
	{
		int index;
		String topKey = keyPath,
		       tempKey = keyPath;
		       
		while (!exists(keyPath))	// while not found along key path a pre-existing key.
		{
			//
			// if: '\' is not found, then we have reached an HKEY entry.
			//
			if ((index = keyPath.lastIndexOf('\\')) != -1)
			{
				//
				// tempKey is used as a backup value so that topKey will 
				// never actually refer to a HKEY
				//
				tempKey = topKey;
				topKey = keyPath;
				
				//
				// back one key out of the current key path (i.e., step up one key
				// on the key's path hierarchy
				//
				keyPath = keyPath.substring(0, index);
			}
			//
			// else: we've made it all the way to an HKEY topKey entry, can't go further.
			//
			else
			{
				topKey = tempKey;  // step back one key, so that topKey is not an HKEY.		
				break;
			}
		}
		
		debugMsg("topKey = " + topKey);
		
		return topKey;
	}


	/**
	 *
	 * This private method supplies the valueType and value in a delimited String
	 * that can be used as the rollback info when the advanced rollback functionality
	 * is enabled.
	 *
	 */
	private String getRollbackInfo(String[] registryArray)
	{
		Object originalValue = null;
		String originalValueType = null,
		       rollbackInfo = null;
		String tempValueName = registryArray[VALUENAME];
		
		// If comparing to the key's default value...
		//
		if (tempValueName.equalsIgnoreCase("$DEFAULT$") || tempValueName.equals("@"))
		{
			tempValueName = "";
		}

		
		try
		{
			originalValue = srm.getRegistryKeyValue(registryArray[KEYPATH], 
			                                        tempValueName);
			
			//
			// Construct the Rollback Information if appropriate.
			//
			if (originalValue != null)
			{
				if (originalValue instanceof Integer)
				{
					originalValueType = INTEGER_TYPE_CONST;		
				}
				else if (originalValue instanceof byte[])
				{
					//
					// store the contents of replaceValue byte array in an actual byte[] and
					// re-initialize the replace value to empty string.  We will refill it
					// with the String representation of the byte array below.
					//	
					byte[] byteArrayOriginalValue  = (byte[])originalValue;
					originalValue = "";
					
					originalValueType = BYTE_TYPE_CONST;
										
					for (int i = 0; i < byteArrayOriginalValue.length; i++)
					{
						originalValue = (String)originalValue 
						              + Byte.toString(byteArrayOriginalValue[i]) 
						              + " ";
					}	
				}
				//
				// if its not a Integer or byte[], it must be a String (assume it is).
				//
				else  
				{
					originalValueType = STRING_TYPE_CONST;
				}
				
				rollbackInfo = originalValueType + delimiter + originalValue;
			}
			else
			{
				rollbackInfo = null;
			}
		}
		catch (Exception e)
		{
			debugMsg("Exception getting rollback for: " + registryArray[KEYPATH] + " - " + registryArray[VALUENAME]);
			rollbackInfo = null;
		}
		
		return rollbackInfo;
	}


	/**
	 *
	 * This private method provides the return value to its calling method that
	 * represents the uninstall deletion data string used that is used to determine
	 * the verified rollback/removal values.
	 *
	 */
	private String getReturnValueForRollback(String[] registryArray, 
	                                         String topKey, 
	                                         String rollbackInfo, 
	                                         boolean trueParentKey)
	{
		String returnValue;
		
		if (rollbackFlag)
		{
			//
			// If we will be rolling back, we need to save the information about the
			// registry entry that we are making.
			//
			returnValue = INSTALLED_CONST + delimiter 
		                + registryArray[KEYPATH] + delimiter
		                + registryArray[VALUENAME] + delimiter
		                + registryArray[VALUETYPE] + delimiter
		                + registryArray[VALUE];
		}
		else
		{
			returnValue = "";
		}
		
		//
		// if: only trueParentKeys will have values to be rolled back.
		//
		if (trueParentKey)
		{
			if (registryArray[VALUENAME].equals(KEY_ONLY_CONST))
			{
				// replace INSTALLED_CONST part of returnValue (a Key will not have a 
				// rollback value).
				//
				returnValue = "";
			}
			else
			{
				//
				// if there were no prior values already occupying the registry entry that was
				// just made, mark for deletion.
				//
				// if we are not rolling back 'rollbackInfo' will always be 'null'.
				//
				if (rollbackInfo ==  null)
				{
					if (rollbackFlag)
					{
						returnValue += DELETE_CONST + delimiter;
					}

					returnValue += topKey + delimiter 
					             + registryArray[VALUENAME];
				}
				else
				{
					//
					// during deletion will want to be able to check if value
					// has changed since we set during install and, if so, do not replace 
					// *OR* remove.  This will make it so that installs after ours are 
					// not effected.
					//
					returnValue += ROLLBACK_CONST  + delimiter 
					             + registryArray[KEYPATH] + delimiter 
					             + registryArray[VALUENAME] + delimiter 
					             + rollbackInfo;
				} 
			}
			
		}
		//
		// else: not a trueParentKey, mark for deletion.
		//
		else
		{
			if (registryArray[VALUENAME].equals(KEY_ONLY_CONST))
			{
				returnValue = topKey; 
			}
			else
			{
				if (rollbackFlag)
				{
					returnValue += DELETE_CONST + delimiter;
				}
				
				returnValue += topKey;
			}
		}
		
		return returnValue;
	}
	

	/**
	 *
	 * String[] convertDelimitedStringTo_SRMP_InternalFormat(String registryEntry)
	 *
	 * This private method is used to convert a delimited String representing a registry entry
	 * into a String[] that can be easily used to represent a registry entry in
	 * SimpleRegistryManagerPlus's internal format, which is four String values
	 * representing: <keyPath, valueName, valueType, value>
	 *
	 * This method returns a String[] value that represents the above mentioned
	 * internal format.  A return value of null signifies that the String value passed
	 * in was a in an unknown format (i.e., it was a malformed registry entry).
	 *
	 */
	private String[] getSRMP_InternalFormatFromDelimitedString(String registryEntry)
	{
		String[] registryArray = null;
		
		// 
		// if !null and !"", then must have at least one Token (keyPath?).
		//
		// if it is null or "", then there is not a registry entry in this String.
		// Allow registryArray to remain null and return this value signifying a 
		// malformed registry entry.
		//
		if (registryEntry != null && !registryEntry.equals(""))
		{
			StringTokenizer st = new StringTokenizer(registryEntry, delimiter);
			int numberOfTokens = st.countTokens();
		
			registryArray = new String[MAX_REG_COMPONENTS];
					
					
			for (int i = 0; i < Math.min(numberOfTokens, MAX_REG_COMPONENTS); i++)
			{
				registryArray[i] = st.nextToken().trim();
			}

			switch (numberOfTokens)
			{
				//
				// Assume they are trying to just create a key, but check for some
				// validity.
				//
				case 1:
					if (registryArray[KEYPATH].toUpperCase().startsWith("HKEY"))
					{
						// registryArray[KEYPATH] remains the same
						registryArray[VALUENAME] = KEY_ONLY_CONST;
						registryArray[VALUETYPE] = "";
						registryArray[VALUE] = "";
					}
					else
					{
						registryArray = null;
					} 
					break;
				
				//
				// Maybe they only wanted to create a key and they left off the rest
				// of the data.
				// 
				case 2:
					if (registryArray[VALUENAME].equalsIgnoreCase(KEY_ONLY_CONST) ||
					    registryArray[VALUENAME].equals(""))
					{
						// registryArray[KEYPATH] remains the same
						registryArray[VALUENAME] = KEY_ONLY_CONST;
						registryArray[VALUETYPE] = "";
						registryArray[VALUE] = "";
					}
					else
					{
						registryArray = null;
					}
					
					break;
					
				//
				// If only have three tokens, just assume that they did not define the valueType
				// If no valueType is defined, default is String (hope that's what they wanted!!).
				// Need to move the the value currently stored at index VALUETYPE to its correct
				// location in the array as the element representing VALUE.
				//
				case 3:
					// registryArray[KEYPATH] remains the same
					// registryArray[VALUENAME] remains the same
					registryArray[VALUE] = registryArray[VALUETYPE];
					registryArray[VALUETYPE] = STRING_TYPE_CONST;
					break;
					
				// 
				// Got all four components, assume they did it correctly.
				//	
				case 4:
					// registryArray[KEYPATH] remains the same
					// registryArray[VALUENAME] remains the same
					// registryArray[VALUETYPE] remains the same
					// registryArray[VALUE] remains the same
					break;

				//
				// Something was wrong with the supplied string
				//
				default:
					stdMsg("Malformed Registry entry: " + registryEntry);
					registryArray = null;
					break;				
			}
		}		
		
		return registryArray;
	}
	
	/**
	 *
	 * String replaceDoubleBackSlashes(String value)
	 *
	 * This private method is used to replace the double backslashes ("\\") with a
	 * single backslash ("\").  If a registry entry value contains double backslashes,
	 * then it will not be rollbacked or removed correctly during the uninstall.
	 *
	 * Parameters:
	 *    value - The string containing the registry entry.
	 *
	 * Returns: a String value that represents the argument string
	 * with single slashes instead of double backslashes.
	 *
	 */
	private String replaceDoubleBackSlashes(String value)
	{
		StringBuffer valueStringBuffer = new StringBuffer("");

		// go through all the characters in the string.

		for ( int i = 0; i < value.length(); i++)
		{
			int j = i + 1;

			// if the char isn't a backslash, append the string buffer
			if ( value.charAt(i) != '\\')
			{
					valueStringBuffer.append(value.charAt(i));
			}
			else //if the char is a backslash
			{
				// check if i is the last character
				// if it is, getting value.charAt(j) would cause an out of bound exception
				if (i != value.length())
				{
				    // if the character after i is also a blackslash, append the
					// first backslash, and skip over the second backslash
					if (value.charAt(j) == '\\')
					{
						valueStringBuffer.append(value.charAt(i));
					    i++;
					}
					else // if the character after i is not a backslash, append the character at i
					{
						valueStringBuffer.append(value.charAt(i));
					}
				}
				else // if i is the last character, append the character at i
				{
						valueStringBuffer.append(value.charAt(i));
				}
			}
		}

		debugMsg("value            : " + value);
		debugMsg("valueStringBuffer: " + valueStringBuffer);
	    return valueStringBuffer.toString();
	}	
	// ***************************************************************************
}
