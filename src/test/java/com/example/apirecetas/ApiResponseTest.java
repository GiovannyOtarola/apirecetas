package com.example.apirecetas;


import org.junit.jupiter.api.Test;

import com.example.apirecetas.model.ApiResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class ApiResponseTest {

    @Test
    void testApiResponseConstructorAndGetters() {
        // Arrange
        String expectedMessage = "Operación exitosa";
        boolean expectedSuccess = true;

        // Act
        ApiResponse apiResponse = new ApiResponse(expectedMessage, expectedSuccess);

        // Assert
        assertEquals(expectedMessage, apiResponse.getMessage());
        assertTrue(apiResponse.isSuccess());
    }

    @Test
    void testSetters() {
        // Arrange
        ApiResponse apiResponse = new ApiResponse("Operación fallida", false);

        // Act
        apiResponse.setMessage("Operación exitosa");
        apiResponse.setSuccess(true);

        // Assert
        assertEquals("Operación exitosa", apiResponse.getMessage());
        assertTrue(apiResponse.isSuccess());
    }

    @Test
    void testToString() {
        // Arrange
        ApiResponse apiResponse = new ApiResponse("Operación exitosa", true);

        // Act
        String result = apiResponse.toString();

        // Assert
        assertTrue(result.contains("Operación exitosa"));
        assertTrue(result.contains("true"));
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        ApiResponse apiResponse1 = new ApiResponse("Operación exitosa", true);
        ApiResponse apiResponse2 = new ApiResponse("Operación exitosa", true);

        // Act & Assert
        assertEquals(apiResponse1, apiResponse2); // Verifica que los objetos sean iguales
        assertEquals(apiResponse1.hashCode(), apiResponse2.hashCode()); // Verifica que sus hashCodes sean iguales
    }
}