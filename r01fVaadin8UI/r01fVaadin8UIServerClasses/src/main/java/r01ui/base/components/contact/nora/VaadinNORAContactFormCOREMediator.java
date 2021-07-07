package r01ui.base.components.contact.nora;

import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Singleton;

import r01f.ejie.nora.NORAService;
import r01f.ejie.nora.NORAServiceConfig;
import r01f.types.geo.GeoCountry;
import r01f.types.geo.GeoCounty;
import r01f.types.geo.GeoLocality;
import r01f.types.geo.GeoMunicipality;
import r01f.types.geo.GeoOIDs.GeoCountryID;
import r01f.types.geo.GeoOIDs.GeoCountyID;
import r01f.types.geo.GeoOIDs.GeoIDBase;
import r01f.types.geo.GeoOIDs.GeoLocalityID;
import r01f.types.geo.GeoOIDs.GeoMunicipalityID;
import r01f.types.geo.GeoOIDs.GeoPortalID;
import r01f.types.geo.GeoOIDs.GeoStateID;
import r01f.types.geo.GeoOIDs.GeoStreetID;
import r01f.types.geo.GeoOIDs.GeoZipCode;
import r01f.types.geo.GeoPortal;
import r01f.types.geo.GeoPosition;
import r01f.types.geo.GeoPosition2D;
import r01f.types.geo.GeoPosition2D.GeoPositionStandard;
import r01f.types.geo.GeoPosition2DByStandard;
import r01f.types.geo.GeoRegion;
import r01f.types.geo.GeoState;
import r01f.types.geo.GeoStreet;
import r01f.types.url.Url;
import r01f.ui.coremediator.UICOREMediator;

