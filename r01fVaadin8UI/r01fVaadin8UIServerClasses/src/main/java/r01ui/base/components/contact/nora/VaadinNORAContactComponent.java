package r01ui.base.components.contact.nora;

import javax.inject.Inject;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.HorizontalLayout;

import r01f.locale.Language;
import r01f.ui.i18n.UII18NService;
import r01f.ui.vaadin.styles.VaadinValoTheme;
import r01f.util.types.Strings;
import r01ui.base.components.geo.VaadinViewGeoPosition;

public class VaadinNORAContactComponent 
      extends CustomField<VaadinViewGeoPosition> {
	private static final long serialVersionUID = 5018317833169484242L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final VaadinNORAContactForm _contactForm;	
	private final BrowserFrame _map;
	private VaadinViewGeoPosition _viewObject;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	@Inject	
	public VaadinNORAContactComponent(final UII18NService i18n,
									  final VaadinNORAContactFormPresenter presenter) {
		_contactForm = new VaadinNORAContactForm(i18n, 
												 Language.SPANISH, 
												 presenter);
		_map = new BrowserFrame("Mapa");
		_map.setWidth("600px");
		_map.setHeight("400px");
		//TODO ajustar al tipo de coordenadas
		_contactForm.getCoords().getCoords2D().addValueChangeListener(event -> {
																		String valueStr = event.getValue();
																		if (Strings.isNOTNullOrEmpty(valueStr) && valueStr.indexOf(",") != -1) {
																			String[] value = valueStr.split(",");
																			String crs = "";//_contactForm.getCoords().getCoordsStandard2D().getValue().getCrs();
																			_map.setSource(new ThemeResource("components/geocoder/previewMap.html?x="+value[0].trim()+"&y="+value[1].trim()+"&crs="+crs+"&zoom="+_contactForm.getZoom_level()));
																		}
																		
		});
		
	}
////////////////////////////////////////////////////////////////////////////////////////////
//	METHODS
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	protected Component initContent() {
		HorizontalLayout hl = new HorizontalLayout();
		hl.addStyleName(VaadinValoTheme.LAYOUT_WHITE_BORDERED);
		hl.setSizeFull();
		hl.setMargin(true);
		hl.addComponent(_contactForm);
		hl.addComponent(_map);
		hl.setComponentAlignment(_map, Alignment.MIDDLE_RIGHT);
		return hl;
	}
	@Override
	public VaadinViewGeoPosition getValue() {
		if (_viewObject != null) {
			_contactForm.writeAsDraftEditedViewObjectTo(_viewObject);
		}
		return _viewObject;
	}
	@Override
	protected void doSetValue(VaadinViewGeoPosition value) {
		_viewObject = value;
		_contactForm.editViewObject(_viewObject);
		
	}

}
