package projet;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FileManager {
    public FileManager(){

    }

    public <T> List<T> getAllElements(String filePath, TypeReference<List<T>> typeReference) throws IOException{
        File file = new File(filePath);

        ObjectMapper mapper = ObjectMapperProvider.getMapper();
        return mapper.readValue(file, typeReference);
    }

    public void writeTravelsToFile(String filePath, List<TravelErrors> travels) throws IOException {
        try {
            File file = new File(filePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        ObjectMapper mapper = ObjectMapperProvider.getMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), travels);
    }
}
