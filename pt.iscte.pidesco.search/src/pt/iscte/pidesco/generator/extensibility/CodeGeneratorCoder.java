package pt.iscte.pidesco.generator.extensibility;


import java.io.File;

import pt.iscte.pidesco.generator.internal.IClass;


/**
 * Represents a code generator that can be activated/deactivated in the generator.
 */
public interface CodeGeneratorCoder {

	/**
	 * @param recive the opened file
	 * @return a String to generate and write
	 */
	public String generate(IClass auxClass);
	
}
