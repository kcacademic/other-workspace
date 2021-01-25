package com.soprabanking.payments.factory

import com.soprabanking.payments.config.ValidationConfig
import com.soprabanking.payments.control.Control
import com.soprabanking.payments.domain.BaseValidatable
import org.springframework.context.support.AbstractApplicationContext
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*
import javax.annotation.PostConstruct


@Component
class ValidationFactory(val validationConfig: ValidationConfig, val applicationContext: AbstractApplicationContext) {

    val controlsByDomainAndTransition: MutableMap<String, List<Control<out BaseValidatable>>> = mutableMapOf()

    @PostConstruct
    fun prepare() {
        println(validationConfig.validators)
        for (validator in validationConfig.validators) {
            val domainObject: String = validator.domainObject
            for (stage in validator.stages) {
                val transition: String = stage.transition
                val controls: MutableList<Control<out BaseValidatable>> = ArrayList()
                for (controlName in stage.controls) {
                    controls.add(applicationContext.getBean(controlName) as Control<out BaseValidatable>)
                }
                controlsByDomainAndTransition["$domainObject-$transition"] = controls
            }
        }
    }

    fun <T : BaseValidatable> getValidators(type: String): Flux<Control<T>> {
        return Mono.just(type)
                .map {
                    if (controlsByDomainAndTransition.containsKey(it)) {
                        return@map controlsByDomainAndTransition[it] as List<Control<T>>?
                    } else {
                        throw RuntimeException("The Validators could not be located for the type: $it")
                    }
                }.flatMapMany {
                    Flux.fromIterable(it!!)
                }
    }

}