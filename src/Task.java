public class Task {
    protected int id;
    protected String name;
    protected String description;
    protected String status;

    public Task(String name, String description) { // конструктор для эпиков, без возможности задать статус
        this.name = name;
        this.description = description;
    }

    public Task(int id, String name, String description) { // конструктор для эпиков, без возможности задать статус
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Task(String name, String description, String status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(int id, String name, String description, String status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
