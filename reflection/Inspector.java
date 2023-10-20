
/*
Some part of the code is written by  Jordan Kidney */
//test commmit

import java.util.*;
import java.lang.reflect.*;


public class Inspector{
//private Set<Class<?>> inspectedClasses = new HashSet<>();

public void inspect(Object obj, boolean recursive)
    {
		Class test = obj.getClass();
		InspectClass(obj, test,recursive);
	}

public void InspectClass(Object obj,Class ObjClass, boolean recursive){
	Vector objectsToInspect = new Vector(); //similar to array but yo udon't need to know size beforehand
	

	System.out.println("CLASS NAME: " + ObjClass.getName()); //CLASS NAME
	System.out.println("inside inspector: " + obj + " (recursive = "+recursive+")");
	
	if (ObjClass.isArray()){
		System.out.println("Array detected");
		InspectArrays(obj, ObjClass, objectsToInspect, recursive);
		return;
	}
	//inspect the current class
	inspectFields(obj, ObjClass,objectsToInspect);
	
	if(recursive)
	    inspectFieldClasses( obj, ObjClass, objectsToInspect, recursive);
		inspectMethods( obj, ObjClass, objectsToInspect, recursive);
		inspectConstructor( obj, ObjClass, objectsToInspect, recursive);
		inspectSuperClass(obj, ObjClass, objectsToInspect, recursive);
		inspectInterfaces( obj, ObjClass, objectsToInspect, recursive);
}

public void inspectFields(Object obj,Class ObjClass,Vector objectsToInspect){
	//passes in the obj and the obj Class
	//IF the val is not a primiive object, add it to an array of objects to inpsect in the future
		if(ObjClass.getDeclaredFields().length >= 1) {
		for (Field classfield : ObjClass.getDeclaredFields()){; //the 
			System.out.println("Field: " + classfield.getName());
			try{
				classfield.setAccessible(true);
				}
			catch(Exception e){System.out.println("Object is not accessible");}
			
			if(!classfield.getType().isPrimitive() ){ 
				//if the field in the class is not primitive, we need to inspect it later
				objectsToInspect.addElement(classfield);
			}
			try
				{
				//print out the value of the represented field in the object
				System.out.println("Field: " + classfield.getName() + " = " + classfield.get(obj));

				Class fieldType = classfield.getType();
					//get the type
					System.out.printf("Type: " + fieldType.getName());
					
					int mod = classfield.getModifiers();
					if(classfield.getModifiers() > 0){
						System.out.printf("\nModifiers: " + Modifier.toString(mod)+"\n");
					}
					else{
						System.out.printf("\nModifiers: NONE \r\n");
					}
				}
			catch(Exception e) {System.out.printf("ERROR");}    
	    }
	} //CLASS NAME
	}

	//Inspect the superclass
public void inspectSuperClass( Object obj, Class ObjClass, Vector objectsToInspect, boolean recursive) {
	Class superClass = ObjClass.getSuperclass();
	if(superClass != null) {
		System.out.printf( "---------------SUPER CLASS TRAVERSE------------" + superClass.getName() +"\n");
			InspectClass(obj, superClass, recursive);
	}
	else {
		System.out.printf("\nSUPERCASS: NONE");
	}
	
}
	//inspect field's classes
public void inspectFieldClasses(Object obj,Class ObjClass, Vector objectsToInspect,boolean recursive)
	{
	if(objectsToInspect.size() > 0 )
		System.out.println("------------- Inspecting Field Classes ---------");
		
	Enumeration emu = objectsToInspect.elements();
	while(emu.hasMoreElements())
	    {
		Field field = (Field) emu.nextElement();
		System.out.println("Inspecting Field: " + field.getName() );
		try
		    {
			System.out.println("---------------------------------------------");
			InspectClass(obj, field.get(obj).getClass() , recursive);
			System.out.println("---------------------------------------------");
		    }
		catch(Exception exp) {System.out.printf("ERROR: " + exp.getMessage()); }
	    }
	}

	//inspect constructor

