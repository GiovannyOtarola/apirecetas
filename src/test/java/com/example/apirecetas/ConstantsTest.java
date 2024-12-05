package com.example.apirecetas;

import org.junit.jupiter.api.Test;

import com.example.apirecetas.contants.Constants;

import io.jsonwebtoken.security.WeakKeyException;

import java.lang.reflect.Constructor;
import java.security.Key;
import static org.junit.jupiter.api.Assertions.*;

public class ConstantsTest {
    
    @Test
    void testGetSigningKey() {
        String secret = "ZnJhc2VzbGFyZ2FzcGFyYWNvbG9jYXJjb21vY2xhdmVlbnVucHJvamVjdG9kZWVtZXBsb3BhcmFqd3Rjb25zcHJpbmdzZWN1cml0eQ==bWlwcnVlYmFkZWVqbXBsb3BhcmFiYXNlNjQ=";

        // Llamar al método
        Key key = Constants.getSigningKey(secret);

        // Verificar que el Key no es nulo
        assertNotNull(key);

        // Verificar que el Key es del tipo esperado
        assertTrue(key instanceof javax.crypto.SecretKey);
    }

    @Test
    void testGetSigningKeyB64() {
        String secret = "ZnJhc2VzbGFyZ2FzcGFyYWNvbG9jYXJjb21vY2xhdmVlbnVucHJvamVjdG9kZWVtZXBsb3BhcmFqd3Rjb25zcHJpbmdzZWN1cml0eQ==bWlwcnVlYmFkZWVqbXBsb3BhcmFiYXNlNjQ=";

        // Llamar al método
        Key key = Constants.getSigningKeyB64(secret);

        // Verificar que el Key no es nulo
        assertNotNull(key);

        // Verificar que el Key es del tipo esperado
        assertTrue(key instanceof javax.crypto.SecretKey);
    }

    @Test
    void testInvalidKeyLength() {
        // Se debe generar un error si el key no tiene una longitud adecuada
        String shortSecret = "shortkey";  // Clave demasiado corta

        // Verificar que el método lanza un WeakKeyException por un secreto inválido
        assertThrows(WeakKeyException.class, () -> {
            Constants.getSigningKey(shortSecret);
        });
    }

    @Test
    void testPrivateConstructor() throws Exception {
        // Use reflection to access the private constructor
        Constructor<Constants> constructor = Constants.class.getDeclaredConstructor();
        
        // Make the constructor accessible
        constructor.setAccessible(true);
        
        // Try to invoke the constructor and catch the InvocationTargetException
        try {
            constructor.newInstance();
        } catch (Exception e) {
            // Unwrap the exception and check if it's an UnsupportedOperationException
            if (e.getCause() instanceof UnsupportedOperationException) {
                return;  // Test passes if the cause is UnsupportedOperationException
            } else {
                throw e;  // Re-throw if it's not the expected exception
            }
        }
        
        // If no exception was thrown, fail the test
        throw new AssertionError("Expected UnsupportedOperationException but got none.");
    }
}
