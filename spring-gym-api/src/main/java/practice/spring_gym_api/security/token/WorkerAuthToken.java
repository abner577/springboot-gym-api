package practice.spring_gym_api.security.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Collection;

public class WorkerAuthToken extends AbstractAuthenticationToken {

    private final Long workerId;
    private final String workerCode;

    public WorkerAuthToken(Long workerId, String workerCode){
        super(null);
        this.workerId = workerId;
        this.workerCode = workerCode;
        setAuthenticated(false);
    }

    // Constructor for authenticated token
    public WorkerAuthToken(Long workerId, String workerCode, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.workerId = workerId;
        this.workerCode = workerCode;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return this.workerCode;
    }

    @Override
    public Object getPrincipal() {
        return this.workerId;
    }
}
