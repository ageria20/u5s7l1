package ageria.u5s7l1.exception;

import org.springframework.web.multipart.MaxUploadSizeExceededException;

public class MaxSizeException extends MaxUploadSizeExceededException {
    public MaxSizeException(Long fileSize) {
        super(fileSize);
    }
}
