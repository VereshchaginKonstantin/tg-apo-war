package ru.verekonn.telegram.appowar.model.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.verekonn.telegram.appowar.model.User;

@Repository
public interface UserRepository  extends CrudRepository<User, String> {
}