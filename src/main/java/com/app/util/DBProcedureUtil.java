package com.app.util;

import oracle.jdbc.OracleTypes;
import oracle.sql.TIMESTAMP;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DBProcedureUtil {

    public static Map execute(DataSource dataSource, String procedureCallStr, Map<Integer, Object> inputParams,
            Map<Integer, Integer> outputParamsTypes, Map<Integer, String> outputParamsName) throws SQLException {

        Map resultMap = new LinkedHashMap<>();

        Connection connection;

        ResultSet resultSet = null;

        CallableStatement callableStatement;

        connection = dataSource.getConnection();
        callableStatement = connection.prepareCall(procedureCallStr);
        
        //Nếu input Map khác null và ko rỗng, sẽ lần lượt duyệt qua các cặp entry trong map để set tham số
        if (inputParams != null && !inputParams.isEmpty()) {
        	
        	//lấy ra cặp entry trong input Map
            for (Map.Entry<Integer, Object> entry : inputParams.entrySet()) {
            	
            	//lấy ra vị trí của tham số trong câu procedure call
                int paramIndex = entry.getKey();
                
                //lấy ra value của tham số
                Object paramValue = entry.getValue();
                
                //Nếu tham số là Integer, statement sẽ setInt
                if (paramValue instanceof Integer) {
                	
                	//GetterUtil sẽ chuyển từ Object sang Integer
                    callableStatement.setInt(paramIndex, GetterUtil.getInteger(paramValue));
                
                    
                } else if (paramValue instanceof Long) {
                	
                    callableStatement.setLong(paramIndex, GetterUtil.getLong(paramValue.toString()));

                } else if (paramValue instanceof Float) {

                    callableStatement.setFloat(paramIndex, GetterUtil.getFloat(paramValue));

                } else if (paramValue instanceof Double) {

                    callableStatement.setDouble(paramIndex, GetterUtil.getDouble(paramValue.toString()));

                } else if (paramValue instanceof Boolean) {

                    callableStatement.setBoolean(paramIndex, GetterUtil.getBoolean(paramValue.toString()));

                } else if (paramValue instanceof String) {

                    callableStatement.setString(paramIndex, paramValue.toString());

                } else if (paramValue instanceof Timestamp) {

                    callableStatement.setTimestamp(paramIndex, (Timestamp) paramValue);

                } else if (paramValue instanceof java.util.Date) {

                    callableStatement.setDate(paramIndex, new Date(((java.util.Date) paramValue).getTime()));

                } else {

                    callableStatement.setNull(paramIndex, OracleTypes.NULL);
                }
            }
        }

        if (outputParamsTypes != null && outputParamsTypes.size() > 0) {

            for (Map.Entry<Integer, Integer> entry : outputParamsTypes.entrySet()) {

                int paramIndex = entry.getKey();
                int paramValue = entry.getValue();

                callableStatement.registerOutParameter(paramIndex, paramValue);
            }
        }

        callableStatement.execute();

        for (Map.Entry<Integer, Integer> entry : outputParamsTypes.entrySet()) {

            int paramIndex = entry.getKey();

            String paramName = (outputParamsName != null && !outputParamsName.isEmpty() && outputParamsName.get(paramIndex) != null)
                    ? outputParamsName.get(paramIndex).toString() : String.valueOf(paramIndex);

            int paramType = entry.getValue();

            Object paramValue = callableStatement.getObject(paramIndex);

            if (paramType == OracleTypes.CURSOR) {

                resultSet = (ResultSet) callableStatement.getObject(paramIndex);

                if (resultSet != null) {

                    List<Map<String, Object>> modelList = new ArrayList<>();

                    ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

                    while (resultSet.next()) {

                        Map<String, Object> modelInfoMap = new LinkedHashMap<>();

                        for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {

                            Object value = resultSet.getObject(i);

                            if (value instanceof TIMESTAMP) {
                                value = new java.util.Date(((TIMESTAMP) value).timestampValue().getTime());
                            }
                            modelInfoMap.put(resultSetMetaData.getColumnName(i), value);
                        }

                        modelList.add(modelInfoMap);
                    }

                    resultMap.put(paramName, modelList);
                }

            } else {

                resultMap.put(paramName, paramValue);

            }
        }

        if (connection != null) {
            connection.close();
        }
        if (callableStatement != null) {
            callableStatement.close();
        }
        if (resultSet != null) {
            resultSet.close();
        }

        return resultMap;
    }
}
