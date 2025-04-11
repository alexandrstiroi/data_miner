package org.shtiroy.data_miner.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shtiroy.data_miner.model.TenderDetail;
import org.shtiroy.data_miner.service.TenderService;
import org.shtiroy.data_miner.service.schedule.ScheduledTasks;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TenderController {
    private final ScheduledTasks scheduledTasks;
    private final TenderService tenderService;
    private static final Logger LOGGER = LogManager.getLogger(TenderController.class.getName());

    public TenderController(ScheduledTasks scheduledTasks, TenderService tenderService) {
        this.scheduledTasks = scheduledTasks;
        this.tenderService = tenderService;
    }

    @PostMapping("/update/data")
    public ResponseEntity<Object> getLastTender() {
        new Thread(scheduledTasks::getUpdateAchizitiimd).start();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/tender/info")
    public ResponseEntity<Object> getTenderInfo(@RequestBody String tenderInfo){
        LOGGER.info("/tender/info - request | {}", tenderInfo);
        return ResponseEntity.ok(tenderService.getTenderDetail(Integer.valueOf(tenderInfo)));
    }
}
