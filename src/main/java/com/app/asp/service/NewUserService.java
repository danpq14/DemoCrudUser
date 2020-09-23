package com.app.asp.service;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.sql.DataSource;

import com.app.util.AppParams;
import com.app.util.DBProcedurePool;
import com.app.util.DBProcedureUtil;
import com.app.util.LoggerInterface;
import com.app.util.ParamUtil;

import io.netty.handler.codec.http.HttpResponseStatus;
import oracle.jdbc.OracleTypes;
import oracle.jdbc.driver.OracleSQLException;

public class NewUserService implements LoggerInterface {
	private static DataSource dataSource;

	public void setDataSource(DataSource dataS) {
		NewUserService.dataSource = dataS;
	}

	public static Map getUserById(String id) throws SQLException {
		logger.log(Level.INFO, "NewService.getUserById : ", id);
		Map resultMap = new LinkedHashMap<>();

		Map inputParams = new LinkedHashMap<>();
		inputParams.put(1, id);

		Map<Integer, Integer> outputParamsTypes = new LinkedHashMap<>();
		outputParamsTypes.put(2, OracleTypes.NUMBER);
		outputParamsTypes.put(3, OracleTypes.VARCHAR);
		outputParamsTypes.put(4, OracleTypes.CURSOR);

		Map<Integer, String> outputParamsNames = new LinkedHashMap<>();
		outputParamsNames.put(2, AppParams.RESULT_CODE);
		outputParamsNames.put(3, AppParams.RESULT_MSG);
		outputParamsNames.put(4, AppParams.RESULT_DATA);

		Map insertResultMap = DBProcedureUtil.execute(dataSource, DBProcedurePool.GET_USER_BY_ID, inputParams,
				outputParamsTypes, outputParamsNames);

		int resultCode = ParamUtil.getInt(insertResultMap, AppParams.RESULT_CODE);

		if (resultCode != HttpResponseStatus.OK.code()) {
			throw new OracleSQLException();
		}

		List<Map> resultDataList = ParamUtil.getListData(insertResultMap, AppParams.RESULT_DATA);

		if (resultDataList.isEmpty()) {
			logger.log(Level.INFO, "Not found user");
			throw new OracleSQLException();
		} else {
			Map resultData = resultDataList.get(0);
			resultMap = format(resultData);
			logger.log(Level.INFO, "User get : ", resultMap);
		}

		return resultMap;
	}

	public static Map getUserByPhone(String phone) throws SQLException {
		logger.log(Level.INFO, "NewUserService.getUserByPhone", phone);
		Map resultMap = new LinkedHashMap<>();

		Map inputParams = new LinkedHashMap<>();
		inputParams.put(1, phone);

		Map<Integer, Integer> outputParamsTypes = new LinkedHashMap<>();
		outputParamsTypes.put(2, OracleTypes.NUMBER);
		outputParamsTypes.put(3, OracleTypes.VARCHAR);
		outputParamsTypes.put(4, OracleTypes.CURSOR);

		Map<Integer, String> outputParamsNames = new LinkedHashMap<>();
		outputParamsNames.put(2, AppParams.RESULT_CODE);
		outputParamsNames.put(3, AppParams.RESULT_MSG);
		outputParamsNames.put(4, AppParams.RESULT_DATA);

		Map insertResultMap = DBProcedureUtil.execute(dataSource, DBProcedurePool.GET_USER_BY_PHONE, inputParams,
				outputParamsTypes, outputParamsNames);

		int resultCode = ParamUtil.getInt(insertResultMap, AppParams.RESULT_CODE);

		if (resultCode != HttpResponseStatus.OK.code()) {
			throw new OracleSQLException();
		}

		List<Map> resultDataList = ParamUtil.getListData(insertResultMap, AppParams.RESULT_DATA);

		if (resultDataList.isEmpty()) {
			logger.log(Level.INFO, "Not found user");
			throw new OracleSQLException();
		} else if(resultDataList.size() == 1) {
			resultMap = format(resultDataList.get(0));
		}
		else  {
			int i = 0;
			for	(Map resultData : resultDataList) {
				Map singleResultMap = format(resultData);
				String key = "User" + i++;
				resultMap.put(key, singleResultMap);
			} 
		
		}
		return resultMap;
	}
	
