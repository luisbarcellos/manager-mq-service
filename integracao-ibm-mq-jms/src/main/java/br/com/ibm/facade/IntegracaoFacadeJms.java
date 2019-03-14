package br.com.ibm.facade;

import br.com.ibm.config.IntegracaoPropertiesLoader;
import br.com.ibm.exception.MensagemJmsException;
import br.com.ibm.service.IntegracaoServiceInJms;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@AllArgsConstructor
public class IntegracaoFacadeJms {
    private IntegracaoPropertiesLoader integracaoPropertiesLoader;
    private IntegracaoServiceInJms integracaoServiceInJms;

    public List<String> buscarMensagensFilaInJms(Integer quantidade) {
        try {
            return new ForkJoinPool(integracaoPropertiesLoader.getNumeroThreads()).submit(() ->
                        IntStream.range(0, quantidade)
                                .parallel()
                                .mapToObj(i -> integracaoServiceInJms.buscarMensagem())
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList())).get();
        } catch (Exception e) {
            e.printStackTrace();
            throw new MensagemJmsException("Erro ao buscar mensagem: " + e.getMessage());
        }
    }
}