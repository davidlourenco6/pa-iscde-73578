package pt.iscte.pidesco.generator.internal;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;


public class GeneratorImpl {

	private static BundleContext context = Activator.getContext();
	private static ServiceReference<JavaEditorServices> serviceReference = context.getServiceReference(JavaEditorServices.class);
	private static JavaEditorServices javaEditorServices = context.getService(serviceReference);



	public List<String> getArgs(){
		
		File file = javaEditorServices.getOpenedFile();
		Visitor checker = new Visitor();
		JavaParser.parse(file, checker);
		return checker.getArgs();
	}
	
	public List<String> getTypes(){
		
		File file = javaEditorServices.getOpenedFile();
		Visitor checker = new Visitor();
		JavaParser.parse(file, checker);
		return checker.getTipo();
	}
	
	public void startAllGetters() {
	
		File file = javaEditorServices.getOpenedFile();
		Visitor checker = new Visitor();
		JavaParser.parse(file, checker);
		checker.generateGetter();
		System.out.println("-------------------------------------");
		
	}
	
	public void startAllSetters() {
		
		File file = javaEditorServices.getOpenedFile();
		Visitor checker = new Visitor();
		JavaParser.parse(file, checker);
		checker.generateSetter();
		System.out.println("-------------------------------------");
		
	}
	
	public void startSelectedSetter(int argID) {

		File file = javaEditorServices.getOpenedFile();
		Visitor checker = new Visitor();
		JavaParser.parse(file, checker);
		List<String> argslista = checker.getArgs();
		System.out.println(argslista.size());
		checker.setSelectedWord(argslista.get(argID));

		checker.generateSelectedSetter();
		System.out.println("-------------------------------------");
		
	}

	public void startSelectedGetter(int argID) {
		File file = javaEditorServices.getOpenedFile();
		Visitor checker = new Visitor();
		JavaParser.parse(file, checker);
		List<String> argslista = checker.getArgs();
		checker.setSelectedWord(argslista.get(argID));
		
		checker.generateSelectedGetter();
		System.out.println("-------------------------------------");
		
	}

}
