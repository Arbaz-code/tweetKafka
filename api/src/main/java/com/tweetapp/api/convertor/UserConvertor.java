package com.tweetapp.api.convertor;


import java.io.IOException;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tweetapp.api.model.User;

public class UserConvertor  implements DynamoDBTypeConverter<String,User> {
	 ObjectMapper objectMapper = new ObjectMapper();
	@Override
    public String convert(User object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new IllegalArgumentException("Unable to parse JSON");
    }

    @Override
    public User unconvert(String object) {
        try {
        	User unconvertedObject = objectMapper.readValue(object, 
                new TypeReference<User>() {
            });
            return unconvertedObject;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}