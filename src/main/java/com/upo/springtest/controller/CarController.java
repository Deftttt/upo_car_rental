package com.upo.springtest.controller;

import com.upo.springtest.dto.CarDto;
import com.upo.springtest.dto.CarModelDto;
import com.upo.springtest.enums.FuelType;
import com.upo.springtest.enums.TransmitionType;
import com.upo.springtest.model.Car;
import com.upo.springtest.model.CarModel;
import com.upo.springtest.service.CarService;
import com.upo.springtest.service.ImageService;
import com.upo.springtest.util.Constants;
import com.upo.springtest.util.PageUtils;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class CarController {

    private final CarService carsService;
    private final ImageService imageService;

    @Value("${MAPS_API_KEY}")
    private String mapsApiKey;

    @Autowired
    public CarController(CarService carsService, ImageService imageService) {
        this.carsService = carsService;
        this.imageService = imageService;
    }


    @GetMapping("/cars")
    public ModelAndView getCars(){
        ModelAndView mav = new ModelAndView("car/car-list");
        List<Car> cars = carsService.getCars();
        mav.addObject("cars", cars);
        return mav;
    }



    @GetMapping("/cars/add")
    public ModelAndView addCarForm(CarDto CarDto){
        ModelAndView mav = new ModelAndView("car/add-car");
        List<CarModel> carModels = carsService.getCarModels();
        mav.addObject("carDto", CarDto);
        mav.addObject("carModels", carModels);
        return mav;
    }


    @PostMapping("/cars/add")
    public String addCar(@Valid CarDto carDto, BindingResult result, Model model){
        if (result.hasErrors()) {
            List<CarModel> carModels = carsService.getCarModels();
            model.addAttribute("carModels", carModels);
            return "car/add-car";
        }
        Car car = carsService.addCar(carDto);
        return "redirect:/cars/" + car.getId();
    }


    @GetMapping("/cars/update")
    public ModelAndView editCarForm(@RequestParam long carId){
        ModelAndView mav = new ModelAndView("car/update-car");
        CarDto carDto = carsService.getSingleCarDto(carId);
        List<CarModel> carModels = carsService.getCarModels();
        mav.addObject("carModels", carModels);
        mav.addObject("carDto", carDto);
        mav.addObject("carId", carId);
        return mav;
    }

    @PostMapping("/cars/update")
    public String updateCar(@RequestParam long carId, @Valid CarDto carDto, BindingResult result, Model model){
        if (result.hasErrors()) {
            List<CarModel> carModels = carsService.getCarModels();
            model.addAttribute("carModels", carModels);
            model.addAttribute("carId", carId);
            return "car/update-car";
        }
        Car car = carsService.editCar(carDto, carId);
        return "redirect:/cars";
    }

    @PostMapping("/cars/delete")
    public String deleteCar(@RequestParam long carId){
        carsService.deleteCar(carId);
        return "redirect:/cars";
    }



    @GetMapping("/cars/map")
    public ModelAndView carsMap() {
        ModelAndView mav = new ModelAndView("car/cars-map");
        List<Car> cars = carsService.getCars();
        mav.addObject("cars", cars);
        mav.addObject("mapsApiKey", mapsApiKey);
        return mav;
    }


    @GetMapping("/cars/{id}")
    public ModelAndView getSingleCar(@PathVariable long id){
        ModelAndView mav = new ModelAndView("car/car2");
        Car car = carsService.getSingleCar(id);
        mav.addObject("car", car);
        mav.addObject("mapsApiKey", mapsApiKey);
        return mav;
    }



    @GetMapping("/models")
    public ModelAndView getCarModels(
            @RequestParam(defaultValue = "asc") String order,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size,
            @RequestParam Optional<TransmitionType> transmitionType,
            @RequestParam Optional<FuelType> fuelType,
            @RequestParam Optional<Boolean> nonActive

    ) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(Constants.DEFAULT_MODELS_PER_PAGE);
        ModelAndView mav = new ModelAndView("model/car-model-list");

        Page<CarModel> carModelPage = carsService.getCarModelsPaginated(order, transmitionType.orElse(null), fuelType.orElse(null), nonActive.orElse(false), PageRequest.of(currentPage - 1, pageSize));
        mav.addObject("order", order);
        mav.addObject("transmitionType", transmitionType.orElse(null));
        mav.addObject("fuelType", fuelType.orElse(null));
        mav.addObject("nonActive", nonActive.orElse(false));

        PageUtils.addPagesToModelAndView(mav, carModelPage, "carPage");
        return mav;
    }


    @GetMapping("/models/{id}")
    public String getSingleModel(@PathVariable long id, Model model){
        model.addAttribute("model", carsService.getSingleModel(id));
        return "model/car-model";
    }



    @GetMapping("/models/update")
    public ModelAndView editCarModelForm(@RequestParam long modelId){
        ModelAndView mav = new ModelAndView("model/update-model");
        CarModelDto carModelDto = carsService.getSingleModelDto(modelId);
        mav.addObject("carModelDto", carModelDto);
        mav.addObject("modelId", modelId);
        return mav;
    }

    @PostMapping("/models/update")
    public String updateCarModel(@RequestParam long modelId, @ModelAttribute CarModelDto carModelDto, BindingResult result){
        if (result.hasErrors()) {
            return "model/update-model";
        }
        CarModel carModel = carsService.editCarModel(carModelDto, modelId);
        return "redirect:/models/" + carModel.getId();
    }


    @GetMapping("/models/add")
        public ModelAndView addCarModelForm(CarModelDto carModelDto){
        ModelAndView mav = new ModelAndView("model/add-model");
        mav.addObject("carModelDto", carModelDto);
        return mav;
    }


    @PostMapping("/models/add")
    public String addCarModel(@Valid CarModelDto carModelDto, BindingResult result, @ModelAttribute("images") MultipartFile[] img) throws IOException {
        if (result.hasErrors()) {
            return "model/add-model";
        }
        CarModel carModel = carsService.addCarModel(carModelDto);
        imageService.uploadImages(img, carModel);
        return "redirect:/models/" + carModel.getId();
    }



    @PostMapping("/models/delete")
    public String deleteCarModel(@RequestParam long id){
        carsService.deleteCarModel(id);
        return "redirect:/models";
    }



}
