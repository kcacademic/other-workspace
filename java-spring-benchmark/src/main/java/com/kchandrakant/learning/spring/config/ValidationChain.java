package com.kchandrakant.learning.spring.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import com.kchandrakant.learning.spring.model.base.BaseValidatable;
import com.kchandrakant.learning.spring.validator.base.Validator;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Configuration
public class ValidationChain {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidationChain.class);

    private static final String CACHE_KEY_SEPARATOR = "::";

    private final ApplicationContext applicationContext;
    private final ValidationConfig validationConfig;

    private final Map<String, Validator<? extends BaseValidatable>> validatorBeans = new HashMap<>();

    private final Map<String, List<Validator<? extends BaseValidatable>>> validatorBeansByStageByOperationCodeByValidationTypeFlat = new HashMap<>();

    private final Map<String, Map<String, Map<String, List<Validator<? extends BaseValidatable>>>>> validatorBeansByStageByOperationCodeByValidationTypeNested = new HashMap<>();

    public ValidationChain(ApplicationContext applicationContext,
                           ValidationConfig validationConfig) {
        this.applicationContext = applicationContext;
        this.validationConfig = validationConfig;
    }

    @PostConstruct
    private void postConstruct() {
        validationConfig.getValidations()
                .values()
                .stream()
                .flatMap(validation -> validation.getValidators().values().stream())
                .flatMap(validatorsByStages -> validatorsByStages.values().stream())
                .flatMap(Collection::stream)
                .forEach(this::verifyAndCacheValidatorBean);

        for (Map.Entry<String, ValidationConfig.Validation> validationEntry : validationConfig.getValidations().entrySet()) {
            String validationType = validationEntry.getKey();
            ValidationConfig.Validation validation = validationEntry.getValue();
            Map<String, Map<String, List<Validator<? extends BaseValidatable>>>> validatorBeansByType = new HashMap<>();
            for (Map.Entry<String, Map<String, List<String>>> validatorByOperationTypeEntry : validation.getValidators().entrySet()) {
                String operationType = validatorByOperationTypeEntry.getKey();
                Map<String, List<String>> validatorByOperationType = validatorByOperationTypeEntry.getValue();
                Map<String, List<Validator<? extends BaseValidatable>>> validatorBeansByOperationType = new HashMap<>();
                for (Map.Entry<String, List<String>> validatorByStageEntry : validatorByOperationType.entrySet()) {
                    String validationStage = validatorByStageEntry.getKey();
                    List<String> validatorByStage = validatorByStageEntry.getValue();
                    List<Validator<? extends BaseValidatable>> validatorBeansByStage = new ArrayList<>();
                    for (String validatorBeanName : validatorByStage) {
                        validatorBeansByStage.add((Validator) applicationContext.getBean(validatorBeanName));
                    }
                    validatorBeansByOperationType.put(validationStage, validatorBeansByStage);
                    validatorBeansByStageByOperationCodeByValidationTypeFlat.put(getCacheKey(validationType, operationType, validationStage), validatorBeansByStage);
                }
                validatorBeansByType.put(operationType, validatorBeansByOperationType);
            }
            validatorBeansByStageByOperationCodeByValidationTypeNested.put(validationType, validatorBeansByType);
        }
    }

    private void verifyAndCacheValidatorBean(String validatorName) {
        Object bean = applicationContext.getBean(validatorName);
        if (null == bean) {
            LOGGER.warn("Bean declaration cannot be found for following validator : " + validatorName);
        }
        if (bean instanceof Validator) {
            // It will throw an exception if the validator does not match with Validator<? extends BaseValidatable>
            validatorBeans.put(validatorName, (Validator) bean);
        } else {
            LOGGER.warn("Following validator bean does not implement Validator interface : " + validatorName);
        }
    }

    public <T extends BaseValidatable> List<Validator<T>> getValidators(String validationType, String operationCode, String validationStage) {
        ValidationConfig.Validation validation = validationConfig.getValidations().get(validationType);
        Map<String, List<String>> validatorsByStage = validation.getValidators().get(operationCode);
        List<String> validators = validatorsByStage.get(validationStage);
        return validators.stream()
                // It will throw an exception if the retrieved validator does not match with Validator<T>
                .map(validatorName -> (Validator<T>) validatorBeans.get(validatorName))
                .collect(Collectors.toList());
    }

    public <T extends BaseValidatable> List<Validator<T>> getValidatorsFromContext(String validationType, String operationCode, String validationStage) {
        ValidationConfig.Validation validation = validationConfig.getValidations().get(validationType);
        Map<String, List<String>> validatorsByStage = validation.getValidators().get(operationCode);
        List<String> validators = validatorsByStage.get(validationStage);
        return validators.stream()
                // It will throw an exception if the retrieved validator does not match with Validator<T>
                .map(validatorName -> (Validator<T>) applicationContext.getBean(validatorName))
                .collect(Collectors.toList());

    }

    public <T extends BaseValidatable> List<Validator<T>> getValidatorsFromFlatCache(String validationType, String operationCode, String validationStage) {
        List<Validator<? extends BaseValidatable>> validators =
                validatorBeansByStageByOperationCodeByValidationTypeFlat.get(getCacheKey(validationType, operationCode, validationStage));

        return (List) validators;

        //return validators.stream()
        //        // It will throw an exception if the retrieved Validator does not match with Validator<T>
        //        .map(validator -> (Validator<T>) validator)
        //        .collect(Collectors.toList());

    }

    public <T extends BaseValidatable> List<Validator<T>> getValidatorsFromNestedCache(String validationType, String operationCode, String validationStage) {
        List<Validator<? extends BaseValidatable>> validators =
                validatorBeansByStageByOperationCodeByValidationTypeNested.get(validationType).get(operationCode).get(validationStage);

        return (List) validators;

        //return validators.stream()
        //        // It will throw an exception if the retrieved Validator does not match with Validator<T>
        //        .map(validator -> (Validator<T>) validator)
        //        .collect(Collectors.toList());
    }

    private String getCacheKey(String validationTypeName, String operationCode, String validationStage) {
        return validationTypeName + CACHE_KEY_SEPARATOR + operationCode + CACHE_KEY_SEPARATOR + validationStage;
    }

}
