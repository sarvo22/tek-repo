package com.tekfilo.sso.util;

import com.tekfilo.sso.base.FilterClause;
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

    public Specification getLoggedInCompanyFilter() {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get("isDeleted"), 0));
            return builder.and(predicates.toArray(new Predicate[]{}));
        };
    }


    public Specification getFilterClause(List<FilterClause> filterClause) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
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
                                    //predicates.add(builder.like(root.get(key), wrapToLike(value)));
                                    predicates.add(builder.like(builder.lower(root.get(key)), wrapToLike(value).toLowerCase()));
                                    break;
                                case "gt":
                                    predicates.add(builder.gt(root.get(key), Double.valueOf(value)));
                                    break;
                                case "dateeq":
                                    predicates.add(builder.equal(root.get(key), wrapToDate(value)));
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
        Date dt = Date.valueOf(value);
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

    public Specification getInClassFilter(List<Integer> ids, String column) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get("isDeleted"), 0));
            if (ids == null || ids.size() == 0) {
                predicates.add(builder.equal(root.get("isDeleted"), 2)); // to avoid fetching the data pass wrong condition
            }
            if (ids != null) {
                if (ids.size() > 0) {
                    predicates.add(
                            builder.in(root.get(column)).value(castToRequiredType(root.get(column).getJavaType(), ids))
                    );
                }
            }
            return builder.and(predicates.toArray(new Predicate[]{}));

        };
    }

    public Specification getReturnClassFilter(List<Integer> invIds, Integer productId) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get("isDeleted"), 0));
            predicates.add(builder.in(root.get("invId")).value(invIds));
            predicates.add(builder.equal(root.get("productId"), productId));
            return builder.and(predicates.toArray(new Predicate[]{}));
        };
    }

    private Object castToRequiredType(Object value) {
        if (value instanceof String) {
            return String.valueOf(value);
        }
        if (value instanceof Integer) {
            return Integer.valueOf(value.toString());
        }
        return value;
    }

    private Object castToRequiredType(Class fieldType, Integer value) {
        if (fieldType.isAssignableFrom(Double.class)) {
            return Double.valueOf(value);
        } else if (fieldType.isAssignableFrom(Integer.class)) {
            return Integer.valueOf(value);
        }
        return null;
    }

    private Object castStringToRequiredType(Class fieldType, String value) {
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

    private Object castStringToRequiredType(Class fieldType, List<String> value) {
        List<Object> list = new ArrayList<>();
        for (String i : value) {
            list.add(castStringToRequiredType(fieldType, i));
        }
        return list;
    }

    public Specification getStringInClassFilter(String column, List<String> ids) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get("isDeleted"), 0));
            if (ids != null) {
                if (ids.size() > 0) {
                    predicates.add(

                            builder.in(root.get(column)).value(castStringToRequiredType(root.get(column).getJavaType(), ids))
                    );
                }
            }
            return builder.and(predicates.toArray(new Predicate[]{}));

        };
    }
}
