package eni.it.gsrestservice.service.post;


import eni.it.gsrestservice.entities.postgres.QsDevUsersAttrib;
import eni.it.gsrestservice.repos.post.QsDevUsersAttributesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Zio Arnold aka Arni
 * @created 11/04/2025 - 14:41 </br>
 */
@Service
@RequiredArgsConstructor
public class QsDevUsersAttributesService {
    private final QsDevUsersAttributesRepository qsDevUsersAttributesRepository;

    List<QsDevUsersAttrib> findAllFarms() {
        return qsDevUsersAttributesRepository.findAll();
    }
}
