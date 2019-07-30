package r01f.ui.coremediator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.model.API;

@Accessors( prefix="_" )
@RequiredArgsConstructor
public abstract class UICOREMediatorBase<A extends API> 
           implements UICOREMediator {
/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter protected final transient A _api;
}
