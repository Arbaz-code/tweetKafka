package com.tweetapp.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
@Configuration
public class SpringfoxConfig {                                    
    @Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)  
          .select()                                  
          .apis(RequestHandlerSelectors.any())              
          .paths(PathSelectors.any())                          
          .build();                                           
    }
    @Bean
	public DynamoDBMapper dynamoDBMapper() {
		return new DynamoDBMapper(buildAmazonDynamoDB());
	}

	private AmazonDynamoDB buildAmazonDynamoDB() {
		return AmazonDynamoDBClientBuilder
				.standard()
				.withEndpointConfiguration(
				new AwsClientBuilder.EndpointConfiguration(
						 "dynamodb.ap-south-1.amazonaws.com",
			             "ap-south-1"
						)	
				)
				.withCredentials(
						new AWSStaticCredentialsProvider(
								new BasicAWSCredentials(
										"AKIAQA4JROTPMVPAAWIA","NvSG6YgH3NPQAc95cFv3U5AsANzhZprh+ZyGelKc"
										)
								)
						)
				.build();
		
				
	}

}

