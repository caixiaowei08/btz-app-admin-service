package app.btz.common.authority;

import com.btz.user.entity.UserEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by User on 2017/7/19.
 */
public class AuthorityPojo implements Serializable{

    private String token;

    private List<CourseAuthorityPojo> authority;

    private UserEntity userEntity;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<CourseAuthorityPojo> getAuthority() {
        return authority;
    }

    public void setAuthority(List<CourseAuthorityPojo> authority) {
        this.authority = authority;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }
}
