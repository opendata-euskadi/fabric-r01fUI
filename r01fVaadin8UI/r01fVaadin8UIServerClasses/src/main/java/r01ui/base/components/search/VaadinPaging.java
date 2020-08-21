package r01ui.base.components.search;

import java.io.Serializable;
import java.util.Arrays;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

import r01f.model.search.SearchResults;
import r01f.types.pager.Paging;
import r01f.ui.i18n.UII18NService;
import r01f.ui.vaadin.styles.VaadinValoTheme;
import r01f.ui.vaadin.view.VaadinViewI18NMessagesCanBeUpdated;

/**
 * Component to paging results,contains navigation buttons an fire an event for searching, 
 * <i>first</i>,<i>last</i>,<i>previus</i>,<i>next</i> buttons.
 */
public class VaadinPaging 
	 extends HorizontalLayout
  implements VaadinViewI18NMessagesCanBeUpdated {

	private static final long serialVersionUID = -9193405814847745953L;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTANTS
/////////////////////////////////////////////////////////////////////////////////////////
	private static final String[] BUTTON_STYLES = new String[] {
																VaadinValoTheme.LABEL_COLORED,
																VaadinValoTheme.LABEL_BOLD
																};
/////////////////////////////////////////////////////////////////////////////////////////
//	UI COMPONENTS
/////////////////////////////////////////////////////////////////////////////////////////
		// << < Page 5 / 10 >  >>
	private final Button _btnFirst = new Button(VaadinIcons.ANGLE_DOUBLE_LEFT);
	private final Button _btnPrevious = new Button(VaadinIcons.ANGLE_LEFT);
	private final Label _lblPage = new Label();
	private final Label _lblFrom = new Label();
	private final Label _lblSlash = new Label(" / ");
	private final Label _lblTo = new Label();
	private final Button _btnNext = new Button(VaadinIcons.ANGLE_RIGHT);
	private final Button _btnLast = new Button(VaadinIcons.ANGLE_DOUBLE_RIGHT);
	
	// Show [ 5   \/] 245 results | 25 - 30 
	private final Label _lblShowItemNumPerPage = new Label();	
	private final ComboBox<Integer> _cmbShowItemNum = new ComboBox<>(null,		// no caption
																	 Arrays.asList(new Integer[] {10,20,30}));
	private final Label _lblNumber = new Label();
	private final Label _lblResults = new Label();
	private final Label _lblVerticalBar = new Label(" | ");
	private final Label _lblItemFrom = new Label();
	private final Label _lblDash = new Label(" - ");
	private final Label _lblItemTo = new Label();

/////////////////////////////////////////////////////////////////////////////////////////
//	EVENT LISTENER
/////////////////////////////////////////////////////////////////////////////////////////	
	private R01UIPagingEventListener _pagingListener;
	
/////////////////////////////////////////////////////////////////////////////////////////
//  STATE (avoid as much as possible)
/////////////////////////////////////////////////////////////////////////////////////////		
	private Paging _paging;
	
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinPaging(final UII18NService i18n) {						
		////////// Style
		_btnFirst.addStyleNames(VaadinValoTheme.BUTTON_TINY);
		_btnPrevious.addStyleNames(VaadinValoTheme.BUTTON_TINY);
		_lblPage.addStyleNames(BUTTON_STYLES);
		_lblFrom.addStyleNames(BUTTON_STYLES);
		_lblTo.addStyleNames(BUTTON_STYLES);
		_lblSlash.addStyleNames(BUTTON_STYLES);
		_btnLast.addStyleNames(VaadinValoTheme.BUTTON_TINY);
		_btnNext.addStyleNames(VaadinValoTheme.BUTTON_TINY);
		
		_lblShowItemNumPerPage.addStyleNames(BUTTON_STYLES);
		_cmbShowItemNum.setSelectedItem(SearchResults.DEFAULT_PAGE_SIZE);
		_cmbShowItemNum.setEmptySelectionAllowed(false);
		_cmbShowItemNum.addStyleName(VaadinValoTheme.COMBOBOX_TINY);
		_cmbShowItemNum.setWidth(70, Unit.PIXELS);
		_lblItemFrom.addStyleNames(BUTTON_STYLES);
		_lblDash.addStyleNames(BUTTON_STYLES);
		_lblItemTo.addStyleNames(BUTTON_STYLES);
		_lblVerticalBar.addStyleNames(BUTTON_STYLES);
		_lblNumber.addStyleNames(BUTTON_STYLES);
		_lblResults.addStyleNames(BUTTON_STYLES);
	
		////////// Layout
		// << < Page 5 / 10 >  >> 
		HorizontalLayout lyBar = new HorizontalLayout();
		lyBar.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		lyBar.addComponents(_btnFirst,_btnPrevious,_lblPage,_lblFrom,_lblSlash,_lblTo,_btnNext, _btnLast);
		
		// 245 results showing / page [ 5   \/] | 25 - 30
		HorizontalLayout lyShowItemNum = new HorizontalLayout();
		lyBar.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
		lyShowItemNum.addComponents(_lblShowItemNumPerPage,_cmbShowItemNum,_lblNumber,_lblResults,_lblVerticalBar,_lblItemFrom,_lblDash,_lblItemTo);


		this.addComponents(lyBar, lyShowItemNum);
		this.setComponentAlignment(lyShowItemNum,Alignment.TOP_RIGHT);
		this.setComponentAlignment(lyBar,Alignment.TOP_LEFT);
		
		this.setMargin(true);
		this.setSizeFull();

		////////// I18N
		this.updateI18NMessages(i18n);
		
		////////// Behavior
		_setBehavior();
	}
	private void _setBehavior() {
		_cmbShowItemNum.addValueChangeListener(valChangeEvent -> {
													// tell the parent view that the number of items / page has changed
													// ... exec the query again
													if (_pagingListener != null) _pagingListener.onPageRequested(0,_cmbShowItemNum.getValue());
											   });
		_btnLast.addClickListener(clickEvent -> {
										if (_pagingListener != null) _pagingListener.onPageRequested(_paging.getLastPageFirstItem(),_paging.getPageItems());
								  });
		_btnFirst.addClickListener(clickEvent -> {
										if (_pagingListener != null) _pagingListener.onPageRequested(0,_paging.getPageItems());
								   });
		_btnPrevious.addClickListener(clickEvent -> {											 
											if (_pagingListener != null) _pagingListener.onPageRequested(_paging.getPreviousPageFirstItem(),_paging.getPageItems());
									  });
		_btnNext.addClickListener(clickEvent -> {
										if (!_paging.hasNextPage()) return;			// it's the last page
										if (_pagingListener != null)  _pagingListener.onPageRequested(_paging.getNextPageFirstItem(),
																 									  _paging.getPageItems());
								  });
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  PUBLIC METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	public void init(final int pageSize,
    			  	 final int startPosition,final int totalItems) {
		// do NOT show if no results
		this.setVisible(totalItems > 0);
		if (totalItems <= 0) return;
		
		// there's results
		_lblFrom.setValue("1");

		// Paging		
		_paging = new Paging(pageSize, 
							 SearchResults.DEFAULT_NAVBAR_WINDOW_SIZE,
							 totalItems, 	// total items count
							 1); 		// start with the first page
		_paging.goToPageWitItem(startPosition + 1); // ... but go to the page with the given number
		
		// Set the number of items / page
		_lblFrom.setValue(Integer.toString(_paging.getCurrentPage()));
		_lblTo.setValue(Integer.toString(_paging.getPageCount()));		
		_lblNumber.setValue(Integer.toString(_paging.getItemCount()));
		_lblItemFrom.setValue(Integer.toString(_paging.getCurrentPageFirstItem()+1));
		_lblItemTo.setValue(Integer.toString(_paging.getCurrentPageLastItem()));
		
		_btnFirst.setEnabled(_paging.hasPrevPage());
		_btnPrevious.setEnabled(_paging.hasPrevPage());
		
		_btnLast.setEnabled(_paging.hasNextPage());
		_btnNext.setEnabled(_paging.hasNextPage());
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	I18N
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public void updateI18NMessages(final UII18NService i18n) {
		_btnFirst.setDescription(i18n.getMessage("search.nav.first"));
		_btnPrevious.setDescription(i18n.getMessage("search.nav.prev"));
		_btnNext.setDescription(i18n.getMessage("search.nav.next"));
		_btnLast.setDescription(i18n.getMessage("search.nav.last"));
		
		_lblPage.setValue(i18n.getMessage("search.nav.page") + ": ");
		
		
		_lblShowItemNumPerPage.setValue(i18n.getMessage("search.nav.page.show-num-of-items") + ": ");
		_lblResults.setValue(i18n.getMessage("search.nav.results"));		
	}
/////////////////////////////////////////////////////////////////////////////////////////
// 	EVENT LISTENER  
/////////////////////////////////////////////////////////////////////////////////////////
	public void setPagingListener(final R01UIPagingEventListener listener) {
		_pagingListener = listener;
	}
	public interface R01UIPagingEventListener 
	     	 extends Serializable {	
		void onPageRequested(int firstItemNum,int numItems);
	}	
}