	private void inspectConstructor(Object obj,Class ObjClass, Vector objectsToInspect,boolean recursive) {

		System.out.printf("\nCONSTRUCTORS: " + ObjClass.getName());
		
		Constructor[] constructorList = ObjClass.getDeclaredConstructors();
		
		if(constructorList.length > 0) {
			for(Constructor construct : constructorList) {
				System.out.printf("CONSTRUCTOR  Names: "+ construct.getName());

				//constructor parameter
				if(construct.getParameterTypes().length > 0){
					System.out.printf("\nParameter types->");
					for(Parameter pam: construct.getParameters()) {
						System.out.printf(pam.getParameterizedType()+", ");
					}
				}
				else{
					System.out.printf("\nParameter types: NONE");
				}
				
				
				int mod = construct.getModifiers();
				if(mod > 0){
					System.out.printf("\nModifiers: " + Modifier.toString(mod)+"\n");
				}
				else{
					System.out.printf("\nModifiers: NONE");
				}
				
			}
			
		}
		
		else {
			System.out.printf(" NONE");	
		}
		
	}

	//inspect methods
private void inspectMethods(Object obj,Class ObjClass, Vector objectsToInspect,boolean recursive){
	if(ObjClass.getDeclaredMethods().length > 0) {
		for(Method m: ObjClass.getDeclaredMethods()) {
			System.out.printf("\nMethod Name: "+ m.getName()+ "\n");
			
			//Exception thrown
			if(m.getExceptionTypes().length >0) {
				System.out.printf("Method Exceptions: ");
				for(Class ex : m.getExceptionTypes()) {
					System.out.printf(ex + "\n");
				}
			}
			else {
				System.out.printf("Exceptions: NONE" + "\n");
			}
		
			//Parameter types
			if(m.getParameterTypes().length > 0) {
				System.out.printf("Parameter types->");
				for(Parameter param : m.getParameters()) {
					System.out.printf(param.getType()+ ", ");
				}
			}
			else {
				System.out.printf("Parameter types: NONE");
			}
			
			//return types
			System.out.printf("\nReturn type: " + m.getReturnType().getName());
			
			//modifiers
			int mod = m.getModifiers();
			if(m.getModifiers() > 0){
				System.out.printf("\nModifiers: " + Modifier.toString(mod)+ "\n");
			}
			else{
				System.out.printf("\nModifiers: NONE" + "\n");
			}
		}
	}

	else {
		System.out.printf("\n Methods-> NONE" + "\n");
		
	}
	
	}

//inspect interfaces

private void inspectInterfaces(Object obj,Class ObjClass, Vector objectsToInspect,boolean recursive) {
	Class[] inter = ObjClass.getInterfaces();
	//get a list of interfaces
	System.out.printf("\nINTERFACES: " + ObjClass.getName() +"'s Interfaces: ");
	//interate over each interface and inspect it
	if(inter.length>0) {
		for (Class interfclass : inter) { 
			System.out.printf("\nINTERFACE name" + interfclass.getName()+ "\n");
			InspectClass(obj,interfclass, recursive);
		}
	}
	else {
			System.out.printf("NONE \n");
		}

	}
public void InspectArrays(Object obj, Class ObjClass,Vector objectsToInspect,boolean recursive){
	Class compType = ObjClass.getComponentType();
		int arrlength = Array.getLength(obj);
		System.out.printf("\nLength: " + Integer.toString(arrlength));

		System.out.printf("Component Type: " + ObjClass.getComponentType());
				
		if(arrlength > 0) {
			System.out.printf("\nEntries: ");
		}
		
		for(int i = 0; i < arrlength; i++) {
			Object aObj = Array.get(obj, i);
			
			if (aObj == null) {
				System.out.printf("null");
			}
			else if(compType.isArray()) {
				System.out.printf("\nValue: " + aObj.toString());
				InspectArrays(aObj, aObj.getClass(), objectsToInspect, recursive);
			}
			
			else if(compType.isPrimitive()) {
				System.out.printf("\nValue: " + aObj.toString());
				
			}
			else if(!recursive) {
				System.out.printf("\n Value (ref): " + aObj.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(aObj)));
			}
			
			else {
				System.out.printf("\n Value (ref): " + aObj.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(aObj)));
				System.out.printf("Inheriance \n");
				InspectClass(aObj, aObj.getClass(), recursive);
			}
			
		}
	}

}