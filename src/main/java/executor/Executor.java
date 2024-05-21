package executor;

import logger.Logger;
import reader.Reader;

/**
 * Clase abstracta para un ejecutador
 * Toma un reader (entrada) y un logger (salida)
 * Contiene un solo método que debe ser implementado por las clases que hereden de esta
 */
public abstract class Executor {
    protected Reader reader;
    protected Logger logger;

    public Executor(Reader reader, Logger logger) {
        this.logger = logger;
        this.reader = reader;
    }

    public abstract void execute();

    public void setBackupLogger(Logger logger) {
        this.logger = logger;
    }
}
