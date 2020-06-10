package tasklists.models;

import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="tasklists")
public class TaskListClass implements TaskList {
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy="increment")
	
	private int id;
	private String name;
	
	@ElementCollection
	@OneToMany(
		    orphanRemoval = true,
		    cascade = CascadeType.ALL,
		    targetEntity = TaskClass.class,
		    fetch = FetchType.EAGER)
	private List<Task> tasks;
	
	public TaskListClass() {
		super();
	}
	
	public TaskListClass(String name) {
		this.name = name;
		this.tasks = new ArrayList<Task>();
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return this.name;
	}

	public List<Task> getTasks() {
		return this.tasks;
	}
	
	public void setName(String name) {
		this.name = name;		
	}
	
	public boolean hasTask(String taskId) {
		return this.tasks.contains(new TaskClass(taskId, null));
	}
	
	public void removeTask(String taskId) {
		this.tasks.remove(new TaskClass(taskId, null));
	}
	
	public Task addTask(String description) {
		Task task = new TaskClass(description);
		this.tasks.add(task);
		return task;
	}
	
	public Task getTask(String taskId) {
		return this.tasks.get(this.tasks.indexOf(new TaskClass(taskId, null)));
	}
	
	public JsonObjectBuilder getJsonObjectBuilder() {
		JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
		jsonObjectBuilder.add("id", this.getId());
        jsonObjectBuilder.add("name", this.getName());
        
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for(Task task : this.getTasks()) {
        	jsonArrayBuilder.add(task.getJsonObjectBuilder());
        }
        jsonObjectBuilder.add("tasks", jsonArrayBuilder);
        return jsonObjectBuilder;
	}
}
