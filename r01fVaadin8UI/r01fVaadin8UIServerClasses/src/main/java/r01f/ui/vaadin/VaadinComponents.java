package r01f.ui.vaadin;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import r01f.util.types.collections.CollectionUtils;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class VaadinComponents {
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	public static void setStyleName(final String styleName,
						   			final AbstractComponent... components) {
		if (CollectionUtils.isNullOrEmpty(components)) return;
		for (AbstractComponent comp : components) {
			comp.setStyleName(styleName);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////
//	Wrapping text in grid row
/////////////////////////////////////////////////////////////////////////////////////////
	public static final HorizontalLayout wrapIntoHorizontalLayout(final String value) {
		HorizontalLayout outLy = new HorizontalLayout();
		outLy.setSizeFull();
		
		Label label = new Label();
		label.setContentMode(ContentMode.HTML);
		label.setValue(value);
		label.setSizeFull();
		label.setStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
		outLy.addComponent(label);
		return outLy;
	}
	public static final CssLayout wrapIntoCssLayout(final String value) {
		CssLayout outLy = new CssLayout();
		outLy.setSizeFull();
		
		Label label = new Label();
		label.setContentMode(ContentMode.HTML);
		label.setValue(value);
		label.setSizeFull();
		label.setStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
		outLy.addComponent(label);
		return outLy;
	}
}
