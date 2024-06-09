package com.example.gestiontask.services.ImpServies;

import com.example.gestiontask.client_config.UserProjet_RestClient;
import com.example.gestiontask.models.Projet;
import com.example.gestiontask.models.Task;
import com.example.gestiontask.models.User;
import com.example.gestiontask.repository.TaskRepository;
import com.example.gestiontask.services.TaskIService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class serviceTaskImpl implements TaskIService {

    @Autowired
    public TaskRepository taskRepository;

    @Autowired
    UserProjet_RestClient userProjet_restClient;


    public serviceTaskImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task addTach(Task task,Long id) {

        User user=userProjet_restClient.findUserById(id);
        if(user !=null){
            task.setId_user(user.getId());
            task.setUser(user);
            userProjet_restClient.updateRole(user.getId());
            return taskRepository.save(task);
        }
        else{
            throw new RuntimeException("ERREUR");
        }
    }

    @Override
    public Task addTask(Task task) {
            return taskRepository.save(task);
    }

    @Override
    public List<Task> getALLTasks() {
        System.out.println("Hello, world!");
        return taskRepository.findAll();
    }

    @Override
    public Task getTaskByID(Long id) {
         return taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task Not Found"));
    }

    @Override
    public Task UpdateTask(Task task, Long id) {
        Task taskup = this.getTaskByID(id);
        if (taskup != null ) {
            task.setId(id);
            task.setTitle(task.getTitle() == null? taskup.getTitle() : task.getTitle());
            task.setTaskType(task.getTaskType() == null? taskup.getTaskType() : task.getTaskType());
            task.setDecription(task.getDecription() == null? taskup.getDecription() : task.getDecription());
            task.setPiecejoint(task.getPiecejoint() == null? taskup.getPiecejoint() : task.getPiecejoint());
            task.setTimepoinage(task.getTimepoinage() == null? taskup.getTimepoinage() : task.getTimepoinage());
            task.setComplexity(task.getComplexity() == null ? taskup.getComplexity() : task.getComplexity());
            task.setEtat(task.getEtat() == null ? taskup.getEtat() : task.getEtat());
            task.setPriority(task.getPriority() == null ? taskup.getPriority() : task.getPriority());
            task.setMessionStrat(task.getMessionStrat() == null ? taskup.getMessionStrat() : task.getMessionStrat());
            task.setMessionEnd(task.getMessionEnd() == null ? taskup.getMessionEnd() : task.getMessionEnd());
            task.setTimeTask(task.getTimeTask() == null ? taskup.getTimeTask() : task.getTimeTask());
            task.setProjet(task.getProjet() == null ? taskup.getProjet() : task.getProjet());
            task.setTitle2(task.getTitle2() == null ? taskup.getTitle2() : task.getTitle2());
            task.setTitle3(task.getTitle3() == null ? taskup.getTitle3() : task.getTitle3());


            return taskRepository.save(task);
        }

        else{
            throw new RuntimeException("error ");
        }
    }

    public Task UpdateEtatTask(Task task, Long id) {
        Task taskup = this.getTaskByID(id);
        if (taskup != null ) {
            task.setId(id);
            task.setEtat(task.getEtat() == null ? taskup.getEtat() : task.getEtat());
            return taskRepository.save(task);
        }
        else{
            throw new RuntimeException("error ");
        }
    }

    @Override
    public HashMap<String,String> DeleteTask(Long id) {
        Task taskup = this.getTaskByID(id);
        HashMap message = new HashMap<>();
        if (taskup != null ) {
            try {
                taskRepository.deleteById(id);
                message.put("Etat","Task supprimé");
                return message;
            }
            catch (Exception e){
                message.put("Etat",""+e.getMessage());
                return message;
            }

        }
        else {
            message.put("Etat","Not Found");
            return message;
            //throw new RuntimeException(" Not Found");
        }
    }


    public Task addTaskByProject(Task task, Long projectId) {
        Projet projet = new Projet();
        projet.setId(projectId);
        task.setProjet(projet);
        return taskRepository.save(task);
    }

    public List<Task> getTasksByProjectId(Long projectId) {
        return taskRepository.findByProjetId(projectId);
    }
}
