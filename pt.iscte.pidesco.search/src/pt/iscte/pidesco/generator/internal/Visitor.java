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


public class Visitor extends ASTVisitor {
	
	private static BundleContext context = Activator.getContext();
	private static ServiceReference<JavaEditorServices> serviceReference = context.getServiceReference(JavaEditorServices.class);
	private static JavaEditorServices javaEditorServices = context.getService(serviceReference);
	List<String> argslista = new ArrayList<String>();
	List<String> argstipo = new ArrayList<String>();
	static String selectedWord;
	
	
	// visits attributes
	@Override
	public boolean visit(FieldDeclaration node) {
	
		// loop for several variables in the same declaration
		for(Object o : node.fragments()) {
			VariableDeclarationFragment var = (VariableDeclarationFragment) o;
			String name = var.getName().toString();
			argslista.add(name);
			argstipo.add(node.getType().toString());
		}
		
		return true; // false to avoid child VariableDeclarationFragment to be processed again
	}
	

	public List<String> getArgs(){
		
		
		return argslista;
	}
	
	public void setSelectedWord(String selectedWord) {
		this.selectedWord=selectedWord;
	}
	
	public List<String> getTipo(){
		
		
		return argstipo;
	}
	
	 void generateSelectedGetter() {
		System.out.println("Lista de args : " + argslista.size());
		for(int i = 0; i!=argslista.size(); i++) {
			
			if(argslista.get(i).equals(selectedWord)) {
				String obj ="\n	public ";
				obj = obj + argstipo.get(i) + " get" +argslista.get(i)+"{\n"
						+"		return "+ argslista.get(i) + ";\n	}\n"; 
				javaEditorServices.insertTextAtCursor(obj);
			}	
		}
		argstipo = new ArrayList<String>();
		argslista = new ArrayList<String>();
	}
	
	 void generateSelectedSetter() {
		 System.out.println(argslista.size());
		for(int i = 0; i!=argslista.size(); i++) {
			if(argslista.get(i).equals(selectedWord)) {
				String obj ="\n	public void ";
				obj = obj +"set" +argslista.get(i)+"("+argstipo.get(i)+ " " +argslista.get(i)+"){\n"
						+"		this."+ argslista.get(i) + "="+argslista.get(i)+";\n	}\n"; 
				javaEditorServices.insertTextAtCursor(obj);
			}
		}
		argstipo = new ArrayList<String>();
		argslista = new ArrayList<String>();
	}
	
	void generateGetter() {
		for(int i = 0; i!=argslista.size(); i++) {
			String obj ="\n	public ";
			obj = obj + argstipo.get(i) + " get" +argslista.get(i)+"{\n"
					+"		return "+ argslista.get(i) + "\n	}\n"; 
			javaEditorServices.insertTextAtCursor(obj);
		}
		argstipo = new ArrayList<String>();
		argslista = new ArrayList<String>();
	}
	
	 void generateSetter() {
		for(int i = 0; i!=argslista.size(); i++) {
			String obj ="\n	public void ";
			obj = obj +"set" +argslista.get(i)+"("+argstipo.get(i)+ " " +argslista.get(i)+"){\n"
					+"		this."+ argslista.get(i) + "="+argslista.get(i)+";\n	}\n"; 
			javaEditorServices.insertTextAtCursor(obj);
		}
		argstipo = new ArrayList<String>();
		argslista = new ArrayList<String>();
	}

}
