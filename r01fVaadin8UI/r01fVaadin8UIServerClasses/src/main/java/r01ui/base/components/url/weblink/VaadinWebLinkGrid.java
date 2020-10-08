package r01ui.base.components.url.weblink;

import r01f.types.url.Url;
import r01f.types.url.web.WebLink;
import r01f.ui.i18n.UII18NService;
import r01f.ui.weblink.UIViewWebLink;

/**
 * A grid to manage a collection of {@link WebLink}
 * 
 * BEWARE!	If a collection of {@link WebLink} objects has to be managed, use {@link VaadinWebLinkGrid}
 * 
 *			Note that a {@link WebLink} is just a {@link Url} with the link text and 
 *			presentation data while a {@link Url} is JUST the raw {@link Url} 
 * 
 * When editing a grid-row, a popup with the [url builder] is shown
 * <pre class='brush:java'>
 *		          +==============================+
 *		 [new]    |------------------------------|         [delete] [up] [down]
 *		 +--------|                              |----------------------------+
 *		 |        | +-------------------------+  |                            |
 *		 |--------| |                         |  |----------------------------|
 *		 |        | |       URL BUILDER       |  |                            |
 *		 |--------| |                         |  |----------------------------|
 * 		 |        | +-------------------------+  |                            |
 *  	 |--------| +-------------------------+  |----------------------------|
 *		 |        | |                         |  |                            |
 *		 |--------| |    PRESENTATION DATA    |  |----------------------------|   <-- this is the DIFERENCE!!!
 *		 |        | |                         |  |                            |
 * 		 |--------| +-------------------------+  |----------------------------|
 *		 |        |                              |                            |
 *		 |--------|                [Cancel] [OK] |----------------------------|
 *		 |        +==============================+                            |
 *		 |--------------------------------------------------------------------|
 *		 |                                                                    |
 *		 +--------------------------------------------------------------------+
 * </pre>
 * If a "simple" url [grid] is needed, the usage is very simple
 * <pre class='brush:java'>
 *		Collection<WebLink> links = Lists.newArrayList(new WebLink(Url.from("www.euskadi.eus")),
 *												       new WebLink(Url.from("www.bizkaia.eus")));
 *		Collection<UIViewWebLink> viewLinks = urls.stream()
 *													 .map(UIViewWebLink::new)
 *													 .collect(Collectors.toList());
 *		
 *		VaadinWebLinkGrid grid = new VaadinWebLinkGrid(i18n);
 *		grid.setItems(viewUrls);
 * </pre>
 * When adding or editing an [url] to the grid, this implementation opens a popup
 * with just the [url builder]
 * 
 * ... the problem is that more often than not, there are more fields associated 
 * with the [web link] so the form opened at the pop-up contains more UI controls 
 * than just the [url builder] and [link presentation data] form
 * To achieve this behavior the usage is still simple but a bit though
 * (see base type)
 */
public class VaadinWebLinkGrid
	 extends VaadinWebLinkGridBase<UIViewWebLink> {

	private static final long serialVersionUID = -308043738760674837L;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinWebLinkGrid(final UII18NService i18n) {
		this(i18n,
			 // [url builder] popup factory
			 new VaadinWebLinkForm(i18n));
	}
	public <F extends VaadinWebLinkFormBase<UIViewWebLink>> VaadinWebLinkGrid(final UII18NService i18n,
				 	        									   			  final F form) {
		super(i18n,
			  // [url builder] popup factory
			  form,
			  // [url builder]-associated [view object]
			  () -> new UIViewWebLink(Url.from("")));	// an empty url
	}
}