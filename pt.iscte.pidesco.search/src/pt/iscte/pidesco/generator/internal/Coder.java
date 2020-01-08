package pt.iscte.pidesco.generator.internal;


import java.io.File;

import pt.iscte.pidesco.generator.extensibility.CodeGeneratorCoder;

public class Coder implements CodeGeneratorCoder {


	@Override
	public String generate(IClass auxClass) {
			
			return "public String toString() { \n return [argsList=" + auxClass.getArgsList() + ", typesList=" + auxClass.getTypesList() + "];\n}";
			
	
	}
	
	
	
	
}
