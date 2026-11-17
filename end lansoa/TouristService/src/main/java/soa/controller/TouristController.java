package soa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import soa.entity.Tourist;
import soa.service.TouristService;

@RestController
@RequestMapping("/api")
@Tag(name = "Tourist API", description = "Endpoints to manage and retrieve tourist records")
public class TouristController {

    @Autowired
    private TouristService touristService;

    // Read the valid token from application.properties
    @Value("${app.valid.token}")
    private String validToken;

    /**
     * GET /api/tourists
     * Returns all tourists if the Token header matches the valid token.
     * Returns 500 with an error message if the token is missing or invalid.
     */
    @GetMapping("/tourists")
    @Operation(
        summary = "Get all tourists",
        description = "Retrieves all tourist records from the database. Requires a valid Token header."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tourists retrieved successfully"),
        @ApiResponse(responseCode = "500", description = "Unauthorized: Invalid Token Provided")
    })
    public ResponseEntity<?> getAllTourists(
            @Parameter(description = "Authorization token. Use: MY_VALID_TOKEN", required = true)
            @RequestHeader(value = "Token", required = false) String token) {

        // Step 1: Check if token is missing or invalid
        if (token == null || token.isBlank() || !token.equals(validToken)) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unauthorized: Invalid Token Provided");
        }

        // Step 2: Token is valid — fetch and return all tourists
        List<Tourist> tourists = touristService.getAllTourists();
        return ResponseEntity.ok(tourists);
    }
}
