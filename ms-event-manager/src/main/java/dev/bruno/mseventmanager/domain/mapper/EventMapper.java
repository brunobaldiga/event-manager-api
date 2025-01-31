package dev.bruno.mseventmanager.domain.mapper;

import dev.bruno.mseventmanager.client.viacep.ViaCepResponse;
import dev.bruno.mseventmanager.domain.Event;
import dev.bruno.mseventmanager.domain.representation.EventSaveRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventMapper {
    public static void update(EventSaveRequest eventSaveRequest, Event event, ViaCepResponse cepInfo) {
        new ModelMapper().map(eventSaveRequest, event);

        if (cepInfo != null) {
            event.setLogradouro(cepInfo.getLogradouro());
            event.setBairro(cepInfo.getBairro());
            event.setCidade(cepInfo.getLocalidade());
            event.setUf(cepInfo.getUf());
        }
    }
}
