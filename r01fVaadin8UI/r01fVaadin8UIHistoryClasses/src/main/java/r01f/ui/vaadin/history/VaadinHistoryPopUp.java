package r01f.ui.vaadin.history;

import java.util.Collection;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import r01f.ui.i18n.UII18NService;
import r01f.ui.vaadin.view.VaadinViewI18NMessagesCanBeUpdated;

/**
 * A pop-up window that shows [history entries]
 * <pre>
 * 		+==========================================================+
 * 		| User history entries about XXXX        				   |
 * 		|														   |
 * 		|  +----------+-----------+-------------+---------------+  |
 * 		|  | When     | What      | About       | Who           |  |
 * 		|  +----------+-----------+-------------+---------------+  |
 * 		|  |          |           |             |               |  |
 * 		|  |          |           |             |               |  |
 * 		|  |          |           |             |               |  |
 * 		|  |          |           |             |               |  |
 * 		|  |          |           |             |               |  |
 * 		|  |          |           |             |               |  |
 * 		|  +----------+-----------+-------------+---------------+  |
 * 		|														   |
 * 		|												  [Close]  |
 * 		+==========================================================+
 * </pre>
 * Usage:
 * <pre class='brush:java'>
 * 		VaadinHistoryPopUp<MyHistoryEntry> popUp = new VaadinHistoryPopUp<>(i18n);
 * 		historyPresenter.onUserHistoryEntriesLoadRequested(objOid,i18n.getCurrentLanguage(),
 * 														   viewEntries -> popUp.setHistoryItems(gridItems));
 * </pre>
 */
public class VaadinHistoryPopUp<V extends VaadinViewHistoryEntry<?,?,?>>
	 extends Window 
  implements VaadinViewI18NMessagesCanBeUpdated {

	private static final long serialVersionUID = -6180288216757523555L;
/////////////////////////////////////////////////////////////////////////////////////////
//	SERVICES
/////////////////////////////////////////////////////////////////////////////////////////
	private final transient UII18NService _i18n;
/////////////////////////////////////////////////////////////////////////////////////////
//	UI FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final Label _lblAbout;
	private final VaadinHistoryGrid<V> _grid;
	private final Button _btnClose;
/////////////////////////////////////////////////////////////////////////////////////////
//	STATE
/////////////////////////////////////////////////////////////////////////////////////////
	private String _aboutDetails;
	
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinHistoryPopUp(final UII18NService i18n) {
		////////// I18N
		_i18n = i18n;
		
		////////// UI
		_lblAbout = new Label();
		_grid = new VaadinHistoryGrid<>(i18n);
		_btnClose = new Button();
		
		////////// Layout
		VerticalLayout vly = new VerticalLayout(_lblAbout,
												_grid,
												_btnClose);
		vly.setComponentAlignment(_btnClose,Alignment.BOTTOM_RIGHT);
		
		this.setContent(vly);
		this.setWidth(80,Unit.PERCENTAGE);
		this.setHeight(60,Unit.PERCENTAGE);
		
		////////// I18n
		this.updateI18NMessages(i18n);
		
		////////// Behavior
		_setBehavior();
	}
	private void _setBehavior() {
		_btnClose.addClickListener(clickEvent -> this.close());		// just close the window
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	SET ITEMS
/////////////////////////////////////////////////////////////////////////////////////////
	public void showHistoryEntries(final String aboutDetails,
								   final Collection<V> gridItems) {
		// label & grid
		_aboutDetails = aboutDetails;
		_lblAbout.setCaption(_i18n.getMessage("history.grid.about",_aboutDetails));
		_grid.setHistoryEntries(gridItems);
		
		// show popup
		UI.getCurrent()
		  .addWindow(this);
		this.center();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	I18N
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void updateI18NMessages(final UII18NService i18n) {
		_lblAbout.setCaption(_i18n.getMessage("history.grid.about",_aboutDetails));
		_grid.updateI18NMessages(i18n);
		_btnClose.setCaption(i18n.getMessage("close"));
	}
}
