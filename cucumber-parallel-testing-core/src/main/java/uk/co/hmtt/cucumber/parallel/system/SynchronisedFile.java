package uk.co.hmtt.cucumber.parallel.system;

import com.fasterxml.jackson.databind.ObjectMapper;
import uk.co.hmtt.cucumber.parallel.Constants;
import uk.co.hmtt.cucumber.parallel.exceptions.ParallelException;

import java.io.IOException;
import java.io.RandomAccessFile;

public class SynchronisedFile<T> {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private RandomAccessFile rw;

    public T read(Class<T> clz) {

        FileSystemHelper.createFolder(Constants.PARALLEL_WORKING_DIR);
        try {
            if (rw == null) {
                rw = new RandomAccessFile(Constants.PARALLEL_WORKING_DIR + clz.getName() + ".lock", "rw");
                rw.getChannel().lock();
            }

            return (rw.length() == 0) ?
                    createNewSynchronizedObject(clz) :
                    objectMapper.readValue(rw.readUTF(), clz);

        } catch (IOException | IllegalAccessException | InstantiationException e) {
            throw new ParallelException(e);
        }

    }

    private T createNewSynchronizedObject(Class<T> clz) throws IllegalAccessException, InstantiationException, IOException {
            final T response = clz.newInstance();
            rw.writeUTF(objectMapper.writeValueAsString(response));
            return response;
    }

    public void write(T obj) {
        try {
            rw.setLength(0);
            rw.writeUTF(objectMapper.writeValueAsString(obj));
            release();
        } catch (IOException e) {
            throw new ParallelException(e);
        }
    }

    public void release() {
        try {
            rw.getChannel().close();
            rw = null;
        } catch (IOException e) {
            throw new ParallelException(e);
        }
    }

}
