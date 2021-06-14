package r01f.ui.vaadin.nora.contact;

import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Singleton;

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
import r01f.ui.presenter.UIPresenter;
import r01f.ui.presenter.UIPresenterSubscriber;

@Singleton
public class VaadinNORAContactFormPresenter 
  implements UIPresenter {
	private static final long serialVersionUID = 5141850348403136735L;

////////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final VaadinNORAContactFormCOREMediator _mediator;
	
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR & BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
	@Inject
	public VaadinNORAContactFormPresenter (final VaadinNORAContactFormCOREMediator mediator) {
		_mediator = mediator;
	}
	
/////////////////////////////////////////////////////////////////////////////////////////
//	LOAD
/////////////////////////////////////////////////////////////////////////////////////////	
	public void onCountriesLoadRequested(final UIPresenterSubscriber<Collection<GeoCountry>> subscriber) {
		try{
			Collection<GeoCountry> contries = _mediator.loadCountries();
			subscriber.onSuccess(contries);
		}catch (Throwable th) {
			subscriber.onError(th);
		}
	}
	public void onStatesLoadRequested(final GeoCountryID countryId,
									  final UIPresenterSubscriber<Collection<GeoState>> subscriber) {
		try{
			Collection<GeoState> states = _mediator.loadStates(countryId);
			subscriber.onSuccess(states);
		}catch (Throwable th) {
			subscriber.onError(th);
		}
	}
	public void onCountiesLoadRequested(final GeoStateID stateId,
									    final UIPresenterSubscriber<Collection<GeoCounty>> subscriber) {
		try{
			Collection<GeoCounty> counties = _mediator.loadCounties(stateId);
			subscriber.onSuccess(counties);
		}catch (Throwable th) {
			subscriber.onError(th);
		}
	}
	public void onRegionsLoadRequested(final GeoStateID stateId,
									  final GeoCountyID countyId,
									  final UIPresenterSubscriber<Collection<GeoRegion>> subscriber) {
		try{
			Collection<GeoRegion> regions = _mediator.loadRegions(stateId, countyId);
			subscriber.onSuccess(regions);
		}catch (Throwable th) {
			subscriber.onError(th);
		}
	}
	public void onMunicipalitiesLoadRequested(final GeoStateID stateId,
									          final GeoCountyID countyId,
									          final GeoRegionID regionId,
									          final UIPresenterSubscriber<Collection<GeoMunicipality>> subscriber) {
		try{
			Collection<GeoMunicipality> muns = _mediator.loadMunicipalities(stateId, countyId, regionId);
			subscriber.onSuccess(muns);
		}catch (Throwable th) {
			subscriber.onError(th);
		}
	}
//	public void onDistrictsLoadRequested(final GeoStateID stateId,
//									     final GeoCountyID countyId,
//									     final GeoMunicipalityID municipalityId,
//									     final UIPresenterSubscriber<Collection<GeoDistrict>> subscriber) {
//		try{
//			Collection<GeoDistrict> districts = _mediator.loadDistricts(stateId, countyId, municipalityId);
//			subscriber.onSuccess(districts);
//		}catch (Throwable th) {
//			subscriber.onError(th);
//		}
//	}
	public void onStreetLoadRequested(final GeoStateID stateId,
									  final GeoCountyID countyId,
									  final GeoRegionID regionId,
									  final GeoMunicipalityID municipalityId,
									  final UIPresenterSubscriber<Collection<GeoStreet>> subscriber) {
		try{
			Collection<GeoStreet> streets = _mediator.loadStreets(stateId, countyId, regionId, municipalityId);
			subscriber.onSuccess(streets);
		}catch (Throwable th) {
			subscriber.onError(th);
		}
	}
	
	public Collection<GeoStreet> findStreets(final GeoStateID stateId,
											 final GeoCountyID countyId,
											 final GeoRegionID regionId,
											 final GeoMunicipalityID municipalityId,
											 final String text) {
		return  _mediator.loadStreets(stateId, countyId, regionId, municipalityId, text);
	}
	
	public void onPortalLoadRequested(final GeoStateID stateId,
									  final GeoCountyID countyId,
									  final GeoRegionID regionId,
									  final GeoMunicipalityID municipalityId,
									  final GeoStreetID streertId,
									  final UIPresenterSubscriber<Collection<GeoPortal>> subscriber) {
		try{
			Collection<GeoPortal> portals = _mediator.loadPortals(stateId, countyId, regionId, municipalityId, streertId);
			subscriber.onSuccess(portals);
		}catch (Throwable th) {
			subscriber.onError(th);
		}
	}
}
