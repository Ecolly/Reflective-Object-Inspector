
/*
Some part of the code is written by  Jordan Kidney */
//test commmit

import java.util.*;
import java.lang.reflect.*;


public class Inspector{
    public Inspector() { 
    }

public void inspect(Object obj, boolean recursive)
    {
	Vector objectsToInspect = new Vector(); //similar to array but yo udon't need to know size beforehand
	Class <?> ObjClass = obj.getClass(); //get the object class

	System.out.println("CLASS NAME: " + ObjClass.getName()); //CLASS NAME
	System.out.println("inside inspector: " + obj + " (recursive = "+recursive+")");
	
	//inspect the current class
	inspectFields(obj, ObjClass,objectsToInspect);
	
	if(recursive)
	    inspectFieldClasses( obj, ObjClass, objectsToInspect, recursive);
		inspectMethods( obj, ObjClass, objectsToInspect, recursive);
	}


public void inspectFields(Object obj,Class ObjClass,Vector objectsToInspect)
	//passes in the obj and the obj Class
	{
		if(ObjClass.getDeclaredFields().length >= 1) //if there's field in Obj
	    {
		Field classfield = ObjClass.getDeclaredFields()[0]; //the 
		try{
			classfield.setAccessible(true);
			}
		catch(Exception e){System.out.println("Object is not accessible");}
		
		if(!classfield.getType().isPrimitive() ) 
			//if the field in the class is not primitive, we need to inspect it later
		    objectsToInspect.addElement(classfield);
		try
		    {
			//print out the value of the represented field in the object
			System.out.println("Field: " + classfield.getName() + " = " + classfield.get(obj));
		    }
		catch(Exception e) {}    
	    }
	if(ObjClass.getSuperclass() != null){ //Immediate SuperClass
		System.out.println("SUPER CLASS NAME: " + ObjClass.getName());
	    inspectFields(obj, ObjClass.getSuperclass() , objectsToInspect);
	 } //CLASS NAME
	}


public void inspectFieldClasses(Object obj,Class ObjClass, Vector objectsToInspect,boolean recursive)
	{
	if(objectsToInspect.size() > 0 )
		System.out.println("---- Inspecting Field Classes ----");
		
	Enumeration e = objectsToInspect.elements();
	while(e.hasMoreElements())
	    {
		Field f = (Field) e.nextElement();
		System.out.println("Inspecting Field: " + f.getName() );
		try
		    {
			System.out.println("******************");
			inspect( f.get(obj) , recursive);
			System.out.println("******************");
		    }
		catch(Exception exp) { exp.printStackTrace(); }
	    }
	}


private void inspectMethods(Object obj,Class ObjClass, Vector objectsToInspect,boolean recursive){
	if(ObjClass.getDeclaredMethods().length > 0) {
		for(Method m: ObjClass.getDeclaredMethods()) {
			System.out.printf("METHOD Name: "+ m.getName()+ "\n");
			
			//Exception thrown
			if(m.getExceptionTypes().length >0) {
				System.out.printf("\n Method Exceptions: ");
				for(Class ex : m.getExceptionTypes()) {
					System.out.printf("\n " + ex + "\n");
				}
			}
			else {
				System.out.printf("Exceptions-> NONE");
			}
		
			//Parameter types
			if(m.getParameterTypes().length > 0) {
				System.out.printf("\n Parameter types->");
				for(Parameter param : m.getParameters()) {
					System.out.printf(param.getType()+ ", ");
				}
			}
			else {
				System.out.printf("Parameter types -> NONE");
			}
			
			//return types
			System.out.printf("\n Return type: " + m.getReturnType().getName());
			
			//modifiers
			int mod = m.getModifiers();
			if(m.getModifiers() > 0){
				System.out.printf("\n  Modifiers: " + Modifier.toString(mod)+ "\n");
			}
			else{
				System.out.printf("\n Modifiers: NONE");
			}
		}
	}

	else {
		System.out.printf("\n Methods-> NONE");
		
	}
	
	}

private void inspectInterfaces(Class c, Object obj, boolean recursive, int depth) {
	Class[] inter = c.getInterfaces();
	System.out.printf("\n INTERFACES ( " +c.getName() + "Interfaces->", depth);
	if(inter.length >0) {
		for (Class f : inter) {
			System.out.printf("\n INTERFACE -> Recursively Inspect\n" + f.getName());
			inspect(obj, recursive);
		}
	}
	else {
			System.out.printf(" NONE", depth);
		}

	}

}


