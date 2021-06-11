package r01f.ui.vaadin.nora.contact;

import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Singleton;

import r01f.ejie.nora.NORAService;
import r01f.ejie.nora.NORAServiceConfig;
import r01f.types.geo.GeoCountry;
import r01f.types.geo.GeoCounty;
import r01f.types.geo.GeoMunicipality;
import r01f.types.geo.GeoOIDs.GeoCountryID;
import r01f.types.geo.GeoOIDs.GeoCountyID;
import r01f.types.geo.GeoOIDs.GeoMunicipalityID;
import r01f.types.geo.GeoOIDs.GeoRegionID;
import r01f.types.geo.GeoOIDs.GeoStateID;
import r01f.types.geo.GeoOIDs.GeoStreetID;
import r01f.types.geo.GeoPortal;
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
	private static final NORAServiceConfig cfg = new NORAServiceConfig(Url.from("http://svc.inter.integracion.jakina.ejgvdns/ctxapp/t17iApiWS"));
	private final NORAService _nora;
	
	@Inject
	public VaadinNORAContactFormCOREMediator() {
		super();
		_nora = new NORAService(cfg);
	}
	
/////////////////////////////////////////////////////////////////////////////////////////
//	METHODS
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
	
	public Collection<GeoMunicipality> loadMunicipalities(final GeoStateID stateId, final GeoCountyID countyId, final GeoRegionID regionId) {
		Collection<GeoMunicipality> muns = _nora.getServicesForMunicipalities()
										  		.getMunicipalitiesOf(stateId, countyId, regionId);
		return muns;
	}
	
	public Collection<GeoStreet> loadStreets(final GeoStateID stateId, final GeoCountyID countyId, final GeoRegionID regionId, final GeoMunicipalityID municipalityId){
		Collection<GeoStreet> streets = _nora.getServicesForStreets()
											 .getStreetsOf(stateId, countyId, regionId, municipalityId);
		return streets;
	}
	public Collection<GeoStreet> loadStreets(final GeoStateID stateId, final GeoCountyID countyId, final GeoRegionID regionId, final GeoMunicipalityID municipalityId, String text){
		Collection<GeoStreet> streets = _nora.getServicesForStreets()
											 .findStreetsWithTextOf(stateId, countyId, regionId, municipalityId, text);
		return streets;
	}
	
	public Collection<GeoPortal> loadPortals(final GeoStateID stateId, final GeoCountyID countyId, final GeoRegionID regionId, final GeoMunicipalityID municipalityId, final GeoStreetID streetId){
		Collection<GeoPortal> portals = _nora.getServicesForPortal()
											 .getPortalsOf(stateId, countyId, regionId, municipalityId,streetId);
		return portals;
	}
//	public Collection<GeoDistrict> loadDistricts(final GeoStateID stateId, final GeoCountyID countyId, final GeoMunicipalityID municipalityId){
//		Collection<GeoDistrict> districts = _nora.getServicesForDistricts()
//											     .getDistrictsOf(stateId, countyId, municipalityId);
//		return districts;
//	}
	

}
