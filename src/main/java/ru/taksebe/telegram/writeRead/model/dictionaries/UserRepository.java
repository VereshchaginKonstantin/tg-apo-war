package ru.taksebe.telegram.writeRead.model.dictionaries;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.taksebe.telegram.writeRead.model.User;

@Repository
public interface UserRepository  extends CrudRepository<User, String> {
}