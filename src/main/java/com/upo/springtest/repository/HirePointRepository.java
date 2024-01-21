package com.upo.springtest.repository;

import com.upo.springtest.model.HirePoint;
import com.upo.springtest.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HirePointRepository extends JpaRepository<HirePoint, Long> {

}
