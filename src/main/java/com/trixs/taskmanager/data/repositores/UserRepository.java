package com.trixs.taskmanager.data.repositores;

import com.trixs.taskmanager.data.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
}
