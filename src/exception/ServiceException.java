package exception;

/**
 * Исключение, выбрасываемое при ошибке в работе сервиса
 */
public class ServiceException extends RuntimeException {
    public ServiceException(String message) {
        super(message);
    }
}
