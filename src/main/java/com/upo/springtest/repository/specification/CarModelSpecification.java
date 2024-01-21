package com.upo.springtest.repository.specification;

import com.upo.springtest.enums.FuelType;
import com.upo.springtest.enums.TransmitionType;
import com.upo.springtest.model.CarModel;
import org.springframework.data.jpa.domain.Specification;

public interface CarModelSpecification extends Specification<CarModel> {

    static Specification<CarModel> hasTransmitionType(TransmitionType transmitionType) {
        return (root, query, criteriaBuilder) ->
                transmitionType == null ? criteriaBuilder.conjunction() :
                        criteriaBuilder.equal(root.get("transmitionType"), transmitionType);
    }

    static Specification<CarModel> hasFuelType(FuelType fuelType) {
        return (root, query, criteriaBuilder) ->
                fuelType == null ? criteriaBuilder.conjunction() :
                        criteriaBuilder.equal(root.get("fuelType"), fuelType);
    }

    static Specification<CarModel> isNonActive(Boolean nonActive) {
        return (root, query, criteriaBuilder) ->
                nonActive == null ? criteriaBuilder.conjunction() :
                    criteriaBuilder.equal(root.get("active"), !nonActive);
    }
}
