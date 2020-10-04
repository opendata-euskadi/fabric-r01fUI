package r01ui.base.components.url.weblink;

import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.shared.Registration;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import lombok.experimental.Accessors;
import r01f.html.HtmlLinkRenderer;
import r01f.types.url.Url;
import r01f.ui.i18n.UII18NService;
import r01f.ui.weblink.IsUIViewWebLink;
import r01f.util.types.Strings;
import r01ui.base.components.url.VaadinUrlForm;
import r01ui.base.components.url.VaadinUrlFormBase;

/////////////////////////////////////////////////////////////////////////////////////////
/**
 * The form: just contains the [url builder] component
 * <pre>
 * 		+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * 		| +-------------------------------------------------------------+ |
 * 		| +   	                                                        + |
 * 		| + Url [                                                    ]  + |
 * 		| +                                                             + |
 * 		| +-------------------------------------------------------------+ |
 * 		| +-------------------------------------------------------------+ |
 * 	    | +                                                             + |
 * 		| +           The [url presentation data] form                <--------- this is an addition from the base {@link VaadinUrlForm}
 * 		| +                                                             + |
 * 		| +-------------------------------------------------------------+ |
 * 		| [Link preview]                                              <--------- this is an addition from the base {@link VaadinUrlForm}
 * 		+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * </pre>
 */
@Accessors(prefix="_")
public abstract class VaadinWebLinkFormBase<V extends IsUIViewWebLink>
	 		  extends VaadinUrlFormBase<V> {

	private static final long serialVersionUID = 3876732454052356848L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	// a form with the presentation data
	protected final VaadinWebLinkPresentationForm<V> _formLinkPresentation;

	// a link preview
	protected final Label _lblWebLinkPreview = new Label();
	protected final HorizontalLayout _lyPreview;

/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinWebLinkFormBase(final Class<V> viewObjType,
								 final UII18NService i18n,
								 // sets the [link builder wizard] features
								 final VaadinWebLinkFormFeatures linkBuilderFeatures) {
		super(viewObjType,
			  i18n);

		////////// UI
		// the [web link] presentation
		_formLinkPresentation = new VaadinWebLinkPresentationForm<>(i18n,
																   viewObjType,
																   linkBuilderFeatures);
		// link preview
		_lblWebLinkPreview.setContentMode(ContentMode.HTML);
		_lyPreview = new HorizontalLayout();
		_lyPreview.setWidth(100, Unit.PERCENTAGE);
		_lyPreview.addComponents(new Label(i18n.getMessage("urlbuilder.presentation.try-weblink")),
					     		 _lblWebLinkPreview);
		_lyPreview.setExpandRatio(_lblWebLinkPreview,1);
		_lyPreview.setVisible(false);

		////////// Layout
//		CssLayout vly = new CssLayout(_urlBuilder,
//									  _formLinkPresentation,
//									  _lyPreview);
		VerticalLayout vly = new VerticalLayout(_txtUrl,
												_formLinkPresentation,
												_lyPreview);
		this.setCompositionRoot(vly);	// the layout just contains the url builder
		vly.addStyleName("r01platea-url-builder");

		////////// behavior
		_setBehavior();
	}
	private void _setBehavior() {
		// If the url changes we must check if it provides extra information as text
		this.addUrlValueChangeListener(valChangeEvent -> _urlChange());
		this.addWebLinkPresentationValueChangeListener(valChangeEvent -> _updateLinkPreview());
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void clear() {
		super.clear();		// clear the url builder
		_formLinkPresentation.clear();
	}

	public Url getUrl() {
		return _txtUrl.getUrl();
	}
	public void setUrl(final Url url) {
		_txtUrl.setValue(url);
	}

	public String getLinkText() {
		return _formLinkPresentation.getLinkText();
	}
	public void setLinkText(final String text) {
		_formLinkPresentation.setLinkText(text);
	}

	public String getLinkDescription() {
		return _formLinkPresentation.getLinkDescription();
	}
	public void setLinkDescription(final String desc) {
		_formLinkPresentation.setLinkDescription(desc);
	}

	public boolean isOpeningInNewWindow() {
		return _formLinkPresentation.isOpeningInNewWindow();
	}
	public void setOpeiningInNewWindow(final boolean val) {
		_formLinkPresentation.setOpeiningInNewWindow(val);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	BINDING
/////////////////////////////////////////////////////////////////////////////////////////
	////////// [viewObject] > [UI control] --------------
	@Override
	public void editViewObject(final V viewObj) {
		// set the url builder value
		super.editViewObject(viewObj);

		// tell the [link presentation] form to edit
		_formLinkPresentation.editViewObject(viewObj);

		// show the link preview
		_updateLinkPreview();
	}
	////////// [UI control] > [viewObject] --------------
	@Override
	public void writeAsDraftEditedViewObjectTo(final V viewObject) {
		// write the url
		super.writeAsDraftEditedViewObjectTo(viewObject);

		// write the presentation
		_formLinkPresentation.writeAsDraftEditedViewObjectTo(viewObject);
	}
	@Override
	public boolean writeIfValidEditedViewObjectTo(final V viewObj) {
		// write the url
		boolean urlValid = super.writeIfValidEditedViewObjectTo(viewObj);

		// write the presentation
		boolean presentationValid = super.writeIfValidEditedViewObjectTo(viewObj);

		return urlValid && presentationValid;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	private void _urlChange() {
		if (Strings.isNullOrEmpty(this.getLinkText())) {
			this.setLinkText(_txtUrl.getUrlAsString());
		}
		_updateLinkPreview();
	}

	private void _updateLinkPreview() {
		String htmlLink = this.getUrl() != null
								? HtmlLinkRenderer.of(this.getUrl(),
													  this.getLinkText(),this.getLinkDescription())
									  	  .renderForceProtocolAndNewWindow()
							    : "";
		_lblWebLinkPreview.setValue(htmlLink);
		_lyPreview.setVisible(this.getUrl() != null);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	EVENT LISTENERS
/////////////////////////////////////////////////////////////////////////////////////////
	public Registration addWebLinkPresentationValueChangeListener(final ValueChangeListener<V> listener) {
		return _formLinkPresentation.addValueChangeListener(listener);
	}
}