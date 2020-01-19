package r01ui.base.components.button;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Composite;
import com.vaadin.ui.HorizontalLayout;

import lombok.experimental.Accessors;
import r01f.ui.i18n.UII18NService;

/**
 * A layout of [accept] [cancel] and [delete] buttons like
 * <pre>
 * 		+++++++++++++++++++++++++++++++++++++++++++++
 * 		+ [Delete]                [Cancel] [Accept] +
 * 		+++++++++++++++++++++++++++++++++++++++++++++
 * </pre>
 */
@Accessors( prefix="_" )
public class VaadinAcceptCancelDeleteButtons
	 extends Composite {
	
	private static final long serialVersionUID = 5892967547409836937L;
/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	private final Button _btnAccept = new Button();
	private final Button _btnCancel = new Button();
	private final Button _btnDelete = new Button();
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public VaadinAcceptCancelDeleteButtons( final UII18NService i18n ) {
		super.setSizeFull();
		
		////////// Create components
		// accept
		_btnAccept.setCaption(i18n.getMessage("save"));
		_btnAccept.setStyleName("primary");
		
		// cancel
		_btnCancel.setCaption(i18n.getMessage("cancel"));
		
		// delete
		_btnDelete.setCaption(i18n.getMessage("delete"));
		_btnDelete.setStyleName("danger");
		
		////////// layout
		HorizontalLayout hly = new HorizontalLayout(_btnDelete, 
													_btnCancel,
													_btnAccept);
		hly.setComponentAlignment(_btnDelete,Alignment.BOTTOM_LEFT);
		hly.setComponentAlignment(_btnAccept,Alignment.BOTTOM_RIGHT);
		hly.setComponentAlignment(_btnCancel,Alignment.BOTTOM_RIGHT);
		
		////////// composition
		this.setCompositionRoot(hly);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public void addCancelButtonClickListner(final ClickListener clickListener) {
		_btnCancel.addClickListener(clickListener);
	}
	public void addAcceptButtonClickListner(final ClickListener clickListener) {
		_btnAccept.addClickListener(clickListener);
	}
	public void addDeleteButtonClickListner(final ClickListener clickListener) {
		_btnDelete.addClickListener(clickListener);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public void setCreatingNewRecordStatus() {
		_btnDelete.setVisible(false);
	}
	public void setEditingExistingRecordStatus() {
		_btnDelete.setVisible(true);
	}
}
