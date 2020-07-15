package r01ui.base.components.treeanddetail;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(prefix="_")
@NoArgsConstructor @AllArgsConstructor
public class VaadinHierarchicalDataEditConfig 
  implements Serializable {
	
	private static final long serialVersionUID = -731843126622054309L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter @Setter private boolean _collection;
	@Getter @Setter private int _maxDepth;
	
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	public boolean isNOTCollection() {
		return !this.isCollection();
	}
}
