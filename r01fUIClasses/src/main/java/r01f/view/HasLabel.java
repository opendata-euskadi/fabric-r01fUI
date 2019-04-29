package r01f.view;

/**
 * Interface for view components that can show a label
 * @param <L> label type
 */
public interface HasLabel<L> {
	/**
	 * Sets the label
	 * @param label
	 */
	public void setLabel(final L label);
	/**
	 * @return the label
	 */
	public L getLabel();
}
