package com.tekfilo.admin.util;

import com.tekfilo.admin.base.FilterClause;
import com.tekfilo.admin.multitenancy.CompanyContext;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FilterClauseAppender {

    public static String wrapToLike(String value) {
        return value == null ? "%" : "%" + value + "%";
    }

    public Specification getCompanyFilter() {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get("companyId"), CompanyContext.getCurrentCompany()));
            predicates.add(builder.equal(root.get("isDeleted"), 0));
            return builder.and(predicates.toArray(new Predicate[]{}));
        };
    }

    public Specification getFilterClause(List<FilterClause> filterClause) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get("companyId"), CompanyContext.getCurrentCompany()));
            predicates.add(builder.equal(root.get("isDeleted"), 0));
            if (filterClause != null) {
                if (filterClause.size() > 0) {
                    filterClause.stream().forEach(e -> {
                        final String key = e.getKey();
                        final String value = e.getValue();
                        if (Optional.ofNullable(key).isPresent() && Optional.ofNullable(value).isPresent())
                            switch (getOperator(e.getOperator())) {
                                case "neq":
                                    predicates.add(builder.notEqual(root.get(key), value));
                                    break;
                                case "like":
                                    predicates.add(builder.like(builder.lower(root.get(key)), wrapToLike(value).toLowerCase()));
                                    break;
                                case "gt":
                                    predicates.add(builder.gt(root.get(key), Double.valueOf(value)));
                                case "dateeq":
                                    predicates.add(builder.equal(root.get(key), wrapToDate(value)));
                                    break;
                                case "dategt":
                                    predicates.add(builder.greaterThanOrEqualTo(root.get(key), wrapToDate(value)));
                                    break;
                                case "dateleeq":
                                    predicates.add(builder.lessThanOrEqualTo(root.get(key), wrapToDate(value)));
                                    break;
                                default:
                                    predicates.add(builder.equal(root.get(key), value));
                                    break;
                            }


                    });
                }
            }
            return builder.and(predicates.toArray(new Predicate[]{}));

        };
    }

    private Date wrapToDate(String value) {
        java.sql.Date dt = Date.valueOf(value);
        return dt;
    }

    private String getOperator(String operator) {
        final String defaultOperator = "eq";
        if (operator == null)
            return defaultOperator;
        return operator.toLowerCase();
    }

    public Specification getInClassFilter(List<Integer> ids) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get("companyId"), CompanyContext.getCurrentCompany()));
            predicates.add(builder.equal(root.get("isDeleted"), 0));
            if (ids != null) {
                if (ids.size() > 0) {
                    predicates.add(
                            builder.in(root.get("id")).value(castToRequiredType(root.get("id").getJavaType(), ids))
                    );
                }
            }
            return builder.and(predicates.toArray(new Predicate[]{}));

        };
    }

    public Specification getCustomInClassFilter(String column, List<Integer> ids) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get("isDeleted"), 0));
            if (ids != null) {
                if (ids.size() > 0) {
                    predicates.add(
                            builder.in(root.get("id")).value(castToRequiredType(root.get(column).getJavaType(), ids))
                    );
                }
            }
            return builder.and(predicates.toArray(new Predicate[]{}));

        };
    }

    private Object castToRequiredType(Class fieldType, Integer value) {
        if (fieldType.isAssignableFrom(Double.class)) {
            return Double.valueOf(value);
        } else if (fieldType.isAssignableFrom(Integer.class)) {
            return Integer.valueOf(value);
        }
        return null;
    }

    private Object castToRequiredType(Class fieldType, List<Integer> value) {
        List<Object> list = new ArrayList<>();
        for (Integer i : value) {
            list.add(castToRequiredType(fieldType, i));
        }
        return list;
    }
}
