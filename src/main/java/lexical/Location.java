package lexical;

public class Location {
    public int line;
    public int column;
    public int position;

    public Location(int line, int column, int position) {
        this.line = line;
        this.column = column;
        this.position = position;
    }
}
