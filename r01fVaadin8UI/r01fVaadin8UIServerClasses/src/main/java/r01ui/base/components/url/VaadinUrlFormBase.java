package r01ui.base.components.url;

import com.vaadin.data.Binder;
import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.shared.Registration;
import com.vaadin.ui.Composite;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.types.url.Url;
import r01f.ui.i18n.UII18NService;
import r01f.ui.vaadin.annotations.VaadinViewComponentLabels;
import r01f.ui.vaadin.annotations.VaadinViewField;
import r01f.ui.vaadin.view.VaadinViews;
import r01f.ui.weblink.IsUIViewUrl;
import r01f.ui.weblink.UIViewUrl;
import r01ui.base.components.form.VaadinDetailForm;
import r01ui.base.components.form.VaadinFormEditsViewObject;
import r01ui.base.components.url.customfield.VaadinUrlField;

/////////////////////////////////////////////////////////////////////////////////////////
/**
 * The form: just contains the [url builder] component
 * <pre>
 * 		+=================================================================+
 * 		| Url [                                                        ]  |
 * 		+=================================================================+
 * </pre>
 */
@Accessors(prefix="_")
public abstract class VaadinUrlFormBase<V extends IsUIViewUrl>
	 		  extends Composite
	 	   implements VaadinDetailForm<V>,
  			 		  VaadinFormEditsViewObject<V> {
	
	private static final long serialVersionUID = -6565226026874277311L;
/////////////////////////////////////////////////////////////////////////////////////////
//	SERVICES
/////////////////////////////////////////////////////////////////////////////////////////
	private final UII18NService _i18n;
	
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final Class<V> _viewObjType;
/////////////////////////////////////////////////////////////////////////////////////////
//	UI FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	@VaadinViewField(bindToViewObjectFieldNamed = UIViewUrl.URL_FIELD,
					 bindStringConverter = false, 
					 required = false)
	@VaadinViewComponentLabels(captionI18NKey="url",
							   useCaptionI18NKeyAsPlaceHolderKey=true)
	protected final VaadinUrlField _txtUrl;
	
	@Getter protected final Binder<V> _vaadinUIBinder;
	
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinUrlFormBase(final Class<V> viewObjType,
							 final UII18NService i18n) {
		_viewObjType = viewObjType;
		_i18n = i18n;
		
		////////// UI
		_txtUrl = new VaadinUrlField(i18n);
		
		////////// binder
		_vaadinUIBinder = new Binder<>(_viewObjType);
		
		// BEWARE!!
		// Do NOT forget to:
		//		- Set the composition root
		// 		- Call _bindUICompnents() and _setUII18NLabels()
		// ... at the end of the sub-type constructor
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	THESE METHODS MUST BE CALLED AT THE END OF THE SUB-TYPE CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	protected void _bindUIComponents() {
		// Bind: automatic binding using using @VaadinViewField annotation of view fields
		VaadinViews.using(_vaadinUIBinder,_i18n)
				   .bindComponentsOf(this)
				   .toViewObjectOfType(_viewObjType);		
	}
	protected void _setUII18NLabels() {
		// set ui labels
		VaadinViews.using(_i18n)
				   .setI18NLabelsOf(this);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public void clear() {
		_txtUrl.clear();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	EVENT LISTENERS DELEGATES
/////////////////////////////////////////////////////////////////////////////////////////	
	public Registration addUrlValueChangeListener(final ValueChangeListener<Url> listener) {
		return _txtUrl.addValueChangeListener(listener);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	BINDING
/////////////////////////////////////////////////////////////////////////////////////////	
	////////// [viewObject] > [UI control] --------------
	@Override
	public void editViewObject(final V viewObj) {
		_vaadinUIBinder.readBean(viewObj);
	}
	////////// [UI control] > [viewObject] --------------
	@Override
	public void writeAsDraftEditedViewObjectTo(final V viewObject) {
		_vaadinUIBinder.writeBeanAsDraft(viewObject);
	}
	@Override
	public boolean writeIfValidEditedViewObjectTo(final V viewObj) {
		return _vaadinUIBinder.writeBeanIfValid(viewObj);
	}
}