package dev.bruno.mseventmanager.client.viacep;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="viacep", url="https://viacep.com.br/ws")
public interface ViaCepClient {
    @GetMapping("/{cep}/json")
    ViaCepResponse getCepInfo(@PathVariable("cep") String cep);
}
