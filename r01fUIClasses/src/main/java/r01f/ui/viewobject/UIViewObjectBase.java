package r01f.ui.viewobject;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(prefix="_")
@RequiredArgsConstructor
public abstract class UIViewObjectBase<T> 
           implements UIViewObjectWrapped<T> {

	private static final long serialVersionUID = 5054336695644200270L;
/////////////////////////////////////////////////////////////////////////////////////////
// 	FIELDS 
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter protected final transient T _wrappedModelObject;
}
