package io.swagger.repository;

import io.swagger.model.ArrayOfUsers;
import io.swagger.model.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IUserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User>
{
    User findByEmail(String email);
    User findById(int id);
    ArrayOfUsers findAll();
    void deleteById(int id);
    List<User> findAll(Specification<User> specification);
}
