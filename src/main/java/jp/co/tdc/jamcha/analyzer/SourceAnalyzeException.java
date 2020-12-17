package jp.co.tdc.jamcha.analyzer;

public class SourceAnalyzeException extends RuntimeException {
    static final long serialVersionUID = 1;

    public SourceAnalyzeException(String message) {
        super(message);
    }

    public SourceAnalyzeException(String message, Throwable cause) {
        super(message, cause);
    }
}
