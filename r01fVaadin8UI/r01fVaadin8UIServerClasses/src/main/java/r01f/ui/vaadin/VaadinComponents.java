package r01f.ui.vaadin;

import com.vaadin.ui.AbstractComponent;

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
}
