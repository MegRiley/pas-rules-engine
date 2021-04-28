package org.hl7.davinci.rules;

import org.hl7.fhir.r4.model.Bundle;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

/**
 * The Subscription endpoint to create new subscriptions or delete outdated
 * ones.
 */
@CrossOrigin
@RestController
@RequestMapping("/Rules")
public class RulesEndpoint {
    // handle the request to process the rules here calls from
    // claimResponseFactory/ProcessCLaimItemTask will
    // be replaced with posts that are then handled here
    @RequestMapping(method = { RequestMethod.GET })
    public ResponseEntity<String> requestDisposition() {
        //return new ResponseEntity<String>("POST Response", HttpStatus.OK);
        return new ResponseEntity<String>(org.springframework.http.HttpStatus.CREATED);

    }

    @RequestMapping(method = { RequestMethod.POST })
    public ResponseEntity<FhirUtils.Disposition> returnDispostion(Bundle bundle, int seq) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        FhirUtils.Disposition dis = PriorAuthRule.computeDisposition(bundle, seq);
        ResponseEntity<FhirUtils.Disposition> response = new ResponseEntity<>(dis, status);
        return response;
    }

}
