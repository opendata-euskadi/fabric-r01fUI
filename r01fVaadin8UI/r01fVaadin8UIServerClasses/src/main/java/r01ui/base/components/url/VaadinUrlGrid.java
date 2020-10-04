package r01ui.base.components.url;

import r01f.types.url.Url;
import r01f.types.url.web.WebLink;
import r01f.ui.i18n.UII18NService;
import r01f.ui.weblink.UIViewUrl;
import r01ui.base.components.url.weblink.VaadinWebLinkGrid;

/**
 * A grid to manage a collection of {@link Url}
 *
 * BEWARE!	If a collection of {@link WebLink} objects has to be managed, use {@link VaadinWebLinkGrid}
 *
 *			Note that a {@link WebLink} is just a {@link Url} with the link text and
 *			presentation data while a {@link Url} is JUST the raw {@link Url}
 *
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
 *		 |        |                              |                            |
 *		 |--------|                [Cancel] [OK] |----------------------------|
 *		 |        +==============================+                            |
 *		 |--------------------------------------------------------------------|
 *		 |                                                                    |
 *		 +--------------------------------------------------------------------+
 * </pre>
 * If a "simple" url [grid] is needed, the usage is very simple
 * <pre class='brush:java'>
 *		Collection<Url> urls = Lists.newArrayList(Url.from("www.euskadi.eus"),
 *												  Url.from("www.bizkaia.eus"));
 *		Collection<UIViewUrl> viewUrls = urls.stream()
 *												.map(UIViewUrl::new)
 *												.collect(Collectors.toList());
 *
 *		VaadinUrlGrid grid = new R01UIUrlGrid(i18n);
 *		grid.setItems(viewUrls);
 * </pre>
 * When adding or editing an [url] to the grid, this implementation opens a popup
 * with just the [url builder]
 *
 * ... the problem is that more often than not, there are more fields associated
 * with the [url] so the form opened at the pop-up contains more UI controls
 * than just the [url builder]
 * To achieve this behavior the usage is still simple but a bit though
 * (see base type)
 */
public class VaadinUrlGrid
	 extends VaadinUrlGridBase<UIViewUrl> {

	private static final long serialVersionUID = -4278124797398424465L;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinUrlGrid(final UII18NService i18n) {
		this(i18n,
			 // [url builder] popup factory
			 new VaadinUrlForm(i18n));
	}
	public VaadinUrlGrid(final UII18NService i18n,
					     final VaadinUrlForm form) {
		super(i18n,
			  // [url builder] popup factory
			  VaadinUrlFormPopUp.createFactoryFrom(i18n,
					  							   form),
			  // [url builder]-associated [view object]
			  UIViewUrl.FACTORY);
	}
}