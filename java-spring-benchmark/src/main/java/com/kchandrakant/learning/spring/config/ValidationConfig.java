package com.kchandrakant.learning.spring.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "epe")
public class ValidationConfig {
    /**
     * Map of Validation configurations
     */
    private Map<String, Validation> validations = new HashMap<>();

    public Map<String, Validation> getValidations() {
        return validations;
    }

    public void setValidations(Map<String, Validation> validations) {
        this.validations = validations;
    }

    public static final class Validation {

        private Map<String, List<String>> stages;

        private Map<String, Map<String, List<String>>> validators;

        public Map<String, List<String>> getStages() {
            return stages;
        }

        public void setStages(Map<String, List<String>> stages) {
            this.stages = stages;
        }

        public Map<String, Map<String, List<String>>> getValidators() {
            return validators;
        }

        public void setValidators(Map<String, Map<String, List<String>>> validators) {
            this.validators = validators;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Validation that = (Validation) o;
            return Objects.equals(stages, that.stages) &&
                    Objects.equals(validators, that.validators);
        }

        @Override
        public int hashCode() {
            return Objects.hash(stages, validators);
        }
    }

}
