package demo.domain;

public class DomainException extends RuntimeException {
    private String code;
    private String msg;

    public DomainException(String code, String message) {
        super(message);
        this.code = code;
        this.msg = message;
    }

    public DomainException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.msg = message;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}