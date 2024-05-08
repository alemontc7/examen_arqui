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
public class LoginController {
    

    public String generateToken() {
        int leftLimit = 48;
        int rightLimit = 122; 
        int targetStringLength = 32;

        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomInt = leftLimit + (int) (random.nextDouble() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomInt);
        }
    return buffer.toString();
}

@GetMapping("/")
    public String index() throws Exception {
        throw new Exception("This is a test.");
    }

    //@ApiResponse(responseCode = "200", description = "OK")
    //@ApiResponse(responseCode = "400", description = "Invalid request", content = @Content)
    //@ApiResponse(responseCode = "404", description = "Invalid email or password", content = @Content)
    @Operation(
        summary = "Validates Login",
        tags = {"login"}
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success", content = {@Content(
                                                                    mediaType = "application/json",
                                                                    schema = @Schema(implementation = LoginResponseDto.class))}),
        @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
        @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "404", description = "NOT_FOUND: Entity could not be found")}
    )
    @PostMapping(value = "/login", produces = "application/json")
    public ResponseEntity<LoginResponseDto> login(@RequestBody UserDto user) {
        ObjectMapper mapper = new ObjectMapper();
        String json;
        try {
            json = mapper.writeValueAsString(user);
            JsonSchemaFactory factory = JsonSchemaFactory.getInstance(VersionFlag.V7);
            JsonSchema jsonSchema = factory.getSchema(LoginController.class.getClassLoader().getResourceAsStream("schemas/user.json"));
            JsonNode jsonNode = mapper.readTree(json);
            Set<ValidationMessage> errors = jsonSchema.validate(jsonNode); 

            String errorsCombined = "";
            for( ValidationMessage error: errors) {
                errorsCombined += error.toString() +  "\n";
            }

            if(errors.size() > 0) {
                return ResponseEntity.badRequest().body(new LoginResponseDto(0, null, "Missing password"));
            }
            long timestamp = System.currentTimeMillis();
            String token = generateToken();
            return ResponseEntity.ok(new LoginResponseDto(timestamp, token));

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            long timestamp = System.currentTimeMillis();
            String token = generateToken();
            return ResponseEntity.ok(new LoginResponseDto(timestamp, token));
        }
    }
}

    
