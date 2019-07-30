package r01f.ui.vaadin.coremediator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.model.API;

@Accessors( prefix="_" )
@RequiredArgsConstructor
public abstract class VaadinCOREMediatorBase<A extends API> 
           implements VaadinCOREMediator {
/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter protected final transient A _api;
}
