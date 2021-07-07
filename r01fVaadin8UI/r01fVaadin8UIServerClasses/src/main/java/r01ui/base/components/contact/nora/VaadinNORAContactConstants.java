package r01ui.base.components.contact.nora;

import r01f.types.geo.GeoPosition2D;
import r01f.types.geo.GeoPosition2D.GeoPositionStandard;

public class VaadinNORAContactConstants {
////////////////////////////////////////////////////////////////////////////////////////////
//	ZOOM
/////////////////////////////////////////////////////////////////////////////////////////

	public final static int COUNTRY_ZOOM = -5;
	public final static int STATE_ZOOM = 0;
	public final static int MUNICIPALITY_ZOOM = 1;
	public final static int PORTAL_ZOOM = 7;
	
////////////////////////////////////////////////////////////////////////////////////////////
//	DEFAULT COORDS
/////////////////////////////////////////////////////////////////////////////////////////
	public final static GeoPosition2D SPAIN_COORDS = new GeoPosition2D(GeoPositionStandard.ETRS89, 440186.125, 4477635.642);
	public final static GeoPosition2D BASQUE_COUNTRY_COORDS = new GeoPosition2D(GeoPositionStandard.ETRS89, 535655.825, 4765014.757);
	
	
}
