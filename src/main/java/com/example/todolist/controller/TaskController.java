package com.example.todolist.controller;

import com.example.todolist.model.TaskModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

//Controller классыбызды белгилеп алабыз
@Controller
//RequestMapping жардамы менен HTTP запросубуздун маршрутун көрсөтөбүз
@RequestMapping("/tasks")
public class TaskController {
    // Тапшырмаларды берилиштер базасына сактап отурбай, тизме түзүп, ушул тизмеге алгач сактай турсак болот
    private static List<TaskModel> tasks = new ArrayList<>();
    private static Long idCounter = 1L;

    static {
        // Алгач бир канча тапшырма кошуп алалы
        tasks.add(new TaskModel(idCounter++, "А тапшырмасы"));
        tasks.add(new TaskModel(idCounter++, "Б тапшырмасы"));
    }

    // Тапшырмалар тизмесинин көрсөтүлүшү (HTML)
    @GetMapping
    public String showTasks(Model model) {
        model.addAttribute("tasks", tasks);
        return "task-list";
    }

    // Жаңы тапшырмалардын кошулушу
    @PostMapping
    public String addTask(@RequestParam String title) {
        TaskModel newTask = new TaskModel(idCounter++, title);
        tasks.add(newTask);
        return "redirect:/tasks";
    }
    // Тапшырмаларды өзгөртүү баракчасын көрсөтүү
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        TaskModel task = tasks.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElse(null);
        if (task == null) {
            return "redirect:/tasks"; // Тапшырмалар табылбаса тизмеге кайтаруу
        }
        model.addAttribute("task", task);
        return "task-edit"; // Ал эми табылса тапшырмаларды өзгөртүү шаблону ачылат
    }

    // Ташырмаларды өзгөртүү
    @PostMapping("/{id}/edit")
    public String updateTask(@PathVariable Long id, @RequestParam String title) {
        tasks.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst()
                .ifPresent(task -> task.setTitle(title));
        return "redirect:/tasks";
    }


    // Тапшырмаларды өчүрүү
    @PostMapping("/{id}/delete")
    public String deleteTask(@PathVariable Long id) {
        tasks.removeIf(task -> task.getId().equals(id));
        return "redirect:/tasks";
    }

    // Тапшырмаларды аткарылды деп белгилеп, же тескерисинче аткарылбады деп
    @PostMapping("/{id}/toggle-completion")
    public String toggleTaskCompletion(@PathVariable Long id) {
        tasks.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst()
                .ifPresent(task -> task.setCompleted(!task.isCompleted())); // Статусун алмаштырабыз
        return "redirect:/tasks";
    }

    //Бардык тапшырмаларды JSON форматында алуу
    @GetMapping("/json")
    @ResponseBody
    public List<TaskModel> getTasksAsJson() {
        return tasks;
    }

    // Белгилүү бир тапшырманы анын id си аркылуу JSON форматында алуу
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
//үөңӨҮ