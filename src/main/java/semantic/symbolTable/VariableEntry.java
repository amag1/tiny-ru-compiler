package semantic.symbolTable;

import lexical.Token;
import semantic.Json;
import semantic.JsonHelper;
import codeGeneration.MipsHelper;

/**
 * Representa una variable de la tabla de simbolos
 * Una variable puede ser una variable local o un parametro formal de metodos
 * Esta clase solo tiene metodos getter y setter
 */
public class VariableEntry implements Json {
    private String name;
    private AttributeType type;
    private Token token;
    private int position;
    private Scope scope;

    private boolean isPrivate;

    private boolean isInherited;

    public VariableEntry(AttributeType type, Token token, int position, Scope scope) {
        this.type = type;
        this.token = token;
        this.position = position;
        this.name = token.getLexem();
        this.scope = scope;
    }

    public VariableEntry(AttributeType type, Token token, Scope scope) {
        this.type = type;
        this.token = token;
        this.name = token.getLexem();
        this.scope = scope;
    }

    public VariableEntry(AttributeType type, Token token, boolean isPrivate, Scope scope) {
        this.type = type;
        this.token = token;
        this.name = token.getLexem();
        this.isPrivate = isPrivate;
        this.scope = scope;
    }

    public AttributeType getType() {
        return type;
    }

    public void setType(AttributeType type) {
        this.type = type;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public boolean isInherited() {
        return isInherited;
    }

    public void setInherited(boolean inherited) {
        isInherited = inherited;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public void initialize(MipsHelper helper, int offset) {
        // Genera el codigo para instanciar con el valor default
        String defaultValue = this.type.getDefaultValue();

        // Obtener el offset de la variable
        // Generar el codigo
        helper.comment("Inicializar variable " + this.name);
        helper.load("$t0", defaultValue);
        helper.push("$t0");
    }

    public String toJson(int identationIndex) {
        identationIndex++;
        return "{" +
                JsonHelper.json("name", this.getName(), identationIndex) + "," +
                JsonHelper.json("type", this.getType().toJson(0), identationIndex) + "," +
                JsonHelper.json("isPrivate", this.isPrivate, identationIndex) + "," +
                JsonHelper.json("isInherited", this.isInherited, identationIndex) + "," +
                JsonHelper.json("position", this.getPosition(), identationIndex) +
                "\n" + JsonHelper.getIdentationString(identationIndex - 1) + "}";
    }
}
