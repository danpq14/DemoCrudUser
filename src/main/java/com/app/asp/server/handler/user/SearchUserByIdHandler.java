package com.app.asp.server.handler.user;

import java.sql.SQLException;
import java.util.Map;

import com.app.asp.service.NewUserService;
import com.app.util.AppParams;
import com.app.util.ParamUtil;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

public class SearchUserByIdHandler implements Handler<io.vertx.rxjava.ext.web.RoutingContext> {


	@Override
	public void handle(io.vertx.rxjava.ext.web.RoutingContext context) {
		// TODO Auto-generated method stub
		
		context.vertx().executeBlocking(future -> {
			String userId = context.request().getParam("id");
			
			try {
				Map user = NewUserService.getUserById(userId);
				
				context.put(AppParams.RESPONSE_CODE, HttpResponseStatus.OK.code());
				context.put(AppParams.RESPONSE_MSG, HttpResponseStatus.OK.reasonPhrase());
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
