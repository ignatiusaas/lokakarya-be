package co.id.ogya.lokakarya.services.impl;

import co.id.ogya.lokakarya.dto.division.DivisionCreateDto;
import co.id.ogya.lokakarya.dto.division.DivisionDto;
import co.id.ogya.lokakarya.dto.division.DivisionUpdateDto;
import co.id.ogya.lokakarya.services.DivisionServ;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional(rollbackOn = Exception.class)
public class DivisionServImpl implements DivisionServ {
    @Autowired
    private DivisionRepo divisionRepo;

    @Override
    public List<DivisionDto> getAllDivision() {
        log.info("Attempting to fetch all Divisions");
        List<DivisionDto> listResult = new ArrayList<>();
        try {
            List<Division> listData = divisionRepo.getDivisions();
            log.debug("Fetched {} Divisions from repository", listData.size());
            for (Division data : listData) {
                DivisionDto result = convertToDto(data);
                listResult.add(result);
            }
        } catch (Exception e) {
            log.error("Error occurred while fetching all Divisions: {}", e.getMessage(), e);
        }
        return listResult;
    }

    @Override
    public DivisionDto getDivisionById(String id) {
        log.info("Attempting to fetch Division by ID: {}", id);
        DivisionDto result = null;
        try {
            Division data = divisionRepo.getDivisionById(id);
            result = convertToDto(data);
            log.debug("Fetched Division: {}", result);
        } catch (Exception e) {
            log.error("Error occurred while fetching Division by ID {}: {}", id, e.getMessage(), e);
        }
        return result;
    }

    @Override
    public DivisionDto createDivision(DivisionCreateDto divisionCreateDto) {
        log.info("Attempting to create a new Division with data: {}", divisionCreateDto);
        DivisionDto result = null;
        try {
            Division data = convertToEntityCreate(divisionCreateDto);
            Division savedData = divisionRepo.saveDivision(data);
            result = convertToDto(savedData);
            log.info("Successfully created Division: {}", result);
        } catch (Exception e) {
            log.error("Error occurred while creating Division: {}", e.getMessage(), e);
        }
        return result;
    }

    @Override
    public DivisionDto updateDivision(DivisionUpdateDto divisionUpdateDto) {
        log.info("Attempting to update Division with data: {}", divisionUpdateDto);
        DivisionDto result = null;
        try {
            Division data = convertToEntityUpdate(divisionUpdateDto);
            Division updatedData = divisionRepo.updateDivision(data);
            result = convertToDto(updatedData);
            log.info("Successfully updated Division: {}", result);
        } catch (Exception e) {
            log.error("Error occurred while updating Division: {}", e.getMessage(), e);
        }
        return result;
    }

    @Override
    public boolean deleteDivision(String id) {
        log.info("Attempting to delete Division with ID: {}", id);
        boolean isDeleted = false;
        try {
            isDeleted = divisionRepo.deleteDivision(id);
            if (isDeleted) {
                log.info("Successfully deleted Division with ID: {}", id);
            } else {
                log.warn("Failed to delete Division with ID: {}. It might not exist.", id);
            }
        } catch (Exception e) {
            log.error("Error occurred while deleting Division with ID {}: {}", id, e.getMessage(), e);
        }
        return isDeleted;
    }

    private Division convertToEntity(DivisionDto convertObject) {
        log.debug("Converting DivisionDto to entity: {}", convertObject);
        Division result = Division.builder()
                .id(convertObject.getId())
                .divisionName(convertObject.getDivisionName())
                .createdAt(convertObject.getCreatedAt())
                .createdBy(convertObject.getCreatedBy())
                .updatedAt(convertObject.getUpdatedAt())
                .updatedBy(convertObject.getUpdatedBy())
                .build();
        return result;
    }

    private Division convertToEntityCreate(DivisionCreateDto convertObject) {
        log.debug("Converting DivisionCreateDto to entity: {}", convertObject);
        Division result = Division.builder()
                .id(convertObject.getId())
                .divisionName(convertObject.getDivisionName())
                .createdBy(convertObject.getCreatedBy())
                .build();
        return result;
    }

    private Division convertToEntityUpdate(DivisionUpdateDto convertObject) {
        log.debug("Converting DivisionUpdateDto to entity: {}", convertObject);
        Division result = Division.builder()
                .id(convertObject.getId())
                .divisionName(convertObject.getDivisionName())
                .updatedAt(convertObject.getUpdatedAt())
                .updatedBy(convertObject.getUpdatedBy())
                .build();
        return result;
    }

    private DivisionDto convertToDto(Division convertObject) {
        log.debug("Converting Division entity to DTO: {}", convertObject);
        DivisionDto result = DivisionDto.builder()
                .id(convertObject.getId())
                .divisionName(convertObject.getDivisionName())
                .createdAt(convertObject.getCreatedAt())
                .createdBy(convertObject.getCreatedBy())
                .updatedAt(convertObject.getUpdatedAt())
                .updatedBy(convertObject.getUpdatedBy())
                .build();
        return result;
    }
}
