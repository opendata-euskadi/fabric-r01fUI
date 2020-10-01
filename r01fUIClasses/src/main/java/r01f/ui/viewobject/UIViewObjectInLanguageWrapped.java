package r01f.ui.viewobject;

import java.util.Map;

import com.google.common.collect.Maps;

import r01f.locale.Language;
import r01f.patterns.Memoized;

public abstract class UIViewObjectInLanguageWrapped<T,
													L extends UIViewObjectInLanguage>
			  extends UIViewObjectWrappedBase<T>
	       implements UIViewObjectByLanguage<L> {

	private static final long serialVersionUID = -2605287394169257653L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	// used to create the view object in language
	private final transient Memoized<Map<Language,L>> _viewObjByLang;
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTORS
/////////////////////////////////////////////////////////////////////////////////////////
	public UIViewObjectInLanguageWrapped(final T obj,
										 final UIViewObjectInLanguageFactory<T,L> viewInLangFactory) {
		super(obj);

		// memoized the lang dependent view objs
		_viewObjByLang = Memoized.using(() -> {
									// create the lang-dependent view objects
									L esViewObj = viewInLangFactory.createViewObjectInLangFor(_wrappedModelObject,
																							  Language.SPANISH);
									L euViewObj = viewInLangFactory.createViewObjectInLangFor(_wrappedModelObject,
																							  Language.BASQUE);
									// put them into a map
									Map<Language,L> outMap = Maps.newHashMapWithExpectedSize(2);
									outMap.put(Language.SPANISH,esViewObj);
									outMap.put(Language.BASQUE,euViewObj);
									return outMap;
							});
	}
	protected interface UIViewObjectInLanguageFactory<T,L> {
		L createViewObjectInLangFor(final T obj,final Language lang);
	}
/////////////////////////////////////////////////////////////////////////////////////////
// 	LANG ACCESS
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public L getViewObjectFor(final Language lang) {
		return _viewObjByLang.get()
							 .get(lang);
	}
}
