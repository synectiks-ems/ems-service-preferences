package com.synectiks.pref.domain;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A ExceptionRecord.
 */
@Entity
@Table(name = "exception_record")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "exceptionrecord")
public class ExceptionRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "exception_source")
    private String exceptionSource;

    @Column(name = "exception_type")
    private String exceptionType;

    @Size(max = 5000)
    @Column(name = "exception", length = 5000)
    private String exception;

    @Column(name = "exception_date")
    private LocalDate exceptionDate;

    @Column(name = "jhi_user")
    private String user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExceptionSource() {
        return exceptionSource;
    }

    public ExceptionRecord exceptionSource(String exceptionSource) {
        this.exceptionSource = exceptionSource;
        return this;
    }

    public void setExceptionSource(String exceptionSource) {
        this.exceptionSource = exceptionSource;
    }

    public String getExceptionType() {
        return exceptionType;
    }

    public ExceptionRecord exceptionType(String exceptionType) {
        this.exceptionType = exceptionType;
        return this;
    }

    public void setExceptionType(String exceptionType) {
        this.exceptionType = exceptionType;
    }

    public String getException() {
        return exception;
    }

    public ExceptionRecord exception(String exception) {
        this.exception = exception;
        return this;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public LocalDate getExceptionDate() {
        return exceptionDate;
    }

    public ExceptionRecord exceptionDate(LocalDate exceptionDate) {
        this.exceptionDate = exceptionDate;
        return this;
    }

    public void setExceptionDate(LocalDate exceptionDate) {
        this.exceptionDate = exceptionDate;
    }

    public String getUser() {
        return user;
    }

    public ExceptionRecord user(String user) {
        this.user = user;
        return this;
    }

    public void setUser(String user) {
        this.user = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExceptionRecord)) {
            return false;
        }
        return id != null && id.equals(((ExceptionRecord) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ExceptionRecord{" +
            "id=" + getId() +
            ", exceptionSource='" + getExceptionSource() + "'" +
            ", exceptionType='" + getExceptionType() + "'" +
            ", exception='" + getException() + "'" +
            ", exceptionDate='" + getExceptionDate() + "'" +
            ", user='" + getUser() + "'" +
            "}";
    }
}
