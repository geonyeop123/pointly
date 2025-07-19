package kr.server.pointly.domain.user;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserRepository {
    Page<User> findAll(Pageable pageable);
    Optional<User> findByIdForUpdate(Long userId);
}
