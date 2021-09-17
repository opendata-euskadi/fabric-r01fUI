package r01ui.base.components.contact.nora;

import java.util.ArrayList;
import java.util.Collection;

import com.vaadin.data.Binder;
import com.vaadin.data.provider.CallbackDataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.ejie.nora.NORAGeoIDs;
import r01f.facets.HasLanguage;
import r01f.locale.Language;
import r01f.types.geo.GeoCountry;
import r01f.types.geo.GeoCounty;
import r01f.types.geo.GeoLocality;
import r01f.types.geo.GeoLocationBase;
import r01f.types.geo.GeoMunicipality;
import r01f.types.geo.GeoOIDs.GeoCountyID;
import r01f.types.geo.GeoOIDs.GeoIDBase;
import r01f.types.geo.GeoOIDs.GeoMunicipalityID;
import r01f.types.geo.GeoOIDs.GeoRegionID;
import r01f.types.geo.GeoPortal;
import r01f.types.geo.GeoPosition2D;
import r01f.types.geo.GeoState;
import r01f.types.geo.GeoStreet;
import r01f.ui.i18n.UII18NService;
import r01f.ui.presenter.UIPresenterSubscriber;
import r01f.ui.vaadin.annotations.LangIndependentVaadinViewField;
import r01f.ui.vaadin.annotations.VaadinViewComponentLabels;
import r01f.ui.vaadin.annotations.VaadinViewField;
import r01f.ui.vaadin.styles.VaadinValoTheme;
import r01f.ui.vaadin.view.VaadinView;
import r01f.ui.vaadin.view.VaadinViewI18NMessagesCanBeUpdated;
import r01f.ui.vaadin.view.VaadinViews;
import r01f.util.types.Strings;
import r01ui.base.components.button.VaadinToggleButton;
import r01ui.base.components.form.VaadinFormEditsViewObject;
import r01ui.base.components.geo.VaadinViewGeoPosition;

