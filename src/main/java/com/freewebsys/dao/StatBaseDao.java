package com.freewebsys.dao;

import java.util.List;
import java.util.Map;

/**
 * User: freewebsys.com
 */
public interface StatBaseDao {

    List<Map<String, Object>> findStat(String sql, Map<String, Object> params);

}
