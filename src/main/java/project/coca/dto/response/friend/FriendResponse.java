package project.coca.dto.response.friend;

import lombok.Builder;
import lombok.Data;
import project.coca.domain.personal.Friend;

@Builder
@Data
public class FriendResponse {
    private Long friendId;
    private String friendMemberId;
    private String friendName;
    private String friendProfileImagePath;

    public static FriendResponse of(Friend friend) {
        return FriendResponse.builder()
                .friendId(friend.getId())
                .friendMemberId(friend.getOpponent().getId())
                .friendName(friend.getOpponentNickname())
                .friendProfileImagePath(friend.getOpponent().getProfileImgPath())
                .build();
    }
}