@Accessors(prefix="_")
public class VaadinNORAContactForm
     extends VerticalLayout
  implements VaadinView,
  			 VaadinFormEditsViewObject<VaadinViewGeoPosition>,
  			 VaadinViewI18NMessagesCanBeUpdated,
  			 HasLanguage {

	private static final long serialVersionUID = -7975207266889688539L;
/////////////////////////////////////////////////////////////////////////////////////////
//  SERVICES
/////////////////////////////////////////////////////////////////////////////////////////
	private final UII18NService _i18n;

/////////////////////////////////////////////////////////////////////////////////////////
//	LANGUAGE
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final Language _language;

	@Override
	public void setLanguage(final Language lang) {
		throw new UnsupportedOperationException();
	}

	private final VaadinNORAContactFormPresenter _presenter;
////////////////////////////////////////////////////////////////////////////////////////////
//	UI
/////////////////////////////////////////////////////////////////////////////////////////
	@VaadinViewField(bindToViewObjectFieldNamed=VaadinViewGeoPosition.COUNTRY_FIELD,
					 bindStringConverter=false,required=false)
	@LangIndependentVaadinViewField
	@VaadinViewComponentLabels(captionI18NKey="geo.country",
							   useCaptionI18NKeyAsPlaceHolderKey=true)
	@Getter @Setter private ComboBox<GeoCountry> _countryCmb = new ComboBox<>();

	@VaadinViewField(bindToViewObjectFieldNamed=VaadinViewGeoPosition.STATE_FIELD,
					 bindStringConverter=false,required=false)
	@LangIndependentVaadinViewField
	@VaadinViewComponentLabels(captionI18NKey="geo.state",
							   useCaptionI18NKeyAsPlaceHolderKey=true)
	@Getter @Setter private ComboBox<GeoState> _stateCmb = new ComboBox<>();
	@VaadinViewComponentLabels(captionI18NKey="geo.state",
							   useCaptionI18NKeyAsPlaceHolderKey=false)
	@Getter @Setter private TextField _stateTf = new TextField();
	
		
	@VaadinViewField(bindToViewObjectFieldNamed=VaadinViewGeoPosition.COUNTY_FIELD,
					 bindStringConverter=false,required=false)
	@LangIndependentVaadinViewField
	@VaadinViewComponentLabels(captionI18NKey="geo.county",
							   useCaptionI18NKeyAsPlaceHolderKey=true)
	@Getter @Setter private ComboBox<GeoCounty> _countyCmb = new ComboBox<>();
	@VaadinViewComponentLabels(captionI18NKey="geo.county",
							   useCaptionI18NKeyAsPlaceHolderKey=false)
	@Getter @Setter private TextField _countyTf = new TextField();


	@VaadinViewField(bindToViewObjectFieldNamed=VaadinViewGeoPosition.MUNICIPALITY_FIELD,
					 bindStringConverter=false,required=false)
	@LangIndependentVaadinViewField
	@VaadinViewComponentLabels(captionI18NKey="geo.municipality",
							   useCaptionI18NKeyAsPlaceHolderKey=true)
	@Getter @Setter private ComboBox<GeoMunicipality> _municipalityCmb = new ComboBox<>();
	@VaadinViewComponentLabels(captionI18NKey="geo.municipality",
							   useCaptionI18NKeyAsPlaceHolderKey=false)
	@Getter @Setter private TextField _municipalityTf = new TextField();
	
	@VaadinViewField(bindToViewObjectFieldNamed=VaadinViewGeoPosition.LOCALITY_FIELD,
					 bindStringConverter=false,required=false)
	@LangIndependentVaadinViewField
	@VaadinViewComponentLabels(captionI18NKey="geo.locality",
							   useCaptionI18NKeyAsPlaceHolderKey=true)
	@Getter @Setter private ComboBox<GeoLocality> _localityCmb = new ComboBox<>();
	@VaadinViewComponentLabels(captionI18NKey="geo.locality",
							   useCaptionI18NKeyAsPlaceHolderKey=false)
	@Getter @Setter private TextField _localityTf = new TextField();
	
	@VaadinViewField(bindToViewObjectFieldNamed=VaadinViewGeoPosition.STREET_FIELD,
					 bindStringConverter=false,required=false)
	@LangIndependentVaadinViewField
	@VaadinViewComponentLabels(captionI18NKey="geo.street",
							   useCaptionI18NKeyAsPlaceHolderKey=false)
	@Getter @Setter private ComboBox<GeoStreet> _streetCmb = new ComboBox<>();
	@VaadinViewComponentLabels(captionI18NKey="geo.street",
							   useCaptionI18NKeyAsPlaceHolderKey=false)
	@Getter @Setter private TextField _streetTf = new TextField();
	
	private final VaadinToggleButton _toggleStreetBtn = new VaadinToggleButton();
	
	@VaadinViewField(bindToViewObjectFieldNamed=VaadinViewGeoPosition.PORTAL_FIELD,
					 bindStringConverter=false,required=false)
	@LangIndependentVaadinViewField
	@VaadinViewComponentLabels(captionI18NKey="geo.portal",
							   useCaptionI18NKeyAsPlaceHolderKey=true)
	@Getter @Setter private ComboBox<GeoPortal> _portalCmb = new ComboBox<GeoPortal>();
	@VaadinViewComponentLabels(captionI18NKey="geo.portal",
							   useCaptionI18NKeyAsPlaceHolderKey=true)
	@Getter @Setter private TextField _portalTf = new TextField();

	@VaadinViewField(bindToViewObjectFieldNamed=VaadinViewGeoPosition.ZIP_CODE_FIELD,
					 bindStringConverter=false,required=false)
	@LangIndependentVaadinViewField
	@VaadinViewComponentLabels(captionI18NKey="geo.zip",
							   useCaptionI18NKeyAsPlaceHolderKey=true)
	@Getter @Setter private TextField _zipCodeTf = new TextField();

	@VaadinViewField(bindToViewObjectFieldNamed=VaadinViewGeoPosition.POSITION_FIELD,
					 bindStringConverter=false,required=false)
	@LangIndependentVaadinViewField
	@Getter private VaadinNORAContactGeoPostion2DComponent _coords;

	private final Button _searchByZipCodeBtn = new Button();


	// Binder
	@Getter private final Binder<VaadinViewGeoPosition> _vaadinUIBinder = new Binder<>(VaadinViewGeoPosition.class);
	private VaadinViewGeoPosition _viewObject;

	private int _zoom_level = VaadinNORAContactConstants.COUNTRY_ZOOM;

////////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinNORAContactForm(final UII18NService i18n,
								 final Language lang,
								 final VaadinNORAContactFormPresenter presenter) {
		super();
		_i18n = i18n;
		_language = lang;
		_presenter = presenter;
		_searchByZipCodeBtn.setIcon(VaadinIcons.SEARCH);
		_searchByZipCodeBtn.setWidth(50,Unit.PIXELS);
		_toggleStreetBtn.setWidth(50,Unit.PIXELS);
		_coords = new VaadinNORAContactGeoPostion2DComponent(_i18n,
															   presenter);

		////////// Layout & style
		this.setSizeFull();
		this.setSpacing(true);
		this.setMargin(false);

		_countryCmb.setWidth(100, Unit.PERCENTAGE);
		_stateCmb.setWidth(100, Unit.PERCENTAGE);
		_stateTf.setWidth(100, Unit.PERCENTAGE);
		_countyCmb.setWidth(100, Unit.PERCENTAGE);
		_countyTf.setWidth(100, Unit.PERCENTAGE);
		_municipalityCmb.setWidth(100, Unit.PERCENTAGE);
		_municipalityTf.setWidth(100, Unit.PERCENTAGE);
		_localityCmb.setWidth(100, Unit.PERCENTAGE);
		_localityTf.setWidth(100, Unit.PERCENTAGE);
		_streetCmb.setWidth(100, Unit.PERCENTAGE);
		_streetTf.setWidth(100, Unit.PERCENTAGE);
		_streetCmb.addStyleName(VaadinValoTheme.COMBO_SUGGESTION);
		_streetCmb.setIcon(VaadinIcons.FILTER);
		_streetCmb.addStyleName("inline-icon");
		_streetCmb.setPlaceholder("Filtrar por texto");
		_portalTf.setWidth(100, Unit.PERCENTAGE);
		
		HorizontalLayout streetHl = new HorizontalLayout(_streetCmb, _toggleStreetBtn);
		streetHl.setSpacing(false);
		streetHl.setWidth(100, Unit.PERCENTAGE);
		streetHl.setExpandRatio(_streetCmb, 3);
		streetHl.setExpandRatio(_toggleStreetBtn, 1);
		streetHl.setComponentAlignment(_toggleStreetBtn, Alignment.BOTTOM_LEFT);
		
		HorizontalLayout hlZip = new HorizontalLayout(_zipCodeTf,_searchByZipCodeBtn);
		hlZip.setSpacing(false);
		hlZip.setComponentAlignment(_searchByZipCodeBtn, Alignment.BOTTOM_LEFT);
		this.addComponent(hlZip);
		this.addComponent(_getHl(_countryCmb, _stateCmb));
		this.addComponent(_getHl(_countyCmb, _municipalityCmb));
		this.addComponent(_getHl(_localityCmb, streetHl));
		this.addComponent(_getHl(_portalCmb, new Label()));
		this.addComponent(_coords);


		CallbackDataProvider<GeoStreet,String> dataProvider = null;
		dataProvider = new CallbackDataProvider<>(// query
									query -> {
												String text = query.getFilter().orElse(null);
												if (_stateCmb.getValue() != null && _countyCmb.getValue() != null &&  _municipalityCmb.getValue() != null && _localityCmb.getValue() != null
														&& Strings.isNOTNullOrEmpty(text) && text.length()>2) {
													return _presenter.findStreets(_stateCmb.getValue() != null ? _stateCmb.getValue().getId() : null,
																				  _countyCmb.getValue() != null ? _countyCmb.getValue().getId() : null,
																				  _municipalityCmb.getValue() != null ? _municipalityCmb.getValue().getId() : null,
																				  _localityCmb.getValue() != null ? _localityCmb.getValue().getId() : null,
																				  text)
																	 .stream();
												}
												return new ArrayList<GeoStreet>().stream();

									},
									// count
									query -> {
												String text = query.getFilter().orElse(null);
												if (_stateCmb.getValue() != null && _countyCmb.getValue() != null &&  _municipalityCmb.getValue() != null && _localityCmb.getValue() != null
														&& Strings.isNOTNullOrEmpty(text) && text.length()>2) {
													int value =  (int)_presenter.findStreets(_stateCmb.getValue() != null ? _stateCmb.getValue().getId() : null,
																				  _countyCmb.getValue() != null ? _countyCmb.getValue().getId() : null,
																				  _municipalityCmb.getValue() != null ? _municipalityCmb.getValue().getId() : null,
																				  _localityCmb.getValue() != null ? _localityCmb.getValue().getId() : null,
																				  text)
																	 .stream()
																	 .count();
													return value;
												}
												return 0;
									});
		_streetCmb.setDataProvider(dataProvider);
		// set ui labels
		VaadinViews.using(_i18n)
				   .setI18NLabelsOf(this);
		_viewObject = new VaadinViewGeoPosition();
		////////// Bind: automatic binding using using @VaadinViewField annotation of view fields
		VaadinViews.using(_vaadinUIBinder,_i18n)
					.bindComponentsOf(this)
					.toViewObjectOfType(VaadinViewGeoPosition.class);
		_setBehavior();
	//	_loadDefaultCountryAndState();
		
	}

	private static HorizontalLayout _getHl(final Component c1, final Component c2) {
		HorizontalLayout hl = new HorizontalLayout(c1, c2);
		hl.setWidth(100, Unit.PERCENTAGE);
		hl.setExpandRatio(c1, 2);
		hl.setExpandRatio(c2, 3);
		return hl;
	}
