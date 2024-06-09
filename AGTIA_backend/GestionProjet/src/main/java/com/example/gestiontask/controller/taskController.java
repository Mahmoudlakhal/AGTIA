package com.example.gestiontask.controller;

import com.example.gestiontask.models.Task;
import com.example.gestiontask.services.ImpServies.serviceTaskImpl;
import com.example.gestiontask.services.TaskIService;
import com.example.gestiontask.utils.StorgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/task")
@CrossOrigin(origins = "http://localhost:8081")

public class taskController {

    @Autowired
    public TaskIService iServiceTask;


    @Autowired
    private StorgeService storgeService;
    @Autowired
    private serviceTaskImpl taskService;

    @PostMapping("/addTask")
    public Task addTask(@RequestBody Task task)
    {
        return iServiceTask.addTask(task);
    }


    @PostMapping("/addTach/{id_user}")
    public Task addTach(@RequestBody Task task,@PathVariable Long id_user)
    {
        return iServiceTask.addTach(task,id_user);
    }


    @GetMapping("/allTasks")
    public List<Task> getAllTasks() {
        return iServiceTask.getALLTasks();
    }

    @GetMapping("/getTask/{id}")
    public Task getTaskByID(@PathVariable Long id) {
        return iServiceTask.getTaskByID(id);
    }

    @PutMapping("/updateTask/{id}")
    public Task updateTask(@RequestBody Task task, @PathVariable Long id) {
        return iServiceTask.UpdateTask(task, id);
    }

    @DeleteMapping("/deleteTask/{id}")
    public HashMap<String,String> deleteTask(@PathVariable Long id) {
        return iServiceTask.DeleteTask(id);
    }


    @PostMapping("/addTaskByProject/{projectId}")
    public Task addTaskByProject(@RequestBody Task task, @PathVariable Long projectId){
       // String original=storgeService.store(file);
      //  task.setPiecejoint(original);
        return taskService.addTaskByProject(task, projectId);
    }

    @GetMapping("/getTasksByProject/{projectId}")
    public List<Task> getTasksByProjectId(@PathVariable Long projectId) {
        return taskService.getTasksByProjectId(projectId);
    }
    @PutMapping("/updateEtat/{id}")
    public Task UpdateEtat(Task task ,@PathVariable Long id){
        return taskService.UpdateEtatTask(task,id);   }
}
