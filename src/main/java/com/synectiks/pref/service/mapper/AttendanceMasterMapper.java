package com.synectiks.pref.service.mapper;

import com.synectiks.pref.domain.*;
import com.synectiks.pref.service.dto.AttendanceMasterDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link AttendanceMaster} and its DTO {@link AttendanceMasterDTO}.
 */
@Mapper(componentModel = "spring", uses = {BatchMapper.class, SectionMapper.class, TeachMapper.class})
public interface AttendanceMasterMapper extends EntityMapper<AttendanceMasterDTO, AttendanceMaster> {

    @Mapping(source = "batch.id", target = "batchId")
    @Mapping(source = "section.id", target = "sectionId")
    @Mapping(source = "teach.id", target = "teachId")
    AttendanceMasterDTO toDto(AttendanceMaster attendanceMaster);

    @Mapping(source = "batchId", target = "batch")
    @Mapping(source = "sectionId", target = "section")
    @Mapping(source = "teachId", target = "teach")
    AttendanceMaster toEntity(AttendanceMasterDTO attendanceMasterDTO);

    default AttendanceMaster fromId(Long id) {
        if (id == null) {
            return null;
        }
        AttendanceMaster attendanceMaster = new AttendanceMaster();
        attendanceMaster.setId(id);
        return attendanceMaster;
    }
}
