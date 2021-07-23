package io.gbloch.tododemo.cucumber;

import io.cucumber.spring.CucumberContextConfiguration;
import io.gbloch.tododemo.TodoDemoApp;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

@CucumberContextConfiguration
@SpringBootTest(classes = TodoDemoApp.class)
@WebAppConfiguration
public class CucumberTestContextConfiguration {}
