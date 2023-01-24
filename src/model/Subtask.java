package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {
    protected int epicId;

    public Subtask(String name, String description, TaskStatus taskStatus, int epicId) {
        super(name, description, taskStatus);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, TaskStatus taskStatus) {
        super(name, description, taskStatus);
    }

    public Subtask(String name, String description, TaskStatus taskStatus, Duration duration,
                   LocalDateTime startTime, int epicId) {
        super(name, description, taskStatus, duration, startTime);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return epicId == subtask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s", id, TaskType.SUBTASK, name, taskStatus, description, epicId);
    }
}
