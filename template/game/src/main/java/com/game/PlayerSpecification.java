package com.game;

import com.game.entity.Player;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;

public class PlayerSpecification implements Specification<Player> {
    private SearchCriteria criteria;

    public PlayerSpecification(SearchCriteria searchCriteria) {
        criteria = searchCriteria;
    }

    @Override
    public Predicate toPredicate
            (Root<Player> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (criteria.getOperation().equalsIgnoreCase(">")) {
            if (root.get(criteria.getKey()).getJavaType() == Date.class) {
                Date date = new Date((Long) criteria.getValue());
                return builder
                        .greaterThanOrEqualTo(root.get(criteria.getKey()), date);
            } else
                return builder
                        .greaterThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());
        } else if (criteria.getOperation().equalsIgnoreCase("<")) {
            if (root.get(criteria.getKey()).getJavaType() == Date.class) {
                Date date = new Date((Long) criteria.getValue());
                return builder
                        .lessThanOrEqualTo(root.get(criteria.getKey()), date);
            } else
                return builder.lessThanOrEqualTo(
                        root.get(criteria.getKey()), criteria.getValue().toString());
        } else if (criteria.getOperation().equalsIgnoreCase(":")) {
            if (root.get(criteria.getKey()).getJavaType() == String.class) {
                return builder.like(
                        root.<String>get(criteria.getKey()), "%" + criteria.getValue() + "%");
            } else {
                return builder.equal(root.get(criteria.getKey()), criteria.getValue());
            }
        }
        return null;
    }
}
