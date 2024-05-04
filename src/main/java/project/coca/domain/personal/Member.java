package project.coca.domain.personal;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import project.coca.domain.group.CoGroup;
import project.coca.domain.group.GroupManager;
import project.coca.domain.group.GroupMember;
import project.coca.domain.request.FriendRequest;
import project.coca.domain.request.GroupRequest;
import project.coca.domain.request.ScheduleRequest;
import project.coca.domain.tag.Interest;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
public class Member {
    @Id
    @Column(name = "MEMBER_ID", length = 16)
    private String id;

    @Column(name = "PASSWORD", length = 16)
    private String password;
    @Column(name = "USERNAME", length = 20)
    private String userName;
    @Column(name = "PROFILE_IMG_PATH")
    private String profileImgPath;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Interest> interests = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<PersonalSchedule> personalSchedules = new ArrayList<>();

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL)
    private List<CoGroup> coGroups = new ArrayList<>();

    @OneToMany(mappedBy = "groupMember", cascade = CascadeType.ALL)
    private List<GroupMember> groupMembers = new ArrayList<>();

    @OneToMany(mappedBy = "groupManager", cascade = CascadeType.ALL)
    private List<GroupManager> groupManagers = new ArrayList<>();

    @OneToMany(mappedBy = "member1", cascade = CascadeType.ALL)
    private List<Friend> friends = new ArrayList<>();

    @OneToMany(mappedBy = "member2", cascade = CascadeType.ALL)
    private List<Friend> friends2 = new ArrayList<>();

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private List<GroupRequest> groupRequestsAsSender = new ArrayList<>();

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    private List<GroupRequest> groupRequestsAsReceiver = new ArrayList<>();

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private List<ScheduleRequest> scheduleRequestsAsSender = new ArrayList<>();

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    private List<ScheduleRequest> scheduleRequestsAsReceiver = new ArrayList<>();

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private List<FriendRequest> friendRequestsAsSender = new ArrayList<>();

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    private List<FriendRequest> friendRequestsAsReceiver = new ArrayList<>();
  
    @Builder
    public Member(String id, String pw) {
        this.id = id;
        this.password = pw;
    }

    @Builder
    public Member(String id, String pw, String userName, String profileImgPath) {
        this.id = id;
        this.password = pw;
        this.userName = userName;
        this.profileImgPath = profileImgPath;
    }

    @Builder
    public Member(String id, String pw, String userName) {
        this.id = id;
        this.password = pw;
        this.userName = userName;
    }
}
