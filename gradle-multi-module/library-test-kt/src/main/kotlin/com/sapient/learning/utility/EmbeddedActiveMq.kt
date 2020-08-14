package com.sapient.learning.utility

import org.springframework.test.context.ActiveProfiles
import java.lang.annotation.Inherited

@MustBeDocumented
@Inherited
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@ActiveProfiles("test")
annotation class EmbeddedActiveMq