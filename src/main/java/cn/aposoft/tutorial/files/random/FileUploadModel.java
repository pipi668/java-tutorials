package cn.aposoft.tutorial.files.random;

/**
 * 分片文件模型
 */
public class FileUploadModel {
    /**
     * 任务ID
     */
    private String taskId;
    /**
     * md5值
     */
    private String md5;
    /**
     * 当前分片数
     */
    private long chunk;
    /**
     * 当前分片文件size[字节]
     */
    private long size;
    /**
     * 分片总数
     */
    private long chunks;
    /**
     * 上传文件名称
     */
    private String name;
    /**
     * 二进制文件内容
     */
    private byte[] data;
    /**
     * 文件记录ID
     */
    private String fileRecordId;

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

    public long getChunk() {
        return chunk;
    }

    public void setChunk(long chunk) {
        this.chunk = chunk;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getChunks() {
        return chunks;
    }

    public void setChunks(long chunks) {
        this.chunks = chunks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getFileRecordId() {
        return fileRecordId;
    }

    public void setFileRecordId(String fileRecordId) {
        this.fileRecordId = fileRecordId;
    }
}

