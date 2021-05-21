package r01f.ui.vaadin.security.login;

import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.Registration;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Composite;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;

import r01f.types.Path;
import r01f.types.url.Url;
import r01f.types.url.UrlPath;
import r01f.types.url.UrlQueryString;
import r01f.types.url.UrlQueryStringParam;
import r01f.util.types.Strings;

/**
 * A login button like:
 * <pre>
 * 		[img] [button]
 * </pre>
 */
public class VaadinLoginMethodButton 
     extends Composite {

	private static final long serialVersionUID = 1128663389100371859L;
/////////////////////////////////////////////////////////////////////////////////////////
//	UI FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final Image _icon;
	private final Button _btn;
	
	private final Url _toUrl;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinLoginMethodButton(final Path imgPath,
								   final Url loginUrl,
								   final Url frontEndUrlBase,final UrlPath appUrlPath) {
		// UI
		_icon = new Image();
		_icon.setIcon(new ThemeResource(imgPath.asRelativeString()));
		_toUrl = frontEndUrlBase.joinWith(appUrlPath);
		
		_btn = new Button();
		this.setLoginUrl(loginUrl);
		
		// Layout
		HorizontalLayout ly = new HorizontalLayout(_icon,_btn);
		this.setCompositionRoot(ly);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public Registration addClickListener(final ClickListener clickListerner) {
		return _btn.addClickListener(clickListerner);
	}
	@Override
	public void addStyleNames(final String... styles) {
		_btn.addStyleNames(styles);
	}
	@Override
	public void setCaption(final String caption) {
		_btn.setCaption(caption);
	}
	public void setLoginUrl(final Url loginUrl) {
		if (loginUrl == null) return;
		
		Url theLoginUrl = loginUrl.joinWith(UrlQueryString.fromParams(UrlQueryStringParam.of("to",_toUrl)));	// http://site/app/google/users/login?to=http://localhost:8080/r01PLATEAWebServiceCatalogUIWar/
		_btn.addListener(clickEvent ->  Page.getCurrent()
											.open(theLoginUrl.asString(),
												  null));	// window name (null = same win)
	}
}
