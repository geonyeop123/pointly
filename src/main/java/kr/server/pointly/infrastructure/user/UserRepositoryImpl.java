package kr.server.pointly.infrastructure.user;

import kr.server.pointly.domain.user.User;
import kr.server.pointly.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    @Override
    public Page<User> findAll(Pageable pageable) {
        return jpaUserRepository.findAll(pageable);
    }

    @Override
    public Optional<User> findByIdForUpdate(Long userId) {
        return jpaUserRepository.findByIdForUpdate(userId);
    }
}