@Singleton
public class VaadinNORAContactFormCOREMediator 
  implements UICOREMediator {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private static final NORAServiceConfig cfg = new NORAServiceConfig(Url.from("http://svc.inter.integracion.jakina.ejgvdns/ctxapp/t17iApiWS")); //http://svc.inter.integracion.jakina.ejgvdns/ctxapp/t17iApiWS
	private final NORAService _nora;
	
	@Inject
	public VaadinNORAContactFormCOREMediator() {
		super();
		_nora = new NORAService(cfg);
	}
	
/////////////////////////////////////////////////////////////////////////////////////////
//	METHODS
/////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////
//	LOAD
/////////////////////////////////////////////////////////////////////////////////////////
	public Collection<GeoCountry> loadCountries() {
		Collection<GeoCountry> countries = _nora.getServicesForCountries()
												.getCountries();
		return countries;
	}
	
	
	public Collection<GeoState> loadStates(final GeoCountryID countryId) {
		Collection<GeoState> states= _nora.getServicesForStates()
										  .getStatesOf(countryId);
		return states;
	} 
	
	public Collection<GeoCounty> loadCounties(final GeoStateID stateId) {
		Collection<GeoCounty> counties = _nora.getServicesForCounties()
											  .getCountiesOf(stateId);
		return counties;
	} 
	
	public Collection<GeoRegion> loadRegions(final GeoStateID stateId, final GeoCountyID countyId) {
		Collection<GeoRegion> region = _nora.getServicesForRegions()
											.getRegionsOf(stateId, countyId);
		return region;
	} 
	
	public Collection<GeoMunicipality> loadMunicipalities(final GeoStateID stateId, final GeoCountyID countyId) {
		Collection<GeoMunicipality> muns = _nora.getServicesForMunicipalities()
										  		.getMunicipalitiesOf(stateId, countyId);
		return muns;
	}
	public Collection<GeoLocality> loadLocalities(final GeoStateID stateId, final GeoCountyID countyId, final GeoMunicipalityID municipalityId) {
		Collection<GeoLocality> loc = _nora.getServicesForLocalities()
										  		.getLocalitiesOf(stateId, countyId, municipalityId);
		return loc;
	}
	public Collection<GeoStreet> loadStreets(final GeoStateID stateId, final GeoCountyID countyId, final GeoMunicipalityID municipalityId, final GeoLocalityID localityId){
		Collection<GeoStreet> streets = _nora.getServicesForStreets()
											 .getStreetsOf(stateId, countyId, municipalityId,localityId);
		return streets;
	}
	public Collection<GeoStreet> loadStreets(final GeoStateID stateId, final GeoCountyID countyId, final GeoMunicipalityID municipalityId, final GeoLocalityID localityId, String text){
		Collection<GeoStreet> streets = _nora.getServicesForStreets()
											 .findStreetsWithTextOf(stateId, countyId, municipalityId, localityId, text);
		return streets;
	}
	
	public Collection<GeoPortal> loadPortals(final GeoStateID stateId, final GeoCountyID countyId, final GeoMunicipalityID municipalityId, final GeoLocalityID localityId,  
											 final GeoStreetID streetId){
		Collection<GeoPortal> portals = _nora.getServicesForPortal()
											 .getPortalsOf(stateId, countyId, municipalityId,localityId,streetId);
		return portals;
	}

/////////////////////////////////////////////////////////////////////////////////////////
//	SEARCH
/////////////////////////////////////////////////////////////////////////////////////////
	public GeoPosition searchByZipCode(final String zipCode) {
		GeoLocality loc = _nora.getServicesForLocalities().getLocalitybyZipCode(GeoZipCode.forId(zipCode)).iterator().next();
		return _loadFromLocality(loc);
	}
	
	public GeoPosition searchByGeoPosition2D(GeoPosition2D geoPosition2D) {
		GeoLocality loc = _nora.getServicesForLocalities().getLocalitybyGeoPosition2D(geoPosition2D);
		return _loadFromLocality(loc);
	}
	
	private GeoPosition _loadFromLocality(final GeoLocality loc) {
		GeoPosition geo = new GeoPosition();
		geo.setLocality(loc);
		GeoCountry country = _nora.getServicesForCountries().getCountry(loc.getCountryId());
		geo.setCountry(country);
		GeoState state = _nora.getServicesForStates().getState(loc.getStateId());
		geo.setState(state);
		GeoCounty county = _nora.getServicesForCounties().getCounty(loc.getStateId(), loc.getCountyId());
		geo.setCounty(county);
		GeoMunicipality mun = _nora.getServicesForMunicipalities().getMunicipality(loc.getStateId(), loc.getCountyId(), loc.getMunicipalityId());
		geo.setMunicipality(mun);
		return geo;
	}
	
	public <OID extends GeoIDBase>GeoPosition2D searchGeoPosition2D(final OID oid, final GeoCountyID countyId) {
		GeoPosition2D geoPosition2D = new GeoPosition2D();
		if (oid instanceof GeoCountryID) {
			geoPosition2D = _nora.getServicesForCountries().getCountry((GeoCountryID)oid).getPosition2D();
		} else if (oid instanceof GeoStateID) {
			geoPosition2D = _nora.getServicesForStates().getState((GeoStateID)oid).getPosition2D();
		} else if (oid instanceof GeoCountyID) {
			geoPosition2D =  _nora.getServicesForCounties().getCounty(null, (GeoCountyID)oid).getPosition2D();
		} else if (oid instanceof GeoMunicipalityID) {
			geoPosition2D =  _nora.getServicesForMunicipalities().getMunicipality(null, countyId, (GeoMunicipalityID)oid).getPosition2D();
		} else if (oid instanceof GeoLocalityID) {
			geoPosition2D =  _nora.getServicesForLocalities().getLocality(null, null, null, (GeoLocalityID)oid).getPosition2D();
		} else if (oid instanceof GeoStreetID) {
			geoPosition2D =  _nora.getServicesForStreets().getStreet(null, null, null, (GeoStreetID)oid).getPosition2D();
		} else if (oid instanceof GeoPortalID) {
			geoPosition2D =  _nora.getServicesForPortal().getPortal(null, null, null, null, null, (GeoPortalID)oid).getPosition2D();
		}
		return geoPosition2D;
		
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	GeoPosition2D Transform
/////////////////////////////////////////////////////////////////////////////////////////	
	public GeoPosition2D getGeoPositionFromETRS89toED50(final GeoPosition2D geoPosition2D) {
		GeoPosition2D outGeoPosition2D = _nora.getServicesForGeoPosition2D().getGeoPositionFromETRS89toED50(geoPosition2D);
		return outGeoPosition2D;
	}
	public GeoPosition2D getGeoPositionFromED50toETRS89(final GeoPosition2D geoPosition2D) {
		GeoPosition2D outGeoPosition2D = _nora.getServicesForGeoPosition2D().getGeoPositionFromED50toETRS89(geoPosition2D);
		return outGeoPosition2D;
	}
	public GeoPosition2D getGeoPositionFromED50toWGS84(final GeoPosition2D geoPosition2D) {
		GeoPosition2D outGeoPosition2D = _nora.getServicesForGeoPosition2D().getGeoPositionFromED50toWGS84(geoPosition2D);
		return outGeoPosition2D;
	}
	public GeoPosition2D getGeoPositionFromWGS84toED50(final GeoPosition2D geoPosition2D) {
		GeoPosition2D outGeoPosition2D = _nora.getServicesForGeoPosition2D().getGeoPositionFromWGS84toED50(geoPosition2D);
		return outGeoPosition2D;
	}
	public GeoPosition2DByStandard getGeoPosition2DByStandard(final GeoPosition2D geoPosition2D) {
		GeoPosition2DByStandard geoPosition2DByStandard = new GeoPosition2DByStandard();
		geoPosition2DByStandard.add(geoPosition2D);
		if (geoPosition2D.getStandard().equals(GeoPositionStandard.ETRS89)) {
			GeoPosition2D geoPosition2DED50 = getGeoPositionFromETRS89toED50(geoPosition2D);
			geoPosition2DByStandard.add(geoPosition2DED50);
			GeoPosition2D geoPosition2DWGS84 = getGeoPositionFromED50toWGS84(geoPosition2DED50);
			geoPosition2DByStandard.add(geoPosition2DWGS84);
		} else if (geoPosition2D.getStandard().equals(GeoPositionStandard.ED50)) {
			GeoPosition2D geoPosition2DWGS84 = getGeoPositionFromED50toWGS84(geoPosition2D);
			geoPosition2DByStandard.add(geoPosition2DWGS84);
			GeoPosition2D geoPosition2DETRS89 = getGeoPositionFromED50toETRS89(geoPosition2D);
			geoPosition2DByStandard.add(geoPosition2DETRS89);
		} else if (geoPosition2D.getStandard().equals(GeoPositionStandard.GOOGLE)) {
			GeoPosition2D geoPosition2DED50 = getGeoPositionFromWGS84toED50(geoPosition2D);
			geoPosition2DByStandard.add(geoPosition2DED50);
			GeoPosition2D geoPosition2DETRS89 = getGeoPositionFromED50toETRS89(geoPosition2DED50);
			geoPosition2DByStandard.add(geoPosition2DETRS89);
		}
		return geoPosition2DByStandard;
	}
}
