package com.synectiks.pref.service.mapper;

import com.synectiks.pref.domain.*;
import com.synectiks.pref.service.dto.SubjectDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Subject} and its DTO {@link SubjectDTO}.
 */
@Mapper(componentModel = "spring", uses = {DepartmentMapper.class, BatchMapper.class})
public interface SubjectMapper extends EntityMapper<SubjectDTO, Subject> {

    @Mapping(source = "department.id", target = "departmentId")
    @Mapping(source = "batch.id", target = "batchId")
    SubjectDTO toDto(Subject subject);

    @Mapping(source = "departmentId", target = "department")
    @Mapping(source = "batchId", target = "batch")
    Subject toEntity(SubjectDTO subjectDTO);

    default Subject fromId(Long id) {
        if (id == null) {
            return null;
        }
        Subject subject = new Subject();
        subject.setId(id);
        return subject;
    }
}
