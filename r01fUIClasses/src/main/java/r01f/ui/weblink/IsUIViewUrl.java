package r01f.ui.weblink;

import r01f.html.HtmlLinkRenderer;
import r01f.types.url.HasUrl;
import r01f.types.url.web.WebLink;
import r01f.types.url.web.WebLinkOpenTarget;
import r01f.types.url.web.WebLinkPresentationData;
import r01f.ui.viewobject.UIViewObject;

public interface IsUIViewUrl 
	     extends HasUrl,
	     		 UIViewObject {
	/**
	 * returns an html string representation of the url: <a href='..'>...</a>
	 * @return
	 */
	public default String getUrlAsHtmlLink() {
		if (this.getUrl() == null) return "";
		
		// return a html string representation of the url: <a href='..'>...</a>
		WebLink webLink = new WebLink(this.getUrl());
		webLink.setPresentation(WebLinkPresentationData.create()
													   .withOpenTarget(WebLinkOpenTarget.BLANK));
		String webLinkStr = HtmlLinkRenderer.of(webLink)
											.render();
		return webLinkStr;
	}
}
