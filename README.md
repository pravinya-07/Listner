# Listner
Listner is responsible for consuming records/messages produced by the producer.
It checks after every 20 seconds of interval(editable) weather records/messages are produced by the producer in the SQS queue.
And consumes those records/messages with maximum number of messages as 10(editable).
