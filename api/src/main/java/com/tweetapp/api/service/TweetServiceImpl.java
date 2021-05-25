package com.tweetapp.api.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.tweetapp.api.model.Tweet;
import com.tweetapp.api.model.User;


@Service
public class TweetServiceImpl implements TweetService {

	@Autowired
	private DynamoDBMapper dynamoDBMapper;
	
	Logger logger = LoggerFactory.getLogger(TweetServiceImpl.class);
	
	@Override
	public Tweet postTweet(Tweet tweet) {
		// TODO Auto-generated method stub
		dynamoDBMapper.save(tweet);
		return tweet;
	}

	@Override
	public Tweet editTweet(Tweet tweet) {
		// TODO Auto-generated method stub
		dynamoDBMapper.save(tweet,
				new DynamoDBSaveExpression()
				.withExpectedEntry("id",
						new ExpectedAttributeValue(
						new AttributeValue().withS(tweet.getId())
						)));
		return tweet;
	}

	@Override
	public Tweet likeTweet(Tweet tweet) {
		// TODO Auto-generated method stub
		tweet.setLikes(tweet.getLikes()+1);
		dynamoDBMapper.save(tweet,
				new DynamoDBSaveExpression()
				.withExpectedEntry("id",
						new ExpectedAttributeValue(
						new AttributeValue().withS(tweet.getId())
						)));
		return tweet;
	}

	@Override
	public Tweet replyTweet(Tweet parentTweet, Tweet replyTweet) {
		// TODO Auto-generated method stub
		dynamoDBMapper.save(replyTweet);
		List<Tweet> parentTweetReplies = parentTweet.getReplies();
		parentTweetReplies.add(replyTweet);
		parentTweet.setReplies(parentTweetReplies);
		dynamoDBMapper.save(parentTweet,
				new DynamoDBSaveExpression()
				.withExpectedEntry("id",
						new ExpectedAttributeValue(
						new AttributeValue().withS(parentTweet.getId())
						)));
		return parentTweet;
	}

	@Override
	public void deleteTweet(Tweet tweet) {
		// TODO Auto-generated method stub
		dynamoDBMapper.delete(tweet);
	}

	@Override
	public List<Tweet> getAllTweets() {
		// TODO Auto-generated method stub
		DynamoDBScanExpression expression = new DynamoDBScanExpression();
		return dynamoDBMapper.scan(Tweet.class, expression);
	}

	@Override
	public List<Tweet> getAllTweetsByUsername(String username) {
		// TODO Auto-generated method stub
		Map<String, AttributeValue> expressionAttributeValues = new HashMap<String, AttributeValue>();
		expressionAttributeValues.put(":username", new AttributeValue().withS(username));
		DynamoDBScanExpression expression = new DynamoDBScanExpression()
				.withFilterExpression("replies.username =:username")
				.withExpressionAttributeValues(expressionAttributeValues);
		return dynamoDBMapper.scan(Tweet.class, expression);
	}
	
	@Override
	public Tweet postTweetByUsername(Tweet tweet, String username) {
		Map<String, AttributeValue> expressionAttributeValues = new HashMap<String, AttributeValue>();
		expressionAttributeValues.put(":username", new AttributeValue().withS(username));
		DynamoDBScanExpression expression = new DynamoDBScanExpression()
				.withFilterExpression("username =:username")
				.withExpressionAttributeValues(expressionAttributeValues);
		List<User> u1 = dynamoDBMapper.scan(User.class, expression);
		System.out.println(u1.toString());
		User u2=u1.iterator().next();
		tweet.setUser(u2);
		dynamoDBMapper.save(tweet);
		System.out.println(tweet.toString());
		return tweet;
	}

	@Override
	public void deleteTweetById(String tweetId) {
		Tweet tweet=dynamoDBMapper.load(Tweet.class,tweetId);
		dynamoDBMapper.delete(tweet);
	}

	@Override
	public Tweet replyTweetById(Tweet replyTweet, String parentTweetId) throws Exception {
		
		Tweet parentTweet = dynamoDBMapper.load(Tweet.class,parentTweetId);
		System.out.println(replyTweet.toString());
		System.out.println(parentTweetId.toString());
		if (!Objects.isNull(parentTweet)) {
			List<Tweet> replies = parentTweet.getReplies();
			replies.add(replyTweet);
			dynamoDBMapper.save(parentTweet);
		}
		else {
			throw new Exception("Incorrect or deleted parent tweet id.");
		}
		return replyTweet;
		

	}

	@Override
	public void likeTweetById(String tweetId) {
		Tweet tweet = dynamoDBMapper.load(Tweet.class,tweetId);
		logger.info("Tweet with Id: {} is {}", tweetId, tweet);
		if (!Objects.isNull(tweet)) {
			tweet.setLikes(tweet.getLikes()+1);
			dynamoDBMapper.save(tweet);
		}
		
	}

}
