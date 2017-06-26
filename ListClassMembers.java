package riachuelo.autcom.caixa.control;

import static java.lang.System.out;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

enum ClassMember {
	CONSTRUCTOR, FIELD, METHOD, CLASS, ALL
}

public class ListClassMembers {

	public static void main(String... args) {
		try {
			Class<?> c = Class.forName(args[0]);
			out.format("Class:%n  %s%n%n", c.getCanonicalName());

			Package p = c.getPackage();
			out.format("Package:%n  %s%n%n",
					(p != null ? p.getName() : "-- No Package --"));

			for (int i = 1; i < args.length; i++) {
				switch (ClassMember.valueOf(args[i])) {
				case CONSTRUCTOR:
					printMembers(c.getConstructors(), "Constructor");
					break;
				case FIELD:
					printMembers(c.getFields(), "Fields");
					listPrivateFields();
					break;
				case METHOD:
					printMembers(c.getMethods(), "Methods");
					listPrivateMethods();
					break;
				case CLASS:
					printClasses(c);
					break;
				case ALL:
					printMembers(c.getConstructors(), "Constuctors");
					printMembers(c.getFields(), "Fields");
					printMembers(c.getMethods(), "Methods");
					printClasses(c);
					break;
				default:
					assert false;
				}
			}

			// production code should handle these exceptions more gracefully
		} catch (ClassNotFoundException x) {
			x.printStackTrace();
		}
	}

	
	private static void listPrivateMethods() {
		 List<Method> methods = getPrivateMethods(VendaControl.class);
	        for(Method field: methods){
	        	out.format("%s%n",field.toGenericString());
	        }
	}	
	
	
	private static void listPrivateFields() {
		 List<Field> fields = getPrivateFields(VendaControl.class);
	        for(Field field: fields){
	        	out.format("%s%n",field.toGenericString());
	        }
	}
	
	private static List<Method> getPrivateMethods(Class<?> theClass){
        List<Method> privateMethods = new ArrayList<Method>();

        Method[] methods = theClass.getDeclaredMethods();

        for(Method method:methods){
            if(Modifier.isPrivate(method.getModifiers())){
            	privateMethods.add(method);
            }
        }
        return privateMethods;
    }
    
	private static List<Field> getPrivateFields(Class<?> theClass){
        List<Field> privateFields = new ArrayList<Field>();

        Field[] fields = theClass.getDeclaredFields();

        for(Field field:fields){
            if(Modifier.isPrivate(field.getModifiers())){
                privateFields.add(field);
            }
        }
        return privateFields;
    }
    
	@SuppressWarnings("rawtypes")
	private static void printMembers(Member[] mbrs, String s) {
		out.format("%s:%n", s);
		for (Member mbr : mbrs) {
			if (mbr instanceof Field)
				out.format("%s%n", ((Field) mbr).toGenericString());
			else if (mbr instanceof Constructor)
				out.format("%s%n", ((Constructor) mbr).toGenericString());
			else if (mbr instanceof Method)
				out.format("%s%n", ((Method) mbr).toGenericString());
		}
		if (mbrs.length == 0)
			out.format("  -- No %s --%n", s);
		out.format("%n");
	}

	private static void printClasses(Class<?> c) {
		out.format("Classes:%n");
		Class<?>[] clss = c.getClasses();
		for (Class<?> cls : clss)
			out.format("%s%n", cls.getCanonicalName());
		if (clss.length == 0)
			out.format("  -- No member interfaces, classes, or enums --%n");
		out.format("%n");
	}
}
