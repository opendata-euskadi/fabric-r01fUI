package r01ui.base.components.url;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Label;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import r01f.html.HtmlLinkRenderer;
import r01f.types.url.web.WebLink;
import r01f.ui.weblink.IsUIViewWebLink;
import r01f.util.types.Strings;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class VaadinUrls {
/////////////////////////////////////////////////////////////////////////////////////////
//	web link
/////////////////////////////////////////////////////////////////////////////////////////	
	/**
	 * returns an html string representation of the url: <a href='..'>...
	 * @return
	 */
	public static final Label webLinkAsHtmlLink(final IsUIViewWebLink viewLink) {
		WebLink weblink = viewLink.getWrappedModelObject();
		String text = weblink.getDisplayText();
		String outText = text;
		weblink.setText(Strings.getfirstNCharacters(text,155));		
		String linkHtml = HtmlLinkRenderer.of(weblink)
										  .renderForceProtocolAndNewWindow();
		Label html = new Label(linkHtml,
							   ContentMode.HTML);
		html.setDescription(text);
		html.setSizeFull();
		weblink.setText(outText);
		return html;
	}
}
