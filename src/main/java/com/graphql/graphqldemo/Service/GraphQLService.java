package com.graphql.graphqldemo.Service;

import com.graphql.graphqldemo.repository.StocksDataFetcher;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;







@Service
public class GraphQLService {
    private GraphQL graphQL;

    @Autowired
    StocksDataFetcher stocksDataFetcher;

    @Value("schema.graphql")
    private ClassPathResource classPathResource;

    @PostConstruct
    private void loadSchema() throws IOException {

        InputStream st = classPathResource.getInputStream();
        Reader targetReader = new InputStreamReader(st);

        TypeDefinitionRegistry typeDefinitionRegistry = new SchemaParser().parse(targetReader);
        RuntimeWiring runtimeWiring = buildRunTimeWiring();

        GraphQLSchema graphQLSchema =  new SchemaGenerator().makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);
        graphQL = GraphQL.newGraphQL(graphQLSchema).build();

    }


    private RuntimeWiring buildRunTimeWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type("Query", typeWiring -> typeWiring
                        .dataFetcher("stocks", stocksDataFetcher))
                .build();
    }


    public GraphQL initiateGraphQL() {return graphQL;}
}
