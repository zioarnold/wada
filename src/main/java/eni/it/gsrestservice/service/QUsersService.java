package eni.it.gsrestservice.service;

import eni.it.gsrestservice.dao.QUsersRepository;
import eni.it.gsrestservice.model.DBConnectionOperation;
import eni.it.gsrestservice.model.QUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QUsersService {
    @Autowired
    QUsersRepository qUsersRepository;
    DBConnectionOperation dbConnectionOperation;

    public List<QUsers> getAllUsers() {
        return this.qUsersRepository.findAll();
    }

    public List<QUsers> findQUser(String userID) {
        dbConnectionOperation = new DBConnectionOperation();
        return dbConnectionOperation.findQUser(userID);
    }
}
