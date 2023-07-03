package com.tekfilo.account.util;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class AccountsUtil {

    public static Specification getInClassFilter(String columnKey, List<Integer> ids) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (ids != null) {
                if (ids.size() > 0) {
                    predicates.add(

                            builder.in(root.get(columnKey)).value(castToRequiredType(root.get(columnKey).getJavaType(), ids))
                    );
                }
            }
            return builder.and(predicates.toArray(new Predicate[]{}));

        };
    }

    public static Object castToRequiredType(Class fieldType, List<Integer> value) {
        List<Object> list = new ArrayList<>();
        for (Integer i : value) {
            list.add(castToRequiredType(fieldType, i));
        }
        return list;
    }

    public static Object castToRequiredType(Class fieldType, Integer value) {
        if (fieldType.isAssignableFrom(Double.class)) {
            return Double.valueOf(value);
        } else if (fieldType.isAssignableFrom(Integer.class)) {
            return Integer.valueOf(value);
        }
        return null;
    }
}