	public static Map insertUser(String name, String email, String address, Integer age, String phone) throws SQLException {
		logger.log(Level.INFO, "Insert User");
		
		Map resultMap = new LinkedHashMap<>();
		Map inputParams = new LinkedHashMap<>();
		inputParams.put(1, name);
		inputParams.put(2, email);
		inputParams.put(3, address);
		inputParams.put(4, age);
		inputParams.put(5, phone);
		

		Map<Integer, Integer> outputParamsTypes = new LinkedHashMap<>();
		outputParamsTypes.put(6, OracleTypes.NUMBER);
		outputParamsTypes.put(7, OracleTypes.VARCHAR);
		outputParamsTypes.put(8, OracleTypes.CURSOR);

		Map<Integer, String> outputParamsNames = new LinkedHashMap<>();
		outputParamsNames.put(6, AppParams.RESULT_CODE);
		outputParamsNames.put(7, AppParams.RESULT_MSG);
		outputParamsNames.put(8, AppParams.RESULT_DATA);

		Map insertResultMap = DBProcedureUtil.execute(dataSource, DBProcedurePool.INSERT_USER, inputParams,
				outputParamsTypes, outputParamsNames);

		int resultCode = ParamUtil.getInt(insertResultMap, AppParams.RESULT_CODE);

		if (resultCode != HttpResponseStatus.CREATED.code()) {
			throw new OracleSQLException();
		}

		List<Map> resultDataList = ParamUtil.getListData(insertResultMap, AppParams.RESULT_DATA);
		
		if (resultDataList.isEmpty()) {
			logger.log(Level.INFO, "Cant created user");
			throw new OracleSQLException();
		} else {
			Map resultData = resultDataList.get(0);
			resultMap = format(resultData);
			logger.log(Level.INFO, "User was created : ", resultMap);
		}

		
		return resultMap;
	}
	
	public static Map updateUser(String id, String name, String email, String address, Integer age, String phone) throws SQLException {
		logger.log(Level.INFO, "Update User");
		
		Map resultMap = new LinkedHashMap<>();
		Map inputParams = new LinkedHashMap<>();
		inputParams.put(1, id);
		inputParams.put(2, name);
		inputParams.put(3, email);
		inputParams.put(4, address);
		inputParams.put(5, age);
		inputParams.put(6, phone);

		

		Map<Integer, Integer> outputParamsTypes = new LinkedHashMap<>();
		outputParamsTypes.put(7, OracleTypes.NUMBER);
		outputParamsTypes.put(8, OracleTypes.VARCHAR);
		outputParamsTypes.put(9, OracleTypes.CURSOR);

		Map<Integer, String> outputParamsNames = new LinkedHashMap<>();
		outputParamsNames.put(7, AppParams.RESULT_CODE);
		outputParamsNames.put(8, AppParams.RESULT_MSG);
		outputParamsNames.put(9, AppParams.RESULT_DATA);

		Map insertResultMap = DBProcedureUtil.execute(dataSource, DBProcedurePool.UPDATE_USER, inputParams,
				outputParamsTypes, outputParamsNames);

		int resultCode = ParamUtil.getInt(insertResultMap, AppParams.RESULT_CODE);

		if (resultCode != HttpResponseStatus.OK.code()) {
			throw new OracleSQLException();
		}

		List<Map> resultDataList = ParamUtil.getListData(insertResultMap, AppParams.RESULT_DATA);
		
		if (resultDataList.isEmpty()) {
			logger.log(Level.INFO, "Cant update user");
			throw new OracleSQLException();
		} else {
			Map resultData = resultDataList.get(0);
			resultMap = format(resultData);
			logger.log(Level.INFO, "User was updated : ", resultMap);
		}

		
		return resultMap;
	}
	
	private static Map format(Map dataList) {
		Map resultMap = new LinkedHashMap<>();
		resultMap.put(AppParams.ID, ParamUtil.getString(dataList, AppParams.S_ID));
		resultMap.put(AppParams.NAME, ParamUtil.getString(dataList, AppParams.S_NAME));
		resultMap.put(AppParams.EMAIL, ParamUtil.getString(dataList, AppParams.S_EMAIL));
		resultMap.put(AppParams.PHONE, ParamUtil.getString(dataList, AppParams.S_PHONE));
		resultMap.put(AppParams.ADDRESS, ParamUtil.getString(dataList, AppParams.S_ADDRESS));
		resultMap.put(AppParams.AGE, ParamUtil.getString(dataList, AppParams.N_AGE));
		resultMap.put(AppParams.GROUP_ID, ParamUtil.getString(dataList, AppParams.S_GROUP_ID));
		resultMap.put(AppParams.GROUP_NAME, ParamUtil.getString(dataList, AppParams.S_GROUP_NAME));

		return resultMap;
	}
}
