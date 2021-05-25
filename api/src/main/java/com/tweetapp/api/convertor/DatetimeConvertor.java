package com.tweetapp.api.convertor;

import java.io.IOException;
import java.time.LocalDateTime;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tweetapp.api.model.User;

public class DatetimeConvertor implements DynamoDBTypeConverter<String, LocalDateTime>{

	    ObjectMapper objectMapper = new ObjectMapper();
		@Override
	    public String convert(LocalDateTime localDateTime) {
	        try {
	            return objectMapper.writeValueAsString(localDateTime);
	        } catch (JsonProcessingException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        throw new IllegalArgumentException("Unable to parse JSON");
	    }

	    @Override
	    public LocalDateTime unconvert(String object) {
	        try {
	        	LocalDateTime unconvertedObject = objectMapper.readValue(object, 
	                new TypeReference<LocalDateTime>() {
	            });
	            return unconvertedObject;
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }
	}
