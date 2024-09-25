package com.t1.rueda_front.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.t1.rueda_front.dto.RuedaRequest;
import com.t1.rueda_front.dto.RuedaResponse;
import com.t1.rueda_front.model.VehiculoModel;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("")
public class VehiculoController {
    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/buscar")
    public String inicio(Model model) {
        VehiculoModel vehiculoModel = new VehiculoModel("00", "", "", 0,0,"");
        model.addAttribute("vehiculoModel", vehiculoModel);
        return "buscar";
    }

    @PostMapping("/validar")
    public String validar(@RequestParam("placa") String placa, Model model) {
        if (placa == null || placa.trim().length() == 0){

            VehiculoModel vehiculoModel = new VehiculoModel("01", "Error: Debe ingresar una placa de vehiculo valida", "", 0, 0, "");
            model.addAttribute("vehiculoModel", vehiculoModel);
            return "buscar";
        }
        try {
            String endpoint = "http://localhost:8080/buscar";
            RuedaRequest requestDTO = new RuedaRequest(placa);
            RuedaResponse respondDTO = restTemplate.postForObject(endpoint, requestDTO, RuedaResponse.class);

            if(respondDTO.codigo().equals("00")){
            VehiculoModel vehiculoModel = new VehiculoModel("00", respondDTO.marca(), respondDTO.modelo(), respondDTO.nroAsientos(), respondDTO.precio(), respondDTO.color());
            model.addAttribute("vehiculoModel", vehiculoModel);
            return "vehiculo";
            } else {
                VehiculoModel vehiculoModel = new VehiculoModel("01", "Error: No se encontró un vehículo con la placa ingresada", "", 0, 0, "");
                model.addAttribute("vehiculoModel", vehiculoModel);
                return "buscar";
            }
            
        } catch (Exception e) {
            VehiculoModel vehiculoModel = new VehiculoModel("99", "Error: Ocurrio un problema al momento de buscar", "", 0, 0, "");
            model.addAttribute("vehiculoModel", vehiculoModel);
            return "buscar";
        }
    }
}
