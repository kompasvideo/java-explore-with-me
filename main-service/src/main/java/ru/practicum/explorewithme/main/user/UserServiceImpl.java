package ru.practicum.explorewithme.main.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.main.exception.NotFoundException;
import ru.practicum.explorewithme.main.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.domain.PageRequest.of;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> findUsers(List<Long> ids, Integer from, Integer size) {
        if (ids == null) {
            return userRepository.findAll(PageRequest.of(from, size)).toList();
        }
        return userRepository.findAllByIds(ids, of(from, size))
                .stream()
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userRepository.findById(id).orElseThrow(() -> new NotFoundException(id.toString()));
        userRepository.deleteById(id);
    }
}
