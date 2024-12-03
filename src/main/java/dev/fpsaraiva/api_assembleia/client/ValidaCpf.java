package dev.fpsaraiva.api_assembleia.client;

import dev.fpsaraiva.api_assembleia.client.dto.CpfResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "cpfValidationClient", url = "${cpf.validation.api.url}")
public interface ValidaCpf {

    @GetMapping("/users/{cpf}")
    CpfResponse validarCpf(@RequestParam("cpf") String cpf);
}
