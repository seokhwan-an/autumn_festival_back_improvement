package likelion.festival.support;

import likelion.festival.booth.domain.Booth;
import likelion.festival.booth.domain.repository.BoothRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class BoothFixtureGenerator {

    @Autowired
    private BoothRepository boothRepository;


    public void generateDatas() {
        LocalDate localDate = LocalDate.now();
        for (int i = 0; i < 10; i++) {
            LocalDate next = localDate.plusDays(i);
            Booth booth = Booth.forSave("주점" + i, "주점 소개", "주점 공지사항", "주점", "주점 소개", "주점 위치" + i, i, next.toString(), next.toString());
            boothRepository.save(booth);
        }
    }

    public Booth generateSingleData() {
        LocalDate now = LocalDate.now();
        Booth booth = Booth.forSave("주점", "주점 소개", "주점 공지사항", "주점", "주점 소개", "주점 위치", 1, now.toString(), now.toString());
        return boothRepository.save(booth);
    }
}
