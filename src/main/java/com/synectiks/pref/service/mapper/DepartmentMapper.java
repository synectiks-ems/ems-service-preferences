package com.synectiks.pref.service.mapper;

import com.synectiks.pref.domain.*;
import com.synectiks.pref.service.dto.DepartmentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Department} and its DTO {@link DepartmentDTO}.
 */
@Mapper(componentModel = "spring", uses = {BranchMapper.class, AcademicYearMapper.class})
public interface DepartmentMapper extends EntityMapper<DepartmentDTO, Department> {

    @Mapping(source = "branch.id", target = "branchId")
    @Mapping(source = "academicYear.id", target = "academicYearId")
    DepartmentDTO toDto(Department department);

    @Mapping(source = "branchId", target = "branch")
    @Mapping(source = "academicYearId", target = "academicYear")
    Department toEntity(DepartmentDTO departmentDTO);

    default Department fromId(Long id) {
        if (id == null) {
            return null;
        }
        Department department = new Department();
        department.setId(id);
        return department;
    }
}
