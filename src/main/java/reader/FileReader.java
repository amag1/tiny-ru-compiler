package reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileReader implements Reader{
    private File file;
    private char[] chars;

    public FileReader(String path) throws FileNotFoundException {
        this.file = new File(path);
        Scanner scanner = new Scanner(this.file);

        if (!scanner.hasNextLine()) {
            this.chars = new char[0];
            return;
        }

        String returnString = scanner.nextLine();
        while (scanner.hasNextLine()) {
            returnString += "\n" + scanner.nextLine();
        }

        this.chars = returnString.toCharArray();
    }

    public char[] getChars() {
        return this.chars;
    }
}
