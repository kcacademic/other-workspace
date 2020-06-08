package com.sapient.learning.handler;

import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.DataFetchingEnvironment;
import ratpack.handling.Context;
import ratpack.handling.Handler;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.sapient.learning.entity.User;
import com.sapient.learning.schema.UserSchema;
import com.sapient.learning.utils.SchemaUtils;

import static ratpack.jackson.Jackson.json;

public class UserHandler implements Handler {
	private static final Logger LOGGER = Logger.getLogger(UserHandler.class.getSimpleName());
	private GraphQL graphql;
	private static List<User> users = new ArrayList<>();
	
	public UserHandler() throws Exception {
		graphql = GraphQL.newGraphQL(new UserSchema().getSchema()).build();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handle(Context context) throws Exception {
		context.parse(Map.class).then(payload -> {
			Map<String, Object> parameters = (Map<String, Object>) payload.get("parameters");
			ExecutionResult executionResult = graphql.execute(payload.get(SchemaUtils.QUERY).toString(), null, this,
					parameters);
			Map<String, Object> result = new LinkedHashMap<>();
			if (executionResult.getErrors().isEmpty()) {
				result.put(SchemaUtils.DATA, executionResult.getData());
			} else {
				result.put(SchemaUtils.ERRORS, executionResult.getErrors());
				LOGGER.warning("Errors: " + executionResult.getErrors());
			}
			context.render(json(result));
		});
	}

	public static List<User> getUsers() {
		return users;
	}

	@SuppressWarnings("static-access")
	public static List<User> getUsers(DataFetchingEnvironment env) {
		return ((UserHandler) env.getSource()).getUsers();
	}
}