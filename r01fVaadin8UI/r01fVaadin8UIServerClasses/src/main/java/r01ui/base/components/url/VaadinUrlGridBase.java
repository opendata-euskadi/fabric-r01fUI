package r01ui.base.components.url;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.renderers.HtmlRenderer;

import r01f.patterns.Factory;
import r01f.ui.i18n.UII18NService;
import r01f.ui.vaadin.styles.VaadinValoTheme;
import r01f.ui.weblink.IsUIViewUrl;
import r01ui.base.components.grid.VaadinCRUDGridWithPopUpDetailBase;
import r01ui.base.components.grid.VaadinGridButton;

/**
 * Base type of a grid with all the source urls
 * When editing a grid-row, a popup with the [url builder] is shown
 * <pre class='brush:java'>
 *		          +==============================+
 *		 [new]    |------------------------------|         [delete] [up] [down]
 *		 +--------|                              |----------------------------+
 *		 |--------|                              |----------------------------|
 *		 |--------|          URL BUILDER         |----------------------------|
 *		 |--------|                              |----------------------------|
 *		 |--------|                              |----------------------------|
 *		 |--------|                [Cancel] [OK] |----------------------------|
 *		 |--------+==============================+----------------------------|
 *		 |--------------------------------------------------------------------|
 *		 +--------------------------------------------------------------------+
 * </pre>
 * Usage:
 * <pre class='brush:java'>
 *		Collection<Url> urls = Lists.newArrayList(Url.from("www.euskadi.eus"),
 *												  Url.from("www.bizkaia.eus"));
 *		Collection<UIViewUrl> viewUrls = urls.stream()
 *												.map(UIViewUrl::new)
 *												.collect(Collectors.toList());
 *		
 *		VaadinUrlGrid grid = new VaadinUrlGrid(i18n);
 *		grid.setItems(viewUrls);
 * </pre>
 * When adding or editing an [url] to the grid, this implementation opens a popup
 * with just the [url builder]
 * 
 * ... the problem is that more often than not, there are more fields associated 
 * with the [url] so the form opened at the pop-up contains more UI controls 
 * than just the [url]
 * To achieve this behavior the usage is still simple but a bit though:
 * <pre class='brush:java'>
 *	    // [1] - create the view object type extending UIViewUrl
 *	    // 		 that just contains an [url]
 *	    @Accessors(prefix="_")
 *	    public class MyViewUrl 
 *	    	 extends UIViewUrl {
 *      
 *	    	private static final long serialVersionUID = 4035876909199946714L;
 *	    	
 *	    	static final String MY_FIELD = "myField";
 *	    	@Getter @Setter private String _myField;
 *	    	
 *	    	public MyViewUrl() {
 *	    		super(Url.from(""));
 *	    	}
 *	    	public MyViewUrl(final Url url) {
 *	    		super(url);
 *	    	}
 *	    }
 *	    // [2] - Create the [form] extending VaadinUrlFormBase and adding as many
 *	    //		 UI controls as needed
 *	    public class MyUrlBuilderForm
 *	    	 extends VaadinUrlFormBase<MyViewUrl> {
 *	    	private static final long serialVersionUID = 3824767699544079034L;
 *	    	
 *	    	@VaadinViewField(bindToViewObjectFieldNamed = MyViewUrl.MY_FIELD,
 *	    					 bindStringConverter = false, 
 *	    					 required = false)
 *	    	protected final TextField _myField = new TextField();
 *	    	
 *	    	public MyUrlBuilderForm(final UII18NService i18n) {
 *	    		super(MyViewUrl.class,
 *					  i18n);
 *	    		// DO NOT forget
 *	    		this.setCompositionRoot(new VerticalLayout(_myField,
 *	    						  		  				   _urlBuilder));		// DO NOT FORGET!
 *	    		_bindUICompnents();
 *	    		_setUII18NLabels();
 *	    	}
 *	    }
 *	    // [3] - Create the [grid] extending VaadinUrlGridBase and customizing (if needed)
 *	    //		 the [grid]'s columns
 *	    public class MyUrlGrid
 *	    	 extends VaadinUrlGridBase<MyViewUrl> {
 *      
 *	    	private static final long serialVersionUID = 2681163502504863344L;
 *	    	
 *	    	public MyUrlGrid(final UII18NService i18n) {
 *	    		super(i18n,
 *	    			  new MyUrlBuilderForm(i18n),
 *	    			  // view object factory
 *	    			  () -> new MyViewUrl());
 *	    	}
 *	    	@Override
 *	    	protected void _configureGridCols() {
 *	    		// add a new column
 *	    		this.addColumn(MyViewUrl::getMyField);
 *	    		// add the standard columns
 *	    		super._configureGridCols();
 *	    	}
 *	    }
 * </pre> 
 */
public abstract class VaadinUrlGridBase<V extends IsUIViewUrl>
	 		  extends VaadinCRUDGridWithPopUpDetailBase<V> {

	private static final long serialVersionUID = -308043738760674837L;	
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public <F extends VaadinUrlFormBase<V>> VaadinUrlGridBase(final UII18NService i18n,
				 	    									  final F form,
				 	    									  final Factory<V> viewObjFactory) {
		super(i18n,
			  // [url builder] popup factory
			  new Factory<VaadinUrlFormPopUpBase<V,F>>() {
						@Override
						public VaadinUrlFormPopUpBase<V,F> create() {
							return new VaadinUrlFormPopUpBase<V,F>(i18n,
						  								  		   form,
						  								  		   viewObjFactory) {
											private static final long serialVersionUID = 2130620811809132949L; 
								   };
						}
			  },
			  // [url builder]-associated [view object]
			  viewObjFactory);	// an empty url
		
		////////// Grid cols
		_configureGridCols();
	}
	public <F extends VaadinUrlFormBase<V>,
			W extends VaadinUrlFormPopUpBase<V,F>>  VaadinUrlGridBase(final UII18NService i18n,
		 	    											  		  final Factory<W> popUpFactory,
		 	    											  		  final Factory<V> viewObjFactory) {
		super(i18n,
			  // [url builder] popup factory
			  popUpFactory,
			  // [url builder]-associated [view object]
			  viewObjFactory);	// an empty url
		
		////////// Grid cols
		_configureGridCols();
	}
	protected void _configureGridCols() {
		////////// row:  >> url         |[edit] [delete]
		// the url
		this.addColumn(V::getUrlAsHtmlLink,new HtmlRenderer())
			.setExpandRatio(1)
			.setId("url");		// paint the link
		
		// the [edit] | [delete] buttons at the end of the row
    	this.addComponentColumn(viewObj -> {
									HorizontalLayout lyButtons = new HorizontalLayout(new VaadinGridButton(_i18n.getMessage("edit"),
																						 	  			   VaadinIcons.EDIT,
																						 	  			    e -> this.enterEdit(viewObj)),
																					  new VaadinGridButton(_i18n.getMessage("delete"),
																							 			   VaadinIcons.TRASH,
																							 			   e -> this.enterDelete(viewObj),
																							 			   VaadinValoTheme.NO_PADDING));
									lyButtons.setMargin(false);
									lyButtons.setSpacing(false);
									return lyButtons;
								})
    		.setId("editButtons")
    		.setResizable(false)
			.setExpandRatio(0);
    	this.setHeaderVisible(false);
    	this.addStylesToGrid(VaadinValoTheme.GRID_CELL_NO_BORDER);
	}
}