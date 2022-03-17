package com.graphql.graphqldemo.controller;

import com.graphql.graphqldemo.Service.GraphQLService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.ExecutionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class GraphQLController {

    @Autowired
    GraphQLService graphQLService;

    @Autowired
    ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping(value = "/test")
    public String test() {
        System.out.println("hitting test endpoint");
        return "Hello";
    }

    @RequestMapping(value = "/stockData", method = RequestMethod.GET)
    public String getPreAuthDesicionData(@RequestBody String query) throws JsonProcessingException {
        ExecutionResult execute = graphQLService.initiateGraphQL().execute(query);
        Map<String, String> obj = (Map<String, String>) execute.getData();
        return objectMapper.writeValueAsString(obj);
    }
}
