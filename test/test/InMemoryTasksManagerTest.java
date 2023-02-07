package test;

import org.junit.jupiter.api.BeforeEach;
import service.InMemoryTasksManager;

public class InMemoryTasksManagerTest extends test.TaskManagerTest<InMemoryTasksManager> {

    public InMemoryTasksManagerTest() {
        super();
    }

    @BeforeEach
    public void createNewManager() {
        manager = new InMemoryTasksManager();
    }

}
