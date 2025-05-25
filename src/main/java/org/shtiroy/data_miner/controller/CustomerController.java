package org.shtiroy.data_miner.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.shtiroy.data_miner.entity.TaskInfo;
import org.shtiroy.data_miner.exception.ParserException;
import org.shtiroy.data_miner.service.CustomerService;
import org.shtiroy.data_miner.service.TaskInfoService;
import org.shtiroy.data_miner.util.TaskType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Tag(name = "Заказчик", description = "Информация о заказчике")
public class CustomerController {
    private final TaskInfoService taskInfoService;
    private final CustomerService customerService;

    public CustomerController(TaskInfoService taskInfoService, CustomerService customerService){
        this.taskInfoService = taskInfoService;
        this.customerService = customerService;
    }

    @PostMapping("/customer/tenders")
    public ResponseEntity<Object> getCustomerTenders(@RequestBody String customerId){
        log.info("Поступил запрос на сборку данных по {}", customerId);
        try {
            TaskInfo taskInfo = taskInfoService.createTask(TaskType.CUSTOMER_INFO, customerId);
            log.debug("Создана задача: {}", taskInfo);

            log.info("Запуск ");
            new Thread(() -> customerService.start(taskInfo)).start();

            return ResponseEntity.ok("Обработка заказчика "+customerId+" запущена");
        } catch (Exception e){
            log.error("Фатальная ошибка при обработке заказчика {}", customerId, e);
            throw new ParserException("Ошибка при обработке запроса");
        }
    }
}