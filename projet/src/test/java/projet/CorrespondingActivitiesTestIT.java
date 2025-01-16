package projet;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class CorrespondingActivitiesTestIT {
    @Test
    public void testGetAllActivities(){
        FileManager fileManager = new FileManager();
        CorrespondingActivities correspondingActivities= new CorrespondingActivities("src/data/activities_database_test.json", fileManager, new CoordinatesManager());
        
        correspondingActivities.getAllActivity();

        List<Activity> allActivities = correspondingActivities.getActivities();
        
        assertEquals(3, allActivities.size());
    }
    
    @Test
    public void testGetAllActivitiesEmpty(){
        FileManager fileManager = new FileManager();
        CorrespondingActivities correspondingActivities = new CorrespondingActivities("src/data/empty_file.json", fileManager, new CoordinatesManager());
        
        correspondingActivities.getAllActivity();

        List<Activity> allActivities = correspondingActivities.getActivities();
        
        assertEquals(0, allActivities.size());
    }

    @Test
    public void testGetAllActivitiesBadPath(){
        FileManager fileManager = new FileManager();
        CorrespondingActivities correspondingActivities = new CorrespondingActivities("src/data/transports_database_test.json", fileManager, new CoordinatesManager());
        
        correspondingActivities.getAllActivity();

        List<Activity> allActivities = correspondingActivities.getActivities();
        
        assertEquals(0, allActivities.size());
    }
}

