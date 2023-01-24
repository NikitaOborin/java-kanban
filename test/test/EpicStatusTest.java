package test;

import model.Epic;
import model.Subtask;
import model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TasksManager;

import static org.junit.jupiter.api.Assertions.*;

class EpicStatusTest {

    private static TasksManager inMemoryTaskManager;
    private static Epic epic1;
    private static int epicId1;

    @BeforeEach
    public void beforeEach() {
        inMemoryTaskManager = Managers.getDefault();
        epic1 = new Epic("Epic #1", "Description: Epic #1");
        epicId1 = inMemoryTaskManager.addNewEpic(epic1);
    }

    @Test
    public void shouldNewEpicStatusByEmptySubtasksList() {
        assertEquals(TaskStatus.NEW, inMemoryTaskManager.getEpic(epicId1).getStatus());
    }

    @Test
    public void shouldNewEpicStatusBySubtasksWithNewStatus() {
        Subtask subtask1 = new Subtask("Subtask #1-e1", "Description: Subtask #1-e1", TaskStatus.NEW, epicId1);
        Subtask subtask2 = new Subtask("Subtask #2-e1", "Description: Subtask #2-e1", TaskStatus.NEW, epicId1);
        int subtaskId1 = inMemoryTaskManager.addNewSubtask(subtask1);
        int subtaskId2 = inMemoryTaskManager.addNewSubtask(subtask2);
        assertEquals(TaskStatus.NEW, inMemoryTaskManager.getEpic(epicId1).getStatus());
    }

    @Test
    public void shouldDoneEpicStatusBySubtasksWithDoneStatus() {
        Subtask subtask1 = new Subtask("Subtask #1-e1", "Description: Subtask #1-e1", TaskStatus.DONE, epicId1);
        Subtask subtask2 = new Subtask("Subtask #2-e1", "Description: Subtask #2-e1", TaskStatus.DONE, epicId1);
        int subtaskId1 = inMemoryTaskManager.addNewSubtask(subtask1);
        int subtaskId2 = inMemoryTaskManager.addNewSubtask(subtask2);
        assertEquals(TaskStatus.DONE, inMemoryTaskManager.getEpic(epicId1).getStatus());
    }

    @Test
    public void shouldInProgressEpicStatusBySubtasksWithNewAndDoneStatus() {
        Subtask subtask1 = new Subtask("Subtask #1-e1", "Description: Subtask #1-e1", TaskStatus.NEW, epicId1);
        Subtask subtask2 = new Subtask("Subtask #2-e1", "Description: Subtask #2-e1", TaskStatus.DONE, epicId1);
        int subtaskId1 = inMemoryTaskManager.addNewSubtask(subtask1);
        int subtaskId2 = inMemoryTaskManager.addNewSubtask(subtask2);
        assertEquals(TaskStatus.IN_PROGRESS, inMemoryTaskManager.getEpic(epicId1).getStatus());
    }

    @Test
    public void shouldInProgressEpicStatusBySubtasksWithInProgressStatus() {
        Subtask subtask1 = new Subtask("Subtask #1-e1", "Description: Subtask #1-e1", TaskStatus.IN_PROGRESS, epicId1);
        Subtask subtask2 = new Subtask("Subtask #2-e1", "Description: Subtask #2-e1", TaskStatus.IN_PROGRESS, epicId1);
        int subtaskId1 = inMemoryTaskManager.addNewSubtask(subtask1);
        int subtaskId2 = inMemoryTaskManager.addNewSubtask(subtask2);
        assertEquals(TaskStatus.IN_PROGRESS, inMemoryTaskManager.getEpic(epicId1).getStatus());
    }
}