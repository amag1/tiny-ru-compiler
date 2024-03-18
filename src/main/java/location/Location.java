package location;

/**
 * Clase que representa la ubicación de un token en el archivo.
 */
public class Location {
    /**
     * Numero de línea
     */
    private int line;
    /**
     * Numero de columna
     */
    private int column;
    /**
     * Posición en el archivo como un offset comenzando desde cero
     */
    private int position;

    public Location(int line, int column, int position) {
        this.line = line;
        this.column = column;
        this.position = position;
    }

    public Location(int line, int column) {
        this.line = line;
        this.column = column;
    }

    public Location() {
        this(1, 1, 0);
    }

    /**
     * @return una copia de la ubicación
     */
    public Location copy() {
        return new Location(this.line, this.column, this.position);
    }

    public void increaseLine() {
        this.line++;
    }

    public void increaseColumn() {
        this.column++;
    }

    public void increasePosition() {
        this.position++;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getLine() {
        return this.line;
    }

    public int getColumn() {
        return this.column;
    }

    public int getPosition() {
        return this.position;
    }

}
