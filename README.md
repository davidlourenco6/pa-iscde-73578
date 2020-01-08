# 
# pa-iscde-73578

##  what is this???

TThis is a Java Code Generator plug-in for PIDESCO. It's partially based on the class about AST we've had near the beginning of the semester. This is an academic project for Advanced Programming @ ISCTE-IUL.

Git Repository: https://github.com/davidlourenco6/pa-iscde-73578.git

## how does it work???

It analyses all variable declaration fragments with a Visitor.
A Visitor is a class that looks at the contents of an AST Node  and reports two non-null lists with all arguments and type arguments in this node.
This two lists are used to generate code about this node.

## how can I extend it???

Use the "coder" extension point and have a class or two or three or many that implement
pt.iscte.pidesco.generator.extensibility.CodeGeneratorCoder.

The CodeGeneratorCoder interface has a generate method that receives an object of type IClass. This method is used to implement what we want to generate in GeneratorView. 
  
IClass is a helper process class and is used to transport fields and enter fields behind our Visitor and CodeGeneratorCoder implementation. Without this, our CodeGeneratorCoder.generate () cannot do pertinent things.

An example of a working extension can be found in pt.iscte.pidesco.generator.internal.CoderExt

## how can I use its services???

Just make sure you've clicked on the Generator tab on PIDESCO at least once before trying anything, so that the view can be activated.
Now you can click on view buttons and generate getters, setters and constructors.  
By default, you will see six buttons for generating predefined methods and one button for each active extension that will generate from this extension.

