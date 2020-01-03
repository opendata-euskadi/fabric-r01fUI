/**
 * 
 */
package r01ui.base.components.layout;


import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.ui.i18n.UII18NService;
import r01f.ui.vaadin.view.VaadinViewI18NMessagesCanBeUpdated;

/**
 * A vaadin's panel that contains a [header], a [view container] and a [footer]
 * <pre>
 * 		+++++++++++++++++++++++++++++++++++++++++++++++++++
 * 		+ [ H E A D E R ]                                 +
 * 		+												  +
 * 		+ +---------------------------------------------+ +
 * 		+ |												| +
 * 		+ |												| +
 * 		+ |	Component container where [vaadin views]	| +
 * 		+ |	are show									| +
 * 		+ |												| +
 * 		+ |												| +
 * 		+ |												| +
 * 		+ +---------------------------------------------+ +
 * 		+												  +
 * 		+ [ F O O T E R] 								  +
 * 		+++++++++++++++++++++++++++++++++++++++++++++++++++
 * </pre>
 */
@Accessors(prefix="_")
public class VaadinViewDisplayWithHeaderAndFooter
	 extends VaadinViewDisplay {
	
	private static final long serialVersionUID = -3921436524914595338L;
/////////////////////////////////////////////////////////////////////////////////////////
// 	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////  
	/**
	 * The header layout
	 */
	@Getter private final VaadinUIHeader _header;
	/**
	 * The footer layout
	 */
	@Getter private final VaadinUIFooter _footer;
/////////////////////////////////////////////////////////////////////////////////////////
// 	CONSTRUCTORS
/////////////////////////////////////////////////////////////////////////////////////////    
	public VaadinViewDisplayWithHeaderAndFooter(final VaadinUIHeader header) {
		super();
		
		_header = header;
		_footer = new VaadinUIFooter();
		
		// BEWARE! the [view display] container is added at the super-type
		this.getContent().addComponentAsFirst(_header);
		this.getContent().addComponent(_footer);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public void updateI18NMessages(final UII18NService i18n) {
		super.updateI18NMessages(i18n);
		
		// update the [header] & [footer] messages
		_header.updateI18NMessages(i18n);
		if (_viewDisplay instanceof VaadinViewI18NMessagesCanBeUpdated) {
			VaadinViewI18NMessagesCanBeUpdated i18nAwareView = (VaadinViewI18NMessagesCanBeUpdated)_viewDisplay;
			i18nAwareView.updateI18NMessages(i18n);
		}
		_footer.updateI18NMessages(i18n);
	}
}
