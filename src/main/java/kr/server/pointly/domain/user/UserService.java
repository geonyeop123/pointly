package kr.server.pointly.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<User> findAll(UserCommand.FindAll command) {
        // Pageable 을 활용한 페이징 처리 시 0부터 페이지를 count 하기 때문에 1 감소
        int pageNo = command.page() - 1;
        Pageable pageable = PageRequest.of(pageNo, command.size(), command.getSort());

        return userRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public User find(UserCommand.Find command){
        return userRepository.findById(command.userId())
                .orElseThrow(() -> new IllegalArgumentException("해당되는 유저가 없습니다."));
    }

    public User addView(UserCommand.AddView command) {
        User user = userRepository.findByIdForUpdate(command.userId())
                .orElseThrow(() -> new IllegalArgumentException("해당되는 유저가 없습니다."));
        user.increaseViewCount();
        return user;
    }

}
