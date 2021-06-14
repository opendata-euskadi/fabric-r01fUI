package r01f.ui.vaadin.nora.contact;

import java.util.ArrayList;

import com.vaadin.data.Binder;
import com.vaadin.data.provider.CallbackDataProvider;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
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
import r01f.types.geo.GeoMunicipality;
import r01f.types.geo.GeoPortal;
import r01f.types.geo.GeoRegion;
import r01f.types.geo.GeoState;
import r01f.types.geo.GeoStreet;
import r01f.ui.i18n.UII18NService;
import r01f.ui.presenter.UIPresenterSubscriber;
import r01f.ui.vaadin.annotations.LangIndependentVaadinViewField;
import r01f.ui.vaadin.annotations.VaadinViewComponentLabels;
import r01f.ui.vaadin.annotations.VaadinViewField;
import r01f.ui.vaadin.view.VaadinView;
import r01f.ui.vaadin.view.VaadinViewI18NMessagesCanBeUpdated;
import r01f.ui.vaadin.view.VaadinViews;
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
	
	@VaadinViewField(bindToViewObjectFieldNamed=VaadinNORAContactViewObject.REGION_FIELD,
					 bindStringConverter=false,required=false)
	@LangIndependentVaadinViewField
	@VaadinViewComponentLabels(captionI18NKey="Comarca",
							   useCaptionI18NKeyAsPlaceHolderKey=true)
	@Getter @Setter private ComboBox<GeoRegion> _regionCmb = new ComboBox<>();
	
	@VaadinViewField(bindToViewObjectFieldNamed=VaadinNORAContactViewObject.MUNICIPALITY_FIELD,
					 bindStringConverter=false,required=false)
	@LangIndependentVaadinViewField
	@VaadinViewComponentLabels(captionI18NKey="Municipio",
							   useCaptionI18NKeyAsPlaceHolderKey=true)
	@Getter @Setter private ComboBox<GeoMunicipality> _municipalityCmb = new ComboBox<>();
	
	
