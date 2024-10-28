package org.rakib.pochttp2stream.config;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.kafka.receiver.KafkaReceiver;

import java.util.logging.Logger;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1")
public class StreamController {
    private final Logger logger = Logger.getLogger(getClass().getName());
    private final KafkaConfiguration kafkaConfiguration;
    private final Sinks.Many<Object> sink;


    public StreamController(KafkaConfiguration kafkaConfiguration) {
        this.kafkaConfiguration = kafkaConfiguration;
        this.sink = Sinks.many().multicast().onBackpressureBuffer();
    }


    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<?> streamData(@RequestParam String topic) {
        KafkaReceiver<String, Object> kafkaReceiver = kafkaConfiguration.createKafkaReceiver(topic);
        return kafkaReceiver.receive()
                .map(receiverRecord -> receiverRecord.key() + " : " + receiverRecord.value())
                .doOnNext(sink::tryEmitNext)
                .doOnSubscribe(_ -> logger.info("New client subscribed to /api/v1/stream endpoint"))
                .doOnCancel(() -> logger.info("Client disconnected from /api/v1/stream endpoint"));
    }
}