////////////////////////////////////////////////////////////////////////////////////////////
//	FORM
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void editViewObject(final VaadinViewGeoPosition viewObj) {
		_viewObject = viewObj;
		_vaadinUIBinder.readBean(viewObj);
		_setValuetoTextField(_stateTf, viewObj.getState());
		_setValuetoTextField(_countyTf, viewObj.getCounty());
		_setValuetoTextField(_municipalityTf, viewObj.getMunicipality());
		_setValuetoTextField(_localityTf, viewObj.getLocality());
		_setValuetoTextField(_streetTf, viewObj.getStreet());
		_toggleStreetBtn.setValue(viewObj.getStreet() != null &&  viewObj.getStreet().getId() == null);
		_setValuetoTextField(_portalTf, viewObj.getPortal());
	}
	
	private <G extends GeoLocationBase>  void _setValuetoTextField(final TextField tf, final G geo) {
		if (tf.getParent() != null && geo != null) {
			tf.setValue(geo.getNameByLanguage() != null && 
								   geo.getNameIn(_language) != null 
																? geo.getNameIn(_language) 
																: geo.getOfficialName());
		}

	}
	private void _loadDefaultCountryAndState() {
		ListDataProvider<GeoCountry> listDataProviderCountry = (ListDataProvider<GeoCountry>)_countryCmb.getDataProvider();
		Collection<GeoCountry> countries = listDataProviderCountry.getItems();
		GeoCountry spain = countries.stream()
				 				    .filter(country -> country.getId().equals(NORAGeoIDs.SPAIN))
				 				    .findFirst()
				 				    .orElse(null);
		if (spain != null) {
			if (_countryCmb.getValue() == null && _countryCmb.getValue().getId() != null) {
				_countryCmb.setValue(spain);
			}
			if (_countryCmb.getValue().getId().equals(spain.getId())) {
				_zoom_level = VaadinNORAContactConstants.COUNTRY_ZOOM;
				_coords.setValue(VaadinNORAContactConstants.SPAIN_COORDS);
			}
		}
		ListDataProvider<GeoState> listDataProvider = (ListDataProvider<GeoState>)_stateCmb.getDataProvider();
		Collection<GeoState> states = listDataProvider.getItems();
		GeoState euskadi = states.stream()
				 		 	     .filter(state -> state.getId().equals(NORAGeoIDs.EUSKADI))
				 				 .findFirst()
				 				 .orElse(null);
		if (euskadi != null) {
			if (_stateCmb.getValue() == null) {
				_stateCmb.setValue(euskadi);
			}
			if (_stateCmb.getValue().getId().equals(euskadi.getId())) {
				_zoom_level = VaadinNORAContactConstants.STATE_ZOOM;
				_coords.setValue(VaadinNORAContactConstants.BASQUE_COUNTRY_COORDS);
			}
		}
	}

	@Override
	public void writeAsDraftEditedViewObjectTo(final VaadinViewGeoPosition viewObj) {
		_vaadinUIBinder.writeBeanAsDraft(viewObj);
		if (_countryCmb.getValue() != null && !_countryCmb.getValue().getId().equals(NORAGeoIDs.SPAIN)) {
			GeoState state = Strings.isNOTNullOrEmpty(_stateTf.getValue()) 
										? GeoState.create()
												   .withNameForDefault(_stateTf.getValue())
										: null;
			viewObj.setState(state);
			GeoCounty county = Strings.isNOTNullOrEmpty(_countyTf.getValue()) 
										? GeoCounty.create()
												   .withNameForDefault(_countyTf.getValue())
										: null;
			viewObj.setCounty(county);
			GeoMunicipality municipality = Strings.isNOTNullOrEmpty(_municipalityTf.getValue()) 
										? GeoMunicipality.create()
												   .withNameForDefault(_municipalityTf.getValue())
										: null;
			viewObj.setMunicipality(municipality);
			GeoLocality locality = Strings.isNOTNullOrEmpty(_localityTf.getValue()) 
										? GeoLocality.create()
												   .withNameForDefault(_localityTf.getValue())
										: null;
			viewObj.setLocality(locality);
		}
		if (_streetTf.getParent() != null) {
			GeoStreet street = Strings.isNOTNullOrEmpty(_streetTf.getValue())
										? GeoStreet.create()
												   .withNameForDefault(_streetTf.getValue())
										: null;
			viewObj.setStreet(street);
			GeoPortal portal = Strings.isNOTNullOrEmpty(_portalTf.getValue())
										? GeoPortal.create()
												   .withNameForDefault(_portalTf.getValue())
										: null;
			viewObj.setPortal(portal);
		}

	}
	@Override
	public boolean writeIfValidEditedViewObjectTo(final VaadinViewGeoPosition viewObj) {
		return true;
	}

	public int getZoom_level() {
		if (_countryCmb.getValue() != null) {
			_zoom_level = VaadinNORAContactConstants.COUNTRY_ZOOM;
		}
		if (_stateCmb.getValue() != null) {
			_zoom_level = VaadinNORAContactConstants.STATE_ZOOM;
		}
		if (_municipalityCmb.getValue() != null) {
			_zoom_level = VaadinNORAContactConstants.MUNICIPALITY_ZOOM;
		}
		if (_portalCmb.getValue() != null) {
			_zoom_level = VaadinNORAContactConstants.PORTAL_ZOOM;
		}

		return _zoom_level;
	}
	
	@Override
	public void setEnabled(final boolean enabled) {
		super.setEnabled(enabled);
		if(!enabled) {
			_clear(_countryCmb, null);
			_clear(_stateCmb, _stateTf);
			_clear(_countyCmb, _countyTf);
			_clear(_municipalityCmb, _municipalityTf);
			_clear(_localityCmb, _localityTf);
			_clear(_streetCmb, _streetTf);
			_clear(_portalCmb, _portalTf);
		} else {
			_countryCmb.setEnabled(enabled);
			_loadCountryCmb();
		}
		_coords.setEnabled(enabled);
		
	}
