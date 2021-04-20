package org.hl7.davinci.rules;


import ca.uhn.fhir.context.FhirContext;
import org.hl7.davinci.ruleutils.ModelResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The Da Vinci Prior Authorization Rules Engine RI microservice is launched
 * with this App.
 */
@SpringBootApplication
public class App {

  /**
   * HAPI FHIR Context. HAPI FHIR warns that the context creation is expensive,
   * and should be performed per-application, not per-record.
   */
  private static final FhirContext FHIR_CTX = FhirContext.forR4();

  /**
   * Create a singular model resolver to be used throughout the application
   */
  private static final ModelResolver MODEL_RESOLVER = new ModelResolver(FHIR_CTX);

  public static boolean debugMode = false;

  private static String baseUrl;

   /**
   * Local database for FHIR resources.
   */
  private static Database DB;

  /**
   * Launch the Rules Engine microservice.
   * 
   * @param args - ignored.
   */
  public static void main(String[] args) {
    if (args.length > 0) {
      if (args[0].equalsIgnoreCase("debug")) {
        debugMode = true;
      }
    }

    if (System.getenv("debug") != null) {
      if (System.getenv("debug").equalsIgnoreCase("true")) {
        debugMode = true;
      }
    }

     // Set the DB
     initializeAppDB();

    // Assemble the microservice
    SpringApplication server = new SpringApplication(App.class);
    server.run();
  }

  /**
   * Set the base URI for the microservice. This is necessary so
   * Bundle.entry.fullUrl data is accurately populated.
   * 
   * @param base - from FhirUtils.setServiceBaseUrl(HttpServletRequest)
   */
  public static void setBaseUrl(String base) {
    baseUrl = base;
  }

  /**
   * Get the base URL for the microservice
   * 
   * @return the the base url
   */
  public static String getBaseUrl() {
    return baseUrl;
  }

  /**
   * Get the FHIR Context for R4
   * 
   * @return the R4 fhir context
   */
  public static FhirContext getFhirContext() {
    return FHIR_CTX;
  }

  /**
   * Get the Model Resolver (for R4)
   * 
   * @return the FhirModelResolver
   */
  public static ModelResolver getModelResolver() {
    return MODEL_RESOLVER;
  }

  public static void initializeAppDB() {
    if (DB == null) {
      DB = new Database();
      PriorAuthRule.populateRulesTable();
     }
  }

  public static Database getDB() {
    return DB;
  }
}
