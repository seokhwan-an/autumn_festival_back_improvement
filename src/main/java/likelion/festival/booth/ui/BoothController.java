package likelion.festival.booth.ui;

import likelion.festival.booth.application.BoothService;
import likelion.festival.booth.application.dto.BoothCreate;
import likelion.festival.booth.application.dto.BoothDayLocationDto;
import likelion.festival.booth.application.dto.BoothDto;
import likelion.festival.booth.application.dto.BoothFilterDto;
import likelion.festival.booth.application.dto.BoothUpdate;
import likelion.festival.global.cookie.CookieUtil;
import likelion.festival.menu.application.MenuService;
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

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/api/booths")
@RestController
public class BoothController {

    private final BoothService boothService;
    private final MenuService menuService;
    private final CookieUtil cookieUtil;

    @GetMapping(params = {"filter"})
    public ResponseEntity<List<BoothFilterDto>> boothFilter(final HttpServletRequest request, @RequestParam final String filter) {
        final List<BoothFilterDto> response = boothService.boothFilterAndSearch(filter);
        response.forEach(b -> b.updateIsLike(checkIsLike(request, b.getId())));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/top5")
    public ResponseEntity<List<BoothFilterDto>> boothTopFive(final HttpServletRequest request) {
        final List<BoothFilterDto> response = boothService.boothTopFive();
        response.forEach(b -> b.updateIsLike(checkIsLike(request, b.getId())));
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<BoothDayLocationDto>> boothDayLocation(final HttpServletRequest request,
                                                                      @RequestParam final String day,
                                                                      @RequestParam final String location
    ) {
        final List<BoothDayLocationDto> response = boothService.boothDayLocation(day, location);
        response.forEach(b -> b.updateIsLike(checkIsLike(request, b.getId())));
        return ResponseEntity.ok(response);
    }

    @PostMapping()
    public ResponseEntity<Void> boothCreate(@RequestPart(value = "imgList", required = false) final List<MultipartFile> imgList,
                                            @RequestBody final BoothCreate boothDto) {
        final Long savedBoothId = boothService.create(boothDto);
        return ResponseEntity.created(URI.create("/api/booths/" + savedBoothId))
            .build();
    }

    @GetMapping("{id}")
    public ResponseEntity<BoothDto> boothRead(final HttpServletRequest request, @PathVariable final Long id) {
        final BoothDto response = boothService.read(id);
        response.updateIsLike(checkIsLike(request, id));
        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}")
    public ResponseEntity<BoothDto> boothUpdate(@PathVariable final Long id, @RequestBody final BoothUpdate boothUpdateRequest) {
        final BoothDto response = boothService.update(id, boothUpdateRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> boothDelete(@PathVariable final Long id) {
        boothService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private boolean checkIsLike(final HttpServletRequest request, final Long id) {
        final Map<String, String> likedBooths = cookieUtil.changeToMap(request.getCookies());
        return likedBooths.containsKey(id.toString());
    }
}
