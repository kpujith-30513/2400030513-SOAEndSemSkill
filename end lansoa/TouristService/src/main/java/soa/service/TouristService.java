package soa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import soa.entity.Tourist;
import soa.repository.TouristRepository;

@Service
public class TouristService {

    @Autowired
    private TouristRepository touristRepository;

    /**
     * Retrieves all tourist records from the database.
     *
     * @return List of all Tourist objects
     */
    public List<Tourist> getAllTourists() {
        return touristRepository.findAll();
    }
}
