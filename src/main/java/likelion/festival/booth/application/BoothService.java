package likelion.festival.booth.application;

import likelion.festival.booth.application.dto.*;
import likelion.festival.booth.domain.Booth;
import likelion.festival.booth.domain.repository.BoothRepository;
import likelion.festival.global.exception.WrongBoothId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BoothService {

    private final BoothRepository boothRepository;

    public List<BoothFilterDto> boothFilterAndSearch(String search) {
        List<Booth> booths = boothRepository.findByTitleContaining(search);
        if (booths.isEmpty()) {
            booths = boothRepository.findByLocation(search);
        }
        if (booths.isEmpty()) {
            booths = boothRepository.findByMenus_NameContaining(search);
        }
        return booths.stream()
            .map(b -> BoothFilterDto.of(b, checkActive(b)))
            .collect(Collectors.toList());
    }

    public List<BoothFilterDto> boothTopFive() {
        List<Booth> booths = boothRepository.findAll();
        return booths.stream()
            .map(b -> BoothFilterDto.of(b, checkActive(b)))
            .filter(BoothFilterDto::isActive)
            .sorted(Comparator.comparing(BoothFilterDto::getLikeCnt).reversed())
            .limit(5)
            .collect(Collectors.toList());
    }

    //날짜와 장소로 필터링 하는 기능 ok
    public List<BoothDayLocationDto> boothDayLocation(String day, String location) {
        HashMap<String, String> date = festivalDate();
        LocalDate today = StringToDate(date.get(day));
        List<Booth> booths = boothRepository.findByLocation(location);
        return booths.stream()
            .filter(e -> StringToDate(e.getStartAt()).isBefore(today)
                && StringToDate(e.getEndAt()).isAfter(today)
                || StringToDate(e.getStartAt()).isEqual(today)
                || StringToDate(e.getEndAt()).isEqual(today))
            .map(BoothDayLocationDto::of)
            .collect(Collectors.toList());
    }

    //생성 ok
    @Transactional
    public Long create(BoothCreate request) {
        Booth booth = Booth.forSave(
            request.getTitle(),
            request.getContent(),
            request.getNotice(),
            request.getBoothType(),
            request.getIntroduction(),
            request.getLocation(),
            request.getBoothNo(),
            request.getStartAt(),
            request.getEndAt()
        );

        return boothRepository.save(booth).getId();
    }

    //읽기 ok
    public BoothDto read(Long id) {
        Booth booth = boothRepository.findById(id)
            .orElseThrow(WrongBoothId::new);
        return BoothDto.of(booth);
    }

    //수정 ok
    @Transactional
    public BoothDto update(Long id, BoothUpdate request) {
        Booth booth = boothRepository.findById(id)
            .orElseThrow(WrongBoothId::new);

        booth.update(request.getTitle(),
            request.getContent(),
            request.getNotice(),
            request.getBoothType(),
            request.getIntroduction(),
            request.getLocation(),
            request.getBoothNo(),
            request.getStartAt(),
            request.getEndAt()
        );
        return BoothDto.of(booth);
    }

    //삭제
    @Transactional
    public String delete(Long id) {
        Optional<Booth> booth = boothRepository.findById(id);
        if (booth.isEmpty()) {
            throw new WrongBoothId();
        }
        boothRepository.delete(booth.get());
        return "Ok";
    }

    public HashMap<String, String> festivalDate() {
        HashMap<String, String> date = new HashMap<>();
        date.put("1", "2022-09-28");
        date.put("2", "2022-09-29");
        date.put("3", "2022-09-30");
        return date;
    }

    public LocalDate StringToDate(String date) {
        return LocalDate.parse(date);
    }

    private Boolean checkActive(Booth booth) {
        LocalDate start = StringToDate(booth.getStartAt());
        LocalDate end = StringToDate(booth.getEndAt());
        LocalDate today = LocalDate.now();
        return start.isBefore(today) && end.isAfter(today) || start.isEqual(today) || end.isEqual(today);
    }

}
