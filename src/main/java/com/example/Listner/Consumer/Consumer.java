package com.example.Listner.Consumer;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

//Consumer script which is connecting with the sqs queue to which producer is producing records,
//then it starts listning those records with 20 seconds time-gap and then process it and logs the
//message as recived the record
@Component
public class Consumer {
    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);

//    taking value of queue-url from applicatio properties
    @Value("${cloud.aws.queue-url}")
    private String queueUrl;

    private final AmazonSQS sqsClient;
    private final ObjectMapper objectMapper;

//    consumer connecting with the same queue where producer is producing records using specified cred's
    public Consumer() {
        this.sqsClient = AmazonSQSClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:4566/000000000000/popup", "us-east-1"))
                .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
                .build();
        this.objectMapper = new ObjectMapper();
        startListening();
    }

//    it listens the sqs records as produced by producer by creating new threads
    private void startListening() {
        new Thread(this::pollMessages).start();
    }

//    Here processing the record with maximum humber of records as 10
//    and wait time in between processing of two records as 20 seconds
    private void pollMessages() {
        while (true) {
            ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl)
                    .withMaxNumberOfMessages(10)
                    .withWaitTimeSeconds(20);


//            It stores messages in list and log the body for received records
            List<Message> messages = sqsClient.receiveMessage(receiveMessageRequest).getMessages();
            for (Message message : messages) {
                try {
                    String body = message.getBody();
                    logger.info("Received message from SQS queue: " + body);

                    // Delete the message from the queue
                    sqsClient.deleteMessage(new DeleteMessageRequest(queueUrl, message.getReceiptHandle()));
                } catch (Exception e) {
                    logger.error("Error processing message", e);
                }
            }
        }
    }
}
