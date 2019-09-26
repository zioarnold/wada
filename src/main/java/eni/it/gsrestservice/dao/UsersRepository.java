package eni.it.gsrestservice.dao;

import eni.it.gsrestservice.model.FarmQSense;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends PagingAndSortingRepository<FarmQSense, String> {
}
