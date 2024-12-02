package likelion.festival.menu.domain.repository;

import likelion.festival.booth.domain.Booth;
import likelion.festival.menu.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByBooth(Booth booth);
}
