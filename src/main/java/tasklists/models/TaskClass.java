package tasklists.models;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="tasks")
public class TaskClass implements Task {
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy="increment")
	private int id;
	private String description;
	private String status; // "doing", "done"
	
	public TaskClass(String description) {
		this.description = description;
		this.status = "doing";
	}
	
	public TaskClass(String taskId, String description) {
		this(description);
		this.id = Integer.parseInt(taskId);
	}

	public TaskClass() {
		super();
	}

	public int getId() {
		return id;
	}

	public String getDescription() {
		return this.description;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public void setDescription(String taskDescription) {
		this.description = taskDescription;
	}
	
	public void toggle() {
		this.status = this.status == "done" ? "doing" : "done";		
	}
	
	public JsonObjectBuilder getJsonObjectBuilder() {
		JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
		jsonObjectBuilder.add("id", this.getId());
		jsonObjectBuilder.add("status", this.getStatus());
		jsonObjectBuilder.add("description", this.getDescription());
		return jsonObjectBuilder;
	}
	

	public boolean equals(Object obj) {
		if(obj instanceof Task) {
			return this.getId() == ((Task)obj).getId();
		}
		return super.equals(obj);
	}	
}
