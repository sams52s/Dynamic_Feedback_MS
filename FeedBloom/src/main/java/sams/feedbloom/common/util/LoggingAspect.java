package sams.feedbloom.common.util;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {
	
	@Around("execution(* sams.feedbloom..*(..)) && !execution(* sams.feedbloom.authentication.filter..*(..))")
	public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
		long startTime = System.currentTimeMillis();
		log.info("Executing: {} with arguments: {}", joinPoint.getSignature(), joinPoint.getArgs());
		
		try {
			Object result = joinPoint.proceed();
			long duration = System.currentTimeMillis() - startTime;
			log.info("Completed: {} in {} ms with result: {}", joinPoint.getSignature(), duration, result);
			return result;
		} catch (Exception e) {
			log.error("Exception in {}: {}", joinPoint.getSignature(), e.getMessage());
			throw e;
		}
	}
}