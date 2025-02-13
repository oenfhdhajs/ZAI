package cn.z.zai.config.encrypt.exception;


public class EncryptException extends RuntimeException {
    private String message;

    public EncryptException(String message) {
        super(message);
        this.message = message;
    }

    public EncryptException(String message, String message1) {
        super(message);
        this.message = message1;
    }
}
