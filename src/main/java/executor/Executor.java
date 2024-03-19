package executor;

import logger.Logger;
import reader.Reader;

public abstract class Executor {
    protected Reader reader;
    protected Logger logger;

    public Executor(Reader reader, Logger logger) {
        this.logger = logger;
        this.reader = reader;
    }

    public abstract void execute();
}
