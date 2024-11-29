package likelion.festival.booth.domain.repository;


import likelion.festival.booth.domain.Booth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoothRepository extends JpaRepository<Booth, Long> {

    List<Booth> findByLocation(final String location);

    List<Booth> findByTitleContaining(final String title);

    // TODO : 메뉴검색 문 추가
    List<Booth> findByMenus_NameContaining(final String menu);
}
