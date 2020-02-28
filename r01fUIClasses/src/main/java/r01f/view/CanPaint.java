package r01f.view;

import r01f.guids.OID;


/**
 * Interface implemented by {@link ViewComponent} that can paint {@link ViewObject}
 */
public interface CanPaint<V extends CanBePainted>
		 extends ViewComponent {	// it's a ViewComponent that can paint ViewObject
	/**
	 * Paints a model object that can be painted
	 * @param nodes
	 */
	public void paint(final V modelObj);
	/**
	 * Paints a model object that can be painted over a previously painted model obj
	 * @param modelObj
	 */
	public void paintOverlay(final V modelObj);
	/**
	 * Retrieves any previously-stored info about the {@link ViewObject} at the {@link ViewComponent} (this {@link CanPaint} object)
	 * This info (maybe an {@link OID}) could be used to identify the {@link ViewObject}
	 * @return
	 */
	public V getPaintedObject();
}
