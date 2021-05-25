package com.tweetapp.api.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.tweetapp.api.model.User;
import com.tweetapp.api.model.UserResponse;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private DynamoDBMapper dynamoDBMapper;
	Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Override
	public User createUser(User user) {
		dynamoDBMapper.save(user);
		return user;
	}

	@Override
	public User updateUser(User user) {
		dynamoDBMapper.save(user,
				new DynamoDBSaveExpression()
				.withExpectedEntry("id",
						new ExpectedAttributeValue(
						new AttributeValue().withS(user.getId())
						)));
		return user;
	}

	@Override
	public int deleteUser(User user) {
		dynamoDBMapper.delete(user);
		return 1;
	}

	@Override
	public List<User> getAllUsers() {
		// TODO Auto-generated method stub
		DynamoDBScanExpression expression = new DynamoDBScanExpression();
		return dynamoDBMapper.scan(User.class, expression);
	}

	@Override
	public List<User> getUserByUsername(String username) {
		// TODO Auto-generated method stub
		Map<String, AttributeValue> expressionAttributeValues = new HashMap<String, AttributeValue>();
		expressionAttributeValues.put(":username", new AttributeValue().withS(username));
		DynamoDBScanExpression expression = new DynamoDBScanExpression()
				.withFilterExpression("username =:username")
				.withExpressionAttributeValues(expressionAttributeValues);
		return dynamoDBMapper.scan(User.class,expression);
	}

	@Override
	public User getUserById(String id) {
		// TODO Auto-generated method stub
		return dynamoDBMapper.load(User.class,id);	
	}

	@Override
	public UserResponse loginUser(String username, String password) throws Exception {
		// TODO Auto-generated method stub
		UserResponse response = new UserResponse();
		Map<String, AttributeValue> expressionAttributeValues = new HashMap<String, AttributeValue>();
		expressionAttributeValues.put(":username", new AttributeValue().withS(username));
		DynamoDBScanExpression expression = new DynamoDBScanExpression()
				.withFilterExpression("username =:username")
				.withExpressionAttributeValues(expressionAttributeValues);
		List<User> u1= dynamoDBMapper.scan(User.class,expression);
		User u2=u1.iterator().next();
		if (u2.getPassword().equals(password)) {
			response.setUser(u2);
			response.setLoginStatus("success");
		}
		else {
			response.setLoginStatus("failed");}
			return response;
	}

	@Override
	public Map<String, String> forgotPassword(String username) {
		// TODO Auto-generated method stub
		Map<String, String> map = new HashMap<String, String>();
		Map<String, AttributeValue> expressionAttributeValues = new HashMap<String, AttributeValue>();
		expressionAttributeValues.put(":username", new AttributeValue().withS(username));
		DynamoDBScanExpression expression = new DynamoDBScanExpression()
				.withFilterExpression("username =:username")
				.withExpressionAttributeValues(expressionAttributeValues);
		List<User> u1= dynamoDBMapper.scan(User.class,expression);
		User u2=u1.iterator().next();
		u2.setPassword(UUID.randomUUID().toString());
		dynamoDBMapper.save(u2);
		map.put("newPassword", u2.getPassword());
		map.put("resetStatus","success");
		return map;
	}
	@Override
	public Map<String, String> resetPassword(String username,String password) {
		// TODO Auto-generated method stub
		Map<String, String> map = new HashMap<String, String>();
		Map<String, AttributeValue> expressionAttributeValues = new HashMap<String, AttributeValue>();
		expressionAttributeValues.put(":username", new AttributeValue().withS(username));
		DynamoDBScanExpression expression = new DynamoDBScanExpression()
				.withFilterExpression("username =:username")
				.withExpressionAttributeValues(expressionAttributeValues);
		List<User> u1= dynamoDBMapper.scan(User.class,expression);
		User u2=u1.iterator().next();
		u2.setPassword(password);
		dynamoDBMapper.save(u2);
		map.put("newPassword",u2.getPassword());
		map.put("resetStatus","success");
		return map;
	}

}
