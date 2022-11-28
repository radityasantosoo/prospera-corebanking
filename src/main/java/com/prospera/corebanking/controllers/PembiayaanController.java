package com.prospera.corebanking.controllers;


import com.prospera.corebanking.dto.models.entities.Nasabah;
import com.prospera.corebanking.dto.models.entities.Officer;
import com.prospera.corebanking.dto.models.entities.Pembiayaan;
import com.prospera.corebanking.dto.models.entities.Tabungan;
import com.prospera.corebanking.dto.request.OfficerData;
import com.prospera.corebanking.dto.request.PembiayaanData;
import com.prospera.corebanking.dto.response.MessageDTO;
import com.prospera.corebanking.dto.response.ResponseData;
import com.prospera.corebanking.dto.response.ResponseDataTerraPembiayaan;
import com.prospera.corebanking.services.OfficerService;
import com.prospera.corebanking.services.PembiayaanService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/pembiayaan")
public class PembiayaanController {
    @Autowired
    private PembiayaanService pembiayaanService;

    @Autowired(required = true)
    private ModelMapper modelMapper;

    /////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////// CREATE PEMBIAYAAN ///////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////
    @PostMapping("/create")
    public ResponseEntity<?> create (@RequestBody @Valid PembiayaanData pembiayaanData, Errors errors){
        ResponseData<Pembiayaan> responseData = new ResponseData<>();
        if(errors.hasErrors()){
            for(ObjectError error : errors.getAllErrors()){
                responseData.getMessages().add(error.getDefaultMessage());
            }
            responseData.setStatus(false);
            responseData.setPayload(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }

        Pembiayaan pembiayaan = pembiayaanService.savePembiayaan(pembiayaanData);
        if(pembiayaan == null){
            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setMessage("Tambah Pembiayaan Gagal");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageDTO);
        }
        responseData.setStatus(true);
        responseData.setPayload(pembiayaan);
        return ResponseEntity.ok(responseData);
    }

    /////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////// GET ALL OFFICER //////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////
    @GetMapping("/all")
    public ResponseEntity<ResponseData<Iterable<Pembiayaan>>> findAll(){
        ResponseData<Iterable<Pembiayaan>> responseData = new ResponseData<>();
        Iterable<Pembiayaan> pembiayaan = pembiayaanService.findAll();
        responseData.setStatus(true);
        responseData.setPayload(pembiayaan);

        return ResponseEntity.ok(responseData);
    }


    /////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////// GET ONE PEMBIAYAAN BY ID /////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////
    @GetMapping("/{noPembiayaan}")
    public ResponseEntity<ResponseData<Pembiayaan>> findPembiayaanByID(@PathVariable("noPembiayaan") Long noPembiayaan){
        ResponseData<Pembiayaan> responseData = new ResponseData<>();
        Pembiayaan pembiayaan = pembiayaanService.findOne(noPembiayaan);
        responseData.setStatus(true);
        responseData.setPayload(pembiayaan);

        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/terra/{id}")
    public ResponseEntity<ResponseDataTerraPembiayaan<Pembiayaan>> findPembiayaanByIDTerra(@PathVariable("id") Long id){
        ResponseDataTerraPembiayaan<Pembiayaan> responseData = new ResponseDataTerraPembiayaan<>();
        Pembiayaan pembiayaan = pembiayaanService.findOne(id);
        responseData.setStatus(true);
        responseData.setPayload(pembiayaan);
        responseData.setPembiayaan(pembiayaan );


        return ResponseEntity.ok(responseData);
    }

    /////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////// UPDATE OFFICER ///////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////
    @PutMapping("/update")
    public ResponseEntity<ResponseData<Pembiayaan>> update (@RequestBody @Valid Pembiayaan pembiayaanData, Errors errors){

        System.out.println(pembiayaanData);
        ResponseData<Pembiayaan> responseData = new ResponseData<>();
        if (errors.hasErrors()){
            for(ObjectError error : errors.getAllErrors()){
                responseData.getMessages().add(error.getDefaultMessage());
            }
            responseData.setStatus(false);
            responseData.setPayload(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }
        Pembiayaan pembiayaan = modelMapper.map(pembiayaanData, Pembiayaan.class);
        responseData.setStatus(true);
        responseData.setPayload(pembiayaanService.update(pembiayaan));
        return ResponseEntity.ok(responseData);
    }

}
