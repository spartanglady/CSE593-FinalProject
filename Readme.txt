#ReadMe

Setup Instructions:

Step 1: Setup Localstack
	Refer this guide for setting up localstack in your computer: https://docs.localstack.cloud/getting-started/installation/

Step 2: Provision AWS Resources in LocalStack
	awslocal kinesis create-stream --stream-name user-data-stream --shard-count 2
	awslocal kinesis list-streams
	awslocal kinesis describe-stream --stream-name user-data-stream

	awslocal sns create-topic --name sev3-email
	awslocal sns create-topic --name sev2-sms
	awslocal sns create-topic --name sev1-phone
	awslocal sns list-topics

	awslocal sqs create-queue --queue-name sev3-email
	awslocal sqs create-queue --queue-name sev2-sms
	awslocal sqs create-queue --queue-name sev1-phone
	awslocal sqs list-queues

	awslocal sqs get-queue-attributes --attribute-names QueueArn --queue-url=http://localhost:4566/000000000000/sev3-email

	awslocal sns subscribe --topic-arn arn:aws:sns:us-east-1:000000000000:sev3-email --protocol sqs --notification-endpoint arn:aws:sqs:us-east-1:000000000000:sev3-email

	awslocal sns subscribe --topic-arn arn:aws:sns:us-east-1:000000000000:sev2-sms --protocol sqs --notification-endpoint arn:aws:sqs:us-east-1:000000000000:sev2-sms

	awslocal sns subscribe --topic-arn arn:aws:sns:us-east-1:000000000000:sev1-phone --protocol sqs --notification-endpoint arn:aws:sqs:us-east-1:000000000000:sev1-phone

Step 3: Start FraudListenerService
	Go inside FraudListenerService Folder and run ./gradlew bootRun


Step 4: Start FraudEngine Service
	Go inside FraudEngine Service and run ./gradlew bootRun


Step 5: Start DemoAppServer
	Go inside DempoppServer folder and run ./gradlew bootRun

Step 6: Start Front End Server (sse-client)
	Go inside sse-client folder and run npm start

Step 7: Run Tests
	Go inside Test Projects and run ./gradlew test