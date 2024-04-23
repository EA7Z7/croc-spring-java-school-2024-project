package exception;

/**
 * Исключение, выбрасываемое при ошибке чтения файла
 */
public class ReadException extends RuntimeException {
    private final String path;

    public ReadException(String path, Exception e) {
        super(String.format("""
                Произошла ошибка при чтении файла по пути %s
                %s""", path, e.getMessage()), e);
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
