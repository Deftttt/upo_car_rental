package com.upo.springtest.service;

import com.upo.springtest.model.Car;
import com.upo.springtest.model.HirePoint;
import com.upo.springtest.repository.HirePointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HirePointService {
    private final HirePointRepository hirePointRepository;

    @Autowired
    public HirePointService(HirePointRepository hirePointRepository) {
        this.hirePointRepository = hirePointRepository;
    }

    public List<HirePoint> getHirePoints(){
        return hirePointRepository.findAll();
    }
}
