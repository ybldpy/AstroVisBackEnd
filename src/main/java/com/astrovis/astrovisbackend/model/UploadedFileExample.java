package com.astrovis.astrovisbackend.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UploadedFileExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_uploaded_file
     *
     * @mbg.generated Sun Feb 02 19:18:11 EST 2025
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_uploaded_file
     *
     * @mbg.generated Sun Feb 02 19:18:11 EST 2025
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_uploaded_file
     *
     * @mbg.generated Sun Feb 02 19:18:11 EST 2025
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_uploaded_file
     *
     * @mbg.generated Sun Feb 02 19:18:11 EST 2025
     */
    public UploadedFileExample() {
        oredCriteria = new ArrayList<>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_uploaded_file
     *
     * @mbg.generated Sun Feb 02 19:18:11 EST 2025
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_uploaded_file
     *
     * @mbg.generated Sun Feb 02 19:18:11 EST 2025
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_uploaded_file
     *
     * @mbg.generated Sun Feb 02 19:18:11 EST 2025
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_uploaded_file
     *
     * @mbg.generated Sun Feb 02 19:18:11 EST 2025
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_uploaded_file
     *
     * @mbg.generated Sun Feb 02 19:18:11 EST 2025
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_uploaded_file
     *
     * @mbg.generated Sun Feb 02 19:18:11 EST 2025
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_uploaded_file
     *
     * @mbg.generated Sun Feb 02 19:18:11 EST 2025
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_uploaded_file
     *
     * @mbg.generated Sun Feb 02 19:18:11 EST 2025
     */
    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_uploaded_file
     *
     * @mbg.generated Sun Feb 02 19:18:11 EST 2025
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_uploaded_file
     *
     * @mbg.generated Sun Feb 02 19:18:11 EST 2025
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table t_uploaded_file
     *
     * @mbg.generated Sun Feb 02 19:18:11 EST 2025
     */
    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andFileidIsNull() {
            addCriterion("fileId is null");
            return (Criteria) this;
        }

        public Criteria andFileidIsNotNull() {
            addCriterion("fileId is not null");
            return (Criteria) this;
        }

        public Criteria andFileidEqualTo(String value) {
            addCriterion("fileId =", value, "fileid");
            return (Criteria) this;
        }

        public Criteria andFileidNotEqualTo(String value) {
            addCriterion("fileId <>", value, "fileid");
            return (Criteria) this;
        }

        public Criteria andFileidGreaterThan(String value) {
            addCriterion("fileId >", value, "fileid");
            return (Criteria) this;
        }

        public Criteria andFileidGreaterThanOrEqualTo(String value) {
            addCriterion("fileId >=", value, "fileid");
            return (Criteria) this;
        }

        public Criteria andFileidLessThan(String value) {
            addCriterion("fileId <", value, "fileid");
            return (Criteria) this;
        }

        public Criteria andFileidLessThanOrEqualTo(String value) {
            addCriterion("fileId <=", value, "fileid");
            return (Criteria) this;
        }

        public Criteria andFileidLike(String value) {
            addCriterion("fileId like", value, "fileid");
            return (Criteria) this;
        }

        public Criteria andFileidNotLike(String value) {
            addCriterion("fileId not like", value, "fileid");
            return (Criteria) this;
        }

        public Criteria andFileidIn(List<String> values) {
            addCriterion("fileId in", values, "fileid");
            return (Criteria) this;
        }

        public Criteria andFileidNotIn(List<String> values) {
            addCriterion("fileId not in", values, "fileid");
            return (Criteria) this;
        }

        public Criteria andFileidBetween(String value1, String value2) {
            addCriterion("fileId between", value1, value2, "fileid");
            return (Criteria) this;
        }

        public Criteria andFileidNotBetween(String value1, String value2) {
            addCriterion("fileId not between", value1, value2, "fileid");
            return (Criteria) this;
        }

        public Criteria andFilenameIsNull() {
            addCriterion("fileName is null");
            return (Criteria) this;
        }

        public Criteria andFilenameIsNotNull() {
            addCriterion("fileName is not null");
            return (Criteria) this;
        }

        public Criteria andFilenameEqualTo(String value) {
            addCriterion("fileName =", value, "filename");
            return (Criteria) this;
        }

        public Criteria andFilenameNotEqualTo(String value) {
            addCriterion("fileName <>", value, "filename");
            return (Criteria) this;
        }

        public Criteria andFilenameGreaterThan(String value) {
            addCriterion("fileName >", value, "filename");
            return (Criteria) this;
        }

        public Criteria andFilenameGreaterThanOrEqualTo(String value) {
            addCriterion("fileName >=", value, "filename");
            return (Criteria) this;
        }

        public Criteria andFilenameLessThan(String value) {
            addCriterion("fileName <", value, "filename");
            return (Criteria) this;
        }

        public Criteria andFilenameLessThanOrEqualTo(String value) {
            addCriterion("fileName <=", value, "filename");
            return (Criteria) this;
        }

        public Criteria andFilenameLike(String value) {
            addCriterion("fileName like", value, "filename");
            return (Criteria) this;
        }

        public Criteria andFilenameNotLike(String value) {
            addCriterion("fileName not like", value, "filename");
            return (Criteria) this;
        }

        public Criteria andFilenameIn(List<String> values) {
            addCriterion("fileName in", values, "filename");
            return (Criteria) this;
        }

        public Criteria andFilenameNotIn(List<String> values) {
            addCriterion("fileName not in", values, "filename");
            return (Criteria) this;
        }

        public Criteria andFilenameBetween(String value1, String value2) {
            addCriterion("fileName between", value1, value2, "filename");
            return (Criteria) this;
        }

        public Criteria andFilenameNotBetween(String value1, String value2) {
            addCriterion("fileName not between", value1, value2, "filename");
            return (Criteria) this;
        }

        public Criteria andUploaderIsNull() {
            addCriterion("uploader is null");
            return (Criteria) this;
        }

        public Criteria andUploaderIsNotNull() {
            addCriterion("uploader is not null");
            return (Criteria) this;
        }

        public Criteria andUploaderEqualTo(Integer value) {
            addCriterion("uploader =", value, "uploader");
            return (Criteria) this;
        }

        public Criteria andUploaderNotEqualTo(Integer value) {
            addCriterion("uploader <>", value, "uploader");
            return (Criteria) this;
        }

        public Criteria andUploaderGreaterThan(Integer value) {
            addCriterion("uploader >", value, "uploader");
            return (Criteria) this;
        }

        public Criteria andUploaderGreaterThanOrEqualTo(Integer value) {
            addCriterion("uploader >=", value, "uploader");
            return (Criteria) this;
        }

        public Criteria andUploaderLessThan(Integer value) {
            addCriterion("uploader <", value, "uploader");
            return (Criteria) this;
        }

        public Criteria andUploaderLessThanOrEqualTo(Integer value) {
            addCriterion("uploader <=", value, "uploader");
            return (Criteria) this;
        }

        public Criteria andUploaderIn(List<Integer> values) {
            addCriterion("uploader in", values, "uploader");
            return (Criteria) this;
        }

        public Criteria andUploaderNotIn(List<Integer> values) {
            addCriterion("uploader not in", values, "uploader");
            return (Criteria) this;
        }

        public Criteria andUploaderBetween(Integer value1, Integer value2) {
            addCriterion("uploader between", value1, value2, "uploader");
            return (Criteria) this;
        }

        public Criteria andUploaderNotBetween(Integer value1, Integer value2) {
            addCriterion("uploader not between", value1, value2, "uploader");
            return (Criteria) this;
        }

        public Criteria andFilestatusIsNull() {
            addCriterion("fileStatus is null");
            return (Criteria) this;
        }

        public Criteria andFilestatusIsNotNull() {
            addCriterion("fileStatus is not null");
            return (Criteria) this;
        }

        public Criteria andFilestatusEqualTo(Integer value) {
            addCriterion("fileStatus =", value, "filestatus");
            return (Criteria) this;
        }

        public Criteria andFilestatusNotEqualTo(Integer value) {
            addCriterion("fileStatus <>", value, "filestatus");
            return (Criteria) this;
        }

        public Criteria andFilestatusGreaterThan(Integer value) {
            addCriterion("fileStatus >", value, "filestatus");
            return (Criteria) this;
        }

        public Criteria andFilestatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("fileStatus >=", value, "filestatus");
            return (Criteria) this;
        }

        public Criteria andFilestatusLessThan(Integer value) {
            addCriterion("fileStatus <", value, "filestatus");
            return (Criteria) this;
        }

        public Criteria andFilestatusLessThanOrEqualTo(Integer value) {
            addCriterion("fileStatus <=", value, "filestatus");
            return (Criteria) this;
        }

        public Criteria andFilestatusIn(List<Integer> values) {
            addCriterion("fileStatus in", values, "filestatus");
            return (Criteria) this;
        }

        public Criteria andFilestatusNotIn(List<Integer> values) {
            addCriterion("fileStatus not in", values, "filestatus");
            return (Criteria) this;
        }

        public Criteria andFilestatusBetween(Integer value1, Integer value2) {
            addCriterion("fileStatus between", value1, value2, "filestatus");
            return (Criteria) this;
        }

        public Criteria andFilestatusNotBetween(Integer value1, Integer value2) {
            addCriterion("fileStatus not between", value1, value2, "filestatus");
            return (Criteria) this;
        }

        public Criteria andCategoryIsNull() {
            addCriterion("category is null");
            return (Criteria) this;
        }

        public Criteria andCategoryIsNotNull() {
            addCriterion("category is not null");
            return (Criteria) this;
        }

        public Criteria andCategoryEqualTo(Integer value) {
            addCriterion("category =", value, "category");
            return (Criteria) this;
        }

        public Criteria andCategoryNotEqualTo(Integer value) {
            addCriterion("category <>", value, "category");
            return (Criteria) this;
        }

        public Criteria andCategoryGreaterThan(Integer value) {
            addCriterion("category >", value, "category");
            return (Criteria) this;
        }

        public Criteria andCategoryGreaterThanOrEqualTo(Integer value) {
            addCriterion("category >=", value, "category");
            return (Criteria) this;
        }

        public Criteria andCategoryLessThan(Integer value) {
            addCriterion("category <", value, "category");
            return (Criteria) this;
        }

        public Criteria andCategoryLessThanOrEqualTo(Integer value) {
            addCriterion("category <=", value, "category");
            return (Criteria) this;
        }

        public Criteria andCategoryIn(List<Integer> values) {
            addCriterion("category in", values, "category");
            return (Criteria) this;
        }

        public Criteria andCategoryNotIn(List<Integer> values) {
            addCriterion("category not in", values, "category");
            return (Criteria) this;
        }

        public Criteria andCategoryBetween(Integer value1, Integer value2) {
            addCriterion("category between", value1, value2, "category");
            return (Criteria) this;
        }

        public Criteria andCategoryNotBetween(Integer value1, Integer value2) {
            addCriterion("category not between", value1, value2, "category");
            return (Criteria) this;
        }

        public Criteria andLastaccessedIsNull() {
            addCriterion("lastAccessed is null");
            return (Criteria) this;
        }

        public Criteria andLastaccessedIsNotNull() {
            addCriterion("lastAccessed is not null");
            return (Criteria) this;
        }

        public Criteria andLastaccessedEqualTo(Date value) {
            addCriterion("lastAccessed =", value, "lastaccessed");
            return (Criteria) this;
        }

        public Criteria andLastaccessedNotEqualTo(Date value) {
            addCriterion("lastAccessed <>", value, "lastaccessed");
            return (Criteria) this;
        }

        public Criteria andLastaccessedGreaterThan(Date value) {
            addCriterion("lastAccessed >", value, "lastaccessed");
            return (Criteria) this;
        }

        public Criteria andLastaccessedGreaterThanOrEqualTo(Date value) {
            addCriterion("lastAccessed >=", value, "lastaccessed");
            return (Criteria) this;
        }

        public Criteria andLastaccessedLessThan(Date value) {
            addCriterion("lastAccessed <", value, "lastaccessed");
            return (Criteria) this;
        }

        public Criteria andLastaccessedLessThanOrEqualTo(Date value) {
            addCriterion("lastAccessed <=", value, "lastaccessed");
            return (Criteria) this;
        }

        public Criteria andLastaccessedIn(List<Date> values) {
            addCriterion("lastAccessed in", values, "lastaccessed");
            return (Criteria) this;
        }

        public Criteria andLastaccessedNotIn(List<Date> values) {
            addCriterion("lastAccessed not in", values, "lastaccessed");
            return (Criteria) this;
        }

        public Criteria andLastaccessedBetween(Date value1, Date value2) {
            addCriterion("lastAccessed between", value1, value2, "lastaccessed");
            return (Criteria) this;
        }

        public Criteria andLastaccessedNotBetween(Date value1, Date value2) {
            addCriterion("lastAccessed not between", value1, value2, "lastaccessed");
            return (Criteria) this;
        }

        public Criteria andOriginalfilenameIsNull() {
            addCriterion("originalFileName is null");
            return (Criteria) this;
        }

        public Criteria andOriginalfilenameIsNotNull() {
            addCriterion("originalFileName is not null");
            return (Criteria) this;
        }

        public Criteria andOriginalfilenameEqualTo(String value) {
            addCriterion("originalFileName =", value, "originalfilename");
            return (Criteria) this;
        }

        public Criteria andOriginalfilenameNotEqualTo(String value) {
            addCriterion("originalFileName <>", value, "originalfilename");
            return (Criteria) this;
        }

        public Criteria andOriginalfilenameGreaterThan(String value) {
            addCriterion("originalFileName >", value, "originalfilename");
            return (Criteria) this;
        }

        public Criteria andOriginalfilenameGreaterThanOrEqualTo(String value) {
            addCriterion("originalFileName >=", value, "originalfilename");
            return (Criteria) this;
        }

        public Criteria andOriginalfilenameLessThan(String value) {
            addCriterion("originalFileName <", value, "originalfilename");
            return (Criteria) this;
        }

        public Criteria andOriginalfilenameLessThanOrEqualTo(String value) {
            addCriterion("originalFileName <=", value, "originalfilename");
            return (Criteria) this;
        }

        public Criteria andOriginalfilenameLike(String value) {
            addCriterion("originalFileName like", value, "originalfilename");
            return (Criteria) this;
        }

        public Criteria andOriginalfilenameNotLike(String value) {
            addCriterion("originalFileName not like", value, "originalfilename");
            return (Criteria) this;
        }

        public Criteria andOriginalfilenameIn(List<String> values) {
            addCriterion("originalFileName in", values, "originalfilename");
            return (Criteria) this;
        }

        public Criteria andOriginalfilenameNotIn(List<String> values) {
            addCriterion("originalFileName not in", values, "originalfilename");
            return (Criteria) this;
        }

        public Criteria andOriginalfilenameBetween(String value1, String value2) {
            addCriterion("originalFileName between", value1, value2, "originalfilename");
            return (Criteria) this;
        }

        public Criteria andOriginalfilenameNotBetween(String value1, String value2) {
            addCriterion("originalFileName not between", value1, value2, "originalfilename");
            return (Criteria) this;
        }

        public Criteria andParametersIsNull() {
            addCriterion("parameters is null");
            return (Criteria) this;
        }

        public Criteria andParametersIsNotNull() {
            addCriterion("parameters is not null");
            return (Criteria) this;
        }

        public Criteria andParametersEqualTo(String value) {
            addCriterion("parameters =", value, "parameters");
            return (Criteria) this;
        }

        public Criteria andParametersNotEqualTo(String value) {
            addCriterion("parameters <>", value, "parameters");
            return (Criteria) this;
        }

        public Criteria andParametersGreaterThan(String value) {
            addCriterion("parameters >", value, "parameters");
            return (Criteria) this;
        }

        public Criteria andParametersGreaterThanOrEqualTo(String value) {
            addCriterion("parameters >=", value, "parameters");
            return (Criteria) this;
        }

        public Criteria andParametersLessThan(String value) {
            addCriterion("parameters <", value, "parameters");
            return (Criteria) this;
        }

        public Criteria andParametersLessThanOrEqualTo(String value) {
            addCriterion("parameters <=", value, "parameters");
            return (Criteria) this;
        }

        public Criteria andParametersLike(String value) {
            addCriterion("parameters like", value, "parameters");
            return (Criteria) this;
        }

        public Criteria andParametersNotLike(String value) {
            addCriterion("parameters not like", value, "parameters");
            return (Criteria) this;
        }

        public Criteria andParametersIn(List<String> values) {
            addCriterion("parameters in", values, "parameters");
            return (Criteria) this;
        }

        public Criteria andParametersNotIn(List<String> values) {
            addCriterion("parameters not in", values, "parameters");
            return (Criteria) this;
        }

        public Criteria andParametersBetween(String value1, String value2) {
            addCriterion("parameters between", value1, value2, "parameters");
            return (Criteria) this;
        }

        public Criteria andParametersNotBetween(String value1, String value2) {
            addCriterion("parameters not between", value1, value2, "parameters");
            return (Criteria) this;
        }

        public Criteria andDeletedIsNull() {
            addCriterion("deleted is null");
            return (Criteria) this;
        }

        public Criteria andDeletedIsNotNull() {
            addCriterion("deleted is not null");
            return (Criteria) this;
        }

        public Criteria andDeletedEqualTo(Boolean value) {
            addCriterion("deleted =", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedNotEqualTo(Boolean value) {
            addCriterion("deleted <>", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedGreaterThan(Boolean value) {
            addCriterion("deleted >", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedGreaterThanOrEqualTo(Boolean value) {
            addCriterion("deleted >=", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedLessThan(Boolean value) {
            addCriterion("deleted <", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedLessThanOrEqualTo(Boolean value) {
            addCriterion("deleted <=", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedIn(List<Boolean> values) {
            addCriterion("deleted in", values, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedNotIn(List<Boolean> values) {
            addCriterion("deleted not in", values, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedBetween(Boolean value1, Boolean value2) {
            addCriterion("deleted between", value1, value2, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedNotBetween(Boolean value1, Boolean value2) {
            addCriterion("deleted not between", value1, value2, "deleted");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table t_uploaded_file
     *
     * @mbg.generated do_not_delete_during_merge Sun Feb 02 19:18:11 EST 2025
     */
    public static class Criteria extends GeneratedCriteria {
        protected Criteria() {
            super();
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table t_uploaded_file
     *
     * @mbg.generated Sun Feb 02 19:18:11 EST 2025
     */
    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}