////////////////////////////////////////////////////////////////////////////////////////////
//	BEHAVIOR
/////////////////////////////////////////////////////////////////////////////////////////
	private void _setBehavior() {
		_countryCmb.setItemCaptionGenerator(item -> item.getNameByLanguage() != null && item.getNameIn(_language) != null ? item.getNameIn(_language) : item.getOfficialName());
		_stateCmb.setItemCaptionGenerator(item -> item.getNameByLanguage() != null && item.getNameIn(_language) != null ? item.getNameIn(_language) : item.getOfficialName());
		_countyCmb.setItemCaptionGenerator(item -> item.getNameByLanguage() != null && item.getNameIn(_language) != null ? item.getNameIn(_language) : item.getOfficialName());
		_municipalityCmb.setItemCaptionGenerator(item -> item.getNameByLanguage() != null && item.getNameIn(_language) != null ? item.getNameIn(_language) : item.getOfficialName());
		_localityCmb.setItemCaptionGenerator(item -> item.getNameByLanguage() != null && item.getNameIn(_language) != null ? item.getNameIn(_language) : item.getOfficialName());
		_streetCmb.setItemCaptionGenerator(item -> item.getNameByLanguage() != null && item.getNameIn(_language) != null ? item.getNameIn(_language) : item.getOfficialName());
		_portalCmb.setItemCaptionGenerator(item -> item.getNameByLanguage() != null && item.getNameIn(_language) != null ? item.getNameIn(_language) : item.getOfficialName());


		_countryCmb.addValueChangeListener(event -> _loadStateCmb());
		_stateCmb.addValueChangeListener(event -> _loadCountyCmb());
		_countyCmb.addValueChangeListener(event -> _loadMunicipalityCmb());
		_municipalityCmb.addValueChangeListener(event -> _loadLocalityCmb());
		_localityCmb.addValueChangeListener(event -> _loadStreetCmb());
		_streetCmb.addValueChangeListener(event -> _loadPortalCmb());
		_portalCmb.addValueChangeListener(event -> {
														_loadCoords2D(_portalCmb.getValue() != null ? _portalCmb.getValue().getId() : null);
														_zoom_level = VaadinNORAContactConstants.PORTAL_ZOOM;
													});

		_searchByZipCodeBtn.addClickListener(event -> {
															_clear(_stateCmb, null);
															_clear(_countyCmb, _countyTf);
															_clear(_municipalityCmb, _municipalityTf);
															_clear(_localityCmb, _localityTf);
															_clear(_streetCmb, _streetTf);
															_clear(_portalCmb, _portalTf);
															_presenter.onSearchByZipRequested(_zipCodeTf.getValue(), 
																						  _language, 
																						  UIPresenterSubscriber.from(
																														//onsuccess
																														noraContactViewObject ->  {
																																					_countryCmb.setValue(noraContactViewObject.getCountry());
																																					_stateCmb.setValue(noraContactViewObject.getState());
																																					_countyCmb.setValue(noraContactViewObject.getCounty());
																																					_municipalityCmb.setValue(noraContactViewObject.getMunicipality());
																																					_localityCmb.setValue(noraContactViewObject.getLocality());
																														},
																														// on error
																									  					th -> {
																									  						Notification.show("Error al buscar por codigo postal "+th.getMessage(),
																									  								 		   Type.ERROR_MESSAGE);
																						}));
											});
		_coords.getSearchByGeoPosition2DBtn().addClickListener(event -> {
																			_clear(_stateCmb, null);
																			_clear(_countyCmb, _countyTf);
																			_clear(_municipalityCmb, _municipalityTf);
																			_clear(_localityCmb, _localityTf);
																			_clear(_streetCmb, _streetTf);
																			_clear(_portalCmb, _portalTf);
																			GeoPosition2D geoPosition2D = _coords.getValue();
																			if (geoPosition2D != null) {
																				_presenter.onSearchByGeoPosition2D(geoPosition2D,
																												   _language,
																												   UIPresenterSubscriber.from(
																																				//onsuccess
																																				noraContactViewObject ->  {
																																											_countryCmb.setValue(noraContactViewObject.getCountry());
																																											_stateCmb.setValue(noraContactViewObject.getState());
																																											_countyCmb.setValue(noraContactViewObject.getCounty());
																																											_municipalityCmb.setValue(noraContactViewObject.getMunicipality());
																																											_localityCmb.setValue(noraContactViewObject.getLocality());
																																				},
																																				// on error
																															  					th -> {
																															  						Notification.show("Error al buscar por coordenadas "+th.getMessage(),
																															  								 		   Type.ERROR_MESSAGE);
																								  					}));
																			}
														});
		_toggleStreetBtn.addClickListener(event -> {
													_showFields(_streetTf, _streetCmb, _toggleStreetBtn.getValue());
													_showFields(_portalTf, _portalCmb, _toggleStreetBtn.getValue());
													
										});
		_loadCountryCmb();
	}

	private void _loadCountryCmb() {
		_presenter.onCountriesLoadRequested(
											UIPresenterSubscriber.from(
													//onsuccess
													countries -> {
																	_countryCmb.clear();
																	_countryCmb.setItems(countries);
													},
													// on error
								  					th -> {
								  						Notification.show("Error al cargar paises "+th.getMessage(),
								  								 		   Type.ERROR_MESSAGE);
								  					})
		);
		_clear(_stateCmb, _stateTf);
		_clear(_countyCmb, _countyTf);
		_clear(_municipalityCmb, _municipalityTf);
		_clear(_localityCmb, _localityTf);
		_clear(_streetCmb, _streetTf);
		_clear(_portalCmb, _portalTf);
	}
	private void _loadStateCmb() {
		if (_countryCmb.getValue() ==  null || _countryCmb.getValue().getId() == null) {
			_clear(_stateCmb, _stateTf);
			_zoom_level = VaadinNORAContactConstants.COUNTRY_ZOOM; 
			return;
		}
		if (_countryCmb.getValue().getId().equals(NORAGeoIDs.SPAIN)) {
			_loadCoords2D(_countryCmb.getValue().getId());
			_presenter.onStatesLoadRequested(_countryCmb.getValue().getId(),
											 UIPresenterSubscriber.from(
													 //onsuccess
													 states ->  {
														 			_stateCmb.clear();
														 			_stateCmb.setItems(states);
														 			_stateCmb.setEnabled(true);
													 },
													 // on error
								  					 th -> {
								  						 Notification.show("Error al cargar comunidades autonomas "+th.getMessage(),
								  								 		   Type.ERROR_MESSAGE);
								  					 })
			);
			_clear(_countyCmb, _countyTf);
			_clear(_municipalityCmb, _municipalityTf);
			_clear(_localityCmb, _localityTf);
			_clear(_streetCmb, _streetTf);
			_clear(_portalCmb, _portalTf);
		} 
		_showFields(_stateTf, _stateCmb, !_countryCmb.getValue().getId().equals(NORAGeoIDs.SPAIN));
		_showFields(_countyTf, _countyCmb, !_countryCmb.getValue().getId().equals(NORAGeoIDs.SPAIN));
		_showFields(_countyTf, _countyCmb, !_countryCmb.getValue().getId().equals(NORAGeoIDs.SPAIN));
		_showFields(_municipalityTf, _municipalityCmb, !_countryCmb.getValue().getId().equals(NORAGeoIDs.SPAIN));
		_showFields(_localityTf, _localityCmb, !_countryCmb.getValue().getId().equals(NORAGeoIDs.SPAIN));
		_showFields(_streetTf, _streetCmb, (!_countryCmb.getValue().getId().equals(NORAGeoIDs.SPAIN) ||  _toggleStreetBtn.getValue()));
		_showFields(_portalTf, _portalCmb, (!_countryCmb.getValue().getId().equals(NORAGeoIDs.SPAIN) ||  _toggleStreetBtn.getValue()));
		
	}
	
	private static void _showFields(final TextField tf, final ComboBox cmb, final boolean showTf) {
		if (showTf && cmb.getParent() != null) {
			((HorizontalLayout)cmb.getParent()).replaceComponent(cmb, tf);
		} else if (!showTf && tf.getParent() != null) {
			((HorizontalLayout)tf.getParent()).replaceComponent(tf, cmb);
		}
		
	}
	private void _loadCountyCmb() {
		if (_stateCmb.getValue() ==  null || _stateCmb.getValue().getId() == null) {
			_clear(_countyCmb, _countyTf);
			return;
		}
		_loadCoords2D(_stateCmb.getValue().getId());
		_presenter.onCountiesLoadRequested(_stateCmb.getValue().getId(),
										   UIPresenterSubscriber.from(
												 //onsuccess
												 counties -> {
													 			_countyCmb.clear();
													 			_countyCmb.setItems(counties);
													 			_countyCmb.setEnabled(true);
												 },
												 // on error
							  					 th -> {
							  						 Notification.show("Error al cargar provincias "+th.getMessage(),
							  								 		   Type.ERROR_MESSAGE);
							  					 })
		);
		_clear(_municipalityCmb, _municipalityTf);
		_clear(_localityCmb, _localityTf);
		_clear(_streetCmb, _streetTf);
		_clear(_portalCmb, _portalTf);
		
		_showFields(_streetTf, _streetCmb, (!_stateCmb.getValue().getId().equals(NORAGeoIDs.EUSKADI) ||  _toggleStreetBtn.getValue()));
		_showFields(_portalTf, _portalCmb, (!_stateCmb.getValue().getId().equals(NORAGeoIDs.EUSKADI) ||  _toggleStreetBtn.getValue()));
	}

	private void _loadMunicipalityCmb() {
		if (_countyCmb.getValue() ==  null || _countyCmb.getValue().getId() == null) {
			_clear(_municipalityCmb, _municipalityTf);
			_zoom_level = VaadinNORAContactConstants.STATE_ZOOM;
			return;
		}
		_loadCoords2D(_countyCmb.getValue().getId());
		_presenter.onMunicipalitiesLoadRequested(_stateCmb.getValue().getId(),
									             _countyCmb.getValue().getId(),
									             UIPresenterSubscriber.from(
									        		   //onsuccess
									        		   muns ->  {
									        			   		_municipalityCmb.clear();
													 			_municipalityCmb.setItems(muns);
													 			_municipalityCmb.setEnabled(true);
									        		   },
									        		   // on error
									        		   th -> {
									        			   Notification.show("Error al cargar municipios "+th.getMessage(),
							  								 		   	     Type.ERROR_MESSAGE);
									        		   })
		);
		_clear(_localityCmb, _localityTf);
		_clear(_streetCmb, _streetTf);
		_clear(_portalCmb, _portalTf);
	}

	private void _loadLocalityCmb() {
		if (_municipalityCmb.getValue() ==  null || _municipalityCmb.getValue().getId() == null) {
			_clear(_localityCmb, _localityTf);
			_zoom_level = VaadinNORAContactConstants.STATE_ZOOM;
			return;
		}
		_zoom_level = VaadinNORAContactConstants.MUNICIPALITY_ZOOM;
		_loadCoords2D(_municipalityCmb.getValue().getId());
		_presenter.onLocalitiesLoadRequested(_stateCmb.getValue().getId(),
									         _countyCmb.getValue().getId(),
									         _municipalityCmb.getValue().getId(),
								             UIPresenterSubscriber.from(
								        		   //onsuccess
								        		   locs ->  {
								        			   			_localityCmb.clear();
								        			   			_localityCmb.setItems(locs);
								        			   			_localityCmb.setEnabled(true);
								        		   },
								        		   // on error
								        		   th -> {
								        			   Notification.show("Error al cargar localidades "+th.getMessage(),
						  								 		   	     Type.ERROR_MESSAGE);
								        		   })
		);
		_clear(_streetCmb, _streetTf);
		_clear(_portalCmb, _portalTf);
		_streetCmb.setEnabled(true);
	}

	private void _loadStreetCmb() {
		if (_localityCmb.getValue() ==  null || _localityCmb.getValue().getId() == null) {
			_clear(_streetCmb, _streetTf);
			_zoom_level = VaadinNORAContactConstants.STATE_ZOOM;
			return;
		}
		_zoom_level = VaadinNORAContactConstants.MUNICIPALITY_ZOOM;
		_loadCoords2D(_localityCmb.getValue().getId());
		_clear(_portalCmb, _streetTf);
	}
	private void _loadPortalCmb() {
		if (_streetCmb.getValue() ==  null || _streetCmb.getValue().getId() == null) {
			_zoom_level = VaadinNORAContactConstants.MUNICIPALITY_ZOOM;
			_clear(_portalCmb, _streetTf);
			return;
		}
		_zoom_level = VaadinNORAContactConstants.PORTAL_ZOOM;
		_loadCoords2D(_streetCmb.getValue().getId());
		_presenter.onPortalLoadRequested(_stateCmb.getValue().getId(),
									     _countyCmb.getValue().getId(),
									     _municipalityCmb.getValue().getId(),
									     _localityCmb.getValue().getId(),
									     _streetCmb.getValue().getId(),
							             UIPresenterSubscriber.from(
							        		   //onsuccess
							        		   portals ->  {
							        			   				_portalCmb.clear();
											 					_portalCmb.setItems(portals);
											 					_portalCmb.setEnabled(true);
							        		   },
							        		   // on error
							        		   th -> {
							        			   Notification.show("Error al cargar portales "+th.getMessage(),
					  								 		   	     Type.ERROR_MESSAGE);
							        		   })
		);
	}

	private <OID extends GeoIDBase>void _loadCoords2D(final OID oid) {
		GeoCountyID countyId = null;
		if (oid instanceof GeoRegionID || oid instanceof GeoMunicipalityID) {
			countyId = _countyCmb.getValue().getId();
		}
		_presenter.onSearchPosition2D(oid,
									  countyId,
									  UIPresenterSubscriber.from(
							        		   //onsuccess
							        		   geoPosition2D -> {
							        			   					if (geoPosition2D != null) {
							        			   						_coords.setValue(geoPosition2D);
							        			   					}

							        		   }
							        		   ,
							        		   // on error
							        		   th -> {
							        			   Notification.show("Error al cargar coordenadas para el oid de tipo: "+ oid.getClass().getName()+ " "+ th.getMessage(),
					  								 		   	     Type.ERROR_MESSAGE);
							        		   })
		);
	}

	
	private static void _clear(final ComboBox<?> cmb, final TextField tf) {
		cmb.clear();
		if (tf != null) {
			tf.clear();
		}
		cmb.setEnabled(false);
	}
}
