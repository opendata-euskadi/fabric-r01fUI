package r01f.view;

import java.util.Collection;

import r01f.brokenrules.BrokenRule;


/**
 * Interface for view components that can show a label
 * @param <L> label type
 */
public interface HasBrokenRules {
	/**
	 * Sets the broken rules text
	 * @param brokenRulesText
	 */
	public void setBrokenRules(final Collection<BrokenRule> brokenRulesText);
	/**
	 * @return the brokenRulesText
	 */
	public String getBrokenRules();
}
