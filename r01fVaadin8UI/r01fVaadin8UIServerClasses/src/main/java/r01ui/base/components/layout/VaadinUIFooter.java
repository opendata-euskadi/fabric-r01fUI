/**
 * 
 */
package r01ui.base.components.layout;


import com.vaadin.server.ThemeResource;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;

import lombok.experimental.Accessors;
import r01f.ui.i18n.UII18NService;
import r01f.ui.vaadin.view.VaadinViewI18NMessagesCanBeUpdated;

@Accessors(prefix="_")
public class VaadinUIFooter
	 extends CustomComponent
  implements VaadinViewI18NMessagesCanBeUpdated {
	
	private static final long serialVersionUID = 7929935024142336860L;
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinUIFooter() {
		super(new HorizontalLayout());
		
		this.getCompositionRoot().setSizeFull();
		this.getCompositionRoot().setMargin(false);
		this.getCompositionRoot().addStyleName("footer");

		Image logo = new Image("",
							   new ThemeResource("img/r01_base.png"));
		this.getCompositionRoot().addComponent(logo);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public HorizontalLayout getCompositionRoot() {
		return (HorizontalLayout)super.getCompositionRoot();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public void updateI18NMessages(final UII18NService i18n) {
		// TODO 
	}
}
