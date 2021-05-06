package com.tweetapp.api.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tweetapp.api.messaging.ProducerService;
import com.tweetapp.api.model.Tweet;
import com.tweetapp.api.model.User;
import com.tweetapp.api.model.UserResponse;
import com.tweetapp.api.service.TweetService;
import com.tweetapp.api.service.UserService;

@CrossOrigin
@RestController
@RequestMapping("/api/v1.0/tweets")
public class TweetAppController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	TweetService tweetService;
	
	@Autowired
	ProducerService producerService; 
	
	Logger logger = LoggerFactory.getLogger(TweetAppController.class);
	
	@PostMapping("/register")
	public ResponseEntity<Object> registerUser(@RequestBody User user) {
		producerService.sendMessage("Registration request received for user: " + user.getUsername());
		return new ResponseEntity<>(userService.createUser(user),HttpStatus.CREATED);
		
	}
	
	@ResponseBody
	@PostMapping("/login")
	public ResponseEntity<UserResponse> loginUser(@RequestBody User user) {
		
		producerService.sendMessage("Login request received for user: "+ user.getUsername());
		try {
			
				return new ResponseEntity<UserResponse>(userService.loginUser(user.getUsername(), user.getPassword()),HttpStatus.OK);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			UserResponse userResponse = new UserResponse();
			userResponse.setLoginStatus("username does not exist");
			logger.info("User {} does not exist", user.getUsername());
			return new ResponseEntity<UserResponse>(userResponse,HttpStatus.OK);
		}
	
	}
	
	@ResponseBody
	@GetMapping("/{username}/forgot")
	public Map<String,String> forgotPassword(@PathVariable("username") String username){
		producerService.sendMessage("Forgot Password request received with username: "+ username);
		return new HashMap<String, String>(userService.forgotPassword(username));
		
	}
	@ResponseBody
	@PostMapping("/reset")
	public Map<String,String> resetUserPassword(@RequestBody User user) {
		producerService.sendMessage("Registration request received for user: " + user.getUsername());
		return new HashMap<String, String>(userService.resetPassword(user.getUsername(),user.getPassword()));
		
	}
	@GetMapping("/all")
	public ResponseEntity<List<Tweet>> getAllTweets() {
		producerService.sendMessage("Received request to send all tweet data.");
		return new ResponseEntity<>(tweetService.getAllTweets(),HttpStatus.OK);
	}
	
	@GetMapping("/users/all")
	public ResponseEntity<List<User>> getAllUsers() {
		producerService.sendMessage("Received request to send all user data.");
		return new ResponseEntity<>(userService.getAllUsers(),HttpStatus.OK);
	}
	
	@GetMapping("/user/search/{username}")
	public ResponseEntity<List<User>> searchUser(@PathVariable("username") String username) {
		return new ResponseEntity<>(userService.getUserByUsername(username),HttpStatus.OK);
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<List<Tweet>> getAllTweetsByUser(@PathVariable("username") String username) {
		return new ResponseEntity<>(tweetService.getAllTweetsByUsername(username),HttpStatus.OK);
	}
	
	@PostMapping("/{username}/add")
	public ResponseEntity<Tweet> postTweetByUser(@PathVariable("username") String username, @RequestBody Tweet tweet){
		return new ResponseEntity<>(tweetService.postTweetByUsername(tweet, username),HttpStatus.OK);
	}
	
	@PutMapping("/{username}/update/{id}")
	public ResponseEntity<Tweet> updateTweetByUser(@PathVariable("username") String username, @PathVariable("id") String tweetId, @RequestBody Tweet tweet) {
		return new ResponseEntity<>(tweetService.editTweet(tweet),HttpStatus.OK);
	}
	
	@DeleteMapping("/{username}/delete/{id}")
	public ResponseEntity<HttpStatus> deleteTweetByUser(@PathVariable("username") String username, @PathVariable("id") String tweetId) {
		tweetService.deleteTweetById(tweetId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PutMapping("/{username}/like/{id}")
	public ResponseEntity<HttpStatus> likeTweetByUser(@PathVariable("username") String username, @PathVariable("id") String tweetId) {
		tweetService.likeTweetById(tweetId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	@PostMapping("/{username}/reply/{id}")
	public ResponseEntity<Tweet> replyTweetByUser(@PathVariable("username") String username, @PathVariable("id") String tweetId, @RequestBody Tweet replyTweet) {
		
		try {
			return new ResponseEntity<>(tweetService.replyTweetById(replyTweet, tweetId),HttpStatus.OK);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<>(new Tweet(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
