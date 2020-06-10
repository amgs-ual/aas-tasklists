package tasklists.views;

import java.io.IOException;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tasklists.controllers.TaskListController;
import tasklists.controllers.TaskListControllerClass;


@WebServlet("/lists/*")
public class RESTAPI extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String taskListId, taskId;
		TaskListController controller = new TaskListControllerClass();
		
        resp.setContentType("application/json");
        JsonObjectBuilder jsonObjectBuilder = null;
        
        String[] splits = req.getRequestURI().split("/");
        switch(splits.length) {
        case 3: // /lists
        	jsonObjectBuilder = controller.getTaskListsJsonObjectBuilder();
        	break;
        case 4: // /lists/<listId>
        	taskListId = splits[3];
        	if(!controller.hasTaskListById(taskListId)) {
        		jsonObjectBuilder = setError("There is no task list with id "+taskListId+".");
        	}
        	else {
        		jsonObjectBuilder = controller.getTaskListById(taskListId).getJsonObjectBuilder();
        	}
        	break;
        case 5: // /lists/<listId>/<taskId>
        	taskListId = splits[3];
        	taskId = splits[4];
        	if(!controller.hasTaskListById(taskListId)) {
        		jsonObjectBuilder = setError("There is no task list with id "+taskListId+".");
        	}
        	else if (!controller.hasTask(taskListId, taskId)) {
        		jsonObjectBuilder = setError("There is no task with id "+taskId+" in task list with id "+taskListId+".");
        	}
        	else {
        		jsonObjectBuilder = controller.getTaskListById(taskListId).getTask(taskId).getJsonObjectBuilder();
        	}
        	break;
        default:
        	jsonObjectBuilder = setError(resp, "Unsupported operation.");
        }
        
        JsonWriter jsonWriter = Json.createWriter(resp.getWriter());
        jsonWriter.writeObject(jsonObjectBuilder.build());
        jsonWriter.close();

        controller.close();
    }
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String taskListId, taskId, taskListName, taskDescription;
		TaskListController controller = new TaskListControllerClass();
		
        resp.setContentType("application/json");
        JsonObjectBuilder jsonObjectBuilder = null;
        
        JsonReader jsonReader = Json.createReader(req.getReader());
    	JsonObject jsonObject = jsonReader.readObject();
    	jsonReader.close();
        
        String[] splits = req.getRequestURI().split("/");
        switch(splits.length) {
        case 3: // /lists
        	taskListName = jsonObject.getString("name");
        	if(controller.hasTaskListByName(taskListName)) {
        		jsonObjectBuilder = setError("There is already a task list with name "+taskListName+".");
        	}
        	else {
        		jsonObjectBuilder = controller.createTaskList(taskListName).getJsonObjectBuilder();
        	}
        	break;
        case 4: // /lists/<listId>
        	taskListId = splits[3];
        	taskDescription = jsonObject.getString("description");
        	if(!controller.hasTaskListById(taskListId)) {
        		jsonObjectBuilder = setError("No such task list.");
        	}
        	else {
        		jsonObjectBuilder = controller.createTask(taskListId, taskDescription).getJsonObjectBuilder();
        	}
        	break;
        case 5: // /lists/<listId>/<taskId>
        	taskListId = splits[3];
        	taskId = splits[4];
        	if(!controller.hasTaskListById(taskListId)) {
        		jsonObjectBuilder = setError("There is no task list with id "+taskListId+".");
        	}
        	else if (!controller.hasTask(taskListId, taskId)) {
        		jsonObjectBuilder = setError("There is no task with id "+taskId+" in task list with id "+taskListId+".");
        	}
        	else {
        		jsonObjectBuilder = controller.changeTaskStatus(taskListId, taskId).getJsonObjectBuilder();
        	}
        	break;
        default:
        	jsonObjectBuilder = setError(resp, "Unsupported operation.");
        }
        
        JsonWriter jsonWriter = Json.createWriter(resp.getWriter());
        jsonWriter.writeObject(jsonObjectBuilder.build());
        jsonWriter.close();
        
        controller.close();
	}
	
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String taskListId, taskId, taskListName, taskDescription;
		TaskListController controller = new TaskListControllerClass();
		
        resp.setContentType("application/json");
        JsonObjectBuilder jsonObjectBuilder = null;
        
        JsonReader jsonReader = Json.createReader(req.getReader());
    	JsonObject jsonObject = jsonReader.readObject();
    	jsonReader.close();
        
        String[] splits = req.getRequestURI().split("/");
        switch(splits.length) {
        case 4: // /lists/<listId>
        	taskListId = splits[3];
        	taskListName = jsonObject.getString("name");
        	if(!controller.hasTaskListById(taskListId)) {
        		jsonObjectBuilder = setError("No such task list.");
        	}
        	else {
        		jsonObjectBuilder = controller.changeTaskListNameById(taskListId, taskListName).getJsonObjectBuilder();
        	}
        	break;
        case 5: // /lists/<listId>/<taskId>
        	taskListId = splits[3];
        	taskId = splits[4];
        	taskDescription = jsonObject.getString("description");
        	if(!controller.hasTaskListById(taskListId)) {
        		jsonObjectBuilder = setError("There is no task list with id "+taskListId+".");
        	}
        	else if (!controller.hasTask(taskListId, taskId)) {
        		jsonObjectBuilder = setError("There is no task with id "+taskId+" in task list with id "+taskListId+".");
        	}
        	else {
        		jsonObjectBuilder = controller.changeTaskDescription(taskListId, taskId, taskDescription).getJsonObjectBuilder();
        	}
        	break;
        default:
        	jsonObjectBuilder = setError(resp, "Unsupported operation.");
        }
        
        JsonWriter jsonWriter = Json.createWriter(resp.getWriter());
        jsonWriter.writeObject(jsonObjectBuilder.build());
        jsonWriter.close();
        
        controller.close();
	}
	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String taskListId, taskId;
		TaskListController controller = new TaskListControllerClass();
		
        resp.setContentType("application/json");
        JsonObjectBuilder jsonObjectBuilder = null;
        
        String[] splits = req.getRequestURI().split("/");
        switch(splits.length) {
        case 4: // /lists/<listId>
        	taskListId = splits[3];
        	if(!controller.hasTaskListById(taskListId)) {
        		jsonObjectBuilder = setError("No such task list.");
        	}
        	else {
        		controller.deleteTaskList(taskListId);
        		jsonObjectBuilder = setSuccess("Task list removed successfully.");
        	}
        	break;
        case 5: // /lists/<listId>/<taskId>
        	taskListId = splits[3];
        	taskId = splits[4];
        	if(!controller.hasTaskListById(taskListId)) {
        		jsonObjectBuilder = setError("There is no task list with id "+taskListId+".");
        	}
        	else if (!controller.hasTask(taskListId, taskId)) {
        		jsonObjectBuilder = setError("There is no task with id "+taskId+" in task list with id "+taskListId+".");
        	}
        	else {
        		controller.deleteTask(taskListId, taskId);
        		jsonObjectBuilder = setSuccess("Task removed successfully.");
        	}
        	break;
        default:
        	jsonObjectBuilder = setError(resp, "Unsupported operation.");
        }
        
        JsonWriter jsonWriter = Json.createWriter(resp.getWriter());
        jsonWriter.writeObject(jsonObjectBuilder.build());
        jsonWriter.close();
        
        controller.close();
	}
	
	private static JsonObjectBuilder setMessage(String key, String message) {
		JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
		jsonObjectBuilder.add(key, message);
		return jsonObjectBuilder;
	}
	
	private static JsonObjectBuilder setSuccess(String message) {
		return setMessage("message", message);
	}
	
	private static JsonObjectBuilder setError(String message) {
		return setMessage("error", message);
	}
	
	private static JsonObjectBuilder setError(HttpServletResponse resp, String message) {
		resp.setStatus(400);
		return setError(message);
	}
}

