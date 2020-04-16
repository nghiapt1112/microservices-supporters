package demo.domain;

public class UserException extends DomainException {

    public UserException(String errorCode, String message) {
        super(errorCode, message);
    }
    public UserException(String errorCode) {
        super(errorCode, errorCode);
    }


}
