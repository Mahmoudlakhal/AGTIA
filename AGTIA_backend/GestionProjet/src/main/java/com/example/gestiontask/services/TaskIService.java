package com.example.gestiontask.services;

import com.example.gestiontask.models.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface TaskIService {
    Task addTask(Task task);
    Task addTach(Task task,Long id);
    List<Task> getALLTasks();
    Task getTaskByID(Long id);
    Task UpdateTask(Task task,Long id);
    HashMap<String,String> DeleteTask(Long id);

}
