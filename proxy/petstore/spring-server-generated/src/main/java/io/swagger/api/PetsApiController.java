package io.swagger.api;

import io.swagger.model.Error;
import io.swagger.model.Pet;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-02-10T23:33:14.005Z[GMT]")
@RestController
public class PetsApiController implements PetsApi {

    private static final Logger log = LoggerFactory.getLogger(PetsApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public PetsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<Void> createPets(@Parameter(in = ParameterIn.DEFAULT, description = "Pet to add to the store", required=true, schema=@Schema()) @Valid @RequestBody Pet body) {
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    public ResponseEntity<Pet> deletePetById(@Parameter(in = ParameterIn.PATH, description = "The id of the pet to delete", required=true, schema=@Schema()) @PathVariable("petId") String petId,@Parameter(in = ParameterIn.HEADER, description = "The key header" ,required=true,schema=@Schema()) @RequestHeader(value="key", required=true) String key) {
        try {
            return new ResponseEntity<Pet>(objectMapper.readValue("{\"id\":1,\"name\":\"Jessica Right\",\"tag\":\"pet\"}", Pet.class), HttpStatus.OK);
        } catch (IOException e) {
            log.error("Couldn't serialize response for content type application/json", e);
            return new ResponseEntity<Pet>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<Pet>> listPets(@Parameter(in = ParameterIn.QUERY, description = "How many items to return at one time (max 100)" ,schema=@Schema()) @Valid @RequestParam(value = "limit", required = false) Integer limit) {
        try {
            return new ResponseEntity<List<Pet>>(objectMapper.readValue("[{\"id\":1,\"name\":\"catten\",\"tag\":\"cat\"},{\"id\":2,\"name\":\"doggy\",\"tag\":\"dog\"}]", List.class), HttpStatus.OK);
        } catch (IOException e) {
            log.error("Couldn't serialize response for content type application/json", e);
            return new ResponseEntity<List<Pet>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Pet> showPetById(@Parameter(in = ParameterIn.PATH, description = "The id of the pet to retrieve", required=true, schema=@Schema()) @PathVariable("petId") String petId) {
        try {
            return new ResponseEntity<Pet>(objectMapper.readValue("{\"id\":1,\"name\":\"Jessica Right\",\"tag\":\"pet\"}", Pet.class), HttpStatus.OK);
        } catch (IOException e) {
            log.error("Couldn't serialize response for content type application/json", e);
            return new ResponseEntity<Pet>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
