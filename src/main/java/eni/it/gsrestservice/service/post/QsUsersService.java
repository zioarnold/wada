package eni.it.gsrestservice.service.post;


import eni.it.gsrestservice.db.repos.post.QsUsersRepository;
import eni.it.gsrestservice.entities.postgres.QsUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author Zio Arnold aka Arni
 * @created 11/04/2025 - 16:20 </br>
 */
@Service
@RequiredArgsConstructor
public class QsUsersService {
    private final QsUsersRepository qsUsersRepository;

    public QsUser findById(Long id) {
        return qsUsersRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Unable to find user with id:" + id));
    }

    public List<QsUser> findAll() {
        return qsUsersRepository.findAll();
    }
}
