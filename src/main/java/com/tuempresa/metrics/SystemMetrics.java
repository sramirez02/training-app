package com.tuempresa.metrics;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.stereotype.Component;

@Component
public class SystemMetrics implements MeterBinder {
    
    private static final String MEMORY_USAGE_METRIC = "jvm.memory.usage";
    
    @Override
    public void bindTo(MeterRegistry registry) {
        Gauge.builder(MEMORY_USAGE_METRIC, this, SystemMetrics::getMemoryUsage)
            .description("JVM memory usage percentage")
            .baseUnit("percent")
            .register(registry);
    }
    
    public double getMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        return (double) usedMemory / runtime.maxMemory() * 100;
    }
}