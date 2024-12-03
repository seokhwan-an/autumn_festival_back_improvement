package likelion.festival.global.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import likelion.festival.notification.application.dto.NotificationResponseDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class NotificationStringToRequestDtoConverter extends Throwable implements Converter<String, NotificationResponseDto> {
    private ObjectMapper objectMapper;

    public NotificationStringToRequestDtoConverter(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }
    NotificationResponseDto notificationResponseDto;

    @Override
    public NotificationResponseDto convert(String source){
        try{
            notificationResponseDto = objectMapper.readValue(source, new TypeReference<NotificationResponseDto>() {
            });
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return notificationResponseDto;
    }
}
