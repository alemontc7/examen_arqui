package com.example.examen;

import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;


import io.sentry.Sentry;
import io.sentry.protocol.User;
import io.swagger.annotations.ApiOperation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.networknt.schema.SpecVersion.VersionFlag;
import com.networknt.schema.ValidationMessage;

import java.util.Random;
import java.util.Set;

import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;


@RestController
public class PaymentController {
    

    @Operation(
        summary = "Credit Card Payment",
        tags = {"Payment"}
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success", content = {@Content(
                                                                    mediaType = "application/json",
                                                                    schema = @Schema(implementation = CardResponseDto.class))}),
        @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
        @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "404", description = "NOT_FOUND: Entity could not be found")}
    )
    @PostMapping(value = "/payment", produces = "application/json")
    public ResponseEntity<CardResponseDto> login(@RequestBody CardDto card) {
        ObjectMapper mapper = new ObjectMapper();
        String json;
        try {
            json = mapper.writeValueAsString(card);
            JsonSchemaFactory factory = JsonSchemaFactory.getInstance(VersionFlag.V7);
            JsonSchema jsonSchema = factory.getSchema(LoginController.class.getClassLoader().getResourceAsStream("schemas/card.json"));
            JsonNode jsonNode = mapper.readTree(json);
            Set<ValidationMessage> errors = jsonSchema.validate(jsonNode); 

            String errorsCombined = "";
            for( ValidationMessage error: errors) {
                errorsCombined += error.toString() +  "\n";
            }

            if(errors.size() > 0) {
                return ResponseEntity.badRequest().body(new CardResponseDto(1, "Something turned out wrong"));
            }
            return ResponseEntity.ok(new CardResponseDto(0, "Ok"));

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.ok(new CardResponseDto(0, "Ok"));
        }
    }
}

    
