package org.ylab.infrastructure.in.servlets;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ylab.domain.dto.MeasurementOutDto;
import org.ylab.domain.models.Measurement;
import org.ylab.services.AdminService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServletTest {
    @InjectMocks
    private AdminServlet adminServlet;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;

    @Mock
    private AdminService adminService;

    @Test
    void doGet_measurements_204() throws IOException {
        when(request.getPathInfo()).thenReturn("/measurements");
        when(adminService.findAllMeasurements()).thenReturn(Collections.emptyMap());
        adminServlet.doGet(request, response);
        assertThat(response.getStatus() == 204);
    }
}