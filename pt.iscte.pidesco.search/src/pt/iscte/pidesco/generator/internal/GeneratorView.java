package pt.iscte.pidesco.generator.internal;


import java.awt.Canvas;
import java.awt.List;
import java.io.Console;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JComboBox;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import pt.iscte.pidesco.demo.extensibility.DemoAction;
import pt.iscte.pidesco.extensibility.PidescoView;
import pt.iscte.pidesco.generator.extensibility.CodeGeneratorCoder;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;
import pt.iscte.pidesco.projectbrowser.extensibility.ProjectBrowserFilter;
import pt.iscte.pidesco.projectbrowser.model.SourceElement;
import pt.iscte.pidesco.projectbrowser.service.ProjectBrowserListener;
import pt.iscte.pidesco.projectbrowser.service.ProjectBrowserServices;

public class GeneratorView implements PidescoView {

	GeneratorImpl generatorImpl = new GeneratorImpl(); ;
	ContructorGeneratorImpl contructorGeneratorImpl = new ContructorGeneratorImpl();
	private static final String EXT_POINT_Generator = "pt.iscte.pidesco.generator.coder";
	private static final String GENERATE_Getters = "  Generate Getters  ";
	private static final String GENERATE_Setters = "  Generate Setters  ";
	private static final String GENERATE_Constructor = "Generate Constructors";
	private static final String GENERATE_Constructor_empty = "Generate Empty Constructors";
	private static final String GENERATE_Selected = "Generate Selected Getters or Setters: ";
	private static final String GENERATE_Getter = "  Generate Getter  ";
	private static final String GENERATE_Setter = "  Generate Setter  ";
	private static final String GENERATE_Ext = "Generate Ext";
	
	@Override
	public void createContents(Composite viewArea, Map<String, Image> imageMap) {

		Label space;
		
		viewArea.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		viewArea.setLayout(new GridLayout(6, false));
		
		space = new Label(viewArea, SWT.NONE);
		space.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 6, 1));
		
		//------------------------------------Buttons---------------------------------------//

		Composite composite3 = new Composite(viewArea, SWT.NONE);
		composite3.setLayout(new GridLayout(3, false));
		composite3.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		
		Button gettersButton = new Button(composite3, SWT.NONE);
		gettersButton.setText(GENERATE_Getters);
		gettersButton.addListener(SWT.Selection, event -> {generatorImpl.startAllGetters();});
		
		Button settersButton = new Button(composite3, SWT.NONE);
		settersButton.setText(GENERATE_Setters);
		settersButton.addListener(SWT.Selection, event -> {generatorImpl.startAllSetters();});
		
		Button constructorButton = new Button(composite3, SWT.NONE);
		constructorButton.setText(GENERATE_Constructor);
		constructorButton.addListener(SWT.Selection, event -> { contructorGeneratorImpl.startConstructor();});
		
		Button emptyConstructorButton = new Button(composite3, SWT.NONE);
		emptyConstructorButton.setText(GENERATE_Constructor_empty);
		emptyConstructorButton.addListener(SWT.Selection, event -> {contructorGeneratorImpl.startEmptyConstructor();});
		
		space = new Label(viewArea, SWT.NONE);
		space.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 6, 1));
		
		Composite composite2 = new Composite(viewArea, SWT.NONE);
		composite2.setLayout(new GridLayout(6, false));
		composite2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 6, 1));
		
		Label replaceTitle = new Label(composite2, SWT.NONE);
		replaceTitle.setText(GENERATE_Selected);
		
		//populate args combobox
		Combo combo = new Combo(composite2, SWT.NONE);
		java.util.List<String> lista = generatorImpl.getArgs();
		for(int i=0; i!=lista.size(); i++) {
			combo.add(lista.get(i));
		}
		
		Button generateSelectedGetButton = new Button(composite2, SWT.NONE);
		generateSelectedGetButton.setText(GENERATE_Getter);
		generatorImpl = new GeneratorImpl();
		generateSelectedGetButton.addListener(SWT.Selection, event -> {generatorImpl.startSelectedGetter(combo.getSelectionIndex());});
		
		Button generateSelectedSetButton = new Button(composite2, SWT.NONE);
		generateSelectedSetButton.setText(GENERATE_Setter);
		generatorImpl = new GeneratorImpl();
		generateSelectedSetButton.addListener(SWT.Selection, event -> {generatorImpl.startSelectedSetter(combo.getSelectionIndex());});

		//----------------------------------------------------------------------------------//

		//-------------------------------extensions treatment-------------------------------//

		BundleContext context = Activator.getContext();
		ServiceReference<JavaEditorServices> serviceReference = context.getServiceReference(JavaEditorServices.class);
		JavaEditorServices javaEditorServices = context.getService(serviceReference);		
		
		IExtensionRegistry reg = Platform.getExtensionRegistry();
			for(IExtension ext : reg.getExtensionPoint(EXT_POINT_Generator).getExtensions()) {
				Button generateExtencionCode = new Button(composite2, SWT.NONE);
				generateExtencionCode.setText(GENERATE_Ext+ext.getExtensionPointUniqueIdentifier());
				generateExtencionCode.addListener(SWT.Selection, event -> {
					CodeGeneratorCoder extension = null;
					
					for(IConfigurationElement iconf : ext.getConfigurationElements()) {
						
						try {
							
							extension = (CodeGeneratorCoder) iconf.createExecutableExtension("class");
						} catch (InvalidRegistryObjectException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (CoreException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
					}
					
					IClass auxClass = new IClass(generatorImpl.getArgs(),generatorImpl.getTypes(), contructorGeneratorImpl.getClassName());
					String codigoGerado = extension.generate(auxClass);
					javaEditorServices.insertTextAtCursor(codigoGerado);
					});
			}
			
			//----------------------------------------------------------------------------------//
	}
	
}
