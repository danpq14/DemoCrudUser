package com.app.asp.server.handler.user;

import java.sql.SQLException;
import java.util.Map;

import com.app.asp.service.NewUserService;
import com.app.util.AppParams;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.ext.web.RoutingContext;

public class UpdateUserHandler implements Handler<RoutingContext> {

	@Override
	public void handle(RoutingContext context) {
		
		context.vertx().executeBlocking(future -> {
			
			try {
				JsonObject jsonObj = context.getBodyAsJson();
				String id = jsonObj.getString(AppParams.ID);
				String name = jsonObj.getString(AppParams.NAME);
				String email = jsonObj.getString(AppParams.EMAIL);
				String address = jsonObj.getString(AppParams.ADDRESS);
				Integer age = jsonObj.getInteger(AppParams.AGE);
				String phone = jsonObj.getString(AppParams.PHONE);
				
				//check if not change
				Map data = NewUserService.getUserById(id);
				if (name == null || name.equals("")) {
					name = (String) data.get(AppParams.NAME);
				}
				if (email == null || email.equals("")) {
					email = (String) data.get(AppParams.EMAIL);
				}
				if (address == null || address.equals("") ) {
					address = (String) data.get(AppParams.ADDRESS);
				}
				if (age == null || age == 0) {
					age =  Integer.parseInt((String) data.get(AppParams.AGE)) ;
				}
				if (phone == null || phone.equals("") ) {
					phone = (String) data.get(AppParams.PHONE);
				}
				
				Map newUser = NewUserService.updateUser(id, name, email, address, age, phone);
				
				context.put(AppParams.RESPONSE_CODE, HttpResponseStatus.OK.code());
				context.put(AppParams.RESPONSE_MSG, HttpResponseStatus.OK.reasonPhrase());
				context.put(AppParams.RESPONSE_DATA, newUser);
				
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
