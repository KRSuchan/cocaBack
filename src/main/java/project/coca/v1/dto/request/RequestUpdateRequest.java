package project.coca.v1.dto.request;

import lombok.Getter;
import lombok.Setter;
import project.coca.v1.domain.request.RequestStatus;

@Setter
@Getter
public class RequestUpdateRequest {
    private Long requestId;
    private RequestStatus status;
}
