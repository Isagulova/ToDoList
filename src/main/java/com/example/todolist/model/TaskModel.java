package com.example.todolist.model;

public class TaskModel {
    private Long id;
    private String title;
    private boolean completed;

    // Конструкторы
    public TaskModel() {}

    public TaskModel(Long id, String title) {
        this.id = id;
        this.title = title;
        this.completed = false;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
