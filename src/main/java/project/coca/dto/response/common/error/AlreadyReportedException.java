package project.coca.dto.response.common.error;

public class AlreadyReportedException extends RuntimeException {
    public AlreadyReportedException(String message) {
        super(message);
    }
}
