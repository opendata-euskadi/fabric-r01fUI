package r01ui.base.components.url;

import java.util.function.Function;

import com.vaadin.shared.Registration;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.TextField;

import r01f.types.url.Url;
import r01f.ui.i18n.UII18NService;
import r01f.util.types.Strings;

/**
 * A wrapper for a {@link TextField} where the user 
 * enters the url
 * <pre>
 * 		+=============================================+
 * 		+ [url.....................................]  +
 * 		+=============================================+
 * <pre>
 */
public class VaadinUrlField 
     extends CustomField<Url> {

	private static final long serialVersionUID = -8035408055147323319L;
/////////////////////////////////////////////////////////////////////////////////////////
//	UI FIELDS                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	private final TextField _txtUrl;
	
/////////////////////////////////////////////////////////////////////////////////////////
//	SERVICES  
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Transforms an string to url
	 */
	private final Function<String,Url> _urlFromString;
/////////////////////////////////////////////////////////////////////////////////////////
//	STATUS (avoid as much as possible)
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * the url could be created a the {@link #getValue()} method 
	 * BUT in order to create the {@link Url} object, the _urlFromString {@link Function}
	 * must be used and this function might use CORE-side logic to compute the {@link Url}
	 * ... so a better approach is compute the {@link Url} just when the _txtUrl field changes
	 */
	private Url _url;
	
	private Url _parseUrl(final String urlStr) {
		return Strings.isNOTNullOrEmpty(urlStr)
				 	? _urlFromString.apply(urlStr)
				 	: null;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinUrlField(final UII18NService i18n) {
		this(i18n,
			 urlStr -> Url.from(urlStr));	// transforms an strint to an url
	}
	public VaadinUrlField(final UII18NService i18n,
			 			  final Function<String,Url> urlFromString) {
		_urlFromString = urlFromString;
		
		////////// TextField
		// [url] text field
		_txtUrl = new TextField(i18n.getMessage("urlbuilder.url") + ":");
		_txtUrl.setWidth(100,Unit.PERCENTAGE);
		_txtUrl.setValueChangeMode(ValueChangeMode.TIMEOUT);	// raise the valueChange event
		_txtUrl.setValueChangeTimeout(3000);					// after 3 second
		
		////////// behavior
		_setBehavior(i18n);
	}
	private void _setBehavior(final UII18NService i18n) {
		_txtUrl.addValueChangeListener(valChangeEvent -> {
											// compute the url
											_url = _parseUrl(valChangeEvent.getValue());
									   });
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	protected Component initContent() {
		return _txtUrl;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public Url getValue() {
		return this.getUrl();
	}
	@Override
	protected void doSetValue(final Url url) {
		this.setUrlFromString(url != null ? url.asString() : "");
	}
	public String getUrlAsString() {
		return _txtUrl.getValue();
	}
	public void setUrlFromString(final String urlStr) {
		_txtUrl.setValue(urlStr);
	}
	public Url getUrl() {
		return _url;	// BEWARE return the already-parsed url
	}
	public void setUrl(final Url url) {
		this.setUrlFromString(url != null ? url.asString() : "");
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	ENABLE / DISABLE                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	 public void setUrlTextFieldEnabled(final boolean enabled) {
		 _txtUrl.setEnabled(enabled);
	 }
/////////////////////////////////////////////////////////////////////////////////////////
//	EVENTS                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	 
	 @Override
	 public Registration addValueChangeListener(final ValueChangeListener<Url> listener) {
		 return _txtUrl.addValueChangeListener(valChangeEvent -> {
			 										String oldUrlStr = valChangeEvent.getOldValue();
			 										Url oldUrl = oldUrlStr != null ? Url.from(oldUrlStr) : null;
			 										
			 										ValueChangeEvent<Url> evt = new ValueChangeEvent<>(this,
			 																					   	   oldUrl,
			 																					   	   valChangeEvent.isUserOriginated());
			 										listener.valueChange(evt);
		 									   });
	 }
}
