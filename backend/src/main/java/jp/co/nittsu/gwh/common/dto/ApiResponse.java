package jp.co.nittsu.gwh.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.List;

/**
 * Standard API response envelope.
 * All API endpoints return this structure for consistency.
 *
 * @param <T> Type of the data payload
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private String status;       // SUCCESS, WARNING, ERROR
    private T data;
    private Long totalRecords;
    private Integer page;
    private Integer size;
    private List<ApiMessage> messages;

    public ApiResponse() {
        this.messages = new ArrayList<>();
    }

    // --- Factory Methods ---

    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setStatus("SUCCESS");
        response.setData(data);
        return response;
    }

    public static <T> ApiResponse<T> success(T data, long totalRecords, int page, int size) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setStatus("SUCCESS");
        response.setData(data);
        response.setTotalRecords(totalRecords);
        response.setPage(page);
        response.setSize(size);
        return response;
    }

    public static <T> ApiResponse<T> warning(String messageCode, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setStatus("WARNING");
        response.addMessage(messageCode, message);
        return response;
    }

    public static <T> ApiResponse<T> error(String messageCode, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setStatus("ERROR");
        response.addMessage(messageCode, message);
        return response;
    }

    public void addMessage(String code, String message) {
        if (this.messages == null) {
            this.messages = new ArrayList<>();
        }
        this.messages.add(new ApiMessage(code, message));
    }

    // --- Getters & Setters ---

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
    public Long getTotalRecords() { return totalRecords; }
    public void setTotalRecords(Long totalRecords) { this.totalRecords = totalRecords; }
    public Integer getPage() { return page; }
    public void setPage(Integer page) { this.page = page; }
    public Integer getSize() { return size; }
    public void setSize(Integer size) { this.size = size; }
    public List<ApiMessage> getMessages() { return messages; }
    public void setMessages(List<ApiMessage> messages) { this.messages = messages; }

    /**
     * Individual message with error code (maps to existing ERR codes).
     */
    public static class ApiMessage {
        private String code;
        private String message;

        public ApiMessage() {}
        public ApiMessage(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}
