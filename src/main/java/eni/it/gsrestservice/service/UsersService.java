package eni.it.gsrestservice.service;

import eni.it.gsrestservice.dao.UsersRepository;
import eni.it.gsrestservice.model.FarmQSense;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UsersService {
    private final UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public List<FarmQSense> findAllQUsersFromDB() {
        List<FarmQSense> users = new ArrayList<>();
        for (FarmQSense u : usersRepository.findAll()) {
            users.add(u);
        }
        return users;
    }
}
