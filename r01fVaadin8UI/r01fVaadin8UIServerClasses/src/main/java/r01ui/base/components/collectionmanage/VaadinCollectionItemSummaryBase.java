package r01ui.base.components.collectionmanage;

import com.vaadin.ui.Composite;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;

import r01f.locale.Language;
import r01f.ui.i18n.UII18NService;
import r01f.ui.viewobject.UIViewObject;
import r01ui.base.components.collectionmanage.VaadinCollectionManageComponent.VaadinCollectionItemSummaryClickEvent;
import r01ui.base.components.collectionmanage.VaadinCollectionManageComponent.VaadinCollectionItemSummaryClickListener;
import r01ui.base.components.collectionmanage.VaadinCollectionManageComponent.VaadinCollectionItemSummaryComponent;

public abstract class VaadinCollectionItemSummaryBase<V extends UIViewObject>
			  extends Composite 
		   implements VaadinCollectionItemSummaryComponent<V> {
	
	private static final long serialVersionUID = -3822406184316106460L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	protected final Language _lang;
	protected final Label _lblSummary;
	
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public VaadinCollectionItemSummaryBase(final UII18NService i18n) {
		super();
		_lang = i18n.getCurrentLanguage();
		
		_lblSummary = new Label();
		
		_lblSummary.addStyleName(ValoTheme.LABEL_H3);
		_lblSummary.addStyleName(ValoTheme.LABEL_BOLD);
		
		CssLayout hly = new CssLayout(_lblSummary);	// in order to capture the click event the label must be wrapped in a layout
		hly.setWidthFull();
		
		this.setCompositionRoot(hly);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	LISTENER
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public void addItemClickListener(final VaadinCollectionItemSummaryClickListener clickListener) {
		// when the button is clicked just fire the evnet
		CssLayout hly = (CssLayout)this.getCompositionRoot();
		hly.addLayoutClickListener(clickEvent -> clickListener.onItemClicked(new VaadinCollectionItemSummaryClickEvent()));
	}
}
