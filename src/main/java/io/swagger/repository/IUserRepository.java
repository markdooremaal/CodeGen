package io.swagger.repository;

import io.swagger.model.ArrayOfUsers;
import io.swagger.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<User, Long>
{
    User findByEmail(String email);
    User findById(int id);
    ArrayOfUsers findAll();
    void deleteById(int id);
}
