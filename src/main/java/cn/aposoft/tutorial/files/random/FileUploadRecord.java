package cn.aposoft.tutorial.files.random;

import java.util.Date;

public class FileUploadRecord {

    private Integer id;
    /**
     * 任务ID
     */
    private String taskId;
    /**
     * md5值
     */
    private String md5;
    /**
     * 当前分片文件 size [字节]
     */
    private long size;
    /**
     * 上传文件名称
     */
    private String fileName;
    /**
     * 文件所在目录
     */
    private String filePath;
    /**
     * 上传时间
     */
    private Date updateTime;
    /**
     * 文件大小
     */
    private long fileSize;
    /**
     * 文件类型
     */
    private String type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
