package com.app.asp.server.handler.user;

import java.sql.SQLException;
import java.util.Map;

import com.app.asp.service.NewUserService;
import com.app.util.AppParams;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.ext.web.RoutingContext;

public class InsertUserHandler implements Handler<RoutingContext> {

	@Override
	public void handle(RoutingContext context) {
		
		context.vertx().executeBlocking(future -> {
			JsonObject jsonObj = context.getBodyAsJson();
			String name = jsonObj.getString(AppParams.NAME);
			String email = jsonObj.getString(AppParams.EMAIL);
			String address = jsonObj.getString(AppParams.ADDRESS);
			Integer age = jsonObj.getInteger(AppParams.AGE);
			String phone = jsonObj.getString(AppParams.PHONE);
			
			try {
				Map user = NewUserService.insertUser(name, email, address, age, phone);
				
				context.put(AppParams.RESPONSE_CODE, HttpResponseStatus.CREATED.code());
				context.put(AppParams.RESPONSE_MSG, HttpResponseStatus.CREATED.reasonPhrase());
				context.put(AppParams.RESPONSE_DATA, user);
				
				future.complete();
			} catch (SQLException e) {
				context.fail(e);
				e.printStackTrace();
			}
			
		}, resultHandler -> {
			if	(resultHandler.succeeded()) {
				context.next();
			} else {
				context.fail(resultHandler.cause());
			}
		});
		
	}
	
	
}
