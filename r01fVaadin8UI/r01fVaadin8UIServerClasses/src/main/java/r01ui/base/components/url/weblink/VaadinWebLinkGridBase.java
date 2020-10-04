package r01ui.base.components.url.weblink;

import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;

import r01f.patterns.Factory;
import r01f.types.url.Url;
import r01f.types.url.web.WebLink;
import r01f.ui.i18n.UII18NService;
import r01f.ui.weblink.IsUIViewWebLink;
import r01ui.base.components.url.VaadinUrlFormPopUpBase;
import r01ui.base.components.url.VaadinUrlGridBase;
import r01ui.base.components.url.VaadinUrls;

/**
 * A grid to manage a collection of {@link WebLink}
 * 
 * BEWARE!	If a collection of {@link WebLink} objects has to be managed, use {@link VaadinWebLinkGridBase}
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
 * with the [url] so the form opened at the pop-up contains more UI controls 
 * than just the [url builder]
 * To achieve this behavior the usage is still simple but a bit though
 * <pre class='brush:java'>
 *	    // [1] - create the view object type extending UIViewWebLink
 *	    // 		 that just contains an [url]
 *	    @Accessors(prefix="_")
 *	    public class MyViewWebLink 
 *	    	 extends UIViewWebLink {
 *      
 *	    	private static final long serialVersionUID = 4035876909199946714L;
 *	    	
 *	    	static final String MY_FIELD = "myField";
 *	    	@Getter @Setter private String _myField;
 *	    	
 *	    	public MyViewWebLink() {
 *	    		super(Url.from(""));
 *	    	}
 *	    	public MyViewWebLink(final Url url) {
 *	    		super(url);
 *	    	}
 *	    }
 *	    // [2] - Create the [form] extending R01UIUrlBuilderWebLinkFormBase and adding as many
 *	    //		 UI controls as needed
 *	    public class MyUrlBuilderWebLinkForm
 *	    	 extends R01UIUrlBuilderWebLinkFormBase<MyViewWebLink> {
 *	    	private static final long serialVersionUID = 3824767699544079034L;
 *	    	
 *	    	@VaadinViewField(bindToViewObjectFieldNamed = MyViewWebLink.MY_FIELD,
 *	    					 bindStringConverter = false, 
 *	    					 required = false)
 *	    	protected final TextField _myField = new TextField();
 *	    	
 *	    	public MyUrlBuilderWebLinkForm(final UII18NService i18n) {
 *	    		super(MyViewWebLink.class,
 *					  i18n);
 *	    		// DO NOT forget
 *	    		this.setCompositionRoot(new VerticalLayout(_myField,
 *	    						  		  				   _urlBuilder,					// DO NOT FORGET!!
 *														   _formLinkPresentation));		// DO NOT FORGET!!
 *	    		_bindUICompnents();
 *	    		_setUII18NLabels();
 *	    	}
 *	    }
 *	    // [3] - Create the [grid] extending VaadinWebLinkGridBase and customizing (if needed)
 *	    //		 the [grid]'s columns
 *	    public class MyWebLinkGrid
 *	    	 extends VaadinWebLinkGridBase<MyViewWebLink> {
 *      
 *	    	private static final long serialVersionUID = 2681163502504863344L;
 *	    	
 *	    	public MyWebLinkGrid(final UII18NService i18n) {
 *	    		super(i18n,
 *	    			  new MyUrlBuilderWebLinkForm(i18n),
 *	    			  // view object factory
 *	    			  () -> new MyViewWebLink());
 *	    	}
 *	    	@Override
 *	    	protected void _configureGridCols() {
 *	    		// add a new column
 *	    		this.addColumn(MyViewWebLink::getMyField);
 *	    		// add the standard columns
 *	    		super._configureGridCols();
 *	    	}
 *	    }
 * </pre>
 */
public class VaadinWebLinkGridBase<V extends IsUIViewWebLink>
	 extends VaadinUrlGridBase<V> {

	private static final long serialVersionUID = -308043738760674837L;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public <F extends VaadinWebLinkFormBase<V>> VaadinWebLinkGridBase(final UII18NService i18n,
				 	        									   	  final F form,
				 	        									   	  final Factory<V> viewObjFactory) {
		this(i18n,
			 // [url builder] popup factory
			 new Factory<VaadinUrlFormPopUpBase<V,F>>() {
						@Override
						public VaadinUrlFormPopUpBase<V, F> create() {
							return new VaadinUrlFormPopUpBase<V,F>(i18n,
												  			   	   form,
												  			   	   viewObjFactory) {
										private static final long serialVersionUID = -7819061122771519263L;
				  				   };
						}
			 },
			 // [url builder]-associated [view object]
			 viewObjFactory);	// an empty url
	}
	public <F extends VaadinWebLinkFormBase<V>,
			W extends VaadinUrlFormPopUpBase<V,F>> VaadinWebLinkGridBase(final UII18NService i18n,
				 	        									   	   	 final Factory<W> popUpFactory,
				 	        									   	   	 final Factory<V> viewObjFactory) {
		super(i18n,
			  // [url builder] popup factory
			  popUpFactory,
			  // [url builder]-associated [view object]
			  viewObjFactory);	// an empty url
		this.enableRowMovement();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	protected void _configureGridCols() {
		// add a new column with the web link
		this.addComponentColumn(item -> {
									Label html = VaadinUrls.webLinkAsHtmlLink(item); 
									CssLayout cs = new CssLayout(html);
									cs.setSizeFull();
									cs.addLayoutClickListener(event -> this.select(item));
									return cs;
								})
			.setExpandRatio(1)
			.setResizable(false)
			.setId("webLink");		// paint the link
		
		// super config
		super._configureGridCols();
		// hide the url column
		this.getColumn("url").setHidden(true);
	}
}