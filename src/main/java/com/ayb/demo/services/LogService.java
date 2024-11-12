package com.ayb.demo.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ayb.demo.models.Log;

@Service
public class LogService {

   @Autowired
    private LogEntryRepository logEntryRepo;
    public void log(String message, String type, String email, String tax, String productId
    ) { 

        var loginLog = new Log();
        loginLog.setMail(email);
        loginLog.setType(type);
        loginLog.setTimestamp(new Date());
        loginLog.setMessage(message);

        if (tax != null) {
            loginLog.setTax(tax);
        };

        if (productId != null) {
            loginLog.setProductId(productId);
        };

        logEntryRepo.save(loginLog);


    }
}
