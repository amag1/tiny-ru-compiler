package semantic;

/**
 * Interfaz para aquellas clases que pueden ser convertidas a JSON
 */
public interface Json {
    String toJson(int indentationIndex);

}
