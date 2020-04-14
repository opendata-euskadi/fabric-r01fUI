package r01ui.base.components.treeanddetail;

import java.util.EventListener;
import java.util.EventObject;

import com.vaadin.ui.Component;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.facets.HasLanguage;
import r01f.ui.vaadin.view.VaadinView;
import r01f.ui.vaadin.view.VaadinViewI18NMessagesCanBeUpdated;
import r01f.ui.viewobject.UIViewObjectInLanguage;
import r01ui.base.components.form.VaadinFormEditsViewObject;

/**
 * <pre>
 * 		++++++++++++++++++++++++++++++++++++++
 * 		+ +--------+ +---------------------+ +
 * 		+ | node   | |                     | +
 * 		+ |  +node | | D: Detail      <--------- This component
 * 		+ |  +node | |                     | +
 * 		+ |        | |                     | +
 * 		+ +--------+ +---------------------+ +
 * 		++++++++++++++++++++++++++++++++++++++
 * </pre>
 * @param <VO>
 */
public interface VaadinHierarchicalDataInLangForm<VO extends UIViewObjectInLanguage> 
	     extends VaadinView,
	    		 HasLanguage,
				 VaadinFormEditsViewObject<VO>, 		// the view uses vaadin ui binder
				 VaadinViewI18NMessagesCanBeUpdated {			// the view i18n messages can be updated {
/////////////////////////////////////////////////////////////////////////////////////////
//	PUBLIC INTERFACE                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Sets a listener on changes of the form-edited view obj
	 * @param listener
	 */
	public void setOnValueChangeEventListener(final VaadinHierarchicalDataInLangDetailViewObjEventListener<VO> listener);
/////////////////////////////////////////////////////////////////////////////////////////
//	EDITED OBJECT CHANGED                                                                        
/////////////////////////////////////////////////////////////////////////////////////////
	@Accessors(prefix="_")
	public static class VaadinHierarchicalDataInLangDetailViewObjChangeEvent<VO extends UIViewObjectInLanguage>
		 		extends EventObject {

		private static final long serialVersionUID = 188417081045453959L;
		
		@Getter private final VO _viewObject;
		
		public VaadinHierarchicalDataInLangDetailViewObjChangeEvent(final Component srcComponent,
													   				final VO viewObj) {
			super(srcComponent);
			_viewObject = viewObj;
		}
	}
	@FunctionalInterface
	public interface VaadinHierarchicalDataInLangDetailViewObjEventListener<VO extends UIViewObjectInLanguage> 
	         extends EventListener {
	
        public abstract void onViewObjChanged(final VaadinHierarchicalDataInLangDetailViewObjChangeEvent<VO> event);
	} 
	
}
