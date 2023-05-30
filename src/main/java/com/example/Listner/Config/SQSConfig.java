package com.example.Listner.Config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

//Configuring the localstack SQS service with script to consume records produced by producer in the SQS
@AutoConfiguration
public class SQSConfig {


//    taking value from application properties for region
    @Value("${cloud.aws.region.static}")
    private String region;

//    taking value for access-key from application properties
    @Value("${cloud.aws.credentials.access-key}")
    private String accessKeyId;

//    taking value from for secret-key from application properties
    @Value("${cloud.aws.credentials.secret-key}")
    private String secretAccessKey;

//    taking value for end-point url from application properties
    @Value("${cloud.aws.end-point.uri}")
    private String sqsUrl;

//    Bean for sqs queue-messaging-tepmplate
    @Bean
    public QueueMessagingTemplate queueMessagingTemplate() {
        return new QueueMessagingTemplate(amazonSQSAsync());
    }


//    Bean for async SQS service
    @Bean
    @Primary
    public AmazonSQSAsync amazonSQSAsync() {
        return AmazonSQSAsyncClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(sqsUrl, region))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKeyId, secretAccessKey)))
                .build();
    }
}
