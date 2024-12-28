package com.github.im2back.orchestrator.clients;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "customerms")
public interface CustomerClient {

	
	
}
