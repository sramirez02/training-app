package com.tuempresa.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tuempresa.service.TraineeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/trainee")
@RequiredArgsConstructor
public class TraineeController {
	private final TraineeService traineeService;

}
