package demo.domain;

import com.nghia.tut.mss.infrustructure.exception.DomainException;

public class UserException extends DomainException {

    public UserException(int errorCode, String message) {
        super(101, errorCode, message);
    }
}
