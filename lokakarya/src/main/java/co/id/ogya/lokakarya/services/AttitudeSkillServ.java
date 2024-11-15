package co.id.ogya.lokakarya.services;

import co.id.ogya.lokakarya.dto.attitudeskill.AttitudeSkillCreateDto;
import co.id.ogya.lokakarya.dto.attitudeskill.AttitudeSkillDto;
import co.id.ogya.lokakarya.dto.attitudeskill.AttitudeSkillUpdateDto;

import java.util.List;

public interface AttitudeSkillServ {
    List<AttitudeSkillDto> getAllAttitudeSkill();
    AttitudeSkillDto getAttitudeSkillById(String id);
    AttitudeSkillDto createAttitudeSkill(AttitudeSkillCreateDto attitudeSkillCreateDto);
    AttitudeSkillDto updateAttitudeSkill(AttitudeSkillUpdateDto attitudeSkillUpdateDto);
    boolean deleteAttitudeSkill(String id);

}
