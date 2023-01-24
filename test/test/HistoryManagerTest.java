package test;

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.HistoryManager;
import service.InMemoryHistoryManager;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HistoryManagerTest {
    Task task1;
    Epic epic1;
    Subtask subtask1;

    HistoryManager historyManager = new InMemoryHistoryManager();

    @BeforeEach
    public void beforeAll() {
        task1 = new Task("Task #1", "Description: Task #1", TaskStatus.NEW);
        task1.setId(1);
        epic1 = new Epic("Epic #1", "Description: Epic #1");
        epic1.setId(2);
        subtask1 = new Subtask("Subtask #1-e1", "Description: Subtask #1-e1", TaskStatus.NEW, 2);
        subtask1.setId(3);
    }

//   тесты для  void add(Task task);
    @Test
    public void shouldAddHistoryByEmptyHistory() {
        historyManager.add(task1);

        List<Task> history = historyManager.getHistory();
        assertNotNull(history);
        assertEquals(1, history.size());
    }

    @Test
    public void shouldAddHistoryByDuplication() {
        historyManager.add(task1);
        historyManager.add(epic1);
        historyManager.add(subtask1);
        historyManager.add(task1);

        List<Task> history = historyManager.getHistory();
        assertEquals(3, history.size());
    }

//   тесты для  void remove(int id);
    @Test
    public void shouldRemoveByMiddleOfList() {
        historyManager.add(task1);
        historyManager.add(epic1);
        historyManager.add(subtask1);
        historyManager.remove(epic1.getId());
        List<Task> history = historyManager.getHistory();

        List<Task> expectedHistory = new ArrayList<>();
        expectedHistory.add(task1);
        expectedHistory.add(subtask1);

        assertEquals(expectedHistory, history);
    }

    @Test
    public void shouldRemoveByStartOfList() {
        historyManager.add(task1);
        historyManager.add(epic1);
        historyManager.add(subtask1);
        historyManager.remove(task1.getId());
        List<Task> history = historyManager.getHistory();

        List<Task> expectedHistory = new ArrayList<>();
        expectedHistory.add(epic1);
        expectedHistory.add(subtask1);

        assertEquals(expectedHistory, history);
    }

    @Test
    public void shouldRemoveByEndOfList() {
        historyManager.add(task1);
        historyManager.add(epic1);
        historyManager.add(subtask1);
        historyManager.remove(subtask1.getId());
        List<Task> history = historyManager.getHistory();

        List<Task> expectedHistory = new ArrayList<>();
        expectedHistory.add(task1);
        expectedHistory.add(epic1);

        assertEquals(expectedHistory, history);
    }

//   тесты для  List<Task> getHistory();
    @Test
    public void shouldGetHistory() {
        historyManager.add(task1);
        historyManager.add(epic1);
        historyManager.add(subtask1);
        List<Task> history = historyManager.getHistory();

        List<Task> expectedHistory = new ArrayList<>();
        expectedHistory.add(task1);
        expectedHistory.add(epic1);
        expectedHistory.add(subtask1);

        assertEquals(expectedHistory, history);
    }
}
