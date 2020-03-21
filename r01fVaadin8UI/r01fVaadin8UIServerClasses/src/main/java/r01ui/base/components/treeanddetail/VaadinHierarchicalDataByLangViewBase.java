package r01ui.base.components.treeanddetail;

import java.util.Collection;
import java.util.function.Function;

import com.vaadin.data.Binder;
import com.vaadin.ui.CustomComponent;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.facets.LangInDependentNamed.HasLangInDependentNamedFacet;
import r01f.locale.Language;
import r01f.patterns.Transfer;
import r01f.ui.i18n.UII18NService;
import r01f.ui.vaadin.view.VaadinView;
import r01f.ui.vaadin.view.VaadinViewFactories.VaadinViewFactoryFrom;
import r01f.ui.vaadin.view.VaadinViewI18NMessagesCanBeUpdated;
import r01f.ui.viewobject.UIViewObjectByLanguage;
import r01f.ui.viewobject.UIViewObjectInLanguage;
import r01ui.base.components.form.VaadinFormBindings.VaadinFormHasVaadinUIBinder;
import r01ui.base.components.tree.VaadinTreeData;

/**
 * <pre>
 * 		+++++++++++++++++++++++++++++++++++++++++++++++++++++
 * 		+ |es| [eu] [en]									+ 
 * 		+ |  +--------------------------------------------+ +
 * 		+ |											      | +
 * 		+ |												  | +
 * 		+ |	[WIL: in lang view   						  | +
 * 		+ |												  | +
 * 		+ |												  | +
 * 		+ +-----------------------------------------------+ +
 * 		+++++++++++++++++++++++++++++++++++++++++++++++++++++
 * </pre>
 *
 * @param <VBL>
 * @param <IL>
 * @param <VIL>
 * @param <WIL>
 */
@Accessors(prefix="_")
public abstract class VaadinHierarchicalDataByLangViewBase<// by lang view object & in-lang view object shown in the [detail] view 
														   VBL extends UIViewObjectByLanguage<VIL>,		// by-lang view obj
														   VIL extends UIViewObjectInLanguage,			// lang dependent (in a lang)
														   // the component used to edit [tree] + [detail]
														   // the [view object] binded at the [tree] + [detail] component
														   // (this [view object] MIGHT BE the VIL [view object in language] BUT is easier if NOT)
														   VO extends UIViewObjectInLanguage
														   			& HasLangInDependentNamedFacet,
														   WIL extends VaadinHierarchicalDataInLangViewBase<VO,? extends VaadinHierarchicalDataInLangDetailView<VO>>>	
	 		  extends CustomComponent 
	 	   implements VaadinView,
  			 		  VaadinViewI18NMessagesCanBeUpdated,
  			 		  VaadinFormHasVaadinUIBinder<VBL> {

	private static final long serialVersionUID = -8652414236006812625L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FILEDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final VaadinHierarchicalDataByLangTabbedView<VO,WIL,
														 		 VBL,VIL> _langTabbedView;
	private final VaadinViewFactoryFrom<Language,WIL> _inLangViewFactory;			// the view inside each tab
	
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinHierarchicalDataByLangViewBase(final UII18NService i18n, 
												final Collection<Language> portalAvailableLangs,
												final VaadinViewFactoryFrom<Language,WIL> inLangDetailViewFactory,
												// transform 
												final Function<VIL,VaadinTreeData<VO>> viewObjInLangToVaadinTreeData,
												final Transfer<VaadinTreeData<VO>,VIL> vaadinTreeDataToViewObjInLang) {
		_inLangViewFactory = inLangDetailViewFactory;
		_langTabbedView = new VaadinHierarchicalDataByLangTabbedView<VO,WIL,
														 			 VBL,VIL>(i18n, 
														 					  portalAvailableLangs,
														 					  _inLangViewFactory) {
									private static final long serialVersionUID = 3240348896255554839L;
									
									@Override
									public VaadinTreeData<VO> transformViewObjInLangToBindedObj(final VIL viewObjInLang) {
										// Although the usual case is that the view-binded object (of type D) is the [view object] of type VIL
										// (D == VIL), there are circumstances where the view-binded object is NO of type VIL (D != VIL)      
										// ... so when binding the [view object] to the [ui controls] the [view object] of type VIL           
										// MUST be transformed to D  
										return viewObjInLangToVaadinTreeData.apply(viewObjInLang);
									}
									@Override
									public void copyBindedObjDataToViewObjectInLang(final VaadinTreeData<VO> bindedViewObjInLang, final VIL viewObjInLang) {
										// Although the usual case is that the view-binded object (of type D) is the [view object] of type VIL
										// (D == VIL), there are circumstances where the view-binded object is NO of type VIL (D != VIL)      
										// ... so when binding the [view object] to the [ui controls] the [view object] of type VIL           
										// MUST be transformed to D
										vaadinTreeDataToViewObjInLang.transfer(bindedViewObjInLang,viewObjInLang);
									}
						     };
		// build the ui
		this.setCompositionRoot(_langTabbedView);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	VIEW OBJECT -> UI CONTROLS
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void bindUIControlsTo(final VBL byLangViewObj) {
		_langTabbedView.bindUIControlsTo(byLangViewObj);		// tell the lang tabbed view to bind 
	}
	@Override
	public void readUIControlsFrom(final VBL byLangViewObj) {
		_langTabbedView.readUIControlsFrom(byLangViewObj);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	UI CONTROL -> VIEW OBJECT
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public VBL getViewObject() {
		return _langTabbedView.getViewObject();
	}
	@Override
	public boolean writeIfValidFromUIControlsTo(final VBL byLangViewObj) {
		boolean valid = _langTabbedView.writeIfValidFromUIControlsTo(byLangViewObj);
		
		return valid;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	BINDER ACCESS
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public Binder<VBL> getVaadinUIBinder() {
		return _langTabbedView.getVaadinUIBinder();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void updateI18NMessages(final UII18NService i18n) {
		// TODO update i18n
	}
}
