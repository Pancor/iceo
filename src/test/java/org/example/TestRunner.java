package org.example;

import io.cucumber.java.ParameterType;
import org.example.model.RatesErrorCode;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasspathResource("features")
public class TestRunner {

    @ParameterType("true|True|TRUE|false|False|FALSE")
    public Boolean booleanValue(String value) {
        return Boolean.valueOf(value);
    }

    @ParameterType("[^\\s]*")
    public RatesErrorCode ratesErrorCode(String value) {
        return RatesErrorCode.json(value);
    }
}
