package com.siemens.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.siemens.domain.UserAudit;

/**
 * Spring Data SQL repository for the Unit entity.
 */
@Repository
public interface UserAuditRepository extends JpaRepository<UserAudit, Long>
{
    @Query(value = "SELECT t1.* FROM user_audit t1 INNER JOIN (SELECT user_name, MAX(last_login) AS lastlogin FROM user_audit GROUP BY user_name) t2\r\n"
        + "ON t1.last_login = t2.lastlogin AND t1.user_name = t2.user_name", nativeQuery = true)
    List<UserAudit> findOperators();
    
    @Query(value = "SELECT count(*) FROM(SELECT count(DISTINCT user_name) FROM user_audit GROUP BY user_name) total_login", nativeQuery = true)
    int findOperatorsCount();
    
    @Query(value = "SELECT count(*) as total_op FROM\r\n"
        + "(SELECT count(DISTINCT user_name), MAX(last_logout) AS lastlogout FROM user_audit GROUP BY user_name,last_logout HAVING last_logout ISNULL) \r\n"
        + "total_login", nativeQuery = true)
    int findLoggedInOperatorsCount();
    
    @Query(value = "SELECT * FROM user_audit WHERE user_name = :userName AND last_logout NOTNULL ORDER BY last_login DESC", nativeQuery = true)
    List<UserAudit> findByUserName(String userName);
    
    @Query(value = "SELECT t1.* FROM user_audit t1 INNER JOIN (SELECT user_name, MAX(last_login) AS lastlogin FROM user_audit WHERE user_name = :userName GROUP BY user_name) t2\r\n"
        + "ON t1.last_login = t2.lastlogin AND t1.user_name = t2.user_name where last_logout is null", nativeQuery = true)
    UserAudit getOperator(@Param("userName") String userName);
    
    @Query(value = "SELECT q0.user_name, q0.last_login, q0.last_logout,  q0.login_status, q1.dailyDuration, q1.avgDuration FROM user_audit q0 INNER JOIN (SELECT FT.user_name, FT.dailyDuration, t5.avgDuration, t5.lastLogina FROM (SELECT SUM(DATE_PART('day', t1.last_logout - t1.last_login) * 24 + DATE_PART('hour', t1.last_logout - t1.last_login) * 60 + DATE_PART('minute', t1.last_logout - t1.last_login))/66 AS avgDuration , t1.user_name, MAX(t1.last_login) as lastLogina FROM user_audit t1 where last_login > now() - Interval '3 months' GROUP BY t1.user_name) t5 INNER JOIN (SELECT SUM(actualDuration) as dailyDuration, T3.user_name, MAX(T3.last_login) as lastLogin2 FROM (SELECT (DATE_PART('day', t1.last_logout - t1.last_login) * 24 + DATE_PART('hour', t1.last_logout - t1.last_login) * 60 + DATE_PART('minute', t1.last_logout - t1.last_login)) AS actualDuration, t1.last_login, t1.user_name FROM user_audit t1 INNER JOIN (SELECT user_name, MAX(last_login) AS lastlogin FROM user_audit GROUP BY user_name) t2 ON (DATE(t1.last_login) = DATE(t2.lastlogin)) AND t1.user_name = t2.user_name) T3 GROUP BY T3.user_name) FT ON FT.user_name = t5.user_name) q1 ON q0.user_name = q1.user_name AND q0.last_login = q1.lastLogina", nativeQuery = true)
    List<String> findOperatorsWithAvg();// used from oplist repo
    
    @Query(value = "SELECT * FROM user_audit WHERE last_logout ISNULL ORDER BY last_login DESC", nativeQuery = true)
    List<UserAudit> findAllLogoutNull();
    
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE user_audit SET login_status = 'INACTIVE', last_logout = CURRENT_TIMESTAMP AT TIME ZONE 'UTC' WHERE id = (SELECT t1.id FROM user_audit t1 INNER JOIN (SELECT user_name, MAX(last_login) AS lastlogin FROM user_audit WHERE user_name = :userName GROUP BY user_name) t2\r\n"
        + "ON t1.last_login = t2.lastlogin AND t1.user_name = t2.user_name WHERE last_logout is null)", nativeQuery = true)
    int setManualLogout(@Param("userName") String userName);
    
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE user_audit SET login_status = 'INACTIVE_BY_SYS', last_logout = CURRENT_TIMESTAMP AT TIME ZONE 'UTC' WHERE id = :id", nativeQuery = true)
    int setAutoLogout(@Param("id") Long id);
    
    @Query(value = "SELECT SUM(dailyduration)/COUNT(*) AS OverallDailyAvg FROM operatorlist_temp WHERE DATE(last_logout) = CURRENT_DATE", nativeQuery = true)
    Double findOverallAvgTime();
    
    // test
    @Modifying(clearAutomatically = true)
    @Query(value = "INSERT INTO hm_test (key, val) VALUES (?1, ?2)", nativeQuery = true)
    Integer userLocationCache(String name, String string);
    
    @Query(value = "SELECT login_status FROM user_audit WHERE session_key = :sessionKey AND last_logout NOTNULL ORDER BY last_login DESC LIMIT 1", nativeQuery = true)
    String findLoginStatusForRequestFilter(String sessionKey);
    
    @Query(value = "SELECT * FROM user_audit WHERE login_status = 'ACTIVE' AND last_logout ISNULL", nativeQuery = true)
    List<UserAudit> findAllActiveUsers();
}
