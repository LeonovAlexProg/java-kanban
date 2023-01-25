package managers;

import org.junit.jupiter.api.BeforeEach;
import ru.yandex.practicum.manager.InMemoryTaskManager;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager>{
    @BeforeEach
    public void initInMemoryManager() {
        super.initManager(new InMemoryTaskManager());
    }
}