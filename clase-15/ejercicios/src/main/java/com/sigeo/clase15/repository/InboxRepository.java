package com.sigeo.clase15.repository;

import com.sigeo.clase15.model.InboxMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InboxRepository extends JpaRepository<InboxMessage, String> {
}
