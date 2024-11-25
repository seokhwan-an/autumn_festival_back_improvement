package likelion.festival.booth.ui;

import likelion.festival.booth.application.BoothService;
import likelion.festival.booth.application.dto.BoothCreate;
import likelion.festival.booth.application.dto.BoothDayLocationDto;
import likelion.festival.booth.application.dto.BoothDto;
import likelion.festival.booth.application.dto.BoothFilterDto;
import likelion.festival.booth.application.dto.BoothUpdate;
import likelion.festival.comment.appliction.CommentService;
import likelion.festival.comment.appliction.dto.CommentRequestDto;
import likelion.festival.comment.appliction.dto.CommentResponseDto;
import likelion.festival.like.application.LikesService;
import likelion.festival.like.application.dto.LikesResponseDto;
import likelion.festival.menu.application.MenuService;
import likelion.festival.menu.application.dto.MenuRequestDto;
import likelion.festival.menu.application.dto.MenuResponseDto;
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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RequestMapping("/api/booths")
@RestController
public class BoothController {

    private final BoothService boothService;
    private final LikesService likesService;
    private final CommentService commentService;
    private final MenuService menuService;

    @GetMapping(params = {"filter"})
    public ResponseEntity<List<BoothFilterDto>> boothFilter(HttpServletRequest request, @RequestParam String filter) {
        List<BoothFilterDto> response = boothService.boothFilterAndSearch(filter);
        response.forEach(b -> b.updateIsLike(checkIsLike(request, b.getId())));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/top5")
    public ResponseEntity<List<BoothFilterDto>> boothTopFive(HttpServletRequest request) {
        List<BoothFilterDto> response = boothService.boothTopFive();
        response.forEach(b -> b.updateIsLike(checkIsLike(request, b.getId())));
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<BoothDayLocationDto>> boothDayLocation(HttpServletRequest request,
                                                                      @RequestParam String day,
                                                                      @RequestParam String location
    ) {
        List<BoothDayLocationDto> response = boothService.boothDayLocation(day, location);
        response.forEach(b -> b.updateIsLike(checkIsLike(request, b.getId())));
        return ResponseEntity.ok(response);
    }

    @PostMapping()
    public ResponseEntity<Void> boothCreate(@RequestPart(value = "imgList", required = false) List<MultipartFile> imgList,
                                            @RequestParam(value = "boothDto") BoothCreate boothDto) {
        Long savedBoothId = boothService.create(boothDto);
        return ResponseEntity.created(URI.create("/api/booths/" + savedBoothId))
            .build();
    }

    @GetMapping("{id}")
    public ResponseEntity<BoothDto> boothRead(HttpServletRequest request, @PathVariable Long id) {
        BoothDto response = boothService.read(id);
        response.updateIsLike(checkIsLike(request, id));
        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}")
    public ResponseEntity<BoothDto> boothUpdate(@PathVariable Long id, @RequestBody BoothUpdate boothUpdateRequest) {
        final BoothDto response = boothService.update(id, boothUpdateRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> boothDelete(@PathVariable Long id) {
        boothService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private boolean checkIsLike(HttpServletRequest request, Long id) {
        Optional<Cookie> boothCookie = likesService.findBoothCookie(request, id);
        return boothCookie.isPresent();
    }

    @PostMapping("/{id}/likes")
    public LikesResponseDto likeCreate(@PathVariable Long id, HttpServletRequest request,
                                       HttpServletResponse response) {
        Optional<Cookie> boothCookie = likesService.findBoothCookie(request, id);
        if (boothCookie.isPresent()) {
            throw new IllegalArgumentException("이미 쿠키 있음");
        }
        LikesResponseDto likes = likesService.create(id);
        Cookie keyCokkie = new Cookie(id.toString(), likes.getCookieKey());
        keyCokkie.setMaxAge(7 * 60 * 60 * 24);
        keyCokkie.setPath("/");
        response.addCookie(keyCokkie);
        return likes;
    }

    @DeleteMapping("/{id}/likes")
    public String likeDelete(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
        Optional<Cookie> boothCookie = likesService.findBoothCookie(request, id);
        if (boothCookie.isPresent()) {
            Cookie userCookie = boothCookie.get();
            String cookieKey = userCookie.getValue();
            likesService.delete(id, cookieKey);

            Cookie keyCookie = new Cookie(id.toString(), null);
            keyCookie.setMaxAge(0);
            keyCookie.setPath("/");
            response.addCookie(keyCookie);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        return "Ok";
    }

    @PostMapping("{id}/comments")
    public CommentResponseDto createComment(@PathVariable Long id, @RequestBody CommentRequestDto commentRequestDto,
                                            HttpServletRequest request) {
        return commentService.create(id, commentRequestDto, request);
    }

    @GetMapping("{id}/comments")
    public List<CommentResponseDto> getCommentList(@PathVariable Long id) {
        return commentService.getAll(id);
    }

    @GetMapping("{id}/menus")
    public List<MenuResponseDto> getMenuList(@PathVariable Long id) {
        return menuService.getAll(id);
    }

    @PostMapping("{id}/menus")
    public MenuResponseDto createMenu(@PathVariable Long id, @RequestBody MenuRequestDto menuRequestDto) {
        return menuService.create(id, menuRequestDto);
    }
}
