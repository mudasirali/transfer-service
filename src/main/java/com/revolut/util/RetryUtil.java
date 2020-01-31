package com.revolut.util;

import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

public class RetryUtil {


    public static final int MAX_RETRIES = 3;

    @Slf4j
    public static class RetryExecutor<T, R> {

        private int maxAttampts;
        private int currentAttempt;
        private Class<? extends Throwable> throwableType;

        public RetryExecutor(int maxAttampts, int currentAttempt) {
            this.maxAttampts = maxAttampts;
            this.currentAttempt = currentAttempt;
            this.throwableType = Throwable.class;
        }

        public RetryExecutor<T, R> retryOn(Class<? extends Throwable> throwableType) {
            this.throwableType = throwableType;
            return this;
        }

        public R execute(Supplier<R> execution) {
            R result = null;
            boolean failed = false;
            do {
                try {
                    result = execution.get();
                    failed = false;
                } catch (Exception e) {
                    failed = true;
                    if (!throwableType.isAssignableFrom(e.getClass())) {
                        throw e;
                    } else if (currentAttempt >= maxAttampts) {
                        throw e;
                    }
                    this.currentAttempt++;
                    log.debug("execution failed: retrying attempt {}/{}", currentAttempt, maxAttampts);
                }
            } while (failed);

            return result;
        }

    }

    public static <T, R> RetryExecutor<T, R> withRetries(int maxRetries) {
        return new RetryExecutor<T, R>(maxRetries, 0);
    }

    public static <T, R> RetryExecutor<T, R> defaultExecutor() {
        return withRetries(MAX_RETRIES);
    }
}
