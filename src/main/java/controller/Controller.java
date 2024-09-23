package controller;

import jakarta.servlet.http.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.stream.Collectors;
import java.security.InvalidParameterException;

import static constants.Constants.*;

public interface Controller {
    default <T> T getObjectFromBody(ObjectMapper mapper, HttpServletRequest req, Class<T> valueType) {
        try {
            BufferedReader reader = req.getReader();
            String requestBody = reader.lines().collect(Collectors.joining(System.lineSeparator()));
            return mapper.readValue(requestBody, valueType);
        } catch (IOException e) {
            throw new InvalidParameterException(DATA_FORMAT_ERROR);
        }
    }
}
