//package com.pobluesky.kafka;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Service;
//
//@Slf4j
//@Service
//public class KafkaConsumer {
//
//    @KafkaListener(topics = "user")
//    public void handleUser(String msg) {
//
//        // 알림 전송 로직 처리
//
//        log.info("카프카 유저 이벤트 : {}",msg);
//    }
//
//    @KafkaListener(topics = "inquiry")
//    public void handleInquiry(String msg) {
//
//        // 알림 전송 로직 처리
//        log.info("카프카 문의 이벤트 : {}",msg);
//    }
//
//    @KafkaListener(topics = "voc")
//    public void handleVoc(String msg) {
//
//        // 알림 전송 로직 처리
//        log.info("카프카 VoC 이벤트 : {}",msg);
//    }
//
//}
