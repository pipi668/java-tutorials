package cn.aposoft.tutorial.files.random;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.FileChannel;
import java.security.AccessController;
import java.security.PrivilegedAction;

public class FileSaveTool {

    public void save(String filePath, FileUploadModel model) throws IOException {
        // 确认已创建文件目录
        ensure(filePath);
        // 生成临时文件名
        String tmpFileName = model.getName() + ".tmp";
        // 保存随机chunk内容
        randomWriteFile(filePath, tmpFileName, model);
    }

    private void randomWriteFile(String filePath, String tmpFileName, FileUploadModel model) throws IOException {
        File file = new File(filePath, tmpFileName);

        long fileSize = 0;
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
             FileChannel channel = randomAccessFile.getChannel()) {
            long offset = model.getChunk() * model.getSize();
            byte[] fileData = model.getData();
            MappedByteBuffer buffer = null;
            try {
                buffer = channel.map(FileChannel.MapMode.READ_WRITE, offset, fileData.length);
                buffer.put(fileData);
            } finally {
                free(buffer);
            }
            fileSize = randomAccessFile.length();
        } finally {

        }
    }

    private void free(MappedByteBuffer buffer) {
        if (buffer != null) {
            buffer.force();
            clean(buffer);
        }
    }

    private synchronized static void clean(final MappedByteBuffer buffer) {
        AccessController.doPrivileged(new PrivilegedAction<Object>() {
            @Override
            public Object run() {
                try {
                    Method getCleanerMethod = buffer.getClass().getMethod("cleaner", new Class[0]);
                    getCleanerMethod.setAccessible(true);
                    sun.misc.Cleaner cleaner = (sun.misc.Cleaner) getCleanerMethod.invoke(buffer, new Object[0]);
                    if (cleaner != null) {
                        cleaner.clean();
                    }
                } catch (Exception e) {

                }
                return null;
            }
        });
    }

    /**
     * 确保文件目录已经创建
     *
     * @param filePath
     * @throws IOException
     */
    public synchronized void ensure(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        } else if (!file.isDirectory()) {
            throw new IOException(filePath + " is not a directory.");
        }
    }


}
