package codeGeneration;

import semantic.abstractSintaxTree.*;
import semantic.symbolTable.*;

import java.util.Map;

public class CodeGenerator {
    private AbstractSyntaxTree ast;
    private SymbolTable symbolTable;
    private Boolean debug = true;


    public CodeGenerator(AbstractSyntaxTree ast, SymbolTable symbolTable) {
        this.ast = ast;
        this.symbolTable = symbolTable;
    }

    public String generateCode() {
        StringBuilder sb = new StringBuilder();

        // Generate Macros
        MipsHelper macrosHelper = new MipsHelper(debug);
        macrosHelper.generateMacros();
        sb.append(macrosHelper.getString());

        // Generar codigo para start
        Context context = new Context(symbolTable);
        sb.append(generateStart(context));

        // Generar codigo pra las clases
        for (ClassEntry classEntry : symbolTable.getClasses()) {
            // Obtener clase del ast
            AstClassEntry astClassEntry = ast.getClasses().get(classEntry.getName());
            ClassGenerator classGenerator = new ClassGenerator(context, classEntry, astClassEntry, debug);
            sb.append(classGenerator.generate());
        }

        // Generar codigo para nuevo array
        sb.append(generateNewArray());

        return sb.toString();
    }


    public String generateStart(Context startContext) {
        MipsHelper startHelper = new MipsHelper(debug);

        // Genera código del start
        AstMethodEntry start = ast.getStart();
        MethodEntry startMethod = symbolTable.getStart();

        // Generar codigo para las variables locales
        // Actualiza el frame pointer
        startHelper.initStart(startMethod);
        startHelper.append(start.generate(startContext, debug));

        // Popear todas las variables locales
        startHelper.popLocalVariables(startMethod);

        // Finaliza el programa
        startHelper.syscall(10);

        startHelper.addDefaultValues();

        return startHelper.getString();
    }

    public String generateNewArray() {
        MipsHelper helper = new MipsHelper(debug);

        helper.startText();
        helper.append("new_array:");

        // Alocar memoria para el array
        helper.mutilply("$a0", "$a0", "4");
        helper.syscall(9);

        // Guardar direccion del array en t1
        helper.move("$t1", "$v0");
        helper.add("$a0", "$a0", "$t1"); // Store in the accumolator the final addres of the array

        // Popear el valor default
        helper.pop("$t3");

        // Popear el tamaño del array
        helper.pop("$t0");

        // Iterar sobre el array y setear el default
        helper.comment("start loop");
        helper.append("start_set_default_array:");
        helper.branchOnEqual("$a0", "$t1", "end_set_default_array");

        // Store in t3 the current address
        // Decrease by 4 for fix the offset difference
        helper.sw("$t3", "-4($a0)");
        helper.addIU("$a0", "$a0", -4);
        helper.jump("start_set_default_array");
        helper.append("end_set_default_array:");

        // Alocar array CIR
        helper.allocateMemory(2*32); // Dos words
        helper.sw("$t0", "0($v0)"); // Guardar tamaño en primer word
        helper.sw("$t1", "4($v0)"); // Guardar direccion en primer word

        // Guardar variable array
        helper.storeInAccumulator("($v0)");

        helper.jumpRegister("$ra");

        return  helper.getString();
    }
}
