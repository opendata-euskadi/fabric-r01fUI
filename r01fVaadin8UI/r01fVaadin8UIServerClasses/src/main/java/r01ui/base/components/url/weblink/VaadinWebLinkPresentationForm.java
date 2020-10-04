package r01ui.base.components.url.weblink;

import com.vaadin.data.Binder;
import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.shared.Registration;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Composite;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.ui.i18n.UII18NService;
import r01f.ui.vaadin.annotations.VaadinViewComponentLabels;
import r01f.ui.vaadin.annotations.VaadinViewField;
import r01f.ui.vaadin.view.VaadinComponent;
import r01f.ui.vaadin.view.VaadinViews;
import r01f.ui.weblink.IsUIViewWebLinkPresentation;
import r01f.ui.weblink.UIViewWebLink;
import r01ui.base.components.form.VaadinFormEditsViewObject;

/**
 * A form to configure the url presentation
 */
@Accessors(prefix="_")
public class VaadinWebLinkPresentationForm<P extends IsUIViewWebLinkPresentation> 
     extends Composite 
  implements VaadinComponent,
  			 VaadinFormEditsViewObject<P> {

	private static final long serialVersionUID = 2547455489803052008L;
/////////////////////////////////////////////////////////////////////////////////////////
//	UI FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@VaadinViewField(bindToViewObjectFieldNamed=UIViewWebLink.TEXT_FIELD,
					 required=true) 
	@VaadinViewComponentLabels(captionI18NKey="urlbuilder.presentation.text",
							   useCaptionI18NKeyAsPlaceHolderKey=true)
	private final TextField _txtLinkText = new TextField();
	
	@VaadinViewField(bindToViewObjectFieldNamed=UIViewWebLink.DESCRIPTION_FIELD,
					 required=false) 
	@VaadinViewComponentLabels(captionI18NKey="urlbuilder.presentation.description",
							   useCaptionI18NKeyAsPlaceHolderKey=true)
	private final TextField _txtLinkDescription = new TextField();
	
	@VaadinViewField(bindToViewObjectFieldNamed=UIViewWebLink.OPEN_IN_NEW_WINDOW_FIELD,
					 required=true) 
	@VaadinViewComponentLabels(captionI18NKey="urlbuilder.presentation.open-new-window",
							   useCaptionI18NKeyAsPlaceHolderKey=true)
	private final CheckBox _chkOpenNewWindow = new CheckBox();
	
	@Getter private final Binder<P> _vaadinUIBinder;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinWebLinkPresentationForm(final UII18NService i18n,
										final Class<P> viewObjType) {
		this(i18n,
			 viewObjType,
			 null);	// no link builder features
	}
	public VaadinWebLinkPresentationForm(final UII18NService i18n,
										 final Class<P> viewObjType,
										 // sets the [link builder wizard] features
										 final VaadinWebLinkFormFeatures linkBuilderFeatures) {
		////////// UI
		VerticalLayout vly = new VerticalLayout(_txtLinkText,
												_txtLinkDescription,
												_chkOpenNewWindow);
		vly.setMargin(false);
		this.setCompositionRoot(vly);
		
		////////// features
		boolean showTextInput = linkBuilderFeatures == null || linkBuilderFeatures.isShowLinkTextInput();
		_txtLinkText.setVisible(showTextInput);
		
		boolean showDescriptionInput = linkBuilderFeatures == null || linkBuilderFeatures.isShowLinkDescriptionInput();
		_txtLinkDescription.setVisible(showDescriptionInput);
		
		boolean showOpeningFeatures = linkBuilderFeatures == null || linkBuilderFeatures.isShowLinkOpeningFeatures();
		_chkOpenNewWindow.setVisible(showOpeningFeatures);
		
		////////// set ui labels
		VaadinViews.using(i18n)
				   .setI18NLabelsOf(this);

		////////// Bind: automatic binding using using @VaadinViewField annotation of view fields
		_vaadinUIBinder = new Binder<>(viewObjType);
		
		VaadinViews.using(_vaadinUIBinder,i18n)
				   .bindComponentsOf(this)
				   .toViewObjectOfType(viewObjType);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	BINDING
/////////////////////////////////////////////////////////////////////////////////////////	
	////////// [viewObject] > [UI control] --------------
	@Override
	public void editViewObject(final P viewObj) {
		_vaadinUIBinder.readBean(viewObj);
	}
	////////// [UI control] > [viewObject] --------------
	@Override
	public void writeAsDraftEditedViewObjectTo(final P viewObject) {
		_vaadinUIBinder.writeBeanAsDraft(viewObject);
	}
	@Override
	public boolean writeIfValidEditedViewObjectTo(final P viewObj) {
		return _vaadinUIBinder.writeBeanIfValid(viewObj);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public void clear() {
		_txtLinkText.setValue("");
		_txtLinkDescription.setValue("");
		_chkOpenNewWindow.setValue(false);
	}
	
	public String getLinkText() {
		return _txtLinkText.getValue();
	}
	public void setLinkText(final String text) {
		_txtLinkText.setValue(text);
	}
	
	public String getLinkDescription() {
		return _txtLinkDescription.getValue();
	}
	public void setLinkDescription(final String desc) {
		_txtLinkDescription.setValue(desc);
	}
	
	public boolean isOpeningInNewWindow() {
		return _chkOpenNewWindow.getValue();
	}
	public void setOpeiningInNewWindow(final boolean val) {
		_chkOpenNewWindow.setValue(val);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	LISTENERS
/////////////////////////////////////////////////////////////////////////////////////////
	public Registration addValueChangeListener(final ValueChangeListener<P> listener) {
		return _vaadinUIBinder.addValueChangeListener(listener);
	}
}
