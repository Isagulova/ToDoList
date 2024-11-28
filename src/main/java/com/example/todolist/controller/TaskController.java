package com.example.todolist.controller;

import com.example.todolist.model.TaskModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private static List<TaskModel> tasks = new ArrayList<>();
    private static Long idCounter = 1L;

    static {
        // Добавим несколько начальных задач
        tasks.add(new TaskModel(idCounter++, "Первая задача"));
        tasks.add(new TaskModel(idCounter++, "Вторая задача"));
    }

    // Отображение списка задач (HTML)
    @GetMapping
    public String showTasks(Model model) {
        model.addAttribute("tasks", tasks);
        return "task-list";
    }

    // Добавление новой задачи
    @PostMapping
    public String addTask(@RequestParam String title) {
        TaskModel newTask = new TaskModel(idCounter++, title);
        tasks.add(newTask);
        return "redirect:/tasks";
    }
    // Показать форму редактирования задачи
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        TaskModel task = tasks.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElse(null);
        if (task == null) {
            return "redirect:/tasks"; // Если задача не найдена, перенаправить на список
        }
        model.addAttribute("task", task);
        return "task-edit"; // Возвращает шаблон для редактирования
    }

    // Обновить задачу
    @PostMapping("/{id}/edit")
    public String updateTask(@PathVariable Long id, @RequestParam String title) {
        tasks.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst()
                .ifPresent(task -> task.setTitle(title));
        return "redirect:/tasks";
    }


    // Удаление задачи
    @PostMapping("/{id}/delete")
    public String deleteTask(@PathVariable Long id) {
        tasks.removeIf(task -> task.getId().equals(id));
        return "redirect:/tasks";
    }

    // Переключить статус выполнения задачи
    @PostMapping("/{id}/toggle-completion")
    public String toggleTaskCompletion(@PathVariable Long id) {
        tasks.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst()
                .ifPresent(task -> task.setCompleted(!task.isCompleted())); // Инвертируем статус
        return "redirect:/tasks";
    }

    // Получение всех задач в формате JSON
    @GetMapping("/json")
    @ResponseBody
    public List<TaskModel> getTasksAsJson() {
        return tasks;
    }

    // Получение одной задачи по ID в формате JSON
    @GetMapping("/json/{id}")
    @ResponseBody
    public ResponseEntity<TaskModel> getTaskById(@PathVariable Long id) {
        return tasks.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}