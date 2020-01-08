package pt.iscte.pidesco.generator.internal;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.prefs.NodeChangeListener;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.internal.eval.VariablesInfo;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;


public class ContructorGeneratorImpl {

	private static BundleContext context = Activator.getContext();
	private static ServiceReference<JavaEditorServices> serviceReference = context.getServiceReference(JavaEditorServices.class);
	private static JavaEditorServices javaEditorServices = context.getService(serviceReference);
	static List<String> argslista = new ArrayList<String>();
	static List<String> argstipo = new ArrayList<String>();
	static boolean haveConstructor = false;
	static String name;
	
	private static class CheckConventions extends ASTVisitor {

	

		// visits class/interface declaration
		@Override
		public boolean visit(TypeDeclaration node) {

			for(int i=0; i!= node.getMethods().length; i++) {
				if(node.getMethods()[i].isConstructor()) {
					haveConstructor=true;
					
				}
			}
			if(!haveConstructor) {
				name = node.getName().toString();
				String obj = "	public " + name +"(";
				javaEditorServices.insertTextAtCursor(obj);
			}
			
			return true;
			
		}

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
		

		
		private void generateAssign() {
			
			String objconcat= "";
			for(int i = 0; i!=argslista.size(); i++) {
			
				if(i==argslista.size()-1) {
					objconcat = objconcat + argstipo.get(i) +" " + argslista.get(i);
				}
				else {
					objconcat = objconcat + argstipo.get(i) +" " + argslista.get(i)+ " ,";
				}
			}
				
			String obj = objconcat + ") {\n";
			argstipo = new ArrayList<String>();
			javaEditorServices.insertTextAtCursor(obj);	
		}
		
		public void generateConstructorsStatement() {
			String obj ="";
			for(int i = 0; i!=argslista.size(); i++) {
				
				obj = obj + "		this."+argslista.get(i)+"="+argslista.get(i) + ";\n" ;
			}	
			javaEditorServices.insertTextAtCursor(obj + "	}");
			argslista= new ArrayList<String>();
		}
		
	
		public void generateEmptyConstructorsStatement() {
			javaEditorServices.insertTextAtCursor( ") {\n	}");	
		}
		
		
	}
	
	
	public void startConstructor() {
		
		File file = javaEditorServices.getOpenedFile();
		CheckConventions checker = new CheckConventions();
		JavaParser.parse(file, checker);
	
		if(haveConstructor==false) {
			checker.generateAssign();
			checker.generateConstructorsStatement();
			System.out.println("-------------------------------------");
		}
	
		
	}
	
	public void startEmptyConstructor() {
		
		File file = javaEditorServices.getOpenedFile();
		CheckConventions checker = new CheckConventions();
		JavaParser.parse(file, checker);
		
		if(haveConstructor==false) {
		checker.generateEmptyConstructorsStatement();
		System.out.println("-------------------------------------");
		}
		
	}

	public String getClassName() {
		File file = javaEditorServices.getOpenedFile();
		CheckConventions checker = new CheckConventions();
		JavaParser.parse(file, checker);
		return name;
	}
	
	
}
