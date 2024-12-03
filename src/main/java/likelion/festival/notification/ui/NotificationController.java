package likelion.festival.notification.ui;

import likelion.festival.global.image.application.ImageService;
import likelion.festival.notification.application.NotificationService;
import likelion.festival.notification.application.dto.NotificationCreateDto;
import likelion.festival.notification.application.dto.NotificationResponseDto;
import likelion.festival.notification.application.dto.NotificationUpdateDto;
import likelion.festival.notification.domain.NotificationType;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping(value = "api/notifications")
@RestController
public class NotificationController {

    private final NotificationService notificationService;
    private final ImageService imageService;

    @GetMapping("{id}")
    public NotificationResponseDto readNotification(@PathVariable Long id) {
        return notificationService.readNotification(id);
    }

    @GetMapping
    public ResponseEntity<List<NotificationResponseDto>> readNotificationAll(@RequestParam(required = false) NotificationType notificationType) {
        return ResponseEntity.ok(notificationService.readNotificationAll(notificationType));
    }

    @PostMapping
    public ResponseEntity<Void> createNotification(@RequestPart(value = "imgList", required = false) List<MultipartFile> imgList,
                                                   @RequestParam(value = "notification", required = false) NotificationCreateDto notificationCreateDto) {
        Long savedId = notificationService.createNotification(notificationCreateDto);
        return ResponseEntity.created(URI.create("/api/notifications/" + savedId)).build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    public ResponseEntity<NotificationResponseDto> updateNotification(@RequestBody NotificationUpdateDto request, @PathVariable Long id) {
        NotificationResponseDto response = notificationService.updateNotification(id, request);
        return ResponseEntity.ok(response);
    }
}
