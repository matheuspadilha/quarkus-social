package io.github.matheuspadilha.quarkussocial.resource.exception;

public class PostBadRequestException extends RuntimeException {

    public PostBadRequestException() {
        super();
    }
    public PostBadRequestException(String msg) {
        super(msg);
    }
    public PostBadRequestException(String msg, Exception e)  {
        super(msg, e);
    }
}
