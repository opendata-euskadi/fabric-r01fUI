package r01ui.base.components.url;

import lombok.experimental.Accessors;
import r01f.ui.i18n.UII18NService;
import r01f.ui.weblink.UIViewUrl;

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
public class VaadinUrlForm
	 extends VaadinUrlFormBase<UIViewUrl> {
	
	private static final long serialVersionUID = -5934555963281887657L;

/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinUrlForm(final UII18NService i18n) {
		super(UIViewUrl.class,
			  i18n);
		// do not forget!!
		this.setCompositionRoot(_txtUrl);	// the layout just contains the url builder
		_bindUIComponents();
		_setUII18NLabels();
	}
}