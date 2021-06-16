package r01f.ui.vaadin.nora.contact;

import java.util.ArrayList;

import com.vaadin.data.Binder;
import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.data.provider.CallbackDataProvider;
import com.vaadin.shared.Registration;
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
import r01f.facets.HasLanguage;
import r01f.locale.Language;
import r01f.types.geo.GeoCountry;
import r01f.types.geo.GeoCounty;
import r01f.types.geo.GeoLocality;
import r01f.types.geo.GeoMunicipality;
import r01f.types.geo.GeoPortal;
import r01f.types.geo.GeoPosition2D;
import r01f.types.geo.GeoRegion;
import r01f.types.geo.GeoState;
import r01f.types.geo.GeoStreet;
import r01f.types.geo.GeoOIDs.GeoCountyID;
import r01f.types.geo.GeoOIDs.GeoIDBase;
import r01f.types.geo.GeoOIDs.GeoLocalityID;
import r01f.types.geo.GeoOIDs.GeoMunicipalityID;
import r01f.types.geo.GeoOIDs.GeoRegionID;
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
	@VaadinViewComponentLabels(captionI18NKey="Pais",
							   useCaptionI18NKeyAsPlaceHolderKey=true)
	@Getter @Setter private ComboBox<GeoCountry> _countryCmb = new ComboBox<>();
	
	@VaadinViewField(bindToViewObjectFieldNamed=VaadinNORAContactViewObject.STATE_FIELD,
					 bindStringConverter=false,required=false)
	@LangIndependentVaadinViewField
	@VaadinViewComponentLabels(captionI18NKey="Comunidad",
							   useCaptionI18NKeyAsPlaceHolderKey=true)
	@Getter @Setter private ComboBox<GeoState> _stateCmb = new ComboBox<>();
		
	@VaadinViewField(bindToViewObjectFieldNamed=VaadinNORAContactViewObject.COUNTY_FIELD,
					 bindStringConverter=false,required=false)
	@LangIndependentVaadinViewField
	@VaadinViewComponentLabels(captionI18NKey="Provincia",
							   useCaptionI18NKeyAsPlaceHolderKey=true)
	@Getter @Setter private ComboBox<GeoCounty> _countyCmb = new ComboBox<>();

	
	@VaadinViewField(bindToViewObjectFieldNamed=VaadinNORAContactViewObject.MUNICIPALITY_FIELD,
					 bindStringConverter=false,required=false)
	@LangIndependentVaadinViewField
	@VaadinViewComponentLabels(captionI18NKey="Municipio",
							   useCaptionI18NKeyAsPlaceHolderKey=true)
	@Getter @Setter private ComboBox<GeoMunicipality> _municipalityCmb = new ComboBox<>();
	
	@VaadinViewField(bindToViewObjectFieldNamed=VaadinNORAContactViewObject.LOCALITY_FIELD,
					 bindStringConverter=false,required=false)
	@LangIndependentVaadinViewField
	@VaadinViewComponentLabels(captionI18NKey="Localidad",
							   useCaptionI18NKeyAsPlaceHolderKey=true)
	@Getter @Setter private ComboBox<GeoLocality> _localityCmb = new ComboBox<>();
	
	@VaadinViewField(bindToViewObjectFieldNamed=VaadinNORAContactViewObject.STREET_FIELD,
					 bindStringConverter=false,required=false)
	@LangIndependentVaadinViewField
	@VaadinViewComponentLabels(captionI18NKey="Calle",
							   useCaptionI18NKeyAsPlaceHolderKey=true)
	@Getter @Setter private ComboBox<GeoStreet> _streetCmb = new ComboBox<>();
	
	@VaadinViewField(bindToViewObjectFieldNamed=VaadinNORAContactViewObject.PORTAL_FIELD,
					 bindStringConverter=false,required=false)
	@LangIndependentVaadinViewField
	@VaadinViewComponentLabels(captionI18NKey="Portal",
							   useCaptionI18NKeyAsPlaceHolderKey=true)
	@Getter @Setter private ComboBox<GeoPortal> _portalCmb = new ComboBox<GeoPortal>();
	
	@VaadinViewComponentLabels(captionI18NKey="Código Postal",
							   useCaptionI18NKeyAsPlaceHolderKey=true)
	@Getter @Setter private TextField _zipCodeTf = new TextField();
	
	@VaadinViewComponentLabels(captionI18NKey="Coordenadas",
							   useCaptionI18NKeyAsPlaceHolderKey=true)
	@Getter @Setter private TextField _coords2D = new TextField();
	
	private final Button _searchByZipCodeBtn = new Button("Buscar");
	
	private final Button _save = new Button("Guardar");
	// Binder
	@Getter private final Binder<VaadinNORAContactViewObject> _vaadinUIBinder = new Binder<>(VaadinNORAContactViewObject.class);
	private VaadinNORAContactViewObject _viewObject;

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
		
		////////// Layout & style
		this.setColumns(2);
		this.setRows(6);
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
	//	_portalCmb.setWidth(100, Unit.PERCENTAGE);
		_coords2D.setWidth(100, Unit.PERCENTAGE);
		_searchByZipCodeBtn.setWidth(90, Unit.PIXELS);
		
		HorizontalLayout hl = new HorizontalLayout(_zipCodeTf,_searchByZipCodeBtn);
		hl.setSpacing(false);
		hl.setWidthFull();
		hl.setComponentAlignment(_searchByZipCodeBtn, Alignment.BOTTOM_LEFT);
	//	hl.setExpandRatio(_zipCodeTf, 1);
		this.addComponent(hl, 0, 0, 1, 0);
		
		this.addComponent(_countryCmb, 0, 1);
		this.addComponent(_stateCmb, 1, 1);
		this.addComponent(_countyCmb, 0, 2);
		this.addComponent(_municipalityCmb, 1, 2);
		this.addComponent(_localityCmb, 0, 3);
		this.addComponent(_streetCmb, 1, 3);
		this.addComponent(_portalCmb, 0, 4);
		this.addComponent(_coords2D, 1, 4);
		this.addComponent(_save, 0, 5, 1, 5);
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
		
		_loadCountryCmb();
		_countryCmb.addValueChangeListener(event -> _loadStateCmb());
		_stateCmb.addValueChangeListener(event -> _loadCountyCmb());
		_countyCmb.addValueChangeListener(event -> _loadMunicipalityCmb());
		_municipalityCmb.addValueChangeListener(event -> _loadLocalityCmb());
		_localityCmb.addValueChangeListener(event -> _loadStreetCmb());
		_streetCmb.addValueChangeListener(event -> _loadPortalCmb());
		_portalCmb.addValueChangeListener(event -> _loadCoords2D(_portalCmb.getValue() != null ? _portalCmb.getValue().getId() : null));
		
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
		_save.addClickListener(event -> writeAsDraftEditedViewObjectTo(_viewObject));
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
			return;
		}
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
			return;
		}
		_loadCoords2D(_localityCmb.getValue().getId());
		_clear(_portalCmb);
	}
	private void _loadPortalCmb() {
		if (_streetCmb.getValue() ==  null) {
			_clear(_portalCmb);
			return;
		}
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
	
////////////////////////////////////////////////////////////////////////////////////////////
//	FORM
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void editViewObject(VaadinNORAContactViewObject viewObj) {
		_viewObject = viewObj;
		_vaadinUIBinder.readBean(viewObj);
		
	}
	@Override
	public void writeAsDraftEditedViewObjectTo(VaadinNORAContactViewObject viewObj) {
		_vaadinUIBinder.writeBeanAsDraft(viewObj);
		
	}
	@Override
	public boolean writeIfValidEditedViewObjectTo(VaadinNORAContactViewObject viewObj) {
		return false;
	}
}
