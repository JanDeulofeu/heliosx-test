### Model

- Consultation have a list of Questions which holds the information for a questioner, this has 2 boolean fields 
  - `expectedAnswer` correct answer to hava a valid question 
  - `questionerAnswer` user answer to validate against `expectedAnswer`

- Consultation has a unique id, which refers to a single consultation from the user. 
- Consultation is persisted in a concurrent map to ensure multiple requests for a given id
- When a new Consultation is created a standard questioner is generated (List of Questions) 


### Rest Interface

- `Get:/consultation/{id}` 
  - Crates a new Consultation request for the user. The request should provide a UUID to generate a new Consultation in the backend.
  - If the id already exists Consultation already stored in the system is returned, if is a new one a new Consultation is generated

- `Get:/validation/consultation/{consultationId}/question/{questionId}/answer/{answerQuestion}`
    - Validate if a single Question inside a Consultation is valid. This is used for the UI to validate on the fly if the answer provided by the user is the expected one, for further modification
    - This method helps the user to validate each single answer to make any modifications before submitting the final Consultation

- `Put:/validation`
  - Validates an existing Consultation persisting it in the system, for tracability
  - Returns the Consultation verification as `valid` or `not valid`
  

### Swagger UI
-  Url to check OpenAPI definitions: http://localhost:8080/api/admin/swagger-ui/index.html