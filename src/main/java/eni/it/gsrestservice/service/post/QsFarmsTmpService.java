package eni.it.gsrestservice.service.post;


import eni.it.gsrestservice.repos.post.QsFarmsTmpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Zio Arnold aka Arni
 * @created 13/04/2025 - 22:58 </br>
 */
@Service
@RequiredArgsConstructor
public class QsFarmsTmpService {
    private final QsFarmsTmpRepository qsFarmsTmpRepository;
}
