package r01f.ui.vaadin.nora.contact;

import javax.inject.Inject;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.HorizontalLayout;

import r01f.locale.Language;
import r01f.ui.i18n.UII18NService;
import r01f.util.types.Strings;

public class VaadinNORAContactComponent 
      extends CustomField<VaadinNORAContactViewObject> {
	private static final long serialVersionUID = 5018317833169484242L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final VaadinNORAContactForm _contactForm;	
	private final UII18NService _i18n;
	private final BrowserFrame _map;
	private VaadinNORAContactViewObject _viewObject;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	@Inject	
	public VaadinNORAContactComponent(final UII18NService i18n,
									  final VaadinNORAContactFormPresenter presenter) {
		_i18n = i18n;
		_contactForm = new VaadinNORAContactForm(i18n, 
												 Language.SPANISH, 
												 presenter);
		_map = new BrowserFrame("Mapa", new ThemeResource("components/geocoder/previewMap.html?x=526776.60765916&y=4743864.59894248"));
		_map.setWidth("600px");
		_map.setHeight("400px");
		_contactForm.getCoords2D().addValueChangeListener(event -> {
																		String valueStr = event.getValue();
																		if(Strings.isNOTNullOrEmpty(valueStr) && valueStr.indexOf(",") != -1) {
																			String[] value = valueStr.split(",");
																			_map.setSource(new ThemeResource("components/geocoder/previewMap.html?x="+value[0].trim()+"&y="+value[1].trim()));
																		}
																		
		});
		
	}
////////////////////////////////////////////////////////////////////////////////////////////
//	METHODS
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	protected Component initContent() {
		HorizontalLayout hl = new HorizontalLayout();
		hl.setSizeFull();
		hl.addComponent(_contactForm);
		hl.addComponent(_map);
		return hl;
	}
	@Override
	public VaadinNORAContactViewObject getValue() {
		if(_viewObject != null) {
			_contactForm.writeAsDraftEditedViewObjectTo(_viewObject);
		}
		return _viewObject;
	}
	@Override
	protected void doSetValue(VaadinNORAContactViewObject value) {
		_viewObject = value;
		_contactForm.editViewObject(_viewObject);
		
	}

}
