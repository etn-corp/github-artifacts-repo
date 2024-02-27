
package com.eaton.electrical.smrc.bo;

import java.util.*;


/** This class is designed to hold all of the Vista Security Records - VSRecord(s)
*/
public class VistaSecurity implements java.io.Serializable {

    private boolean hasUniversalAccess = false;
	private Collection vistaSecurityRecords = new ArrayList();
	
	private static final long serialVersionUID = 100;

	public VistaSecurity(boolean hasUniversalAccess, Collection vistaSecurityRecords) {
	    this.setHasUniversalAccess(hasUniversalAccess);
	    this.setVistaSecurityRecords(vistaSecurityRecords);
	}

    /**
     * @return Returns the vistaSecurityRecords.
     */
    public Collection getVistaSecurityRecords() {
        return vistaSecurityRecords;
    }
    /**
     * @param vistaSecurityRecords The vistaSecurityRecords to set.
     */
    private void setVistaSecurityRecords(Collection vistaSecurityRecords) {
        this.vistaSecurityRecords = vistaSecurityRecords;
    }
    
    /**
     * @param hasUniversalAccess The hasUniversalAccess to set.
     */
    private void setHasUniversalAccess(boolean hasUniversalAccess) {
        this.hasUniversalAccess = hasUniversalAccess;
    }

    /**
     * @return Returns the hasUniversalAccess.
     */
    public boolean hasUniversalAccess() {
        return hasUniversalAccess;
    }
}
