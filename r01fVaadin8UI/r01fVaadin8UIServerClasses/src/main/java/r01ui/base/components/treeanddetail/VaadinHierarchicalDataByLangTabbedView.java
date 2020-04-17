package r01ui.base.components.treeanddetail;

import java.util.Collection;

import r01f.locale.Language;
import r01f.ui.i18n.UII18NService;
import r01f.ui.vaadin.view.VaadinViewFactories.VaadinViewFactoryFrom;
import r01f.ui.viewobject.UIViewObjectByLanguage;
import r01f.ui.viewobject.UIViewObjectInLanguage;
import r01ui.base.components.layout.VaadinUILangHTabbedView;
import r01ui.base.components.tree.VaadinTreeData;

public abstract class VaadinHierarchicalDataByLangTabbedView<// the [view object] binded at the [tree] + [detail] component
															 // (this [view object] MIGHT BE the VIL [view object in language] BUT is easier if NOT)
													 		 VO extends UIViewObjectInLanguage
													   				  & VaadinHierarchicalDataViewObj<VO>,	
													 		 // the component used to edit [tree] + [detail]
													 		 WIL extends VaadinHierarchicalDataInLangComponentBase<VO,? extends VaadinHierarchicalDataInLangForm<VO>>,
												 			 // the [view obj] that contains lang dependent view objs (VIL)
													 		 // and the [view obj in lang] (the lang-dependent view obj)
												 			 VBL extends UIViewObjectByLanguage<VIL>,	
												 			 VIL extends UIViewObjectInLanguage> 			
	 		  extends VaadinUILangHTabbedView<VaadinTreeData<VO>,WIL,
	 										  VBL,VIL> {

	private static final long serialVersionUID = -2595802321796578664L;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	protected VaadinHierarchicalDataByLangTabbedView(final UII18NService i18n,
							   			   		  	 final Collection<Language> langs,
							   			   		  	 final VaadinViewFactoryFrom<Language,WIL> viewByLangFactory) {
		super(i18n,
			  langs,
			  viewByLangFactory); 
	}
}
