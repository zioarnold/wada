package eni.it.gsrestservice.service.post;


import eni.it.gsrestservice.repos.post.QsDevUsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Zio Arnold aka Arni
 * @created 13/04/2025 - 22:56 </br>
 */
@Service
@RequiredArgsConstructor
public class QsDevUsersService {
    private final QsDevUsersRepository qsDevUsersRepository;
}
