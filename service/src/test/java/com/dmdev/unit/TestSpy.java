package com.dmdev.unit;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class TestSpy {

    @Test
    void trying() {
        try (MockedStatic<Mockito> mockitoMockedStatic = Mockito.mockStatic(Mockito.class)) {
        }
    }
}
