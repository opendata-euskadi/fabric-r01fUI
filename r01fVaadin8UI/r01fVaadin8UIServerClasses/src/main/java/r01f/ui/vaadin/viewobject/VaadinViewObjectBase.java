package r01f.ui.vaadin.viewobject;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.model.ModelObject;

@Accessors(prefix="_")
@RequiredArgsConstructor
public abstract class VaadinViewObjectBase<T extends ModelObject> 
           implements VaadinViewObjectWrapped<T> {

	private static final long serialVersionUID = 5054336695644200270L;
/////////////////////////////////////////////////////////////////////////////////////////
// 	FIELDS 
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter protected final transient T _wrappedModelObject;
}
