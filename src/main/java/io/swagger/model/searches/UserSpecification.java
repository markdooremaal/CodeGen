package io.swagger.model.searches;

import io.swagger.model.User;
import io.swagger.model.enums.Role;
import io.swagger.model.enums.Status;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.Predicate;

//Class to create a search specification based on set criteria
public class UserSpecification implements Specification<User> {
    private UserSearch criteria;

    public UserSpecification(UserSearch criteria) {
        this.criteria = criteria;
    }

    //Generate the predicate
    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query,
                                 CriteriaBuilder cb) {
        Path<String> firstName = root.get("firstName");
        Path<String> lastName = root.get("lastName");
        Path<String> email = root.get("email");
        Path<Role> role = root.get("role");
        Path<Status> status = root.get("status");

        final List<Predicate> predicates = new ArrayList<>();
        if(criteria.getFirstName()!=null) {
            predicates.add(cb.like(firstName, criteria.getFirstName()));
        }
        if(criteria.getLastName()!=null) {
            predicates.add(cb.like(lastName, criteria.getLastName()));
        }
        if(criteria.getEmail()!=null) {
            predicates.add(cb.like(email, criteria.getEmail()));
        }
        if(criteria.getRole()!=null) {
            predicates.add(cb.equal(role, criteria.getRole()));
        }
        if(criteria.getStatus()!=null) {
            predicates.add(cb.equal(status, criteria.getStatus()));
        }

        return cb.and(predicates.toArray(new Predicate[predicates.size()]));
    }
}