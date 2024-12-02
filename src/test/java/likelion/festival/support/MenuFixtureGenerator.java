package likelion.festival.support;

import likelion.festival.booth.domain.Booth;
import likelion.festival.menu.domain.Menu;
import likelion.festival.menu.domain.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MenuFixtureGenerator {

    @Autowired
    private MenuRepository menuRepository;

    public List<Menu> generateDatas(Booth booth) {
        List<Menu> menus = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Menu menu = Menu.forSave("메뉴" + i, 1000, booth);
            menuRepository.save(menu);
            menus.add(menu);
        }

        return menus;
    }

    public Menu generateSingleData(Booth booth) {
        Menu menu = Menu.forSave("메뉴", 1000, booth);
        return menuRepository.save(menu);
    }
}
