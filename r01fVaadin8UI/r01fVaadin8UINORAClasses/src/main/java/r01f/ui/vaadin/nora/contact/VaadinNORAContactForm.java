package r01f.ui.vaadin.nora.contact;

import java.util.ArrayList;
import java.util.Collection;

import com.vaadin.data.Binder;
import com.vaadin.data.provider.CallbackDataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.ejie.nora.NORAGeoIDs;
import r01f.facets.HasLanguage;
import r01f.locale.Language;
import r01f.types.geo.GeoCountry;
import r01f.types.geo.GeoCounty;
import r01f.types.geo.GeoLocality;
import r01f.types.geo.GeoMunicipality;
import r01f.types.geo.GeoOIDs.GeoCountyID;
import r01f.types.geo.GeoOIDs.GeoIDBase;
import r01f.types.geo.GeoOIDs.GeoMunicipalityID;
import r01f.types.geo.GeoOIDs.GeoRegionID;
import r01f.types.geo.GeoPortal;
import r01f.types.geo.GeoPosition2D;
import r01f.types.geo.GeoPosition2D.GeoPositionStandard;
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
import r01ui.base.components.form.VaadinFormEditsViewObject;

@Accessors(prefix="_")
public class VaadinNORAContactForm 
     extends GridLayout
  implements VaadinView,
  			 VaadinFormEditsViewObject<VaadinNORAContactViewObject>,
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
	@VaadinViewField(bindToViewObjectFieldNamed=VaadinNORAContactViewObject.COUNTRY_FIELD,
					 bindStringConverter=false,required=false)
	@LangIndependentVaadinViewField
	@VaadinViewComponentLabels(captionI18NKey="geo.country",
							   useCaptionI18NKeyAsPlaceHolderKey=true)
	@Getter @Setter private ComboBox<GeoCountry> _countryCmb = new ComboBox<>();
	
	@VaadinViewField(bindToViewObjectFieldNamed=VaadinNORAContactViewObject.STATE_FIELD,
					 bindStringConverter=false,required=false)
	@LangIndependentVaadinViewField
	@VaadinViewComponentLabels(captionI18NKey="geo.state",
							   useCaptionI18NKeyAsPlaceHolderKey=true)
	@Getter @Setter private ComboBox<GeoState> _stateCmb = new ComboBox<>();
		
	@VaadinViewField(bindToViewObjectFieldNamed=VaadinNORAContactViewObject.COUNTY_FIELD,
					 bindStringConverter=false,required=false)
	@LangIndependentVaadinViewField
	@VaadinViewComponentLabels(captionI18NKey="geo.county",
							   useCaptionI18NKeyAsPlaceHolderKey=true)
	@Getter @Setter private ComboBox<GeoCounty> _countyCmb = new ComboBox<>();

	
	@VaadinViewField(bindToViewObjectFieldNamed=VaadinNORAContactViewObject.MUNICIPALITY_FIELD,
					 bindStringConverter=false,required=false)
	@LangIndependentVaadinViewField
	@VaadinViewComponentLabels(captionI18NKey="geo.municipality",
							   useCaptionI18NKeyAsPlaceHolderKey=true)
	@Getter @Setter private ComboBox<GeoMunicipality> _municipalityCmb = new ComboBox<>();
	
	@VaadinViewField(bindToViewObjectFieldNamed=VaadinNORAContactViewObject.LOCALITY_FIELD,
					 bindStringConverter=false,required=false)
	@LangIndependentVaadinViewField
	@VaadinViewComponentLabels(captionI18NKey="geo.locality",
							   useCaptionI18NKeyAsPlaceHolderKey=true)
	@Getter @Setter private ComboBox<GeoLocality> _localityCmb = new ComboBox<>();
	
	@VaadinViewField(bindToViewObjectFieldNamed=VaadinNORAContactViewObject.STREET_FIELD,
					 bindStringConverter=false,required=false)
	@LangIndependentVaadinViewField
	@VaadinViewComponentLabels(captionI18NKey="geo.street",
							   useCaptionI18NKeyAsPlaceHolderKey=true)
	@Getter @Setter private ComboBox<GeoStreet> _streetCmb = new ComboBox<>();
	
	@VaadinViewField(bindToViewObjectFieldNamed=VaadinNORAContactViewObject.PORTAL_FIELD,
					 bindStringConverter=false,required=false)
	@LangIndependentVaadinViewField
	@VaadinViewComponentLabels(captionI18NKey="geo.portal",
							   useCaptionI18NKeyAsPlaceHolderKey=true)
	@Getter @Setter private ComboBox<GeoPortal> _portalCmb = new ComboBox<GeoPortal>();
	
	@VaadinViewComponentLabels(captionI18NKey="geo.zip",
							   useCaptionI18NKeyAsPlaceHolderKey=true)
	@Getter @Setter private TextField _zipCodeTf = new TextField();
	
	@VaadinViewComponentLabels(captionI18NKey="geo.coords",
							   useCaptionI18NKeyAsPlaceHolderKey=true)
	@Getter @Setter private TextField _coords2D = new TextField();
	
	private final Button _searchByZipCodeBtn = new Button();
	private final Button _searchByGeoPosition2DBtn = new Button();
	
	
	private final Button _save = new Button("Guardar");
	// Binder
	@Getter private final Binder<VaadinNORAContactViewObject> _vaadinUIBinder = new Binder<>(VaadinNORAContactViewObject.class);
	private VaadinNORAContactViewObject _viewObject;
	
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
		_searchByZipCodeBtn.setCaption(_i18n.getMessage("geo.search"));
		_searchByGeoPosition2DBtn.setCaption(_i18n.getMessage("geo.search"));
		_save.setCaption(_i18n.getMessage("geo.save"));
		
		////////// Layout & style
		this.setColumns(2);
		this.setRows(7);
		//this.setColumnExpandRatio(1, 1);
		this.setSizeFull();
		this.setSpacing(true);
		this.setMargin(false);
		_countryCmb.setWidth(100, Unit.PERCENTAGE);
		_stateCmb.setWidth(100, Unit.PERCENTAGE);
		_countyCmb.setWidth(100, Unit.PERCENTAGE);
		_municipalityCmb.setWidth(100, Unit.PERCENTAGE);
		_localityCmb.setWidth(100, Unit.PERCENTAGE);
		_streetCmb.setWidth(100, Unit.PERCENTAGE);
		_streetCmb.addStyleName(VaadinValoTheme.COMBO_SUGGESTION);
		_coords2D.setWidth(100, Unit.PERCENTAGE);
		_searchByZipCodeBtn.setWidth(90, Unit.PIXELS);
		_searchByGeoPosition2DBtn.setWidth(90, Unit.PIXELS);
		
		HorizontalLayout hl = new HorizontalLayout(_zipCodeTf,_searchByZipCodeBtn);
		hl.setSpacing(false);
		hl.setWidthFull();
		hl.setComponentAlignment(_searchByZipCodeBtn, Alignment.BOTTOM_LEFT);
		this.addComponent(hl, 0, 0, 1, 0);
		
		this.addComponent(_countryCmb, 0, 1);
		this.addComponent(_stateCmb, 1, 1);
		this.addComponent(_countyCmb, 0, 2);
		this.addComponent(_municipalityCmb, 1, 2);
		this.addComponent(_localityCmb, 0, 3);
		this.addComponent(_streetCmb, 1, 3);
		this.addComponent(_portalCmb, 0, 4);
		HorizontalLayout hlCoords = new HorizontalLayout(_coords2D,_searchByGeoPosition2DBtn);
		hlCoords.setSpacing(false);
		hlCoords.setWidthFull();
		hlCoords.setComponentAlignment(_searchByGeoPosition2DBtn, Alignment.BOTTOM_LEFT);
		this.addComponent(hlCoords, 0, 5, 1, 5);
		this.addComponent(_save, 0, 6, 1, 6);
		this.setComponentAlignment(_save, Alignment.BOTTOM_RIGHT);
		
		
		CallbackDataProvider<GeoStreet,String> dataProvider = null;
		dataProvider = new CallbackDataProvider<>(// query
									query -> {
												String text = query.getFilter().orElse(null);
												if(_stateCmb.getValue() != null && _countyCmb.getValue() != null &&  _municipalityCmb.getValue() != null && _localityCmb.getValue() != null 
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
												if(_stateCmb.getValue() != null && _countyCmb.getValue() != null &&  _municipalityCmb.getValue() != null && _localityCmb.getValue() != null 
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
		_viewObject = new VaadinNORAContactViewObject(_language);
		////////// Bind: automatic binding using using @VaadinViewField annotation of view fields
		VaadinViews.using(_vaadinUIBinder,_i18n)
					.bindComponentsOf(this)
					.toViewObjectOfType(VaadinNORAContactViewObject.class);
		_setBehavior();
	}
////////////////////////////////////////////////////////////////////////////////////////////
//	FORM
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void editViewObject(VaadinNORAContactViewObject viewObj) {
		_viewObject = viewObj;
		_vaadinUIBinder.readBean(viewObj);
		_loadDefaultCountryAndState();
	}
	private void _loadDefaultCountryAndState() {
		if(_countryCmb.getValue() == null) {
			ListDataProvider<GeoCountry> listDataProvider = (ListDataProvider<GeoCountry>)_countryCmb.getDataProvider();
			Collection<GeoCountry> countries = listDataProvider.getItems();
			GeoCountry spain = countries.stream()
					 				    .filter(country -> country.getId().equals(NORAGeoIDs.SPAIN))
					 				    .findFirst()
					 				    .orElse(null);
			if(spain != null) {
				_countryCmb.setValue(spain);
				_zoom_level = VaadinNORAContactConstants.COUNTRY_ZOOM;
				_coords2D.setValue(VaadinNORAContactConstants.SPAIN_COORDS.getX() +" , " + VaadinNORAContactConstants.SPAIN_COORDS.getY());
				
			}
		}
		if(_stateCmb.getValue() == null) {
			ListDataProvider<GeoState> listDataProvider = (ListDataProvider<GeoState>)_stateCmb.getDataProvider();
			Collection<GeoState> states = listDataProvider.getItems();
			GeoState euskadi = states.stream()
					 		 	     .filter(country -> country.getId().equals(NORAGeoIDs.EUSKADI))
					 				 .findFirst()
					 				 .orElse(null);
			if(euskadi != null ) {
				_stateCmb.setValue(euskadi);
				_zoom_level = VaadinNORAContactConstants.STATE_ZOOM;
				_coords2D.setValue(VaadinNORAContactConstants.BASQUE_COUNTRY_COORDS.getX() +" , " + VaadinNORAContactConstants.BASQUE_COUNTRY_COORDS.getY());
			}
		}
	}
	
	@Override
	public void writeAsDraftEditedViewObjectTo(VaadinNORAContactViewObject viewObj) {
		_vaadinUIBinder.writeBeanAsDraft(viewObj);
		
	}
	@Override
	public boolean writeIfValidEditedViewObjectTo(VaadinNORAContactViewObject viewObj) {
		return false;
	}
	
	public int getZoom_level() {
		if(_countryCmb.getValue() != null) {
			_zoom_level = VaadinNORAContactConstants.COUNTRY_ZOOM;
		}
		if(_stateCmb.getValue() != null) {
			_zoom_level = VaadinNORAContactConstants.STATE_ZOOM;
		}
		if(_municipalityCmb.getValue() != null) {
			_zoom_level = VaadinNORAContactConstants.MUNICIPALITY_ZOOM;
		}
		if(_portalCmb.getValue() != null) {
			_zoom_level = VaadinNORAContactConstants.PORTAL_ZOOM;
		}
		
		return _zoom_level;
	}
////////////////////////////////////////////////////////////////////////////////////////////
//	BEHAVIOR
/////////////////////////////////////////////////////////////////////////////////////////
	private void _setBehavior() {
		_countryCmb.setItemCaptionGenerator(item -> item.getNameIn(_language) != null ? item.getNameIn(_language) : item.getOfficialName());
		_stateCmb.setItemCaptionGenerator(item -> item.getNameIn(_language) != null ? item.getNameIn(_language) : item.getOfficialName());
		_countyCmb.setItemCaptionGenerator(item -> item.getNameIn(_language) != null ? item.getNameIn(_language) : item.getOfficialName());
		_municipalityCmb.setItemCaptionGenerator(item -> item.getNameIn(_language) != null ? item.getNameIn(_language) : item.getOfficialName());
		_localityCmb.setItemCaptionGenerator(item -> item.getNameIn(_language) != null ? item.getNameIn(_language) : item.getOfficialName());
		_streetCmb.setItemCaptionGenerator(item -> item.getNameIn(_language) != null ? item.getNameIn(_language) : item.getOfficialName());
		_portalCmb.setItemCaptionGenerator(item -> item.getNameIn(_language) != null ? item.getNameIn(_language) : item.getOfficialName());
		
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
																									  						Notification.show(th.getMessage(),
																									  								 		   Type.ERROR_MESSAGE);
																						}));
											});
		_searchByGeoPosition2DBtn.addClickListener(event -> {
																String valueStr = _coords2D.getValue();
																if(Strings.isNOTNullOrEmpty(valueStr) && valueStr.indexOf(",") != -1) {
																	String[] value = valueStr.split(",");
																	GeoPosition2D geoPosition2D = new GeoPosition2D(GeoPositionStandard.ETRS89, Double.parseDouble(value[0].trim()), Double.parseDouble(value[1].trim()));
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
																												  						Notification.show(th.getMessage(),
																												  								 		   Type.ERROR_MESSAGE);
																					  					}));
																}
											});
		_loadCountryCmb();
		_save.addClickListener(event -> writeAsDraftEditedViewObjectTo(_viewObject));
		_coords2D.addValueChangeListener(event -> {
													String valueStr = event.getValue();
													if(Strings.isNOTNullOrEmpty(valueStr) && valueStr.indexOf(",") != -1) {
														String[] value = valueStr.split(",");
														GeoPosition2D geoPosition2D = new GeoPosition2D(GeoPositionStandard.ETRS89, Double.parseDouble(value[0].trim()), Double.parseDouble(value[1].trim()));
														_presenter.onTransformGeoPositionFromETRS89toED50(geoPosition2D,
																										 UIPresenterSubscriber.from(
																										 //onsuccess
																										 outGeoPosition2D ->  {
																											//TODO
																											 System.out.println(outGeoPosition2D.getStandard());
																										 },
																										 // on error
																					  					 th -> {
																					  						 Notification.show(th.getMessage(),
																					  								 		   Type.ERROR_MESSAGE);
																					  					 }));
													}
										});
	}
	
	private void _loadCountryCmb() {
		_presenter.onCountriesLoadRequested(
											UIPresenterSubscriber.from(
													//onsuccess
													countries ->  _countryCmb.setItems(countries),
													// on error
								  					th -> {
								  						Notification.show(th.getMessage(),
								  								 		   Type.ERROR_MESSAGE);
								  					})
		);
		_clear(_stateCmb);
		_clear(_countyCmb);
		_clear(_municipalityCmb);
		_clear(_localityCmb);
		_clear(_streetCmb);
		_clear(_portalCmb);
	}
	private void _loadStateCmb() {
		if (_countryCmb.getValue() ==  null) {
			_clear(_stateCmb);
			_zoom_level = VaadinNORAContactConstants.COUNTRY_ZOOM; 
			return;
		}
		_loadCoords2D(_countryCmb.getValue().getId());
		_presenter.onStatesLoadRequested(_countryCmb.getValue().getId(),
										 UIPresenterSubscriber.from(
												 //onsuccess
												 states ->  {
													 			_stateCmb.setItems(states);
													 			_stateCmb.setEnabled(true);
												 },
												 // on error
							  					 th -> {
							  						 Notification.show(th.getMessage(),
							  								 		   Type.ERROR_MESSAGE);
							  					 })
		);
		_clear(_countyCmb);
		_clear(_municipalityCmb);
		_clear(_localityCmb);
		_clear(_streetCmb);
		_clear(_portalCmb);
	}
	private void _loadCountyCmb() {
		if (_stateCmb.getValue() ==  null) {
			_clear(_countyCmb);
			return;
		}
		_loadCoords2D(_stateCmb.getValue().getId());
		_presenter.onCountiesLoadRequested(_stateCmb.getValue().getId(),
										   UIPresenterSubscriber.from(
												 //onsuccess
												 counties -> { 
													 			_countyCmb.setItems(counties);
													 			_countyCmb.setEnabled(true);
												 },
												 // on error
							  					 th -> {
							  						 Notification.show(th.getMessage(),
							  								 		   Type.ERROR_MESSAGE);
							  					 })
		);
		_clear(_municipalityCmb);
		_clear(_localityCmb);
		_clear(_streetCmb);
		_clear(_portalCmb);
	}
	
	private void _loadMunicipalityCmb() {
		if (_countyCmb.getValue() ==  null) {
			_clear(_municipalityCmb);
			_zoom_level = VaadinNORAContactConstants.STATE_ZOOM;
			return;
		}
		_loadCoords2D(_countyCmb.getValue().getId());
		_presenter.onMunicipalitiesLoadRequested(_stateCmb.getValue().getId(),
									             _countyCmb.getValue().getId(),
									             UIPresenterSubscriber.from(
									        		   //onsuccess
									        		   muns ->  { 
													 			_municipalityCmb.setItems(muns);
													 			_municipalityCmb.setEnabled(true);
									        		   },
									        		   // on error
									        		   th -> {
									        			   Notification.show(th.getMessage(),
							  								 		   	     Type.ERROR_MESSAGE);
									        		   })
		);
		_clear(_localityCmb);
		_clear(_streetCmb);
		_clear(_portalCmb);
	}
	
	private void _loadLocalityCmb() {
		if (_municipalityCmb.getValue() ==  null) {
			_clear(_localityCmb);
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
												 			_localityCmb.setItems(locs);
												 			_localityCmb.setEnabled(true);
								        		   },
								        		   // on error
								        		   th -> {
								        			   Notification.show(th.getMessage(),
						  								 		   	     Type.ERROR_MESSAGE);
								        		   })
		);
		_clear(_streetCmb);
		_clear(_portalCmb);
		_streetCmb.setEnabled(true);
	}
	
	private void _loadStreetCmb() {
		if (_localityCmb.getValue() ==  null) {
			_clear(_streetCmb);
			_zoom_level = VaadinNORAContactConstants.STATE_ZOOM;
			return;
		}
		_zoom_level = VaadinNORAContactConstants.MUNICIPALITY_ZOOM;
		_loadCoords2D(_localityCmb.getValue().getId());
		_clear(_portalCmb);
	}
	private void _loadPortalCmb() {
		if (_streetCmb.getValue() ==  null) {
			_zoom_level = VaadinNORAContactConstants.MUNICIPALITY_ZOOM;
			_clear(_portalCmb);
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
											 			_portalCmb.setItems(portals);
											 			_portalCmb.setEnabled(true);
							        		   },
							        		   // on error
							        		   th -> {
							        			   Notification.show(th.getMessage(),
					  								 		   	     Type.ERROR_MESSAGE);
							        		   })
		);
	}
	
	private <OID extends GeoIDBase>void _loadCoords2D(final OID oid) {
		GeoCountyID countyId = null;
		if(oid instanceof GeoRegionID || oid instanceof GeoMunicipalityID) {
			countyId = _countyCmb.getValue().getId();
		}
		_presenter.onSearchPosition2D(oid,
									  countyId, 
									  UIPresenterSubscriber.from(
							        		   //onsuccess
							        		   geoPosition2D -> {
							        			   					if(geoPosition2D != null) {
							        			   						_coords2D.setValue(geoPosition2D.getX() +" , " + geoPosition2D.getY());
							        			   					}
							        			   				
							        		   }
							        		   ,
							        		   // on error
							        		   th -> {
							        			   Notification.show(th.getMessage(),
					  								 		   	     Type.ERROR_MESSAGE);
							        		   })
		);
	}

	
	private static void _clear(final ComboBox<?> cmb) {
		cmb.clear();
		cmb.setEnabled(false);
	}
}
