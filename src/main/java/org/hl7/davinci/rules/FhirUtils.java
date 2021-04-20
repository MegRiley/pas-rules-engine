package org.hl7.davinci.rules;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.*;

import java.util.Date;

/**
 *  Note this is a stripped down version of this file from the DaVinci Prior Auth RI which can be found here:
 * 
 * any  changes made here need to be reflected in the corresponding file there or the behavior will be undefined
 * 
 */
public class FhirUtils {



  // FHIR Extension URLS
  public static final String SECURITY_SYSTEM_URL = "http://terminology.hl7.org/CodeSystem/v3-ObservationValue";
  public static final String SECURITY_SUBSETTED = "SUBSETTED";

  /**
   * Enum for the ClaimResponse Disposition field Values are Granted, Denied,
   * Partial, Pending, Cancelled, and Unknown
   */
  public enum Disposition {
    GRANTED("Granted"), DENIED("Denied"), PARTIAL("Partial"), PENDING("Pending"), CANCELLED("Cancelled"),
    UNKNOWN("Unknown");

    private final String value;

    Disposition(String value) {
      this.value = value;
    }

    public String value() {
      return this.value;
    }
  }

  /**
   * Enum for the ClaimResponse.item reviewAction extensions used for X12 HCR01
   * Responde Code. Codes taken from X12 and CMS
   * http://www.x12.org/x12org/subcommittees/X12N/N0210_4010MultProcedures.pdf
   * https://www.cms.gov/Research-Statistics-Data-and-Systems/Computer-Data-and-Systems/ESMD/Downloads/esMD_X12_278_09_2016Companion_Guide.pdf
   */
  public enum ReviewAction {
    APPROVED("A1"), PARTIAL("A2"), DENIED("A3"), PENDED("A4"), CANCELLED("A6");

    private final String value;

    ReviewAction(String value) {
      this.value = value;
    }

    public CodeType valueCode() {
      return new CodeType(this.value);
    }

    public String value() {
      return this.value;
    }

    public static ReviewAction fromString(String value) {
      for (ReviewAction reviewAction : ReviewAction.values()) {
        if (reviewAction.value().equals(value))
          return reviewAction;
      }

      return null;
    }
  }



  /**
   * Convert the response disposition into a review action
   * 
   * @param disposition - the response disposition
   * @return corresponding ReviewAction for the Disposition
   */
  public static ReviewAction dispositionToReviewAction(Disposition disposition) {
    if (disposition == Disposition.DENIED)
      return ReviewAction.DENIED;
    else if (disposition == Disposition.GRANTED)
      return ReviewAction.APPROVED;
    else if (disposition == Disposition.PARTIAL)
      return ReviewAction.PARTIAL;
    else if (disposition == Disposition.PENDING)
      return ReviewAction.PENDED;
    else if (disposition == Disposition.CANCELLED)
      return ReviewAction.CANCELLED;
    else
      return null;
  }


  /**
   * Get the Claim from a PAS Claim Bundle. Claim is the first entry
   * 
   * @param bundle - PAS Claim Request Bundle
   * @return Claim resource for the request
   */
  public static Claim getClaimFromRequestBundle(Bundle bundle) {
    Resource resource = bundle.getEntry().get(0).getResource();
    if (resource.getResourceType() == ResourceType.Claim)
      return (Claim) resource;
    else
      return null;
  }



  /**
   * Return the id of a resource
   * 
   * @param resource - the resource to get the id from
   * @return the id of the resource
   */
  public static String getIdFromResource(IBaseResource resource) {
    if (resource.getIdElement().hasIdPart())
      return resource.getIdElement().getIdPart();
    return null;
  }

  /**
   * Get the system from the first coding
   * 
   * @param codeableConcept - the codeable concept to get the system from
   * @return the system of the first coding
   */
  public static String getSystem(CodeableConcept codeableConcept) {
    return codeableConcept.getCoding().get(0).getSystem();
  }

  /**
   * Get the code from the first coding
   * 
   * @param codeableConcept - the codeable concept to get the code from
   * @return the code of the first coding
   */
  public static String getCode(CodeableConcept codeableConcept) {
    return codeableConcept.getCoding().get(0).getCode();
  }



  /**
   * Convert a FHIR resource into JSON.
   * 
   * @param resource - the resource to convert to JSON.
   * @return String - the JSON.
   */
  public static String json(IBaseResource resource) {
    String json = App.getFhirContext().newJsonParser().setPrettyPrint(true).encodeResourceToString(resource);
    return json;
  }


  /**
   * Create a FHIR OperationOutcome.
   *
   * @param severity The severity of the result.
   * @param type     The issue type.
   * @param message  The message to return.
   * @return OperationOutcome - the FHIR resource.
   */
  public static OperationOutcome buildOutcome(OperationOutcome.IssueSeverity severity, OperationOutcome.IssueType type,
      String message) {
    OperationOutcome error = new OperationOutcome();
    OperationOutcome.OperationOutcomeIssueComponent issue = error.addIssue();
    issue.setSeverity(severity);
    issue.setCode(type);
    issue.setDiagnostics(message);
    return error;
  }

  /**
   * Get a random number between 1 and max.
   *
   * @param max The largest the random number could be.
   * @return int representing the random number.
   */
  public static int getRand(int max) {
    Date date = new Date();
    return (int) ((date.getTime() % max) + 1);
  }

  /**
   * Determines whether or not the PAS request is differential or complete.
   * Wrapper around securityIsSubsetted for readability.
   * 
   * @param bundle - the bundle resource.
   * @return true if the bundle is a differential request and false otherwise.
   */
  public static boolean isDifferential(Bundle bundle) {
    return FhirUtils.securityIsSubsetted(bundle);
  }

  /**
   * Internal function to determine whether the security tag is SUBSETTED or not
   *
   * @param bundle - the resource.
   * @return true if the security tag is SUBSETTED and false otherwise
   */
  private static boolean securityIsSubsetted(Bundle bundle) {
    // Using a loop since bundle.getMeta().getSecurity(SYSTEM, CODE) returns null
    for (Coding coding : bundle.getMeta().getSecurity()) {

      if (coding.getSystem().equals(SECURITY_SYSTEM_URL) && coding.getCode().equals(SECURITY_SUBSETTED)) {

        return true;
      }
    }
    return false;
  }
}