//	@VaadinViewField(bindToViewObjectFieldNamed=VaadinNORAContactViewObject.DISTRICT_FIELD,
//					 bindStringConverter=false,required=false)
//	@LangIndependentVaadinViewField
//	@VaadinViewComponentLabels(captionI18NKey="Localidad",
//							   useCaptionI18NKeyAsPlaceHolderKey=true)
//	@Getter @Setter private ComboBox<GeoDistrict> _districtCmb = new ComboBox<>();
	
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
	@Getter @Setter private ComboBox<GeoPortal> _portalCmb = new ComboBox();
	
	@VaadinViewComponentLabels(captionI18NKey="Código Postal",
							   useCaptionI18NKeyAsPlaceHolderKey=true)
	@Getter @Setter private TextField _zipCodeTf = new TextField();
	
	private final Button _save = new Button("Guardar");
	// Binder
	@Getter private final Binder<VaadinNORAContactViewObject> _vaadinUIBinder = new Binder<>(VaadinNORAContactViewObject.class);
	private VaadinNORAContactViewObject _viewObject;
	
	public VaadinNORAContactForm(final UII18NService i18n,
								 final Language lang,
								 final VaadinNORAContactFormPresenter presenter) {
		super();
		_i18n = i18n;
		_language = lang;
		_presenter = presenter;
		
		////////// Layout & style
		this.setColumns(2);
		this.setRows(5);
		this.setSizeFull();
		this.setSpacing(true);
		this.setMargin(false);
		
		this.addComponent(_countryCmb, 0, 0);
		this.addComponent(_stateCmb, 1, 0);
		this.addComponent(_countyCmb, 0, 1);
		this.addComponent(_regionCmb, 1, 1);
		this.addComponent(_municipalityCmb, 0, 2);
		this.addComponent(_streetCmb, 1, 2);
		//this.addComponent(_streetCmb, 0, 3, 1, 3);
		this.addComponent(_portalCmb, 0, 3);
		this.addComponent(_zipCodeTf, 1, 3);
		this.addComponent(_save, 0, 4, 1, 4);
		this.setComponentAlignment(_save, Alignment.BOTTOM_RIGHT);
		
		
		CallbackDataProvider<GeoStreet,String> dataProvider = null;
		dataProvider = new CallbackDataProvider<>(// query
									query -> {
//												if(_stateCmb.getValue() != null && _countyCmb.getValue() != null && _regionCmb.getValue() != null && _municipalityCmb.getValue() != null &&
//														 query.getFilter().toString().length() > 3) {
													return _presenter.findStreets(_stateCmb.getValue() != null ? _stateCmb.getValue().getId() : null,
																				  _countyCmb.getValue() != null ? _countyCmb.getValue().getId() : null,
																				  _regionCmb.getValue().getId(),
																				  _municipalityCmb.getValue().getId(),
																				  query.getFilter()
																			  		   .orElse(null))
																	 .stream();
//												} 
//												return new ArrayList().stream();
										
									},
									// count
									query -> {
												if(_stateCmb.getValue() != null && _countyCmb.getValue() != null && _regionCmb.getValue() != null && _municipalityCmb.getValue() != null) {
													return (int)_presenter.findStreets(_stateCmb.getValue().getId(),
																						  _countyCmb.getValue().getId(),
																						  _regionCmb.getValue().getId(),
																						  _municipalityCmb.getValue().getId(),
																						  query.getFilter()
																			  		           .orElse(null))
																			 .stream()
																		     .count();
												}
												return 0;
									});
		_streetCmb.setDataProvider(dataProvider);
		// set ui labels
		VaadinViews.using(_i18n)
				   .setI18NLabelsOf(this);
		
		////////// Bind: automatic binding using using @VaadinViewField annotation of view fields
		VaadinViews.using(_vaadinUIBinder,_i18n)
					.bindComponentsOf(this)
					.toViewObjectOfType(VaadinNORAContactViewObject.class);
		_viewObject = new VaadinNORAContactViewObject(_language);
		this.editViewObject(_viewObject);
		_setBehavior();
	}
	
	private void _setBehavior() {
		_countryCmb.setItemCaptionGenerator(item -> item.getNameIn(_language) != null ? item.getNameIn(_language) : item.getOfficialName());
		_stateCmb.setItemCaptionGenerator(item -> item.getNameIn(_language) != null ? item.getNameIn(_language) : item.getOfficialName());
		_countyCmb.setItemCaptionGenerator(item -> item.getNameIn(_language) != null ? item.getNameIn(_language) : item.getOfficialName());
		_regionCmb.setItemCaptionGenerator(item -> item.getNameIn(_language) != null ? item.getNameIn(_language) : item.getOfficialName());
		_municipalityCmb.setItemCaptionGenerator(item -> item.getNameIn(_language) != null ? item.getNameIn(_language) : item.getOfficialName());
	//	_districtCmb.setItemCaptionGenerator(GeoDistrict::getOfficialName);
		_streetCmb.setItemCaptionGenerator(item -> item.getNameIn(_language) != null ? item.getNameIn(_language) : item.getOfficialName());
		_portalCmb.setItemCaptionGenerator(item -> item.getNameIn(_language) != null ? item.getNameIn(_language) : item.getOfficialName());
		
		_loadCountryCmb();
		_countryCmb.addValueChangeListener(event -> _loadStateCmb());
		_stateCmb.addValueChangeListener(event -> _loadCountyCmb());
		_countyCmb.addValueChangeListener(event -> _loadRegionCmb());
		_regionCmb.addValueChangeListener(event -> _loadMunicipalityCmb());
		_municipalityCmb.addValueChangeListener(event -> _loadStreetCmb());
		_streetCmb.addValueChangeListener(event -> _loadPortalCmb());
		
		
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
		_clear(_regionCmb);
		_clear(_municipalityCmb);
		_clear(_streetCmb);
		_clear(_portalCmb);
	}
	private void _loadStateCmb() {
		if (_countryCmb.getValue() ==  null) {
			_clear(_stateCmb);
			return;
		}
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
		_clear(_regionCmb);
		_clear(_municipalityCmb);
		_clear(_streetCmb);
		_clear(_portalCmb);
	}
	private void _loadCountyCmb() {
		if (_stateCmb.getValue() ==  null) {
			_clear(_countyCmb);
			return;
		}
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
		_clear(_regionCmb);
		_clear(_municipalityCmb);
		_clear(_streetCmb);
		_clear(_portalCmb);
	}
	private void _loadRegionCmb() {
		if (_countyCmb.getValue() ==  null) {
			_clear(_regionCmb);
			return;
		}
		_presenter.onRegionsLoadRequested(_stateCmb.getValue().getId(),
									      _countyCmb.getValue().getId(),
										   UIPresenterSubscriber.from(
												 //onsuccess
												 regions -> { 
													 			_regionCmb.setItems(regions);
													 			_regionCmb.setEnabled(true);
												 },
												 // on error
							  					 th -> {
							  						 Notification.show(th.getMessage(),
							  								 		   Type.ERROR_MESSAGE);
							  					 })
		);
		_clear(_municipalityCmb);
		_clear(_streetCmb);
	}
	private void _loadMunicipalityCmb() {
		if (_regionCmb.getValue() ==  null) {
			_clear(_municipalityCmb);
			return;
		}
		_presenter.onMunicipalitiesLoadRequested(_stateCmb.getValue().getId(),
									             _countyCmb.getValue().getId(),
									             _regionCmb.getValue().getId(),
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
		_clear(_streetCmb);
		_clear(_portalCmb);
	}
	private void _loadStreetCmb() {
		if (_municipalityCmb.getValue() ==  null) {
			_clear(_streetCmb);
			return;
		}
		_streetCmb.setEnabled(true);
//		_presenter.onStreetLoadRequested(_stateCmb.getValue().getId(),
//									     _countyCmb.getValue().getId(),
//									     _regionCmb.getValue().getId(),
//									     _municipalityCmb.getValue().getId(),
//							             UIPresenterSubscriber.from(
//							        		   //onsuccess
//							        		   streets ->  { 
//											 			_streetCmb.setItems(streets);
//											 			_streetCmb.setEnabled(true);
//							        		   },
//							        		   // on error
//							        		   th -> {
//							        			   Notification.show(th.getMessage(),
//					  								 		   	     Type.ERROR_MESSAGE);
//							        		   })
//		);
		_clear(_portalCmb);
	}
	private void _loadPortalCmb() {
		if (_streetCmb.getValue() ==  null) {
			_clear(_portalCmb);
			return;
		}
		_presenter.onPortalLoadRequested(_stateCmb.getValue().getId(),
									     _countyCmb.getValue().getId(),
									     _regionCmb.getValue().getId(),
									     _municipalityCmb.getValue().getId(),
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
//	private void _loadDistrictCmb() {
//		_presenter.onDistrictsLoadRequested(_stateCmb.getValue().getId(),
//									        _countyCmb.getValue().getId(),
//									        _municipalityCmb.getValue().getId(),
//									        UIPresenterSubscriber.from(
//									        	   //onsuccess
//									        	   districtricts -> { 
//													 			_districtCmb.setItems(districtricts);
//													 			_districtCmb.setEnabled(true);
//									        		   },
//									        	   // on error
//									        	   th -> {
//									        		   Notification.show(th.getMessage(),
//							  							 		   	     Type.ERROR_MESSAGE);
//									        	   })
//		);
//	}
	
	private static void _clear(final ComboBox<?> cmb) {
		cmb.clear();
		cmb.setEnabled(false);
	}

	@Override
	public void editViewObject(VaadinNORAContactViewObject viewObj) {
		_vaadinUIBinder.readBean(viewObj);
		
	}
	@Override
	public void writeAsDraftEditedViewObjectTo(VaadinNORAContactViewObject viewObj) {
		_vaadinUIBinder.writeBeanAsDraft(viewObj);
		System.out.println(viewObj.getWrappedModelObject());
		
	}
	@Override
	public boolean writeIfValidEditedViewObjectTo(VaadinNORAContactViewObject viewObj) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
}
