package r01f.ui.viewobject;

import r01f.locale.Language;

/**
 * An interface for [view objects] that can provide a summary in a given language
 * It's useful for [view objects] that are summarized in a [grid]
 */
@FunctionalInterface
public interface UIViewObjectHasSummaryByLanguage<S> {
	public S getSummaryIn(final Language lang);
}
