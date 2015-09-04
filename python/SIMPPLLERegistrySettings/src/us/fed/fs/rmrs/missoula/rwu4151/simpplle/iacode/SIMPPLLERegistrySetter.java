package us.fed.fs.rmrs.missoula.rwu4151.simpplle.iacode;

import com.zerog.ia.api.pub.CustomCodeAction;
import com.zerog.ia.api.pub.InstallException;
import com.zerog.ia.api.pub.InstallerProxy;
import com.zerog.ia.api.pub.UninstallerProxy;
import com.zerog.ia.customcode.win32.registry.SimpleRegistryManagerPlus;



public class SIMPPLLERegistrySetter extends CustomCodeAction {
    private static final String INSTALL_MESSAGE = "Registry Entries";
    private static final String UNINSTALL_MESSAGE = "Registry Entries";

    public String getInstallStatusMessage() {
        return INSTALL_MESSAGE;	// screen will say "Installing... <INSTALL_MESSAGE>"
    }

    public String getUninstallStatusMessage() {
        return UNINSTALL_MESSAGE; // screen will say "Uninstalling... <UNINSTALL_MESSAGE>"
    }

    public void install(InstallerProxy installerProxy) throws InstallException {
        SimpleRegistryManagerPlus srmp = new SimpleRegistryManagerPlus(installerProxy);

        //
        // check to see if we should be in 'full' debugging mode.
        //
        String checkDebug = null;
        if ( (((checkDebug = (String)installerProxy.getVariable("CC_DEBUG")) != null) &&
              checkDebug.equalsIgnoreCase("TRUE")) ||
             (((checkDebug = (String)installerProxy.getVariable("lax.nl.env.CC_DEBUG")) != null) &&
              checkDebug.equalsIgnoreCase("TRUE")) ||
             (((checkDebug = (String)System.getProperty("cc_debug")) != null) &&
              checkDebug.equalsIgnoreCase("TRUE")))
        {
                SimpleRegistryManagerPlus.debug = true;
        }


        String installDir = installerProxy.substitute("$PARAM_INSTALL_DIR$");
        srmp.setRegistryKeyValue("HKEY_CURRENT_USER\\Environment","SIMPPLLE_HOME",installDir);
        srmp.setRegistryKeyValue("HKEY_CURRENT_USER\\Environment","SIMPPLLE_JAVAMEM","800");
    }

    public void uninstall(UninstallerProxy uninstallerProxy) throws
            InstallException {
    }
}
