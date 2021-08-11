package study.datajpa.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class ItemRepositoryTest {

    @Autowired private ItemRepository itemRepository;

    @Test
    void save() {
        Item item = new Item();
        itemRepository.save(item);
    }
}