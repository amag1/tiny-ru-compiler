package semantic.symbolTable;

import lexical.Token;
import lexical.Type;
import location.Location;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PredefinedClassCreator {
    public PredefinedClassCreator() {
    }

    List<ClassEntry> generatePredefinedClasses() {
        Map<String, AttributeType> primitiveAttributeTypes = getPrimitiveAttributeTypes();
        List<ClassEntry> classes = new ArrayList<>();
        classes.add(generateIoClass(primitiveAttributeTypes));
        classes.add(generateObjectClass(primitiveAttributeTypes));
        classes.add(generateStrClass(primitiveAttributeTypes));
        classes.add(generateArrayClass(primitiveAttributeTypes));

        for (ClassEntry classEntry : classes) {
            setFieldInPredefinedStruct(classEntry);
        }

        return classes;
    }

    private ClassEntry generateArrayClass(Map<String, AttributeType> types) {
        ClassEntry arrayClass = new ClassEntry(new Token("Array", Type.ID_CLASS, new Location()));

        MethodEntry length = new MethodEntry(new Token("length", Type.ID, new Location()), false);
        length.setReturnType(types.get("Int"));
        length.setPosition(0);
        arrayClass.addMethod(length);

        return arrayClass;
    }

    private ClassEntry generateStrClass(Map<String, AttributeType> types) {
        ClassEntry strClass = new ClassEntry(new Token("Str", Type.ID_CLASS, new Location()));

        MethodEntry length = new MethodEntry(new Token("length", Type.ID, new Location()), false);
        length.setReturnType(types.get("Int"));
        length.setPosition(0);
        strClass.addMethod(length);

        MethodEntry concat = new MethodEntry(new Token("concat", Type.ID, new Location()), false);
        concat.addFormalParam(new VariableEntry(types.get("Str"), new Token("s", Type.ID, new Location())));
        concat.setReturnType(types.get("Str"));
        concat.setPosition(1);
        strClass.addMethod(concat);

        return strClass;
    }

    private ClassEntry generateObjectClass(Map<String, AttributeType> types) {
        return new ClassEntry(new Token("Object", Type.ID_CLASS, new Location()));
    }


    private ClassEntry generateIoClass(Map<String, AttributeType> types) {
        ClassEntry ioClass = new ClassEntry(new Token("IO", Type.ID_CLASS, new Location()));

        MethodEntry out_str = new MethodEntry(new Token("out_string", Type.ID, new Location()), true);
        out_str.addFormalParam(new VariableEntry(types.get("Str"), new Token("x", Type.ID, new Location())));
        out_str.setPosition(0);
        ioClass.addMethod(out_str);

        MethodEntry out_int = new MethodEntry(new Token("out_int", Type.ID, new Location()), true);
        out_int.addFormalParam(new VariableEntry(types.get("Int"), new Token("x", Type.ID, new Location())));
        out_int.setPosition(1);
        ioClass.addMethod(out_int);

        MethodEntry out_bool = new MethodEntry(new Token("out_bool", Type.ID, new Location()), true);
        out_bool.addFormalParam(new VariableEntry(types.get("Bool"), new Token("x", Type.ID, new Location())));
        out_bool.setPosition(2);
        ioClass.addMethod(out_bool);

        MethodEntry out_char = new MethodEntry(new Token("out_char", Type.ID, new Location()), true);
        out_char.addFormalParam(new VariableEntry(types.get("Char"), new Token("x", Type.ID, new Location())));
        out_char.setPosition(3);
        ioClass.addMethod(out_char);

        MethodEntry out_array_int = new MethodEntry(new Token("out_array_int", Type.ID, new Location()), true);
        out_array_int.addFormalParam(new VariableEntry(toArray(types.get("Int")), new Token("x", Type.ID, new Location())));
        out_array_int.setPosition(4);
        ioClass.addMethod(out_array_int);

        MethodEntry out_array_str = new MethodEntry(new Token("out_array_str", Type.ID, new Location()), true);
        out_array_str.addFormalParam(new VariableEntry(toArray(types.get("Str")), new Token("x", Type.ID, new Location())));
        out_array_str.setPosition(5);
        ioClass.addMethod(out_array_str);

        MethodEntry out_array_bool = new MethodEntry(new Token("out_array_bool", Type.ID, new Location()), true);
        out_array_bool.addFormalParam(new VariableEntry(toArray(types.get("Bool")), new Token("x", Type.ID, new Location())));
        out_array_bool.setPosition(6);
        ioClass.addMethod(out_array_bool);

        MethodEntry out_array_char = new MethodEntry(new Token("out_array_char", Type.ID, new Location()), true);
        out_array_char.addFormalParam(new VariableEntry(toArray(types.get("Char")), new Token("x", Type.ID, new Location())));
        out_array_char.setPosition(7);
        ioClass.addMethod(out_array_char);

        MethodEntry in_string = new MethodEntry(new Token("in_string", Type.ID, new Location()), true);
        in_string.setReturnType(types.get("Str"));
        in_string.setPosition(8);
        ioClass.addMethod(in_string);

        MethodEntry in_int = new MethodEntry(new Token("in_int", Type.ID, new Location()), true);
        in_int.setReturnType(types.get("Int"));
        in_int.setPosition(9);
        ioClass.addMethod(in_int);

        MethodEntry in_bool = new MethodEntry(new Token("in_bool", Type.ID, new Location()), true);
        in_bool.setReturnType(types.get("Bool"));
        in_bool.setPosition(10);
        ioClass.addMethod(in_bool);

        MethodEntry in_char = new MethodEntry(new Token("in_char", Type.ID, new Location()), true);
        in_char.setReturnType(types.get("Char"));
        in_char.setPosition(11);
        ioClass.addMethod(in_char);

        return ioClass;
    }

    private Map<String, AttributeType> getPrimitiveAttributeTypes() {
        Map<String, AttributeType> primitiveAttributeTypes = new HashMap<>();
        primitiveAttributeTypes.put("Int", new AttributeType(false, true, new Token("Int", Type.TYPE_INT, new Location())));
        primitiveAttributeTypes.put("Str", new AttributeType(false, true, new Token("Str", Type.TYPE_STRING, new Location())));
        primitiveAttributeTypes.put("Bool", new AttributeType(false, true, new Token("Bool", Type.TYPE_BOOL, new Location())));
        primitiveAttributeTypes.put("Char", new AttributeType(false, true, new Token("Char", Type.TYPE_CHAR, new Location())));
        return primitiveAttributeTypes;
    }

    private AttributeType toArray(AttributeType type) {
        return new AttributeType(true, type.isPrimitive(), type.getToken());
    }

    private void setFieldInPredefinedStruct(ClassEntry classEntry) {
        classEntry.setHandledInheritance(true);
        classEntry.setFoundImpl(true);
        classEntry.setHasConstructor(true);
        classEntry.setFoundStruct(true);
    }
}
