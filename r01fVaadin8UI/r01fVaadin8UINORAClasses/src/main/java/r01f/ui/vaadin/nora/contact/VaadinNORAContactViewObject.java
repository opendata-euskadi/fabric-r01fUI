package r01f.ui.vaadin.nora.contact;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.locale.Language;
import r01f.types.geo.GeoCountry;
import r01f.types.geo.GeoCounty;
import r01f.types.geo.GeoLocality;
import r01f.types.geo.GeoMunicipality;
import r01f.types.geo.GeoPortal;
import r01f.types.geo.GeoPosition;
import r01f.types.geo.GeoRegion;
import r01f.types.geo.GeoState;
import r01f.types.geo.GeoStreet;
import r01f.ui.viewobject.UIViewObjectInLanguage;
import r01f.ui.viewobject.UIViewObjectWrappedBase;

@Accessors(prefix="_")
public class VaadinNORAContactViewObject 
     extends UIViewObjectWrappedBase<GeoPosition>
  implements UIViewObjectInLanguage {

	private static final long serialVersionUID = 3737245877819480835L;
	
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter protected final Language _lang;
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTORS
/////////////////////////////////////////////////////////////////////////////////////////	
	public VaadinNORAContactViewObject(final GeoPosition wrappedModelObject,
									   final Language lang) {
		super(wrappedModelObject);
		_lang = lang;
	}
	public VaadinNORAContactViewObject(final Language lang) {
		this(new GeoPosition(), lang);
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

}
