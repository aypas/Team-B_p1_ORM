package com.revature.util.crud;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Jbialon
 * Date: 5/29/2021
 * Time: 12:27 PM
 *
 * Description: Takes a ResultSet object and transfers it to a list of maps used heavily in the object creation
 *              process.
 *
 */
public class ResultSetMapper {

    protected ResultSetMapper() {}

    protected List<Map<String, Object>> mapResultSet(ResultSet rs) throws SQLException {

        List<Map<String, Object>> returnVal = new ArrayList<>();

        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        while (rs.next()) {

            Map<String, Object> result = new HashMap<>();

            for (int i = 1; i <= columnCount; i++) {
                result.put(metaData.getColumnName(i), rs.getObject(i));
            }

            returnVal.add(result);

        }

        return returnVal;

    }

}
