package r01f.ui.vaadin.nora.contact;

import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Singleton;

import r01f.locale.Language;
import r01f.types.geo.GeoCountry;
import r01f.types.geo.GeoCounty;
import r01f.types.geo.GeoLocality;
import r01f.types.geo.GeoMunicipality;
import r01f.types.geo.GeoOIDs.GeoCountryID;
import r01f.types.geo.GeoOIDs.GeoCountyID;
import r01f.types.geo.GeoOIDs.GeoIDBase;
import r01f.types.geo.GeoOIDs.GeoLocalityID;
import r01f.types.geo.GeoOIDs.GeoMunicipalityID;
import r01f.types.geo.GeoOIDs.GeoStateID;
import r01f.types.geo.GeoOIDs.GeoStreetID;
import r01f.types.geo.GeoPortal;
import r01f.types.geo.GeoPosition2D;
import r01f.types.geo.GeoPosition2DByStandard;
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

	public void onMunicipalitiesLoadRequested(final GeoStateID stateId,
									          final GeoCountyID countyId,
									          final UIPresenterSubscriber<Collection<GeoMunicipality>> subscriber) {
		try{
			Collection<GeoMunicipality> muns = _mediator.loadMunicipalities(stateId, countyId);
			subscriber.onSuccess(muns);
		}catch (Throwable th) {
			subscriber.onError(th);
		}
	}
	public void onLocalitiesLoadRequested(final GeoStateID stateId,
									      final GeoCountyID countyId,
									      final GeoMunicipalityID municipalityId,
									      final UIPresenterSubscriber<Collection<GeoLocality>> subscriber) {
		try{
			Collection<GeoLocality> loc = _mediator.loadLocalities(stateId, countyId, municipalityId);
			subscriber.onSuccess(loc);
		}catch (Throwable th) {
			subscriber.onError(th);
		}
	}


	public void onStreetLoadRequested(final GeoStateID stateId,
									  final GeoCountyID countyId,
									  final GeoMunicipalityID municipalityId,
									  final GeoLocalityID localityId,
									  final UIPresenterSubscriber<Collection<GeoStreet>> subscriber) {
		try{
			Collection<GeoStreet> streets = _mediator.loadStreets(stateId, countyId, municipalityId, localityId);
			subscriber.onSuccess(streets);
		}catch (Throwable th) {
			subscriber.onError(th);
		}
	}

	public Collection<GeoStreet> findStreets(final GeoStateID stateId,
											 final GeoCountyID countyId,
											 final GeoMunicipalityID municipalityId,
											 final GeoLocalityID localityId,
											 final String text) {
		return  _mediator.loadStreets(stateId, countyId, municipalityId, localityId, text);
	}

	public void onPortalLoadRequested(final GeoStateID stateId,
									  final GeoCountyID countyId,
									  final GeoMunicipalityID municipalityId,
									  final GeoLocalityID localityId,
									  final GeoStreetID streertId,
									  final UIPresenterSubscriber<Collection<GeoPortal>> subscriber) {
		try{
			Collection<GeoPortal> portals = _mediator.loadPortals(stateId, countyId, municipalityId, localityId, streertId);
			subscriber.onSuccess(portals);
		}catch (Throwable th) {
			subscriber.onError(th);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	SEARCH
/////////////////////////////////////////////////////////////////////////////////////////
	public void onSearchByZipRequested(final String zipCode, final Language lang, final UIPresenterSubscriber<VaadinNORAContactViewObject> subscriber) {

		try{
			VaadinNORAContactViewObject noraContactViewObject = new VaadinNORAContactViewObject(_mediator.searchByZipCode(zipCode), lang);
			subscriber.onSuccess(noraContactViewObject);
		}catch (Throwable th) {
			subscriber.onError(th);
		}
	}

	public void onSearchByGeoPosition2D(final GeoPosition2D geoPosition2D, final Language lang, final UIPresenterSubscriber<VaadinNORAContactViewObject> subscriber ) {
		try{
			VaadinNORAContactViewObject noraContactViewObject = new VaadinNORAContactViewObject(_mediator.searchByGeoPosition2D(geoPosition2D), lang);
			subscriber.onSuccess(noraContactViewObject);
		}catch (Throwable th) {
			subscriber.onError(th);
		}
	}

	public <OID extends GeoIDBase>void onSearchPosition2D(final OID oid, final GeoCountyID countyId,
														  final UIPresenterSubscriber<GeoPosition2D> subscriber) {
		try{
			GeoPosition2D geoPosition2D = _mediator.searchGeoPosition2D(oid, countyId);
			subscriber.onSuccess(geoPosition2D);
		}catch (Throwable th) {
			subscriber.onError(th);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	GeoPosition2D Transform
/////////////////////////////////////////////////////////////////////////////////////////
	public void onTransformGeoPositionFromETRS89toED50(final GeoPosition2D geoPosition2D,
													   final UIPresenterSubscriber<GeoPosition2D> subscriber) {
		try{
			GeoPosition2D outGeoPosition2D = _mediator.getGeoPositionFromETRS89toED50(geoPosition2D);
			subscriber.onSuccess(outGeoPosition2D);
		}catch (Throwable th) {
			subscriber.onError(th);
		}
	}
	public void onTransformGeoPositionFromED50toETRS89(final GeoPosition2D geoPosition2D,
													   final UIPresenterSubscriber<GeoPosition2D> subscriber) {
		try{
			GeoPosition2D outGeoPosition2D = _mediator.getGeoPositionFromED50toETRS89(geoPosition2D);
			subscriber.onSuccess(outGeoPosition2D);
		}catch (Throwable th) {
			subscriber.onError(th);
		}
	}
	public void onTransformGeoPositionFromED50toWGS84(final GeoPosition2D geoPosition2D,
													  final UIPresenterSubscriber<GeoPosition2D> subscriber) {
		try{
			GeoPosition2D outGeoPosition2D = _mediator.getGeoPositionFromED50toWGS84(geoPosition2D);
			subscriber.onSuccess(outGeoPosition2D);
		}catch (Throwable th) {
			subscriber.onError(th);
		}
	}
	public void onTransformGeoPositionFromWGS84toED50(final GeoPosition2D geoPosition2D,
													  final UIPresenterSubscriber<GeoPosition2D> subscriber) {
		try{
			GeoPosition2D outGeoPosition2D = _mediator.getGeoPositionFromWGS84toED50(geoPosition2D);
			subscriber.onSuccess(outGeoPosition2D);
		}catch (Throwable th) {
			subscriber.onError(th);
		}
	}
	public void onTransformGeoPositionByStatandard(final GeoPosition2D geoPosition2D,
												   final UIPresenterSubscriber<GeoPosition2DByStandard> subscriber) {
		try{
			GeoPosition2DByStandard outGeoPosition2DByStandard = _mediator.getGeoPosition2DByStandard(geoPosition2D);
			subscriber.onSuccess(outGeoPosition2DByStandard);
		}catch (Throwable th) {
			subscriber.onError(th);
		}
	}
}
