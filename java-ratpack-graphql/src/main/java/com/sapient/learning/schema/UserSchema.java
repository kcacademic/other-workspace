package com.sapient.learning.schema;

import graphql.annotations.GraphQLAnnotations;
import graphql.schema.GraphQLSchema;

import static graphql.schema.GraphQLSchema.newSchema;

import com.sapient.learning.mutation.UserMutation;
import com.sapient.learning.query.UserQuery;

public class UserSchema {

	private final GraphQLSchema schema;

	public UserSchema() throws IllegalAccessException, NoSuchMethodException, InstantiationException {
		schema = newSchema().query(GraphQLAnnotations.object(UserQuery.class))
				.mutation(GraphQLAnnotations.object(UserMutation.class)).build();
	}

	public GraphQLSchema getSchema() {
		return schema;
	}
}