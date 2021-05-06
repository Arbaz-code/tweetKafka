package com.tweetapp.api.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tweetapp.api.model.Tweet;
import com.tweetapp.api.model.User;
import com.tweetapp.api.repository.TweetRepository;
import com.tweetapp.api.repository.UserRepository;

@Service
public class TweetServiceImpl implements TweetService {

	@Autowired
	TweetRepository tweetRepository;
	
	@Autowired
	UserRepository userRepository;
	
	Logger logger = LoggerFactory.getLogger(TweetServiceImpl.class);
	
	@Override
	public Tweet postTweet(Tweet tweet) {
		// TODO Auto-generated method stub
		return tweetRepository.save(tweet);
	}

	@Override
	public Tweet editTweet(Tweet tweet) {
		// TODO Auto-generated method stub
		return tweetRepository.save(tweet);
	}

	@Override
	public Tweet likeTweet(Tweet tweet) {
		// TODO Auto-generated method stub
		tweet.setLikes(tweet.getLikes()+1);
		return tweetRepository.save(tweet);
	}

	@Override
	public Tweet replyTweet(Tweet parentTweet, Tweet replyTweet) {
		// TODO Auto-generated method stub
		tweetRepository.save(replyTweet);
		List<Tweet> parentTweetReplies = parentTweet.getReplies();
		parentTweetReplies.add(replyTweet);
		parentTweet.setReplies(parentTweetReplies);
		tweetRepository.save(parentTweet);
		return replyTweet;
	}

	@Override
	public void deleteTweet(Tweet tweet) {
		// TODO Auto-generated method stub
		tweetRepository.delete(tweet);
	}

	@Override
	public List<Tweet> getAllTweets() {
		// TODO Auto-generated method stub
		return tweetRepository.findAll();
	}

	@Override
	public List<Tweet> getAllTweetsByUsername(String username) {
		// TODO Auto-generated method stub
		return tweetRepository.findByUserUsername(username);
	}

	@Override
	public Tweet postTweetByUsername(Tweet tweet, String username) {
		User user = userRepository.findByUsername(username);
		tweet.setUser(user);
		return tweetRepository.save(tweet);
		
	}

	@Override
	public void deleteTweetById(String tweetId) {
		tweetRepository.deleteById(tweetId);
		
	}

	@Override
	public Tweet replyTweetById(Tweet replyTweet, String parentTweetId) throws Exception {
		Optional<Tweet> parentTweet = tweetRepository.findById(parentTweetId);
		if (parentTweet.isPresent()) {
			List<Tweet> replies = parentTweet.get().getReplies();
			replies.add(replyTweet);
			tweetRepository.save(parentTweet.get());
		}
		else {
			throw new Exception("Incorrect or deleted parent tweet id.");
		}
		return replyTweet;
		

	}

	@Override
	public void likeTweetById(String tweetId) {
		Optional<Tweet> tweet = tweetRepository.findById(tweetId);
		logger.info("Tweet with Id: {} is {}", tweetId, tweet.get());
		if (tweet.isPresent()) {
			tweet.get().setLikes(tweet.get().getLikes()+1);
			tweetRepository.save(tweet.get());
		}
		
	}

}
