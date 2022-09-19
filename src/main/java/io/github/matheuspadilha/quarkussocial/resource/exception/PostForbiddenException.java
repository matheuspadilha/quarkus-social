package io.github.matheuspadilha.quarkussocial.resource.exception;

public class PostForbiddenException extends RuntimeException {

    public PostForbiddenException() {
        super();
    }
    public PostForbiddenException(String msg) {
        super(msg);
    }
    public PostForbiddenException(String msg, Exception e)  {
        super(msg, e);
    }
}
