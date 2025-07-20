package com.example.messages.repository;

import com.example.messages.entity.MessageRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRecordRepository extends JpaRepository<MessageRecord, Long> {
}
