package practice.spring_gym_api.security.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CoachAuthToken extends AbstractAuthenticationToken {

    private final Long coachId;
    private final String coachCode;

    public CoachAuthToken(Collection<? extends GrantedAuthority> authorities, Long coachId, String coachCode) {
        super(authorities);
        this.coachId = coachId;
        this.coachCode = coachCode;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return coachCode;
    }

    @Override
    public Object getPrincipal() {
        return coachId;
    }

    public Long getCoachId() {
        return coachId;
    }
    public String getCoachCode(){
        return coachCode;
    }
}
