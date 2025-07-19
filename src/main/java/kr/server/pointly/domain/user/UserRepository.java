package kr.server.pointly.domain.user;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepository {
    Page<User> findAll(Pageable pageable);
}
