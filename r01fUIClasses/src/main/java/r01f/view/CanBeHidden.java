package r01f.view;

/**
 * Interface for view components that has the visibility trait
 * @see com.google.CanBeHidden.user.client.ui.HasVisibility
 */
public interface CanBeHidden {
	public void hide();
	public void display();
	public boolean isVisible();
	public boolean isHidden();
}
