package r01ui.base.components;

import java.io.Serializable;

import com.google.common.collect.Lists;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;

import r01f.model.search.SearchResults;
import r01f.types.pager.Paging;
import r01f.ui.i18n.UII18NService;
import r01f.ui.vaadin.view.VaadinViewI18NMessagesCanBeUpdated;

/**
 * Creates a paging component like:
 * <pre>
 *       ---------------------------------     ----------------------------------------------------- 
 *       |  show [    \/] results of 243 |     |    |  [ |< ] [ << ]     10 / 20    [ >> ] [ >| ]  | 
 *       ------------------------------- -     ------------------------------------------------ ----
 * </pre>
 */
public class VaadinPagingComponent 
	 extends HorizontalLayout
  implements VaadinViewI18NMessagesCanBeUpdated {

	private static final long serialVersionUID = -9193405814847745953L;
/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////		
	private Paging _paging;
	
	//  [|<]  [<<]  10 / 20  [>>]  [>|]
	private final Label _lblFrom = new Label();
	private final Label _lblTo = new Label();		
	private final Button _btnLast;
	private final Button _btnFirst;
	private final Button _btnPrevious;
	private final Button _btnNext;
	
	// Show: [      \/] results of 234
	private final Label _lblShowText;
	private final ComboBox<Integer> _cbPageSize;
	private final Label _lblResultsText;
	private final Label _lblTotalItemCount = new Label();

	
	private VaadinPagingListener _pagingListener;
	
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinPagingComponent() {						
		//    ------------------------------------------------
		//    |  [ |< ] [ << ]     10 / 20    [ >> ] [ >| ]  |
		//    ------------------------------------------------
		_btnFirst = new Button(VaadinIcons.ANGLE_DOUBLE_LEFT);
		_btnFirst.setDescription("Start");
		_btnFirst.addClickListener(event -> {
												 _paging.goToFirstPage();
												 _updateUIStatusFromPaging();
												 _pagingListener.goTo(_paging.getCurrentPageFirstItem(),// goto page first item	
																	  _paging.getPageItems());			// and get x items
											});
		
		_btnPrevious = new Button(VaadinIcons.ANGLE_LEFT);
		_btnPrevious.setDescription("Previous");
		_btnPrevious.addClickListener(event -> {
												_paging.goToPrevPage();
												_updateUIStatusFromPaging();
												_pagingListener.goTo(_paging.getCurrentPageFirstItem(),	// goto page first item
																     _paging.getPageItems());			// and get x items
											   });
		
		_btnNext = new Button(VaadinIcons.ANGLE_RIGHT);
		_btnNext.setDescription("Next");
		_btnNext.addClickListener(event -> {
												_paging.goToNextPage();
												_updateUIStatusFromPaging();
												_pagingListener.goTo(_paging.getCurrentPageFirstItem(),	// goto page first item
																	 _paging.getPageItems());			// and get x items
										   });
		
		_btnLast = new Button(VaadinIcons.ANGLE_DOUBLE_RIGHT);
		_btnLast.setDescription("End");
		_btnLast.addClickListener(event -> {
												_paging.goToLastPage();
												_updateUIStatusFromPaging();
												_pagingListener.goTo(_paging.getCurrentPageFirstItem(),	// goto page first item
																	 _paging.getPageItems());			// and get x items
										   });
		
		// create layout
		final HorizontalLayout hlayoutForPager = new HorizontalLayout(_btnFirst,_btnPrevious,				// [0] first  [1] previous
																      _lblFrom,new Label(" / "),_lblTo,		// [2] from [3] / [4] to
																      _btnNext,_btnLast);					// [5] next  [6] last
		// align
		hlayoutForPager.setComponentAlignment(hlayoutForPager.getComponent(0),Alignment.MIDDLE_LEFT);		// first
		hlayoutForPager.setComponentAlignment(hlayoutForPager.getComponent(1),Alignment.MIDDLE_LEFT);		// previous
		hlayoutForPager.setComponentAlignment(hlayoutForPager.getComponent(2),Alignment.MIDDLE_CENTER);		// from
		hlayoutForPager.setComponentAlignment(hlayoutForPager.getComponent(3),Alignment.MIDDLE_CENTER);		// /
		hlayoutForPager.setComponentAlignment(hlayoutForPager.getComponent(4),Alignment.MIDDLE_CENTER);		// to
		hlayoutForPager.setComponentAlignment(hlayoutForPager.getComponent(5),Alignment.MIDDLE_RIGHT);		// next
		hlayoutForPager.setComponentAlignment(hlayoutForPager.getComponent(6),Alignment.MIDDLE_RIGHT);		// last
		// style
		_styleComponents(hlayoutForPager);
		
		//  --------------------------------------
		//  |  show [    \/] results of 243      |
		//  --------------------------------------
		_lblShowText = new Label("Show:");
		_cbPageSize = new ComboBox<>(null,
									 Lists.newArrayList(10,20,30));
		_cbPageSize.setEmptySelectionAllowed(false);
		_cbPageSize.setValue(10);
		_cbPageSize.addValueChangeListener(event -> {
														// [1] - Rebuild the paging with the new page size
														int newPageSize = _cbPageSize.getValue();
														_paging = new Paging(newPageSize,_paging.getNavBarWindowItems(),	// 10 items / page, 5 pages / window
																			 _paging.getItemCount(),						// total items
																			 1);											// current page
														// [2] - Update the ui
														_updateUIStatusFromPaging();
														// [3] - Signal the listener
														_pagingListener.goTo(_paging.getCurrentPage(),	// goto page first item
																		 	 _cbPageSize.getValue());	// and get x items
													});
		_lblResultsText = new Label("results");
		
		final HorizontalLayout hLayoutForPageSize = new HorizontalLayout(_lblShowText,			
																   		 _cbPageSize, 			
																   		 _lblResultsText, 
																   		 _lblTotalItemCount);
		// align
		hLayoutForPageSize.setComponentAlignment(_lblShowText,Alignment.MIDDLE_LEFT);		
		hLayoutForPageSize.setComponentAlignment(_cbPageSize,Alignment.MIDDLE_LEFT);
		hLayoutForPageSize.setComponentAlignment(_lblResultsText,Alignment.MIDDLE_LEFT);
		hLayoutForPageSize.setComponentAlignment(_lblTotalItemCount,Alignment.MIDDLE_LEFT);
		// style
		_styleComponents(hLayoutForPageSize);
		
		
		//     ---------------------------------     ----------------------------------------------------- 
		//     |  show [    \/] results of 243 |     |    |  [ |< ] [ << ]     10 / 20    [ >> ] [ >| ]  | 
		//     ------------------------------- -     ------------------------------------------------ ----
		this.addComponents(hLayoutForPageSize,hlayoutForPager);
		this.setComponentAlignment(hLayoutForPageSize,Alignment.MIDDLE_LEFT);
		this.setComponentAlignment(hlayoutForPager,Alignment.MIDDLE_RIGHT);
		
		this.setMargin(true);
		this.setSizeFull();
	}
	private void _styleComponents(final Iterable<Component> comps) {
		for (Component comp : comps ) {
			if (comp instanceof Label) {				
				comp.addStyleNames(ValoTheme.LABEL_H4,ValoTheme.LABEL_COLORED,ValoTheme.LABEL_BOLD);
			} else if (comp instanceof Button) {
				comp.addStyleNames(ValoTheme.BUTTON_PRIMARY);
			}
		}
	}
	private void _updateUIStatusFromPaging() {
		_setPageNavigationButtonsStatus();
		_lblFrom.setValue(Integer.toString(_paging.getCurrentPage()));
		_lblTo.setValue(Integer.toString(_paging.getPageCount()));		
	}
	private void _setPageNavigationButtonsStatus() {
		// enable / disable buttons
		_btnFirst.setEnabled(_paging.hasPrevPage());
		_btnPrevious.setEnabled(_paging.hasPrevPage());
		
		_btnLast.setEnabled(_paging.hasNextPage());
		_btnNext.setEnabled(_paging.hasNextPage());
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  INIT
/////////////////////////////////////////////////////////////////////////////////////////
	public void init(final int newPageSize,
    			  	 final int currPage,
    			  	 final int numItems) {	
		// Paging		
		Paging paging = new Paging(newPageSize,SearchResults.DEFAULT_NAVBAR_WINDOW_SIZE,	// page size & pages window size
							 	   numItems, 	// total items count
							 	   1); 		// start with the first page
		paging.goToPage(currPage);
		this.init(paging);
	}
	public void init(final Paging paging) {
		_paging = paging;
		
		// Set the number of items / page
		_lblFrom.setValue(Integer.toString(_paging.getCurrentPage()));
		_lblTo.setValue(Integer.toString(_paging.getPageCount()));		
		_lblTotalItemCount.setValue(Integer.toString(_paging.getItemCount()));
				
		// enable / disable buttons
		_setPageNavigationButtonsStatus();

	}
/////////////////////////////////////////////////////////////////////////////////////////
//	LISTENER                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	public void addPagingListener(final VaadinPagingListener listener) {
		_pagingListener = listener;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public void updateI18NMessages(final UII18NService i18n) {
		_btnFirst.setDescription(i18n.getMessage("common.paging.first"));
		_btnPrevious.setDescription(i18n.getMessage("common.paging.prev"));
		_btnNext.setDescription(i18n.getMessage("common.paging.next"));
		_btnLast.setDescription(i18n.getMessage("common.paging.last"));
		
		_lblShowText.setCaption(i18n.getMessage("common.paging.show"));
		_lblResultsText.setCaption(i18n.getMessage("common.paging.items"));		
	}
	
/////////////////////////////////////////////////////////////////////////////////////////
// 	LISTENER  
/////////////////////////////////////////////////////////////////////////////////////////   	
	public interface VaadinPagingListener 
	     	 extends Serializable {	
		void goTo(int firstItemNum,
				  int numItems);    
	}	
}