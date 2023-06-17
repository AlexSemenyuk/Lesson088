package org.itstep.addressservice.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.itstep.addressservice.domain.Address;
import org.itstep.addressservice.repository.AddressRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RequestMapping("/api/v1/address/")
@RequiredArgsConstructor
@RestController
@Slf4j
@CrossOrigin(origins = "${ui.host}", methods = {
        RequestMethod.GET,
        RequestMethod.DELETE,
        RequestMethod.PUT,
        RequestMethod.POST
})
public class AddressController {
    private final AddressRepository addressRepository;

    @GetMapping
    public List<Address> findAll() {
        return addressRepository.findAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<Address> findAll(@PathVariable Integer id) {
        return ResponseEntity.of(addressRepository.findById(id));
    }

    @PostMapping(consumes = {
            MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> save(@RequestBody Address address) {
        try {
            var savedAddress = addressRepository.save(address);
            return ResponseEntity
                    .created(URI.create("/api/v1/address/%s".formatted(savedAddress.getId())))
                    .build();
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            throw exception;
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> update(@PathVariable Integer id,
                                    @RequestBody Address address) {
        Optional<Address> optionalAddress = addressRepository.findById(id);
        if (optionalAddress.isPresent()) {
            Address addr = optionalAddress.get();
            addr.setCountry(address.getCountry());
            addr.setCity(address.getCity());
            addr.setAddressLine1(address.getAddressLine1());
            addr.setAddressLine2(address.getAddressLine2());
            addressRepository.save(addr);
            return new ResponseEntity<>(addr, HttpStatus.OK);
        }
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setDetail("Address by id %s not found".formatted(id));
        problemDetail.setTitle("Error update address");
        return ResponseEntity.badRequest()
                .body(problemDetail);
    }


}

