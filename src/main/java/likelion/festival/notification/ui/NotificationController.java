package likelion.festival.notification.ui;

import likelion.festival.notification.application.NotificationService;
import likelion.festival.notification.application.dto.NotificationCreateDto;
import likelion.festival.notification.application.dto.NotificationResponseDto;
import likelion.festival.notification.application.dto.NotificationUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping(value = "api/notifications")
@RestController
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("{id}")
    public ResponseEntity<NotificationResponseDto> readNotification(@PathVariable final Long id) {
        final NotificationResponseDto response = notificationService.readNotification(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<NotificationResponseDto>> readNotificationAll(@RequestParam(required = false) final String notificationType) {
        return ResponseEntity.ok(notificationService.readNotificationAll(notificationType));
    }

    @PostMapping
    public ResponseEntity<Void> createNotification(@RequestBody final NotificationCreateDto notificationCreateDto) {
        final Long savedId = notificationService.createNotification(notificationCreateDto);
        return ResponseEntity.created(URI.create("/api/notifications/" + savedId)).build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable final Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    public ResponseEntity<NotificationResponseDto> updateNotification(@PathVariable final Long id, @RequestBody final NotificationUpdateDto request) {
        final NotificationResponseDto response = notificationService.updateNotification(id, request);
        return ResponseEntity.ok(response);
    }
}
