package io.github.matheuspadilha.quarkussocial.resource.exception;

public class FollowerAndUserConflictException extends RuntimeException {

    public FollowerAndUserConflictException() {
        super();
    }
    public FollowerAndUserConflictException(String msg) {
        super(msg);
    }
    public FollowerAndUserConflictException(String msg, Exception e)  {
        super(msg, e);
    }
}
