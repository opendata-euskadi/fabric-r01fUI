package r01f.ui.weblink;

import java.util.Collection;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import r01f.locale.Language;
import r01f.types.url.Url;
import r01f.types.url.web.WebLink;
import r01f.types.url.web.WebLinkCollection;
import r01f.types.url.web.WebLinkOpenTarget;
import r01f.types.url.web.WebLinkPresentationData;
import r01f.ui.viewobject.UIViewObjectWrappedBase;
import r01f.util.types.collections.CollectionUtils;

/**
 * The [UI] view object wrapping the {@link WebLink} [model object]
 */
public class UIViewWebLink
  	 extends UIViewObjectWrappedBase<WebLink>
  implements IsUIViewWebLink {

	private static final long serialVersionUID = -2958846330336464208L;	
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR / BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
	public UIViewWebLink(final WebLink webLink) {
		super(webLink);
	}
	public UIViewWebLink(final Url url) {
		this(new WebLink(url));
	}
	public UIViewWebLink(final UIViewWebLink other) {
		this(new WebLink(other.getWrappedModelObject()));		// clone
	}
	public static UIViewWebLink from(final WebLink webLink) {
		return new UIViewWebLink(webLink);
	}
	public static UIViewWebLink from(final Url url) {
		return new UIViewWebLink(url);
	}
	public static Collection<UIViewWebLink> uiWebLinkCollectionFromModelObject(final WebLinkCollection linkCol,
																	 		   final Language lang) {
		if (linkCol == null) return Lists.newArrayList();

		Collection<WebLink> links = linkCol.getUrlsIn(lang);
		Collection<UIViewWebLink> outViewObjs = CollectionUtils.hasData(links) ? links.stream()
																						 .map(UIViewWebLink::new)
																						 .collect(Collectors.toList())
																				  : Lists.newArrayList();
		return outViewObjs;
	}
	public static WebLinkCollection uiWebLinkCollectionToModelObject(final Collection<UIViewWebLink> uiWebLinkCol,
																	 final Language lang) {
		if (uiWebLinkCol == null) return new WebLinkCollection();
		Collection<WebLink> links = CollectionUtils.hasData(uiWebLinkCol)
											? uiWebLinkCol.stream()
												 .map(UIViewWebLink::getWrappedModelObject)
												 .map(webLink -> {
													 		// ensure the link has the correct lang
													 		webLink.setLanguage(lang);
													 		return webLink;
												 	  })
												 .collect(Collectors.toList())
											: Lists.newArrayList();
		return new WebLinkCollection(links);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	URL
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String URL_FIELD = "url";

	@Override
	public Url getUrl() {
		return _wrappedModelObject.getUrl();
	}
	@Override
	public void setUrl(final Url url) {
		_wrappedModelObject.setUrl(url);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	TEXT / TITLE / DESCRIPTION
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String TEXT_FIELD = "text";

	@Override
	public String getText() {
		return _wrappedModelObject.getText();
	}
	@Override
	public void setText(final String text) {
		_wrappedModelObject.setText(text);
	}

	public static final String TITLE_FIELD = "title";

	@Override
	public String getTitle() {
		return _wrappedModelObject.getTitle();
	}
	@Override
	public void setTitle(final String text) {
		_wrappedModelObject.setTitle(text);
	}

	public static final String DESCRIPTION_FIELD = "description";

	@Override
	public String getDescription() {
		return _wrappedModelObject.getDescription();
	}
	@Override
	public void setDescription(final String description) {
		_wrappedModelObject.setDescription(description);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	PRESENTATION
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String OPEN_IN_NEW_WINDOW_FIELD = "openingInNewWindow";

	@Override
	public boolean isOpeningInNewWindow() {
		return _wrappedModelObject.getPresentation() != null
			&& _wrappedModelObject.getPresentation().isOpeningInNewWindow();
	}
	@Override
	public void setOpeningInNewWindow(final boolean open) {
		if (_wrappedModelObject.getPresentation() == null) _wrappedModelObject.setPresentation(new WebLinkPresentationData());
		if (open) {
			_wrappedModelObject.getPresentation()
							   .setOpenTarget(WebLinkOpenTarget.BLANK);		// new window
		} else {
			_wrappedModelObject.getPresentation()
							   .setOpenTarget(null);
			_wrappedModelObject.getPresentation()
							   .setNewWindowOpeningMode(null);
		}
	}
	@Override
	public boolean equals(final Object other) {
		return other instanceof IsUIViewWebLink
					? _wrappedModelObject.equals(((IsUIViewWebLink)other).getWrappedModelObject())
					: false;
	}
	@Override
	public int hashCode() {
		return _wrappedModelObject.hashCode();
	}
}
