package com.tweetapp.api.convertor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tweetapp.api.model.Tweet;
import com.tweetapp.api.model.User;

public class tweetConvertor implements DynamoDBTypeConverter<String, List<Tweet>>{

	@Override
	public String convert(List<Tweet> object) {
		ObjectMapper objectMapper = new ObjectMapper();
	    try {
	        String objectsString = objectMapper.writeValueAsString(object);
	        return objectsString;
	    } catch (JsonProcessingException e) {
	        //do something
	    }
	    return null;
	}

	@Override
    public List<Tweet> unconvert(String object) {
		ObjectMapper objectMapper = new ObjectMapper();
        try {
        	List<Tweet>  unconvertedObject = objectMapper.readValue(object, 
        			new TypeReference<List<Tweet>>(){});
            return unconvertedObject;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
