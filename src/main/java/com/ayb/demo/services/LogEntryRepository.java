package com.ayb.demo.services;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ayb.demo.models.Log;

public interface LogEntryRepository extends MongoRepository<Log, String> {
}
