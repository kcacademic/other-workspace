package com.sapient.learning

import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.atomic.AtomicLong
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@RestController
class GreetingController {
	
	val counter = AtomicLong()
	
	@RequestMapping("/greeting")
	fun greeting(@RequestParam(value = "name", defaultValue = "World") name: String): Greeting {
		return Greeting(counter.incrementAndGet(), "Hello, $name")
	}
	
	
}