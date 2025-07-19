package kr.server.pointly.infrastructure.user;

import kr.server.pointly.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepository extends JpaRepository<User, Long> {

}
