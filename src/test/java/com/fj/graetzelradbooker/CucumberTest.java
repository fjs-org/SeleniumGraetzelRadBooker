package com.fj.graetzelradbooker;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages("features")
@ConfigurationParameter(key = "cucumber.glue", value = "com.fj.graetzelradbooker")
@ConfigurationParameter(key = "cucumber.plugin", value = "pretty,json:target/cucumber.json,html:target/index.html")
public class CucumberTest {
}
