package ru.taksebe.telegram.writeRead.api.dictionaries;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.taksebe.telegram.writeRead.model.Battle;

@Repository
public interface BattleRepository  extends CrudRepository<Battle, String> {
}
