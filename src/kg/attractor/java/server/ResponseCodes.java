package kg.attractor.java.server;

public enum ResponseCodes {
    OK(200),
    NOT_FOUND(404),
    REDIRECT_303(303),
    SERVER_ERROR(500);

    private final int code;

    ResponseCodes(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
