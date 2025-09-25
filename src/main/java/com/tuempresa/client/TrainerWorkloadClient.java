package com.tuempresa.client;

import com.tuempresa.dto.WorkloadRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "trainer-workload-service")
public interface TrainerWorkloadClient {

    
    @PostMapping("/workload/update")
    ResponseEntity<String> updateWorkload(@RequestBody WorkloadRequest request);
}
	
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//
//import com.tuempresa.dto.WorkloadRequest;
//
//@FeignClient(name = "trainer-workload-service")
//public interface TrainerWorkloadClient {
//
//
//    @PostMapping("/api/workload")
//    void sendWorkload(@RequestBody WorkloadRequest request);
//}