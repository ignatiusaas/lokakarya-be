package co.id.ogya.lokakarya.repositories.impl;

import co.id.ogya.lokakarya.entities.EmpDevPlan;
import co.id.ogya.lokakarya.repositories.EmpDevPlanRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class EmpDevPlanRepoImpl implements EmpDevPlanRepo {

    private final RowMapper<EmpDevPlan> rowMapper = new BeanPropertyRowMapper<>(EmpDevPlan.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<EmpDevPlan> getEmpDevPlans() {
        String sql = "SELECT * FROM tbl_emp_dev_plan";
        log.info("Fetching all EmpDevPlans with query: {}", sql);
        try {
            List<EmpDevPlan> result = jdbcTemplate.query(sql, rowMapper);
            log.info("Successfully fetched {} EmpDevPlans", result.size());
            return result;
        } catch (Exception e) {
            log.error("Error fetching EmpDevPlans. Error: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public EmpDevPlan getEmpDevPlanById(String id) {
        String sql = "SELECT * FROM tbl_emp_dev_plan WHERE ID = ?";
        log.info("Fetching EmpDevPlan by ID: {} with query: {}", id, sql);
        try {
            EmpDevPlan result = jdbcTemplate.queryForObject(sql, rowMapper, id);
            log.info("Successfully fetched EmpDevPlan: {}", result);
            return result;
        } catch (Exception e) {
            log.error("Error fetching EmpDevPlan by ID: {}. Error: {}", id, e.getMessage());
            return null;
        }
    }

    @Override
    public EmpDevPlan saveEmpDevPlan(EmpDevPlan empDevPlan) {
        empDevPlan.prePersist();
        String sql = "INSERT INTO tbl_emp_dev_plan (ID, USER_ID, DEV_PLAN_ID, TOO_BRIGHT, ASSESSMENT_YEAR, CREATED_BY) VALUES (?, ?, ?, ?, ?, ?)";
        log.info("Saving EmpDevPlan: {} with query: {}", empDevPlan, sql);
        try {
            int rowsAffected = jdbcTemplate.update(sql, empDevPlan.getId(), empDevPlan.getUserId(), empDevPlan.getDevPlanId(), empDevPlan.getTooBright(),
                    empDevPlan.getAssessmentYear(), empDevPlan.getCreatedBy());
            if (rowsAffected > 0) {
                log.info("Successfully saved EmpDevPlan: {}", empDevPlan);
                return empDevPlan;
            } else {
                log.warn("No rows affected while saving EmpDevPlan: {}", empDevPlan);
                return null;
            }
        } catch (Exception e) {
            log.error("Error saving EmpDevPlan: {}. Error: {}", empDevPlan, e.getMessage());
            return null;
        }
    }

    @Override
    public EmpDevPlan updateEmpDevPlan(EmpDevPlan empDevPlan) {
        String sql = "UPDATE tbl_emp_dev_plan SET USER_ID = ?, DEV_PLAN_ID = ?, TOO_BRIGHT = ?, ASSESSMENT_YEAR = ?, UPDATED_AT = SYSDATE(), UPDATED_BY = ? " +
                "WHERE ID = ?";
        log.info("Updating EmpDevPlan with ID: {} using query: {}", empDevPlan.getId(), sql);
        try {
            int rowsAffected = jdbcTemplate.update(sql, empDevPlan.getUserId(), empDevPlan.getDevPlanId(), empDevPlan.getTooBright(),
                    empDevPlan.getAssessmentYear(), empDevPlan.getUpdatedBy(), empDevPlan.getId());
            if (rowsAffected > 0) {
                log.info("Successfully updated EmpDevPlan: {}", empDevPlan);
                return empDevPlan;
            } else {
                log.warn("No rows affected while updating EmpDevPlan with ID: {}", empDevPlan.getId());
                return null;
            }
        } catch (Exception e) {
            log.error("Error updating EmpDevPlan with ID: {}. Error: {}", empDevPlan.getId(), e.getMessage());
            return null;
        }
    }

    @Override
    public Boolean deleteEmpDevPlan(String id) {
        String sql = "DELETE FROM tbl_emp_dev_plan WHERE ID = ?";
        log.info("Deleting EmpDevPlan with ID: {} using query: {}", id, sql);
        try {
            int rowsAffected = jdbcTemplate.update(sql, id);
            if (rowsAffected > 0) {
                log.info("Successfully deleted EmpDevPlan with ID: {}", id);
                return true;
            } else {
                log.warn("No rows affected while deleting EmpDevPlan with ID: {}", id);
                return false;
            }
        } catch (Exception e) {
            log.error("Error deleting EmpDevPlan with ID: {}. Error: {}", id, e.getMessage());
            return false;
        }
    }

    @Override
    public List<Map<String, Object>> getEmpDevPlanGets() {
        String sql = "SELECT EDP.ID, EDP.USER_ID, AU.FULL_NAME, EDP.DEV_PLAN_ID, DP.PLAN, EDP.ASSESSMENT_YEAR, EDP.TOO_BRIGHT " +
                "FROM tbl_emp_dev_plan EDP " +
                "LEFT JOIN tbl_app_user AU ON EDP.USER_ID = AU.ID " +
                "LEFT JOIN tbl_dev_plan DP ON EDP.DEV_PLAN_ID = DP.ID";
        log.info("Fetching all EmpDevPlans with LEFT JOIN query: {}", sql);
        try {
            List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
            log.info("Successfully fetched {} EmpDevPlans", result.size());
            return result;
        } catch (Exception e) {
            log.error("Error fetching EmpDevPlans. Error: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Map<String, Object>> getEmpDevPlanGetByUserId(String userId) {
        String sql = "SELECT EDP.ID, AU.FULL_NAME, EDP.DEV_PLAN_ID, DP.PLAN, EDP.ASSESSMENT_YEAR, EDP.TOO_BRIGHT " +
                "FROM tbl_emp_dev_plan EDP " +
                "LEFT JOIN tbl_app_user AU ON EDP.USER_ID = AU.ID " +
                "LEFT JOIN tbl_dev_plan DP ON EDP.DEV_PLAN_ID = DP.ID " +
                "WHERE EDP.USER_ID = ?";
        log.info("Fetching EmpDevPlans by User ID: {} with LEFT JOIN query: {}", userId, sql);
        try {
            List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, userId);
            log.info("Successfully fetched {} EmpDevPlans for User ID: {}", result.size(), userId);
            return result;
        } catch (Exception e) {
            log.error("Error fetching EmpDevPlans for User ID: {}. Error: {}", userId, e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Map<String, Object>> getEmpDevPlanGetByUserIdAssessmentYear(String userId, String assessmentYear) {
        String sql = "SELECT EDP.ID, AU.FULL_NAME, EDP.DEV_PLAN_ID, DP.PLAN, EDP.ASSESSMENT_YEAR, EDP.TOO_BRIGHT " +
                "FROM tbl_emp_dev_plan EDP " +
                "LEFT JOIN tbl_app_user AU ON EDP.USER_ID = AU.ID " +
                "LEFT JOIN tbl_dev_plan DP ON EDP.DEV_PLAN_ID = DP.ID " +
                "WHERE EDP.USER_ID = ? AND EDP.ASSESSMENT_YEAR = ?";
        log.info("Fetching EmpDevPlans by User ID: {} and Assessment Year: {} with LEFT JOIN query: {}", userId, assessmentYear, sql);
        try {
            List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, userId, assessmentYear);
            log.info("Successfully fetched {} EmpDevPlans for User ID: {} and Assessment Year: {}", result.size(), userId, assessmentYear);
            return result;
        } catch (Exception e) {
            log.error("Error fetching EmpDevPlans for User ID: {} and Assessment Year: {}. Error: {}", userId, assessmentYear, e.getMessage());
            throw e;
        }
    }

    @Override
    public Boolean ifAnyEmpDevPlanExist(String userId, String devPlanId, int assessmentYear) {
        String sql = "SELECT ID FROM tbl_emp_dev_plan WHERE USER_ID = ? AND DEV_PLAN_ID = ? AND ASSESSMENT_YEAR = ?";
        log.info("Looking for EmpDevPlan with User ID: {}, Dev Plan ID: {}, and Assessment Year: {} with query: {}", userId, devPlanId, assessmentYear, sql);
        try {
            List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, userId, devPlanId, assessmentYear);
            if (result.isEmpty()) {
                log.info("There is no EmpDevPlan with User ID: {}, Dev Plan ID: {}, and Assessment Year: {}", userId, devPlanId, assessmentYear);
                return false;
            } else {
                log.info("There is an EmpDevPlan with User ID: {}, Dev Plan ID: {}, and Assessment Year: {}", userId, devPlanId, assessmentYear);
                return true;
            }
        } catch (Exception e) {
            log.error("Error while looking EmpDevPlan by User ID: {}, Dev Plan ID: {}, and Assessment Year: {}. Error: {}", userId, devPlanId, assessmentYear, e.getMessage());
            throw e;
        }
    }
}
