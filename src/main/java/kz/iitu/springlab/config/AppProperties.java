package kz.iitu.springlab.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@Validated
@ConfigurationProperties(prefix = "app")
public record AppProperties(
        @NotBlank String owner,
        @NotBlank String group,
        @NotNull @Valid Mail mail,
        @NotNull @Valid Cache cache
) {

    public record Mail(
            @NotBlank @Email String from,
            @Min(1) @Max(10) @DefaultValue("3") int retryCount,
            @NotNull @DefaultValue("5s") Duration timeout,
            @DefaultValue("true") boolean enabled
    ) {
    }

    public record Cache(
            @NotNull @DefaultValue("10m") Duration ttl,
            @Min(10) @Max(10000) @DefaultValue("1000") int maxEntries
    ) {
    }
}