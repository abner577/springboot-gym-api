package practice.spring_gym_api.security.provider;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import practice.spring_gym_api.entity.WorkerEntity;
import practice.spring_gym_api.repository.WorkerRepository;
import practice.spring_gym_api.security.token.WorkerAuthToken;

import java.util.List;

@Component
public class WorkerAuthProvider implements AuthenticationProvider {

    private final WorkerRepository workerRepository;

    public WorkerAuthProvider(WorkerRepository workerRepository) {
        this.workerRepository = workerRepository;
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        WorkerAuthToken workerAuthToken = (WorkerAuthToken) authentication;
        Long workerId = workerAuthToken.getWorkerId();
        String workerCode = workerAuthToken.getWorkerCode();

        WorkerEntity workerEntity = workerRepository.findById(workerId)
                .orElseThrow(() -> new IllegalStateException("Worker with an id of: " + authentication.getPrincipal() + " doesnt exist"));

        WorkerEntity workerEntity1 = workerRepository.findByWorkerCode(workerEntity.getWorkerCode());
        if(workerEntity1 == null) throw new IllegalStateException("Worker with a worker code of: " + workerCode + " doesnt exist");
        if(!workerEntity.equals(workerEntity1)) throw new IllegalStateException("Worker with an id of: " + workerEntity.getId() + " is not the same worker with a worker code of: " + workerCode);

        return new WorkerAuthToken(workerId, workerCode, List.of(new SimpleGrantedAuthority("ROLE_WORKER")));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return WorkerAuthToken.class.isAssignableFrom(authentication);
    }
}
