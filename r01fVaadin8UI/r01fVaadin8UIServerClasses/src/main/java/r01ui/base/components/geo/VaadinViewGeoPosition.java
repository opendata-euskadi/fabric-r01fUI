package r01ui.base.components.geo;

import r01f.types.geo.GeoCountry;
import r01f.types.geo.GeoCounty;
import r01f.types.geo.GeoLocality;
import r01f.types.geo.GeoMunicipality;
import r01f.types.geo.GeoOIDs.GeoZipCode;
import r01f.types.geo.GeoPortal;
import r01f.types.geo.GeoPosition;
import r01f.types.geo.GeoPosition2D;
import r01f.types.geo.GeoState;
import r01f.types.geo.GeoStreet;
import r01f.ui.viewobject.UIViewObjectWrappedBase;
import r01f.util.types.Strings;

public class VaadinViewGeoPosition 
     extends UIViewObjectWrappedBase<GeoPosition> {

	private static final long serialVersionUID = -4291550776181866804L;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinViewGeoPosition() {
		this(new GeoPosition());
	}
	public VaadinViewGeoPosition(final GeoPosition geoPos) {
		super(geoPos);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public String getAddressText() {
		return _wrappedModelObject.getAddressText() != null ? _wrappedModelObject.getAddressText() : "";
	}
	public void setAddressText(final String text) {
		_wrappedModelObject.setAddressText(text);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  COUNTRY
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String COUNTRY_FIELD = "country";

	public GeoCountry getCountry() {
		return _wrappedModelObject.getCountry();
	}
	public void setCountry(final GeoCountry country) {
		_wrappedModelObject.setCountry(country);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  STATE
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String STATE_FIELD = "state";

	public GeoState getState() {
		return _wrappedModelObject.getState();
	}
	public void setState(final GeoState state) {
		_wrappedModelObject.setState(state);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  COUNTY
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String COUNTY_FIELD = "county";

	public GeoCounty getCounty() {
		return _wrappedModelObject.getCounty();
	}
	public void setCounty(final GeoCounty county) {
		_wrappedModelObject.setCounty(county);
	}

/////////////////////////////////////////////////////////////////////////////////////////
//  MUNICIPALITY
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String MUNICIPALITY_FIELD = "municipality";

	public GeoMunicipality getMunicipality() {
		return _wrappedModelObject.getMunicipality();
	}
	public void setMunicipality(final GeoMunicipality municipality) {
		_wrappedModelObject.setMunicipality(municipality);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  LOCALITY
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String LOCALITY_FIELD = "locality";

	public GeoLocality getLocality() {
		return _wrappedModelObject.getLocality();
	}
	public void setLocality(final GeoLocality locality) {
		_wrappedModelObject.setLocality(locality);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  STREET
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String STREET_FIELD = "street";
	
	public GeoStreet getStreet() {
		return _wrappedModelObject.getStreet();
	}
	public void setStreet(final GeoStreet street) {
		_wrappedModelObject.setStreet(street);
	}
	
/////////////////////////////////////////////////////////////////////////////////////////
//  PORTAL
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String PORTAL_FIELD = "portal";
	
	public GeoPortal getPortal() {
		return _wrappedModelObject.getPortal();
	}
	public void setPortal(final GeoPortal portal) {
		_wrappedModelObject.setPortal(portal);
	}
	
/////////////////////////////////////////////////////////////////////////////////////////
//  GEOPOSITION 2D
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String POSITION_FIELD = "position";

	public GeoPosition2D getPosition() {
		return _wrappedModelObject.getPosition();
	}
	public void setPosition(final GeoPosition2D position) {
		_wrappedModelObject.setPosition(position);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  ZIP CODE
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String ZIP_CODE_FIELD = "zipCode";
	public String getZipCode() {
		return _wrappedModelObject.getZipCode() != null && Strings.isNOTNullOrEmpty(_wrappedModelObject.getZipCode().getId()) ? _wrappedModelObject.getZipCode().getId() : null;
	}
	public void setZipCode(final String zipCode) {
		_wrappedModelObject.setZipCode(Strings.isNOTNullOrEmpty(zipCode) ? GeoZipCode.forId(zipCode) : null);
	}
}
