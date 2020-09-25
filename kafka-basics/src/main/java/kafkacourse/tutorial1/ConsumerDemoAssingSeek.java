package kafkacourse.tutorial1;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ConsumerDemoAssingSeek {

    private static Logger logger = LoggerFactory.getLogger(ConsumerDemoAssingSeek.class);

    public static void main(String[] args) {
        String bootstrapServers = "localhost:9092";

        String topic = "first_topic";

        //consumer properties
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        //create consumer
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);

        // assign and seek are mostly used to replay data or fetch a specific message

        //assing
        TopicPartition partitionToReadFrom = new TopicPartition(topic, 0);
        long offsetToRead = 15L;
        consumer.assign(Arrays.asList(partitionToReadFrom));

        //seek
        consumer.seek(partitionToReadFrom, offsetToRead);

        int numberOfMeshToRead = 5;
        boolean keepOnRiding = true;
        int numberOFmEsgReadSoFar = 0;

        //poll new data
        while(keepOnRiding){
            ConsumerRecords<String, String> records =  consumer.poll(Duration.ofMillis(100));
            for(ConsumerRecord<String, String> record: records){
                numberOFmEsgReadSoFar++;
                logger.info("Key: " + record.key() + ", Value: " + record.value());
                logger.info("Partition: " + record.partition() + ", Offset: " + record.offset());
                if(numberOFmEsgReadSoFar >= numberOfMeshToRead){
                    keepOnRiding = false;
                    break;
                }
            }

        }
        logger.info("Exiting application");

    }
}
