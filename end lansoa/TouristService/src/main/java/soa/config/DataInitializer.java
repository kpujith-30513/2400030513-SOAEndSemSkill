package soa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import soa.entity.Tourist;
import soa.repository.TouristRepository;

/**
 * DataInitializer runs once on application startup.
 * It inserts sample tourist records into the database if the table is empty.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private TouristRepository touristRepository;

    @Override
    public void run(String... args) throws Exception {

        // Only insert data if the table is empty (prevents duplicate inserts on restart)
        if (touristRepository.count() == 0) {

            touristRepository.save(new Tourist("Alice Johnson",    "United States"));
            touristRepository.save(new Tourist("Marco Rossi",      "Italy"));
            touristRepository.save(new Tourist("Yuki Tanaka",      "Japan"));
            touristRepository.save(new Tourist("Priya Sharma",     "India"));
            touristRepository.save(new Tourist("Carlos Mendoza",   "Mexico"));
            touristRepository.save(new Tourist("Emma Müller",      "Germany"));
            touristRepository.save(new Tourist("Liam O'Brien",     "Ireland"));
            touristRepository.save(new Tourist("Fatima Al-Rashid", "UAE"));
            touristRepository.save(new Tourist("Chen Wei",         "China"));
            touristRepository.save(new Tourist("Sofia Andersen",   "Denmark"));

            System.out.println("✅  Sample tourist records inserted successfully.");
        } else {
            System.out.println("ℹ️  Tourist records already exist. Skipping data initialization.");
        }
    }
}
