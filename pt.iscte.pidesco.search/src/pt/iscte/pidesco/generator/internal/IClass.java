package pt.iscte.pidesco.generator.internal;

import java.util.List;

public class IClass {
	
	List<String> argsList;
	List<String> typesList;
	String className;
	
	public IClass(List<String> argsList, List<String> typesList, String className) {
		this.argsList = argsList;
		this.typesList = typesList;
		this.className=className;
	}


	public List<String> getArgsList() {
		return argsList;
	}

	public List<String> getTypesList() {
		return typesList;
	}

	public String getClassName() {
		return className;
	}

	@Override
	public String toString() {
		return "IClass [argsList=" + argsList + ", typesList=" + typesList + "]";
	}


	
	

}
