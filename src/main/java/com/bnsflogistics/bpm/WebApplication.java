package com.bnsflogistics.bpm;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.time.LocalDate;

@SpringBootApplication
public class WebApplication {
    private static final String PROCESS_NAME = "DemoProcess";
    private static final String BUSINESS_KEY = "demo1";
    private static final String VARIABLE_NAME = "myVariable";

    @Autowired
    private RuntimeService runtimeService;

    public static void main(String... args) {
        SpringApplication.run(WebApplication.class, args);
    }

    @PostConstruct
    public void attempt() {
        String processId = getProcessInstanceId();
        MyVariable myVariable = (MyVariable) runtimeService.getVariable(processId, VARIABLE_NAME);
        System.out.println(myVariable);
    }

    private String getProcessInstanceId() {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceBusinessKey(BUSINESS_KEY)
                .singleResult();

        if (processInstance != null) {
            return processInstance.getId();
        }

        processInstance = runtimeService.startProcessInstanceByKey(PROCESS_NAME, BUSINESS_KEY);

        runtimeService.setVariable(processInstance.getId(), VARIABLE_NAME, MyVariable.builder()
                .a(1)
                .b(2)
                .c("C")
                .d(LocalDate.now())
//                .thisWillBeDeletedInNextRelease(LocalDateTime.now())
                .build());

        return processInstance.getId();
    }

}
