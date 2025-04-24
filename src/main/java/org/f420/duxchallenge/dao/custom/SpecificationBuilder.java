package org.f420.duxchallenge.dao.custom;

import jakarta.persistence.criteria.Path;
import org.f420.duxchallenge.dto.Criteria;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.f420.duxchallenge.utils.ConvertUtils.convertToFieldType;

public class SpecificationBuilder<T> {

    public static <T> Specification<T> buildSpecification(Criteria criteria) {
        return (root, query, cb) -> {
            Path<String> path = root.get(criteria.getField());
            String value = criteria.getValue();
            Class<?> fieldType = path.getJavaType();
            Object convertedValue = convertToFieldType(value, fieldType);
            if (value == null) {
                return cb.conjunction();
            }
            return switch (criteria.getComparator()) {
                case EQUALS -> cb.equal(path, convertedValue);
                case LIKE -> cb.like(path.as(String.class), "%" + convertedValue + "%");
                case ILIKE -> cb.like(cb.lower(path.as(String.class)), "%" + convertedValue.toString().toLowerCase() + "%");
            };
        };
    }

    public static <T> Specification<T> buildSpecification(List<Criteria> criteriaList) {
        Specification<T> result = null;

        for (Criteria c : criteriaList) {
            Specification<T> current = buildSpecification(c);
            result = (result == null) ? current : result.and(current);
        }

        return result;
    }

}
