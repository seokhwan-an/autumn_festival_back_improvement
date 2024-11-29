package likelion.festival.booth.application;

import likelion.festival.booth.application.dto.BoothCreate;
import likelion.festival.booth.application.dto.BoothDayLocationDto;
import likelion.festival.booth.application.dto.BoothDto;
import likelion.festival.booth.application.dto.BoothFilterDto;
import likelion.festival.booth.application.dto.BoothUpdate;
import likelion.festival.booth.domain.Booth;
import likelion.festival.booth.domain.repository.BoothRepository;
import likelion.festival.global.exception.WrongBoothId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BoothService {

    private final BoothRepository boothRepository;

    public List<BoothFilterDto> boothFilterAndSearch(final String search) {
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
        final List<Booth> booths = boothRepository.findAll();
        return booths.stream()
            .map(b -> BoothFilterDto.of(b, checkActive(b)))
            .filter(BoothFilterDto::isActive)
            .sorted(Comparator.comparing(BoothFilterDto::getLikeCnt).reversed())
            .limit(5)
            .collect(Collectors.toList());
    }

    //날짜와 장소로 필터링 하는 기능 ok
    public List<BoothDayLocationDto> boothDayLocation(final String day, final String location) {
        final LocalDate today = LocalDate.parse(day);
        final List<Booth> booths = boothRepository.findByLocation(location);
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
    public Long create(final BoothCreate request) {
        final Booth booth = Booth.forSave(
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
    public BoothDto read(final Long id) {
        final Booth booth = boothRepository.findById(id)
            .orElseThrow(WrongBoothId::new);
        return BoothDto.of(booth);
    }

    //수정 ok
    @Transactional
    public BoothDto update(final Long id, final BoothUpdate request) {
        final Booth booth = boothRepository.findById(id)
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
    public void delete(final Long id) {
        final Booth booth = boothRepository.findById(id)
            .orElseThrow(WrongBoothId::new);
        boothRepository.delete(booth);
    }

    private LocalDate StringToDate(final String date) {
        return LocalDate.parse(date);
    }

    private Boolean checkActive(final Booth booth) {
        final LocalDate start = StringToDate(booth.getStartAt());
        final LocalDate end = StringToDate(booth.getEndAt());
        final LocalDate today = LocalDate.now();
        return start.isBefore(today) && end.isAfter(today) || start.isEqual(today) || end.isEqual(today);
    }

}
