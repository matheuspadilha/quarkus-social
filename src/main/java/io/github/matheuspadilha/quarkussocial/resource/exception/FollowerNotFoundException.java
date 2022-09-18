package io.github.matheuspadilha.quarkussocial.resource.exception;

public class FollowerNotFoundException extends RuntimeException {

    public FollowerNotFoundException() {
        super();
    }
    public FollowerNotFoundException(String msg) {
        super(msg);
    }
    public FollowerNotFoundException(String msg, Exception e)  {
        super(msg, e);
    }
}
